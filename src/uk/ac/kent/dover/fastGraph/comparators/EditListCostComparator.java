package uk.ac.kent.dover.fastGraph.comparators;

import java.util.Comparator;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;

/**
 * Simple degree comparison for nodes within the same graph. Defaults to ascending,
 * set ascending to false to use descending order.
 * 
 * @author Peter Rodgers
 *
 */
public class EditListCostComparator implements Comparator<EditList> {

	boolean ascending = true;

	/**
	 * default constructor
	 */
	public EditListCostComparator() {
		super();
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
	 * Just compares the costs via standard Double comparison
	 * 
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second,
	 * unless ascending is set to false in which case negative and positive are swapped.
	 */
	@Override
	public int compare(EditList el1, EditList el2) {
		if(ascending) {
			return Double.compare(el1.getCost(), el2.getCost());
		}
		
		return Double.compare(el2.getCost(), el1.getCost());

	}

}
