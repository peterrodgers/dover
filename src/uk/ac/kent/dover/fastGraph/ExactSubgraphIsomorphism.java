package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import uk.ac.kent.dover.fastGraph.comparators.AlwaysTrueEdgeComparator;
import uk.ac.kent.dover.fastGraph.comparators.AlwaysTrueNodeComparator;
import uk.ac.kent.dover.fastGraph.comparators.EdgeComparator;
import uk.ac.kent.dover.fastGraph.comparators.NodeComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleEdgeLabelComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleNodeLabelComparator;

/**
 * Testing the containment of a FastGraph inside another FastGraph. Assumes a single edge between each node.
 * 
 * @author Peter Rodgers
 *
 */
public class ExactSubgraphIsomorphism extends SubgraphIsomorphism {

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

	
	
	public static void main(String [] args) throws Exception {

		double patternDensity = 0.2;
		double targetDensity = 0.12;
		long i = 1000;
		long j = 1000000;
		for(int patternNodes = 8; patternNodes <= 14; patternNodes+=2) {
			if(patternNodes >= 12) {
				patternDensity = 0.16;
			}
			if(patternNodes >= 14) {
				patternDensity = 0.15;
			}
			int patternEdges = (int)(patternDensity*(patternNodes*(patternNodes-1)));
			for(int targetNodes = 30; targetNodes <= 40; targetNodes+=2) {
				int targetEdges = (int)(targetDensity*(targetNodes*(targetNodes-1)));
					i++;
					FastGraph pattern = FastGraph.randomGraphFactory(patternNodes, patternEdges, i, true, false);
					while(!Connected.connected(pattern)) {
						i++;
						pattern = FastGraph.randomGraphFactory(patternNodes, patternEdges, i, true, false);
					}
					j++;
					FastGraph target = FastGraph.randomGraphFactory(targetNodes, targetEdges, j, true, false);
					while(!Connected.connected(target)) {
						j++;
						target = FastGraph.randomGraphFactory(targetNodes, targetEdges, j, true, false);
					}
					ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target,pattern,null,null);
					long time = System.currentTimeMillis();
					boolean result = esi.subgraphIsomorphismFinder();
					double resultTime = (System.currentTimeMillis()-time)/1000.0;
					LinkedList<SubgraphMapping> mapping = esi.getFoundMappings();

					System.out.println(patternNodes+"\t"+patternEdges+"\t"+targetNodes+"\t"+targetEdges+"\t"+mapping.size()+"\t"+resultTime);

			}
		}

		FastGraph targetB = FastGraph.loadBuffersGraphFactory(null, "soc-pokec-relationships.txt-reduced");
		 		
		LinkedList<NodeStructure> addNodes = new LinkedList<NodeStructure>();
		NodeStructure ns0 = new NodeStructure(0,"Shelia Riley", 1, (byte)0, (byte)0);
		NodeStructure ns1 = new NodeStructure(1,"Michael Flemming", 1, (byte)0, (byte)0);
		NodeStructure ns2 = new NodeStructure(2,"", 1, (byte)0, (byte)0);
		NodeStructure ns3 = new NodeStructure(3,"", 1, (byte)0, (byte)0);
		NodeStructure ns4 = new NodeStructure(4,"", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addNodes.add(ns4);
		
		// define the edges that will appear in the new graph
		// the nodes must be defined first, as the last two params give the node indexes
		LinkedList<EdgeStructure> addEdges = new LinkedList<EdgeStructure>();
		EdgeStructure es0 = new EdgeStructure(0,"", 1, (byte)0, (byte)0, 0, 1);
		EdgeStructure es1 = new EdgeStructure(1,"", 2, (byte)0, (byte)0, 0, 2);
		EdgeStructure es2 = new EdgeStructure(2,"", 2, (byte)0, (byte)0, 1, 3);
		EdgeStructure es3 = new EdgeStructure(3,"", 2, (byte)0, (byte)0, 2, 3);
		EdgeStructure es4 = new EdgeStructure(4,"", 2, (byte)0, (byte)0, 2, 4);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		addEdges.add(es3);
		addEdges.add(es4);
		
		// use a factory to create the new graph
		FastGraph patternB = FastGraph.structureFactory("pattern",(byte)0,addNodes,addEdges,false);
 
//patternB.displayFastGraph();		
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(targetB, patternB);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(targetB, patternB);
		ExactSubgraphIsomorphism esiB = new ExactSubgraphIsomorphism(targetB,patternB,snlc,selc);
		long timeB = System.currentTimeMillis();
		boolean resultB = esiB.subgraphIsomorphismFinder();
		double resultTimeB = (System.currentTimeMillis()-timeB)/1000.0;
		LinkedList<SubgraphMapping> mappingB = esiB.getFoundMappings();
		System.out.println("names");
		System.out.println(patternB.getNumberOfNodes()+"\t"+patternB.getNumberOfEdges()+"\t"+targetB.getNumberOfNodes()+"\t"+targetB.getNumberOfEdges()+"\t"+mappingB.size()+"\t"+resultTimeB);
	}
	
	
	
	/**
	 * Create an ExactSubgraphIsomorphism before running {@link #getFoundMappings() getFoundMappings}.
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
		
	}
	

	/**
	 * Call this after subGraphIsomorphismFinder.
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
	 * Output the results of this subgraph finder
	 * @throws FileNotFoundException If the output file cannot be created
	 */
	public void outputResults() throws FileNotFoundException {
		File mainDir = new File(
				Launcher.startingWorkingDirectory+File.separatorChar+"subgraph_results"+
				File.separatorChar+targetGraph.getName()+"_"+Util.dateAsString()
			);
		mainDir.mkdirs(); //make directory
		int count = 0;
		for(SubgraphMapping map : foundMappings) {
			
			//Debugger.log(map.toString());						
			int[] nodeMapping = map.getNodeMapping();
			int[] edgeMapping = map.getEdgeMapping();
			FastGraph newSub = targetGraph.generateGraphFromSubgraph(nodeMapping, edgeMapping);
			saveSubgraph(targetGraph, newSub, count, mainDir);
			
			count++;
		}
		buildHtmlOutput(targetGraph, mainDir, count, "Exact");
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
