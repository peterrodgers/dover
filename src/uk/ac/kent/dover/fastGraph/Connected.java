package uk.ac.kent.dover.fastGraph;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;

public class Connected {
	
	
	/**
	 *
	 * Create a Connected before running connected.
	 *
	 */
	public Connected() {

	}
	

	
	
	/** Breadth first search through the graph.
	 * Note direct access to connectionBuf is a 3x speed up over accessing getNodeConnectingNodes(currentNode).
	 * Using arrays for nodeFlagBuf is a minor speed up on ByteBuffer
	 * 
	 * @return true if the graph is connected, false otherwise. Empty graphs are connected.
	 */
	public boolean connected(FastGraph g) {

		int numberOfNodes = g.getNumberOfNodes();
		
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
			
			int connectionOffset = g.getNodeBuf().getInt(FastGraph.NODE_IN_CONNECTION_START_OFFSET+currentNode*FastGraph.NODE_BYTE_SIZE);
			int degree = g.getNodeDegree(currentNode);
			for(int i = 0; i < degree; i++) {
//edgeCount++;
				// step over edge/node pairs and the edge
				int nodeOffset = FastGraph.CONNECTION_NODE_OFFSET+connectionOffset+i*FastGraph.CONNECTION_PAIR_SIZE;
				int connectingNode = g.getConnectionBuf().getInt(nodeOffset);
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



}
