package uk.ac.kent.displayGraph;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

//import graphStructure.*;

/**
 * This is a simple edge, connecting two nodes together in a graph.
 * It can be treated as a directed or undirected edge.
 * <p>
 * It contains an additional field for helping when writing algorithms, the
 * boolean {@link #visited}, a flag for indicating that an edge has been traversed
 * @see Graph
 * @see Node
 * @author Peter Rodgers
 */

public class Edge {

	/** Source node. Must be assigned.*/
	protected Node from;
	/** Target node. Must be assigned.*/
	protected Node to;
	/** Optional label.*/
	protected String label = "";
	/** Optional weight value.*/
	protected double weight = 0.0;
	/** Edge type, must be present. */
	protected EdgeType type = Graph.DEFAULT_EDGE_TYPE;
	/** This flag can be set if the edge has been traversed during a graph algorithm. */
	protected boolean visited = false;
	/** This value can be used to score edges during a graph algorithm. */
	protected double score = 0.0;
	/**
	 * A variable for use in graph algorithms. General Use, but can be used for
	 * pointing at nodes in matching algorithms.
	 */
	protected Object match = null;
	/** The last generated shape object for this edge */
	Shape shape = null;
	/** The location of edge bends. This is a list of Points.*/
	protected ArrayList<Point> bends = new ArrayList<Point>();
	/** The slope of an edge */
	protected double slope = 0.0;
	/** The index of an edge */
	protected int index = 0;

	/**
	 * Pass the two nodes that the edge should connect to this constructor.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo) {
		setFromTo(inFrom, inTo);
	}
	/**
	 * Constructor initialising label.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, String inLabel) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
	}
	/**
	 * Constructor initialising weight.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, double inWeight) {
		setFromTo(inFrom, inTo);
		setWeight(inWeight);
	}
	/**
	 * Constructor initialising type.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, EdgeType inType) {
		setFromTo(inFrom, inTo);
		setType(inType);
	}
	/**
	 * Constructor initialising label and weight.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, String inLabel, double inWeight) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
		setWeight(inWeight);
	}	
	/**
	 * Constructor initialising label and type.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, String inLabel, EdgeType inType) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
		setType(inType);
	}
	/**
	 * Constructor initialising label, weight and type.
	 * It creates an edge between the two nodes.
	 */
	public Edge(Node inFrom, Node inTo, String inLabel, double inWeight, EdgeType inType) {
		setFromTo(inFrom, inTo);
		setLabel(inLabel);
		setWeight(inWeight);
		setType(inType);
	}
/*	public Edge (PEdge pEdge){
		PNode pStart = pEdge.getStartPNode();
		PNode pEnd = pEdge.getEndPNode();
		Node start = new Node (pStart);
		Node end = new Node (pEnd);
		setFromTo(start, end);	
	}*/
	public Node getFrom() {return from;}
	public Node getTo() {return to;}
	public String getLabel() {return label;}
	public double getWeight() {return weight;}
	public EdgeType getType() {return type;}
	public boolean getVisited() {return visited;}
	public double getScore() {return score;}
	public Object getMatch() {return match;}
	public ArrayList<Point> getBends() {return bends;}
	public void setLabel(String inLabel) {label = inLabel;}
	public void setWeight(double inWeight) {weight = inWeight;}
	public void setType(EdgeType inType) {type=inType;}
	public void setVisited(boolean inVisited) {visited = inVisited;}
	public void setScore(double inScore) {score = inScore;}
	public void setMatch(Object inMatch) {match = inMatch;}
	public void setBends(ArrayList<Point> inEdgeBends) {bends = inEdgeBends;}
	public void setIndex(int i){ index =i;}
	
	/** set the slope of the edge*/	
  	public void setSlope(){
  		double rise = from.getY() - to.getY();
  		double run = from.getX() - to.getX();
  		if (run == 0)
  			slope = Double.MAX_VALUE;
  		else 
  			slope = rise / run;
    }  		
/** Adds an edge bend to the end of the edge bend list. */
	public void addBend(Point p) {
		getBends().add(p);
	}
/** Removes all edge bends by setting the bend list to a new, empty list. */
	public void removeAllBends() {
		bends = new ArrayList<Point>();
	}

/** find out if the edge connects to the same node at each end. */
	public boolean selfSourcing() {
		if(from == to) {
			return(true);
		}
		return(false);
	}
/**
 * Return the coordinate of the middle point alone the edge line
 * */
	public Point getMidPoint(){
		
		Point p1 = from.getCentre();
		Point p2 = to.getCentre();
	//	System.out.println("p1 " + p1);
	//	System.out.println("p2 " + p2);
		int x = (int)(p1.getX() + p2.getX())/2;
		int y = (int)(p1.getY() + p2.getY())/2;
		
		return (new Point(x,y));
	}
/**
 * Gives the other end of the edge to the argument node
 * @return the node at the other end of the edge, or null if the passed node is not connected to the edge
 */
	public Node getOppositeEnd(Node n) {

		Node ret = null;
		if (getFrom() == n) {
			ret = getTo();
		}
		if (getTo() == n) {
			ret = getFrom();
		}

		return(ret);
	}
/** Trival accessor. */
	public double getSlope(){ return slope;}	
	public int getIndex(){return index;}
/**
 * Sets or moves both ends of the edge.
 * @return the success of the operation, if fail, then the old state
 * should be preserved.
 */
	public boolean setFromTo(Node inFrom, Node inTo) {

		Node oldFrom=from;
		if(!setFrom(inFrom)) {
			return(false);
		}
		if(!setTo(inTo)) {
			setFrom(oldFrom);
			return(false);
		}
		return(true);
	}
/**
 * Sets or moves the source of the edge. Accounts for the
 * redundant data in the node. Also deals with nulls, either
 * currently attached to edges, or passed to this method.
 * @return the success of the operation.
 */
	public boolean setFrom(Node inFrom) {
		if(from != null) {
			if(!from.removeEdgeFrom(this)) {
				return(false);
			}
		}
		if(inFrom != null) {
			inFrom.addEdgeFrom(this);
		}
		from = inFrom;
		return(true);
	}
/**
* Sets or moves the target of the edge. Accounts for the
* redundant data in the node. Also deals with nulls, either
* currently attached to edges, or passed to this method.
* @return the success of the operation.
*/
	public boolean setTo(Node inTo) {
		if(to != null) {
			if(!to.removeEdgeTo(this)) {
				return(false);
			}
		}
		if(inTo != null) {
			inTo.addEdgeTo(this);
		}
		to = inTo;
		return(true);
	}
	/**
	 * This method reverses the ends of the edge. It also works for undirected edges,
	 * reversing the from and to values, although in this case no difference should
	 * be apparent.
	 */
		public void reverse() {
			
			Node oldFrom = from;
			setFrom(to);
			setTo(oldFrom);
			
			Collections.reverse(bends);
		}
/**
 * Checks to see if the two edges intersect, only works
 * if there are no edge bends in either edge.
 */
	public boolean straightLineIntersects(Edge e) {
		
		// if the edges connect to the same node there is no crossing
		if(getFrom() == e.getFrom()) {return false;}
		if(getFrom() == e.getTo()) {return false;}
		if(getTo() == e.getFrom()) {return false;}
		if(getTo() == e.getTo()) {return false;}
		
		int x1 = getFrom().getX();
		int y1 = getFrom().getY();
		int x2 = getTo().getX();
		int y2 = getTo().getY();
		int x3 = e.getFrom().getX();
		int y3 = e.getFrom().getY();
		int x4 = e.getTo().getX();
		int y4 = e.getTo().getY();


		return Line2D.linesIntersect(x1,y1,x2,y2,x3,y3,x4,y4);
	}
/**
 * Gives a new shape object representing the edge.

 */
	public Shape generateShape(Point offset) {

		Point fromPoint = new Point(from.getCentre());
		Point toPoint = new Point(to.getCentre());

		fromPoint.x += offset.x;
		fromPoint.y += offset.y;
		toPoint.x += offset.x;
		toPoint.y += offset.y;

		GeneralPath path = new GeneralPath();
		path.moveTo(fromPoint.x+offset.x, fromPoint.y+offset.y);

		for(Point p : bends) {
			path.lineTo(p.x+offset.x, p.y+offset.y);
		}

		path.lineTo(toPoint.x+offset.x, toPoint.y+offset.y);

		shape = path;

		return(shape);
	}
/**
 * Returns the shape object representing the edge, or if
 * there is none, generate a new shape. The generation is
 * performed using a zero offset, which may cause difficulties
 * if parallel edges are being separated, however generation
 * due to the call of this method will be extremely rare, and
 * no known situation where generation is performed is known.
 */
	public Shape shape() {
		if(shape == null) {
			generateShape(GraphPanel.ZEROOFFSET);
		}
		return(shape);
	}
/**
 * Checks that the connecting nodes have consistent redundant data. Used principally
 * by {@link Graph#consistent}.
 */
	public boolean consistent(Graph g) {
		if(!getFrom().getEdgesFrom().contains(this)) {
			return(false);
		}
		if(!getTo().getEdgesTo().contains(this)) {
			return(false);
		}
		if(!g.getNodes().contains(getFrom())) {
			return(false);
		}
		if(!g.getNodes().contains(getTo())) {
			return(false);
		}

		return(true);
	}
/**
 * returns half edge line from one end node
 * */
	
	public Line2D getHalfEdgeLine(Node node){
		
		Line2D line = new Line2D.Double();
		if(!node.equals(to)&&!node.equals(from)){
			System.out.println("Error : node is neither a start or a end node of edge");			
		}
		else{
			Point p1 = this.getMidPoint();
			Point p2 = node.getCentre();
			line.setLine(p1, p2);
		}
		return line;
	}
	
/**
 * returns a line that merges the edge 
 * */	
	public Line2D getEdgeLine(){
		Line2D line = new Line2D.Double();
		Point p1 = from.getCentre();
		Point p2 = to.getCentre();
		line.setLine(p1,p2);		
		return line;
	}
	
	public boolean hasNode(Node n) {
		
		if(n.getLabel().compareTo(to.getLabel()) == 0 ||n.getLabel().compareTo(from.getLabel()) == 0){
			return true;
		}
		return false;
		
	}
	
	public double findLength() {

		double length = 0.0;
		
		Point current = getFrom().getCentre();
		for(Point bend : getBends()) {
			double distance = uk.ac.kent.displayGraph.Util.distance(current, bend);
			length += distance;
			current = bend;
		}
		double distance = uk.ac.kent.displayGraph.Util.distance(current, getTo().getCentre());
		length += distance;
		
		return length;

	}
	
	
	/**
	 * Check to see if the argument edge has the same label,
	 * end nodes in the same position and with the same labels
	 * and the same number of bend points at the same positions.
	 */
	public boolean isMatchingEdge(Edge e) {
		
		if(!e.getLabel().equals(getLabel())) {return false;}
		if(!e.getFrom().getLabel().equals(getFrom().getLabel())) {return false;}
		if(!e.getTo().getLabel().equals(getTo().getLabel())) {return false;}
		if(e.getFrom().getX() != getFrom().getX()) {return false;}
		if(e.getFrom().getY() != getFrom().getY()) {return false;}
		if(e.getTo().getX() != getTo().getX()) {return false;}
		if(e.getTo().getY() != getTo().getY()) {return false;}
		
		ArrayList<Point> inBends = e.getBends();
		ArrayList<Point> thisBends = getBends();
		if(inBends.size() != thisBends.size()) {return false;}
		
		int i = 0;
		while(i < inBends.size()) {
			Point inBend = inBends.get(i); 
			Point thisBend = thisBends.get(i);
			if(inBend.getX() != thisBend.getX()) {return false;}
			if(inBend.getY() != thisBend.getY()) {return false;}
			i++;
		}
		
		return true;

	}


	/**
	 * Makes the number of edge bends up to the input value.
	 * It does this by successively splitting the largest line
	 * segment between two edges. If two edge segments are equal,
	 * then the first found is split.
	 * @return if a change was made.
	 */
	public boolean addEdgeBends(int bendsRequired) {
		if(bends.size() >= bendsRequired) {
			return false;
		}

		while(bends.size() < bendsRequired) {
			int firstBendIndex = findFirstBendOfLongestSection();

			Point firstPoint = from.getCentre();
			if(firstBendIndex != -1) {
				firstPoint = bends.get(firstBendIndex);
			}
			Point secondPoint = to.getCentre();
			if(firstBendIndex != bends.size()-1) {
				secondPoint = bends.get(firstBendIndex+1);
			}
			
			Point newPoint = Util.midPoint(firstPoint, secondPoint);
			bends.add(firstBendIndex+1, newPoint);
		}
		
		return true;
	}
	
	/**
	 * Finds the first bend in the longest bend section.
	 * If two bend sections are equal, then the first found is returned.
	 * @return bend index, or -1 if it is the from node.
	 */
	public int findFirstBendOfLongestSection() {
		if(bends.size() ==0) {
			return -1;
		}
		Point nextPoint = from.getCentre();
		int i = 0;
		double maxDistance = 0;
		int maxIndex = -1;
		while(i<=bends.size()) {
			Point thisPoint = nextPoint;
			
			if(i == bends.size()) {
				nextPoint = to.getCentre();
			} else {
				nextPoint = bends.get(i);
			}
			
			double distance = Util.distance(thisPoint, nextPoint);
			if(distance > maxDistance) {
				maxDistance = distance;
				maxIndex = i-1;
			}
			i++;
			
		}
		
		return maxIndex;
	}

	/**
	 * Tests to see if any bend segment crosses the given line
	 */
	public boolean crossesLine(Point lineStart, Point lineEnd) {
		Point currentPoint = getFrom().getCentre();
		for(Point nextPoint : getBends()) {
			if(uk.ac.kent.displayGraph.Util.linesCross(lineStart,lineEnd,currentPoint,nextPoint)) {
				return true;
			}
			currentPoint = nextPoint;
		}
		if(uk.ac.kent.displayGraph.Util.linesCross(lineStart,lineEnd,currentPoint,getTo().getCentre())) {
			return true;
		}
		
		return false;
	}

	/**
	 * Tests to see if any bend segment crosses the given Edge segment,
	 * the connectingNodes parameter give nodes that are at the end of
	 * the edge segments, otherwise null.
	 */
	public boolean crossesEdgeSegment(Point lineStart, Point lineEnd) {
		Point currentPoint = getFrom().getCentre();
		for(Point nextPoint : getBends()) {
			if(currentPoint == lineStart || currentPoint == lineEnd || nextPoint == lineStart || nextPoint == lineEnd ) {
				// two line segments can't cross if they are attached at the same point
				currentPoint = nextPoint;
				continue;
			}
			if(uk.ac.kent.displayGraph.Util.linesCross(lineStart,lineEnd,currentPoint,nextPoint)) {
				return true;
			}
			currentPoint = nextPoint;
		}
		if(currentPoint == lineStart || currentPoint == lineEnd || getTo().getCentre() == lineStart || getTo().getCentre() == lineEnd) {
			// two line segments can't cross if they are attached at the same point
			return false;
		}
		if(uk.ac.kent.displayGraph.Util.linesCross(lineStart,lineEnd,currentPoint,getTo().getCentre())) {
			return true;
		}
		
		return false;
	}

	/**
	 * Tests to see if any the edges have any crossing line segments.
	 */
	public boolean crossesEdge(Edge e) {
		
		
		Point currentPoint = getFrom().getCentre();
		for(Point nextPoint : getBends()) {
			if(e.crossesEdgeSegment(currentPoint,nextPoint)) {
				return true;
			}
			currentPoint = nextPoint;
		}
		if(e.crossesEdgeSegment(currentPoint,getTo().getCentre())) {
			return true;
		}
		
		return false;
	}

	/** Gives the connecting nodes strings in a pair, plus the edge label and weight. */
	public String toString() {
		return("("+getFrom()+":"+getTo()+","+getLabel()+")");
	}
	
	
}

 
