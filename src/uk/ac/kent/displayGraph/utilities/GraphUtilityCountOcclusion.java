package uk.ac.kent.displayGraph.utilities;

import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;

/**
 *
 * @author Peter Rodgers
 */

public class GraphUtilityCountOcclusion extends GraphUtility {


	protected int vvLimit = 5;
	protected int veLimit = 5;
	
	public static double VE_ZERO_FUDGE_FACTOR = 1;

	protected static Random random = new Random();
	
	


/** Trivial constructor. */
	public GraphUtilityCountOcclusion() {
		super(KeyEvent.VK_O,"Count Occlusion");
	}

/** Trivial constructor. */
	public GraphUtilityCountOcclusion(int key, String s) {
		super(key,s);
	}

	public GraphUtilityCountOcclusion(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

/** Counts the occlusion. */
	public void apply() {

		String vvString = JOptionPane.showInputDialog("Please input a vertex-vertex occlusion limit (0 for exact overlap)",new Integer(vvLimit));
		if(vvString == null) {
			return;
		}
		try {
			vvLimit = Integer.parseInt(vvString);
		} catch(NumberFormatException e) {
			String message = "Cant parse "+vvString+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		
		String veString = JOptionPane.showInputDialog("Please input a vertex-edge occlusion limit (0 for exact overlap)",new Integer(veLimit));
		if(veString == null) {
			return;
		}
		
		try {
			veLimit = Integer.parseInt(veString);
		} catch(NumberFormatException  e) {
			String message = "Cant parse "+veString+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		
		int vvCount = get_vv_count(getGraph(),vvLimit);
		int veCount = get_ve_count(getGraph(),vvLimit,veLimit);
		
		String resultMessage = "vertex-vertex: "+vvCount+", vertex-edge: "+veCount;
		JOptionPane.showMessageDialog(null, resultMessage, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//		System.out.println(resultMessage);
		
int noVECount = countNoVEOcclusionOverlap(getGraph(),vvLimit,veLimit);
System.out.println("overlap and no VE "+noVECount);

	}
		
		
	/** Gets the count of occluded vertices */
	public static int get_vv_count(Graph graph,int limit) {
		int count = 0;
		for(Node v1 : graph.getNodes()) {
			for(Node v2 : graph.getNodes()) {
				if(v1 != v2 && test_vv_occlusion(v1,v2,limit)) {
					count = count+1;
				}
			}
		}
		return(count/2);
	}
	
	/** Gets the list of occluded vertices */
	public static ArrayList<Node> get_vv_list(Graph graph, int limit) {
		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node v1 : graph.getNodes()) {
			for(Node v2 : graph.getNodes()) {
				if(v1 != v2 && test_vv_occlusion(v1,v2,limit)) {
					if(!ret.contains(v1)) {
						ret.add(v1);
					}
					if(!ret.contains(v2)) {
						ret.add(v2);
					}
				}
			}
		}
		return ret;
	}
	
	public static boolean test_vv_occlusion(Node v1, Node v2, int limit) {
		int x1 = v1.getX();
		int y1 = v1.getY();
		int x2 = v2.getX();
		int y2 = v2.getY();
	 
		double distance = distance_between_points(x1,y1,x2,y2);
		if(distance <= limit) {
			return true;
		}
		return false;
	}

	public static double distance_between_points(int x1, int y1, int x2,int y2) {
		double distance = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		return distance;
	}
	
	/** Gets the count of occluded edges */
	public static int get_ve_count(Graph graph,int vv_limit, int ve_limit) {
		int count = 0;
		for(Node v1 : graph.getNodes()) {
			for(Edge e1 : graph.getEdges()) {
				if(test_ve_occlusion(v1,e1,graph,vv_limit,ve_limit)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/** Gets the list of nodes occluded by edges */
	public static ArrayList<Node> get_ve_list(Graph graph,int vv_limit, int ve_limit) {
		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node v1 : graph.getNodes()) {
			for(Edge e1 : graph.getEdges()) {
				if(test_ve_occlusion(v1,e1,graph,vv_limit,ve_limit) && !ret.contains(v1)) {
					ret.add(v1);
				}
			}
		}
		return ret;
	}
	
	
	public static boolean test_ve_occlusion(Node v,Edge e,Graph graph,int vv_limit,int ve_limit) {
		int x0 = v.getX();
		int y0 = v.getY();
		Node v1 = e.getFrom();
		Node v2 = e.getTo();
		
		if(v1 == v) {
			return false;
		}
		if(v2 == v) {
			return false;
		}
		
		if(test_vv_occlusion(v,v1,vv_limit)) {
			return false;
		}
		
		if(test_vv_occlusion(v,v2,vv_limit)) {
			return false;
		}
		
		int x1 = v1.getX();
		int y1 = v1.getY();
		int x2 = v2.getX();
		int y2 = v2.getY();

		double line_length = distance_between_points(x1,y1,x2,y2);

		// zero length edges will be covered by node-node occlusion
		if(line_length == 0) {
			return false;
		}

		// point-line distance formula nabbed off the www
		double point_line_distance = 1/line_length;
		point_line_distance = Math.abs((x2-x1)*(y1-y0)-(x1-x0)*(y2-y1))/line_length;
		if(point_line_distance >= ve_limit+VE_ZERO_FUDGE_FACTOR) {
			return false;
		}

		// hack to see if the vertex is between the two edge vertexes,
		// if it is then the distance to both vertexes must be less than
		// the line distance
		double point_v1_distance = distance_between_points(x1,y1,x0,y0);
		double point_v2_distance = distance_between_points(x2,y2,x0,y0);
		if(point_v1_distance > line_length) {
			return false;
		}
		if(point_v2_distance > line_length) {
			return false;
		}

		return true;
	}


	/**
	 */
//	public static int countNoVEOcclusionOverlap(Graph graph, int vv_limit, int ve_limit) {
	public int countNoVEOcclusionOverlap(Graph graph, int vv_limit, int ve_limit) {
		int count = 0;
		for(Node v1 : graph.getNodes()) {
			for(Edge e1 : graph.getEdges()) {
				if(v1 == e1.getFrom() || v1 == e1.getTo()) {
					continue;
				}
				if(!test_ve_occlusion(v1,e1,graph,vv_limit,ve_limit)) {
					Area v1Area = new Area(v1.shape());
					Area e1Area = new Area(e1.shape());
					v1Area.intersect(e1Area);

					if(!v1Area.isEmpty()) {
System.out.println("no VE occluding "+v1);
						count++;
					}
				}
			}
		}
		return count;
	}


	
	public static boolean sleep(int time) {
		try {
			Thread.sleep(time);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
