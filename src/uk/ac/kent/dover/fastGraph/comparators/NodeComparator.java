package uk.ac.kent.dover.fastGraph.comparators;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;


/**
 * Implement compare(n1,n2) return if n1 in g1 is less, equal or greater than n2 in g2.
 * Negative for n1 less than n2, 0 for n1 equal to n2, positive for n1 greater than n2
 * 
 *  * @author Peter Rodgers
 */
public abstract class NodeComparator implements Comparator<Integer> {

	FastGraph g1;
	FastGraph g2;

	
	/**
	 * Constructor, the two FastGraphs must be included.
	 * 
	 * @param g1 the first FastGraph, containing n1 in compare(n1,n2)
	 * @param g2 the second FastGraph, containing n2 in compare(n1,n2)
	 */
	public NodeComparator(FastGraph g1, FastGraph g2) {
		this.g1 = g1;
		this.g2 = g2;
	}
	

}
