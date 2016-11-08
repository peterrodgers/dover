package uk.ac.kent.dover.fastGraph;

import java.util.*;

public class ExactMotifFinder {
	
	/**
	 *  string is the hash value of the fastGraph, first list is the
	 *  list of FastGraphs with the same hash value, second linked list is the list
	 *  of FastGraphs that are isomorphic
	 */
	private HashMap<String,LinkedList<LinkedList<FastGraph>>> hashBuckets;
	
	private FastGraph g;
	private EnumerateSubgraph enumerator;
	private FastGraph[] subgraphs; // subgraphs found by the enumerator
	

	public static void main(String[] args) {
		
		Debugger.enabled = true;
		
		FastGraph g = null;
		try {
			
			g = FastGraph.randomGraphFactory(100,1000,1,false); // 1 hundred nodes, 1 thousand edges
//			g = FastGraph.randomGraphFactory(10000,100000,1,false,false); //10 thousand nodes 100 thousand edges
//			g = FastGraph.randomGraphFactory(6,9,1,true,false); // 5 nodes, 6 edges
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		int nodes = 5;
		long time = Debugger.createTime();		
		ExactMotifFinder emf = new ExactMotifFinder(g);
		emf.findMotifs(nodes, 0);
		
		System.out.println("number of subgraphs "+emf.subgraphs.length);
		System.out.println("graph with "+g.getNumberOfNodes()+" nodes and "+g.getNumberOfEdges()+" edges");
		Debugger.outputTime("time for motifs with "+nodes+" nodes",time);
		
		
		int count = 0;
		for(String key : emf.hashBuckets.keySet()) {
			LinkedList<LinkedList<FastGraph>> sameHashList = emf.hashBuckets.get(key);
			System.out.println(key+" "+sameHashList.size());
			for(LinkedList<FastGraph> isoList: sameHashList) {
				System.out.println(isoList.size());
				count += isoList.size();
			}
		}
		System.out.println("stored subgraphs "+count);

	}

	/**
	 * 
	 * @param g the FastGraph to find motifs in
	 * @param numOfNodes the number of nodes in each motif
	 */
	public ExactMotifFinder(FastGraph g) {
		this.g = g;
		enumerator = new EnumerateSubgraph(g);

	}
	

	/**
	 * Run the motif finder.
	 * 
	 * @param k the size of motifs in terms of number of nodes.
	 * @param q the fraction of nodes to sample.
	 */
	public void findMotifs(int k, double q) {

		hashBuckets = new HashMap<String,LinkedList<LinkedList<FastGraph>>> (g.getNumberOfNodes());
		
		subgraphs = enumerator.enumerateSubgraphs(k, q);
		
		for(FastGraph subgraph : subgraphs) {
			ExactIsomorphism ei = new ExactIsomorphism(subgraph);
			String hashString = ei.generateStringForHash();
//Debugger.log("new subgraph, hash value "+hashString);			
			if(hashBuckets.containsKey(hashString)) {
				LinkedList<LinkedList<FastGraph>> sameHashList = hashBuckets.get(hashString); // all of the FastGraphs with the given hash value
				boolean found = false;
				for(LinkedList<FastGraph> isoList : sameHashList) { // now need to test all the same hash value buckets for isomorphism
					// only need to test the first Graph in an isomorphic list!
					FastGraph comparisonGraph = isoList.getFirst();

					if(ei.isomorphic(comparisonGraph)) {
						isoList.add(subgraph);
						found = true;
						break;
					}
				}
				if(!found) { // no isomorphic graphs found, so need to create a new list
					LinkedList<FastGraph> newIsoList = new LinkedList<FastGraph>();
					newIsoList.add(subgraph);
					sameHashList.add(newIsoList);
				}
			} else {
				LinkedList<LinkedList<FastGraph>> newHashList = new LinkedList<LinkedList<FastGraph>>();
				hashBuckets.put(hashString, newHashList);
				LinkedList<FastGraph> newIsoList = new LinkedList<FastGraph>();
				newIsoList.add(subgraph);
				newHashList.add(newIsoList);
			}

		}
		
	}


}
