package uk.ac.kent.dover.fastGraph;

import java.io.IOException;
import java.util.*;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.dover.fastGraph.comparators.*;

/**
 * Testing the structural similarity of two FastGraphs. Assumes a single edge between each node.
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
	
	private LinkedList<SubgraphMapping> foundMappings = null;
	
	private MatchArrayComparator matchArrayComparitor = new MatchArrayComparator();

	private int[] indexToTargetNodeMatches; // successful matches pattern index to target nodes so far for each element in patternNodeOrder, -1 means no match
	private int[] targetToPatternNodeMatches; // stores reverse matches target node to pattern node, -1 means no match
	private int[] patternToTargetNodeMatches; // stores matches pattern node to target node, -1 means no match
	private int[] possibleMatchIndexProgress; // stores progress through possibleNodeMatches of each patternNode
	
	private int[] patternToTargetEdgeMatches; // successful matches pattern edge to target edge, -1 means no match
	
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
			target = FastGraph.randomGraphFactory(1000, 10000, 123, false);
//			pattern = FastGraph.randomGraphFactory(subgraphNodes, subgraphEdges, 2222, true);
		} catch(Exception e) {}
		

		
/*		
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nA");
		targetGraph.addNode(n1);
		Node n2 = new Node("nB");
		targetGraph.addNode(n2);
//		Node n3 = new Node("nB");
//		targetGraph.addNode(n3);
		Edge e0 = new Edge(n0,n1,"eB");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eA");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
//		Edge e3 = new Edge(n3,n0,"eB");
//		targetGraph.addEdge(e3);
//Edge e4 = new Edge(n2,n1,"eA");
//targetGraph.addEdge(e4);
		target = FastGraph.displayGraphFactory(targetGraph,false);
*/
		
/*
		Graph patternGraph = new Graph("triangle");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
//n3 = new Node("nB");
//patternGraph.addNode(n3);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eA");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eB");
		patternGraph.addEdge(e2);
//e3 = new Edge(n2,n3,"eB");
//patternGraph.addEdge(e3);
//e3 = new Edge(n1,n2,"eA");
//patternGraph.addEdge(e3);
		pattern = FastGraph.displayGraphFactory(patternGraph,false);
	

	
		Graph targetGraph = new Graph("two connected nodes");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("two connected nodes");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		pattern = FastGraph.displayGraphFactory(patternGraph,false);

		

		
		Graph targetGraph = new Graph("triangle");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Edge e0 = new Edge(n2,n0,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n1,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n0,"eA");
		targetGraph.addEdge(e2);
		target = FastGraph.displayGraphFactory(targetGraph,false);
*/
		Graph patternGraph = new Graph("square with edge across");
		Node nn0 = new Node("");
		patternGraph.addNode(nn0);
		Node nn1 = new Node("");
		patternGraph.addNode(nn1);
		Node nn2 = new Node("");
		patternGraph.addNode(nn2);
		Node nn3 = new Node("");
		patternGraph.addNode(nn3);
		Edge ee0 = new Edge(nn0,nn1,"");
		patternGraph.addEdge(ee0);
		Edge ee1 = new Edge(nn1,nn2,"");
		patternGraph.addEdge(ee1);
		Edge ee2 = new Edge(nn2,nn3,"");
		patternGraph.addEdge(ee2);
		Edge ee3 = new Edge(nn3,nn0,"");
		patternGraph.addEdge(ee3);
		Edge ee4 = new Edge(nn2,nn0,"");
		patternGraph.addEdge(ee4);
		pattern = FastGraph.displayGraphFactory(patternGraph,false);
		
//uk.ac.kent.displayGraph.display.GraphWindow gw1 = new uk.ac.kent.displayGraph.display.GraphWindow(patternGraph,true);
//uk.ac.kent.displayGraph.display.GraphWindow gw2 = new uk.ac.kent.displayGraph.display.GraphWindow(targetGraph,true);
		
		
		ExactSubgraphIsomorphism esi;
		boolean result;
		
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
Debugger.resetTime();
		result = esi.subgraphIsomorphismFinder();
Debugger.outputTime("time for subgraph isomorphism");
		
/*		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		result = esi.subGraphIsomorphismFinder();
*/
		System.out.println("number of mappings found "+esi.getFoundMappings().size());
		
//Debugger.log(result);
/*
if(result) {
	for(SubgraphMapping sgm : esi.getFoundMappings()) {
		Debugger.log(" ");
		int[] nodeMap = esi.getFoundMappings().get(0).getNodeMapping();
		for(int i = 0; i <nodeMap.length; i++) {
			System.out.println("node map from "+i+" label "+pattern.getNodeLabel(i)+ " to "+nodeMap[i]+" label "+target.getNodeLabel(i));
		}
		
		int[] edgeMap = esi.getFoundMappings().get(0).getEdgeMapping();
		for(int i = 0; i < edgeMap.length; i++) {
			System.out.println("edge map from "+i+" label "+pattern.getEdgeLabel(i)+ " to "+nodeMap[i]+" label "+target.getEdgeLabel(i));
		}
	}
}
*/

//new uk.ac.kent.displayGraph.display.GraphWindow(patternGraph, true);
//new uk.ac.kent.displayGraph.display.GraphWindow(targetGraph, true);
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
		possibleMatchIndexProgress = new int[patternGraph.getNumberOfNodes()];
		
		patternToTargetEdgeMatches = new int[patternGraph.getNumberOfEdges()];
		
		resultPossible = findPossibleNodeMappings();
		
		foundMappings = new LinkedList<SubgraphMapping>();
/*		
int pattern = 0;
for(int[] matches : possibleNodeMappings) {

	String labels = "";
	for(int i = 0; i < matches.length; i++) {
		labels += targetGraph.getNodeLabel(matches[i])+" ";
	}
	Debugger.log("pattern node id "+pattern+" label "+patternGraph.getNodeLabel(pattern)+" possible match with target node ids "+Arrays.toString(matches)+" labels "+labels);
	pattern++;
}
*/
		
	}
	

	/**
	 * call this after subGraphIsomorphismFinder
	 * @return the found mappings from the pattern graph to the target graph
	 */
	public LinkedList<SubgraphMapping> getFoundMappings () {return foundMappings;}
	

	/**
	 * Find the pattern graph in the target graph. Returns all possible mappings. Assumes single edge between nodes.
	 *
	 * @return true if there is one or more subgraph found, false if none are found.
	 */
	public boolean subgraphIsomorphismFinder() {
		
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
		
		// do the backtracking search based on order found

		Arrays.fill(indexToTargetNodeMatches, -1); // successful matches pattern index to target nodes so far for each element in patternNodeOrder, -1 means no match
		Arrays.fill(targetToPatternNodeMatches, -1); // stores reverse matches target node to pattern node, -1 means no match
		Arrays.fill(patternToTargetNodeMatches, -1); // stores matches of pattern node to target node, -1 means no match
		Arrays.fill(possibleMatchIndexProgress, -1); // stores matches of target index progress through possibleNodeMappings -1 means no match

		int currentPatternIndex = 0; // the position in the patternNodeOrder we are matching
		int currentPatternNode = patternNodeOrder[currentPatternIndex]; // the actual pattern node we are trying for a match
		int currentTargetIndex = 0; // the position in the possibleNodeMappings we are currently looking at
		int currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex]; // the actual target node we are testing the pattern against
		
		boolean mappingFound = false;
		boolean fullSearchComplete = false;
		while(!fullSearchComplete) {

			boolean match = isAMatch(currentTargetNode, currentPatternNode);
			
			if(match) {
				indexToTargetNodeMatches[currentPatternIndex] = currentTargetNode;
				targetToPatternNodeMatches[currentTargetNode] = currentPatternNode;
				patternToTargetNodeMatches[currentPatternNode] = currentTargetNode;
				possibleMatchIndexProgress[currentPatternNode] = currentTargetIndex;
/*				
Debugger.log("found a match between pattern node "+currentPatternNode+" and target node "+currentTargetNode);
for(int q = 0; q < patternToTargetNodeMatches.length; q++) {
	Debugger.log("patternToTargetNodeMatches["+q+"] "+patternToTargetNodeMatches[q]);
}
Debugger.log("------------------");
for(int q = 0; q < targetToPatternNodeMatches.length; q++) {
	Debugger.log("targetToPatternNodeMatches["+q+"] "+targetToPatternNodeMatches[q]);
}
Debugger.log();
*/
				if(currentPatternIndex == numberOfPatternNodes-1) { // success, found full mapping!
					mappingFound = true;
					findEdgeMappings(patternToTargetEdgeMatches);
					SubgraphMapping storedMapping = new SubgraphMapping(targetGraph,patternGraph,patternToTargetNodeMatches,patternToTargetEdgeMatches);
					foundMappings.add(storedMapping);
					
					match = false; // force search onto next target index, and possibly backtracking 

				} else {
					// partial match found, so move on to next element in the patternNodeOrder
					currentPatternIndex++;
					currentPatternNode = patternNodeOrder[currentPatternIndex];
					currentTargetIndex = 0;
					currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex];
				}
			}
			
			if(!match){
//Debugger.log("match failed between pattern node "+currentPatternNode+" and target node "+currentTargetNode);
				// nodes do not match
				currentTargetIndex++;
				while(currentTargetIndex >= possibleNodeMappings.get(currentPatternNode).length) { // run out of target nodes to test, so go back to the previous node in patternIndex, and try the next one, this may happen multiple times
					 // unset the matches of last matched nodes if already matched, it may be set if we are backtracking
					if(indexToTargetNodeMatches[currentPatternIndex] != -1) {
						int matchedTargetNode = patternToTargetNodeMatches[currentPatternNode];
						indexToTargetNodeMatches[currentPatternIndex] = -1;
						patternToTargetNodeMatches[currentPatternNode] = -1;
						targetToPatternNodeMatches[matchedTargetNode] = -1;
						possibleMatchIndexProgress[currentPatternNode] = -1;
					}
					
					// backtrack
					currentPatternIndex--;
					if(currentPatternIndex == -1) {
						fullSearchComplete = true;
						return mappingFound;
					}
					currentPatternNode = patternNodeOrder[currentPatternIndex];
					currentTargetIndex = possibleMatchIndexProgress[currentPatternNode]+1;
				}
				
				// if we have been backtracking, the current patternNode may be matched, so unset it
				if(indexToTargetNodeMatches[currentPatternIndex] != -1) {
					 // unset the matches of last matched nodes
					int matchedTargetNode = patternToTargetNodeMatches[currentPatternNode];
					indexToTargetNodeMatches[currentPatternIndex] = -1;
					patternToTargetNodeMatches[currentPatternNode] = -1;
					targetToPatternNodeMatches[matchedTargetNode] = -1;
					possibleMatchIndexProgress[currentPatternNode] = -1;
				}
				
				currentPatternNode = patternNodeOrder[currentPatternIndex];
				currentTargetNode = possibleNodeMappings.get(currentPatternNode)[currentTargetIndex];
					
			}
			
		}
		
		return mappingFound;
		
	}

	




	/**
	 * Check to see if the matched neighbours of patternNode are neigbours of targetNode.
	 * Checks if the target node is already matched and checks that the neighbours of the pattern node are matched to corresponding neighbours of the target node.
	 * If there are multiple edges between nodes in either target or pattern, one edge is tested (it is undefined as to which one).
	 * 
	 * @param n1 node in fastGraph
	 * @param n2 node in g
	 * @return true if the neighbours match, false otherwise
	 */
	private boolean isAMatch(int targetNode, int patternNode) {
		
		// check if there is a match already for targetNode, if so return false
		if(targetToPatternNodeMatches[targetNode] != -1) {
//Debugger.log("isAMatch fail on target already matched");
			return false;
		}
		
		int[] patternConnectingEdges = patternGraph.getNodeConnectingEdges(patternNode);
		int[] targetConnectingEdges = targetGraph.getNodeConnectingEdges(targetNode);
		
		int[] targetConnectingNodes = targetGraph.getNodeConnectingNodes(targetNode);

		// this used for efficient containment test
		HashSet<Integer> targetConnectingNodeSet = new HashSet<Integer>(targetConnectingEdges.length*2);
		for(int i : targetConnectingNodes) {
			targetConnectingNodeSet.add(i);
		}
				
		// this allows duplicates to be discarded
		HashSet<Integer> testedPatternNodeNeighbours = new HashSet<Integer>(patternConnectingEdges.length*2);
		
		// for each pattern neighbour check that it matches to an equivalent neighbour of the target node
		for(int i = 0; i < patternConnectingEdges.length; i++) {
			int patternEdge = patternConnectingEdges[i];
			int patternNeighbour = patternGraph.oppositeEnd(patternEdge, patternNode);
			if(testedPatternNodeNeighbours.contains(patternNeighbour)) { // this is a test for duplicate connecting nodes (parallel edges), we don't test node more than once
				continue;
			}
			testedPatternNodeNeighbours.add(patternNeighbour);
			
			int targetMatch = patternToTargetNodeMatches[patternNeighbour];
//Debugger.log("pattern neighbour "+patternNeighbour+" matches with target "+targetMatch);
			if(targetMatch == -1) {
				// pattern neighbouring node is not matched with anything, so can continue
				continue;
			}
			if(!targetConnectingNodeSet.contains(targetMatch)) {
//Debugger.log("isAMatch fail on target neigbour already matched with non-neighbour");
				// pattern neighbour has a match that is not a neighbour of target node, so the patternNode and targetNode cannot match
				return false;
			}

			// get the edge between the target and target neighbour
			int targetEdge = targetGraph.edgesBetween(targetNode,targetMatch).get(0);
			
			if(edgeComparator.compare(targetEdge,patternEdge) != 0) {
//Debugger.log("isAMatch fail on edge comparator between the target nodes");
				// edge between pattern node and pattern neighbour does not match the edge between pattern node and pattern neighbour
				return false;
			}

		}
		
		return true;
	}
	
	


	/**
	 * Get the possible target node mappings for all nodes in the pattern graph based on target nodes that have
	 * the same or greater degree and also comparator between pattern node and target node.
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
	 * once a node mapping has been found, populate the Edge match arrays
	 *
	 */
	private void findEdgeMappings(int[] edgeMatches) {
		Arrays.fill(edgeMatches, -1); // stores matches pattern edge to target edge, -1 means no match

		for(int patternEdge = 0; patternEdge < patternGraph.getNumberOfEdges(); patternEdge++) {
			int patternNode1 = patternGraph.getEdgeNode1(patternEdge);
			int patternNode2 = patternGraph.getEdgeNode2(patternEdge);
			int targetNode1 = patternToTargetNodeMatches[patternNode1];
			int targetNode2 = patternToTargetNodeMatches[patternNode2];
			 
			// there will be at least one of these, as the target nodes cannot have mappings
			// in isAMatch without a connecting edge
			int targetEdge = targetGraph.edgesBetween(targetNode1, targetNode2).get(0);
			edgeMatches[patternEdge] = targetEdge;
		}
		
		
	}




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
	

	



}
