package uk.ac.kent.displayGraph.drawers;

import java.awt.event.KeyEvent;


/**
 * Randomize the location of the nodes in a graph in a given rectangle,
 * then apply the spring embedder.
 *
 * @author Peter Rodgers
 */

public class GravitySpringEmbedder extends GraphDrawer {


/** Trivial constructor. */
	public GravitySpringEmbedder() {
		super(KeyEvent.VK_G,"Gravity Spring Embedder");
	}

/** Trivial constructor. */
	public GravitySpringEmbedder(int key, String s) {
		super(key,s);
	}

	public GravitySpringEmbedder(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

	public void layout() {

//		 standard spring embedder with randomization and no animation
		GraphDrawerGravitySpringEmbedder se = new GraphDrawerGravitySpringEmbedder(KeyEvent.VK_G,"Gravity Spring Embedder - randomize, no animation",true);
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
