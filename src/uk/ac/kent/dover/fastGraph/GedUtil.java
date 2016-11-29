package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.AlignmentInfo;
import org.cytoscape.gedevo.GedevoNative;
import org.cytoscape.gedevo.UserSettings;
import org.cytoscape.gedevo.simplenet.Edge;
import org.cytoscape.gedevo.simplenet.Graph;
import org.cytoscape.gedevo.simplenet.Node;
import org.cytoscape.gedevo.task.AlignmentTaskData;

/**
 * Created by dw3 on 24/11/2016.
 * This entire class uses items from the gedevo module NOT
 * from the uk.ac.kent.dover package. Care should be taken not to confuse them.
 */
public class GedUtil {

	private static Graph fastGraphToCytoGraph(FastGraph fastgraph, String name) {
		Graph cytograph = new Graph(name);

		int numEdges = fastgraph.getNumberOfEdges();
		int numNodes = fastgraph.getNumberOfNodes();

		cytograph.eSrc = new int[numEdges];
		cytograph.eDst = new int[numEdges];
		cytograph.edges = new Edge[numEdges];
		cytograph.nodes = new Node[numNodes];

		for (int i=0; i < numEdges; i++) {
			int src = fastgraph.getEdgeNode1(i);
			int dst = fastgraph.getEdgeNode2(i);
			cytograph.eSrc[i] = src;
			cytograph.eDst[i] = dst;
			Edge e = new Edge(src, dst, false);
			cytograph.edges[i] = e;
		}

		for (int i=0; i < numNodes; i++) {
			Node n = new Node(i, fastgraph.getNodeLabel(i));
			cytograph.nodes[i] = n;
		}

		// This is a graph from the cytogedevo library, not this one

		return cytograph;
	}



	private static void printInfo(AlignmentInfo info) {
		System.out.println("Iterations: " + info.iterations);
		System.out.println("Without change: " + info.iterationsWithoutScoreChange);
		System.out.println("Unaligned edges: " + info.unalignedEdges);
		System.out.println("Aligned edges: " + info.alignedEdges);
		System.out.println("");
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

		System.out.println("Converted to cytograph");

		GedevoNative.Network g1Network = GedevoNative.Network.convertToNative(cytog1);
		GedevoNative.Network g2Network = GedevoNative.Network.convertToNative(cytog2);

		System.out.println("Converted to native");

		UserSettings userSettings = new UserSettings();
		final AlignmentTaskData alignmentTaskData = new AlignmentTaskData(userSettings);

		alignmentTaskData.instance = GedevoNative.Instance.create(alignmentTaskData.settings);
		alignmentTaskData.instance.importNetwork(g1Network, 0);
		alignmentTaskData.instance.importNetwork(g2Network, 1);
		alignmentTaskData.graphs = new Graph[]{cytog1, cytog2};
		alignmentTaskData.cynet = null;

		alignmentTaskData.instance.init1();
		System.out.println("Init 1 complete. Next phase may take some time");
		alignmentTaskData.instance.init2();
		System.out.println("Init 2 complete");

//		final int maxsteps = alignmentTaskData.settings.;
//		final int maxsteps = 150;
		System.out.println("Set up the alignment task");

		while (!alignmentTaskData.instance.isDone()) {
			alignmentTaskData.instance.fillAlignmentInfo(0, alignmentTaskData.info);
			alignmentTaskData.instance.update(1);
			printInfo(alignmentTaskData.info);
		}

		alignmentTaskData.instance.shutdown();


		System.out.println("Alignment finished");

		return alignmentTaskData.info.toString();
	}


}
