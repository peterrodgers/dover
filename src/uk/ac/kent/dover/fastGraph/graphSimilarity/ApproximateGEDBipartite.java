package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;

import blogspot.software_and_algorithms.stern_library.optimization.HungarianAlgorithm;
import edu.isi.karma.modeling.research.graphmatching.algorithms.VJAccess;

/**
 * Defines a cost between all nodes in g1 to g2, then finds a minimal local matching
 * using a bipartite matching algorithm.
 * 
 * An approximation method for GED using the Volgenant Jonker algorithm from
 * Fankhauser S, Riesen K, Bunke H. Speeding up graph edit distance computation through fast bipartite matching. Graph-based representations in pattern recognition. 2011:102-11.
 * 
 * Or (Slower) the Hungarian Algorithm from:
 * Riesen K, Bunke H. Approximate graph edit distance computation by means of bipartite graph matching. Image and Vision computing. 2009 Jun 4;27(7):950-9.
 * doi:10.1016/j.imavis.2008.04.004
 *
 * @author Peter Rodgers
 *
 */
public class ApproximateGEDBipartite extends GraphEditDistance {

	private boolean nodeLabels;
	private boolean directed;
	
	private boolean useHungarian = false; // keep this at false, hungarian is slower and less accurate
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;

	HashMap<Integer,Double> editCosts;
	
	private double[][] costMatrix;
	private int[] mapping;

	
	private long approximationTime;
	
	

	/**
	 * defaults to treating graph as undirected and no node label comparison,
	 * with all edit costs at 1.0.
	 * 
	 * @throws FastGraphException should not throw this as all required edit operations are present by default
	 */
	public ApproximateGEDBipartite() throws FastGraphException {
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
	 * @param editCosts should include nodeDelete, nodeAdd, edgeDelete and edgeAdd
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ApproximateGEDBipartite(HashMap<Integer,Double> editCosts) throws FastGraphException {
		
		super();
		this.editCosts = editCosts;
		this.directed = false;
		this.nodeLabels = false;
		
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
	public ApproximateGEDBipartite(boolean directed, boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {

		super();
		this.editCosts = editCosts;
		this.directed = directed;
		this.nodeLabels = nodeLabels;
		
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
	}

	/**
	 * 
	 * @return how long the approximation took.
	 */
	public long getApproximationTime() {return approximationTime;}
	
	/**
	 * 
	 * @return the costMatrix, set after similarity is called.
	 */
	public double[][] getCostMatrix() {return costMatrix;}
	
	/**
	 * 
	 * @return the g1 nodes to g2 nodes mapping, set after similarity is called.
	 */
	public int[] getMapping() {return mapping;}
	
	/**
	 * Returns the current algorithm used to find bipartite matching
	 * 
	 * @return true if current algorithm is Hungarian, false if it is Volgenant Jonker
	 */
	public boolean getUseHungarian() {
		return useHungarian;
	}

	/**
	 * Set the algorithm for bipartite matching, defaults to false.
	 * Keep on false as the Hungarian algorithm is 
	 * slower than Volgenant Jonker,
	 * so there is no reason beyond testing to set this to true.
	 * 
	 * @param useHungarian true for Hungarian algorithm, false for Volgenant Jonker
	 */
	public void setUseHungarian(boolean useHungarian) {
		this.useHungarian = useHungarian;
	}
	

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
		
		long startTime = System.currentTimeMillis();
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		int size = nodes1+nodes2;
		
		costMatrix = new double[size][size];
		
		for(int y = 0; y < size; y++) { // g1 nodes
			for(int x = 0; x < size; x++) { // g2 nodes
				if(x < nodes2 && y < nodes1) { // top left of the matrix
					costMatrix[y][x] = singleNodeEditCost(y,x,g1,g2);
				} else if(x >= nodes2 && y < nodes1) { // top right of the matrix
					if(x-nodes2 == y) { // cost of a node deletion appears on diagonal
						costMatrix[y][x] = singleNodeDeleteCost(y, g1);
					} else {
						costMatrix[y][x] = Double.MAX_VALUE;
					}
				} else if(x < nodes2 && y >= nodes1) { // bottom left of the matrix
					if(y-nodes1 == x) { // cost of a node addition appears on diagonal
						costMatrix[y][x] = singleNodeAddCost(x, g2);
					} else {
						costMatrix[y][x] = Double.MAX_VALUE;
					}
				} else { // bottom right
					costMatrix[y][x] = 0.0;
				}
			}
		}
		
		mapping = null;

		if(useHungarian) {
			HungarianAlgorithm ha = new HungarianAlgorithm(costMatrix);
			mapping = ha.execute();
		} else {
			VJAccess vja = new VJAccess();
			vja.computeAssignment(costMatrix);
			mapping = vja.getAssignment();
		}

		editList = findEditListFromMapping(directed, nodeLabels, g1, g2, mapping, editCosts);
		
		approximationTime = startTime-System.currentTimeMillis();
		
		return editList.getCost();

	}

	/**
	 * Takes a mapping from g1 nodes to g2 nodes that includes node to node, node to delete, add to node.
	 * Mapping from g1 nodes to values greater than the number of g2 nodes are delete.
	 * Mapping from values greater the the number of g1 nodes to g2 nodes are add.
	 * Edge addition and deletion can be inferred from edges between mapped nodes
	 * 
	 * @param directed true if the graph should be treated as directed, false if undirected
	 * @param nodeLabels true if the nodeLabels are considered, false if they are ignored
	 * @param g1 the graph the mapping is from
	 * @param g2 the graph the mapping is to
	 * @param mapping the mapping
	 * @param editCosts the costs of each edit
	 * @return an edit list that uses the mapping to make g1 into g2
	 */
	public static EditList findEditListFromMapping(boolean directed, boolean nodeLabels, FastGraph g1, FastGraph g2,int[] mapping, HashMap<Integer,Double> editCosts) {
		EditList el = new EditList();
		
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		
		int[] nodeMapping = new int[nodes1]; // if -1 then deleted node
		int[] reverseNodeMapping = new int[nodes2]; // points at both existing and added nodes
		
		
		int n1Count = nodes1; // this is used to assign added node indexes, so starts at the end of the node1 ids
		for(int n1=0; n1 < mapping.length; n1++) {
			int n2 = mapping[n1];
			if(n1 < nodes1 && n2 < nodes2) { // just a normal map (top left of matrix)
				if(nodeLabels) {
					String label1 = g1.getNodeLabel(n1);
					String label2 = g2.getNodeLabel(n2);
					if(!label1.equals(label2)) {
						EditOperation eo = new EditOperation(EditOperation.RELABEL_NODE,editCosts.get(EditOperation.RELABEL_NODE),n1,label2,-1,-1);
						el.addOperation(eo);
					}
				}
				nodeMapping[n1] = n2;
				reverseNodeMapping[n2] = n1;

			} else if (n1 < nodes1 && n2 >= nodes2) { // a node deletion (top right of matrix)
				EditOperation eo = new EditOperation(EditOperation.DELETE_NODE,editCosts.get(EditOperation.DELETE_NODE),n1,null,-1,-1);
				el.addOperation(eo);
				
				nodeMapping[n1] = -1;
			} else if (n1 >= nodes1 && n2 < nodes2) { // a node addition (bottom left of matrix)
				
				String label = g2.getNodeLabel(n2);
				EditOperation eo = new EditOperation(EditOperation.ADD_NODE,editCosts.get(EditOperation.ADD_NODE),0-n1Count,label,-1,-1); // 0-n1Count puts the nodes in the right order
				el.addOperation(eo);
				
				reverseNodeMapping[n2] = n1Count;
				
				n1Count++;
			} else { // indexes beyond nodes1 map to indexes beyond nodes2 (bottom right of matrix), so ignore
			}
		}
		
		// sort the edges
		
		HashMap<String,ArrayList<Integer>> g1EdgeIdMap = new HashMap<>(g1.getNumberOfEdges()*2);
		for(int e=0; e < g1.getNumberOfEdges(); e++) {
			int nodeA = g1.getEdgeNode1(e);
			int nodeB = g1.getEdgeNode2(e);
			if(!directed) { // in this case both directions are the same edge
				if(nodeA > nodeB) {
					nodeB = g1.getEdgeNode1(e);
					nodeA = g1.getEdgeNode2(e);
				}
			}
			String edgeString = nodeA+"-"+nodeB;
			ArrayList<Integer> edges = g1EdgeIdMap.get(edgeString);
			if(edges == null) {
				edges = new ArrayList<>();
			}
			edges.add(e);
			g1EdgeIdMap.put(edgeString, edges);
		}
		
		
		HashMap<String,ArrayList<Integer>> g2EdgeIdMap = new HashMap<>(g2.getNumberOfEdges()*2);
		for(int e=0; e < g2.getNumberOfEdges(); e++) {
			int nodeA = g2.getEdgeNode1(e);
			int nodeB = g2.getEdgeNode2(e);
			if(!directed) { // in this case both directions are the same edge
				if(nodeA > nodeB) {
					nodeB = g2.getEdgeNode1(e);
					nodeA = g2.getEdgeNode2(e);
				}
			}
			String edgeString = nodeA+"-"+nodeB;
			ArrayList<Integer> edges = g2EdgeIdMap.get(edgeString);
			if(edges == null) {
				edges = new ArrayList<>();
			}
			edges.add(e);
			g2EdgeIdMap.put(edgeString, edges);
		}
		
		// find the difference between g1 edges and g2 edges
		for(String e1String : g1EdgeIdMap.keySet()) {
			ArrayList<Integer> e1Ids = g1EdgeIdMap.get(e1String);
			int e1Count = e1Ids.size();
			String[] split1 = e1String.split("-");
			int e1NodeA = Integer.parseInt(split1[0]);
			int e1NodeB = Integer.parseInt(split1[1]);
			
			int e2NodeA = nodeMapping[e1NodeA];
			int e2NodeB = nodeMapping[e1NodeB];
			if(!directed) { // direction not important, so order the nodes by id
				if(e2NodeA > e2NodeB) {
					e2NodeB = nodeMapping[e1NodeA];
					e2NodeA = nodeMapping[e1NodeB];
				}
			}
			
			String e2String = e2NodeA+"-"+e2NodeB;
			ArrayList<Integer> e2Ids = g2EdgeIdMap.get(e2String);
			g2EdgeIdMap.remove(e2String);
			int e2Count = 0;
			if(e2Ids != null) {
				e2Count = e2Ids.size();
			}
			
			if(e1Count > e2Count) {
				for(int i = 0; i < e1Count-e2Count; i++) {
					int deleteId = e1Ids.get(i);
					EditOperation eo = new EditOperation(EditOperation.DELETE_EDGE,editCosts.get(EditOperation.DELETE_EDGE),deleteId,null,-1,-1);
					el.addOperation(eo);
				}
			}
			if(e1Count < e2Count) {
				for(int i = 0; i < e2Count-e1Count; i++) {
					int e2Id = e2Ids.get(i);
					String label = g2.getEdgeLabel(e2Id);
					EditOperation eo = new EditOperation(EditOperation.ADD_EDGE,editCosts.get(EditOperation.ADD_EDGE),-1,label,e1NodeA,e1NodeB);
					el.addOperation(eo);
				}
			}
		}
		
		// add remaining g2 edges, these are edges between nodes in g2 for which
		// there is no corresponding connection in g1
		for(String e2String: g2EdgeIdMap.keySet()) {
			
			ArrayList<Integer> e2Ids = g2EdgeIdMap.get(e2String);
			int e2Count = e2Ids.size();

			String[] split2 = e2String.split("-");
			int e2NodeA = Integer.parseInt(split2[0]);
			int e2NodeB = Integer.parseInt(split2[1]);
			int e1NodeA = reverseNodeMapping[e2NodeA];
			int e1NodeB = reverseNodeMapping[e2NodeB];

			for(int i = 0; i < e2Count; i++) {
				int e2Id = e2Ids.get(i);
				String label = g2.getEdgeLabel(e2Id);

				EditOperation eo = new EditOperation(EditOperation.ADD_EDGE,editCosts.get(EditOperation.ADD_EDGE),-1,label,e1NodeA,e1NodeB);
				el.addOperation(eo);
			}
		}
		
		// sort the EditOperations by largest Id first
		el.sort();

		return el;
		
	}



	/**
	 * Find the cost to delete n from g.
	 *
	 * @param n the node
	 * @param g the graph containing n
	 * @return the total cost
	 */
	private double singleNodeDeleteCost(int n, FastGraph g) {
		double ret = (deleteEdgeCost*g.getNodeDegree(n));
		ret+=deleteNodeCost;
		return ret;
	}
	
	/**
	 * Find the cost to add a node like n.
	 *
	 * @param n the node
	 * @param g the graph containing n
	 * @return the total cost
	 */
	private double singleNodeAddCost(int n, FastGraph g) {
		double ret = (addEdgeCost*g.getNodeDegree(n));
		ret+=deleteNodeCost;
		return ret;
	}

	
	/**
	 * Find the smallest cost to turn n1 into n2.
	 *
	 * @param n1 the node in g1
	 * @param n2 the node in g2
	 * @param g1 the graph for n1
	 * @param g2 the graph for n2
	 * @return the total cost
	 */
	private double singleNodeEditCost(int n1, int n2, FastGraph g1, FastGraph g2) {
		double ret = 0.0;
		
		if(nodeLabels) {
			String n1Label = g1.getNodeLabel(n1);
			String n2Label = g2.getNodeLabel(n2);
			if(!n1Label.equals(n2Label)) {
				ret += relabelNodeCost;
			}
		}
		
		if(!directed) {
			int degree1 = g1.getNodeDegree(n1);
			int degree2 = g2.getNodeDegree(n2);
			
			if(degree1 < degree2) {
				ret += (degree2-degree1)*addEdgeCost;
			} else {
				ret += (degree1-degree2)*deleteEdgeCost;
			}
		} else {
			int inDegree1 = g1.getNodeInDegree(n1);
			int inDegree2 = g2.getNodeInDegree(n2);
			if(inDegree1 < inDegree2) {
				ret += (inDegree2-inDegree1)*addEdgeCost;
			} else {
				ret += (inDegree1-inDegree2)*deleteEdgeCost;
			}
			int outDegree1 = g1.getNodeOutDegree(n1);
			int outDegree2 = g2.getNodeOutDegree(n2);
			if(outDegree1 < outDegree2) {
				ret += (outDegree2-outDegree1)*addEdgeCost;
			} else {
				ret += (outDegree1-outDegree2)*deleteEdgeCost;
			}
		}

		return ret;
	}
	
	

	/**
	 * output matrix in readable format for debugging. Must be a square matrix
	 * 
	 * @param matrix the matrix to format
	 * @return a string with a formatted matrix
	 */
	public static String formatMatrix(double[][] matrix) {
		String ret = "";
		for(int y = 0; y < matrix.length; y++) {
			for(int x = 0; x < matrix.length; x++) {
				ret += matrix[y][x]+"\t";
			}
			ret += "\n";
		}
		return ret;

	}
	
	
}
	
	
	