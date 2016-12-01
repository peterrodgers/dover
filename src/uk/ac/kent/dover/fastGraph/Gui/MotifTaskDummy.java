package uk.ac.kent.dover.fastGraph.Gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Dummy class to avoid null checking when methods are run by the command line.<br>
 * This implements all public methods of MotifTask, but has no functionality.<br>
 * Potentially this could be modified to output to a log file
 * 
 * @author Rob Baker
 *
 */
public class MotifTaskDummy extends MotifTask {

	int bigP, smallP;
	String bigT, smallT;
	
	/**
	 * Trivial constructor
	 */
	public MotifTaskDummy() {
		this(null,null,null,null,0,0,null,null,null);
	}
	
	/**
	 * Trivial constructor. Parameters are not used.
	 * 
	 * @param launcher Not Used.
	 * @param bigProgress Not Used.
	 * @param smallProgress Not Used.
	 * @param graph Not Used.
	 * @param minSize Not Used.
	 * @param maxSize Not Used.
	 * @param progressBar Not Used.
	 * @param status Not Used.
	 */
	public MotifTaskDummy(Launcher launcher, JProgressBar bigProgress, JProgressBar smallProgress, String graph,
			int minSize, int maxSize, JProgressBar progressBar, JLabel status, JPanel panel) {
		super(launcher, bigProgress, smallProgress, graph, minSize, maxSize, progressBar, status, panel);
	}
	
	/**
	 * Dummy class to avoid null checking when methods are run by the command line.
	 * 
	 * @param mainTaskNum Not Used.
	 * @param mainTaskText Not Used.
	 * @param childTaskNum Not Used.
	 * @param childTaskText Not Used.
	 */
	public void publish(int mainTaskNum, String mainTaskText, int childTaskNum, String childTaskText){
		bigP = mainTaskNum;
		bigT = mainTaskText;
		smallP = childTaskNum;
		smallT = childTaskText;		
	}
	
	/**
	 * Dummy class to avoid null checking when methods are run by the command line.
	 * 
	 * @param taskNum Not Used.
	 * @param taskText Not Used.
	 * @param main Not Used.
	 */
	public void publish(int taskNum, String taskText, boolean main){
		if(main) {
			bigP = taskNum;
			bigT = taskText;
		} else {
			smallP = taskNum;
			smallT = taskText;			
		}
	}
	
	/**
	 * Dummy class to avoid null checking when methods are run by the command line.
	 * @param Not Used.
	 */
	public void setSmallIndeterminate(boolean indeterminate) {
	}
	
	

}
