package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.displayGraph.*;
import uk.ac.kent.dover.fastGraph.*;

public class ExactIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;

		g1 = FastGraph.randomGraphFactory(0,0,1,false);
		g2 = FastGraph.randomGraphFactory(0,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));

		g1 = FastGraph.randomGraphFactory(1,0,1,false);
		g2 = FastGraph.randomGraphFactory(1,0,1,false);
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
	}

	
	@Test
	public void test002()  {
		ExactIsomorphism ei;
		FastGraph g2;
		FastGraph g1;
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertTrue(ei.isomorphic(g1));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertTrue(ei.isomorphic(g1));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertFalse(ei.isomorphic(g1));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertFalse(ei.isomorphic(g1));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertFalse(ei.isomorphic(g1));

	}
	
	
	
	@Test
	public void test003() throws Exception {
		FastGraph g2;
		FastGraph g1;

		
		g1 = FastGraph.randomGraphFactory(12,30,111,true);
		g2 = FastGraph.randomGraphFactory(12,30,111,true);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1));
		
		g1 = FastGraph.randomGraphFactory(12,30,111,false);
		g2 = FastGraph.randomGraphFactory(12,30,112,false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));

	}

	
	@Test
	public void test004() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,1,false); // disconnected graph
		g2 = FastGraph.randomGraphFactory(10,20,2222,false); // disconnected graph
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertFalse(ei.isomorphic(g1));
		
	}
		
	@Test
	public void test005() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,6,false); // connected graph
		g2 = FastGraph.randomGraphFactory(10,20,1,false); // disconnected graph
		ei = new ExactIsomorphism(g1,false);
	    ei.isomorphic(g2);
	    
		ei = new ExactIsomorphism(g1,true);
	    ei.isomorphic(g2);

	}
		
	@Test
	public void test006() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,6,false);
		g2 = FastGraph.randomGraphFactory(10,25,6,false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));

		g2 = FastGraph.randomGraphFactory(12,20,6,false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));

		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		ei = new ExactIsomorphism(g1,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
	}

	
	@Test
	public void test007()  {
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
		assertTrue(dg2.isomorphic(dg1));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		int[] mapping = {0,2,1};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));

	}
	

	@Test
	public void test008()  {
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
		assertTrue(ExactIsomorphism.isomorphic(g2,g1));
		
	}
	
	@Test
	public void test009()  {
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
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false));
		
	}

	
	@Test
	public void test010()  {
		
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
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
	}
	
	
	@Test
	public void test011()  {
		
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
		assertTrue(dg2.isomorphic(dg1));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1));
		
	}
	
	@Test
	public void test012()  {
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
		assertFalse(dg2.isomorphic(dg1));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		
	}
	
	@Test
	public void test013()  {
		/*
		 * g1
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6]
		 * Edges:[(n6:n2,e0), (n0:n1,e1), (n6:n0,e2), (n1:n4,e3), (n5:n2,e4), (n6:n1,e5), (n2:n0,e6), (n0:n3,e7), (n3:n6,e8), (n3:n1,e9)]
		 * g2
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6]
		 * Edges:[(n2:n1,e0), (n5:n4,e1), (n0:n3,e2), (n6:n2,e3), (n6:n0,e4), (n3:n2,e5), (n5:n0,e6), (n2:n0,e7), (n1:n6,e8), (n0:n1,e9)]
		 * 
		 * Not Isomorphic, fails only on brute force check [not true after nodes with degree check]
		 */
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n6", "n2");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n6", "n0");
		dg1.addAdjacencyEdge("n1", "n4");
		dg1.addAdjacencyEdge("n5", "n2");
		dg1.addAdjacencyEdge("n6", "n1");
		dg1.addAdjacencyEdge("n2", "n0");
		dg1.addAdjacencyEdge("n0", "n3");
		dg1.addAdjacencyEdge("n3", "n6");
		dg1.addAdjacencyEdge("n3", "n1");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n5", "n4");
		dg2.addAdjacencyEdge("n0", "n3");
		dg2.addAdjacencyEdge("n6", "n2");
		dg2.addAdjacencyEdge("n6", "n0");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n5", "n0");
		dg2.addAdjacencyEdge("n2", "n0");
		dg2.addAdjacencyEdge("n1", "n6");
		dg2.addAdjacencyEdge("n0", "n1");
		
		assertFalse(dg1.isomorphic(dg2));
		assertFalse(dg2.isomorphic(dg1));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		
	}
	
	
	@Test
	public void test014()  {
		/*
		 * g1
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6, n7]
		 * Edges:[(n5:n3,e0), (n3:n0,e1), (n4:n1,e2), (n6:n0,e3), (n1:n6,e4), (n7:n5,e5), (n0:n2,e6), (n6:n2,e7), (n6:n4,e8), (n1:n7,e9), (n4:n5,e10), (n1:n3,e11), (n7:n0,e12), (n5:n1,e13), (n2:n3,e14)]
		 * g2
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6, n7]
		 * Edges:[(n2:n1,e0), (n0:n3,e1), (n3:n4,e2), (n3:n2,e3), (n3:n6,e4), (n6:n1,e5), (n1:n4,e6), (n2:n7,e7), (n5:n6,e8), (n7:n4,e9), (n0:n7,e10), (n3:n5,e11), (n5:n4,e12), (n1:n7,e13), (n2:n0,e14)]
		 * 
		 * Not Isomorphic, fails only on brute force check including with degree check
		 */
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n5", "n3");
		dg1.addAdjacencyEdge("n3", "n0");
		dg1.addAdjacencyEdge("n4", "n1");
		dg1.addAdjacencyEdge("n6", "n0");
		dg1.addAdjacencyEdge("n1", "n6");
		dg1.addAdjacencyEdge("n7", "n5");
		dg1.addAdjacencyEdge("n0", "n2");
		dg1.addAdjacencyEdge("n6", "n2");
		dg1.addAdjacencyEdge("n6", "n4");
		dg1.addAdjacencyEdge("n1", "n7");
		dg1.addAdjacencyEdge("n4", "n5");
		dg1.addAdjacencyEdge("n1", "n3");
		dg1.addAdjacencyEdge("n7", "n0");
		dg1.addAdjacencyEdge("n5", "n1");
		dg1.addAdjacencyEdge("n2", "n3");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n0", "n3");
		dg2.addAdjacencyEdge("n3", "n4");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n3", "n6");
		dg2.addAdjacencyEdge("n6", "n1");
		dg2.addAdjacencyEdge("n1", "n4");
		dg2.addAdjacencyEdge("n2", "n7");
		dg2.addAdjacencyEdge("n5", "n6");
		dg2.addAdjacencyEdge("n7", "n4");
		dg2.addAdjacencyEdge("n0", "n7");
		dg2.addAdjacencyEdge("n3", "n5");
		dg2.addAdjacencyEdge("n5", "n4");
		dg2.addAdjacencyEdge("n1", "n7");
		dg2.addAdjacencyEdge("n2", "n0");		
		assertFalse(dg1.isomorphic(dg2));
		assertFalse(dg2.isomorphic(dg1));
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false));
		
	}
	
	
	@Test
	public void test015()  {
		/*
		 * g1
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6, n7]
		 * Edges:[(n6:n5,e0), (n2:n4,e1), (n0:n3,e2), (n1:n0,e3), (n4:n3,e4), (n6:n7,e5), (n2:n3,e6), (n1:n6,e7), (n2:n5,e8), (n3:n7,e9), (n5:n1,e10), (n6:n3,e11), (n1:n7,e12), (n4:n5,e13), (n7:n0,e14)]
		 * g2
		 * Nodes:[n0, n1, n2, n3, n4, n5, n6, n7]
		 * Edges:[(n0:n1,e0), (n7:n2,e1), (n4:n0,e2), (n6:n3,e3), (n7:n3,e4), (n0:n5,e5), (n7:n4,e6), (n5:n4,e7), (n5:n1,e8), (n1:n2,e9), (n4:n3,e10), (n1:n6,e11), (n3:n2,e12), (n6:n2,e13), (n7:n1,e14)]
		 * 
		 * Isomorphic match [0:6, 1:3, 2:0, 3:1, 4:5, 5:4, 6:7, 7:2]
		 */
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n6", "n5");
		dg1.addAdjacencyEdge("n2", "n4");
		dg1.addAdjacencyEdge("n0", "n3");
		dg1.addAdjacencyEdge("n1", "n0");
		dg1.addAdjacencyEdge("n4", "n3");
		dg1.addAdjacencyEdge("n6", "n7");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n1", "n6");
		dg1.addAdjacencyEdge("n2", "n5");
		dg1.addAdjacencyEdge("n3", "n7");
		dg1.addAdjacencyEdge("n5", "n1");
		dg1.addAdjacencyEdge("n6", "n3");
		dg1.addAdjacencyEdge("n1", "n7");
		dg1.addAdjacencyEdge("n4", "n5");
		dg1.addAdjacencyEdge("n7", "n0");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n0", "n1");
		dg2.addAdjacencyEdge("n7", "n2");
		dg2.addAdjacencyEdge("n4", "n0");
		dg2.addAdjacencyEdge("n6", "n3");
		dg2.addAdjacencyEdge("n7", "n3");
		dg2.addAdjacencyEdge("n0", "n5");
		dg2.addAdjacencyEdge("n7", "n4");
		dg2.addAdjacencyEdge("n5", "n4");
		dg2.addAdjacencyEdge("n5", "n1");
		dg2.addAdjacencyEdge("n1", "n2");
		dg2.addAdjacencyEdge("n4", "n3");
		dg2.addAdjacencyEdge("n1", "n6");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n6", "n2");
		dg2.addAdjacencyEdge("n7", "n1");
		assertTrue(dg1.isomorphic(dg2));
		assertTrue(dg2.isomorphic(dg1));
//new uk.ac.kent.displayGraph.display.GraphWindow(dg1);
//new uk.ac.kent.displayGraph.display.GraphWindow(dg2);
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1));
		
	}
	

	@Test
	public void test016()  {
		FastGraph g1 = null;
		ExactIsomorphism ei;
		try {
			g1 = FastGraph.randomGraphFactory(50,150,999,true,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,444,false);
		
		String l1 = g1.getNodeLabel(5);
		String l2 = g2.getNodeLabel(5);
		
		assertNotEquals(l1,l2);
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g1));
		
	}

	@Test
	public void test017()  {
		FastGraph g1 = null;
		ExactIsomorphism ei;
		try {
			g1 = FastGraph.randomGraphFactory(100,500,1,true,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,2,false);
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g1));
		
	}
	
	@Test
	public void test018()  {
		/*
		 * g1
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n0:n1,e0), (n1:n2,e1), (n2:n0,e2)]
		 * g2
		 * Nodes:[n0, n1, n2]
		 * Edges:[(n0:n1,e0), (n0:n2,e1), (n1:n2,e2)]
		 * 
		 * undirected isomorphic match [n0:n0, n1:n1, n2:n2]
		 * directed not isomorphic
		 */
		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n0");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n0", "n1");
		dg2.addAdjacencyEdge("n0", "n2");
		dg2.addAdjacencyEdge("n1", "n2");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
	}
	
	@Test
	public void test019()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n0");
		dg1.addAdjacencyEdge("n2", "n3");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n0", "n1");
		dg2.addAdjacencyEdge("n0", "n2");
		dg2.addAdjacencyEdge("n1", "n2");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
	}
	
	@Test
	public void test020()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		
		Graph dg2 = new Graph("dg2");
		dg2.addNode(new Node("n0"));
		dg2.addNode(new Node("n1"));
		dg2.addAdjacencyEdge("n1", "n0");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
		
		int[] mapping = {1,0};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));
	}
	
	
	@Test
	public void test021()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n2", "n1");
		dg1.addAdjacencyEdge("n1", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n0", "n1");
		dg2.addAdjacencyEdge("n1", "n2");
		dg2.addAdjacencyEdge("n2", "n0");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
		
		int[] mapping = {0,1,2};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));

	}
	

	@Test
	public void test22()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n2", "n1");
		dg1.addAdjacencyEdge("n1", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		dg1.addAdjacencyEdge("n0", "n3");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n1", "n3");
		dg2.addAdjacencyEdge("n3", "n2");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
		
		int[] mapping = {2,3,1,0};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));
		
	}
	

	@Test
	public void test23()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n2", "n1");
		dg1.addAdjacencyEdge("n1", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		dg1.addAdjacencyEdge("n0", "n3");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n3", "n1");
		dg2.addAdjacencyEdge("n3", "n2");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
	}
	
	@Test
	public void test24()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n3", "n4");
		dg1.addAdjacencyEdge("n4", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n4", "n1");
		dg2.addAdjacencyEdge("n4", "n3");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n0", "n4");
		dg2.addAdjacencyEdge("n2", "n1");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
	}
	

	@Test
	public void test25()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n3", "n4");
		dg1.addAdjacencyEdge("n4", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n4", "n3");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n4", "n2");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n0", "n4");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
	}
	
	@Test
	public void test26()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n0");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n3", "n4");
		dg1.addAdjacencyEdge("n4", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n4", "n3");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n4", "n4");
		dg2.addAdjacencyEdge("n4", "n2");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n0", "n4");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertTrue(ei.isomorphic(g1));
	}
	
	@Test
	public void test27()  {

		ExactIsomorphism ei;
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n0");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n3", "n4");
		dg1.addAdjacencyEdge("n4", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n4", "n3");
		dg2.addAdjacencyEdge("n2", "n1");
		dg2.addAdjacencyEdge("n2", "n2");
		dg2.addAdjacencyEdge("n4", "n2");
		dg2.addAdjacencyEdge("n1", "n0");
		dg2.addAdjacencyEdge("n3", "n2");
		dg2.addAdjacencyEdge("n0", "n4");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		
		ei = new ExactIsomorphism(g1,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));
		
	}
	
	@Test
	public void test028() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,6,false); // connected graph
		g2 = FastGraph.randomGraphFactory(10,20,1,false); // disconnected graph
		ei = new ExactIsomorphism(g1,true);
		assertFalse(ei.isomorphic(g2));
	    
		ei = new ExactIsomorphism(g2,true);
		assertFalse(ei.isomorphic(g1));

	}
		
	@Test
	public void test29()  {

		ExactIsomorphism ei;
		
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n0");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n1", "n2");
		dg2.addAdjacencyEdge("n2", "n0");
		dg2.addAdjacencyEdge("n0", "n1");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		
		int[] mapping = {2,0,1};
		assertTrue(Arrays.equals(mapping, ei.getLastMatch()));
		
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));

	}

	
	@Test
	public void test30()  {
	
		ExactIsomorphism ei;
		
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n0");
		dg1.getNodes().get(0).setLabel("n2");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n1", "n2");
		dg2.addAdjacencyEdge("n2", "n0");
		dg2.addAdjacencyEdge("n0", "n1");
	
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,false));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,false));
		
		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		ei = new ExactIsomorphism(g1,false,true);
		assertFalse(ei.isomorphic(g2));
		
		ei = new ExactIsomorphism(g2,false,true);
		assertFalse(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g2,true,true);
		assertFalse(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g2,true,true);
		assertFalse(ei.isomorphic(g1));
	}
	
	@Test
	public void test031()  {
		FastGraph g1 = null;
		ExactIsomorphism ei;
		try {
			g1 = FastGraph.randomGraphFactory(50,150,876,true,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,654,false);
		
		String l1 = g1.getNodeLabel(5);
		String l2 = g2.getNodeLabel(5);
		
		assertNotEquals(l1,l2);
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));
	}

	
	@Test
	public void test032()  {
		
		ExactIsomorphism ei;
		
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		NodeStructure ns13 = new NodeStructure(3,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		nodes1.add(ns13);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 3);
		EdgeStructure es13 = new EdgeStructure(3,"es13", 0, (byte)0, (byte)0, 3, 0);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		edges1.add(es13);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 2, 1);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 3, 2);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 3);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 1, 0);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));

	}

	
	@Test
	public void test033()  {
		
		ExactIsomorphism ei;
		
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		NodeStructure ns13 = new NodeStructure(3,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		nodes1.add(ns13);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 3);
		EdgeStructure es13 = new EdgeStructure(3,"es13", 0, (byte)0, (byte)0, 3, 0);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		edges1.add(es13);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 3, 2);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 3);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 1, 0);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertFalse(ei.isomorphic(g2));
		
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertFalse(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));
	}

	
	@Test
	public void test034()  {
		
		ExactIsomorphism ei;
		
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	
	@Test
	public void test035()  {
		
		ExactIsomorphism ei;
		
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns13 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns14 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns15 = new NodeStructure(5,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		nodes1.add(ns13);
		nodes1.add(ns14);
		nodes1.add(ns15);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 0);
		EdgeStructure es13 = new EdgeStructure(3,"es13", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es14 = new EdgeStructure(4,"es14", 0, (byte)0, (byte)0, 4, 5);
		EdgeStructure es15 = new EdgeStructure(5,"es15", 0, (byte)0, (byte)0, 5, 3);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		edges1.add(es13);
		edges1.add(es14);
		edges1.add(es15);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		nodes2.add(ns24);
		nodes2.add(ns25);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 2, 1);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 2);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 4, 3);
		EdgeStructure es24 = new EdgeStructure(4,"es24", 0, (byte)0, (byte)0, 5, 4);
		EdgeStructure es25 = new EdgeStructure(5,"es25", 0, (byte)0, (byte)0, 3, 5);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);
		edges2.add(es24);
		edges2.add(es25);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test036()  {
		
		ExactIsomorphism ei;
		
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns13 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns14 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns15 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		NodeStructure ns16 = new NodeStructure(6,"b", 0, (byte)0, (byte)0);
		NodeStructure ns17 = new NodeStructure(7,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		nodes1.add(ns13);
		nodes1.add(ns14);
		nodes1.add(ns15);
		nodes1.add(ns16);
		nodes1.add(ns17);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 0);
		EdgeStructure es13 = new EdgeStructure(3,"es13", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es14 = new EdgeStructure(4,"es14", 0, (byte)0, (byte)0, 4, 5);
		EdgeStructure es15 = new EdgeStructure(5,"es15", 0, (byte)0, (byte)0, 5, 3);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		edges1.add(es13);
		edges1.add(es14);
		edges1.add(es15);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"a", 0, (byte)0, (byte)0);
		NodeStructure ns26 = new NodeStructure(6,"a", 0, (byte)0, (byte)0);
		NodeStructure ns27 = new NodeStructure(7,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		nodes2.add(ns24);
		nodes2.add(ns25);
		nodes2.add(ns26);
		nodes2.add(ns27);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 2, 1);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 2);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 4, 3);
		EdgeStructure es24 = new EdgeStructure(4,"es24", 0, (byte)0, (byte)0, 5, 4);
		EdgeStructure es25 = new EdgeStructure(5,"es25", 0, (byte)0, (byte)0, 5, 3);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);
		edges2.add(es24);
		edges2.add(es25);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertTrue(ei.isomorphic(g2));
		
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertTrue(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));

		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}

	
	@Test
	public void test037()  {
		
		ExactIsomorphism ei;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns13 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns14 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns15 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		nodes1.add(ns13);
		nodes1.add(ns14);
		nodes1.add(ns15);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 0);
		EdgeStructure es13 = new EdgeStructure(3,"es13", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es14 = new EdgeStructure(4,"es14", 0, (byte)0, (byte)0, 4, 5);
		EdgeStructure es15 = new EdgeStructure(5,"es15", 0, (byte)0, (byte)0, 5, 3);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		edges1.add(es13);
		edges1.add(es14);
		edges1.add(es15);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"a", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		nodes2.add(ns24);
		nodes2.add(ns25);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 2, 1);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 2);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 4, 3);
		EdgeStructure es24 = new EdgeStructure(4,"es24", 0, (byte)0, (byte)0, 5, 4);
		EdgeStructure es25 = new EdgeStructure(5,"es25", 0, (byte)0, (byte)0, 5, 3);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);
		edges2.add(es24);
		edges2.add(es25);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		ei = new ExactIsomorphism(g1,false,true);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertFalse(ei.isomorphic(g2));
		
		ei = new ExactIsomorphism(g2,false,true);
		assertTrue(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertFalse(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertTrue(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertTrue(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertTrue(ei.isomorphic(g1));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test038()  {
		
		ExactIsomorphism ei;
		
		Graph dg1 = new Graph("dg1");
		dg1.addAdjacencyEdge("n0", "n1");
		dg1.addAdjacencyEdge("n1", "n2");
		dg1.addAdjacencyEdge("n2", "n3");
		dg1.addAdjacencyEdge("n3", "n4");
		dg1.addAdjacencyEdge("n4", "n5");
		dg1.addAdjacencyEdge("n5", "n0");
		dg1.addAdjacencyEdge("n0", "n2");
		dg1.addAdjacencyEdge("n11", "n12");
		dg1.addAdjacencyEdge("n12", "n13");
		dg1.addAdjacencyEdge("n13", "n11");
		
		Graph dg2 = new Graph("dg2");
		dg2.addAdjacencyEdge("n23", "n21");
		dg2.addAdjacencyEdge("n22", "n23");
		dg2.addAdjacencyEdge("n0", "n1");
		dg2.addAdjacencyEdge("n2", "n3");
		dg2.addAdjacencyEdge("n5", "n0");
		dg2.addAdjacencyEdge("n21", "n22");
		dg2.addAdjacencyEdge("n0", "n3");
		dg2.addAdjacencyEdge("n3", "n4");
		dg2.addAdjacencyEdge("n4", "n5");
		dg2.addAdjacencyEdge("n1", "n2");

		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);

		
		ei = new ExactIsomorphism(g1,true,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertFalse(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertFalse(ei.isomorphic(g1));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	

	
	@Test
	public void test039()  {
		
		ExactIsomorphism ei;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 0);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		edges1.add(es10);
		edges1.add(es11);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 2, 2);
		edges2.add(es20);
		edges2.add(es21);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		ei = new ExactIsomorphism(g1,false,true);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g1,true,true);
		assertFalse(ei.isomorphic(g2));
		
		ei = new ExactIsomorphism(g2,false,true);
		assertFalse(ei.isomorphic(g1));
		ei = new ExactIsomorphism(g2,true,true);
		assertFalse(ei.isomorphic(g1));
		
		ei = new ExactIsomorphism(g1,true,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,true,false);
		assertFalse(ei.isomorphic(g1));

		ei = new ExactIsomorphism(g1,false,false);
		assertFalse(ei.isomorphic(g2));
		ei = new ExactIsomorphism(g2,false,false);
		assertFalse(ei.isomorphic(g1));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

	}
	
	
	
	@Test
	public void test040()  {
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	

	
	@Test
	public void test041()  {
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 2, 2);
		edges1.add(es10);
		edges1.add(es11);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 2, 2);
		edges2.add(es20);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);

		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g1,g2,true,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,false,true));
		assertFalse(ExactIsomorphism.isomorphic(g2,g1,true,true));
		
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	@Test
	public void test042()  {
		
		FastGraph g1 = null;

		for(int i = 0; i < 10; i++) {
			try {
				g1 = FastGraph.randomGraphFactory(20,15,i*77,false,false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,i*2222,false);
			
			String l1 = g1.getNodeLabel(5);
			String l2 = g2.getNodeLabel(5);
			assertNotEquals(l1,l2);
			
			assertTrue(ExactIsomorphism.isomorphic(g1,g2));
			assertTrue(ExactIsomorphism.isomorphic(g1,g2,true));
			assertTrue(ExactIsomorphism.isomorphic(g2,g1));
			assertTrue(ExactIsomorphism.isomorphic(g2,g1,true));
			assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
			assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
			assertTrue(ExactIsomorphism.isomorphic(g2,g1,false,true));
			assertTrue(ExactIsomorphism.isomorphic(g2,g1,true,true));
		}

	}
		
	
}
