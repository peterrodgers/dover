package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;


/**
 * Finds a lower bound on GED based on Hausdorff Distance. Result is guaranteed to be below or equal to the exact value.
 * Undirected graphs only. This implementation does consider node labels (if required) but does not consider edge labels.
 * 
 * From:
 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., and Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343.
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
		double ret = hausdorffDistance(g1,g2);
		return ret;
	}
	

	
	/**
	 * Algorithm 3 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., and Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 *
	 * @param g1 the first graph to be compared
	 * @param g2 the seconod graph to be compared
	 * @return the Hausdorff distance between the two graphs
	 */
	public double hausdorffDistance(FastGraph g1, FastGraph g2) {
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		
		double d1[] = new double[nodes1];
		double d2[] = new double[nodes2];
		
		for(int n1 = 0; n1 < nodes1; n1++) {
			d1[n1] = deleteNodeCost;
			int[] connectingEdges = g1.getNodeConnectingEdges(n1);
			for(int e = 0; e < connectingEdges.length; e++) {
				d1[n1] += deleteEdgeCost/2;
			}
		}

		for(int n2 = 0; n2 < nodes2; n2++) {
			d2[n2] = addNodeCost;
			int[] connectingEdges = g2.getNodeConnectingEdges(n2);
			for(int e = 0; e < connectingEdges.length; e++) {
				d2[n2] += addEdgeCost/2;
			}
		}
		
		double relabelCost = relabelNodeCost;
		if(!nodeLabels) {
			relabelCost = 0.0;
		}
		
		for(int n1 = 0; n1 < nodes1; n1++) {
			for(int n2 = 0; n2 < nodes2; n2++) {
				
				// no need for the edge costs
				double ce = lowerNodeBound(n1, n2, g1, g2);

				double subsitutionCost = relabelCost;
				if(g1.getNodeLabel(n1).equals(g2.getNodeLabel(n2))) {
					subsitutionCost = 0.0;
				}
				double param = (subsitutionCost+ce/2)/2;

				if(d1[n1] > param) {
					d1[n1] = param;
				}
				if(d2[n2] > param) {
					d2[n2] = param;
				}
				
			}
		}
		

		double d = 0.0;
		for(int n1 = 0; n1 < nodes1; n1++) {
			d += d1[n1];
		}
		for(int n2 = 0; n2 < nodes2; n2++) {
			d += d2[n2];
		}
		
		double lowerGraphBound = lowerGraphBound(g1, g2);
		if(lowerGraphBound > d) {
			d= lowerGraphBound;
		}
	
		return d;
	}
	

	
	/**
	 * First half of equation 12 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., and Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343. * 
	 *
	 * @param g1 the first graph
	 * @param g2 the second graph
	 * @return the lower bound
	 */
	public double lowerGraphBound(FastGraph g1, FastGraph g2) {
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		
		double ret = 0.0;
		if(nodes1 > nodes2) {
			ret = (nodes1-nodes2)*deleteNodeCost;
		} else {
			ret = (nodes2-nodes1)*addNodeCost;
		}
		
		return ret;

	}


	
	/**
	 * Second half of equation 12 from 
	 * Fischer, A., Suen, C. Y., Frinken, V., Riesen, K., and Bunke, H. (2015). Approximation of graph edit distance based on Hausdorff matching. Pattern Recognition, 48(2), 331-343.
	 *
	 * @param n1 a node in graph1
	 * @param n2 a node in graph2
	 * @param g1 the graph for n1
	 * @param g2 the graph for n2
	 * @return the lower bound
	 */
	public double lowerNodeBound(int n1, int n2, FastGraph g1, FastGraph g2) {
		int[] connectingNodes1 = g1.getNodeConnectingNodes(n1);
		int[] connectingNodes2 = g2.getNodeConnectingNodes(n2);

		double ret = 0.0;
		if(connectingNodes1.length > connectingNodes2.length) {
			ret = (connectingNodes1.length-connectingNodes2.length)*deleteEdgeCost;
		} else {
			ret = (connectingNodes2.length-connectingNodes1.length)*addEdgeCost;
		}
		
		return ret;

	}
	

	
}
