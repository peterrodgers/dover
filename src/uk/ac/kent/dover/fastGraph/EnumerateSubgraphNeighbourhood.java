package uk.ac.kent.dover.fastGraph;

import java.util.HashSet;
import java.util.Random;

/**
 * Creates subgraphs based on neighbourhood sampling
 * 
 * @author Rob Baker
 *
 */
public class EnumerateSubgraphNeighbourhood {
	
	private FastGraph g;
	
	/**
	 * Trivial constructor
	 * @param g The FastGraph the subgraphs will be generated from
	 */
	public EnumerateSubgraphNeighbourhood(FastGraph g) {
		this.g = g;
	}
	
	/**
	 * Generates a set of subgraphs based on neighbourhoods.<br>
	 * The system will generate a neighbourhood for each node.
	 * Each connecting node will be added until minNumOfNodes is met, when the current depth is added.
	 * This avoids always sampling those that appear numerically first.<br>
	 * For each node, a certain number of connected subgraphs (subgraphsPerNode) will be generated.<br>
	 * If this number of subgraphs cannot be generated in attemptsToFindSubgraph attempts, the node will be skipped.
	 * 
	 * @param subgraphSize The number of nodes in each subgraph
	 * @param subgraphsPerNode The number of subgraphs per node
	 * @param attemptsToFindSubgraph The number of attempts to find a connected subgraph.
	 * @return A set of FastGraphs
	 */
	public HashSet<FastGraph> enumerateSubgraphs(int subgraphSize, int subgraphsPerNode, int attemptsToFindSubgraph) {
		Random r = new Random(g.getNodeBuf().getLong(0));
		HashSet<FastGraph> subgraphs = new HashSet<FastGraph>();
		
		//for each node
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			enumerateSubgraphsFromNode(subgraphSize, subgraphsPerNode, attemptsToFindSubgraph, n, r, subgraphs);
		}	

		return subgraphs;
	}
	
	/**
	 * Generates a set of subgraphs from a given node
	 * 
	 * @param subgraphSize The number of nodes in each subgraph
	 * @param subgraphsPerNode The number of subgraphs per node
	 * @param attemptsToFindSubgraph The number of attempts to find a connected subgraph.
	 * @param n The id of the node to build from
	 * @param r A random number generator
	 * @param subgraphs A set of FastGraphs to populate
	 */
	public void enumerateSubgraphsFromNode(int subgraphSize, int subgraphsPerNode, int attemptsToFindSubgraph, int n, Random r, HashSet<FastGraph> subgraphs) {
		//skip if the node has no connections
		if(g.getNodeDegree(n) == 0) {
			return;
		}
		
		//for each subgraph at this neighbourhood
		int foundSubgraphs = 0;
		while(foundSubgraphs < subgraphsPerNode) {
			//build neighbourhood
			HashSet<Integer> nodes = new HashSet<Integer>();
			nodes.add(n);
			int attempts = 0; //in case we happen to pick one already in the list, but also to stop when there are no more to pick
			while(nodes.size() < subgraphSize && attempts < attemptsToFindSubgraph) {
				int nextNode = Util.getFromHashSet(nodes,r.nextInt(nodes.size()));
				int[] cn = g.getNodeConnectingNodes(nextNode);
				int nextToAdd = cn[r.nextInt(cn.length)];
				int nSize = nodes.size();
				nodes.add(nextToAdd);
				if(nSize == nodes.size()) {
					//no nodes added
					attempts++;
				}
			}

			//Are there enough nodes found?
			if(nodes.size() < subgraphSize) {
			//	failuresNeighbourhoodTooSmall++;
				//Debugger.log("neighbourhood too small: " + nodes.size());
				break; //don't check this node again
			} else {
				//add subgraph
				HashSet<Integer> edges = new HashSet<Integer>();
				addMissingEdges(nodes, edges);
				FastGraph subgraph = g.generateGraphFromSubgraph(Util.convertHashSet(nodes), Util.convertHashSet(edges));
				foundSubgraphs++;
				subgraph.setName("subgraph");
				subgraphs.add(subgraph);
			}
		}
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
