package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 * What happens when a user chooses a graph for the GED 
 * 
 * @author Rob Baker
 *
 */
public class GraphSelectedActionListener implements ActionListener{
	
	private JFileChooser targetChooser;
	private JTextField graphOneTextField;
	private File graph;
	private LauncherGUI launcherGUI;
	private int number;
	
	/**
	 * What happens when a user chooses a graph for the GED
	 * @param graph The targetGraph
	 * @param targetChooser The targetChooser the chooser for the target graph
	 * @param graphOneTextField The text field to update
	 * @param launcherGUI The launcherGUI for callbacks
	 * @param number If this is the first or second GED graph
	 */
	public GraphSelectedActionListener(File graph, JFileChooser targetChooser, JTextField graphOneTextField, 
			LauncherGUI launcherGUI, int number) {
		this.graph = graph;
		this.targetChooser = targetChooser;
		this.graphOneTextField = graphOneTextField;
		this.launcherGUI = launcherGUI;
		this.number = number;
	}


	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		File f = targetChooser.getSelectedFile();
		System.out.println(f);
		if (f != null) {
			if (number == 1) {
				launcherGUI.setGed1(f);
			} else {
				launcherGUI.setGed2(f);
			}
			
			graphOneTextField.setText(f.getName());
		}
		
	}

}
