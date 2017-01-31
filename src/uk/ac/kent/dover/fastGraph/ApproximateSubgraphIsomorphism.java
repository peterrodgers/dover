package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;
import uk.ac.kent.dover.fastGraph.comparators.SimpleEdgeLabelComparator;
import uk.ac.kent.dover.fastGraph.comparators.SimpleNodeLabelComparator;

/**
 * Class to perform the inexact subgraph isomorphism
 * 
 * @author Rob Baker
 *
 */
public class ApproximateSubgraphIsomorphism {

	private FastGraph target, pattern;
	private int patternNodes, subgraphsPerNode;
	private HashMap<String,Integer> uniqueSubgraphs = new HashMap<String,Integer>();
	
	/**
	 * For informal testing when developing
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Debugger.enabled = true;		
		
		FastGraph target = null;
		FastGraph pattern = null;
	/*	
		Graph patternGraph = new Graph("square with edge across");
		Node nn0 = new Node("David Martin");
		patternGraph.addNode(nn0);
		Node nn1 = new Node("");
		patternGraph.addNode(nn1);
		Node nn2 = new Node("");
		patternGraph.addNode(nn2);		
		Node nn3 = new Node("");
		patternGraph.addNode(nn3);		
		Edge ee0 = new Edge(nn0,nn1,"");
		patternGraph.addEdge(ee0);
		Edge ee1 = new Edge(nn1,nn2,"");
		patternGraph.addEdge(ee1);
		Edge ee2 = new Edge(nn2,nn3,"");
		patternGraph.addEdge(ee2);
		Edge ee3 = new Edge(nn3,nn0,"");
		patternGraph.addEdge(ee3);
		Edge ee4 = new Edge(nn2,nn0,"");
		patternGraph.addEdge(ee4);
	*/
		
		/*	
		Graph patternGraph = new Graph("4 clique");
		Node nn0 = new Node("David Martin");
		patternGraph.addNode(nn0);
		Node nn1 = new Node("");
		patternGraph.addNode(nn1);
		Node nn2 = new Node("");
		patternGraph.addNode(nn2);		
		Node nn3 = new Node("");
		patternGraph.addNode(nn3);		
		Edge ee0 = new Edge(nn0,nn1,"");
		patternGraph.addEdge(ee0);
		Edge ee1 = new Edge(nn1,nn2,"");
		patternGraph.addEdge(ee1);
		Edge ee2 = new Edge(nn2,nn3,"");
		patternGraph.addEdge(ee2);
		Edge ee3 = new Edge(nn3,nn0,"");
		patternGraph.addEdge(ee3);
		Edge ee4 = new Edge(nn2,nn0,"");
		patternGraph.addEdge(ee4);
		Edge ee5 = new Edge(nn1,nn3,"");
		patternGraph.addEdge(ee5);
	*/

		Graph patternGraph = new Graph("3 node straight line");
		Node nn0 = new Node("Gary Cook");
		patternGraph.addNode(nn0);
		Node nn1 = new Node("");
		patternGraph.addNode(nn1);
		Node nn2 = new Node("");
		patternGraph.addNode(nn2);			
		Edge ee0 = new Edge(nn0,nn1,"");
		patternGraph.addEdge(ee0);
		Edge ee1 = new Edge(nn1,nn2,"");
		patternGraph.addEdge(ee1);	


	
		pattern = FastGraph.displayGraphFactory(patternGraph,false);
		
		try {
			target = FastGraph.loadBuffersGraphFactory(null, "simple-random-n-100-e-500");
			//target = FastGraph.loadBuffersGraphFactory(null, "random-n-8-e-9");
			
			
			//pattern = FastGraph.loadBuffersGraphFactory(
			//		Launcher.startingWorkingDirectory+File.separatorChar+"subgraphs"+File.separatorChar+"4line", "4line");
		} catch(Exception e) {}
		Debugger.log("nodes in target graph: " + target.getNumberOfNodes());
		Debugger.log("nodes in pattern graph: " + pattern.getNumberOfNodes());
		Debugger.log("edges in pattern graph: " + pattern.getNumberOfEdges());

		long time = Debugger.createTime();
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(target, pattern, 6, 30);
		isi.subgraphIsomorphismFinder();
		Debugger.outputTime("Completed in: ", time);
	}
	
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
			if(i % 50 == 0) {
				Debugger.outputTime("Completed node: " + i + " Found subs: "+count,time);
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
		buildHtmlOutput(mainDir, count);
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
			ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(sub,pattern,snlc,null);
			boolean result = esi.subgraphIsomorphismFinder();

			if(result) {
				LinkedList<SubgraphMapping> submaps = esi.getFoundMappings();
				
				for(SubgraphMapping map : submaps) {
					
					//Debugger.log(map.toString());						
					int[] nodeMapping = map.getNodeMapping();
					int[] edgeMapping = map.getEdgeMapping();
					FastGraph newSub = sub.generateGraphFromSubgraph(nodeMapping, edgeMapping);
					
					saveSubgraph(newSub, count, mainDir);
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
	
	/**
	 * Saves the given subgraph into the relevant folder with the key of count
	 * @param sub The FastGraph to save
	 * @param count The number of this FastGraph
	 * @param mainDir The parent directory to save to
	 */
	private void saveSubgraph(FastGraph sub, int count, File mainDir) {
		//save graph
		File dir = new File(mainDir.getAbsolutePath()+File.separatorChar+count);
		dir.mkdir();
		sub.saveBuffers(mainDir.getAbsolutePath()+File.separatorChar+count, target.getName());
		
		//save SVG
		uk.ac.kent.displayGraph.Graph dg = sub.generateDisplayGraph();
		dg.randomizeNodePoints(new Point(20,20),300,300);
		uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg, false);
		uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
		se.setAnimateFlag(false);
		se.setIterations(100);
		se.setTimeLimit(200);
		se.setGraphPanel(gw.getGraphPanel());
		se.layout();
		File saveLocation = null;
		saveLocation = new File(mainDir.getAbsolutePath()+File.separatorChar+count+File.separatorChar+"subgraph.svg");
		uk.ac.kent.displayGraph.ExportSVG exSVG = new uk.ac.kent.displayGraph.ExportSVG(dg);
		exSVG.saveGraph(saveLocation);
		
	}
	
	/**
	 * Exports the results to a HTML file
	 * 
	 * @param mainDir The parent directory
	 * @param count The number of results
	 * @throws FileNotFoundException If the output file cannot be created
	 */
	private void buildHtmlOutput(File mainDir, int count) throws FileNotFoundException {
		Document doc = Document.createShell("");
		
		doc.head().appendElement("title").text(target.getName());

		Element headline = doc.body().appendElement("h1").text(target.getName());
		
		Element pageNumberHeader = doc.body().appendElement("h2").text("Inexact Subgraph Isomorphism");
		
		//size
		Element linksDiv = doc.body().appendElement("div");
		linksDiv.appendText("Subgraphs found: ");
		for(int i = 0; i < count; i++) {		
			linksDiv.appendElement("a").text(i+"").attr("href", i+"/subgraph.svg");
		}
		
		File output = new File(mainDir.getAbsolutePath()+File.separatorChar+"index.html");
		//save the output html file
		
		try(PrintWriter out = new PrintWriter( output )){ //will close file after use
		    out.println( doc.toString() );
		}
	}
	
	
}
