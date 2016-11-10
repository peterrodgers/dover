package uk.ac.kent.dover.fastGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
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
	
	private final String DEFAULT_STATUS_MESSAGE = "Ready"; //The default message displayed to a user
	
	private Launcher launcher;
	private DefaultListModel model = new DefaultListModel();
	private double screenWidth; //size of the user's screen
	private double screenHeight;
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
		
		JPanel motifPanel = buildMotifTab();
		tabbedPane.addTab("Motif", motifPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JPanel patternPanel = buildPatternTab();
		tabbedPane.addTab("Pattern", patternPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);
		
		JPanel convertPanel = buildConvertTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Convert Graph", convertPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_3);

		JPanel otherPanel = buildOtherTab(graphList, progressBar, statusBar);
		tabbedPane.addTab("Others", otherPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_4);
		
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
		mainPanel.setPreferredSize(new Dimension((int) Math.round(screenHeight/2),(int) Math.round(screenHeight/2))); //makes a square window
		pack();
		setVisible(true);
	}
	
	
	private JMenuBar buildMenuBar(JPanel panel) {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.getAccessibleContext().setAccessibleDescription("Exit the program");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);	
			}
		});
		fileMenu.add(exit);
		
		menuBar.add(fileMenu);
		
		JMenu dataMenu = new JMenu("Data");
		
		JMenuItem data = new JMenuItem("Get More Data");
		data.getAccessibleContext().setAccessibleDescription("Allows the user to get more data");
		data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
		        try {
		            Desktop.getDesktop().browse(new URI(launcher.DATA_URL));
		        } catch(Exception e) {
		        	JOptionPane.showMessageDialog(panel, "Browser loading is not supported. \nInstead, please visit:\n" + launcher.DATA_URL, "Browser Loading not supported", JOptionPane.WARNING_MESSAGE);
		            e.printStackTrace();
		        }
			}
		});
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
	 * @return The Motif Tab
	 */
	private JPanel buildMotifTab() {
		JPanel motifPanel = new JPanel(new BorderLayout());
		
		JTextField input = new JTextField(5);		
		JButton motifBtn = new JButton("Find Motifs");
		
		motifBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				int number = checkForPositiveInteger(input.getText(),motifPanel);
				if (number != -1) {
					
					//replace with actual motif code
					System.out.println("Motif search, with number: " + number);
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
		openBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				status.setText("Waiting for user response");
			    //Handle open button action.
			    if (evt.getSource() == openBtn) {
			        int returnVal = fileChooser.showOpenDialog(convertPanel);
	
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fileChooser.getSelectedFile();
			            fileLabel.setText(file.getName());
			            fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.PLAIN, fileLabel.getFont().getSize()));
			        }
			   }
			   status.setText(DEFAULT_STATUS_MESSAGE);
			}
		});
		
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
		convertBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//check that the numbers of nodes are valid
				
				int nodeNumber = checkForPositiveInteger(nodeField.getText(),convertPanel);
				int edgeNumber = checkForPositiveInteger(edgeField.getText(),convertPanel);
				if (nodeNumber != -1 && edgeNumber != -1) {
					//input numbers are valid
					
					//if the undirected button is selected, then the graph is undirected
					boolean directedGraph = directed.isSelected();
					File graphFile = fileChooser.getSelectedFile();
					String name = graphFile.getName();
					String path = fileChooser.getCurrentDirectory().toString();
					
					System.out.println(fileChooser.getSelectedFile());
					System.out.println("path: " + fileChooser.getCurrentDirectory());
					System.out.println("node: " + nodeNumber);
					System.out.println("edge: " + edgeNumber);
					System.out.println("directed: " + directedGraph);
					
					//set the Progress Bar to move
					progressBar.setIndeterminate(true);
					status.setText("Converting...");

					// Start converting the Graph, but in a separate thread, to avoid locking up the GUI
					Thread thread = new Thread(new Runnable() {

					    @Override
					    public void run() {
					    	
					    	//Display error message to the user if this is unavailable
							try {
								
								launcher.convertGraphToBuffers(nodeNumber, edgeNumber, path, name, directedGraph);
								
								status.setText("Conversion Complete");
								model.addElement(name);
						    	//stop the Progress Bar
						    	progressBar.setIndeterminate(false);
							} catch (Exception e) {
								//stop the Progress Bar
								progressBar.setIndeterminate(false);
								JOptionPane.showMessageDialog(convertPanel, "This buffer could not be built. \n" + e.getMessage(), "Error: Exception", JOptionPane.ERROR_MESSAGE);
								
								e.printStackTrace();
							}

					    }
					            
					});					        
					thread.start();
					
				}
			}
		});
		
		GridBagConstraints c = new GridBagConstraints();
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
					    	
					    	//Display error message to the user if this is unavailable
							try {
								FastGraph g = launcher.loadFromBuffers(null,graph);
								status.setText("Loading Complete");
						    	System.out.println("Maximum Degree: " + g.maximumDegree());
						    	System.out.println("Degree Counts: " + Arrays.toString(g.countInstancesOfNodeDegrees(5)));
						    	
						    	//stop the Progress Bar
						    	progressBar.setIndeterminate(false);
							} catch (IOException e) {
								//stop the Progress Bar
								progressBar.setIndeterminate(false);
								JOptionPane.showMessageDialog(otherPanel, "This buffer could not be found. \n" + e.getMessage(), "Error: IOException", JOptionPane.ERROR_MESSAGE);
								
								e.printStackTrace();
							}

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
	private int checkForPositiveInteger(String input, JPanel panel) {
		try {
			int number = Util.checkForPositiveInteger(input);
			return number;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(panel, "Please enter a positive integer", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			return -1;
		} 
	}

}
