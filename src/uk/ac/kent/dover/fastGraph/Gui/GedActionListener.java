package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JTextField;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.GedUtil;
import uk.ac.kent.dover.fastGraph.Launcher;

public class GedActionListener implements ActionListener {
	
	JTextField graphOneTextField, graphTwoTextField;
	Launcher launcher;
	
	/**
	 * @param graphOneTextField
	 * @param graphTwoTextField
	 * @param launcher
	 */
	public GedActionListener(JTextField graphOneTextField, JTextField graphTwoTextField, Launcher launcher) {
		this.graphOneTextField = graphOneTextField;
		this.graphTwoTextField = graphTwoTextField;
		this.launcher = launcher;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("Click");
				String g1String = graphOneTextField.getText();
				String g2String = graphTwoTextField.getText();

				FastGraph g1 = null;
				FastGraph g2 = null;
				try {
					g1 = launcher.loadFromBuffers(null, g1String);
					g2 = launcher.loadFromBuffers(null, g2String);
					System.out.println("Created dover graphs");

				} catch (IOException e) {
					e.printStackTrace();
				}

				if (g1 != null && g2 != null) {
					System.out.println(GedUtil.getGedScore(g1, g2));
				}
			}
		});
		thread.start();
	}

}
