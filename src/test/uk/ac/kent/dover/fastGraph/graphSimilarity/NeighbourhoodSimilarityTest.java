package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.NeighbourhoodSimilarity;

public class NeighbourhoodSimilarityTest {


	@Test
	public void test001() throws FastGraphException {
		
		NeighbourhoodSimilarity ns1;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		ns1 = new NeighbourhoodSimilarity();
		ret = ns1.similarity(g1,g2);
		assertEquals(0.0,ret,0.001);
		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g1,g2);
		assertEquals(0.0,ret,0.001);

		
		ns1 = new NeighbourhoodSimilarity(false);
		ns1.setEpsilon(0.001);
		assertEquals(0.001,ns1.getEpsilon(),0.000001);
		ret = ns1.similarity(g2,g1);
		assertEquals(0.0,ret,0.001);
		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g2,g1);
		assertEquals(0.0,ret,0.001);
		
		g1 = FastGraph.randomGraphFactory(10, 20, 888, true, false);
		
		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g2,g1);
		assertEquals(1.0,ret,0.001);
		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g2,g1);
		assertEquals(1.0,ret,0.001);
		
		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g2,g1);
		assertEquals(1.0,ret,0.001);
		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g2,g1);
		assertEquals(1.0,ret,0.001);
		
	}
	
	@Test
	public void test002() throws FastGraphException {
		
		NeighbourhoodSimilarity ns1;
		double ret;
		
		FastGraph g1 = FastGraph.randomGraphFactory(5, 8, 888, true, false);
		FastGraph g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1, 999, false);

		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g1,g2);
		assertEquals(0.0,ret,0.001);

		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g2,g1);
		assertEquals(0.0,ret,0.001);

	}
	
	@Test
	public void test003() throws FastGraphException {
		
		NeighbourhoodSimilarity ns1;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		edges1.add(es10);
		edges1.add(es11);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		nodes2.add(ns24);
		nodes2.add(ns25);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 1, 3);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 1, 4);
		EdgeStructure es23 = new EdgeStructure(3,"es23", 0, (byte)0, (byte)0, 2, 3);
		EdgeStructure es24 = new EdgeStructure(4,"es24", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es25 = new EdgeStructure(5,"es25", 0, (byte)0, (byte)0, 4, 5);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		edges2.add(es23);
		edges2.add(es24);
		edges2.add(es25);
		List<NodeStructure> nodes3 = new ArrayList<NodeStructure>();
		NodeStructure ns30 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns31 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns32 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns33 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns34 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns35 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes3.add(ns30);
		nodes3.add(ns31);
		nodes3.add(ns32);
		nodes3.add(ns33);
		nodes3.add(ns34);
		nodes3.add(ns35);
		List<EdgeStructure> edges3 = new ArrayList<EdgeStructure>();
		EdgeStructure es30 = new EdgeStructure(0,"es30", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es31 = new EdgeStructure(1,"es31", 0, (byte)0, (byte)0, 1, 3);
		EdgeStructure es32 = new EdgeStructure(2,"es32", 0, (byte)0, (byte)0, 1, 4);
		EdgeStructure es33 = new EdgeStructure(3,"es33", 0, (byte)0, (byte)0, 2, 3);
		EdgeStructure es34 = new EdgeStructure(4,"es34", 0, (byte)0, (byte)0, 3, 4);
		EdgeStructure es35 = new EdgeStructure(5,"es35", 0, (byte)0, (byte)0, 4, 5);
		EdgeStructure es36 = new EdgeStructure(6,"es36", 0, (byte)0, (byte)0, 5, 0);
		edges3.add(es30);
		edges3.add(es31);
		edges3.add(es32);
		edges3.add(es33);
		edges3.add(es34);
		edges3.add(es35);
		edges3.add(es36);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		FastGraph g3 = FastGraph.structureFactory("g3", (byte)0, nodes3, edges3, false);
		
		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g1,g2);
		assertEquals(0.40604,ret,0.001);

		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g2,g1);
		assertEquals(0.40604,ret,0.001);
		
		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g1,g3);
		assertEquals(0.73611,ret,0.001);
		
		ret = ns1.similarity(g3,g1);
		assertEquals(0.73611,ret,0.001);
		
		ret = ns1.similarity(g2,g3);
		assertEquals(0.82958,ret,0.001);
		
		ret = ns1.similarity(g3,g2);
		assertEquals(0.85044,ret,0.001); // note, probably wrong, lacks symmetry
		
	}
	
	
	@Test
	public void test004() throws FastGraphException {
		
		NeighbourhoodSimilarity ns1;
		double ret;
		
		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es12", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es12 = new EdgeStructure(2,"es12", 0, (byte)0, (byte)0, 2, 0);
		edges1.add(es10);
		edges1.add(es11);
		edges1.add(es12);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 1, 2);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 0, 2);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		
		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g1,g2);
		assertEquals(0.0,ret,0.001);

		ns1 = new NeighbourhoodSimilarity(true);
		ret = ns1.similarity(g2,g1);
		assertEquals(0.0,ret,0.001);
		
		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g1,g2);
		assertEquals(1.0,ret,0.001);

		ns1 = new NeighbourhoodSimilarity(false);
		ret = ns1.similarity(g2,g1);
		assertEquals(1.0,ret,0.001);
		
	}
	
	@Test
	public void test005() throws FastGraphException {
		
		double ret;
		NeighbourhoodSimilarity ns;

		List<NodeStructure> nodes1 = new ArrayList<NodeStructure>();
		NodeStructure ns10 = new NodeStructure(0,"a", 0, (byte)0, (byte)0);
		NodeStructure ns11 = new NodeStructure(1,"a", 0, (byte)0, (byte)0);
		NodeStructure ns12 = new NodeStructure(2,"b", 0, (byte)0, (byte)0);
		nodes1.add(ns10);
		nodes1.add(ns11);
		nodes1.add(ns12);
		List<EdgeStructure> edges1 = new ArrayList<EdgeStructure>();
		EdgeStructure es10 = new EdgeStructure(0,"es10", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es11 = new EdgeStructure(1,"es11", 0, (byte)0, (byte)0, 1, 2);
		edges1.add(es10);
		edges1.add(es11);
		List<NodeStructure> nodes2 = new ArrayList<NodeStructure>();
		NodeStructure ns20 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns21 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns22 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns23 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns24 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns25 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes2.add(ns20);
		nodes2.add(ns21);
		nodes2.add(ns22);
		nodes2.add(ns23);
		nodes2.add(ns24);
		nodes2.add(ns25);
		List<EdgeStructure> edges2 = new ArrayList<EdgeStructure>();
		EdgeStructure es20 = new EdgeStructure(0,"es20", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es21 = new EdgeStructure(1,"es21", 0, (byte)0, (byte)0, 1, 3);
		EdgeStructure es22 = new EdgeStructure(2,"es22", 0, (byte)0, (byte)0, 1, 4);
		edges2.add(es20);
		edges2.add(es21);
		edges2.add(es22);
		List<NodeStructure> nodes3 = new ArrayList<NodeStructure>();
		NodeStructure ns30 = new NodeStructure(0,"b", 0, (byte)0, (byte)0);
		NodeStructure ns31 = new NodeStructure(1,"b", 0, (byte)0, (byte)0);
		NodeStructure ns32 = new NodeStructure(2,"a", 0, (byte)0, (byte)0);
		NodeStructure ns33 = new NodeStructure(3,"a", 0, (byte)0, (byte)0);
		NodeStructure ns34 = new NodeStructure(4,"b", 0, (byte)0, (byte)0);
		NodeStructure ns35 = new NodeStructure(5,"b", 0, (byte)0, (byte)0);
		nodes3.add(ns30);
		nodes3.add(ns31);
		nodes3.add(ns32);
		nodes3.add(ns33);
		nodes3.add(ns34);
		nodes3.add(ns35);
		List<EdgeStructure> edges3 = new ArrayList<EdgeStructure>();
		EdgeStructure es30 = new EdgeStructure(0,"es30", 0, (byte)0, (byte)0, 0, 1);
		EdgeStructure es31 = new EdgeStructure(1,"es31", 0, (byte)0, (byte)0, 1, 3);
		EdgeStructure es32 = new EdgeStructure(3,"es32", 0, (byte)0, (byte)0, 2, 3);
		edges3.add(es30);
		edges3.add(es31);
		edges3.add(es32);
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, nodes1, edges1, false);
		FastGraph g2 = FastGraph.structureFactory("g2", (byte)0, nodes2, edges2, false);
		FastGraph g3 = FastGraph.structureFactory("g3", (byte)0, nodes3, edges3, false);
		
        ns = new NeighbourhoodSimilarity(true);
        ret = ns.similarity(g1, g2);
		assertEquals(0.99982,ret,0.001);
        ret = ns.similarity(g2, g1);
		assertEquals(0.99982,ret,0.001);
        
        ns = new NeighbourhoodSimilarity(false);
        ret = ns.similarity(g1, g2);
		assertEquals(0.26667,ret,0.001);
        ret = ns.similarity(g2, g1);
		assertEquals(0.26667,ret,0.001);
		
        ns = new NeighbourhoodSimilarity(true);
        ret = ns.similarity(g1, g3);
		assertEquals(0.99971,ret,0.001);
        ret = ns.similarity(g3, g1);
		assertEquals(0.99971,ret,0.001);
        
        ns = new NeighbourhoodSimilarity(false);
        ret = ns.similarity(g1, g3);
		assertEquals(0.20001,ret,0.001);
        ret = ns.similarity(g3, g1);
		assertEquals(0.20001,ret,0.001);
		
        ns = new NeighbourhoodSimilarity(true);
        ret = ns.similarity(g3, g2);
		assertEquals(0.66654,ret,0.001);
        ret = ns.similarity(g2, g3);
		assertEquals(0.66654,ret,0.001);
        
        ns = new NeighbourhoodSimilarity(false);
        ret = ns.similarity(g3, g2);
		assertEquals(0.340914,ret,0.001);
        ret = ns.similarity(g2, g3);
		assertEquals(0.340914,ret,0.001);
		
		
        
	}
	
}
