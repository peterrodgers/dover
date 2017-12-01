package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;

public class NodeDelete extends NodeEditOperation {

	/**
	 * 
	 * @param cost the cost of the operation
	 */
	public NodeDelete(double cost) {
		super(cost);
	}

	/**
	 * Deletes the node with id.
	 * @param g the graph to edit
	 * @param id the id of the node to edit
	 * @return the new graph with edit operation applied, or null if the node has any connecting edges.
	 */
	@Override
	public FastGraph edit(FastGraph g, int id) {
		FastGraph ret = g.generateGraphByDeletingSingletonNode(id);
		if(ret == null) {
			return null;
		}
		return null;
	}

}
