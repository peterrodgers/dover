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
public class FindSubgraphsActionListener implements ActionListener {

	private JList graphList;
	private JProgressBar progressBar;
	private JLabel status;
	private Launcher launcher;
	private JPanel subgraphPanel;
	private JFileChooser fileChooser;
	private FastGraph subgraph;
	
	/**
	 * Trivial constructor
	 * 
	 * @param graphList The list of graphs to choose from
	 * @param progressBar The progress bar to update
	 * @param status The status bar to update
	 * @param launcher The launcher for callbacks
	 * @param subgraphPanel The panel to attach messages to
	 * @param fileChooser The filechooser for a user to select a subgraph
	 * @param subgraph If the've created or edited one, it will be here
	 */
	public FindSubgraphsActionListener(JList graphList, JProgressBar progressBar, JLabel status, Launcher launcher,
			JPanel subgraphPanel, JFileChooser fileChooser, FastGraph subgraph) {
		this.graphList = graphList;
		this.progressBar = progressBar;
		this.status = status;
		this.launcher = launcher;
		this.subgraphPanel = subgraphPanel;
		this.fileChooser = fileChooser;
		this.subgraph = subgraph;
	}

	@Override
	/**
	 * Handles the actual code to load the two graphs and get ready to find subgraphs
	 */
	public void actionPerformed(ActionEvent arg0) {
		String graph = (String) graphList.getSelectedValue();
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
						FastGraph g = launcher.loadFromBuffers(null,graph);
						
						status.setText("Loading Complete");

						System.out.println("Finding subgraphs!");
						System.out.println("Main graph nodes:" + g.getNumberOfNodes());
						System.out.println("Subgraph nodes:" + subgraph.getNumberOfNodes());
				    	
				    	//stop the Progress Bar
				    	progressBar.setIndeterminate(false);
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