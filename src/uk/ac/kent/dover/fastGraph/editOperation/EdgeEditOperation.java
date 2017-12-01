package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * 
 * Superclass for edge edit operations.
 * 
 * @author pjr
 *
 */
public abstract class EdgeEditOperation extends EditOperation {

	/**
	 * 
	 * @param cost the cost of the edge operation
	 * @param the label, leave null if not required for the operation
	 */
	public EdgeEditOperation(double cost) {
		super(cost);
	}

	/**
	 * Edit the edge with id.
	 * @param g the graph to edit
	 * @param id the id of the edge to edit
	 * @return the new graph with edit operation applied, or null if the edit fails.
	 */
	@Override
	public abstract FastGraph edit(FastGraph g, int id);

}
