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
public class GraphWindow extends JFrame implements ActionListener {

	protected GraphPanel gp = null;
	protected GraphWindow gw = null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = 600;
	protected int height = 600;

	
	private static final long serialVersionUID = 1L;

	public GraphWindow(Graph g) {
		super("Graph Editor "+g.getLabel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);
// this for convenience, as the program normally starts in graph/display
//		startDirectory = startDirectory.getParentFile();

		gw = this;
		
		gp = new GraphPanel(g,this);
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
	public Graph getGraph() {return gp.getGraph();}
/** Trival accessor. */
	public GraphPanel getGraphPanel() {return gp;}

	private void initView() {
		gp.addGraphView(new GraphViewShowNodeLabel(KeyEvent.VK_K, "Toggle Node Labels",KeyEvent.VK_K));
		gp.addGraphView(new GraphViewToggleEdgeLabelsAndDirection(KeyEvent.VK_V, "Toggle Edge Labels and Direction",KeyEvent.VK_V));
		gp.addGraphView(new GraphViewSeparateEdges(KeyEvent.VK_P, "Toggle Separate Parallel Edges",KeyEvent.VK_P));
	}

	private void initExperiment() {
// for finding good values for metric graph drawing
//		gp.addGraphExperiment(new GraphExperimentMetricValues(KeyEvent.VK_N,"Metric Spring Embedder Experiment",KeyEvent.VK_N));
// Generate edge length data
//		gp.addGraphExperiment(new GraphExperimentEdgeLengthData(KeyEvent.VK_V,"Generate Edge Length Data",KeyEvent.VK_V));
// Generate second bit of edge length data
//		gp.addGraphExperiment(new GraphExperimentEdgeLengthDataAfterSE(KeyEvent.VK_W,"Generate Edge Length After SE Data",KeyEvent.VK_W));
	}


	private void initUtility() {
		gp.addGraphUtility(new GraphUtilityRemoveDummyNodes(KeyEvent.VK_D,"Remove Dummy Nodes",KeyEvent.VK_D));
		gp.addGraphUtility(new GraphUtilityRandomizer(KeyEvent.VK_R,"Randomize Graph",KeyEvent.VK_R,50,50,500,500,true));
		gp.addGraphUtility(new GraphUtilityEdgeLabelRandomizer(KeyEvent.VK_W, "Randomize Edge Labels",0, 1.0, 5.0));
		gp.addGraphUtility(new GraphUtilityCreateRandomEulerGraph(KeyEvent.VK_C,"Create Random Euler Graph",0,50,150));
		gp.addGraphUtility(new GraphUtilityCreateRandomGraph(KeyEvent.VK_B,"Create Random Graph"));
		gp.addGraphUtility(new GraphUtilityDistanceStats());
		gp.addGraphUtility(new GraphUtilityReverseEdgeWeights(KeyEvent.VK_J, "Reverse All Edge Weights",KeyEvent.VK_J));
		gp.addGraphUtility(new GraphUtilitySnapToGrid(KeyEvent.VK_G, "Snap to Grid",KeyEvent.VK_G));
		gp.addGraphUtility(new GraphUtilityCountOcclusion(KeyEvent.VK_O, "Count Occlusion",KeyEvent.VK_O));
		gp.addGraphUtility(new GraphUtilityOccludeGraph(KeyEvent.VK_Z, "Occlude Graph",KeyEvent.VK_Z));
		gp.addGraphUtility(new GraphUtilityConnectivity(KeyEvent.VK_S, "Check Connectivity",KeyEvent.VK_S));
	}


	private void initLayout() {

// standard spring embedder
		
		
		gp.addGraphDrawer(new GraphDrawerSpringEmbedder(KeyEvent.VK_S,"Spring Embedder - no randomization",KeyEvent.VK_S,false));
// this version has the same constants as the edge length drawers
/*		GraphDrawerEdgeLength gdel = new GraphDrawerEdgeLength(KeyEvent.VK_E,"Spring Embedder - edge length version",KeyEvent.VK_E,true,true);
  		gp.addGraphDrawer(gdel);
*/
// metric spring embedder
		gp.addGraphDrawer(new GraphDrawerEdgeLength(KeyEvent.VK_M,"Metric Spring Embedder",KeyEvent.VK_M,false,true));

//		 standard spring embedder with randomization and no animation
/*		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
		se.setAnimateFlag(false);
		gp.addGraphDrawer(se);
*/
		// quick start spring embedder
		BasicSpringEmbedder bse = new BasicSpringEmbedder(KeyEvent.VK_S,"Quick Start Spring Embedder");
		gp.addGraphDrawer(bse);

		// attractive forces only
		//gp.addGraphDrawer(new GraphDrawerAttractive(KeyEvent.VK_A,"Attractive Spring Embedder Forces Only"));
		// Barycenter method
		gp.addGraphDrawer(new GraphDrawerBarycenter(KeyEvent.VK_B,"Barycenter - select some nodes"));
		// standard hierarchical algorithm
		gp.addGraphDrawer(new GraphDrawerHierarchical(KeyEvent.VK_H,"Hierarchical",KeyEvent.VK_H,true,false));
		// hierarchical algorithm with animation
		gp.addGraphDrawer(new GraphDrawerHierarchical(KeyEvent.VK_I,"Animate Hierarchical",0,true,true));
		// upside down hierarchical algorithm
		gp.addGraphDrawer(new GraphDrawerHierarchical(KeyEvent.VK_U,"Upside Down Hierarchical",KeyEvent.VK_U,false));
		// edge length method
		gp.addGraphDrawer(new GraphDrawerEdgeLength(KeyEvent.VK_E, "Even Edge Length", KeyEvent.VK_E,true,true));
		// edge length heuristic only
		gp.addGraphDrawer(new GraphDrawerEdgeLength(KeyEvent.VK_L, "Even Edge Length - no spring embedding", KeyEvent.VK_L,true,false));
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
		JMenuItem fileSVGItem = new JMenuItem("Export to SVG");
		fileMenu.add(fileSVGItem);

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
		fileSVGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveSVG();
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
// no experiments at the moment
/*		JMenu experimentsMenu = new JMenu("Experiments");
        experimentsMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(experimentsMenu);

		for(GraphExperiment ge : gp.getGraphExperimentList()) {
	        JMenuItem menuItem = new JMenuItem(ge.getMenuText(),ge.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(ge.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			experimentsMenu.add(menuItem);
		}
*/

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
		getGraph().clear();
		gp.update(gp.getGraphics());
	}

	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			getGraph().clear();
			currentFile = chooser.getSelectedFile();
			getGraph().loadAll(currentFile);
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
			
		int returnVal = chooser.showOpenDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			getGraph().clear();
			currentFile = chooser.getSelectedFile();
			getGraph().loadAdjacencyFile(currentFile.getAbsolutePath());
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
			
		int returnVal = chooser.showOpenDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			getGraph().clear();
			currentFile = chooser.getSelectedFile();
			getGraph().loadWeightedAdjacencyFile(currentFile.getAbsolutePath());
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
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
				getGraph().saveAll(currentFile);
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
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			getGraph().saveAll(currentFile);
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
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			getGraph().saveSimple(currentFile);
		}
	}
	
	protected void fileSaveSVG(){
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			//generalXML.saveGraph(currentFile);
			ExportSVG export = new ExportSVG(graph);
			export.saveGraph(currentFile);
			
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
		return;
/*		JFileChooser chooser = null;
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
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File pngFile = chooser.getSelectedFile();

			int maxX = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;

			while(Node node : getNodes()) {
				if(node.getX() > maxX) {
					maxX = node.getX();
				}
				if(node.getX() < minX) {
					minX = node.getX();
				}
				if(node.getY() > maxY) {
					maxY = node.getY();
				}
				if(node.getY() < minY) {
					minY = node.getY();
				}
			}

			BufferedImage image = new BufferedImage(maxX-minX+50,maxY-minY+50,BufferedImage.TYPE_INT_RGB);



			ImageIO.write(image,"png",pngFile);
		}
*/	}


	protected void fileExit() {
		System.exit(0);
	}


	protected void editSelectAll() {
		gp.getSelection().addNodes(getGraph().getNodes());
		gp.getSelection().addEdges(getGraph().getEdges());
		gp.repaint();
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



