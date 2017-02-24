package uk.ac.kent.displayGraph.utilities;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.Node;


/**
 * Removes the dummy nodes created by graph drawing algorithms such
 * as heirarchial algorithms.
 *
 * @author Peter Rodgers
 */
public class GraphUtilityRemoveDummyNodes extends GraphUtility {

/** Minimal constructor */
	public GraphUtilityRemoveDummyNodes() {
		super(KeyEvent.VK_D, "Hierachical", KeyEvent.VK_D);
	}

/** Full constructor */
	public GraphUtilityRemoveDummyNodes(int accelerator, String s,int mnemonic) {
		super(accelerator, s,mnemonic);
	}

	public void apply() {

		GraphPanel gp = getGraphPanel();
		Graph g = getGraph();

		removeDummyNodes(g);
		gp.repaint();

	}

/** Remove dummy nodes */
	public static void removeDummyNodes(Graph g) {

		ArrayList<Node> nodes = new ArrayList<Node>(g.getNodes());
		for(Node n : nodes) {
			if(n.getType().getLabel() == "dummy") {

				ArrayList<Edge> edgesTo = new ArrayList<Edge>(n.getEdgesTo());
				Edge moveEdge = (Edge)edgesTo.get(0);

				ArrayList<Edge> edgesFrom = new ArrayList<Edge>(n.getEdgesFrom());
				Edge deleteEdge = (Edge)edgesFrom.get(0);

				Node newTarget = deleteEdge.getOppositeEnd(n);
				moveEdge.setTo(newTarget);

				g.removeEdge(deleteEdge);
				g.removeNode(n);
			}
		}
	}


}



