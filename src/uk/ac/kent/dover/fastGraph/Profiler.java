package uk.ac.kent.dover.fastGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;

import uk.ac.kent.dover.fastGraph.Gui.MotifTaskDummy;
import uk.ac.kent.dover.fastGraph.comparators.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.GraphSimilarity;
import uk.ac.kent.dover.fastGraph.graphSimilarity.NodeDegreeDifference;

/**
 * Profiles the existing methods
 * 
 * @author Rob Baker
 *
 */
public class Profiler {

	private File outputFile;
	private FastGraph targetGraph, patternGraph;
	private ArrayList<ProfilerResult> results = new ArrayList<>();
	
	/**
	 * Change this to determine what to profile
	 * @param args Unused
	 * @throws IOException If the graph cannot be loaded
	 */
	public static void main(String[] args) throws IOException {
		Debugger.enabled = false; //will mute all output except that below
		
		//User customisable variables for testing
		String outputFileName = "approxMotifTesting"; //TODO change this as needed
		String targetGraphName = "simple-random-n-10-e-20-time"; //TODO change this as needed
		String patternGraphName = "2-line-time-2"; //TODO change this as needed
		
		
		File outputFile = new File(Launcher.startingWorkingDirectory+File.separatorChar+"profiling"+File.separatorChar+outputFileName+".csv");
		FastGraph targetGraph = FastGraph.loadBuffersGraphFactory(null,targetGraphName);
		FastGraph patternGraph = FastGraph.loadBuffersGraphFactory(null,patternGraphName);
		
		//approx motif testing
		System.out.println("### Profiling motifs");
		Profiler p = new Profiler(outputFile, targetGraph, null);
		int size = 4, numOfClusters = 4, iterations = 2, subsPerNode = 5, attempts = 20; //TODO change this as needed
		p.profileApproximateMotif(size, numOfClusters, iterations, subsPerNode, attempts); 
		p.saveResult();
		System.out.println("### Profiling motifs Complete");
		
	/*	
		//exact motif testing
		System.out.println("### Profiling motifs");
		Profiler p = new Profiler(outputFile, targetGraph, null);
		p.testMotifMultiple(4,6); //TODO change this as needed
		p.saveResult();
		System.out.println("### Profiling motifs Complete");
	*/	
	
		/*
		//exact subgraph testing
		System.out.println("### Profiling subgraphs");
		Profiler p = new Profiler(outputFile, targetGraph, patternGraph);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(targetGraph, patternGraph); //TODO change this as needed
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(targetGraph, patternGraph); //TODO change this as needed
		p.profileExactSubgraphIsomorphism(snlc, selc);
		p.saveResult();
		System.out.println("### Profiling subgraphs Complete");
	*/
		
	/*	
		//approximate subgraph testing
		System.out.println("### Profiling approximate subgraphs");
		Profiler p = new Profiler(outputFile, targetGraph, patternGraph);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(targetGraph, patternGraph); //TODO change this as needed
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(targetGraph, patternGraph); //TODO change this as needed
		p.profileApproximateSubgraphIsomorhpism(4, 10, snlc, selc); //TODO change this as needed
		p.saveResult();
		System.out.println("### Profiling approximate subgraphs Complete");
	 */
	
	
	}
	
	/**
	 * Constructor
	 * @param outputFile The outputFile for the results
	 * @param targetGraph The target graph
	 * @param patternGraph The pattern graph (if there is one)
	 */
	public Profiler(File outputFile, FastGraph targetGraph, FastGraph patternGraph) {
		this.outputFile = outputFile;
		this.targetGraph = targetGraph;
		this.patternGraph = patternGraph;
	}
	
	/**
	 * Profiles the approximate subgraph isomorphism method
	 * 
	 * @param nodesInEnumSubgraphs The number of nodes in enumerated subgraphs
	 * @param subgraphsPerNode The number of subgraphs generated per node
	 * @param nc The node comparator to be used
	 * @param ec The edge comparator to be used
	 * @throws IOException If subgraphs cannot be saved or the files cannot be found
	 */
	public void profileApproximateSubgraphIsomorhpism(int nodesInEnumSubgraphs, int subgraphsPerNode, 
		NodeComparator nc, EdgeComparator ec) throws IOException {
		long time = Debugger.createTime();
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(targetGraph, 
				patternGraph, nodesInEnumSubgraphs, subgraphsPerNode, nc, ec);
		int result = isi.subgraphIsomorphismFinder();
		long timeResult = Debugger.createTime()-time;
		
		ProfilerResult pr = new ProfilerResult(targetGraph.getNumberOfNodes(), targetGraph.getNumberOfEdges(), targetGraph.getName(),
				patternGraph.getNumberOfNodes(), patternGraph.getNumberOfEdges(), patternGraph.getName(), 
				nodesInEnumSubgraphs, subgraphsPerNode, result, timeResult);
		results.add(pr);
	}
	
	/**
	 * Profiles the subgraph isomorphism
	 * @param nc Node Comparator
	 * @param ec Edge Comparator
	 */
	public void profileExactSubgraphIsomorphism(NodeComparator nc, EdgeComparator ec) {
		long time = Debugger.createTime();
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(targetGraph, patternGraph, nc, ec);
		long timeResult = Debugger.createTime()-time;
		int totalSize = esi.getFoundMappings().size();
		
		ProfilerResult pr = new ProfilerResult(targetGraph.getNumberOfNodes(), targetGraph.getNumberOfEdges(), targetGraph.getName(),
				patternGraph.getNumberOfNodes(), patternGraph.getNumberOfEdges(), patternGraph.getName(), -1, -1, totalSize, timeResult);
		results.add(pr);
		
	}
	
	/**
	 * This will run the exact motif finder multiple times, rather than finding a range.
	 * 
	 * @param minSize Min size of motifs
	 * @param maxSize Max size of motifs
	 * @throws IOException If the graph cannot be loaded
	 */
	public void profileExactMotifMultiple(int minSize, int maxSize) throws IOException {
		for(int i = minSize; i <= maxSize; i++) {
			System.out.println("    # Profiling motif of size "+i);
			profileExactMotif(i);
		}
	}
	
	/**
	 * Profiles the exact motif finder for a given size
	 * 
	 * @param size The size of motif to test
	 * @throws IOException If the graph cannot be loaded
	 */
	public void profileExactMotif(int size) throws IOException {		
		long time = Debugger.createTime();
		ExactMotifFinder emf = new ExactMotifFinder(targetGraph, new MotifTaskDummy(), true);
		emf.findMotifsReferenceSet(10,size,size); //reference
		emf.findMotifsRealSet(size,size); //real
		emf.compareMotifDatas(size,size);
		int totalSize = emf.getNumOfResults();
		long timeResult = Debugger.createTime()-time;
		
		ProfilerResult pr = new ProfilerResult(targetGraph.getNumberOfNodes(), targetGraph.getNumberOfEdges(), targetGraph.getName(),
				size, -1, "motif", -1, -1, totalSize, timeResult);
		results.add(pr);
	}
	
	/**
	 * Profiles the approx motif finder for a given size
	 * 
	 * @param size The size of motif to test
	 * @param numOfClusters The number of clusters
	 * @param iterations The number of iterations
	 * @param subsPerNode The number of subs per node
	 * @param attempts The number of attempts
	 */
	public void profileApproximateMotif(int size, int numOfClusters, int iterations, int subsPerNode, int attempts) {
		long time = Debugger.createTime();
		try{
			GraphSimilarity similarityMeasure = new NodeDegreeDifference(false);
			KMedoids km = new KMedoids(targetGraph, numOfClusters, iterations, similarityMeasure);
			EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(targetGraph);
			HashSet<FastGraph> subs = esn.enumerateSubgraphs(size, subsPerNode, attempts);
			ArrayList<FastGraph> subgraphs = new ArrayList<FastGraph>(subs);
			ArrayList<ArrayList<FastGraph>> clusters = km.cluster(subgraphs);
			km.saveClusters(clusters);
			
			long timeResult = Debugger.createTime()-time;
			ProfilerResult pr = new ProfilerResult(targetGraph.getNumberOfNodes(), targetGraph.getNumberOfEdges(), targetGraph.getName(),
					size, -1, "approxmotif", -1, -1, subs.size(), timeResult);
			results.add(pr);
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Outputs the results as a string
	 * @return the results
	 */
	private String outputResults() {
		StringBuilder sb = new StringBuilder();
		sb.append("targetNodes,");
		sb.append("targetEdges,");
		sb.append("targetName,");
		sb.append("patternNodes,");
		sb.append("patternEdges,");
		sb.append("patternName,");
		sb.append("nodesInEnumSubgraphs,");
		sb.append("subgraphsPerNode,");
		sb.append("numOfResults,");
		sb.append("time\n");
		
		for(ProfilerResult result : results) {
			sb.append(result.toString());
		}		
		return sb.toString();
	}
	
	/**
	 * Saves the profiling results to file
	 * 
	 * @throws UnsupportedEncodingException If the file cannot be written in UTF-8
	 * @throws FileNotFoundException If the file cannot be found
	 * @throws IOException Other IO problem
	 */
	public void saveResult() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		outputFile.getParentFile().mkdirs();
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(outputFile), "utf-8"))) {
			writer.write(outputResults());
		}
	
	};


	/**
	 * Holds a profile result
	 * 
	 * @author Rob Baker
	 *
	 */
	private class ProfilerResult {
		private int targetNodes, targetEdges, patternNodes, patternEdges, numOfResults, nodesInEnumSubgraphs, subgraphsPerNode;
		private long time;
		private String targetName, patternName;
		
		/**
		 * 
		 * @param targetNodes Number of nodes in target graph
		 * @param targetEdges Number of edges in target graph
		 * @param targetName Name of target graph
		 * @param patternNodes Number of nodes in pattern graph
		 * @param patternEdges Number of edges in pattern graph
		 * @param patternName Name of pattern graph
		 * @param nodesInEnumSubgraphs The number of nodes in the enumerated subgraphs (for approx methods)
		 * @param subgraphsPerNode The number of nodes per subgraph (for approx methods)
		 * @param numOfResults Number of results
		 * @param time Time taken to export results
		 */
		public ProfilerResult(int targetNodes, int targetEdges, String targetName, int patternNodes, int patternEdges, String patternName,
				int nodesInEnumSubgraphs, int subgraphsPerNode, int numOfResults, long time) {
			this.targetNodes = targetNodes;
			this.targetEdges = targetEdges;
			this.targetName = targetName;
			this.patternNodes = patternNodes;
			this.patternEdges = patternEdges;
			this.patternName = patternName;
			this.numOfResults = numOfResults;
			this.nodesInEnumSubgraphs = nodesInEnumSubgraphs;
			this.subgraphsPerNode = subgraphsPerNode;
			this.time = time;
		}
		
		/**
		 * Returns the string representation of this
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(targetNodes+",");
			sb.append(targetEdges+",");
			sb.append(targetName+",");
			sb.append(patternNodes+",");
			sb.append(patternEdges+",");
			sb.append(patternName+",");
			sb.append(numOfResults+",");
			sb.append(nodesInEnumSubgraphs+",");
			sb.append(subgraphsPerNode+",");
			sb.append(time+"\n");
			return sb.toString();
		}
	}
}
