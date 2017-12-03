package uk.ac.kent.dover.fastGraph.graphSimilarity;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * Base graph for all similarity measures.
 * Including Graph Edit Distance.
 * 
 * @author Peter Rodgers
 *
 */
public abstract class GraphSimilarity {
	
	boolean directed;
	

	/**
	 * defaults to treating graph as undirected.
	 */
	public GraphSimilarity() {
		super();
		this.directed = false;
	}

	/** 
	 * 
	 * @param directed Treat graph as directed if true, otherwise treat graph as undirected.
	 */
	public GraphSimilarity(boolean directed) {
		super();
		this.directed = directed;
	}


	/**
	 * This returns the difference between the two graphs. 
	 * Zero means the graphs isomorphic. Greater values mean more dissimilarity. 
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the similarity of the two graphs.
	 */
	abstract public double similarity(FastGraph g1, FastGraph g2);

}
