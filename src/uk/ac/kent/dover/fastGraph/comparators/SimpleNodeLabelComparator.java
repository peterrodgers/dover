package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.FastGraph;


/**
 * Simple label comparison.
 * 
 * @author Peter Rodgers
 *
 */
public class SimpleNodeLabelComparator extends NodeComparator {

	public SimpleNodeLabelComparator(FastGraph g1, FastGraph g2) {
		super(g1, g2);
	}

	
	/**
	 * Just compares the labels via standard string comparateTo, except returns equal (0) if the pattern label is empty. 
	 * 
	 * @return comparison of node label strings by standard Java compareTo: a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
	 */
	@Override
	public int compare(Integer n1, Integer n2) {
		String label1 = g1.getNodeLabel(n1);
		String label2 = g2.getNodeLabel(n2);
		if(label2.equals("")) {
			return 0;
		}
		int ret = label1.compareTo(label2);
		return ret;
	}

}
