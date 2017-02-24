package uk.ac.kent.displayGraph.utilities;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.drawers.GraphDrawerEdgeLength;

/**
 * Outputs statistics on node and edge distances
 *
 * @author Peter Rodgers
 */

public class GraphUtilityDistanceStats extends GraphUtility {


/** Trivial constructor. */
	public GraphUtilityDistanceStats() {
		super(KeyEvent.VK_T,"Graph edge length and node separation stats",KeyEvent.VK_T);
	}

/** Trivial constructor. */
	public GraphUtilityDistanceStats(int key, String s) {
		super(key,s);
	}

/** Trivial constructor. */
	public GraphUtilityDistanceStats(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}


	public void apply() {
		runMeasures(getGraph(),true);
	}

/**
 * This allows a measure to be returned to a calling method, apply
 * must be have a void return value.
 */ 
	public double runMeasures(Graph g, boolean output) {
		GraphDrawerEdgeLength.setEdgeLabelsToWeights(g);

		double ret = outputEdgeLengthDifferences(g,output);

//		outputNearestNodeSeparationDifferences(g,output);
//		outputNodeSeparationDifferences(g,output);

		if(output) {
			String message = "nodes :"+g.getNodes().size()+" edges :"+g.getEdges().size()+" distortion: "+ret;
			JOptionPane.showMessageDialog(graphPanel,message,"Distortion",JOptionPane.INFORMATION_MESSAGE);
		}

		return ret;
	}


/**
 * This outputs a distortion value based on variance of average difference
 * from expected edge length.
 * The expected edge length is based on the edge weights in the graph.
 * The return value is the overall percentage difference between actual and ideal.
 */
	public static double outputEdgeLengthDifferences(Graph g, boolean output) {

		ArrayList<Edge> edges = g.getEdges();
		ArrayList<Node> nodes = g.getNodes();
		if(edges.size() == 0) {
			System.out.println("No Edges");
		}

		double totalLength = 0.0;
		double totalWeight = 0.0;
		for(Edge e : edges) {
			totalLength += edgeLength(e);
			totalWeight += e.getWeight();
		}
		double averageLength = totalLength/edges.size();
		double averageWeight = totalWeight/edges.size();

		double unitWeightLength = averageLength/averageWeight;

		double total = 0.0;
		for(Edge e : edges) {
			double idealLength = e.getWeight()*unitWeightLength;
			double diff = (edgeLength(e)-idealLength);
			total += (diff*diff)/edges.size();
		}

		double distortion = total/(totalLength*totalLength);

//		double percentDifference = 100*totalDifference/totalLength;
		if(output) {
//			System.out.println("nodes :"+nodes.size()+" edges :"+edges.size()+" total length difference: "+totalDifference+" total edge length: "+totalLength+" percent difference: "+percentDifference);
			System.out.println("nodes :"+nodes.size()+" edges :"+edges.size()+" distortion: "+distortion);
		}

		return distortion;
	}


	public static double edgeLength(Edge e) {
		Node n1 = e.getFrom();
		Node n2 = e.getTo();
		return(n1.getCentre().distance(n2.getCentre()));
	}


/**
 * This outputs the average difference between node separations.
 * It is probably not a sensible measure, as all nodes are compared
 * against all other nodes
 */
	public static double outputNodeSeparationDifferences(Graph g,boolean output) {
		ArrayList<Edge> edges = g.getEdges();
		ArrayList<Node> nodes = g.getNodes();
		if(nodes.size() == 0) {
			System.out.println("No Nodes");
		}

		double totalSeparation = 0.0;
		int comparisonCount = 0;

		for(Node n1 : nodes) {
			boolean compare = false;
			for(Node n2 : nodes) {
// go to next in list past n1
				if(compare) {
					totalSeparation += nodeSeparation(n1,n2);
					comparisonCount++;
				}
				if(n1 == n2) {
					compare = true;
				}
			}
		}

		double averageSeparation = totalSeparation/comparisonCount;
		double totalDifference = 0.0;
		for(Node n3 : nodes) {
			boolean compare = false;
			for(Node n4 : nodes) {
// go to next in list past n3
				if(compare) {
					double diff = Math.abs(nodeSeparation(n3,n4)-averageSeparation);
					totalDifference += diff;
				}
				if(n4 == n3) {
					compare = true;
				}
			}
		}

		double percentDifference = 100*totalDifference/totalSeparation;

		if(output) {
			System.out.println("nodes :"+nodes.size()+" edges :"+edges.size()+" total separation difference: "+totalDifference+" total node separation: "+totalSeparation+" percent difference: "+percentDifference);
		}
		return percentDifference;
	}

/**
 * This outputs the average difference between nearest nodes.
 * This is an attempt to measure evenness of node distribution.
 */
	public static double outputNearestNodeSeparationDifferences(Graph g,boolean output) {
		ArrayList<Edge> edges = g.getEdges();
		ArrayList<Node> nodes = g.getNodes();
		if(nodes.size() <= 1) {
			System.out.println("Less than 2 Nodes");
		}

		double totalSeparation = 0.0;

		for(Node n1 : nodes) {
			double closestTo1 = Double.MAX_VALUE;
			for(Node n2 : nodes) {
				if(n1 != n2) {
					double separation = nodeSeparation(n1,n2);
					if(separation < closestTo1) {
						closestTo1 = separation;
					}
				}
			}
			totalSeparation += closestTo1;
		}

		double averageSeparation = totalSeparation/nodes.size();
		double totalDifference = 0.0;

		for(Node n3 : nodes) {
			double closestTo3 = Double.MAX_VALUE;
			for(Node n4 : nodes) {
				if(n3 != n4) {
					double separation = nodeSeparation(n3,n4);
					if(separation < closestTo3) {
						closestTo3 = separation;
					}
				}
			}
			double diff = Math.abs(closestTo3-averageSeparation);
			totalDifference += diff;
		}

		double percentDifference = 100*totalDifference/totalSeparation;
		if (output) {
			System.out.println("nodes :"+nodes.size()+" edges :"+edges.size()+" closest node separation difference: "+totalDifference+" total node separation: "+totalSeparation+" percent difference: "+percentDifference);
		}

		return percentDifference;
	}


	public static double nodeSeparation(Node n1, Node n2) {
		return(n1.getCentre().distance(n2.getCentre()));
	}

}


