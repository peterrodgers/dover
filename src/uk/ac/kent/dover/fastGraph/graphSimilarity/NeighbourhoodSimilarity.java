package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;
import uk.ac.kent.dover.fastGraph.editOperation.EditOperation;

/**
 * 
 * Code based on https://wadsashika.wordpress.com/2014/09/19/measuring-graph-similarity-using-neighbor-matching/
 * Based on research paper:
 * Mladen Nikolić. 2012. Measuring similarity of graph nodes by neighbor matching. Intell. Data Anal. 16, 6 (November 2012), 865-878. DOI=http://dx.doi.org/10.3233/IDA-2012-00556
 * 
 * @author Peter Rodgers
 *
 */
public class NeighbourhoodSimilarity extends GraphSimilarity {

	double[][] similarity;
	double[][] inSimilarity;
	double[][] outSimilarity;
	double epsilon = 0.0001;

	
	public static void main(String [] args) throws FastGraphException {
		
		Debugger.enabled = false;
		
temp();		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		edges1.add(es10);
		edges1.add(es11);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
//		nodes2.add(ns24);
//		nodes2.add(ns25);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
//		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 1, 3);
//		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 1, 4);
//		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 2, 3);
//		EdgeStructure es24 = new EdgeStructure(4,"es24", 0, (byte)0, (byte)0, 3, 4);
//		EdgeStructure es25 = new EdgeStructure(5,"es25", 0, (byte)0, (byte)0, 4, 5);
		edges2.add(es20);
//		edges2.add(es21);
//		edges2.add(es22);
//		edges2.add(es23);
//		edges2.add(es24);
//		edges2.add(es25);
		List<NodeStructure> nodes3 = new ArrayList<NodeStructure>();
		NodeStructure ns30 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns31 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns32 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns33 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns34 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns35 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes3.add(ns30);
		nodes3.add(ns31);
		nodes3.add(ns32);
		nodes3.add(ns33);
//		nodes3.add(ns34);
//		nodes3.add(ns35);
		List<EdgeStructure> edges3 = new ArrayList<EdgeStructure>();
		EdgeStructure es30 = new EdgeStructure(0,"es30", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es31 = new EdgeStructure(1,"es31", 0, (byte)0, (byte)0, 1, 3);
		EdgeStructure es32 = new EdgeStructure(2,"es32", 0, (byte)0, (byte)0, 1, 4);
		EdgeStructure es33 = new EdgeStructure(3,"es33", 0, (byte)0, (byte)0, 2, 3);
		EdgeStructure es34 = new EdgeStructure(4,"es34", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es35 = new EdgeStructure(5,"es35", 0, (byte)0, (byte)0, 4, 5);
		EdgeStructure es36 = new EdgeStructure(6,"es36", 0, (byte)0, (byte)0, 5, 0);
		edges3.add(es30);
		edges3.add(es31);
//		edges3.add(es32);
//		edges3.add(es33);
//		edges3.add(es34);
//		edges3.add(es35);
//		edges3.add(es36);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		FastGraph g3 = FastGraph.structureFactory("g3", (byte)0, nodes3, edges3, false);
		
		NeighbourhoodSimilarity ns1 = new NeighbourhoodSimilarity(false);
		double ret = ns1.similarity(g2,g3);
		System.out.println(ret);
		ret = ns1.similarity(g3,g2);
		System.out.println(ret);
		

	}
	
	
private static void temp() throws FastGraphException {
//	testByRandomGraphLoop();

	int startNodes = 10;
	int nodes = startNodes;
	while(true) {
		double ret;
		boolean directed = true;
		FastGraph g1,g2;
		NeighbourhoodSimilarity ns;
		
		int edges = nodes*5;
		
		g1 = FastGraph.randomGraphFactory(nodes, edges, 777777, false);
		
		g2 = FastGraph.randomGraphFactory(nodes, edges, 557755, false);
		
		long start2 = System.currentTimeMillis();
		ns = new NeighbourhoodSimilarity(directed);
		ret = ns.similarity(g1, g2);
		System.out.println("NEIGHBOURHOOD SIMILARITY nodes "+nodes+" edges "+edges+" directed "+directed);
		System.out.println("similarity time "+(System.currentTimeMillis()-start2)/1000.0);
		System.out.println("cost g1 g2: "+ret);
		
		ret = ns.similarity(g2, g1);
		System.out.println("cost g2 g1: "+ret);
		
		nodes = nodes + startNodes;

	}
}

	/**
	* defaults to treating graph as undirected,
	* with all edit costs at 1.0.
	* 
	* @throws FastGraphException should not throw this as all required edit operations are present by default
	*/
	public NeighbourhoodSimilarity() {
		super();
		this.directed = false;
	}


	/**
	* editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	* Defaults to no attempted node map swaps
	* 
	* @param directed true if the graph is treated as directed, false if undirected
	*/
	public NeighbourhoodSimilarity(boolean directed) {
		super();
		this.directed = directed;
	}


	public double getEpsilon() {return epsilon;}

	public void setEpsilon(double epsilon) {this.epsilon = epsilon;}


	/**
	* This returns an the neighbourhood similarity between the two graphs. Values between 0 and 1, with
	* 0 being isomorphic and 1 being entirely dissimilar.
	* 
	* @param g1 the first graph to be compared.
	* @param g2 the second graph to be compared.
	* @return the cost of the edits between two graphs.
	*/
	@Override
	public double similarity(FastGraph inG1, FastGraph inG2) {

		
		FastGraph g1 = inG1;
		FastGraph g2 = inG2;
		
		
		if(g1.getNumberOfNodes() == 0 && g2.getNumberOfNodes() == 0) {
			return 0.0;
		}
		
		if(g1.getNumberOfNodes() == 0) {
			return 1.0;
		}
		
		if(g2.getNumberOfNodes() == 0) {
			return 1.0;
		}
		
		if(directed) {
			g1 = inG1.generateByAddingReversedEdges();
			g2 = inG2.generateByAddingReversedEdges();
		}
		
		initMatrices(g1,g2);
		
		calculateSimilarity(g1,g2);
		
		int[] g1NodeArray = new int[g1.getNumberOfNodes()];
		for(int i = 0; i < g1NodeArray.length; i++) {
			g1NodeArray[i] = i;
		}
		int[] g2NodeArray = new int[g2.getNumberOfNodes()];
		for(int i = 0; i < g2NodeArray.length; i++) {
			g2NodeArray[i] = i;
		}

		double difference = 0.0;
		if(g1NodeArray.length < g2NodeArray.length) {
			difference = enumerate(g1NodeArray, g2NodeArray, 0) / g1NodeArray.length;
		} else {
			difference = enumerate(g2NodeArray, g1NodeArray, 1) / g2NodeArray.length;
		}
		
		double ret = 1-difference;
		
		return ret;
		
	}
		
	/**
	* Initialize the similarity matrices.
	* 
	* @param g1 the first graph to be compared
	* @param g2 the second graph to be compared
	*/
	protected void initMatrices(FastGraph g1, FastGraph g2) {
		int g1Nodes = g1.getNumberOfNodes();
		int g2Nodes = g2.getNumberOfNodes();
		
		this.similarity = new double[g1Nodes][g2Nodes];
		this.inSimilarity = new double[g1Nodes][g2Nodes];
		this.outSimilarity = new double[g1Nodes][g2Nodes];

		for(int i = 0; i < g1Nodes; i++) {
			for(int j = 0; j < g2Nodes; j++) {
				
				// in similarity
				double maxInDegree = g1.getNodeInDegree(i);
				if(g2.getNodeInDegree(j) > maxInDegree) {
					maxInDegree = g2.getNodeInDegree(j);
				}
				if(maxInDegree != 0) {
					// min degree divided by max degree
					double inVal = g1.getNodeInDegree(i);
					if(g2.getNodeInDegree(j) < maxInDegree) {
						inVal = g2.getNodeInDegree(j);
					}
					inVal = inVal/maxInDegree;
					inSimilarity[i][j] = inVal;
				} else {
					inSimilarity[i][j] = 0.0;
				}

				// out similarity
				double maxOutDegree = g1.getNodeOutDegree(i);
				if(g2.getNodeOutDegree(j) > maxOutDegree) {
					maxOutDegree = g2.getNodeOutDegree(j);
				}
				if(maxOutDegree != 0) {
					// min degree divided by max degree
					double outVal = g1.getNodeOutDegree(i);
					if(g2.getNodeOutDegree(j) < maxOutDegree) {
						outVal = g2.getNodeOutDegree(j);
					}
					outVal = outVal/maxOutDegree;
					outSimilarity[i][j] = outVal;
				} else {
					outSimilarity[i][j] = 0.0;
				}
			}
		}

		for(int i = 0; i < g1Nodes; i++) {
			for(int j = 0; j < g2Nodes; j++) {
				similarity[i][j] =(inSimilarity[i][j] + outSimilarity[i][j]) / 2;
			}
		}
		
	}
	

	/**
	 * Find the similarity.
	 * 
	 * @param g1 first graph
	 * @param g2 second graph
	 */
	private void calculateSimilarity(FastGraph g1, FastGraph g2) {
		double maxDifference = 0.0;
		boolean finish = false;
		
		int g1Nodes = g1.getNumberOfNodes();
		int g2Nodes = g2.getNumberOfNodes();

		while(!finish) {
				maxDifference = 0.0;
				for(int i = 0; i < g1Nodes; i++) {
					for(int j = 0; j < g2Nodes; j++) {

						// in degree
						double similaritySum = 0.0;
						double maxInDegree = g1.getNodeInDegree(i);
						if(g2.getNodeInDegree(j) > maxInDegree) {
							maxInDegree = g2.getNodeInDegree(j);
						}
						double minInDegree = g1.getNodeInDegree(i);
						if(g2.getNodeInDegree(j) < maxInDegree) {
							minInDegree = g2.getNodeInDegree(j);
						}
						
						if(minInDegree == g1.getNodeInDegree(i)) {
							similaritySum = enumerate(g1.getNodeConnectingInNodes(i),g2.getNodeConnectingInNodes(j),0);
						} else {
							similaritySum = enumerate(g2.getNodeConnectingInNodes(j),g1.getNodeConnectingInNodes(i),1);
						}
						
						if(maxInDegree == 0.0 && similaritySum == 0.0) {
							inSimilarity[i][j] = 1.0;
						} else if(maxInDegree == 0.0) {
							inSimilarity[i][j] = 0.0;
						} else {
							inSimilarity[i][j] = similaritySum / maxInDegree;
						}

						// out degree
						similaritySum = 0.0;
						double maxOutDegree = g1.getNodeOutDegree(i);
						if(g2.getNodeOutDegree(j) > maxOutDegree) {
							maxOutDegree = g2.getNodeOutDegree(j);
						}
						double minOutDegree = g1.getNodeOutDegree(i);
						if(g2.getNodeOutDegree(j) < maxOutDegree) {
							minOutDegree = g2.getNodeOutDegree(j);
						}
						
						if(minOutDegree == g1.getNodeOutDegree(i)) {
							similaritySum = enumerate(g1.getNodeConnectingOutNodes(i),g2.getNodeConnectingOutNodes(j),0);
						} else {
							similaritySum = enumerate(g2.getNodeConnectingOutNodes(j),g1.getNodeConnectingOutNodes(i),1);
						}
						
						if(maxOutDegree == 0.0 && similaritySum == 0.0) {
							outSimilarity[i][j] = 1.0;
						} else if(maxOutDegree == 0.0) {
							outSimilarity[i][j] = 0.0;
						} else {
							outSimilarity[i][j] = similaritySum / maxOutDegree;
						}

					}
				}

				for(int i = 0; i < g1Nodes; i++) {
					for(int j = 0; j < g2Nodes; j++) {
						double temp =(inSimilarity[i][j] + outSimilarity[i][j]) / 2;
						if(Math.abs(similarity[i][j] - temp) > maxDifference) {
								maxDifference = Math.abs(similarity[i][j] - temp);
						}
						similarity[i][j] = temp;
					}
				}

				if(maxDifference < epsilon) {
					finish = true;
				}
		}
	}

	
	/**
	 * Enumeration part of the algorithm
	 * 
	 * @param minArray minimum values
	 * @param maxArray maximum valudes
	 * @param graph 0 for standard direction, 1 for reverse direction
	 * @return enumeration value
	 */
	public double enumerate(int[] minArray, int[]maxArray, int graph) {
		double similaritySum = 0.0;
		
		HashMap<Integer,Double> valueMap = new HashMap<Integer,Double>();
		if(graph == 0) {
				for(int i = 0; i < minArray.length; i++) {
					int node = minArray[i];
					double max = 0.0;
					int maxIndex = -1;
					for(int j = 0; j < maxArray.length; j++) {
						int key = maxArray[j];
						if(!valueMap.containsKey(key)) {
								if(max < similarity[node][key]) {
									max = similarity[node][key];
									maxIndex = key;
								}
						}
					}
					valueMap.put(maxIndex, max);
				}
		} else {
				for(int i = 0; i < minArray.length; i++) {
					int node = minArray[i];
					double max = 0.0;
					int maxIndex = -1;
					for(int j = 0; j < maxArray.length; j++) {
						int key = maxArray[j];
						if(!valueMap.containsKey(key)) {
								if(max < similarity[key][node]) {
									max = similarity[key][node];
									maxIndex = key;
								}
						}
					}
					valueMap.put(maxIndex, max);
				}
		}

		for(double value : valueMap.values()) {
				similaritySum += value;
		}
		return similaritySum;
	}

	
	
}

