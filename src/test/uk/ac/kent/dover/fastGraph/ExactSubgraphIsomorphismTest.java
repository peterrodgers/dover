package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;


import org.junit.Test;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.NodeType;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;

public class ExactSubgraphIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleNodeLabelComparator snlc;
		AlwaysTrueNodeComparator atnc;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		snlc = new SimpleNodeLabelComparator(g1,g2);
		atnc = new AlwaysTrueNodeComparator(g1,g2);
		assertTrue(snlc.compare(4,4) == 0);
		assertTrue(snlc.compare(3,5) < 0);
		assertTrue(snlc.compare(5,3) > 0);
		assertTrue(snlc.compare(2,1) > 0);
		
		assertTrue(atnc.compare(4,4) == 0);
		assertTrue(atnc.compare(3,5) == 0);
		assertTrue(atnc.compare(5,3) == 0);
		assertTrue(atnc.compare(2,1) == 0);
	}

	@Test
	public void test002() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleEdgeLabelComparator selc;
		AlwaysTrueEdgeComparator atec;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		selc = new SimpleEdgeLabelComparator(g1,g2);
		atec = new AlwaysTrueEdgeComparator(g1, g2);
		assertTrue(selc.compare(0,0) == 0);
		assertTrue(selc.compare(13,15) < 0);
		assertTrue(selc.compare(19,0) > 0);
		assertTrue(selc.compare(12,13) < 0);
		
		assertTrue(atec.compare(0,0) == 0);
		assertTrue(atec.compare(13,15) == 0);
		assertTrue(atec.compare(19,0) == 0);
		assertTrue(atec.compare(12,13) == 0);
	}

	
	
	@Test
	public void test003() {
		Graph targetGraph = new Graph("empty");
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("empty");
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
	}

	
	@Test
	public void test004() {
		Graph targetGraph = new Graph("single node");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("empty");
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
	}

	
	@Test
	public void test005() {
		Graph targetGraph = new Graph("empty");
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("single node");
		Node n0 = new Node("nA");
		patternGraph.addNode(n0);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertFalse(result);
	
	}

	

	@Test
	public void test006() {
		Graph targetGraph = new Graph("two connected nodes");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("two connected nodes");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		
		result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
//TODO matches	
	}

	
	@Test
	public void test007() {
		Graph targetGraph = new Graph("single node");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nC");
		targetGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("single node");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertFalse(result);
	
		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		
		result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
	}

	
	
	@Test
	public void test008() {
		Graph targetGraph = new Graph("single node");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("single node");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		e0 = new Edge(n0,n1,"eB");
		patternGraph.addEdge(e0);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertFalse(result);
	
		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		
		result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
	}


	

	
	
	@Test
	public void test010() {
		Graph targetGraph = new Graph("triangle");
		Node n0 = new Node("nB");
		targetGraph.addNode(n0);
		Node n1 = new Node("nA");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		patternGraph.addEdge(e2);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
		
		//TODO test mappings
	
	}

	
	
	
	@Test
	public void test011() {
		Graph targetGraph = new Graph("triangle");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		patternGraph.addEdge(e2);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertFalse(result);
	
	}

	
	@Test
	public void test012() {
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Node n3 = new Node("nB");
		targetGraph.addNode(n3);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n0,"eB");
		targetGraph.addEdge(e3);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle");
		n0 = new Node("nA");
		patternGraph.addNode(n0);
		n1 = new Node("nB");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		n3 = new Node("nB");
		patternGraph.addNode(n3);
		e0 = new Edge(n2,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n2,n0,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
// TODO check mappings		
	}

	
	
	
	@Test
	public void test013() {
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Node n3 = new Node("nB");
		targetGraph.addNode(n3);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n0,"eB");
		targetGraph.addEdge(e3);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle with single edge attached");
		n0 = new Node("nA");
		patternGraph.addNode(n0);
		n1 = new Node("nB");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		n3 = new Node("nB");
		patternGraph.addNode(n3);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		patternGraph.addEdge(e2);
		e3 = new Edge(n2,n3,"eB");
		patternGraph.addEdge(e3);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);

		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertTrue(result);
	
// TODO check mappings		
	}

	

	@Test
	public void test014() {
		Graph targetGraph = new Graph("triangle with single edge attached");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Node n3 = new Node("nB");
		targetGraph.addNode(n3);
		Edge e0 = new Edge(n0,n1,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n0,"eB");
		targetGraph.addEdge(e3);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("triangle with single edge attached");
		n0 = new Node("nA");
		patternGraph.addNode(n0);
		n1 = new Node("nB");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		n3 = new Node("nB");
		patternGraph.addNode(n3);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n0,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n2,"eA");
		patternGraph.addEdge(e2);
		e3 = new Edge(n3,n1,"eB");
		patternGraph.addEdge(e3);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);

		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		assertFalse(result);
	
// TODO check mappings
	}

	
	
	
	
	


}
