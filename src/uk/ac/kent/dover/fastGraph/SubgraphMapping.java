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
	
	int numberOfNodes = -1;
	int numberOfEdges = -1;

	public SubgraphMapping(int numberOfNodes, int numberOfEdges) {
		this.numberOfNodes = numberOfNodes;
		this.numberOfEdges = numberOfEdges;
		nodeMapping = new int[numberOfNodes];
		edgeMapping = new int[numberOfEdges];
	}


	/**
	 * Sets the node mapping
	 * 
	 * @param i
	 */
	public void setNodeMapping(int i[]) {
		nodeMapping = i;
	}
	
	/**
	 * Sets the edge mapping
	 * 
	 * @param i
	 */
	public void setEdgeMapping(int i[]) {
		edgeMapping = i;
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
	public int getSingleNodeMapping(int subgraphIndex) {
		return nodeMapping[subgraphIndex];
	}

	
	/**
	 * @return a single edge mapping
	 */
	public int getSingleEdgeMapping(int subgraphIndex) {
		return edgeMapping[subgraphIndex];
	}

	
	/**
	 * Adds a single node mapping.
	 * 
	 * @param subGraphIndex
	 * @param graphIndex
	 */
	public void addSingleNodeMapping(int subGraphIndex, int graphIndex) {
		nodeMapping[subGraphIndex] = graphIndex;
	}
	
	/**
	 * Adds a single edge mapping.
	 * 
	 * @param subGraphIndex
	 * @param graphIndex
	 */
	public void addSingleEdgeMapping(int subGraphIndex, int graphIndex) {
		edgeMapping[subGraphIndex] = graphIndex;
	}
	

}
