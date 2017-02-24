package uk.ac.kent.displayGraph;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This is a selected group of nodes and edges from a graph.
 * @author Peter Rodgers
 */


public class GraphSelection {

/** Collection of nodes. */
	protected ArrayList<Node> nodes = new ArrayList<Node>();
/** Collection of edges. */
	protected ArrayList<Edge> edges = new ArrayList<Edge>();
/** Originating graph. */
	protected Graph graph = null;

/** Minimal constructor. It creates an empty selection of a graph. */
	public GraphSelection(Graph g) {
		graph = g;
	}

/** Trival accessor. */
	public Graph getGraph() {return graph;}
/** Trivial accessor */
	public ArrayList<Node> getNodes() {return nodes;}
/** Trivial accessor */
	public ArrayList<Edge> getEdges() {return edges;}

/** Adds a node to the selection. */
	public boolean addNode(Node n) {
		if (nodes.contains(n)) {
			return(false);
		}
		return(nodes.add(n));
	}


/** Adds an edge to the graph. */
	public boolean addEdge(Edge e) {
		if(edges.contains(e)) {
			return(false);
		}
		return(edges.add(e));
	}



/** Clears the nodes and edges. */
	public void clear() {
		nodes.clear();
		edges.clear();
	}


/** Check for the item. */
	public boolean contains(Node n) {
	
		if (nodes.contains(n)) {
			return(true);
		}
		return(false);
	}


/** Check for the item. */
	public boolean contains(Edge e) {
	
		if (edges.contains(e)) {
			return(true);
		}
		return(false);
	}




/** Adds the node collection to the selection. */
	public void addNodes(Collection<Node> inNodes) {
		for(Node n : inNodes) {
			addNode(n);
		}
	}


/** Adds the edge collection to the selection. */
	public void addEdges(Collection<Edge> inEdges) {
		for(Edge e : inEdges) {
			addEdge(e);
		}
	}



/** Outputs the node and edge lists in a string. */
	public String toString() {
		return("Selection in graph: "+getGraph().getLabel()+"\nNodes:"+getNodes().toString()+"\nEdges:"+getEdges().toString());
	}

}



