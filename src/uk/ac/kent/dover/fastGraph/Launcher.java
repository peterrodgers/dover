package uk.ac.kent.dover.fastGraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main class from which all the other functionality is called.
 * 
 * @author Rob Baker
 *
 */
public class Launcher {
	
	public static final String startingWorkingDirectory = System.getProperty("user.dir");
	
	/**
	 * Main method
	 * 
	 * If no parameters given, loads GUI interface. Otherwise:
	 * 
	 * Takes in a variety of parameters:
	 *  <none yet specified>
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new Launcher(args);
	}
	
	public Launcher(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));
		System.out.println(args.length);
		if (args.length == 0) {
			new LauncherGUI(this);
			//FastGraph.main(args); //will replace this with loading GUI instead
		} else {
			FastGraph.main(args); //will replace this with some actual handling of arguments
		}
	}
	
	public FastGraph loadFromBuffers(String directory, String fileBaseName) throws IOException {
		return FastGraph.loadBuffersGraphFactory(directory, fileBaseName);
	}
	
	public void convertGraphToBuffers(int nodeCount, int edgeCount, String directory, String fileName, boolean direct) throws Exception {
		FastGraph g1 = FastGraph.adjacencyListGraphFactory(nodeCount, edgeCount, directory, fileName, direct);
		g1.saveBuffers(null,fileName);
	}

}
