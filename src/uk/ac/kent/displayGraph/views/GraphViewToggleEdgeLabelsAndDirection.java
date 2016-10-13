package uk.ac.kent.displayGraph.views;


import java.awt.event.*;

import uk.ac.kent.displayGraph.GraphPanel;

/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class GraphViewToggleEdgeLabelsAndDirection extends GraphView {

/** Trivial constructor. */
	public GraphViewToggleEdgeLabelsAndDirection() {
		super(KeyEvent.VK_C,"Cycle Items Displayed",KeyEvent.VK_C);
	}


/** Trivial constructor. */
	public GraphViewToggleEdgeLabelsAndDirection(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphViewToggleEdgeLabelsAndDirection(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	/**
	 * Cycle between three states - show both graph and contours, show
	 * just graph, show just contours.
	 */
	public void view() {

		GraphPanel panel = getGraphPanel();

		if(panel.getShowEdgeDirection()) { // can only test on one of the booleans
			panel.setShowEdgeLabel(false);
			panel.setShowEdgeDirection(false);
		} else {
			panel.setShowEdgeLabel(true);
			panel.setShowEdgeDirection(true);
		}

	}
}

