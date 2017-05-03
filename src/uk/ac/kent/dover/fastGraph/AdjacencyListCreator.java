package uk.ac.kent.dover.fastGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Converts FastGraphs into Adjacency Lists. Used for profiling.
 * 
 * @author Rob Baker
 *
 */
public class AdjacencyListCreator {

	/**
	 * Main method. Converts FG to ADJ list
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//TODO change these as required
		String graphName = "simple-random-n-4-e-4-time";
		FastGraph g = FastGraph.loadBuffersGraphFactory("data"+File.separatorChar+graphName,graphName);
		File outputFile = new File(Launcher.startingWorkingDirectory+File.separatorChar+"data"
				+File.separatorChar+graphName+".adj");
		
	
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < g.getNumberOfEdges(); i++) {
			int n1 = g.getEdgeNode1(i);
			int n2 = g.getEdgeNode2(i);
			sb.append(n1 + " " + n2 + "\n");
		}
		
		outputFile.getParentFile().mkdirs();
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(outputFile), "utf-8"))) {
			writer.write(sb.toString());
		}
	
	}
	
}
