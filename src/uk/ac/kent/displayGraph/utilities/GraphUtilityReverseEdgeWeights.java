package uk.ac.kent.displayGraph.utilities;

import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;


import uk.ac.kent.displayGraph.*;
import uk.ac.kent.displayGraph.experiments.GraphExperimentEdgeLengthData;

/**
 * Make large edge weights smaller and smaller edge weights larger for all graphs
 * in the chosen directory. The values are reversed between the current max and min
 * weights in the graph.
 *
 * @author Peter Rodgers
 */

public class GraphUtilityReverseEdgeWeights extends GraphUtility {

	public static String GRAPH_EXTENSION = "graph";
	public static String REVERSED_EXTENSION = "reversed";

/** Trivial constructor. */
	public GraphUtilityReverseEdgeWeights() {
		super(KeyEvent.VK_J,"Reverse All Edge Weights",KeyEvent.VK_J);
	}


/** Trivial constructor. */
	public GraphUtilityReverseEdgeWeights(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphUtilityReverseEdgeWeights(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}

	public void apply() {

		String startFileName = System.getProperty("user.dir");
		File startFile = new File(startFileName);
		startFile = startFile.getParentFile();

		JFileChooser fc = new JFileChooser(startFile);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle("Choose a Directory to reverse the weights in all graphs");

		int returnVal = fc.showOpenDialog(getGraphPanel().getContainerFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedDir = fc.getSelectedFile();

			if (!selectedDir.isDirectory()) {
				selectedDir = selectedDir.getParentFile();
			}

			reverseEdgeWeightsInDirectory(selectedDir);
		}
	}

	public void reverseEdgeWeightsInDirectory(File directory) {
		ArrayList<File> graphFiles = GraphExperimentEdgeLengthData.getGraphsInDirectory(directory,GRAPH_EXTENSION);

		for(File file : graphFiles) {
			Graph g = getGraphPanel().getGraph();

System.out.println("loading "+file);
			g.loadAll(file);
			reverseEdgeWeights(g);

			File reversedFile = new File(file.getAbsolutePath()+"."+REVERSED_EXTENSION);
			g.saveAll(reversedFile);
//			g.saveAll(file);
System.out.println("DONE REVERSING");
		}
	}

/**
 * Makes small edge weights large, and large edge weights small
 * in between the max and min weights present in the graph.
 */
	public static void reverseEdgeWeights(Graph g) {

		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		
		for(Edge e : g.getEdges()) {
			if(e.getWeight() > max) {
				max = e.getWeight();
			}
			if(e.getWeight() < min) {
				min = e.getWeight();
			}
		}

		for(Edge e : g.getEdges()) {
			double weight = e.getWeight();
			double newWeight = max+min-weight;
			e.setWeight(newWeight);
			e.setLabel(Double.toString(newWeight));
		}
	}

}


