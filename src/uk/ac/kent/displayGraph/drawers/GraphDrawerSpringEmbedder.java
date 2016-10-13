package uk.ac.kent.displayGraph.drawers;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import uk.ac.kent.displayGraph.*;

/**
 * A version of Eades spring embedder for laying out graphs
 * Selected nodes are not moved, but still participate in the
 * force calculation
 *
 * @author Peter Rodgers
 */
public class GraphDrawerSpringEmbedder extends GraphDrawer {

/** The strength of a spring */
	protected double k = 0.05;
/** The strength of the repulsive force */
	protected double r = 10000.0;
/** The amount of movement on each iteration */
	protected double f = 1.0;
	/** The number of iterations */
	protected int iterations = 10000;
/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 3000;
/** The amount of max force below which the algorithm stops*/
	protected double forceThreshold = 0.1;
/** The maximum force applied on one iteration */
	protected double maxForce = Double.MAX_VALUE;
/** The node buffer. This holds copies of node locations */
	protected DrawCoordCollection nodeBuffer = new DrawCoordCollection();
/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
/** Set to randomize the graph before spring embedding */
	protected boolean randomize = false;
/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
/** The nodes selected at the start of the algorithm */
	protected ArrayList<Node> selectedNodes = null;

/** Trivial constructor. */
	public GraphDrawerSpringEmbedder() {
		super(KeyEvent.VK_S,"Spring Embedder");
	}

/** Trivial constructor. */
	public GraphDrawerSpringEmbedder(int key, String s) {
		super(key,s);
	}
/** Constructor. */
	public GraphDrawerSpringEmbedder(int key, String s, boolean inRandomize) {
		super(key,s);
		randomize = inRandomize;
	}
/** Full constructor. */
	public GraphDrawerSpringEmbedder(int key, String s, int mnemomic, boolean inRandomize) {
		super(key,s,mnemomic);
		randomize = inRandomize;
	}


/** Trival accessor. */
	public long getTime() {return time;}
/** Trival accessor. */
	public double getK() {return k;}
/** Trival accessor. */
	public double getR() {return r;}
/** Trival accessor. */
	public double getF() {return f;}
/** Trival accessor. */
	public int getIterations() {return iterations;}
/** Trival accessor. */
	public boolean getAnimateFlag() {return animateFlag;}

/** Trivial modifier. */
	public void setK(double inK) {k = inK;}
/** Trivial modifier. */
	public void setR(double inR) {r = inR;}
/** Trivial modifier. */
	public void setF(double inF) {f = inF;}
/** Trivial modifier. */
	public void setIterations(int inIterations) {iterations = inIterations;}
	/** Trivial modifier. */
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
	/** Trivial modifier. */
	public void setTimeLimit(int time) {timeLimit = time;}

/** Draws the graph. */
	public void layout() {

		selectedNodes = getGraphPanel().getSelection().getNodes();

		if(randomize) {
			getGraph().randomizeNodePoints(new Point(50,50),400,400);
		}
	
		maxForce = Double.MAX_VALUE;
		nodeBuffer.setUpNodes(getGraph().getNodes());
		int i = 0;
		long startTime = System.currentTimeMillis();
		while(maxForce-forceThreshold > 0) {
			i++;
			maxForce = 0.0;
			embed();
			if(animateFlag && getGraphPanel() != null) {
				nodeBuffer.switchOldCentresToNode();
				getGraphPanel().update(getGraphPanel().getGraphics());
			}
			if((System.currentTimeMillis() - startTime) > timeLimit) {
				System.out.println("Exit due to time expiry after "+timeLimit+" milliseconds and "+i+" iterations");
				break;
			}
		}
		//System.out.println("Iterations: "+i+", max force: "+maxForce+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));

		nodeBuffer.switchOldCentresToNode();
		
		if(!animateFlag && getGraphPanel() != null) {
			getGraphPanel().update(getGraphPanel().getGraphics());
		}
	}


/**
 * Move all the nodes in the graph.
 */
	public void embed() {
		for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
			if (!selectedNodes.contains(nb.getNode())) {
				nb.setNewCentre(force(nb));
			}
		}
		nodeBuffer.switchNewCentresToOld();
	}


/**
 * Finds the new location of a node.
 */
	public Point2D.Double force(DrawCoord nb) {
		Node n = nb.getNode();
		Point2D.Double p = nb.getOldCentre();

		double xRepulsive = 0.0;
		double yRepulsive = 0.0;
		double xAttractive = 0.0;
		double yAttractive = 0.0;
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

				if (n.connectingNodes().contains(nextN)) {
					if(xDistance > 0) {
						xAttractive -= k*xForceShare*absDistance;
					} else {
						if(xDistance < 0) {
							xAttractive += k*xForceShare*absDistance;
						}
					}

					if(yDistance > 0) {
						yAttractive -= k*yForceShare*absDistance;
					} else {
						if(yDistance < 0) {
							yAttractive += k*yForceShare*absDistance;
						}
					}
				}


				double repulsiveForce = r / (distance * distance);

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
		if(totalForce > maxForce) {
			maxForce = totalForce;
		}

		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return(ret);
	}

}
