package uk.ac.kent.displayGraph.display;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import uk.ac.kent.displayGraph.*;
import uk.ac.kent.displayGraph.drawers.*;
import uk.ac.kent.displayGraph.experiments.*;
import uk.ac.kent.displayGraph.utilities.*;
import uk.ac.kent.displayGraph.views.*;


/** Graph layout window using GraphPanel */
public class GraphFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	Graph graph = null;
	GraphPanel gp = null;
	GraphFrame gf = null;
	File currentFile = null;
	File startDirectory;
	
	int width = 600;
	int height = 600;

	public GraphFrame(Graph g) {
		
		super("Graph Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);

		graph = g;
		gf = this;
		gp = new GraphPanel(graph,this);
		getContentPane().add(gp);
		initView();
		initExperiment();
		initUtility();
		initLayout();
		initMenu();
		setSize(width,height);
		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);
		setVisible(true);
		gp.requestFocus();
	}


/** Trival accessor. */
	public Graph getGraph() {return graph;}
/** Trival accessor. */
	public GraphPanel getGraphPanel() {return gp;}

	private void initView() {
		gp.addGraphView(new GraphViewSeparateEdges(KeyEvent.VK_P, "Toggle Separate Parallel Edges",KeyEvent.VK_P));
	}

	private void initExperiment() {
	}


	private void initUtility() {
		gp.addGraphUtility(new GraphUtilityRandomizer(KeyEvent.VK_R,"Randomize Graph",KeyEvent.VK_R,50,50,500,500,true));
		gp.addGraphUtility(new GraphUtilityCreateRandomGraph(KeyEvent.VK_B,"Create Random Graph"));
	}


	private void initLayout() {
		gp.addGraphDrawer(new GraphDrawerSpringEmbedder(KeyEvent.VK_S,"Spring Embedder - no randomization",KeyEvent.VK_S,false));
	}

	private void initMenu() {	
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

// File Menu
		JMenu fileMenu = new JMenu("File");

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
	
		JMenuItem fileNewItem = new JMenuItem("New",KeyEvent.VK_N);
		fileNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(fileNewItem);

		JMenuItem fileOpenItem = new JMenuItem("Open...",KeyEvent.VK_O);
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(fileOpenItem);

		JMenuItem fileOpenAdjacencyItem = new JMenuItem("Open Adjacency File...");
		fileMenu.add(fileOpenAdjacencyItem);

		JMenuItem fileOpenWeightedAdjacencyItem = new JMenuItem("Open Weighted Adjacency File...");
		fileMenu.add(fileOpenWeightedAdjacencyItem);

		JMenuItem fileOpenXMLItem = new JMenuItem("Open XML File...");
		fileMenu.add(fileOpenXMLItem);

		JMenuItem fileSaveItem = new JMenuItem("Save",KeyEvent.VK_S);
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveItem);

		JMenuItem fileSaveAsItem = new JMenuItem("Save As...");
		fileMenu.add(fileSaveAsItem);

		JMenuItem fileSaveSimpleItem = new JMenuItem("Save Simple Graph...");
		fileMenu.add(fileSaveSimpleItem);

		JMenuItem fileSaveXMLItem = new JMenuItem("Save XML File...");
		fileMenu.add(fileSaveXMLItem);

		JMenuItem filePNGItem = new JMenuItem("Export to png");
		fileMenu.add(filePNGItem);

		JMenuItem fileExitItem = new JMenuItem("Exit",KeyEvent.VK_X);
		fileExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		fileMenu.add(fileExitItem);

		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileExit();
			}
		});
		
		fileNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileNew();
			}
		});

		fileOpenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpen();
			}
		});

		fileOpenAdjacencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenAdjacency();
			}
		});

		fileOpenWeightedAdjacencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenWeightedAdjacency();
			}
		});


		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSave();
			}
		});

		fileSaveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});

		fileSaveSimpleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveSimple();
			}
		});

		filePNGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filePNG();
			}
		});

// Edit Menu
		JMenu editMenu = new JMenu("Edit");

		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
	
		JMenuItem editNodesItem = new JMenuItem("Edit Selected Nodes...",KeyEvent.VK_N);
		editNodesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
		editMenu.add(editNodesItem);

		JMenuItem editEdgesItem = new JMenuItem("Edit Selected Edges...",KeyEvent.VK_E);
		editEdgesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
		editMenu.add(editEdgesItem);

		JMenuItem editEdgeTypesItem = new JMenuItem("Edit Edge Types...");
		editMenu.add(editEdgeTypesItem);

		JMenuItem editNodeTypesItem = new JMenuItem("Edit Node Types...");
		editMenu.add(editNodeTypesItem);

		JMenuItem editMoveGraphItem = new JMenuItem("Move Graph...");
		editMenu.add(editMoveGraphItem);

		JMenuItem editAddEdgeBendItem = new JMenuItem("Add Edge Bend");
		editMenu.add(editAddEdgeBendItem);

		JMenuItem editRemoveEdgeBendsItem = new JMenuItem("Remove Edge Bends");
		editMenu.add(editRemoveEdgeBendsItem);

		JMenuItem editSelectAllItem = new JMenuItem("Select All",KeyEvent.VK_A);
		editSelectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		editMenu.add(editSelectAllItem);

		editNodesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editNodes(gp.getSelection().getNodes());
			}
		});
		
		editEdgesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editEdges(gp.getSelection().getEdges());
			}
		});

		editEdgeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editEdgeTypes();
			}
		});

		editNodeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.editNodeTypes();
			}
		});

		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.moveGraph();
			}
		});

		editAddEdgeBendItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.addEdgeBend();
			}
		});

		editRemoveEdgeBendsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gp.removeEdgeBends();
			}
		});

		editSelectAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editSelectAll();
			}
		});


// View Menu
		JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(viewMenu);

		for(GraphView v : gp.getGraphViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			viewMenu.add(menuItem);
		}

// Experiment Menu
		JMenu experimentsMenu = new JMenu("Experiments");
        experimentsMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(experimentsMenu);

		for(GraphExperiment ge : gp.getGraphExperimentList()) {
	        JMenuItem menuItem = new JMenuItem(ge.getMenuText(),ge.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(ge.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			experimentsMenu.add(menuItem);
		}


// Utilities Menu
		JMenu utilitiesMenu = new JMenu("Utilities");
        utilitiesMenu.setMnemonic(KeyEvent.VK_U);
		menuBar.add(utilitiesMenu);

		for(GraphUtility u : gp.getGraphUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			utilitiesMenu.add(menuItem);
		}


		JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(layoutMenu);

		for(GraphDrawer d : gp.getGraphDrawerList()) {
	        JMenuItem menuItem = new JMenuItem(d.getMenuText(), d.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(d.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			layoutMenu.add(menuItem);
		}
		
	}

	protected void fileNew() {
		if (currentFile != null) {
			if (!currentFile.isDirectory()) {
				currentFile = currentFile.getParentFile();
			}
		}
		graph.clear();
		gp.update(gp.getGraphics());
	}

	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			graph.clear();
			currentFile = chooser.getSelectedFile();
			graph.loadAll(currentFile);
			gp.update(gp.getGraphics());
		}
	}
	
	protected void fileOpenAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			graph.clear();
			currentFile = chooser.getSelectedFile();
			graph.loadAdjacencyFile(currentFile.getAbsolutePath());
			gp.update(gp.getGraphics());
		}
	}


	protected void fileOpenWeightedAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			graph.clear();
			currentFile = chooser.getSelectedFile();
			graph.loadWeightedAdjacencyFile(currentFile.getAbsolutePath());
			graph.randomizeNodePoints(new Point(50,50),400,400);
			gp.update(gp.getGraphics());
		}
	}



	protected void fileSave() {
		if (currentFile == null) {
			fileSaveAs();
		} else {
			if (currentFile.isDirectory()) {
				fileSaveAs();
			} else {
				graph.saveAll(currentFile);
				gp.update(gp.getGraphics());
			}
		}
	}


	protected void fileSaveAs() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			graph.saveAll(currentFile);
		}
	}


	protected void fileSaveSimple() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			graph.saveSimple(currentFile);
		}
	}




	protected void filePNG() {

		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			pngFile = new File(currentFile.getName()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}

		if (pngFile == null)
			return;
		try {
			BufferedImage image = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
			paint(image.getGraphics());
			ImageIO.write(image,"png",pngFile);
		} catch (Exception e) {}
	}


	protected void fileExit() {
		System.exit(0);
	}


	protected void editSelectAll() {
		gp.getSelection().addNodes(graph.getNodes());
		gp.getSelection().addEdges(graph.getEdges());
		//gp.repaint();
	}


	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(GraphView v : gp.getGraphViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}

		for(GraphDrawer d : gp.getGraphDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(GraphUtility u : gp.getGraphUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.apply();
				repaint();
				return;
			}
		}

		for(GraphExperiment ge : gp.getGraphExperimentList()) {
			if (ge.getMenuText().equals(source.getText())) {
				ge.experiment();
				repaint();
				return;
			}
		}
	}

}



