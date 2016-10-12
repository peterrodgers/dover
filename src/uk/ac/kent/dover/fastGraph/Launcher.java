package uk.ac.kent.dover.fastGraph;

import java.util.Arrays;

/**
 * Main class from which all the other functionality is called.
 * 
 * @author Rob Baker
 *
 */
public class Launcher {
	
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
	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args.length == 0) {
			FastGraph.main(args); //will replace this with loading GUI instead
		} else {
			FastGraph.main(args); //will replace this with some actual handling of arguments
		}
	}

}
