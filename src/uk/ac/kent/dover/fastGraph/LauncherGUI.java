package uk.ac.kent.dover.fastGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * This class handles all the GUI for the main Launcher.
 * It will callback to the launcher when the use requests an algorithm or such like
 * 
 * @author Rob Baker
 *
 */
@SuppressWarnings("serial")
public class LauncherGUI extends JFrame {
	
	/**
	 * The main builder for the GUI
	 * 
	 * @param launcher Requires the launcher so it can make callback commands when the user presses buttons etc
	 */
	public LauncherGUI(Launcher launcher) {
		try {
			//Makes the GUI look like the OS. This has the benefit of scaling the display if the user has zooming or a changed DPI (as Rob does)
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//Builds the graph selection section
		JPanel northPanel = new JPanel(new BorderLayout());
		JList graphList = buildSourceGraphList();
		northPanel.add(graphList, BorderLayout.NORTH);
		northPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder titled = BorderFactory.createTitledBorder(blackline, "Target Graph");
		titled.setTitleJustification(TitledBorder.LEFT);
		northPanel.setBorder(titled);
		
		JPanel statusArea = new JPanel(new BorderLayout());
		
		//Displays the progressBar. Not displayed until later, but needed here
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("");
		
		//builds the status bar. Again, not used until later;
		JPanel statusBar = buildStatusBar();
		
		//adds the two status updates to the statusArea
		statusArea.add(progressBar, BorderLayout.NORTH);
		statusArea.add(statusBar, BorderLayout.SOUTH);
		
		// Builds the Tabbed area
		JTabbedPane tabbedPane = new JTabbedPane();
		
		JPanel motifPanel = buildMotifTab();
		tabbedPane.addTab("Motif", motifPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel patternPanel = buildPatternTab();
		tabbedPane.addTab("Pattern", patternPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

		JPanel otherPanel = buildOtherTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Others", otherPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_3);
		
		blackline = BorderFactory.createLineBorder(Color.black);
		titled = BorderFactory.createTitledBorder(blackline, "Task");
		titled.setTitleJustification(TitledBorder.LEFT);
		tabbedPane.setBorder(titled);		
		
		
		//Adds both the upper and lower areas to the main Panel
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(statusArea, BorderLayout.SOUTH);
		
		//Builds, Packs and Displays the GUI
		this.setContentPane(mainPanel);
		setTitle("Dover");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * Build the main graph list.
	 * This allows the user to choose which graph they wish to perform things on
	 * @return The JList component containing the graphs
	 */
	private JList buildSourceGraphList() {
		File folder = new File(Launcher.startingWorkingDirectory+File.separatorChar+"data");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> graphs = new ArrayList<String>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (listOfFiles[i].isDirectory() && !listOfFiles[i].getName().equals("snap")) {
	    		graphs.add(listOfFiles[i].getName());
	    	}
	    }
	    Collections.sort(graphs, String.CASE_INSENSITIVE_ORDER);
		
		JList list = new JList(graphs.toArray());
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		return list;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Motif Tab
	 * @return The Motif Tab
	 */
	private JPanel buildMotifTab() {
		JPanel motifPanel = new JPanel(new BorderLayout());
		
		JTextField input = new JTextField(5);		
		JButton motifBtn = new JButton("Find Motifs");
		
		motifBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					int number = Integer.parseInt(input.getText());
					System.out.println("Motif search, with number: " + number);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(motifPanel, "Please enter an integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}				
			}
		});
		
		motifPanel.add(input, BorderLayout.WEST);	
		motifPanel.add(motifBtn, BorderLayout.EAST);
		return motifPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * @return The Pattern Tab
	 */
	private JPanel buildPatternTab() {
		JPanel patternPanel = new JPanel(new BorderLayout());
		patternPanel.add(new JButton("Button"), BorderLayout.WEST);
		return patternPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * 
	 * @param graphList The JList element used to select which is the target graph
	 * @param progressBar The JProgressBar to update when loading a graph
	 * @return the Other tab
	 */
	private JPanel buildOtherTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {
		JPanel otherPanel = new JPanel(new BorderLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);
		
		JButton selectedBtn = new JButton("Node Count");
		otherPanel.add(selectedBtn, BorderLayout.WEST);
		
		//The action for when the use selected the 
		selectedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				String graph = (String) graphList.getSelectedValue();
				if (graph != null) {
					System.out.println(graph);
					
					//set the Progress Bar to move
					progressBar.setIndeterminate(true);
					status.setText("Loading...");

					// Start loading the Graph, but in a separate thread, to avoid locking up the GUI
					Thread thread = new Thread(new Runnable() {

					    @Override
					    public void run() {
					    	FastGraph g = FastGraph.loadBuffersGraphFactory(null, graph);
					    	status.setText("Loading Complete");
					    	System.out.println(g.getNumberOfNodes());
					    	
					    	//stop the Progress Bar
					    	progressBar.setIndeterminate(false);
					    }
					            
					});					        
					thread.start();
					
				} else {
					JOptionPane.showMessageDialog(otherPanel, "Please select a target graph", "No target graph selected", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		
		
		otherPanel.add(new JLabel("more text"), BorderLayout.EAST);
		return otherPanel;
	}
	
	/**
	 * Builds the status bar - used for updating the user on small piece of information
	 * @return The fully built Status Bar
	 */
	private JPanel buildStatusBar() {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("Okay");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		return statusPanel;
	}

}
