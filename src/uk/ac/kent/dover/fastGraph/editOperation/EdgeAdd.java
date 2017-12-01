package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.EdgeStructure;

public class EdgeAdd extends EdgeEditOperation {

	EdgeStructure es;
	
	/**
	 * 
	 * @param cost the cost of the operation
	 * @param label the label of the new node
	 * @param n1 the source node
	 * @param n2 the destination node
	 */
	public EdgeAdd(double cost, int n1, int n2) {
		super(cost);
		es = new EdgeStructure(-1,"",0,(byte)0,(byte)0,n1,n2);
	}

	/**
	 * Adds a edge with source node n1, target node n2, disregards id.
	 * @param g the graph to edit
	 * @param id disregarded
	 * @return the new graph with edit operation applied, or null if the node has any connecting edges.
	 * @throws Exception Throws if the new FastGraph does not build correctly. Most likely out of memory error.
	 */
	@Override
	public FastGraph edit(FastGraph g, int id) {
		
		FastGraph ret = null;
		try {
			ret = g.generateGraphByAddingEdge(es);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ret == null) {
			return null;
		}
		return null;
	}

}
