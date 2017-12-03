package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.FastGraph;


/**
 * Simple label comparison.
 * 
 * @author Peter Rodgers
 *
 */
public class SimpleEdgeLabelComparator extends EdgeComparator {

	public SimpleEdgeLabelComparator(FastGraph g1, FastGraph g2) {
		super(g1, g2);
	}

	
	/**
	 * Just compares the labels via standard string comparateTo, except returns equal (0) if the pattern label is empty. 
	 * 
	 * @return comparison of edge label strings by standard Java compareTo: a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
	 */
	@Override
	public int compare(Integer e1, Integer e2) {
		String label1 = g1.getEdgeLabel(e1);
		String label2 = g2.getEdgeLabel(e2);
		if(label2.equals("")) {
			return 0;
		}
		int ret = label1.compareTo(label2);
		return ret;
	}

}
