package uk.ac.kent.dover.fastGraph;

import java.util.LinkedList;
import java.util.Random;

/**
 * Contains all the methods for generating a InducedSubgraph from the FastGraph given in the constructor.
 * 
 * @author Rob Baker
 *
 */
public class InducedSubgraph {

	FastGraph g; //The FastGraph
	
	/**
	 * Constructor.
	 * Assigns a FastGraph for callbacks to methods
	 * 
	 * @param g The FastGraph
	 */
	public InducedSubgraph(FastGraph g) {
		this.g = g;
	}
	
	/**
	 * Will populated nodes and edges with the nodeIndex and edgeIndex of all the nodes and edges of this subgraph.<br>
	 * Will create a subgraph with the required number of nodes in.
	 * 
	 * @param nodes The list of nodes to populate
	 * @param edges The list of edges to populate
	 * @param numOfNodes The number of nodes in the subgraph
	 * @throws FastGraphException If there is an unspecified error - usually the number of nodes requested is too low
	 */
	public void createInducedSubgraph(LinkedList<Integer> nodes, LinkedList<Integer> edges, int numOfNodes) throws FastGraphException {
		if (numOfNodes < 2) {
			throw new FastGraphException("Can only induce a subgraph with 2 or more nodes");
		}
		if (numOfNodes > g.getNumberOfNodes()) {
			throw new FastGraphException("Cannot find a subgraph that is bigger than the original graph");
		}
		
		//initialise this Random generator if is hasn't been already
		//don't do this in a constructor, as the node buffer might not have been built or populated yet
		Random r = g.getRandomGen();
		if (r == null) {
			long seed = g.getNodeBuf().getLong(1); //used to ensure the random is the same for each graph
			r = new Random(seed);
		}		
		
		int startingEdge = r.nextInt(g.getNumberOfEdges()); //picks an edge at random
		int[] startingNodes = new int[2];
		boolean[] visitedNodes = new boolean[g.getNumberOfNodes()];
		boolean[] visitedEdges = new boolean[g.getNumberOfEdges()];
		
		startingNodes[0] = g.getEdgeNode1(startingEdge);
		startingNodes[1] = g.getEdgeNode2(startingEdge);
		
		//add starting nodes and edges to lists
		nodes.add(startingNodes[0]);
		visitedNodes[startingNodes[0]] = true;
		if(startingNodes[0] != startingNodes[1]) { //incase the first edge is to itself
			nodes.add(startingNodes[1]);
			visitedNodes[startingNodes[1]] = true;
		}		
		edges.add(startingEdge);
		visitedEdges[startingEdge] = true;
		
		if (numOfNodes == 2) { //return with the current selection if graph has been found already - no inducing required.
			return;
		}		
		
		//while there are starting Nodes (incase the graph isn't big enough)
		boolean neverAdded = false; //this ensures that if there are no nodes to add, quit. Useful for small disconnected subgraphs
		while(startingNodes.length != 0 && !neverAdded) {
			neverAdded = true;
			//for each of these
			for(int n : startingNodes) {
				//find every edge that connects to this node
				int[] edgeConnections = g.getNodeConnectingEdges(n);
				for(int fe : edgeConnections) {
					
					//find the other end of the node
					int fn = g.oppositeEnd(fe,n);
					if(!visitedNodes[fn]) { //if it hasn't been visited
						//then add to the subgraph
						visitedNodes[fn] = true;
						nodes.add(fn);
						edges.add(fe);
						visitedEdges[fe] = true;
						neverAdded = false;
					}
					//if we've found enough nodes, then induce the rest of the graph and quit
					if (nodes.size() == numOfNodes) {
						induceGraph(nodes, edges, visitedEdges);
						return;
					}
				}
				//if not, go one step deeper
				startingNodes = g.getNodeConnectingNodes(n);
			}			
		}	
		
		return;
	}
	
	/**
	 * When given an array of nodes will induce any edges that connect between any two of the given nodes.
	 * 
	 * @param nodes The list of nodes in the graph
	 * @param edges The list of edges in the graph - to be expanded with the newly induced edges.
	 * @param visitedEdges The list of edges already visited
	 */
	private void induceGraph(LinkedList<Integer> nodes, LinkedList<Integer> edges, boolean[] visitedEdges) {
		//for every node in the graph
		for(int n : nodes) {
			//find what edges it connects to
			int[] edgeConnections = g.getNodeConnectingEdges(n);
			//for each of these connecting edges
			for (int ce : edgeConnections) {
				//find the other node
				int cn = g.oppositeEnd(ce,n);
				//if that node is also in the graph and this edge hasn't been visited already, then add the edge
				if (nodes.contains(cn) && !visitedEdges[ce]) {
					edges.add(ce);
					visitedEdges[ce] = true;
				}
			}
			
		}		
		return;
	}
}
