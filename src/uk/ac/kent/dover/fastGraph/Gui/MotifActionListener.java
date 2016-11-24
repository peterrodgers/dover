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

	Launcher launcher;
	LauncherGUI launcherGUI;
	JTextField minInput;
	JTextField maxInput;
	JPanel motifPanel;
	JProgressBar bigProgress;
	
	/**
	 * Constructor to assign all the relevant fields
	 * @param launcher The launcher - for callback methods
	 * @param launcherGUI The launcherGUI - for callback methods
	 * @param minInput The minimum size of motifs field
	 * @param maxInput The maximum size of motifs field
	 * @param motifPanel The panel this button is attached to
	 * @param bigProgress The big progress bar
	 */
	public MotifActionListener(Launcher launcher, LauncherGUI launcherGUI, JTextField minInput, JTextField maxInput, JPanel motifPanel, JProgressBar bigProgress) {
		this.launcher = launcher;
		this.launcherGUI = launcherGUI;
		this.minInput = minInput;
		this.maxInput = maxInput;
		this.motifPanel = motifPanel;
		this.bigProgress = bigProgress;
	}
	
	@Override
	/**
	 * When the button has been clicked
	 */
	public void actionPerformed(ActionEvent evt) {
		
		int minNumber = launcherGUI.checkForPositiveInteger(minInput.getText(),motifPanel);
		int maxNumber = launcherGUI.checkForPositiveInteger(maxInput.getText(),motifPanel);
		if (minNumber != -1 && maxNumber != -1) {
			
			//replace with actual motif code
			System.out.println("Motif search, with number: " + minNumber);
			MotifTask task = new MotifTask();
			task.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if("progress" == evt.getPropertyName()) {
						int bigProcessNum = (int) evt.getNewValue();
						bigProgress.setValue(bigProcessNum);
					}
					//TODO add else ifs for the other changes
				}		
			});
			task.execute();
		}		
	}

}
