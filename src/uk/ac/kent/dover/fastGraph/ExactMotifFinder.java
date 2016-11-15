package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.*;

import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;

public class ExactMotifFinder {
	
	/**
	 *  string is the hash value of the fastGraph, first list is the
	 *  list of FastGraphs with the same hash value, second linked list is the list
	 *  of FastGraphs that are isomorphic
	 */
	private static HashMap<String,LinkedList<LinkedList<FastGraph>>> hashBuckets;
	
	private FastGraph g;
	private EnumerateSubgraphNeighbourhood enumerator;
	private EnumerateSubgraphRandom enumeratorRandom;
	private HashSet<FastGraph> subgraphs; // subgraphs found by the enumerator
	

	public static void main(String[] args) {
		
		Debugger.enabled = true;
		
		FastGraph g = null;
		try {
//			g = FastGraph.loadBuffersGraphFactory(null,"soc-pokec-relationships.txt-reduced");
			
//			g = FastGraph.randomGraphFactory(2,1,1000,true,false); // 1 hundred nodes, 1 thousand edges
//			g = FastGraph.randomGraphFactory(100,1000,1,true,false); // 2 hundred nodes, 2 thousand edges
			g = FastGraph.randomGraphFactory(200,2000,1,true,false); // 3 hundred nodes, 3 thousand edges
//			g = FastGraph.randomGraphFactory(300,3000,1,true,false); // 3 hundred nodes, 3 thousand edges
//			g = FastGraph.randomGraphFactory(1000,10000,1,true,false); // 1 thousand nodes, 10 thousand edges
//			g = FastGraph.randomGraphFactory(10000,100000,1,true,false); //10 thousand nodes 100 thousand edges
//			g = FastGraph.randomGraphFactory(6,9,1,true,false); // 5 nodes, 6 edges
			
		} catch(Exception e) {
			e.printStackTrace();
		}
/*
Debugger.resetTime();
Debugger.log("Starting");
		FastGraph h = g.generateRandomRewiredGraph(10,1);
System.out.println("h consistent "+h.checkConsistency());
System.out.println("g consistent "+g.checkConsistency());
System.out.println(Arrays.equals(h.degreeProfile(), g.degreeProfile())+" "+Arrays.toString(h.degreeProfile()));
System.out.println(Arrays.equals(h.inDegreeProfile(), g.inDegreeProfile())+" "+Arrays.toString(h.inDegreeProfile()));
System.out.println(Arrays.equals(h.outDegreeProfile(), g.outDegreeProfile())+" "+Arrays.toString(h.outDegreeProfile()));

System.exit(0);
*/
		int numOfNodes = 4;
		long time = Debugger.createTime();		
		ExactMotifFinder emf = new ExactMotifFinder(g);
		emf.findMotifs(numOfNodes, 0, hashBuckets);
		
		System.out.println("number of subgraphs "+emf.subgraphs.size());
		System.out.println("graph with "+g.getNumberOfNodes()+" nodes and "+g.getNumberOfEdges()+" edges");
		Debugger.outputTime("time for motifs with "+numOfNodes+" nodes",time);
		
		
		int count = 0;
/*		for(String key : emf.hashBuckets.keySet()) {
			LinkedList<LinkedList<FastGraph>> sameHashList = emf.hashBuckets.get(key);
//			System.out.println("hash string \""+key+"\" number of different isomorphic groups "+sameHashList.size());
			for(LinkedList<FastGraph> isoList: sameHashList) {
System.out.println("hash string \t"+key+"\tnumber of different isomorphic groups\t"+sameHashList.size()+"\tnumber of nodes in iso list\t"+isoList.size());
				count += isoList.size();
				
				uk.ac.kent.displayGraph.Graph dg = isoList.get(0).generateDisplayGraph();
				dg.randomizeNodePoints(new Point(20,20),300,300);
				dg.setLabel(key);
//				uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg);
//				uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
//				GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
//				se.setAnimateFlag(false);
//				se.setIterations(1000);
//				se.setTimeLimit(2000);
//				se.setGraphPanel(gw.getGraphPanel());
//				se.layout();
				
			}
		}
*/		
		HashMap<String,LinkedList<FastGraph>> isoLists = emf.extractGraphLists(hashBuckets);
		for(String key : isoLists.keySet()) {
			LinkedList<FastGraph> isoList = isoLists.get(key);
			System.out.println(key+" "+isoList.size());
			
			
			uk.ac.kent.displayGraph.Graph dg = isoList.get(0).generateDisplayGraph();
			dg.randomizeNodePoints(new Point(20,20),300,300);
			dg.setLabel(key);
			uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg);
			uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
			GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
			se.setAnimateFlag(false);
			se.setIterations(100);
			se.setTimeLimit(200);
			se.setGraphPanel(gw.getGraphPanel());
			se.layout();

		}
		
		System.out.println("stored subgraphs "+count);
		ExactIsomorphism.reportFailRatios();
		ExactIsomorphism.reportTimes();
	
	}

	/**
	 * Generates a list mapping of keys to lists of motifs with that key
	 * 
	 * @param rewiresNeeded The number of times the graph needs to be rewired
	 * @param sizeOfMotifs The size of motifs being investigated
	 * @param motifSampling The number of motifs to sample?
	 * @param hashBuckets The buckets to store the results in
	 * @return The map of keys to buckets of motifs that match those keys
	 */
	public HashMap<String,LinkedList<FastGraph>> findAllMotifs(int rewiresNeeded, int sizeOfMotifs, int motifSampling, HashMap<String,LinkedList<LinkedList<FastGraph>>> hashBuckets) {
		HashMap<String,LinkedList<FastGraph>> isoLists = new HashMap<>();
		
	//	isoLists.forEach((key, isoList) -> {
	//		Debugger.log(key+" "+isoList.size());
	//	});
		
		
		FastGraph currentGraph = g;
		
		if(rewiresNeeded == 0) {
			ExactMotifFinder emf = new ExactMotifFinder(g);
			emf.findMotifs(sizeOfMotifs, 0, hashBuckets);
			isoLists = emf.extractGraphLists(hashBuckets);
		} else {
			for (int i = 0; i < rewiresNeeded; i++) {
				currentGraph = currentGraph.generateRandomRewiredGraph(10,1);
				ExactMotifFinder emf = new ExactMotifFinder(currentGraph);
				emf.findMotifs(sizeOfMotifs, 0, hashBuckets);
				HashMap<String,LinkedList<FastGraph>> newIsoLists = emf.extractGraphLists(hashBuckets);
				
				//newIsoLists.forEach((key, isoList) -> {
				//	Debugger.log(key+" "+isoList.size());
				//});
				
				//store local list of motifs
				for(String key : newIsoLists.keySet()) {
					LinkedList<FastGraph> isoList = newIsoLists.get(key);
					//add list to existing list for this key
					//Debugger.log("key" + key);
					if(isoLists.containsKey(key)) {
						isoLists.get(key).addAll(isoList);
					} else {
						//create it if it doesn't exist
						isoLists.put(key, new LinkedList<FastGraph>(isoList));
					}
					
				}
				
			}			
		}
		return isoLists;
		
	}
	
	/**
	 * 
	 * @param g the FastGraph to find motifs in
	 * @param numOfNodes the number of nodes in each motif
	 */
	public ExactMotifFinder(FastGraph g) {
		this.g = g;
		//enumerator = new EnumerateSubgraphFanmod(g);
		enumerator = new EnumerateSubgraphNeighbourhood(g);
		enumeratorRandom = new EnumerateSubgraphRandom(g);
	}
	

	/**
	 * This extracts the lists of isomorphic graphs. Each element in a list is isomorphic.
	 * Call after running findMotifs.
	 * 
 	 * @param hashBuckets The buckets to store the results in
	 * @return collection of lists, all graphs in a single list are isomorphic
	 */
	public HashMap<String,LinkedList<FastGraph>> extractGraphLists(HashMap<String,LinkedList<LinkedList<FastGraph>>> hashBuckets) {
		
		HashMap<String,LinkedList<FastGraph>> ret = new HashMap<String,LinkedList<FastGraph>>(hashBuckets.size()*2);
		
		for(String key : hashBuckets.keySet()) {
			LinkedList<LinkedList<FastGraph>> bucket = hashBuckets.get(key);
			int count = 0;
			for(LinkedList<FastGraph> list : bucket) {
				count++;
				
				String retKey = key+"-"+count;
				ret.put(retKey,list);
			}
		}
		
		return ret;
	}

	
	
	/**
	 * Run the motif finder.
	 * 
	 * @param k the size of motifs in terms of number of nodes.
	 * @param q the fraction of nodes to sample.
	 * @param hashBuckets The buckets to store the results in
	 */
	public void findMotifs(int k, double q, HashMap<String,LinkedList<LinkedList<FastGraph>>> hashBuckets) {

		//hashBuckets = new HashMap<String,LinkedList<LinkedList<FastGraph>>> (g.getNumberOfNodes());
		
		subgraphs = enumerator.enumerateSubgraphs(k, 50 ,10);
//		subgraphs = enumeratorRandom.randomSampleSubgraph(k,10000);		
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
