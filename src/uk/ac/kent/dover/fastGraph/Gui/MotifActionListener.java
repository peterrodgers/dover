package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Handles the code relating to the when the Motif button is clicked
 * 
 * @author Rob Baker
 *
 */
public class MotifActionListener implements ActionListener {

	private Launcher launcher;
	private LauncherGUI launcherGUI;
	private JTextField minInput;
	private JTextField maxInput;
	private JPanel motifPanel;
	private JLabel statusBar;
	private JProgressBar bigProgress, smallProgress, progressBar;
	private JList graphList;
	
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
	 * @param graphList The list of graphs to identify which buffer the user wishes to load
	 */
	public MotifActionListener(Launcher launcher, LauncherGUI launcherGUI, JTextField minInput, JTextField maxInput, JPanel motifPanel, 
			JProgressBar bigProgress, JProgressBar smallProgress, JProgressBar progressBar, JLabel statusBar, JList graphList) {
		this.launcher = launcher;
		this.launcherGUI = launcherGUI;
		this.minInput = minInput;
		this.maxInput = maxInput;
		this.motifPanel = motifPanel;
		this.bigProgress = bigProgress;
		this.smallProgress = smallProgress;
		this.progressBar = progressBar;
		this.statusBar = statusBar;
		this.graphList = graphList;
	}
	
	@Override
	/**
	 * When the button has been clicked
	 */
	public void actionPerformed(ActionEvent evt) {
		//ensure that the numbers are valid
		int minNumber = launcherGUI.checkForPositiveInteger(minInput.getText(),motifPanel);
		int maxNumber = launcherGUI.checkForPositiveInteger(maxInput.getText(),motifPanel);
		if (minNumber != -1 && maxNumber != -1) {

			String graph = (String) graphList.getSelectedValue();
			if(graph != null) {
				//create a Task that will run the motif code and provide updates
				MotifTask task = new MotifTask(launcher, bigProgress, smallProgress, graph, minNumber, maxNumber, progressBar, statusBar);
				task.execute();
			} else {
				JOptionPane.showMessageDialog(motifPanel, "Please select a target graph", "No target graph selected", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}

}
