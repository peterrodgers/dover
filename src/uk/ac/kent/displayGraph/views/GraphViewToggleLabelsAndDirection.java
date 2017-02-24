package uk.ac.kent.displayGraph.views;


import java.awt.event.KeyEvent;

import uk.ac.kent.displayGraph.GraphPanel;

/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class GraphViewToggleLabelsAndDirection extends GraphView {

/** Trivial constructor. */
	public GraphViewToggleLabelsAndDirection() {
		super(KeyEvent.VK_K,"Toggle Label And Direction Display",KeyEvent.VK_L);
	}


/** Trivial constructor. */
	public GraphViewToggleLabelsAndDirection(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphViewToggleLabelsAndDirection(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {

		GraphPanel panel = getGraphPanel();

		if(panel.getShowEdgeDirection()) { // can only test on one of the booleans
			panel.setShowNodeLabel(false);
			panel.setShowEdgeLabel(false);
			panel.setShowEdgeDirection(false);
		} else {
			panel.setShowNodeLabel(true);
			panel.setShowEdgeLabel(true);
			panel.setShowEdgeDirection(true);
		}

	}
}

