package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;
import uk.ac.kent.dover.fastGraph.InducedSubgraph;

public class InducedSubgraphTest {
	
	FastGraph g;
	LinkedList<Integer> nodes;
	LinkedList<Integer> edges;
	
	@Before
	public void prepare() {
		nodes = new LinkedList<>();
		edges = new LinkedList<>();
	}

	@Test
	public void test001() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{2,3,0}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{4,1}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,1,2}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{0,1,2}));
		
	}
	
	@Test
	public void test001b() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{2,3,0,1}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{4,1,2,3,0}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,1,2,4}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{0,1,5,2}));
		
	}
	
	@Test
	public void test002() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{2,1,0}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{0,1,2}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,2,1}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{1,2,0}));
		
	}
	
	@Test
	public void test002b() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{2,1,0,3}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{0,1,3,4,2}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,2,1,3}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{1,2,3,0,4}));
		
	}
	
	@Test
	public void test002c() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 5);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{2,1,0,3,4}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{0,1,3,5,4,2,6}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 5);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,2,1,3,4}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{1,2,3,5,0,4,6}));
		
	}
	
	
	@Test
	public void test003() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{1,2,0}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{2,0,1}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 3);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,2,1}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{1,0,2}));
		
		
	}
	
	@Test
	public void test003b() throws FastGraphException {		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),false);
		InducedSubgraph is = new InducedSubgraph(g);
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{1,2,0}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{2,0,1}));
		
		prepare();
		is.createInducedSubgraph(nodes, edges, 4);
		assertEquals(nodes.toString(),Arrays.toString(new int[]{0,2,1}));
		assertEquals(edges.toString(),Arrays.toString(new int[]{1,0,2}));
		
	}

}
