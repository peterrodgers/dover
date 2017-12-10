package uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.NodeDegreeComparator;
import uk.ac.kent.dover.fastGraph.comparators.ReverseIntegerComparator;
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
	
	private long nodeSwapTimeLimit;
	private int nodeSwapAttempts;
	private long randomSeed;
	
	private int nodeSwaps = 0;
	private long approximationTime = 0;;
	
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;

	private HashMap<Integer,Integer> nodeMapping; // g1 nodes to g2 nodes
	private HashMap<Integer,Integer> reverseNodeMapping; // g2 nodes to g1 nodes
	private ArrayList<Integer> deleteNodes; // g1 nodes to be removed
	private HashMap<Integer,Integer> addNodeMapping; // g1 new node Index to g2 nodes to be added
	private ArrayList<Integer> deleteEdges; // g1 nodes to be removed
	private HashMap<Integer,Integer> addEdgeMapping; // g1 new node Index to g2 nodes to be added
	private LinkedList<Integer> addEdgeNode1List; // g1 new node Index to g1 node1 to be added
	private LinkedList<Integer> addEdgeNode2List; // g1 new node Index to g1 node2 to be added
	
	private ReverseIntegerComparator reverseComparator = new ReverseIntegerComparator();
	
	private Random random;
	
	public static void main(String [] args) {
		
		try {

			int nodes = 1000000;
			while(true) {
				double ret;
				List<EditOperation> retList;
				FastGraph g1,g2,gRet;
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
				
				g1 = FastGraph.randomGraphFactory(nodes, edges, 7777,false);
				
				g2 = FastGraph.randomGraphFactory(nodes, edges, 5555, false);
				
				long start1 = System.currentTimeMillis();
				
				ged = new ApproximateGEDSimple(false,false,editCosts,0,0,111);
				
				ret = ged.similarity(g1, g2);
				
				System.out.println("NO OPTIMIZATION nodes "+nodes+" edges "+edges);
				System.out.println("similarity time "+(System.currentTimeMillis()-start1)/1000.0);
				System.out.println("cost "+ged.getEditList().getCost()+" length "+ged.getEditList().getEditList().size());
				
				retEditList1 = ged.getEditList();
				retList = retEditList1.getEditList();
//				gRet = retEditList1.applyOperations(g1);
				long start2 = System.currentTimeMillis();
						
				ged = new ApproximateGEDSimple(false,false,editCosts,1000,0,777777);
				
				ret = ged.similarity(g1, g2);
				
				System.out.println("OPTIMIZATION nodes "+nodes+" edges "+edges);
				System.out.println("similarity time "+(System.currentTimeMillis()-start2)/1000.0);
				
				retEditList2 = ged.getEditList();
				retList = retEditList2.getEditList();
				
				System.out.println("node swaps "+ged.getNodeSwaps());
				System.out.println("cost "+retEditList2.getCost()+" length "+retList.size());
				
//				gRet = retEditList2.applyOperations(g1);
System.out.println("checks "+g1.checkConsistency()+" "+g2.checkConsistency()+" ");
				nodes = (nodes * 3)/2;

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
		super(editCosts);
		
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
		super(directed,editCosts);
		
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
		super(directed,editCosts);

		if(nodeSwapTimeLimit == -1 && nodeSwapAttempts == -1) {
			throw new FastGraphException("Cannot have both nodeSwapTimeLimit and nodeSwapAttempts set to unlimited.");
		}
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
	 * @return how long the node swaps were made for in milliseconds.
	 */
	public long getApproximationTime() {return approximationTime;}
	
	/**
	 * This returns the graph edit distance calculation between the two graphs. 
	 * Zero means the graphs are isomorphic. Greater values mean more dissimilarity. 
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the cost of the edits between two graphs.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
		
		int addCapacity = 0;
		if((g2.getNumberOfNodes() > g1.getNumberOfNodes())) {
			addCapacity = (g2.getNumberOfNodes()-g1.getNumberOfNodes())*4;
		}
		int deleteCapacity = 0;
		if((g1.getNumberOfNodes() > g2.getNumberOfNodes())) {
			deleteCapacity = (g1.getNumberOfNodes()-g2.getNumberOfNodes());
		}
		nodeMapping = new HashMap<>(g1.getNumberOfNodes()*4); // g1 nodes to g2 nodes
		reverseNodeMapping = new HashMap<>(g1.getNumberOfNodes()*4); // g2 nodes to g1 nodes
		addNodeMapping = new HashMap<>(addCapacity); // g2 nodes to be added
		deleteNodes = new ArrayList<>(deleteCapacity); // g1 nodes to be removed
		
		// find initial node mappings based on degree
		ArrayList<Integer> n1List = new ArrayList<Integer>();
		for(int i =0; i < g1.getNumberOfNodes(); i++) {
			n1List.add(i);
		}
		ArrayList<Integer> n2List = new ArrayList<Integer>();
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
					addNodeMapping.put(g1NodeIndex,g2NodeIndex);
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

		findEdgeChanges(g1,g2);
		editList = createEditList(g1,g2);
		
		if(g1.getNumberOfNodes() == 0 && g1.getNumberOfNodes() == 0) {
			return editList.getCost();
		}
		
		long startTime = System.currentTimeMillis();
		boolean keepBadMove = false;
		approximationTime = System.currentTimeMillis()-startTime;
		nodeSwaps = 0;
		while( approximationTime < nodeSwapTimeLimit || nodeSwaps < nodeSwapAttempts) {
			HashMap<Integer,Integer> oldNodeMapping = new HashMap<>(nodeMapping);
			HashMap<Integer,Integer> oldReverseNodeMapping = new HashMap<>(reverseNodeMapping);
			HashMap<Integer,Integer> oldAddNodeMapping = new HashMap<>(addNodeMapping);
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
				nodeMapping.put(g1Node1,g2Node2);
			}
			if(deleteNodes.contains(g1Node2)) {
				int g2Node1 = nodeMapping.get(g1Node1);
				int index = deleteNodes.indexOf(g1Node2);
				deleteNodes.remove(index);
				deleteNodes.add(g1Node1);
				nodeMapping.put(g1Node2,g2Node1);
			}
			
			if(!deleteNodes.contains(g1Node1) && !deleteNodes.contains(g1Node2)) {
				int g2Node1 = nodeMapping.get(g1Node1);
				int g2Node2 = nodeMapping.get(g1Node2);
				nodeMapping.put(g1Node1,g2Node2);
				nodeMapping.put(g1Node2,g2Node1);
				reverseNodeMapping.put(g2Node2, g1Node1);
				reverseNodeMapping.put(g2Node1, g1Node2);
				// if only one is in an add node, leave it as
				// an add node, as add ids are at the end
				// but change its mapping
				if(addNodeMapping.get(g1Node1) != null & addNodeMapping.get(g1Node2) == null) {
					addNodeMapping.put(g1Node1,g2Node2);
				} else 	if(addNodeMapping.get(g1Node1) == null & addNodeMapping.get(g1Node2) != null) {
					addNodeMapping.put(g1Node2,g2Node1);
				}
			}
		
			findEdgeChanges(g1,g2);
			EditList testEditList = createEditList(g1,g2);
			
			if(keepBadMove || testEditList.getCost() < editList.getCost()) {
				editList = testEditList;
//System.out.println(keepBadMove+" swapNodes g1Node1 "+g1Node1+" g1Node2 "+g1Node2+" cost "+editList.getCost()+"\n"+editList);
//System.out.println("swapped "+g1Node1+" "+g1Node2);
			} else { // no benefit, so revert unless bad move allowed
				nodeMapping = oldNodeMapping;
				reverseNodeMapping = oldReverseNodeMapping;
				addNodeMapping = oldAddNodeMapping;
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

		return editList.getCost();
	}
	

	/**
	 * Take the required changes and create the appropriate edit list.
	 * @param g1 the graph to be changed
	 * @param g2 the target graph
	 * @return the edit list of required operations
	 */
	private EditList createEditList(FastGraph g1, FastGraph g2) {
		EditList ret = new EditList();
		
		EditOperation eo;
		// add nodes
		for(Integer n : addNodeMapping.keySet()) {
			Integer g2n = addNodeMapping.get(n);
			String label = g2.getNodeLabel(g2n);
			eo = new EditOperation(EditOperation.ADD_NODE,addNodeCost,-1,label,-1,-1);
			ret.addOperation(eo);
		}
		// add edges
		while(!addEdgeNode1List.isEmpty()) {
			int node1 = addEdgeNode1List.pop();
			int node2 = addEdgeNode2List.pop();
			eo = new EditOperation(EditOperation.ADD_EDGE,addEdgeCost,-1,"",node1,node2);
			ret.addOperation(eo);
		}
		
		// delete edges, need to be largest id first
		deleteEdges.sort(reverseComparator);
		for(Integer e : deleteEdges) {
			eo = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,e,null,-1,-1);
			ret.addOperation(eo);
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
	 * populate the edge addition and deletion data structures.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	public void findEdgeChanges(FastGraph g1, FastGraph g2) {
		
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
			
			Integer node1Map = reverseNodeMapping.get(g2Node1);
			Integer node2Map = reverseNodeMapping.get(g2Node2);

			boolean found = false;
			if(addNodeMapping.get(node1Map) != null || addNodeMapping.get(node2Map) != null) {
				// if either node is going to be added, we will need to add the edge
				found = false;
			} else {
				// both nodes are in g1, so check for a corresponding edge in g1
				int[] connecting = g1.getNodeConnectingEdges(node1Map);
				for(int connectingIndex = 0; connectingIndex < connecting.length; connectingIndex++) {
					int connectingE = connecting[connectingIndex];
					int connectingN = g1.oppositeEnd(connectingE, node1Map);
					if(connectingN == node2Map) {
						found = true;
						break;
					}
				}
			}

			// no edge, so add one
			if(found == false) {
				addEdgeNode1List.add(node1Map);
				addEdgeNode2List.add(node2Map);
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
		// find the edges that need adding
		
	}
	
	
}
