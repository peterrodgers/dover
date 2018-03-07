package uk.ac.kent.dover.fastGraph;

import java.util.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * This class holds all the methods associated with adjacency matrices
 * 
 * @author Rob Baker
 *
 */
public class AdjacencyMatrix {
	
	FastGraph g; //the FastGraph
	
	/**
	 * Constructor.
	 * Requires a FastGraph for a callbacks to methods
	 * @param g The FastGraph to be used.
	 */
	public AdjacencyMatrix(FastGraph g) {
		this.g = g;
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
		
		int[][] matrix = new int[g.getNumberOfNodes()][g.getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int n = 0; n < g.getNumberOfNodes(); n++) {
			int[] connectingNodeIndexes = g.getNodeConnectingOutNodes(n);
			for (int i : connectingNodeIndexes) {
				matrix[n][i]++;
				matrix[i][n]++;
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
		
		boolean[][] matrix = new boolean[g.getNumberOfNodes()][g.getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		for (int n = 0; n < g.getNumberOfNodes(); n++) {
			int[] connectingNodeIndexes = g.getNodeConnectingOutNodes(n);
			for (int i : connectingNodeIndexes) {
				matrix[n][i] = true;
				matrix[i][n] = true;
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
		
		int[][] matrix = new int[g.getNumberOfNodes()][g.getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int n = 0; n < g.getNumberOfNodes(); n++) {
			int[] connectingNodeIndexes = g.getNodeConnectingOutNodes(n);
			for (int i : connectingNodeIndexes) {
				matrix[n][i]++;
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
		
		boolean[][] matrix = new boolean[g.getNumberOfNodes()][g.getNumberOfNodes()]; //create an 2D array that has the dimensions of the current graph 
		
		for (int n = 0; n < g.getNumberOfNodes(); n++) {
			int[] connectingNodeIndexes = g.getNodeConnectingOutNodes(n);
			for (int i : connectingNodeIndexes) {
				matrix[n][i] = true;
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
		double[] result = e.getRealEigenvalues();
		Arrays.sort(result);
		return result;	
	}
}
