package uk.ac.kent.dover.fastGraph;


import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import org.json.*;

import uk.ac.kent.displayGraph.*;

import Jama.Matrix;
import Jama.EigenvalueDecomposition;

/**
 * 
 * Graph class with redundant node connections. Stores nodes, edges, node connections, node labels, edge labels
 * in ByteBuffers. Note due to the complexity of storage, create only with the factory methods.
 * <p>
 * The design is scalable, has fast access, and allows quick file save and load of the ByteBuffers.
 * However, poor dynamic performance.
 * <p>
 * Storage:
 * node and edge indexes are integers and must start at 0 and end and size-1. Indexes are not stored,
 * they are assumed, so node info with nodeIndex n can be found starting in nodeBuf at offset n*nodeByteSize,
 * similarly edge with edgeIndex e starts in edgeBuf at e*edgeByteSize.
 * <p>
 * <ul>
 * <li>nodeBuf stores offset of label start in nodeLabelBuf and size (in chars) of labels.</li>
 * <li>nodeBuf stores in and out offset and in and out number (degree) of connecting nodes and edges start
 * which link to connectionBuf and size (in chars) of in or out edges.</li>
 * <li>edgeBuf stores offset of label start in edgeLabelBuf and size (in chars) of labels.</li>
 * <li>connectionBuf stores pairs of edgeIndex-nodeIndex (both are stored for fastest access) which form a
 * list of connecting items, with the in edge-nodes first, then out edge-nodes</li>
 * </ul>
 * json from <a href="https://github.com/stleary/JSON-java"> json library </a>
 * 
 * @author Peter Rodgers
 * @author Rob Baker
 */
public class FastGraph {

	protected static final int NODE_LABEL_START_OFFSET = 0; // integer
	protected static final int NODE_LABEL_LENGTH_OFFSET = 4; // short
	protected static final int NODE_IN_CONNECTION_START_OFFSET = 6; // integer
	protected static final int NODE_IN_DEGREE_OFFSET = 10; // short
	protected static final int NODE_OUT_CONNECTION_START_OFFSET = 12; // integer
	protected  static final int NODE_OUT_DEGREE_OFFSET = 16; // short
	protected  static final int NODE_WEIGHT_OFFSET = 18; // integer
	protected  static final int NODE_TYPE_OFFSET = 22; // byte
	protected  static final int NODE_AGE_OFFSET = 23; // byte
	
	protected  static final int EDGE_NODE1_OFFSET = 0; // integer
	protected  static final int EDGE_NODE2_OFFSET = 4; // integer
	protected  static final int EDGE_LABEL_START_OFFSET = 8; // integer
	protected  static final int EDGE_LABEL_LENGTH_OFFSET = 12; // short
	protected  static final int EDGE_WEIGHT_OFFSET = 14; // integer
	protected  static final int EDGE_TYPE_OFFSET = 18; // byte
	protected  static final int EDGE_AGE_OFFSET = 19; // byte
	
	protected  static final int CONNECTION_EDGE_OFFSET = 0; // integer, edge is first of the pair
	protected  static final int CONNECTION_NODE_OFFSET = 4; // integer, node is straight after the edge
	
	public static final int DEFAULT_AVERAGE_LABEL_LENGTH = 20;
	
	protected  static final int NODE_BYTE_SIZE = 24;
	protected  static final int EDGE_BYTE_SIZE = 20;
	protected  static final int CONNECTION_PAIR_SIZE = 8; // this is an edge index plus an node index
	
	public static final String INFO_SPLIT_STRING = "~";
	
	public static final int MAX_BYTE_BUFFER_SIZE = Integer.MAX_VALUE-5000;
	

	private ByteBuffer nodeBuf;
	private ByteBuffer edgeBuf;
	private ByteBuffer connectionBuf;
	private ByteBuffer nodeLabelBuf;
	private ByteBuffer edgeLabelBuf;

	private int numberOfNodes;
	private int numberOfEdges;
	
	
	private String name = "";
	private boolean direct;
	
	private Random randomGen; //used to pick random edges;
		
	/**
	 * No direct access to constructor, as a number of data structures need to be created when
	 * graph nodes and edges are added.
	 * 
	 * @param nodeTotal the number of nodes in the graph
	 * @param edgeTotal the number of edges in the graph
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 */
	private FastGraph(int nodeTotal, int edgeTotal, boolean direct) {
		
		this.numberOfNodes = nodeTotal;
		this.numberOfEdges = edgeTotal;
		this.direct = direct;
		
		init();
		
		
	}

	
	/**
	 * @param args not used
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		long time;
		
		
//		FastGraph g1 = randomGraphFactory(2,1,false);
//		FastGraph g1 = randomGraphFactory(5,6,1,true);
//		FastGraph g1 = randomGraphFactory(8,9,1,true);
//		FastGraph g1 = randomGraphFactory(10000,100000,false); // 10 thousand nodes, 100 thousand edges
//		FastGraph g1 = randomGraphFactory(1000000,10000000,false); // 1 million nodes, 10 million edges
//		FastGraph g1 = randomGraphFactory(5000000,50000000,false); // limit for edgeLabelBuf at 20 chars per label
//		FastGraph g1 = randomGraphFactory(4847571,68993773,false); // Size of LiveJournal1 example from SNAP
//		FastGraph g1 = randomGraphFactory(10000000,100000000,false); // 10 million nodes, 100 million edges, close to edgeBuf limit, but fails on heap space with 14g, but pass with heap space of 30g

//		time = System.currentTimeMillis();
//		FastGraph g1 = adjacencyListGraphFactory(7115,103689,null,"Wiki-Vote.txt",false);
//		FastGraph g1 = adjacencyListGraphFactory(36692,367662,null,"Email-Enron1.txt",false);
//		FastGraph g1 = adjacencyListGraphFactory(81306,2420766,null,"twitter_combined.txt",false); // SNAP web page gives 1768149 edges
//		FastGraph g1 = adjacencyListGraphFactory(1632803,30622564,null,"soc-pokec-relationships.txt",false);
//		FastGraph g1 = adjacencyListGraphFactory(4847571,68993773,null,"soc-LiveJournal1.txt",false);
		
//System.out.println("snap load time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
		
		time = System.currentTimeMillis();
/*		
		g1.saveBuffers(null,g1.getName());
		System.out.println("saveBuffers test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
		time = System.currentTimeMillis();
*/
String name = "random-n-8-e-9";
//		String name = g1.getName();
		FastGraph g2;
		try {
			g2 = loadBuffersGraphFactory(null,name);
			System.out.println("create graph from file test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
			
	 		
/*			
			time = System.currentTimeMillis();
			boolean connected = g2.isConnected();
			System.out.println("connected test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
			
			System.out.println("connected "+connected);
*/			
			time = System.currentTimeMillis();
			AdjacencyMatrix am = new AdjacencyMatrix(g2);
			int[][] matrix = am.buildIntAdjacencyMatrix();
			//boolean[][] matrix = g2.buildBooleanAdjacencyMatrix();
			System.out.println("building matrix test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
			am.printMatrix(matrix);
			//System.out.println(Arrays.toString(g2.findEigenvalues(matrix)));
			//System.out.println(matrix.length);
			
			LinkedList<Integer> nodes = new LinkedList<Integer>();
			LinkedList<Integer> edges = new LinkedList<Integer>();
			InducedSubgraph is = new InducedSubgraph(g2);
			is.createInducedSubgraph(nodes, edges, 6);
			
			System.out.println("nodes:");
			System.out.println(nodes);
			System.out.println("edges:");
			System.out.println(edges);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

 		
	}	


	/**
	 * @return the number of nodes in the graph
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}


	/**
	 * @return the number of edges in the graph
	 */
	public int getNumberOfEdges() {
		return numberOfEdges;
	}

	
	/**
	 * @return the randomGen
	 */
	public Random getRandomGen() {
		return randomGen;
	}


	/**
	 * @return the graph name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * @return the direct flag, false is on heap, true is off heap
	 */
	public boolean getDirect() {
		return direct;
	}
	
	/**
	 * 
	 * @return the node ByteBuffer
	 */
	public ByteBuffer getNodeBuf() {
		return nodeBuf;
	}


	/**
	 * 
	 * @return the edge ByteBuffer
	 */
	public ByteBuffer getEdgeBuf() {
		return edgeBuf;
	}


	/**
	 * 
	 * @return the node label ByteBuffer
	 */
	public ByteBuffer getNodeLabelBuf() {
		return nodeLabelBuf;
	}


	/**
	 * 
	 * @return the edge label ByteBuffer
	 */
	public ByteBuffer getEdgeLabelBuf() {
		return edgeLabelBuf;
	}


	/**
	 * 
	 * @return the connections ByteBuffer
	 */
	public ByteBuffer getConnectionBuf() {
		return connectionBuf;
	}



	/**
	 * @param nodeIndex the node
	 * @return the node label
	 */
	public String getNodeLabel(int nodeIndex) {
		int labelStart = nodeBuf.getInt(NODE_LABEL_START_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		int labelLength = nodeBuf.getShort(NODE_LABEL_LENGTH_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		char[] label = new char[labelLength];
		for(int i = 0; i < labelLength; i++) {
			int offset = labelStart+i*2;
			char c = nodeLabelBuf.getChar(offset);
			label[i] = c;
		}
		String ret = new String(label);
		return ret;
	}
	
	
	/**
	 * @param nodeIndex the node
	 * @return the node weight
	 */
	public int getNodeWeight(int nodeIndex) {
		int weight = nodeBuf.getInt(NODE_WEIGHT_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return weight;
	}
	
	
	/**
	 * @param nodeIndex the node
	 * @return the node type
	 */
	public byte getNodeType(int nodeIndex) {
		byte type = nodeBuf.get(NODE_TYPE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return type;
	}
	
	
	/**
	 * @param nodeIndex the node
	 * @return the node age
	 */
	public byte getNodeAge(int nodeIndex) {
		byte age = nodeBuf.get(NODE_AGE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return age;
	}
	

	/**
	 * @param nodeIndex the node
	 * @return the node degree (number of connecting edges)
	 */
	public int getNodeDegree(int nodeIndex) {
		int degree = getNodeInDegree(nodeIndex)+getNodeOutDegree(nodeIndex);
		return degree;
	}
	

	/**
	 * @param nodeIndex the node
	 * @return the node in-degree (number of edges entering the node)
	 */
	public short getNodeInDegree(int nodeIndex) {
		short degree = nodeBuf.getShort(NODE_IN_DEGREE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return degree;
	}

	
	/**
	 * @param nodeIndex the node
	 * @return the node out-degree (number of edges leaving the node)
	 */
	public short getNodeOutDegree(int nodeIndex) {
		short degree = nodeBuf.getShort(NODE_OUT_DEGREE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return degree;
	}
	

	/**
	 * @param nodeIndex the node
	 * @return all connecting edges
	 */
	public int[] getNodeConnectingEdges(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	

	/**
	 * This version puts the connecting edges in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting edges found
	 * @param nodeIndex the node
	 * @return all connecting edges via parameter array. 
	 */
	public void getNodeConnectingEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}
	

	/**
	 * @param nodeIndex the node
	 * @return all node neighbours. 
	 */
	public int[] getNodeConnectingNodes(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	

	/**
	 * This version puts the connecting nodes in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting nodes found
	 * @param nodeIndex the node
	 * @return all node neighbours. 
	 */
	public void getNodeConnectingNodes(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}
	

	/**
	 * For directed graphs.
	 * 
	 * @param nodeIndex the node
	 * @return all connecting edges for the node
	 */
	public int[] getNodeConnectingInEdges(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	

	/**
	 * For directed graphs.
	 * This version puts the connecting edges in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeInDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting edges found
	 * @param nodeIndex the node
	 * @return all connecting edges that enter the node via the parameter array. 
	 */
	public void getNodeConnectingInEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	/**
	 * For directed graphs.
	 * 
	 * @param nodeIndex the node
	 * @return all node neighbours that are on the end of edges that enter the node. 
	 */
	public int[] getNodeConnectingInNodes(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}

	/**
	 * For directed graphs.
	 * This version puts the connecting nodes in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeInDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting nodes found
	 * @param nodeIndex the node
 	 * @return all node neighbours that are on the end of edges that enter the node via the parameter array.
	 */
	public void getNodeConnectingInNodes(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}

	/**
	 * For directed graphs.
	 * 
	 * @param nodeIndex the node
	 * @return all edges that leave the node. 
	 */
	public int[] getNodeConnectingOutEdges(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	
	/**
	 * For directed graphs.
	 * This version puts the connecting nodes in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeOutDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting edges found
	 * @param nodeIndex the node
	 * @return all edges that leave the node via the argument array. 
	 */
	public void getNodeConnectingOutEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	/**
	 * For directed graphs. 
	 *
	 * @param nodeIndex the node
 	 * @return all node neighbours that are on the end of edges that leave the passed node. 
	 */
	public int[] getNodeConnectingOutNodes(int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	
	
	/**
	 * For directed graphs. 
	 * This version puts the connecting nodes in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeOutDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with the connecting nodes found
	 * @param nodeIndex the node
  	 * @return all node neighbours that are on the end of edges that leave the passed node via the parameter array. 
	 */
	public void getNodeConnectingOutNodes(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	/**
	 * @param edgeIndex the edge
	 * @return the edge label
	 */
	public String getEdgeLabel(int edgeIndex) {
		int labelStart = edgeBuf.getInt(EDGE_LABEL_START_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		int labelLength = edgeBuf.getShort(EDGE_LABEL_LENGTH_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		char[] label = new char[labelLength];
		for(int i = 0; i < labelLength; i++) {
			int offset = labelStart+i*2;
			char c = edgeLabelBuf.getChar(offset);
			label[i] = c;
		}
		String ret = new String(label);
		return ret;
	}

	
	/**
	 * @param edgeIndex the edge
	 * @return the first connecting node (the node the edge leaves for directed graphs).
	 */
	public int getEdgeNode1(int edgeIndex) {
		int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		return n1;
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @return the second connecting node (the node the edge enters for directed graphs).
	 */
	public int getEdgeNode2(int edgeIndex) {
		int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		return n2;
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @return the edge weight
	 */
	public int getEdgeWeight(int edgeIndex) {
		int type = edgeBuf.getInt(EDGE_WEIGHT_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		return type;
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @return the edge type
	 */
	public byte getEdgeType(int edgeIndex) {
		byte type= edgeBuf.get(EDGE_TYPE_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		return type;
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @return the edge age
	 */
	public byte getEdgeAge(int edgeIndex) {
		byte age = edgeBuf.get(EDGE_AGE_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
		return age;
	}
	
	
	/**
	 * Names should be simple alphanumeric. Spaces and dashes are permitted. Note that tilde ("~") cannot be used.
	 * @param name the name of the graph
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @param nodeIndex the node
	 * @param weight the new node weight
	 */
	public void setNodeWeight(int nodeIndex, int weight) {
		nodeBuf.putInt(NODE_WEIGHT_OFFSET+nodeIndex*NODE_BYTE_SIZE, weight);
	}
	
	
	/**
	 * @param nodeIndex the node
	 * @param type the new node type
	 */
	public void setNodeType(int nodeIndex, byte type) {
		nodeBuf.put(NODE_TYPE_OFFSET+nodeIndex*NODE_BYTE_SIZE, type);
	}
	
	
	/**
	 * @param nodeIndex the node
	 * @return the node age
	 */
	public void setNodeAge(int nodeIndex, byte age) {
		nodeBuf.put(NODE_AGE_OFFSET+nodeIndex*NODE_BYTE_SIZE, age);
	}
	

	
	/**
	 * @param edgeIndex the edge
	 * @param weight the new edge weight
	 */
	public void setEdgeWeight(int edgeIndex, int weight) {
		edgeBuf.putInt(EDGE_WEIGHT_OFFSET+edgeIndex*EDGE_BYTE_SIZE, weight);
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @param type the new edge type
	 */
	public void setEdgeType(int edgeIndex, byte type) {
		edgeBuf.put(EDGE_TYPE_OFFSET+edgeIndex*EDGE_BYTE_SIZE, type);
	}
	
	
	/**
	 * @param edgeIndex the edge
	 * @return the edge age
	 */
	public void setEdgeAge(int edgeIndex, byte age) {
		edgeBuf.put(EDGE_AGE_OFFSET+edgeIndex*EDGE_BYTE_SIZE, age);
	}
	

	/**
	 * Change all the node labels in the graph. Creates a new nodeLabelBuf, changes the label pointers in nodeBuf.
	 * 
	 * @param labels Must contain the same number of labels as number of nodes in the graph
	 */
	public void setAllNodeLabels(String[] labels) {
		
		long totalLabelLength = 0;
		
		for(int i = 0; i < numberOfNodes; i++) {
			totalLabelLength += labels[i].length();
		}
		
		if(totalLabelLength*2 > MAX_BYTE_BUFFER_SIZE) {
			throw new OutOfMemoryError("Tried to create a nodeLabelBuf with too many chars");
		}
		int bufSize = (int)(totalLabelLength*2); // this cast is safe because of the previous test
		
		if(!direct) {
			nodeLabelBuf = ByteBuffer.allocate(bufSize);
		} else {
			nodeLabelBuf = ByteBuffer.allocateDirect(bufSize);
		}
		nodeLabelBuf.clear();
		int labelOffset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			String label = labels[i];
			char[] labelArray = label.toCharArray();
			short labelLength = (short)(labelArray.length);
	
			nodeBuf.putInt(NODE_LABEL_START_OFFSET+i*NODE_BYTE_SIZE,labelOffset); // label start
			nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+i*NODE_BYTE_SIZE,labelLength); // label size
	
			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				nodeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}
		}
		
	}
	
	/**
	 * Change all the edge labels in the graph. Creates a new edgeLabelBuf, changes the label pointers in edgeBuf
	 * 
	 * @param labels Must contain the same number of labels as there are edges in the graph
	 * @throws OutofMemoryError
	 */
	public void setAllEdgeLabels(String[] labels)  {
		
		long totalLabelLength = 0;
		
		for(int i = 0; i < numberOfEdges; i++) {
			totalLabelLength += labels[i].length();
		}
		
		if(totalLabelLength*2 > MAX_BYTE_BUFFER_SIZE) {
			throw new OutOfMemoryError("Tried to create a edgeLabelBuf with too many chars");
		}
		int bufSize = (int)(totalLabelLength*2); // this cast is safe because of the previous test
		
		if(!direct) {
			edgeLabelBuf = ByteBuffer.allocate(bufSize);
		} else {
			edgeLabelBuf = ByteBuffer.allocateDirect(bufSize);
		}
		edgeLabelBuf.clear();

		int labelOffset = 0;
		for(int i = 0; i < numberOfEdges; i++) {
			String label = labels[i];
			char[] labelArray = label.toCharArray();
			short labelLength = (short)(labelArray.length);
	
			edgeBuf.putInt(EDGE_LABEL_START_OFFSET+i*EDGE_BYTE_SIZE,labelOffset); // label start
			edgeBuf.putShort(EDGE_LABEL_LENGTH_OFFSET+i*EDGE_BYTE_SIZE,labelLength); // label size
	
			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				edgeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}
		}

	}
	
	
	/**
	 * gets the other node connecting to the edge.
	 * If the argument node is not connected to the edge, then an undefined node will
	 * be returned.
	 * 
	 * @param edge the edge
	 * @param node the known node
	 * @return the node on the opposite side of the edge
	 */
	public int oppositeEnd(int edge, int node) {
		int n1 = getEdgeNode1(edge);
		int n2 = getEdgeNode2(edge);
		
		if(n1 == node) {
			return n2;
		}
		return n1;
	}


	
	
	/**
	 * Allocates space for the node, edge and connection ByteBuffers. The label ByteBuffers
	 * are created later
	 */
	private void init() {

		if(!direct) {
			nodeBuf = ByteBuffer.allocate(numberOfNodes*NODE_BYTE_SIZE);
			edgeBuf = ByteBuffer.allocate(numberOfEdges*EDGE_BYTE_SIZE);
			connectionBuf = ByteBuffer.allocate(numberOfEdges*2*CONNECTION_PAIR_SIZE);
			// nodeLabelBuf and edgeLabelBuf now created in Factories by setAllNodeLabels
		} else {
			nodeBuf = ByteBuffer.allocateDirect(numberOfNodes*NODE_BYTE_SIZE);
			edgeBuf = ByteBuffer.allocateDirect(numberOfEdges*EDGE_BYTE_SIZE);
			connectionBuf = ByteBuffer.allocateDirect(numberOfEdges*2*CONNECTION_PAIR_SIZE);
			// nodeLabelBuf and edgeLabelBuf now created in Factories by setAllNodeLabels
		}
		
		nodeBuf.clear();
		edgeBuf.clear();
		connectionBuf.clear();
		
	}


	/**
	 * Create a FastGraph from a json string.
	 *
	 * @param json the json as a string
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the created FastGraph.
	 */
	public static FastGraph jsonStringGraphFactory(String json, boolean direct) {
		
		int nodeCount = 0;
		int edgeCount = 0;
		
		JSONObject jsonObj = new JSONObject(json);
		
		String graphName = jsonObj.getString("name");
		
		JSONArray nodes = jsonObj.getJSONArray("nodes");
		Iterator<Object> itNodes = nodes.iterator();
		while(itNodes.hasNext()) {
			JSONObject node = (JSONObject)(itNodes.next());
			int index = node.getInt("nodeIndex");
			if(index+1 > nodeCount) {
				nodeCount = index+1;
			}
		}
		
		JSONArray edges = jsonObj.getJSONArray("edges");
		Iterator<Object> itEdges = edges.iterator();
		while(itEdges.hasNext()) {
			JSONObject edge = (JSONObject)(itEdges.next());
			int index = edge.getInt("edgeIndex");
			if(index+1 > edgeCount) {
				edgeCount = index+1;
			}
		}

		FastGraph g = new FastGraph(nodeCount,edgeCount,direct);
		g.populateFromJsonString(jsonObj);
		g.setName(graphName);
		
		return g;
	}
	

	/**
	 * Generate a random graph of the desired size. Self sourcing edges and parallel edges may exist.
	 * 
	 * @param numberOfNodes the number of nodes in the graph
	 * @param numberOfEdges the number of edges in the graph
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the created FastGraph
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, boolean direct) {
		FastGraph graph = randomGraphFactory(numberOfNodes, numberOfEdges, -1, direct);
		return graph;
	}
	
	
	/**
	 * Generate a random graph of the desired size. Self sourcing edges and parallel edges may exist.
	 * 
	 * @param numberOfNodes the number of nodes in the graph
	 * @param numberOfEdges the number of edges in the graph
	 * @param seed random number seed, -1 for current time
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the created FastGraph
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, long seed, boolean direct) {
		FastGraph g = new FastGraph(numberOfNodes,numberOfEdges,direct);
		g.setName("random-n-"+numberOfNodes+"-e-"+numberOfEdges);
		g.populateRandomGraph(seed);
		return g;
	}
	

	/**
	 * creates a FastGraph by loading in various files from the given directory, or data under
	 * current working directory if directory is null.
	 * 
	 * @param directory where the files are held, or if null fileBaseName under data under the current working directory
	 * @param fileBaseName the name of the files, to which extensions are added
	 * @return the created FastGraph
	 * @throws IOException If the buffers cannot be loaded
	 * @See loadBuffers
	 */
	public static FastGraph loadBuffersGraphFactory(String directory, String fileBaseName) throws IOException {
		FastGraph g = loadBuffers(null,fileBaseName);
		return g;
	}

	
	
	/**
	 * Populates the FastGraph ByteBuffers from a json string.
	 * @param jsonObj the json code after parsing
	 */
	private void populateFromJsonString(JSONObject jsonObj) {

		//long time;

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -888;
		short inLength = -3;
		int outStart = -777;
		short outLength = -2;
		int index = -1;
		int weight = -5;
		byte type = -7;
		byte age = -9;
		String label;
		
		//the nodes are the first elements
		JSONArray nodes = jsonObj.getJSONArray("nodes");
		Iterator<Object> itNodes = nodes.iterator();
		while(itNodes.hasNext()) {
			
			JSONObject node = (JSONObject)(itNodes.next());
			index = node.getInt("nodeIndex");
			weight = node.getInt("nodeWeight");
			type = (byte)(node.getInt("nodeType"));
			age = (byte)(node.getInt("nodeAge"));
			label = node.getString("nodeLabel");
			
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+index*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+index*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+index*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+index*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+index*NODE_BYTE_SIZE,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+index*NODE_BYTE_SIZE,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+index*NODE_BYTE_SIZE,age); // age
			
			// save labels for later
			nodeLabels[index] = label;

		}

		setAllNodeLabels(nodeLabels);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(i,edges);
		}
		
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		index = -1;
		weight = -101;
		type = -103;
		age = -105;
		//time = System.currentTimeMillis();
		
		//populate the edges
		JSONArray edges = jsonObj.getJSONArray("edges");
		Iterator<Object> itEdges = edges.iterator();
		while(itEdges.hasNext()) {
			
			JSONObject edge = (JSONObject)(itEdges.next());
			index = edge.getInt("edgeIndex");
			node1 = edge.getInt("node1");
			node2 = edge.getInt("node2");
			weight = edge.getInt("edgeWeight");
			type = (byte)(edge.getInt("edgeType"));
			age = (byte)(edge.getInt("edgeAge"));
			label = edge.getString("edgeLabel");
			
			edgeBuf.putInt(EDGE_NODE1_OFFSET+index*EDGE_BYTE_SIZE,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+index*EDGE_BYTE_SIZE,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+index*EDGE_BYTE_SIZE,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+index*EDGE_BYTE_SIZE,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+index*EDGE_BYTE_SIZE,age); // age
			
			// save labels for later
			edgeLabels[index] = label;
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(index);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(index);
			

		}

		setAllEdgeLabels(edgeLabels);
	
		// Initialise the connection buffer, modifying the node buffer connection data
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
			// now put the in edge/node pairs
			for(int edgeIndex : inEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);

			// now put the out edge/node pairs
			for(int edgeIndex : outEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
		}

	}



	/**
	 * saves the current graph to several files, in directory given to base name given  (i.e. fileBaseName should have no extension).
	 * If directory is null, then to a directory named data under current working directory. Directory should
	 * already exist.
	 * @param directory where the files are to be stored, or if null fileBaseName under data under the current working directory
	 * @param fileBaseName the name of the files, to which extensions are added
	 */
	@SuppressWarnings("resource")
	public void saveBuffers(String directory, String fileBaseName) {
		File file;
		
		String directoryAndBaseName = "";
		if(directory != null) {
			if(directory.charAt(directory.length()-1)== File.separatorChar) {
				directoryAndBaseName = directory+fileBaseName;
			} else {
				directoryAndBaseName = directory+File.separatorChar+fileBaseName;
			}
		} else {
			directoryAndBaseName = Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+fileBaseName+File.separatorChar+fileBaseName;
			new File(Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+fileBaseName).mkdirs();
		}
		
		boolean append;
		FileChannel wChannel;
		try {
			FileOutputStream fso = new FileOutputStream(directoryAndBaseName+".info");
			Writer writer = new BufferedWriter(new OutputStreamWriter(fso, "utf-8"));
			writer.write("name"+INFO_SPLIT_STRING+name+"\n");
			writer.write("numberOfNodes"+INFO_SPLIT_STRING+numberOfNodes+"\n");
			writer.write("numberOfEdges"+INFO_SPLIT_STRING+numberOfEdges+"\n");
			writer.write("numberOfNodeLabelBytes"+INFO_SPLIT_STRING+nodeLabelBuf.capacity()+"\n");
			writer.write("numberOfEdgeLabelBytes"+INFO_SPLIT_STRING+nodeLabelBuf.capacity()+"\n");
			String directValue = "false";
			if(direct) {
				directValue = "true";
			}
			writer.write("direct"+INFO_SPLIT_STRING+directValue+"\n");

			writer.close();
			
			file = new File(directoryAndBaseName+".nodeBuf");
			append = false;
			wChannel = new FileOutputStream(file, append).getChannel();
			wChannel.write(nodeBuf);
			wChannel.close();
			
			file = new File(directoryAndBaseName+".edgeBuf");
			append = false;
			wChannel = new FileOutputStream(file, append).getChannel();
			wChannel.write(edgeBuf);
			wChannel.close();
			
			file = new File(directoryAndBaseName+".connectionBuf");
			append = false;
			wChannel = new FileOutputStream(file, append).getChannel();
			wChannel.write(connectionBuf);
			wChannel.close();
			file = new File(directoryAndBaseName+".nodeLabelBuf");
			append = false;
			wChannel = new FileOutputStream(file, append).getChannel();
			wChannel.write(nodeLabelBuf);
			wChannel.close();
			
			file = new File(directoryAndBaseName+".edgeLabelBuf");
			append = false;
			wChannel = new FileOutputStream(file, append).getChannel();
			wChannel.write(edgeLabelBuf);
			wChannel.close();
			
		}catch(Exception e) {
			System.out.println("ERROR executing saveBuffers("+directory+","+fileBaseName+")");
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Creates a graph from a SNAP .txt adjacency list file. Number of nodes and edges are given
	 * by the <a href="https://snap.stanford.edu/data/">SNAP website</a>. Assumes edges
	 * represented by one node index pair per line delimited by tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory If directory is null, then to a
	 * directory named data/snap under current working directory.
	 * @throws Exception Throws if the adjacency list cannot be built correctly. Might be an IO error
	 */
	public static FastGraph adjacencyListGraphFactory(int nodeCount, int edgeCount, String directory, String fileName, boolean direct) throws Exception {
		FastGraph g = new FastGraph(nodeCount,edgeCount,direct);
		g.setName(fileName);
		g.loadAdjacencyListGraph(directory,fileName);
		return g;
	}


	
	/**
     * Assumes edges represented by one node index pair per line delimited by
     * tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory. If directory is null, then to a
	 * directory named /data/snap under current working directory.
	 * 
	 * @throws IOException If the buffers cannot be loaded
	 */
	private void loadAdjacencyListGraph(String dir, String fileName) throws Exception {
	
		String directory = dir;
		if(directory == null) {
			directory = Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+"snap";
		}
		String path = null;
		if(directory.charAt(directory.length()-1)== File.separatorChar) {
			path = directory+fileName;
		} else {
			path = directory+File.separatorChar+fileName;
		}
		
		int edgeIndex = 0;
		int nodeIndex = 0;
		HashMap<String,Integer> nodeSnapIdToIndexMap = new HashMap<String,Integer>(numberOfNodes*4);
		HashMap<Integer,String> nodeIndexToSnapIdMap = new HashMap<Integer,String>(numberOfNodes*4);
		HashMap<Integer,Integer> edgeNode1Map = new HashMap<Integer,Integer>(numberOfEdges*4);
		HashMap<Integer,Integer> edgeNode2Map = new HashMap<Integer,Integer>(numberOfEdges*4);
		
		
		File f = new File(path);
		if(!f.exists()) {
			throw new IOException("Problem loading file "+path+". If you expect to access a SNAP file try downloading the file from:\nhttps://snap.stanford.edu/data/\nthen unzipping it and placing it in the directory "+directory);
			//System.exit(1);
		}
		
		FileInputStream is = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String[] splitLine;
		
		String line = "";
long time = System.currentTimeMillis();			
		while(line != null) {
			line = br.readLine();
			if(line == null) {
				continue;
			}
			if(line.length() == 0) {
				continue;
			}
			if(line.charAt(0) == '#') {
				continue;
			}
			splitLine = line.split(" ");
			if(splitLine.length < 2) {
				splitLine = line.split("\t");
				if(splitLine.length < 2) {
					System.out.println("FAILED TO RECOGNISE LINE:"+line+" in loadAdjacencyListGraph("+directory+","+fileName+")");
					continue;
				}
			}
	
			String node1String = splitLine[0];
			String node2String = splitLine[1];
			
			if(!nodeSnapIdToIndexMap.containsKey(node1String)) {
				nodeSnapIdToIndexMap.put(node1String,nodeIndex);
				nodeIndexToSnapIdMap.put(nodeIndex,node1String);
				nodeIndex++;
			}
			if(!nodeSnapIdToIndexMap.containsKey(node2String)) {
				nodeSnapIdToIndexMap.put(node2String,nodeIndex);
				nodeIndexToSnapIdMap.put(nodeIndex,node2String);
				nodeIndex++;
			}
			edgeNode1Map.put(edgeIndex, nodeSnapIdToIndexMap.get(node1String));
			edgeNode2Map.put(edgeIndex, nodeSnapIdToIndexMap.get(node2String));
				
			edgeIndex++;
if(edgeIndex%1000000==0 ) {
	System.out.println("edgesLoaded "+edgeIndex+" time "+(System.currentTimeMillis()-time)/1000.0);
}
		}
			
		

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -88;
		short inLength = -33;
		int outStart = -77;
		short outLength = -22;
		int weight = -55;
		byte type = -77;
		byte age = -99;
		for(int i = 0; i < numberOfNodes; i++) {
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+i*NODE_BYTE_SIZE,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+i*NODE_BYTE_SIZE,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+i*NODE_BYTE_SIZE,age); // age

			// save labels for later
			String label = nodeIndexToSnapIdMap.get(i);
			nodeLabels[i] = label;

		}

		setAllNodeLabels(nodeLabels);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(i,edges);
		}
				
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		weight = -51;
		type = -53;
		age = -55;
		for(int i = 0; i < numberOfEdges; i++) {
			node1 = edgeNode1Map.get(i);
			node2 = edgeNode2Map.get(i);
			edgeBuf.putInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+i*EDGE_BYTE_SIZE,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+i*EDGE_BYTE_SIZE,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+i*EDGE_BYTE_SIZE,age); // age
			
			// store labels for later
			String label = "e"+i;
			edgeLabels[i] = label;
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(i);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(i);

		}
		
		setAllEdgeLabels(edgeLabels);


		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int e : inEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int e : outEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
	}


	
	/**
	 * loads the current graph from several files created by saveBuffers,
	 * in directory given to base name given (i.e. fileBaseName should have no extension).
	 * If directory is null, then to a directory named data under current working directory.
	 * 
	 * @param directory where the files are held, or if null fileBaseName under data under the current working directory
	 * @param fileBaseName the name of the files, to which extensions are added
	 * @return the created FastGraph
	 * @throws IOException If the buffers cannot be loaded
	 */
	@SuppressWarnings("resource")
	private static FastGraph loadBuffers(String directory, String fileBaseName) throws IOException {
		String directoryAndBaseName = Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+fileBaseName+File.separatorChar+fileBaseName;
		if(directory != null) {
			if(directory.charAt(directory.length()-1)== File.separatorChar) {
				directoryAndBaseName = directory+fileBaseName;
			} else {
				directoryAndBaseName = directory+File.separatorChar+fileBaseName;
			}
		}
		
		FastGraph g = null;
		
		File file;
		FileChannel rChannel;
		String line;
		String[] splitLine;

		FileInputStream is = new FileInputStream(directoryAndBaseName+".info");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		String name = splitLine[1];
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		int inNodeTotal = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		int inEdgeTotal = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		int inNodeLabelSize = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		int inEdgeLabelSize = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		String directValue = splitLine[1];
		boolean inDirect = true;
		if(directValue.equals("false")) {
			inDirect = false;
		}
		br.close();
		
		g = new FastGraph(inNodeTotal, inEdgeTotal, inDirect);
		if(!inDirect) {
			g.nodeLabelBuf = ByteBuffer.allocate(inNodeLabelSize);
			g.edgeLabelBuf = ByteBuffer.allocate(inEdgeLabelSize);
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(inNodeLabelSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(inEdgeLabelSize);
		}
		g.setName(name);

		file = new File(directoryAndBaseName+".nodeBuf");
		rChannel = new FileInputStream(file).getChannel();
		rChannel.read(g.nodeBuf);
		rChannel.close();

		file = new File(directoryAndBaseName+".edgeBuf");
		rChannel = new FileInputStream(file).getChannel();
		rChannel.read(g.edgeBuf);
		rChannel.close();
		
		file = new File(directoryAndBaseName+".connectionBuf");
		rChannel = new FileInputStream(file).getChannel();
		rChannel.read(g.connectionBuf);
		rChannel.close();

		file = new File(directoryAndBaseName+".nodeLabelBuf");
		rChannel = new FileInputStream(file).getChannel();
		rChannel.read(g.nodeLabelBuf);
		rChannel.close();
		
		file = new File(directoryAndBaseName+".edgeLabelBuf");
		rChannel = new FileInputStream(file).getChannel();
		rChannel.read(g.edgeLabelBuf);
		rChannel.close();

		
		return g;
	}


	/**
	 * Creates a graph with the size specified by numberOfNodes and numberOfEdges. Possibly includes parallel edges and self sourcing nodes
	 * 
	 * @param seed the random number generator seed, -1 for current time
	 */
	public void populateRandomGraph(long seed) {

		//long time;
		if(seed == -1) {
			seed = System.currentTimeMillis();
		}
		Random r = new Random(seed);

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -888;
		short inLength = -3;
		int outStart = -777;
		short outLength = -2;
		int weight = -5;
		byte type = -7;
		byte age = -9;
		//generate the nodes
		for(int i = 0; i < numberOfNodes; i++) {
			weight = r.nextInt(100);
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+i*NODE_BYTE_SIZE,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+i*NODE_BYTE_SIZE,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+i*NODE_BYTE_SIZE,age); // age
			
			// store labels for later
			String label = "n"+i;
			nodeLabels[i] = label;

		}

		setAllNodeLabels(nodeLabels);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(i,edges);
		}
				
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		weight = -101;
		type = -103;
		age = -105;
		//generate the edges, with random node connections, possibly including parallel edges and self sourcing nodes
		for(int i = 0; i < numberOfEdges; i++) {
			weight = r.nextInt(100);
			node1 = r.nextInt(numberOfNodes);
			node2 = r.nextInt(numberOfNodes);
			edgeBuf.putInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+i*EDGE_BYTE_SIZE,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+i*EDGE_BYTE_SIZE,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+i*EDGE_BYTE_SIZE,age); // age
			
			// label
			String label = "e"+i;
			edgeLabels[i] = label;
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(i);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(i);
			
		}

		setAllEdgeLabels(edgeLabels);
		
		// Initialise the connection buffer, modifying the node buffer connection data
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int e : inEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int e : outEdges) {
				int n = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
		
		}
		//System.out.println("connection put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());

	}
	
	
	
	/**
	 * Creates a graph from a displayGraph.Graph. label becomes the displayGraph.Graph name
	 * node and edge labels, are taken from displayGraph.Graph nodes and edges.
	 * node and edge weights are from displayGraph node and edge scores. Types are
	 * from displayGraph edgeType edgeType if they can be parsed as bytes,
	 * otherwise they get a default of -1. Node and edge Age is from displayGraph age, but
	 * only least significant byte, as the displayGraph age is a integer.
	 * Order of nodes and edges is as in the displayGraph.Graph
	 * 
	 * @param displayGraph the graph that the new FastGraph is based on
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return new FastGraph with attributes based on the given displayGraph.
	 */
	public static FastGraph displayGraphFactory(Graph displayGraph, boolean direct) {
		FastGraph g = new FastGraph(displayGraph.getNodes().size(),displayGraph.getEdges().size(),direct);
		g.setName(displayGraph.getLabel());
		g.populateFromDisplayGraph(displayGraph);
		return g;
	}


	
	/**
	 * Populates byteBuffers based on the contents of the displayGraph.graph.
	 * Nodes and edges are in the order they appear in the displayGraph.
	 * 
	 * @param displayGraph the graph that the new FastGraph is based on
	 */
	private void populateFromDisplayGraph(Graph displayGraph) {

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -27;
		short inLength = -37;
		int outStart = -47;
		short outLength = -57;
		int weight = -67;
		byte type = -77;
		byte age = -87;
		ByteBuffer bb = ByteBuffer.allocate(4); // used to convert from int to byte, due to lack of direct casting
		// nodes first, will be in the same order as the list in the displayGraph
		for(int i = 0; i < numberOfNodes; i++) {
			Node dgn = displayGraph.getNodes().get(i);
			weight = (int)(dgn.getScore());
			bb.putInt(0,dgn.getAge());
			age = bb.get(3); // get least significant byte of age
			try {
				type = Byte.parseByte(dgn.getType().getLabel());
			} catch(NumberFormatException e) {
				type = -1;
			}
			
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+i*NODE_BYTE_SIZE,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+i*NODE_BYTE_SIZE,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+i*NODE_BYTE_SIZE,age); // age

			// store labels for later
			String label = dgn.getLabel();
			nodeLabels[i] = label;

		}

		setAllNodeLabels(nodeLabels);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i < numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(i,edges);
		}
				
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		weight = -15;
		type = -25;
		age = -35;
		// edges once nodes exist, will be in the same order as the list in the displayGraph
		for(int i = 0; i < numberOfEdges; i++) {
			Edge dge = displayGraph.getEdges().get(i);
			node1 = displayGraph.getNodes().indexOf(dge.getFrom()); // we can find the FastGraph node index from its position in the displayGraph nodeList
			node2 = displayGraph.getNodes().indexOf(dge.getTo()); // we can find the FastGraph node index from its position in the displayGraph nodeList
			weight = (int)(dge.getScore());
			bb.putInt(0,dge.getAge());
			age = bb.get(3); // get least significant byte of age
			try {
				type = Byte.parseByte(dge.getType().getLabel());
			} catch(NumberFormatException e) {
				type = -1;
			}
			
			edgeBuf.putInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+i*EDGE_BYTE_SIZE,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+i*EDGE_BYTE_SIZE,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+i*EDGE_BYTE_SIZE,age); // age
			
			// store labels for later
			String label = dge.getLabel();
			edgeLabels[i] = label;
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(i);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(i);

		}
		
		setAllEdgeLabels(edgeLabels);

		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int edgeIndex : inEdges) {
				int nodeIndex = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				if(n1 == i) {
					nodeIndex = n2;
				} else if(n2 == i) {
					nodeIndex = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int edgeIndex : outEdges) {
				int nodeIndex = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				if(n1 == i) {
					nodeIndex = n2;
				} else if(n2 == i) {
					nodeIndex = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
	}
	
	
	

	/**
	 * Generates a new graph from the subgraph specified by the parameters.
	 * 
	 * @aparam subgraphNodes nodes in this graph that will appear in the new graph
	 * @aparam subgraphEdges edges in this graph that will appear in the new graph, must connect only to subgraphNodes
	 * @return
	 */
	public FastGraph generateGraphFromSubgraph(int[] subgraphNodes, int[] subgraphEdges) {

		FastGraph g = new FastGraph(subgraphNodes.length, subgraphEdges.length, getDirect());
		
		String[] nodeLabels = new String[subgraphNodes.length]; // stores the labels for creating the nodeLabelBuffer
		HashMap<Integer,Integer> oldNodesToNew = new HashMap<>(subgraphNodes.length*4); // for reference when adding edges, multiplier reduces chances of clashes
		// initial population of the new node array
		int weight = -98;
		byte type = -97;
		byte age = -96;
		int index = 0;
		for(int n : subgraphNodes) {

			weight = nodeBuf.getInt(NODE_WEIGHT_OFFSET+n*NODE_BYTE_SIZE);
			type = nodeBuf.get(NODE_TYPE_OFFSET+n*NODE_BYTE_SIZE);
			age = nodeBuf.get(NODE_AGE_OFFSET+n*NODE_BYTE_SIZE);

			g.nodeBuf.putInt(NODE_WEIGHT_OFFSET+index*NODE_BYTE_SIZE,weight);
			g.nodeBuf.put(NODE_TYPE_OFFSET+index*NODE_BYTE_SIZE,type);
			g.nodeBuf.put(NODE_AGE_OFFSET+index*NODE_BYTE_SIZE,age);
			
			// store labels for later
			nodeLabels[index] = getNodeLabel(n);
			// store old to new mapping for later
			oldNodesToNew.put(n, index);
			index++;
		}
		
		g.setAllNodeLabels(nodeLabels); // create the node label buffer
		
		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(subgraphNodes.length); // temporary store of inward edges
		for(int nodeIndex = 0; nodeIndex < subgraphNodes.length; nodeIndex++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(nodeIndex,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(subgraphNodes.length); // temporary store of outward edges
		for(int nodeIndex = 0; nodeIndex < subgraphNodes.length; nodeIndex++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(nodeIndex,edges);
		}
				
		String[] edgeLabels = new String[subgraphEdges.length]; // stores the labels for creating the edgeLabelBuffer
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		// create the edges
		index = 0;
		edgeBuf.position(0);
		g.edgeBuf.position(0);
		for(int e : subgraphEdges) {
			
			weight = edgeBuf.getInt(EDGE_WEIGHT_OFFSET+e*EDGE_BYTE_SIZE);
			type = edgeBuf.get(EDGE_TYPE_OFFSET+e*EDGE_BYTE_SIZE);
			age = edgeBuf.get(EDGE_AGE_OFFSET+e*EDGE_BYTE_SIZE);

			g.edgeBuf.putInt(EDGE_WEIGHT_OFFSET+index*EDGE_BYTE_SIZE,weight);
			g.edgeBuf.put(EDGE_TYPE_OFFSET+index*EDGE_BYTE_SIZE,type);
			g.edgeBuf.put(EDGE_AGE_OFFSET+index*EDGE_BYTE_SIZE,age);
			
			int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
			int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
			
			int gn1 = oldNodesToNew.get(n1);
			int gn2 = oldNodesToNew.get(n2);
			
			g.edgeBuf.putInt(EDGE_NODE1_OFFSET+index*EDGE_BYTE_SIZE,gn1); // one end of edge
			g.edgeBuf.putInt(EDGE_NODE2_OFFSET+index*EDGE_BYTE_SIZE,gn2); // other end of edge
			
			// store labels for later
			edgeLabels[index] = getEdgeLabel(e);
			
			// store connecting edges
			inEdgeList = nodeIn.get(gn2);
			inEdgeList.add(index);
			outEdgeList = nodeOut.get(gn1);
			outEdgeList.add(index);
			index++;
		}

		g.setAllEdgeLabels(edgeLabels);
		
		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int node = 0; node < subgraphNodes.length; node++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(node);
			short inEdgeLength = (short)(inEdges.size());
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putShort(node*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
			
			// now put the in edge/node pairs
			for(int edgeIndex : inEdges) {
				int nodeIndex = -1;
				int n1 = g.edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = g.edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				if(n1 == node) {
					nodeIndex = n2;
				} else if(n2 == node) {
					nodeIndex = n1;
				} else {
					System.out.println("ERROR A When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(node);
			short outEdgeLength = (short)(outEdges.size());
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putShort(node*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
			
			// now put the out edge/node pairs
			for(int edgeIndex : outEdges) {
				int nodeIndex = -1;
				int n1 = g.edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeIndex*EDGE_BYTE_SIZE);
				int n2 = g.edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeIndex*EDGE_BYTE_SIZE);

				if(n1 == node) {
					nodeIndex = n2;
				} else if(n2 == node) {
					nodeIndex = n1;
				} else {
					System.out.println("ERROR B When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
		
		return g;
	}


	
	/**
	 * @return the largest degree for a node in the graph.
	 */
	public int maximumDegree() {
		
		int max = 0;
		for(int i = 0; i < numberOfNodes; i++) {
				short inDegree = nodeBuf.getShort(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
				short outDegree = nodeBuf.getShort(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
				int degree = inDegree+outDegree;
				if(degree > max) {
					max = degree;
				}
		}
		return max;
	}
	
	/**
	 * Creates a displayGraph.Graph which can then be accessed, manipulated and visualized
	 * using that package. displayGraph.Graph name becomes this FastGraph label
	 * The displayGraph.Graph node and edge labels, are taken
	 * from this FastGraph nodes and edges. node and edge weights become node
	 * and edge scores node and edge ages become ages in displayGraph nodes and edges.
	 * New NodeType and EdgeType are created if needed with label of the integer of this type.
	 * Order of nodes and edges in the displayGraph.Graph is as this FastGraph.
	 * 
	 * @return a displayGraph.Graph with the same data as this Fast Graph
	 */
	public Graph generateDisplayGraph() {
		
		Graph g = new Graph(getName());
		
		for(int i = 0; i < numberOfNodes; i++) {
			Node n = new Node();
			n.setLabel(getNodeLabel(i));
			n.setScore(getNodeWeight(i));
			n.setAge(getNodeAge(i));
			String typeLabel = Integer.toString(getNodeType(i));
			NodeType type = NodeType.withLabel(typeLabel);
			if(type == null) {
				type = new NodeType(typeLabel);
			}
			n.setType(type);
			g.addNode(n);
		}
		
		for(int i = 0; i < numberOfEdges; i++) {
			Node n1 = g.getNodes().get(getEdgeNode1(i));
			Node n2 = g.getNodes().get(getEdgeNode2(i));
			Edge e = new Edge(n1,n2);
			e.setLabel(getEdgeLabel(i));
			e.setScore(getEdgeWeight(i));
			e.setAge(getEdgeAge(i));
			String typeLabel = Integer.toString(getEdgeType(i));
			EdgeType type = EdgeType.withLabel(typeLabel);
			if(type == null) {
				type = new EdgeType(typeLabel);
			}
			e.setType(type);
			g.addEdge(e);
		}
		
		return g;
	}
	
	
}
