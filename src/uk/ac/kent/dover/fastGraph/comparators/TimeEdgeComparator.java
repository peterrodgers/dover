package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphEdgeType;


/**
 * Time edge comparison.
 * 
 * @author Robert Baker
 *
 */
public class TimeEdgeComparator extends EdgeComparator {

	public TimeEdgeComparator(FastGraph g1, FastGraph g2) {
		super(g1, g2);
	}

	
	/**
	 * Just compares the time type. If both edges are time edges, or if both edges are not time edges then, returns 0, if not 1
	 * 
	 * @return 0 if both edges have time type, 0 if both edges are not time type, 1 if one is time type and the other is not.
	 */
	@Override
	public int compare(Integer target, Integer pattern) {
		
		byte type1 = g1.getEdgeType(target);
		byte type2 = g2.getEdgeType(pattern);
		
		if(type1 == FastGraphEdgeType.TIME.getValue() && type2 == FastGraphEdgeType.TIME.getValue()) {
			return 0;
		}
		
		if(type1 != FastGraphEdgeType.TIME.getValue() && type2 != FastGraphEdgeType.TIME.getValue()) {
			return 0;
		}
		
		
		return 1;
	}

}
