package uk.ac.kent.displayGraph;

import javax.swing.*;
import javax.swing.event.*;

import uk.ac.kent.displayGraph.dialogs.*;
import uk.ac.kent.displayGraph.drawers.*;
import uk.ac.kent.displayGraph.experiments.*;
import uk.ac.kent.displayGraph.utilities.*;
import uk.ac.kent.displayGraph.views.*;

import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;

/**
 * A panel on which a graph is displayed.
 * <p>
 * Functions:
 * <br>
 * - Add a node with double button 1 click on the background.
 * <br>
 * - Add an edge with a button 3 drag (picks closest nodes to start
 *   and end of the drag, but does not add self sourcing nodes).
 * <br>
 * - Edit a node or edge with a double button 1 click on the item.
 * <br>
 * - Drag a node with a button 1 drag on a node.
 * <br>
 * - Select a node with a single button 1 click on a node or a button 1 drag on the background,
 *   add new nodes to the selection by pressing the control key whilst selecting.
 * <br>
 * - Delete the selection with Del or Backspace
 *
 * @author Peter Rodgers
 */
public class GraphPanel extends JPanel implements MouseInputListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	public final Color PANELBACKGROUNDCOLOR = Color.white;
	public final Color SELECTEDPANELAREACOLOR = Color.gray;
	public static final Color ADDBENDCOLOR = Color.orange;
    public final BasicStroke SELECTEDPANELAREASTROKE = new BasicStroke(1.0f);
	public final String LABELFONTNAME = "Arial";
	public final int LABELFONTSTYLE = Font.BOLD;
	public final int LABELFONTSIZE = 12;
	public static final Dimension BUTTONSIZE = new Dimension(78,32);
	public static final Dimension LARGEBUTTONSIZE = new Dimension(116,32);
	public static final Point ZEROOFFSET = new Point(0,0);
	public static final int OFFSETINCREMENT = 5;
	
	protected boolean showGraph = true;
	protected boolean showEdgeDirection = true;
	protected boolean showEdgeLabel = true;
	protected boolean showNodeLabel = true;
	/** Indicates if parallel edges should be separated when displayed. */
	protected boolean separateParallel = true;
	/** Stops the panel from being redrawn */
	protected boolean forceNoRedraw = false;
	private Graph graph;
	private ArrayList<GraphDrawer> graphDrawerList = new ArrayList<GraphDrawer>();
	private ArrayList<GraphUtility> graphUtilityList = new ArrayList<GraphUtility>();
	private ArrayList<GraphView> graphViewList = new ArrayList<GraphView>();
	private ArrayList<GraphExperiment> graphExperimentList = new ArrayList<GraphExperiment>();
	protected GraphSelection selection;
	protected boolean dragSelectionFlag = false;
	protected Node dragNode = null;
	protected Node selectNode = null;
	protected Edge selectEdge = null;
	protected Node newEdgeNode = null;
	protected Point newEdgePoint = null;
	protected Point pressedPoint = null;
	protected Point lastPoint = null;
	protected Point dragSelectPoint = null;
	protected Edge addBendEdge = null;
	protected Point addBendPoint1 = null;
	protected Point addBendPoint2 = null;
	protected Point sharedBendPoint = null;
	protected Frame containerFrame = null;
	protected Applet containerApplet = null;
	protected Color panelBackgroundColor = PANELBACKGROUNDCOLOR;
	protected Color selectedPanelAreaColor = SELECTEDPANELAREACOLOR;
	protected BasicStroke selectedPanelAreaStroke = SELECTEDPANELAREASTROKE;

	public GraphPanel(Graph inGraph, Applet inApplet) {
		super();
		graph = inGraph;
		containerFrame = null;
		containerApplet = inApplet;
		setup(graph);
	}

	public GraphPanel(Graph inGraph, Frame inContainerFrame) {
		super();
		graph = inGraph;
		containerFrame = inContainerFrame;
		containerApplet = null;
		setup(graph);
	}
	
	protected void setup(Graph graph) {
		selection = new GraphSelection(graph);
		setBackground(panelBackgroundColor);
		addMouseListener(this);
		addKeyListener(this);
	}

	public boolean getShowGraph() {return showGraph;}
	public boolean getShowEdgeDirection() {return showEdgeDirection;}
	public boolean getShowEdgeLabel() {return showEdgeLabel;}
	public boolean getShowNodeLabel() {return showNodeLabel;}
	public boolean getSeparateParallel() {return separateParallel;}
	public boolean getForceNoRedraw() {return forceNoRedraw;}
	public Graph getGraph() {return graph;}
	public GraphSelection getSelection() {return selection;}
	public ArrayList<GraphDrawer> getGraphDrawerList() {return graphDrawerList;}
	public ArrayList<GraphUtility> getGraphUtilityList() {return graphUtilityList;}
	public ArrayList<GraphView> getGraphViewList() {return graphViewList;}
	public ArrayList<GraphExperiment> getGraphExperimentList() {return graphExperimentList;}
	public Frame getContainerFrame() {return containerFrame;}

	/**
	 * Clear the selection when showing or hiding the graph.
	 */
	public void setShowGraph(boolean flag) {
		// if it is going to change, clear the partial edits
		if(showGraph != flag) {
			showGraph = flag;
			selection.clear();
			
			// deal with drag nodes, creating edges and creating new
			newEdgeNode = null;
			dragNode = null;
			newEdgeNode = null;
			newEdgePoint = null;
			dragSelectPoint = null;
		}
	}

	public void setShowEdgeDirection(boolean flag) {showEdgeDirection = flag;}
	public void setShowEdgeLabel(boolean flag) {showEdgeLabel = flag;}
	public void setShowNodeLabel(boolean flag) {showNodeLabel = flag;}
	public void setSeparateParallel(boolean flag) {separateParallel = flag;}
	public void setForceNoRedraw(boolean flag) {forceNoRedraw = flag;}
	public void setGraph(Graph g) {
		graph = g;
		repaint();
	}

	/** Add a drawing algorithm to the panel. */
	public void addGraphDrawer(GraphDrawer gd) {
		graphDrawerList.add(gd);
		gd.setGraphPanel(this);
	}

	/** Removes a drawing algorithm from the panel. */
	public void removeGraphDrawer(GraphDrawer gd) {
		graphDrawerList.remove(gd);
		gd.setGraphPanel(null);
	}

	/** Add a graph utility to the panel. */
	public void addGraphUtility(GraphUtility gu) {
		graphUtilityList.add(gu);
		gu.setGraphPanel(this);
	}

	/** Removes a graph utility from the panel. */
	public void removeGraphUtility(GraphUtility gu) {
		graphUtilityList.remove(gu);
		gu.setGraphPanel(null);
	}

	/** Add a graph view to the panel. */
	public void addGraphView(GraphView gv) {
		graphViewList.add(gv);
		gv.setGraphPanel(this);
	}

	/** Removes a graph view from the panel. */
	public void removeGraphView(GraphView gv) {
		graphViewList.remove(gv);
		gv.setGraphPanel(null);
	}

	/** Add a graph experiment to the panel. */
	public void addGraphExperiment(GraphExperiment ge) {
		graphExperimentList.add(ge);
		ge.setGraphPanel(this);
	}

	/** Removes a graph experiment from the panel. */
	public void removeGraphExperiment(GraphExperiment ge) {
		graphExperimentList.remove(ge);
		ge.setGraphPanel(null);
	}


	public void paintComponent(Graphics g) {
		if(forceNoRedraw) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D) g;
	
		//paint background
		super.paintComponent(g2);
		
		if(showGraph) {
			//draw the edges
			if(!separateParallel) {
				paintOverlaidEdges(g2,graph);
			} else {
				paintSeparateEdges(g2,graph);
			}
	
			//draw the new edge drag
			if (newEdgePoint != null) {
				g2.setColor(selectedPanelAreaColor);
				Point centre = newEdgeNode.getCentre();
				g2.drawLine(centre.x,centre.y,newEdgePoint.x,newEdgePoint.y);
			}
	
			//draw the nodes
			for(Node n : graph.getNodes()) {
				paintNode(g2,n);
			}
		}

		// draw the area selection
		if (dragSelectPoint != null) {
			g2.setColor(selectedPanelAreaColor);
	        g2.setStroke(selectedPanelAreaStroke);
			Shape r = convertPointsToRectangle(pressedPoint,dragSelectPoint);
			g2.draw(r);
		}		
		
		// draw the new bend point lines
		if(sharedBendPoint != null) {
			g2.setColor(ADDBENDCOLOR);
			g2.drawLine(sharedBendPoint.x,sharedBendPoint.y,addBendPoint1.x,addBendPoint1.y);
			g2.drawLine(sharedBendPoint.x,sharedBendPoint.y,addBendPoint2.x,addBendPoint2.y);
		}

	}


	/** This is used when parallel edges are overlaid */
	protected void paintOverlaidEdges(Graphics2D g2, Graph g) {

		for(Edge e : graph.getEdges()) {
			paintEdge(g2,e,ZEROOFFSET);
		}
	}
	
	
	/** This is used when parallel edges are displayed separately */
	protected void paintSeparateEdges(Graphics2D g2, Graph g) {

		// set up the lists of parallel edges
		ParallelEdgeList parallelList = new ParallelEdgeList(g);

		// iterate through the lists displaying the edges
		// first consider any edge type order in the nodes neighbouring the parallel edges.
		// second order by edge type priority
		// third, choose randomly

		parallelList.setAllSorted(false);

		for(ParallelEdgeTuple tuple : parallelList.getParallelList()) {
			ParallelEdgeTuple sortedTuple = null;

			// check for current order from neighbouring nodes
			//sortedTuple = getSortedNeigbour(tuple);
			// TBD this will order the edges based on the neighbours ordering. Is this needed?

			// order by sorting
			if(sortedTuple == null) {
				tuple.sortList();
			}

			// set up the offset values

			Node n1 = tuple.getFromNode();
			Node n2 = tuple.getToNode();

			double x = n1.getX()-n2.getX();
			double y = n1.getY()-n2.getY();

			double incrementX = 0;
			double incrementY = 0;
			double divisor = Math.abs(x)+Math.abs(y);

			if (divisor != 0) {
				incrementX = y/divisor;
				incrementY = -x/divisor;
			}

			incrementX *= OFFSETINCREMENT;
			incrementY *= OFFSETINCREMENT;

			// find a sensible starting offset
			double numberOfEdges = tuple.getList().size();
			Point offset = new Point((int)(-((numberOfEdges-1)*incrementX)/2),(int)(-((numberOfEdges-1)*incrementY)/2));

			// display the edges given the order
			for(Edge e : tuple.getList()) {
				paintEdge(g2,e,offset);
				offset.x += (int)incrementX;
				offset.y += (int)incrementY;
			}
			tuple.setSorted(true);
		}
	}

	
	/** Draws an edge on the graphics */
	public void paintEdge(Graphics2D g2, Edge e, Point offset) {

		EdgeType et = e.getType();

		if(!selection.contains(e)) {
			g2.setColor(et.getLineColor());
		} else {
			g2.setColor(et.getSelectedLineColor());
		}
		if(!selection.contains(e)) {
	        g2.setStroke(et.getStroke());
		} else {
	        g2.setStroke(et.getSelectedStroke());
		}

		Shape edgeShape = e.generateShape(offset);
		g2.draw(edgeShape);

		// draw the arrow if required
		if(et.getDirected() && showEdgeDirection) {

			// TODO take account of edge bends, put the arrow on the the middle edge bend

			int n1X = e.getFrom().getCentre().x+offset.x;
			int n1Y = e.getFrom().getCentre().y+offset.y;
			int n2X = e.getTo().getCentre().x+offset.x;
			int n2Y = e.getTo().getCentre().y+offset.y;
			int tipX = 0;
			int tipY = 0;
				tipX = (int)(n2X+(n1X-n2X)/1.5); // 1.5 puts the arrow 1/3 down the edge, avoiding the label
				tipY = (int)(n2Y+(n1Y-n2Y)/1.5);
			
			Point2D.Double tipPoint = new Point2D.Double(tipX,tipY);

			double angle = Util.calculateAngle(n1X, n1Y, n2X, n2Y);
			double reverseAngle = angle-180;
			double line1Angle = reverseAngle - et.getArrowAngle()/2;
			double line2Angle = reverseAngle + et.getArrowAngle()/2;
			
			Point2D.Double line1Point = Util.movePoint(tipPoint,et.getArrowLength(),line1Angle);
			Point2D.Double line2Point = Util.movePoint(tipPoint,et.getArrowLength(),line2Angle);
			
			Line2D.Double arrowLine1Shape = new Line2D.Double(tipPoint,line1Point);
			Line2D.Double arrowLine2Shape = new Line2D.Double(tipPoint,line2Point);
			g2.draw(arrowLine1Shape);
			g2.draw(arrowLine2Shape);
		}
		
		// draw the label if required
		if(!e.getLabel().equals("") && showEdgeLabel) {

			//if there are edge bends, put the label at the middle edge bend

			int n1X = e.getFrom().getCentre().x+offset.x;
			int n1Y = e.getFrom().getCentre().y+offset.y;
			int n2X = e.getTo().getCentre().x+offset.x;
			int n2Y = e.getTo().getCentre().y+offset.y;
			int x = 0;
			int y = 0;
			if(n1X-n2X > 0) {
				x = n2X+(n1X-n2X)/2;
			} else {
				x = n1X+(n2X-n1X)/2;
			}
			if(n1Y-n2Y > 0) {
				y = n2Y+(n1Y-n2Y)/2;
			} else {
				y = n1Y+(n2Y-n1Y)/2;
			}
			
			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(e.getLabel(), font, frc);

			g2.setColor(PANELBACKGROUNDCOLOR);
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+x-2, bounds.getY()+y-2, bounds.getWidth()+4,bounds.getHeight()+4);
			g2.fill(bounds);

			if(!selection.contains(e)) {
				g2.setColor(et.getTextColor());
			} else {
				g2.setColor(et.getSelectedTextColor());
			}
			labelLayout.draw(g2,x,y);
		}

	}
	
	
	/** Draws a node on the graphics */
	public void paintNode(Graphics2D g2, Node n) {

		NodeType nt = n.getType();
		Point centre = n.getCentre();

		if(!selection.contains(n)) {
			g2.setColor(nt.getFillColor());
		} else {
			g2.setColor(nt.getSelectedFillColor());
		}
		if(!selection.contains(n)) {
			g2.setStroke(nt.getStroke());
		} else {
			g2.setStroke(nt.getSelectedStroke());
		}

		Shape nodeShape = n.generateShape();
		g2.fill(nodeShape);

		if(!selection.contains(n)) {
			g2.setColor(nt.getBorderColor());
		} else {
			g2.setColor(nt.getSelectedBorderColor());
		}

		g2.draw(nodeShape);

		if(!n.getLabel().equals("") && showNodeLabel) {
			if(!selection.contains(n)) {
				g2.setColor(nt.getTextColor());
			} else {
				g2.setColor(nt.getSelectedTextColor());
			}

			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(n.getLabel(), font, frc);

			Rectangle2D labelBounds = labelLayout.getBounds();
			int labelX = (int)Math.round(centre.x-(labelBounds.getWidth()/2));
			int labelY = (int)Math.round(centre.y+(labelBounds.getHeight()/2));

			labelLayout.draw(g2,labelX,labelY);
		}
		
	}


	/**
	 * This converts two points to a rectangle, with first two
	 * coordinates always the top left of the rectangle
	 */
	public Shape convertPointsToRectangle (Point p1, Point p2) {
		int x1,x2,y1,y2;

		if (p1.x < p2.x) {
			x1 = p1.x;
			x2 = p2.x;
		} else {
			x1 = p2.x;
			x2 = p1.x;
		}
		if (p1.y < p2.y) {
			y1 = p1.y;
			y2 = p2.y;
		} else {
			y1 = p2.y;
			y2 = p1.y;
		}
		return (new Rectangle2D.Double(x1,y1,x2-x1,y2-y1));

	}


	public void mouseClicked(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}
		// left button only
		if (!SwingUtilities.isLeftMouseButton(event)) {
			selection.clear();
			repaint();
			return;
		}
		selectNode = graph.getNodeNearPoint(event.getPoint(),1);
		if (selectNode == null) {

			selectEdge = graph.getEdgeNearPoint(event.getPoint(),3);
			if (selectEdge == null) {
				// no node or edge selected so add a node on double click
				if (event.getClickCount() > 1) {
					graph.addNode(new Node(new Point(event.getPoint())));
					selection.clear();
				} else {
					// single click might have been a missed selection
					if (!event.isControlDown()) {
						selection.clear();
					}
				}
				repaint();
			} else {
				if (event.getClickCount() == 1) {
					// edge selected, so add it to the selection
					if (!event.isControlDown()) {
						selection.clear();
					}
					selection.addEdge(selectEdge);
					repaint();
				} else {
					// edit edge dialog on double click
					ArrayList<Edge> el = new ArrayList<Edge>();
					el.add(selectEdge);
					editEdges(el);
				}
			}
		} else {
			if (event.getClickCount() == 1) {
				// node selected
				if (!event.isControlDown()) {
					selection.clear();
				}
				selection.addNode(selectNode);
				repaint();
			} else {
				// edit node dialog on double click
				ArrayList<Node> nl = new ArrayList<Node>();
				nl.add(selectNode);
				editNodes(nl);
			}
			selectNode = null;
		}
		event.consume();
	}
	
	
	/** Call this to edit nodes in the graph panel */
	public void editNodes(ArrayList<Node> nodes) {
		if(nodes.size() == 0) {
			return;
		}
		new EditNodeDialog(nodes,this,containerFrame,selection);
	}
	/** Call this to edit edges in the graph panel */
	public void editEdges(ArrayList<Edge> edges) {
		if(edges.size() == 0) {
			return;
		}
		new EditEdgeDialog(edges,this,containerFrame,selection);
	}
	/** Call this to edit all edge types */
	public void editEdgeTypes() {
		new ManageEdgeTypesDialog(this,containerFrame);
		repaint();
	}

	/** Call this to edit all node types */
	public void editNodeTypes() {
		new ManageNodeTypesDialog(this,containerFrame);
		repaint();
	}


	/** Call this to move the graph around */
	public void moveGraph() {

		new MoveGraphFrame(this);
	}

	/** Call this to allow the user to add an edge bend to the selected edge by clicking on the panel. */
	public void addEdgeBend() {

		if(selection.getEdges().size() != 1) {
			return;
		}
		addBendEdge = (Edge)selection.getEdges().get(0);
		addBendPoint1 = addBendEdge.getFrom().getCentre();
		ArrayList<Point> bends = addBendEdge.getBends();
		if(bends != null && bends.size() != 0) {
			addBendPoint1 = bends.get(bends.size()-1);
		}
		addBendPoint2 = addBendEdge.getTo().getCentre();
		
		addMouseMotionListener(this);
		
		repaint();
	}


	/** Call this to remove all edge bends from selected edges */
	public void removeEdgeBends() {
		for(Edge e : selection.getEdges()) {
			e.removeAllBends();
		}
		repaint();
	}


	public void mousePressed(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}
		requestFocus();
		pressedPoint = event.getPoint();
		lastPoint = event.getPoint();
		addMouseMotionListener(this);
		if (SwingUtilities.isLeftMouseButton(event)) {

			if (addBendEdge != null) {
				addBendEdge.addBend(event.getPoint());
				addBendEdge = null;
				addBendPoint1 = null;
				addBendPoint2 = null;
				sharedBendPoint = null;
				repaint();
				event.consume();
				return;
			}

			Node chosenNode = graph.getNodeNearPoint(pressedPoint,1);
			if(chosenNode != null) {
				if(selection.contains(chosenNode)) {
					// if its a selected node then we are dragging a selection
					dragSelectionFlag = true;
				} else {
					// otherwise just drag the node
					dragNode = chosenNode;
				}
			} else {
				// no node chosen, so drag an area selection
				dragSelectPoint = new Point(event.getPoint());
			}
			graph.moveNodeToEnd(dragNode);
			repaint();
		}
		if (SwingUtilities.isRightMouseButton(event)) {
			newEdgeNode = graph.closestNode(pressedPoint);
			graph.moveNodeToEnd(newEdgeNode);
			newEdgePoint = new Point(event.getPoint());
			repaint();
		}
		event.consume();
	}



	public void mouseReleased(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}

		removeMouseMotionListener(this);
		if(pressedPoint.distance(event.getPoint()) < 1) {
			// dont do anything if no drag occurred
			dragSelectionFlag = false;
			dragNode = null;
			dragSelectPoint = null;
			newEdgeNode = null;
			newEdgePoint = null;
			return;
		}

		addBendEdge = null;
		addBendPoint1 = null;
		addBendPoint2 = null;
		sharedBendPoint = null;

		// select all in the area
		if (dragSelectPoint != null) {

			// if no control key modifier, then replace current selection
			if (!event.isControlDown()) {
				selection.clear();
			}

			Shape r = convertPointsToRectangle(pressedPoint,event.getPoint());

			for(Node node : graph.getNodes()) {
				Point centre = node.getCentre();

				if(r.contains(centre) && !selection.contains(node)) {
					selection.addNode(node);
				}
			}

			for(Edge edge : graph.getEdges()) {
				Rectangle edgeBounds = edge.shape().getBounds();

				//rectangles with zero dimension dont get included, so quick hack
				edgeBounds.grow(1,1);

				if(r.contains(edgeBounds) && !selection.contains(edge)) {
					selection.addEdge(edge);
				}
			}

			dragSelectPoint = null;
			repaint();
		}

		// finish the selection drag
		if (dragSelectionFlag) {
			dragSelectionFlag = false;
			repaint();
		}

		// finish the node drag
		if (dragNode != null) {
			dragNode = null;
			repaint();
		}

		// finish adding an edge
		if (newEdgeNode != null) {
			Node toNode = graph.closestNode(event.getPoint());
			// dont add a self sourcing edge
			if (newEdgeNode != toNode) {
				graph.addEdge(new Edge(newEdgeNode,toNode));
			}
			newEdgeNode = null;
			newEdgePoint = null;
			graph.moveNodeToEnd(toNode);
			repaint();
		}
		event.consume();
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}
		
		if (dragSelectPoint != null) {
			dragSelectPoint = event.getPoint();
			repaint();
		}

		if (newEdgePoint != null) {
			newEdgePoint = event.getPoint();
			repaint();
		}

		if (dragSelectionFlag) {
			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			for(Node n : selection.getNodes()) {
				Point centre = n.getCentre();
				centre.setLocation(centre.x+deltaX,centre.y+deltaY);
			}
			lastPoint = event.getPoint();

			repaint();
		}

		if (dragNode != null) {

			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			Point centre = dragNode.getCentre();
			centre.setLocation(centre.x+deltaX,centre.y+deltaY);

			lastPoint = event.getPoint();

			repaint();
		}
		event.consume();
	}


	public void mouseMoved(MouseEvent event) {
		if(addBendPoint1 != null && addBendPoint2 != null) {
			sharedBendPoint = event.getPoint();
			repaint();
		}
//		event.consume();
	}


	public void keyTyped(KeyEvent event) {
	}


	public void keyPressed(KeyEvent event) {
	}


	public void keyReleased(KeyEvent event) {
		// this stuff would be in keyTyped, but it doesnt register delete
		if((event.getKeyChar() == KeyEvent.VK_BACK_SPACE) || (event.getKeyChar() == KeyEvent.VK_DELETE)) {
			graph.removeEdges(selection.getEdges());
			graph.removeNodes(selection.getNodes());
			selection.clear();
			repaint();
		}
	}

}



