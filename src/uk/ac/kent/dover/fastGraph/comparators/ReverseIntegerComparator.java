package uk.ac.kent.dover.fastGraph.comparators;

import java.util.Comparator;



	/**
	 * 
	 * Returns opposite of usual integer comparator. For ordering lists descending.
	 * 
	 * @author Peter Rodgers
	 *
	 */
	public class ReverseIntegerComparator implements Comparator<Integer> {  
		
		/**
		 * Constructor does nothing extra.
		 */
		public ReverseIntegerComparator() {
			super();
		}

		/**
		 * @return the opposite of the usual Integer comparator
		 */
		public int compare(Integer i1, Integer i2) {
			int ret = i2.compareTo(i1);
			return ret;
		} 
	}
