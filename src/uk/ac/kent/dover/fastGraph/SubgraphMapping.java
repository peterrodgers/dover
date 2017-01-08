package uk.ac.kent.dover.fastGraph;


/**
 * For mapping a graph to a subgraph of a larger graph
 * The nodeMapping and edgeMapping indexes are those of the smaller graph
 * The contents are the corresponding nodes and edges in the larger graph
 * 
 * @author Peter Rodgers
 */
public class SubgraphMapping {
	
	private int[] nodeMapping = null;
	private int[] edgeMapping = null;
	
	FastGraph targetGraph;
	FastGraph patternGraph;
	
	public SubgraphMapping(FastGraph targetGraph, FastGraph patternGraph, int[] nodeMapping, int[] edgeMapping) {
		this.targetGraph = targetGraph;
		this.patternGraph = patternGraph;
		this.nodeMapping = nodeMapping.clone();
		this.edgeMapping = edgeMapping.clone();
	}



	/**
	 * @return the node mapping
	 */
	public int[] getNodeMapping() {
		return nodeMapping;
	}

	/**
	 * @return the edge mapping
	 */
	public int[] getEdgeMapping() {
		return edgeMapping;
	}

	
	/**
	 * @return a single node mapping
	 */
	public int getSingleNodeMapping(int patternGraphIndex) {
		return nodeMapping[patternGraphIndex];
	}

	
	/**
	 * @return a single edge mapping
	 */
	public int getSingleEdgeMapping(int patternGraphIndex) {
		return edgeMapping[patternGraphIndex];
	}

	
	

}
