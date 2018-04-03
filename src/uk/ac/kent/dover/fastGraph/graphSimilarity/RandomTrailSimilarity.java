package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import edu.isi.karma.modeling.research.graphmatching.algorithms.VJAccess;
import uk.ac.kent.dover.fastGraph.*;

/**
 * A similarity method for graphs, using random trails. To achieve symmetry use the same object
 * in both g1 g2 and g2 g1 directions, as the same random seed will be applied.
 * 
 * @author Peter Rodgers
 *
 */
public class RandomTrailSimilarity extends GraphSimilarity {

	private boolean nodeLabels;
	private boolean directed;
	
	private long randomSeed;
	
	private int trailLength = 4; // maximum length for each trail
	private int trailsPerNode = 10; // number of random trails found for each node
	
	private double[][] costMatrix;
	private int[] mapping;

	public static void main(String [] args) {
		
		Debugger.enabled = false;
//		randomIncreasing();
		randomIsomorphismComparison();
		
		
		try {
			int i = 3855;
			double similarity1,similarity2;
			LinkedList<NodeStructure> addNodes;
			LinkedList<EdgeStructure> addEdges;
			FastGraph g1,g2;
			RandomTrailSimilarity rts;
			addNodes = new LinkedList<NodeStructure>();
			addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
			addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
			addEdges = new LinkedList<EdgeStructure>();
			addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 1));
			addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 0));
			addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 0, 1));
			g1 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

			addNodes = new LinkedList<NodeStructure>();
			addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
			addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
			addEdges = new LinkedList<EdgeStructure>();
			addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 0));
			addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 1));
			addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 0, 1));
			g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
			
			rts = new RandomTrailSimilarity(false, false, 999);
			rts.setTrailLength(4);
			rts.setTrailsPerNode(10);
			similarity1 = rts.similarity(g1, g2);
			similarity2 = rts.similarity(g2, g1);
			System.out.println(g1);
			System.out.println(g2);
			System.out.println("similarity1 "+similarity1+" similarity2 "+similarity2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void randomIsomorphismComparison() {
		
		try {
			
			RandomTrailSimilarity rts;
			double similarity1,similarity2;
			boolean isomorphic,directed,labels;
			FastGraph g1,g2;
			long g1Seed,g2Seed,rtsSeed;
			int trailLength = 5;
			int trailsPerNode = 10;
			int nodes = 10;
			int edges = 20;
			int i = 0;
			while(true) {
				boolean fail = false;
				i++;
				if(i%1000 == 0) {
					System.out.println("iteration: "+i);
				}
				g1Seed = i*111;
				g2Seed = i*333;
				rtsSeed = i*555;
				
				directed = true;
				labels = true;
				g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
				g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
//				g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1, g2Seed, false);
				isomorphic = ExactIsomorphism.isomorphic(g1, g2, directed,labels);
				rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
				rts.setTrailLength(trailLength);
				rts.setTrailsPerNode(trailsPerNode);
				similarity1 = rts.similarity(g1, g2);
				similarity2 = rts.similarity(g2, g1);
				if(Math.abs(similarity1-similarity2) > 0.001) {
					System.out.println("similarity1 "+similarity1+" similarity2 "+similarity2+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 < 0.001 && !isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 > 0.001 && isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				
				directed = false;
				labels = true;
				g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
				g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
				isomorphic = ExactIsomorphism.isomorphic(g1, g2, directed,labels);
				rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
				rts.setTrailLength(trailLength);
				rts.setTrailsPerNode(trailsPerNode);
				similarity1 = rts.similarity(g1, g2);
				similarity2 = rts.similarity(g2, g1);
				if(Math.abs(similarity1-similarity2) > 0.001) {
					System.out.println("similarity1 "+similarity1+" similarity2 "+similarity2+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 < 0.001 && !isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 > 0.001 && isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}

				directed = true;
				labels = false;
				g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
				g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
				isomorphic = ExactIsomorphism.isomorphic(g1, g2, directed,labels);
				rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
				rts.setTrailLength(trailLength);
				rts.setTrailsPerNode(trailsPerNode);
				similarity1 = rts.similarity(g1, g2);
				similarity2 = rts.similarity(g2, g1);
				if(Math.abs(similarity1-similarity2) > 0.001) {
					System.out.println("similarity1 "+similarity1+" similarity2 "+similarity2+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 < 0.001 && !isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 > 0.001 && isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}

				directed = false;
				labels = false;
				g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
				g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
				isomorphic = ExactIsomorphism.isomorphic(g1, g2, directed,labels);
				rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
				rts.setTrailLength(trailLength);
				rts.setTrailsPerNode(trailsPerNode);
				similarity1 = rts.similarity(g1, g2);
				similarity2 = rts.similarity(g2, g1);
				if(Math.abs(similarity1-similarity2) > 0.001) {
					System.out.println("similarity1 "+similarity1+" similarity2 "+similarity2+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 < 0.001 && !isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				if(similarity1 > 0.001 && isomorphic) {
					System.out.println("similarity1 "+similarity1+" isomorphic "+isomorphic+" "+ directed+" "+labels);
					fail = true;
				}
				
				if(fail) {
					System.out.println("Failed on i "+i);
//					System.out.println(g1);
//					System.out.println(g2);
				}

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void randomIncreasing() {
		
		try {
			
			RandomTrailSimilarity rts;
			double similarity;
			FastGraph g1,g2;
			int nodes = 50;
			int edges = 500;
			long time;
			while(true) {
				System.out.println("NODES "+nodes+" EDGES "+edges);
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
				
				time = System.currentTimeMillis();
				rts = new RandomTrailSimilarity(true,true,System.currentTimeMillis());
				rts.setTrailLength(4);
				rts.setTrailsPerNode(10);
				similarity = rts.similarity(g1, g2);
				System.out.println("similarity directed labels "+similarity+" time "+((System.currentTimeMillis()-time)/1000.0)+" seconds");
				
				time = System.currentTimeMillis();
				rts = new RandomTrailSimilarity(true,false,System.currentTimeMillis());
				rts.setTrailLength(4);
				rts.setTrailsPerNode(10);
				similarity = rts.similarity(g1, g2);
				System.out.println("similarity directed no labels "+similarity+" time "+((System.currentTimeMillis()-time)/1000.0)+" seconds");

				time = System.currentTimeMillis();
				rts = new RandomTrailSimilarity(false,false,System.currentTimeMillis());
				rts.setTrailLength(4);
				rts.setTrailsPerNode(10);
				similarity = rts.similarity(g1, g2);
				System.out.println("similarity undirected no labels "+similarity+" time "+((System.currentTimeMillis()-time)/1000.0)+" seconds");
				
				nodes *= 1.1;
				edges *= 1.1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * defaults to treating graph as undirected and no node label comparison.
	 * 
	 * @throws FastGraphException should not throw this as all required edit operations are present by default
	 */
	public RandomTrailSimilarity() throws FastGraphException {
		
		this.directed = false;
		this.nodeLabels = false;
		this.randomSeed = System.currentTimeMillis();
	}


	/**
	 * Constructor for specifying directed and use of node labels.
	 * 
	 * @param directed true if the graph is treated as directed, false if undirected
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param randomSeed set to System.currentTimeMillis() for true random
	 */
	public RandomTrailSimilarity(boolean directed, boolean nodeLabels, long randomSeed) {

		this.directed = directed;
		this.nodeLabels = nodeLabels;
		this.randomSeed = randomSeed;
		
	}

	/**
	 * gets the maximum trail length for each random trail
	 * @return the maximum trail length
	 */
	public int getTrailLength() {return trailLength;}
	
	/**
	 * gets the numbers of trails found for each node
	 * @return the number of trails
	 */
	public int getTrailsPerNode() {return trailsPerNode;}
	
	/**
	 * 
	 * @return the costMatrix, set after similarity is called.
	 */
	public double[][] getCostMatrix() {return costMatrix;}
	
	/**
	 * 
	 * @return the g1 nodes to g2 nodes mapping, set after similarity is called.
	 */
	public int[] getMapping() {return mapping;}

	/**
	 * alters the maximum trail length for each random trail.
	 * @param trailLength the length of trails
	 */
	public void setTrailLength(int trailLength) {this.trailLength = trailLength;}
	
	/**
	 * alters the numbers of trails found for each node.
	 * @param trailsPerNode the number of trails
	 */
	public void setTrailsPerNode(int trailsPerNode) {this.trailsPerNode = trailsPerNode;}



	/**
	 * This returns an similarity measure between the two graphs. 
	 * Zero means the graphs are isomorphic, greater values mean more dissimilarity.
	 * The method works by finding random trails from each node in g1, and seeing how
	 * much of each trail can be found in g2.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the similarity between two graphs.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
		
		int nodes1 = g1.getNumberOfNodes();
		int nodes2 = g2.getNumberOfNodes();
		int size = nodes1+nodes2;
		int maxNodes = nodes1;
		if(nodes2 > maxNodes) {
			maxNodes = nodes2;
		}
		
		if(maxNodes == 0) {
			return 0.0;
		}

		
		costMatrix = new double[size][size];
		
		double maxCost = 0;
		
		for(int y = 0; y < size; y++) { // g1 nodes
			for(int x = 0; x < size; x++) { // g2 nodes
				if(x < nodes2 && y < nodes1) { // top left of the matrix
					double trailCost = randomTrailCost(y,x,g1,g2);
					costMatrix[y][x] = trailCost;
					if(trailCost > maxCost) {
						maxCost = trailCost;
					}
				} else if(x >= nodes2 && y < nodes1) { // top right of the matrix
					if(x-nodes2 == y) { // cost of a node deletion appears on diagonal
						costMatrix[y][x] = -1; // this will be set to the largest value, 1 after normalization
					} else {
						costMatrix[y][x] = -2; // this will be set to MAX_VALUE
					}
				} else if(x < nodes2 && y >= nodes1) { // bottom left of the matrix
					if(y-nodes1 == x) { // cost of a node addition appears on diagonal
						costMatrix[y][x] = -1; // this will be set to the largest mapping value, 1 after normalization
					} else {
						costMatrix[y][x] = -2; // this will be set to MAX_VALUE
					}
				} else { // bottom right
					costMatrix[y][x] = 0.0;
				}
			}
		}
		
		// normalize costs to maxCost
		// set all -1s (add and delete nodes) to 1
		// set all -2s (unused cells) to MAX_VALUE
		for(int y = 0; y < size; y++) { // g1 nodes
			for(int x = 0; x < size; x++) { // g2 nodes
				double cost = costMatrix[y][x];
				if(cost > 0) {
					costMatrix[y][x] = cost/maxCost; // should end up between 0 and 1
				}
				if(cost == -1) {
					costMatrix[y][x] = 1;
				}
				if(cost == -2) {
					costMatrix[y][x] = Double.MAX_VALUE;
				}
			}
		}
		
		
		mapping = null;

		VJAccess vja = new VJAccess();
		vja.computeAssignment(costMatrix);
		mapping = vja.getAssignment();

		// get the similarity from the mapping
		double cost = 0.0;
		for(int i = 0; i < maxNodes; i++) {
			cost += costMatrix[i][mapping[i]];
		}
		
		double ret = cost/maxNodes; // keep it between 0 and 1
		return ret;

	}

	

	/**
	 * Find a number of trails from n1 in g2 and see how many and how far they can be repeated from n2 in g2. 
	 * 
	 * @param n1 node in g1
	 * @param n2 node in g2
	 * @param g1 the first graph
	 * @param g2 the second graph
	 * @return
	 */
	private Double randomTrailCost(int n1, int n2, FastGraph g1, FastGraph g2) {
		
		double ret = 0.0;
		// first g1 to g2
		for(int i = 0; i < trailsPerNode; i++) {
			// commented out because different random seeds g1 to g2 and g2 to g1 mean no symmetry
			// randomSeed = randomSeed*11;
			double singleCost = randomSingleTrail(n1, n2, g1, g2);
			ret += singleCost;
		}
		// then g2 to g1
		for(int i = 0; i < trailsPerNode; i++) {
			// commented out because different random seeds g1 to g2 and g2 to g1 mean no symmetry
			// randomSeed = randomSeed*555;
			double singleCost = randomSingleTrail(n2, n1, g2, g1);
			ret += singleCost;
		}
		
		return ret;
		
	}
		
	/**
	 * Find a trail from n1 in g2 and see how far it can be repeated from n2 in g2. 
	 * 
	 * @param n1 node in g1
	 * @param n2 node in g2
	 * @param g1 the first graph
	 * @param g2 the second graph
	 * @return the length of trail1 minus the longest trail in trail2
	 */
	private Double randomSingleTrail(int n1, int n2, FastGraph g1, FastGraph g2) {
		ArrayList<TrailNode> trail1 = null;
		
		try {
			RandomTrail rt = new RandomTrail(directed,randomSeed);
			trail1 = rt.findTrail(g1, n1, trailLength);
		} catch (FastGraphException e) {
			e.printStackTrace();
			System.err.println("Fail in randomSingleTrail n1 "+n1+" not found in g1");
			return null;
		}

		// exhaustive search for trail1 in g2

		HashMap<TrailNode,ArrayList<Integer>> untestedNeighbours2 = new HashMap<>(trailLength*3);
		ArrayList<Integer> trail2Edges = new ArrayList<>(trailLength+1);
		ArrayList<TrailNode> trail2 = new ArrayList<>(trailLength+1);
		TrailNode tn2 = new TrailNode(0,n2,-1);
		trail2.add(tn2);
		ArrayList<Integer> neighbourEdgesList = getNeighbourEdgesList(tn2,g2,trail2Edges);
		untestedNeighbours2.put(tn2, neighbourEdgesList);
		int longestTrail2 = 1;
		while(trail2.size() != 0 && trail1.size() != trail2.size()) {
			
			ArrayList<Integer> currentNeighbours2 = untestedNeighbours2.get(tn2);
			
			TrailNode nextTN2 = null;
			int nextEdge = -1;
			while(currentNeighbours2.size() != 0) {
				nextEdge = currentNeighbours2.remove(0);
				nextTN2 = checkNodeMatch(tn2,nextEdge,g1,g2,trail1,trail2);
				if(nextTN2 != null) { // success
					break;
				}
			}

			
			if(nextTN2 == null) { // finished with the search of this node, no luck
				if(trail2.size() == 1) {
					// removing start node, so end
					break;
				}
				untestedNeighbours2.remove(tn2);
				trail2Edges.remove(trail2Edges.size()-1);
				trail2.remove(trail2.size()-1); // the same as removing tn2
				tn2 = trail2.get(trail2.size()-1);
			} else { // found a match
				tn2 = nextTN2;
				trail2.add(tn2);
				trail2Edges.add(nextEdge);
				if(trail2.size() > longestTrail2) {
					longestTrail2 = trail2.size();
				}
				if(trail2.size() == trail1.size()) { // success in finding a trail
					break;
				}

				neighbourEdgesList = getNeighbourEdgesList(tn2,g2,trail2Edges);
				untestedNeighbours2.put(tn2, neighbourEdgesList);
			}
				
		}
		double ret = trail1.size()-longestTrail2;

		return ret;
	}


	/**
	 * 
	 * @param tn2 the current node
	 * @param g2 the graph for the current node
	 * @param trail2Edges the current visited edges
	 * @return the list of unvisited neighbour edges of tn2
	 */
	private ArrayList<Integer> getNeighbourEdgesList(TrailNode tn2, FastGraph g2, ArrayList<Integer> trail2Edges) {
		// add the neighbour edges for testing
		int[] neighbourEdgesArray;
		if(directed) {
			neighbourEdgesArray = g2.getNodeConnectingOutEdges(tn2.getNode());
		} else {
			neighbourEdgesArray = g2.getNodeConnectingEdges(tn2.getNode());
		}
		ArrayList<Integer> neighbourEdgesList = new ArrayList<Integer>(neighbourEdgesArray.length+1);
		for(int i = 0; i < neighbourEdgesArray.length;i++) {
			int e = neighbourEdgesArray[i];
			if(!trail2Edges.contains(e)) {
				neighbourEdgesList.add(e);
			}
		}
		return neighbourEdgesList;
	}

	/**
	 * 
	 * Checks the match of trail2 node tn2 with the corresponding trail1 node tn1 at the position we are going to insert the new 
	 * node into trail2. Checks equal degree, checks node label if {@link nodeLabels} is true.
	 * 
	 * @param tn2 the g2 current node in a TrailNode
	 * @param nextEdge the edge that connects from tn2 to the next node to test
	 * @param g1 the first graph, with fixed trail
	 * @param g2 the second graph, we are building a trail in this graph
	 * @param trail1 the first trail
	 * @param trail2 the current state of the second trail
	 * @param visitedNodes1 a list of g1 nodes that are already in the trail
	 * @return the other end of nextEdge if it is a match, null otherwise
	 */
	private TrailNode checkNodeMatch(TrailNode tn2, int nextEdge, FastGraph g1, FastGraph g2, ArrayList<TrailNode> trail1, ArrayList<TrailNode> trail2) {

		int nextNode2 = g2.oppositeEnd(nextEdge, tn2.getNode());
		TrailNode nextTN2 = new TrailNode(trail2.size(), nextNode2, -1);
		
		TrailNode tn1 = trail1.get(trail2.size()-1); // equivalent to
		
		// if a repeated node, must be repeated in same point in trail
		// plus cannot have one repeated and not the other
		if(tn1.getDuplicatePosition() != tn2.getDuplicatePosition()) {
			return null;
		}
		
		if(!directed) {
			int n1Degree = g1.getNodeDegree(tn1.getNode());
			int n2Degree = g2.getNodeDegree(tn2.getNode());
			if(n1Degree != n2Degree) {
				return null;
			}
		} else {
			int n1OutDegree = g1.getNodeOutDegree(tn1.getNode());
			int n2OutDegree = g2.getNodeOutDegree(tn2.getNode());
			if(n1OutDegree != n2OutDegree) {
				return null;
			}
			int n1InDegree = g1.getNodeInDegree(tn1.getNode());
			int n2InDegree = g2.getNodeInDegree(tn2.getNode());
			if(n1InDegree != n2InDegree) {
				return null;
			}
		}
		
		
		// set the duplicate position for the next node in trail2
		for(TrailNode tn : trail2) {
			if(tn.getNode() == nextNode2) { // the trail2 node is going through a previously used node
				nextTN2.setDuplicatePositon(tn.getPosition());
				break;
			}
		}
		
		if(!nodeLabels) {
			return nextTN2;
		}
		
		String label1 = g1.getNodeLabel(tn1.getNode());
		String label2 = g2.getNodeLabel(tn2.getNode());
		
		if(label1.equals(label2)) {
			return nextTN2;
		}
		
		return null;

	}
	

	
}
