package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Handles the exiting of the program.<br>
 * Uses the closing code in ClosingWindowListener
 * 
 * @author Rob Baker
 *
 */
public class ExitActionListener implements ActionListener {

	JFrame frame;
	
	/**
	 * Trivial constructor
	 * @param frame The frame to attach the closing action to
	 */
	public ExitActionListener(JFrame frame) {
		this.frame = frame;
	}

	@Override
	/**
	 * Exits the system
	 */
	public void actionPerformed(ActionEvent evt) {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

}
