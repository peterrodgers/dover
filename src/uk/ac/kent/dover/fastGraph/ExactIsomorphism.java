package uk.ac.kent.dover.fastGraph;

import java.nio.*;
import java.util.*;

import uk.ac.kent.displayGraph.Graph;

/**
 * Testing the structural similarity of two FastGraphs
 * 
 * @author Peter Rodgers
 *
 */
public class ExactIsomorphism {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExactIsomorphism ei = new ExactIsomorphism();
		FastGraph g2;
		FastGraph g1;
		int comparisons = 10000;
		long time;
		
		time = System.currentTimeMillis();
		for(int i = 0; i < comparisons; i++) {
			g1 = FastGraph.randomGraphFactory(8,12,i,false);
			g2 = FastGraph.randomGraphFactory(8,12,i,false);
			boolean res = ei.isomorphic(g1,g2);
			if(!res) {
				System.out.println("PROBLEM with isomorphism with seed "+i);
			}
		}
		System.out.println("time for "+comparisons+" equal graph isomorphism test "+((System.currentTimeMillis()-time)/1000.0)+" seconds "+ " time for one "+((1.0*System.currentTimeMillis()-time)/comparisons)+" milliseconds");

		time = System.currentTimeMillis();
		for(int i = 0; i < comparisons; i++) {
			g1 = FastGraph.randomGraphFactory(8,12,i,false);
			g2 = FastGraph.randomGraphFactory(8,12,i+1,false);
			boolean res = ei.isomorphic(g1,g2);
			if(res) {
				System.out.println("PROBLEM with isomorphism with seed "+i);
			}
		}
		System.out.println("time for "+comparisons+" random graph isomorphism test "+((System.currentTimeMillis()-time)/1000.0)+" seconds "+ " time for one "+((1.0*System.currentTimeMillis()-time)/comparisons)+" milliseconds");


		int c = 100000;
		g1 = FastGraph.randomGraphFactory(16,32,1,false);
		int[] edges = {0,2,4,7,8,9,10,11,13,14,15,17,19,20,23,27,28,31};
//		int[] edges = {0,2,4,7,8,9,10,11,13,14,15,17};
		ArrayList<Integer> nodeList = new ArrayList<Integer>();
		for(int i = 0; i < edges.length; i++) {
			int n1 = g1.getEdgeNode1(edges[i]);
			if(!nodeList.contains(n1)) {
				nodeList.add(n1);
			}
			int n2 = g1.getEdgeNode2(edges[i]);
			if(!nodeList.contains(n2)) {
				nodeList.add(n2);
			}
		}
		int[] nodes = new int[nodeList.size()+1];
		int b = 0;
		for(int a : nodeList) {
			nodes[b] = a;
			b++;
		}
		
		time = System.currentTimeMillis();
		for(int i = 0; i < c; i++) {
			FastGraph subGraph = g1.generateGraphFromSubgraph(nodes,edges);
			if(i%2 == 0) { 
				edges[0] = 0;
			} else {
//System.out.println(i);
				edges[0] = 3; // this has nodes that are in the list
			}
		}
		System.out.println("time for "+c+" subgraph creations "+((System.currentTimeMillis()-time)/1000.0)+" seconds "+" time for one "+((1.0*System.currentTimeMillis()-time)/c)+" milliseconds");

	}
	/**
	 *
	 * Create an ExactIsomorphism before running isomorphic. The ExactIsomorphism creates
	 * static data to speed up the algorithm. Arguments can have quite high values
	 * without compromising efficiency.
	 *
	 */
	public ExactIsomorphism() {

		//TODO create the stuff for the subgraph to be constructed
				
	}
	
	
	

	/**
	 * Check if the two graphs have the same structure.
	 * 
	 * @param g1 a FastGraph to be tested
	 * @param g2 the FastGraph from which a subgraph will be found
	 * @param nodes the nodes in g2 that form the subgraph
	 * @param edges the edges in g2 that form the subgraph
	 * @return true if the g1 and the subgraph of g2 are isomorphic, false otherwise
	 */
	public boolean isomorphic(FastGraph g1, FastGraph g2, int[] nodes, int[] edges) {

		FastGraph subGraph = g2.generateGraphFromSubgraph(nodes,edges);
		
		boolean iso = isomorphic(g1,subGraph);

		return iso;
	}


	/**
	 * Check if the two graphs have the same structure.
	 * 
	 * @param g1 one FastGraph to be tested
	 * @param g2 the other FastGraph to be tested
	 * @return true if the g1 and g2 are isomorphic, false otherwise
	 */
	public boolean isomorphic(FastGraph g1, FastGraph g2) {
		// TODO This is seriously ugly, fix with code here
		
		Graph dg1 = g1.generateDisplayGraph();
		Graph dg2 = g2.generateDisplayGraph();
		boolean iso = dg1.isomorphic(dg2);
		return iso;
	}



}
