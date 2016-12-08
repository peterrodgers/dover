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
	 * @param systemStatus
	 * @param launcherGui
	 * @param frame
	 * @param status
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
             int i = JOptionPane.showConfirmDialog(null, "Are you sure to wish to quit?");
             if(i == JOptionPane.YES_OPTION) {
            	 status.setText(launcherGui.DEFAULT_STATUS_MESSAGE);
            	 frame.setVisible(false);
            	 System.out.println("closing");
             }
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
