package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Handles the code for the user to get more data, by loading the website
 * 
 * @author Rob Baker
 *
 */
public class GetDataActionListener implements ActionListener{
	
	private Launcher launcher;
	private JPanel panel;
	
	
	/**
	 * Trivial constructor
	 * 
	 * @param launcher The launcher for callback functions
	 * @param panel The panel this attaches to
	 */
	public GetDataActionListener(Launcher launcher, JPanel panel) {
		this.launcher = launcher;
		this.panel = panel;
	}



	@Override
	/**
	 * Loads the user's default web browser and navigate to our website
	 */
	public void actionPerformed(ActionEvent evt) {
        try {
            Desktop.getDesktop().browse(new URI(launcher.DATA_URL));
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(panel, "Browser loading is not supported. \nInstead, please visit:\n" + launcher.DATA_URL, "Browser Loading not supported", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
	}
}
