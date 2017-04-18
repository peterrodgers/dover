package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.dover.fastGraph.ExactIsomorphism;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;

public class ExactIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;

		g1 = FastGraph.randomGraphFactory(0,0,1,false);
		g2 = FastGraph.randomGraphFactory(0,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(1,0,1,false);
		g2 = FastGraph.randomGraphFactory(1,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
	}

	
	@Test
	public void test002() throws FastGraphException {
		ExactIsomorphism ei;
		FastGraph g2;
		FastGraph g1;
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
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
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

	}

	
	@Rule
	public ExpectedException thrown1 = ExpectedException.none();
	@Test
	public void test004() throws Exception {
		FastGraph g1;
		g1 = FastGraph.randomGraphFactory(10,20,1,false); // disconnected graph
	    thrown1.expect(FastGraphException.class);
		new ExactIsomorphism(g1);
	}
		
	@Rule
	public ExpectedException thrown2 = ExpectedException.none();
	@Test
	public void test005() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,6,false); // connected graph
		ei = new ExactIsomorphism(g1);
		
		g2 = FastGraph.randomGraphFactory(10,20,1,false); // disconnected graph
	    thrown2.expect(FastGraphException.class);
	    ei.isomorphic(g2);
	}
		
	@Test
	public void test006() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,6,false);
		ei = new ExactIsomorphism(g1);
	    
		g2 = FastGraph.randomGraphFactory(10,25,6,false);
		assertFalse(ei.isomorphic(g2));

		g2 = FastGraph.randomGraphFactory(12,20,6,false);
		assertFalse(ei.isomorphic(g2));

		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		assertFalse(ei.isomorphic(g2));
	}

	
	@Test
	public void test007() throws FastGraphException {
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
	public void test008() throws FastGraphException {
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
	public void test009() throws FastGraphException {
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
	public void test010() throws FastGraphException {
		
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
	public void test011() throws FastGraphException {
		
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
	public void test012() throws FastGraphException {
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
	
	@Test
	public void test013() throws FastGraphException {
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
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	
	@Test
	public void test014() throws FastGraphException {
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
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	
	
	@Test
	public void test015() throws FastGraphException {
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
//new uk.ac.kent.displayGraph.display.GraphWindow(dg1);
//new uk.ac.kent.displayGraph.display.GraphWindow(dg2);
		FastGraph g1 = FastGraph.displayGraphFactory(dg1, false);
		FastGraph g2 = FastGraph.displayGraphFactory(dg2, false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
	}
	

	@Test
	public void test016() throws FastGraphException {
		FastGraph g1 = null;
		try {
			g1 = FastGraph.randomGraphFactory(50,150,999,true,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,444,false);
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		
	}

	@Test
	public void test017() throws FastGraphException {
		FastGraph g1 = null;
		try {
			g1 = FastGraph.randomGraphFactory(100,300,1,true,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,2,false);
		ExactIsomorphism ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));
		
	}

}
