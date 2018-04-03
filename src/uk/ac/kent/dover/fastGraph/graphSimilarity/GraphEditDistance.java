package uk.ac.kent.dover.fastGraph.graphSimilarity;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;

/**
 * @author Peter Rodgers
 *
 */
public abstract class GraphEditDistance extends GraphSimilarity {

	EditList editList;


	/**
	 * use this to define editCosts in the constructor. Typically used
	 * to give all editCosts entries default values of 1.0
	 */
	public GraphEditDistance() {
		super();
	}
	

	/**
	 * Apply this after calling @see{similarity}.
	 * 
	 * @return returns the edit list after the algorithm has run
	 */
	public EditList getEditList() {return editList;}
	
	/**
	 * This returns the graph edit distance calculation between the two graphs. 
	 * Zero means the graphs are isomorphic. Greater values mean more dissimilarity. 
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the cost of the edits between two graphs.
	 */
	@Override
	public abstract double similarity(FastGraph g1, FastGraph g2);	
	

}
