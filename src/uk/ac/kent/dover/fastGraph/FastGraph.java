package uk.ac.kent.dover.fastGraph;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;

import org.json.*;

import uk.ac.kent.displayGraph.*;
import uk.ac.kent.displayGraph.drawers.GraphDrawerGravitySpringEmbedder;


/**
 * 
 * Graph class with redundant node connections. Stores nodes, edges, node connections, node labels, edge labels
 * in ByteBuffers. Note due to the complexity of storage, create only with the factory methods.
 * <p>
 * The design is scalable, has fast access, and allows quick file save and load of the ByteBuffers.
 * However, poor dynamic performance.
 * </p>
 * <p>
 * Storage:
 * node and edge indexes are integers and must start at 0 and end and size-1. Indexes are not stored,
 * they are assumed, so node info with nodeIndex n can be found starting in nodeBuf at offset n*nodeByteSize,
 * similarly edge with edgeIndex e starts in edgeBuf at e*edgeByteSize.
 * </p>
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

	public static final int NODE_LABEL_START_OFFSET = 0; // integer
	public static final int NODE_LABEL_LENGTH_OFFSET = 4; // short
	public static final int NODE_IN_CONNECTION_START_OFFSET = 6; // integer
	public static final int NODE_IN_DEGREE_OFFSET = 10; // integer
	public static final int NODE_OUT_CONNECTION_START_OFFSET = 14; // integer
	public static final int NODE_OUT_DEGREE_OFFSET = 18; // integer
	public static final int NODE_WEIGHT_OFFSET = 22; // integer
	public static final int NODE_TYPE_OFFSET = 26; // byte
	public static final int NODE_AGE_OFFSET = 27; // byte
	
	public static final int EDGE_NODE1_OFFSET = 0; // integer
	public static final int EDGE_NODE2_OFFSET = 4; // integer
	public static final int EDGE_LABEL_START_OFFSET = 8; // integer
	public static final int EDGE_LABEL_LENGTH_OFFSET = 12; // short
	public static final int EDGE_WEIGHT_OFFSET = 14; // integer
	public static final int EDGE_TYPE_OFFSET = 18; // byte
	public static final int EDGE_AGE_OFFSET = 19; // byte
	
	public static final int CONNECTION_EDGE_OFFSET = 0; // integer, edge is first of the pair
	public static final int CONNECTION_NODE_OFFSET = 4; // integer, node is straight after the edge
	
	public static final int DEFAULT_AVERAGE_LABEL_LENGTH = 20;
	
	public static final int NODE_BYTE_SIZE = 28;
	public static final int EDGE_BYTE_SIZE = 20;
	public static final int CONNECTION_PAIR_SIZE = 8; // this is an edge index plus an node index
	
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
	private boolean direct; // true if off heap storage for byte buffers, false if on heap
	
	private byte generation = 0; // the oldest generation time slice
	
	public static void main(String [] args) throws Exception {
		
		Debugger.enabled = true;
		
		FastGraph g,g1;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		NodeStructure ns;
		EdgeStructure es;
		
		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		ns = new NodeStructure(-1,"nlkjh1", 1, (byte)1, (byte)0);
addNodes.add(ns);
		g = g.generateGraphByAddingNode(ns);

		ns = new NodeStructure(-1,"yhtggtff1", 1, (byte)8, (byte)0);
addNodes.add(ns);
		g = g.generateGraphByAddingNode(ns);
		
		es = new EdgeStructure(-1,"edge label 0", 12, (byte)0, (byte)1, 0, 1);
addEdges.add(es);
		g = g.generateGraphByAddingEdge(es);
		
		ns = new NodeStructure(-1,"2", 1, (byte)8, (byte)0);
addNodes.add(ns);
		g = g.generateGraphByAddingNode(ns);
		
		es = new EdgeStructure(-1,"1", 9, (byte)0, (byte)1, 1, 2);
addEdges.add(es);
		g = g.generateGraphByAddingEdge(es);
//		g.outputInternalData();
//System.out.println(g.checkConsistency());
g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
g1.outputInternalData();
	}	

	
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
	 * Allocates space for the node, edge and connection ByteBuffers. The label ByteBuffers
	 * are created later
	 */
	private void init() {

		if(!direct) {
			nodeBuf = ByteBuffer.allocate(numberOfNodes*NODE_BYTE_SIZE);
			edgeBuf = ByteBuffer.allocate(numberOfEdges*EDGE_BYTE_SIZE);
			connectionBuf = ByteBuffer.allocate(numberOfEdges*2*CONNECTION_PAIR_SIZE);
			// nodeLabelBuf and edgeLabelBuf now created in Factories by setAllNodeLabels and setAllEdgeLabels
		} else {
			nodeBuf = ByteBuffer.allocateDirect(numberOfNodes*NODE_BYTE_SIZE);
			edgeBuf = ByteBuffer.allocateDirect(numberOfEdges*EDGE_BYTE_SIZE);
			connectionBuf = ByteBuffer.allocateDirect(numberOfEdges*2*CONNECTION_PAIR_SIZE);
			// nodeLabelBuf and edgeLabelBuf now created in Factories by setAllNodeLabels and setAllEdgeLabels
		}
		
		nodeBuf.clear();
		edgeBuf.clear();
		connectionBuf.clear();
		
	}

	
	/**
	 * Relabels the current FastGraph with the family groups in subgraphs/families folder.<br>
	 * Any remaining nodes and edges are labelled randomly.
	 * Each family is tested against each of the induced subgraphs and will be relabeled if the two are isomorphic
	 * 
	 * @param subgraphsToTest How many subgraphs will be induced for each family.
	 * 
	 * @throws Exception If there is a problem loading the family subgraphs, or if there is a problem inducing a subgraph
	 */
	public void relabelFastGraph(int subgraphsToTest) throws Exception{
		Debugger.log("Relabelling FastGraph");
		long time = 0;
		time = Debugger.createTime();
		
		//load node and edge labels arrays
		String[] nodeLabels = new String[this.getNumberOfNodes()];
		String[] edgeLabels = new String[this.getNumberOfEdges()];
		
		//load node and edge Types arrays
		byte[] nodeTypes = new byte[this.getNumberOfNodes()];
		byte[] edgeTypes = new byte[this.getNumberOfEdges()];
		
		//create induction class
		InducedSubgraph is = new InducedSubgraph(this);
		
		//create Name Picker class
		NamePicker np = new NamePicker();
		
		//number of families found
		int fams = 0;
		
		//load family subgraphs
		FastGraph[] families = loadFamilies();
		for(FastGraph family : families) {
			Debugger.log("Testing family " + family.getName());
			
			ExactIsomorphism ei = new ExactIsomorphism(family);
			
			int familyNodesSize = family.getNumberOfNodes();			
			
			for (int i = 0; i < subgraphsToTest; i++) { //induce subgraphs to test
				
				if(i%10000 == 0) {
					Debugger.log("Testing subgraph " + i);
				}
				
				LinkedList<Integer> subNodes = new LinkedList<Integer>();
				LinkedList<Integer> subEdges = new LinkedList<Integer>();
				
				//create a subgraph and build to a FastGraph
				is.createInducedSubgraph(subNodes, subEdges, familyNodesSize);
				FastGraph subgraph = this.generateGraphFromSubgraph(Util.convertLinkedList(subNodes), Util.convertLinkedList(subEdges));
				
				//is this FastGraph isomorphic to the one in the constructor (i.e. the family)
				boolean isomorphic = ei.isomorphic(subgraph);
				
				if(isomorphic) {
					fams++;
					//rename original graph
					
					//pick a surname, so all family members have the same surname
					String surname = np.getSurname();
				//	Debugger.log("Family name: " + surname);
					for(int n : subNodes) {
						nodeLabels[n] = np.getForename() + " " + surname;
						nodeTypes[n] = FastGraphNodeType.CHILD.getValue();
					}
					
					//set the parents
					nodeTypes[subNodes.get(0)] = FastGraphNodeType.PARENT.getValue();
					nodeTypes[subNodes.get(1)] = FastGraphNodeType.PARENT.getValue();
										
					//label the edges with types rather than names
					for(int e : subEdges) {
						
						//if this is the parent's relationship
						if ((getEdgeNode1(e) == subNodes.get(0) && getEdgeNode2(e) == subNodes.get(1)) ||
						(getEdgeNode1(e) == subNodes.get(1) && getEdgeNode2(e) == subNodes.get(0))) {
							edgeTypes[e] = FastGraphEdgeType.MARRIED.getValue();
							edgeLabels[e] = FastGraphEdgeType.MARRIED.toString();
							//if this is the parent child relationship
						} else if (getEdgeNode1(e) == subNodes.get(0) || getEdgeNode1(e) == subNodes.get(1) ||
								getEdgeNode2(e) == subNodes.get(0) || getEdgeNode2(e) == subNodes.get(1)) {
							edgeTypes[e] = FastGraphEdgeType.PARENT.getValue();
							edgeLabels[e] = FastGraphEdgeType.PARENT.toString();
							//otherwise these are siblings
						} else {
							edgeTypes[e] = FastGraphEdgeType.SIBLING.getValue();
							edgeLabels[e] = FastGraphEdgeType.SIBLING.toString();
						}		
					}//end for each subEdge	
				} //end if isomorphic
			}//end for each subgraph to test	
		}//end foreach family
		
		Debugger.log("## Number of families found: " + fams);
		
		//replace the blanks with other names
		for(int j = 0; j < nodeLabels.length; j++) {						
			if (nodeLabels[j] == null) {
				nodeLabels[j] = np.getName();
			}
		}
		
		//Leave node types as they are - these are not used
		
		Random r = new Random(nodeBuf.getLong(0));
		//replace the blanks with other edge types
		FastGraphEdgeType[] values = FastGraphEdgeType.values();
		for(int j = 0; j < edgeTypes.length; j++) {						
			if (edgeTypes[j] == FastGraphEdgeType.UNKNOWN.getValue()) {
				//pick a random relationship
				//byte relationship = (byte) (r.nextInt(values.length - 5)+4); //ignore the family relationships
				FastGraphEdgeType type = FastGraphEdgeType.pickRandomExceptFamilyAndTime(r);
				byte relationship = type.getValue();
				edgeTypes[j] = relationship;
				edgeLabels[j] = type.toString();
			}
		}		
		
		//Set all node & edge labels, and node & edge types
		this.setAllNodeLabels(nodeLabels);
		this.setAllEdgeLabels(edgeLabels);
		for(int i = 0; i < nodeTypes.length; i++) {
			this.setNodeType(i, nodeTypes[i]);			
		}
		for(int i = 0; i < edgeTypes.length; i++) {
			this.setEdgeType(i, edgeTypes[i]);			
		}

		
	}
	
	/**
	 * Loads the families subgraphs
	 * @return A list of FastGraphs based on the family subgraphs
	 * @throws Exception If the node or edge count can't be converted, or the file can't be loaded
	 */
	public FastGraph[] loadFamilies() throws Exception {
		File folder = new File(Launcher.startingWorkingDirectory+File.separatorChar+"subgraphs"+File.separatorChar+"families");
		File[] listOfFiles = folder.listFiles();
		LinkedList<FastGraph> graphs = new LinkedList<>();
		
		for (File f : listOfFiles) {
			String[] splits = f.getName().split("-");
			int nodeCount = Integer.parseInt(splits[2]);
			int edgeCount = Integer.parseInt(splits[4]);
			Debugger.log(f + " n" + nodeCount + "e" + edgeCount);
			FastGraph g = nodeListEdgeListGraphFactory(nodeCount, edgeCount, folder.getPath() + File.separatorChar + f.getName(), f.getName(), direct);
			Debugger.log("new g nodes" + g.getNumberOfNodes());
			graphs.add(g);
		}
		
		//Debugger.log(Arrays.toString(listOfFiles));
		
		FastGraph[] graphArray = new FastGraph[graphs.size()];
		Util.convertLinkedListObject(graphs, graphArray);
		return graphArray;
	}
	
	/**
	 * This method creates a new FastGraph of the rough size given in targetNodes and targetEdges. <br>
	 * The new graph will never have fewer nodes than the target, but may have fewer edges. <br>
	 * <b>Note: This may take some time to complete</b>
	 * 
	 * @param nodes The list of nodes to be removed
	 * @param edges The list of edges to be removed
	 * @param targetNodes The target number of nodes
	 * @param targetEdges The target number of edges
	 * @return A new FastGraph that is roughly the size of the target
	 * @throws FastGraphException If there is an exception here, e.g. targetNodes is too big
	 */
	public FastGraph removeNodesAndEdgesFromGraph(LinkedList<Integer> nodes, LinkedList<Integer> edges, int targetNodes, int targetEdges) throws FastGraphException {
		
		Debugger.log("Suggesting nodes and egdes to remove");
		long time = Debugger.createTime();
		
		int currentTotalNodes = getNumberOfNodes();
		int currentTotalEdges = getNumberOfEdges();
		
		//if a graph of the same size has been specified
		if (targetNodes == currentTotalNodes && targetEdges == currentTotalEdges) {
			return this;
		}
		
		//if the node target is too big
		if(targetNodes > currentTotalNodes) {
			throw new FastGraphException("The target node size is too big");
		}
		//if the edge target is too big
		if(targetEdges > currentTotalEdges) {
			throw new FastGraphException("The target edge size is too big");
		}
		
		
		int nodeReductionAmount = currentTotalNodes - targetNodes; //how many nodes we need to remove
		int edgeReductionAmount = currentTotalEdges - targetEdges; //how many edges we need to remove
		LinkedHashSet<Integer> edgesToRemove = new LinkedHashSet<Integer>(); //edges that need removing
		LinkedHashSet<Integer> nodesToRemove = new LinkedHashSet<Integer>(); //nodes that need removing
		
		Debugger.log("Current Nodes: " + currentTotalNodes + " Target Nodes: " + targetNodes);
		Debugger.log("Current Edges: " + currentTotalEdges + " Target Edges: " + targetEdges);
		
		Debugger.outputTime("setup test time ");
		Debugger.log();
		
		time = Debugger.createTime();
		Debugger.log("# Starting STEP ONE");
		//STEP ONE:
		//Find a subgraph with the required number of nodes. Remove it
		InducedSubgraph is = new InducedSubgraph(this);
		LinkedList<Integer> subNodes = new LinkedList<Integer>();
		LinkedList<Integer> subEdges = new LinkedList<Integer>();
		is.createInducedSubgraph(subNodes, subEdges, nodeReductionAmount);
		
		nodesToRemove.addAll(subNodes);
		edgesToRemove.addAll(subEdges);
		
		//delete all edges connecting to the nodes to be deleted
		for(int n : nodesToRemove) {
			Util.addAll(edgesToRemove, this.getNodeConnectingInEdges(n));
			Util.addAll(edgesToRemove, this.getNodeConnectingOutEdges(n));
		}
		
		Debugger.outputTime("After induction test time ");
		Debugger.log("nodes to remove size: " + nodesToRemove.size() + " edges to remove size: " + edgesToRemove.size());
		Debugger.log();
		
		
		//STEP TWO:
		//if we haven't removed enough nodes
		Debugger.log("# Starting STEP TWO");
		time = Debugger.createTime();
		if(nodeReductionAmount > nodesToRemove.size()) { //could we thread these to make this quicker?
			Random r = new Random(nodeBuf.getLong(1));
			
			//make local stores, as we might not want to remove these nodes if they are too big
			LinkedHashSet<Integer> localEdgesToRemove = new LinkedHashSet<Integer>(); //edges that need removing
			LinkedHashSet<Integer> localNodesToRemove = new LinkedHashSet<Integer>(); //nodes that need removing
			
			int chances = 10; //if a tree is too big, then skip it. But only do this 10 times, in case we are stuck
			while(nodeReductionAmount > nodesToRemove.size() && chances > 0) {
				//long time2 = System.currentTimeMillis();
				localNodesToRemove.clear();
				localEdgesToRemove.clear();
				
				int stillToRemove = nodeReductionAmount - nodesToRemove.size(); //what nodes are left to remove
				this.buildTree(localNodesToRemove, localEdgesToRemove, r, 3);
				
				//Debugger.log("Tree: " + localNodesToRemove);				
				//Debugger.log("nodeRA: " + nodeReductionAmount + " stillTR: " + stillToRemove + " localNodesToRemove: " + localNodesToRemove.size());
				
				if (localNodesToRemove.size() <= stillToRemove) {
					nodesToRemove.addAll(localNodesToRemove);
					edgesToRemove.addAll(localEdgesToRemove);
					
					//delete all edges connecting to the nodes in this tree
					for(int n : localNodesToRemove) {
						Util.addAll(edgesToRemove, this.getNodeConnectingInEdges(n));
						Util.addAll(edgesToRemove, this.getNodeConnectingOutEdges(n));
					}
					
				} else {
					chances--; //Avoids getting stuck if there are no further options
					continue;
				}
				
				//Debugger.log("After this tree test time " + (System.currentTimeMillis()-time2)/1000.0+" seconds");
			}
			
		}
		Debugger.outputTime("After tree test time ");
		Debugger.log("nodes to remove size: " + nodesToRemove.size() + " edges to remove size: " + edgesToRemove.size());
		Debugger.log();
		
		
		//STEP THREE:
		//if we haven't removed enough nodes
		//pick some at random
		Debugger.log("# Starting STEP THREE");
		time = Debugger.createTime();
		if(nodeReductionAmount > nodesToRemove.size()) {
			Random r = new Random(nodeBuf.getLong(2));		
			while(nodeReductionAmount > nodesToRemove.size()) {
				int n = r.nextInt(this.getNumberOfNodes());
				nodesToRemove.add(n);
				edgesToRemove.addAll(Util.convertArray(this.getNodeConnectingEdges(n)));
			}
		}
		Debugger.outputTime("After node removal test time ");
		Debugger.log("nodes to remove size: " + nodesToRemove.size() + " edges to remove size: " + edgesToRemove.size());
		Debugger.log();
		
		
		//STEP FOUR:
		//if we haven't removed enough edges
		//pick some at random
		Debugger.log("# Starting STEP FOUR");
		time = Debugger.createTime();
		if(edgeReductionAmount > edgesToRemove.size()) {
			Random r = new Random(edgeBuf.getLong(2));
			while(edgeReductionAmount > edgesToRemove.size()) {
				int e = r.nextInt(this.getNumberOfEdges());
				edgesToRemove.add(e);
			}
			
		}
		Debugger.outputTime("After edge removal test time ");
		Debugger.log("nodes to remove size: " + nodesToRemove.size() + " edges to remove size: " + edgesToRemove.size());
		Debugger.log();		
		
		nodes.addAll(nodesToRemove);
		edges.addAll(edgesToRemove);

		time = Debugger.createTime();
		Debugger.log("Building new FastGraph");
		FastGraph g = this.generateGraphByDeletingItems(Util.convertLinkedList(nodes), Util.convertLinkedList(edges), false);
		Debugger.outputTime("After FastGraph building test time ");
		return g;
	}
	
	/**
	 * Builds a tree like structure from a random node, to a particular depth
	 * 
	 * @param nodes A LinkedHashSet of nodes, ready to be populated with nodes to be removed
	 * @param edges A LinkedHashSet of edges, ready to be populated with nodes to be removed
	 * @param r A random number generator used to pick a starting place.
	 * @param depth The depth of the tree. 1 would equal the starting node and it's children. 2 would be the same as 1, but with grandchildren.
	 */
	public void buildTree(LinkedHashSet<Integer> nodes, LinkedHashSet<Integer> edges, Random r, int depth) {
		int startingNode = r.nextInt(this.getNumberOfNodes());
		
		nodes.add(startingNode);
		edges.addAll(Util.convertArray(this.getNodeConnectingEdges(startingNode)));
		
		LinkedList<Integer> startingNodes = new LinkedList<>();
		startingNodes.add(startingNode);

		//while we are not at the required depth
		while(depth != 0) {
			//Debugger.log("Starting Node: " + startingNode);
			int[] cn = new int[0]; //get ready to store connecting nodes
			for (int sn : startingNodes) { //for each of the starting nodes
				cn = this.getNodeConnectingNodes(sn); //get this node's connecting nodes
				//Debugger.log("    Connecting Nodes: " + Arrays.toString(cn));
				for(int n : cn) {
					nodes.add(n); //add them all the the tree
					edges.addAll(Util.convertArray(this.getNodeConnectingEdges(n))); //add the edges too
				}
			}
			startingNodes = Util.convertArray(cn); // make the connections the starting nodes for the next loop
			depth--; //"We need to go deeper"
		}		
	}
	

	/**
	 * 
	 * @return the oldest time slice generation, usually equates to the oldest node age in the graph.
	 */
	public byte getGeneration() {
		byte maxAge = findMaximumNodeAge();
		if(generation < maxAge) { // this is required because factory methods do not set generation
			generation = maxAge;
		}
		return generation;
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
	public int getNodeInDegree(int nodeIndex) {
		int degree = nodeBuf.getInt(NODE_IN_DEGREE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
		return degree;
	}

	
	/**
	 * @param nodeIndex the node
	 * @return the node out-degree (number of edges leaving the node)
	 */
	public int getNodeOutDegree(int nodeIndex) {
		int degree = nodeBuf.getInt(NODE_OUT_DEGREE_OFFSET+nodeIndex*NODE_BYTE_SIZE);
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
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
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
	 */
	public void getNodeConnectingEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
		}
	}
	

	/**
	 * All connecting nodes of same age.
	 * 
	 * @param nodeIndex the node
	 * @return all node neighbours. 
	 */
	public int[] getNodeConnectingNodesOfSameAge(int nodeIndex) {
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeDegree(nodeIndex);
		ArrayList<Integer> ret = new ArrayList<Integer>(degree);
		int age = this.getNodeAge(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the edge
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			if(age == this.getNodeAge(node)) {
				ret.add(node);
			}
		}
		
		return Util.convertArrayList(ret);
	}
	
	
	/**
	 * All connecting nodes of same age that are connected by edges pointing at the given parameter node.
	 * 
	 * @param nodeIndex the node
	 * @return all node neighbours. 
	 */
	public int[] getNodeConnectingInNodesOfSameAge(int nodeIndex) {
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		ArrayList<Integer> ret = new ArrayList<Integer>(degree);
		int age = this.getNodeAge(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the edge
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			if(age == this.getNodeAge(node)) {
				ret.add(node);
			}
		}
		
		return Util.convertArrayList(ret);
	}
	
	/**
	 * All connecting nodes of same age that are connected by edges pointing away from the given parameter node.
	 * 
	 * @param nodeIndex the node
	 * @return all node neighbours. 
	 */
	public int[] getNodeConnectingOutNodesOfSameAge(int nodeIndex) {
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		ArrayList<Integer> ret = new ArrayList<Integer>(degree);
		int age = this.getNodeAge(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			// don't need the edge, so step over edge/node pairs and the edge
			int nodeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_NODE_OFFSET;
			int node = connectionBuf.getInt(nodeOffset);
			if(age == this.getNodeAge(node)) {
				ret.add(node);
			}
		}
		
		return Util.convertArrayList(ret);
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
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
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
	 */
	public void getNodeConnectingInEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeInDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
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
	 * @param ret this is populated with all node neighbours that are on the end of edges that enter the node via the parameter array.
	 * @param nodeIndex the node
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
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
		}
		
		return ret;
	}
	
	/**
	 * For directed graphs.
	 * This version puts the connecting nodes in the argument array, to avoid repeated object creation and so speed up multiple accesses.
	 * create array with size of either getNodeOutDegree(nodeIndex) or maxDegree(). array elements beyond nodeDegree(nodeIndex)-1 are undefined.
	 * Will throw an exception if ret is not large enough.
	 * 
	 * @param ret this is populated with all edges that leave the node via the argument array.
	 * @param nodeIndex the node
	 */
	public void getNodeConnectingOutEdges(int[] ret, int nodeIndex) {
		
		int connectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+nodeIndex*NODE_BYTE_SIZE); // in offset is the first one
		int degree = getNodeOutDegree(nodeIndex);
		
		for(int i = 0; i < degree; i++) {
			int edgeOffset = connectionOffset+(i*CONNECTION_PAIR_SIZE)+CONNECTION_EDGE_OFFSET;
			int edge = connectionBuf.getInt(edgeOffset);
			ret[i] = edge;
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
	 * @param ret this is populated with all node neighbours that are on the end of edges that leave the passed node via the parameter array. 
	 * @param nodeIndex the node
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
	 * @param age The new age of the node
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
	 * @param age the new edge age
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
	 * Change all the edge labels in the graph. Creates a new edgeLabelBuf, changes the label pointers in edgeBuf.<br>
	 * Will throw an OutofMemoryError is label length is too long
	 * 
	 * @param labels Must contain the same number of labels as there are edges in the graph
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
	 * Find the edges between two nodes
	 * 
	 * @param n1 a node in the graph
	 * @param n2 a node in the graph
	 * @return the edges connecting the two nodes
	 */
	public ArrayList<Integer> edgesBetween(int n1, int n2) {
		
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		int[] n1Edges = getNodeConnectingEdges(n1); 
		for(int i = 0; i < n1Edges.length; i++) {
			int edge = n1Edges[i];
			if(oppositeEnd(edge, n1) == n2) {
				ret.add(edge);
			}
		}

		return ret;
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
	 * @throws Exception If the file cannot be saved, or other FastGraph Exception
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, boolean direct) throws Exception {
		FastGraph graph = randomGraphFactory(numberOfNodes, numberOfEdges, -1, false, direct);
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
	 * @throws FastGraphException If the desired number of edges is more than a complete graph for when simple is true
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, long seed, boolean direct) throws FastGraphException {
		FastGraph graph = randomGraphFactory(numberOfNodes, numberOfEdges, seed, false, direct);
		return graph;
	}
	
	
	/**
	 * Generate a random graph of the desired size. Self sourcing edges and parallel edges may exist, if simple is false
	 * 
	 * @param numberOfNodes the number of nodes in the graph
	 * @param numberOfEdges the number of edges in the graph
	 * @param seed random number seed, -1 for current time
	 * @param simple If the new graph is a simple graph.
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the created FastGraph
	 * @throws FastGraphException If the desired number of edges is more than a complete graph for when simple is true
	 */
	public static FastGraph randomGraphFactory(int numberOfNodes, int numberOfEdges, long seed, boolean simple, boolean direct) throws FastGraphException {
		FastGraph g = new FastGraph(numberOfNodes,numberOfEdges,direct);
		g.setName("random-n-"+numberOfNodes+"-e-"+numberOfEdges);
		g.populateRandomGraph(simple, seed);
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
	 * @see loadBuffers
	 */
	public static FastGraph loadBuffersGraphFactory(String directory, String fileBaseName) throws IOException {
		FastGraph g = loadBuffers(directory,fileBaseName);
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
		int inLength = -3;
		int outStart = -777;
		int outLength = -2;
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
			nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+index*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+index*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+index*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
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
		//time = Debugger.createTime();
		
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
			int inEdgeLength = inEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);

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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
		}

	}



	/**
	 * saves the current graph to several files, in directory given to base name given (i.e. fileBaseName should have no extension).
	 * If directory is null, then to a directory named data under current working directory.
	 * 
	 * @param directory where the files are to be stored, or if null fileBaseName under data under the current working directory
	 * @param fileBaseName the name of the files, to which extensions are added
	 * @throws IOException If there is a problem with saving the buffers
	 */
	public void saveBuffers(String directory, String fileBaseName) throws IOException {
		
		if(fileBaseName == null) {
			throw new IOException("null fileBaseName");
		}
		if(fileBaseName.length() == 0) {
			throw new IOException("empty fileBaseName");
		}
		if(name == null) { // this should not normally be needed, it prevents an empty name being saved in the .info file 
			name = fileBaseName;
		}
		if(name.length() == 0) { // this should not normally be needed, it prevents an empty name being saved in the .info file 
			name = fileBaseName;
		}
		String directoryAndBaseName = "";
		if(directory != null) {
			if(directory.charAt(directory.length()-1)== File.separatorChar) {
				directoryAndBaseName = directory+fileBaseName;
			} else {
				directoryAndBaseName = directory+File.separatorChar+fileBaseName;
			}
			new File(Launcher.startingWorkingDirectory+File.separatorChar+directory).mkdirs();
		} else {
			directoryAndBaseName = Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+fileBaseName+File.separatorChar+fileBaseName;
			new File(Launcher.startingWorkingDirectory+File.separatorChar+"data"+File.separatorChar+fileBaseName).mkdirs();
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(directoryAndBaseName+".info");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			writer.write("name"+INFO_SPLIT_STRING+name+"\n");
			writer.write("numberOfNodes"+INFO_SPLIT_STRING+numberOfNodes+"\n");
			writer.write("numberOfEdges"+INFO_SPLIT_STRING+numberOfEdges+"\n");
			writer.write("numberOfNodeLabelBytes"+INFO_SPLIT_STRING+nodeLabelBuf.capacity()+"\n");
			writer.write("numberOfEdgeLabelBytes"+INFO_SPLIT_STRING+edgeLabelBuf.capacity()+"\n");
			String directValue = "false";
			if(direct) {
				directValue = "true";
			}
			writer.write("direct"+INFO_SPLIT_STRING+directValue+"\n");

			writer.close();
			fos.close();
		} catch(Exception e) {
			Debugger.log("ERROR executing info file save in saveBuffers("+directory+","+fileBaseName+")");
			e.printStackTrace();
		}
			
		try {
			writeBuf(directoryAndBaseName+".nodeBuf",nodeBuf);
			writeBuf(directoryAndBaseName+".edgeBuf",edgeBuf);
			writeBuf(directoryAndBaseName+".connectionBuf",connectionBuf);
			writeBuf(directoryAndBaseName+".nodeLabelBuf",nodeLabelBuf);
			writeBuf(directoryAndBaseName+".edgeLabelBuf",edgeLabelBuf);
		} catch(Exception e) {
			Debugger.log("ERROR executing buffer save in saveBuffers("+directory+","+fileBaseName+")");
			e.printStackTrace();
		}
			
	}
	

	/**
	 * Debugging info
	 */
	public void outputInternalData() {
		
		HashMap<String,Integer> outConnectionMap = new HashMap<String,Integer>();
		HashMap<String,Integer> inConnectionMap = new HashMap<String,Integer>();
		ArrayList<Integer> nodeLabelIndex = new ArrayList<Integer>();
		ArrayList<Integer> nodeLabelNode = new ArrayList<Integer>();
		ArrayList<Integer> edgeLabelIndex = new ArrayList<Integer>();
		ArrayList<Integer> edgeLabelEdge = new ArrayList<Integer>();
		
		System.out.println("Graph name: "+getName());
		for(int i = 0; i< numberOfNodes; i++) {
			System.out.println("node "+i+" "+getNodeLabel(i));
			System.out.println("  LABEL_START: "+nodeBuf.getInt(NODE_LABEL_START_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  LABEL_LENGTH: "+nodeBuf.getShort(NODE_LABEL_LENGTH_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  IN_CONNECTION_START: "+nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  IN_DEGREE: "+nodeBuf.getInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  OUT_CONNECTION_START: "+nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  OUT_DEGREE: "+nodeBuf.getInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  WEIGHT: "+nodeBuf.getInt(NODE_WEIGHT_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  TYPE: "+nodeBuf.get(NODE_TYPE_OFFSET+i*NODE_BYTE_SIZE));
			System.out.println("  AGE: "+nodeBuf.get(NODE_AGE_OFFSET+i*NODE_BYTE_SIZE));

			outConnectionMap.put("node"+i+"out",nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE));
			inConnectionMap.put("node"+i+"in",nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE));
			nodeLabelIndex.add(nodeBuf.getInt(NODE_LABEL_START_OFFSET+i*NODE_BYTE_SIZE));
			nodeLabelNode.add(i);
		}
		
		for(int i = 0; i< numberOfEdges; i++) {
			System.out.println("edge "+i+" "+getEdgeLabel(i));
			System.out.println("  NODE1: "+edgeBuf.getInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  NODE2: "+edgeBuf.getInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  LABEL_START: "+edgeBuf.getInt(EDGE_LABEL_START_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  LABEL_LENGTH: "+edgeBuf.getShort(EDGE_LABEL_LENGTH_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  WEIGHT: "+edgeBuf.getInt(EDGE_WEIGHT_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  TYPE: "+edgeBuf.get(EDGE_TYPE_OFFSET+i*EDGE_BYTE_SIZE));
			System.out.println("  AGE: "+edgeBuf.get(EDGE_AGE_OFFSET+i*EDGE_BYTE_SIZE));
			
			edgeLabelIndex.add(edgeBuf.getInt(EDGE_LABEL_START_OFFSET+i*EDGE_BYTE_SIZE));
			edgeLabelEdge.add(i);
		}
		
		nodeLabelBuf.rewind();
		System.out.print("nodeLabelBuf: ");
		for(int i = 0; i< nodeLabelBuf.capacity(); i+= 2) {
			int index = nodeLabelIndex.indexOf(i);
			while(index != -1) {
				System.out.print("node"+nodeLabelNode.get(index)+" ");
				nodeLabelIndex.set(index,-1);
				index = nodeLabelIndex.indexOf(i);
			}
			System.out.print(nodeLabelBuf.getChar()+" ");
		}
		int index = nodeLabelIndex.indexOf(nodeLabelBuf.capacity());
		while(index != -1) {
			System.out.print("node"+nodeLabelNode.get(index)+" ");
			nodeLabelIndex.set(index,-1);
			index = nodeLabelIndex.indexOf(nodeLabelBuf.capacity());
		}
		
		System.out.println();
		
		edgeLabelBuf.rewind();
		System.out.print("edgeLabelBuf: ");
		for(int i = 0; i< edgeLabelBuf.capacity(); i+= 2) {
			index = edgeLabelIndex.indexOf(i);
			while(index != -1) {
				System.out.print("edge"+edgeLabelEdge.get(index)+" ");
				edgeLabelIndex.set(index,-1);
				index = edgeLabelIndex.indexOf(i);
			}
			System.out.print(edgeLabelBuf.getChar()+" ");
		}
		index = edgeLabelIndex.indexOf(edgeLabelBuf.capacity());
		while(index != -1) {
			System.out.print("edge"+edgeLabelEdge.get(index)+" ");
			edgeLabelIndex.set(index,-1);
			index = edgeLabelIndex.indexOf(edgeLabelBuf.capacity());
		}
		
		System.out.println();
		
		connectionBuf.rewind();
		System.out.print("connectionBuf: ");
		for(int i = 0; i< connectionBuf.capacity(); i+= 4) {
			for(String s : inConnectionMap.keySet()) {
				if(inConnectionMap.get(s) == i) {
					System.out.print(s+" ");
				}
			}
			for(String s : outConnectionMap.keySet()) {
				if(outConnectionMap.get(s) == i) {
					System.out.print(s+" ");
				}
			}
			System.out.print(connectionBuf.getInt()+" ");
		}
		for(String s : inConnectionMap.keySet()) {
			if(inConnectionMap.get(s) == connectionBuf.capacity()) {
				System.out.print(s+" ");
			}
		}
		for(String s : outConnectionMap.keySet()) {
			if(outConnectionMap.get(s) == connectionBuf.capacity()) {
				System.out.print(s+" ");
			}
		}
		System.out.println();
		
		System.out.println("consistent: "+checkConsistency());

	}
	

	
	/**
	 * Save a ByteBuffer to a file.
	 * 
	 * @param file name to write to
	 * @param buf buffer to be written
	 * @throws Exception if file save fails
	 */
	private void writeBuf(String fileName, ByteBuffer buf) throws IOException {
		
		buf.rewind();
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file, false);
		FileChannel wChannel = fos.getChannel();
		wChannel.write(buf);
		fos.flush();
		wChannel.close();
		fos.close();

	}

	
	/**
	 * Creates a graph from a SNAP .txt adjacency list file. Number of nodes and edges are given
	 * by the <a href="https://snap.stanford.edu/data/">SNAP website</a>. Assumes edges
	 * represented by one node index pair per line delimited by tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory If directory is null, then to a
	 * directory named data/snap under current working directory.
	 * 
	 * @param nodeCount the number of nodes
	 * @param edgeCount the number of edges
	 * @param dir the directory for the file, if null then a directory called data/ under the current working directory
	 * @param fileName the file name for the file
	 * @param direct if true the ByteBuffers are direct, if false they are allocated on the heap
	 * @return The new FastGraph
	 * @throws Exception Throws if the adjacency list cannot be built correctly. Might be an IO error
	 */
	public static FastGraph adjacencyListGraphFactory(int nodeCount, int edgeCount, String dir, String fileName, boolean direct) throws Exception {
		FastGraph g = new FastGraph(nodeCount,edgeCount,direct);
		g.setName(fileName);
		g.loadAdjacencyListGraph(dir,fileName);
		return g;
	}


	
	/**
     * Assumes edges represented by one node index pair per line delimited by
     * tabs or spaces, ignores lines starting with # and any line without a tab.
	 * Looks for the file in given directory. If directory is null, then to a
	 * directory named /data/snap under current working directory.
	 * 
	 * @param dir the directory for the file, if null then a directory called data/ under the current working directory
	 * @param fileName the fileName for the file
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
long time = Debugger.createTime();			
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
					Debugger.log("FAILED TO RECOGNISE LINE:"+line+" in loadAdjacencyListGraph("+directory+","+fileName+")");
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
	Debugger.outputTime("edgesLoaded "+edgeIndex+" time ");
}
		}

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -88;
		int inLength = -33;
		int outStart = -77;
		int outLength = -22;
		int weight = -55;
		byte type = -77;
		byte age = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
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
		age = 0;
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
		//time = Debugger.createTime();
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			int inEdgeLength = inEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
	}
	
	
	/**
	 * Creates a graph from two files: baseFileName.nodes and baseFileName.edges.
	 * Files are structured as line *"\n" separated) lists of items. Each element
	 * in an item is tab ("\t") separated. Hence no tabs in file names are allowed.
	 * <br>
	 * Nodes are lists of <code>index	label	weight	type	age</code>
	 * <br>
	 * where index must start at 0 and end at nodeCount-1, label is a string, weight is
	 * integer valued, type is byte valued and age is byte valued.
	 * <br> 
	 * Edges are lists of <code>index	node1Index	node2Index	label	weight	type	age</code>
	 * <br>
	 * where index must start at 0 and end at edgeCount-1, node1Index is a node index,
	 * node2Index is a nodeIndex, label is a string, weight is 
	 * integer valued, type is byte valued and age is byte valued.
	 * <br>
	 * Ignores empty lines and lines starting with a hash ("#").
	 * 
	 * @param nodeCount the number of nodes
	 * @param edgeCount the number of edges
	 * @param dir the directory for the file, if null then a directory called data/ under the current working directory
	 * @param baseFileName the base of the file name for the file, two files called baseFileName.nodes and baseFileName.edges are expected
	 * @param direct if true the ByteBuffers are direct, if false they are allocated on the heap
     * @return The new FastGraph
	 * @throws Exception Throws if the graph cannot be built correctly. Might be an IO error
	 */
	public static FastGraph nodeListEdgeListGraphFactory(int nodeCount, int edgeCount, String dir, String baseFileName, boolean direct) throws Exception {
		FastGraph g = new FastGraph(nodeCount,edgeCount,direct);
		g.setName(baseFileName);
		g.loadnodeListEdgeListGraph(dir,baseFileName);
		return g;
	}


	
	/**
	 * Populates a graph from two files: baseFileName.nodes and baseFileName.edges.
	 * Files are structured as line *"\n" separated) lists of items. Each element
	 * in an item is tab ("\t") separated. Hence no tabs in file names are allowed.
	 * <br/>
	 * Nodes are lists of <code>index	label	weight	type	age</code>
	 * <br>
	 * where index must start at 0 and end at nodeCount-1, label is a string, weight is
	 * integer valued, type is byte valued and age is byte valued.
	 * <br/> 
	 * Edges are lists of <code>index	node1Index	node2Index	label	weight	type	age</code>
	 * <br>
	 * where index must start at 0 and end at edgeCount-1, node1Index is a node index,
	 * node2Index is a nodeIndex, label is a string, weight is 
	 * integer valued, type is byte valued and age is byte valued.
	 * <br>
	 * Ignores empty lines and lines starting with a hash ("#").
	 * 
	 * @param dir the directory for the file, if null then a directory called data/ under the current working directory
	 * @param baseFileName the base of the file name for the file, two files called baseFileName.nodes and baseFileName.edges are expected
	 * 
	 * @throws IOException If the buffers cannot be loaded
	 * 
	 */
	private void loadnodeListEdgeListGraph(String dir, String baseFileName) throws Exception {
	
		String directory = dir;
		if(directory == null) {
			directory = Launcher.startingWorkingDirectory+File.separatorChar+"data";
		}
		String basePath = null;
		if(directory.charAt(directory.length()-1)== File.separatorChar) {
			basePath = directory+baseFileName;
		} else {
			basePath = directory+File.separatorChar+baseFileName;
		}
		
		String nodePath = basePath+".nodes";
		File f = new File(nodePath);
		if(!f.exists()) {
			throw new IOException("Problem loading file "+nodePath);
		}
		
		FileInputStream is = new FileInputStream(nodePath);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		// load the nodes
		String[] splitLine;
		String[] nodeLabels = new String[numberOfNodes];
		int inStart = -18;
		int inLength = -13;
		int outStart = -21;
		int outLength = -23;
		int index = -1;
		String label;
		int weight = -15;
		byte type = -17;
		byte age = -19;
		String nodeLine = "";
		while(nodeLine != null) {
			nodeLine = br.readLine();
			if(nodeLine == null) {
				continue;
			}
			if(nodeLine.length() == 0) {
				continue;
			}
			if(nodeLine.charAt(0) == '#') {
				continue;
			}
			
			splitLine = nodeLine.split("\t");
			
			if(splitLine.length < 5) {
				br.close();
				throw new IOException("Not enough elements, needs 5 tab separated elements in "+nodeLine);
			}
	
			try {
				index = Integer.parseInt(splitLine[0]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node index in line "+nodeLine);
			}
			if(index > numberOfNodes) {
				br.close();
				throw new IOException("index "+index+" is greater than the number of nodes "+numberOfNodes);
			}
			label = splitLine[1];
			try {
				weight = Integer.parseInt(splitLine[2]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node weight in line "+nodeLine);
			}
			try {
				type = Byte.parseByte(splitLine[3]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node type in line "+nodeLine);
			}
			try {
				age = Byte.parseByte(splitLine[4]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node age in line "+nodeLine);
			}
			
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+index*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+index*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+index*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+index*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
			nodeBuf.putInt(NODE_WEIGHT_OFFSET+index*NODE_BYTE_SIZE,weight); // weight
			nodeBuf.put(NODE_TYPE_OFFSET+index*NODE_BYTE_SIZE,type); // type
			nodeBuf.put(NODE_AGE_OFFSET+index*NODE_BYTE_SIZE,age); // age

			// save labels for later
			nodeLabels[index] = label;

		}
		br.close();

		setAllNodeLabels(nodeLabels);

		String[] edgeLabels = new String[numberOfEdges];
		
		String edgePath = basePath+".edges";
		f = new File(edgePath);
		if(!f.exists()) {
			throw new IOException("Problem loading file "+edgePath+""+directory);
		}
		
		is = new FileInputStream(edgePath);
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);


		// load the Edges
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
		
		int node1 = -64;
		int node2 = -65;
		String edgeLine = "";
		while(edgeLine != null) {
			edgeLine = br.readLine();
			if(edgeLine == null) {
				continue;
			}
			if(edgeLine.length() == 0) {
				continue;
			}
			if(edgeLine.charAt(0) == '#') {
				continue;
			}
			
			splitLine = edgeLine.split("\t");
			
			if(splitLine.length < 7) {
				br.close();
				throw new IOException("Not enough elements, needs 7 tab separated elements in "+edgeLine);
			}
	
			try {
				index = Integer.parseInt(splitLine[0]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing edge index in line "+edgeLine);
			}
			if(index > numberOfEdges) {
				br.close();
				throw new IOException("index "+index+" is greater than the number of edges "+numberOfEdges);
			}
			try {
				node1 = Integer.parseInt(splitLine[1]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node 1 index in line "+edgeLine);
			}
			try {
				node2 = Integer.parseInt(splitLine[2]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing node 2 index in line "+edgeLine);
			}
			label = splitLine[3];
			try {
				weight = Integer.parseInt(splitLine[4]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing edge weight in line "+edgeLine);
			}
			try {
				type = Byte.parseByte(splitLine[5]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing edge type in line "+edgeLine);
			}
			try {
				age = Byte.parseByte(splitLine[6]);
			} catch(NumberFormatException e) {
				br.close();
				throw new IOException("Problem parsing edge age in line "+edgeLine);
			}

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
		br.close();
			
		
		setAllEdgeLabels(edgeLabels);


		// Initialise the connection buffer, modifying the node buffer connection data
		//time = Debugger.createTime();
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			int inEdgeLength = inEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
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
		String name = "graph";
		if(splitLine.length != 1) {
			name = splitLine[1];
		}
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		int inNodeTotal = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		int inEdgeTotal = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
		int inNodeLabelSize = Integer.parseInt(splitLine[1]);
		line = br.readLine();
		splitLine = line.split(INFO_SPLIT_STRING);
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
	 * Creates a graph with the size specified by numberOfNodes and numberOfEdges. Possibly includes parallel edges and self sourcing nodes.
	 * If the graph is simple, and there are too many edges for the nodes, an exception is thrown
	 * 
	 * @param seed the random number generator seed, -1 for current time
	 * @param simple if true then no self sourcing edges or parallel edges
	 * @throws FastGraphException If the desired number of edges is more than a complete graph for when simple is true
	 */
	public void populateRandomGraph(boolean simple, long seed) throws FastGraphException {
		
		if(simple) {
			long nodeN = numberOfNodes;
			long edgeN = numberOfEdges;
			if((nodeN*(nodeN-1))/2 < edgeN) {
				throw new FastGraphException("Too many edges to generate a simple graph.");
			}
		}

		//long time;
		if(seed == -1) {
			seed = System.currentTimeMillis();
		}
		Random r = new Random(seed);

		String[] nodeLabels = new String[numberOfNodes];
		String[] edgeLabels = new String[numberOfEdges];
		int inStart = -888;
		int inLength = -3;
		int outStart = -777;
		int outLength = -2;
		int weight = -5;
		byte type = -7;
		byte age = 0;
		//generate the nodes
		for(int i = 0; i < numberOfNodes; i++) {
			weight = r.nextInt(100);
			nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,inStart); // offset for inward connecting edges/nodes
			nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
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
		age = 0;
		//generate the edges, with random node connections
		HashSet<String> nodePairs = new HashSet<String>();
		if(simple) {
			nodePairs = new HashSet<String>(numberOfEdges);
		}
		for(int i = 0; i < numberOfEdges; i++) {
			weight = r.nextInt(100);
			node1 = r.nextInt(numberOfNodes);
			node2 = r.nextInt(numberOfNodes);
			if(simple) {
				boolean parallel = false;
				String pairString1 = Integer.toString(node1)+" "+Integer.toString(node2);
				String pairString2 = Integer.toString(node2)+" "+Integer.toString(node1);
				if(nodePairs.contains(pairString1) || nodePairs.contains(pairString2) ) {
					parallel = true;
				}
				while(node2 == node1 || parallel) {
					node1 = r.nextInt(numberOfNodes);
					node2 = r.nextInt(numberOfNodes);
					pairString1 = Integer.toString(node1)+" "+Integer.toString(node2);
					pairString2 = Integer.toString(node2)+" "+Integer.toString(node1);
					if(nodePairs.contains(pairString1) || nodePairs.contains(pairString2) ) {
						parallel = true;
					} else {
						parallel = false;
					}
				}
				nodePairs.add(pairString1);
			}
			
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
			int inEdgeLength = inEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
		
		}
		//Debugger.outputTime("connection put time, direct "+edgeBuf.isDirect());

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
		int inLength = -37;
		int outStart = -47;
		int outLength = -57;
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
			nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE,inLength); // number of inward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+i*NODE_BYTE_SIZE,outStart); // offset for outward connecting edges/nodes
			nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE,outLength); // number of outward connecting edges/nodes
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
				if(dge.getType().getLabel().equals("timeEdge")) {
					type = FastGraphEdgeType.TIME.getValue();
				} else {
					type = Byte.parseByte(dge.getType().getLabel());
				}
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
		//time = Debugger.createTime();
		int offset = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			int inEdgeLength = inEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
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
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
	}
	
	
	

	/**
	 * Generates a new graph from the subgraph specified by the parameters. All
	 * edges connected to deleted nodes are also removed.
	 *
	 * 
	 * @param nodesToDelete nodes in this graph that will not appear in the new graph
	 * @param edgesToDelete edges in this graph that will not appear in the new graph
	 * @param orphanEdgeCheckNeeded If the method calling this has already checked for orphan nodes, then false.
	 * @return the new FastGraph
	 */
	public FastGraph generateGraphByDeletingItems(int[] nodesToDelete, int[] edgesToDelete, boolean orphanEdgeCheckNeeded) {
		
		Debugger.log("Nodes to remove: " + Arrays.toString(nodesToDelete));
		
		long time = Debugger.createTime();

		LinkedList<Integer> allEdgesToDeleteList = new LinkedList<Integer>();
		LinkedList<Integer> allNodesToDeleteList = new LinkedList<Integer>();
		
		Debugger.outputTime("Z setup ");
		time = Debugger.createTime();
		
		for(int e : edgesToDelete) {
			allEdgesToDeleteList.add(e);
		}
		
		// delete the edges connecting to deleted nodes and create the node list
		for(int n : nodesToDelete) {
			allNodesToDeleteList.add(n);
			if(orphanEdgeCheckNeeded) { //only check for orphan nodes if needed
				int[] connectingEdges = getNodeConnectingEdges(n);
				for(int e : connectingEdges) {
					if(!allEdgesToDeleteList.contains(e)) {
						allEdgesToDeleteList.add(e);
					}
				}
			}			
		}
Debugger.outputTime("A Created the node and edge delete lists ");
time = Debugger.createTime();
		
		// find the nodes that will remain
		HashSet<Integer> remainingNodeList = new HashSet<Integer>(allNodesToDeleteList.size()*3);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			remainingNodeList.add(i);
		}
		remainingNodeList.removeAll(allNodesToDeleteList); //this is quicker than checking each entry
		
		Debugger.outputTime("AAA created the node remain lists ");
		time = Debugger.createTime();
		// turn it into an array
		int[] remainingNodes = Util.convertHashSet(remainingNodeList);
		
		Debugger.outputTime("AA converted the node remain lists ");
		time = Debugger.createTime();

		// find the edges that will remain
		HashSet<Integer> remainingEdgeList = new HashSet<Integer>(allEdgesToDeleteList.size()*3);
		for(int i = 0; i < getNumberOfEdges(); i++) {
			remainingEdgeList.add(i);
		}
		remainingEdgeList.removeAll(allEdgesToDeleteList);
		
		Debugger.outputTime("AB Created the edge remain lists ");
		time = Debugger.createTime();
		// turn it into an array
		int[] remainingEdges = Util.convertHashSet(remainingEdgeList);

Debugger.outputTime("B converted the edge remain lists ");
time = Debugger.createTime();

		FastGraph g = generateGraphFromSubgraph(remainingNodes,remainingEdges);
		
		return g;
	}

	/**
	 * Generates a new graph from the subgraph specified by the parameters. The nodes at the end of the edges must be in subgraphEdges.
	 * 
	 * @param subgraphNodes nodes in this graph that will appear in the new graph
	 * @param subgraphEdges edges in this graph that will appear in the new graph, must connect only to subgraphNodes
	 * @return the new FastGraph
	 */
	public FastGraph generateGraphFromSubgraph(int[] subgraphNodes, int[] subgraphEdges) {
		
		long time = Debugger.createTime();

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
//Debugger.outputTime("C popluated the new node buffer ");
time = Debugger.createTime();
		
		g.setAllNodeLabels(nodeLabels); // create the node label buffer
//Debugger.outputTime("D popluated the new node list buffer ");
time = Debugger.createTime();
		
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
//Debugger.outputTime("E created the neighbour store ");
time = Debugger.createTime();
		
//Debugger.log(oldNodesToNew);


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
//Debugger.outputTime("F populated the new edge buffer ");
time = Debugger.createTime();

		g.setAllEdgeLabels(edgeLabels);
//Debugger.outputTime("G populated the new edge label buffer ");
time = Debugger.createTime();
		
		// Initialise the connection buffer, modifying the node buffer connection data
		//time = Debugger.createTime();
		int offset = 0;
		for(int node = 0; node < subgraphNodes.length; node++) {
if(node%100000 == 0) {
	//Debugger.outputTime("H populated "+node+" nodes in connection buffer ");
	time = Debugger.createTime();
}
			
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(node);
			int inEdgeLength = inEdges.size();
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
			
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
					Debugger.log("ERROR A When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(node);
			int outEdgeLength = outEdges.size();
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
			
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
					Debugger.log("ERROR B When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
		
		return g;
	}


	/**
	 * Generates a new graph from the changes in edge wiring specified.
	 * 
	 * @param rewireEdges list of 3 element arrays, rewireEdges[0] is the edge, rewireEdges[1] is the new node1, rewireEdges[2] is the new node2
	 * @return the new FastGraph
	 */
	public FastGraph generateRewiredGraph(List<int[]> rewireEdges) {
		
//Debugger.resetTime();

		FastGraph g = new FastGraph(getNumberOfNodes(), getNumberOfEdges(), getDirect());
		
		g.nodeBuf = Util.cloneByteBuffer(nodeBuf);
		g.nodeLabelBuf = Util.cloneByteBuffer(nodeLabelBuf);
		g.edgeBuf = Util.cloneByteBuffer(edgeBuf);
		g.edgeLabelBuf = Util.cloneByteBuffer(edgeLabelBuf);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(g.getNumberOfNodes()); // temporary store of inward edges
		for(int nodeIndex = 0; nodeIndex < g.getNumberOfNodes(); nodeIndex++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeIn.add(nodeIndex,edges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(g.getNumberOfNodes()); // temporary store of outward edges
		for(int nodeIndex = 0; nodeIndex < g.getNumberOfNodes(); nodeIndex++) {
			ArrayList<Integer> edges = new ArrayList<Integer>(100);
			nodeOut.add(nodeIndex,edges);
		}

		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;	
		
		// create the edges
		edgeBuf.position(0);
		g.edgeBuf.position(0);

		for(int[] eArray : rewireEdges) {
			
			int e = eArray[0];
			
			int gn1 = eArray[1];
			int gn2 = eArray[2];
			
			
			g.edgeBuf.putInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE,gn1); // one end of edge
			g.edgeBuf.putInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE,gn2); // other end of edge
		}
//Debugger.outputTime("A Changed the edge connections");
			
		// the edge connections have been changed, now change the connection lists
		for(int e = 0; e < g.numberOfEdges; e++) {
			
			int gn1 = g.getEdgeNode1(e);
			int gn2 = g.getEdgeNode2(e);
			
			// store connecting edges
			inEdgeList = nodeIn.get(gn2);
			inEdgeList.add(e);
			outEdgeList = nodeOut.get(gn1);
			outEdgeList.add(e);
		}
		
		// Initialise the connection buffer, modifying the node buffer connection data
		int offset = 0;
		for(int node = 0; node < g.getNumberOfNodes(); node++) {
			
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(node);
			int inEdgeLength = inEdges.size();
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
			
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
					Debugger.log("ERROR A When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(node);
			int outEdgeLength = outEdges.size();
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(node*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
			
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
					Debugger.log("ERROR B When finding connections for node "+node+" connecting edge "+edgeIndex+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,edgeIndex);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,nodeIndex);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
		
		return g;
	}
	

	/**
	 * Create an integer array, length of maximum degree in the graph. Each element arr[i] of the array contains a count of the number
	 * of nodes with degree i.
	 * 		
	 * @return the degree profile of the graph. each index contains the number of nodes with that degree
	 */
	public int[] degreeProfile() {
		int maxDegree = maximumDegree();
		
		if(getNumberOfNodes() == 0) {
			return new int[0];
		}
		
		int[] ret = new int[maxDegree+1];
		Arrays.fill(ret, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			int degree = getNodeDegree(i);
			ret[degree]++;
		}
		
		return ret;
	}


	/**
	 * Create an integer array, length of maximum in degree in the graph. Each element arr[i] of the array contains a count of the number
	 * of nodes with in degree i.
	 * 		
	 * @return the in degree profile of the graph. each index contains the number of nodes with that in degree
	 */
	public int[] inDegreeProfile() {
		int maxDegree = maximumInDegree();
		
		if(getNumberOfNodes() == 0) {
			return new int[0];
		}
		
		int[] ret = new int[maxDegree+1];
		Arrays.fill(ret, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			int degree = getNodeInDegree(i);
			ret[degree]++;
		}
		
		return ret;
	}


	
	/**
	 * Create an integer array, length of maximum in degree in the graph. Each element arr[i] of the array contains a count of the number
	 * of nodes with in degree i.
	 * 		
	 * @return the in degree profile of the graph. each index contains the number of nodes with that in degree
	 */
	public int[] outDegreeProfile() {
		int maxDegree = maximumOutDegree();
		
		if(getNumberOfNodes() == 0) {
			return new int[0];
		}
		
		int[] ret = new int[maxDegree+1];
		Arrays.fill(ret, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			int degree = getNodeOutDegree(i);
			ret[degree]++;
		}
		
		return ret;
	}


	
	/**
	 * @return the largest degree for a node in the graph.
	 */
	public int maximumDegree() {
		
		int max = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			int inDegree = nodeBuf.getInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
			int outDegree = nodeBuf.getInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
			int degree = inDegree+outDegree;
			if(degree > max) {
				max = degree;
			}
		}
		return max;
	}
	
	
	/**
	 * @return the largest degree for a node in the graph.
	 */
	public int maximumInDegree() {
		
		int max = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			int inDegree = nodeBuf.getInt(NODE_IN_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
			if(inDegree > max) {
				max = inDegree;
			}
		}
		return max;
	}
	
	
	/**
	 * @return the largest degree for a node in the graph.
	 */
	public int maximumOutDegree() {
		
		int max = 0;
		for(int i = 0; i < numberOfNodes; i++) {
			int outDegree = nodeBuf.getInt(NODE_OUT_DEGREE_OFFSET+i*NODE_BYTE_SIZE);
			if(outDegree > max) {
				max = outDegree;
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
		
		int oldestAge = findMaximumNodeAge();
		Color[] colors = new Color[oldestAge+1];
		if(oldestAge >= 0 && oldestAge <= 11) {
			colors = ColorBrewer.BuGn.getColorPalette(oldestAge+1);
		} else {
			Arrays.fill(colors, Color.WHITE); //fill with white
		}
		for(int i = 0; i < numberOfNodes; i++) {
			Node n = new Node();
			n.setLabel(getNodeLabel(i));
			n.setScore(getNodeWeight(i));
			
			byte age = getNodeAge(i);
			n.setAge(age);
			String typeLabel = Integer.toString(getNodeType(i));
			
			NodeType type = NodeType.withLabel("age"+age);
			if(type == null) {
				type = new NodeType("age"+age);
				type.setFillColor(colors[age]);
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
			
			if(getEdgeType(i) == FastGraphEdgeType.TIME.getValue()) {
				type = getTimeEdgeType();
			}
			e.setType(type);
			g.addEdge(e);
		}
		
		return g;
	}	
	
	/**
	 * Counts the number of instances of nodes with various degrees.
	 * 
	 * @param maxDegrees The maximum number of degrees to look for. If given 3, will count all nodes with degrees 0,1,2.
	 * @return The list of number of nodes at each degree.
	 */
	public int[] countInstancesOfNodeDegrees(int maxDegrees) {
		int[] res = new int[maxDegrees];
		
		for(int n = 0; n < getNumberOfNodes(); n++) {
			int deg = getNodeDegree(n);
			if (deg < maxDegrees) {
				res[deg]++;
			}
		}		
		return res;
	}
	
	
	/**
	 * Check the consistency of a graph. Checks: <ul>
	 * <li> If edges link to node indexes outside of the current range</li>
	 * <li> If all edges are reflected in the connection lists</li>
	 * <li> If the connection list data points to the correct edges</li>
	 * <li> If the nodes and edges in the connection list are correct</li>
	 * </ul>
	 * 
	 * @return true if the graph is consistent, false otherwise
	 */
	public boolean checkConsistency() {

		// consistency of edges
		for(int e = 0; e < getNumberOfEdges(); e++) {
			int node1 = getEdgeNode1(e);
			int node2 = getEdgeNode2(e);
			if(node1 < 0 || node1 >= getNumberOfNodes()) {
				Debugger.log("INCONSISTENT. Edge "+e+" has node1 "+node1+ " but there are only "+getNumberOfNodes()+" nodes");
				return false;
			}
			if(node2 < 0 || node2 >= getNumberOfNodes()) {
				Debugger.log("INCONSISTENT. Edge "+e+" has node2 "+node2+ " but there are only "+getNumberOfNodes()+" nodes");
				return false;
			}
			if(!Util.convertArray(getNodeConnectingOutEdges(node1)).contains(e)) {
				Debugger.log("INCONSISTENT. Edge "+e+" has node1 "+node1+ " but edge "+e+" is not in the node's out edges list");
				return false;
			}
			if(!Util.convertArray(getNodeConnectingInEdges(node2)).contains(e)) {
				Debugger.log("INCONSISTENT. Edge "+e+" has node2 "+node2+ " but edge "+e+" is not in the node's in edges list");
				return false;
			}
		}
		
		// consistency of nodes and connection lists
		for(int n = 0; n < getNumberOfNodes(); n++) {
			if(getNodeConnectingOutEdges(n).length != getNodeConnectingOutNodes(n).length) {
				Debugger.log("INCONSISTENT. Node "+n+" has different number of out edges to out nodes");
				return false;
			}
			if(getNodeConnectingInEdges(n).length != getNodeConnectingInNodes(n).length) {
				Debugger.log("INCONSISTENT. Node "+n+" has different number of in edges to in nodes");
				return false;
			}
			for(int i = 0; i < getNodeConnectingOutEdges(n).length; i++) {
				int connectingEdge = getNodeConnectingOutEdges(n)[i];
				int otherEnd = oppositeEnd(connectingEdge, n);
				int connectingNode = getNodeConnectingOutNodes(n)[i];
				if(otherEnd != connectingNode) {
					Debugger.log("INCONSISTENT. Node "+n+" has inconsistent edge and node connecting out list");
					return false;
				}
				if(n != oppositeEnd(connectingEdge, otherEnd)) {
					Debugger.log("INCONSISTENT. Node "+n+" has edge in connecting  out list that does not point to the node");
					return false;
				}
			}
				
			for(int i = 0; i < getNodeConnectingInEdges(n).length; i++) {
					int connectingEdge = getNodeConnectingInEdges(n)[i];
					int otherEnd = oppositeEnd(connectingEdge, n);
					int connectingNode = getNodeConnectingInNodes(n)[i];
					if(otherEnd != connectingNode) {
						Debugger.log("INCONSISTENT. Node "+n+" has inconsistent edge and node in connecting in list");
					return false;
				}
				if(n != oppositeEnd(connectingEdge, otherEnd)) {
					Debugger.log("INCONSISTENT. Node "+n+" has edge in connecting in list that does not point to the node");
					return false;
				}

			}
			
			// orientation and position of node connections
			int inConnectionOffset = nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+n*NODE_BYTE_SIZE);
			int outConnectionOffset = nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+n*NODE_BYTE_SIZE);
			
			if(inConnectionOffset > outConnectionOffset) {
				Debugger.log("INCONSISTENT. node "+n+" inConnectionOffset must be less than or equal to outConnectionOffset");
				return false;
			}
			if(outConnectionOffset != inConnectionOffset+getNodeInDegree(n)*8) {
				Debugger.log("INCONSISTENT. node "+n+" outConnectionOffset must straight after inConnectionOffset");
				return false;
			}

		}
		
		return true;
	}




	/**
	 * This generates a new random graph based on the existing graph, but with rewired edges. The node degrees are maintained.
	 * It performs multiple rewrites 
	 * 
	 * @param iterations the number of times to rewire, more means better chance of a truly random graph
	 * @param seed random number generator seed
	 * @return a new graph, based on g, but rewired graph
	 */
	public FastGraph generateRandomRewiredGraph(int iterations, long seed) {
		
		String name = this.getName();
Debugger.resetTime();
		long theSeed = seed;
		FastGraph g = this;
		for(int i = 0; i< iterations; i++) {
Debugger.log("        iteration number: " + i);
			FastGraph h = g.oneIterationGenerateRandomRewiredGraph(theSeed);
			if(h == null) {
				return null;
			} else {
				h.setName(name+"-"+i);
			}
			g = h;
			theSeed += g.getNumberOfEdges()*10;
Debugger.outputTime("time for rewiring");
//h.saveBuffers(null, g.getName());
		}
		
		return g;
	}
	
	
	
	
	/**
	 * This generates a new random graph based on the existing graph, but with rewired edges. The node degrees are maintained.
	 * 
	 * 
	 * @param seed random number generator seed
	 * @return a new graph, based on g, but rewired graph
	 */
	private FastGraph oneIterationGenerateRandomRewiredGraph(long seed) {
		
		final int ITERATIONS_TIME_OUT = 10000;

		Random r = new Random(seed);

		// rewiring the node1 of edges

		HashSet<Integer> removedEdges = new HashSet<Integer>(getNumberOfEdges()*2);
		// don't rewire node1 of edges more than once
		int[] remainingEdgeArray = new int[getNumberOfEdges()];
		int remainingEdgeCount = getNumberOfEdges();
		for(int i = 0; i < getNumberOfEdges(); i++) {
			remainingEdgeArray[i] = i;
		}
		// keep track of nodes that have unvisited out edges
		ArrayList<Integer> candidateNodes = new ArrayList<Integer>(getNumberOfNodes()*2);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			if(getNodeOutDegree(i) > 0) {
				candidateNodes.add(i);
			}
		}

		// this removes an edge from remainingEdgeArray
		// by replacing the edge with the last edge in the array, and reducing the elements in the array that will be searched next time by one
		Debugger.log("number of edges "+getNumberOfEdges());
		Debugger.log("remaining edge count "+remainingEdgeCount);
		int startEdge = r.nextInt(remainingEdgeCount);
		int startNode = getEdgeNode1(startEdge);
		remainingEdgeArray[startEdge] = remainingEdgeArray[remainingEdgeCount-1];
		remainingEdgeCount--;
		removedEdges.add(startEdge);

		int nextEdge = startEdge;
		HashMap<Integer,Integer> rewireNode1 = new HashMap<Integer,Integer>(getNumberOfEdges()*2); // edge then new node1 for the edge
		int nextNode = -1;
		while(remainingEdgeCount > 0) {
			// find another node and an in edge to swap
			nextNode = -1;
			int foundEdge = -1;
			int nodeIterations = 0;
			while(nextNode == -1) {
				nodeIterations++;
				if(nodeIterations > ITERATIONS_TIME_OUT) { // end here-  no candidate nodes
					break;
				}

				int nodeIndex = r.nextInt(candidateNodes.size());
				int tryNode = candidateNodes.get(nodeIndex);
				
				// need to find an unused edge, connecting to tryNode, randomly
				ArrayList<Integer> candidateEdges = new ArrayList<Integer>(Util.convertArray(getNodeConnectingOutEdges(tryNode)));
				while(candidateEdges.size() > 0 && foundEdge == -1) {
					int edgeIndex = r.nextInt(candidateEdges.size());
					int tryEdge = candidateEdges.get(edgeIndex);
					if(!removedEdges.contains(tryEdge)) {
						foundEdge = tryEdge;
						// these commands remove tryEdge
						remainingEdgeArray[tryEdge] = remainingEdgeArray[remainingEdgeCount-1];
						remainingEdgeCount--;
						removedEdges.add(tryEdge);

						nextNode = tryNode;
					}
					candidateEdges.remove(edgeIndex);
				}
				
				if(foundEdge == -1) { // if there is no suitable edge, node is no longer a candidate as all its out edges have been used up
					candidateNodes.remove(nodeIndex);
				}
			}
			
			if(nextNode == -1) { // cant find a suitable node so exit
				break;
			}
			
			rewireNode1.put(nextEdge, nextNode);
			nextEdge = foundEdge; // the next edge to be rewired attaches to nextNode
			
		}
		// rewire the current edge to the start node, making the start node degree correct
		if(nextNode != -1) {
			rewireNode1.put(nextEdge, startNode);
		}
		
		
		// rewiring the node2 of edges

		// don't rewire node2 of edges more than once
		removedEdges.clear();
		remainingEdgeCount = getNumberOfEdges();
		for(int i = 0; i < getNumberOfEdges(); i++) {
			remainingEdgeArray[i] = i;
		}
		
		// keep track of nodes that have unvisited in edges
		candidateNodes = new ArrayList<Integer>(getNumberOfNodes()*2);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			if(getNodeInDegree(i) > 0) {
				candidateNodes.add(i);
			}
		}
		
		startEdge = r.nextInt(getNumberOfEdges());
		startNode = getEdgeNode2(startEdge);
		// this code removes startEdge
		remainingEdgeArray[startEdge] = remainingEdgeArray[remainingEdgeCount-1];
		remainingEdgeCount--;
		removedEdges.add(startEdge);

		nextEdge = startEdge;
		
		HashMap<Integer,Integer> rewireNode2 = new HashMap<Integer,Integer>(getNumberOfEdges()*2); // edge then new node1 for the edge
		nextNode = -1;
		while(remainingEdgeCount > 0) {
			
			// find another node and an in edge to swap
			nextNode = -1;
			int foundEdge = -1;
			int nodeIterations = 0;
			while(nextNode == -1) {
				nodeIterations++;
				if(nodeIterations > ITERATIONS_TIME_OUT) { // end here, no candidate nodes
					break;
				}
				
				int nodeIndex = r.nextInt(candidateNodes.size());
				int tryNode = candidateNodes.get(nodeIndex);
				
				// need to find an unused edge, connecting to tryNode, randomly
				ArrayList<Integer> candidateEdges = new ArrayList<Integer>(Util.convertArray(getNodeConnectingInEdges(tryNode)));
				while(candidateEdges.size() > 0 && foundEdge == -1) {
					int edgeIndex = r.nextInt(candidateEdges.size());
					int tryEdge = candidateEdges.get(edgeIndex);
					if(!removedEdges.contains(tryEdge)) {
						foundEdge = tryEdge;
						// these commands remove tryEdge
						remainingEdgeArray[tryEdge] = remainingEdgeArray[remainingEdgeCount-1];
						remainingEdgeCount--;
						removedEdges.add(tryEdge);
						
						nextNode = tryNode;
					}
					candidateEdges.remove(edgeIndex);
				}

				if(foundEdge == -1) { // if there is no suitable edge, node is no longer a candidate as all its in edges have been used up
					candidateNodes.remove(nodeIndex);
				}
			}
			
			if(nextNode == -1) { // cant find a suitable node so exit
				break;
			}
			
			rewireNode2.put(nextEdge, nextNode);
			nextEdge = foundEdge; // the next edge to be rewired attaches to nextNode
			
		}
		// rewire the current edge to the end node, making the start node degree correct
		if(nextNode != -1) {
			rewireNode2.put(nextEdge, startNode);
		}
		
		// populate the rewiring
		LinkedList<int[]> rewireEdges = new LinkedList<int[]>();
		for(int i = 0; i < getNumberOfEdges(); i++) {
			Integer node1 = rewireNode1.get(i);
			Integer node2 = rewireNode2.get(i);
			int[] rewiring = new int[3];
			if(node1 != null && node2 != null) { // both ends are rewired
				rewiring[0] = i;
				rewiring[1] = node1;
				rewiring[2] = node2;
				rewireEdges.add(rewiring);
			}
			if(node1 != null && node2 == null) { // only node1 is rewired
				rewiring[0] = i;
				rewiring[1] = node1;
				rewiring[2] = getEdgeNode2(i);
				rewireEdges.add(rewiring);
			}
			if(node1 == null && node2 != null) { // only node2 is rewired
				rewiring[0] = i;
				rewiring[1] = getEdgeNode1(i);
				rewiring[2] = node2;
				rewireEdges.add(rewiring);
			}
		}
		

		
		FastGraph ret = generateRewiredGraph(rewireEdges);

		
		return ret;
		
		
	}

	
	/**
	 * Displays a FastGraph onscreen for the user
	 */
	public void displayFastGraph() {
		uk.ac.kent.displayGraph.Graph dg = this.generateDisplayGraph();
		dg.randomizeNodePoints(new Point(20,20),300,300);
		uk.ac.kent.displayGraph.display.GraphWindow gw = new uk.ac.kent.displayGraph.display.GraphWindow(dg, true);
		uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder bse = new uk.ac.kent.displayGraph.drawers.BasicSpringEmbedder();
//		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
		GraphDrawerGravitySpringEmbedder se = new GraphDrawerGravitySpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
		se.setAnimateFlag(false);
		se.setIterations(1000);
		se.setTimeLimit(200);
		se.setGraphPanel(gw.getGraphPanel());
		se.layout();
	}
	
	/**
	 * Gets a big string of the node labels.
	 * Useful for identifying unique subgraphs/motifs
	 * @return a big string of the node labels
	 */
	public String getNodeLabelString() {
		String[] output = new String[this.getNumberOfNodes()];
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			output[i] = this.getNodeLabel(i);
		}
		Arrays.sort(output);
		return Arrays.toString(output);
	}

	/**
	 * Gets a big string of the edge labels.
	 * Useful for identifying unique subgraphs/motifs
	 * @return a big string of the edge labels
	 */
	public String getEdgeLabelString() {
		String[] output = new String[this.getNumberOfEdges()];
		for(int i = 0; i < this.getNumberOfEdges(); i++) {
			output[i] =  this.getEdgeLabel(i);
		}
		Arrays.sort(output);
		return Arrays.toString(output);
	}
	
	/**
	 * Do any of the nodes have a label?
	 * @return if any node has a label
	 */
	public boolean isAnyNodeLabelled() {
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			if(!this.getNodeLabel(i).equals("")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Do any of the Edges have a label?
	 * @return if any Edge has a label
	 */
	public boolean isAnyEdgeLabelled() {
		for(int i = 0; i < this.getNumberOfEdges(); i++) {
			if(!this.getEdgeLabel(i).equals("")) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @return the largest node age in the graph
	 */
	public byte findMaximumNodeAge() {
		byte ret = 0;
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			if(this.getNodeAge(i) > ret) {
				ret = this.getNodeAge(i);
			}
		}
		return ret;
	}
	
	
	/**
	 * @return the smallest node age in the graph
	 */
	public byte findMinimumNodeAge() {
		byte ret = Byte.MAX_VALUE;
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			if(this.getNodeAge(i) < ret) {
				ret = this.getNodeAge(i);
			}
		}
		return ret;
	}
	
	/**
	 * Counts the number of nodes at the given age
	 * @param age The given age
	 * @return The number of nodes at this age
	 */
	public int countNodesOfAge(int age) {
		int ret = 0;
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			if(this.getNodeAge(i) == age) {
				ret++;
			}
		}
		return ret;		
	}
	
	/**
	 * @return the largest node age in the graph
	 */
	public byte findMaximumEdgeAge() {
		byte ret = Byte.MIN_VALUE;
		for(int i = 0; i < this.getNumberOfEdges(); i++) {
			if(this.getEdgeAge(i) > ret) {
				ret = this.getEdgeAge(i);
			}
		}
		return ret;
	}
	
	/**
	 * @return the smallest node age in the graph
	 */
	public byte findMinimumEdgeAge() {
		byte ret = Byte.MAX_VALUE;
		for(int i = 0; i < this.getNumberOfEdges(); i++) {
			if(this.getEdgeAge(i) < ret) {
				ret = this.getEdgeAge(i);
			}
		}
		return ret;
	}
	

	/**
	 * Creates a new FastGraph with a new time slice, the nodes and edges in the new time slice are those in the current oldest
	 * time slice, with the edits given by the parameters. Time type edges are created between
	 * nodes that persist from the current generation the new time slice. Orphaned edges (created by node delete) are also
	 * deleted. Time slice of nodes and edges is given by the age attribute. New generation nodes and edges get an age of current oldest plus one, so any new edges and nodes have their current
	 * age discarded.
	 * addNodes should contain node with unique ids greater than the number of nodes in the current graph.
	 * 
	 * @param deleteNodes nodes in the oldest generation that should not appear in the new time slice
	 * @param deleteEdges edges in the oldest generation that should not appear in the new time slice
	 * @param addNodes extra nodes to appear in the new time slice. Ids must not appear in the current FastGraph
	 * @param addEdges extra edges to appear in the new time slice. They can use addNode indexes or indexes in the current graph from nodes in the current oldest time slice
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the new graph with time slice added. Information about added indexes and mappings is stored in various 
	 */
	public FastGraph addNewTimeSlice(Collection<Integer> deleteNodes, Collection<Integer> deleteEdges, Collection<NodeStructure> addNodes, Collection<EdgeStructure> addEdges, boolean direct) {
		
		// find the greatest age in the current FastGraph to get the nextGeneration, has to be maximum node generation, as edges cannot exist alone
		byte oldGeneration = getGeneration();
		byte newGeneration = (byte)(oldGeneration+1);
		HashMap<Integer,Integer> oldToNewNodeMapping = new HashMap<Integer,Integer>();
		
		LinkedList<NodeStructure> allNodes = new LinkedList<NodeStructure>();
		LinkedList<EdgeStructure> allEdges = new LinkedList<EdgeStructure>();
		
		// store the previous generation nodes in the list
		for(int i = 0; i < numberOfNodes; i++) {
			String label = getNodeLabel(i);
			int weight = getNodeWeight(i);
			byte type = getNodeType(i);
			byte age = getNodeAge(i);
			NodeStructure ns = new NodeStructure(i,label,weight,type,age);
			allNodes.add(ns);
		}
		
		int nodeId = numberOfNodes;
		int edgeId = numberOfEdges;

		LinkedList<EdgeStructure> timeEdges = new LinkedList<EdgeStructure>();
		
		// duplicate the existing nodes first, and create the time edges
		for(int i = 0 ; i < numberOfNodes; i++) {
			if(getNodeAge(i) != oldGeneration) { // only want nodes in the latest generation
				continue;
			}
			if(deleteNodes.contains(i)) {
				continue;
			}

			String label = getNodeLabel(i);
			int weight = getNodeWeight(i);
			byte type = getNodeType(i);
			byte age = newGeneration;
			NodeStructure ns = new NodeStructure(nodeId,label,weight,type,age);
			allNodes.add(ns);
			
			oldToNewNodeMapping.put(i,nodeId);
			
			// add the time edges
			String timeLabel = "";
			int timeWeight = 0;
			byte timeType = FastGraphEdgeType.TIME.getValue();
			byte timeAge = newGeneration;
			int timeNode1 = i;
			int timeNode2 = nodeId;
			EdgeStructure es = new EdgeStructure(edgeId,timeLabel,timeWeight,timeType,timeAge,timeNode1,timeNode2);
			timeEdges.add(es);
			edgeId++;
			nodeId++;
		}

		// add the new nodes
		for(NodeStructure addNS : addNodes) {
			NodeStructure ns = new NodeStructure(nodeId, addNS.getLabel(), addNS.getWeight(), addNS.getType(), newGeneration); 
			allNodes.add(ns);
			oldToNewNodeMapping.put(addNS.getId(),nodeId);
			nodeId++;
		}
		
		HashSet<Integer> fullDeleteEdges = new HashSet<Integer>(deleteEdges.size()*3);
		
		for(Integer n : deleteNodes) {
			for(int e : getNodeConnectingEdges(n)) {
				fullDeleteEdges.add(e);
			}
		}
		fullDeleteEdges.addAll(deleteEdges);

		// store the previous generation edges in the list
		for(int i = 0; i < numberOfEdges; i++) {
			String label = getEdgeLabel(i);
			int weight = getEdgeWeight(i);
			byte type = getEdgeType(i);
			byte age = getEdgeAge(i);
			int node1 = getEdgeNode1(i);
			int node2 = getEdgeNode2(i);
			EdgeStructure es = new EdgeStructure(i,label,weight,type,age,node1,node2);
			allEdges.add(es);
		}
		// duplicate edges in the new time slice
		LinkedList<EdgeStructure> edgesInTimeSlice = new LinkedList<EdgeStructure>();
		for(int i = 0; i < numberOfEdges; i++) {
			if(getEdgeAge(i) != oldGeneration) { // only want edges in the latest generation
				continue;
			}
			if(getEdgeType(i) == FastGraphEdgeType.TIME.getValue()) { // don't duplicate time edges
				continue;
			}
			if(fullDeleteEdges.contains(i)) { // don't duplicate deleted edges
				continue;
			}

			String label = getEdgeLabel(i);
			int weight = getEdgeWeight(i);
			byte type = getEdgeType(i);
			byte age = newGeneration;
			int node1 = oldToNewNodeMapping.get(getEdgeNode1(i));
			int node2 = oldToNewNodeMapping.get(getEdgeNode2(i));
			EdgeStructure es = new EdgeStructure(edgeId,label,weight,type,age,node1,node2);
			edgesInTimeSlice.add(es);
			edgeId++;
		}
		// add the new edges
		for(EdgeStructure addES : addEdges) {
			int node1 = oldToNewNodeMapping.get(addES.getNode1());
			int node2 = oldToNewNodeMapping.get(addES.getNode2());
			EdgeStructure es = new EdgeStructure(edgeId, addES.getLabel(), addES.getWeight(), addES.getType(), newGeneration,node1,node2); 
			edgesInTimeSlice.add(es);
			edgeId++;
		}


		// fix the node1 and node2 for each edge to match the new node ids
		for(EdgeStructure es : edgesInTimeSlice) {
			int node1 = es.getNode1();
			int node2 = es.getNode2();
			es.setNode1(node1);
			es.setNode2(node2);
		}
		
		allEdges.addAll(timeEdges);
		allEdges.addAll(edgesInTimeSlice); 
		
		
		FastGraph g = structureFactory(getName()+"-"+newGeneration,newGeneration,allNodes,allEdges,direct);
		
		return g;
	}
	


	/**
	 * 
	 * Create a graph. Rewire generation (age) 0 then apply random changes to later
	 * generations to create a graph with the same number of generations as this graph.
	 * 
	 * @param iterations the number of times to rewire generation 0, more means better chance of a truly random graph
	 * @param seed random number generator seed for rewiring
	 * @param deleteNodeProbability The probability a node will be removed
	 * @param deleteEdgeProbability The probability an edge will be removed. Note edges will be removed if orphaned
	 * @param nodesToAdd The number of nodes to add
	 * @param edgesToAdd The number of edges to add
	 * @param sensibleLabels If the new nodes and edges will have realistic labels. Increases the time and memory usage
	 * @param direct set to false if on heap, true if off heap
	 * @return The new graph based on this generation 0, rewired with the added timeslices
	 * @throws IOException If the graph cannot be saved
	 */
	public FastGraph generateRewiredBehaviourGraphWithRandomGenerations(int iterations, long seed,double deleteNodeProbability, double deleteEdgeProbability, int nodesToAdd, int edgesToAdd, 
			boolean sensibleLabels, boolean direct) throws IOException {

		FastGraph generation0Graph = findGenerationSubGraph((byte)0,direct);
		FastGraph nextGenerationGraph = generation0Graph.generateRandomRewiredGraph(iterations, seed);
		
		byte maxGeneration = findMaximumNodeAge();
		
		byte generation = 0;
		while(generation < maxGeneration) {
			
			nextGenerationGraph = nextGenerationGraph.randomTimeSeriesFactory(deleteNodeProbability, deleteEdgeProbability, nodesToAdd, edgesToAdd, sensibleLabels, direct);
			generation++;
		}
	
		nextGenerationGraph.setName(this.name+"-rewire-"+iterations);

		return nextGenerationGraph;
	}
	
	
	/**
	 * Return a new FastGraph that is the nodes and edges of a particular generation
	 * of this FastGraph. Does not include time edge. Assumes all other edges connect
	 * only with nodes of the same generation.
	 * 
	 * @param inGeneration the generation of nodes and edges which will form the new FastGraph
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return The new FastGraph
	 */
	public FastGraph findGenerationSubGraph(byte inGeneration, boolean direct) {
		
		LinkedList<NodeStructure> nodes = new LinkedList<NodeStructure>();
		LinkedList<EdgeStructure> edges = new LinkedList<EdgeStructure>();


		HashMap<Integer,Integer> oldToNewNodeIds = new HashMap<Integer,Integer>();
		int nodeId = 0;
		for(int i = 0; i < getNumberOfNodes();i++) {
			
			if(getNodeAge(i) != inGeneration) {
				continue;
			}
			
			NodeStructure ns = new NodeStructure(nodeId, getNodeLabel(i), getNodeWeight(i), getNodeType(i), getNodeAge(i));
			nodes.add(ns);
			oldToNewNodeIds.put(i, nodeId);
			
			nodeId++;
		}
		
		int edgeId = 0;
		for(int i = 0; i < getNumberOfEdges();i++) {
			
			if(getEdgeAge(i) != inGeneration) {
				continue;
			}
			if(getEdgeType(i) == FastGraphEdgeType.TIME.getValue()) {
				continue;
			}

			int node1 = oldToNewNodeIds.get(getEdgeNode1(i));
			int node2 = oldToNewNodeIds.get(getEdgeNode2(i));
			EdgeStructure es = new EdgeStructure(edgeId, getEdgeLabel(i), getEdgeWeight(i), getEdgeType(i), getEdgeAge(i), node1, node2);
			edges.add(es);
			
			edgeId++;
		}
		
		FastGraph g = structureFactory(getName()+"-sub"+inGeneration,inGeneration,nodes,edges,direct);
		
		return g;
	}


	/**
	 * Given a collection of NodeStructure and EdgeStructure, create a new graph.
	 * Edges node1 and node2 refer to the ids in the nodes list. This method
	 * does not use ids of elements in nodes and edges, instead assigns them
	 * according to their position in the list, starting at 0.
	 * 
	 * @param inName the name of the new FastGraph
	 * @param inGeneration the generation of the new FastGraph, put 0 if unsure
	 * @param nodes the nodes to be in the new FastGraph. ids are ignored
	 * @param edges the edges to be in the new FastGraph. ids are ignored
	 * @param direct if true then off heap ByteBuffers, if false then on heap ByteBuffers
	 * @return the new FastGraph with the nodes and edges from the input
	 */
	public static FastGraph structureFactory(String inName, byte inGeneration, List<NodeStructure> nodes, List<EdgeStructure> edges, boolean direct) {
		int nodeCount = nodes.size();
		int edgeCount = edges.size();
		FastGraph g = new FastGraph(nodeCount,edgeCount,direct);
		g.setName(inName);
		g.generation = inGeneration;
		
		String[] nodeLabels = new String[nodeCount];
		String[] edgeLabels = new String[edgeCount];

		int nodeId = 0;
		for(NodeStructure ns : nodes) {
			ns.setId(nodeId);
			g.nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*NODE_BYTE_SIZE,-1); // offset for inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+nodeId*NODE_BYTE_SIZE,-1); // number of inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*NODE_BYTE_SIZE,-1); // offset for outward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+nodeId*NODE_BYTE_SIZE,-1); // number of outward connecting edges/nodes
			g.nodeBuf.putInt(NODE_WEIGHT_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getWeight()); // weight
			g.nodeBuf.put(NODE_TYPE_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getType()); // type
			g.nodeBuf.put(NODE_AGE_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getAge()); // age

			// save labels for later
			nodeLabels[nodeId] = ns.getLabel();
			nodeId++;
		}

		g.setAllNodeLabels(nodeLabels);

		ArrayList<ArrayList<Integer>> nodeIn = new ArrayList<ArrayList<Integer>>(nodeCount); // temporary store of inward edges
		for(int i = 0; i < nodeCount; i++) {
			ArrayList<Integer> connectingEdges = new ArrayList<Integer>(100);
			nodeIn.add(i,connectingEdges);
		}
		
		ArrayList<ArrayList<Integer>> nodeOut = new ArrayList<ArrayList<Integer>>(nodeCount); // temporary store of outward edges
		for(int i = 0; i < nodeCount; i++) {
			ArrayList<Integer> connectingEdges = new ArrayList<Integer>(100);
			nodeOut.add(i,connectingEdges);
		}
		
		ArrayList<Integer> inEdgeList;	
		ArrayList<Integer> outEdgeList;
		int edgeId = 0;
		for(EdgeStructure es : edges) {
			es.setId(edgeId);
			g.edgeBuf.putInt(EDGE_NODE1_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getNode1()); // one end of edge
			g.edgeBuf.putInt(EDGE_NODE2_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getNode2()); // other end of edge
			g.edgeBuf.putInt(EDGE_WEIGHT_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getWeight()); // weight
			g.edgeBuf.put(EDGE_TYPE_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getType()); // type
			g.edgeBuf.put(EDGE_AGE_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getAge()); // age
			
			// store labels for later
			edgeLabels[edgeId] = es.getLabel();
			
			// store connecting nodes
			inEdgeList = nodeIn.get(es.getNode2());
			inEdgeList.add(edgeId);
			outEdgeList = nodeOut.get(es.getNode1());
			outEdgeList.add(edgeId);
			
			edgeId++;
			
		}
		
		g.setAllEdgeLabels(edgeLabels);

		// Initialise the connection buffer, modifying the node buffer connection data
		//time = Debugger.createTime();
		int offset = 0;
		for(int i = 0; i < nodeCount; i++) {
			// setting the in connection offset and length
			ArrayList<Integer> inEdges = nodeIn.get(i);
			int inEdgeLength = inEdges.size();
			g.nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inEdgeLength);
		
			// now put the in edge/node pairs
			for(int e : inEdges) {
				int n = -1;
				int n1 = g.edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = g.edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
			
			// setting the out connection offset and length
			ArrayList<Integer> outEdges = nodeOut.get(i);
			int outEdgeLength = outEdges.size();
			g.nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_CONNECTION_START_OFFSET,offset);
			g.nodeBuf.putInt(i*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outEdgeLength);
		
			// now put the out edge/node pairs
			for(int e : outEdges) {
				int n = -1;
				int n1 = g.edgeBuf.getInt(EDGE_NODE1_OFFSET+e*EDGE_BYTE_SIZE);
				int n2 = g.edgeBuf.getInt(EDGE_NODE2_OFFSET+e*EDGE_BYTE_SIZE);
				if(n1 == i) {
					n = n2;
				} else if(n2 == i) {
					n = n1;
				} else {
					Debugger.log("ERROR When finding connections for node "+i+" connecting edge "+e+ " has connecting nodes "+n1+" "+n2);
				}
				g.connectionBuf.putInt(CONNECTION_EDGE_OFFSET+offset,e);
				g.connectionBuf.putInt(CONNECTION_NODE_OFFSET+offset,n);
				offset += CONNECTION_PAIR_SIZE;
			}
		}
		
		return g;
		
	}
		
	/**
	 * Creates a random time slice based on the current graph.
	 * 
	 * @param deleteNodeProbability The probability a node will be removed
	 * @param deleteEdgeProbability The probability an edge will be removed. Note edges will be removed if orphaned
	 * @param nodesToAdd The number of nodes to add
	 * @param edgesToAdd The number of edges to add
	 * @param sensibleLabels If the new nodes and edges will have realistic labels. Increases the time and memory usage
	 * @param direct set to false if on heap, true if off heap
	 * @return The new graph with the added timeslice
	 * @throws IOException If the names file cannot be loaded
	 */
	public FastGraph randomTimeSeriesFactory(double deleteNodeProbability, double deleteEdgeProbability, int nodesToAdd, int edgesToAdd, 
			boolean sensibleLabels, boolean direct) throws IOException {
		
		byte oldGeneration = getGeneration();
		byte newGeneration = (byte)(oldGeneration+1);
		
		//int nodeSize = this.getNumberOfNodes();
		ArrayList<Integer> thisGenNodes = this.findAllNodesOfAge(oldGeneration);
		ArrayList<Integer> thisGenEdges = this.findAllEdgesOfAge(oldGeneration);
		//int edgeSize = this.getNumberOfEdges();
		
		
		Random r = new Random(this.getNodeBuf().getLong(0));
		//create Name Picker class
		NamePicker np = null;
		if(sensibleLabels) {
			np = new NamePicker();
		}		
		
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		//select nodes to remove
		for(int i : thisGenNodes) {
			double prob = r.nextDouble();
			if (prob < deleteNodeProbability) {
				deleteNodes.add(i);
			}
		}
		thisGenNodes.removeAll(deleteNodes);
		
		//select edges to remove
		for(int i : thisGenEdges) {
			double prob = r.nextDouble();
			if (prob < deleteEdgeProbability) {
				deleteEdges.add(i);
			}
		}
		thisGenEdges.removeAll(deleteEdges);
		
		//add nodes
		for(int i = 0; i < nodesToAdd; i++) {
			String name = "added"+i;
			if(sensibleLabels) {
				name = np.getName();
			}
			NodeStructure ns = new NodeStructure(thisGenNodes.size()+i, name, 0, FastGraphNodeType.UNKNOWN.getValue(), newGeneration);
			addNodes.add(ns);
			//nodeSize++;
		}
		
		//add edges
		for(int i = 0; i < edgesToAdd; i++) {
			
			FastGraphEdgeType type = FastGraphEdgeType.UNKNOWN;
			if(sensibleLabels) {
				type = FastGraphEdgeType.pickRandomExceptFamilyAndTime(r);
			}
			byte relationship = type.getValue();
			String label = type.toString();
			
			int n1 = Util.pickRandom(r,thisGenNodes);
			int n2 = Util.pickRandom(r,thisGenNodes);
			
			EdgeStructure es = new EdgeStructure(thisGenEdges.size()+i, label, 0, relationship, newGeneration, n1, n2);
			addEdges.add(es);
			//edgeSize++;
		}
		
Debugger.resetTime();		
		FastGraph g2 = this.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, direct);
Debugger.outputTime("time to create new time slice total nodes "+g2.getNumberOfNodes()+" edges "+g2.getNumberOfEdges());		
		
		return g2;
	}
	
	/**
	 * Creates the Display EdgeType for time edges.
	 * @return The edgeType
	 */
	public static EdgeType getTimeEdgeType() {
		EdgeType time = new EdgeType("timeEdge");
		time.setLineColor(Color.magenta);
		time.setSelectedLineColor(Color.gray);
		return time;
	}
	
	/**
	 * Finds a list of all nodes of the given age
	 * @param age The given age
	 * @return a list of all nodes of the given age
	 */
	public ArrayList<Integer> findAllNodesOfAge(int age){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			if(age == this.getNodeAge(i)) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	/**
	 * Finds a list of all edges of the given age
	 * @param age The given age
	 * @return a list of all edges of the given age
	 */
	public ArrayList<Integer> findAllEdgesOfAge(int age){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i = 0; i < this.getNumberOfEdges(); i++) {
			if(age == this.getEdgeAge(i)) {
				ret.add(i);
			}
		}
		return ret;
	}
	
	/**
	 * Finds the degrees of each node.
	 * 
	 * @return an array containing the degrees
	 */
	public int[] findDegrees() {
		int[] degrees = new int[this.getNumberOfNodes()];
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			degrees[i] = this.getNodeDegree(i);
		}
		
		return degrees;
	}
	
	/**
	 * Finds the indegrees of each node. This is the number of nodes connecting
	 * where the edge points at the node.
	 * 
	 * @return an array containing the indegrees
	 */
	public int[] findInDegrees() {
		int[] degrees = new int[this.getNumberOfNodes()];
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			degrees[i] = this.getNodeInDegree(i);
		}
		
		return degrees;
	}
	
	
	/**
	 * Finds the outdegrees of each node. This is the number of nodes connecting
	 * where the edge points away from the node.
	 * 
	 * @return an array containing the outdegrees
	 */
	public int[] findOutDegrees() {
		int[] degrees = new int[this.getNumberOfNodes()];
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			degrees[i] = this.getNodeOutDegree(i);
		}
		
		return degrees;
	}
	
	

	/**
	 * Finds an array where the each array index gives the number of nodes at that age with the degree
	 * 
	 * @param age The age to consider
	 * @return the degree profile at the given age
	 */
	public int[] findDegreeProfileAtAge(int age) {
		int[] degrees = new int[maximumDegree()+1];
		Arrays.fill(degrees, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			if(getNodeAge(i) == age) {
				int degree = getNodeDegree(i);
				degrees[degree]++;
			}
		}
		return degrees;
	}
	
	
	/**
	 * Finds an array where the each array index gives the number of nodes at that age with the indegree. Indegree is
	 * the number of edges entering the node
	 * 
	 * @param age The age to consider
	 * @return the indegree profile at the given age
	 */
	public int[] findInDegreeProfileAtAge(int age) {
		int[] degrees = new int[maximumInDegree()+1];
		Arrays.fill(degrees, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			if(getNodeAge(i) == age) {
				int degree = getNodeInDegree(i);
				degrees[degree]++;
			}
		}
		return degrees;
	}
	
	
	/**
	 * Finds an array where the each array index gives the number of nodes at that age with the outdegree. Outdegree is
	 * the number of edges leaving the node
	 * 
	 * @param age The age to consider
	 * @return the outdegree profile at the given age
	 */
	public int[] findOutDegreeProfileAtAge(int age) {
		int[] degrees = new int[maximumOutDegree()+1];
		Arrays.fill(degrees, 0);
		for(int i = 0; i < getNumberOfNodes(); i++) {
			if(getNodeAge(i) == age) {
				int degree = getNodeOutDegree(i);
				degrees[degree]++;
			}
		}
		return degrees;
	}
	
	
	/**
	 * populate buckets index with the number of nodes that have degree index
	 * 
	 * @param buckets big enough to hold all degrees, should be at least maxDegree+1
	 * @param degrees from findDegrees
	 */
	public void findDegreeBuckets(int[] buckets, int[] degrees) {
		for(int i = 0; i < degrees.length; i++) {
			buckets[degrees[i]]++;
		}
	}
	

	/**
	 * Outputs a list where each element is a list of parallel edges
	 * 
	 * @return all the parallel edges grouped in lists
	 */
	public ArrayList<ArrayList<Integer>> findParallelEdges() {
		
		HashSet<Integer> foundEdges = new HashSet<Integer>(getNumberOfEdges()*3);
		
		ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
		
		for(int e1 = 0; e1 < getNumberOfEdges()-1; e1++) {
			ArrayList<Integer> duplicateList = new ArrayList<Integer>();
			duplicateList.add(e1);
			if(foundEdges.contains(e1)) {
				continue;
			}
			for(int e2 = e1+1; e2 < getNumberOfEdges(); e2++) {
				if(foundEdges.contains(e2)) {
					continue;
				}
				if(getEdgeNode1(e1) == getEdgeNode1(e2) && getEdgeNode2(e1) == getEdgeNode2(e2)) {
					duplicateList.add(e2);
					foundEdges.add(e2);
				} else if(getEdgeNode2(e1) == getEdgeNode1(e2) && getEdgeNode1(e1) == getEdgeNode2(e2)) {
					duplicateList.add(e2);
					foundEdges.add(e2);
				}
			}
			if(duplicateList.size() > 1) {
				ret.add(duplicateList);
			}
			
		}
		return ret;
	}
	

	/**
	 * 
	 * Generates a new FastGraph with n removed, n must have degree 0.
	 * This is a fast method to use when performance is crucial.
	 * Leaves unused memory in the nodeLabelBuffer.
	 * 
	 * @param n the node to remove
	 * @return if successful, the new fast graph or null if n is connected
	 */
	public FastGraph generateGraphByDeletingSingletonNode(int n) {
		if(this.getNodeDegree(n) != 0) {
			return null;
		}
		
		FastGraph g = new FastGraph(this.numberOfNodes-1,this.numberOfEdges,this.direct);
		// nodeLabelBuf and edgeLabelBuf no longer allocated in init 
		int nodeLabelBufSize = this.nodeLabelBuf.capacity();
		int edgeLabelBufSize = this.edgeLabelBuf.capacity();
		if(!direct) {
			g.nodeLabelBuf = ByteBuffer.allocate(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocate(edgeLabelBufSize);
			// connectionBuf could be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelBufSize);
			// connectionBuf could be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		}

		this.connectionBuf.rewind();
		this.nodeLabelBuf.rewind();
		this.edgeLabelBuf.rewind();

		g.connectionBuf.put(this.connectionBuf);
		// here we just leave the old label in the Buffer, it would be
		// better for memory, but slower, to delete the label from this
		// buffer and reassign all the offsets after the deleted node in the nodeBuf
		g.nodeLabelBuf.put(this.nodeLabelBuf); 
		g.edgeLabelBuf.put(this.edgeLabelBuf);

		this.nodeBuf.rewind();
		// first bit of the nodeBuf remains the same
		int uptoDeletedNodeEnd = n*NODE_BYTE_SIZE;
		byte[] nodes1 = new byte[uptoDeletedNodeEnd];
		this.nodeBuf.get(nodes1, 0, uptoDeletedNodeEnd);

		// this because the offset in get seems to fail for the bulk get
		// it is a way to move the position past the deleted node
		byte[] throwAway = new byte[NODE_BYTE_SIZE];
		this.nodeBuf.get(throwAway, 0, NODE_BYTE_SIZE);
		
		// second bit of the nodeBuf moves down by one node
		int afterDeletedNodeSize = (this.numberOfNodes-(n+1))*NODE_BYTE_SIZE;
		byte[] nodes2 = new byte[afterDeletedNodeSize];
		this.nodeBuf.get(nodes2, 0, afterDeletedNodeSize);
		this.nodeBuf.rewind();
		
		g.nodeBuf.put(nodes1);
		g.nodeBuf.put(nodes2);
		g.nodeBuf.rewind();

		// recalculate connections to nodes that have moved id down by one
		this.edgeBuf.rewind();
		g.edgeBuf.put(this.edgeBuf);
		for(int i = 0; i < g.getNumberOfEdges(); i+= 1) {
			int n1 = g.edgeBuf.getInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE);
			int n2 = g.edgeBuf.getInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE);
			if(n1 > n) {
				n1--;
				g.edgeBuf.putInt(EDGE_NODE1_OFFSET+i*EDGE_BYTE_SIZE,n1);
			}
			if(n2 > n) {
				n2--;
				g.edgeBuf.putInt(EDGE_NODE2_OFFSET+i*EDGE_BYTE_SIZE,n2);
			}
		}
		
		// recalculate node connections to nodes that have moved id down by one
		g.connectionBuf.rewind();
		int i = 4; // start on nodes, the second in the pair
		while(i < g.connectionBuf.capacity()) {
			int node = g.connectionBuf.getInt(i);
			if(node > n) {
				g.connectionBuf.putInt(i,node-1);
			}
			i += CONNECTION_PAIR_SIZE;
		}


		
		g.name = this.name;
		
		return g;
	}
	
	

	/**
	 * 
	 * Generates a new FastGraph with ns added. Ignores the id of ns, the new node will have the
	 * id of the number of nodes in the original graph.
	 * 
	 * @param ns the node to add
	 * @return the new fast graph
	 */
	public FastGraph generateGraphByAddingNode(NodeStructure ns) {
		
		FastGraph g = new FastGraph(this.numberOfNodes+1,this.numberOfEdges,this.direct);
		// nodeLabelBuf and edgeLabelBuf no longer allocated in init 
		int nodeLabelBufSize = this.nodeLabelBuf.capacity()+(ns.getLabel().length()*2);
		if(nodeLabelBufSize*2 > MAX_BYTE_BUFFER_SIZE) {
			throw new OutOfMemoryError("Tried to create a nodeLabelBuf with too many chars");
		}

		int edgeLabelBufSize = this.edgeLabelBuf.capacity();
		if(!direct) {
			g.nodeLabelBuf = ByteBuffer.allocate(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocate(edgeLabelBufSize);
			// connectionBuf could be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelBufSize);
			// connectionBuf could be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocateDirect(this.connectionBuf.capacity());
		}

		this.connectionBuf.rewind();
		this.edgeLabelBuf.rewind();
		this.edgeBuf.rewind();
		g.connectionBuf.rewind();
		g.edgeLabelBuf.rewind();
		g.edgeBuf.rewind();
		g.connectionBuf.put(this.connectionBuf);
		g.edgeLabelBuf.put(this.edgeLabelBuf);
		g.edgeBuf.put(this.edgeBuf);
		
		this.nodeLabelBuf.rewind();
		g.nodeLabelBuf.rewind();
		g.nodeLabelBuf.put(this.nodeLabelBuf);
		
		// place the new label
		int labelStart = this.nodeLabelBuf.capacity();
		String label = ns.getLabel();
		char[] labelArray = label.toCharArray();
		short labelLength = (short)(labelArray.length);
	
		g.nodeLabelBuf.rewind();
		int labelOffset = labelStart;
		for(int j = 0; j < labelArray.length; j++) {
			char c = labelArray[j];
			g.nodeLabelBuf.putChar(labelOffset,c);
			labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
		}

		// place the new node
		this.nodeBuf.rewind();
		g.nodeBuf.put(this.nodeBuf);
		int nodeId = this.numberOfNodes; // the new node is at the end of node list
		g.nodeBuf.putInt(NODE_LABEL_START_OFFSET+nodeId*NODE_BYTE_SIZE,labelStart); // label start
		g.nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+nodeId*NODE_BYTE_SIZE,labelLength); // label size
		g.nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+nodeId*NODE_BYTE_SIZE,0); // offset for inward connecting edges/nodes
		g.nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+nodeId*NODE_BYTE_SIZE,0); // number of inward connecting edges/nodes
		g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+nodeId*NODE_BYTE_SIZE,0); // offset for outward connecting edges/nodes
		g.nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+nodeId*NODE_BYTE_SIZE,0); // number of outward connecting edges/nodes
		g.nodeBuf.putInt(NODE_WEIGHT_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getWeight()); // weight
		g.nodeBuf.put(NODE_TYPE_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getType()); // type
		g.nodeBuf.put(NODE_AGE_OFFSET+nodeId*NODE_BYTE_SIZE,ns.getAge()); // age

		g.name = this.name;
		
		return g;
	}
	

	/**
	 * 
	 * Generates a new FastGraph with e removed.
	 * Leaves unused memory in the edgeLabelBuffer and connectionBuf.
	 * 
	 * @param e the node to remove
	 * @return the new fast graph
	 */
	public FastGraph generateGraphByDeletingEdge(int e) {
		
		FastGraph g = new FastGraph(this.numberOfNodes,this.numberOfEdges-1,this.direct);
		// nodeLabelBuf and edgeLabelBuf no longer allocated in init 
		int nodeLabelBufSize = this.nodeLabelBuf.capacity();
		int edgeLabelBufSize = this.edgeLabelBuf.capacity();
		if(!direct) {
			g.nodeLabelBuf = ByteBuffer.allocate(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocate(edgeLabelBufSize);
			// connectionBuf may be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelBufSize);
			// connectionBuf may be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocateDirect(this.connectionBuf.capacity());
		}

		this.nodeBuf.rewind();
		this.nodeLabelBuf.rewind();
		this.edgeLabelBuf.rewind();

		g.nodeBuf.put(this.nodeBuf);
		g.nodeLabelBuf.put(this.nodeLabelBuf); 
		// here we just leave the old label in the Buffer, it would be
		// better for memory, but slower, to delete the label from this
		// buffer and reassign all the offsets after the deleted node in the nodeBuf
		g.edgeLabelBuf.put(this.edgeLabelBuf);
		
		this.edgeBuf.rewind();
		// first bit of edgeBuf remains the same
		int uptoDeletedEdgeEnd = e*EDGE_BYTE_SIZE;
		byte[] edges1 = new byte[uptoDeletedEdgeEnd];
		this.edgeBuf.get(edges1, 0, uptoDeletedEdgeEnd);

		// this because the offset in get seems to fail for the bulk get
		// it is a way to move the position past the deleted edge
		byte[] throwAway = new byte[EDGE_BYTE_SIZE];
		this.edgeBuf.get(throwAway, 0, EDGE_BYTE_SIZE);
		
		// second bit of edgeBuf moves down by one
		int afterDeletedEdgeSize = (this.numberOfEdges-(e+1))*EDGE_BYTE_SIZE;
		byte[] edges2 = new byte[afterDeletedEdgeSize];
		this.edgeBuf.get(edges2, 0, afterDeletedEdgeSize);
		this.edgeBuf.rewind();

		g.edgeBuf.rewind();
		g.edgeBuf.put(edges1);
		g.edgeBuf.put(edges2);
		g.edgeBuf.rewind();
		
		// change the connections of the nodes at either end of the edge
		// this leaves space at the end of in and out edge connection lists
		// could retrieve this space by moving all other lists, but a lot of
		// node references would need to be changed

		g.connectionBuf.rewind();
		this.connectionBuf.rewind();
		g.connectionBuf.put(this.connectionBuf);
		
		// out edges for node1
		int n1 = this.getEdgeNode1(e);
		int n2 = this.getEdgeNode2(e);

		int outDegree1 = this.getNodeOutDegree(n1);
		int outConnectionOffset1 = g.nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE);
		boolean foundEdge = false;
		for(int i = 0; i < outDegree1; i++) { // just outDegrees, as out is after in
			int edge = g.connectionBuf.getInt(outConnectionOffset1+i*CONNECTION_PAIR_SIZE);
			int node = g.connectionBuf.getInt(outConnectionOffset1+i*CONNECTION_PAIR_SIZE+4);
			if(foundEdge) { // delete the edge and move all other connecting edges down by an edge
				g.connectionBuf.putInt(outConnectionOffset1+(i-1)*CONNECTION_PAIR_SIZE,edge); // edge is first in pair
				g.connectionBuf.putInt(outConnectionOffset1+(i-1)*CONNECTION_PAIR_SIZE+4,node); // node is second in pair
			}
			if(e == edge) {
				foundEdge = true;
			}
		}
		// change out degrees length only as in degree starts list
		g.nodeBuf.putInt(n1*NODE_BYTE_SIZE+NODE_OUT_DEGREE_OFFSET,outDegree1-1);
		
		// in edges for node2
		int inDegree2 = this.getNodeInDegree(n2);
		int outDegree2 = this.getNodeOutDegree(n2);
		int degree2 = inDegree2+outDegree2;
		int inConnectionOffset2 = g.nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE);
		int outConnectionOffset2 = g.nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE);
		foundEdge = false;

//this.outputInternalData();
		for(int i = 0; i < degree2; i++) {
			int edge = g.connectionBuf.getInt(inConnectionOffset2+i*CONNECTION_PAIR_SIZE);
			int node = g.connectionBuf.getInt(inConnectionOffset2+i*CONNECTION_PAIR_SIZE+4);

			if(foundEdge) { // delete the edge and move all other connecting edges down by an edge, this moves all out edge,node pairs
				g.connectionBuf.putInt(inConnectionOffset2+(i-1)*CONNECTION_PAIR_SIZE,edge); // edge is first in pair
				g.connectionBuf.putInt(inConnectionOffset2+(i-1)*CONNECTION_PAIR_SIZE+4,node); // node is second in pair
			}
			if(e == edge) {
				foundEdge = true;
			}
		}

		// change in degree
		g.nodeBuf.putInt(n2*NODE_BYTE_SIZE+NODE_IN_DEGREE_OFFSET,inDegree2-1);
		// length of in degree has changed so out degree start position has changed
		g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE,outConnectionOffset2-CONNECTION_PAIR_SIZE);

		// recalculate node connections to edges that have moved id down by one
		this.connectionBuf.rewind();
		g.connectionBuf.put(this.nodeBuf);
		int i = 0;
		while(i < g.connectionBuf.capacity()) {
			int edge = g.connectionBuf.getInt(i);
			if(edge > e) {
				g.connectionBuf.putInt(i,edge-1);
			}
			i += CONNECTION_PAIR_SIZE;
		}

		
		g.name = this.name;
		
		return g;
	}
	
	
	/**
	 * 
	 * Generates a new FastGraph with es added. Ignores the id of es, the new edge will have the
	 * id of the number of edges in the original graph. Redundant space is added as the
	 * connection list of the attached nodes is recreated and appended to the end of the connectionBuf.
	 * 
	 * @param es the edge to add
	 * @return the new fast graph
	 * @throws Exception Throws if the new FastGraph does not build correctly. Most likely out of memory error.
	 */
	public FastGraph generateGraphByAddingEdge(EdgeStructure es) throws Exception {
		
		if(es.getNode1() >= this.getNumberOfNodes()) {
			throw new FastGraphException("node n1 is greater than the number of nodes");
		}
		if(es.getNode2() >= this.getNumberOfNodes()) {
			throw new FastGraphException("node n2 is greater than the number of nodes");
		}
		
		FastGraph g = new FastGraph(this.numberOfNodes,this.numberOfEdges+1,this.direct);
		// nodeLabelBuf and edgeLabelBuf no longer allocated in init 
		int edgeLabelBufSize = this.edgeLabelBuf.capacity()+(es.getLabel().length()*2);
		if(edgeLabelBufSize*2 > MAX_BYTE_BUFFER_SIZE) {
			throw new OutOfMemoryError("Tried to create an edgeLabelBuf with too many chars");
		}

		int n1 = es.getNode1(); // the source node
		int n2 = es.getNode2(); // the target node
		int nodeLabelBufSize = this.nodeLabelBuf.capacity();
		int connectionBufSize = this.connectionBuf.capacity()+(this.getNodeDegree(n1)+1)*CONNECTION_PAIR_SIZE+(this.getNodeDegree(n2)+1)*CONNECTION_PAIR_SIZE;
		if(!direct) {
			g.nodeLabelBuf = ByteBuffer.allocate(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocate(edgeLabelBufSize);
			// connectionBuf is set to wrong size in init(), it needs extra edges
			g.connectionBuf = ByteBuffer.allocate(connectionBufSize);
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelBufSize);
			// connectionBuf is set to wrong size in init(), it needs extra edges
			g.connectionBuf = ByteBuffer.allocate(connectionBufSize);
		}

		this.nodeLabelBuf.rewind();
		this.nodeBuf.rewind();
		g.nodeLabelBuf.put(this.nodeLabelBuf);
		g.nodeBuf.put(this.nodeBuf);
		
		this.edgeLabelBuf.rewind();
		g.edgeLabelBuf.rewind();
		g.edgeLabelBuf.put(this.edgeLabelBuf);
		
		// place the new label
		int labelStart = this.edgeLabelBuf.capacity();
		String label = es.getLabel();
		char[] labelArray = label.toCharArray();
		short labelLength = (short)(labelArray.length);
	
		g.edgeLabelBuf.rewind();
		int labelOffset = labelStart;
		for(int j = 0; j < labelArray.length; j++) {
			char c = labelArray[j];
			g.edgeLabelBuf.putChar(labelOffset,c);
			labelOffset += 2;  // increment by 2 as it is a char (2 bytes)
		}
		
		int edgeId = this.numberOfEdges; // the new edge is at the end of edge list
		
		// place the new edge
		this.edgeBuf.rewind();
		g.edgeBuf.rewind();
		g.edgeBuf.put(this.edgeBuf);
		g.edgeBuf.putInt(EDGE_LABEL_START_OFFSET+edgeId*EDGE_BYTE_SIZE,labelStart); // label start
		g.edgeBuf.putShort(EDGE_LABEL_LENGTH_OFFSET+edgeId*EDGE_BYTE_SIZE,labelLength); // label size
		g.edgeBuf.putInt(EDGE_NODE1_OFFSET+edgeId*EDGE_BYTE_SIZE,n1); // one end of edge
		g.edgeBuf.putInt(EDGE_NODE2_OFFSET+edgeId*EDGE_BYTE_SIZE,n2); // other end of edge
		g.edgeBuf.putInt(EDGE_WEIGHT_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getWeight()); // weight
		g.edgeBuf.put(EDGE_TYPE_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getType()); // type
		g.edgeBuf.put(EDGE_AGE_OFFSET+edgeId*EDGE_BYTE_SIZE,es.getAge()); // age

		g.name = this.name;
		
		this.connectionBuf.rewind();
		g.connectionBuf.rewind();
		g.connectionBuf.put(this.connectionBuf);
		g.edgeBuf.rewind();
		
		// add new connection elements to the connectionBuf,
		// this leaves the old connections as redundant data
		// but its better than attempting to insert a new edge node pair
		// and changing a large number of node connection offsets
		int connectionOffset = this.nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE);
		int degree = this.getNodeInDegree(n1);
		int[] n1In = new int[degree*2]; // for the edge node pairs
		for(int i = 0; i < degree*2; i++) {
			int offset = connectionOffset+(i*4);
			int id = this.connectionBuf.getInt(offset);
			n1In[i] = id;
		}
		connectionOffset = this.nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE);
		degree = this.getNodeOutDegree(n1);
		int[] n1Out = new int[degree*2+2]; // for the edge node pairs, plus the new one
		for(int i = 0; i < degree*2; i++) {
			int offset = connectionOffset+(i*4);
			int id = this.connectionBuf.getInt(offset);
			n1Out[i] = id;
		}
		n1Out[n1Out.length-2] = edgeId;
		n1Out[n1Out.length-1] = n2;
		connectionOffset = this.nodeBuf.getInt(NODE_IN_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE);
		degree = this.getNodeInDegree(n2);
		int[] n2In = new int[degree*2+2]; // for the edge node pairs, plus the new one
		for(int i = 0; i < degree*2; i++) {
			int offset = connectionOffset+(i*4);
			int id = this.connectionBuf.getInt(offset);
			n2In[i] = id;
		}
		n2In[n2In.length-2] = edgeId;
		n2In[n2In.length-1] = n1;
		connectionOffset = this.nodeBuf.getInt(NODE_OUT_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE);
		degree = this.getNodeOutDegree(n2);
		int[] n2Out = new int[degree*2]; // for the edge node pairs
		for(int i = 0; i < degree*2; i++) {
			int offset = connectionOffset+(i*4);
			int id = this.connectionBuf.getInt(offset);
			n2Out[i] = id;
		}
		g.connectionBuf.rewind();
		g.connectionBuf.put(this.connectionBuf);

		
		if(n1 != n2) {
			int n1InStart = this.connectionBuf.capacity();
			for(int i = 0; i < n1In.length; i++) {
				g.connectionBuf.putInt(n1InStart+i*4,n1In[i]);
			}
			int n1OutStart = n1InStart+n1In.length*4;
			for(int i = 0; i < n1Out.length; i++) {
				g.connectionBuf.putInt(n1OutStart+i*4,n1Out[i]);
			}
			int n2InStart = n1OutStart+n1Out.length*4;
			for(int i = 0; i < n2In.length; i++) {
				g.connectionBuf.putInt(n2InStart+i*4,n2In[i]);
			}
			int n2OutStart = n2InStart+n2In.length*4;
			for(int i = 0; i < n2Out.length; i++) {
				g.connectionBuf.putInt(n2OutStart+i*4,n2Out[i]);
			}

			int n1InDegree = this.getNodeInDegree(n1);
			int n1OutDegree = this.getNodeOutDegree(n1)+1;
			int n2InDegree = this.getNodeInDegree(n2)+1;
			int n2OutDegree = this.getNodeOutDegree(n2);
			g.nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE,n1InStart); // offset for inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+n1*NODE_BYTE_SIZE,n1InDegree); // number of inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE,n1OutStart); // offset for outward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+n1*NODE_BYTE_SIZE,n1OutDegree); // number of outward connecting edges/nodes
			g.nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE,n2InStart); // offset for inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+n2*NODE_BYTE_SIZE,n2InDegree); // number of inward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+n2*NODE_BYTE_SIZE,n2OutStart); // offset for outward connecting edges/nodes
			g.nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+n2*NODE_BYTE_SIZE,n2OutDegree); // number of outward connecting edges/nodes
		} else { // self sourcing edge

			// recall n1 and n2 are the same
			// as we are adding one to both in and out lists of the node
			// the new information is in n2In and n1Out, so use those
			int n1InStart = this.connectionBuf.capacity();
			for(int i = 0; i < n2In.length; i++) {
				g.connectionBuf.putInt(n1InStart+i*4,n2In[i]);
			}
			int n1OutStart = n1InStart+n2In.length*4;
			for(int i = 0; i < n1Out.length; i++) {
				g.connectionBuf.putInt(n1OutStart+i*4,n1Out[i]);
			}

			int n1InDegree = this.getNodeInDegree(n1)+1;
			int n1OutDegree = this.getNodeOutDegree(n1)+1;
			g.nodeBuf.putInt(NODE_IN_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE,n1InStart); 
			g.nodeBuf.putInt(NODE_IN_DEGREE_OFFSET+n1*NODE_BYTE_SIZE,n1InDegree); 
			g.nodeBuf.putInt(NODE_OUT_CONNECTION_START_OFFSET+n1*NODE_BYTE_SIZE,n1OutStart); 
			g.nodeBuf.putInt(NODE_OUT_DEGREE_OFFSET+n1*NODE_BYTE_SIZE,n1OutDegree); 
		}

		return g;
	}
	

	/**
	 * 
	 * Generates a new FastGraph with the label of id changed. Redundant space may be left in or added to
	 * nodeLabelBuf if the new label is not the same size as the old.
	 * 
	 * @param id the node to have a new label
	 * @param label the new label
	 * @return the new graph
	 * @throws Exception Throws if the new FastGraph does not build correctly. Most likely out of memory error.
	 */
	public FastGraph generateGraphByRelabellingNode(int id, String label) throws Exception {
		FastGraph g = new FastGraph(this.numberOfNodes,this.numberOfEdges,this.direct);
		// nodeLabelBuf and edgeLabelBuf no longer allocated in init
		int nodeLabelBufSize = this.nodeLabelBuf.capacity();
		String originalLabel = this.getNodeLabel(id);
		
		if(label.length() > originalLabel.length()) { // if the label is bigger, a new label is needed at the end of the buffer
			nodeLabelBufSize += label.length()*2;
		}
		if(!direct) {
			g.nodeLabelBuf = ByteBuffer.allocate(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocate(edgeLabelBuf.capacity());
			// connectionBuf may be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		} else {
			g.nodeLabelBuf = ByteBuffer.allocateDirect(nodeLabelBufSize);
			g.edgeLabelBuf = ByteBuffer.allocateDirect(edgeLabelBuf.capacity());
			// connectionBuf may be set to wrong size in init()
			g.connectionBuf = ByteBuffer.allocate(this.connectionBuf.capacity());
		}

		this.nodeBuf.rewind();
		this.edgeBuf.rewind();
		this.nodeLabelBuf.rewind();
		this.edgeLabelBuf.rewind();
		this.connectionBuf.rewind();

		g.nodeBuf.rewind();
		g.edgeBuf.rewind();
		g.nodeLabelBuf.rewind();
		g.edgeLabelBuf.rewind();
		g.connectionBuf.rewind();
		
		g.nodeBuf.put(this.nodeBuf);
		g.edgeBuf.put(this.edgeBuf);
		g.nodeLabelBuf.put(this.nodeLabelBuf);
		g.edgeLabelBuf.put(this.edgeLabelBuf);
		g.connectionBuf.put(this.connectionBuf);

		int labelStart = 0;		if(label.length() > originalLabel.length()) { // if the new offset needed, it is the end of the old buffer
			labelStart = this.nodeLabelBuf.capacity();
			g.nodeBuf.putInt(NODE_LABEL_START_OFFSET+id*NODE_BYTE_SIZE,labelStart);
		} else { // label start offset not changed
			labelStart = this.nodeBuf.getInt(NODE_LABEL_START_OFFSET+id*NODE_BYTE_SIZE);
		}

		short labelLength = (short)(label.length());		
		g.nodeBuf.putShort(NODE_LABEL_LENGTH_OFFSET+id*NODE_BYTE_SIZE,labelLength); // label size
		
		for(int i = 0; i < label.length(); i++) {
			int offset = labelStart+i*2;
			g.nodeLabelBuf.putChar(offset,label.charAt(i));
		}
		
		return g;

	}


	/**
	 * @return a list of connected components
	 */
	public ArrayList<FastGraph> breakIntoConnectedComponents() {
		ArrayList<FastGraph> ret = new ArrayList<>();
		Connected c = new Connected();
		
		FastGraph g = this;
		int i = 0;
		while(g.getNumberOfNodes() > 0) {
			
			// first build the new connected component
			c.connected(g);
			LinkedList<Integer> connectedNodes = c.getVisitedNodes();
			
			HashSet<Integer> connectedNodeSet = new HashSet(connectedNodes.size()*3); // to speed up contains test
			ArrayList<NodeStructure> nsList = new ArrayList<>();
			ArrayList<EdgeStructure> esList = new ArrayList<>();
			HashMap<Integer,Integer> nodeMapping = new HashMap<>(); // mapping from old to new node ids

			int id = 0;
			for(Integer n : connectedNodes) {
				NodeStructure ns = g.generateNodeStructure(n);
				ns.setId(id);
				nsList.add(ns);

				nodeMapping.put(n, id);
				connectedNodeSet.add(n);
				id++;
			}

			// this could be done through running through connected edges of connectedNodes
			// but this will do for now
			for(int e = 0; e < g.getNumberOfEdges(); e++) {
				int node1 = g.getEdgeNode1(e);
				int node2 = g.getEdgeNode2(e);
				if(connectedNodeSet.contains(node1)) {
					EdgeStructure es = g.generateEdgeStructure(e);
					es.setNode1(nodeMapping.get(node1));
					es.setNode2(nodeMapping.get(node2));
					esList.add(es);
				}
			}
			FastGraph gSub = structureFactory(g.getName()+"-"+i,g.getGeneration(),nsList,esList,g.getDirect());
			ret.add(gSub);
			
			// now take the rest of the graph and form the subgraph
			
			nsList = new ArrayList<>();
			esList = new ArrayList<>();
			nodeMapping = new HashMap<>(); // mapping from old to new node ids
			id = 0;
			for(int n = 0; n < g.getNumberOfNodes(); n++) {
				if(connectedNodeSet.contains(n)) {
					continue;
				}
				NodeStructure ns = g.generateNodeStructure(n);
				ns.setId(id);
				nsList.add(ns);

				nodeMapping.put(n, id);
				id++;
			}

			for(int e = 0; e < g.getNumberOfEdges(); e++) {
				int node1 = g.getEdgeNode1(e);
				int node2 = g.getEdgeNode2(e);
				if(!connectedNodeSet.contains(node1)) {
					EdgeStructure es = g.generateEdgeStructure(e);
					es.setNode1(nodeMapping.get(node1));
					es.setNode2(nodeMapping.get(node2));
					esList.add(es);
				}
			}

			g = structureFactory(g.getName(),g.getGeneration(),nsList,esList,g.getDirect());
			
			i++;
		}

		return ret;
	}
	
	

	/**
	 * Generates a new graph with every edge having a pair facing the other way. One way to represent
	 * undirected graphs. A simple graph is returned, only one edge in each direction is given. Self sourcing
	 * edges are not duplicated.
	 * 
	 * @return a new graph with duplicated and reversed edges
	 */
	public FastGraph generateByAddingReversedEdges() {
		
		ArrayList<NodeStructure> nodes = new ArrayList<>(getNumberOfNodes());
		for(int n = 0; n < getNumberOfNodes(); n++) {
			NodeStructure ns = generateNodeStructure(n);
			nodes.add(ns);
		}
		
		ArrayList<EdgeStructure> edges = new ArrayList<>(getNumberOfEdges()*2);
		for(int e = 0; e < getNumberOfEdges(); e++) {
			if(!edgeExistsInStructureList(edges,getEdgeNode1(e),getEdgeNode2(e))) {
				EdgeStructure es = generateEdgeStructure(e);
				edges.add(es);
			}
			if(getEdgeNode2(e) != getEdgeNode1(e) && !edgeExistsInStructureList(edges,getEdgeNode2(e),getEdgeNode1(e))) {
				EdgeStructure es = generateEdgeStructure(e);
				es.setNode1(getEdgeNode2(e));
				es.setNode2(getEdgeNode1(e));
				edges.add(es);
			}
		}
		
		FastGraph ret = structureFactory(getName()+"-edgesDoubled",getGeneration(),nodes,edges,getDirect());
		
		return ret;
		
	}
	

	/**
	 * Checks to see if the nodes are already connected in the list
	 * 
	 * @param list the list to check
	 * @param node1 the start node
	 * @param node2 the target node
	 * @return true if the nodes are connected, false otherwise
	 */
	private boolean edgeExistsInStructureList(List<EdgeStructure> list, int node1, int node2) {
		for(EdgeStructure es : list) {
			if(es.getNode1() == node1 && es.getNode2() == node2) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * @param n the node to base the NodeStructure on.
	 * @return the new NodeStructure with the node info.
	 */
	public NodeStructure generateNodeStructure(Integer n) {
		String label = getNodeLabel(n);
		int weight = getNodeWeight(n);
		byte type = getNodeType(n);
		byte age = getNodeAge(n);
		NodeStructure ns = new NodeStructure(n,label,weight,type,age);
		return ns;
	}

	
	/**
	 * 
	 * @param e the edge to base the EdgeStructure on.
	 * @return the new EdgeStructure with the node info.
	 */
	public EdgeStructure generateEdgeStructure(Integer e) {
		String label = getEdgeLabel(e);
		int weight = getEdgeWeight(e);
		byte type = getEdgeType(e);
		byte age = getEdgeAge(e);
		int node1 = getEdgeNode1(e);
		int node2 = getEdgeNode2(e);
		EdgeStructure es = new EdgeStructure(e,label,weight,type,age,node1,node2);
		return es;
	}


	/**
	 * @return the graph information as a human readable String.
	 */
	public String toString() {
		
		StringBuffer sb = new StringBuffer("name: "+name+" number of Nodes: "+numberOfNodes+" number of Edges: "+numberOfEdges+"\n");
		
		for(int i = 0; i < numberOfNodes; i++) {
			sb.append("node: "+i+" label: "+getNodeLabel(i)+" weight: "+getNodeWeight(i)+" type: "+getNodeType(i)+" age: "+getNodeAge(i)+"\n");
		}
		for(int i = 0; i < numberOfEdges; i++) {
			sb.append("edge: "+i+" node1: "+getEdgeNode1(i)+" node2: "+getEdgeNode2(i)+" label: "+getEdgeLabel(i)+" weight: "+getEdgeWeight(i)+" type: "+getEdgeType(i)+" age: "+getEdgeAge(i)+"\n");
		}
		
		return sb.toString();
		
	}


}
