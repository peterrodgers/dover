package uk.ac.kent.dover.fastGraph.graphSimilarity;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * Base graph for all similarity measures.
 * Including Graph Edit Distance.
 * 
 * @author pjr
 *
 */
public abstract class GraphSimilarity {
	
	boolean directed;
	
	
	public GraphSimilarity(boolean directed) {
		super();
		this.directed = directed;
	}


	/**
	 * This returns the difference between the two graphs. 
	 * Zero means the graphs are the same. Greater values mean more dissimilarity. 
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the similarity of the two graphs.
	 */
	abstract public double similarity(FastGraph g1, FastGraph g2);

}
