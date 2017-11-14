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


	public static void main(String [ ] args) {
	
		double similarity;
		NodeStructure ns0,ns1,ns2;
		EdgeStructure es0,es1,es2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		NodeDegreeDifference ndd;
		
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
//for(int i = 0; i < g1.getNumberOfNodes(); i++) {
//	System.out.println("node "+i+" age "+g1.getNodeAge(i));
//}
		
		ndd = new NodeDegreeDifference(false);
	
		similarity = ndd.similarity(g1, g2);
System.out.println(similarity);
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
			
//System.out.println("buckets1["+i+"] "+buckets1[i]);
//System.out.println("buckets2["+i+"] "+buckets2[i]);
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
