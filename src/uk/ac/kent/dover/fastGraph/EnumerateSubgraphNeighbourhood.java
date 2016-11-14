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
		
		Debugger.log("number of subgraphs per node " + subgraphsPerNode);
	/*	
		int failuresNeighbourhoodTooSmall = 0;
		int failuresAttemptsMaxedOut = 0;
		double neighbourhoodTotal = 0;
		
		Debugger.resetTime();
		long time = Debugger.createTime();
		*/
		Random r = new Random(g.getNodeBuf().getLong(0));
		HashSet<FastGraph> subgraphs = new HashSet<FastGraph>();
		
		//for each node
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
		//for(int n = 0; n < 6002; n++) {
			/*
			int step = 100000;
			if(n % step == 0 && n!=0) {
				Debugger.log();
				Debugger.log("Node: " + n);
				Debugger.log("Subgraphs found so far: " + subgraphs.size());
				double smallHoodPercentage = ( (double) failuresNeighbourhoodTooSmall/n)*100;
				Debugger.log("Failures due to small hood so far: " + failuresNeighbourhoodTooSmall + " (" + String.format( "%.2f", smallHoodPercentage ) + "%)");
				double maxedOutPercentage = ( (double) failuresAttemptsMaxedOut/n)*100;
				Debugger.log("Failures due to attempts maxed out so far: " + failuresAttemptsMaxedOut + " (" + String.format( "%.2f", maxedOutPercentage ) + "%)");
				double fullySuccessNodesPercentage = ((double) (n-(failuresNeighbourhoodTooSmall+failuresAttemptsMaxedOut))/n)*100;
				Debugger.log("Fully sucessful nodes: " + String.format( "%.2f", fullySuccessNodesPercentage ) + "%"); 
				double avgNeighbourhoodSize = neighbourhoodTotal / n;
				Debugger.log("Average neighbourhood size: " + String.format( "%.2f",avgNeighbourhoodSize));
				long timeLeft = Debugger.getTimeSinceInSeconds(time)*((g.getNumberOfNodes()-n)/step);
				Debugger.outputTime("Time since last: ", time);
				
				
				Debugger.log("Estimated time left: " + timeLeft + " seconds (" + timeLeft/60 + " mins)");
				
				time = Debugger.createTime();
				Debugger.outputTime("Time so far");
				
			}
			*/
			
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
					subgraphs.add(subgraph);
				}
			}
			
		}	
		Debugger.log();
		return subgraphs;
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
