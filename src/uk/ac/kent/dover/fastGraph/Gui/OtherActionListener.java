package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * 
 * Handles the button on the "Other" tab
 * @author Rob Baker
 *
 */
public class OtherActionListener implements ActionListener {

	private JList graphList;
	private JProgressBar progressBar;
	private JLabel status;
	private Launcher launcher;
	private JPanel otherPanel;
	
	
	
	/**
	 * @param graphList The list of graphs for a user to choose from
	 * @param progressBar The main window's progress bar
	 * @param status The main window's status bar
	 * @param launcher The launcher for callback functions
	 * @param otherPanel The panel to attach to
	 */
	public OtherActionListener(JList graphList, JProgressBar progressBar, JLabel status, Launcher launcher,
			JPanel otherPanel) {
		this.graphList = graphList;
		this.progressBar = progressBar;
		this.status = status;
		this.launcher = launcher;
		this.otherPanel = otherPanel;
	}



	@Override
	public void actionPerformed(ActionEvent evt) {
		String graph = (String) graphList.getSelectedValue();
		if (graph != null) {
			System.out.println(graph);
			
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
				    	System.out.println("Maximum Degree: " + g.maximumDegree());
				    	System.out.println("Degree Counts: " + Arrays.toString(g.countInstancesOfNodeDegrees(5)));
				    	
				    	//stop the Progress Bar
				    	progressBar.setIndeterminate(false);
					} catch (IOException e) {
						//stop the Progress Bar
						progressBar.setIndeterminate(false);
						JOptionPane.showMessageDialog(otherPanel, "This buffer could not be found. \n" + e.getMessage(), "Error: IOException", JOptionPane.ERROR_MESSAGE);
						
						e.printStackTrace();
					}

			    }
			            
			});					        
			thread.start();
			
		} else {
			JOptionPane.showMessageDialog(otherPanel, "Please select a target graph", "No target graph selected", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
}
