package uk.ac.kent.displayGraph.utilities;

import java.awt.event.KeyEvent;
import java.util.Random;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class GraphUtilityEdgeLabelRandomizer extends GraphUtility {

	protected double lower = 1;
	protected double upper = 10;

/** Trivial constructor. */
	public GraphUtilityEdgeLabelRandomizer() {
		super(KeyEvent.VK_W,"Graph Edge Label Randomizer");
	}

/** Trivial constructor. */
	public GraphUtilityEdgeLabelRandomizer(int key, String s) {
		super(key,s);
	}

/** Full Constructor. */
	public GraphUtilityEdgeLabelRandomizer(int key, String s, int mnemonic, double lower, double upper) {
		super(key,s,mnemonic);
		setLower(lower);
		setUpper(upper);
	}

/** Trival accessor. */
	public double getLower() {return lower;}
/** Trival accessor. */
	public double getUpper() {return upper;}

/** Trival mutator. */
	public void setLower(double d) {lower = d;}
/** Trival mutator. */
	public void setUpper(double d) {upper = d;}


/** Draws the graph. */
	public void apply() {

		randomize(getGraph(), lower, upper);
		getGraphPanel().update(getGraphPanel().getGraphics());
	}


	public static void randomize(Graph g, double l, double u) {
		Random r = new Random();
		for(Edge e : g.getEdges()) {
			double value = l+(r.nextDouble()*(u-l));
// this bit keeps the value to 2 decimal places
			value = Math.round(value*100);
			value = value/100;
			e.setLabel(Double.toString(value));
		}

	}
}
