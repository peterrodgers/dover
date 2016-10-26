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

	private int DECIMAL_PLACES = 6; // number of decimal places to round to
	
	private FastGraph fastGraph;
	private int[][] matrix;
	private double[] eigenvalues;

	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FastGraph g1;
		FastGraph g2;
		int comparisons = 10000;
		long time;

		int numNodes = 3;
		int numEdges= 2;

		time = System.currentTimeMillis();
		for(int i = 0; i < comparisons; i++) {
			g1 = FastGraph.randomGraphFactory(numNodes,numEdges,i,false);
			ExactIsomorphism ei = new ExactIsomorphism(g1);
			g2 = FastGraph.randomGraphFactory(numNodes,numEdges,i,false);
			boolean res = ei.isomorphic(g2);
			if(!res) {
				System.out.println("PROBLEM with isomorphism with seed "+i+" fails, should succeed");
			}
		}
		System.out.println("time for "+comparisons+" equal graph isomorphism test "+((System.currentTimeMillis()-time)/1000.0)+" seconds "+ " time for one "+((1.0*System.currentTimeMillis()-time)/comparisons)+" milliseconds");

		time = System.currentTimeMillis();
		g1 = FastGraph.randomGraphFactory(numNodes,numEdges,-1,false);
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		for(int i = 0; i < comparisons; i++) {
			g2 = FastGraph.randomGraphFactory(numNodes,numEdges,i,false);
			boolean res = ei.isomorphic(g2);
			if(res) {
				System.out.println("PROBLEM with isomorphism with seed "+i+" succeeds, should fail");
			}
			boolean comp = ei.compareByEigenvalues(g2);
			if(comp) {
				System.out.println("Equal by eigenvalues "+i);
				System.out.println();
			}
			if(res && !comp) {
				System.out.println("Not equal by eigenvalues but equal by isomorphism");
				System.out.println("Graph 1");
				for(int e = 0; e < numEdges; e++) {
					System.out.println(g1.getEdgeNode1(e)+ " "+g1.getEdgeNode1(e));
				}
				AdjacencyMatrix am = new AdjacencyMatrix(g1);
				int[][] gMatrix = am.buildIntAdjacencyMatrix();
				double[] gEigenvalues = am.findEigenvalues(gMatrix);
				for(double d : gEigenvalues) {
					System.out.print(d+" ");
				}
				System.out.println();
				System.out.println("Graph 2");
				for(int e = 0; e < numEdges; e++) {
					System.out.println(g1.getEdgeNode1(e)+ " "+g1.getEdgeNode1(e));
				}
				am = new AdjacencyMatrix(g1);
				gMatrix = am.buildIntAdjacencyMatrix();
				gEigenvalues = am.findEigenvalues(gMatrix);
				for(double d : gEigenvalues) {
					System.out.print(d+" ");
				}
				System.out.println();
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
	 * Create an ExactIsomorphism before running isomorphic. This makes multiple tests against
	 * one graph to be more efficient as data for that graph does not need to be recreated.
	 * 
	 * @ param one graph to be tested.
	 *
	 */
	public ExactIsomorphism(FastGraph fastGraph) {

		this.fastGraph = fastGraph;

		if(fastGraph.getNumberOfNodes() == 0) {
			matrix = new int[0][0];
			eigenvalues = new double[0];
		} else {
			AdjacencyMatrix am = new AdjacencyMatrix(fastGraph);
			matrix = am.buildIntAdjacencyMatrix();
			eigenvalues = am.findEigenvalues(matrix);
			eigenvalues = Util.roundArray(eigenvalues,DECIMAL_PLACES);
		}

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
	 * Check if the given graph has the same structure as the graph passed to the constructor.
	 * 
	 * @param g the FastGraph to be tested
	 * @return true if the two graphs are isomorphic, false otherwise
	 */
	public boolean isomorphic(FastGraph g) {
		// TODO This is seriously ugly, fix with code here
		
		Graph displayGraph = fastGraph.generateDisplayGraph();
		Graph dg = g.generateDisplayGraph();
		boolean iso = displayGraph.isomorphic(dg);
		return iso;
	}


	/**
	 * Check if two graphs have the same structure. Use the constructor and non static method if
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
	

	public boolean compareByEigenvalues(FastGraph g) {
		AdjacencyMatrix am = new AdjacencyMatrix(g);
		int[][] gMatrix = am.buildIntAdjacencyMatrix();
		double[] gEigenvalues = am.findEigenvalues(gMatrix);
		gEigenvalues = Util.roundArray(gEigenvalues,DECIMAL_PLACES);
		boolean ret = Arrays.equals(eigenvalues,gEigenvalues);
/*
System.out.println("QWERTY");
for(double d : eigenvalues) {
	System.out.print(d+" ");
}
System.out.println();
for(double d : gEigenvalues) {
	System.out.print(d+" ");
}
System.out.println("\nresult "+ret);
*/	
		return ret;
	}


}
