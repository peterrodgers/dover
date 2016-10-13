package uk.ac.kent.displayGraph;

import java.util.*;



/**
 * A list of lists of parallel edges. The creation of these
 * lists is done in a depth first search through the nodes in the
 * graph. The search is 'thin'- adding one node to the stack at
 * a time rather than the neigbours, this effect finds circuits.
 *
 * @author Peter Rodgers
 */
public class ParallelEdgeList {

/** The list of lists */
	protected ArrayList<ParallelEdgeTuple> parallelList;

/** Constructor. It creates the list of lists. */
	public ParallelEdgeList(Graph g) {
		setupList(g);
	}

/** Trivial Accessor. */
	public ArrayList<ParallelEdgeTuple> getParallelList() {return parallelList;}

/* 
 * Sets up the tuple list using a depth first search through the
 * graph from an arbitary starting node.
 */
	public void setupList(Graph g) {

		parallelList = new ArrayList<ParallelEdgeTuple>();

		HashSet<Edge> unvisitedEdges = new HashSet<Edge>(g.getEdges());

		while(unvisitedEdges.size() != 0) {

// pick a node connecting to an unvisited edge
			Iterator<Edge> uvEi = unvisitedEdges.iterator();
			Edge firstEdge = (Edge)uvEi.next();
			Node firstNode = firstEdge.getFrom();

			Stack<Node> stack = new Stack<Node>();
			stack.push(firstNode);

// depth first search from the node 
			while(!stack.isEmpty()) {

				Node head = (Node)stack.peek();

// get a connecting node that can be visited by the unvisited edges and continue
// the thin search.
				Node nextNode = null;
				for(Edge nextEdge : head.connectingEdges()) {
					if(unvisitedEdges.contains(nextEdge)) {
						nextNode = nextEdge.getOppositeEnd(head);
						break;
					}
				}

// If there is no possible neigbouring node, unwind by poping the head
				if(nextNode == null) {
					stack.pop();
				} else {

// find the parallel edges, create a new tuple with them and mark them as visited
					ArrayList<Edge> edges = new ArrayList<Edge>(head.connectingEdges(nextNode));
					ParallelEdgeTuple tuple = new ParallelEdgeTuple(head,nextNode,edges);
					parallelList.add(tuple);

					unvisitedEdges.removeAll(edges);

// this may push duplicates onto the stack, but thats OK
					stack.push(nextNode);
				}
			}
		}
	}


/** Sets the sorted flag of all tuples in the list */
	public void setAllSorted(boolean flag) {
		for(ParallelEdgeTuple tuple : getParallelList()) {
			tuple.setSorted(flag);
		}
	}


/** 
 * Output the list of tuples, this uses a slightly modified format from
 * the standard arraylist toString command for clarity
 */
	public String toString() {

		StringBuffer ret = new StringBuffer("<");
		Iterator<ParallelEdgeTuple> i = getParallelList().iterator();
		while(i.hasNext()) {
			ParallelEdgeTuple tuple = (ParallelEdgeTuple)i.next();
			ret.append(tuple.toString());
			if(i.hasNext()) {
				ret.append(";");
			}
		}
		ret.append(">");

		return ret.toString();
	}

}


 
