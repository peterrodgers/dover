package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * Clears the current file selection
 * @author Rob Baker
 *
 */
public class ClearGraphActionListener implements ActionListener {

	private LauncherGUI launcherGUI;
	private JFileChooser targetChooser;
	private FastGraph targetGraph;
	private JLabel fileLabel;
	
	/**
	 * Clears the current file selection
	 * @param launcherGUI the launcherGUI for callbacks
	 * @param targetChooser The file chooser to reset
	 * @param targetGraph
	 * @param fileLabel
	 * @param status
	 */
	public ClearGraphActionListener(LauncherGUI launcherGUI, JFileChooser targetChooser, FastGraph targetGraph, JLabel fileLabel) {
		this.launcherGUI = launcherGUI;
		this.targetChooser = targetChooser;
		this.targetGraph = targetGraph;
		this.fileLabel = fileLabel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		targetChooser.setSelectedFile(null);
		targetGraph = null;
		fileLabel.setText(launcherGUI.DEFAULT_FILE_MESSAGE);
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));
	}

}
