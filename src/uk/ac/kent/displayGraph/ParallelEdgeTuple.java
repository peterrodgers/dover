package uk.ac.kent.displayGraph;

import java.util.ArrayList;
import java.util.Collections;

import uk.ac.kent.displayGraph.comparators.EdgeParallelComparator;


/**
 * This class holds a list of parallel edges and a boolean to
 * indicate if the edges have been sorted
 */
public class ParallelEdgeTuple {

/** The list of parallel edges */
	ArrayList<Edge> list;
/** Indicates that the list has been sorted */
	boolean sorted;
/**
 * Connecting node, it does not neccessarily relate to the from node in the
 * Edge class. It is set during the breadth first search in {@link #ParallelEdgeList},
 * so that the nodes are linked in a DAG from the start node.
 */
	Node fromNode;
/** Connecting node, it does not neccessarily relate to the to node in the
 * Edge class. It is set during the breadth first search in {@link #ParallelEdgeList},
 * so that the nodes are linked in a DAG from the start node.
 */
	Node toNode;

/** Constructor that sets sorted to false. */
	public ParallelEdgeTuple(Node inFromNode, Node inToNode, ArrayList<Edge> inList) {
		fromNode = inFromNode;
		toNode = inToNode;
		list = inList;
		sorted = false;
	}

/** Trivial Constructor. */
	public ParallelEdgeTuple(Node inFromNode, Node inToNode, ArrayList<Edge> inList, boolean inSorted) {
		fromNode = inFromNode;
		toNode = inToNode;
		list = inList;
		sorted = inSorted;
	}

/** Trivial Accessor. */
	public ArrayList<Edge> getList() {return list;}
/** Trivial Accessor. */
	public boolean getSorted() {return sorted;}
/** Trivial Accessor. */
	public Node getFromNode() {return fromNode;}
/** Trivial Accessor. */
	public Node getToNode() {return toNode;}

/** Trivial Mutator. */
	public void setSorted(boolean inSorted) {sorted = inSorted;}

/** Sort the list */
	public void sortList() {
		EdgeParallelComparator eComp = new EdgeParallelComparator();
		Collections.sort(list,eComp);
		sorted = true;
	}

/** Ouputs the list and the sorted value */
	public String toString () {
		return getList().toString()+"-"+getSorted();
	}

}


