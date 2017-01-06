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
	 * Just compares the labels via standard string comparateTo
	 * 
	 * Just compares the labels via standard string comparateTo, except returns equal (0) if the pattern label is empty. 
	 */
	@Override
	public int compare(Integer target, Integer pattern) {
		String label1 = g1.getNodeLabel(target);
		String label2 = g2.getNodeLabel(pattern);
		if(label2.equals("")) {
			return 0;
		}
		int ret = label1.compareTo(label2);
		return ret;
	}

}
