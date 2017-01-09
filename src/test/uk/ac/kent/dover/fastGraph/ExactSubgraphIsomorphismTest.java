package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
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
		
		LinkedList<SubgraphMapping> sgm;
		
		boolean result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
		assertTrue(result);
		
		assertEquals(1,sgm.size());
		assertEquals(1,sgm.get(0).getNodeMapping()[0]);
		assertEquals(0,sgm.get(0).getNodeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[0]);
	
		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		
		result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();

		assertTrue(result);
		assertEquals(2,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[0]);
	
		assertEquals(1,sgm.get(1).getNodeMapping()[0]);
		assertEquals(0,sgm.get(1).getNodeMapping()[1]);
		assertEquals(0,sgm.get(1).getEdgeMapping()[0]);

	}

	
	@Test
	public void test007() {
		
		LinkedList<SubgraphMapping> sgm;
		
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
		sgm = esi.getFoundMappings();
		assertFalse(result);
		assertEquals(0,sgm.size());
	
		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		
		result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();

		assertTrue(result);
		assertEquals(2,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[0]);
	
		assertEquals(1,sgm.get(1).getNodeMapping()[0]);
		assertEquals(0,sgm.get(1).getNodeMapping()[1]);
		assertEquals(0,sgm.get(1).getEdgeMapping()[0]);
	
	
	}

	
	
	@Test
	public void test008() {
		
		LinkedList<SubgraphMapping> sgm;
		
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
		sgm = esi.getFoundMappings();
		
		assertTrue(result);
		assertEquals(2,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[0]);
	
		assertEquals(1,sgm.get(1).getNodeMapping()[0]);
		assertEquals(0,sgm.get(1).getNodeMapping()[1]);
		assertEquals(0,sgm.get(1).getEdgeMapping()[0]);
		
	
	}


	
	@Test
	public void test009() {

		LinkedList<SubgraphMapping> sgm;
		Graph targetGraph = new Graph("triangle");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Edge e0 = new Edge(n2,n0,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n1,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n0,"eA");
		targetGraph.addEdge(e2);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("square");
		n0 = new Node("nB");
		patternGraph.addNode(n0);
		n1 = new Node("nA");
		patternGraph.addNode(n1);
		n2 = new Node("nA");
		patternGraph.addNode(n2);
		Node n3 = new Node("nB");
		patternGraph.addNode(n3);
		e0 = new Edge(n0,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n1,n2,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n2,n3,"eA");
		patternGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n0,"eB");
		patternGraph.addEdge(e3);
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
		assertFalse(result);
		
	
	}


	

	
	
	@Test
	public void test010() {
		
		LinkedList<SubgraphMapping> sgm;
		
		Graph targetGraph = new Graph("triangle");
		Node n0 = new Node("nA");
		targetGraph.addNode(n0);
		Node n1 = new Node("nB");
		targetGraph.addNode(n1);
		Node n2 = new Node("nA");
		targetGraph.addNode(n2);
		Edge e0 = new Edge(n2,n0,"eA");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n1,n2,"eB");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n0,"eA");
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
		
		ExactSubgraphIsomorphism esi;
		boolean result;
		
		esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
		
		assertTrue(result);
		assertEquals(1,sgm.size());
		assertEquals(1,sgm.get(0).getNodeMapping()[0]);
		assertEquals(0,sgm.get(0).getNodeMapping()[1]);
		assertEquals(2,sgm.get(0).getNodeMapping()[2]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[2]);

		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
		assertTrue(result);
		assertEquals(6,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(2,sgm.get(0).getNodeMapping()[2]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[2]);
	
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
		
		LinkedList<SubgraphMapping> sgm;
		
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
		e0 = new Edge(n2,n1,"eA");
		patternGraph.addEdge(e0);
		e1 = new Edge(n2,n0,"eB");
		patternGraph.addEdge(e1);
		e2 = new Edge(n1,n0,"eA");
		patternGraph.addEdge(e2);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();

		assertTrue(result);
		assertEquals(2,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(2,sgm.get(0).getNodeMapping()[2]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[2]);

		assertEquals(2,sgm.get(1).getNodeMapping()[0]);
		assertEquals(1,sgm.get(1).getNodeMapping()[1]);
		assertEquals(0,sgm.get(1).getNodeMapping()[2]);
		assertEquals(0,sgm.get(1).getEdgeMapping()[0]);
		assertEquals(1,sgm.get(1).getEdgeMapping()[1]);
		assertEquals(2,sgm.get(1).getEdgeMapping()[2]);

		AlwaysTrueNodeComparator nc = new AlwaysTrueNodeComparator(target, pattern);
		AlwaysTrueEdgeComparator ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		esi = new ExactSubgraphIsomorphism(target, pattern, nc, ec);
		result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
		assertTrue(result);
		assertEquals(6,sgm.size());
		assertEquals(0,sgm.get(0).getNodeMapping()[0]);
		assertEquals(1,sgm.get(0).getNodeMapping()[1]);
		assertEquals(2,sgm.get(0).getNodeMapping()[2]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[2]);
	
	}

	
	
	
	@Test
	public void test013() {
		
		LinkedList<SubgraphMapping> sgm;
		
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
		Node nnX0 = new Node("nB");
		patternGraph.addNode(nnX0);
		Node nnX1 = new Node("nA");
		patternGraph.addNode(nnX1);
		Node nnX2 = new Node("nB");
		patternGraph.addNode(nnX2);
		Node nnX3 = new Node("nA");
		patternGraph.addNode(nnX3);
		Edge eeX0 = new Edge(nnX2,nnX1,"eA");
		patternGraph.addEdge(eeX0);
		Edge eeX1 = new Edge(nnX3,nnX2,"eA");
		patternGraph.addEdge(eeX1);
		Edge eeX2 = new Edge(nnX0,nnX3,"eB");
		patternGraph.addEdge(eeX2);
		Edge eeX3 = new Edge(nnX3,nnX1,"eB");
		patternGraph.addEdge(eeX3);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);

		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
		
		assertTrue(result);
		assertEquals(1,sgm.size());
		assertEquals(3,sgm.get(0).getNodeMapping()[0]);
		assertEquals(2,sgm.get(0).getNodeMapping()[1]);
		assertEquals(1,sgm.get(0).getNodeMapping()[2]);
		assertEquals(0,sgm.get(0).getNodeMapping()[3]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(3,sgm.get(0).getEdgeMapping()[2]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[3]);
	
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
	
	}

	
	@Test
	public void test015() {
		
		/*
		 * B-C-B
		 * | | |
		 * D-A-D
		 *  \|/|
		 *   C-B
		 *   
		 *D-C
		 *| |
		 *A-B
		 *
		 */
		
		LinkedList<SubgraphMapping> sgm;
		
		Graph targetGraph = new Graph("larger graph");
		Node n0 = new Node("A");
		targetGraph.addNode(n0);
		Node n1 = new Node("B");
		targetGraph.addNode(n1);
		Node n2 = new Node("C");
		targetGraph.addNode(n2);
		Node n3 = new Node("B");
		targetGraph.addNode(n3);
		Node n4 = new Node("D");
		targetGraph.addNode(n4);
		Node n5 = new Node("B");
		targetGraph.addNode(n5);
		Node n6 = new Node("C");
		targetGraph.addNode(n6);
		Node n7 = new Node("D");
		targetGraph.addNode(n7);
		
		Edge e0 = new Edge(n0,n7,"");
		targetGraph.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"");
		targetGraph.addEdge(e1);
		Edge e2 = new Edge(n0,n4,"");
		targetGraph.addEdge(e2);
		Edge e3 = new Edge(n6,n0,"");
		targetGraph.addEdge(e3);
		Edge e4 = new Edge(n7,n1,"");
		targetGraph.addEdge(e4);
		Edge e4a = new Edge(n2,n1,"");
		targetGraph.addEdge(e4a);
		Edge e5 = new Edge(n3,n2,"");
		targetGraph.addEdge(e5);
		Edge e6 = new Edge(n3,n4,"");
		targetGraph.addEdge(e6);
		Edge e7 = new Edge(n4,n6,"");
		targetGraph.addEdge(e7);
		Edge e8 = new Edge(n4,n5,"");
		targetGraph.addEdge(e8);
		Edge e9 = new Edge(n6,n5,"");
		targetGraph.addEdge(e9);
		Edge e10 = new Edge(n6,n7,"");
		targetGraph.addEdge(e10);
		FastGraph target = FastGraph.displayGraphFactory(targetGraph,false);

		Graph patternGraph = new Graph("square");
		Node nnX0 = new Node("B");
		patternGraph.addNode(nnX0);
		Node nnX1 = new Node("C");
		patternGraph.addNode(nnX1);
		Node nnX2 = new Node("A");
		patternGraph.addNode(nnX2);
		Node nnX3 = new Node("D");
		patternGraph.addNode(nnX3);
		Edge eeX0 = new Edge(nnX0,nnX1,"");
		patternGraph.addEdge(eeX0);
		Edge eeX1 = new Edge(nnX2,nnX1,"");
		patternGraph.addEdge(eeX1);
		Edge eeX2 = new Edge(nnX2,nnX3,"");
		patternGraph.addEdge(eeX2);
		Edge eeX3 = new Edge(nnX3,nnX0,"");
		patternGraph.addEdge(eeX3);
		FastGraph pattern = FastGraph.displayGraphFactory(patternGraph,false);
	
		SimpleNodeLabelComparator snlc = new SimpleNodeLabelComparator(target, pattern);
		SimpleEdgeLabelComparator selc = new SimpleEdgeLabelComparator(target, pattern);
		
		ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(target, pattern, snlc, selc);
		
		boolean result = esi.subGraphIsomorphismFinder();
		sgm = esi.getFoundMappings();
System.out.println(sgm);
		assertTrue(result);
		assertEquals(3,sgm.size());
		assertEquals(3,sgm.get(0).getNodeMapping()[0]);
		assertEquals(2,sgm.get(0).getNodeMapping()[1]);
		assertEquals(1,sgm.get(0).getNodeMapping()[2]);
		assertEquals(0,sgm.get(0).getNodeMapping()[3]);
		assertEquals(2,sgm.get(0).getEdgeMapping()[0]);
		assertEquals(0,sgm.get(0).getEdgeMapping()[1]);
		assertEquals(3,sgm.get(0).getEdgeMapping()[2]);
		assertEquals(1,sgm.get(0).getEdgeMapping()[3]);
	
	}

	
	
	
	


}
