package uk.ac.kent.dover.fastGraph;

import java.util.ArrayList;
import java.util.Random;

/**
 * Algorithm to implement the KMedoids
 * 
 * Modified from https://sourceforge.net/p/java-ml/java-ml-code/ci/a25ddde7c3677da44e47a643f88e32e2c8bbc32f/tree/net/sf/javaml/clustering/KMedoids.java#l40
 * 
 * @author Rob Baker
 *
 */
public class KMedoids {

	/* Number of clusters to generate */
	private int numberOfClusters;

	/* Random generator for selection of candidate medoids */
	private Random r;

	/* The maximum number of iterations the algorithm is allowed to run. */
	private int maxIterations;
	
	private FastGraph targetGraph;
	
	/**
	 * Constructor
	 * 
	 * @param targetGraph The target graph
	 * @param numberOfClusters The number of clusters
	 * @param maxIterations The maximum number of iterations
	 */
	public KMedoids(FastGraph targetGraph, int numberOfClusters, int maxIterations) {
		this.numberOfClusters = numberOfClusters;
		this.maxIterations = maxIterations;
		r = new Random(targetGraph.getNodeBuf().getLong(1));
	}
	
	/**
	 * Clusters the subgraphs
	 * 
	 * @param subgraphs The subgraphs to cluster
	 * @return The clusters, as a list of lists of FastGraphs
	 * @throws FastGraphException If the random selection cannot be obtained (i.e. k is too big)
	 */
	public ArrayList<ArrayList<FastGraph>> cluster(ArrayList<FastGraph> subgraphs) throws FastGraphException {
		System.out.println("subs: " + subgraphs.size());
		System.out.println("numOfClusters: " + numberOfClusters);
		
		ArrayList<FastGraph> medoids = Util.randomSelection(r, numberOfClusters, subgraphs);	
		ArrayList<ArrayList<FastGraph>> output = new ArrayList<ArrayList<FastGraph>>(numberOfClusters);
		
		boolean changed = true;
		int count = 0;
		while (changed && count < maxIterations) {
			changed = false;
			count++;
			int[] assignment = assign(medoids, subgraphs);
			Debugger.log("assignment complete");
			changed = recalculateMedoids(assignment, medoids, output, subgraphs);

		}

		return output;
	}
	
	/**
	 * Assign all instances from the data set to the medoids.
	 * 
	 * @param medoids candidate medoids
	 * @param subgraphs the data to assign to the medoids
	 * @return best cluster indices for each instance in the data set
	 */
	private int[] assign(ArrayList<FastGraph> medoids, ArrayList<FastGraph> subgraphs) {
		
		int[] out = new int[subgraphs.size()];
		for (int i = 0; i < subgraphs.size(); i++) {
						
			double bestDistance = comparisonScore(subgraphs.get(i), medoids.get(0));

			int bestIndex = 0;
			for (int j = 1; j < medoids.size(); j++) {
				double tmpDistance = comparisonScore(subgraphs.get(i), medoids.get(j));
				if (tmpDistance < bestDistance) {
					bestDistance = tmpDistance;
					bestIndex = j;
				}
			}
			out[i] = bestIndex;

		}
		return out;
	}

	/**
	 * Return a array with on each position the clusterIndex to which the
	 * Instance on that position in the dataset belongs.
	 * 
	 * @param medoids the current set of cluster medoids, will be modified to fit the new assignment
	 * @param assigment the new assignment of all instances to the different medoids
	 * @param output the cluster output, this will be modified at the end of the method
	 * @return If any of the medoids have changed
	 */
	private boolean recalculateMedoids(int[] assignment, ArrayList<FastGraph> medoids, ArrayList<ArrayList<FastGraph>> output,
			ArrayList<FastGraph> subgraphs) {
		
		boolean changed = false;
		
		for (int i = 0; i < numberOfClusters; i++) {
			output.add(new ArrayList<FastGraph>());
			for (int j = 0; j < assignment.length; j++) {
				if (assignment[j] == i) {
					output.get(i).add(subgraphs.get(j));
				}
			}
			
			if (output.get(i).size() == 0) { // new random, empty medoid
				medoids.set(i,subgraphs.get(r.nextInt(subgraphs.size())));
				changed = true;
			} else {
				FastGraph centroid = findAverageGraph(output.get(i));
				FastGraph oldMedoid = medoids.get(i);
				medoids.set(i, findClosestGraph(centroid, subgraphs));
				if (!medoids.get(i).equals(oldMedoid)) {
					changed = true;
				}
			}
		}
		return changed;
	}
	
	/**
	 * Finds the graph closest to the "average" of the given cluster
	 * 
	 * @param cluster The cluster
	 * @return The graph closest to the "average"
	 */
	private FastGraph findAverageGraph(ArrayList<FastGraph> cluster) {
		FastGraph averageGraph = null;
		float bestScore = Float.POSITIVE_INFINITY;
		
		for(FastGraph g : cluster) {
			float currentScore = 0;
			
			for(FastGraph h : cluster) {
				
				if(g == h) { //skip if the same
					continue;
				}
				
				currentScore += comparisonScore(g, h);
				
			}
			
			if(currentScore < bestScore) {
				bestScore = currentScore;
				averageGraph = g;
			}
		}
		
		return averageGraph;
	}
	
	/**
	 * Finds the graph closest to the given centroid
	 * 
	 * @param centroid The centroid
	 * @param subgraphs The entire list of subgraphs
	 * @return The graph closest to the centroid
	 */
	private FastGraph findClosestGraph(FastGraph centroid, ArrayList<FastGraph> subgraphs) {
		FastGraph closestGraph = null;
		double bestScore = Double.POSITIVE_INFINITY;
		
		for(FastGraph g : subgraphs) {
			double currentScore = comparisonScore(g, centroid);
			
			if(currentScore < bestScore) {
				bestScore = currentScore;
				closestGraph = g;
			}
		}
		
		return closestGraph;
	}
	
	/**
	 * Returns the comparison score of the two graphs. Normally, GED
	 * @param g1 The first graph
	 * @param g2 The second graph
	 * @return The comparison score
	 */
	private double comparisonScore(FastGraph g1, FastGraph g2) {
		return (g1.getNumberOfNodes() + g1.getNumberOfEdges()) - (g2.getNumberOfNodes() + g2.getNumberOfEdges());
		
		//return GedUtil.getGedScore(g1, g2);
	}

}
