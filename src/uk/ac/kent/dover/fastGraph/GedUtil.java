package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.AlignmentInfo;
import org.cytoscape.gedevo.GedevoNative;
import org.cytoscape.gedevo.UserSettings;
import org.cytoscape.gedevo.simplenet.Edge;
import org.cytoscape.gedevo.simplenet.Graph;
import org.cytoscape.gedevo.simplenet.Node;

/**
 * Created by dw3 on 24/11/2016.
 * This entire class uses items from the gedevo module NOT
 * from the uk.ac.kent.dover package. Care should be taken not to confuse them.
 */
public class GedUtil {

	private static GedevoNative.Network fastGraphToNetwork(FastGraph fastgraph, String name) {
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
			Debugger.log("node: " + n.name + " id: " + n.id);
		}

		for (int i = 0; i < cytograph.edges.length; i++) {
			Edge e = cytograph.edges[i];
			Debugger.log("to: " + e.node1 + "  from: " + e.node2);
			
		}
		
		// This is a graph from the cytogedevo library, not this one
		
		GedevoNative.Network n = GedevoNative.Network.convertToNative(cytograph);

		return n;
	}



	private static void printInfo(AlignmentInfo info) {
		System.out.println("Iterations: " + info.iterations);
		System.out.println("Without change: " + info.iterationsWithoutScoreChange);
		System.out.println("Population: " + info.numAgents);
		System.out.println("EC: " + info.edgeCorrectness);
		System.out.println("Life: " + info.lifeTime);
		System.out.println("");
	}


	private static float calculateScore(GedevoNative.Instance instance)
	{
		if (!instance.init1() || !instance.init2()) {
			System.exit(1);
		}

		AlignmentInfo info = new AlignmentInfo();

		while (!instance.isDone()) {
//			printInfo(info);
			instance.fillAlignmentInfo(0, info);
			instance.update(1);
		}

		float gedScore = instance.overallGed();

		return gedScore;
	}

	/**
	 *
	 * Converts the FastGraph objects to native Network objects and uses these to calculate the GED score.
	 *
	 * @param g1
	 * @param g2
     * @return The GED score as a string
     */
	public static float getGedScore(FastGraph g1, FastGraph g2) {

		GedevoNative.Network g1Network = GedUtil.fastGraphToNetwork(g1, "Graph One");
		GedevoNative.Network g2Network = GedUtil.fastGraphToNetwork(g2, "Graph Two");

		UserSettings userSettings = new DoverSettings();

		GedevoNative.Instance instance = GedevoNative.Instance.create(userSettings);

		instance.importNetwork(g1Network, 0);
		instance.importNetwork(g2Network, 1);

		Float gedScore = calculateScore(instance);
		instance.shutdown();

		return gedScore;
	}

}
