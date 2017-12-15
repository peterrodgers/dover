package uk.ac.kent.dover.fastGraph.comparators;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * Simple indegree comparison for nodes within the same graph. Defaults to ascending,
 * set ascending to false to use descending order.
 * 
 * @author Peter Rodgers
 *
 */
public class NodeOutDegreeComparator extends NodeComparator {

	boolean ascending = true;

	public NodeOutDegreeComparator(FastGraph g1, FastGraph g2) {
		super(g1,g2);
	}


	/**
	 * @return true if the comparator returns -1 when degree1 smallest, false if -1 returned when degree2 is smallest
	 */
	public boolean getAscending() {return ascending;}

	/**
	 * @param if set to true the comparator returns -1 when degree1 smallest, if set to true -1 returned when degree2 is smallest
	 */
	public void setAscending(boolean ascending) {this.ascending = ascending;}

	
	/**
	 * Just compares the outdegrees via standard Integer comparison
	 * 
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
	 */
	@Override
	public int compare(Integer n1, Integer n2) {
		int degree1 = g1.getNodeOutDegree(n1);
		int degree2 = g2.getNodeOutDegree(n2);
		if(!ascending) {
			degree2 = g1.getNodeOutDegree(n1);
			degree1 = g2.getNodeOutDegree(n2);
		}
		if(degree1 < degree2) {
			return -1;
		}
		if(degree1 > degree2) {
			return 1;
		}
		return 0;
	}

}
