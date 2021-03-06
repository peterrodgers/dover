package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import uk.ac.kent.dover.fastGraph.Gui.LauncherGUI;
import uk.ac.kent.dover.fastGraph.Gui.MotifTask;
import uk.ac.kent.dover.fastGraph.comparators.EdgeComparator;
import uk.ac.kent.dover.fastGraph.comparators.NodeComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleEdgeLabelComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleNodeLabelComparator;
import uk.ac.kent.dover.fastGraph.comparators.TimeEdgeComparator;
import uk.ac.kent.dover.fastGraph.graphSimilarity.GraphSimilarity;
import uk.ac.kent.dover.fastGraph.graphSimilarity.NodeDegreeDifference;

/**
 * Main class from which all the other functionality is called.
 * 
 * @author Rob Baker
 *
 */
public class Launcher {
	
	public static final String startingWorkingDirectory = System.getProperty("user.dir");
	public final String DATA_URL = "https://www.cs.kent.ac.uk/projects/dover/"; //the URL for extra data
	public static final int DEFAULT_SUBGRAPH_ENUMERATION_ATTEMPTS = 20; //the default number of attempts when enumerating a subgraph
	
	/**
	 * Main method
	 * 
	 * If no parameters given, loads GUI interface. Otherwise:
	 * 
	 * Takes in a variety of parameters which are detailed in LauncherCmd, or by running with the -h parameter.
	 * 
	 * @param args The command line instructions given
	 * @throws Exception Only used when testing direct access to FastGraph
	 */
	public static void main(String[] args) throws Exception {
		Debugger.enabled = false;
		new Launcher(args);
	}
	
	/**
	 * Constructor to avoid every method being static
	 * 
	 * @param args The command line instructions given
	 * @throws Exception Only used when testing direct access to FastGraph
	 */
	public Launcher(String[] args) throws Exception{
		Debugger.enabled = false;

		//if there are no arguments given, then load the GUI. Otherwise, load the command line interface
		if (args.length == 0) {
			new LauncherGUI(this);
		} else {
			new LauncherCmd(this, args);
		}
	}
	
	/**
	 * Loads a graph from buffers. Called by both Launcher modes
	 * 
	 * @param directory The directory to load the buffers from
	 * @param fileBaseName The name of the buffers
	 * @return The FastGraph loaded
	 * @throws IOException Thrown if the buffers cannot be loaded
	 */
	public FastGraph loadFromBuffers(String directory, String fileBaseName) throws IOException {
		return FastGraph.loadBuffersGraphFactory(directory, fileBaseName);
	}
	
	/**
	 * Calls the conversion method to convert an Adjacency List to Buffers
	 * 
	 * @param nodeCount The number of nodes in the graph
	 * @param edgeCount The number of edges in the graph
	 * @param directory The directory to load the list from
	 * @param fileName The file name of the list
	 * @param direct Is the graph directed
	 * @throws Exception If the file cannot be loaded, or in the wrong format, or the buffers cannot be saved
	 */
	public void convertGraphToBuffers(int nodeCount, int edgeCount, String directory, String fileName, boolean direct) throws Exception {
		FastGraph g1 = FastGraph.adjacencyListGraphFactory(nodeCount, edgeCount, directory, fileName, direct);
		g1.saveBuffers(null,fileName);
	}

	
	/**
	 * Calls the method to find all motifs with the parameters given
	 * 
	 * @param mt The MotifTask to handle GUI updates
	 * @param directory The directory of the graph to be loaded
	 * @param fileBaseName The name of the graph to be loaded
	 * @param minNum The minimum size of motifs
	 * @param maxNum The maximum size of motifs
	 * @param saveAll If every example is to be saved
	 * @param reference The reference graph, if there is one
	 * @throws IOException If the files cannot be loaded
	 */
	public void findMotifs(MotifTask mt, String directory, String fileBaseName, int minNum, int maxNum, 
			boolean saveAll, File reference) throws IOException {
		
		long time = Debugger.createTime();
		
		double sizeDiff = maxNum - minNum;	
		double step = 100/(sizeDiff+4);
		
		mt.publish(0, "Loading Buffers", 0, "");
		mt.setSmallIndeterminate(true);
		FastGraph g2 = FastGraph.loadBuffersGraphFactory(directory, fileBaseName);
			
		mt.setSmallIndeterminate(false);			
		ExactMotifFinder emf = new ExactMotifFinder(g2, mt, saveAll);
		
		if(reference != null) {
			String name = reference.getName();
			String path = reference.getParent();
			FastGraph g3 = FastGraph.loadBuffersGraphFactory(path+File.separatorChar+name, path);
			emf.setReferenceGraph(g3);
		}
		
		emf.setSaveAll(false); //never want to save all examples in the rewired graph
		mt.publish((int) step, "Building Reference Set", 0, "");		
		emf.findMotifsReferenceSet(10,minNum,maxNum);
		
		emf.setSaveAll(saveAll);
		mt.publish((int) (100-(2*step)), "Building Main Set", 0, "");
		emf.findMotifsRealSet(minNum,maxNum);
		
		mt.publish((int) (100-step), "Comparing Motif Sets", 0, "");
		emf.compareMotifDatas(minNum,maxNum);
		
		//emf.compareAndExportResults(referenceBuckets, realBuckets);
		//emf.outputHashBuckets(referenceBuckets);
		Debugger.outputTime("Time total motif detection");
		mt.publish(100, "Complete", 0, "");
	}
	
	/**
	 * Calls the method to find subgraphs using the exact subgraph finder
	 * 
	 * @param targetGraph The graph to search in
	 * @param patternGraph The subgraph to find
	 * @throws IOException If the results cannot be saved
	 */
	public void exactSubgraphs(FastGraph targetGraph, FastGraph patternGraph) throws IOException {
		
		NodeComparator nc = null;
		if(patternGraph.isAnyNodeLabelled()) {
			nc = new SimpleNodeLabelComparator(targetGraph, patternGraph);
		}
		EdgeComparator ec = new TimeEdgeComparator(targetGraph, patternGraph);
		if(patternGraph.isAnyEdgeLabelled()) {
			ec = new SimpleEdgeLabelComparator(targetGraph, patternGraph);
		}

		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(targetGraph, patternGraph, nc, ec);
		boolean result = esi.subgraphIsomorphismFinder();
		esi.outputResults();

		esi = null; //GC
	}
	
	/**
	 * Calls the method to find subgraphs using the approximate subgraph finder
	 * 
	 * @param targetGraph The graph to search in
	 * @param patternGraph The subgraph to find
	 * @param patternNodes The number of nodes in the enumerated subgraphs
	 * @param subgraphsPerNode The number of subgraphs to generate per node
	 * @throws IOException If a subgraph cannot be saved
	 */
	public void approximateSubgraphs(FastGraph targetGraph, FastGraph patternGraph,
			int patternNodes, int subgraphsPerNode) throws IOException {
		
		NodeComparator nc = null;
		if(patternGraph.isAnyNodeLabelled()) {
			nc = new SimpleNodeLabelComparator(targetGraph, patternGraph);
		}
		EdgeComparator ec = new TimeEdgeComparator(targetGraph, patternGraph);
		if(patternGraph.isAnyEdgeLabelled()) {
			ec = new SimpleEdgeLabelComparator(targetGraph, patternGraph);
		}
		
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(targetGraph, patternGraph,
				patternNodes, subgraphsPerNode, nc, ec);
		isi.subgraphIsomorphismFinder();
		isi = null; //GC
	}
	
	/**
	 * Generates a random graph
	 * @param saveLocation Where to save the graph
	 * @param nodes Number of nodes in the graph
	 * @param edges Number of edges in the graph
	 * @param directed If the graph is directed
	 * @param simple If the graph is simple
	 * @throws Exception If the graph cannot be saved (or created). Could be IO
	 */
	public void generateRandomGraph(File saveLocation, int nodes, int edges, boolean directed, boolean simple) throws Exception {
		String directory = saveLocation.getPath();
		String name = saveLocation.getName();					
		
		FastGraph r = FastGraph.randomGraphFactory(nodes, edges, -1, simple, directed);
		r.setName(name);
		r.saveBuffers(directory, name);
		r = null; //GC
	}
	
	/**
	 * Finds motifs using the approximate method
	 * 
	 * @param targetGraph The graph to find motifs in
	 * @param minSize The minimum size of motifs
	 * @param maxSize The maximum size of motifs
	 * @param numOfClusters The number of clusters
	 * @param iterations The number of kMedoids iterations
	 * @param subgraphsPerNode The number of generated subgraphs per node
	 * @param attemptsToFindSubgraph The number of attempts to find a subgraph
	 * @throws FastGraphException If there is a problem in the kMedoids code
	 * @throws IOException If the clusters cannot be saved
	 */
	public void approximateMotifs(FastGraph targetGraph, int minSize, int maxSize, int numOfClusters, int iterations, 
			int subgraphsPerNode, int attemptsToFindSubgraph) throws FastGraphException, IOException {

/* TODO needs a list of possible graph similarity measures in interface */
		GraphSimilarity similarityMeasure = new NodeDegreeDifference(false);
		KMedoids km = new KMedoids(targetGraph, numOfClusters, iterations, similarityMeasure);
		EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(targetGraph);
		HashSet<FastGraph> subs = new HashSet<FastGraph>();
		for(int i = minSize; i <= maxSize; i++) {
			subs.addAll(esn.enumerateSubgraphs(i, subgraphsPerNode, attemptsToFindSubgraph));
		}
		
		ArrayList<FastGraph> subgraphs = new ArrayList<FastGraph>(subs);
		ArrayList<ArrayList<FastGraph>> clusters = km.cluster(subgraphs);

		km.saveClusters(clusters);		
	}
}
