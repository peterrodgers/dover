package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import Jama.*;
import uk.ac.kent.dover.fastGraph.*;

/**
 * 
 * Calculate the BP value for two graphs. Does not consider node labels or node direction.
 * Must have a node mapping set.
 * 
 * Belief Propagation value calculated using the method in:
 * Danai Koutra, Joshua T. Vogelstein, and Christos Faloutsos. "Deltacon: A principled massive-graph similarity function." Proceedings of the 2013 SIAM International Conference on Data Mining. Society for Industrial and Applied Mathematics, 2013.
 * 
 * @author Peter Rodgers
 *
 */
public class BeliefPropagationCalculation {
	
	FastGraph g1;
	FastGraph g2;
	HashMap<Integer,Integer> nodeMapping; // mapping from g1 to g2

	
	HashMap<Integer,Integer> reverseNodeMapping; // mapping from g2 to g1
	
	Matrix I; // identity matrix
	Matrix A1; // adjacency matrix for g1
	Matrix D1; // diagonal degree matrix for g1
	Matrix A2; // adjacency matrix for g2
	Matrix D2; // diagonal degree matrix for g2
	Matrix S1; // pairwise node affinity for g1
	Matrix S2; // pairwise node affinity for g2
	double epsilon; // encodes the influence between neighbours, is between 0 and 1
	int n; // the number of nodes in g1 or g2 (takes the largest)
	
	
	/**
	 * 
	 * @param g1 first graph to compare, must be simple with no self sourcing
	 * @param g2 second graph to compare, must be simple with no self sourcing
	 * @param nodeMapping the nodes in g1 mapped to nodes in g2. This may not be complete if one graph is larger than the other, but all the nodes in the smallest graph must be mapped.
	 */
	public BeliefPropagationCalculation(FastGraph g1, FastGraph g2, HashMap<Integer,Integer> nodeMapping) {
		this.g1 = g1;
		this.g2 = g2;
		this.nodeMapping = nodeMapping;
		
		init();
	}
	
	/**
	 * Set up the matrices for calculation
	 */
	private void init() {
		
		n = g1.getNumberOfNodes();
		if(n < g2.getNumberOfNodes()) {
			n = g2.getNumberOfNodes();
		}
		if(n == 0) {
			return;
		}
		
		// S = [I+e^2D-eA]^-1
		
		I = Matrix.identity(n, n);
		
		A1 = findAMatrix(g1,n,null);
		
		D1 = findDMatrix(g1,n,null);
		
		reverseNodeMapping = new HashMap<>();
		for(int g1Node : nodeMapping.keySet()) {
			int g2Node = nodeMapping.get(g1Node);
			reverseNodeMapping.put(g2Node, g1Node);
		}
		
		A2 = findAMatrix(g2,n,reverseNodeMapping);

		D2 = findDMatrix(g2,n,reverseNodeMapping);

		// epsilon is the influence between neighbours
		int maxDegree1 = g1.maximumDegree();
		int maxDegree2 = g2.maximumDegree();
		int maxDegree = maxDegree1;
		if(maxDegree2 > maxDegree1) {
			maxDegree = maxDegree2;
		}
		
		epsilon = 1.0/(1.0+maxDegree);
		
		S1 = findSMatrix(n,epsilon,I,A1,D1);
		
		S2 = findSMatrix(n,epsilon,I,A2,D2);
		
	}
	
	/**
	 * Calculate affinity matrix. This encodes the pairwise affinity of the nodes.
	 * 
	 * @param n number of columns and rows
	 * @param epsilon neighbour influence
	 * @param I nxn identity matrix
	 * @param A nxn adjacency matrix
	 * @param D nxn diagonal degree matrix
	 * @return affinity matrix
	 */
	private static Matrix findSMatrix(int n, double epsilon, Matrix I, Matrix A, Matrix D) {

		Matrix arg1 = A.times(epsilon*epsilon);
		Matrix arg2 = D.times(epsilon);
		
		Matrix S = I.plus(arg1).minus(arg2);
		S = S.inverse();
		
		return S;
	}


	/**
	 * Create an adjacency matrix from g of size n
	 * @param g the graph from which to populate the matrix
	 * @param n the size of the matrix, must be the same as or bigger than the number of nodes in g
	 * @param nodeMapping a mapping to modify the node indexes
	 * @return the new adjacency matrix
	 */
	private static Matrix findAMatrix(FastGraph g, int n, HashMap<Integer,Integer> nodeMapping) {
		double[][] a = new double[n][n];
		
		for(int e = 0; e < g.getNumberOfEdges(); e++) {
			int node1 = g.getEdgeNode1(e);
			int node2 = g.getEdgeNode2(e);
			if(nodeMapping != null) {
				Integer mapNode1 = nodeMapping.get(node1);
				Integer mapNode2 = nodeMapping.get(node2);
				if(mapNode1 != null) {
					node1 = mapNode1;
				}
				if(mapNode2 != null) {
					node2 = mapNode2;
				}
			}
			
			
			a[node1][node2] = 1;
			a[node2][node1] = 1;
		}
		
		Matrix A = new Matrix(a);
		return A;
	}

	/**
	 * Create an diagonal degree matrix from g of size n
	 * @param g the graph from which to populate the matrix
	 * @param n the size of the matrix, must be the same as or bigger than the number of nodes in g
	 * @param nodeMapping a mapping to modify the node indexes
	 * @return the new matrix with diagonals populated with node degree
	 */
	private static Matrix findDMatrix(FastGraph g, int n, HashMap<Integer,Integer> nodeMapping) {
		double[][] d = new double[n][n];
		
		for(int node = 0; node < g.getNumberOfNodes(); node++) {
			int nodeIndex = node;
			if(nodeMapping != null) {
				Integer mapNode = nodeMapping.get(nodeIndex);
				if(mapNode != null) {
					nodeIndex = mapNode;
				}
			}
			
			int degree = g.getNodeDegree(node);
			d[nodeIndex][nodeIndex] = degree;
		}
		
		Matrix D = new Matrix(d);
		return D;
	}


	/**
	 * 
	 * @return a value between 0 and 1, with 0 being most similar
	 */
	public double similarity() {
		
		if(g1.getNumberOfNodes() == 0 && g2.getNumberOfNodes() == 0) {
			return 0.0;
		}
		
		double d = rootED(n,S1,S2);
		double ret = 1-(1/(1+d)); // differs from the paper, as we want 0 to be similar, 1 to be different
		return ret;
	}
	
	
	/**
	 * Matusita distance between affinity matrices.
	 * 
	 * @param S1 affinity matrix 1
	 * @param S2 affinity matrix 1
	 * @return distance between the matrices
	 */
	private static double rootED(int n, Matrix S1, Matrix S2) {
		
		double ret = 0.0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				double e1 = S1.get(i,j);
				double e2 = S2.get(i,j);
				double eDistance = (e1-e2)*(e1-e2);
				ret += eDistance;
			}
		}
		
		return ret;
	}



}
