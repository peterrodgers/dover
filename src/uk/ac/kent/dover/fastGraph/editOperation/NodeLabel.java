package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;

public class NodeLabel extends NodeEditOperation {
	
	String label;

	public NodeLabel(double cost,String label) {
		super(cost);
		this.label = label;
	}

	/**
	 * Changes the label of node with id.
	 * @param g the graph to edit
	 * @param id the id of the node to edit
	 * @return the new graph with edit operation applied, or null if the node has any connecting edges.
	 */
	@Override
	public FastGraph edit(FastGraph g, int id) {
		FastGraph ret = null;
		try {
			ret = g.generateGraphByRelabellingNode(id,label);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ret == null) {
			return null;
		}
		return null;
	}

}
