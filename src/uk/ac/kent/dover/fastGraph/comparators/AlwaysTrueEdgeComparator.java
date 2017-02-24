package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.FastGraph;


/**
 * Simplest possible comparator, always returns true.
 * 
 * @author Peter Rodgers
 *
 */
public class AlwaysTrueEdgeComparator extends EdgeComparator {

	public AlwaysTrueEdgeComparator(FastGraph g1, FastGraph g2) {
		super(g1, g2);
	}

	
	/**
	 * Just returns true.
	 * 
	 * @return true
	 */
	@Override
	public int compare(Integer e1, Integer e2) {
		byte type1 = g1.getEdgeType(e1);
		byte type2 = g2.getEdgeType(e2);
		//if(type1 == FastGraphEdgeType.TIME.getValue() || type2 == FastGraphEdgeType.TIME.getValue()) {
			Debugger.log("ids: t:" + e1 + " p:" + e2 + " types: t:" + type1 + " p:" + type2);
		//}
		return 0;
	}

}
