package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;


/**
 * Finds a lower bound on GED based on Hausdorff Distance. Result is guaranteed to be below or equal to the exact value.
 * Undirected graphs only. This implementation does consider node labels (if required) but does not consider edge labels.
 * 
 * From:
 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., & Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
 *
 * @author Peter Rodgers
 *
 */
public class ApproximateGEDHausdorff extends GraphEditDistance {

	private boolean nodeLabels;
	
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;
	
	HashMap<Integer,Double> editCosts;


	
	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {

			FastGraph g1,g2;
			EditOperation eo;		
			EditList el;
			HashMap<Integer,Double> editCosts;
			ApproximateGEDHausdorff gedH;
			ApproximateGEDSimple gedS;
			
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,7.0);
			editCosts.put(EditOperation.ADD_NODE,6.0);
			editCosts.put(EditOperation.DELETE_EDGE,3.0);
			editCosts.put(EditOperation.ADD_EDGE,4.0);
			editCosts.put(EditOperation.RELABEL_NODE,5.0);
			
			el = new EditList();
			eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
			el.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
			el.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
			el.addOperation(eo);
			g1 = FastGraph.randomGraphFactory(0, 0, false);
			g1 = el.applyOperations(g1);
		
			el = new EditList();
			eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
			el.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
			el.addOperation(eo);
			eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
			el.addOperation(eo);
			g2 = FastGraph.randomGraphFactory(0, 0, false);
			g2 = el.applyOperations(g2);

			gedH = new ApproximateGEDHausdorff(true,editCosts);
			
			gedS = new ApproximateGEDSimple(false,true,editCosts);
			
			double costH = gedH.similarity(g1, g2);
			double costS = gedS.similarity(g1, g2);
System.out.println("hausdorff lower bound: "+costH);
System.out.println("simple approximation: "+costS);

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
	public ApproximateGEDHausdorff() throws FastGraphException {
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
	public ApproximateGEDHausdorff(HashMap<Integer,Double> editCosts) throws FastGraphException {
		
		super();
		this.editCosts = editCosts;
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
	public ApproximateGEDHausdorff(boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {

		super();
		this.editCosts = editCosts;
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
		
		double estimate = hausdorffEditDistance(g1, g2);
		return estimate;
		
	}
	
	
	/**
	 * Algorithm 3 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., & Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 *
	 * @param a a collection of edges (adjacent to a node) from graph 1
	 * @param b a collection edges (adjacent to a node) from graph 2
	 */
	public double hausdorffEditDistance(FastGraph g1, FastGraph g2) {
		
		double d1[] = new double[g1.getNumberOfNodes()];
		double d2[] = new double[g2.getNumberOfNodes()];

		for(int n1 = 0; n1 < g1.getNumberOfNodes(); n1++) {
			d1[n1] = deleteNodeCost;
			int[] neighbouringEdges = g1.getNodeConnectingEdges(n1);
			double edgeCost = (neighbouringEdges.length*deleteEdgeCost)/2;
			d1[n1]+= edgeCost;
System.out.println("BBBB d1[n1] "+d1[n1]);
		}

		for(int n2 = 0; n2 < g2.getNumberOfNodes(); n2++) {
			d2[n2] = addNodeCost;
			int[] neighbouringEdges = g2.getNodeConnectingEdges(n2);
			double edgeCost = (neighbouringEdges.length*addEdgeCost)/2;
			d2[n2]+= edgeCost;
System.out.println("CCCC d2[n2] "+d2[n2]);
		}
		
		if(nodeLabels) {
			for(int n1 = 0; n1 < g1.getNumberOfNodes(); n1++) {
				int[] neighbouringEdges1 = g1.getNodeConnectingEdges(n1);
				for(int n2 = 0; n2 < g2.getNumberOfNodes(); n2++) {
					int[] neighbouringEdges2 = g2.getNodeConnectingEdges(n2);
					
					double ce = hausdorffEdgeEditCost(neighbouringEdges1, neighbouringEdges2);
System.out.println("DDDD ce "+ce);
					double lowerNodeBounds = lowerNodeBounds(n1, n2, g1, g2);
					if(lowerNodeBounds > ce) {
						ce = lowerNodeBounds;
					}
					
					double param1 = (relabelNodeCost+ce/2)/2;
System.out.println("EEEE param1 "+param1);

					if(param1 < d1[n1]) {
						d1[n1] = param1;
					}
				
					if(param1 < d2[n2]) {
						d2[n2] = param1;
					}
				}
			}
		}
		
		double d = 0.0;
		for(int n1 = 0; n1 < g1.getNumberOfNodes(); n1++) {
			d += d1[n1];
		}

		for(int n2 = 0; n2 < g2.getNumberOfNodes(); n2++) {
			d += d2[n2];
		}
		
		double lowerGraphBounds = lowerGraphBounds(g1,g2);
		
		if(lowerGraphBounds > d) {
			d = lowerGraphBounds;
		}
	
		return d;
	}
		



	/**
	 * Algorithm 2 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., & Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 * As we have no edge substitution, this algorithm is simplified by missing a step.
	 *
	 * @param a a collection of edges (adjacent to a node) from graph 1
	 * @param b a collection edges (adjacent to a node) from graph 2
	 */
	public double hausdorffEdgeEditCost(int[] a, int[] b) {
			
		double[] costA = new double[a.length];
		double[] costB = new double[b.length];
		
		for(int i = 0; i < a.length; i++) {
			costA[i] = deleteEdgeCost;
		}
		for(int i = 0; i < b.length; i++) {
			costB[i] = addEdgeCost;
		}
		
		// edge relabelling costs would go here (alg lines 7 to 12)
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b.length; j++) {
				costA[i] = 0; // cost of edge relabelling is 0
				costB[j] = 0; // cost of edge relabelling is 0
			}
		}
		
		double c = 0;
		for(int i = 0; i < costA.length; i++) {
			c+= costA[i];
		}
		for(int i = 0; i < costB.length; i++) {
			c += costB[i];
		}
		
		return c;
	}

	
	/**
	 * First half of equation 12 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., & Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 * @param n1 a node in graph1
	 * @param n2 a node in graph2
	 */
	private double lowerGraphBounds(FastGraph g1, FastGraph g2) {

		int numberOfNodes1 = g1.getNumberOfNodes();
		int numberOfNodes2 = g2.getNumberOfNodes();
		
		double bounds = -1.0;
		if(numberOfNodes1 > numberOfNodes2) {
			bounds = (numberOfNodes1-numberOfNodes2)*deleteNodeCost;
		} else {
			bounds = (numberOfNodes2-numberOfNodes1)*addNodeCost;
		}
		
		return bounds;
	}

	
	/**
	 * Second half of equation 12 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., & Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 * @param n1 a node in graph1
	 * @param n2 a node in graph2
	 */
	public double lowerNodeBounds(int n1, int n2, FastGraph g1, FastGraph g2) {
		
		int[] neighbouringEdges1 = g1.getNodeConnectingEdges(n1);
		int[] neighbouringEdges2 = g2.getNodeConnectingEdges(n2);
		
		double bounds = -1.0;
		if(neighbouringEdges1.length > neighbouringEdges2.length) {
			bounds = (neighbouringEdges1.length-neighbouringEdges2.length)*deleteEdgeCost;
		} else {
			bounds = (neighbouringEdges2.length-neighbouringEdges1.length)*addEdgeCost;
		}
		
		return bounds;
	}
	
	

	
}
	
	
	