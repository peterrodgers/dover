package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.kent.dover.fastGraph.EdgeStructure;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;
import uk.ac.kent.dover.fastGraph.NodeStructure;
import uk.ac.kent.dover.fastGraph.TrailNode;
import uk.ac.kent.dover.fastGraph.RandomTrail;

public class RandomTrailTest {
	
	@Rule
	public ExpectedException thrown1 = ExpectedException.none();
	@Test
	public void test001() throws Exception {
		RandomTrail rt;
		FastGraph g;
		g = FastGraph.randomGraphFactory(0, 0, false);
		rt = new RandomTrail(false);
	    thrown1.expect(FastGraphException.class);
		rt.findTrail(g, 0, 3);
	}

	@Rule
	public ExpectedException thrown2 = ExpectedException.none();
	@Test
	public void test002() throws Exception {
		RandomTrail rt;
		FastGraph g;
		g = FastGraph.randomGraphFactory(4, 5, false);
		rt = new RandomTrail(true,999);
	    thrown2.expect(FastGraphException.class);
		rt.findTrail(g, 4, 5);
	}

	
	@Test
	public void test003() throws Exception {		
		FastGraph g;
		RandomTrail rt;
		ArrayList<TrailNode> ret;
		
		g = FastGraph.randomGraphFactory(1, 0, false);
		rt = new RandomTrail(true,999);
		
		ret = rt.findTrail(g, 0, 3);
		assertEquals(1,ret.size());
		
	}
	
	@Test
	public void test004() throws Exception {		
		FastGraph g;
		RandomTrail rt;
		ArrayList<TrailNode> ret;
		NodeStructure ns0,ns1;
		EdgeStructure es0,es1;
		ArrayList<NodeStructure> addNodes;
		ArrayList<EdgeStructure> addEdges;

		addNodes = new ArrayList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)55, (byte)44);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)55, (byte)44);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new ArrayList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)55, (byte)44, 0, 1);
		es1 = new EdgeStructure(1,"es1", 1, (byte)55, (byte)44, 0, 1);
		addEdges.add(es0);
		addEdges.add(es1);
		g = FastGraph.structureFactory("g",(byte)0,addNodes,addEdges,false);
		
		rt = new RandomTrail(true);
		ret = rt.findTrail(g, 0, 0);
		assertEquals(0,ret.size());
		
		rt = new RandomTrail(false,111);
		ret = rt.findTrail(g, 0, 0);
		assertEquals(0,ret.size());
		
		rt = new RandomTrail(true,999);
		ret = rt.findTrail(g, 0, 1);
		assertEquals(1,ret.size());
		assertEquals(0,ret.get(0).getNode());
		
		rt = new RandomTrail(false);
		ret = rt.findTrail(g, 0, 2);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		
		rt = new RandomTrail(false,777);
		ret = rt.findTrail(g, 0, 3);
		assertEquals(3,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		assertEquals(0,ret.get(2).getNode());
		
		rt = new RandomTrail(false);
		ret = rt.findTrail(g, 0, 4);
		assertEquals(3,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		assertEquals(0,ret.get(2).getNode());
		
		rt = new RandomTrail(false);
		ret = rt.findTrail(g, 1, 4);
		assertEquals(3,ret.size());
		assertEquals(1,ret.get(0).getNode());
		assertEquals(0,ret.get(1).getNode());
		assertEquals(1,ret.get(2).getNode());
		
		rt = new RandomTrail(true,444);
		ret = rt.findTrail(g, 0, 2);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		
		rt = new RandomTrail(true);
		ret = rt.findTrail(g, 0, 4);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		
		rt = new RandomTrail(true);
		ret = rt.findTrail(g, 1, 4);
		assertEquals(1,ret.size());
		assertEquals(1,ret.get(0).getNode());
		
		
	}
	
	
	@Test
	public void test005() throws Exception {		
		FastGraph g;
		RandomTrail rt;
		ArrayList<TrailNode> ret;
		NodeStructure ns0,ns1,ns2,ns3;
		EdgeStructure es0,es1,es2;
		ArrayList<NodeStructure> addNodes;
		ArrayList<EdgeStructure> addEdges;

		addNodes = new ArrayList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)55, (byte)44);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)55, (byte)44);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)55, (byte)44);
		ns3 = new NodeStructure(3,"ns3", 1, (byte)55, (byte)44);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addEdges = new ArrayList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)55, (byte)44, 0, 1);
		es1 = new EdgeStructure(1,"es1", 1, (byte)55, (byte)44, 1, 2);
		es2 = new EdgeStructure(2,"es2", 1, (byte)55, (byte)44, 3, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g = FastGraph.structureFactory("g",(byte)0,addNodes,addEdges,false);
		
		rt = new RandomTrail(true,999);
		ret = rt.findTrail(g, 0, 1);
		assertEquals(1,ret.size());
		assertEquals(0,ret.get(0).getNode());
		
		rt = new RandomTrail(false);
		ret = rt.findTrail(g, 0, 2);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		
		rt = new RandomTrail(false,777);
		ret = rt.findTrail(g, 3, 4);
		assertEquals(4,ret.size());
		assertEquals(3,ret.get(0).getNode());
		assertEquals(2,ret.get(1).getNode());
		assertEquals(1,ret.get(2).getNode());
		assertEquals(0,ret.get(3).getNode());
		
		rt = new RandomTrail(true,444);
		ret = rt.findTrail(g, 3, 2);
		assertEquals(2,ret.size());
		assertEquals(3,ret.get(0).getNode());
		assertEquals(2,ret.get(1).getNode());
		
		rt = new RandomTrail(true,444);
		ret = rt.findTrail(g, 0, 4);
		assertEquals(3,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(1,ret.get(1).getNode());
		assertEquals(2,ret.get(2).getNode());
		
	}
	
	@Test
	public void test006() throws Exception {		
		FastGraph g;
		RandomTrail rt;
		ArrayList<TrailNode> ret;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 0));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 0));
		g = FastGraph.structureFactory("g",(byte)0,addNodes,addEdges,false);
		
		rt = new RandomTrail(true, 8888);
		ret = rt.findTrail(g, 0, 2);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(0,ret.get(1).getNode());
		
		rt = new RandomTrail(false, 8888);
		ret = rt.findTrail(g, 0, 2);
		assertEquals(2,ret.size());
		assertEquals(0,ret.get(0).getNode());
		assertEquals(0,ret.get(1).getNode());
		
	}
		

}
