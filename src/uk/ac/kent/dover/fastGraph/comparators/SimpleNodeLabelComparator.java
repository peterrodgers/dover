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
	 * @return comparison of node label strings by standard Java compareTo
	 */
	@Override
	public int compare(Integer n1, Integer n2) {
		String label1 = g1.getNodeLabel(n1);
		String label2 = g1.getNodeLabel(n2);
		int ret = label1.compareTo(label2);
		return ret;
	}

}
