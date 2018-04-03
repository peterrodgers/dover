package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;

/**
 * A simple method to search for the belief propagation value.
 * 
 * Nodes are mapped, then the BP value is found. Then iteratively nodes are swapped at random,
 * keeping good nodes with some bad moves kept.
 * 
 * Cannot deal with node labels or directed graphs.
 * 
 * Belief Propagation value calculated using the method in:
 * Danai Koutra, Joshua T. Vogelstein, and Christos Faloutsos. "Deltacon: A principled massive-graph similarity function." Proceedings of the 2013 SIAM International Conference on Data Mining. Society for Industrial and Applied Mathematics, 2013.
 * 
 * @author Peter Rodgers
 *
 */
public class BeliefPropagationSimple extends GraphSimilarity {

	private long nodeSwapTimeLimit;
	private int nodeSwapAttempts;
	private long randomSeed;
	
	private int nodeSwaps = 0;
	private long approximationTime = 0;
	
	private HashMap<Integer,Integer> nodeMapping; // g1 nodes to g2 nodes
	
//	private ReverseIntegerComparator reverseComparator = new ReverseIntegerComparator();
//	HashMap<Integer,Double> editCosts;
	
	private Random random;

	

	/**
	 * Constructor to be called before running algorithm.
	 */
	public BeliefPropagationSimple() {
		super();

		this.nodeSwapTimeLimit = 0;
		this.nodeSwapAttempts = 0;
		this.randomSeed = System.currentTimeMillis();
		
	}


	/**
	 * 
	 * Constructor to be called before running algorithm.
	 * 
	 * @param nodeSwapTimeLimit time in milliseconds to exit approximation
	 * @param nodeSwapAttempts number of iterations of the node swap routine
	 * @param randomSeed set to System.currentTimeMillis() for true random
	 */
	public BeliefPropagationSimple(long nodeSwapTimeLimit, int nodeSwapAttempts, long randomSeed) {
		this.nodeSwapTimeLimit = nodeSwapTimeLimit;
		if(nodeSwapTimeLimit == -1) {
			this.nodeSwapTimeLimit = Long.MAX_VALUE;
		}
		this.nodeSwapAttempts = nodeSwapAttempts;
		if(nodeSwapAttempts == -1) {
			this.nodeSwapAttempts = Integer.MAX_VALUE;
		}
		this.randomSeed = randomSeed;
		
	}


	/**
	 * 
	 * @return how many node swaps were made
	 */
	public int getNodeSwaps() {return nodeSwaps;}

	
	/**
	 * 
	 * @return how long the node swaps took in milliseconds.
	 */
	public long getApproximationTime() {return approximationTime;}

	
	/**
	 * This returns the similarity between the two graphs. 
	 * Zero means the graphs are isomorphic.
	 * Greater values mean more dissimilarity. Value is between 0 and 1.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the similarity of the two graphs.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
		
		random = new Random(randomSeed);
	
		nodeMapping = new HashMap<>(g1.getNumberOfNodes()*4); // g1 nodes to g2 nodes

		// initial node mapping (and final mapping if there are no swaps), based on degree
		createUndirectedMapping(g1,g2);
		
		double similarity = calculateBPValue(g1,g2,nodeMapping);

		if(g1.getNumberOfNodes() == 0 && g2.getNumberOfNodes() == 0) {
			return similarity;
		}
		
		long startTime = System.currentTimeMillis();
		boolean keepBadMove = false;
		approximationTime = System.currentTimeMillis()-startTime;
		nodeSwaps = 0;
		while( approximationTime < nodeSwapTimeLimit || nodeSwaps < nodeSwapAttempts) {
			HashMap<Integer,Integer> oldNodeMapping = new HashMap<>(nodeMapping);
			
			int g1NodeSize = g1.getNumberOfNodes();

			int g1Node1 = random.nextInt(g1NodeSize);
			int g1Node2 = random.nextInt(g1NodeSize);
			
			Integer g2Node1 = nodeMapping.remove(g1Node1);
			Integer g2Node2 = nodeMapping.remove(g1Node2);
			
			if(g2Node1 != null) {
				nodeMapping.put(g1Node2,g2Node1);
			}
			if(g2Node2 != null) {
				nodeMapping.put(g1Node1,g2Node2);
			}
			
			double testSimilarity = calculateBPValue(g1,g2,nodeMapping);
		
			if(keepBadMove || testSimilarity < similarity) {
				similarity = testSimilarity;
//System.out.println("swapped "+g1Node1+" "+g1Node2+" "+similarity);
			} else { // no benefit, so revert unless bad move allowed
				nodeMapping = oldNodeMapping;
			}
			
			nodeSwaps++;
			approximationTime = System.currentTimeMillis()-startTime;
			
			double remainingTime = 1.0-approximationTime/(nodeSwapTimeLimit*1.0);
			double remainingIteration = 1.0-nodeSwaps/(nodeSwapAttempts*1.0);
			double remainingPercent = 0.0;
			if(nodeSwapTimeLimit == 0) {
				remainingPercent = remainingIteration;
			} else if(nodeSwapAttempts == 0) {
				remainingPercent = remainingTime;
			} else if(remainingIteration > remainingTime) {
				remainingPercent = remainingIteration;
			} else {
				remainingPercent = remainingTime;
			}
			double chanceOfBadMove = remainingPercent-0.25;
			if(chanceOfBadMove < 0) {
				chanceOfBadMove = 0;
			}
			keepBadMove = false;
			if(random.nextDouble() < chanceOfBadMove) {
				keepBadMove = true;
			}
			
		}

//System.out.println(editList);
//System.out.println("node Mapping    "+nodeMapping);
//System.out.println("reverse Mapping "+reverseNodeMapping);
//System.out.println("delete node list "+deleteNodes);
//System.out.println("delete edge list "+deleteEdges);
//System.out.println("add node set "+addNodeSet);
//System.out.println("add edge lists "+addEdgeNode1List+" "+addEdgeNode2List);

		return similarity;
	}
	

	/**
	 * Find the Belief Propagation value given the existing node mappings.
	 * @return the belief propagation value
	 */
	private double calculateBPValue(FastGraph g1, FastGraph  g2, HashMap<Integer,Integer> nodeMapping) {
		BeliefPropagationCalculation bpc = new BeliefPropagationCalculation(g1,g2,nodeMapping);
		double ret = bpc.similarity();
		return ret;

	}



	/**
	 * Set the initial mapping for undirected graphs. Compare the nodes
	 * by degree size, largest first. This will work OK
	 * for the labelled version as long as the node label edit cost is not too high.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 */
	private void createUndirectedMapping(FastGraph g1, FastGraph g2) {
		ArrayList<Integer> n1List = new ArrayList<Integer>(g1.getNumberOfNodes());
		ArrayList<Integer> n2List = new ArrayList<Integer>(g2.getNumberOfNodes());
		for(int i =0; i < g1.getNumberOfNodes(); i++) {
			n1List.add(i);
		}
		for(int i =0; i < g2.getNumberOfNodes(); i++) {
			n2List.add(i);
		}
		NodeDegreeComparator ndc1 = new NodeDegreeComparator(g1,g1); // comparing nodes from the same graph
		ndc1.setAscending(false);
		n1List.sort(ndc1);

		NodeDegreeComparator ndc2 = new NodeDegreeComparator(g2,g2); // comparing nodes from the same graph
		ndc2.setAscending(false);
		n2List.sort(ndc2);
		
		// create initial mapping, and either deleted or added arrays
		// depending on the number of nodes in g1 and g2
		while(true) {
			if(n1List.size() == 0 || n2List.size() == 0) {
				for(int j = 0; j < n2List.size() ;j++) {
					int g1NodeIndex = g1.getNumberOfNodes()+j;
					int g2NodeIndex = n2List.get(j);
					nodeMapping.put(g1NodeIndex,g2NodeIndex); // also add to the node mapping, as we will need this to add edges
				}
				break;
			}
			// largest degrees match
			nodeMapping.put(n1List.get(0),n2List.get(0));
			n1List.remove(0);
			n2List.remove(0);
		}
	}
	
	

	
}
