package uk.ac.kent.dover.fastGraph.editOperation;

import java.util.*;
import uk.ac.kent.dover.fastGraph.*;


/**
 * 
 * @author Peter Rodgers
 *
 */
public class EditList {

	LinkedList<EditOperation> editList;
	double cost = 0.0;
			
	public EditList() {
		editList = new LinkedList<EditOperation>();
	}

	/**
	 * Makes a new list. The edit list is not shared, but the edit operations are.
	 * The expectation is that the list will have one element added at some point,
	 * hence the size of the list is slightly bigger than the length of the passed one.
	 * 
	 * @param an existing list to copy. 
	 */
	public EditList(EditList el) {
		editList = new LinkedList<EditOperation>(el.getEditList());
	}

	/**
	 * 
	 * @return the editList
	 */
	public List<EditOperation> getEditList() {
		return editList;
	}
	
 	/**
 	 * Find the total cost of the edit operations.
 	 * @return
 	 */
	public double getCost() {
		return cost;
	}

	
/**
 * Add a graph edit operation to the list
 * 
 * @param eo edit operation to add to the end of the list.
 */
 	public void addOperation(EditOperation eo) {

		editList.add(eo);
		cost += eo.getCost();
	}
 	
 	/**
 	 * Apply the list in sequence.
 	 * 
 	 * @param g the graph to edit.
 	 * @return the new FastGraph, or null if any of the operation fail.
 	 */
 	public FastGraph applyOperations(FastGraph g) {
 		
 		FastGraph ret = g;
 		for(EditOperation eo : editList) {
 			ret = eo.edit(ret);
 			if(ret == null) {
 System.out.println("NULL eo "+eo);
 				return null;
 			}
 		}
 		
 		return ret;
 	}
 	

 	/**
 	 * 
 	 * @return returns only theh delete node operations
 	 */
 	public List<EditOperation> findDeleteNodeOperations () {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.DELETE_NODE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only theh add node operations
 	 */
 	public List<EditOperation> findAddNodeOperations () {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.ADD_NODE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only theh delete edge operations
 	 */
 	public List<EditOperation> findDeleteEdgeOperations () {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.DELETE_EDGE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only theh add edge operations
 	 */
 	public List<EditOperation> findAddEdgeOperations () {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.ADD_EDGE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	
	/**
	 * @return a String output for debugging
	 */
	public String toString() {
		String ret = "";
		for(EditOperation eo : editList) {
			ret += eo.toString()+"\n";
		}
		return ret;
	}

}
