package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = new HashMap<String,LinkedList<IsoHolder>>(g.getNumberOfNodes());
		emf.findMotifs(numOfNodes, 0, hashBuckets);
		
		
	//	System.out.println("number of subgraphs "+emf.subgraphs.size());
		System.out.println("graph with "+g.getNumberOfNodes()+" nodes and "+g.getNumberOfEdges()+" edges");
		Debugger.outputTime("time for motifs with "+numOfNodes+" nodes",time);

		//System.out.println(hashBuckets);
		
		int count = 0;
		for(String key : hashBuckets.keySet()) {
			LinkedList<IsoHolder> sameHashList = hashBuckets.get(key);
//			System.out.println("hash string \""+key+"\" number of different isomorphic groups "+sameHashList.size());
			for(IsoHolder isoList: sameHashList) {
System.out.println("hash string \t"+key+"\tnumber of different isomorphic groups\t"+sameHashList.size()+"\tnumber of nodes in iso list\t"+isoList.getNumber());
				count += isoList.getNumber();
				
				uk.ac.kent.displayGraph.Graph dg = isoList.getGraph().generateDisplayGraph();
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
/*		
		HashMap<String,IsoHolder> isoLists = emf.extractGraphLists(hashBuckets);
		for(String key : isoLists.keySet()) {
			IsoHolder isoList = isoLists.get(key);
			System.out.println(key+" "+isoList.getNumber());
			
			
			uk.ac.kent.displayGraph.Graph dg = isoList.getGraph().generateDisplayGraph();
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
	*/	
		System.out.println("stored subgraphs "+count);
		ExactIsomorphism.reportFailRatios();
		ExactIsomorphism.reportTimes();
	
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
	 * Merges two isoLists together, and returns the enlarged list
	 * 
	 * @param oldList The first list
	 * @param newList The second list
	 * @return The new list contains elements from both.
	 */
	public HashMap<String,IsoHolder> mergeIsoLists(HashMap<String,IsoHolder> oldList, HashMap<String,IsoHolder> newList) {
		//store local list of motifs
		for(String key : newList.keySet()) {
			IsoHolder isoList = newList.get(key);
			//add list to existing list for this key
			//Debugger.log("key" + key);
			if(oldList.containsKey(key)) {
				oldList.get(key).setNumber(oldList.get(key).getNumber() + isoList.getNumber());
			} else {
				//create it if it doesn't exist
				oldList.put(key, isoList);
			}
		}
		return oldList;
	}

	/**
	 * Finds and exports all motifs between the given sizes for this FastGraph
	 * 
	 * @param rewiresNeeded The number of times the graph needs to be rewired (0 if none required)
	 * @param minSize The minimum size of motifs being investigated
	 * @param maxSize The maximum size of motifs being investigated
	 * @param motifSampling The number of motifs to sample?
	 * @param comparisonSet Are these motifs the ones that will be compared against?
	 * @throws FileNotFoundException If the output file cannot be written to
	 */
	public void findAndExportAllMotifs(int rewiresNeeded, int minSize, int maxSize, int motifSampling, boolean comparisonSet) throws FileNotFoundException {
		long time = Debugger.createTime();
		
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = new HashMap<String,LinkedList<IsoHolder>>(g.getNumberOfNodes());
		//HashMap<String,LinkedList<FastGraph>> isoLists = new HashMap<String,LinkedList<FastGraph>>();
		
		HashMap<String,IsoHolder> isoLists = new HashMap<String,IsoHolder>();
		
		for(int i = minSize; i <= maxSize; i++) {
			HashMap<String,IsoHolder> newList = findAllMotifs(rewiresNeeded, i, motifSampling, hashBuckets);
			isoLists = mergeIsoLists(isoLists, newList);
			Debugger.outputTime("Found motifs with size "+i, time);
		}			
		

		
		//isoLists = extractGraphLists(hashBuckets);
		
		Debugger.outputTime("Time to find motifs", time);
		Debugger.log();
		time = Debugger.createTime();
		
		int totalSize = 0;
		for(IsoHolder isoList : isoLists.values()) {
			totalSize+= isoList.getNumber();
		}
		StringBuilder sb = new StringBuilder();
		for(String key : isoLists.keySet()) {
			IsoHolder isoList = isoLists.get(key);
			double percentage = ((double) isoList.getNumber()/totalSize)*100;
			sb.append(key+"\t"+isoList.getNumber() + "\t" + String.format( "%.2f", percentage ) +"\n");
		}

		//export the motifs
		String graphName = g.getName();
		String nameString = "_reference";
		if (!comparisonSet) {
			nameString = "_real";
		}
		/*
		if(comparisonSet) {
			for(String key : isoLists.keySet()) {
				LinkedList<FastGraph> isoList = isoLists.get(key);
				FastGraph gOut = isoList.getFirst();
				gOut.saveBuffers("motifs"+File.separatorChar+graphName+File.separatorChar+key, key);
			}
		}
*/
		//save the motif info file
		File output = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName+File.separatorChar+"motifs"+nameString+".txt");
		try(PrintWriter out = new PrintWriter( output )){ //will close file after use
		    out.println( sb );
		}
		
		Debugger.outputTime("Time to save: ", time);
	}
	
	/**
	 * Returns the hashbuckets from this instance
	 * @return The hashbuckets
	 */
	public HashMap<String,LinkedList<LinkedList<FastGraph>>> getHashBuckets() {
		return hashBuckets;
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
	public HashMap<String,IsoHolder> findAllMotifs(int rewiresNeeded, int sizeOfMotifs, int motifSampling, HashMap<String,LinkedList<IsoHolder>> hashBuckets) {
		HashMap<String,IsoHolder> isoLists = new HashMap<>();

		FastGraph currentGraph = g;
		
		if(rewiresNeeded == 0) {
			ExactMotifFinder emf = new ExactMotifFinder(g);
			emf.findMotifs(sizeOfMotifs, 0, hashBuckets);
			
			System.out.println("### rewiring again");
			for(String key : hashBuckets.keySet()) {
				System.out.println(key + " size: " + hashBuckets.get(key).size());
				for(IsoHolder list : hashBuckets.get(key)) {
					System.out.println("    Bucket size: " + list.getNumber());
					//System.out.println("    Bucket contents: " + list);
				}
				System.out.println();
			}
			
			isoLists = emf.extractGraphLists(hashBuckets);
			

		} else {
			for (int i = 0; i < rewiresNeeded; i++) {
				currentGraph = currentGraph.generateRandomRewiredGraph(10,1);
				ExactMotifFinder emf = new ExactMotifFinder(currentGraph);
				emf.findMotifs(sizeOfMotifs, 0, hashBuckets);
				HashMap<String,IsoHolder> newIsoLists = emf.extractGraphLists(hashBuckets);
				isoLists = mergeIsoLists(isoLists, newIsoLists);
				
				
			}			
		}
		return isoLists;		
	}
	

	/**
	 * This extracts the lists of isomorphic graphs. Each element in a list is isomorphic.
	 * Call after running findMotifs.
	 * 
 	 * @param hashBuckets The buckets to store the results in
	 * @return collection of lists, all graphs in a single list are isomorphic
	 */
	public HashMap<String,IsoHolder> extractGraphLists(HashMap<String,LinkedList<IsoHolder>> hashBuckets) {
		
		HashMap<String,IsoHolder> ret = new HashMap<String,IsoHolder>(hashBuckets.size()*2);
		
		for(String key : hashBuckets.keySet()) {
			LinkedList<IsoHolder> bucket = hashBuckets.get(key);
			int count = 0;
			for(IsoHolder list : bucket) {
				count++;
				
				String retKey = key+"-"+count;
				ret.put(retKey,new IsoHolder(retKey, list.getNumber(), list.getGraph()));
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
	public void findMotifs(int k, double q, HashMap<String,LinkedList<IsoHolder>> hashBuckets) {
		
		//hashBuckets = new HashMap<String,LinkedList<LinkedList<FastGraph>>> (g.getNumberOfNodes());
		
		subgraphs = enumerator.enumerateSubgraphs(k, 50 ,10);
//		subgraphs = enumeratorRandom.randomSampleSubgraph(k,10000);		
		for(FastGraph subgraph : subgraphs) {
			ExactIsomorphism ei = new ExactIsomorphism(subgraph);
			String hashString = ei.generateStringForHash();
//Debugger.log("new subgraph, hash value "+hashString);			
			if(hashBuckets.containsKey(hashString)) {
				LinkedList<IsoHolder> sameHashList = hashBuckets.get(hashString); // all of the FastGraphs with the given hash value
				boolean found = false;
				for(IsoHolder isoList : sameHashList) { // now need to test all the same hash value buckets for isomorphism
					// only need to test the first Graph in an isomorphic list!
					FastGraph comparisonGraph = isoList.getGraph();

					if(ei.isomorphic(comparisonGraph)) {
						isoList.incrementNumber();
						found = true;
						break;
					}
				}
				if(!found) { // no isomorphic graphs found, so need to create a new list
					IsoHolder newIsoList = new IsoHolder(hashString, 0, subgraph);
					//newIsoList.setGraph(subgraph);
					sameHashList.add(newIsoList);
				}
			} else {
				LinkedList<IsoHolder> newHashList = new LinkedList<IsoHolder>();
				hashBuckets.put(hashString, newHashList);
				IsoHolder newIsoList = new IsoHolder(hashString, 0, subgraph);
				//newIsoList.add(subgraph);
				newHashList.add(newIsoList);
			}

		}
		
	}
	
	/**
	 * Inner class to hold a FastGraph and the number of instances of that FastGraph for a particluar key.
	 * Saves keeping big lists and lists of lists.
	 * 
	 * @author Rob Baker
	 *
	 */
	private class IsoHolder {
		
		private String key; //The key
		private int number; //The number of graphs
		private FastGraph graph; //An example of a graph
		
		/**
		 * Trivial constructor
		 * 
		 * @param key The hashkey for this graph
		 * @param number The number of graphs with this hashkey
		 * @param graph An example of one of the graphs
		 */
		public IsoHolder(String key, int number, FastGraph graph) {
			this.key = key;
			this.number = number;
			this.graph = graph;
		}

		/**
		 * Gets the key
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Sets the key
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * Gets the number
		 * @return the number
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * Sets the number
		 * @param number the number to set
		 */
		public void setNumber(int number) {
			this.number = number;
		}

		/**
		 * Adds 1 to the number
		 */
		public void incrementNumber() {
			number++;
		}
		
		/**
		 * Gets the graph
		 * @return the graph
		 */
		public FastGraph getGraph() {
			return graph;
		}

		/**
		 * Sets the graph
		 * @param graph the graph to set
		 */
		public void setGraph(FastGraph graph) {
			this.graph = graph;
		}
		
	}
	

}
