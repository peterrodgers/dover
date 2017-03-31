package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;
import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Handles the code for the GUI finding approximate motifs
 * 
 * @author Rob Baker
 *
 */
public class FindApproximateMotifActionListener implements ActionListener {


	private Launcher launcher;
	private LauncherGUI launcherGUI;
	private JTextField minInput, maxInput, clustersInput, iterationsInput, subgraphsPerNodeField, attemptsField;
	private JPanel motifPanel;
	private JLabel statusBar;
	private JProgressBar progressBar;
	private JFileChooser targetChooser;
	
	/**
	 * Constructor to assign all the relevant fields
	 * @param launcher The launcher - for callback methods
	 * @param launcherGUI The launcherGUI - for callback methods
	 * @param minInput The minimum size of motifs field
	 * @param maxInput The maximum size of motifs field
	 * @param motifPanel The panel this button is attached to
	 * @param bigProgress The big progress bar
	 * @param smallProgress The smaller progress bar
	 * @param progressBar The main window progress bar
	 * @param statusBar The main window status bar
	 * @param targetChooser The chooser for the user to select the target graph
	 * @param saveAllBox If the user wishes to save every example of motif
	 * @param refChooser The box for the user to select a reference graph. May be empty
	 */
	public FindApproximateMotifActionListener(Launcher launcher, LauncherGUI launcherGUI, JTextField minInput, JTextField maxInput, 
			JTextField clustersInput, JTextField iterationsInput, JTextField subgraphsPerNodeField, JTextField attemptsField, 
			JPanel motifPanel, JProgressBar progressBar, JLabel statusBar, JFileChooser targetChooser) {
		this.launcher = launcher;
		this.launcherGUI = launcherGUI;
		this.minInput = minInput;
		this.maxInput = maxInput;
		this.clustersInput = clustersInput;
		this.iterationsInput = iterationsInput;
		this.subgraphsPerNodeField = subgraphsPerNodeField;
		this.attemptsField = attemptsField;
		this.motifPanel = motifPanel;
		this.progressBar = progressBar;
		this.statusBar = statusBar;
		this.targetChooser = targetChooser;
	}
	
	@Override
	/**
	 * When the button has been clicked
	 */
	public void actionPerformed(ActionEvent evt) {
		//ensure that the numbers are valid
		int minNumber = launcherGUI.checkForPositiveInteger(minInput.getText(),motifPanel);
		int maxNumber = launcherGUI.checkForPositiveInteger(maxInput.getText(),motifPanel);
		int clusters = launcherGUI.checkForPositiveInteger(clustersInput.getText(),motifPanel);
		int iterations = launcherGUI.checkForPositiveInteger(iterationsInput.getText(),motifPanel);
		int subgraphsPerNode = launcherGUI.checkForPositiveInteger(subgraphsPerNodeField.getText(),motifPanel);
		int attempts = launcherGUI.checkForPositiveInteger(attemptsField.getText(),motifPanel);
		
		if (minNumber != -1 && maxNumber != -1 && clusters != -1 && iterations != -1 && subgraphsPerNode != -1 && attempts != -1) {

			File graph = targetChooser.getSelectedFile();
			if(graph != null) {
				//run motifs
				
				//set the Progress Bar to move
				progressBar.setIndeterminate(true);
				statusBar.setText("Finding Motifs ...");
				
				// Start loading the Graph, but in a separate thread, to avoid locking up the GUI
				Thread thread = new Thread(new Runnable() {

				    @Override
				    public void run() {
				
						try {
							String name = graph.getName();
							String path = graph.getParent();
							
							FastGraph g = launcher.loadFromBuffers(path+File.separatorChar+name, name);
						
							launcher.approximateMotifs(g, minNumber, maxNumber, clusters, iterations, subgraphsPerNode, attempts);
							
							//stop the Progress Bar
					    	progressBar.setIndeterminate(false);
					    	statusBar.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
					    	JOptionPane.showMessageDialog(motifPanel, "Approximate Motif Finding Completed.\nResults are in the kmedoids_results folder", "Approximate Motif Finding Completed", JOptionPane.INFORMATION_MESSAGE);
					    	
					    	
						} catch (IOException e) {
							JOptionPane.showMessageDialog(motifPanel, "Error Loading Graph", "Error Loading Graph", JOptionPane.ERROR_MESSAGE);
						} catch (FastGraphException e) {
							JOptionPane.showMessageDialog(motifPanel, "Error Running Algorithm", "Error Running Algorithm", JOptionPane.ERROR_MESSAGE);
						}
						progressBar.setIndeterminate(false);
						statusBar.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
				    }
				});
				thread.start();
				
			} else {
				JOptionPane.showMessageDialog(motifPanel, "Please select a target graph", "No target graph selected", JOptionPane.ERROR_MESSAGE);
			}
			
		} else {
			JOptionPane.showMessageDialog(motifPanel, "All parameters must be positive integers", "Error in parameters", JOptionPane.ERROR_MESSAGE);
		}
	}

}
