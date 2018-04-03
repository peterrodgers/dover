package uk.ac.kent.dover.fastGraph;

import java.util.*;

/**
*
* Class to find a random trail in the graph. A trail uses edges only once but can reuse nodes.
*
*/
public class RandomTrail {
	
	private boolean directed;
	
	private Random random;
	
	
	public static void main(String [] args) throws Exception {
		
		Debugger.enabled = false;
		
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrail rt;
		ArrayList<TrailNode> ret;
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 0));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 0, 1));
		g1 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rt = new RandomTrail(false, 666);
		
		ret = rt.findTrail(g1, 0, 4);
		System.out.println(g1);
		System.out.println(ret);
		
	}


	
	/**
	 *
	 * Create a RandomTrail before running findTrail.
	 *
	 * @param directed if true treat the edges as directed, otherwise travel along them in either direction
	 */
	public RandomTrail(boolean directed) {
		this.random = new Random(System.currentTimeMillis());
		this.directed = directed;
	}
	
	/**
	 *
	 * Create a RandomTrail before running findTrail.
	 *
	 * @param directed if true treat the edges as directed, otherwise travel along them in either direction
	 * @param seeds allows testing of known seed for the random branch finder.
	 */
	public RandomTrail(boolean directed, long seed) {
		this.random = new Random(seed);
		this.directed = directed;
	}

	
	/** 
	 *
	 * @param g The FastGraph to test
	 * @param startNode the node to start the search at, must be an id in g
	 * @param length the size of trail in nodes to return
	 * 
	 * @return the TrailNode list returns the trail, with number of nodes given by length, or shorter if a dead end is reached.
	 * @throws FastGraphException if the node id is not in the graph
	 */
	public ArrayList<TrailNode> findTrail(FastGraph g, int startNode, int length) throws FastGraphException {
		
		if(g.getNumberOfNodes()-1 < startNode) {
			throw new FastGraphException("start node not found in graph");
		}
		
		ArrayList<TrailNode> trail = new ArrayList<>(length+1);
		
		if(length == 0) {
			return trail;
		}

		HashSet<Integer> visitedNodes = new HashSet<Integer>(length*3);
		HashSet<Integer> visitedEdges = new HashSet<Integer>(length*3);
		
		TrailNode startTN = new TrailNode(0,startNode,-1);
		trail.add(startTN);
		visitedNodes.add(startNode);

		int[] neighbourEdgesArray;
		int currentNode = startNode;
		for(int pos = 1; pos < length; pos++) {
			if(directed) {
				neighbourEdgesArray = g.getNodeConnectingOutEdges(currentNode);
			} else {
				neighbourEdgesArray = g.getNodeConnectingEdges(currentNode);
			}
			ArrayList<Integer> neighbourEdgesList = new ArrayList<Integer>(neighbourEdgesArray.length+1);
			for(int i = 0; i < neighbourEdgesArray.length;i++) {
				int e = neighbourEdgesArray[i];
				if(!visitedEdges.contains(e)) {
					neighbourEdgesList.add(e);
				}
			}

			// no available neighbours, so dead end
			if(neighbourEdgesList.size() == 0) {
				break;
			}
			
			int ePos = random.nextInt(neighbourEdgesList.size());
			
			int edge = neighbourEdgesList.get(ePos);
			
			int node = g.oppositeEnd(edge, currentNode);

			// test to see if the node is already in the trail
			// if so, set it to the first position it is found in
			int duplicatePosition = -1;
			if(visitedNodes.contains(node)) {
				for(TrailNode tn : trail) {
					if(tn.getNode() == node) {
						duplicatePosition = tn.getPosition();
						break;
					}
				}
			}

			TrailNode tn = new TrailNode(pos,node,duplicatePosition);
			trail.add(tn);
			
			visitedEdges.add(edge);
			visitedNodes.add(node);
			
			currentNode = node;
			
		}
		return trail;
		
	}




}
