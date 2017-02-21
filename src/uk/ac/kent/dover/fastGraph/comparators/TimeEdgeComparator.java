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
	 * Just compares the time type. If both edges are time edges, returns 0, if not 1
	 * 
	 * @return comparison of time type
	 */
	@Override
	public int compare(Integer target, Integer pattern) {
		
		byte type1 = g1.getEdgeType(target);
		byte type2 = g2.getEdgeType(pattern);
		
		//if( (type1 == FastGraphEdgeType.TIME.getValue()) || (type2 == FastGraphEdgeType.TIME.getValue()) ) {
			Debugger.log("ids: t:" + target + " p:" + pattern + " types: t:" + type1 + " p:" + type2);
		//}
		
		if(type1 == FastGraphEdgeType.TIME.getValue() && type2 == FastGraphEdgeType.TIME.getValue()) {
			Debugger.log("full time matches. ids: " + target + " " + pattern);
			//if both edges are time edges then find a match
			return 0;
		} else {
			return -1;
		}
	}

}
