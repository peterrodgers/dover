package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * The ActionListener for convert a graph into buffers
 * 
 * @author Rob Baker
 *
 */
public class ConvertGraphActionListener implements ActionListener{

	private Launcher launcher;
	private LauncherGUI launcherGUI;
	private JTextField nodeField;
	private JTextField edgeField;
	private JFileChooser fileChooser;
	private JRadioButton directed;
	private JPanel convertPanel;
	private JProgressBar progressBar;
	private JLabel status;
	
	/**
	 * Trivial constructor
	 * 
	 * @param launcher The launcher for callback methods
	 * @param launcherGUI The launcherGUI for callback methods
	 * @param nodeField The number of nodes field
	 * @param edgeField The number of edges field
	 * @param fileChooser The file Chooser to select the input file
	 * @param directed If the graph is directed
	 * @param convertPanel The JPanel the button sits on
	 * @param progressBar The main progress bar to be updated
	 * @param status The main status message to be updated
	 */
	public ConvertGraphActionListener(Launcher launcher, LauncherGUI launcherGUI, JTextField nodeField, JTextField edgeField, 
			JFileChooser fileChooser, JRadioButton directed, JPanel convertPanel, JProgressBar progressBar,
			JLabel status) {
		this.launcher = launcher;
		this.launcherGUI = launcherGUI;
		this.nodeField = nodeField;
		this.edgeField = edgeField;
		this.fileChooser = fileChooser;
		this.directed = directed;
		this.convertPanel = convertPanel;
		this.progressBar = progressBar;
		this.status = status;
	}
	
	@Override
	/**
	 * Performs the conversion
	 */
	public void actionPerformed(ActionEvent evt) {
		//check that the numbers of nodes are valid
		
		int nodeNumber = launcherGUI.checkForPositiveInteger(nodeField.getText(),convertPanel);
		int edgeNumber = launcherGUI.checkForPositiveInteger(edgeField.getText(),convertPanel);
		if (nodeNumber != -1 && edgeNumber != -1) {
			//input numbers are valid
			
			//if the undirected button is selected, then the graph is undirected
			boolean directedGraph = directed.isSelected();
			File graphFile = fileChooser.getSelectedFile();
			String name = graphFile.getName();
			String path = fileChooser.getCurrentDirectory().toString();
			
			System.out.println(fileChooser.getSelectedFile());
			System.out.println("path: " + fileChooser.getCurrentDirectory());
			System.out.println("node: " + nodeNumber);
			System.out.println("edge: " + edgeNumber);
			System.out.println("directed: " + directedGraph);
			
			//set the Progress Bar to move
			progressBar.setIndeterminate(true);
			status.setText("Converting...");

			// Start converting the Graph, but in a separate thread, to avoid locking up the GUI
			Thread thread = new Thread(new Runnable() {

			    @Override
			    public void run() {
			    	
			    	//Display error message to the user if this is unavailable
					try {
						
						launcher.convertGraphToBuffers(nodeNumber, edgeNumber, path, name, directedGraph);
						
						status.setText("Conversion Complete");
				    	//stop the Progress Bar
				    	progressBar.setIndeterminate(false);
					} catch (Exception e) {
						//stop the Progress Bar
						progressBar.setIndeterminate(false);
						JOptionPane.showMessageDialog(convertPanel, "This buffer could not be built. \n" + e.getMessage(), "Error: Exception", JOptionPane.ERROR_MESSAGE);
						
						e.printStackTrace();
					}

			    }
			            
			});					        
			thread.start();
			
		}
	}
}
