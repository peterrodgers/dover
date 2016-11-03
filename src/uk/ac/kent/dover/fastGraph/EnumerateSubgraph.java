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
		Debugger.log("k " + k);
		Debugger.resetTime();
		
		LinkedList<FastGraph> subs = new LinkedList<FastGraph>();
		for (int v = 0; v < g.getNumberOfNodes(); v++) {
			Debugger.log("v " + v);
			HashSet<Integer> extension = new HashSet<Integer>();
			
			//ad all nodes connection to v that have a bigger index
			HashSet<Integer> vSet = new HashSet<Integer>();
			vSet.add(v);
			int[] connections = Util.convertHashSet(neighbourhood(vSet));
			//Debugger.log("connections " + Arrays.toString(connections));
			for(int u : connections) {
				if (u > v) {
					extension.add(u);
				}
			}
			HashSet<Integer> vSubgraph = new HashSet<Integer>();
			vSubgraph.add(v);
			
			Debugger.log("extension " + extension);
			Debugger.log("vSubgraph " + vSubgraph);
			
			//extend Subgraph
			HashSet<Integer> nodes = extendSubgraph(vSubgraph,extension,v,k);
			Debugger.log("adding subgraph nodes " + nodes);
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
				Debugger.log("adding subgraph " + (subs.size()+1));
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
		Debugger.log();
		Debugger.log("extend k " + k);
		Debugger.log("extend vExtension " + vExtension);
		Debugger.log("extend vSubgraph " + vSubgraph);
		
		//if the correct size already exists
		if(vSubgraph.size() == k) {
			Debugger.log("### extend vSubgraph size match");
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
			
			HashSet<Integer> wSet = new HashSet<Integer>();			
			HashSet<Integer> neighboursToW = neighbourhood(wSet);			
			HashSet<Integer> neighboursToVsub = neighbourhood(vSubgraph);
			/*for (int i : vSubgraph) {
				neighboursToVsub.addAll(Util.convertArray(g.getNodeConnectingNodes(i)));
			}*/
			neighboursToW.removeAll(neighboursToVsub);
			
			HashSet<Integer> vDashExtension = new HashSet<Integer>(vExtension);
			for (int u : neighboursToW) {
				if (u > v) {
					vDashExtension.add(u);
				}
			}
			
			vSubgraph.add(w);
			
			HashSet<Integer> res = extendSubgraph(vSubgraph, vDashExtension, v, k);
			if (res != null) {
				return res;
			}
			
		}
		
		
		return null;
	}
	
	private HashSet<Integer> neighbourhood(HashSet<Integer> v) {
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i : v) {
			set.addAll(Util.convertArray(g.getNodeConnectingNodes(i)));
		}
		set.removeAll(v);
		return set;
	}

}
