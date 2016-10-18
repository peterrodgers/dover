package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 
 * Runs the Command Line interface.
 * This will make callbacks to Launcher to execute methods in other parts of the system
 * This Launcher uses Apache's CLI interface to easily handle parameters and arguments.
 * A full list of commands is available by running the software with the -h or --help parameters.
 * 
 * @author Rob Baker
 *
 */
public class LauncherCmd {
	
	private Launcher launcher;

	/**
	 * Constructor that calls to the building and then parsing of command line options.
	 * 
	 * @param launcher
	 * @param args
	 */
	public LauncherCmd(Launcher launcher, String[] args) {
		this.launcher = launcher;
		
		//System.out.println("Cmd interface");
		Options options = buildOptions();
		parseCommands(options, args);
		
	}
	
	/**
	 * Builds the Options available to the user.
	 * This also specifies the help text that belongs to each item.
	 * 
	 * @return The CommandLine Options available to the user
	 */
	private Options buildOptions() {
		// create Options object
		Options options = new Options();

		// add convert options
		options.addOption("c", "convert", true, "Convert an Adjacency Matrix to buffers. Requires e,d,n.");
		options.addOption("n", "nodes", true, "Number of nodes in Adjacency Matrix to convert");
		options.addOption("e", "edges", true, "Number of edges in Adjacency Matrix to convert");
		options.addOption("d", "directed", true, "Adjacency Matrix to convert is a directed graph");
		
		// add help option
		options.addOption("h", "help", false, "Prints this message");
		
		// add t option
		options.addOption("t", false, "Runs the FastGraph as it used to in the early stages of development");
		
		return options;
	}
	
	/**
	 * Parse the command given.
	 * Will accept inputs in any order or style (i.e. -h or --help).
	 * Throws to itself any errors which are reported back to the user in a helpful way.
	 * 
	 * @param options The Command Line Options already set
	 * @param args The String[] of arguments from Launcher's main method.
	 */
	private void parseCommands(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			
			//if the user wishes to convert an adjacency List
			if(cmd.hasOption("c")) { 
				if(cmd.hasOption("n") && cmd.hasOption("e") && cmd.hasOption("d")) {
					String cVal = cmd.getOptionValue("c");
					String eVal = cmd.getOptionValue("e");
					String dVal = cmd.getOptionValue("d");
					String nVal = cmd.getOptionValue("n");
					
					//ensure that the user has specified the arguments for these
					if (cVal != null && eVal != null && dVal != null && nVal != null) {
						
						//ensure the node and egde inputs are valid
						int nodes, edges;
						try {
							nodes = Util.checkForPositiveInteger(nVal);
							edges = Util.checkForPositiveInteger(eVal);
						} catch (NumberFormatException e) {
							throw new ParseException("The values for n & e must be positive (or 0) integers");
						}						
						
						//ensure the directed input is valid
						boolean directed;
						if(dVal.equals("true") || dVal.equals("True")) {
							directed = true;
						} else if(dVal.equals("false") || dVal.equals("False")) {
							directed = false;
						} else {
							throw new ParseException("Directed input (d) must be true, True, false, False");
						}
						
						//ensure the file is valid and readable
						File f = new File(cVal);
						if(f.isFile() && f.canRead()) {
							String name = f.getName();
							String path = f.getParent();							
							
							System.out.println("Converting graph. This may take some time....");
							launcher.convertGraphToBuffers(nodes, edges, path, name, directed);
							System.out.println("Conversion Complete");
							
						} else {
							throw new ParseException("File does not exist, or is not readable");
						}						
					} else {
						throw new ParseException("Converting requires the c, e, d, n all have arguments. See --help for details.");
					}					
				} else {
					throw new ParseException("Converting requires e, d, n. See --help for details.");
				}		
				
				//if the user is testing the command line
			} else if(cmd.hasOption("t")) {
				//FastGraph.main(args);
			    try {
			    	NamePicker np = new NamePicker();
			    	System.out.println(Arrays.toString(np.getNames(10)));
			    } catch (Exception e) {
			    	System.err.println(e.getMessage());
			    	e.printStackTrace();
			    }
				
			    //if the user wants some help
			} else if (cmd.hasOption("h")) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar -Xmx14g dover.jar", options);
			}
		} catch (Exception e) {
			//ParseException
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
}
