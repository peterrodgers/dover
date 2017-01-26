package test.uk.ac.kent.dover;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import test.uk.ac.kent.dover.fastGraph.*;

public class TestRunner {
	public static void main(String[] args) {
		Result result;
		
		result = JUnitCore.runClasses(FastGraphTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());

		result = JUnitCore.runClasses(ExactIsomorphismTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
		
		result = JUnitCore.runClasses(AdjacencyMatrixTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
		
		result = JUnitCore.runClasses(InducedSubgraphTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());

		result = JUnitCore.runClasses(ExactMotifFinderTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
		
		result = JUnitCore.runClasses(ExactSubgraphIsomorphismTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());

		result = JUnitCore.runClasses(ApproximateSubgraphIsomorphismTest.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
		
		
   }
	
	public static String get0Node0Edge() {
		String json = "{\n";
		json += "\"name\": \"empty\",\n";
		json += "\"nodes\": [],\n";
		json += "\"edges\": []\n";
		json += "}\n";
		return json;
	}
	
	public static  String get1Node0Edge() {
	
		String json = "{\n";
		json += "\"name\": \"one node, no edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"12\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"-1\"\n";
		json += "}\n";
		json += "],\n";
		json += "\"edges\": []\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get0Node0EdgeAdjMatrix() {
		return new int[][]{{0}};
	}
	
	public static  String get2Node0Edge() {
		String json = "{\n";
		json += "\"name\": \"two nodes, no edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"12\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"-1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"6\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"4\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": []\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get2Node0EdgeAdjMatrix() {
		return new int[][]{{0,0},{0,0}};
	}

	public static  String get2Node1Edge() {
		String json = "{\n";
		json += "\"name\": \"two nodes, one edge\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"12\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"-1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"6\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"6\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"43\",\n";
		json += "\"edgeType\": \"6\",\n";
		json += "\"edgeAge\": \"-1\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get2Node1EdgeAdjMatrix() {
		return new int[][]{{0,1},{1,0}};
	}
	
	public static  String get2Node2Edge() {
		String json = "{\n";
		json += "\"name\": \"two nodes, two edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"12\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"-1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"6\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"6\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"43\",\n";
		json += "\"edgeType\": \"6\",\n";
		json += "\"edgeAge\": \"-1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"0\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"32\",\n";
		json += "\"edgeType\": \"9\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get2Node2EdgeAdjMatrix() {
		return new int[][]{{2,1},{1,0}};
	}
	
	/*
	 * 0-1
	 * |/
	 * 2-3-4
	 */
	public static  String get5Node5Edge() {
	
		String json = "{\n";
		json += "\"name\": \"five nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"31\",\n";
		json += "\"nodeType\": \"32\",\n";
		json += "\"nodeAge\": \"33\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"44\",\n";
		json += "\"edgeType\": \"45\",\n";
		json += "\"edgeAge\": \"46\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get5Node5EdgeAdjMatrix() {
		return new int[][]{{0,1,1,0,0},{1,0,1,0,0},{1,1,0,1,0},{0,0,1,0,1},{0,0,0,1,0}};
	}
	
	/*
	 * Disconnected
	 * 0-1
	 * |/
	 * 2   3-4
	 */
	public static  String get5Node4Edge() {
	
		String json = "{\n";
		json += "\"name\": \"five nodes, four edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"4\",\n";
		json += "\"nodeType\": \"4\",\n";
		json += "\"nodeAge\": \"4\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get5Node4EdgeAdjMatrix() {
		return new int[][]{{0,1,1,0,0},{1,0,1,0,0},{1,1,0,0,0},{0,0,0,0,1},{0,0,0,1,0}};
	}

	/*
	 * 0-1
	 * |/|
	 * 2-3
	 */
	public static  String get4Node5Edge() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	public static int[][] get4Node5EdgeAdjMatrix() {
		return new int[][]{{0,1,1,0},{1,0,1,1},{1,1,0,1},{0,1,1,0}};
	}
	

	/*
	 * 0-1\
	 * |/| 4
	 * 2-3/
	 */
	public static String get5Node7EdgeA() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	/*
	 * 0-1\
	 * |/| 4
	 * 2-3/
	 */
	public static String get5Node7EdgeB() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node 3\",\n";
		json += "\"nodeWeight\": \"31\",\n";
		json += "\"nodeType\": \"32\",\n";
		json += "\"nodeAge\": \"33\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge 0\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"0\",\n";
		json += "\"edgeLabel\": \"edge 2\",\n";
		json += "\"edgeWeight\": \"25\",\n";
		json += "\"edgeType\": \"26\",\n";
		json += "\"edgeAge\": \"27\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"4\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	/*  /--4
	 * 0-1 |
	 * |/| | 
	 * 2-3/
	 */
	public static String get5Node7EdgeC() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
}