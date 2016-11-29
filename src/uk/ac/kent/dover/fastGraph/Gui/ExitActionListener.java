package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the exiting of the program.<br>
 * 
 * @author Rob Baker
 *
 */
public class ExitActionListener implements ActionListener {

	@Override
	/**
	 * Exits the system
	 */
	public void actionPerformed(ActionEvent evt) {
		System.exit(0);	
	}

}
