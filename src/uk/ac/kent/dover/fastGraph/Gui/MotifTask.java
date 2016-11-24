package uk.ac.kent.dover.fastGraph.Gui;

import javax.swing.SwingWorker;

/**
 * Handles the actual processing of the task, including keeping the status bars updated
 * 
 * @author Rob Baker
 *
 */
public class MotifTask extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		int progress = 0;
		setProgress(progress);
		
		while (progress < 100) {
			Thread.sleep(500);
			progress++;
			setProgress(progress);
		}
		
		return null;
	}		

}
