package uk.ac.kent.dover.fastGraph.comparators;

import java.util.Comparator;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;
import uk.ac.kent.dover.fastGraph.editOperation.EditOperation;

/**
 * Simple degree comparison for nodes within the same graph. Defaults to ascending,
 * set ascending to false to use descending order.
 * 
 * @author Peter Rodgers
 *
 */
public class EditOperationComparator implements Comparator<EditOperation> {

	/**
	 * default constructor
	 */
	public EditOperationComparator() {
		super();
	}


	/**
	 * Allows the sorting of an edit list into an order that can be safely
	 * applied. Makes sure
	 * 
	 * Otherwise, if they are the same operation, higher ids are applied first, as
	 * otherwise for delete operations ids will change as earlier operations are applied.
	 * 
	 * @return a negative integer, zero, or a positive integer if the first argument is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(EditOperation eo1, EditOperation eo2) {
		int codeCompare = Integer.compare(eo1.getOperationCode(),eo2.getOperationCode());
		
		if(codeCompare != 0) {
			return codeCompare;
		}
		
		int idCompare = Integer.compare(eo1.getId(), eo2.getId());

		return -idCompare;
		
	}


}
