package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Converter from ARG file formats to FastGraph. For profiling.
 * 
 * NOTE: ONLY HANDLES UNDIRECTED GRAPHS - ones with the prefix s2, s4 or s6.
 * 
 * Taken from http://mivia.unisa.it/datasets/graph-database/arg-database/documentation/
 * 
 The graph file format
 The graphs are stored in a compact binary format, one graph per file. The file is composed of 16 bit words, 
 which are represented using the so-called little-endian convention, i.e. the least significant byte of the word is stored first.
 
 Two different formats are used for labeled and unlabeled graphs. 
 In unlabeled graphs, the first word contains the number of nodes in the graph; this means that this format can deal with 
 graphs of up to 65535 nodes (however, actual graphs in the database are quite smaller, up to 1024 nodes). Then, for each node, 
 the file contains the list of edges coming out of the node itself. The list is represented by a word encoding its length, 
 followed by a word for each edge, representing the destination node of the edge. Node numeration is 0-based, so the first node 
 of the graph has index 0.The following C code shows how a graph file can be read into an adjacency matrix; the code assumes that 
 the input file has been opened using the binary mode.
 
 * 
 * @author Rob Baker
 *
 */
public class ArgParser {

	/**
	 * Main method. Change some values in here for converting the graphs.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		//TODO Change the following line:
		String graph = "data\\arg\\si2_b03\\si2\\bvg\\b03\\si2_b03_m200.A00";		
		
		ArgParser ap = new ArgParser();
		Path path = Paths.get(graph);
		FastGraph fg = ap.convertGraph(path);
		fg.displayFastGraph(); //TODO Optional
		fg.saveBuffers("profiling"+File.separatorChar+path.getFileName().toString(), path.getFileName().toString());
	}	
	
	/**
	 * Converts a graph from the ARG dataset to a FastGraph
	 * @param path The path to the file
	 * @return The converted FastGraph
	 */
	public FastGraph convertGraph(Path path) {		
		FastGraph result = null;
		try {
			ByteBuffer bytes = ByteBuffer.wrap(Files.readAllBytes(path)); //read files
			bytes.order(ByteOrder.LITTLE_ENDIAN); //ARG files are little endian, so beware of that
			result = convertUndirectedGraph(bytes, path.getFileName().toString());			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Handles the actual conversion
	 * @param bytes The ByteBuffer generated from reading the file
	 * @param graphName The name of the graph
	 * @return The converted FastGraph
	 */
	private FastGraph convertUndirectedGraph(ByteBuffer bytes, String graphName) {
		bytes.rewind(); //just in case
		int numberOfNodes = bytes.getShort(); //increments
		int totalEdges = 0; //used for edge IDs and labels
		
		ArrayList<NodeStructure> nodes = new ArrayList<>(); //For holding structures
		ArrayList<EdgeStructure> edges = new ArrayList<>(); //For holding structures

		for(int i = 0; i < numberOfNodes; i++) {
			int numberOfEdges = bytes.getShort(); //increments
			
			NodeStructure n = new NodeStructure(i, "n"+i, 0, (byte) 0, (byte) 0);
			nodes.add(n);
			
			for(int j = 0; j < numberOfEdges; j++) {
				int target = bytes.getShort(); //increments
				
				EdgeStructure e = new EdgeStructure(totalEdges, "e"+totalEdges, (byte) 0, (byte) 0, (byte) 0, i, target);
				edges.add(e);
				totalEdges++;
			}
		}
		FastGraph fg = FastGraph.structureFactory(graphName, (byte) 0, nodes, edges, false); //Build a FG from this
		
		return fg;
	}

	
}
