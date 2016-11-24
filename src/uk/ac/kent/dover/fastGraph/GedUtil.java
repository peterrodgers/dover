package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.GedevoNative;
import org.cytoscape.gedevo.UserSettings;
import org.cytoscape.gedevo.simplenet.Edge;
import org.cytoscape.gedevo.simplenet.Graph;
import org.cytoscape.gedevo.simplenet.Node;
import org.cytoscape.gedevo.task.AlignmentTaskData;

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
	private static Graph fastGraphToCytoGraph(FastGraph fastgraph, String name) {
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

		return cytograph;
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

		Graph cytog1 = GedUtil.fastGraphToCytoGraph(g1, "Graph One");
		Graph cytog2 = GedUtil.fastGraphToCytoGraph(g2, "Graph Two");

		GedevoNative.Network g1Network = GedevoNative.Network.convertToNative(cytog1);
		GedevoNative.Network g2Network = GedevoNative.Network.convertToNative(cytog2);

		UserSettings userSettings = new UserSettings();
		final AlignmentTaskData alignmentTaskData = new AlignmentTaskData(userSettings);

		alignmentTaskData.instance = GedevoNative.Instance.create(alignmentTaskData.settings);
		alignmentTaskData.instance.init1();
		alignmentTaskData.instance.init2();
//		alignmentTaskData.instance.importNetwork(g1Network, 0);
//		alignmentTaskData.instance.importNetwork(g2Network, 1);
//		alignmentTaskData.graphs = new Graph[]{cytog1, cytog2};
//		alignmentTaskData.cynet = null;
		alignmentTaskData.instance.fillAlignmentInfo(0, alignmentTaskData.info);
		alignmentTaskData.instance.update(1);

		return "GED score goes here";
	}


}
