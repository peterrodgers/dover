package uk.ac.kent.displayGraph.experiments;

import java.util.*;
import java.io.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import uk.ac.kent.displayGraph.*;
import uk.ac.kent.displayGraph.drawers.*;
import uk.ac.kent.displayGraph.utilities.*;

/**
 * Attempts to find the best values for k and r when metric spring embedding the current graph.
 *
 * @author Peter Rodgers
 */

public class GraphExperimentEdgeLengthDataAfterSE extends GraphExperiment {

	public static String GRAPH_EXTENSION = "se";

	public static Point TOPLEFT = new Point(50,50);
	public static int WIDTH = 400;
	public static int HEIGHT = 400;

//	public static double K = 0.0005;
	public static double K = 0.001;
//	public static double K = 0.000001;
//	public static double R = 15.0;
//	public static double R = 1.0;
	public static double R = 50.0;
	public static double F = 0.01;
	public static double IDEALLENGTH = 300.0;
//	public static double IDEALLENGTH = 1000.0;
	public static int ITERATIONS = 1004;

	StringBuffer log = new StringBuffer("");


/** Trivial constructor. */
	public GraphExperimentEdgeLengthDataAfterSE() {
		super(KeyEvent.VK_W,"Generate Edge Length Data",KeyEvent.VK_W);
	}

/** Trivial constructor. */
	public GraphExperimentEdgeLengthDataAfterSE(int key, String s, int accelerator) {
		super(key,s,accelerator);
	}


/** Starts the experiment. */
	public void experiment() {

		String startFileName = System.getProperty("user.dir");
		File startFile = new File(startFileName);
		startFile = startFile.getParentFile();

		JFileChooser fc = new JFileChooser(startFile);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setDialogTitle("Choose a Directory to put the experiments");

		int returnVal = fc.showOpenDialog(getGraphPanel().getContainerFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedDir = fc.getSelectedFile();

			if (!selectedDir.isDirectory()) {
				selectedDir = selectedDir.getParentFile();
			}

			runTests(selectedDir);
		}
	}

	public void runTests(File directory) {

		GraphDrawerEdgeLength gdel = new GraphDrawerEdgeLength(KeyEvent.VK_S,"Spring Embedder - edge length version",KeyEvent.VK_S,false,true);
		getGraphPanel().addGraphDrawer(gdel);

		gdel.setScaleEdgeLength(true);
		gdel.setScaleAttractive(true);
		gdel.setScaleWeightsToUnitary(true);
		gdel.setAnimateFlag(false);
		gdel.setTimerFlag(false);
		gdel.setDialogFlag(false);

		gdel.setK(K);
		gdel.setR(R);
		gdel.setF(F);
		gdel.setIdealLength(IDEALLENGTH);
		gdel.setIterations(ITERATIONS);

		ArrayList<File> graphFiles = getGraphsInDirectory(directory);

		Date date = new Date();
   		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); 
   		String s = formatter.format(date);
		addToLog("START GraphExperimentEdgeLengthDataOnlyAfterSE. Time: "+s+"\n");
		addToLog("Constants: ITERATIONS\t"+ITERATIONS+"\tK\t"+K+"\tR\t"+R+"\tF\t"+F+"\tIDEALLENGTH\t"+IDEALLENGTH+"\n");
		addToLog("file\tnodes\tedges\tedge length\tnearest node separation\n");

		for(File file : graphFiles) {
			String fileName = file.getAbsolutePath();
			Graph g = getGraphPanel().getGraph();

			g.loadAll(file);

			addToLog(fileName+"\t"+g.getNodes().size()+"\t"+g.getEdges().size());

			gdel.setIncludeEdgeLength(true);
			gdel.setIncludeSpringEmbedder(false);
			getGraphPanel().update(getGraphPanel().getGraphics());
			gdel.drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());

			String fileSEName = new String(fileName+".plusel");
			File fileSE = new File(fileSEName);
			g.saveAll(fileSE);

			double lengthStats = GraphUtilityDistanceStats.outputEdgeLengthDifferences(g,false);
			double separationStats = GraphUtilityDistanceStats.outputNearestNodeSeparationDifferences(g,false);
			addToLog("\t"+lengthStats+"\t"+separationStats+"\n");

		}

		writeLog(directory+File.separator+"log.sethenel.txt");

		System.out.println("FINISHED GraphExperimentEdgeLengthData");

	}


	public void addToLog(String s) {
		System.out.println(s);
		log.append(s);
	}


	public boolean writeLog(String fileName) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(fileName));
			b.write(log.toString());
			b.close();
		} catch(IOException e){
			System.out.println("An IO exception occured when executing writeLog("+fileName+") in GraphExperimentEdgeLength.java: "+e+"\n");
			return false;
		}

		return true;
	}


/** Returns a list of files with a .graph extension. */
	public ArrayList<File> getGraphsInDirectory(File directory) {

		ArrayList<File> ret = new ArrayList<File>();

		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] fileArray = fsv.getFiles(directory, false);
		for (int i = 0; i < fileArray.length; i++) {

			File f = fileArray[i];
			String extension = getFileExtension(f);

			if(extension.equals(GRAPH_EXTENSION)) {
				ret.add(f);
			}
		}


		return ret;
	}


	public String getFileExtension(File f) {
		String fileString = f.getName();
		int index = fileString.lastIndexOf(".");
		return fileString.substring(index+1);
	}

}



