package uk.ac.kent.displayGraph.drawers;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

import uk.ac.kent.displayGraph.Node;

/**
 * Attractive forces only version of Eades spring embedder.
 * Selected nodes are not moved.
 *
 * @author Peter Rodgers
 */

public class GraphDrawerAttractive extends GraphDrawer {

/** The strength of a spring */
	protected double k = .05;
/** The amount of movement on each iteration */
	protected double f = 2.0;
/** The number of iterations */
	protected int iterations = 100;
/** The node buffer. This holds copies of node locations */
	protected DrawCoordCollection nodeBuffer = new DrawCoordCollection();
/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
/** The nodes selected at the start of the algorithm */
	protected ArrayList<Node> selectedNodes = null;

/** Trivial constructor. */
	public GraphDrawerAttractive() {
		super(KeyEvent.VK_A,"Attractive Embedder");
	}

/** Trivial constructor. */
	public GraphDrawerAttractive(int key, String s) {
		super(key,s);
	}


/** Trival accessor. */
	public long getTime() {return time;}
/** Trival accessor. */
	public double getK() {return k;}
/** Trival accessor. */
	public double getF() {return f;}
/** Trival accessor. */
	public int getIterations() {return iterations;}
/** Trival accessor. */
	public boolean getAnimateFlag() {return animateFlag;}

/** Trivial modifier. */
	public void setK(double inK) {k = inK;}
/** Trivial modifier. */
	public void setF(double inF) {f = inF;}
/** Trivial modifier. */
	public void setIterations(int inIterations) {iterations = inIterations;}
/** Trivial modifier. */
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}


/** Draws the graph. */
	public void layout() {

		selectedNodes = getGraphPanel().getSelection().getNodes();

		Date oldTimer = new Date();

		nodeBuffer.setUpNodes(getGraph().getNodes());
		for(int i = 1; i <= iterations; i++) {
			embed();
			if(animateFlag && getGraphPanel() != null) {
				nodeBuffer.switchOldCentresToNode();
				getGraphPanel().update(getGraphPanel().getGraphics());
			}
		}

		nodeBuffer.switchOldCentresToNode();

		Date newTimer = new Date();
		time = newTimer.getTime() - oldTimer.getTime();
		System.out.println("time: "+time);
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
			}
		}

		double newX = 0;
		double newY = 0;

		newX = p.x + f * xAttractive;
		newY = p.y + f * yAttractive;

		Point2D.Double ret = new Point2D.Double(newX,newY);

		return(ret);
	}


}

 
