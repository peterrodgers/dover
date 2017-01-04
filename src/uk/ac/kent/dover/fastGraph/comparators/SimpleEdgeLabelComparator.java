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
	 * Just compares the labels via standard string comparateTo
	 * 
	 * @return comparison of edge label strings by standard Java compareTo
	 */
	@Override
	public int compare(Integer e1, Integer e2) {
		String label1 = g1.getEdgeLabel(e1);
		String label2 = g1.getEdgeLabel(e2);
		int ret = label1.compareTo(label2);
		return ret;
	}

}
