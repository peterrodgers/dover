package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Handles the code for the GUI finding subgraphs
 * 
 * @author Rob Baker
 *
 */
public class FindApproximateSubgraphsActionListener implements ActionListener {

	private JFileChooser targetChooser;
	private JProgressBar progressBar;
	private JLabel status;
	private Launcher launcher;
	private JPanel subgraphPanel;
	private JFileChooser fileChooser;
	private FastGraph subgraph;
	private JTextField patternNodesField, subgraphsPerNodeField;
	private LauncherGUI launcherGui;
	
	/**
	 * Trivial constructor
	 * 
	 * @param targetChooser The chooser for the target graph
	 * @param progressBar The progress bar to update
	 * @param status The status bar to update
	 * @param launcher The launcher for callbacks
	 * @param subgraphPanel The panel to attach messages to
	 * @param fileChooser The filechooser for a user to select a subgraph
	 * @param subgraph If the've created or edited one, it will be here
	 */
	public FindApproximateSubgraphsActionListener(JFileChooser targetChooser, JProgressBar progressBar, JLabel status, Launcher launcher,
			JPanel subgraphPanel, JFileChooser fileChooser, FastGraph subgraph, 
			JTextField patternNodesField, JTextField subgraphsPerNodeField, LauncherGUI launcherGui) {
		this.targetChooser = targetChooser;
		this.progressBar = progressBar;
		this.status = status;
		this.launcher = launcher;
		this.subgraphPanel = subgraphPanel;
		this.fileChooser = fileChooser;
		this.subgraph = subgraph;
		this.patternNodesField = patternNodesField;
		this.subgraphsPerNodeField = subgraphsPerNodeField;
		this.launcherGui = launcherGui;
	}

	@Override
	/**
	 * Handles the actual code to load the two graphs and get ready to find subgraphs
	 */
	public void actionPerformed(ActionEvent arg0) {
		File graph = targetChooser.getSelectedFile();
		if (graph != null) {
			System.out.println(graph);
			
			File file = fileChooser.getSelectedFile();
			
			//check that the subgraph has been selected
			if (file == null && subgraph == null) {
				JOptionPane.showMessageDialog(subgraphPanel, "Please select a subgraph", "No subgraph selected", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String directory = file.getPath();
			String name = file.getName();
			try {
				subgraph = launcher.loadFromBuffers(directory, name);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(subgraphPanel, "This buffer could not be found. \n" + e.getMessage(), 
						"Error: IOException", JOptionPane.ERROR_MESSAGE);e.printStackTrace();
				return;
			}
			
			//set the Progress Bar to move
			progressBar.setIndeterminate(true);
			status.setText("Loading...");

			// Start loading the Graph, but in a separate thread, to avoid locking up the GUI
			Thread thread = new Thread(new Runnable() {

			    @Override
			    public void run() {
			    	
			    	//Display error message to the user if this is unavailable
					try {
						
						String name = graph.getName();
						String path = graph.getParent();							
						
						FastGraph g = launcher.loadFromBuffers(path+File.separatorChar+name, name);
						
						status.setText("Finding subgraphs");
						
						int patternNodes = launcherGui.checkForPositiveInteger(patternNodesField.getText(),subgraphPanel);
						int subgraphsPerNode = launcherGui.checkForPositiveInteger(subgraphsPerNodeField.getText(),subgraphPanel);
						
						launcher.approximateSubgraphs(g, subgraph, patternNodes, subgraphsPerNode);
				    	
				    	//stop the Progress Bar
				    	progressBar.setIndeterminate(false);
				    	status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
				    	JOptionPane.showMessageDialog(subgraphPanel, "Subgraph Isomorphism Completed.\nResults are in the subgraph_results folder", "Subgraph Isomorphism Completed", JOptionPane.INFORMATION_MESSAGE);
				    	
					} catch (IOException e) {
						//stop the Progress Bar
						progressBar.setIndeterminate(false);
						JOptionPane.showMessageDialog(subgraphPanel, "This buffer could not be found. \n" + e.getMessage(), "Error: IOException", JOptionPane.ERROR_MESSAGE);
						
						e.printStackTrace();
					}

			    }
			            
			});					        
			thread.start();
			
		} else {
			JOptionPane.showMessageDialog(subgraphPanel, "Please select a target graph", "No target graph selected", JOptionPane.ERROR_MESSAGE);
		}
		
	}

}

