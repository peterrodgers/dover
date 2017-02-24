package uk.ac.kent.displayGraph.drawers;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import uk.ac.kent.displayGraph.Node;

/**
 * A collection of centre buffered nodes.
 *
 * @author Peter Rodgers
 */


public class DrawCoordCollection {

/** The list of nodes with centres */
	protected ArrayList<DrawCoord> bufferedNodes = new ArrayList<DrawCoord>();

/** Trival constructor. */
	public DrawCoordCollection() {
	}

/** Constructor initializing node collection. */
	public DrawCoordCollection(ArrayList<Node> nodes) {
		setUpNodes(nodes);
	}

/** Trivial accessor */
	public ArrayList<DrawCoord> getBufferedNodes() {return bufferedNodes;}
	
	public DrawCoord getBufferedNode(Node n) {
		for(DrawCoord nb : bufferedNodes) {
			if(nb.getNode() == n) {
				return nb;
			}
		}
		return null;
	}


/**
 * Initialises the centre buffered nodes.
 */
	public void setUpNodes(ArrayList<Node> nodes) {
		bufferedNodes = new ArrayList<DrawCoord>();
		for(Node n : nodes) {
			bufferedNodes.add(new DrawCoord(n));
		}

// set up the redundant data
		for(DrawCoord dc1 : bufferedNodes) {
			ArrayList<DrawCoord> connecting = new ArrayList<DrawCoord>();
			Node n1 = dc1.getNode();
			
			for(DrawCoord dc2 : bufferedNodes) {
				Node n2 = dc2.getNode();
				if (n1.connectingNodes().contains(n2)) {
					connecting.add(dc2);
				}
			}
			dc1.setConnectingDrawCoords(connecting);
		}

	}


/** Moves the node centres from the new buffer to the old buffer */
	public void switchNewCentresToOld() {
		for(DrawCoord bn : bufferedNodes) {
			bn.setOldCentre(bn.getNewCentre());
		}
	}

	public Point2D.Double getCentreOfOldCentres() {

		double maxX = Double.NEGATIVE_INFINITY;
		double minX = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;

		for(DrawCoord bn : bufferedNodes) {
			double x = bn.getOldCentre().getX();
			double y = bn.getOldCentre().getY();
			
			if(x > maxX) {
				maxX = x;
			}
			if(x < minX) {
				minX = x;
			}
			if(y > maxY) {
				maxY = y;
			}
			if(y < minY) {
				minY = y;
			}
		}

		double cx = minX + (maxX - minX)/2;
		double cy = minY + (maxY - minY)/2;

		Point2D.Double ret = new Point2D.Double(cx,cy);
		return ret;
	}

	
	/** Centre the graph on the given point */
	public void centreOldCentresOnPoint(int centreX, int centreY) {
		Point2D.Double graphCentre = getCentreOfOldCentres();
	
		double moveX = centreX - graphCentre.x;
		double moveY = centreY - graphCentre.y;
		
		moveOldNodeCentres(moveX,moveY);
	}

/** Move all the nodes by the values given */
	public void moveOldNodeCentres(double moveX, double moveY) {
	
		for(DrawCoord bn : bufferedNodes) {
			double x = bn.getOldCentre().getX();
			double y = bn.getOldCentre().getY();
			
			Point2D.Double changedCentre = new Point2D.Double(x+moveX,y+moveY);
	
			bn.setOldCentre(changedCentre);
		}
	}

	
/** Moves the node centres from the old buffer to the actual node*/
	public void switchOldCentresToNode() {
		for(DrawCoord bn : bufferedNodes) {
			Node n = bn.getNode();
			Point p = n.getCentre();
			p.setLocation(bn.getOldCentre().x,bn.getOldCentre().y);
		}
	}

	/**
	 * returns a copy of the new centres
	 */
	public ArrayList<Point.Double> duplicateNewCentres() {
	    ArrayList<Point.Double> ret = new ArrayList<Point.Double>();
		for(DrawCoord bn : bufferedNodes) {
			Point2D.Double p = new Point2D.Double(bn.getNewCentre().x,bn.getNewCentre().y);
			ret.add(p);
		}
		return ret;
	}

/**
 * Outputs the node and edge lists in a string.
 */
	public String toString() {
		StringBuffer ret = new StringBuffer("");
		Iterator<DrawCoord> bni = bufferedNodes.iterator();
		ret.append("[");
		while(bni.hasNext()) {
			DrawCoord bn = (DrawCoord)bni.next();
			ret.append("DrawCoord: ");
			ret.append(bn);
			ret.append(" connecting: ");
			ret.append(bn.getConnectingDrawCoords().toString());
			if(bni.hasNext()) {
				ret.append("|");
			}
		}
		ret.append("]");
		return(ret.toString());
	}


}


