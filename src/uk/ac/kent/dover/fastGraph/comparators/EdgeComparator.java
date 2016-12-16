package uk.ac.kent.dover.fastGraph.comparators;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;


/**
 * Implement compare(e1,e2) to return if e1 in g1 is less, equal or greater than e2 in g2.
 * Negative for e1 less than e2, 0 for e1 equal to e2, positive for e1 greater than e2
 * 
 * @author Peter Rodgers
 */
public abstract class EdgeComparator implements Comparator<Integer> {

	FastGraph g1;
	FastGraph g2;

	
	/**
	 * Constructor, the two FastGraphs must be included.
	 * 
	 * @param g1 the first FastGraph, containing e1 in compare(e1,e2)
	 * @param g2 the second FastGraph, containing e2 in compare(e1,e2)
	 */
	public EdgeComparator(FastGraph g1, FastGraph g2) {
		this.g1 = g1;
		this.g2 = g2;
	}
	
	

}
