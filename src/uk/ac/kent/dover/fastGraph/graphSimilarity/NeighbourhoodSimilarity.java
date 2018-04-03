package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;

/**
 * 
 * Code based on https://wadsashika.wordpress.com/2014/09/19/measuring-graph-similarity-using-neighbor-matching/
 * Based on research paper:
 * Mladen Nikolić. 2012. Measuring similarity of graph nodes by neighbor matching. Intell. Data Anal. 16, 6 (November 2012), 865-878. DOI=http://dx.doi.org/10.3233/IDA-2012-00556
 * 
 * Use with extreme caution, if at all. There are issues
 * <ul>
 * <li> similarity(g1,g2) is often not equal to similarity(g2,g1)</li>
 * <li> The undirected version is close to 1 very easily. Its probably worth recoding the undirected version more finely, rather than just doubling the edges</li>
 * </ul>
 * 
 * 
 * @author Peter Rodgers
 *
 */
public class NeighbourhoodSimilarity extends GraphSimilarity {

	double[][] similarity;
	double[][] inSimilarity;
	double[][] outSimilarity;
	double epsilon = 0.0001;

	

	/**
	* defaults to treating graph as undirected,
	* with all edit costs at 1.0.
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
	* @param inG1 the first graph to be compared.
	* @param inG2 the second graph to be compared.
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

