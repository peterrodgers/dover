package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.dover.fastGraph.comparators.SimpleEdgeLabelComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleNodeLabelComparator;

/**
 * Class to perform the inexact subgraph isomorphism
 * 
 * @author Rob Baker
 *
 */
public class ApproximateSubgraphIsomorphism extends SubgraphIsomorphism {

	private FastGraph target, pattern;
	private int patternNodes, subgraphsPerNode;
	private HashMap<String,Integer> uniqueSubgraphs = new HashMap<String,Integer>();
	
	/**
	 * Trivial constructor. Assumes a subgraphsPerNode of 5.
	 * 
	 * @param target The target graph
	 * @param pattern The pattern graph
	 * @param patternNodes The number of nodes in the enumerated subgraphs
	 */
	public ApproximateSubgraphIsomorphism(FastGraph target, FastGraph pattern, int patternNodes) {
		this(target, pattern, patternNodes, 5);
	}
	
	/**
	 * Trivial constructor
	 * 
	 * @param target The target graph
	 * @param pattern The pattern graph
	 * @param patternNodes The number of nodes in the enumerated subgraphs
	 * @param subgraphsPerNode The number of subgraphs to generate per node.
	 */
	public ApproximateSubgraphIsomorphism(FastGraph target, FastGraph pattern, int patternNodes, int subgraphsPerNode) {
		this.target = target;
		this.pattern = pattern;
		this.patternNodes = patternNodes;
		this.subgraphsPerNode = subgraphsPerNode;
	}
	
	/**
	 * Performs the approximate subgraph isomorphism
	 * @throws FileNotFoundException If the output file cannot be created
	 * 
	 * @return The number of subgraphs that match
	 */
	public int subgraphIsomorphismFinder() throws FileNotFoundException {
		//don't want to generate potential subgraphs that are smaller than the pattern being searched for
		if(patternNodes < pattern.getNumberOfNodes()) {
			Debugger.log("error");
			return -1;
		}		
		
		int subgraphsTested = 0;
		
		File mainDir = new File(
				Launcher.startingWorkingDirectory+File.separatorChar+"subgraph_results"+
				File.separatorChar+target.getName()+"_"+Util.dateAsString()
			);
		
		mainDir.mkdirs(); //make directories if needed
		
		Random r = new Random(target.getNodeBuf().getLong(0));
		int count = 0;
		long time = Debugger.createTime();
		
		for (int i = 0; i < target.getNumberOfNodes(); i++) {
			if(i % 5000 == 0) {
				Debugger.outputTime("Completed node: " + i + " Found subs: "+count+" Unique subs: " + uniqueSubgraphs.size(),time);
			}
			
			//generate set of subgraphs
			EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(target);
			HashSet<FastGraph> subs = new HashSet<FastGraph>();
			esn.enumerateSubgraphsFromNode(patternNodes, subgraphsPerNode, 100, i, r, subs);
			
			subgraphsTested += subs.size();
			
			count = testSubgraphs(subs, count, mainDir);
			
			//Debugger.log("number of generated subs: " + subs.size());

		}
		
		Debugger.log("number of tested subs: " + subgraphsTested);
		Debugger.log("number of unique subs: " + uniqueSubgraphs.size());
		Debugger.log("number of found subs: " + count);
		buildHtmlOutput(target, mainDir, count, "Approximate");
		return count;
	}
	
	/**
	 * Tests the list of subgraphs
	 * 
	 * @param subs The potential subgraphs
	 * @param count The number of subgraphs that match so far
	 * @param mainDir The parent directory to save to
	 * @return The number of subgraphs that match
	 */
	private int testSubgraphs(HashSet<FastGraph> subs, int count, File mainDir) {
		//for each generated subgraph
		for(FastGraph sub : subs) {
			
			//check isomorphism
			SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(sub, pattern);
			SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(sub, pattern);
			ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(sub,pattern,snlc,selc);
			boolean result = esi.subgraphIsomorphismFinder();

			if(result) {
				LinkedList<SubgraphMapping> submaps = esi.getFoundMappings();
				
				for(SubgraphMapping map : submaps) {
					
					//Debugger.log(map.toString());						
					int[] nodeMapping = map.getNodeMapping();
					int[] edgeMapping = map.getEdgeMapping();
					FastGraph newSub = sub.generateGraphFromSubgraph(nodeMapping, edgeMapping);
					
					saveSubgraph(target, newSub, count, mainDir);
					count++;

					//add to unique list
					String key = newSub.getNodeLabelString() + newSub.getEdgeLabelString();
					if(uniqueSubgraphs.containsKey(key)) {
						uniqueSubgraphs.put(key, uniqueSubgraphs.get(key)+1);
					} else {
						uniqueSubgraphs.put(key,1);
					}
					
				}
			}
		}
		return count;
	}
	

	
	
}
