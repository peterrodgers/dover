package uk.ac.kent.dover.fastGraph;

import java.io.IOException;
import java.util.*;

import org.junit.experimental.theories.PotentialAssignment;

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

	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		Debugger.enabled = true;
		// Profiling code for number of required rewirings
		int graphNodes = 100;
		int graphEdges = 1000;
		int subgraphNodes = 10;
		int subgraphEdges = 100;
		int iterations = 1;
			
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
		
		
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n1 = new Node("nA");
		targetGraph.addNode(n1);
		Node n2 = new Node("nB");
		targetGraph.addNode(n2);
		Node n3 = new Node("nA");
		targetGraph.addNode(n3);
		Node n4 = new Node("nB");
		targetGraph.addNode(n4);
		Edge e1 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n3,"eB");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n2,n3,"eA");
		targetGraph.addEdge(e3);
		Edge e4 = new Edge(n3,n1,"eB");
		targetGraph.addEdge(e4);
		target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternSubgraph = new Graph("triangle");
		n1 = new Node("nA");
		patternSubgraph.addNode(n1);
		n2 = new Node("nB");
		patternSubgraph.addNode(n2);
		n3 = new Node("nA");
		patternSubgraph.addNode(n3);
		e1 = new Edge(n1,n2,"eA");
		patternSubgraph.addEdge(e1);
		e2 = new Edge(n1,n3,"eB");
		patternSubgraph.addEdge(e2);
		e3 = new Edge(n2,n3,"eA");
		patternSubgraph.addEdge(e3);
		pattern = FastGraph.displayGraphFactory(patternSubgraph,false);
		
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);

		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		esi.subGraphIsomorphismFinder();		

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
		
		findPossibleNodeMappings();
		
int pattern = 0;		
for(int[] matches : possibleNodeMappings) {
	for(int i = 0; i < matches.length; i++) {
//		System.out.println("target node id "+pattern+"  possible match with target node id "+matches[i]);
		System.out.println("target node id "+pattern+" label "+patternGraph.getNodeLabel(pattern)+" possible match with target node id "+matches[i]+" label "+targetGraph.getNodeLabel(matches[i]));
	}
	pattern++;
}

	}
	
	

	private void findPossibleNodeMappings() {
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
			
			// chop the array down to size
			int[] candidates = Arrays.copyOf(oversizeCandidates, candidatePos);
			possibleNodeMappings.add(candidates);
			
		}
		
	}



	/**
	 * Find the pattern graph in the target graph. Returns all possible mappings.
	 *
	 * @return true if there is one or more subgraph found, false if none are found.
	 */
	public boolean subGraphIsomorphismFinder() {
		
		int numberOfPatternNodes = patternGraph.getNumberOfNodes();
		int numberOfTargetNodes = targetGraph.getNumberOfNodes();
		
		// sort the nodes in the pattern graph into fewest to most possible matches
		
		Integer[] patternNodeOrder = new Integer[numberOfPatternNodes];
		for(int i = 0; i < numberOfPatternNodes; i++) {
			patternNodeOrder[i] = i;
		}
		
		Arrays.sort(patternNodeOrder,matchArrayComparitor);
		
		// do the backgtracking search based on order found

		// all these index patternNodeOrder
		int currentPatternOrder = 0;
		int[] matches = new int[numberOfPatternNodes-1]; // successful node matches so far, -1 means no match
		Arrays.fill(matches, -1);

		
		boolean searchComplete = false;
		while(!searchComplete) {
		
			
		}
		
/*
numberOfIsomorphismTests++;
startTime = System.currentTimeMillis();

		FastGraph g1 = fastGraph;
		FastGraph g2 = g;
		
		int numberOfNodes1 = g1.getNumberOfNodes();
		int numberOfNodes2 = g2.getNumberOfNodes();

		int numberOfEdges1 = g1.getNumberOfEdges();
		int numberOfEdges2 = g2.getNumberOfEdges();

		// ensure that the same graph returns true
		if(g1 == g2) {
			return(true);
		}
		
		if(numberOfNodes1 == 0 && numberOfNodes2 == 0) {
//System.out.println("Isomorphic: empty graphs");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
			return true;
		}
				
		if(numberOfNodes1 != numberOfNodes2) {
//System.out.println("Not isomorphic: different number of edges");
failOnNodeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
			return false;
		}
				
		if(numberOfEdges1 != numberOfEdges2) {
//System.out.println("Not isomorphic: different number of nodes");
failOnEdgeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
			return false;
		}
		
		degrees2 = findDegrees(g);
		
		maxDegree2 = g.maximumDegree();

		// check the number of nodes at each degree
		degreeBuckets2 = new int[maxDegree2+1];
		findDegreeBuckets(degreeBuckets2,degrees2);
		if(!Arrays.equals(degreeBuckets1, degreeBuckets2)) {
//System.out.println("Not isomorphic: different quantities of nodes with the same degree");
failOnDegreeComparison++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
			return false;
		}

		
		
		am2 = new AdjacencyMatrix(g);
		matrix2 = am2.buildIntAdjacencyMatrix();
		eigenvalues2 = am2.findEigenvalues(matrix2);
		eigenvalues2 = Util.roundArray(eigenvalues2, DECIMAL_PLACES);
//System.out.println(Arrays.toString(eigenvalues1));
//System.out.println(Arrays.toString(eigenvalues2));
		if(!compareEigenValues(eigenvalues2)) {
//System.out.println("Not isomorphic: eigenvalues are different");
failOnEigenvalues++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
			return false;
		}
		
if(startTime == -1) {
	startTime = System.currentTimeMillis();
}
		
		neighbours2 = findNeighbours(g,maxDegree2);
		
		int[] numberOfMatches = new int[numberOfNodes1]; // gives the number of relevant elements in the second array of possibleMatches 
		int[][] possibleMatches = new int[numberOfNodes1][numberOfNodes1]; // first element is the node, second is a list of potential matches
		
		for(int n1 = 0; n1 < numberOfNodes1; n1++) {
			int i = 0;
			for(int n2 = 0; n2 < numberOfNodes2; n2++) {
				if(matrix1[n1][n1] != matrix2[n2][n2]) { // check that they have the same number of self sourcing edges
					continue;
				}
				if(degrees1[n1] != degrees2[n2]) { // make sure the number of connecting edges is equal
					continue;
				}
				possibleMatches[n1][i] = n2;
				i++;
			}
			if(i == 0) {
//System.out.println("Not isomorphic: no possible match for a node");
failOnNodeMatches++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
				return false;
			}
			numberOfMatches[n1] = i;
//System.out.println("node "+n1+" number of matches "+numberOfMatches[n1]+" possibleMatches "+Arrays.toString(possibleMatches[n1]));
		}

		
		int[] matchesIndex = new int[numberOfNodes1]; // current indexes for the search
		Arrays.fill(matchesIndex,-1);
		Arrays.fill(matches1,-1);
		Arrays.fill(matches2,-1);

		// backtracking search here
		int currentNode = 0;
		matchesIndex[currentNode] = 0;
		while(currentNode < numberOfNodes1) {
//System.out.println("current progress "+Arrays.toString(matchesIndex));

			if(matchesIndex[currentNode] == numberOfMatches[currentNode]) { // backtrack here to previous node if all nodes have been tried
//System.out.println("Backtracking from node "+ currentNode+ " matched node "+matches1[currentNode]);
				if(matches1[currentNode] != -1) {
					matches2[matches1[currentNode]] = -1;
				}
				matches1[currentNode] = -1;
				matchesIndex[currentNode] = -1;
				matchesIndex[currentNode]= 0;
				currentNode--;
				if(currentNode == -1) {
//System.out.println("Not isomorphic: brute force");
failOnBruteForce++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
					return false;
				}
				if(matches1[currentNode] != -1) { // reset the previous match
					matches2[matches1[currentNode]] = -1;
				}
				matches1[currentNode] = -1; // reset the previous match
				matchesIndex[currentNode]++; // increment to the next node of the previous
				continue; // might have to happen multiple times
			}

			
			int possibleMatch = possibleMatches[currentNode][matchesIndex[currentNode]];
			if(isAMatch(currentNode,possibleMatch)) { // successful match, try the next node
//System.out.println("SUCCESSFUL match node "+ currentNode+" with node "+possibleMatch);
				matches1[currentNode] = possibleMatch;
				matches2[possibleMatch] = currentNode;
				currentNode++;
				if(currentNode == numberOfNodes1) {
//System.out.println("Isomorphic");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
					return true;
				}
				matchesIndex[currentNode] = 0;
			} else {
//System.out.println("Not a successful match node "+ currentNode+" with node "+possibleMatch);
				matchesIndex[currentNode]++; // fail so try the next node
			}

		}

// Never gets to here
System.out.println("Isomorphic - Should never get to here");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-startTime;
startTime = -1;		
*/		return true;
		
	}

	

	

	/**
	 * Check to see if the matched neighbours of n1 are neigbours of n2.
	 * Checks number of connecting edges, assumes checks on number of neighbours for each node has been performed.
	 * 
	 * @param n1 node in fastGraph
	 * @param n2 node in g
	 * @return true if the neighbours match, false otherwise
	 */
	private boolean isAMatch(int n1, int n2) {
		
/*		
//if(n1 == 2 && n2 == 1) {
//System.out.println("AAA");
//}
		if(matches1[n1] != -1) {
			return false;
		}
		if(matches2[n2] != -1) {
			return false;
		}
		
		HashSet<Integer> n1Neighbours = neighbours1.get(n1);
		HashSet<Integer> n2Neighbours = neighbours2.get(n2);

		int numberOfn1NeigboursMatched = 0;
		for(int node : n1Neighbours) {
			
			
			int matchNode = matches1[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n2Neighbours.contains(matchNode)) { // a neighbour of n1 has a matched node that is not a neigbour of n2
				return false;
			}
			

			
			numberOfn1NeigboursMatched++;
		}

		// now test it the other way - are all matched neighbours of n2 neighbours of n1
		int numberOfn2NeigboursMatched = 0;
		for(int node : n2Neighbours) {
			int matchNode = matches2[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n1Neighbours.contains(matchNode)) { // a neighbour of n2 has a matched node that is not a neighbour of n1
				return false;
			}
			numberOfn2NeigboursMatched++;
		}
		
		// test the same number of matches
		if(numberOfn1NeigboursMatched != numberOfn2NeigboursMatched) {
			return false;
		}
*/

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
