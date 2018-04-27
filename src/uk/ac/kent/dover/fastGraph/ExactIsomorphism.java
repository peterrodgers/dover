package uk.ac.kent.dover.fastGraph;


import java.util.*;

import uk.ac.kent.dover.fastGraph.comparators.*;
import uk.ac.kent.displayGraph.Graph;

/**
 * Testing the structural similarity of two FastGraphs. Now splits the graph
 * into connected components and tests individual components.
 * 
 * @author Peter Rodgers
 *
 */
public class ExactIsomorphism {

	private int DECIMAL_PLACES = 6; // number of decimal places to round to
	
	private FastGraph fastGraph;
	private boolean directed; // if true, treat the graph as directed, false undirected
	private boolean nodeLabels; // if true, use node label comparison, if false ignore node labels
	ArrayList<FastGraph> connectedList1;
	ArrayList<ExactIsomorphism> eiList1;
	private AdjacencyMatrix am1;
	private AdjacencyMatrix am2;
	private int[][] matrix1;
	private int[][] matrix2;
	private int[][] inMatrix1;
	private int[][] inMatrix2;
	private int[][] outMatrix1;
	private int[][] outMatrix2;
	private double[] eigenvalues1;
	private double[] eigenvalues2;
	
	private boolean connected; // set in init() if the graph is connected
	private SimpleNodeLabelComparator nodeLabelComparator;
	
	private int[] matches1;
	private int[] matches2;

	private int[] degrees1;
	private int[] degrees2;
	
	private int[] inDegrees1;
	private int[] inDegrees2;
	private int[] outDegrees1;
	private int[] outDegrees2;

	private int[] degreeBuckets1; // how many of nodes each degree
	private int[] degreeBuckets2; // how many of nodes each degree
	
	private int[] inDegreeBuckets1; // how many of nodes each degree
	private int[] inDegreeBuckets2; // how many of nodes each degree
	private int[] outDegreeBuckets1; // how many of nodes each degree
	private int[] outDegreeBuckets2; // how many of nodes each degree
	
	private int maxDegree1;
	private int maxDegree2;

	private int maxInDegree1;
	private int maxInDegree2;
	private int maxOutDegree1;
	private int maxOutDegree2;

	private ArrayList<HashSet<Integer>> neighbours1;  // Non self-sourcing neighbour nodes for each node
	private ArrayList<HashSet<Integer>> neighbours2;  // Non self-sourcing neighbour nodes for each node

	private ArrayList<HashSet<Integer>> inNeighbours1;  // Non self-sourcing neighbour nodes for each node
	private ArrayList<HashSet<Integer>> inNeighbours2;  // Non self-sourcing neighbour nodes for each node
	private ArrayList<HashSet<Integer>> outNeighbours1;  // Non self-sourcing neighbour nodes for each node
	private ArrayList<HashSet<Integer>> outNeighbours2;  // Non self-sourcing neighbour nodes for each node

	private static int numberOfIsomorphismTests = 0;
	private static int numberOfOldIsomorphismTests = 0;
	private static int numberOfEigenvalueTests = 0;
	
	private static long timeForIsomorphismTests = 0;
	private static long timeForBruteForceTests = 0;
	private static long timeForOldIsomorphismTests = 0;
	private static long timeForEigenvalueTests = 0;
	private static long isomorphismStartTime = -1;
	private static long bruteForceStartTime = -1;
	
	private static int failOnConnectedness = 0;
	private static int failOnNodeCount = 0;
	private static int failOnEdgeCount = 0;
	private static int failOnEigenvalues = 0;
	private static int failOnDegreeComparison = 0;
	private static int failOnNodeMatches = 0;
	private static int failOnBruteForce = 0;
	private static int succeed = 0;

	
	public static void main(String [] args) {
		
		FastGraph g1,g2;
		
		try {
			
			int nodes = 50;
			int edges = 500;
			long time;
			while(true) {
				g1 = FastGraph.randomGraphFactory(nodes, edges, false);
				g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1, System.currentTimeMillis(), false);
				time = System.currentTimeMillis();
				if(ExactIsomorphism.isomorphic(g1, g2, true)) {
					System.out.println("Ids changed directed isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0);
				} else {
					System.out.println("NOT ISOMORPHIC Ids changed directed isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0+" saving");
					g1.saveBuffers(".", System.currentTimeMillis()+"Q");
					g2.saveBuffers(".", System.currentTimeMillis()+"R");
				}
				
				g2 = FastGraph.randomGraphFactory(nodes, edges, false);
				time = System.currentTimeMillis();
				if(ExactIsomorphism.isomorphic(g1, g2, false)) {
					System.out.println("Random undirected isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0);
				} else {
					System.out.println("Random undirected not isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0);
				}
				time = System.currentTimeMillis();
				if(ExactIsomorphism.isomorphic(g1, g2, true)) {
					System.out.println("Random directed isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0);
				} else {
					System.out.println("Random directed not isomorphic nodes "+nodes+" edges "+edges+" time "+(System.currentTimeMillis()-time)/1000.0);
				}
				nodes *= 1.1;
				edges *= 1.1;
				System.out.println("NODES "+nodes+" EDGES "+edges);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 *
	 * Create an ExactIsomorphism before running isomorphic for undirected graphs.
	 * No node matching test is done.
	 * This makes multiple tests against one graph to be more efficient as data
	 * for that graph does not need to be recreated.
	 * 
	 * @param fastGraph one graph to be tested.
	 *
	 */
	public ExactIsomorphism(FastGraph fastGraph) {

		this.fastGraph = fastGraph;
		this.directed = false;
		this.nodeLabels = false;
		init();
	}

	/**
	 *
	 * Create an ExactIsomorphism before running isomorphic.
	 * No node matching test is done.
	 * This makes multiple tests against one graph to be more efficient as
	 * data for that graph does not need to be recreated.
	 * 
	 * @param fastGraph one graph to be tested.
	 * @param directed true if the graphs should be treated as directed, false if they are undirected
	 */
	public ExactIsomorphism(FastGraph fastGraph, boolean directed) {

		this.fastGraph = fastGraph;
		this.directed = directed;
		this.nodeLabels = false;
		init();
	}
	
	/**
	 *
	 * Create an ExactIsomorphism before running isomorphic.
	 * Node matching test is done if a nodeComparator is passed, e.g. for comparing node labels.
	 * This makes multiple tests against one graph to be more efficient as
	 * data for that graph does not need to be recreated.
	 * 
	 * @param fastGraph one graph to be tested.
	 * @param directed true if the graphs should be treated as directed, false if they are undirected
	 * @param nodeLabels if true compares node labels for a match, if false ignores node labels.
	 * g1 in the node comparator should be the first parameter, g2 should be the graph to be tested for isomorphism
	 */
	public ExactIsomorphism(FastGraph fastGraph, boolean directed, boolean nodeLabels) {

		this.fastGraph = fastGraph;
		this.directed = directed;
		this.nodeLabels = nodeLabels;
		init();
	}
	

	/**
	 * initialize the class
	 */
	private void init() {

		Connected c = new Connected();
		connected = c.connected(fastGraph);
		
		if(!connected) {
			connectedList1 = fastGraph.breakIntoConnectedComponents();
			eiList1 = new ArrayList<>();
			for(FastGraph g : connectedList1) {
				ExactIsomorphism ei = new ExactIsomorphism(g,directed,nodeLabels);
				eiList1.add(ei);
				
			}
		}
		
		am1 = new AdjacencyMatrix(fastGraph);
		if(fastGraph.getNumberOfNodes() == 0) {
			matrix1 = new int[0][0];
			eigenvalues1 = new double[0];
		} else {
			matrix1 = am1.buildIntAdjacencyMatrix();
			eigenvalues1 = am1.findEigenvalues(matrix1);
			eigenvalues1 = Util.roundArray(eigenvalues1,DECIMAL_PLACES);
			
			if(directed) {
				inMatrix1 = am1.buildIntInAdjacencyMatrix();
				outMatrix1 = am1.buildIntOutAdjacencyMatrix();
			}
		}

		matches1 = new int[fastGraph.getNumberOfNodes()];
		matches2 = new int[fastGraph.getNumberOfNodes()];

		if(!directed) {
			degrees1 = fastGraph.findDegrees();
			
			maxDegree1 = fastGraph.maximumDegree();
	
			degreeBuckets1 = new int[maxDegree1+1];
			fastGraph.findDegreeBuckets(degreeBuckets1,degrees1);
			
			neighbours1 = findNeighbours(fastGraph,maxDegree1);
		} else {
			inDegrees1 = fastGraph.findInDegrees();
			outDegrees1 = fastGraph.findOutDegrees();
			
			maxInDegree1 = fastGraph.maximumInDegree();
			maxOutDegree1 = fastGraph.maximumOutDegree();
	
			inDegreeBuckets1 = new int[maxInDegree1+1];
			fastGraph.findDegreeBuckets(inDegreeBuckets1,inDegrees1);
			outDegreeBuckets1 = new int[maxOutDegree1+1];
			fastGraph.findDegreeBuckets(outDegreeBuckets1,outDegrees1);
			
			inNeighbours1 = findInNeighbours(fastGraph,maxInDegree1);
			outNeighbours1 = findOutNeighbours(fastGraph,maxOutDegree1);
		}
	}
	
	/**
	 * Call this after getting true from a call to isomorphism().
	 * 
	 * @return the nodes matched from the graph passed to the constructor to the graph
	 * passed to {@link isomorphic}, if the last call to isomorphism() returned true, undefined otherwise
	 */
	public int[] getLastMatch() {
		return matches1;
	}

	
	/**
	 * Equality of graphs. Returns a mapping if this graph is equal
	 * to the given graph. Resultant mapping on returning
	 * true can be found with {@link #getLastMatch()}
	 *
	 * @param g the graph to compare
	 * @return true if there is an equality with the given graph, null if is not.
	 */
	public boolean isomorphic(FastGraph g) {
		
		if(connected) {
			return(isomorphicConnected(g));
		}

		ArrayList<FastGraph> connectedList2 = g.breakIntoConnectedComponents();
		if(connectedList1.size() != connectedList2.size()) {
Debugger.log("Not isomorphic: different number of connected components");
failOnConnectedness++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;
			return false;
		}
		
		// if there are multiple components, we have to iterate through them
		// this discards all the speed up in init(), which might be fixed later
		
		for(int i = 0; i < connectedList1.size(); i++) {
			
			ExactIsomorphism ei = eiList1.get(i);
			
			boolean found = false;
			int foundIndex = 0;
			for(FastGraph g2 : connectedList2) {
				if(ei.isomorphic(g2)) {
					found = true;
					break;
				}
				foundIndex++;
			}
			if(!found) {
				return false;
			} else {
				connectedList2.remove(foundIndex);
			}

		}
		
		return true;
		
	}
	
	/**
	 * Will actually return isomorphism of any graph, connected or disconnected
	 * but is inefficient for sparse graphs.
	 * 
	 * @param g
	 * @return
	 */
	private boolean isomorphicConnected(FastGraph g) {
		
		nodeLabelComparator = null;
		if(nodeLabels) {
			nodeLabelComparator = new SimpleNodeLabelComparator(fastGraph, g);
		}
		
		boolean ret = true;
		if(directed) {
			ret = directedIsomorphic(g);
		} else {
			ret = undirectedIsomorphic(g);
		}
		return ret;
		
	}


	
	/**
	 * Equality of undirected graphs. Returns a mapping if this graph is equal
	 * to the given graph.
	 *
	 * @param g the graph to compare
	 * @return true if there is an equality with the given graph, null if is not.
	 */
	public boolean undirectedIsomorphic(FastGraph g) {
numberOfIsomorphismTests++;
isomorphismStartTime = System.currentTimeMillis();

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
Debugger.log("Isomorphic: empty graphs");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return true;
		}

		// commented out because the connected components code subsumes this
		// Connected c = new Connected();
		//if(!c.connected(g1) && c.connected(g2)) {
		//	return false;
		//}
		//if(c.connected(g1) && !c.connected(g2)) {
		//	return false;
		//}

		if(numberOfNodes1 != numberOfNodes2) {
Debugger.log("Not isomorphic: different number of nodes");
failOnNodeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
				
		if(numberOfEdges1 != numberOfEdges2) {
Debugger.log("Not isomorphic: different number of edges");
failOnEdgeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
		
		degrees2 = g.findDegrees();
		
		maxDegree2 = g.maximumDegree();

		// check the number of nodes at each degree
		degreeBuckets2 = new int[maxDegree2+1];
		g.findDegreeBuckets(degreeBuckets2,degrees2);
		if(!Arrays.equals(degreeBuckets1, degreeBuckets2)) {
Debugger.log("Not isomorphic: different quantities of nodes with the same degree");
failOnDegreeComparison++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}

		
		
		am2 = new AdjacencyMatrix(g);
		matrix2 = am2.buildIntAdjacencyMatrix();
		eigenvalues2 = am2.findEigenvalues(matrix2);
		eigenvalues2 = Util.roundArray(eigenvalues2, DECIMAL_PLACES);
		if(!compareEigenValues(eigenvalues2)) {
Debugger.log("Not isomorphic: eigenvalues are different");
failOnEigenvalues++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
		
if(isomorphismStartTime == -1) {
	isomorphismStartTime = System.currentTimeMillis();
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
				if(nodeLabels) {
					if(nodeLabelComparator.compare(n1, n2) != 0) {
						continue;
					}
				}
				possibleMatches[n1][i] = n2;
				i++;
			}
			if(i == 0) {
Debugger.log("Not isomorphic: no possible match for node "+n1+" from g1");
failOnNodeMatches++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
				return false;
			}
			numberOfMatches[n1] = i;
//System.out.println("node "+n1+" number of matches "+numberOfMatches[n1]+" possibleMatches "+Arrays.toString(possibleMatches[n1]));
		}

		bruteForceStartTime = System.currentTimeMillis();
		
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
Debugger.log("Not isomorphic: brute force");
failOnBruteForce++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
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
			if(isAnUndirectedMatch(currentNode,possibleMatch)) { // successful match, try the next node
//System.out.println("SUCCESSFUL match node "+ currentNode+" with node "+possibleMatch);
				matches1[currentNode] = possibleMatch;
				matches2[possibleMatch] = currentNode;
				currentNode++;
				if(currentNode == numberOfNodes1) {
Debugger.log("Isomorphic");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
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
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
		return true;
		
	}
	
	
	

	/**
	 * Check to see if the matched neighbours of n1 are neighbours of n2.
	 * Checks number of connecting edges, assumes checks on number of neighbours for each node has been performed.
	 * 
	 * @param n1 node in fastGraph
	 * @param n2 node in g
	 * @return true if the neighbours match, false otherwise
	 */
	private boolean isAnUndirectedMatch(int n1, int n2) {
		
		
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
			
			if(nodeLabels) {
				if(nodeLabelComparator.compare(node, matchNode) != 0) { // the matched neighbours have different labels
					return false;
				}
			}

			// removed edges Count check, never seems to be used
			int edgeCount = matrix1[n1][node];
			int matchedEdgeCount = matrix2[n2][matchNode];
			if(edgeCount != matchedEdgeCount) { // different number of edge between the nodes and the matched nodes
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


		return true;
	}
	
	

	/**
	 * Equality of directed graphs. Returns a mapping if this graph is equal
	 * to the given graph.
	 *
	 * @param g the graph to compare
	 * @return true if there is an equality with the given graph, null if is not.
	 */
	private boolean directedIsomorphic(FastGraph g) {
numberOfIsomorphismTests++;
isomorphismStartTime = System.currentTimeMillis();
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
Debugger.log("Isomorphic: empty graphs");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return true;
		}

		// commented out because the connected components code subsumes this
		//Connected c = new Connected();
		//if(!c.connected(g1) && c.connected(g2)) {
		//	return false;
		//}
		//if(c.connected(g1) && !c.connected(g2)) {
		//	return false;
		//}

		if(numberOfNodes1 != numberOfNodes2) {
Debugger.log("Not isomorphic: different number of nodes");
failOnNodeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
				
		if(numberOfEdges1 != numberOfEdges2) {
Debugger.log("Not isomorphic: different number of edges");
failOnEdgeCount++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
		
		inDegrees2 = g.findInDegrees();
		outDegrees2 = g.findOutDegrees();
		
		maxInDegree2 = g.maximumInDegree();
		maxOutDegree2 = g.maximumOutDegree();

		// check the number of nodes at each degree
		inDegreeBuckets2 = new int[maxInDegree2+1];
		g.findDegreeBuckets(inDegreeBuckets2,inDegrees2);
		if(!Arrays.equals(inDegreeBuckets1, inDegreeBuckets2)) {
Debugger.log("Not isomorphic: different quantities of nodes with the same indegree");
failOnDegreeComparison++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}

		outDegreeBuckets2 = new int[maxOutDegree2+1];
		g.findDegreeBuckets(outDegreeBuckets2,outDegrees2);
		if(!Arrays.equals(outDegreeBuckets1, outDegreeBuckets2)) {
//System.out.println("Not isomorphic: different quantities of nodes with the same outdegree");
failOnDegreeComparison++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
		
		am2 = new AdjacencyMatrix(g);
		matrix2 = am2.buildIntAdjacencyMatrix();
		eigenvalues2 = am2.findEigenvalues(matrix2);
		eigenvalues2 = Util.roundArray(eigenvalues2, DECIMAL_PLACES);
		if(!compareEigenValues(eigenvalues2)) {
Debugger.log("Not isomorphic: eigenvalues are different");
failOnEigenvalues++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
			return false;
		}
		
		inMatrix2 = am2.buildIntInAdjacencyMatrix();
		outMatrix2 = am2.buildIntOutAdjacencyMatrix();

		
if(isomorphismStartTime == -1) {
	isomorphismStartTime = System.currentTimeMillis();
}
		
		inNeighbours2 = findInNeighbours(g,maxInDegree2);
		outNeighbours2 = findOutNeighbours(g,maxOutDegree2);
		
		int[] numberOfMatches = new int[numberOfNodes1]; // gives the number of relevant elements in the second array of possibleMatches 
		int[][] possibleMatches = new int[numberOfNodes1][numberOfNodes1]; // first element is the node, second is a list of potential matches
		
		for(int n1 = 0; n1 < numberOfNodes1; n1++) {
			int i = 0;
			for(int n2 = 0; n2 < numberOfNodes2; n2++) {
				if(matrix1[n1][n1] != matrix2[n2][n2]) { // check that they have the same number of self sourcing edges
					continue;
				}
				if(inDegrees1[n1] != inDegrees2[n2]) { // make sure the number of connecting in edges is equal
					continue;
				}
				if(outDegrees1[n1] != outDegrees2[n2]) { // make sure the number of connecting out edges is equal
					continue;
				}
				if(nodeLabels) {
					if(nodeLabelComparator.compare(n1, n2) != 0) {
						continue;
					}
				}
				possibleMatches[n1][i] = n2;
				i++;
			}
			if(i == 0) {
Debugger.log("Not isomorphic: no possible match for node "+n1+" from g1");
failOnNodeMatches++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
				return false;
			}
			numberOfMatches[n1] = i;
//System.out.println("node "+n1+" number of matches "+numberOfMatches[n1]+" possibleMatches "+Arrays.toString(possibleMatches[n1]));
		}

		bruteForceStartTime = System.currentTimeMillis();
		
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
Debugger.log("Not isomorphic: brute force");
failOnBruteForce++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
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
			if(isADirectedMatch(currentNode,possibleMatch)) { // successful match, try the next node
//System.out.println("SUCCESSFUL match node "+ currentNode+" with node "+possibleMatch);
				matches1[currentNode] = possibleMatch;
				matches2[possibleMatch] = currentNode;
				currentNode++;
				if(currentNode == numberOfNodes1) {
Debugger.log("Isomorphic");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
					return true;
				}
				matchesIndex[currentNode] = 0;
			} else {
//System.out.println("Not a successful match node "+ currentNode+" with node "+possibleMatch);
				matchesIndex[currentNode]++; // fail so try the next node
			}

		}

// Never gets to here
//System.out.println("Isomorphic - Should never get to here");
succeed++;
timeForIsomorphismTests += System.currentTimeMillis()-isomorphismStartTime;
isomorphismStartTime = -1;		
timeForBruteForceTests += System.currentTimeMillis()-bruteForceStartTime;
bruteForceStartTime = -1;		
		return true;
		
	}

	
	/**
	 * Check to see if the matched neighbours of n1 are neighbours of n2.
	 * Checks number of connecting in and out edges, assumes checks on number of neighbours for each node has been performed.
	 * 
	 * @param n1 node in fastGraph
	 * @param n2 node in g
	 * @return true if the neighbours match, false otherwise
	 */
	private boolean isADirectedMatch(int n1, int n2) {

		if(matches1[n1] != -1) {
			return false;
		}
		if(matches2[n2] != -1) {
			return false;
		}
		
		// InNeighbours first
		
		HashSet<Integer> n1InNeighbours = inNeighbours1.get(n1);
		HashSet<Integer> n2InNeighbours = inNeighbours2.get(n2);

		int numberOfn1NeigboursMatched = 0;
		for(int node : n1InNeighbours) {
			
			int matchNode = matches1[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n2InNeighbours.contains(matchNode)) { // a neighbour of n1 has a matched node that is not a neigbour of n2
				return false;
			}
			
			if(nodeLabels) {
				if(nodeLabelComparator.compare(node, matchNode) != 0) { // the matched neighbours have different labels
					return false;
				}
			}

			int edgeCount = inMatrix1[n1][node];
			int matchedEdgeCount = inMatrix2[n2][matchNode];
			if(edgeCount != matchedEdgeCount) { // different number of edge between the nodes and the matched nodes
				return false;
			}
			
			numberOfn1NeigboursMatched++;
		}

		// now test it the other way - are all matched neighbours of n2 neighbours of n1
		int numberOfn2NeigboursMatched = 0;
		for(int node : n2InNeighbours) {
			int matchNode = matches2[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n1InNeighbours.contains(matchNode)) { // a neighbour of n2 has a matched node that is not a neighbour of n1
				return false;
			}
			numberOfn2NeigboursMatched++;
		}
		
		// test the same number of in node matches
		if(numberOfn1NeigboursMatched != numberOfn2NeigboursMatched) {
			return false;
		}

		// Now test OutNeighbours
		
		HashSet<Integer> n1OutNeighbours = outNeighbours1.get(n1);
		HashSet<Integer> n2OutNeighbours = outNeighbours2.get(n2);

		numberOfn1NeigboursMatched = 0;
		for(int node : n1OutNeighbours) {
			
			int matchNode = matches1[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n2OutNeighbours.contains(matchNode)) { // a neighbour of n1 has a matched node that is not a neigbour of n2
				return false;
			}
			
			if(nodeLabels) {
				if(nodeLabelComparator.compare(node, matchNode) != 0) { // the matched neighbours have different labels
					return false;
				}
			}
			int edgeCount = outMatrix1[n1][node];
			int matchedEdgeCount = outMatrix2[n2][matchNode];
			if(edgeCount != matchedEdgeCount) { // different number of edge between the nodes and the matched nodes
				return false;
			}
			
			
			numberOfn1NeigboursMatched++;
		}

		// now test it the other way - are all matched neighbours of n2 neighbours of n1
		numberOfn2NeigboursMatched = 0;
		for(int node : n2OutNeighbours) {
			int matchNode = matches2[node];
			if(matchNode == -1) { // no match, so nothing to do here
				continue;
			}
			if(!n1OutNeighbours.contains(matchNode)) { // a neighbour of n2 has a matched node that is not a neighbour of n1
				return false;
			}
			numberOfn2NeigboursMatched++;
		}
		
		// test the same number of out node matches
		if(numberOfn1NeigboursMatched != numberOfn2NeigboursMatched) {
			return false;
		}

		return true;
	}



	/**
	 * Generate a string that can be used to put graph in buckets before final brute force comparison.
	 * 
	 * @return a string based on calculated values
	 */
	protected String generateStringForHash() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.toString(fastGraph.getNumberOfNodes()));
		sb.append(",");
		sb.append(Integer.toString(fastGraph.getNumberOfEdges()));
		sb.append(Arrays.toString(degreeBuckets1));
		sb.append(Arrays.toString(eigenvalues1));
		sb.append(generateTimeString());
		return sb.toString();

	}
	
	/**
	 * Generates a count of the nodes at each age.<br>
	 * Uses relative ages, so if a graph had 1 node of age 4, 2 of age 5 and 3 of age 6, would be represented as [1,2,3]
	 * @return The age string
	 */
	protected String generateTimeString() {
		int maxAge = fastGraph.findMaximumNodeAge();
		int minAge = fastGraph.findMinimumNodeAge();
		//Debugger.log("minAge: " + minAge + " maxAge: " + maxAge);
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i = minAge; i < maxAge; i++) {
			sb.append(fastGraph.countNodesOfAge(i));
			sb.append(",");
		}
		sb.append(fastGraph.countNodesOfAge(maxAge));
		sb.append("]");
		return sb.toString();
	}


	/**
	 * gives the neighbours of nodes in g, without duplicates and without self sourcing.
	 * 
	 * @param g the graph
	 * @param maxDegree The maximum degree of the nodes
	 * @return the neighbours for each node in the graph
	 */
	public static ArrayList<HashSet<Integer>> findNeighbours(FastGraph g, int maxDegree) {
		
		ArrayList<HashSet<Integer>> ret = new ArrayList<HashSet<Integer>>(g.getNumberOfNodes());
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			HashSet<Integer> neighbours = new HashSet<Integer>(maxDegree);
			int[] connections = g.getNodeConnectingNodesOfSameAge(n);
			for(int i = 0; i < connections.length; i++) {
				Integer connectingNode = connections[i];
				if(n == connectingNode) {
					continue;
				}
				if(!neighbours.contains(connectingNode)) {
					neighbours.add(connectingNode);
				}
			}
			ret.add(neighbours);
		}
		return ret;
	}
	
	/**
	 * gives the neighbours of nodes in g which are connected by edges pointing at the node,
	 * without duplicates and without self sourcing.
	 * 
	 * @param g the graph
	 * @param maxDegree The maximum indegree of the nodes
	 * @return the neighbours for each node in the graph
	 */
	public static ArrayList<HashSet<Integer>> findInNeighbours(FastGraph g, int maxDegree) {
		
		ArrayList<HashSet<Integer>> ret = new ArrayList<HashSet<Integer>>(g.getNumberOfNodes());
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			HashSet<Integer> neighbours = new HashSet<Integer>(maxDegree);
			int[] connections = g.getNodeConnectingInNodesOfSameAge(n);
			for(int i = 0; i < connections.length; i++) {
				Integer connectingNode = connections[i];
				if(n == connectingNode) {
					continue;
				}
				if(!neighbours.contains(connectingNode)) {
					neighbours.add(connectingNode);
				}
			}
			ret.add(neighbours);
		}
		return ret;
	}
	
	
	/**
	 * gives the neighbours of nodes in g which are connected by edges pointing away from the node,
	 * without duplicates and without self sourcing.
	 * 
	 * @param g the graph
	 * @param maxDegree The maximum indegree of the nodes
	 * @return the neighbours for each node in the graph
	 */
	public static ArrayList<HashSet<Integer>> findOutNeighbours(FastGraph g, int maxDegree) {
		
		ArrayList<HashSet<Integer>> ret = new ArrayList<HashSet<Integer>>(g.getNumberOfNodes());
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			HashSet<Integer> neighbours = new HashSet<Integer>(maxDegree);
			int[] connections = g.getNodeConnectingOutNodesOfSameAge(n);
			for(int i = 0; i < connections.length; i++) {
				Integer connectingNode = connections[i];
				if(n == connectingNode) {
					continue;
				}
				if(!neighbours.contains(connectingNode)) {
					neighbours.add(connectingNode);
				}
			}
			ret.add(neighbours);
		}
		return ret;
	}


	/**
	 * Check if the two graphs have the same structure.
	 * 
	 * @param g the FastGraph from which a subgraph will be found
	 * @param nodes the nodes in g2 that form the subgraph
	 * @param edges the edges in g2 that form the subgraph
	 * @return true if the g1 and the subgraph of g2 are isomorphic, false otherwise
	 */
	public boolean isomorphic(FastGraph g, int[] nodes, int[] edges) {

		FastGraph subGraph = g.generateGraphFromSubgraph(nodes,edges);
		
		boolean iso = isomorphic(subGraph);

		return iso;
	}
		


	/**
	 * Check if two graphs have the same structure. Use the constructor and non static method if
	 * lots of comparisons are going to be made against the same graph.
	 * 
	 * @param g1 one FastGraph to be tested
	 * @param g2 the other FastGraph to be tested
	 * @param directed false if the graphs are treated as undirected, true if they are directed
	 * @return true if g1 and g2 are isomorphic, false otherwise
	 */
	public static boolean isomorphic(FastGraph g1, FastGraph g2, boolean directed) {
		ExactIsomorphism ei = new ExactIsomorphism(g1,directed);
		boolean ret = ei.isomorphic(g2);
		return ret;
	}
	
	/**
	 * Check if two graphs have the same structure. Use the constructor and non static method if
	 * lots of comparisons are going to be made against the same graph.
	 * 
	 * @param g1 one FastGraph to be tested
	 * @param g2 the other FastGraph to be tested
	 * @param directed false if the graphs are treated as undirected, true if they are directed
	 * @param nodeLabels if true the node labels must be equal for a match, if false then node labels are ignored
	 * @return true if g1 and g2 are isomorphic, false otherwise
	 */
	public static boolean isomorphic(FastGraph g1, FastGraph g2, boolean directed, boolean nodeLabels) {
		ExactIsomorphism ei = new ExactIsomorphism(g1,directed,nodeLabels);
		boolean ret = ei.isomorphic(g2);
		return ret;
	}
	
	/**
	 * Check if two undirected graphs have the same structure. Use the constructor and non static method if
	 * lots of comparisons are going to be made against the same graph.
	 * 
	 * @param g1 one FastGraph to be tested
	 * @param g2 the other FastGraph to be tested
	 * @return true if g1 and g2 are isomorphic, false otherwise
	 */
	public static boolean isomorphic(FastGraph g1, FastGraph g2) {
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		boolean ret = ei.isomorphic(g2);
		return ret;
	}
	

	/**
	 * Check if the given graph has the same structure as the graph passed to the constructor.
	 * Only for undirected graphs.
	 * 
	 * @param g the FastGraph to be tested
	 * @return true if the two graphs are isomorphic, false otherwise
	 */
	public boolean isomorphicOld(FastGraph g) {
		
		Graph displayGraph = fastGraph.generateDisplayGraph();
		Graph dg = g.generateDisplayGraph();
		boolean iso = displayGraph.isomorphic(dg);
		return iso;
	}


	/**
	 * Compare graphs by their eigenvalues
	 * @param g the graph to compare
	 * @return true if they are equal by eigenvalue, false otherwise
	 */
	public boolean compareByEigenvalues(FastGraph g) {
		AdjacencyMatrix gam = new AdjacencyMatrix(g);
		int[][] gMatrix = gam.buildIntAdjacencyMatrix();
		double[] gEigenvalues = gam.findEigenvalues(gMatrix);
		gam = null;
		//System.gc();
		return compareEigenValues(gEigenvalues);
	}
	
	
	/**
	 * Compare eigenvalues
	 * @param values the values to compare
	 * @return true if the eigenvalues are equal, false otherwise
	 */
	public boolean compareEigenValues(double[] values) {
		boolean ret = Arrays.equals(eigenvalues1,values);

		return ret;
	}

	/**
	 * Generates a random graph isomorphic to the input one. Used for testing isomorphism as
	 * testing two isomorphic graphs guarantees that the brute force part of the algorithm is used.
	 * @param inGraph The starting graph
	 * @param seed The random seed
	 * @param direct If the graph is to be on heap (false) or off heap (true)
	 * @return The new graph, isomorphic to the old one but with swapped node ids.
	 */
	public static FastGraph generateRandomIsomorphicGraph(FastGraph inGraph, long seed, boolean direct) {
		ArrayList<NodeStructure> allNodes = new ArrayList<NodeStructure>();
		ArrayList<EdgeStructure> allEdges = new ArrayList<EdgeStructure>();
		
		Random r = new Random(seed);
		
		ArrayList<Integer> remainingNodes = new ArrayList<Integer>(inGraph.getNumberOfNodes());
		for(int i = 0; i < inGraph.getNumberOfNodes(); i++) {
			remainingNodes.add(i);
		}

		HashMap<Integer,Integer> inToNewNodeMapping = new HashMap<Integer,Integer>(inGraph.getNumberOfNodes()*3);
		int index = 0;
		while(!remainingNodes.isEmpty()) {
			int nodeIndex = r.nextInt(remainingNodes.size());
			int node = remainingNodes.get(nodeIndex);
			remainingNodes.remove(nodeIndex);
			String label = inGraph.getNodeLabel(node);
			int weight = inGraph.getNodeWeight(node);
			byte type = inGraph.getNodeType(node);
			byte age = inGraph.getNodeAge(node);
			NodeStructure ns = new NodeStructure(index,label,weight,type,age);
			allNodes.add(ns);
			inToNewNodeMapping.put(node, index);
			index++;
		}
		

		ArrayList<Integer> remainingEdges = new ArrayList<Integer>(inGraph.getNumberOfEdges());
		for(int i = 0; i < inGraph.getNumberOfEdges(); i++) {
			remainingEdges.add(i);
		}

		index = 0;
		while(!remainingEdges.isEmpty()) {
			int edgeIndex = r.nextInt(remainingEdges.size());
			int edge = remainingEdges.get(edgeIndex);
			remainingEdges.remove(edgeIndex);
			String label = inGraph.getEdgeLabel(edge);
			int weight = inGraph.getEdgeWeight(edge);
			byte type = inGraph.getEdgeType(edge);
			byte age = inGraph.getEdgeAge(edge);
			int oldNode1 = inGraph.getEdgeNode1(edge);
			int oldNode2 = inGraph.getEdgeNode2(edge);
			int node1 = inToNewNodeMapping.get(oldNode1);
			int node2 = inToNewNodeMapping.get(oldNode2);
			EdgeStructure es = new EdgeStructure(index,label,weight,type,age,node1,node2);
			allEdges.add(es);
			index++;
		}
		
		FastGraph g = FastGraph.structureFactory(inGraph.getName()+"-isomorphic",(byte)0,allNodes,allEdges,direct);
		
		return g;
	}
	
	

	/**
	 * Output timing
	 */
	public static void reportTimes() {
		if(numberOfIsomorphismTests > 0) {
			System.out.println("Isomorphism test average "+(timeForIsomorphismTests/(1000.0*numberOfIsomorphismTests))+" seconds total tests "+numberOfIsomorphismTests+" total time "+(timeForIsomorphismTests/1000.0)+" seconds");
		} else {
			System.out.println("Isomorphism total tests "+numberOfIsomorphismTests);
		}
/*		if(numberOfOldIsomorphismTests > 0) {
			System.out.println("Old Isomorphism test average "+(timeForOldIsomorphismTests/(1000.0*numberOfOldIsomorphismTests))+" seconds total tests "+numberOfOldIsomorphismTests+" total time "+(timeForOldIsomorphismTests/1000.0)+" seconds");
		}
		if(numberOfEigenvalueTests > 0) {
			System.out.println("Eigenvalue test average "+(timeForEigenvalueTests/(1000.0*numberOfEigenvalueTests))+" total tests "+numberOfEigenvalueTests+" total time "+(timeForEigenvalueTests/1000.0)+" seconds");
		} else {
			System.out.println("Eigenvalue total tests "+numberOfEigenvalueTests);
		}
		if(numberOfSubgraphsGenerated > 0) {
			System.out.println("Subgraph generation average "+(timeForSubgraphsGenerated/(1000.0*numberOfSubgraphsGenerated))+" seconds total generated "+numberOfSubgraphsGenerated+" total time "+(timeForSubgraphsGenerated/1000.0)+" seconds");
		}
*/	}

	
	/**
	 * Output counts
	 */
	public static void reportFailRatios() {
		
		double total = failOnConnectedness+failOnNodeCount+failOnEdgeCount+failOnEigenvalues+failOnDegreeComparison+failOnNodeMatches+failOnBruteForce+succeed;
		
		System.out.println("fail on Connectedness "+failOnConnectedness+" "+(100.0*failOnConnectedness/total)+" % of calls");
		System.out.println("fail on Node Count "+failOnNodeCount+" "+(100.0*failOnNodeCount/total)+" % of calls");
		System.out.println("fail on Edge Count "+failOnEdgeCount+" "+(100.0*failOnEdgeCount/total)+" % of calls");
		System.out.println("fail on Degree Comparison "+failOnDegreeComparison+" "+(100.0*failOnDegreeComparison/total)+" % of calls");
		System.out.println("fail on Eigenvalues "+failOnEigenvalues+" "+(100.0*failOnEigenvalues/total)+" % of calls");
		System.out.println("fail on Node Matches "+failOnNodeMatches+" "+(100.0*failOnNodeMatches/total)+" % of calls");
		System.out.println("fail on Brute Force "+failOnBruteForce+" "+(100.0*failOnBruteForce/total)+" % of calls");
		System.out.println("succeed "+succeed+" "+(100.0*succeed/total)+" "+" % of calls");

	}


	/**
	 * sets all the profiling counts and timing to zero
	 */
	public static void resetProfiling() {
		numberOfIsomorphismTests = 0;
		numberOfOldIsomorphismTests = 0;
		numberOfEigenvalueTests = 0;
		
		timeForIsomorphismTests = 0;
		timeForBruteForceTests = 0;
		timeForOldIsomorphismTests = 0;
		timeForEigenvalueTests = 0;
		isomorphismStartTime = -1;
		bruteForceStartTime = -1;
		
		failOnNodeCount = 0;
		failOnEdgeCount = 0;
		failOnEigenvalues = 0;
		failOnDegreeComparison = 0;
		failOnNodeMatches = 0;
		failOnBruteForce = 0;
		succeed = 0;
	
	}

}
