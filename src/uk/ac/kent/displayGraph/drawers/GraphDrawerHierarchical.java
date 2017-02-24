package uk.ac.kent.displayGraph.drawers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.GraphSelection;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.NodeType;
import uk.ac.kent.displayGraph.comparators.NodeScoreComparator;
import uk.ac.kent.displayGraph.utilities.GraphUtilityRemoveDummyNodes;

/**
 * A varient of Sugiyamas Algorithm with dummy nodes.
 *
 * The root of an adjacency graph file is the first node.
 * Using selection changes the root. When one node is selected
 * it becomes the root.
 *
 * The method works by taking the root and assigning layers
 * by placing nodes as close to the root as possible.
 * The nodes are laid out in their layers in the Y direction
 * and then laid out in the X direction based on barycentres
 * so that firstly children are placed at the X barycentre of their
 * parents, and then parents are placed at the Y barycentre of
 * their children. This process is repeated in order to get a
 * stable result.
 *
 * The code is a bit of a mess - layers are stored in both
 * the score attribute of nodes, and in the layers attribute
 * of this class, which is a list of lists, each element
 * of layers is a list of nodes that is a layer. There are places
 * where this dual representation could be used more effectively.
 *
 * @author Peter Rodgers
 */
public class GraphDrawerHierarchical extends GraphDrawer {

	public final int DEFAULTGRIDX = 50;
	public final int DEFAULTGRIDY = 50;
/** number of times the process goes up and down the layers */
	public final int M = 2;

	protected int gridX = DEFAULTGRIDX;
	protected int gridY = DEFAULTGRIDY;
/** defines the way up of the drawing. A true value puts the root top */
	protected boolean topFirst = true;
	protected boolean animate = false;
	protected ArrayList<ArrayList<Node>> layers = null;
/** node type for dummy nodes */
	protected static NodeType dummy = new NodeType("dummy");

	public GraphDrawerHierarchical() {
		super(KeyEvent.VK_H, "Hierachical");
	}

	public GraphDrawerHierarchical(int key, String s) {
		super(key, s);
	}

	public GraphDrawerHierarchical(int key, String s, int mnemonic, boolean topFirst) {
		super(key, s, mnemonic);
		setTopFirst(topFirst);
	}

	public GraphDrawerHierarchical(int key, String s, int mnemonic, boolean topFirst, boolean animate) {
		super(key, s, mnemonic);
		setTopFirst(topFirst);
		setAnimate(animate);
	}

/** Trivial accessor. */
	public boolean getTopFirst() {return topFirst;}
/** Trivial accessor. */
	public boolean getAnimate() {return animate;}
/** Trivial modifier. */
	public void setTopFirst(boolean inTopFirst) {topFirst = inTopFirst;}
/** Trivial modifier. */
	public void setAnimate(boolean inAnimate) {animate = inAnimate;}

/**
 * The layout algorithm.
 */
	public void layout() {

// set up the dummy nodes
		dummy.setShapeString("Rectangle");
		dummy.setFillColor(Color.blue);
		dummy.setBorderColor(Color.blue);
		dummy.setWidth(5);
		dummy.setHeight(5);


		GraphPanel gp = getGraphPanel();
		Graph g = getGraph();

		ArrayList<Node> nodes = g.getNodes();

		int gpWidth = gp.getWidth();
		int gpHeight = gp.getHeight();

// no need to draw an empty graph
		if(nodes.size() == 0) {
			return;
		}

// remove any current dummy nodes
		GraphUtilityRemoveDummyNodes.removeDummyNodes(g);

// root is first node in the selected nodes
// which means selecting a single node makes it the root
// If there is no selected nodes then we
// assume the root is first in the node list, which should be
// the first node in the file if selections have yet
// been made
		Node root;
		GraphSelection gs = gp.getSelection();
		ArrayList<Node> selectedNodes = gs.getNodes();
		if(selectedNodes.size() > 0) {
			root = (Node)selectedNodes.get(0);
		} else {
			root = (Node)nodes.get(0);
		}

// find the node layers
		layers = assignLayers(g,root);

// make the nodes fit the space by altering the grid
		setGrid(gpWidth, gpHeight);


// add dummy nodes
		addDummyNodes(g);

// set the Y coordinates
		setNodeYCoordinates(g);

// reasonable value for the root x position
// this will allow the algorithm to be centred
		int newX = snap(gpWidth/2,gridX);
		if(!animate) {
			root.setX(newX);
		} else {
			root.setX(newX,getGraphPanel());
		}

// set the X coordinates
// iterate around the cycle several times to ensure a stable result
		for(int j=1; j<=M; j++) {
			assignParentBaryCentres(g);
			assignChildBaryCentres(g);
		}

	}


/** add dummy nodes */
	public void addDummyNodes(Graph g) {

		ArrayList<Node> nodeList = new ArrayList<Node>(g.getNodes());
		for(Node from : nodeList) {
			HashSet<Edge> edgeList = new HashSet<Edge>(from.connectingEdges());
			for(Edge e : edgeList) {
				Node to = e.getOppositeEnd(from);
				addDummyNode(g,from,to,e);
			}
		}
	}

/** Add a dummy node */
	public void addDummyNode(Graph g, Node from, Node to, Edge connectingEdge) {

		Node nextNode = from;
		while(nextNode.getScore() +1 < to.getScore()) {
// create the Node
			Node newNode = new Node(dummy);
			g.addNode(newNode);
			newNode.setScore(nextNode.getScore()+1);

			newNode.setX((from.getX()+to.getX())/2);
			newNode.setY((from.getY()+to.getY())/2);

			connectingEdge.setFromTo(nextNode, newNode);

			Edge e = new Edge(newNode, to);
			g.addEdge(e);

			nextNode = newNode;
			connectingEdge = e;
		}
	}


/** Assigning nodes in the hierarchy */
	public ArrayList<ArrayList<Node>> assignLayers(Graph g, Node root) {
		g.setNodesVisited(false);
		g.setNodesScores(0.0);
		ArrayList<ArrayList<Node>> layerList = new ArrayList<ArrayList<Node>>();

		int layerNumber = 1;

		ArrayList<Node> nextLayer;
		ArrayList<Node> currentLayer = new ArrayList<Node>();
		currentLayer.add(root);
		root.setVisited(true);
		root.setScore(layerNumber);
		layerNumber++;
		layerList.add(currentLayer);

		while(currentLayer.size() != 0) {
			nextLayer = new ArrayList<Node>();

			for(Node n : currentLayer) {
				ArrayList<Node> addNodes = n.unvisitedConnectingNodes();
				nextLayer.addAll(addNodes);
				g.setNodesVisited(addNodes,true);
				g.setNodesScores(addNodes,layerNumber);
			}
			dealWithConnectedNodes(nextLayer);

			if(nextLayer.size() !=0) {
				layerList.add(nextLayer);
				layerNumber++;
			}

			currentLayer = nextLayer;
		}

		return(layerList);
	}



/** line up the nodes with the average of their parents */
	public void assignParentBaryCentres(Graph g) {

		int layerNumber = 2;

		ArrayList<Node> layerNodes = getNodesInLayer(g,layerNumber);
		while(layerNodes.size() != 0) {

			for(Node n : layerNodes) {

				ArrayList<Node> parents = getConnectedAbove(n);

				int newX = getXBarycentre(n,parents);
				newX = snap(newX,gridX);

				if(!animate) {
					n.setX(newX);
				} else {
					n.setX(newX,getGraphPanel());
				}

				conflictResolution(layerNodes,gridX);
			}

			layerNumber++;
			layerNodes = getNodesInLayer(g,layerNumber);
		}
	}


/** line up the nodes with the average of their children */
	public void assignChildBaryCentres(Graph g) {

		int layerNumber = (int)getMaxNodeScore(g);
//		layerNumber--;

		ArrayList<Node> layerNodes = getNodesInLayer(g,layerNumber);
		while(layerNodes.size() != 0) {

			for(Node n : layerNodes) {

				ArrayList<Node> children = getConnectedBelow(n);

				int newX = getXBarycentre(n,children);
				newX = snap(newX,gridX);

				if(!animate) {
					n.setX(newX);
				} else {
					n.setX(newX,getGraphPanel());
				}


				conflictResolution(layerNodes,gridX);
			}

			layerNumber--;
			layerNodes = getNodesInLayer(g,layerNumber);
		}
	}


	public ArrayList<Node> getNodesInLayer(Graph g, double layerNumber) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : g.getNodes()) {
			if(n.getScore() == layerNumber) {
				ret.add(n);
			}
		}
		return ret;
	}


/** finds the barycentre */
	public int getXBarycentre(Node node, ArrayList<Node> connected) {

		if(connected.size() == 0) {
			return(node.getX());
		}

		int total = 0;
		int count = 0;
		for(Node n : connected) {
			total = total + n.getX();
			count++;
		}

		int ret;
		if(count == 0) {
			ret = 0;
		} else {
			ret = total/count;
		}
		return(ret);
	}


/** deals with overlaps */
	public void conflictResolution(ArrayList<Node> inLayer,int grid) {
		
		ArrayList<Node> layer = new ArrayList<Node>(inLayer);
		
// sort the layer into its order in the X direction
		Collections.sort(layer,new NodeXComparator());

		ArrayList<ArrayList<Node>> layerGroups = getLayerGroups(layer);
		
		for(ArrayList<Node> group : layerGroups) {
			if(group.size() > 1) {
				findBestGroupPosition(group,layer,gridX);
			}
		}
	}
	
/*
 * Arranges a group of nodes that share the same X position
 * so that the group are in the nearest free spaces to their current X position
 * and with fewest line crossings. Both lists must be sorted by X position.
 */
	public void findBestGroupPosition(ArrayList<Node> group, ArrayList<Node> nodes, int grid) {
		final int ALLOWED_DELAY = 100; // milliseconds allowed for the exponetial loop, 100 breaks on size = 6 for my computer

		if(group.size() == 0) {
			return;
		}
		Node firstNode = (Node)group.get(0);
		int groupX = firstNode.getX();
		
		ArrayList<Integer> freePositions = findNearestFreeXLocations(group,nodes,grid);
		// search through every possible position.
		// Inefficient as it strips out duplicate values from every combination
		int size = group.size();
		int count = 0;
		long startTime = System.currentTimeMillis();
		ArrayList<Integer> bestGroupLayout = new ArrayList<Integer>();
		int bestCrossingsCount = -1;
		while(count < Math.pow(size,size)) {
			ArrayList<Integer> nextGroupLayout = getSignificanceList(count,size,size);
			if(noDuplicates(nextGroupLayout)) {
				if(bestGroupLayout.size() == 0) {
					bestGroupLayout = nextGroupLayout;
					moveNodesToFreeXPositions(group,freePositions,bestGroupLayout,false);
					bestCrossingsCount = getGraph().edgeIntersections();
				} else {
					moveNodesToFreeXPositions(group,freePositions,nextGroupLayout,false);
					int nextCrossingsCount = getGraph().edgeIntersections();
					if(nextCrossingsCount < bestCrossingsCount) {
						bestCrossingsCount = nextCrossingsCount;
					} else {
						moveNodesToFreeXPositions(group,freePositions,bestGroupLayout,false);
					}
				}
			}
			count++;
			if(System.currentTimeMillis() > startTime+ALLOWED_DELAY) {
				// this because this loop is exponential time on size,
				// if size is large this will go on for a long time
				break;
			}
		}
		if(animate) {
			for(Node n : group) {
				n.setX(groupX);
			}
			moveNodesToFreeXPositions(group,freePositions,bestGroupLayout,true);
		}
	}
	
/**
 * Moves the nodes X locations to the free position indexes given in the second arraylist.
 * Both arrayLists should be of the same size.
 */
	public void moveNodesToFreeXPositions(ArrayList<Node> nodes, ArrayList<Integer> freePositions, ArrayList<Integer> freeIndexes, boolean animateMove) {
		for(int index = 0; index < nodes.size(); index++) {
			Node n = (Node)nodes.get(index);
			Integer freeIndex = (Integer)freeIndexes.get(index);
			Integer position = (Integer)freePositions.get(freeIndex.intValue());
			if(!animateMove) {
				n.setX(position.intValue());
			} else {
				n.setX(position.intValue(),getGraphPanel());
			}
		}
		
	}

/**
 * Check to see if a sorted arrayList of Integers has duplicates
 */
	public static boolean noDuplicates (ArrayList<Integer> numbers) {
		for(int i = 0; i < numbers.size()-1; i++) {
			Integer i1 = (Integer)numbers.get(i);
			Integer i2 = (Integer)numbers.get(i+1);
			if(i1.compareTo(i2) == 0) {
				return false;
			}
		}
		return true;
	}

/**
 * Form an arraylist of integers, with the base of the divider, least significant
 * has index 0. Pad with 0s to make a list of the given size, if the size is too
 * small, the list will chop the most significant digits.
 */
	public static ArrayList<Integer> getSignificanceList(int amount, int divider, int size) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int left = amount;
		while(ret.size() < size) {
			int remainder = left%divider;
			left -= remainder;
			left /= divider;
			ret.add(remainder);
		}
		return ret;
	}

	
/*
 * checks leftwards and rightwards to find the nearest free spaces in the nodes
 * for a group of nodes that share the same X position and with fewest line crossings.
 * Both lists must be sorted by X position.
 */
	public static ArrayList<Integer> findNearestFreeXLocations(ArrayList<Node> group, ArrayList<Node> nodes, int grid) {
		if(group.size() == 0) {
			return new ArrayList<Integer>();
		}
		Node groupNode = (Node)group.get(0);
		
		int startX = groupNode.getX();
		int currentLeft = startX;
		int currentRight = startX;
		ArrayList<Integer> freeLocations = new ArrayList<Integer>();
		freeLocations.add(startX);
		while(freeLocations.size() != group.size()) {
			currentLeft -= grid;
			currentRight += grid;
			
			boolean foundLeft = false;
			boolean foundRight = false;
			for(Node n : nodes) {
				if(currentLeft == n.getX()) {
					foundLeft = true;
				}
				if(currentRight == n.getX()) {
					foundRight = true;
				}
			}
			// favour rightwards here
			if(!foundRight) {
				freeLocations.add(currentRight);
			}
			if(!foundLeft && freeLocations.size() != group.size()) {
				freeLocations.add(currentLeft);
			}
		}
		return freeLocations;
	}

/** 
 * Returns an ArrayList of ArrayLists of nodes, where
 * each ArrayList has nodes with the same X coordinate.
 * The groups are in X coordinate order.
 */
	public static ArrayList<ArrayList<Node>> getLayerGroups(ArrayList<Node> inGraphList) {
		
		ArrayList<Node> graphList = new ArrayList<Node>(inGraphList);
		
		Collections.sort(graphList,new NodeXComparator());
		if(graphList.size() == 0) {
			return new ArrayList<ArrayList<Node>>();
		}
	
		ArrayList<ArrayList<Node>> groups = new ArrayList<ArrayList<Node>>();
		
		Node firstN = (Node)graphList.get(0);
		ArrayList<Node> nodesAtX = new ArrayList<Node>();
		nodesAtX.add(firstN);
		int currentX = firstN.getX();
		
		int index = 1;
		while(index < graphList.size()) {
			Node n = (Node)graphList.get(index);
			if(n.getX() == currentX) {
				nodesAtX.add(n);
			} else {
				groups.add(nodesAtX);
				currentX = n.getX();
				nodesAtX = new ArrayList<Node>();
				nodesAtX.add(n);
			}
			index++;
		}
		groups.add(nodesAtX);
		return groups;
	}

	

/** deals with overlaps */
/*	public void conflictResolution(ArrayList<Node> layer,int grid) {

		boolean forward = true;
		ArrayList testNodes = new ArrayList<Node>(layer);
		while(testNodes.size() != 0) {
			Node n1 = (Node)testNodes.get(0);

			boolean conflict = false;
			while(Node n2 : layer) {
				if (n1 != n2 && n1.getX() == n2.getX()) {
					int val = grid;
					if(!forward) {
						val = -grid;
					}

					if(!animate) {
						n1.setX(n1.getX()+val);
					} else {
						n1.setX(n1.getX()+val,getGraphPanel());
					}
					conflict = true;
				}
			}
			if (!conflict) {
				testNodes.remove(n1);
				if (forward) {
					forward = false;
				}
			}
		}		
	}
*/

/** place the nodes on the layers in the Y direction */
	public void setNodeYCoordinates(Graph g) {

		ArrayList<Node> nodes = g.getNodes();

// this sort means the top levels are dealt with first,
// helping the animation
		NodeScoreComparator nComp = new NodeScoreComparator();
		Collections.sort(nodes,nComp);

		double maxLayer = getMaxNodeScore(g);
		for(Node n : nodes) {
			int newY = (int)n.getScore()*gridY;
			if(!topFirst) {
				newY = (int)(1+maxLayer-n.getScore())*gridY;
			}
			if(!animate) {
				n.setY(newY);
			} else {
				n.setY(newY,getGraphPanel());
			}
		}
	}


	public double getMaxNodeScore(Graph g) {

		double ret = 0.0;
		for(Node n : g.getNodes()) {
			if(n.getScore() > ret) {
				ret = n.getScore();
			}
		}
		return(ret);
	}


/**
 * We dont want connected nodes in the same level, so
 * if a pair is found, remove one. 
 */
	protected void dealWithConnectedNodes(ArrayList<Node> nodeList) {

		ArrayList<Node> remove = new ArrayList<Node>();

		for(Node n1 : nodeList) {
			for(Node n2 : n1.connectingNodes()) {
				if((n1!=n2) && nodeList.contains(n2) && !remove.contains(n2)) {
					remove.add(n1);
				}
			}
		}
		for(Node nR : remove) {
			nR.setVisited(false);
			nodeList.remove(nR);
		}
	}


/** get the nodes in the collection that the single node attaches to */
	public ArrayList<Node> getConnectedAbove(Node node) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : node.connectingNodes()) {
			if(node.getScore() > n.getScore()) {
				ret.add(n);
			}
		}
		return(ret);
	}	

/** get the nodes in the collection that the single node attaches to */
	public ArrayList<Node> getConnectedBelow(Node node) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : node.connectingNodes()) {
			if(node.getScore() < n.getScore()) {
				ret.add(n);
			}
		}
		return(ret);
	}	


/** set a grid size so the nodes fit */
	public void setGrid(int width, int height) {

		int requiredYSpaces = (layers.size()+1);
		if(requiredYSpaces*DEFAULTGRIDY > height) {
			gridY = height/requiredYSpaces;
		} else {
			gridY = DEFAULTGRIDY;
		}

		int maxLayer = getMaxLayer();
//required spaces is a guestimate
		int requiredXSpaces = maxLayer+2;
		if(requiredXSpaces*DEFAULTGRIDX > width) {
			gridX = width/requiredXSpaces;
		} else {
			gridX = DEFAULTGRIDX;
		}

	}


/** gets the size of the largest layer */
	public int getMaxLayer() {
		int ret = 0;
		for(ArrayList<Node> al : layers) {
			if(al.size()>ret) {
				ret = al.size();
			}
		}
		return(ret);
	}


/** returns a value snapping to the grid value*/
	public int snap(int val, int grid) {
		return((val/grid)*grid);
	}


	/**
	 * Orders nodes by their score.
	 */
	static class NodeXComparator implements Comparator<Node> {

		public int compare(Node n1, Node n2) {
			Integer i1 = n1.getX();
			Integer i2 = n2.getX();
			return i1.compareTo(i2);
		}
	}

}



