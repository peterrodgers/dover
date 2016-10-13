package uk.ac.kent.displayGraph.drawers;

import java.awt.event.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle,
 * then apply the spring embedder.
 *
 * @author Peter Rodgers
 */

public class BasicSpringEmbedder extends GraphDrawer {


/** Trivial constructor. */
	public BasicSpringEmbedder() {
		super(KeyEvent.VK_S,"Spring Embedder");
	}

/** Trivial constructor. */
	public BasicSpringEmbedder(int key, String s) {
		super(key,s);
	}

	public BasicSpringEmbedder(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

	public void layout() {

//		 standard spring embedder with randomization and no animation
		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
//		se.setAnimateFlag(true);
		se.setAnimateFlag(false);
		se.setIterations(1000);
		se.setTimeLimit(2000);
		if(getGraphPanel() != null) {
			se.setGraphPanel(getGraphPanel());
		}
		se.layout();
	}


	
	
	
}
