package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import com.dberm22.lapjv4j.lapjv;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;

import blogspot.software_and_algorithms.stern_library.optimization.HungarianAlgorithm;
import edu.isi.karma.modeling.research.graphmatching.algorithms.VJAccess;

/**
 * Defines a cost between all nodes in g1 to g2, then finds a minimal local matching
 * using a bipartite matching algorithm.
 * 
 * An approximation method for GED using the Hungarian Algorithm from:
 * Riesen K, Bunke H. Approximate graph edit distance computation by means of bipartite graph matching. Image and Vision computing. 2009 Jun 4;27(7):950-9.
 * doi:10.1016/j.imavis.2008.04.004
 *
 * or (faster) the Volgenant Jonker algorithm from
 * Fankhauser S, Riesen K, Bunke H. Speeding up graph edit distance computation through fast bipartite matching. Graph-based representations in pattern recognition. 2011:102-11.
 * 
 * comparison between:
 * Hungarian Algorithm from: a. https://github.com/KevinStern/software-and-algorithms used this
 * Hungarian Algorithm from: b. https://www.programcreek.com/java-api-examples/index.php?source_dir=JContextExplorer-master/JContextExplorer/src/operonClustering/HungarianAlgorithm.java
 * Volgenant Jonker Algorithm from c. https://github.com/usc-isi-i2/szeke/blob/master/src/main/java/edu/isi/karma/modeling/research/graphmatching/algorithms/VolgenantJonker.java
 * Volgenant Jonker Algorithm from d. https://github.com/dberm22/Bipartite-Solver
 * and all seem to return the same matching on random matrices,
 * Speed testing gives: c fastest, then a and d similar, with b significantly slower.
 * E.g. time for 6000 nodes: a: 9.6 secs, b: no termination in reasonable time, c: 2.9, d: 10.2
 * 
 * @author Peter Rodgers
 *
 */
public class ApproximateGEDBipartite extends GraphEditDistance {

	private boolean nodeLabels;
	private boolean directed;
	
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;

	HashMap<Integer,Double> editCosts;
	
	private long approximationTime;
	
	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {

			ApproximateGEDBipartite AGED = new ApproximateGEDBipartite();
			AGED.test();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


	private void test() {
long start;

		int size = 1000;
		double[][] costMatrix = new double[size][size];
//		Random r = new Random(996322L);
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0; i < costMatrix.length; i++) {
			for(int j = 0; j < costMatrix.length; j++) {
				costMatrix[i][j] = r.nextDouble();
			}
		}
		
start = System.currentTimeMillis();
		VJAccess vja = new VJAccess();
		vja.computeAssignment(costMatrix);
		int[] retVJ = vja.getAssignment();
System.out.println("VJ time "+(System.currentTimeMillis()-start)/1000.0);

start = System.currentTimeMillis();
		HungarianAlgorithm ha = new HungarianAlgorithm(costMatrix);
		int[] retH = ha.execute();
System.out.println("Hungarian time "+(System.currentTimeMillis()-start)/1000.0);

		for(int i = 0; i< size; i++) {
//			System.out.println("i "+retH[i]+" "+retVJ[i]);
			if(retH[i] != retVJ[i]) {
				System.out.println("MISMATCH "+i+" : "+retH[i]+" "+retVJ[i]);
			}
		}
		
	}




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
	 * @param editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd
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
		
		int size = g1.getNumberOfNodes();
		int deleteNodeStart = g2.getNumberOfNodes(); // matrix has deleted nodes on left
		int addNodeStart = Integer.MAX_VALUE; // matrix has no added nodes on bottom
		if(size < g2.getNumberOfNodes()) {
			size = g2.getNumberOfNodes();
			deleteNodeStart = Integer.MAX_VALUE; // matrix has no deleted nodes on left
			addNodeStart = g1.getNumberOfNodes(); // matrix has added nodes on bottom
		}
		
		double[][] costMatrix = new double[size][size];
		
		for(int y = 0; y < size; y++) {
			for(int x = 0; x < size; x++) {
				if(x >= deleteNodeStart) { // left hand side of the matrix
					if(deleteNodeStart-x == y) { // cost of a node deletion appears on diagonal
						costMatrix[x][y] = deleteNodeCost;
					} else {
						costMatrix[x][y] = Double.MAX_VALUE;
					}
				} else if(y >= addNodeStart) { // bottom of the matrix
					if(deleteNodeStart-y == x) { // cost of a node addition appears on diagonal
						costMatrix[x][y] = addNodeCost;
					} else {
						costMatrix[x][y] = Double.MAX_VALUE;
					}
				} else { // filling in the top right
					costMatrix[x][y] = singleNodeEditCost(x,y,g1,g2);
				}
			}
		}
System.out.println(formatMatrix(costMatrix));		
		
		EditList editList = new EditList();
		
		approximationTime = startTime-System.currentTimeMillis();
		
		// form the edit list
		
		return editList.getCost();
	

	}
	
	/**
	 * Find the smallest cost to turn n1 into n2.
	 *
	 * @param n1 the node in g1
	 * @param n2 the node in g2
	 * @param g1 the graph for n1
	 * @param f2 the graph for n2
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
			if(degree1 > degree2) {
				ret += (degree2-degree1)*addNodeCost;
			} else {
				ret += (degree1-degree2)*deleteNodeCost;
			}
		} else {
			int inDegree1 = g1.getNodeInDegree(n1);
			int inDegree2 = g2.getNodeInDegree(n2);
			if(inDegree1 > inDegree2) {
				ret += (inDegree2-inDegree1)*addNodeCost;
			} else {
				ret += (inDegree1-inDegree2)*deleteNodeCost;
			}
			int outDegree1 = g1.getNodeOutDegree(n1);
			int outDegree2 = g2.getNodeOutDegree(n2);
			if(outDegree1 > outDegree2) {
				ret += (outDegree2-outDegree1)*addNodeCost;
			} else {
				ret += (outDegree1-outDegree2)*deleteNodeCost;
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
	protected String formatMatrix(double[][] matrix) {
		String ret = "";
		for(int y = 0; y < matrix.length; y++) {
			for(int x = 0; x < matrix.length; x++) {
				ret += matrix[x][y]+"\t";
			}
			ret += "\n";
		}
		return ret;

	}
	
	
}
	
	
	