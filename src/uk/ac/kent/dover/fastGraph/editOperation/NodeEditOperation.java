package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * 
 * Superclass for node edit operations.
 * 
 * @author pjr
 *
 */
public abstract class NodeEditOperation extends EditOperation {

	/**
	 * 
	 * @param cost the cost of the node operation
	 * @param the label, leave null if not required for the operation
	 */
	public NodeEditOperation(double cost) {
		super(cost);
	}

	/**
	 * Edit the node with id.
	 * @param g the graph to edit
	 * @param id the id of the node to edit
	 * @return the new graph with edit operation applied, or null if the edit fails.
	 */
	@Override
	public abstract FastGraph edit(FastGraph g, int id);

}
