package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.GedevoNativeUtil;
import uk.ac.kent.dover.fastGraph.Gui.LauncherGUI;
import uk.ac.kent.dover.fastGraph.Gui.MotifTask;
import uk.ac.kent.dover.fastGraph.comparators.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Main class from which all the other functionality is called.
 * 
 * @author Rob Baker
 *
 */
public class Launcher {
	
	public static final String startingWorkingDirectory = System.getProperty("user.dir");
	public final String DATA_URL = "https://www.cs.kent.ac.uk/projects/dover/"; //the URL for extra data
	
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
	public static void main(String[] args) throws Exception{
		Debugger.enabled = true;
		new Launcher(args);
	}
	
	/**
	 * Constructor to avoid every method being static
	 * 
	 * @param args The command line instructions given
	 * @throws Exception Only used when testing direct access to FastGraph
	 */
	public Launcher(String[] args) throws Exception{
		//System.out.println("Launched!");
		//System.out.println(Arrays.toString(args));
		//System.out.println(args.length);

		GedevoNativeUtil.initNativeLibs();
		//if there are no arguments given, then load the GUI. Otherwise, load the command line interface
		if (args.length == 0) {
			new LauncherGUI(this);
			//FastGraph.main(args); //will replace this with loading GUI instead
		} else {
			new LauncherCmd(this, args);
			//FastGraph.main(args); //will replace this with some actual handling of arguments
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
	 * @throws IOException If the files cannot be loaded
	 */
	public void findMotifs(MotifTask mt, String directory, String fileBaseName, int minNum, int maxNum, boolean saveAll) throws IOException {
		double sizeDiff = maxNum - minNum;	
		double step = 100/(sizeDiff+4);
		
		mt.publish(0, "Loading Buffers", 0, "");
		mt.setSmallIndeterminate(true);
		FastGraph g2 = FastGraph.loadBuffersGraphFactory(directory, fileBaseName);
			
		mt.setSmallIndeterminate(false);			
		ExactMotifFinder emf = new ExactMotifFinder(g2, mt, saveAll);
		
		emf.setSaveAll(false); //never want to save all examples in the rewired graph
		mt.publish((int) step, "Building Reference Set", 0, "");		
		emf.findAllMotifs(10,minNum,maxNum);
		
		emf.setSaveAll(saveAll);
		mt.publish((int) (100-(2*step)), "Building Main Set", 0, "");
		emf.findAllMotifs(0,minNum,maxNum);
		
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
	 * @throws FileNotFoundException If the output file cannot be saved
	 */
	public void exactSubgraphs(FastGraph targetGraph, FastGraph patternGraph) throws FileNotFoundException {
		
		NodeComparator nc = null;
		if(patternGraph.isAnyNodesLabelled()) {
			System.out.println("node labelled");
			nc = new SimpleNodeLabelComparator(targetGraph, patternGraph);
		}
		EdgeComparator ec = null;
		if(patternGraph.isAnyEdgesLabelled()) {
			System.out.println("edge labelled");
			ec = new SimpleEdgeLabelComparator(targetGraph, patternGraph);
		}

		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(targetGraph, patternGraph, nc, ec);
		boolean result = esi.subgraphIsomorphismFinder();
		esi.outputResults();
		//System.out.println("result:" + result);
		//System.out.println(esi.getFoundMappings());
		esi = null; //GC
	}
	
	/**
	 * Calls the method to find subgraphs using the approximate subgraph finder
	 * 
	 * @param targetGraph The graph to search in
	 * @param patternGraph The subgraph to find
	 * @throws FileNotFoundException If the output file cannot be saved
	 */
	public void approximateSubgraphs(FastGraph targetGraph, FastGraph patternGraph,
			int patternNodes, int subgraphsPerNode) throws FileNotFoundException {

		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(targetGraph, patternGraph, patternNodes, subgraphsPerNode);
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
	 * @throws Exception 
	 */
	public void generateRandomGraph(File saveLocation, int nodes, int edges, boolean directed, boolean simple) throws Exception {
		String directory = saveLocation.getPath();
		String name = saveLocation.getName();					
		
		FastGraph r = FastGraph.randomGraphFactory(nodes, edges, -1, simple, directed);
		r.setName(name);
		r.saveBuffers(directory, name);
		r = null; //GC
	}
}
