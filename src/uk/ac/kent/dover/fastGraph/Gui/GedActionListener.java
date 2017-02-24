package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.GedUtil;
import uk.ac.kent.dover.fastGraph.Launcher;

/**
 * Runs when the user chooses to find the Graph Edit Distance
 * 
 * @author Rob Baker
 *
 */
public class GedActionListener implements ActionListener {

	private final JTextArea gedScore;
	private final JLabel lastComputed;
	private Launcher launcher;
	private LauncherGUI launcherGUI;
	
	/**
	 * Runs when the user chooses to find the Graph Edit Distance
	 * 
	 * @param launcher The launcher for callbacks
	 * @param launcherGUI The launcherGUI to find what graphs to run on
	 * @param gedScore The label to update the score
	 */
	public GedActionListener(Launcher launcher, LauncherGUI launcherGUI, JTextArea gedScore, JLabel lastComputed) {
		this.launcher = launcher;
		this.launcherGUI = launcherGUI;
		this.gedScore = gedScore;
		this.lastComputed = lastComputed;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				File f1 = launcherGUI.getGed1();
				File f2 = launcherGUI.getGed2();

				FastGraph g1 = null;
				FastGraph g2 = null;
				try {
					String name = f1.getName();
					String path = f1.getParent();				
					g1 = launcher.loadFromBuffers(path+File.separatorChar+name, name);
					
					String name2 = f2.getName();
					String path2 = f2.getParent();						
					g2 = launcher.loadFromBuffers(path2+File.separatorChar+name2, name2);


				} catch (IOException e) {
					e.printStackTrace();
				}

				if (g1 != null && g2 != null) {
					float score = GedUtil.getGedScore(g1, g2);
					gedScore.setText(String.valueOf(score));
					lastComputed.setText("Calculated: " + g1.getName() + " and " + g2.getName());
				}
			}
		});
		thread.start();
	}

}
