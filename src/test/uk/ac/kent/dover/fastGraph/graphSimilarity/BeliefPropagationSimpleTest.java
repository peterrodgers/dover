package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.*;

public class BeliefPropagationSimpleTest {


	@Test
	public void test001() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);

		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);
		
	}
	
	@Test
	public void test002() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();

		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);

		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);
		
	}
	
	@Test
	public void test003() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 1, 0);
		edges1.add(es10);
		
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
		edges2.add(es20);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		nodeMapping.put(1, 1);
		
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);

		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);
		
	}
	
	@Test
	public void test004() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 0, 2);
		edges1.add(es10);
		edges1.add(es11);
		
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 2, 0);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 0, 1);
		edges2.add(es20);
		edges2.add(es21);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		nodeMapping.put(1, 1);
		nodeMapping.put(2, 2);
		
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);

		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);
		
	}
	
	@Test
	public void test005() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 0, 2);
		edges1.add(es10);
		edges1.add(es11);
		
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 0, 2);
		edges2.add(es20);
		edges2.add(es21);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 2);
		nodeMapping.put(1, 1);
		nodeMapping.put(2, 0);
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);

		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 1);
		nodeMapping.put(1, 2);
		nodeMapping.put(2, 0);
		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret = bpc.similarity();
		assertEquals(0.0,ret,0.001);
		
	}
	
	@Test
	public void test006() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret1,ret2;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 0, 2);
		EdgeStructure es12 = new EdgeStructure(1,"es12", 0, (byte)0, (byte)0, 1, 2);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret1 = bpc.similarity();
		assertEquals(0.961744,ret1,0.001);

		nodeMapping = new HashMap<>();
		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret2 = bpc.similarity();
		assertEquals(ret1,ret2,0.001);
		
	}
	
	
	
	@Test
	public void test007() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret1,ret2;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 0, 2);
		edges1.add(es10);
		edges1.add(es11);
		
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 0, 2);
		edges2.add(es20);
		edges2.add(es21);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		nodeMapping.put(1, 1);
		nodeMapping.put(2, 2);
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret1 = bpc.similarity();
		assertEquals(0.88972,ret1,0.001);

		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret2 = bpc.similarity();
		assertEquals(ret1,ret2,0.001);
	}
	
	
	@Test
	public void test008() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret1,ret2;
		
		FastGraph g1 = FastGraph.randomGraphFactory(10, 20, 888333, true, false);
		FastGraph g2 = FastGraph.randomGraphFactory(10, 20, 777222, true, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		nodeMapping.put(1, 1);
		nodeMapping.put(2, 2);
		nodeMapping.put(3, 3);
		nodeMapping.put(4, 4);
		nodeMapping.put(5, 5);
		nodeMapping.put(6, 6);
		nodeMapping.put(7, 7);
		nodeMapping.put(8, 8);
		nodeMapping.put(9, 9);
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret1 = bpc.similarity();
		assertTrue(ret1 > 0.0);
		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret2 = bpc.similarity();
		assertEquals(ret1,ret2,0.001);
	}
	
	
	
	@Test
	public void test009() throws FastGraphException {
		
		BeliefPropagationCalculation bpc;
		HashMap<Integer,Integer> nodeMapping;
		double ret1,ret2;
		
		FastGraph g1 = FastGraph.randomGraphFactory(5, 10, 987654, true, false);
		FastGraph g2 = FastGraph.randomGraphFactory(10, 20, 123456, true, false);
		
		nodeMapping = new HashMap<>();
		nodeMapping.put(0, 0);
		nodeMapping.put(1, 1);
		nodeMapping.put(2, 2);
		nodeMapping.put(3, 3);
		nodeMapping.put(4, 4);
		bpc = new BeliefPropagationCalculation(g1, g2, nodeMapping);
		ret1 = bpc.similarity();
		assertTrue(ret1 > 0.0);
		bpc = new BeliefPropagationCalculation(g2, g1, nodeMapping);
		ret2 = bpc.similarity();
		assertEquals(ret1,ret2,0.001);

	}
	
	
	@Test
	public void test010() throws FastGraphException {
		
		BeliefPropagationSimple bps;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		bps = new BeliefPropagationSimple();
		ret = bps.similarity(g1,g2);
		assertEquals(0.0,ret,0.001);

		bps = new BeliefPropagationSimple();
		ret = bps.similarity(g2,g1);
		assertEquals(0.0,ret,0.001);
		
	}

	
	@Test
	public void test011() throws FastGraphException {
		
		BeliefPropagationSimple bps;
		double ret1,ret2,ret3,ret4;
		
		FastGraph g1 = FastGraph.randomGraphFactory(10, 20, 888333, true, false);
		FastGraph g2 = FastGraph.randomGraphFactory(10, 20, 777222, true, false);
		
		bps = new BeliefPropagationSimple(0,0,777666);
		ret1 = bps.similarity(g1,g2);
		assertTrue(ret1 > 0);

		bps = new BeliefPropagationSimple(0,0,111222);
		ret2 = bps.similarity(g2,g1);
		assertEquals(ret1,ret2,0.001);
		
		bps = new BeliefPropagationSimple(0,200,559999);
		ret3 = bps.similarity(g1,g2);
		assertTrue(ret1 > ret3);

		bps = new BeliefPropagationSimple(0,200,559999);
		ret4 = bps.similarity(g2,g1);
		assertEquals(ret3,ret4,0.001);
		
	}
	
	
	@Test
	public void test012() throws FastGraphException {
		
		BeliefPropagationSimple bps;
		double ret1,ret2,ret3,ret4;
		
		FastGraph g1 = FastGraph.randomGraphFactory(10, 20, 888333, true, false);
		FastGraph g2 = FastGraph.randomGraphFactory(12, 25, 777222, true, false);
		
		bps = new BeliefPropagationSimple(0,0,777444);
		ret1 = bps.similarity(g1,g2);
		assertTrue(ret1 > 0);

		bps = new BeliefPropagationSimple(0,0,111444);
		ret2 = bps.similarity(g2,g1);
		assertEquals(ret1,ret2,0.001);
		
		bps = new BeliefPropagationSimple(100,0,554444);
		ret3 = bps.similarity(g1,g2);
		assertTrue(ret1 > ret3);

		bps = new BeliefPropagationSimple(100,0,554444);
		ret4 = bps.similarity(g2,g1);
		assertEquals(ret3,ret4,0.001);
		
		
	}

	
}
