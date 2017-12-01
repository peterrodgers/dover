package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;

/**
 * Simplistic graph difference measure that returns the sum of difference in degree profiles of nodes in the two graphs.
 * Takes account of age, comparing only those of the same age.
 * Directed version takes adds the sum of indegree differences to the sum of outdegree differences.
 * 
 * @author pjr
 *
 */
public class NodeDegreeDifference extends GraphSimilarity {


	public static void main(String [] args) {

		final int MILLION = 1000000;
		int numberOfNodes = MILLION/5;
		int numberOfEdges = MILLION*2;
		double similarity;
		FastGraph g1,g2;
		NodeDegreeDifference ndd = new NodeDegreeDifference(true);
		long time;
		
		while(true) {
			try {
				if(numberOfEdges < MILLION) {
					System.out.println("nodes: "+numberOfNodes+", edges: "+numberOfEdges);
				} else {
					System.out.println("nodes: "+numberOfNodes/(MILLION*1.0)+" million, edges: "+numberOfEdges/(MILLION*1.0)+" million");
				}
				time = System.currentTimeMillis();
				g1 = FastGraph.randomGraphFactory(numberOfNodes, numberOfEdges, false);
				System.out.println("Random g1 created time: "+(System.currentTimeMillis()-time)/1000.0+" seconds");
				time = System.currentTimeMillis();
				
				g2 = FastGraph.randomGraphFactory(numberOfNodes, numberOfEdges, false);
				System.out.println("Random g2 created time: "+(System.currentTimeMillis()-time)/1000.0+" seconds");
				time = System.currentTimeMillis();
				
				similarity = ndd.similarity(g1, g2);
				System.out.println("Similarity: "+similarity+" time: "+(System.currentTimeMillis()-time)/1000.0+" seconds\n");
				time = System.currentTimeMillis();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			numberOfNodes *= 2;
			numberOfEdges *= 2;
		}
	
	}
	
	
	public NodeDegreeDifference() {
		super();
	}
	
	public NodeDegreeDifference(boolean directed) {
		super(directed);
	}
	
	
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {
		double ret = 0.0;
		if(directed) {
			ret = directedDegreeDifference(g1,g2);
		} else {
			ret = undirectedDegreeDifference(g1,g2);
			
		}

		return ret;
	}

	
	private double undirectedDegreeDifference(FastGraph g1, FastGraph g2) {
		int minAgeG1 = g1.findMinimumNodeAge();
		int minAgeG2 = g2.findMinimumNodeAge();
		int maxAgeG1 = g1.findMaximumNodeAge();
		int maxAgeG2 = g2.findMaximumNodeAge();
		
		int minAge = Math.min(minAgeG1, minAgeG2);
		int maxAge = Math.max(maxAgeG1, maxAgeG2);

		double result = 0;
		for(int i = 0; i <= maxAge-minAge; i++) {
			//find the degree buckets for this age, do the comparison and store the result only

			int[] buckets1 = g1.findDegreeProfileAtAge(i);
			int[] buckets2 = g2.findDegreeProfileAtAge(i);
			result += findDifferenceInDegreeProfiles(buckets1, buckets2);
		}		
		return result;		
	}

	
	

	private double directedDegreeDifference(FastGraph g1, FastGraph g2) {
		int minAgeG1 = g1.findMinimumNodeAge();
		int minAgeG2 = g2.findMinimumNodeAge();
		int maxAgeG1 = g1.findMaximumNodeAge();
		int maxAgeG2 = g2.findMaximumNodeAge();
		
		int minAge = Math.min(minAgeG1, minAgeG2);
		int maxAge = Math.max(maxAgeG1, maxAgeG2);

		double result = 0;
		for(int i = 0; i <= maxAge-minAge; i++) {
			//find the degree buckets for this age, do the comparison and store the result only

			int[] inBuckets1 = g1.findInDegreeProfileAtAge(i);
			int[] inBuckets2 = g2.findInDegreeProfileAtAge(i);
			result += findDifferenceInDegreeProfiles(inBuckets1, inBuckets2);
			
			int[] outBuckets1 = g1.findOutDegreeProfileAtAge(i);
			int[] outBuckets2 = g2.findOutDegreeProfileAtAge(i);
			result += findDifferenceInDegreeProfiles(outBuckets1, outBuckets2);

		}		
		return result;		
	}


	/**
	 * Finds the difference in degree profile between two graphs
	 * @param buckets1 The first set of buckets
	 * @param buckets2 The second set of buckets
	 * @return The difference (double to maintain consistency with other methods)
	 */
	private double findDifferenceInDegreeProfiles(int[] buckets1, int[] buckets2) {

		double total = 0;
		for(int i = 0; i < Math.max(buckets1.length, buckets2.length); i++) {
			
			if(i >= buckets1.length) {
				total += buckets2[i];
			} else if(i >= buckets2.length) {
				total += buckets1[i];
			} else {
				total += Math.abs(buckets1[i] - buckets2[i]);
			}
		}		
		return total;

	}
	

}
