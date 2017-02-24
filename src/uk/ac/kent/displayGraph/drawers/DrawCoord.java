package uk.ac.kent.displayGraph.drawers;


import java.awt.geom.Point2D;
import java.util.ArrayList;

import uk.ac.kent.displayGraph.Node;


/**
 * A way of storing new node centres without having
 * to update the actual node.
 * Used in the Spring Embedder graph drawing algorithm.
 *
 * @see Node
 * @see GraphDrawerSpringEmbedder
 *
 * @author Peter Rodgers
 */
public class DrawCoord {

	protected Node node = null;
	protected Point2D.Double oldCentre = new Point2D.Double(0.0,0.0);
	protected Point2D.Double newCentre = new Point2D.Double(0.0,0.0);

/** Added to make edge length algorithm more efficient */
	protected ArrayList<DrawCoord> connectingDrawCoords = null;

/**
 * This constructor sets the new centre to be the
 * current centre of the passed node.
 */
	public DrawCoord(Node n) {
		node = n;
		oldCentre.setLocation(n.getCentre());
		newCentre.setLocation(n.getCentre());
	}

/** Trival constructor. */
	public DrawCoord(Node n,Point2D.Double p) {
		node = n;
		oldCentre.setLocation(n.getCentre());
		newCentre.setLocation(p);
	}

/** Trival accessor. */
	public Node getNode() {return node;}
/** Trival accessor. */
	public Point2D.Double getOldCentre() {return oldCentre;}
/** Trival accessor. */
	public Point2D.Double getNewCentre() {return newCentre;}
/** Trival accessor. */
	public ArrayList<DrawCoord> getConnectingDrawCoords() {return connectingDrawCoords;}

/** Trivial modifier. */
	public void setOldCentre(Point2D.Double p) {oldCentre.setLocation(p);}
/** Trivial modifier. */
	public void setNewCentre(Point2D.Double p) {newCentre.setLocation(p);}
/** Trivial modifier. */
	public void setConnectingDrawCoords(ArrayList<DrawCoord> inConnectingDrawCoords) {connectingDrawCoords = inConnectingDrawCoords;}

/** Gives a textual representation of the node, at the moment just the label. */
	public String toString() {
		return(node.getLabel()+" old centre:"+oldCentre+" new centre:"+newCentre);
	}
}


 
