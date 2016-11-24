package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.GedevoNative;
import org.cytoscape.gedevo.UserSettings;
import org.cytoscape.gedevo.simplenet.Edge;
import org.cytoscape.gedevo.simplenet.Graph;
import org.cytoscape.gedevo.simplenet.Node;

/**
 * Created by dw3 on 24/11/2016.
 */
public class GedUtil {
	/**
	 *
	 * Creates and returns a Network object from the Cytogedevo module.
	 * The Graph and Node classes used in this function are from the gedevo module NOT
	 * from the uk.ac.kent.dover package. Care should be taken not to confuse them.
	 *
	 * @param fastgraph
	 * @param name
     * @return A Network object
     */
	private static GedevoNative.Network fastGraphToNetwork(FastGraph fastgraph, String name) {
		int numEdges = fastgraph.getNumberOfEdges();
		int[] src = new int[numEdges];
		int[] dst = new int[numEdges];
		Edge[] cytoedges = new Edge[numEdges];
		Node[] cytonodes = new Node[fastgraph.getNumberOfNodes()];

		for (int i=0; i < numEdges; i++) {
			int srcInt = fastgraph.getEdgeNode1(i);
			int dstInt = fastgraph.getEdgeNode2(i);
			src[i] = srcInt;
			dst[i] = dstInt;
			cytoedges[i] = new Edge(srcInt, dstInt, false);
		}

		for(int i=0; i < fastgraph.getNumberOfNodes(); i++) {
			cytonodes[i] = new Node(i, fastgraph.getNodeLabel(i));
		}

		// This is a graph from the cytogedevo library, not this one
		Graph cytograph = new Graph(name);
		cytograph.eSrc = src;
		cytograph.eDst = dst;
		cytograph.nodes = cytonodes;
		cytograph.edges = cytoedges;

		return GedevoNative.Network.convertToNative(cytograph);
	}



	/**
	 *
	 * Converts the FastGraph objects to native Network objects and uses these to calculate the GED score.
	 *
	 * @param g1
	 * @param g2
     * @return The GED score as a string
     */
	public static String getGedScore(FastGraph g1, FastGraph g2) {

		GedevoNative.Network g1Network = GedUtil.fastGraphToNetwork(g1, "Graph One");
		GedevoNative.Network g2Network = GedUtil.fastGraphToNetwork(g2, "Graph Two");
		System.out.println("Created gedevo Network objects from graphs");

		UserSettings userSettings = new UserSettings();
		GedevoNative.Instance instance = GedevoNative.Instance.create(userSettings);
		System.out.println("Instance created");

		if (instance.importNetwork(g1Network, 0) && instance.importNetwork(g2Network, 1)) {
			System.out.println("Networks imported into instance");
		}
		else {
			System.out.println("ERROR. Networks not imported into instance");
		}

		return "GED score goes here";
	}
}
