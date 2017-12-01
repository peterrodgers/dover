package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * An edit operation on a graph. For example node addition, deletion. The intention
 * is that where possible, a single instance of subclasses is instantiated, and that all edit
 * operations of that kind are made with the instance.
 * 
 * @author pjr
 *
 */
public abstract class EditOperation {

	/** The cost of the operation */
	private double cost;

	/**
	 * 
	 * @param cost the cost of the operation
	 * @param the label, leave null if not required for the operation
	 */
	public EditOperation(double cost) {
		super();
		this.cost = cost;
	}
	

	/**
	 * 
	 * @return the cost of the operation
	 */
	public double getCost() { return cost;}


	/**
	 * An edit operation on a graph. Wether the id is node or edge needs to be specified in the sub class
	 * 
	 * @param g the graph to edit
	 * @param id the id of the item to edit
	 * @return the new graph with edit operation applied, or null if the edit fails.
	 */
	public abstract FastGraph edit(FastGraph g, int id);


}
