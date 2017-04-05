package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Handles the actual processing of the task, including keeping the status bars updated
 * 
 * @author Rob Baker
 *
 */
public class MotifTask extends SwingWorker<Void, Progress> {
	
	private JProgressBar bigProgress, smallProgress, progressBar;
	private Launcher launcher;
	private File graph, reference;
	private int minSize, maxSize;
	private JLabel status;
	private Progress lastProgress;
	private JPanel motifPanel;
	private boolean saveAll;
	
	/**
	 * Trivial constructor
	 * 
	 * @param launcher The main launcher of this project. Used for call back methods
	 * @param bigProgress The bigger progress bar
	 * @param smallProgress The smaller progress bar
	 * @param graph The file of the graph to be run
	 * @param minSize The minimum number of nodes in a motif
	 * @param maxSize The maximum number of nodes in a motif
	 * @param progressBar The main window's progress bar
	 * @param status The main window's status bar
	 * @param motifPanel The panel to attach errors to
	 * @param saveAll If the user wishes to save every motif example
	 * @param reference The reference graph, if there is one
	 */
	public MotifTask(Launcher launcher, JProgressBar bigProgress, JProgressBar smallProgress, File graph, int minSize, int maxSize,
			JProgressBar progressBar, JLabel status, JPanel motifPanel, boolean saveAll, File reference) {
		this.launcher = launcher;
		this.bigProgress = bigProgress;
		this.smallProgress = smallProgress;
		this.graph = graph;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.progressBar = progressBar;
		this.status = status;
		this.motifPanel = motifPanel;
		this.saveAll = saveAll;
		this.reference = reference;
	}
	

	/**
	 * Publishes the current state of the system to the worker for display on the progress bars
	 * 
	 * @param mainTaskNum The number of the main Progress bar (0-100)
	 * @param mainTaskText The text to be displayed on the main Progress bar
	 * @param childTaskNum The number of the smaller Progress bar (0-100)
	 * @param childTaskText The text to be displayed on the smaller Progress bar
	 */
	public void publish(int mainTaskNum, String mainTaskText, int childTaskNum, String childTaskText){
		lastProgress = new Progress(mainTaskNum, mainTaskText, childTaskNum, childTaskText);
		publish(lastProgress);
	}
	
	/**
	 * Publishes the current state of the system to the worker for display on the progress bars.
	 * Does not update all fields. If main = true, then it updates the main ones only, if not only the child ones
	 * 
	 * @param taskNum The number of the Progress bar (0-100)
	 * @param taskText The text to be displayed on the Progress bar
	 * @param main Which progress bar to update
	 */
	public void publish(int taskNum, String taskText, boolean main){
		if(main) {
			lastProgress = new Progress(taskNum, taskText, lastProgress.getChildTaskNum(), lastProgress.getChildTaskText());
		} else {
			lastProgress = new Progress(lastProgress.getMainTaskNum(), lastProgress.getMainTaskText(), taskNum, taskText);
		}
		publish(lastProgress);
	}
	

	@Override
	/**
	 * Updates the progress bars with current state of the system.<br>
	 * Never called explicitly
	 * 
	 * @param list It needs a list
	 */
	protected void process(List<Progress> list) {
		//Progress p = list.get(list.size()-1);
		for(Progress p : list) {
			bigProgress.setValue(p.getMainTaskNum());
			bigProgress.setString(p.getMainTaskText());
			smallProgress.setValue(p.getChildTaskNum());
			smallProgress.setString(p.getChildTaskText());
			
		}
	}
	
	@Override
	/**
	 * Runs the code which will be run in a different thread, so not to lock up the GUI
	 */
	protected Void doInBackground() throws Exception {
		int progress = 0;
		setProgress(progress);
		progressBar.setIndeterminate(true);
		status.setText("Finding Motifs...");

		String name = graph.getName();
		String path = graph.getParent();							
		
		launcher.findMotifs(this, path+File.separatorChar+name, name, minSize, maxSize, saveAll, reference);
		//launcher
		
		return null;
		
	}
	
	@Override
	/**
	 * This is run when the main body is complete
	 */
	protected void done() {
		progressBar.setIndeterminate(false);
		status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
		String url = "file:///"+Launcher.startingWorkingDirectory+File.separatorChar+"motifs"+File.separatorChar+graph.getName()+File.separatorChar+"index.html";
		url = url.replace("\\", "/");
		try {
            Desktop.getDesktop().browse(new URI(url));
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(motifPanel, "Exporting complete. \nOutput file is sotred in motif/graphName/index.html \n", "Motif finding complete", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
		
	}

	/**
	 * Used for setting if the smaller bar is indeterminate, i.e. if loading an unknown quantity
	 * @param indeterminate If the small bar is indeterminate
	 */
	public void setSmallIndeterminate(boolean indeterminate) {
		smallProgress.setIndeterminate(indeterminate);
	}
	
}
