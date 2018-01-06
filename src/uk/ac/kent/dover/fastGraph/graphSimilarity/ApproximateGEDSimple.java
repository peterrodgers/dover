package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;

/**
 * A simple approximation method for GED, make a node match, add in nodes,
 * delete unnecessary edges, add in necessary edges.
 * 
 * @author Peter Rodgers
 *
 */
public class ApproximateGEDSimple extends GraphEditDistance {

	private boolean nodeLabels;
	private boolean directed;
	
	private long nodeSwapTimeLimit;
	private int nodeSwapAttempts;
	private long randomSeed;
	
	private int nodeSwaps = 0;
	private long approximationTime = 0;
	
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;

	private HashMap<Integer,Integer> nodeMapping; // g1 nodes to g2 nodes
	private HashMap<Integer,Integer> reverseNodeMapping; // g2 nodes to g1 nodes
	private ArrayList<Integer> deleteNodes; // g1 nodes to be removed
	private ArrayList<Integer> deleteEdges; // g1 nodes to be removed
	private LinkedList<Integer> addEdgeNode1List; // g1 new node1 to be added
	private LinkedList<Integer> addEdgeNode2List; // g1 new node2 to be added
	
	private ReverseIntegerComparator reverseComparator = new ReverseIntegerComparator();
	HashMap<Integer,Double> editCosts;
	
	private Random random;

	private int addCapacity;
	private int addStart;
	
	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {
			
//			testByRandomGraphLoop();

			int startNodes = 100;
			int nodes = startNodes;
			while(true) {
				double ret;
				List<EditOperation> retList;
				FastGraph g1,g2;
				HashMap<Integer,Double> editCosts;
				EditList retEditList1, retEditList2;
				ApproximateGEDSimple ged;
				
				editCosts = new HashMap<>();
				editCosts.put(EditOperation.DELETE_NODE,1.0);
				editCosts.put(EditOperation.ADD_NODE,2.0);
				editCosts.put(EditOperation.DELETE_EDGE,3.0);
				editCosts.put(EditOperation.ADD_EDGE,4.0);
				editCosts.put(EditOperation.RELABEL_NODE,5.0);
				
				int edges = nodes*10;
				
				g1 = FastGraph.randomGraphFactory(nodes, edges, 7777, false);
				
				g2 = FastGraph.randomGraphFactory(nodes, edges, 5555, false);
				
				long start1 = System.currentTimeMillis();
				
				ged = new ApproximateGEDSimple(false,false,editCosts,0,0,111);
				
				ret = ged.similarity(g1, g2);
				
				System.out.println("NO OPTIMIZATION nodes "+nodes+" edges "+edges);
				System.out.println("similarity time "+(System.currentTimeMillis()-start1)/1000.0);
				System.out.println("cost "+ret+" length "+ged.getEditList().getEditList().size());
				
				retEditList1 = ged.getEditList();
				retList = retEditList1.getEditList();
				long start2 = System.currentTimeMillis();
				ged = new ApproximateGEDSimple(false,false,editCosts,1000,0,7777);
				ret = ged.similarity(g1, g2);
				
				System.out.println("OPTIMIZATION nodes "+nodes+" edges "+edges);
				System.out.println("similarity time "+(System.currentTimeMillis()-start2)/1000.0);
				
				retEditList2 = ged.getEditList();
				retList = retEditList2.getEditList();
				
				System.out.println("node swaps "+ged.getNodeSwaps());
				System.out.println("cost "+retEditList2.getCost()+" length "+retList.size());
				
				nodes = nodes + startNodes;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * defaults to treating graph as undirected and no node label comparison,
	 * with all edit costs at 1.0.
	 * 
	 * @throws FastGraphException should not throw this as all required edit operations are present by default
	 */
	public ApproximateGEDSimple() throws FastGraphException {
		super();
		HashMap<Integer,Double> defaultEditCosts = new HashMap<>();
		defaultEditCosts.put(EditOperation.DELETE_NODE,1.0);
		defaultEditCosts.put(EditOperation.ADD_NODE,1.0);
		defaultEditCosts.put(EditOperation.DELETE_EDGE,1.0);
		defaultEditCosts.put(EditOperation.ADD_EDGE,1.0);
		this.editCosts = defaultEditCosts;

		this.nodeLabels = false;
		this.nodeSwapTimeLimit = 0;
		this.nodeSwapAttempts = 0;
		this.randomSeed = System.currentTimeMillis();
		
		init();
	}

	/**
	 * defaults to treating graph as undirected and no node label comparison.
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * 
	 * @param editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ApproximateGEDSimple(HashMap<Integer,Double> editCosts) throws FastGraphException {
		
		super();
		this.editCosts = editCosts;
		this.directed = false;
		this.nodeLabels = false;
		this.nodeSwapTimeLimit = 0;
		this.nodeSwapAttempts = 0;
		this.randomSeed = System.currentTimeMillis();
		
		init();
	}

	/**
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * Defaults to no attempted node map swaps
	 * 
	 * @param directed true if the graph is treated as directed, false if undirected
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param editCosts are a mapping between edit codes and their costs DELETE_NODE, ADD_NODE, DELETE_EDGE, ADD_EDGE, if @see{nodeLabels} is true, RELABEL_NODE also needs to be present
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ApproximateGEDSimple(boolean directed, boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {

		super();
		this.editCosts = editCosts;
		this.directed = directed;
		this.nodeLabels = nodeLabels;
		this.nodeSwapTimeLimit = 0;
		this.nodeSwapAttempts = 0;
		this.randomSeed = System.currentTimeMillis();
		
		init();
	}



	/**
	 * 
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * Set either nodeSwapTimeLimit or nodeSwapAttempts to 0 to ensure it is not used.
	 * The expectation is that one or the other, or both is 0. If both are non-zero then
	 * the one that means greater number of node swaps is the limit.
	 * 
	 * @param directed true if the graph is treated as directed, false if undirected
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param editCosts are a mapping between edit codes and their costs DELETE_NODE, ADD_NODE, DELETE_EDGE, ADD_EDGE, if @see{nodeLabels} is true, RELABEL_NODE also needs to be present
	 * @param nodeSwapTimeLimit time in milliseconds to exit approximation
	 * @param nodeSwapAttempts number of iterations of the node swap routine
	 * @param randomSeed set to System.currentTimeMillis() for true random
	 * @throws FastGraphException if an edit operation cost is missing or if both limits are -1
	 */
	public ApproximateGEDSimple(boolean directed, boolean nodeLabels, HashMap<Integer,Double> editCosts, long nodeSwapTimeLimit, int nodeSwapAttempts, long randomSeed) throws FastGraphException {
		super();
		this.editCosts = editCosts;
		if(nodeSwapTimeLimit == -1 && nodeSwapAttempts == -1) {
			throw new FastGraphException("Cannot have both nodeSwapTimeLimit and nodeSwapAttempts set to unlimited.");
		}
		this.directed = directed;
		this.nodeLabels = nodeLabels;
		this.nodeSwapTimeLimit = nodeSwapTimeLimit;
		if(nodeSwapTimeLimit == -1) {
			this.nodeSwapTimeLimit = Long.MAX_VALUE;
		}
		this.nodeSwapAttempts = nodeSwapAttempts;
		if(nodeSwapAttempts == -1) {
			this.nodeSwapAttempts = Integer.MAX_VALUE;
		}
		this.randomSeed = randomSeed;
		
		init();
	}

	/**
	 * Set up the edit operation costs from the cost mapping.
	 * 
	 * @throws FastGraphException if any required cost is not present
	 */
	private void init() throws FastGraphException {

		deleteNodeCost = editCosts.get(EditOperation.DELETE_NODE);
		addNodeCost = editCosts.get(EditOperation.ADD_NODE);
		deleteEdgeCost = editCosts.get(EditOperation.DELETE_EDGE);
		addEdgeCost = editCosts.get(EditOperation.ADD_EDGE);
		relabelNodeCost = editCosts.get(EditOperation.RELABEL_NODE);
		
		if(deleteNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.DELETE_NODE");
		}
		if(addNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.ADD_NODE");
		}
		if(deleteEdgeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.DELETE_EDGE");
		}
		if(addEdgeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.ADD_EDGE");
		}
		if(nodeLabels && relabelNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.RELABEL_NODE");
		}
		
		random = new Random(randomSeed);
	}

	/**
	 * 
	 * @return how many node swaps were made
	 */
	public int getNodeSwaps() {return nodeSwaps;}

	
	/**
	 * 
	 * @return how long the node swaps took in milliseconds.
	 */
	public long getApproximationTime() {return approximationTime;}

	
	/**
	 * This returns an approximation of graph edit distance between the two graphs. 
	 * Zero means the graphs are already isomorphic.
	 * Greater values mean more dissimilarity. 
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the cost of the edits between two graphs.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
	
		addCapacity = 0;
		addStart = g1.getNumberOfNodes();
		if((g1.getNumberOfNodes() < g2.getNumberOfNodes())) {
			addCapacity = (g2.getNumberOfNodes()-g1.getNumberOfNodes());
		}
		int deleteCapacity = 0;
		if((g1.getNumberOfNodes() > g2.getNumberOfNodes())) {
			deleteCapacity = (g1.getNumberOfNodes()-g2.getNumberOfNodes());
		}
		nodeMapping = new HashMap<>(g1.getNumberOfNodes()*4); // g1 nodes to g2 nodes
		reverseNodeMapping = new HashMap<>(g1.getNumberOfNodes()*4); // g2 nodes to g1 nodes
		deleteNodes = new ArrayList<>(deleteCapacity); // g1 nodes to be removed

		// initial node mapping (and final mapping if there are no swaps), based on degree
		if(directed) {
			createDirectedMapping(g1,g2);
		} else {
			createUndirectedMapping(g1,g2);
		}

		if(directed) {
			findDirectedEdgeChanges(g1,g2);
		} else {
			findUndirectedEdgeChanges(g1,g2);
		}
		
		editList = createEditList(g1,g2);
		
		if(g1.getNumberOfNodes() == 0 && g2.getNumberOfNodes() == 0) {
			return editList.getCost();
		}
		
		long startTime = System.currentTimeMillis();
		boolean keepBadMove = false;
		approximationTime = System.currentTimeMillis()-startTime;
		nodeSwaps = 0;
		while( approximationTime < nodeSwapTimeLimit || nodeSwaps < nodeSwapAttempts) {
			HashMap<Integer,Integer> oldNodeMapping = new HashMap<>(nodeMapping);
			HashMap<Integer,Integer> oldReverseNodeMapping = new HashMap<>(reverseNodeMapping);
			ArrayList<Integer> oldDeleteNodes = new ArrayList<>(deleteNodes);
			
			int size = g2.getNumberOfNodes();
			if(g1.getNumberOfNodes() > g2.getNumberOfNodes()) {
				size = g1.getNumberOfNodes();
			}

			int g1Node1 = random.nextInt(size);
			int g1Node2 = random.nextInt(size);

			if(g1Node1 == g1Node2) {
				nodeSwaps++;
				approximationTime = System.currentTimeMillis()-startTime;
				continue;
			}
			
			// if both are delete nodes, no changes required
			if(deleteNodes.contains(g1Node1) && deleteNodes.contains(g1Node2)) {
				nodeSwaps++;
				approximationTime = System.currentTimeMillis()-startTime;
				continue;
			}
			// if one is a delete node then modify the various lists and maps
			// will not need to worry about add nodes
			if(deleteNodes.contains(g1Node1)) {
				int g2Node2 = nodeMapping.get(g1Node2);
				int index = deleteNodes.indexOf(g1Node1);
				deleteNodes.remove(index);
				deleteNodes.add(g1Node2);
				nodeMapping.remove(g1Node2);
				nodeMapping.put(g1Node1,g2Node2);
				reverseNodeMapping.remove(g2Node2);
				reverseNodeMapping.put(g2Node2,g1Node1);
			}
			if(deleteNodes.contains(g1Node2)) {
				int g2Node1 = nodeMapping.get(g1Node1);
				int index = deleteNodes.indexOf(g1Node2);
				deleteNodes.remove(index);
				deleteNodes.add(g1Node1);
				nodeMapping.remove(g1Node1);
				nodeMapping.put(g1Node2,g2Node1);
				reverseNodeMapping.remove(g2Node1);
				reverseNodeMapping.put(g2Node1,g1Node2);
			}
			
			if(!deleteNodes.contains(g1Node1) && !deleteNodes.contains(g1Node2)) {
				int g2Node1 = nodeMapping.get(g1Node1);
				int g2Node2 = nodeMapping.get(g1Node2);
				nodeMapping.put(g1Node1,g2Node2);
				nodeMapping.put(g1Node2,g2Node1);
				reverseNodeMapping.put(g2Node2, g1Node1);
				reverseNodeMapping.put(g2Node1, g1Node2);
				
			}
		
			if(directed) {
				findDirectedEdgeChanges(g1,g2);
			} else {
				findUndirectedEdgeChanges(g1,g2);
			}

			EditList testEditList = createEditList(g1,g2);
			
			if(keepBadMove || testEditList.getCost() < editList.getCost()) {
				editList = testEditList;
//System.out.println("swapped "+g1Node1+" "+g1Node2);
			} else { // no benefit, so revert unless bad move allowed
				nodeMapping = oldNodeMapping;
				reverseNodeMapping = oldReverseNodeMapping;
				deleteNodes = oldDeleteNodes;
			}
			
			nodeSwaps++;
			approximationTime = System.currentTimeMillis()-startTime;
			
			double remainingTime = 1.0-approximationTime/(nodeSwapTimeLimit*1.0);
			double remainingIteration = 1.0-nodeSwaps/(nodeSwapAttempts*1.0);
			double remainingPercent = 0.0;
			if(nodeSwapTimeLimit == 0) {
				remainingPercent = remainingIteration;
			} else if(nodeSwapAttempts == 0) {
				remainingPercent = remainingTime;
			} else if(remainingIteration > remainingTime) {
				remainingPercent = remainingIteration;
			} else {
				remainingPercent = remainingTime;
			}
			double chanceOfBadMove = remainingPercent-0.25;
			if(chanceOfBadMove < 0) {
				chanceOfBadMove = 0;
			}
			keepBadMove = false;
			if(random.nextDouble() < chanceOfBadMove) {
				keepBadMove = true;
			}
			
		}

//System.out.println(editList);
//System.out.println("node Mapping    "+nodeMapping);
//System.out.println("reverse Mapping "+reverseNodeMapping);
//System.out.println("delete node list "+deleteNodes);
//System.out.println("delete edge list "+deleteEdges);
//System.out.println("add node set "+addNodeSet);
//System.out.println("add edge lists "+addEdgeNode1List+" "+addEdgeNode2List);

		return editList.getCost();
	}
	

	/**
	 * Set the initial mapping for undirected graphs. Compare the nodes
	 * by degree size, largest first. This will work OK
	 * for the labelled version as long as the node label edit cost is not too high.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	private void createUndirectedMapping(FastGraph g1, FastGraph g2) {
		ArrayList<Integer> n1List = new ArrayList<Integer>(g1.getNumberOfNodes());
		ArrayList<Integer> n2List = new ArrayList<Integer>(g2.getNumberOfNodes());
		for(int i =0; i < g1.getNumberOfNodes(); i++) {
			n1List.add(i);
		}
		for(int i =0; i < g2.getNumberOfNodes(); i++) {
			n2List.add(i);
		}
		NodeDegreeComparator ndc1 = new NodeDegreeComparator(g1,g1); // comparing nodes from the same graph
		ndc1.setAscending(false);
		n1List.sort(ndc1);

		NodeDegreeComparator ndc2 = new NodeDegreeComparator(g2,g2); // comparing nodes from the same graph
		ndc2.setAscending(false);
		n2List.sort(ndc2);
		
		// create initial mapping, and either deleted or added arrays
		// depending on the number of nodes in g1 and g2
		while(true) {
			if(n1List.size() == 0) {
				// run out of g1 nodes, so put the rest of g2 in the add nodes list
				// this happens after the existing nodes are mapped, below
				for(int j = 0; j < n2List.size() ;j++) {
					int g1NodeIndex = g1.getNumberOfNodes()+j;
					int g2NodeIndex = n2List.get(j);
					nodeMapping.put(g1NodeIndex,g2NodeIndex); // also add to the node mapping, as we will need this to add edges
					reverseNodeMapping.put(g2NodeIndex,g1NodeIndex);
				}
				break;
			} else if(n2List.size() == 0) { // run out of g2 nodes, so put the rest of g1 in the delete nodes list
				for(int j = 0; j < n1List.size() ;j++) {
					deleteNodes.add(n1List.get(j));
				}
				break;

			}
			// largest degrees match
			nodeMapping.put(n1List.get(0),n2List.get(0));
			reverseNodeMapping.put(n2List.get(0),n1List.get(0));
			n1List.remove(0);
			n2List.remove(0);
		}
	}
	
	
	/**
	 * Set the initial mapping for directed graphs. Compare the nodes
	 * alternately by indegree and outdegree size, largest first. This will work OK
	 * for the labelled version as long as the node label edit cost is not too high.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	private void createDirectedMapping(FastGraph g1, FastGraph g2) {
		ArrayList<Integer> n1InList = new ArrayList<Integer>(g1.getNumberOfNodes());
		ArrayList<Integer> n1OutList = new ArrayList<Integer>(g1.getNumberOfNodes());
		ArrayList<Integer> n2InList = new ArrayList<Integer>(g2.getNumberOfNodes());
		ArrayList<Integer> n2OutList = new ArrayList<Integer>(g2.getNumberOfNodes());
		for(int i =0; i < g1.getNumberOfNodes(); i++) {
			n1InList.add(i);
			n1OutList.add(i);
		}
		for(int i =0; i < g2.getNumberOfNodes(); i++) {
			n2InList.add(i);
			n2OutList.add(i);
		}
		NodeInDegreeComparator ndcIn1 = new NodeInDegreeComparator(g1,g1); // comparing nodes from the same graph
		ndcIn1.setAscending(false);
		n1InList.sort(ndcIn1);

		NodeOutDegreeComparator ndcOut1 = new NodeOutDegreeComparator(g1,g1); // comparing nodes from the same graph
		ndcOut1.setAscending(false);
		n1OutList.sort(ndcOut1);

		NodeInDegreeComparator ndcIn2 = new NodeInDegreeComparator(g2,g2); // comparing nodes from the same graph
		ndcIn2.setAscending(false);
		n2InList.sort(ndcIn2);
		
		NodeOutDegreeComparator ndcOut2 = new NodeOutDegreeComparator(g2,g2); // comparing nodes from the same graph
		ndcOut2.setAscending(false);
		n2OutList.sort(ndcOut2);
		
		// create initial mapping, and either deleted or added arrays
		// depending on the number of nodes in g1 and g2
		boolean pickingOut = false;
		while(true) {
			if(n1InList.size() == 0) {
				// run out of g1 nodes, so put the rest of g2 in the add nodes list
				// this happens after the existing nodes are mapped, below
				for(int j = 0; j < n2InList.size() ;j++) {
					int g1NodeIndex = g1.getNumberOfNodes()+j;
					int g2NodeIndex = n2InList.get(j);
					nodeMapping.put(g1NodeIndex,g2NodeIndex); // also add to the node mapping, as we will need this to add edges
					reverseNodeMapping.put(g2NodeIndex,g1NodeIndex);

				}
				break;
			} else if(n2InList.size() == 0) { // run out of g2 nodes, so put the rest of g1 in the delete nodes list
				for(int j = 0; j < n1InList.size() ;j++) {
					deleteNodes.add(n1InList.get(j));
				}
				break;

			}
			
			// largest degrees match, alternate between in and out degree lists
			if(pickingOut) {
				int n1 = n1OutList.get(0);
				int n2 = n2OutList.get(0);
				nodeMapping.put(n1,n2);
				reverseNodeMapping.put(n2,n1);
				n1OutList.remove(0);
				n2OutList.remove(0);
				int index1 = n1InList.indexOf(n1);
				int index2 = n2InList.indexOf(n2);
				n1InList.remove(index1);
				n2InList.remove(index2);
				pickingOut = false;
			} else {
				int n1 = n1InList.get(0);
				int n2 = n2InList.get(0);
				nodeMapping.put(n1,n2);
				reverseNodeMapping.put(n2,n1);
				n1InList.remove(0);
				n2InList.remove(0);
				int index1 = n1OutList.indexOf(n1);
				int index2 = n2OutList.indexOf(n2);
				n1OutList.remove(index1);
				n2OutList.remove(index2);
				pickingOut = true;
			}
		}
		
	}
	

	/**
	 * Take the required changes and create the appropriate edit list.
	 * 
	 * @param g1 the graph to be changed
	 * @param g2 the target graph
	 * @return the edit list of required operations
	 */
	private EditList createEditList(FastGraph g1, FastGraph g2) {
		EditList ret = new EditList();
		
		EditOperation eo;
		// add nodes, must be in order of their index, otherwise subtle bugs occur
		for(int n = addStart; n < addCapacity+addStart; n++) {
			Integer g2n = nodeMapping.get(n);
			String label = g2.getNodeLabel(g2n);
			eo = new EditOperation(EditOperation.ADD_NODE,addNodeCost,-1,label,-1,-1);
			ret.addOperation(eo);
		}
		// add edges
		LinkedList<Integer> listNode1 = new LinkedList<Integer>(addEdgeNode1List);
		LinkedList<Integer> listNode2 = new LinkedList<Integer>(addEdgeNode2List);
		while(!listNode1.isEmpty()) {
			int node1 = listNode1.pop();
			int node2 = listNode2.pop();
			eo = new EditOperation(EditOperation.ADD_EDGE,addEdgeCost,-1,"",node1,node2);
			ret.addOperation(eo);
		}
		
		// delete edges, need to be largest id first
		deleteEdges.sort(reverseComparator);
		for(Integer e : deleteEdges) {
			eo = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,e,null,-1,-1);
			ret.addOperation(eo);
		}
		// change node labels if flag set, must be before the node delete
		// as it uses g1 ids
		if(nodeLabels) {
			for(Integer n : nodeMapping.keySet()) {
				if(n < addStart) { // need to be existing nodes
					Integer g2n = nodeMapping.get(n);
					String nLabel = g1.getNodeLabel(n);
					String g2nLabel = g2.getNodeLabel(g2n);
					if(!nLabel.equals(g2nLabel)) {
						eo = new EditOperation(EditOperation.RELABEL_NODE,relabelNodeCost,n,g2nLabel,-1,-1);
						ret.addOperation(eo);
					}
				}
			}
		}
		
		// delete nodes, need to be largest id first
		deleteNodes.sort(reverseComparator);
		for(Integer n : deleteNodes) {
			eo = new EditOperation(EditOperation.DELETE_NODE,deleteNodeCost,n,null,-1,-1);
			ret.addOperation(eo);
		}
		
		return ret;
		
	}
	

	/**
	 * populate the edge addition and deletion data structures in the undirected case.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	public void findUndirectedEdgeChanges(FastGraph g1, FastGraph g2) {
		
		HashMap<String,LinkedList<Integer>> pairToMultipleEdgeMapping = new HashMap<>(g1.getNumberOfEdges());
		HashMap<String,Integer> pairToNode1Mapping = new HashMap<>(g1.getNumberOfEdges());
		HashMap<String,Integer> pairToNode2Mapping = new HashMap<>(g1.getNumberOfEdges());

		// find the edges to delete where g2 does not have the corresponding edge
		deleteEdges = new ArrayList<>(g1.getNumberOfEdges());
		for(int e = 0; e < g1.getNumberOfEdges(); e++) {
			int node1 = g1.getEdgeNode1(e);
			int node2 = g1.getEdgeNode2(e);
			
			// if the g1 node is being deleted, the edge must be deleted
			if(deleteNodes.contains(node1) || deleteNodes.contains(node2)) {
				deleteEdges.add(e);
				continue;
			}
			
			// check for a corresponding edge in g2
			boolean found = false;
			Integer node1Map = nodeMapping.get(node1);
			Integer node2Map = nodeMapping.get(node2);
			if(node1Map == null || node2Map == null) {
				// if connecting node in n2 does not exist, then there is no edge
				found = false;
			} else {

				int[] connecting = g2.getNodeConnectingEdges(node1Map);
				for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
					int connectingE = connecting[connectingIndex];
					int connectingN = g2.oppositeEnd(connectingE, node1Map);
					if(connectingN == node2Map) {
						found = true;
						break;
					}
				}
			}
			if(found == false) {
				deleteEdges.add(e);
				continue;
			}
			
			// store potential multiedge details, for possible delete
			String pairEntry = node1+"-"+node2;
			if(node1 > node2) {
				pairEntry = node2+"-"+node1;
			}
			LinkedList<Integer> edges = pairToMultipleEdgeMapping.get(pairEntry);
			if(edges == null) {
				edges = new LinkedList<Integer>();
				pairToMultipleEdgeMapping.put(pairEntry, edges);
				pairToNode1Mapping.put(pairEntry, node1);
				pairToNode2Mapping.put(pairEntry, node2);
			}
			edges.add(e);
		}
		// find the edges to add where g1 does not currently have an edge
		// checks for g2 edges that are not in g1
		addEdgeNode1List = new LinkedList<>();
		addEdgeNode2List = new LinkedList<>();
		for(int e = 0; e < g2.getNumberOfEdges(); e++) {
			int g2Node1 = g2.getEdgeNode1(e);
			int g2Node2 = g2.getEdgeNode2(e);
			
			Integer g1Node1Map = reverseNodeMapping.get(g2Node1);
			Integer g1Node2Map = reverseNodeMapping.get(g2Node2);

			boolean found = false;
			if(isAnAddNode(g1Node1Map) || isAnAddNode(g1Node2Map)) {
				// if either node is going to be added, we will need to add the edge
				found = false;
			} else {
				// both nodes are in g1, so check for a corresponding edge in g1
				int[] connecting = g1.getNodeConnectingEdges(g1Node1Map);
				for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
					int connectingE = connecting[connectingIndex];
					int connectingN = g1.oppositeEnd(connectingE, g1Node1Map);
					if(connectingN == g1Node2Map) {
						found = true;
						break;
					}
				}
			}
			// no edge, so add one
			if(found == false) {
				addEdgeNode1List.add(g1Node1Map);
				addEdgeNode2List.add(g1Node2Map);
				continue;
			}
			
		}
		// check for multiple edge entries without sufficient g2 edges to match
		// this is potentially quite slow if there are a lot of multiedges
		for(String pairEntry : pairToMultipleEdgeMapping.keySet()) {

			LinkedList<Integer> edges = pairToMultipleEdgeMapping.get(pairEntry);
			int node1 = pairToNode1Mapping.get(pairEntry);
			int node2 = pairToNode2Mapping.get(pairEntry);
			
			Integer node1Map = nodeMapping.get(node1);
			Integer node2Map = nodeMapping.get(node2);
			// we know there is at least one edge in g1. Lets see if there is the same number in g2
			int count = 0;
			int[] connecting = g2.getNodeConnectingEdges(node1Map);
			for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
				int connectingE = connecting[connectingIndex];
				int connectingN = g2.oppositeEnd(connectingE, node1Map);
				if(connectingN == node2Map) {
					count++;
				}
			}
			
			// don't want to count self sourcing twice
			if(node1 == node2) {
				count = count/2;
			}
			
			// if there are too many edges in g1, add some to the remove list 
			while(count < edges.size()) {
				Integer removeEdge = edges.pop();
				deleteEdges.add(removeEdge);
			}
			
			// if there are too few edges in g1, add some to the add list
			if(count > edges.size()) {
				for(int i = 0; i < (count - edges.size());i++) {
					addEdgeNode1List.add(node1);
					addEdgeNode2List.add(node2);
				}
			}
		}
	}
	
	
	
	/**
	 * populate the edge addition and deletion data structures in the directed case.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	public void findDirectedEdgeChanges(FastGraph g1, FastGraph g2) {
		
		HashMap<String,LinkedList<Integer>> pairToMultipleEdgeMapping = new HashMap<>(g1.getNumberOfEdges());
		HashMap<String,Integer> pairToNode1Mapping = new HashMap<>(g1.getNumberOfEdges());
		HashMap<String,Integer> pairToNode2Mapping = new HashMap<>(g1.getNumberOfEdges());

		// find the edges to delete where g2 does not have the corresponding edge
		deleteEdges = new ArrayList<>(g1.getNumberOfEdges());
		for(int e = 0; e < g1.getNumberOfEdges(); e++) {
			int node1 = g1.getEdgeNode1(e);
			int node2 = g1.getEdgeNode2(e);
			
			// if the g1 node is being deleted, the edge must be deleted
			if(deleteNodes.contains(node1) || deleteNodes.contains(node2)) {
				deleteEdges.add(e);
				continue;
			}
			
			// check for a corresponding edge in g2
			boolean found = false;
			Integer node1Map = nodeMapping.get(node1);
			Integer node2Map = nodeMapping.get(node2);
			if(node1Map == null || node2Map == null) {
				// if connecting node in n2 does not exist, then there is no edge
				found = false;
			} else {

				// check the out edges of the g2 node1 to see if it points to the g2 node2
				int[] connecting = g2.getNodeConnectingOutEdges(node1Map);
				for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
					int connectingE = connecting[connectingIndex];
					int connectingN = g2.getEdgeNode2(connectingE);
					if(connectingN == node2Map) {
						found = true;
						break;
					}
				}
			}
			if(found == false) {
				deleteEdges.add(e);
				continue;
			}
			
			// store potential multiedge details, for possible delete
			String pairEntry = node1+"-"+node2;
			LinkedList<Integer> edges = pairToMultipleEdgeMapping.get(pairEntry);
			if(edges == null) {
				edges = new LinkedList<Integer>();
				pairToMultipleEdgeMapping.put(pairEntry, edges);
				pairToNode1Mapping.put(pairEntry, node1);
				pairToNode2Mapping.put(pairEntry, node2);
			}
			edges.add(e);
		}
		// find the edges to add where g1 does not currently have an edge
		// checks for g2 edges that are not in g1
		addEdgeNode1List = new LinkedList<>();
		addEdgeNode2List = new LinkedList<>();
		for(int e = 0; e < g2.getNumberOfEdges(); e++) {
			int g2Node1 = g2.getEdgeNode1(e);
			int g2Node2 = g2.getEdgeNode2(e);
			
			Integer g1Node1Map = reverseNodeMapping.get(g2Node1);
			Integer g1Node2Map = reverseNodeMapping.get(g2Node2);
			
			boolean found = false;
			if(isAnAddNode(g1Node1Map) || isAnAddNode(g1Node2Map)) {
				// if either node is going to be added, we will need to add the edge
				found = false;
			} else {
				// both nodes are in g1, so check for a corresponding edge in g1
				int[] connecting = g1.getNodeConnectingOutEdges(g1Node1Map);
				for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
					int connectingE = connecting[connectingIndex];
					if(g1.getEdgeNode1(connectingE) != g1Node1Map) {
						continue;
					}
					if(g1.getEdgeNode2(connectingE) != g1Node2Map) {
						continue;
					}
					found = true;
					break;
				}
			}

			// no edge, so add one
			if(!found) {
				addEdgeNode1List.add(g1Node1Map);
				addEdgeNode2List.add(g1Node2Map);
				continue;
			}
			
		}
		// check for multiple edge entries without sufficient g2 edges to match
		// this is potentially quite slow if there are a lot of multiedges
		for(String pairEntry : pairToMultipleEdgeMapping.keySet()) {
			LinkedList<Integer> edges = pairToMultipleEdgeMapping.get(pairEntry);

			int node1 = pairToNode1Mapping.get(pairEntry);
			int node2 = pairToNode2Mapping.get(pairEntry);
			
			Integer node1Map = nodeMapping.get(node1);
			Integer node2Map = nodeMapping.get(node2);
			// we know there is at least one edge in g1.
			// Lets see if there is the same number going in the same direction in g2
			int count = 0;
			int[] connecting = g2.getNodeConnectingEdges(node1Map);
			
			for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
				int connectingE = connecting[connectingIndex];
				if(g2.getEdgeNode1(connectingE) != node1Map) {
					continue;
				}
				if(g2.getEdgeNode2(connectingE) != node2Map) {
					continue;
				}
				count++;
			}
			
			// don't want to count self sourcing twice
			if(node1 == node2) {
				count = count/2;
			}
			
			// if there are too many edges in g1, add some to the remove list 
			while(count < edges.size()) {
				Integer removeEdge = edges.pop();
				deleteEdges.add(removeEdge);
			}
			
			// if there are too few edges in g1, add some to the add list
			if(count > edges.size()) {
				for(int i = 0; i < (count - edges.size());i++) {

					addEdgeNode1List.add(node1);
					addEdgeNode2List.add(node2);
				}
			}
		}
	}

	/**
	 * See if a nodeMapping node is additional to the current g1 nodes
	 * 
	 * @param n
	 * @return true if the node is going to be added from g2 to the new graph, false if it already exists in g1
	 */
	private boolean isAnAddNode(int n) {
		if(n >= addStart) {
			return true;
		}
		return false;
	}
	

	/**
	 * Run this to test randomly generated graphs infinitely.
	 * 
	 * @throws FastGraphException if a generator fails.
	 */
	public static void testByRandomGraphLoop() throws FastGraphException {
		while(true) {
			long seed = System.currentTimeMillis();
			Random r = new Random(seed);
			FastGraph g1,g2,gRet;
			HashMap<Integer,Double> editCosts;
			ApproximateGEDSimple ged;
			EditList el, retEditList1, retEditList2;
			int maxNodes = 20;
			int maxEdges = 80;
		
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,90.9);
			editCosts.put(EditOperation.ADD_NODE,80.4);
			editCosts.put(EditOperation.DELETE_EDGE,70.8);
			editCosts.put(EditOperation.ADD_EDGE,60.2);
			editCosts.put(EditOperation.RELABEL_NODE,50.1);
			
			int nodes1 = r.nextInt(maxNodes)+1;
			int edges1 = r.nextInt(maxEdges+1);
			int nodes2 = r.nextInt(maxNodes)+1;
			int edges2 = r.nextInt(maxEdges+1);
			
			System.out.println("g1 nodes: "+nodes1+", g1 edges: "+edges1+", g2 nodes: "+nodes2+", g2 edges: "+edges2);

			g1 = FastGraph.randomGraphFactory(nodes1, edges1, seed+10, false);
			el = new EditList();
			for(int i = 0; i < g1.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g1 = el.applyOperations(g1);

			g2 = FastGraph.randomGraphFactory(nodes2, edges2, seed+20, false);
			el = new EditList();
			for(int i = 0; i < g2.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g2 = el.applyOperations(g2);

			ged = new ApproximateGEDSimple(false,false,editCosts,0,0,-1);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,false,false)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,false,false)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("AAA problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("AAA OK "+seed);
			}

			ged = new ApproximateGEDSimple(false,false,editCosts,0,1000,seed+30);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,false,false)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				Debugger.enabled = true;
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,false,false)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("BBB problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("BBB OK "+seed);
			}

			ged = new ApproximateGEDSimple(false,true,editCosts,0,0,-1);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,false,true)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,false,true)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("CCC problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("CCC OK "+seed);
			}

			ged = new ApproximateGEDSimple(false,true,editCosts,0,1000,seed+30);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,false,true)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				Debugger.enabled = true;
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,false,true)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("DDD problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("DDD OK "+seed);
			}

			ged = new ApproximateGEDSimple(true,false,editCosts,0,0,-1);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,true,false)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,true,false)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("EEE problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("EEE OK "+seed);
			}

			ged = new ApproximateGEDSimple(true,false,editCosts,0,1000,seed+30);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,true,false)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				Debugger.enabled = true;
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,true,false)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("FFF problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("FFF OK "+seed);
			}

			ged = new ApproximateGEDSimple(true,true,editCosts,0,0,-1);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,true,true)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,true,true)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("GGG problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("GGG OK "+seed);
			}

			ged = new ApproximateGEDSimple(true,true,editCosts,0,1000,seed+30);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			if(!ExactIsomorphism.isomorphic(g2,gRet,true,true)||!g1.checkConsistency()||!g2.checkConsistency()||!gRet.checkConsistency()) {
				Debugger.enabled = true;
				System.out.println(ExactIsomorphism.isomorphic(g2,gRet,true,true)+" "+g1.checkConsistency()+" "+g2.checkConsistency()+" "+gRet.checkConsistency());
				System.out.println("HHH problem seed "+seed);
				System.exit(0);
			} else {
				System.out.println("HHH OK "+seed);
			}

		}
	}
	
	
}
