package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.NodeDegreeComparator;
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
	
	public static void main(String [] args) {
		
		try {
			FastGraph g1,g2;
			EditOperation eo;		
			EditList el1,el2;
			FastGraph g;
			HashMap<Integer,Double> editCosts;
			ApproximateGEDSimple ged;
			
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,2.0);
			editCosts.put(EditOperation.ADD_NODE,2.0);
			editCosts.put(EditOperation.DELETE_EDGE,2.0);
			editCosts.put(EditOperation.ADD_EDGE,2.0);
			editCosts.put(EditOperation.RELABEL_NODE,2.0);
			
			el1 = new EditList();
			eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
			el1.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 1",-1,-1);
			el1.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 0",1,0);
			el1.addOperation(eo);
			g1 = FastGraph.randomGraphFactory(0, 0, false);
			g1 = el1.applyOperations(g1);
		
			el2 = new EditList();
			eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
			el2.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 1",-1,-1);
			el2.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 2",-1,-1);
			el2.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 0",1,2);
			el2.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 1",1,2);
			el2.addOperation(eo);
			g2 = FastGraph.randomGraphFactory(0, 0, false);
			g2 = el2.applyOperations(g2);

			ged = new ApproximateGEDSimple(editCosts);
			
			double res = ged.similarity(g1, g2);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		init();
	}

	/**
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * 
	 * @param directed true if the graph is treated as directed, false if undirected
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param editCosts are a mapping between edit codes and their costs DELETE_NODE, ADD_NODE, DELETE_EDGE, ADD_EDGE, if @see{nodeLabels} is true, RELABEL_NODE also needs to be present
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ApproximateGEDSimple(boolean directed, boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {
		super(directed,editCosts);
		this.nodeLabels = nodeLabels;
		init();
	}

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
		
	}
	
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
System.out.println("n1List "+n1List);
System.out.println("n2List "+n2List);

		// create initial mapping, and either deleted or added arrays
		// depending on the number of nodes in g1 and g2
		while(true) {
			if(n1List.size() == 0) { // run out of g1 nodes, so put the rest of g2 in the add nodes list
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

System.out.println("g1 to g2 node mapping "+nodeMapping);
System.out.println("g2 to g1 node mapping "+reverseNodeMapping);
System.out.println("add node mapping "+addNodeMapping);
System.out.println("delete node list "+deleteNodes);

		findEdgeChanges(g1,g2);
		
		// find new mapping and check cost loop 
		
		
		editList = new EditList();
		// create edit list
		// remove extra nodes
		// add needed nodes
		return editList.getCost();
		
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
			if(deleteNodes.contains(node1) || deleteNodes.contains(node1)) {
				deleteEdges.add(e);
				continue;
			}
			
			// check for a corresponding edge in g2
			Integer node1Map = nodeMapping.get(node1);
			Integer node2Map = nodeMapping.get(node2);
			int[] connecting = g2.getNodeConnectingEdges(node1Map);
			boolean found = false;
			for(int connectingE = 0; connectingE < connecting.length; connectingE++) {
				int connectingN = g2.oppositeEnd(connectingE, node1Map);
				if(connectingN == node2Map) {
					found = true;
					break;
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
			
			// check for a corresponding edge in g1
			int[] connecting = g1.getNodeConnectingEdges(node1Map);
			boolean found = false;
			for(int connectingE = 0; connectingE < connecting.length; connectingE++) {
				int connectingN = g1.oppositeEnd(connectingE, node1Map);
//System.out.println("connectingE "+connectingE+"oppNode "+connectingN);
				if(connectingN == node2Map) {
					found = true;
					break;
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

			// we know there is an edge in g1. 
			// test to see if there are less in g2, and delete the excess
			int count = 0;
			int[] connecting = g2.getNodeConnectingEdges(node1Map);
			for(int connectingE = 0; connectingE < connecting.length; connectingE++) {
				int connectingN = g2.oppositeEnd(connectingE, node1Map);
				if(connectingN == node2Map) {
					count++;
				}
			}
System.out.println("count "+" edges.size() "+edges.size());
			// if there are too many edges in g1, add some to the remove list 
			while(count < edges.size()) {
				Integer removeEdge = edges.pop();
				deleteEdges.add(removeEdge);
			}
			
			// if there are too few edges in g1, add some to the add list
			if(count > edges.size())
			for(int i = 0; i < (count - edges.size());i++) {
				addEdgeNode1List.push(node1);
				addEdgeNode2List.push(node2);
			}

		}
System.out.println("deleteEdges "+deleteEdges);	
System.out.println("addEdgeNode1List "+addEdgeNode1List);	
System.out.println("addEdgeNode2List "+addEdgeNode2List);	
		// find the edges that need adding
		
	}
	
	
}
