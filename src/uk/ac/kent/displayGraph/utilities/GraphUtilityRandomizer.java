package uk.ac.kent.displayGraph.utilities;

import java.awt.*;
import java.awt.event.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class GraphUtilityRandomizer extends GraphUtility {

	protected int topX = 50;
	protected int topY = 50;
	protected int bottomX = 400;
	protected int bottomY = 400;
	protected boolean redrawFlag = true;

/** Trivial constructor. */
	public GraphUtilityRandomizer() {
		super(KeyEvent.VK_R,"Graph Randomizer",KeyEvent.VK_R);
	}

/** Trivial constructor. */
	public GraphUtilityRandomizer(int key, String s) {
		super(key,s);
	}

/** Full Constructor. */
	public GraphUtilityRandomizer(int key, String s,int mnemonic, int topX, int topY, int bottomX, int bottomY, boolean redrawFlag) {
		super(key,s,mnemonic);
		setTopX(topX);
		setTopY(topY);
		setBottomX(bottomX);
		setBottomY(bottomY);
		setRedrawFlag(redrawFlag);
	}

/** Trival accessor. */
	public int getTopX() {return topX;}
/** Trival accessor. */
	public int getTopY() {return topY;}
/** Trival accessor. */
	public int getBottomX() {return bottomX;}
/** Trival accessor. */
	public int getBottomY() {return bottomY;}
/** Trival accessor. */
	public boolean getRedrawFlag() {return redrawFlag;}

/** Trival mutator. */
	public void setTopX(int inTopX) {topX = inTopX;}
/** Trival mutator. */
	public void setTopY(int inTopY) {topY = inTopY;}
/** Trival mutator. */
	public void setBottomX(int inBottomX) {bottomX = inBottomX;}
/** Trival mutator. */
	public void setBottomY(int inBottomY) {bottomY = inBottomY;}
/** Trival mutator. */
	public void setRedrawFlag(boolean inRedrawFlag) {redrawFlag = inRedrawFlag;}


/** Draws the graph. */
	public void apply() {

		getGraph().randomizeNodePoints(new Point(topX,topY),bottomX,bottomY);

		if(redrawFlag) {
			getGraphPanel().update(getGraphPanel().getGraphics());
		}
	}

}
