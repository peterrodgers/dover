package uk.ac.kent.dover.fastGraph;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import uk.ac.kent.dover.fastGraph.Gui.MotifTaskDummy;

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
	 * @param launcher The Launcher for callbacks
	 * @param args The arguments passed to the system through the command line
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
		options.addOption("c", "convert", true, "Convert an Adjacency List to buffers. Requires e,d,n. (Convert)");
		options.addOption("n", "nodes", true, "Number of nodes. Either in Adjacency List to convert, or in enumerated subgraphs for approximate sunbgrpah isomorphism  (ApproxSubgraph, Convert)");
		options.addOption("e", "edges", true, "Number of edges in Adjacency List to convert  (Convert)");
		options.addOption("d", "directed", true, "Adjacency List to convert is a directed graph  (Convert)");
		
		//add exact motif options
		options.addOption("m","exactmotif", true, "Find exact motifs in this graph. Requires minSize and maxSize. (ExactMotif)");
		options.addOption(Option.builder().longOpt("minsize").desc("The minimum size of motif to find  (ExactMotif, ApproxMotif)").hasArg().build());
		options.addOption(Option.builder().longOpt("maxsize").desc("The maximum size of motif to find  (ExactMotif, ApproxMotif)").hasArg().build());
		options.addOption(Option.builder().longOpt("saveall").desc("Saves every example of motifs. This may take some time.  (ExactMotif)").build());

		//add approximate motif options
		options.addOption("M","approxmotif", true, "Find approximate motifs in this graph. "
				+ "Requires minSize, maxSize, clusters, iterations, subspernode and optionally attempts. (ApproxMotif)");
		options.addOption(Option.builder().longOpt("clusters").desc("The number of clusters (ApproxMotif)").hasArg().build());
		options.addOption(Option.builder().longOpt("iterations").desc("The number of iterations (ApproxMotif)").hasArg().build());
		options.addOption(Option.builder().longOpt("subspernode").desc("The number of subgraphs per node (ApproxMotif, ApproxSubgraph)").hasArg().build());
		options.addOption(Option.builder().longOpt("attempts").desc("The number of attempts to find a subgraph. Default is " 
				+ String.valueOf(Launcher.DEFAULT_SUBGRAPH_ENUMERATION_ATTEMPTS) + " (ApproxMotif)").hasArg().build());
		//and minsize & maxSize
		
		//add the exact subgraph options
		options.addOption("s","exactsubgraph", true, "Find exact subgraphs in this graph. Requires t or targetgraph. (ExactSubgraph)");
		options.addOption("p","patterngraph", true, "Specifies the pattern graph to use (ExactSubgraph, ApproxSubgraph)");
		
		//add approx subgraph options
		options.addOption("S","approxsubgraph", true, "Find approx subgraphs in this graph. Requires t or targetgraph, n or nodes, subspernode and p or patterngraph. (ApproxSubgraph)");
		//and n, subspernode, and patterngraph
		
		// add help option
		options.addOption("h", "help", false, "Prints this message (Help)");
		
		// add t option
		options.addOption("q", false, "Runs the FastGraph as it used to in the early stages of development (Debug)");
		
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
				convert(cmd);
				
				//if the user is finding exact motifs
			} else if(cmd.hasOption("m")){
				motif(cmd);	
				
				//if the user is finding approx motifs
			} else if(cmd.hasOption("M")) {
				approxMotif(cmd);
				
				//if the user is finding exact subgraphs
			} else if(cmd.hasOption("s")) {
				exactSubgraph(cmd);
				
				//if the user is finding exact subgraphs
			} else if(cmd.hasOption("S")) {
				approxSubgraph(cmd);
				
			    //if the user wants some help
			} else if (cmd.hasOption("h")) {
				// automatically generate the help statement
				System.out.println("More data is available at: " + launcher.DATA_URL);
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar -Xmx14g dover.jar", options);
			}
		} catch (Exception e) {
			//ParseException
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Runs the approx subgraph finding code and checks that parameters are valid
	 * @param cmd The CommandLine object that holds the user's input
	 * @throws ParseException If there is an error with the user's input
	 */
	private void approxSubgraph(CommandLine cmd) throws ParseException {
		if (cmd.hasOption("p") && cmd.hasOption("n")) {
			Option subsPerNodeFound = null;
			for(Option o : cmd.getOptions()) {
				if (o.getLongOpt().toLowerCase().equals("subspernode")) {
					subsPerNodeFound = o;
				}
			}
			
			if(subsPerNodeFound != null) {
				String sVal = cmd.getOptionValue("S");
				String pVal = cmd.getOptionValue("p");
				String nVal = cmd.getOptionValue("n");
				String subsPerNodeVal = subsPerNodeFound.getValues()[0];
				
				if(!Util.areAnyObjectsNull(sVal, pVal, nVal, subsPerNodeVal)) {
					//ensure the node and edge inputs are valid
					int subsPerNode, nodes;
					try {
						subsPerNode = Util.checkForPositiveInteger(subsPerNodeVal);
						nodes = Util.checkForPositiveInteger(nVal);
					} catch (NumberFormatException e) {
						throw new ParseException("The values for n & subspernode must be positive (or 0) integers");
					}
					
					//ensure the file is valid and readable
					File target = new File(sVal);
					File pattern = new File(pVal);
					if(target.canRead()) { //ensure this file can be read
						
						if (pattern.canRead()) { //ensure this file can be read
							
							String targetName = target.getName();
							String targetPath = target.getParent();	
							String patternName = pattern.getName();
							String patternPath = pattern.getParent();	
							System.out.println("Finding subgraphs. This may take some time....");
							try {
								FastGraph targetGraph = launcher.loadFromBuffers(targetPath+File.separatorChar+targetName, targetName);
								FastGraph patternGraph = launcher.loadFromBuffers(patternPath+File.separatorChar+patternName, patternName);
								
								launcher.approximateSubgraphs(targetGraph, patternGraph, nodes, subsPerNode);
								
							} catch (IOException e) {
								throw new ParseException("Error occurred: "+e.getMessage());
							}
							System.out.println("Finding subgraphs Complete. Output has been exported");
							
							
						} else {
							throw new ParseException("Pattern File does not exist, or is not readable");
						}					
						
					} else {
						throw new ParseException("Target File does not exist, or is not readable");
					}
					
				} else {
					throw new ParseException("Approximate subgraph isomorphism requires that n, p, s, t, subspernode all have arguments. See --help for details.");
				}				
			} else {
				throw new ParseException("Approximate subgraph isomorphism requires t or targetgraph, n or nodes, subspernode and p or patterngraph. See --help for details.");
			}			
		} else {
			throw new ParseException("Approximate subgraph isomorphism requires t or targetgraph, n or nodes, subspernode and p or patterngraph. See --help for details.");
		}

	}
	
	/**
	 * Runs the exact subgraph finding code and checks that parameters are valid
	 * @param cmd The CommandLine object that holds the user's input
	 * @throws ParseException If there is an error with the user's input
	 */
	private void exactSubgraph(CommandLine cmd) throws ParseException {
		
		if(cmd.hasOption("p")) {
			String pVal = cmd.getOptionValue("p");
			String sVal = cmd.getOptionValue("s");
			
			//if both p and s have arguments
			if(!Util.areAnyObjectsNull(pVal, sVal)) {
				
				//ensure the file is valid and readable
				File target = new File(sVal);
				File pattern = new File(pVal);
				if(target.canRead()) { //ensure this file can be read
					
					if (pattern.canRead()) { //ensure this file can be read
						
						String targetName = target.getName();
						String targetPath = target.getParent();	
						String patternName = pattern.getName();
						String patternPath = pattern.getParent();	
						System.out.println("Finding subgraphs. This may take some time....");
						try {
							FastGraph targetGraph = launcher.loadFromBuffers(targetPath+File.separatorChar+targetName, targetName);
							FastGraph patternGraph = launcher.loadFromBuffers(patternPath+File.separatorChar+patternName, patternName);
							
							launcher.exactSubgraphs(targetGraph, patternGraph);
							
						} catch (IOException e) {
							throw new ParseException("Error occurred: "+e.getMessage());
						}
						System.out.println("Finding subgraphs Complete. Output has been exported");
						
						
					} else {
						throw new ParseException("Pattern File does not exist, or is not readable");
					}					
					
				} else {
					throw new ParseException("Target File does not exist, or is not readable");
				}
				
				
			} else {
				throw new ParseException("Exact subgraph isomorphism requires that t and s (or targetgraph and exactsubgraph) both have arguments. See --help for details.");
			}
			
		} else {
			throw new ParseException("Exact subgraph isomorphism requires t or targetgraph. See --help for details.");
		}
		
	}
	
	/**
	 * Runs the exact motif finding code and checks that parameters are valid
	 * @param cmd The CommandLine object that holds the user's input
	 * @throws ParseException If there is an error with the user's input
	 */
	private void motif(CommandLine cmd) throws ParseException {
		Option minFound = null;
		Option maxFound = null;
		Option saveAllFound = null;
		for(Option o : cmd.getOptions()) {
			if (o.getLongOpt().toLowerCase().equals("minsize")) {
				minFound = o;
			}
			if (o.getLongOpt().toLowerCase().equals("maxsize")) {
				maxFound = o;
			}
			if (o.getLongOpt().toLowerCase().equals("saveall")) {
				saveAllFound = o;
			}
		}
		
		if(minFound!=null && maxFound!=null) {
			String mVal = cmd.getOptionValue("m");
			String minSizeVal = minFound.getValues()[0];
			String maxSizeVal = maxFound.getValues()[0];
			
			boolean saveAll = saveAllFound != null; //if saveAll wasn't passed, then false.
			
			//ensure that the user has specified the arguments for these
			if (mVal != null && minSizeVal != null && maxSizeVal != null) {
				//ensure the node and edge inputs are valid
				int minSize, maxSize;
				try {
					minSize = Util.checkForPositiveInteger(minSizeVal);
					maxSize = Util.checkForPositiveInteger(maxSizeVal);
				} catch (NumberFormatException e) {
					throw new ParseException("The values for minSize & maxSize must be positive (or 0) integers");
				}
				//ensure the file is valid and readable
				File f = new File(mVal);
				if(f.canRead()) {
					String name = f.getName();
					String path = f.getParent();							
					System.out.println("Finding motifs. This may take some time....");
					try {
						launcher.findMotifs(new MotifTaskDummy(), path+File.separatorChar+name, name, minSize, maxSize, saveAll, null);
					} catch (IOException e) {
						throw new ParseException("Error occurred: "+e.getMessage());
					}
					System.out.println("Motif finding Complete. Output has been exported");
					
				} else {
					throw new ParseException("File does not exist, or is not readable");
				}
				
			} else {
				throw new ParseException("Motif finding requires the minSize, maxSize all have arguments. See --help for details.");
			}
			
		} else {
			throw new ParseException("Motif finding requires minSize, maxSize. See --help for details.");
		}
	}
	
	/**
	 * Runs the approximate motif finding code and checks that parameters are valid
	 * @param cmd The CommandLine object that holds the user's input
	 * @throws ParseException If there is an error with the user's input
	 */
	private void approxMotif(CommandLine cmd) throws ParseException {
		
		Option minFound = null;
		Option maxFound = null;
		Option clustersFound = null;
		Option iterationsFound = null;
		Option subspernodeFound = null;
		Option attemptsFound = null;
		for(Option o : cmd.getOptions()) {
			
			switch(o.getLongOpt().toLowerCase()) {
				case "minsize" :
					minFound = o;
					break;
				case "maxsize" :
					maxFound = o;
					break;
				case "clusters" :
					clustersFound = o;
					break;
				case "iterations" :
					iterationsFound = o;
					break;
				case "subspernode" :
					subspernodeFound = o;
					break;
				case "attempts" :
					attemptsFound = o;
					break;			
			}
		}
		
		//if all of the following are NOT NULL (ignore attempts, as that is optional)
		if(!Util.areAnyObjectsNull(minFound, maxFound, clustersFound, iterationsFound, subspernodeFound)) {
			String mVal = cmd.getOptionValue("M");
			String minSizeVal = minFound.getValues()[0];
			String maxSizeVal = maxFound.getValues()[0];
			String clustersVal = clustersFound.getValues()[0];
			String iterationsVal = iterationsFound.getValues()[0];
			String subspernodeVal = subspernodeFound.getValues()[0];
			String attemptsVal = String.valueOf(Launcher.DEFAULT_SUBGRAPH_ENUMERATION_ATTEMPTS);
			if(attemptsFound != null) { //only update the value if there was one
				attemptsVal = attemptsFound.getValues()[0];
			} 
			
			if(!Util.areAnyObjectsNull(mVal, minSizeVal, maxSizeVal, clustersVal, iterationsVal, subspernodeVal, attemptsVal)) {
				//all parameters have options given
				
				//ensure the inputs are valid numbers
				int minSize, maxSize, clusters, iterations, subsPerNode, attempts;
				try {
					minSize = Util.checkForPositiveInteger(minSizeVal);
					maxSize = Util.checkForPositiveInteger(maxSizeVal);
					clusters = Util.checkForPositiveInteger(clustersVal);
					iterations = Util.checkForPositiveInteger(iterationsVal);
					subsPerNode = Util.checkForPositiveInteger(subspernodeVal);
					attempts = Util.checkForPositiveInteger(attemptsVal);
				} catch (NumberFormatException e) {
					throw new ParseException("The values for minSize & maxSize must be positive (or 0) integers");
				}
				
				//ensure the file is valid and readable
				File f = new File(mVal);
				if(f.canRead()) {
					String name = f.getName();
					String path = f.getParent();							
					System.out.println("Finding motifs. This may take some time....");
					try {
						FastGraph g = launcher.loadFromBuffers(path+File.separatorChar+name, name);

						launcher.approximateMotifs(g, minSize, maxSize, clusters, iterations, subsPerNode, attempts);
					} catch (IOException e) {
						throw new ParseException("Error occurred: "+e.getMessage());
					} catch (FastGraphException e) {
						throw new ParseException("Error occurred: "+e.getMessage());
					}
					System.out.println("Motif finding Complete. Output has been exported");
					
				} else {
					throw new ParseException("File does not exist, or is not readable");
				}				
				
			} else {
				throw new ParseException("Approximate Motif finding requires that minSize, maxSize, clusters, iterations, subspernode all have arguments. See --help for details.");
			}
		} else {
			throw new ParseException("Approximate Motif finding requires minSize, maxSize, clusters, iterations, subspernode and optionally attempts. See --help for details.");
		}
		
	}
	
	/**
	 * Converts a graph
	 * @param cmd The CommandLine function
	 * @throws Exception If the graph cannot be loaded, or incorrect parameters
	 */
	private void convert(CommandLine cmd) throws Exception {
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
	}
	
}
