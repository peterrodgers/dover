package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Handles any messages displayed to the user when clicking options that might take some time or space.
 * @author Rob Baker
 *
 */
public class MessageActionListener implements ActionListener {

	private JPanel panel;
	private String title, message;
	private int messageType;
	private JCheckBox checkBox;
	
	/**
	 * Trivial constructor
	 * @param checkBox The checkBox, so only show a message when enabled
	 * @param panel The panel to attach the message
	 * @param title The message title
	 * @param message The message text
	 * @param messageType The type of message
	 */
	public MessageActionListener(JCheckBox checkBox, JPanel panel, String title, String message, int messageType) {
		this.checkBox = checkBox;
		this.panel = panel;
		this.title = title;
		this.message = message;
		this.messageType = messageType;
	}

	@Override
	/**
	 * Display the relevant message
	 */
	public void actionPerformed(ActionEvent arg0) {
		if(checkBox.isSelected()) {
			JOptionPane.showMessageDialog(panel, message, title, messageType);
		}		
	}

}
