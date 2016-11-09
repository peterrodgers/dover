package uk.ac.kent.dover.fastGraph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

/**
 * Generates subgraphs randomly
 * 
 * @author Peter Rodgers and Rob Baker
 *
 */
public class EnumerateSubgraphRandom {

	FastGraph g;
	
	/**
	 * Trivial constructor
	 * @param g The Fast Graph
	 */
	public EnumerateSubgraphRandom(FastGraph g) {
		this.g = g;
	}
	
	public HashSet<FastGraph> randomSampleSubgraph(int numOfNodes, int subgraphsWanted) {

		int discards = 0;
		int maxNodes = g.getNumberOfNodes();
		HashSet<FastGraph> ret = new HashSet<FastGraph>(subgraphsWanted*3);
		
		Random r = new Random(1);
		
		int subgraphsFound = 0;
		while(subgraphsFound < subgraphsWanted) {
			LinkedList<Integer> nodes = new LinkedList<Integer>();
			for(int nextNode = 0; nextNode < numOfNodes; nextNode++) {
				Integer node = r.nextInt(maxNodes);
				nodes.add(node);
			}
			
			
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
			FastGraph sub = g.generateGraphFromSubgraph(Util.convertLinkedList(nodes), Util.convertHashSet(edges));
			
			if(Connected.connected(sub)) {
				ret.add(sub);
				subgraphsFound++;
			} else {
				discards++;
			}

if(subgraphsFound == subgraphsWanted || discards%100000 == 0) {
Debugger.log("wanted "+subgraphsWanted+" found "+subgraphsFound+" discards "+discards);
}

		}
		
		
		return ret;
		
	}
}
