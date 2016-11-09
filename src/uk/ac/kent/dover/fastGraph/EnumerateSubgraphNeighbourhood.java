package uk.ac.kent.dover.fastGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class EnumerateSubgraphNeighbourhood {
	
	FastGraph g;
	
	public EnumerateSubgraphNeighbourhood(FastGraph g) {
		this.g = g;
	}
	
	public HashSet<FastGraph> enumerateSubgraphs(int subgraphSize, int minNumOfNodes, int subgraphsPerNode) {
		Random r = new Random(g.getNodeBuf().getLong(0));
		HashSet<FastGraph> subgraphs = new HashSet<FastGraph>();
		
		//for each node
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			
			//build nodes to pick from
			HashSet<Integer> startingNodes = new HashSet<Integer>();
			HashSet<Integer> nodes = new HashSet<Integer>();
			startingNodes.add(n);
			
			buildNeighbourhood(startingNodes, nodes, minNumOfNodes);			
			//for each subgraph at this neighbourhood
			int foundSubgraphs = 0;
			while(foundSubgraphs < subgraphsPerNode) {
				
				//pick nodes to add
				HashSet<Integer> pickedNodes = new HashSet<Integer>();
				HashSet<Integer> pickedEdges = new HashSet<Integer>();
				pickedNodes.add(n);
				while (pickedNodes.size() < subgraphSize) { //incase duplicated are picked (very small chance)
					int nextNode = r.nextInt(nodes.size());
					pickedNodes.add(nextNode);
				}
				addMissingEdges(pickedNodes, pickedEdges);
				FastGraph subgraph = g.generateGraphFromSubgraph(Util.convertHashSet(pickedNodes), Util.convertHashSet(pickedEdges));
				if(Connected.connected(subgraph)) {
					foundSubgraphs++;
					subgraphs.add(subgraph);
				}				
				
			}
			
		}	
		
		return subgraphs;
	}
	
	
	private void buildNeighbourhood(HashSet<Integer> startingNodes, HashSet<Integer> nodes, int minNumOfNodes) {
		//while there are starting Nodes (incase the graph isn't big enough)
		boolean neverAdded = false; //this ensures that if there are no nodes to add, quit. Useful for small disconnected subgraphs
		while(startingNodes.size() != 0 && !neverAdded) {
			neverAdded = true;
			//for each of these
			for(int sn : startingNodes) {
				//find every edge that connects to this node
				int[] edgeConnections = g.getNodeConnectingEdges(sn);
				for(int fe : edgeConnections) {
					
					//find the other end of the node
					int fn = g.oppositeEnd(fe,sn);
					int nSize = nodes.size();
					nodes.add(fn);
					//edges.add(fe);
					neverAdded = nSize != nodes.size();
				}
				//if we've found enough nodes (or more nodes than needed), then induce the rest of the graph and quit
				if (nodes.size() >= minNumOfNodes) {
					//addMissingEdges(nodes, edges);
					return;
				}
				//if not, go one step deeper
				startingNodes.clear();
				Util.convertArray(g.getNodeConnectingNodes(sn), startingNodes);
			}			
		}
		//addMissingEdges(nodes, edges);
		return;
	}
	
	/**
	 * When given an HashSet of nodes will induce any edges that connect between any two of the given nodes.
	 * 
	 * @param nodes The HashSet of nodes in the graph
	 * @param edges The HashSet of edges in the graph - to be expanded with the newly induced edges.
	 */
	private void addMissingEdges(HashSet<Integer> nodes, HashSet<Integer> edges) {
	
		//for every node in the graph
		for(int n : nodes) {
			//find what edges it connects to
			int[] edgeConnections = g.getNodeConnectingEdges(n);
			//for each of these connecting edges
			for (int ce : edgeConnections) {
				//find the other node
				int cn = g.oppositeEnd(ce,n);
				//if that node is also in the graph, then add the edge
				if (nodes.contains(cn)) {
					edges.add(ce);
				}
			}
			
		}
	}

}
