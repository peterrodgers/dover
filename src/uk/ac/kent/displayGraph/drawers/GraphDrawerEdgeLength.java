package uk.ac.kent.displayGraph.drawers;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;


import uk.ac.kent.displayGraph.*;


/**
 * A version of Eades spring embedder for laying out graphs
 * Selected nodes are not moved, but still participate in the
 * force calculation
 *
 * @author Peter Rodgers
 */

public class GraphDrawerEdgeLength extends GraphDrawer implements ActionListener {

/** The strength of a spring */
	protected double k = 0.0005;
/** The strength of the repulsive force */
	protected double r = 10;
/** The amount of movement on each iteration */
	protected double f = 1.0;
/** The ideal edge length for the edge length operation */
	protected double idealLength = 300.0;
/** The number of iterations */
	// protected int iterations = Integer.MAX_VALUE;
	protected int iterations = 100;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 5000;
/** The amount of max force below which the algorithm stops*/
	protected double forceThreshold = 0.01;
/** The maximum spring embedder force applied on one iteration */
	protected double maxSpringForce = Double.MAX_VALUE;
/** The node buffer. This holds copies of node locations */
	protected DrawCoordCollection nodeBuffer = new DrawCoordCollection();
/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
/** Set to output time taken */
	protected boolean timerFlag = true;
/** Set to display variable dialog before laying out */
	protected boolean dialogFlag = true;
/** The nodes selected at the start of the algorithm */
	protected ArrayList<Node> selectedNodes = null;
/** Gives the delay on each iteration */
	protected long delay = 0;
/** Used to detect termination */
	protected ArrayList<Point.Double> oldEdgeLengthCentres = new ArrayList<Point.Double>();
/** Used to detect termination */
	protected ArrayList<Point.Double> newEdgeLengthCentres = new ArrayList<Point.Double>();

/** set this to include the edge length operator when drawing */
	protected boolean includeEdgeLength = true;
/** set this to include the spring embedder when drawing */
	protected boolean includeSpringEmbedder = true;
/** set this to make the ideal edge length multiply by edge label */
	protected boolean scaleEdgeLength = true;
/** set this to reduce attractive force as edge label increases */
	protected boolean scaleAttractive = true;
/** set this to ensure all edge weights are between 0 and 1 */
	protected boolean scaleWeightsToUnitary = true;
/**
 * This defines the frequency of edge length operation. A value of 1 sets
 * the edge length operation for every iteration, 2 sets it for every
 * other iteration, etc.
 */
	protected int edgeLengthFrequency = 1;

	JFrame frame;
	JPanel panel;
	JTextField kField;
	JTextField rField;
	JTextField fField;
	JTextField iterationField;

	JTextField idealLengthField;
	JTextField includeEdgeLengthField;
	JTextField edgeLengthFrequencyField;
	JTextField includeSpringEmbedderField;
	JTextField scaleEdgeLengthField;
	JTextField scaleAttractiveField;
	JTextField scaleWeightsToUnitaryField;
	JTextField animateField;
	JTextField timerField;
	JTextField delayField;

	JButton okButton;


/** Trivial constructor. */
	public GraphDrawerEdgeLength() {
		super(KeyEvent.VK_E,"Edge Length",KeyEvent.VK_E);
	}

/** Trivial constructor. */
	public GraphDrawerEdgeLength(int key, String s, int accelerator) {
		super(key,s,accelerator);
	}

/** Trivial constructor. */
	public GraphDrawerEdgeLength(int key, String s, int accelerator, boolean inIncludeEdgeLength, boolean inIncludeSpringEmbedder) {
		super(key,s,accelerator);
		includeEdgeLength = inIncludeEdgeLength;
		includeSpringEmbedder = inIncludeSpringEmbedder;
	}

/** Trival accessor. */
	public double getK() {return k;}
/** Trival accessor. */
	public double getR() {return r;}
/** Trival accessor. */
	public double getF() {return f;}
/** Trival accessor. */
	public double getIdealLength() {return idealLength;}
/** Trival accessor. */
	public int getIterations() {return iterations;}
/** Trival accessor. */
	public long getTimeLimit() {return timeLimit;}
/** Trival accessor. */
	public boolean getAnimateFlag() {return animateFlag;}
/** Trival accessor. */
	public boolean getTimerFlag() {return timerFlag;}
/** Trival accessor. */
	public boolean getDialogFlag() {return dialogFlag;}
/** Trival accessor. */
	public boolean getScaleEdgeLength() {return scaleEdgeLength;}
/** Trival accessor. */
	public boolean getScaleAttractive() {return scaleAttractive;}
/** Trival accessor. */
	public boolean getScaleWeightsToUnitary() {return scaleWeightsToUnitary;}
/** Trival accessor. */
	public int getEdgeLengthFrequency() {return edgeLengthFrequency;}
/** Trival accessor. */
	public boolean getIncludeEdgeLength() {return includeEdgeLength;}
/** Trival accessor. */
	public boolean getIncludeSpringEmbedder() {return includeSpringEmbedder;}

/** Trivial modifier. */
	public void setK(double inK) {k = inK;}
/** Trivial modifier. */
	public void setR(double inR) {r = inR;}
/** Trivial modifier. */
	public void setF(double inF) {f = inF;}
/** Trivial modifier. */
	public void setIdealLength(double inIdealLength) {idealLength = inIdealLength;}
/** Trivial modifier. */
	public void setIterations(int inIterations) {iterations = inIterations;}
/** Trivial modifier. */
	public void setTimeLimit(int inLimit) {timeLimit = inLimit;}
/** Trivial modifier. */
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
/** Trivial modifier. */
	public void setTimerFlag(boolean inTimerFlag) {timerFlag = inTimerFlag;}
/** Trivial modifier. */
	public void setDialogFlag(boolean inDialogFlag) {dialogFlag = inDialogFlag;}
/** Trivial modifier. */
	public void setScaleEdgeLength(boolean inFlag) {scaleEdgeLength = inFlag;}
/** Trivial modifier. */
	public void setScaleAttractive(boolean inFlag) {scaleAttractive = inFlag;}
/** Trivial modifier. */
	public void setScaleWeightsToUnitary(boolean inFlag) {scaleWeightsToUnitary = inFlag;}
/** Trivial modifier. */
	public void setEdgeLengthFrequency(int inF) {edgeLengthFrequency = inF;}
/** Trivial modifier. */
	public void setIncludeEdgeLength(boolean inFlag) {includeEdgeLength = inFlag;}
/** Trivial modifier. */
	public void setIncludeSpringEmbedder(boolean inFlag) {includeSpringEmbedder = inFlag;}



/** Draws the graph. */
	public void layout() {

		if(dialogFlag) {
			createFrame();
		} else {
			drawGraph();
			getGraphPanel().update(getGraphPanel().getGraphics());
		}
	}

/**
 * Draws the graph. This stuf has been moved from
 * the layout method to allow an option frame to appear.
 */
	public void drawGraph() {

		selectedNodes = getGraphPanel().getSelection().getNodes();

		setEdgeLabelsToWeights(getGraph());

		double edgeWeightFactor = 1.0;
		if(scaleWeightsToUnitary) {
			edgeWeightFactor = Double.NEGATIVE_INFINITY;
			for(Edge e : getGraph().getEdges()) {
				if(e.getWeight() > edgeWeightFactor) {
					edgeWeightFactor = e.getWeight();
				}
			}
			for(Edge e : getGraph().getEdges()) {
				e.setWeight(e.getWeight()/edgeWeightFactor);
			}
		}

		oldEdgeLengthCentres = new ArrayList<Point.Double>();
		newEdgeLengthCentres = new ArrayList<Point.Double>();
			
		maxSpringForce = Double.POSITIVE_INFINITY;
		nodeBuffer.setUpNodes(getGraph().getNodes());
		int i = 0;
		long startTime = System.currentTimeMillis();
		while(maxSpringForce-forceThreshold > 0) {
			i++;
			maxSpringForce = 0.0;
			boolean keepLooping = embed(i);
			
			// centre the graph in the panel, because the edge length heuristic tends to push the graph in one direction
			int panelCentreX = getGraphPanel().getX() + (getGraphPanel().getWidth()/2);
			int panelCentreY = getGraphPanel().getY() + (getGraphPanel().getHeight()/2);
			nodeBuffer.centreOldCentresOnPoint(panelCentreX,panelCentreY);
			
			if(animateFlag && getGraphPanel() != null) {
				nodeBuffer.switchOldCentresToNode();
				getGraphPanel().update(getGraphPanel().getGraphics());
			}
			try {
				Thread.sleep(delay);
			} catch(Exception e) {
				System.out.println("Exception occurred in Thread.sleep() in GraphDrawerEdgeLength.drawGraph "+e);
			}
			if(!keepLooping) {
				System.out.println("Exit due to no movement after "+i+" iterations and "+(System.currentTimeMillis() - startTime)+" milliseconds");
				break;
			}
			if((System.currentTimeMillis() - startTime) > timeLimit) {
				System.out.println("Exit due to time expiry after "+timeLimit+" milliseconds and "+i+" iterations");
				break;
			}
			if(i >= iterations) {
				System.out.println("Exit due to iterations limit "+iterations+" reached after "+(System.currentTimeMillis() - startTime)+" milliseconds");
				break;
			}
			//System.out.println("Iterations: "+i+", max force: "+maxSpringForce+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));
		}

		nodeBuffer.switchOldCentresToNode();

		if(timerFlag) {
			//System.out.println("Iterations: "+i+", max force: "+maxSpringForce+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));
		}

		if(scaleWeightsToUnitary) {
			for(Edge e : getGraph().getEdges()) {
				e.setWeight(e.getWeight()*edgeWeightFactor);
			}
		}
	}


	protected void createFrame() {

		frame = new JFrame("Edge Length Layout Options");
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();

		panel.setLayout(gridbag);

		addWidgets(panel,gridbag);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {


		includeEdgeLengthField = new JTextField(4);
		includeEdgeLengthField.setText(Boolean.toString(includeEdgeLength));
		JLabel includeEdgeLengthLabel = new JLabel("Include Edge Length: ", SwingConstants.LEFT);

		includeSpringEmbedderField = new JTextField(4);
		includeSpringEmbedderField.setText(Boolean.toString(includeSpringEmbedder));
		JLabel includeSpringEmbedderLabel = new JLabel("Include Spring Embedder: ", SwingConstants.LEFT);

		edgeLengthFrequencyField = new JTextField(4);
		edgeLengthFrequencyField.setText(Integer.toString(edgeLengthFrequency));
		JLabel edgeLengthFrequencyLabel = new JLabel("Edge Length Frequency (EL): ", SwingConstants.LEFT);

		idealLengthField = new JTextField(4);
		idealLengthField.setText(Double.toString(idealLength));
		JLabel idealLengthLabel = new JLabel("Ideal Edge Length (EL): ", SwingConstants.LEFT);

		kField = new JTextField(4);
		kField.setText(Double.toString(k));
		JLabel kLabel = new JLabel("Strength of Spring, K (SE): ", SwingConstants.LEFT);

		rField = new JTextField(4);
		rField.setText(Double.toString(r));
		JLabel rLabel = new JLabel("Strength of Repulsion, R (SE): ", SwingConstants.LEFT);

		fField = new JTextField(4);
		fField.setText(Double.toString(f));
		JLabel fLabel = new JLabel("Force Multiplier, F (SE): ", SwingConstants.LEFT);

		scaleEdgeLengthField = new JTextField(4);
		scaleEdgeLengthField.setText(Boolean.toString(scaleEdgeLength));
		JLabel scaleEdgeLengthLabel = new JLabel("Scale Edge Length (EL): ", SwingConstants.LEFT);

		scaleAttractiveField = new JTextField(4);
		scaleAttractiveField.setText(Boolean.toString(scaleAttractive));
		JLabel scaleAttractiveLabel = new JLabel("Scale Attractive Force (SE): ", SwingConstants.LEFT);

		scaleWeightsToUnitaryField = new JTextField(4);
		scaleWeightsToUnitaryField.setText(Boolean.toString(scaleWeightsToUnitary));
		JLabel scaleWeightsToUnitaryLabel = new JLabel("Scale Edge Weights Between 0 and 1 (Both): ", SwingConstants.LEFT);

		iterationField = new JTextField(4);
		iterationField.setText(Integer.toString(iterations));
		JLabel iterationLabel = new JLabel("Number of Iterations: ", SwingConstants.LEFT);

		animateField = new JTextField(4);
		animateField.setText(Boolean.toString(animateFlag));
		JLabel animateLabel = new JLabel("Animate Algorithm: ", SwingConstants.LEFT);

		timerField = new JTextField(4);
		timerField.setText(Boolean.toString(timerFlag));
		JLabel timerLabel = new JLabel("Output Timer: ", SwingConstants.LEFT);

		delayField = new JTextField(4);
		delayField.setText(Long.toString(delay));
		JLabel delayLabel = new JLabel("Animate Delay: ", SwingConstants.LEFT);


		okButton = new JButton("OK");
		frame.getRootPane().setDefaultButton(okButton);

		okButton.addActionListener(this);

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;

		int yLevel = 0;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(includeEdgeLengthLabel,c);
		widgetPanel.add(includeEdgeLengthLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(includeEdgeLengthField,c);
		widgetPanel.add(includeEdgeLengthField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(includeSpringEmbedderLabel,c);
		widgetPanel.add(includeSpringEmbedderLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(includeSpringEmbedderField,c);
		widgetPanel.add(includeSpringEmbedderField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(edgeLengthFrequencyLabel,c);
		widgetPanel.add(edgeLengthFrequencyLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(edgeLengthFrequencyField,c);
		widgetPanel.add(edgeLengthFrequencyField);


		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(idealLengthLabel,c);
		widgetPanel.add(idealLengthLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(idealLengthField,c);
		widgetPanel.add(idealLengthField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleEdgeLengthLabel,c);
		widgetPanel.add(scaleEdgeLengthLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleEdgeLengthField,c);
		widgetPanel.add(scaleEdgeLengthField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(kLabel,c);
		widgetPanel.add(kLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(kField,c);
		widgetPanel.add(kField);
		kField.requestFocus();

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(rLabel,c);
		widgetPanel.add(rLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(rField,c);
		widgetPanel.add(rField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(fLabel,c);
		widgetPanel.add(fLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(fField,c);
		widgetPanel.add(fField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleAttractiveLabel,c);
		widgetPanel.add(scaleAttractiveLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleAttractiveField,c);
		widgetPanel.add(scaleAttractiveField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleWeightsToUnitaryLabel,c);
		widgetPanel.add(scaleWeightsToUnitaryLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(scaleWeightsToUnitaryField,c);
		widgetPanel.add(scaleWeightsToUnitaryField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(iterationLabel,c);
		widgetPanel.add(iterationLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(iterationField,c);
		widgetPanel.add(iterationField);

		yLevel++;

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(animateField,c);
		widgetPanel.add(animateField);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(animateLabel,c);
		widgetPanel.add(animateLabel);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(timerLabel,c);
		widgetPanel.add(timerLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(timerField,c);
		widgetPanel.add(timerField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(delayLabel,c);
		widgetPanel.add(delayLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(delayField,c);
		widgetPanel.add(delayField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

	}


	public void actionPerformed(ActionEvent event) {

		k = (Double.parseDouble(kField.getText()));
		r = (Double.parseDouble(rField.getText()));
		f = (Double.parseDouble(fField.getText()));
		iterations = (int)(Double.parseDouble(iterationField.getText()));
		edgeLengthFrequency = (int)(Double.parseDouble(edgeLengthFrequencyField.getText()));

		idealLength = (int)(Double.parseDouble(idealLengthField.getText()));

		if(includeEdgeLengthField.getText().equals("true")) {
			includeEdgeLength = true;
		} else {
			includeEdgeLength = false;
		}

		if(includeSpringEmbedderField.getText().equals("true")) {
			includeSpringEmbedder = true;
		} else {
			includeSpringEmbedder = false;
		}

		if(scaleEdgeLengthField.getText().equals("true")) {
			scaleEdgeLength = true;
		} else {
			scaleEdgeLength = false;
		}

		if(scaleAttractiveField.getText().equals("true")) {
			scaleAttractive = true;
		} else {
			scaleAttractive = false;
		}

		if(scaleWeightsToUnitaryField.getText().equals("true")) {
			scaleWeightsToUnitary = true;
		} else {
			scaleWeightsToUnitary = false;
		}

		if(animateField.getText().equals("true")) {
			animateFlag = true;
		} else {
			animateFlag = false;
		}

		if(timerField.getText().equals("true")) {
			timerFlag = true;
		} else {
			timerFlag = false;
		}

		delay = Long.parseLong(delayField.getText());

		frame.dispose();
		drawGraph();
		getGraphPanel().update(getGraphPanel().getGraphics());
		getGraphPanel().requestFocus();
	}



/**
 * Move all the nodes in the graph.
 */
	public boolean embed(int iteration) {

		if(includeSpringEmbedder) {
			for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
				if (!selectedNodes.contains(nb.getNode())) {
					nb.setNewCentre(force(nb));
				}
			}
			nodeBuffer.switchNewCentresToOld();
		}

		if(includeEdgeLength && (iteration%edgeLengthFrequency == 0)) {
			for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
				if (!selectedNodes.contains(nb.getNode())) {
					nb.setNewCentre(moveByMeanEdgeLength(nb));
				}
			}

nodeBuffer.switchNewCentresToOld();
for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
	if (!selectedNodes.contains(nb.getNode())) {
		nb.setNewCentre(moveByMeanEdgeLength(nb));
	}
}
			newEdgeLengthCentres = nodeBuffer.duplicateNewCentres();
			if(sameIntegerLocation(oldEdgeLengthCentres,newEdgeLengthCentres)) {
				return false;
			}
			oldEdgeLengthCentres = newEdgeLengthCentres;
			nodeBuffer.switchNewCentresToOld();
		}

		return true;

	}
	
	/**
	 * True if all the points in both array lists are in the same location
	 * when converted to integer values.
	 */
	boolean sameIntegerLocation(ArrayList<Point2D.Double> l1, ArrayList<Point2D.Double> l2) {
		if(l1.size() != l2.size()) {
			return false;
		}
		for(int i = 0; i <l1.size(); i++) {
			Point2D.Double p1 = (Point2D.Double)l1.get(i);
			Point2D.Double p2 = (Point2D.Double)l2.get(i);
			int x1 = (int)p1.x;
			int y1 = (int)p1.y;
			int x2 = (int)p2.x;
			int y2 = (int)p2.y;
			if(x1 != x2 || y1 != y2) {
				return false;
			}
		}
		return true;
	}



/**
 * Set the TempX and TempY of the node based on edge length
 * Code adapted from PJM2
 * This is very inefficient, it has to iterate through the entire node collection.
 */
	public Point2D.Double moveByMeanEdgeLength(DrawCoord nb) {
		double x = 0.0;
		double y = 0.0;
		int edgeCount = 0;

		Node n = nb.getNode();
 		Point2D.Double p = nb.getOldCentre();
 				
		for(DrawCoord nextNb : nb.getConnectingDrawCoords()) {
			if(nb == nextNb) {
				continue;
			}

			Node nextN = nextNb.getNode();

			Edge e = null;
			for(Edge edge : n.connectingEdges()) {
				Node target = edge.getOppositeEnd(n);

				if(target == nextN) {
					e = edge;
					break;
				}
			}
// if nodes not connected finish this loop
			if(e == null) {
				continue;
			}

			Point2D.Double nextP = nextNb.getOldCentre();

			double dx = p.x - nextP.x;
			double dy = p.y - nextP.y;

			double distance = p.distance(nextP);

// Get the unit vector for n node to target node.
			dx = dx / distance;
			dy = dy / distance;

			double lengthMultiplier = idealLength;

			if(scaleEdgeLength) {
				lengthMultiplier = lengthMultiplier * e.getWeight();
			}

			x += nextP.x + dx * lengthMultiplier;
			y += nextP.y + dy * lengthMultiplier;

			edgeCount++;
		}

		Point2D.Double ret = p;
		if(edgeCount > 0) {
			ret = new Point2D.Double(x/edgeCount,y/edgeCount);
		}
		return(ret);
	}



/**
 * Finds the new location of a node.
 */
	public Point2D.Double force(DrawCoord nb) {
		Node n = nb.getNode();
		Point2D.Double p = nb.getOldCentre();

		double xAttractive = 0.0;
		double yAttractive = 0.0;
		double xRepulsive = 0.0;
		double yRepulsive = 0.0;
		for(DrawCoord nextNb : nodeBuffer.getBufferedNodes()) {
			if(nb != nextNb) {
				Point2D.Double nextP = nextNb.getOldCentre();
				Node nextN = nextNb.getNode();

				double distance = p.distance(nextP);
				double xDistance = p.x - nextP.x;
				double yDistance = p.y - nextP.y;

				double absDistance = Math.abs(distance);
				double absXDistance = Math.abs(xDistance);
				double absYDistance = Math.abs(yDistance);

				double xForceShare = absXDistance/(absXDistance+absYDistance);
				double yForceShare = absYDistance/(absXDistance+absYDistance);

				double distanceSquared = absDistance*absDistance;


// attractive force
				if (n.connectingNodes().contains(nextN)) {
					double attractiveMultiplier = k;
					if(scaleAttractive) {
						for(Edge e : n.connectingEdges()) {
							if(e.getOppositeEnd(n) == nextN) {
								attractiveMultiplier = attractiveMultiplier/e.getWeight();
							}
						}
					}

					if(xDistance > 0) {
						xAttractive -= xForceShare*distanceSquared*attractiveMultiplier;
					} else {
						if(xDistance < 0) {
							xAttractive += xForceShare*distanceSquared*attractiveMultiplier;
						}
					}

					if(yDistance > 0) {
						yAttractive -= yForceShare*distanceSquared*attractiveMultiplier;
					} else {
						if(yDistance < 0) {
							yAttractive += yForceShare*distanceSquared*attractiveMultiplier;
						}
					}

				}


// repulsive force
				double repulsiveForce = r*r / distance;

				if(xDistance > 0) {
					xRepulsive += repulsiveForce*xForceShare;
				} else {
					if(xDistance < 0) {
						xRepulsive -= repulsiveForce*xForceShare;
					}
				}

				if(yDistance > 0) {
					yRepulsive += repulsiveForce*yForceShare;
				} else {
					if(yDistance < 0) {
						yRepulsive -= repulsiveForce*yForceShare;
					}
				}
			}
		}

		double totalXForce = f*(xRepulsive + xAttractive);
		double totalYForce = f*(yRepulsive + yAttractive);
		
		double totalForce = Math.sqrt(totalXForce*totalXForce+totalYForce*totalYForce);
		if(totalForce > maxSpringForce) {
			maxSpringForce = totalForce;
		}

		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return(ret);
	}


/**
 * Puts any numeric value in the edge label
 * to the edge weight. If no numeric value can be
 * found the weight is set to 1.
 */
	public static void setEdgeLabelsToWeights(Graph g) {

		for(Edge e : g.getEdges()) {
			DecimalFormat nf = (DecimalFormat)NumberFormat.getInstance();
			Number n = nf.parse(e.getLabel(),new ParsePosition(0));

			if(n != null) {
				e.setWeight(n.doubleValue());
			} else {
				e.setWeight(1.0);
			}
		}
	}


}
