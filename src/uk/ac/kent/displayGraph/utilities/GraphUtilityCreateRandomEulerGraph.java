package uk.ac.kent.displayGraph.utilities;

import java.awt.*;
import java.awt.event.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class GraphUtilityCreateRandomEulerGraph extends GraphUtility {


	protected int numberOfNodes = 15;
	protected int numberOfEdges = 30;

/** Trivial constructor. */
	public GraphUtilityCreateRandomEulerGraph() {
		super(KeyEvent.VK_C,"Create Random Euler Graph");
	}

/** Trivial constructor. */
	public GraphUtilityCreateRandomEulerGraph(int key, String s) {
		super(key,s);
	}

/** Full Constructor. */
	public GraphUtilityCreateRandomEulerGraph(int key, String s, int mnemonic, int nodes, int edges) {
		super(key,s,mnemonic);
		setNumberOfNodes(nodes);
		setNumberOfEdges(edges);
	}

/** Trival accessor. */
	public int getNumberOfNodes() {return numberOfNodes;}
/** Trival accessor. */
	public int getNumberOfEdges() {return numberOfEdges;}

/** Trival mutator. */
	public void setNumberOfNodes(int nodes) {numberOfNodes = nodes;}
/** Trival mutator. */
	public void setNumberOfEdges(int edges) {numberOfEdges = edges;}


/** Creates the graph. */
	public void apply() {

// this creates a graph with lots of loops
		getGraph().generateRandomEulerGraph(numberOfNodes,numberOfEdges,false,false);

// this places the nodes randomly
		getGraph().randomizeNodePoints(new Point(50,50),500,500);

// this weights the edges
		GraphUtilityEdgeLabelRandomizer.randomize(getGraph(),1,5);

		getGraphPanel().update(getGraphPanel().getGraphics());

	}

}
