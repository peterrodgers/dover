package uk.ac.kent.dover.fastGraph.editOperation;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.NodeStructure;

public class NodeAdd extends NodeEditOperation {

	NodeStructure ns;
	
	/**
	 * 
	 * @param cost the cost of the operation
	 * @param label the label of the new node
	 */
	public NodeAdd(double cost, String label) {
		super(cost);
		ns = new NodeStructure(-1,label,0,(byte)0,(byte)0);
	}

	/**
	 * Adds a node with label l, disregards id.
	 * @param g the graph to edit
	 * @param id disregarded
	 * @return the new graph with edit operation applied, or null if the node has any connecting edges.
	 */
	@Override
	public FastGraph edit(FastGraph g, int id) {
		
		FastGraph ret = g.generateGraphByAddingNode(ns);
		if(ret == null) {
			return null;
		}
		return null;
	}

}
