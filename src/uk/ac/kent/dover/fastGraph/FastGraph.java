package uk.ac.kent.dover.fastGraph;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import org.json.*;

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
 * node and edge ids are integers and must start at 0 and end and size-1. Ids are not stored,
 * they are assumed, so node info with nodeId nid can be found starting in nodeBuf at offset nid*nodeByteSize,
 * similarly eid starts in edgeBuf at eid*edgeByteSize.
 * <p>
 * <ul>
 * <li>nodeBuf stores offset of label start in nodeLabelBuf and size (in chars) of labels.</li>
 * <li>nodeBuf stores in and out offset and in and out number (degree) of connecting nodes and edges start
 * which link to connectionBuf and size (in chars) of in or out edges.</li>
 * <li>edgeBuf stores offset of label start in edgeLabelBuf and size (in chars) of labels.</li>
 * <li>connectionBuf stores pairs of edgeId/nodeId (store both for fastest access) which form a
 * list of connecting items, for each node the in edge/nodes first, then out edge nodes</li>
 * </ul>
 * json from <a href="https://github.com/stleary/JSON-java"> json library </a>
 * 
 * @author pjr
 *
 */
/**
 * @author pjr
 *
 */
public class FastGraph {

	private static final int NODE_LABEL_START_OFFSET = 0; // integer
	private static final int NODE_LABEL_LENGTH_OFFSET = 4; // short
	private static final int NODE_IN_CONNECTION_START_OFFSET = 6; // integer
	private static final int NODE_IN_DEGREE_OFFSET = 10; // short
	private static final int NODE_OUT_CONNECTION_START_OFFSET = 12; // integer
	private static final int NODE_OUT_DEGREE_OFFSET = 16; // short
	private static final int NODE_WEIGHT_OFFSET = 18; // integer
	private static final int NODE_TYPE_OFFSET = 22; // byte
	private static final int NODE_AGE_OFFSET = 23; // byte
	
	private static final int EDGE_NODE1_OFFSET = 0; // integer
	private static final int EDGE_NODE2_OFFSET = 4; // integer
	private static final int EDGE_LABEL_START_OFFSET = 8; // integer
	private static final int EDGE_LABEL_LENGTH_OFFSET = 12; // short
	private static final int EDGE_WEIGHT_OFFSET = 14; // byte
	private static final int EDGE_TYPE_OFFSET = 18; // byte
	private static final int EDGE_AGE_OFFSET = 19; // byte
	
	private static final int CONNECTION_EDGE_OFFSET = 0; // integer, edge is first of the pair
	private static final int CONNECTION_NODE_OFFSET = 4; // integer, node is straight after the edge
	
	public static final int DEFAULT_AVERAGE_LABEL_LENGTH = 20;
	
	private static int nodeByteSize = 24;
	private static int edgeByteSize = 20;
	private static int connectionPairSize = 8; // this is an edge id plus an node id
	
	public static final String INFO_SPLIT_STRING = "~";
	

	private ByteBuffer nodeBuf;
	private ByteBuffer edgeBuf;
	private ByteBuffer connectionBuf;
	private ByteBuffer nodeLabelBuf;
	private ByteBuffer edgeLabelBuf;

	private int numberOfNodes;
	private int numberOfEdges;
	private int averageNodeLabelLength; // estimate of largest value for average node label length.
	private int averageEdgeLabelLength; // estimate of largest value for average edge label length. 
	
	
	private String name = "";
	private boolean direct;
		
	/**
	 * No direct access to constructor, as a number of data structures need to be created when
	 * graph nodes and edges are added.
	 */
	private FastGraph(int nodeTotal, int edgeTotal, int averageNodeLabelLength, int averageEdgeLabelLength, boolean direct) {
		
		this.numberOfNodes = nodeTotal;
		this.numberOfEdges = edgeTotal;
		this.averageNodeLabelLength = averageNodeLabelLength;
		this.averageEdgeLabelLength = averageEdgeLabelLength;
		this.direct = direct;
		
		init();
		
		
	}

	
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		long time;
		
//		FastGraph g1 = randomGraphFactory(2,1,false);
		FastGraph g1 = randomGraphFactory(5,6,true);
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
/*		
		time = System.currentTimeMillis();
		g1.saveBuffers(null,g1.getName());
		System.out.println("saveBuffers test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
*/
		
		time = System.currentTimeMillis();

String name = "random-n-2-e-1";
//		String name = g1.getName();
		FastGraph g2 = loadBuffersGraphFactory(null,name);

 		System.out.println("create graph from file test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
	
 		
		
		time = System.currentTimeMillis();
		boolean connected = g2.isConnected();
		System.out.println("connected test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
		
		System.out.println("connected "+connected);
		
		time = System.currentTimeMillis();
		int[][] matrix = g2.buildIntAdjacencyMatrix();
		//boolean[][] matrix = g2.buildBooleanAdjacencyMatrix();
		System.out.println("building matrix test time " + (System.currentTimeMillis()-time)/1000.0+" seconds");
		g2.printMatrix(matrix);
		System.out.println(Arrays.toString(g2.findEigenvalues(matrix)));
		System.out.println(matrix.length);
	}	


	public int getNumberOfNodes() {
		return numberOfNodes;
	}


	public int getNumberOfEdges() {
		return numberOfEdges;
	}

	

	public String getNodeLabel(int nodeId) {
		int labelStart = nodeBuf.getInt(NODE_LABEL_START_OFFSET+nodeId*nodeByteSize);
		int labelLength = nodeBuf.getShort(NODE_LABEL_LENGTH_OFFSET+nodeId*nodeByteSize);
		char[] label = new char[labelLength];
		for(int i = 0; i < labelLength; i++) {
			int offset = labelStart+i*2;
			char c = nodeLabelBuf.getChar(offset);
			label[i] = c;
		}
		String ret = new String(label);
		return ret;
	}
	
	
	public int getNodeWeight(int nodeId) {
		int type= nodeBuf.getInt(NODE_WEIGHT_OFFSET+nodeId*nodeByteSize);
		return type;
	}
	
	
	public byte getNodeType(int nodeId) {
		byte type= nodeBuf.get(NODE_TYPE_OFFSET+nodeId*nodeByteSize);
		return type;
	}
	
	
	public byte getNodeAge(int nodeId) {
		byte age = nodeBuf.get(NODE_AGE_OFFSET+nodeId*nodeByteSize);
		return age;
	}
	
	public int getNodeDegree(int nodeId) {
		int degree = getNodeInDegree(nodeId)+getNodeOutDegree(nodeId);
		return degree;
	}
	
	public short getNodeInDegree(int nodeId) {
		short degree = nodeBuf.getShort(NODE_IN_DEGREE_OFFSET+nodeId*nodeByteSize);
		return degree;
	}
	
	public short getNodeOutDegree(int nodeId) {
		short degree = nodeBuf.getShort(NODE_OUT_DEGREE_OFFSET+nodeId*nodeByteSize);
		return degree;
	}
	
	
	

	public int[] getNodeConnectingEdges(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeDegree(nodeId);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}

	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingEdges(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}

	public int[] getNodeConnectingNodes(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeDegree(nodeId);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}

	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingNodes(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
	}

	public int[] getNodeConnectingInEdges(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeInDegree(nodeId);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}

	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeInDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingInEdges(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeInDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	
	public int[] getNodeConnectingInNodes(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeInDegree(nodeId);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}

	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeInDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingInNodes(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeInDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}

	public int[] getNodeConnectingOutEdges(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeOutDegree(nodeId);
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	
	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeOutDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingOutEdges(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeOutDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_EDGE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	public int[] getNodeConnectingOutNodes(int nodeId) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeOutDegree(nodeId);
		
		int[] ret = new int[degree];
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
		
		return ret;
	}
	
	/**
	 * This version puts the connecting ids in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeOutDegree(nodeId) or maxDegree(). array elements beyond nodeDegree(nodeId)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 */
	public void getNodeConnectingOutNodes(int nodeId, int[] ret) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*nodeByteSize); // in offset is the first one
		int degree = getNodeOutDegree(nodeId);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the ege
			int nodeOffset = connectionOffset+(i*connectionPairSize)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			ret[i] = node;
		}
	}


	
	public String getEdgeLabel(int edgeId) {
		int labelStart = edgeBuf.getInt(EDGE_LABEL_START_OFFSET+edgeId*edgeByteSize);
		int labelLength = edgeBuf.getShort(EDGE_LABEL_LENGTH_OFFSET+edgeId*edgeByteSize);
		char[] label = new char[labelLength];
		for(int i = 0; i < labelLength; i++) {
			int offset = labelStart+i*2;
			char c = edgeLabelBuf.getChar(offset);
			label[i] = c;
		}
		String ret = new String(label);
		return ret;
	}

	
	public int getEdgeNode1(int edgeId) {
		int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
		return n1;
	}
	
	
	public int getEdgeNode2(int edgeId) {
		int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
		return n2;
	}
	
	
	public int getEdgeWeight(int edgeId) {
		int type= edgeBuf.getInt(EDGE_WEIGHT_OFFSET+edgeId*edgeByteSize);
		return type;
	}
	
	
	public byte getEdgeType(int edgeId) {
		byte type= edgeBuf.get(EDGE_TYPE_OFFSET+edgeId*edgeByteSize);
		return type;
	}
	
	
	public byte getEdgeAge(int edgeId) {
		byte age = edgeBuf.get(EDGE_AGE_OFFSET+edgeId*edgeByteSize);
		return age;
	}

	public String getName() {return name;}
	
	/**
	 * Names should be simple alphanumeric. Spaces and dashes are permitted. Note that tilde ("~") cannot be used.
	 */
	public void setName(String name) {this.name = name;}



	private void init() {
		
		int nodeLabelSize;
		long longNodeLabelSize = (long)numberOfNodes*(long)averageNodeLabelLength*2;
		if(longNodeLabelSize > Integer.MAX_VALUE-5000) { // if the average calculation will not fit, max out the buffer and hope
			nodeLabelSize = Integer.MAX_VALUE-5000; // Integer.MAX_VALUE-5000 seems to be value that allocateDirect() and the Writer.write(buffer) can cope with
		} else {
			nodeLabelSize = (int)longNodeLabelSize;
		}
		int edgeLabelSize;
		long longEdgeLabelSize = (long)numberOfEdges*(long)averageEdgeLabelLength*2;
		if(longEdgeLabelSize > Integer.MAX_VALUE-5000) {
			edgeLabelSize = Integer.MAX_VALUE-5000; // if the average calculation will not fit, max out the buffer and hope
		} else {
			edgeLabelSize = (int)longEdgeLabelSize;
		}

		if(!direct) {
			nodeBuf = ByteBuffer.allocate(numberOfNodes*nodeByteSize);
			edgeBuf = ByteBuffer.allocate(numberOfEdges*edgeByteSize);
			connectionBuf = ByteBuffer.allocate(numberOfEdges*2*connectionPairSize);
			nodeLabelBuf = ByteBuffer.allocate(nodeLabelSize);
			edgeLabelBuf = ByteBuffer.allocate(edgeLabelSize);
		} else {
			nodeBuf = ByteBuffer.allocateDirect(numberOfNodes*nodeByteSize);
			edgeBuf = ByteBuffer.allocateDirect(numberOfEdges*edgeByteSize);
			connectionBuf = ByteBuffer.allocateDirect(numberOfEdges*2*connectionPairSize);
			nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelSize);
			edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelSize);
		}
		
		nodeBuf.clear();
		edgeBuf.clear();
		connectionBuf.clear();
		
	}


	public static FastGraph jsonStringGraphFactory(String json, boolean direct) {
		
		int nodeCount = 0;
		int edgeCount = 0;
		
		JSONObject jsonObj = new JSONObject(json);
		
		String graphName = jsonObj.getString("name");
		
		JSONArray nodes = jsonObj.getJSONArray("nodes");
		Iterator<Object> itNodes = nodes.iterator();
		while(itNodes.hasNext()) {
			JSONObject node = (JSONObject)(itNodes.next());
			int id = node.getInt("nodeId");
			if(id+1 > nodeCount) {
				nodeCount = id+1;
			}
		}
		
		JSONArray edges = jsonObj.getJSONArray("edges");
		Iterator<Object> itEdges = edges.iterator();
		while(itEdges.hasNext()) {
			JSONObject edge = (JSONObject)(itEdges.next());
			int id = edge.getInt("edgeId");
			if(id+1 > edgeCount) {
				edgeCount = id+1;
			}
		}

		FastGraph g = new FastGraph(nodeCount,edgeCount,DEFAULT_AVERAGE_LABEL_LENGTH,DEFAULT_AVERAGE_LABEL_LENGTH,direct);
		g.populateFromJsonString(jsonObj, direct);
		g.setName(graphName);
		
		return g;
	}


	/**
	 * Generate a random graph of the desired size. Self sourcing edges may exist.
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, boolean direct) {
		FastGraph g = new FastGraph(numberOfNodes,numberOfEdges,DEFAULT_AVERAGE_LABEL_LENGTH,DEFAULT_AVERAGE_LABEL_LENGTH,direct);
		g.setName("random-n-"+numberOfNodes+"-e-"+numberOfEdges);
		g.populateRandomGraph();
		return g;
	}

	/**
	 * creates a FastGraph by loading in various files from the given directory, or data under
	 * current working directory if directory is null.
	 * @See loadBuffers
	 */
	public static FastGraph loadBuffersGraphFactory(String directory, String fileBaseName) {
		FastGraph g = loadBuffers(null,fileBaseName);
		return g;
	}

	
	
	private void populateFromJsonString(JSONObject jsonObj, boolean direct2) {

		//long time;

		short labelLength = -999;
		int inStart = -888;
		short inLength = -3;
		int outStart = -777;
		short outLength = -2;
		int id = -1;
		int weight = -5;
		byte type = -7;
		byte age = -9;
		int labelOffset = 0;
		String label;
		
		//time = System.currentTimeMillis();
		JSONArray nodes = jsonObj.getJSONArray("nodes");
		Iterator<Object> itNodes = nodes.iterator();
		while(itNodes.hasNext()) {
			
			JSONObject node = (JSONObject)(itNodes.next());
			id = node.getInt("nodeId");
			weight = node.getInt("nodeWeight");
			type = (byte)(node.getInt("nodeType"));
			age = (byte)(node.getInt("nodeAge"));
			label = node.getString("nodeLabel");
			
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+id*nodeByteSize,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+id*nodeByteSize,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+id*nodeByteSize,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+id*nodeByteSize,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+id*nodeByteSize,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+id*nodeByteSize,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+id*nodeByteSize,age); // age
			
			// label
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			nodeBuf.putInt(NODE_LABEL_START_OFFSET+id*nodeByteSize,labelOffset); // label start
			nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+id*nodeByteSize,labelLength); // label size

			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				nodeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}

		}
		// System.out.println("node put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+nodeBuf.isDirect());



		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeOut.add(i,edges);
		}
		
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		id = -1;
		weight = -101;
		type = -103;
		age = -105;
		labelOffset = 0;
		//time = System.currentTimeMillis();
		
		JSONArray edges = jsonObj.getJSONArray("edges");
		Iterator<Object> itEdges = edges.iterator();
		while(itEdges.hasNext()) {
			
			JSONObject edge = (JSONObject)(itEdges.next());
			id = edge.getInt("edgeId");
			node1 = edge.getInt("node1");
			node2 = edge.getInt("node2");
			weight = edge.getInt("edgeWeight");
			type = (byte)(edge.getInt("edgeType"));
			age = (byte)(edge.getInt("edgeAge"));
			label = edge.getString("edgeLabel");
			
			edgeBuf.putInt(EDGE_NODE1_OFFSET+id*edgeByteSize,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+id*edgeByteSize,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+id*edgeByteSize,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+id*edgeByteSize,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+id*edgeByteSize,age); // age
			
			// label
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			edgeBuf.putInt(EDGE_LABEL_START_OFFSET+id*edgeByteSize,labelOffset); // label start
			edgeBuf.putShort(EDGE_LABEL_LENGTH_OFFSET+id*edgeByteSize,labelLength); // label size
			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				edgeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(id);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(id);
			

		}
		//System.out.println("edge put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());
	
	
		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int i = 0; i< numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_IN_DEGREE_OFFSET,inEdgeLength);
			// now put the in edge/node pairs
			for(int edgeId : inEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_OUT_DEGREE_OFFSET,outEdgeLength);

			// now put the out edge/node pairs
			for(int edgeId : outEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
			

		}
		//System.out.println("connection put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());

	}



	/**
	 * saves the current graph to several files, in directory given to base name given  (i.e. fileBaseName should have no extension).
	 * If directory is null, then to a directory named data under current working directory. Directory should
	 * already exist.
	 */
	@SuppressWarnings("resource")
	public void saveBuffers(String directory, String fileBaseName) {
		File file;
		
		//new File("/path/directory").mkdirs();
		
		
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

			FileOutputStream fso = new FileOutputStream(File.separatorChar+directoryAndBaseName+".info");
			Writer writer = new BufferedWriter(new OutputStreamWriter(fso, "utf-8"));
			writer.write("name"+INFO_SPLIT_STRING+name+"\n");
			writer.write("numberOfNodes"+INFO_SPLIT_STRING+numberOfNodes+"\n");
			writer.write("numberOfEdges"+INFO_SPLIT_STRING+numberOfEdges+"\n");
			writer.write("averageNodeLabelLength"+INFO_SPLIT_STRING+averageNodeLabelLength+"\n");
			writer.write("averageEdgeLabelLength"+INFO_SPLIT_STRING+averageEdgeLabelLength+"\n");
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
	 * represented by one node id pair per line delimited by tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory If directory is null, then to a
	 * directory named data/snap under current working directory.
	 */
	public static FastGraph adjacencyListGraphFactory(int nodeCount, int edgeCount, String directory, String fileName, boolean direct) {
		FastGraph g = new FastGraph(nodeCount,edgeCount,DEFAULT_AVERAGE_LABEL_LENGTH,DEFAULT_AVERAGE_LABEL_LENGTH,direct);
		g.setName(fileName);
		g.loadAdjacencyListGraph(directory,fileName);
		return g;
	}


	
	/**
     * Assumes edges represented by one node id pair per line delimited by
     * tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory. If directory is null, then to a
	 * directory named /data/snap under current working directory.
	 */
	@SuppressWarnings("resource")
	private void loadAdjacencyListGraph(String dir, String fileName) {
	
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
		try {
			
			File f = new File(path);
			if(!f.exists()) {
				System.out.println("Problem loading file "+path+". If you expect to access a SNAP file try downloading the file from:\nhttps://snap.stanford.edu/data/\nthen unzipping it and placing it in the directory "+directory);
				System.exit(1);
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
System.out.println("edgeIndex "+edgeIndex);
System.out.println("nodeIndex "+nodeIndex);
			
		} catch(Exception e) {
			System.out.println("ERROR executing loadSnapGraph("+directory+","+fileName+")");
			e.printStackTrace();
		}
		

		short labelLength = -999;
		int inStart = -88;
		short inLength = -33;
		int outStart = -77;
		short outLength = -22;
		int weight = -55;
		byte type = -77;
		byte age = -99;
		int labelOffset = 0;
		for(int i = 0; i< numberOfNodes; i++) {
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*nodeByteSize,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+i*nodeByteSize,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*nodeByteSize,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+i*nodeByteSize,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+i*nodeByteSize,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+i*nodeByteSize,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+i*nodeByteSize,age); // age

			// label
			String label = nodeIndexToSnapIdMap.get(i);
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			nodeBuf.putInt(NODE_LABEL_START_OFFSET+i*nodeByteSize,labelOffset); // label start
			nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+i*nodeByteSize,labelLength); // label size

			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				nodeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}

		}
		//System.out.println("node put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+nodeBuf.isDirect());


		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeOut.add(i,edges);
		}
				
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		weight = -51;
		type = -53;
		age = -55;
		labelOffset = 0;
		for(int i = 0; i< numberOfEdges; i++) {
			node1 = edgeNode1Map.get(i);
			node2 = edgeNode2Map.get(i);
			edgeBuf.putInt(EDGE_NODE1_OFFSET+i*edgeByteSize,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+i*edgeByteSize,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+i*edgeByteSize,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+i*edgeByteSize,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+i*edgeByteSize,age); // age
			
			// label
			String label = "e"+i;
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			edgeBuf.putInt(EDGE_LABEL_START_OFFSET+i*edgeByteSize,labelOffset); // label start
			edgeBuf.putShort(EDGE_LABEL_LENGTH_OFFSET+i*edgeByteSize,labelLength); // label size
			
			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				edgeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(i);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(i);

		}


		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int i = 0; i< numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int edgeId : inEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int edgeId : outEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
		}
	}


	
	/**
	 * loads the current graph from several files created by saveBuffers,
	 * in directory given to base name given (i.e. fileBaseName should have no extension).
	 * If directory is null, then to a directory named data under current working directory.
	 */
	@SuppressWarnings("resource")
	private static FastGraph loadBuffers(String directory, String fileBaseName) {
		
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
		try {
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
			splitLine = line.split(INFO_SPLIT_STRING);
			int inAverageNodeLabelLength = Integer.parseInt(splitLine[1]);
			line = br.readLine();
			splitLine = line.split(INFO_SPLIT_STRING);
			int inAverageEdgeLabelLength = Integer.parseInt(splitLine[1]);
			line = br.readLine();
			splitLine = line.split(INFO_SPLIT_STRING);
			String directValue = splitLine[1];
			boolean inDirect = true;
			if(directValue.equals("false")) {
				inDirect = false;
			}
			br.close();
			
			g = new FastGraph(inNodeTotal, inEdgeTotal, inAverageNodeLabelLength, inAverageEdgeLabelLength, inDirect);
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
			
		} catch(Exception e) {
			System.out.println("ERROR executing loadBuffers("+directory+","+fileBaseName+")");
			e.printStackTrace();
		}
		
		return g;
	}


	
	public void populateRandomGraph() {

		long time;
		Random r = new Random();

		short labelLength = -999;
		int inStart = -888;
		short inLength = -3;
		int outStart = -777;
		short outLength = -2;
		int weight = -5;
		byte type = -7;
		byte age = -9;
		int labelOffset = 0;
		time = System.currentTimeMillis();
		for(int i = 0; i< numberOfNodes; i++) {
			weight = r.nextInt(100);
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*nodeByteSize,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putShort(NODE_IN_DEGREE_OFFSET+i*nodeByteSize,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*nodeByteSize,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putShort(NODE_OUT_DEGREE_OFFSET+i*nodeByteSize,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+i*nodeByteSize,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+i*nodeByteSize,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+i*nodeByteSize,age); // age
			
			// label
			String label = "n"+i;
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			nodeBuf.putInt(NODE_LABEL_START_OFFSET+i*nodeByteSize,labelOffset); // label start
			nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+i*nodeByteSize,labelLength); // label size

			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				nodeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}

		}
		//System.out.println("node put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+nodeBuf.isDirect());

time = System.currentTimeMillis();
for(int i = 0; i< numberOfNodes; i++) {
	int labelStart = nodeBuf.getInt(NODE_LABEL_START_OFFSET+i*nodeByteSize);
	int labelLengthOut = nodeBuf.getShort(NODE_LABEL_LENGTH_OFFSET+i*nodeByteSize);
	int inStartOut = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+i*nodeByteSize);
	int inLengthOut = nodeBuf.getShort(NODE_IN_DEGREE_OFFSET+i*nodeByteSize);
	int outStartOut = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+i*nodeByteSize);
	int outLengthOut = nodeBuf.getShort(NODE_OUT_DEGREE_OFFSET+i*nodeByteSize);
	int weightOut = nodeBuf.getInt(NODE_WEIGHT_OFFSET+i*nodeByteSize);
	int typeOut = nodeBuf.get(NODE_TYPE_OFFSET+i*nodeByteSize);
	int ageOut = nodeBuf.get(NODE_AGE_OFFSET+i*nodeByteSize);
	//System.out.println(i+" "+labelStart+" "+labelLengthOut+" "+inStartOut+" "+inLengthOut+" "+outStartOut+" "+outLengthOut+" "+weightOut+" "+typeOut+" "+ageOut);
}
//System.out.println("node get time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+nodeBuf.isDirect());

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of inward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeIn.add(i,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(numberOfNodes); // temporary store of outward edges
		for(int i = 0; i< numberOfNodes; i++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(20);
			nodeOut.add(i,edges);
		}
				
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		int node1;
		int node2;
		weight = -101;
		type = -103;
		age = -105;
		labelOffset = 0;
		time = System.currentTimeMillis();
		for(int i = 0; i< numberOfEdges; i++) {
			weight = r.nextInt(100);
			node1 = r.nextInt(numberOfNodes);
			node2 = r.nextInt(numberOfNodes);
			edgeBuf.putInt(EDGE_NODE1_OFFSET+i*edgeByteSize,node1); // one end of edge
			edgeBuf.putInt(EDGE_NODE2_OFFSET+i*edgeByteSize,node2); // other end of edge
			edgeBuf.putInt(EDGE_WEIGHT_OFFSET+i*edgeByteSize,weight); // weight
			edgeBuf.put(EDGE_TYPE_OFFSET+i*edgeByteSize,type); // type
			edgeBuf.put(EDGE_AGE_OFFSET+i*edgeByteSize,age); // age
			
			// label
			String label = "e"+i;
			char[] labelArray = label.toCharArray();
			labelLength = (short)(labelArray.length);

			edgeBuf.putInt(EDGE_LABEL_START_OFFSET+i*edgeByteSize,labelOffset); // label start
			edgeBuf.putShort(EDGE_LABEL_LENGTH_OFFSET+i*edgeByteSize,labelLength); // label size
			
			for(int j = 0; j < labelArray.length; j++) {
				char c = labelArray[j];
				edgeLabelBuf.putChar(labelOffset,c);
				labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
			}
			
			// store connecting nodes
			inEdgeList = nodeIn.get(node2);
			inEdgeList.add(i);
			outEdgeList = nodeOut.get(node1);
			outEdgeList.add(i);
			

		}
//		System.out.println("edge put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());
	

time = System.currentTimeMillis();
for(int i = 0; i< numberOfEdges; i++) {
	int node1Out = edgeBuf.getInt(EDGE_NODE1_OFFSET+i*edgeByteSize);
	int node2Out = edgeBuf.getInt(EDGE_NODE2_OFFSET+i*edgeByteSize);
	int labelStart = edgeBuf.getInt(EDGE_LABEL_START_OFFSET+i*edgeByteSize);
	int labelSize = edgeBuf.getShort(EDGE_LABEL_LENGTH_OFFSET+i*edgeByteSize);
	int weightOut = edgeBuf.getInt(EDGE_WEIGHT_OFFSET+i*edgeByteSize);
	int typeOut = edgeBuf.get(EDGE_TYPE_OFFSET+i*edgeByteSize);
	int ageOut = edgeBuf.get(EDGE_AGE_OFFSET+i*edgeByteSize);
	//System.out.println(i+" "+node1Out+" "+node2Out+" "+labelStart+" "+labelSize+" "+weightOut+" "+typeOut+" "+ageOut);
}
//System.out.println("edge get time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());

		// Initialise the connection buffer, modifying the node buffer connection data
		//time = System.currentTimeMillis();
		int offset = 0;
		for(int i = 0; i< numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			short inEdgeLength = (short)(inEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int edgeId : inEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			short outEdgeLength = (short)(outEdges.size());
			nodeBuf.putInt(i*nodeByteSize+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putShort(i*nodeByteSize+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int edgeId : outEdges) {
				int nodeId = -1;
				int n1 = edgeBuf.getInt(EDGE_NODE1_OFFSET+edgeId*edgeByteSize);
				int n2 = edgeBuf.getInt(EDGE_NODE2_OFFSET+edgeId*edgeByteSize);
				if(n1 == i) {
					nodeId = n2;
				} else if(n2 == i) {
					nodeId = n1;
				} else {
					System.out.println("ERROR When finding connections for node "+i+" connecting edge "+edgeId+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeId);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeId);
				offset += connectionPairSize;
			}
			
		
		}
		//System.out.println("connection put time " + (System.currentTimeMillis()-time)/1000.0+" seconds, direct "+edgeBuf.isDirect());


	}

	
	/**
	 * Finds the largest degree for a node in the graph.
	 */
	public int maximumDegree() {
		
		int max = 0;
		for(int i = 0; i< numberOfNodes; i++) {
				short inDegree = nodeBuf.getShort(NODE_IN_DEGREE_OFFSET+i*nodeByteSize);
				short outDegree = nodeBuf.getShort(NODE_OUT_DEGREE_OFFSET+i*nodeByteSize);
				int degree = inDegree+outDegree;
				if(degree > max) {
					max = degree;
				}
		}
		return max;
	}


	
	
	/** Breadth first search through the graph.
	 * Note direct access to connectionBuf is a 3x speed up over accessing getNodeConnectingNodes(currentNode).
	 * Using arrays for nodeFlagBuf is a minor speed up on ByteBuffer
	 */

	public boolean isConnected() {

		boolean[] nodeFlag = new boolean[numberOfNodes];
		
		if(numberOfNodes == 0) {
			return true;
		}
		
		boolean visited = true;
		int nodeCount = 0;
//int edgeCount = 0;		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		nodeFlag[0] = visited;
		while(queue.size() != 0) {
			int currentNode = queue.removeFirst();
			nodeCount++;
			
			int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+currentNode*nodeByteSize);
			int degree = getNodeDegree(currentNode);
			for(int i = 0; i < degree; i++) {
//edgeCount++;
				// step over edge/node pairs and the edge
				int nodeOffset = CONNECTION_NODE_OFFSET+connectionOffset+i*connectionPairSize;
				int connectingNode = connectionBuf.getInt(nodeOffset);
				boolean flag = nodeFlag[connectingNode];
				if(!flag) {
					queue.add(connectingNode);
					nodeFlag[connectingNode] = visited;
				}
			}
		}
		boolean allVisited = true;
		if(nodeCount < numberOfNodes) {
			allVisited = false;
		}
//System.out.println("edges tested "+edgeCount);		
//System.out.println("nodes tested "+nodeCount);		
		return allVisited;
	}

	/**
	 * Prints the contents of an adjacency matrix to the screen in a simple way
	 * Loops instead of using toDeepString() as it's better to display the matrix as a table
	 * @param matrix A 2D array of ints representing the graph
	 */
	public void printMatrix(int[][] matrix) {
		for (int[] i : matrix) {
			System.out.println(Arrays.toString(i));
		}
			
	}
	
	/**
	 * Prints the contents of an adjacency matrix to the screen in a simple way
	 * Loops instead of using toDeepString() as it's better to display the matrix as a table
	 * @param matrix A 2D array of booleans representing the graph
	 */
	public void printMatrix(boolean[][] matrix) {
		for (boolean[] i : matrix) {
			System.out.println(Arrays.toString(i));
		}
			
	}


	/**
	 * Builds an adjacency matrix from a graph.
	 * Assumes the graph is undirected
	 * 
	 * @return A 2D array of ints representing the graph
	 */
	public int[][] buildIntAdjacencyMatrix() {
		
		int[][] matrix = new int[getNumberOfNodes()][getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int nid = 0; nid < getNumberOfNodes(); nid++) {
			int[] connectingNodeIDs = getNodeConnectingOutNodes(nid);
			for (int i : connectingNodeIDs) {
				matrix[nid][i]++;
				matrix[i][nid]++;
			}			
		}		
	 
		return matrix;
	}
	
	/**
	 * Builds an adjacency matrix from a graph.
	 * Assumes a nodes only connects to another once
	 * Assumes the graph is undirected
	 * 
	 * @return A 2D array of booleans representing the graph
	 */
	public boolean[][] buildBooleanAdjacencyMatrix() {
		
		boolean[][] matrix = new boolean[getNumberOfNodes()][getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		System.out.println("Matrix created");
		for (int nid = 0; nid < getNumberOfNodes(); nid++) {
			int[] connectingNodeIDs = getNodeConnectingOutNodes(nid);
			for (int i : connectingNodeIDs) {
				matrix[nid][i] = true;
				matrix[i][nid] = true;
			}			
		}		
		
		return matrix;
	}
	
	
	/**
	 * Builds an adjacency matrix from a graph.
	 * Assumes the graph is directed
	 * 
	 * @return A 2D array of ints representing the graph
	 */
	public int[][] buildIntDirectedAdjacencyMatrix() {
		
		int[][] matrix = new int[getNumberOfNodes()][getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int nid = 0; nid < getNumberOfNodes(); nid++) {
			int[] connectingNodeIDs = getNodeConnectingOutNodes(nid);
			for (int i : connectingNodeIDs) {
				matrix[nid][i]++;
			}			
		}		
	 
		return matrix;
	}
	
	/**
	 * Builds an adjacency matrix from a graph.
	 * Assumes a nodes only connects to another once
	 * Assumes the graph is directed
	 * 
	 * @return A 2D array of booleans representing the graph
	 */
	public boolean[][] buildBooleanDirectedAdjacencyMatrix() {
		
		boolean[][] matrix = new boolean[getNumberOfNodes()][getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int nid = 0; nid < getNumberOfNodes(); nid++) {
			int[] connectingNodeIDs = getNodeConnectingOutNodes(nid);
			for (int i : connectingNodeIDs) {
				matrix[nid][i] = true;
			}			
		}		
		
		return matrix;
	}
	
	/**
	 * Converts an int[][] into a double[][]
	 * Used when creating eigenvalues
	 * @param inputMatrix The input 2D array
	 * @return The output 2D array
	 */
	private double[][] convertMatrix(int[][] inputMatrix) {
		//have to convert the int[][] input into a double[][]
		double[][] dArray = new double[inputMatrix.length][inputMatrix.length];
		for (int row = 0; row < inputMatrix.length; row++) {
		    for (int column = 0; column < inputMatrix[0].length; column++) {
		        dArray[row][column] = (double) inputMatrix[row][column];
		    }
		}
		return dArray;
	}
	
	/**
	 * Converts an boolean[][] into a double[][]
	 * Used when creating eigenvalues
	 * @param inputMatrix The input 2D array
	 * @return The output 2D array
	 */
	private double[][] convertMatrix(boolean[][] inputMatrix) {
		//have to convert the boolean[][] input into a double[][]
		double[][] dArray = new double[inputMatrix.length][inputMatrix.length];
		for (int row = 0; row < inputMatrix.length; row++) {
		    for (int column = 0; column < inputMatrix[0].length; column++) {
		    	if (inputMatrix[row][column]) {
		    		dArray[row][column] = 1;
		    	} else {
		    		dArray[row][column] = 0;
		    	}
		    }
		}
		return dArray;
	}
	
	/**
	 * Finds the eigenvalue of a matrix
	 * Taken from: http://introcs.cs.princeton.edu/java/95linear/Eigenvalues.java.html
	 * 
	 * @param inputMatrix int[][] is required. This is converted to a double[][]
	 * @return double[][] of Eigenvalues
	 */
	public double[] findEigenvalues(int[][] inputMatrix) {		
		return findEigenvalues(convertMatrix(inputMatrix));	
	}
	
	/**
	 * Finds the eigenvalue of a matrix
	 * Taken from: http://introcs.cs.princeton.edu/java/95linear/Eigenvalues.java.html
	 * 
	 * @param inputMatrix boolean[][] is required. This is converted to a double[][]
	 * @return double[][] of Eigenvalues
	 */
	public double[] findEigenvalues(boolean[][] inputMatrix) {		
		return findEigenvalues(convertMatrix(inputMatrix));	
	}
	
	/**
	 * Finds the eigenvalue of a matrix
	 * Taken from: http://introcs.cs.princeton.edu/java/95linear/Eigenvalues.java.html
	 * 
	 * @param inputMatrix A double[][]
	 * @return double[][] of Eigenvalues
	 */
	public double[] findEigenvalues(double[][] inputMatrix) {
		int matrixSize = inputMatrix.length;

		Matrix A = new Matrix(inputMatrix);
		
		// compute the spectral decomposition
		EigenvalueDecomposition e = A.eig();
		//e.getD().print(6, 1);
		//e.getV().print(6, 1);
		return e.getRealEigenvalues();	
	}
	
	/** this version uses arrays for the connected nodes */
/*	public boolean isConnected() {

		boolean[] nodeFlag = new boolean[numberOfNodes];
		
		if(numberOfNodes == 0) {
			return true;
		}
		
		int[] neighbours = new int[100000];
		
		boolean visited = true;
		int nodeCount = 0;
int edgeCount = 0;		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(0);
		nodeFlag[0] = visited;
		while(queue.size() != 0) {
			int currentNode = queue.removeFirst();
			nodeCount++;
			
//			int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+currentNode*nodeByteSize);
			getNodeConnectingNodes(currentNode,neighbours);
			int degree = getNodeDegree(currentNode);
			for(int i = 0; i < degree; i++) {
edgeCount++;
				// step over edge/node pairs and the edge
//				int nodeOffset = CONNECTION_NODE_OFFSET+connectionOffset+i*connectionPairSize;
//				int connectingNode = connectionBuf.getInt(nodeOffset);
				int connectingNode = neighbours[i];
				boolean flag = nodeFlag[connectingNode];
				if(!flag) {
					queue.add(connectingNode);
					nodeFlag[connectingNode] = visited;
				}
			}
		}
		boolean allVisited = true;
		if(nodeCount < numberOfNodes) {
			allVisited = false;
		}
System.out.println("edges tested "+edgeCount);		
System.out.println("nodes tested "+nodeCount);		
		return allVisited;
	}
*/
	

	
	
}
