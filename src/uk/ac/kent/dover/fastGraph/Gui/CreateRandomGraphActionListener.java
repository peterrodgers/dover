package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import uk.ac.kent.dover.fastGraph.Launcher;

public class CreateRandomGraphActionListener implements ActionListener {

	private JFileChooser saveChooser;
	private JPanel randomPanel;
	private LauncherGUI launcherGUI;
	private JTextField nodeField, edgeField;
	private JCheckBox directedBox, simpleBox;
	private Launcher launcher;
	private JProgressBar progressBar;
	private JLabel status;
	
	/**
	 * @param saveChooser The chooser for where to save the file
	 * @param randomPanel The panel to attach messages to
	 * @param launcherGUI The launcher GUI for callbacks
	 * @param nodeField The number of nodes field
	 * @param edgeField The number of edges field
	 * @param directedBox The directed box
	 * @param simpleBox The simple box
	 * @param launcher The launcher for callbacks
	 * @param progressBar The progress bar
	 * @param status The status message
	 */
	public CreateRandomGraphActionListener(JFileChooser saveChooser, JPanel randomPanel, LauncherGUI launcherGUI,
			JTextField nodeField, JTextField edgeField, JCheckBox directedBox, JCheckBox simpleBox, Launcher launcher, 
			JProgressBar progressBar, JLabel status) {
		this.saveChooser = saveChooser;
		this.randomPanel = randomPanel;
		this.launcherGUI = launcherGUI;
		this.nodeField = nodeField;
		this.edgeField = edgeField;
		this.directedBox = directedBox;
		this.simpleBox = simpleBox;
		this.launcher = launcher;
		this.progressBar = progressBar;
		this.status = status;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File saveLocation = saveChooser.getSelectedFile();
		
		if (saveLocation == null) {
			JOptionPane.showMessageDialog(randomPanel, "No save location selected", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		boolean fileResult = saveLocation.mkdirs();
		if(!fileResult) { //if the file location is invalid
			JOptionPane.showMessageDialog(randomPanel, "File cannot be created", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int nodeNumber = launcherGUI.checkForPositiveInteger(nodeField.getText(),randomPanel);
		int edgeNumber = launcherGUI.checkForPositiveInteger(edgeField.getText(),randomPanel);
		if (nodeNumber != -1 && edgeNumber != -1) { //if the inputs are incorrect
			
			boolean simple = simpleBox.isSelected();
			boolean directed = directedBox.isSelected();
			
			//set the Progress Bar to move
			progressBar.setIndeterminate(true);
			status.setText("Creating random graph...");
			// Start loading the Graph, but in a separate thread, to avoid locking up the GUI
			Thread thread = new Thread(new Runnable() {

			    @Override
			    public void run() {
			    	try{
				    	launcher.generateRandomGraph(saveLocation, nodeNumber, edgeNumber, directed, simple);

				    	JOptionPane.showMessageDialog(randomPanel, "Graph generation complete", 
				    			"Graph generation complete", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(randomPanel, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					} finally {
				    	//stop the Progress Bar
				    	progressBar.setIndeterminate(false);
				    	status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
					}
			    }
			});
			thread.start();

			
		}
		
	}

}
