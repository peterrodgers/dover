package uk.ac.kent.dover.fastGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Algorithm to generate subgraphs for motif detection, based on: A Faster Algorithm for Detecting Network Motifs, Sebastian Wernicke (2005)
 * 
 * @author Rob Baker
 *
 */
public class EnumerateSubgraph {
	
	private FastGraph g;
	
	public EnumerateSubgraph(FastGraph g) {
		this.g = g;
	}
	
	/**
	 * ESU - Returns all size-k subgraphs in the FastGraph.
	 * 
	 * @param k The size of subgraph to find
	 * @return All size-k subgraphs in the FastGraph
	 */
	public FastGraph[] enumerateSubgraphs(int k) {
		Debugger.log("testing enumerateSubgraph");
		Debugger.resetTime();
		
		LinkedList<FastGraph> subs = new LinkedList<FastGraph>();
		for (int v = 0; v < g.getNumberOfNodes(); v++) {
			HashSet<Integer> extension = new HashSet<Integer>();
			
			//ad all nodes connection to v that have a bigger index
			int[] connections = g.getNodeConnectingNodes(v);
			for(int u : connections) {
				if (u > v) {
					extension.add(v);
				}
			}
			HashSet<Integer> vSubgraph = new HashSet<Integer>();
			vSubgraph.add(v);
			
			//extend Subgraph
			HashSet<Integer> nodes = extendSubgraph(vSubgraph,extension,v,k);
			if (nodes != null) {
				HashSet<Integer> edges = new HashSet<Integer>();
				
				//add all edges connecting to only the nodes in the subgraph. Might be slow
				for(int n : nodes) {
					for (int edge : g.getNodeConnectingEdges(n)) {
						if (nodes.contains(g.oppositeEnd(edge,n))) {
							edges.add(edge);
						}
					}
				}
				
				//convert and add FastGraph
				subs.add(g.generateGraphFromSubgraph(Util.convertHashSet(nodes), Util.convertHashSet(edges)));
			}
			
		}
		
		//convert HashMap to array
		FastGraph[] arr = new FastGraph[subs.size()];
		Util.convertLinkedListObject(subs, arr);
		
		Debugger.outputTime("finishing enumerate Subgraphs");
		
		return arr;
	}
	
	/**
	 * Extends a subgraph. Returns a set of nodes, from which edges must be added
	 * 
	 * @param vSubgraph The current Subgraph
	 * @param vExtension The extension to the subgraph
	 * @param v The starting node
	 * @param k The size of subgraph to be found
	 * @return The extended subgraph (in the form of a set of nodes)
	 */
	private HashSet<Integer> extendSubgraph(HashSet<Integer> vSubgraph, HashSet<Integer> vExtension, int v, int k) {
		
		//if the correct size already exists
		if(vSubgraph.size() == k) {
			return vSubgraph;
		}
		
		while(vExtension.size() != 0) {
			
			//remove an arbitrary element, w (in this case, the first)
			int w = 0;
			for(int i : vExtension) {
				w = i;
				vExtension.remove(i);
				break;
			}
			
			HashSet<Integer> neighboursToW = new HashSet<Integer>(Util.convertArray(g.getNodeConnectingNodes(w)));
			HashSet<Integer> neighboursToVsub = new HashSet<Integer>();
			for (int i : vSubgraph) {
				neighboursToVsub.addAll(Util.convertArray(g.getNodeConnectingNodes(i)));
			}
			neighboursToW.removeAll(neighboursToVsub);
			
			HashSet<Integer> vDashExtension = new HashSet<Integer>(vExtension);
			for (int u : neighboursToW) {
				if (u > v) {
					vDashExtension.add(u);
				}
			}
			
			vSubgraph.add(w);
			
			extendSubgraph(vSubgraph, vDashExtension, v, k);
			
		}
		
		
		return null;
	}

}
