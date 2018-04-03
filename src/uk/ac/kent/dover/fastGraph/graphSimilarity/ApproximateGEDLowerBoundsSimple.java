package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;
import uk.ac.kent.dover.fastGraph.editOperation.EditOperation;

public class ApproximateGEDLowerBoundsSimple  extends GraphEditDistance {

	boolean nodeLabels;
	
	HashMap<Integer,Double> editCosts;

	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;
	
	


	/**
	 * defaults to treating graph as undirected and no node label comparison,
	 * with all edit costs at 1.0.
	 * 
	 * @throws FastGraphException should not throw this as all required edit operations are present by default
	 */
	public ApproximateGEDLowerBoundsSimple() throws FastGraphException {
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
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * Defaults to no attempted node map swaps. Does not use directed graphs, all
	 * edges are treated as undirected.
	 * 
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param editCosts are a mapping between edit codes and their costs DELETE_NODE, ADD_NODE, DELETE_EDGE, ADD_EDGE, if @see{nodeLabels} is true, RELABEL_NODE also needs to be present
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ApproximateGEDLowerBoundsSimple(boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {

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


	
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		
		int edges1 = g1.getNumberOfEdges();
		int edges2 = g2.getNumberOfEdges();
		
		int deleteNodeCount = 0;
		int addNodeCount = 0;
		
		double ret = 0.0;
		if(nodes1 > nodes2) {
			ret += (nodes1-nodes2)*deleteNodeCost;
			deleteNodeCount = nodes1-nodes2;
		} else {
			ret += (nodes2-nodes1)*addNodeCost;
			addNodeCount = nodes2-nodes1;
		}
		if(edges1 > edges2) {
			ret += (edges1-edges2)*deleteEdgeCost;
		} else {
			ret += (edges2-edges1)*addEdgeCost;
		}
		
		// count the different labels and see how many need to change
		if(nodeLabels) {
			HashMap<String,Integer> labelCount1 = new HashMap<>(nodes1*2);
			HashMap<String,Integer> labelCount2 = new HashMap<>(nodes2*2);
			HashSet<String> labels = new HashSet<>(nodes1+nodes2);
			for(int n1 = 0; n1 < nodes1; n1++) {
				String label = g1.getNodeLabel(n1);
				labels.add(label);
				Integer count = labelCount1.get(label);
				if(count == null) {
					count = 0;
				}
				count++;
				labelCount1.put(label, count);
			}
			for(int n2 = 0; n2 < nodes2; n2++) {
				String label = g2.getNodeLabel(n2);
				labels.add(label);
				Integer count = labelCount2.get(label);
				if(count == null) {
					count = 0;
				}
				count++;
				labelCount2.put(label, count);
			}
			
			double relabelCost = relabelNodeCost;
			if(relabelCost > deleteNodeCost+addNodeCost) {
				relabelCost = deleteNodeCost+addNodeCost;
			}
			
			double labelDiff = 0.0;
			
			if(deleteNodeCount > 0) {
				int relabelCount = 0;
				for(String label : labelCount1.keySet()) {
					Integer count1 = labelCount1.get(label);
					Integer count2 = labelCount2.get(label);
					if(count2 == null) {
						count2 = 0;
					}

					if(count1 > count2) {
						relabelCount += (count1-count2);
					}
					
				}
				relabelCount = relabelCount-deleteNodeCount;
				if(relabelCount < 0) {
					relabelCount = 0;
				}
				labelDiff = relabelCount*relabelCost;
			} else { // adding nodes
				int relabelCount = 0;
				for(String label : labelCount2.keySet()) {
					Integer count1 = labelCount1.get(label);
					Integer count2 = labelCount2.get(label);
					if(count1 == null) {
						count1 = 0;
					}
					if(count2 > count1) {
						relabelCount += (count2-count1);
					}
					
				}
				relabelCount = relabelCount-addNodeCount;
				if(relabelCount < 0) {
					relabelCount = 0;
				}
				labelDiff = relabelCount*relabelCost;
			}
			ret += labelDiff;
			
		}
		
		return ret;

	}

	
}

