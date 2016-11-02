package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import uk.ac.kent.displayGraph.*;
import uk.ac.kent.dover.fastGraph.*;

public class ExactIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(0,0,1,false);
		g2 = FastGraph.randomGraphFactory(0,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(1,0,1,false);
		g2 = FastGraph.randomGraphFactory(1,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(2,0,1,false);
		g2 = FastGraph.randomGraphFactory(2,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

	}

	
	@Test
	public void test002() {
		ExactIsomorphism ei;
		FastGraph g2;
		FastGraph g1;
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

	}
	
	
	
	@Test
	public void test003() throws Exception {
		FastGraph g2;
		FastGraph g1;

		
		g1 = FastGraph.randomGraphFactory(12,30,111,true);
		g2 = FastGraph.randomGraphFactory(12,30,111,true);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
		g1 = FastGraph.randomGraphFactory(12,30,111,false);
		g2 = FastGraph.randomGraphFactory(12,30,112,false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

	}

	
	
	@Test
	public void test004() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		ei = new ExactIsomorphism(g1);
		
		g2 = FastGraph.randomGraphFactory(10,25,1,false);
		assertFalse(ei.isomorphic(g2));

		g2 = FastGraph.randomGraphFactory(12,20,1,false);
		assertFalse(ei.isomorphic(g2));

		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		assertTrue(ei.isomorphic(g2));

		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		assertFalse(ei.isomorphic(g2));

	}

	@Test
	public void test005() {
		// problem with randomly generated graph. ok with display graph algorithm, failed with fastgraph algorithm
		/* g1
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n2:n2,e0), (n0:n0,e1), (n2:n2,e2), (n1:n2,e3), (n1:n0,e4)]
		 * g2
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n2:n1,e0), (n1:n1,e1), (n1:n1,e2), (n0:n0,e3), (n2:n0,e4)]
		 * 
		 * Isomorphic. [<n0,n0>;<n1,n2>;<n2,n1>]
		 */
		
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		Edge e0 = new Edge(n2, n2, "e0");
		Edge e1 = new Edge(n0, n0, "e1");
		Edge e2 = new Edge(n2, n2, "e2");
		Edge e3 = new Edge(n1, n2, "e3");
		Edge e4 = new Edge(n1, n0, "e4");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		e0 = new Edge(n2, n1, "e0");
		e1 = new Edge(n1, n1, "e1");
		e2 = new Edge(n1, n1, "e2");
		e3 = new Edge(n0, n0, "e3");
		e4 = new Edge(n2, n0, "e4");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		
		assertTrue(dg1.isomorphic(dg2));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		int[] mapping = {0,2,1};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));

		
	}
	

	@Test
	public void test006() {
		// problem with randomly generated graph. ok with display graph algorithm, failed with fastgraph algorithm
		/* g1
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n2:n0,e0), (n0:n1,e1), (n0:n2,e2), (n0:n1,e3), (n2:n2,e4)]
		 * g2
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n0:n0,e0), (n2:n0,e1), (n2:n0,e2), (n2:n1,e3), (n2:n1,e4)]
		 * 
		 * Isomorphic. [<n0,n2>;<n1,n1>;<n2,n0>]
		 */
		
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		Edge e0 = new Edge(n2, n0, "e0");
		Edge e1 = new Edge(n0, n1, "e1");
		Edge e2 = new Edge(n0, n2, "e2");
		Edge e3 = new Edge(n0, n1, "e3");
		Edge e4 = new Edge(n2, n2, "e4");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		e0 = new Edge(n0, n0, "e0");
		e1 = new Edge(n2, n0, "e1");
		e2 = new Edge(n2, n0, "e2");
		e3 = new Edge(n2, n1, "e3");
		e4 = new Edge(n2, n1, "e4");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		
		assertTrue(dg1.isomorphic(dg2));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	@Test
	public void test007() {
		// problem with displayGraph isomorphism test generated graph. failed with display graph algorithm, ok with fastgraph algorithm
		/* g1
		 * Nodes:[n0, n1, n2, n3]
		 * Edges:[(n1:n2,e0), (n2:n3,e1), (n1:n1,e2), (n3:n3,e3), (n0:n3,e4)]
		 * g2
		 * Nodes:[n0, n1, n2, n3]
		 * Edges:[(n2:n3,e0), (n0:n3,e1), (n1:n3,e2), (n0:n1,e3), (n0:n3,e4)]
		 * Not isomorphic. [<n0,n2>;<n1,n1>;<n2,n0>]
		 */
		
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		dg1.addNode(n3);
		Edge e0 = new Edge(n1, n2, "e0");
		Edge e1 = new Edge(n2, n3, "e1");
		Edge e2 = new Edge(n1, n1, "e2");
		Edge e3 = new Edge(n3, n3, "e3");
		Edge e4 = new Edge(n0, n3, "e4");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		dg2.addNode(n3);
		e0 = new Edge(n2, n3, "e0");
		e1 = new Edge(n0, n3, "e1");
		e2 = new Edge(n1, n3, "e2");
		e3 = new Edge(n0, n1, "e3");
		e4 = new Edge(n0, n3, "e4");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		
//		assertFalse(dg1.isomorphic(dg2)); // bug in displayGraph
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
	}

	
	@Test
	public void test008() {
		
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		dg1.addNode(n3);
		dg1.addNode(n4);
		Edge e0 = new Edge(n1, n4, "e0");
		Edge e1 = new Edge(n1, n0, "e1");
		Edge e2 = new Edge(n2, n1, "e2");
		Edge e3 = new Edge(n0, n2, "e3");
		Edge e4 = new Edge(n0, n3, "e4");
		Edge e5 = new Edge(n1, n3, "e5");
		Edge e6 = new Edge(n4, n3, "e6");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		dg1.addEdge(e5);
		dg1.addEdge(e6);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		dg2.addNode(n3);
		dg2.addNode(n4);
		e0 = new Edge(n2, n1, "e0");
		e1 = new Edge(n2, n4, "e1");
		e2 = new Edge(n1, n4, "e2");
		e3 = new Edge(n0, n4, "e3");
		e4 = new Edge(n0, n1, "e4");
		e5 = new Edge(n0, n3, "e5");
		e6 = new Edge(n2, n3, "e6");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		dg2.addEdge(e5);
		dg2.addEdge(e6);
		
		assertFalse(dg1.isomorphic(dg2));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
	}
	
	
	@Test
	public void test009() {
		
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		dg1.addNode(n3);
		dg1.addNode(n4);
		Edge e0 = new Edge(n1, n4, "e0");
		Edge e1 = new Edge(n1, n0, "e1");
		Edge e2 = new Edge(n2, n1, "e2");
		Edge e3 = new Edge(n0, n2, "e3");
		Edge e4 = new Edge(n0, n3, "e4");
		Edge e5 = new Edge(n1, n3, "e5");
		Edge e6 = new Edge(n4, n3, "e6");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		dg1.addEdge(e5);
		dg1.addEdge(e6);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		dg2.addNode(n3);
		dg2.addNode(n4);
		e0 = new Edge(n2, n1, "e0");
		e1 = new Edge(n2, n0, "e1");
		e2 = new Edge(n1, n4, "e2");
		e3 = new Edge(n0, n4, "e3");
		e4 = new Edge(n0, n1, "e4");
		e5 = new Edge(n0, n3, "e5");
		e6 = new Edge(n2, n3, "e6");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		dg2.addEdge(e5);
		dg2.addEdge(e6);
		
		assertTrue(dg1.isomorphic(dg2));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	
	@Test
	public void test010() {
		/*
		 * 	Nodes:[n0, n1, n2, n3, n4]
			Edges:[(n1:n1,e0), (n4:n2,e1), (n3:n2,e2), (n4:n4,e3), (n1:n0,e4)]
			random-n-5-e-5
			Nodes:[n0, n1, n2, n3, n4]
			Edges:[(n0:n0,e0), (n3:n3,e1), (n2:n1,e2), (n0:n1,e3), (n3:n4,e4)]
			
		* Isomorphic, disconnected

		 */
		Graph dg1 = new Graph("dg1");
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		dg1.addNode(n0);
		dg1.addNode(n1);
		dg1.addNode(n2);
		dg1.addNode(n3);
		dg1.addNode(n4);
		Edge e0 = new Edge(n1, n1, "e0");
		Edge e1 = new Edge(n4, n2, "e1");
		Edge e2 = new Edge(n3, n2, "e2");
		Edge e3 = new Edge(n4, n4, "e3");
		Edge e4 = new Edge(n1, n0, "e4");
		dg1.addEdge(e0);
		dg1.addEdge(e1);
		dg1.addEdge(e2);
		dg1.addEdge(e3);
		dg1.addEdge(e4);
		
		Graph dg2 = new Graph("dg2");
		n0 = new Node("n0");
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		dg2.addNode(n0);
		dg2.addNode(n1);
		dg2.addNode(n2);
		dg2.addNode(n3);
		dg2.addNode(n4);
		e0 = new Edge(n0, n0, "e0");
		e1 = new Edge(n3, n3, "e1");
		e2 = new Edge(n2, n1, "e2");
		e3 = new Edge(n0, n1, "e3");
		e4 = new Edge(n3, n4, "e4");
		dg2.addEdge(e0);
		dg2.addEdge(e1);
		dg2.addEdge(e2);
		dg2.addEdge(e3);
		dg2.addEdge(e4);
		
		assertTrue(dg1.isomorphic(dg2));

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	
	@Test
	public void test011() {
		/*
		 * g1
		 * Nodes:[n0, n1, n2, n3, n4, n5]
		 * Edges:[(n0:n3,e0), (n1:n2,e1), (n2:n3,e2), (n5:n2,e3), (n2:n1,e4), (n5:n0,e5), (n5:n3,e6), (n1:n4,e7), (n1:n5,e8), (n0:n5,e9)]
		 * g2
		 * Nodes:[n0, n1, n2, n3, n4, n5]
		 * Edges:[(n2:n0,e0), (n2:n5,e1), (n1:n2,e2), (n1:n5,e3), (n0:n4,e4), (n3:n0,e5), (n5:n1,e6), (n5:n3,e7), (n0:n2,e8), (n3:n2,e9)]
			
		* Not Isomorphic, fails only on brute force check

		 */
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n3");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n5", "n2");
		dg1.addAdjacencyEdge("n2", "n1");
		dg1.addAdjacencyEdge("n5", "n0");
		dg1.addAdjacencyEdge("n5", "n3");
		dg1.addAdjacencyEdge("n1", "n4");
		dg1.addAdjacencyEdge("n1", "n5");
		dg1.addAdjacencyEdge("n0", "n5");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n2", "n0");
		dg2.addAdjacencyEdge("n2", "n5");
		dg2.addAdjacencyEdge("n1", "n2");
		dg2.addAdjacencyEdge("n1", "n5");
		dg2.addAdjacencyEdge("n0", "n4");
		dg2.addAdjacencyEdge("n3", "n0");
		dg2.addAdjacencyEdge("n5", "n1");
		dg2.addAdjacencyEdge("n5", "n3");
		dg2.addAdjacencyEdge("n0", "n2");
		dg2.addAdjacencyEdge("n3", "n2");
		
		assertFalse(dg1.isomorphic(dg2));

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	
	
	
	/*
	 * 0-1\
	 * |/| 4
	 * 2-3/
	 */
	public String get5Node7EdgeA() {
	
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
	public String get5Node7EdgeB() {
	
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
	public String get5Node7EdgeC() {
	
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
