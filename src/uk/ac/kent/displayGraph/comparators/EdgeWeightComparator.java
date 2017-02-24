package uk.ac.kent.displayGraph.comparators;

import java.util.Comparator;

import uk.ac.kent.displayGraph.Edge;



/**
 * Orders edges by their weight.
 */
public class EdgeWeightComparator implements Comparator<Edge> {

	public int compare(Edge e1,Edge e2) {
		Double w1 = new Double(e1.getWeight());
		int ret = w1.compareTo(new Double(e2.getWeight()));
		return(ret);
	}
}


