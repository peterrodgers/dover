package uk.ac.kent.dover.fastGraph;

import java.io.IOException;
import java.util.*;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.dover.fastGraph.comparators.*;

/**
 * Testing the structural similarity of two FastGraphs
 * 
 * @author Peter Rodgers
 *
 */
public class ExactSubgraphIsomorphism {

	private FastGraph targetGraph;
	private FastGraph patternGraph;
	private NodeComparator nodeComparator;
	private EdgeComparator edgeComparator;	
	
	private ArrayList<int[]> possibleNodeMappings = null;
	
	private MatchArrayComparator matchArrayComparitor = new MatchArrayComparator();

	private int[] indexToTargetNodeMatches; // successful matches pattern index to target nodes so far for each element in patternNodeOrder, -1 means no match
	private int[] targetToPatternNodeMatches; // stores reverse matches target node to pattern node, -1 means no match
	private int[] patternToTargetNodeMatches; // stores matches pattern node to target node, -1 means no match

	private int[] patternToTargetEdgeMatches; // successful matches pattern edge to target edge, -1 means no match
	private int[] targetToPatternEdgeMatches; // successful matches target edge to pattern edge, -1 means no match
	
	private boolean resultPossible; // set to false if a pattern node has no possible mappings in the target graph

	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		Debugger.enabled = true;
		
		FastGraph target = null;
		FastGraph pattern = null;
		try {
//			target = FastGraph.loadBuffersGraphFactory(null, "soc-pokec-relationships-reduced");
//			target = FastGraph.adjacencyListGraphFactory(7115,103689,null,"Wiki-Vote.txt",false);
//			target = FastGraph.adjacencyListGraphFactory(36692,367662,null,"Email-Enron1.txt",false);
//			target = FastGraph.adjacencyListGraphFactory(81306,2420766,null,"twitter_combined.txt",false);
//			target = FastGraph.adjacencyListGraphFactory(1696415,11095298,null,"as-skitter.txt",false);
//			target = FastGraph.adjacencyListGraphFactory(1632803,30622564,null,"soc-pokec-relationships.txt",false);
//			target = FastGraph.randomGraphFactory(graphNodes, graphEdges, 1111, true);
//			pattern = FastGraph.randomGraphFactory(subgraphNodes, subgraphEdges, 2222, true);
		} catch(Exception e) {}
		
/*
		Graph targetGraph = new Graph("");
		Node n0 = new Node("a");
		targetGraph.addNode(n0);
		Node n1 = new Node("b");
		targetGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"");
		targetGraph.addEdge(e0);
//		Edge e1 = new Edge(n1,n0,"");
//		targetGraph.addEdge(e1);
		target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("");
		n0 = new Node("b");
		patternGraph.addNode(n0);
		n1 = new Node("a");
		patternGraph.addNode(n1);
		e0 = new Edge(n0,n1,"");
		patternGraph.addEdge(e0);
//		e1 = new Edge(n1,n0,"");
//		patternGraph.addEdge(e1);
		pattern = FastGraph.displayGraphFactory(patternGraph,false);
*/		
		
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Node n3 = new Node("nB");
		targetGraph.addNode(n3);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n0,"eB");
		targetGraph.addEdge(e3);
//Edge e4 = new Edge(n2,n1,"eA");
//targetGraph.addEdge(e4);
		target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle");
		n0 = new Node("nA");
		patternGraph.addNode(n0);
		n1 = new Node("nB");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
n3 = new Node("nB");
patternGraph.addNode(n3);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		patternGraph.addEdge(e2);
e3 = new Edge(n2,n3,"eB");
patternGraph.addEdge(e3);
//e3 = new Edge(n1,n2,"eA");
//patternGraph.addEdge(e3);
		pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);

		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		
Debugger.log(result);

	}

	

	/**
	 * Create an ExactSubgraphIsomorphism before running findMatches.
	 * 
	 * @param patternGraph the small subgraph to be found in the larger target
	 * @param targetGraph the larger target graph in which to find the pattern
	 * @param nodeComparator a comparator to tell when two nodes match, if null then all nodes potentially match all other nodes
	 * @param edgeComparator a comparator to tell when two edges match, if null then all edges with two matching ends will match
	 */
	public ExactSubgraphIsomorphism(FastGraph targetGraph, FastGraph patternGraph, NodeComparator nodeComparator, EdgeComparator edgeComparator) {

		this.targetGraph = targetGraph;
		this.patternGraph = patternGraph;
		
		if(nodeComparator != null) {
			this.nodeComparator = nodeComparator;
		} else {
			this.nodeComparator = new AlwaysTrueNodeComparator(targetGraph, patternGraph);
		}
		if(edgeComparator != null) {
			this.edgeComparator = edgeComparator;
		} else {
			this.edgeComparator = new AlwaysTrueEdgeComparator(targetGraph, patternGraph);
		}
		
		indexToTargetNodeMatches = new int[patternGraph.getNumberOfNodes()];
		targetToPatternNodeMatches = new int[targetGraph.getNumberOfNodes()];
		patternToTargetNodeMatches = new int[patternGraph.getNumberOfNodes()];
		
		patternToTargetEdgeMatches = new int[patternGraph.getNumberOfEdges()];
		targetToPatternEdgeMatches = new int[targetGraph.getNumberOfEdges()];
		
		resultPossible = findPossibleNodeMappings();
		
int pattern = 0;		
for(int[] matches : possibleNodeMappings) {
	for(int i = 0; i < matches.length; i++) {
		Debugger.log("target node id "+pattern+" label "+patternGraph.getNodeLabel(pattern)+" possible match with target node id "+matches[i]+" label "+targetGraph.getNodeLabel(matches[i]));
	}
	pattern++;
}

		
	}
	
	

	/**
	 * Find the pattern graph in the target graph. Returns all possible mappings.
	 *
	 * @return true if there is one or more subgraph found, false if none are found.
	 */
	public boolean subGraphIsomorphismFinder() {
		
		if(!resultPossible) {
			return false;
		}
		
		int numberOfPatternNodes = patternGraph.getNumberOfNodes();
		
		if(numberOfPatternNodes == 0) {
			// empty graph is always found in any graph
			return true;
		}
		
		// sort the nodes in the pattern graph into fewest to most possible matches
		
		Integer[] patternNodeOrder = new Integer[numberOfPatternNodes];
		for(int i = 0; i < numberOfPatternNodes; i++) {
			patternNodeOrder[i] = i;
		}
		
		Arrays.sort(patternNodeOrder,matchArrayComparitor);
		
		// do the backgtracking search based on order found

		Arrays.fill(indexToTargetNodeMatches, -1); // successful matches pattern index to target nodes so far for each element in patternNodeOrder, -1 means no match
		Arrays.fill(targetToPatternNodeMatches, -1); // stores reverse matches target node to pattern node, -1 means no match
		Arrays.fill(patternToTargetNodeMatches, -1); // stores matches pattern node to target node, -1 means no match

		Arrays.fill(patternToTargetEdgeMatches, -1); // stores matches pattern edge to target edge, -1 means no match
		Arrays.fill(targetToPatternEdgeMatches, -1); // stores matches target edge to pattern edge, -1 means no match

		int currentPatternIndex = 0; // the position in the patternNodeOrder we are matching
		int currentPatternNode = patternNodeOrder[currentPatternIndex]; // the actual pattern node we are trying for a match
		int currentTargetIndex = 0; // the position in the possibleNodeMappings we are currently looking at
		int currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex]; // the actual target node we are testing the pattern against
		
		boolean mappingFound = false;
		boolean fullSearchComplete = false;
		while(!fullSearchComplete) {
Debugger.log("current pattern index "+currentPatternIndex+" current target index "+currentTargetIndex);
			
			boolean match = isAMatch(currentTargetNode, currentPatternNode);
Debugger.log("match "+match);
			if(match) {
				indexToTargetNodeMatches[currentPatternIndex] = currentTargetNode;
				targetToPatternNodeMatches[currentTargetNode] = currentPatternNode;
				patternToTargetNodeMatches[currentPatternNode] = currentTargetNode;
				currentPatternIndex++;
Debugger.log("current pattern index "+currentPatternIndex+" number of pattern nodes "+numberOfPatternNodes);
				if(currentPatternIndex == numberOfPatternNodes) { // success, found full mapping!
					mappingFound = true;
					break;
				}

				// move on to next element in the patternNodeOrder
				currentPatternNode = patternNodeOrder[currentPatternIndex];
				currentTargetIndex = 0;
				currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex];
				
			} else { // nodes do not match
				currentTargetIndex++;
				while(currentTargetIndex == possibleNodeMappings.get(currentPatternNode).length) { // run out of target nodes to test, so go back to the previous node in patternIndex, and try the next one, this may happen multiple times
					 // unset the matches
					indexToTargetNodeMatches[currentPatternIndex] = -1;
					targetToPatternNodeMatches[currentTargetNode] = -1;
					patternToTargetNodeMatches[currentPatternNode] = -1;
					// backtrack
					currentPatternIndex--;
					if(currentPatternIndex == -1) {
						fullSearchComplete = true;
						break;
					}
					currentTargetIndex = indexToTargetNodeMatches[currentPatternIndex]++;
				}
				if(!fullSearchComplete) {
					currentPatternNode = patternNodeOrder[currentPatternIndex];
					currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex];
				}
					
			}
			
		}
		
		
if(mappingFound) {
	Debugger.log("Found match");
	for(int i = 0; i < numberOfPatternNodes; i++) {
		Debugger.log("    pattern node "+patternNodeOrder[i]+" label "+patternGraph.getNodeLabel(patternNodeOrder[i]) +" target node "+indexToTargetNodeMatches[i]+" label "+targetGraph.getNodeLabel(indexToTargetNodeMatches[i]));
	}
	return true;
}


		return false;
		
	}

	


	/**
	 * Get the possible target node mappings for all nodes in the pattern graph.
	 * 
	 * @return true if all nodes have at least one possible mapping, false if one or more node has none.
	 */
	private boolean findPossibleNodeMappings() {
		possibleNodeMappings = new ArrayList<int[]>(patternGraph.getNumberOfNodes());

		for(int p = 0; p < patternGraph.getNumberOfNodes(); p++) {
			int[] oversizeCandidates = new int[targetGraph.getNumberOfNodes()];
			
			// find all the candidate matches for the node in the pattern graph
			int candidatePos = 0;
			for(int t = 0; t < targetGraph.getNumberOfNodes(); t++) {
				if(targetGraph.getNodeDegree(t) < patternGraph.getNodeDegree(p)) { // don't match if the target has fewer connecting edges
					continue;
				}
				if(nodeComparator.compare(t,p) == 0) {
					oversizeCandidates[candidatePos] = t;
					candidatePos++;
				}
			}
			
			if(candidatePos == 0) {
				// no possible matches, so return false
				return false;
			}
			
			// chop the array down to size
			int[] candidates = Arrays.copyOf(oversizeCandidates, candidatePos);
			possibleNodeMappings.add(candidates);
			
		}
		
		return true;
		
	}

	



	/**
	 * Check to see if the matched neighbours of patternNode are neigbours of targetNode and sets the edge mapping.
	 * Checks if the target node is already matched and checks that the neighbours of the pattern node are matched to corresponding neighbours of the target node.
	 * Also checks edges and accounts for multiple possible edge matches. The matching of multiple edges is done in a greedy fashion - the
	 * first edge in the target that matches (by source, target and edge comparator) with a pattern edge is taken, this works for most
	 * simple comparators, getting exact solution for some complex comparators ones may require exhaustive search of every possible combination
	 * of parallel edges between two nodes, but this is not supported.
	 * 
	 * @param n1 node in fastGraph
	 * @param n2 node in g
	 * @return true if the neighbours match, false otherwise
	 */
	private boolean isAMatch(int targetNode, int patternNode) {
		
		
		
		// check match already for targetNode, if so return false
		if(targetToPatternNodeMatches[targetNode] != -1) {
			return false;
		}
		
		int[] patternConnectingEdges = patternGraph.getNodeConnectingEdges(patternNode);
		int[] targetConnectingEdges = targetGraph.getNodeConnectingEdges(targetNode);
		
		
		int[] targetConnectingNodes = targetGraph.getNodeConnectingNodes(targetNode);
		HashSet<Integer> targetConnectingNodeSet = new HashSet<Integer>(targetConnectingEdges.length*2);
		for(int i : targetConnectingNodes) {
			targetConnectingNodeSet.add(i);
		}
		
		// for each pattern neighbour check that it matches to an equivalent neighbour of the target node
		for(int i = 0; i < patternConnectingEdges.length; i++) {
			int patternNeighbour = patternGraph.oppositeEnd(patternConnectingEdges[i], patternNode);
			int targetMatch = patternToTargetNodeMatches[patternNeighbour];
			if(targetMatch != -1) {
				if(!targetConnectingNodeSet.contains(targetMatch)) {
Debugger.log("Neigbour node "+patternNeighbour+" of pattern node "+patternNode+" has match that does not match to neigbour of target node "+targetNode);
					// pattern neighbour has a match that is not a neighbour of target node
					return false;
				}
			}
		}
		

		// for each edge, check that there is a match, that the match is not currently used and that the neighbouring nodes are consistent

		// get the connecting nodes and edges between for both pattern and target node
		HashMap<Integer,ArrayList<Integer>> patternNeighbours = new HashMap<Integer,ArrayList<Integer>>(patternConnectingEdges.length*2);
		for(int i = 0; i < patternConnectingEdges.length; i++) {
			int neighbour = patternGraph.oppositeEnd(patternConnectingEdges[i], patternNode);
			ArrayList<Integer> edges = patternNeighbours.get(neighbour);
			if(edges == null) {
				edges = new ArrayList<Integer>();
				edges.add(patternConnectingEdges[i]);
				patternNeighbours.put(neighbour,edges);
			} else {
				edges.add(patternConnectingEdges[i]);
			}
		}
		HashMap<Integer,ArrayList<Integer>> targetNeighbours = new HashMap<Integer,ArrayList<Integer>>(targetConnectingEdges.length*2);
		for(int i = 0; i < targetConnectingEdges.length; i++) {
			int neighbour = targetGraph.oppositeEnd(targetConnectingEdges[i], targetNode);
			ArrayList<Integer> edges = targetNeighbours.get(neighbour);
			if(edges == null) {
				edges = new ArrayList<Integer>();
				edges.add(targetConnectingEdges[i]);
				targetNeighbours.put(neighbour,edges);
			} else {
				edges.add(targetConnectingEdges[i]);
			}
		}

Debugger.log("targetNode "+targetNode);
for(int i : targetNeighbours.keySet()) {
	Debugger.log("    targetNeighbour i "+i+" edges "+targetNeighbours.get(i));
}


		// attempt to match the connecting edges
		boolean success = true;
		for(int patternNeighbour : patternNeighbours.keySet()) {
			int targetNeighbour = patternToTargetNodeMatches[patternNeighbour];
			if(targetNeighbour == -1) {
				// no match yet, so no need to worry about it at present
				continue;
			}
			
			// check to see if we can find an edge match
			for(int patternEdge : patternNeighbours.get(patternNeighbour)) {
				for(int targetEdge : targetNeighbours.get(targetNeighbour)) {
					if(targetToPatternEdgeMatches[targetEdge] == -1) { // possible edge match as there is no current one
						if(edgeComparator.compare(targetEdge,patternEdge) == 0) {
							// we have a match for this edge
							patternToTargetEdgeMatches[patternEdge] = targetEdge;
							targetToPatternEdgeMatches[targetEdge] = patternEdge;
						}
					}
				}
				if(patternToTargetEdgeMatches[patternEdge] == -1) {
					// no target edge match found for this pattern edge so fail
					success = false;
					break;
				}

			}
			if(!success) {
				break;
			}
		}
		
		if(!success) {
			// reset the edge matches on fail
			for(int patternNeighbour : patternNeighbours.keySet()) {
				for(int patternEdge : patternNeighbours.get(patternNeighbour)) {
					if(patternToTargetEdgeMatches[patternEdge] != -1) {
						targetToPatternEdgeMatches[patternToTargetEdgeMatches[patternEdge]] = -1;
						patternToTargetEdgeMatches[patternEdge] = -1;
					}
				}
			}
			
			return false;
		}



		return true;
	}



	/**
	 * Check if the two graphs have the same structure.
	 * 
	 * @param g the FastGraph from which a subgraph will be found
	 * @param nodes the nodes in g2 that form the subgraph
	 * @param edges the edges in g2 that form the subgraph
	 * @return true if the g1 and the subgraph of g2 are isomorphic, false otherwise
	 */
/*	public boolean isomorphic(FastGraph g, int[] nodes, int[] edges) {

		FastGraph subGraph = g.generateGraphFromSubgraph(nodes,edges);
		
		boolean iso = isomorphic(subGraph);

		return iso;
	}
*/

		


	/**
	 * Check if two graphs have the same structure. Use the constructor and non static method if
	 * lots of comparisons are going to be made against the same graph.
	 * 
	 * @param g1 one FastGraph to be tested
	 * @param g2 the other FastGraph to be tested
	 * @return true if g1 and g2 are isomorphic, false otherwise
	 */
/*	public static boolean isomorphic(FastGraph g1, FastGraph g2) {
		ExactSubgraphIsomorphism ei = new ExactSubgraphIsomorphism(g1);
		boolean ret = ei.isomorphic(g2);
		return ret;
	}
*/	

	


	/**
	 * compares potential match arrays by size for deciding which node in the pattern graph should be tested first.
	 * 
	 * @author Peter Rodgers
	 *
	 */
	class MatchArrayComparator implements Comparator<Integer> {
		
	    public int compare(Integer n1, Integer n2) {
	    	int[] array1 = possibleNodeMappings.get(n1);
	    	int[] array2 = possibleNodeMappings.get(n2);
	    	
	    	Integer length1 = array1.length;
	    	Integer length2 = array2.length;
	    	
	        return length1.compareTo(length2);
	    }
	}
	
	
	

	/**
	 * Output timing
	 */
	public static void reportTimes() {
//		if(numberOfIsomorphismTests > 0) {
//			System.out.println("Isomorphism test average "+(timeForIsomorphismTests/(1000.0*numberOfIsomorphismTests))+" seconds total tests "+numberOfIsomorphismTests+" total time "+(timeForIsomorphismTests/1000.0)+" seconds");
//		} else {
//			System.out.println("Isomorphism total tests "+numberOfIsomorphismTests);
//		}
	}

	
	/**
	 * Output counts
	 */
	public static void reportFailRatios() {
/*		
		double total = failOnNodeCount+failOnEdgeCount+failOnEigenvalues+failOnDegreeComparison+failOnNodeMatches+failOnBruteForce+succeed;
		
		System.out.println("fail on Node Count "+failOnNodeCount+" "+(100.0*failOnNodeCount/total)+" % of calls");
		System.out.println("fail on Edge Count "+failOnEdgeCount+" "+(100.0*failOnEdgeCount/total)+" % of calls");
		System.out.println("fail on Degree Comparison "+failOnDegreeComparison+" "+(100.0*failOnDegreeComparison/total)+" % of calls");
		System.out.println("fail on Eigenvalues "+failOnEigenvalues+" "+(100.0*failOnEigenvalues/total)+" % of calls");
		System.out.println("fail on Node Matches "+failOnNodeMatches+" "+(100.0*failOnNodeMatches/total)+" % of calls");
		System.out.println("fail on Brute Force "+failOnBruteForce+" "+(100.0*failOnBruteForce/total)+" % of calls");
		System.out.println("succeed "+succeed+" "+(100.0*succeed/total)+" "+" % of calls");
*/
	}

	



}
