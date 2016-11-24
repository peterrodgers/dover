package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Handles the code required when a user tries to select a file
 * 
 * @author Rob Baker
 *
 */
public class LoadFileActionListener implements ActionListener {

	private JLabel status, fileLabel;
	private JFileChooser fileChooser;
	private JPanel convertPanel;
	private JButton openBtn;
	
	/**
	 * Trivial constructor
	 * 
	 * @param status The main window's status label
	 * @param fileLabel The label to display what file has been selected
	 * @param fileChooser The chooser for choosing a file
	 * @param convertPanel The panel to attach the chooser to
	 * @param openBtn The button that was clicked
	 */
	public LoadFileActionListener(JLabel status, JLabel fileLabel, JFileChooser fileChooser, JPanel convertPanel,
			JButton openBtn) {
		this.status = status;
		this.fileLabel = fileLabel;
		this.fileChooser = fileChooser;
		this.convertPanel = convertPanel;
		this.openBtn = openBtn;
	}
	
	@Override
	/**
	 * When the user clicks on the button
	 */
	public void actionPerformed(ActionEvent evt) {
		status.setText("Waiting for user response");
	    //Handle open button action.
	    if (evt.getSource() == openBtn) {
	        int returnVal = fileChooser.showOpenDialog(convertPanel);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fileLabel.setText(file.getName());
	            fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.PLAIN, fileLabel.getFont().getSize()));
	        }
	   }
	   status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
	}
	
}
