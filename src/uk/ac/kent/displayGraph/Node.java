package uk.ac.kent.displayGraph;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * A node in a graph. This implements a simple labeled node for a graph.
 * <p>
 * It contains some additional fields for helping when writing algorithms, the
 * boolean {@link #visited}, a flag for indicating that a node has been traversed
 * and the list {@link #path} for indicating some sort of route to the
 * node, which also allows a test (null) to see if the node has been visited.
 * <p>
 * A redundant structure is used to speed up the access of neighbouring
 * nodes. This is in the form of the two Hash Sets, {@link #pjr.graph.Node.edgesFrom} and
 * {@link #pjr.graph.Node.edgesTo}, which are modified when edges are created, deleted or modified.
 *
 * @see Graph
 * @see Edge
 * @author Peter Rodgers
 */
public class Node {
	/**
	 * Redundant data giving the edges out of this node to speed up algorithms.
	 * It needs to be maintained by edges as they are connected and
	 * removed from this node. The node should not alter this member.
	 * The collection should have unique content so that an edge must appear only once.
	 */
	protected ArrayList<Edge> edgesFrom = new ArrayList<Edge>();
	/**
	 * Redundant data giving the edges into this node to speed up algorithms.
	 * It needs to be  maintained by edges as they are connected and
	 * removed from this node. The node should not alter this member.
	 * The collection should have unique content so that an edge must appear only once.
	 */
	protected ArrayList<Edge> edgesTo = new ArrayList<Edge>();
	protected int nodeId;
	/** Label, can be empty. */
	protected String label = "";
	/** Type, must be present. */
	protected NodeType type = Graph.DEFAULT_NODE_TYPE;
	/** Coordinate of the middle of the node on the screen. */
	protected Point centre = new Point(0,0);
	/** This flag that can be set if the node has been traversed in an algorithm. */
	protected boolean visited = false;
	/** A variable for use in graph algorithms. This can be used to score nodes. */
	protected double score = 0.0;
	/**
	 * A variable for use in graph algorithms. General Use, but can be used for
	 * pointing at nodes in matching algorithms.
	 */
	protected Object match = null;
	/** The last generated shape object for this node */
	Shape shape = null;
	/** The animation X distance on each iteration. */
	public static final int ANIMATEXINCREMENT = 3;
	/** The animation Y distance on each iteration. */
	public static final int ANIMATEYINCREMENT = 1;
	protected int nodeIdx  = -1;
	
	public int getIndex(){return nodeIdx;}
	public void setIndex(int idx){nodeIdx=idx;}

/** Minimal constructor. It creates an unlabelled node. */
	public Node() {}
		/** Creates a labelled node */
	public Node(String inLabel) {
		setLabel(inLabel);
	}

/*	public Node (PNode pNode){
		this.setLabel(pNode.getLabel());
		Point point = new Point();
		point.setLocation(pNode.getX(), pNode.getY());		
		this.setCentre(point);
	}*/
	/** Creates a node at the given point. */
	public Node(Point inCentre) {
		setCentre(inCentre);
	}
		/** Creates a node of the given type. */
	public Node(NodeType inType) {
		setType(inType);
	}
	/** Creates a labelled node of the given type. */
	public Node(String inLabel, NodeType inType) {
		setLabel(inLabel);
		setType(inType);
	}
	/** Creates a node of the given type at the given point. */
	public Node(NodeType inType, Point inCentre) {
		setType(inType);
		setCentre(inCentre);
	}
	/** Creates a labelled node at the given point. */
	public Node(String inLabel, Point inCentre) {
		setLabel(inLabel);
		setCentre(inCentre);
	}
	/** Creates a labelled node of the given type at the given point. */
	public Node(String inLabel, NodeType inType, Point inCentre) {
		setLabel(inLabel);
		setType(inType);
		setCentre(inCentre);
	}
	public void setNodeId(int id){
		nodeId = id;
	}
	public int getId(){return nodeId;}
	public ArrayList<Edge> getEdgesFrom() {return edgesFrom;}
	public ArrayList<Edge> getEdgesTo() {return edgesTo;}
	public String getLabel() {return label;}
	public NodeType getType() {return type;}
	public Point getCentre() {return centre;}
	public int getX() {return getCentre().x;}
	public int getY() {return getCentre().y;}
	public boolean getVisited() {return visited;}
	public double getScore() {return score;}
	public Object getMatch() {return match;}

	public void setLabel(String inLabel) {label=inLabel;}
	public void setType(NodeType inType) {type=inType;}
	public void setCentre(Point inCentre) {centre=inCentre;}
	public void setVisited(boolean inVisited) {visited = inVisited;}
	public void setScore(double inScore) {score = inScore;}
	public void setMatch(Object inMatch) {match = inMatch;}
	protected boolean addEdgeFrom(Edge e) {return(edgesFrom.add(e));}
	protected boolean addEdgeTo(Edge e) {return(edgesTo.add(e));}
	protected boolean removeEdgeFrom(Edge e) {return(edgesFrom.remove(e));}
	protected boolean removeEdgeTo(Edge e) {return(edgesTo.remove(e));}
	public void setX(int inX) {setCentre(new Point(inX,centre.y));}
	public void setY(int inY) {setCentre(new Point(centre.x, inY));}
	/** Set the X part of the node centre only and animate the changes. */
	public void setX(int inX, JPanel p) {
		while (centre.x > inX) {
			setCentre(new Point(centre.x-ANIMATEXINCREMENT,centre.y));
			p.update(p.getGraphics());
		}
		while (centre.x < inX) {
			setCentre(new Point(centre.x+ANIMATEXINCREMENT,centre.y));
			p.update(p.getGraphics());
		}
		// a final move, in case the increments are uneven
		setCentre(new Point(inX,centre.y));
		p.update(p.getGraphics());
	}
	/** Set the Y part of the node centre only and animate the changes. */
	public void setY(int inY, JPanel p) {
		while (centre.y > inY) {
			setCentre(new Point(centre.x,centre.y-ANIMATEYINCREMENT));
			p.update(p.getGraphics());
		}
		while (centre.y < inY) {
			setCentre(new Point(centre.x,centre.y+ANIMATEYINCREMENT));
			p.update(p.getGraphics());
		}
		// a final move, in case the increments are uneven
		setCentre(new Point(centre.x,inY));
		p.update(p.getGraphics());
	}

	/**
	 * Gives all the connecting edges.
	 * @return all the connecting edges, without duplicates.
	 */
	public ArrayList<Edge> connectingEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>(getEdgesFrom());
		edges.addAll(getEdgesTo());
		return(edges);
	}
	/**
	 * Gives the unvisited connecting edges, without duplicates. The predicate
	 * is based on the visited field of the edge.
	 */
	public ArrayList<Edge> unvisitedConnectingEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Collection<Edge> allEdges = connectingEdges();
		for(Edge e : allEdges) {
			if(!e.getVisited()) {
				edges.add(e);
			}
		}
		return(edges);
	}

	/**
	 * Gives all the connecting edges from this node to the argument node.
	 * @return all the connecting edges to the argument node, without duplicates.
	 */
	public ArrayList<Edge> connectingEdges(Node n) {
		ArrayList<Edge> ret = new ArrayList<Edge>();

		// needs to be done twice, once for from list, once for to list
		for(Edge e : edgesFrom) {
			if(e.getTo() == n) {
				ret.add(e);
			}
		}
		for(Edge e : edgesTo) {
			if(e.getFrom() == n) {
				ret.add(e);
			}
		}
		return ret;
	}
	/**
	 * Gives all the connecting nodes without duplicates.
	 * Or, put another way all the neigbouring nodes.
	 * Duplicates could be due to self sourcing edges or parallel nodes, and are removed.
	 * @return all the neigbouring nodes, without duplicates.
	 */
	public ArrayList<Node> connectingNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>(degree());
		// iterate through the edges
		for(Edge e : connectingEdges()) {
			Node n;
			// get the node at the right end (this test copes with self sourcing nodes)
			if(e.getTo() == this) {
				n = e.getFrom();
			} else {
				n = e.getTo();
			}
			//check for duplicates
			if(n == null){
				System.out.println(e.toString());
			}
			if(!nodes.contains(n)) {
				nodes.add(n);
			}
		}

		return(nodes);
	}

	/**
	 * Gives all the unvisited connecting nodes, according to the visited field.
	 * @return all unvisited neighbouring nodes, without duplicates.
	 */
	public ArrayList<Node> unvisitedConnectingNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>(degree());
		// iterate through the connecting nodes
		if( connectingNodes()!=null){
			for(Node n : connectingNodes()) {
				if(n == null){
					System.out.println("n = null");
				}
				if( !n.getVisited()) {
					nodes.add(n);
				}
			}
		}
		return nodes;
	}


	/** Gives the number of connecting edges. */
	public int degree() {
		return(getEdgesFrom().size()+getEdgesTo().size());
	}

	/**
	 * Gives a new shape object repsenting the node. At the moment
	 * rectangles and ellipses are supported
	 */
	public Shape generateShape() {
		int height = type.getHeight();
		int width = type.getWidth();
		if(type.getShapeString().equals("Ellipse")) {
			shape = new Ellipse2D.Double(centre.x-width/2,centre.y-height/2,width,height);
		 } else {
			shape = new Rectangle2D.Double(centre.x-width/2,centre.y-height/2,width,height);
		}
		return(shape);
	}


	/**
	 * Returns the shape object representing the node, or if
	 * there is none, generate a new shape.
	 */
	public Shape shape() {
		if(shape == null) {
			generateShape();
		}
		return(shape);
	}


	/**
	 * Check consistency- ensure all redundant connection data matches
	 * with edge to and from. Principally used by the Graph method
	 * {@link Graph#consistent}.
	 */
	public boolean consistent(Graph g) {
		// needs to be done twice, once for from list, once for to list
		for(Edge e : edgesFrom) {
			// test for edge having wrong data
			if(e.getFrom() != this) {
				return false;
			}
			if(!g.getEdges().contains(e)) {
				return false;
			}
		}
		for(Edge e : edgesTo) {
			// test for edge having wrong data
			if(e.getTo() != this) {
				return false;
			}
			if(!g.getEdges().contains(e)) {
				return false;
			}
		}
		return true;
	}


	/** Gives a textual representation of the node, at the moment just the label. */
	public String toString() {
		return(label);
	}
}

