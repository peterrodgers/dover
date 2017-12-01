package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.NodeStructure;

public class EdgeDelete extends EdgeEditOperation {

	/**
	 * 
	 * @param cost the cost of the operation
	 * @param label the label of the new node
	 */
	public EdgeDelete(double cost) {
		super(cost);
	}

	/**
	 * Deletes the edge with id.
	 * @param g the graph to edit
	 * @return the new graph with edit operation applied, or null if the node has any connecting edges.
	 */
	@Override
	public FastGraph edit(FastGraph g, int id) {
		
		FastGraph ret = g.generateGraphByDeletingEdge(id);
		if(ret == null) {
			return null;
		}
		return null;
	}

}
