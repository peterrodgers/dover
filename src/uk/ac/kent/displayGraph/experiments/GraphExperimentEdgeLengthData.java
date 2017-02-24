package uk.ac.kent.displayGraph.experiments;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.drawers.GraphDrawerEdgeLength;
import uk.ac.kent.displayGraph.utilities.GraphUtilityDistanceStats;

/**
 *
 * @author Peter Rodgers
 */

public class GraphExperimentEdgeLengthData extends GraphExperiment {

	public static String GRAPH_EXTENSION = "graph";

	public static Point TOPLEFT = new Point(50,50);
	public static int WIDTH = 400;
	public static int HEIGHT = 400;

	public double K = 5.0E-5;
	public double R = 10.0;
	public double F = 1.0;
	public double IDEALLENGTH = 300.0;
	public int ITERATIONS = 1004;
	public int EXPERIMENTS = 1;


	public static String DEFAULTSETTINGFILENAME = "settings.txt";

	StringBuffer log = new StringBuffer("");


/** Trivial constructor. */
	public GraphExperimentEdgeLengthData() {
		super(KeyEvent.VK_V,"Generate Edge Length Data",KeyEvent.VK_V);
	}

/** Trivial constructor. */
	public GraphExperimentEdgeLengthData(int key, String s, int accelerator) {
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

			loadSettings(new File(selectedDir+File.separator+DEFAULTSETTINGFILENAME));

			addToLog("file\tnodes\tedges\texperiment\tSE distortion\tSE5EL1 distortion\tSE1EL1 distortion\tEL distortion\tTIME\tITERATIONS\tK\tR\tF\tIDEALLENGTH\n");

			for(int i=1; i<=EXPERIMENTS;i++) {
				runTests(selectedDir,i);
			}

			writeLog(selectedDir+File.separator+"log.txt");

			System.out.println("FINISHED GraphExperimentEdgeLengthData");


		}
	}

	public void runTests(File directory, int experiment) {

		String experimentString = Integer.toString(experiment);
		if(experiment < 10) {
			experimentString = "0"+experimentString;
		}

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

		ArrayList<File> graphFiles = getGraphsInDirectory(directory,GRAPH_EXTENSION);

		Date date = new Date();
   		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); 
   		String timeString = formatter.format(date);

		for(File file : graphFiles) {
			String fileName = file.getAbsolutePath();
			Graph g = getGraphPanel().getGraph();

			g.loadAll(file);

			addToLog(fileName+"\t"+g.getNodes().size()+"\t"+g.getEdges().size()+"\t"+experimentString);

// first experiment, metric spring embedder only
			g.randomizeNodePoints(TOPLEFT,WIDTH,HEIGHT);
			gdel.setIncludeEdgeLength(false);
			gdel.setIncludeSpringEmbedder(true);
			getGraphPanel().update(getGraphPanel().getGraphics());
			gdel.drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());

			String fileSEName = new String(fileName+"."+experimentString+".se");
			File fileSE = new File(fileSEName);
			g.saveAll(fileSE);

			double SELengthStats = GraphUtilityDistanceStats.outputEdgeLengthDifferences(g,false);
			addToLog("\t"+SELengthStats);


// second experiment, metric spring embedder and edge length, 5-1 ratio
			g.randomizeNodePoints(TOPLEFT,WIDTH,HEIGHT);
			gdel.setIncludeEdgeLength(true);
			gdel.setIncludeSpringEmbedder(true);
			gdel.setEdgeLengthFrequency(5);
			getGraphPanel().update(getGraphPanel().getGraphics());
			gdel.drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());

			String fileSE5EL1Name = new String(fileName+"."+experimentString+".se5el1");
			File fileSE5EL1 = new File(fileSE5EL1Name);
			g.saveAll(fileSE5EL1);

			double SE5EL1LengthStats = GraphUtilityDistanceStats.outputEdgeLengthDifferences(g,false);
			addToLog("\t"+SE5EL1LengthStats);


// third experiment, metric spring embedder and edge length, 1-1 ratio
			g.randomizeNodePoints(TOPLEFT,WIDTH,HEIGHT);
			gdel.setIncludeEdgeLength(true);
			gdel.setIncludeSpringEmbedder(true);
			gdel.setEdgeLengthFrequency(1);
			getGraphPanel().update(getGraphPanel().getGraphics());
			gdel.drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());

			String fileSE1EL1Name = new String(fileName+"."+experimentString+".se1el1");
			File fileSE1EL1 = new File(fileSE1EL1Name);
			g.saveAll(fileSE1EL1);

			double SE1EL1LengthStats = GraphUtilityDistanceStats.outputEdgeLengthDifferences(g,false);
			addToLog("\t"+SE1EL1LengthStats);


// fourth experiment, edge length only
			g.randomizeNodePoints(TOPLEFT,WIDTH,HEIGHT);
			gdel.setIncludeEdgeLength(true);
			gdel.setIncludeSpringEmbedder(false);
			gdel.setEdgeLengthFrequency(1);
			getGraphPanel().update(getGraphPanel().getGraphics());
			gdel.drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());

			String fileELName = new String(fileName+"."+experimentString+".el");
			File fileEL = new File(fileELName);
			g.saveAll(fileEL);

			double ELLengthStats = GraphUtilityDistanceStats.outputEdgeLengthDifferences(g,false);
			addToLog("\t"+ELLengthStats);

			addToLog("\t"+timeString+"\t"+ITERATIONS+"\t"+K+"\t"+R+"\t"+F+"\t"+IDEALLENGTH+"\n");

		}

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
		} catch(IOException x){
			System.out.println("An IO exception occured when executing writeLog("+fileName+") in GraphExperimentEdgeLength.java: "+x+"\n");
			return false;
		}

		return true;
	}


/** Returns a list of files with a .graph extension. */
	public static ArrayList<File> getGraphsInDirectory(File directory, String extension) {

		ArrayList<File> ret = new ArrayList<File>();

		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] fileArray = fsv.getFiles(directory, false);
		for (int i = 0; i < fileArray.length; i++) {

			File f = fileArray[i];
			String currentExtension = getFileExtension(f);

			if(currentExtension.equals(extension)) {
				ret.add(f);
			}
		}


		return ret;
	}


	public static String getFileExtension(File f) {
		String fileString = f.getName();
		int index = fileString.lastIndexOf(".");
		return fileString.substring(index+1);
	}


	protected void loadSettings(File file) {
		InputStream inStream;
		try{
			inStream = new FileInputStream(file);
		}
		catch(FileNotFoundException x) {
System.out.println("Settings file not found: "+file);
			return;
		}

		try {
			int separatorInd;
			InputStreamReader isReader =new InputStreamReader(inStream);
			BufferedReader b = new BufferedReader(isReader);
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}

				StringBuffer parseLine = new StringBuffer(line);

				separatorInd = parseLine.indexOf(" ");
				String part = parseLine.substring(0,separatorInd);
				parseLine.delete(0,separatorInd+1);

				if (part.equals("K")) {
					K = Double.parseDouble(parseLine.toString());
				}
				if (part.equals("R")) {
					R = Double.parseDouble(parseLine.toString());
				}
				if (part.equals("F")) {
					F = Double.parseDouble(parseLine.toString());
				}
				if (part.equals("IDEALLENGTH")) {
					IDEALLENGTH = Double.parseDouble(parseLine.toString());
				}
				if (part.equals("ITERATIONS")) {
					ITERATIONS = (int)Double.parseDouble(parseLine.toString());
				}
				if (part.equals("EXPERIMENTS")) {
					EXPERIMENTS = (int)Double.parseDouble(parseLine.toString());
				}
				line = b.readLine();
			}
			b.close();

		} catch(IOException x) {
			System.out.println("An IO exception occured when"
				+"executing loadSettings("+file
				+") in GraphExperimentEdgeLengthData.java: "+x+"\n");
		}

	}


}



