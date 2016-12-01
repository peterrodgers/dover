package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.text.html.parser.TagElement;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;
import uk.ac.kent.dover.fastGraph.Gui.MotifTask;

public class ExactMotifFinder {
	
	/**
	 *  string is the hash value of the fastGraph, first list is the
	 *  list of FastGraphs with the same hash value, second linked list is the list
	 *  of FastGraphs that are isomorphic
	 */
	private static HashMap<String,LinkedList<IsoHolder>> hashBuckets;
	
	private FastGraph g;
	private MotifTask mt = null;
	private EnumerateSubgraphNeighbourhood enumerator;
	private EnumerateSubgraphRandom enumeratorRandom;
	private HashSet<FastGraph> subgraphs; // subgraphs found by the enumerator
	

	public static void main(String[] args) {
		
		Debugger.enabled = true;
		
		FastGraph g = null;
		try {
//			g = FastGraph.loadBuffersGraphFactory(null,"soc-pokec-relationships.txt-reduced");
			
//			g = FastGraph.randomGraphFactory(2,1,1000,true,false); // 1 hundred nodes, 1 thousand edges
			g = FastGraph.randomGraphFactory(100,1000,1,true,false); // 2 hundred nodes, 2 thousand edges
//			g = FastGraph.randomGraphFactory(200,2000,1,true,false); // 3 hundred nodes, 3 thousand edges
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
Debugger.log("h consistent "+h.checkConsistency());
Debugger.log("g consistent "+g.checkConsistency());
Debugger.log(Arrays.equals(h.degreeProfile(), g.degreeProfile())+" "+Arrays.toString(h.degreeProfile()));
Debugger.log(Arrays.equals(h.inDegreeProfile(), g.inDegreeProfile())+" "+Arrays.toString(h.inDegreeProfile()));
Debugger.log(Arrays.equals(h.outDegreeProfile(), g.outDegreeProfile())+" "+Arrays.toString(h.outDegreeProfile()));

System.exit(0);
*/
		int numOfNodes = 4;
		long time = Debugger.createTime();		
		ExactMotifFinder emf = new ExactMotifFinder(g);
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = new HashMap<String,LinkedList<IsoHolder>>(g.getNumberOfNodes());
		emf.findMotifs(numOfNodes, 0, hashBuckets);
		emf.outputHashBuckets(hashBuckets);
		//emf.findMotifs(numOfNodes, 0, hashBuckets);
		//emf.outputHashBuckets(hashBuckets);
		
	//	Debugger.log("number of subgraphs "+emf.subgraphs.size());
		Debugger.log("graph with "+g.getNumberOfNodes()+" nodes and "+g.getNumberOfEdges()+" edges");
		Debugger.outputTime("time for motifs with "+numOfNodes+" nodes",time);

		//Debugger.log(hashBuckets);
		

		
		HashMap<String,IsoHolder> isoLists = emf.extractGraphLists(hashBuckets);
		uk.ac.kent.displayGraph.display.GraphWindow gw = null;
		for(String key : isoLists.keySet()) {
			IsoHolder isoList = isoLists.get(key);
			Debugger.log(key+" "+isoList.getNumber());
			
			
			uk.ac.kent.displayGraph.Graph dg = isoList.getGraph().generateDisplayGraph();
			dg.randomizeNodePoints(new Point(20,20),300,300);
			dg.setLabel(key);
			gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg, true);
			uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
			GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
			se.setAnimateFlag(false);
			se.setIterations(100);
			se.setTimeLimit(200);
			se.setGraphPanel(gw.getGraphPanel());
			se.layout();
			File saveLocation = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+g.getName()+File.separatorChar+"motifs"+key+".svg");
			uk.ac.kent.displayGraph.ExportSVG exSVG = new uk.ac.kent.displayGraph.ExportSVG(dg);
			exSVG.saveGraph(saveLocation);
			//gw.fileExit();
		}
		if(gw != null) {
			gw.fileExit();
		}
		

	
	}
	
	/**
	 * Outputs a given map hashBuckets to the screen. Note: Not to a file!
	 * 
	 * @param hashBuckets The hashbuckets to output
	 */
	public void outputHashBuckets(HashMap<String,LinkedList<IsoHolder>> hashBuckets) {
		int count = 0;
		for(String key : hashBuckets.keySet()) {
			LinkedList<IsoHolder> sameHashList = hashBuckets.get(key);
//			Debugger.log("hash string \""+key+"\" number of different isomorphic groups "+sameHashList.size());
			for(IsoHolder isoList: sameHashList) {
Debugger.log("hash string \t"+key+"\tnum of diff isom groups\t"+sameHashList.size()+"\tnum of nodes in iso list\t"+isoList.getNumber());
				count += isoList.getNumber();
				
//				uk.ac.kent.displayGraph.Graph dg = isoList.getGraph().generateDisplayGraph();
//				dg.randomizeNodePoints(new Point(20,20),300,300);
//				dg.setLabel(key);
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
		Debugger.log("stored subgraphs "+count);
		ExactIsomorphism.reportFailRatios();
		ExactIsomorphism.reportTimes();
	}

	/**
	 * 
	 * @param g the FastGraph to find motifs in
	 * @param numOfNodes the number of nodes in each motif
	 */
	public ExactMotifFinder(FastGraph g) {
		this(g,null);
	}
	
	/**
	 * 
	 * @param g the FastGraph to find motifs in
	 * @param mf The MotifTask to report progress to
	 */
	public ExactMotifFinder(FastGraph g, MotifTask mt) {
		this.g = g;
		this.mt = mt;
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
	 * Finds and exports all motifs between the given sizes for this FastGraph.<br>
	 * Will only export if this is a reference set.<br>
	 * Will load motifs from files if the output text file exists.<br>
	 * <br>
	 * @param rewiresNeeded The number of times the graph needs to be rewired (0 if none required)
	 * @param minSize The minimum size of motifs being investigated
	 * @param maxSize The maximum size of motifs being investigated
	 * @param motifSampling The number of motifs to sample?
	 * @param referenceSet Are these motifs the ones that will be referenced against? Will be saved to disk, if so
	 * @throws IOException Files cannot be read
	 */
	public void findAndExportAllMotifs(int rewiresNeeded, int minSize, int maxSize, int motifSampling, boolean referenceSet) throws IOException {
		long time = Debugger.createTime();
		
		mt.publish(0,"Finding Motifs", false);
		
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = new HashMap<String,LinkedList<IsoHolder>>(g.getNumberOfNodes());
		//HashMap<String,LinkedList<FastGraph>> isoLists = new HashMap<String,LinkedList<FastGraph>>();
		
		HashMap<String,IsoHolder> isoLists = new HashMap<String,IsoHolder>();
		
		String graphName = g.getName();
		String nameString = "_reference";
		if(!referenceSet) {
			rewiresNeeded = 0;
			nameString = "_real";
		}
		
		//finds all the motifs
		File output = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName+File.separatorChar+"motifs"+nameString+".txt");
		if(output.exists()) {
			//then motifs already exist
			
			Debugger.log("loading from file");
			loadMotifsFromFiles(output, new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName), isoLists, hashBuckets);
			this.hashBuckets = hashBuckets;
			
		} else {
			//otherwise, generate motifs
			
			HashMap<String,IsoHolder> newList = findAllMotifs(rewiresNeeded, minSize, maxSize, motifSampling, hashBuckets);
			Debugger.log("merging lists");
			isoLists = mergeIsoLists(isoLists, newList);

			isoLists = extractGraphLists(hashBuckets);
			
			Debugger.outputTime("Time to find motifs", time);
			Debugger.log();
			time = Debugger.createTime();
			
			int totalSize = 0;
			for(IsoHolder isoList : isoLists.values()) {
			//	Debugger.log("    "+isoList.getKey() + " " + isoList.getNumber());
				totalSize+= isoList.getNumber();
			}
			Debugger.log("#TOTAL SIZE: "+totalSize);
			//export the motifs

			if(referenceSet) {
				mt.publish(33, "Saving Reference Set", 0, "");	
			} else {
				mt.publish(66, "Saving Main Set", 0, "");	
			}
			
			//build the output file, and if needed, save the motif buffers
			StringBuilder sb = new StringBuilder();
			int outputCounter = 0;
			long outputTime = Debugger.createTime();
			for(String key : hashBuckets.keySet()) {
				LinkedList<IsoHolder> holders = hashBuckets.get(key);
				int count = 1;
				outputCounter++;
				
				int outputPercentage = (int) ( ((double) outputCounter/hashBuckets.size())*100);
				mt.publish(outputPercentage, "Saving motif " + outputCounter + " of " + hashBuckets.size(),false);
				
				for (IsoHolder holder : holders) {
			//		Debugger.log("    "+holder.getKey() + " num: " + holder.getNumber() + " total: " + totalSize);
					double percentage = ((double) holder.getNumber()/totalSize)*100;
					sb.append(key+"-"+count+"\t"+holder.getNumber() + "\t" + String.format( "%.10f", percentage ) +"\n");
					
					//save buffer
					//if(referenceSet) {
						FastGraph gOut = holder.getGraph();
						gOut.setName(key+"-"+count);
						gOut.saveBuffers("motifs"+File.separatorChar+graphName+File.separatorChar+key+"-"+count, key+"-"+count);
						
						//save SVG
						exportSVG(holder,count);
					//}

					count++;
				}
				
				if(outputCounter % 1000 == 0) {
					Debugger.outputTime("Saved "+outputCounter+" so far, out of " + hashBuckets.size() + " in ", outputTime);
				}
			}

			//save the motif info file
			//File output = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName+File.separatorChar+"motifs"+nameString+".txt");
			try(PrintWriter out = new PrintWriter( output )){ //will close file after use
			    out.println( sb );
			}
			this.hashBuckets = hashBuckets;
			
			Debugger.outputTime("Time to save: ", time);
		}
		
		
	}
	
	/**
	 * Returns the hashbuckets from this instance
	 * @return The hashbuckets
	 */
	public HashMap<String,LinkedList<IsoHolder>> getHashBuckets() {
		return hashBuckets;
	}
	
	/**
	 * Generates a list mapping of keys to lists of motifs with that key
	 * 
	 * @param rewiresNeeded The number of times the graph needs to be rewired
	 * @param minSizeOfMotifs The minimum size of motifs being investigated
	 * @param maxSizeOfMotifs The maximum size of motifs being investigated
	 * @param motifSampling The number of motifs to sample?
	 * @param hashBuckets The buckets to store the results in
	 * @return The map of keys to buckets of motifs that match those keys
	 */
	public HashMap<String,IsoHolder> findAllMotifs(int rewiresNeeded, int minSizeOfMotifs, int maxSizeOfMotifs, int motifSampling, HashMap<String,LinkedList<IsoHolder>> hashBuckets) {
		HashMap<String,IsoHolder> isoLists = new HashMap<>();

		FastGraph currentGraph = g;
		
		if(rewiresNeeded == 0) {
			ExactMotifFinder emf = new ExactMotifFinder(g);
			for(int i = minSizeOfMotifs; i <= maxSizeOfMotifs; i++) {
				emf.findMotifs(i, 0, hashBuckets);
				HashMap<String,IsoHolder> newIsoLists = emf.extractGraphLists(hashBuckets);
				Debugger.log("    merging lists");
				isoLists = mergeIsoLists(isoLists, newIsoLists);
			}
		} else {
			for (int i = 0; i < rewiresNeeded; i++) {
				int rewirePercentage = (int) ((double) i / rewiresNeeded)*100;
				mt.publish(rewirePercentage, "Rewiring "+(i+1)+" out of "+rewiresNeeded+" times",false);	
				Debugger.log("    rewiring for the "+i+" time");
				currentGraph = currentGraph.generateRandomRewiredGraph(1,1); 
				ExactMotifFinder emf = new ExactMotifFinder(currentGraph);
				Debugger.log("    finding motifs");
				for(int j = minSizeOfMotifs; j <= maxSizeOfMotifs; j++) {
					
					mt.publish(rewirePercentage, "Finding motifs sized "+j+" ("+(i+1)+"/"+rewiresNeeded+")",false);	
					
					emf.findMotifs(j, 0, hashBuckets);
					HashMap<String,IsoHolder> newIsoLists = emf.extractGraphLists(hashBuckets);
					Debugger.log("    merging lists");
					isoLists = mergeIsoLists(isoLists, newIsoLists);
				}
				//currentGraph = newGraph;
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
		
		subgraphs = enumerator.enumerateSubgraphs(k, 5 ,10);
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
					IsoHolder newIsoList = new IsoHolder(hashString, 1, subgraph);
					//newIsoList.setGraph(subgraph);
					sameHashList.add(newIsoList);
				}
			} else {
				LinkedList<IsoHolder> newHashList = new LinkedList<IsoHolder>();
				hashBuckets.put(hashString, newHashList);
				IsoHolder newIsoList = new IsoHolder(hashString, 1, subgraph);
				//newIsoList.add(subgraph);
				newHashList.add(newIsoList);
			}

		}
		
	}
	
	/**
	 * Loads motifs from files.<br>
	 * Uses the log file to find which motifs to load and their occurrences in the original reference set.<br>
	 * Loads the motifs and rebuilds the isoLits and hashbuckets<br>
	 * 
	 * @param logFile The log file to load
	 * @param directory The directory of the log file - used to load the motifs
	 * @param isoLists The iosLists to populate
	 * @param hashBuckets The hashbuckets to populate
	 * @throws FileNotFoundException If any of the files cannot be found
	 * @throws IOException If there is a problem loading any of the files
	 */
	public void loadMotifsFromFiles(File logFile, File directory, HashMap<String,IsoHolder> isoLists, HashMap<String,LinkedList<IsoHolder>> hashBuckets) throws FileNotFoundException, IOException {
		
		int directories = (int) (Files.find(Paths.get(directory.toString()),1,(path, attributes) -> attributes.isDirectory()).count() - 1);
		int i = 0;
		
		long time = Debugger.createTime();
		try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	i++;
	    	
		    	if(line.equals("\n") || line.equals("\r\n") || line.equals("")) {
		    		continue;
		    	}
		    	
		    	//update the GUI
	    		int percent = (int) (((double) i / directories)*100);
		    	mt.publish(percent, "Loading motif "+i+" of "+directories, false);
		    	
		    	String[] lineArr = line.split("\t");
		    	Debugger.log(directory.toString() + " file: " + lineArr[0]);
		    	FastGraph h = FastGraph.loadBuffersGraphFactory(directory.toString()+File.separatorChar+lineArr[0], lineArr[0]);
		    	Debugger.log("num of nodes" + h.getNumberOfNodes());
		    	
		    	String isoKey = lineArr[0];
		    	int lastIndex = isoKey.lastIndexOf("-");
		    	String key = isoKey.substring(0, lastIndex);
		    	int isoIndex = Integer.parseInt(isoKey.substring(lastIndex+1));
		    	
		    	//Debugger.log("key: " + key);
		    	//Debugger.log("index: " + isoIndex);
		    	
		    	IsoHolder holder = new IsoHolder(key, Integer.parseInt(lineArr[1]), h);
		    	
		    	//store in isoholder
		    	isoLists.put(key, holder);
		    	
		    	//store in hashbuckets
		    	if(hashBuckets.containsKey(key)) {
		    		LinkedList<IsoHolder> list = hashBuckets.get(key);
		    		list.add(isoIndex-1, holder);
		    	} else {
		    		LinkedList<IsoHolder> list = new LinkedList<IsoHolder>();
		    		hashBuckets.put(key, list);
		    		list.add(isoIndex-1, holder);
		    	}
		    }
		}
		Debugger.outputTime("Loading complete in", time);
	}
	
	/**
	 * Exports the given IsoHolder (and isomorphic count) to SVG.<br>
	 * Saves as motifs/[graph name]/[iso key]-count/motif.svg
	 * 
	 * @param isoList The IsoHolder for this graph
	 * @param count The isomorphic count
	 */
	private void exportSVG(IsoHolder isoList, int count) {
		String key = isoList.getKey();
		//int count = isoList.getNumber();
		uk.ac.kent.displayGraph.Graph dg = isoList.getGraph().generateDisplayGraph();
		dg.randomizeNodePoints(new Point(20,20),300,300);
		dg.setLabel(key);
		uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg, false);
		uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
		se.setAnimateFlag(false);
		se.setIterations(100);
		se.setTimeLimit(200);
		se.setGraphPanel(gw.getGraphPanel());
		se.layout();
		File saveLocation = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+g.getName()+File.separatorChar+key+"-"+count+File.separatorChar+"motif.svg");
		uk.ac.kent.displayGraph.ExportSVG exSVG = new uk.ac.kent.displayGraph.ExportSVG(dg);
		exSVG.saveGraph(saveLocation);
		
	}
	
	/**
	 * Compares the results from the reference set to the real set.<br>
	 * Exports these in a user friendly manner
	 * 
	 * @param referenceBuckets The hashbuckets of the reference set (may be loaded or calculated)
	 * @param realBuckets The hashbuckets of the real set.
	 * @throws IOException If the File cannot be read
	 * @throws FileNotFoundException  If the file cannot be found
	 */
	public void compareAndExportResults(HashMap<String,LinkedList<IsoHolder>> referenceBuckets, HashMap<String,LinkedList<IsoHolder>> realBuckets) throws FileNotFoundException, IOException {
		String graphName = g.getName();
		File refOutput = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName+File.separatorChar+"motifs_reference"+".txt");
		File realOutput = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graphName+File.separatorChar+"motifs_real"+".txt");
		
		HashMap<String,MotifResultHolder> results = new HashMap<String,MotifResultHolder>();
		buildResults(results, refOutput, true);
		buildResults(results, realOutput, false);
		
		ArrayList<MotifResultHolder> motifResults = new ArrayList<MotifResultHolder>(results.values());
		
		//sort by the significance method, then by the percentage of occurrences in the real set
		motifResults.sort(Comparator.comparing(MotifResultHolder::generateSignificance).thenComparing(MotifResultHolder::getRealPercentage).reversed());
		
		Debugger.log("length of motifResults" + motifResults.size());
		
		mt.publish(83, "Exporting Results", 0, "");
		
		int sample = 1000;
		int numberOfPages = (int) Math.ceil((double) motifResults.size()/sample);
		for(int i = 0; i < numberOfPages; i++) {
			int pagePercentage = (int) (((double) i/numberOfPages)*100);
			mt.publish(pagePercentage, "Exporting page "+i+" of "+numberOfPages, false);
			
			buildPage(i,numberOfPages,Util.subList(motifResults,i*sample, (i*sample)+sample));
		}	
		
		Debugger.log("number of pages required: " + numberOfPages);
		//Debugger.log(motifResults);
		
	}

	/**
	 * Builds a list of results from the specified file
	 * 
	 * @param results The list of results to populate
	 * @param logFile The log file to read
	 * @param referenceSet If this is a reference set
	 * @return The populated list of results
	 * @throws IOException If the File cannot be read
	 * @throws FileNotFoundException  If the file cannot be found
	 */
	private HashMap<String,MotifResultHolder> buildResults(HashMap<String,MotifResultHolder> results, File logFile, boolean referenceSet) throws FileNotFoundException, IOException {
		long time = Debugger.createTime();
		try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.equals("\n") || line.equals("\r\n") || line.equals("")) {
		    		continue;
		    	}
		    	String[] lineArr = line.split("\t");
		    	String key = lineArr[0];
		    	double percentage = Double.parseDouble(lineArr[2]);
		    	
		    	//store result
		    	if(results.containsKey(key)) {
		    		MotifResultHolder result = results.get(key);
		    		result.setRealPercentage(percentage);
		    	} else {
		    		MotifResultHolder result = new MotifResultHolder();
			    	result.setKey(key);
			    	result.setReferencePercentage(percentage);
			    	results.put(key,result);
		    	}		    	
		    }
		}
		Debugger.outputTime("Loading complete in", time);
		return results;
	}
	
	/**
	 * Builds and exports a particular page for the HTML output.<br>
	 * Note: expects the results list to only contain the required results to display
	 * 
	 * @param pageNumber This page number
	 * @param totalPages The total number of pages
	 * @param results The list of results to output
	 * @throws FileNotFoundException If the output file cannot be written to
	 */
	private void buildPage(int pageNumber, int totalPages, List<MotifResultHolder> results) throws FileNotFoundException {

		Debugger.log("length of output results" + results.size());
		
		Document doc = Document.createShell("");
		
		doc.head().appendElement("title").text(g.getName());

		Element headline = doc.body().appendElement("h1").text(g.getName());
		Element pageNumberHeader = doc.body().appendElement("h2").text("Page "+(pageNumber+1));
		Element linksDiv = doc.body().appendElement("div");
		linksDiv.appendElement("a").text("1").attr("href", "index.html");
		for(int i = 1; i < totalPages; i++) {
			linksDiv.appendElement("a").text((i+1)+"").attr("href", "index"+(i+1)+".html");
		}
		doc.body().appendElement("br");
		//build output table
		Element table = doc.body().appendElement("table").attr("style", "border: 2px solid; border-collapse: collapse");
		Element headerRow = table.appendElement("tr").attr("style", "border: 2px solid;");
		headerRow.appendElement("th").text("Image").attr("style", "border: 1px solid;");
		headerRow.appendElement("th").text("Key").attr("style", "border: 1px solid;");
		headerRow.appendElement("th").text("Diff in %").attr("style", "border: 1px solid;");
		headerRow.appendElement("th").text("% of motifs").attr("style", "border: 1px solid;");
		headerRow.appendElement("th").text("% in reference set").attr("style", "border: 1px solid;");
		for(MotifResultHolder result : results) {
			Element row = table.appendElement("tr");
			Element imageCell = row.appendElement("td").attr("style", "border: 1px solid;");
			imageCell.appendElement("img").attr("src",result.getKey()+"/motif.svg");
			row.appendElement("td").text(result.getKey()).attr("style", "border: 1px solid;");
			row.appendElement("td").text(String.format( "%.4f", result.generateSignificance())).attr("style", "border: 1px solid;");
			row.appendElement("td").text(String.format( "%.4f", result.getRealPercentage())).attr("style", "border: 1px solid;");
			row.appendElement("td").text(String.format( "%.4f", result.getReferencePercentage())).attr("style", "border: 1px solid;");
		}
		doc.body().appendElement("br");
		//output links again
		linksDiv = doc.body().appendElement("div");
		linksDiv.appendElement("a").text("1").attr("href", "index.html");
		for(int i = 1; i < totalPages; i++) {
			linksDiv.appendElement("a").text((i+1)+"").attr("href", "index"+(i+1)+".html");
		}
		
		String outputNum = (pageNumber+1)+"";
		if(pageNumber == 0) {
			outputNum = "";
		}
		File output = new File(Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+g.getName()+File.separatorChar+"index"+outputNum+".html");
		//save the output html file
		
		try(PrintWriter out = new PrintWriter( output )){ //will close file after use
		    out.println( doc.toString() );
		}
	}
	
	/**
	 * Inner class to hold details of motifs ready for output
	 * @author Rob Baker
	 *
	 */
	private class MotifResultHolder {
		private String key; //the key
		private double referencePercentage, realPercentage; //the relevant percentages

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the referencePercentage
		 */
		public double getReferencePercentage() {
			return referencePercentage;
		}

		/**
		 * @param referencePercentage the referencePercentage to set
		 */
		public void setReferencePercentage(double referencePercentage) {
			this.referencePercentage = referencePercentage;
		}

		/**
		 * @return the realPercentage
		 */
		public double getRealPercentage() {
			return realPercentage;
		}

		/**
		 * @param realPercentage the realPercentage to set
		 */
		public void setRealPercentage(double realPercentage) {
			this.realPercentage = realPercentage;
		}
		
		/**
		 * Outputs a string representation of this object
		 */
		public String toString() {
			return key+" "+referencePercentage+" "+getRealPercentage();
		}
		
		/**
		 * Generates the significance of this result. This will be used to compare them
		 * @return The significance
		 */
		public double generateSignificance() {
			return getRealPercentage() - getReferencePercentage();
		}
		
	}
	
	
	/**
	 * Class to hold a FastGraph and the number of instances of that FastGraph for a particular key.
	 * Saves keeping big lists and lists of lists.
	 * 
	 * @author Rob Baker
	 *
	 */
	public class IsoHolder {
		
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
		
		public String toString() {
			return key+"|"+number;
		}
		
	}
	

}
