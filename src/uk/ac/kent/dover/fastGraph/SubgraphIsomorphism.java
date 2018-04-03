package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;

/**
 * Find one graph inside of another.
 * 
 * @author pjr
 *
 */
public abstract class SubgraphIsomorphism {

	/**
	 * Saves the given subgraph into the relevant folder with the key of count
	 * 
	 * @param target The target graph
	 * @param sub The FastGraph to save
	 * @param count The number of this FastGraph
	 * @param mainDir The parent directory to save to
	 * @throws IOException If the buffers cannot be saved 
	 */
	protected void saveSubgraph(FastGraph target, FastGraph sub, int count, File mainDir) throws IOException {
		//save graph
		sub.setName(target.getName());
		File dir = new File(mainDir.getAbsolutePath()+File.separatorChar+count);
		dir.mkdir();
		sub.saveBuffers(mainDir.getAbsolutePath()+File.separatorChar+count, target.getName());
		
		//save SVG
		uk.ac.kent.displayGraph.Graph dg = sub.generateDisplayGraph();
		dg.randomizeNodePoints(new Point(20,20),300,300);
		uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg, false);
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
	 * @param target The target graph
	 * @param mainDir The parent directory
	 * @param count The number of results
	 * @param type The type of subgraph isomorphism
	 * @throws FileNotFoundException If the output file cannot be created
	 */
	protected void buildHtmlOutput(FastGraph target, File mainDir, int count, String type) throws FileNotFoundException {
		Document doc = Document.createShell("");
		
		doc.head().appendElement("title").text(target.getName());

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
