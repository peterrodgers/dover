package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import uk.ac.kent.displayGraph.drawers.GraphDrawerSpringEmbedder;
import uk.ac.kent.dover.fastGraph.FastGraph;

/**
 * Handles the creation and editing of displaygraphs to generate subgraphs
 * 
 * @author Rob Baker
 *
 */
public class CreateSubgraphActionListener implements ActionListener{

	private LauncherGUI launcherGui;
	private JFrame graphFrame;
	private JFileChooser fileChooser;
	private JPanel panel;
	private File currentFile;
	private uk.ac.kent.displayGraph.Graph dg;
	private FastGraph subgraph;
	private JLabel fileLabel, status;
	private boolean saved = false;
	private uk.ac.kent.displayGraph.GraphPanel gp;

	/**
	 *  Trivial constructor
	 * @param launcherGui The GUI this was launched from - for callback commands
	 * @param fileChooser The file Chooser the user may have selected a subgraph from
	 * @param panel The panel to attach error messages
	 * @param subgraph The subgraph that's been created - saves loading twice
	 * @param fileLabel The label which says the selected subgraph - for updating
	 * @param status The status bar on the GUI to be updated
	 */
	public CreateSubgraphActionListener(LauncherGUI launcherGui, JFileChooser fileChooser, JPanel panel, 
			FastGraph subgraph, JLabel fileLabel, JLabel status) {
		this.launcherGui = launcherGui;
		this.panel = panel;
		this.fileChooser = fileChooser;
		this.subgraph = subgraph;
		this.fileLabel = fileLabel;
		this.status = status;
	}

	
	@Override
	/**
	 * Loads the DG from the given file (if there is one), and generates a window for the user to edit the graph in
	 */
	public void actionPerformed(ActionEvent arg0) {
		currentFile = fileChooser.getSelectedFile();

		try {
			status.setText("Use Graph Window");
			dg = loadAndConvertFile(currentFile);
			buildFrame(dg);
			//load DG into frame
			initMenu();
			graphFrame.setVisible(true);
		} catch (IOException e) {
			status.setText(launcherGui.DEFAULT_STATUS_MESSAGE);
			JOptionPane.showMessageDialog(panel, "This buffer could not be found. \n" + e.getMessage(), 
					"Error: IOException", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads a display grpah from a given file. If none is given, then loads a new display graph
	 * @param file The file to load
	 * @return A display graph from the given file, or a new one
	 * @throws IOException If the buffers cannot be loaded
	 */
	private uk.ac.kent.displayGraph.Graph loadAndConvertFile(File file) throws IOException {
		if (file != null) {
			String directory = file.getPath();
			String name = file.getName();
			FastGraph g = launcherGui.getLauncher().loadFromBuffers(directory, name);
			System.out.println("num nodes" + g.getNumberOfNodes());
			uk.ac.kent.displayGraph.Graph dg = g.generateDisplayGraph();
			dg.randomizeNodePoints(new Point(20,20),300,300);
			return dg;
		} else {
			return new uk.ac.kent.displayGraph.Graph();
		}
	}
	
	/**
	 * Builds a JFrame without all the controls of the standard Display Graph software
	 * @param dg The display graph to show in this frame
	 */
	private void buildFrame(uk.ac.kent.displayGraph.Graph dg) {
		graphFrame = new JFrame();
		gp = new uk.ac.kent.displayGraph.GraphPanel(dg, graphFrame);
		graphFrame.setContentPane(gp);
		graphFrame.setTitle("Create/Edit a subgraph");
		graphFrame.setSize(launcherGui.getWidth(), launcherGui.getHeight());
		graphFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - graphFrame.getSize().width)/2;
		int posY = (frameDim.height - graphFrame.getSize().height)/2;
		graphFrame.setLocation(posX, posY);
		
		graphFrame.addWindowListener(new ClosingWindowListener(saved,launcherGui,graphFrame,status));
		
	}
	
	/**
	 * Builds the toolbars
	 */
	private void initMenu() {
		
		JMenuBar menuBar = new JMenuBar();

		graphFrame.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Graph");

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
	
		JMenuItem fileSaveItem = new JMenuItem("Save",KeyEvent.VK_S);
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});
		fileMenu.add(fileSaveItem);
		
		JMenuItem drawItem = new JMenuItem("Layout with Spring Embedder",KeyEvent.VK_L);
		drawItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				draw(false);
			}
		});
		fileMenu.add(drawItem);
		
		JMenuItem drawAnimatedItem = new JMenuItem("Layout with Spring Embedder (Animated)",KeyEvent.VK_K);
		drawAnimatedItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				draw(true);
			}
		});
		fileMenu.add(drawAnimatedItem);
	}

	/**
	 * Saves a display graph as a FastGraph buffers to disk. Also updates GUI displays
	 */
	protected void fileSaveAs() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(new File("."));
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			saved = true;
			currentFile = chooser.getSelectedFile();
			//dg.saveAll(currentFile);
			FastGraph g = FastGraph.displayGraphFactory(dg, false);
			g.setName(currentFile.getName());
			currentFile.mkdir();
			g.saveBuffers(currentFile.getPath(), currentFile.getName());
			subgraph = g;
			fileLabel.setText(currentFile.getName());
            fileLabel.setFont(new Font(fileLabel.getFont().getFontName(), Font.PLAIN, fileLabel.getFont().getSize()));
			fileChooser.setSelectedFile(currentFile);
			graphFrame.setVisible(false);
			graphFrame.dispose();
		}
	}
	
	/**
	 * Draws the graph using a spring embedder
	 * 
	 * @param animated If the user whish to animate the drawing
	 */
	protected void draw(boolean animated) {
		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_S,"Spring Embedder - no randomization",KeyEvent.VK_S,false);
		se.setAnimateFlag(animated);
		se.setIterations(100);
		se.setTimeLimit(200);
		se.setGraphPanel(gp);
		se.layout();
	}

}
