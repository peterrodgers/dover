package uk.ac.kent.dover.fastGraph;

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
		System.out.println("time for "+comparisons+" equal graph isomorphism test "+((System.currentTimeMillis()-time)/1000.0)+" seconds");

		for(int i = 0; i < comparisons; i++) {
			g1 = FastGraph.randomGraphFactory(8,12,i,false);
			g2 = FastGraph.randomGraphFactory(8,12,i+1,false);
			boolean res = ei.isomorphic(g1,g2);
			if(res) {
				System.out.println("PROBLEM with isomorphism with seed "+i);
			}
		}
		System.out.println("time for "+comparisons+" random graph isomorphism test "+((System.currentTimeMillis()-time)/1000.0)+" seconds");

	}
	

	/**
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

	/**
	 *
	 * Create an ExactIsomorphism before running isomorphic. The ExactIsomorphism creates
	 * static data to speed up the algorithm.
	 *
	 */
	public ExactIsomorphism() {
				
	}
	
	


}
