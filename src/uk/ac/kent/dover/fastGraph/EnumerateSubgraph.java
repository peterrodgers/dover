package uk.ac.kent.dover.fastGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

/**
 * Algorithm to generate subgraphs for motif detection, based on: A Faster Algorithm for Detecting Network Motifs, Sebastian Wernicke (2005)
 * Variable names in this class are an attempt to match those in the algorithm in the paper
 *  
 * @author Rob Baker
 *
 */
public class EnumerateSubgraph {
	
	private FastGraph g;
	private Random r;
	private double q;
	
	public EnumerateSubgraph(FastGraph g) {
		this.g = g;
	}
	
	/**
	 * ESU - Returns all size-k subgraphs in the FastGraph.
	 * 
	 * @param k The size of subgraph to find
	 * @param q The percentage of nodes to sample, i.e. 50% = 0.5
	 * @return All size-k subgraphs in the FastGraph
	 */
	public HashSet<FastGraph> enumerateSubgraphs(int k, double q) {
		this.q = q;
		r = new Random(g.getNodeBuf().getLong(0));
		Debugger.log("testing enumerateSubgraph");
		Debugger.log("k " + k);
		Debugger.resetTime();
		long time = Debugger.createTime();
		
		HashSet<FastGraph> subs = new HashSet<FastGraph>();
		
		for (int v = 0; v < g.getNumberOfNodes(); v++) {
			
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
			
			//Debugger.log("extension " + extension);
			//Debugger.log("vSubgraph " + vSubgraph);
			
			//extend Subgraph
			if(r.nextDouble() > q) {
				extendSubgraph(vSubgraph,extension,v,k, subs, 1);
			}
			
			if(v % 1000 == 0) {
				Debugger.log("v " + v);
				Debugger.log("subgraphs found " + subs.size());
				Debugger.outputTime("Taken for this step:", time);
				Debugger.outputTime("Taken so far:");
				Debugger.log();
				time = Debugger.createTime();
			}
			
		}
		
		//convert HashMap to array
		//FastGraph[] arr = new FastGraph[subs.size()];
		//Util.convertLinkedListObject(subs, arr);
		
		Debugger.outputTime("finishing enumerate Subgraphs");
		
		return subs;
	}
	
	/**
	 * Extends a subgraph. Returns a set of nodes, from which edges must be added
	 * 
	 * @param vSubgraph The current Subgraph
	 * @param vExtension The extension to the subgraph
	 * @param v The starting node
	 * @param k The size of subgraph to be found
	 * @param subs The list of subgraphs found
	 * @return The extended subgraph (in the form of a set of nodes)
	 */
	private void extendSubgraph(HashSet<Integer> vSubgraph, HashSet<Integer> vExtension, int v, int k, HashSet<FastGraph> subs, int depth) {
		//Debugger.log();
		//Debugger.log("extend k " + k + " v" + v);
		//Debugger.log("extend vExtension " + vExtension);
		//Debugger.log("extend vSubgraph " + vSubgraph);
		//Debugger.log("depth " + depth);
		
		//if the correct size already exists
		if(vSubgraph.size() == k) {
		//	Debugger.log("### extend vSubgraph size match");
			addFoundSubgraph(subs, vSubgraph);
			return;
		}
		
		if(vSubgraph.size() > k) {
			return;
		}
		
		while(vExtension.size() != 0) {
			
			//remove an arbitrary element, w (in this case, the first)
			int w = 0;
			for(int i : vExtension) {
				w = i;
				vExtension.remove(i);
				break;
			}

			//sampling
			//double prob = Math.pow(q,1/depth);
			double prob = (q / depth);
			if(r.nextDouble() > prob) {
				HashSet<Integer> wSet = new HashSet<Integer>();			
				HashSet<Integer> neighboursToW = neighbourhood(wSet);			
				HashSet<Integer> neighboursToVsub = neighbourhood(vSubgraph);
				neighboursToW.removeAll(neighboursToVsub);
				
				HashSet<Integer> vDashExtension = new HashSet<Integer>(vExtension);
				for (int u : neighboursToW) {
					if (u > v) {
						vDashExtension.add(u);
					}
				}
				
				vSubgraph.add(w);
				
				//create new HashSets as the previous ones shouldn't be modified
				extendSubgraph(new HashSet<Integer>(vSubgraph), new HashSet<Integer>(vDashExtension), v, k, subs, depth+1);
			}			
		}
		
		
		return;
	}
	
	
	/**
	 * Creates a set of all nodes connecting to all those in v, but not those in v.
	 * 
	 * @param v The set to build the neighbourhood from
	 * @return A set of connecting nodes (but not the starting nodes themselves)
	 */
	private HashSet<Integer> neighbourhood(HashSet<Integer> v) {
		HashSet<Integer> set = new HashSet<Integer>();
		for(int i : v) {
			set.addAll(Util.convertArray(g.getNodeConnectingNodes(i)));
		}
		set.removeAll(v);
		return set;
	}
	
	/**
	 * Saves a subgraph. Builds the edges between the nodes given
	 * 
	 * @param subs The list of subgraphs
	 * @param nodes The set of nodes in a new subgraph
	 */
	private void addFoundSubgraph(HashSet<FastGraph> subs, HashSet<Integer> nodes) {
	//	Debugger.log("adding subgraph nodes " + nodes);
		HashSet<Integer> edges = new HashSet<Integer>();
		
		//add all edges connecting to only the nodes in the subgraph. Might be slow
		for(int n : nodes) {
			for (int edge : g.getNodeConnectingEdges(n)) {
				if (nodes.contains(g.oppositeEnd(edge,n))) {
					edges.add(edge);
				}
			}
		}
		//Debugger.log("adding subgraph " + (subs.size()+1));
		//convert and add FastGraph
		subs.add(g.generateGraphFromSubgraph(Util.convertHashSet(nodes), Util.convertHashSet(edges)));
	}

}