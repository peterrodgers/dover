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
	 * @return comparison of edge label strings by standard Java compareTo
	 */
	@Override
	public int compare(Integer target, Integer pattern) {
		String label1 = g1.getEdgeLabel(target);
		String label2 = g2.getEdgeLabel(pattern);
		if(label2.equals("")) {
			return 0;
		}
		int ret = label1.compareTo(label2);
		return ret;
	}

}
