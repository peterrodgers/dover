package uk.ac.kent.dover.fastGraph;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;

/**
 * Algorithm to implement the KMedoids
 * 
 * Modified from https://sourceforge.net/p/java-ml/java-ml-code/ci/a25ddde7c3677da44e47a643f88e32e2c8bbc32f/tree/net/sf/javaml/clustering/KMedoids.java#l40
 * 
 * @author Rob Baker
 *
 */
public class KMedoids {

	/* Number of clusters to generate */
	private int numberOfClusters;

	/* Random generator for selection of candidate medoids */
	private Random r;

	/* The maximum number of iterations the algorithm is allowed to run. */
	private int maxIterations;
	
	private FastGraph targetGraph;
	
	public int numberOfGedCalcs = 0;
	public long gedTime = 0;
	
	/**
	 * Constructor
	 * 
	 * @param targetGraph The target graph
	 * @param numberOfClusters The number of clusters
	 * @param maxIterations The maximum number of iterations
	 */
	public KMedoids(FastGraph targetGraph, int numberOfClusters, int maxIterations) {
		this.numberOfClusters = numberOfClusters;
		this.maxIterations = maxIterations;
		this.targetGraph = targetGraph;
		r = new Random(targetGraph.getNodeBuf().getLong(1));
	}
	
	/**
	 * Clusters the subgraphs
	 * 
	 * @param subgraphs The subgraphs to cluster
	 * @return The clusters, as a list of lists of FastGraphs
	 * @throws FastGraphException If the random selection cannot be obtained (i.e. k is too big)
	 */
	public ArrayList<ArrayList<FastGraph>> cluster(ArrayList<FastGraph> subgraphs) throws FastGraphException {
		
		ArrayList<FastGraph> medoids = Util.randomSelection(r, numberOfClusters, subgraphs);	
		ArrayList<ArrayList<FastGraph>> output = new ArrayList<ArrayList<FastGraph>>(numberOfClusters);
		
		Debugger.log("Map created. Not included in time");
		boolean changed = true;
		int count = 0;
		long time = Debugger.createTime();
		while (changed && count < maxIterations) {
			changed = false;
			count++;
			int[] assignment = assign(medoids, subgraphs);
			Debugger.outputTime("assignment complete. Count: "+count, time);
			changed = recalculateMedoids(assignment, medoids, output, subgraphs);
			Debugger.outputTime("recalculateMedoids complete.", time);
		}

		return output;
	}
	
	/**
	 * Assign all instances from the data set to the medoids.
	 * 
	 * @param medoids candidate medoids
	 * @param subgraphs the data to assign to the medoids
	 * @return best cluster indices for each instance in the data set
	 */
	private int[] assign(ArrayList<FastGraph> medoids, ArrayList<FastGraph> subgraphs) {
		
		int[] out = new int[subgraphs.size()];
		for (int i = 0; i < subgraphs.size(); i++) {

			double bestDistance = comparisonScore(subgraphs.get(i), medoids.get(0));

			int bestIndex = 0;
			for (int j = 1; j < medoids.size(); j++) {
				double tmpDistance = comparisonScore(subgraphs.get(i), medoids.get(j));
				if (tmpDistance < bestDistance) {
					bestDistance = tmpDistance;
					bestIndex = j;
				}
			}
			out[i] = bestIndex;

		}
		return out;
	}

	/**
	 * Return a array with on each position the clusterIndex to which the
	 * Instance on that position in the dataset belongs.
	 * 
	 * @param medoids the current set of cluster medoids, will be modified to fit the new assignment
	 * @param assigment the new assignment of all instances to the different medoids
	 * @param output the cluster output, this will be modified at the end of the method
	 * @return If any of the medoids have changed
	 */
	private boolean recalculateMedoids(int[] assignment, ArrayList<FastGraph> medoids, ArrayList<ArrayList<FastGraph>> output,
			ArrayList<FastGraph> subgraphs) {
		
		boolean changed = false;
		
		for (int i = 0; i < numberOfClusters; i++) {
			if(output.size() > i) {
				output.set(i, new ArrayList<FastGraph>());
			} else {
				output.add(new ArrayList<FastGraph>());
			}			
			
			for (int j = 0; j < assignment.length; j++) {
				if (assignment[j] == i) {
					output.get(i).add(subgraphs.get(j));
				}
			}
			
			if (output.get(i).size() == 0) { // new random, empty medoid
				medoids.set(i,subgraphs.get(r.nextInt(subgraphs.size())));
				changed = true;
			} else {
				FastGraph centroid = findAverageGraph(output.get(i));
				FastGraph oldMedoid = medoids.get(i);
				medoids.set(i, findClosestGraph(centroid, subgraphs));
				if (!medoids.get(i).equals(oldMedoid)) {
					changed = true;
				}
			}
		}
		return changed;
	}
	
	/**
	 * Finds the graph closest to the "average" of the given cluster
	 * 
	 * @param cluster The cluster
	 * @return The graph closest to the "average"
	 */
	private FastGraph findAverageGraph(ArrayList<FastGraph> cluster) {
		FastGraph averageGraph = null;
		float bestScore = Float.POSITIVE_INFINITY;
		
		for(FastGraph g : cluster) {
			float currentScore = 0;
			
			for(FastGraph h : cluster) {
				
				if(g == h) { //skip if the same
					continue;
				}
				
				currentScore += comparisonScore(g, h);
				
			}
			
			if(currentScore < bestScore) {
				bestScore = currentScore;
				averageGraph = g;
			}
		}
		
		return averageGraph;
	}
	
	/**
	 * Finds the graph closest to the given centroid
	 * 
	 * @param centroid The centroid
	 * @param subgraphs The entire list of subgraphs
	 * @return The graph closest to the centroid
	 */
	private FastGraph findClosestGraph(FastGraph centroid, ArrayList<FastGraph> subgraphs) {
		FastGraph closestGraph = null;
		double bestScore = Double.POSITIVE_INFINITY;
		
		for(FastGraph g : subgraphs) {
			double currentScore = comparisonScore(g, centroid);
			
			if(currentScore < bestScore) {
				bestScore = currentScore;
				closestGraph = g;
			}
		}
		
		return closestGraph;
	}
	
	/**
	 * Returns the comparison score of the two graphs. Normally, GED
	 * @param g1 The first graph
	 * @param g2 The second graph
	 * @return The comparison score
	 */
	public double comparisonScore(FastGraph g1, FastGraph g2) {
//		return (g1.getNumberOfNodes() + g1.getNumberOfEdges()) - (g2.getNumberOfNodes() + g2.getNumberOfEdges()); //placeholder
		numberOfGedCalcs++;
		long time = Debugger.createTime();

		//double result = GedUtil.getGedScore(map.get(g1), map.get(g2));
		double result = findDifferenceInDegreeProfiles(g1,g2);
		
		long diff = Debugger.createTime() - time;
		gedTime += diff;
		return result;
	}
	
	/**
	 * Finds the difference in degree profile between two graphs
	 * @param g1 The first graph
	 * @param g2 The second graph
	 * @return The difference (double to maintain consistency with other methods)
	 */
	private double findDifferenceInDegreeProfiles(FastGraph g1, FastGraph g2) {
		int[] buckets1 = populateDegreeBuckets(g1);
		int[] buckets2 = populateDegreeBuckets(g2);

		double total = 0;
		for(int i = 0; i < Math.max(buckets1.length, buckets2.length); i++) {
			if(i >= buckets1.length) {
				total += buckets2[i];
			} else if(i >= buckets2.length) {
				total += buckets1[i];
			} else {
				total += Math.abs(buckets1[i] - buckets2[i]);
			}		
		}		
		return total;
	}
	
	/**
	 * Populates degree buckets with the profiles of each degree
	 * 
	 * @param g1 The graph to run on
	 * @return The buckets
	 */
	private int[] populateDegreeBuckets(FastGraph g1) {
		int maxDegree1 = g1.maximumDegree();
		int[] degreeBuckets1 = new int[maxDegree1+1];
		int[] degrees1 = g1.findDegrees();
		g1.findDegreeBuckets(degreeBuckets1,degrees1);
		return degreeBuckets1;
	}

	/**
	 * Save clusters to disk
	 * @param clusters The clusters to save
	 * @throws FileNotFoundException If the files cannot be saved
	 */
	public void saveClusters(ArrayList<ArrayList<FastGraph>> clusters) throws FileNotFoundException {
		File mainDir = new File(
				Launcher.startingWorkingDirectory+File.separatorChar+"kmedoids_results"+
				File.separatorChar+targetGraph.getName()+"_"+Util.dateAsString()
			);
		mainDir.mkdirs();
		for(int i = 0; i < clusters.size(); i++) {
			File innerDir = new File(mainDir.getAbsolutePath()+File.separatorChar+i);
			innerDir.mkdirs();
			ArrayList<FastGraph> cluster = clusters.get(i);
			
			for(int j = 0; j < cluster.size(); j++) {
				FastGraph g = cluster.get(j);
				
				File thisDir = new File(innerDir.getAbsolutePath()+File.separatorChar+j);
				thisDir.mkdirs();
				g.saveBuffers(innerDir.getAbsolutePath()+File.separatorChar+j, targetGraph.getName());
				
				//save SVG
				uk.ac.kent.displayGraph.Graph dg = g.generateDisplayGraph();
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
				saveLocation = new File(innerDir.getAbsolutePath()+File.separatorChar+j+File.separatorChar+"subgraph.svg");
				uk.ac.kent.displayGraph.ExportSVG exSVG = new uk.ac.kent.displayGraph.ExportSVG(dg);
				exSVG.saveGraph(saveLocation);
				
			}
		}
		buildHtmlOutput(clusters, mainDir);
	}
	
	/**
	 * Exports the results to a HTML file
	 * 
	 * @param clusters The clusters to save
	 * @param mainDir The parent directory
	 * @throws FileNotFoundException If the output file cannot be created
	 */
	private void buildHtmlOutput(ArrayList<ArrayList<FastGraph>> clusters, File mainDir) throws FileNotFoundException {
		Document doc = Document.createShell("");
		
		doc.head().appendElement("title").text(targetGraph.getName());

		Element headline = doc.body().appendElement("h1").text(targetGraph.getName());
		
		Element pageNumberHeader = doc.body().appendElement("h2").text("kMedoids");
		
		Element table = doc.body().appendElement("table").attr("style", "border: 2px solid; border-collapse: collapse; width: 100%");
		Element headerRow = table.appendElement("tr").attr("style", "border: 2px solid;");
		headerRow.appendElement("th").text("Cluster Num").attr("style", "border: 1px solid;");
		headerRow.appendElement("th").text("Graphs").attr("style", "border: 1px solid;");
		
		for(int i = 0; i < clusters.size(); i++) {
			Element row = table.appendElement("tr").attr("style", "border: 2px solid;");
			row.appendElement("td").text(i+"").attr("style", "border: 1px solid;");
			Element cell = row.appendElement("td").attr("style", "border: 1px solid;");
			for(int j = 0; j < clusters.get(i).size(); j++) {
				cell.appendElement("a").text(j+"").attr("href",i+"/"+j+"/subgraph.svg");
				cell.appendText(" ");
			}
		}
		
		File output = new File(mainDir.getAbsolutePath()+File.separatorChar+"index.html");
		//save the output html file
		
		try(PrintWriter out = new PrintWriter( output )){ //will close file after use
		    out.println( doc.toString() );
		}
	}
}
