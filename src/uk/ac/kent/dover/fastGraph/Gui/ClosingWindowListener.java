package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Provides a prompt to ensure the user wishes to close down the window. Useful for misclicks if the system is doing something
 * @author Rob Baker
 *
 */
public class ClosingWindowListener implements WindowListener {
	
	boolean systemStatus;
	LauncherGUI launcherGui;
	JFrame frame;
	JLabel status;
	
	 /**
	 * @param systemStatus The current system status. Can switch on/off prompting if needed
	 * @param launcherGui The launcherGui for callbacks
	 * @param frame The frame to attach the confirmation dialog to
	 * @param status The status bar to update
	 */
	public ClosingWindowListener(boolean systemStatus, LauncherGUI launcherGui, JFrame frame, JLabel status) {
		this.systemStatus = systemStatus;
		this.launcherGui = launcherGui;
		this.frame = frame;
		this.status = status;
	}

	/**
	 * When the window closes, if the system is doing something, then ask the user
	 */
	public void windowClosing(WindowEvent e){
     	if(!systemStatus){
     		status.setText("Waiting for user input...");
            int i = JOptionPane.showConfirmDialog(null, "Are you sure to wish to quit?");
            if(i == JOptionPane.YES_OPTION) {
            	status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
            	frame.setVisible(false);
            	frame.dispose();
            	System.exit(0);
            }
            status.setText(LauncherGUI.DEFAULT_STATUS_MESSAGE);
             
     	}
     }

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
