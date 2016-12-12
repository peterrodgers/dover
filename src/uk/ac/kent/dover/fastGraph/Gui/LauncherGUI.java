package uk.ac.kent.dover.fastGraph.Gui;

import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.GedUtil;
import uk.ac.kent.dover.fastGraph.Launcher;
import uk.ac.kent.dover.fastGraph.Util;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

	private Launcher launcher;
	private DefaultListModel model = new DefaultListModel();
	private double screenWidth; //size of the user's screen
	private double screenHeight;
	private int windowWidth, windowHeight;	
	private double textHeight;


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

		JPanel motifPanel = buildMotifTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Motif", motifPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel subgraphPanel = buildSubgraphTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Subgraph", subgraphPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

		JPanel convertPanel = buildConvertTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Convert Graph", convertPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_3);

		JPanel otherPanel = buildOtherTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Others", otherPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_4);

		JPanel gedPanel = buildGedTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("GED", gedPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_5);

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
		
		windowWidth = (int) Math.round(screenHeight/2);
		windowHeight = (int) Math.round(screenHeight/2);
		mainPanel.setPreferredSize(new Dimension(windowWidth,windowHeight)); //makes a square window
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ClosingWindowListener(false,this,this,(JLabel) statusBar.getComponent(0)));
		pack();
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
		exit.addActionListener(new ExitActionListener());
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
	    
	    for(String s : graphs) {
	    	model.addElement(s);
	    }		
		JList list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		return list;
	}
	
	/**
	 * Builds the Panel used to house the GUI elements for the Motif Tab
	 * @param progressBar The main progress bar
	 * @return The Motif Tab
	 */
	private JPanel buildMotifTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {
		JPanel motifPanel = new JPanel(new GridBagLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);

		JLabel infoLabel = new JLabel("Find and export motifs", SwingConstants.CENTER);
		JLabel minLabel = new JLabel("Min Size: ");
		JLabel maxLabel = new JLabel("Max Size: ");
		JTextField minInput = new JTextField(3);
		JTextField maxInput = new JTextField(3);
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

		//add an action when the user clickes this button
		//in an external class, so needs lots(!) of parameters
		motifBtn.addActionListener(new MotifActionListener(launcher, this, minInput, maxInput, motifPanel,
				bigProgress, smallProgress, progressBar, status, graphList));

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
        c.gridheight = 2;
        c.gridx = 2;
        c.gridy = 1;
        motifPanel.add(motifBtn, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 3;
        motifPanel.add(sep, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 4;
		motifPanel.add(bigProgress, c);	
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 5;
		motifPanel.add(smallProgress, c);	
		return motifPanel;
	}
	


	/**
	 * Builds the Panel used to house the GUI elements for the Pattern Tab
	 * @return The Pattern Tab
	 */
	private JPanel buildSubgraphTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {
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
		addBtn.addActionListener(new CreateSubgraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new CreateSubgraphActionListener(this, fileChooser, subgraphPanel, subgraph, fileLabel, status));
		
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		
		JButton findBtn = new JButton("Find subgraphs");
		findBtn.addActionListener(new FindSubgraphsActionListener(graphList, progressBar, status, launcher, subgraphPanel, 
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
	 * @return The Pattern Tab
	 */
	private JPanel buildConvertTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {
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
				fileChooser, directed, model, convertPanel, progressBar, status));

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
	 * @return the Other tab
	 */
	private JPanel buildOtherTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {
		JPanel otherPanel = new JPanel(new BorderLayout());
		JLabel status = (JLabel) statusBar.getComponent(0);

		JButton selectedBtn = new JButton("Node Count");
		otherPanel.add(selectedBtn, BorderLayout.WEST);

		//The action for when the use selected the
		selectedBtn.addActionListener(new OtherActionListener(graphList, progressBar, status, launcher, otherPanel));

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
	 * Builds the Panel used to house the GUI elements for the Graph Edit Distance Tab
	 *
	 * @param graphList   The JList element used to select which is the target graph
	 * @param progressBar The JProgressBar to update when loading a graph
	 * @return the GED tab
	 */
	private JPanel buildGedTab(JList graphList, JProgressBar progressBar, JPanel statusBar) {

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

		// Set behaviour
		selectGraphOne.addActionListener(new GraphSelectedActionListener(graphList,graphOneTextField));
		selectGraphTwo.addActionListener(new GraphSelectedActionListener(graphList,graphTwoTextField));

		getGedBtn.addActionListener(new GedActionListener(graphOneTextField,graphTwoTextField,launcher));

		return gedPanel;
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
}
