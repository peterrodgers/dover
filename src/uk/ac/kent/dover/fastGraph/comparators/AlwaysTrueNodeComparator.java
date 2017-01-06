package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.FastGraph;


/**
 * Simplest possible comparator, always returns true.
 * 
 * @author Peter Rodgers
 *
 */
public class AlwaysTrueNodeComparator extends NodeComparator {

	public AlwaysTrueNodeComparator(FastGraph g1, FastGraph g2) {
		super(g1, g2);
	}

	
	/**
	 * Just returns true.
	 * 
	 * @return true
	 */
	@Override
	public int compare(Integer n1, Integer n2) {
		return 0;
	}

}
