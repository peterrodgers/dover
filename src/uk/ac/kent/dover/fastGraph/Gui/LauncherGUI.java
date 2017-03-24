package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.Launcher;
import uk.ac.kent.dover.fastGraph.Util;

/**
 * This class handles all the GUI for the main Launcher.
 * It will callback to the launcher when the use requests an algorithm or such like
 * 
 * @author Rob Baker
 *
 */
@SuppressWarnings("serial")
public class LauncherGUI extends JFrame {
	
	public static final String DEFAULT_STATUS_MESSAGE = "Ready"; //The default message displayed to a user
	public static final String DEFAULT_FILE_MESSAGE = "No file selected  ";

	private Launcher launcher;
	private double screenWidth; //size of the user's screen
	private double screenHeight;
	private int windowWidth, windowHeight;	
	private double textHeight;
	private File ged1, ged2;
	private JFileChooser targetChooser;


	/**
	 * The main builder for the GUI
	 *
	 * @param launcher Requires the launcher so it can make callback commands when the user presses buttons etc
	 */
	public LauncherGUI(Launcher launcher) {
		this.launcher = launcher;
		try {
			//Makes the GUI look like the OS. This has the benefit of scaling the display if the user has zooming or a changed DPI (as Rob does)
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		//find and store the current screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();
		textHeight = screenHeight / 100;

		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//builds the status bar. Again, not used until later;
		JPanel statusBar = buildStatusBar();

		//Builds the graph selection section
		JPanel northPanel = new JPanel(new BorderLayout());
		//JList graphList = buildSourceGraphList();
		
		
		JPanel targetGraphPanel = buildTargetGraphPanel(statusBar);
		//find targetChooser
		
		northPanel.add(targetGraphPanel, BorderLayout.NORTH);

		mainPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

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

		//adds the two status updates to the statusArea
		statusArea.add(progressBar, BorderLayout.NORTH);
		statusArea.add(statusBar, BorderLayout.SOUTH);

		// Builds the Tabbed area
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);

		JPanel motifPanel = buildMotifTab(progressBar, statusBar, targetChooser);
		tabbedPane.addTab("Motif", motifPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel subgraphPanel = buildSubgraphTab(progressBar, statusBar, targetChooser);
		tabbedPane.addTab("Exact Subgraph", subgraphPanel);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		JPanel approxSubgraphPanel = buildApproximateSubgraphTab(progressBar, statusBar, targetChooser);
		tabbedPane.addTab("Approximate Subgraph", approxSubgraphPanel);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JPanel convertPanel = buildConvertTab(progressBar, statusBar);
		tabbedPane.addTab("Convert Graph", convertPanel);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
		
		JPanel randomGraphPanel = buildRandomGraphTab(progressBar, statusBar);
		tabbedPane.addTab("Random Graph", randomGraphPanel);
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

		JPanel otherPanel = buildOtherTab(progressBar, statusBar, targetChooser);
		tabbedPane.addTab("Others", otherPanel);
		tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);

		JPanel gedPanel = buildGedTab(progressBar, statusBar, targetChooser);
		tabbedPane.addTab("GED", gedPanel);
		tabbedPane.setMnemonicAt(6, KeyEvent.VK_7);

		blackline = BorderFactory.createLineBorder(Color.black);
		titled = BorderFactory.createTitledBorder(blackline, "Task");
		titled.setTitleJustification(TitledBorder.LEFT);
		tabbedPane.setBorder(titled);		
		
		
		//Adds both the upper and lower areas to the main Panel
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(statusArea, BorderLayout.SOUTH);
		
		setJMenuBar(buildMenuBar(mainPanel));
		
		//Builds, Packs and Displays the GUI
		this.setContentPane(mainPanel);
		setTitle("Dover");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		windowWidth = (int) Math.round(screenHeight/2.5);
		windowHeight = (int) Math.round(screenHeight/2.5);
		mainPanel.setPreferredSize(new Dimension(windowWidth,windowHeight)); //makes a square window
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ClosingWindowListener(false,this,this,(JLabel) statusBar.getComponent(0)));
		
		List<Image> icons = new ArrayList<Image>();
		ImageIcon icon128 = new ImageIcon("lib"+File.separatorChar+"icon128.png");
		ImageIcon icon64 = new ImageIcon("lib"+File.separatorChar+"icon64.png");
		ImageIcon icon32 = new ImageIcon("lib"+File.separatorChar+"icon32.png");
		icons.add(icon128.getImage());
		icons.add(icon64.getImage());
		icons.add(icon32.getImage());
		setIconImages(icons);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Builds the menu bar
	 * 
	 * @param panel The panel which the menu bar is attached to - used for error messages
	 * @return The menu bar
	 */
	private JMenuBar buildMenuBar(JPanel panel) {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.getAccessibleContext().setAccessibleDescription("Exit the program");
		exit.addActionListener(new ExitActionListener(this));
		fileMenu.add(exit);

		menuBar.add(fileMenu);

		JMenu dataMenu = new JMenu("Data");

		JMenuItem data = new JMenuItem("Get More Data");
		data.getAccessibleContext().setAccessibleDescription("Allows the user to get more data");
		data.addActionListener(new GetDataActionListener(launcher,panel));
		dataMenu.add(data);
		
		menuBar.add(dataMenu);
		
		return menuBar;
	}

	/**
	 * Build the main graph list.
	 * This allows the user to choose which graph they wish to perform things on
	 * @return The JList component containing the graphs
	 */
/*	private JList buildSourceGraphList() {
		File folder = new File(Launcher.startingWorkingDirectory+File.separatorChar+"data");
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> graphs = new ArrayList<String>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	if (listOfFiles[i].isDirectory() && !listOfFiles[i].getName().equals("snap")) {
	    		graphs.add(listOfFiles[i].getName());
	    	}
	    }
	    Collections.sort(graphs, String.CASE_INSENSITIVE_ORDER);
	    
	    for(String s : graphs) {
	    	model.addElement(s);
	    }		
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		return list;
	}*/
	
	/**
	 * Builds the Panel used to house the GUI elements for the Target Graph Tab
	 * @return The Pattern Tab
	 */
	private JPanel buildTargetGraphPanel(JPanel statusBar) {
		FastGraph targetGraph = null;
		JLabel status = (JLabel) statusBar.getComponent(0);
		
		JPanel targetGraphPanel = new JPanel(new GridBagLayout());
		
		JLabel targetGraphLabel = new JLabel("Select Target Graph", SwingConstants.CENTER);		

		targetChooser = new JFileChooser();
		targetChooser.setCurrentDirectory(new java.io.File("."));
		targetChooser.setDialogTitle("Select target graph directory");
		targetChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
		JButton openBtn = new JButton("Open File...");	
		openBtn.setToolTipText("Open a graph from disk");
		
		JLabel fileLabel = new JLabel(DEFAULT_FILE_MESSAGE);
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));
		
		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, targetChooser, targetGraphPanel, openBtn));
		
		JButton addBtn = new JButton("Create");	
		addBtn.addActionListener(new CreateGraphActionListener(this, targetChooser, targetGraphPanel, targetGraph, fileLabel, status));
		addBtn.setToolTipText("Design a graph");
		
		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new CreateGraphActionListener(this, targetChooser, targetGraphPanel, targetGraph, fileLabel, status));
		editBtn.setToolTipText("Edit a selected graph. Only for small graphs");
		
		JButton clearBtn = new JButton("Clear Selection");
		clearBtn.addActionListener(new ClearGraphActionListener(this, targetChooser, targetGraph, fileLabel));
		clearBtn.setToolTipText("Clear selected graph");
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		targetGraphPanel.add(targetGraphLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		targetGraphPanel.add(openBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		targetGraphPanel.add(fileLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		targetGraphPanel.add(editBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		targetGraphPanel.add(addBtn, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		targetGraphPanel.add(clearBtn, c);
		
		return targetGraphPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Motif Tab
	 * @param progressBar The main progress bar
	 * @param statusBar The status bar to update
	 * @param targetChooser The chooser for the target graph
	 * @return The Motif Tab
	 */
	private JPanel buildMotifTab(JProgressBar progressBar, JPanel statusBar, JFileChooser targetChooser) {
		JPanel motifPanel = new JPanel(new GridBagLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);

		JLabel infoLabel = new JLabel("Find and export motifs", SwingConstants.CENTER);
		
		JLabel minLabel = new JLabel("Min Size: ");
		JTextField minInput = new JTextField(3);
		String minToolTip = "The minimum size of motif to be found";
		minLabel.setToolTipText(minToolTip);
		minInput.setToolTipText(minToolTip);		
		
		JLabel maxLabel = new JLabel("Max Size: ");
		JTextField maxInput = new JTextField(3);
		String maxToolTip = "The maximum size of motif to be found";
		maxLabel.setToolTipText(maxToolTip);
		maxInput.setToolTipText(maxToolTip);			
		
		JCheckBox saveAllBox = new JCheckBox("Save all motifs: ");
		saveAllBox.setSelected(false);
		saveAllBox.setToolTipText("This will take some time and disk space");
		saveAllBox.setHorizontalTextPosition(SwingConstants.LEFT);
		saveAllBox.addActionListener(new MessageActionListener(saveAllBox, motifPanel, "Save all motifs", 
				"This may take some time to complete and potentially use a large amount of disk space", JOptionPane.WARNING_MESSAGE));
		
		JSeparator sep1 = new JSeparator(SwingConstants.HORIZONTAL);
		
		JLabel refLabel = new JLabel("Optional: Specify reference set", SwingConstants.CENTER);
		refLabel.setToolTipText("If no reference set is chosen, then rewire target graph");
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select subgraph directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
		JButton openBtn = new JButton("Open File...");
		
		JLabel fileLabel = new JLabel("No file selected  ");
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));
		
		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, fileChooser, motifPanel, openBtn));
		
		JButton motifBtn = new JButton("Find Motifs");
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		JProgressBar bigProgress = new JProgressBar(0, 100);
		bigProgress.setValue(0);
		bigProgress.setStringPainted(true);
		bigProgress.setString("");
		JProgressBar smallProgress = new JProgressBar(0, 100);
		smallProgress.setValue(0);
		smallProgress.setStringPainted(true);
		smallProgress.setString("");

		//add an action when the user clicks this button
		//in an external class, so needs lots(!) of parameters
		motifBtn.addActionListener(new MotifActionListener(launcher, this, minInput, maxInput, motifPanel,
				bigProgress, smallProgress, progressBar, status, targetChooser, saveAllBox, fileChooser));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        motifPanel.add(infoLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        motifPanel.add(minLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        motifPanel.add(minInput, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        motifPanel.add(maxLabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        motifPanel.add(maxInput, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        motifPanel.add(saveAllBox, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridx = 2;
        c.gridy = 1;
        motifPanel.add(motifBtn, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 4;
        motifPanel.add(sep1, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 5;
		motifPanel.add(refLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 6;
		motifPanel.add(openBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 2;
		motifPanel.add(fileLabel, c);
		
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 7;
        motifPanel.add(sep, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 8;
		motifPanel.add(bigProgress, c);	
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 9;
		motifPanel.add(smallProgress, c);	
		return motifPanel;
	}
	


	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * @param progressBar The progress bar to update
	 * @param statusBar The status bar to update
	 * @param targetChooser The chooser for the target graph
	 * @return The Pattern Tab
	 */
	private JPanel buildSubgraphTab(JProgressBar progressBar, JPanel statusBar, JFileChooser targetChooser) {
		JLabel status = (JLabel) statusBar.getComponent(0);
		FastGraph subgraph = null;
		
		JPanel subgraphPanel = new JPanel(new GridBagLayout());
		
		JLabel subgraphLabel = new JLabel("Find subgraphs in main graph");		

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select subgraph directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
		JButton openBtn = new JButton("Open File...");
		
		JLabel fileLabel = new JLabel("No file selected  ");
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));
		
		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, fileChooser, subgraphPanel, openBtn));
		
		JButton addBtn = new JButton("Create");	
		addBtn.addActionListener(new CreateGraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new CreateGraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		
		JButton findBtn = new JButton("Find subgraphs");
		findBtn.addActionListener(new FindSubgraphsActionListener(targetChooser, progressBar, status, launcher, subgraphPanel, 
				fileChooser, subgraph));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		subgraphPanel.add(subgraphLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		subgraphPanel.add(openBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		subgraphPanel.add(fileLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		subgraphPanel.add(editBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		subgraphPanel.add(addBtn, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		subgraphPanel.add(sep, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		subgraphPanel.add(findBtn, c);
		
		return subgraphPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * @param progressBar The progress bar to update
	 * @param statusBar The status bar to update
	 * @param targetChooser The chooser for the target graph
	 * @return The Pattern Tab
	 */
	private JPanel buildApproximateSubgraphTab(JProgressBar progressBar, JPanel statusBar, JFileChooser targetChooser) {
		JLabel status = (JLabel) statusBar.getComponent(0);
		FastGraph subgraph = null;
		
		JPanel subgraphPanel = new JPanel(new GridBagLayout());
		
		JLabel subgraphLabel = new JLabel("Find subgraphs in main graph", SwingConstants.CENTER);		

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select subgraph directory");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
		JButton openBtn = new JButton("Open File...");
		
		JLabel fileLabel = new JLabel("No file selected  ");
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));
		
		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, fileChooser, subgraphPanel, openBtn));
		
		JButton addBtn = new JButton("Create");	
		addBtn.addActionListener(new CreateGraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new CreateGraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		
		JLabel tuningLabel = new JLabel("Tuning parameters:", SwingConstants.CENTER);			
		
		JLabel patternNodesLabel = new JLabel("Number of Nodes in Subgraphs:");
		JTextField patternNodesField = new JTextField(12);
		String patternNodesTip = "The number of nodes in generated subgraphs";
		patternNodesLabel.setToolTipText(patternNodesTip);
		patternNodesField.setToolTipText(patternNodesTip);
		
		JLabel subgraphsPerNodeLabel = new JLabel("Number of Subgraphs per Node:");
		JTextField subgraphsPerNodeField = new JTextField(12);
		String subgraphsPerNodeTip = "The number of subgraphs generated for each node in the target graph";
		subgraphsPerNodeLabel.setToolTipText(subgraphsPerNodeTip);
		subgraphsPerNodeField.setToolTipText(subgraphsPerNodeTip);
		
		JSeparator sep2 = new JSeparator(SwingConstants.HORIZONTAL);
		
		JButton findBtn = new JButton("Find subgraphs");
		findBtn.addActionListener(new FindApproximateSubgraphsActionListener(targetChooser, progressBar, status, launcher, subgraphPanel, 
				fileChooser, subgraph, patternNodesField, subgraphsPerNodeField, this));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		subgraphPanel.add(subgraphLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		subgraphPanel.add(openBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		subgraphPanel.add(fileLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		subgraphPanel.add(editBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		subgraphPanel.add(addBtn, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		subgraphPanel.add(sep, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		subgraphPanel.add(tuningLabel, c);		

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		subgraphPanel.add(patternNodesLabel, c);	
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		subgraphPanel.add(patternNodesField, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		subgraphPanel.add(subgraphsPerNodeLabel, c);	
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		subgraphPanel.add(subgraphsPerNodeField, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		subgraphPanel.add(sep2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		subgraphPanel.add(findBtn, c);
		
		return subgraphPanel;
	}

	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * @param progressBar The progress bar to update
	 * @param statusBar The status bar to update
	 * @return The Pattern Tab
	 */
	private JPanel buildConvertTab(JProgressBar progressBar, JPanel statusBar) {
		JLabel status = (JLabel) statusBar.getComponent(0);
		JLabel label = new JLabel("Convert from adjacency list to buffers");
		JLabel fileLabel = new JLabel("No file selected");
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));

		//Panel used to house the two buttons
		JPanel convertPanel = new JPanel(new GridBagLayout());

		JFileChooser fileChooser = new JFileChooser();
		JButton openBtn = new JButton("Open File...");

		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, fileChooser, convertPanel, openBtn));

		JLabel nodeLabel = new JLabel("Number of Nodes:");
		JTextField nodeField = new JTextField(12);

		JLabel edgeLabel = new JLabel("Number of Edges:");
		JTextField edgeField = new JTextField(12);

		JRadioButton undirected = new JRadioButton("Undirected");
		undirected.setSelected(true);
		JRadioButton directed = new JRadioButton("Directed");
		ButtonGroup group = new ButtonGroup();
		group.add(undirected);
		group.add(directed);

		JButton convertBtn = new JButton("Convert");

		//The action for when the user converts a file
		convertBtn.addActionListener(new ConvertGraphActionListener(launcher, this, nodeField, edgeField,
				fileChooser, directed, convertPanel, progressBar, status));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		convertPanel.add(label, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		convertPanel.add(openBtn, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		convertPanel.add(fileLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		convertPanel.add(nodeLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		convertPanel.add(nodeField, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		convertPanel.add(edgeLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		convertPanel.add(edgeField, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		convertPanel.add(undirected, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		convertPanel.add(directed, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		convertPanel.add(convertBtn, c);

		return convertPanel;
	}

	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 *
	 * @param graphList The JList element used to select which is the target graph
	 * @param progressBar The JProgressBar to update when loading a graph
	 * @param targetChooser The chooser for the target graph
	 * @return the Other tab
	 */
	private JPanel buildOtherTab(JProgressBar progressBar, JPanel statusBar, JFileChooser targetChooser) {
		JPanel otherPanel = new JPanel(new BorderLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);

		JButton selectedBtn = new JButton("Node Count");
		otherPanel.add(selectedBtn, BorderLayout.WEST);

		//The action for when the use selected the
		selectedBtn.addActionListener(new OtherActionListener(targetChooser, progressBar, status, launcher, otherPanel));

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
		JLabel statusLabel = new JLabel(DEFAULT_STATUS_MESSAGE);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		return statusPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Graph Edit Distance Tab
	 *
	 * @param graphList   The JList element used to select which is the target graph
	 * @param progressBar The JProgressBar to update when loading a graph
	 * @param targetChooser The chooser for the target graph
	 * @return the GED tab
	 */
	private JPanel buildGedTab(JProgressBar progressBar, JPanel statusBar, JFileChooser targetChooser) {

		JPanel gedPanel = new JPanel(new GridBagLayout());

		// Set the layout
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 2, 2, 2);

		JLabel graphOneLabel = new JLabel("Graph one:");

		c.gridx = 0;
		c.gridy = 0;
		gedPanel.add(graphOneLabel, c);

		JLabel graphTwoLabel = new JLabel("Graph two:");

		c.gridx = 1;
		c.gridy = 0;
		gedPanel.add(graphTwoLabel, c);

		JTextField graphOneTextField = new JTextField();
		graphOneTextField.setEditable(false);

		c.gridx = 0;
		c.gridy = 1;
		gedPanel.add(graphOneTextField, c);

		JTextField graphTwoTextField = new JTextField();
		graphTwoTextField.setEditable(false);

		c.gridx = 1;
		c.gridy = 1;
		gedPanel.add(graphTwoTextField, c);

		JButton selectGraphOne = new JButton("Set selected graph as graph one");

		c.gridx = 0;
		c.gridy = 2;
		gedPanel.add(selectGraphOne, c);

		JButton selectGraphTwo = new JButton("Set selected graph as graph two");

		c.gridx = 1;
		c.gridy = 2;
		gedPanel.add(selectGraphTwo, c);

		JButton getGedBtn = new JButton("Get GED of selected graphs");

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		gedPanel.add(getGedBtn, c);
		// Layout finished

		JLabel scoreLabel = new JLabel("GED score:");

		c.gridx=0;
		c.gridy=4;
		c.gridwidth=1;
		gedPanel.add(scoreLabel, c);

		JTextArea gedScore = new JTextArea("");

		c.gridx=1;
		c.gridy=4;
		c.gridwidth=1;
		gedPanel.add(gedScore, c);

		JLabel gedLastComputed = new JLabel("");

		c.gridx=0;
		c.gridy=5;
		c.gridwidth=2;
		gedPanel.add(gedLastComputed, c);

		// Set behaviour
		selectGraphOne.addActionListener(new GraphSelectedActionListener(ged1, targetChooser, graphOneTextField, this, 1));
		selectGraphTwo.addActionListener(new GraphSelectedActionListener(ged2, targetChooser, graphTwoTextField, this, 2));

		getGedBtn.addActionListener(new GedActionListener(launcher, this, gedScore, gedLastComputed));

		return gedPanel;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Graph Edit Distance Tab
	 *
	 * @param graphList   The JList element used to select which is the target graph
	 * @param progressBar The JProgressBar to update when loading a graph
	 * @param targetChooser The chooser for the target graph
	 * @return the GED tab
	 */
	private JPanel buildRandomGraphTab(JProgressBar progressBar, JPanel statusBar) {
		JPanel randomPanel = new JPanel(new GridBagLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);
		
		JLabel label = new JLabel("Generate Random Graph", SwingConstants.CENTER);
		JLabel fileLabel = new JLabel("No file selected");
		fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.ITALIC, fileLabel.getFont().getSize()));

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select graph directory to save");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JButton openBtn = new JButton("Open File...");

		//The action for when the user chooses a file
		openBtn.addActionListener(new LoadFileActionListener(status, fileLabel, fileChooser, randomPanel, openBtn));

		JLabel nodeLabel = new JLabel("Number of Nodes:");
		JTextField nodeField = new JTextField(12);

		JLabel edgeLabel = new JLabel("Number of Edges:");
		JTextField edgeField = new JTextField(12);
		
		JCheckBox simpleBox = new JCheckBox("Simple graph");
		simpleBox.setToolTipText("If checked, no duplicate or self-sourcing edges are created");
		simpleBox.setSelected(true);
		
		JCheckBox directedBox = new JCheckBox("Directed");
		directedBox.setToolTipText("If checked, graph will have directed edges");
		directedBox.setSelected(false);
		
		JButton buildGraphBtn = new JButton("Create Random Graph");
		buildGraphBtn.addActionListener(new CreateRandomGraphActionListener(fileChooser, randomPanel, this, 
				nodeField, edgeField, directedBox, simpleBox, launcher, progressBar, status));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		randomPanel.add(label, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		randomPanel.add(openBtn, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		randomPanel.add(fileLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		randomPanel.add(nodeLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		randomPanel.add(nodeField, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		randomPanel.add(edgeLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		randomPanel.add(edgeField, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		randomPanel.add(simpleBox, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		randomPanel.add(directedBox, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		randomPanel.add(buildGraphBtn, c);
		
		return randomPanel;
	}

	/**
	 * Checks that a String is a positive integer
	 * @param input The input String to be tested
	 * @param panel The JPanel to attach the error Popup
	 * @return The converted integer, or -1 if failed.
	 */
	public int checkForPositiveInteger(String input, JPanel panel) {
		try {
			int number = Util.checkForPositiveInteger(input);
			return number;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Please enter a positive integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			return -1;
		} 
	}

	/**
	 * @return the windowWidth
	 */
	public int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * @param windowWidth the windowWidth to set
	 */
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * @return the windowHeight
	 */
	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * @param windowHeight the windowHeight to set
	 */
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	/**
	 * @return the textHeight
	 */
	public double getTextHeight() {
		return textHeight;
	}

	/**
	 * @param textHeight the textHeight to set
	 */
	public void setTextHeight(double textHeight) {
		this.textHeight = textHeight;
	}

	/**
	 * @return the launcher
	 */
	public Launcher getLauncher() {
		return launcher;
	}

	/**
	 * Gets the first graph for GED comparison
	 * @return the first graph for GED comparison
	 */
	public File getGed1() {
		return ged1;
	}

	/**
	 * Gets the second graph for GED comparison
	 * @return the second graph for GED comparison
	 */
	public File getGed2() {
		return ged2;
	}

	/**
	 * Gets the first graph for GED comparison
	 * @param ged1 the ged1 to set
	 */
	public void setGed1(File ged1) {
		this.ged1 = ged1;
	}

	/**
	 * Gets the second graph for GED comparison
	 * @param ged2 the ged2 to set
	 */
	public void setGed2(File ged2) {
		this.ged2 = ged2;
	}
}
