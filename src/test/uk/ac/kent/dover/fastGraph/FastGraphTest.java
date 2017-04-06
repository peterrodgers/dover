package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.NodeType;
import uk.ac.kent.dover.fastGraph.Connected;
import uk.ac.kent.dover.fastGraph.EdgeStructure;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphEdgeType;
import uk.ac.kent.dover.fastGraph.Launcher;
import uk.ac.kent.dover.fastGraph.NodeStructure;


/**
 * 
 * @author Peter Rodgers
 * @author Rob Baker
 *
 */
public class FastGraphTest {
	
	@Test
	public void test001() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		assertEquals(0,g.getNumberOfNodes());
	}
		
	@Test
	public void test002() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		assertEquals(0,g.getNumberOfEdges());
	}
	
	@Test
	public void test003() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test004() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),true);
		assertEquals(0,g.getNumberOfNodes());
		
	}
	
	@Test
	public void test005() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),true);
		assertEquals(0,g.getNumberOfEdges());
		
	}
	
	@Test
	public void test006() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),true);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test007() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(1,g.getNumberOfNodes());
	}
	
	@Test
	public void test008() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(0,g.getNumberOfEdges());
	}
	
	@Test
	public void test009() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test010() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),true);
		assertEquals(1,g.getNumberOfNodes());
	}
	
	@Test
	public void test011() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),true);
		assertEquals(0,g.getNumberOfEdges());
	}
	
	@Test
	public void test012() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),true);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test013() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(2,g.getNumberOfNodes());
	}
	
	@Test
	public void test014() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(0,g.getNumberOfEdges());
	}
	
	@Test
	public void test015() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertFalse(Connected.connected(g));
	}
	
	@Test
	public void test016() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals("node label 0",g.getNodeLabel(0));
	}
	
	@Test
	public void test017() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(12,g.getNodeWeight(0));
	}
	
	@Test
	public void test018() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(1,g.getNodeType(0));
	}
	
	@Test
	public void test019() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(0,g.getNodeAge(0));
	}
	
	@Test
	public void test020() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals("node label 1",g.getNodeLabel(1));
	}
	
	@Test
	public void test021() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(6,g.getNodeWeight(1));
	}
	
	@Test
	public void test022() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(0,g.getNodeType(1));
	}
	
	@Test
	public void test023() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),false);
		assertEquals(0,g.getNodeAge(1));
	}
	
	@Test
	public void test024() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(2,g.getNumberOfNodes());
	}
	
	@Test
	public void test025() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(0,g.getNumberOfEdges());
	}
	
	@Test
	public void test026() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertFalse(Connected.connected(g));
	}
	
	@Test
	public void test027() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals("node label 0",g.getNodeLabel(0));
	}
	
	@Test
	public void test028() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(12,g.getNodeWeight(0));
	}
	
	@Test
	public void test029() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(1,g.getNodeType(0));
	}
	
	@Test
	public void test030() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(0,g.getNodeAge(0));
	}
	
	@Test
	public void test031() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals("node label 1",g.getNodeLabel(1));
	}
	
	@Test
	public void test032() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(6,g.getNodeWeight(1));
	}
	
	@Test
	public void test033() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(0,g.getNodeType(1));
	}
	
	@Test
	public void test034() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node0Edge(),true);
		assertEquals(0,g.getNodeAge(1));
	}
	
	@Test
	public void test035() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(2,g.getNumberOfNodes());
	}
	
	@Test
	public void test036() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(1,g.getNumberOfEdges());
	}
	
	@Test
	public void test037() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test038() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),true);
		assertEquals(2,g.getNumberOfNodes());
	}
	
	@Test
	public void test039() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),true);
		assertEquals(1,g.getNumberOfEdges());
	}
	
	@Test
	public void test040() {
		
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),true);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test041() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(2,g.getNumberOfNodes());
	}
	
	@Test
	public void test042() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(2,g.getNumberOfEdges());
	}
	
	@Test
	public void test043() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test044() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals("node label 0",g.getNodeLabel(0));
	}
	
	@Test
	public void test045() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(12,g.getNodeWeight(0));
	}
	
	@Test
	public void test046() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(1,g.getNodeType(0));
	}
	
	@Test
	public void test047() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getNodeAge(0));
	}
	
	@Test
	public void test048() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals("edge label 0",g.getEdgeLabel(0));
	}
	
	@Test
	public void test049() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getEdgeNode1(0));
	}
	
	@Test
	public void test050() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(1,g.getEdgeNode2(0));
	}
	
	@Test
	public void test051() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(43,g.getEdgeWeight(0));
	}
	
	@Test
	public void test052() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(6,g.getEdgeType(0));
	}
	
	@Test
	public void test053() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getEdgeAge(0));
	}
	
	@Test
	public void test054() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals("edge label 1",g.getEdgeLabel(1));
	}
	
	@Test
	public void test055() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getEdgeNode1(1));
	}
	
	@Test
	public void test056() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getEdgeNode2(1));
	}
	
	@Test
	public void test057() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(32,g.getEdgeWeight(1));
	}
	
	@Test
	public void test058() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(9,g.getEdgeType(1));
	}
	
	@Test
	public void test059() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(0,g.getEdgeAge(1));
	}
	
	@Test
	public void test060() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals("node label 0",g.getNodeLabel(0));
	}
	
	@Test
	public void test061() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(12,g.getNodeWeight(0));
	}
	
	@Test
	public void test062() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(1,g.getNodeType(0));
	}
	
	@Test
	public void test063() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getNodeAge(0));
	}
	
	@Test
	public void test064() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals("edge label 0",g.getEdgeLabel(0));
	}
	
	@Test
	public void test065() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getEdgeNode1(0));
	}
	
	@Test
	public void test066() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(1,g.getEdgeNode2(0));
	}
	
	@Test
	public void test067() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(43,g.getEdgeWeight(0));
	}
	
	@Test
	public void test068() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(6,g.getEdgeType(0));
	}
	
	@Test
	public void test069() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getEdgeAge(0));
	}
	
	@Test
	public void test070() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals("edge label 1",g.getEdgeLabel(1));
	}
	
	@Test
	public void test071() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getEdgeNode1(1));
	}
	
	@Test
	public void test072() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getEdgeNode2(1));
	}
	
	@Test
	public void test073() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(32,g.getEdgeWeight(1));
	}
	
	@Test
	public void test074() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(9,g.getEdgeType(1));
	}
	
	@Test
	public void test075() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(0,g.getEdgeAge(1));
	}
	
	@Test
	public void test076() throws Exception {
		FastGraph g = FastGraph.randomGraphFactory(0, 0, false);
		assertEquals(0,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test077() throws Exception {
		FastGraph g = FastGraph.randomGraphFactory(0, 0, true);
		assertEquals(0,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
		assertTrue(Connected.connected(g));
	}
	
	@Test
	public void test078() throws Exception {
		FastGraph g = FastGraph.randomGraphFactory(10, 5, -1, true, false);
		assertEquals(10,g.getNumberOfNodes());
		assertEquals(5,g.getNumberOfEdges());
		assertFalse(Connected.connected(g));
	}
	
	@Test
	public void test079() throws Exception {
		FastGraph g = FastGraph.randomGraphFactory(10, 5, true);
		assertEquals(10,g.getNumberOfNodes());
		assertEquals(5,g.getNumberOfEdges());
		assertFalse(Connected.connected(g));
	}
	
	@Test
	public void test080() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),true);
		assertFalse(Connected.connected(g));
	}
	
	@Test
	public void test081() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(0,g.getNodeInDegree(0));
		assertEquals(0,g.getNodeOutDegree(0));
		assertEquals(0,g.getNodeOutDegree(0));
	}

	@Test
	public void test082() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(0,g.getNodeInDegree(0));
		assertEquals(1,g.getNodeOutDegree(0));
		assertEquals(1,g.getNodeDegree(0));
	}
	
	@Test
	public void test083() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),true);
		assertEquals(1,g.getNodeInDegree(1));
		assertEquals(0,g.getNodeOutDegree(1));
		assertEquals(1,g.getNodeDegree(1));
	}
	
	@Test
	public void test084() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		assertEquals(0,g.getNodeInDegree(0));
		assertEquals(2,g.getNodeOutDegree(0));
		assertEquals(2,g.getNodeDegree(0));
	}
	
	@Test
	public void test085() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		assertEquals(2,g.getNodeInDegree(1));
		assertEquals(1,g.getNodeOutDegree(1));
		assertEquals(3,g.getNodeDegree(1));
	}
	
	@Test
	public void test086() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(1,g.getNodeInDegree(2));
		assertEquals(2,g.getNodeOutDegree(2));
		assertEquals(3,g.getNodeDegree(2));
	}
	
	@Test
	public void test087() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(2,g.getNodeInDegree(3));
		assertEquals(0,g.getNodeOutDegree(3));
		assertEquals(2,g.getNodeDegree(3));
	}
	
	@Test
	public void test088() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(1,g.getNodeInDegree(0));
		assertEquals(2,g.getNodeOutDegree(0));
		assertEquals(3,g.getNodeDegree(0));
	}
	
	@Test
	public void test089() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(1,g.getNodeInDegree(1));
		assertEquals(0,g.getNodeOutDegree(1));
		assertEquals(1,g.getNodeDegree(1));
	}
	
	@Test
	public void test090() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		g.saveBuffers(null,"test");
		FastGraph g2 = FastGraph.loadBuffersGraphFactory(null,"test");
		assertEquals("empty",g2.getName());
		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());
	}
	
	@Test
	public void test091() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		g.saveBuffers(null,"test");
		FastGraph g2 = FastGraph.loadBuffersGraphFactory(null,"test");
		assertEquals("four nodes, five edges",g2.getName());
		assertEquals(4,g2.getNumberOfNodes());
		assertEquals(5,g2.getNumberOfEdges());
		assertEquals(0,g2.getNodeInDegree(0));
		assertEquals(1,g2.getNodeOutDegree(1));
		assertEquals(2,g2.getNodeOutDegree(2));
		assertEquals(0,g2.getNodeOutDegree(3));
		assertEquals(2,g2.getNodeInDegree(3));
		assertEquals("node label 3",g2.getNodeLabel(3));
		assertEquals(3,g2.getNodeWeight(3));
		assertEquals(3,g2.getNodeType(3));
		assertEquals(0,g2.getNodeAge(3));
		assertEquals("edge label 4",g2.getEdgeLabel(4));
		assertEquals(4,g2.getEdgeWeight(4));
		assertEquals(4,g2.getEdgeType(4));
		assertEquals(0,g2.getEdgeAge(4));
		assertTrue(Connected.connected(g2));
	}

	@Test
	public void test092() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		int[] connections;
		connections = g.getNodeConnectingNodes(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingEdges(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingInNodes(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingInEdges(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingOutNodes(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingOutEdges(0);
		assertEquals(0,connections.length);
	}
	
	@Test
	public void test093() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),true);
		int[] connections;
		connections = g.getNodeConnectingNodes(0);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingEdges(0);
		assertEquals(1,connections.length);
		assertEquals(0,connections[0]);
		connections = g.getNodeConnectingInNodes(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingInEdges(0);
		assertEquals(0,connections.length);
		connections = g.getNodeConnectingOutNodes(0);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingOutEdges(0);
		assertEquals(1,connections.length);
		assertEquals(0,connections[0]);
		
		connections = new int[10];
		g.getNodeConnectingNodes(connections,0);
		assertEquals(1,connections[0]);
		g.getNodeConnectingEdges(connections,0);
		assertEquals(0,connections[0]);
		g.getNodeConnectingOutNodes(connections,0);
		assertEquals(1,connections[0]);
		g.getNodeConnectingOutEdges(connections,0);
		assertEquals(0,connections[0]);
	}

	@Test
	public void test094() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		int[] connections;
		connections = g.getNodeConnectingNodes(2);
		assertEquals(3,connections.length);
		assertEquals(3,connections[2]);
		connections = g.getNodeConnectingEdges(2);
		assertEquals(3,connections.length);
		assertEquals(4,connections[2]);
		connections = g.getNodeConnectingInNodes(2);
		assertEquals(1,connections.length);
		assertEquals(0,connections[0]);
		connections = g.getNodeConnectingInEdges(2);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingOutNodes(2);
		assertEquals(2,connections.length);
		assertEquals(3,connections[1]);
		connections = g.getNodeConnectingOutEdges(2);
		assertEquals(2,connections.length);
		assertEquals(4,connections[1]);
		
		connections = new int[10];
		g.getNodeConnectingNodes(connections,2);
		assertEquals(3,connections[2]);
		g.getNodeConnectingEdges(connections,2);
		assertEquals(4,connections[2]);
		g.getNodeConnectingInNodes(connections,2);
		assertEquals(0,connections[0]);
		g.getNodeConnectingInEdges(connections,2);
		assertEquals(1,connections[0]);
		g.getNodeConnectingOutNodes(connections,2);
		assertEquals(3,connections[1]);
		g.getNodeConnectingOutEdges(connections,2);
		assertEquals(4,connections[1]);

	}

	@Test
	public void test094a() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		int[] connections;
		connections = g.getNodeConnectingNodes(0);
		assertEquals(3,connections.length);
		assertEquals(0,connections[2]);
		connections = g.getNodeConnectingEdges(0);
		assertEquals(3,connections.length);
		assertEquals(1,connections[2]);
		connections = g.getNodeConnectingInNodes(0);
		assertEquals(1,connections.length);
		assertEquals(0,connections[0]);
		connections = g.getNodeConnectingInEdges(0);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingOutNodes(0);
		assertEquals(2,connections.length);
		assertEquals(0,connections[1]);
		connections = g.getNodeConnectingOutEdges(0);
		assertEquals(2,connections.length);
		assertEquals(1,connections[1]);

		connections = new int[10];
		g.getNodeConnectingNodes(connections,0);
		assertEquals(0,connections[2]);
		g.getNodeConnectingEdges(connections,0);
		assertEquals(1,connections[2]);
		g.getNodeConnectingInNodes(connections,0);
		assertEquals(0,connections[0]);
		g.getNodeConnectingInEdges(connections,0);
		assertEquals(1,connections[0]);
		g.getNodeConnectingOutNodes(connections,0);
		assertEquals(0,connections[1]);
		g.getNodeConnectingOutEdges(connections,0);
		assertEquals(1,connections[1]);
	}

	@Test
	public void test095() throws Exception {
		FastGraph g = FastGraph.adjacencyListGraphFactory(0, 0, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "testAdj1.txt", false);
		assertEquals(0,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
		assertTrue(Connected.connected(g));
	}

	@Test
	public void test096() throws Exception {
		FastGraph g = FastGraph.adjacencyListGraphFactory(2, 1, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "testAdj2.txt", true);
		assertEquals("testAdj2.txt",g.getName());
		assertEquals(2,g.getNumberOfNodes());
		assertEquals(1,g.getNumberOfEdges());
		int[] connections;
		connections = g.getNodeConnectingNodes(0);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingInEdges(1);
		assertEquals(1,connections.length);
		assertEquals(0,connections[0]);
		assertEquals("45",g.getNodeLabel(0));
		assertEquals("76",g.getNodeLabel(1));
		assertTrue(Connected.connected(g));
	}


	@Test
	public void test097() throws Exception {
		FastGraph g = FastGraph.adjacencyListGraphFactory(4, 4, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "testAdj3.txt", false);
		int[] connections;
		connections = g.getNodeConnectingOutNodes(0);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		connections = g.getNodeConnectingInEdges(2);
		assertEquals(1,connections.length);
		assertEquals(1,connections[0]);
		assertTrue(Connected.connected(g));
	}

	@Test
	public void test098() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		assertEquals(0,g.maximumDegree());
		assertEquals(0,g.maximumInDegree());
		assertEquals(0,g.maximumOutDegree());
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(0,g.maximumDegree());
		assertEquals(0,g.maximumInDegree());
		assertEquals(0,g.maximumOutDegree());
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(1,g.maximumDegree());
		assertEquals(1,g.maximumInDegree());
		assertEquals(1,g.maximumOutDegree());
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(3,g.maximumDegree());
		assertEquals(1,g.maximumInDegree());
		assertEquals(2,g.maximumOutDegree());
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(3,g.maximumDegree());
		assertEquals(2,g.maximumInDegree());
		assertEquals(2,g.maximumOutDegree());
	}
	
	@Test
	public void test102() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		Graph displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(0,displayGraph.getNodes().size());
		assertEquals(0,displayGraph.getEdges().size());
	}
	
	@Test
	public void test103() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		Graph displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(1,displayGraph.getNodes().size());
		assertEquals(0,displayGraph.getEdges().size());
		assertEquals(g.getNodeLabel(0),displayGraph.getNodes().get(0).getLabel());
		assertEquals(g.getNodeWeight(0),(int)(displayGraph.getNodes().get(0).getScore()));
		assertEquals(g.getNodeAge(0),(int)(displayGraph.getNodes().get(0).getAge()));
		assertEquals("age0",displayGraph.getNodes().get(0).getType().getLabel());
	}
	
	@Test
	public void test104() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		Graph displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(2,displayGraph.getNodes().size());
		assertEquals(1,displayGraph.getEdges().size());
		assertEquals(g.getNodeLabel(1),displayGraph.getNodes().get(1).getLabel());
		assertEquals(g.getNodeWeight(1),(int)(displayGraph.getNodes().get(1).getScore()));
		assertEquals(g.getNodeAge(1),(int)(displayGraph.getNodes().get(1).getAge()));
		assertEquals("age0",displayGraph.getNodes().get(1).getType().getLabel());
		assertEquals("edge label 0",displayGraph.getEdges().get(0).getLabel());
		assertEquals(g.getEdgeWeight(0),(int)(displayGraph.getEdges().get(0).getScore()));
		assertEquals(g.getEdgeAge(0),(int)(displayGraph.getEdges().get(0).getAge()));
		assertEquals(Byte.toString(g.getEdgeType(0)),displayGraph.getEdges().get(0).getType().getLabel());
		assertEquals(g.getNodeLabel(0),displayGraph.getEdges().get(0).getFrom().getLabel());
		assertEquals(g.getNodeLabel(1),displayGraph.getEdges().get(0).getTo().getLabel());
	}
	
	@Test
	public void test105() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		Graph displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(2,displayGraph.getNodes().size());
		assertEquals(2,displayGraph.getEdges().size());
		assertEquals(g.getNodeLabel(0),displayGraph.getNodes().get(0).getLabel());
		assertEquals(g.getNodeWeight(0),(int)(displayGraph.getNodes().get(0).getScore()));
		assertEquals(g.getNodeAge(0),(int)(displayGraph.getNodes().get(0).getAge()));
		assertEquals("age0",displayGraph.getNodes().get(0).getType().getLabel());
		assertEquals(g.getEdgeLabel(1),displayGraph.getEdges().get(1).getLabel());
		assertEquals(g.getEdgeWeight(1),(int)(displayGraph.getEdges().get(1).getScore()));
		assertEquals(Byte.toString(g.getEdgeType(1)),displayGraph.getEdges().get(1).getType().getLabel());
		assertEquals(g.getNodeLabel(0),displayGraph.getEdges().get(1).getFrom().getLabel());
		assertEquals(g.getNodeLabel(0),displayGraph.getEdges().get(1).getTo().getLabel());
	}
	
	@Test
	public void test106() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		Graph displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(5,displayGraph.getNodes().size());
		assertEquals(5,displayGraph.getEdges().size());
		assertEquals(g.getNodeLabel(4),displayGraph.getNodes().get(4).getLabel());
		assertEquals(g.getNodeWeight(4),(int)(displayGraph.getNodes().get(4).getScore()));
		assertEquals(g.getNodeAge(4),(int)(displayGraph.getNodes().get(4).getAge()));
		assertEquals("age0",displayGraph.getNodes().get(4).getType().getLabel());
		assertEquals(g.getEdgeLabel(4),displayGraph.getEdges().get(4).getLabel());
		assertEquals(g.getEdgeWeight(4),(int)(displayGraph.getEdges().get(4).getScore()));
		assertEquals(g.getEdgeAge(4),(int)(displayGraph.getEdges().get(4).getAge()));
		assertEquals(Byte.toString(g.getEdgeType(4)),displayGraph.getEdges().get(4).getType().getLabel());
		assertEquals(g.getNodeLabel(3),displayGraph.getEdges().get(4).getFrom().getLabel());
		assertEquals(g.getNodeLabel(4),displayGraph.getEdges().get(4).getTo().getLabel());
	}
	
	@Test
	public void test107() {
		Graph displayGraph = new Graph("empty");
		FastGraph g = FastGraph.displayGraphFactory(displayGraph,false);
		
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(g.getNumberOfNodes(),displayGraph.getNodes().size());
		assertEquals(g.getNumberOfEdges(),displayGraph.getEdges().size());
		Graph displayGraph2 = g.generateDisplayGraph();
		assertTrue(displayGraph2.consistent());
		assertTrue(displayGraph.isomorphic(displayGraph2));
	}
	
	@Test
	public void test108() {
		Graph displayGraph = new Graph("one node");
		Node n1 = new Node("node 1");
		n1.setScore(4.0);
		n1.setAge(1);
		n1.setType(new NodeType("3"));
		displayGraph.addNode(n1);
		FastGraph g = FastGraph.displayGraphFactory(displayGraph,false);
		
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(g.getNumberOfNodes(),displayGraph.getNodes().size());
		assertEquals(g.getNumberOfEdges(),displayGraph.getEdges().size());
		assertEquals(g.getNodeWeight(0),(int)(displayGraph.getNodes().get(0).getScore()));
		assertEquals(Byte.toString(g.getNodeType(0)),displayGraph.getNodes().get(0).getType().getLabel());
		assertEquals(g.getNodeAge(0),displayGraph.getNodes().get(0).getAge());
		assertEquals(g.getNodeLabel(0),displayGraph.getNodes().get(0).getLabel());
		assertEquals(g.getNodeInDegree(0),0);
		assertEquals(g.getNodeOutDegree(0),0);
		Graph displayGraph2 = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		assertTrue(displayGraph.isomorphic(displayGraph2));
	}
	
	@Test
	public void test109() {
		Graph displayGraph = new Graph("two nodes, one edge");
		Node n1 = new Node("node 1");
		n1.setScore(4.0);
		n1.setAge(1);
		n1.setType(new NodeType("3"));
		displayGraph.addNode(n1);
		Node n2 = new Node("node 2");
		n2.setScore(-4.0);
		n2.setAge(1);
		n2.setType(new NodeType("text label"));
		displayGraph.addNode(n2);
		Edge e1 = new Edge(n1,n2,"edge 1");
		e1.setScore(0.0);
		e1.setAge(1);
		e1.setType(new EdgeType("14"));
		displayGraph.addEdge(e1);
		FastGraph g = FastGraph.displayGraphFactory(displayGraph,false);
		
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(g.getNumberOfNodes(),displayGraph.getNodes().size());
		assertEquals(g.getNumberOfEdges(),displayGraph.getEdges().size());
		assertEquals(g.getNodeWeight(1),(int)(displayGraph.getNodes().get(1).getScore()));
		assertEquals(g.getNodeType(1),-1);
		assertEquals(g.getNodeAge(1),displayGraph.getNodes().get(1).getAge());
		assertEquals(g.getNodeLabel(1),displayGraph.getNodes().get(1).getLabel());
		assertEquals(1,g.getNodeInDegree(1));
		assertEquals(0,g.getNodeOutDegree(1));
		assertEquals(0,g.getNodeInDegree(0));
		assertEquals(1,g.getNodeOutDegree(0));
		assertEquals(g.getEdgeWeight(0),(int)(displayGraph.getEdges().get(0).getScore()));
		assertEquals(Byte.toString(g.getEdgeType(0)),displayGraph.getEdges().get(0).getType().getLabel());
		assertEquals(g.getEdgeAge(0),displayGraph.getEdges().get(0).getAge());
		assertEquals(g.getEdgeLabel(0),displayGraph.getEdges().get(0).getLabel());
		assertEquals(g.getNodeLabel(g.getEdgeNode1(0)),"node 1");
		assertEquals(g.getNodeLabel(g.getEdgeNode2(0)),"node 2");
		Graph displayGraph2 = g.generateDisplayGraph();
		assertTrue(displayGraph2.consistent());
		assertTrue(displayGraph.isomorphic(displayGraph2));
	}
	
	@Test
	public void test110() {
		Graph displayGraph = new Graph("four nodes, four edges");
		Node n1 = new Node("node 1");
		n1.setScore(4.0);
		n1.setAge(1);
		n1.setType(new NodeType("3"));
		displayGraph.addNode(n1);
		Node n2 = new Node("node 2");
		n2.setScore(-4.0);
		n2.setAge(1);
		n2.setType(new NodeType("text label"));
		displayGraph.addNode(n2);
		Node n3 = new Node("node label 3");
		n3.setScore(0);
		n3.setAge(1);
		n3.setType(new NodeType("90"));
		displayGraph.addNode(n3);
		Node n4 = new Node("node 4");
		n4.setScore(45);
		n4.setAge(1);
		n4.setType(new NodeType("65"));
		displayGraph.addNode(n4);
		Edge e1 = new Edge(n1,n2,"edge 1");
		e1.setScore(0.0);
		e1.setAge(1);
		e1.setType(new EdgeType("14"));
		displayGraph.addEdge(e1);
		Edge e2 = new Edge(n1,n1,"edge 2 label");
		e2.setScore(12);
		e2.setAge(1);
		e2.setType(new EdgeType("0"));
		displayGraph.addEdge(e2);
		Edge e3 = new Edge(n3,n1,"edge 3");
		e3.setScore(121);
		e3.setAge(1);
		e3.setType(new EdgeType("21"));
		displayGraph.addEdge(e3);
		Edge e4 = new Edge(n3,n1,"edge 4");
		e4.setScore(123);
		e4.setAge(1);
		e4.setType(new EdgeType("text label"));
		displayGraph.addEdge(e4);
		FastGraph g = FastGraph.displayGraphFactory(displayGraph,false);
		
		assertEquals(g.getName(),displayGraph.getLabel());
		assertEquals(g.getNumberOfNodes(),displayGraph.getNodes().size());
		assertEquals(g.getNumberOfEdges(),displayGraph.getEdges().size());
		assertEquals(g.getNodeWeight(3),(int)(displayGraph.getNodes().get(3).getScore()));
		assertEquals(Byte.toString(g.getNodeType(3)),displayGraph.getNodes().get(3).getType().getLabel());
		assertEquals(g.getNodeAge(3),displayGraph.getNodes().get(3).getAge());
		assertEquals(g.getNodeLabel(3),displayGraph.getNodes().get(3).getLabel());
		assertEquals(0,g.getNodeInDegree(3));
		assertEquals(0,g.getNodeOutDegree(3));
		assertEquals(3,g.getNodeInDegree(0));
		assertEquals(2,g.getNodeOutDegree(0));
		assertEquals(g.getEdgeWeight(3),(int)(displayGraph.getEdges().get(3).getScore()));
		assertEquals(g.getEdgeType(3),-1);
		assertEquals(g.getEdgeAge(3),displayGraph.getEdges().get(3).getAge());
		assertEquals(g.getEdgeLabel(3),displayGraph.getEdges().get(3).getLabel());
		assertEquals(g.getNodeLabel(g.getEdgeNode1(3)),"node label 3");
		assertEquals(g.getNodeLabel(g.getEdgeNode2(3)),"node 1");
		Graph displayGraph2 = g.generateDisplayGraph();
		assertTrue(displayGraph2.consistent());
		assertTrue(displayGraph.isomorphic(displayGraph2));
		assertTrue(displayGraph.consistent());
	}
	

	@Test
	public void test111() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		String[] nodeLabels = {};
		g.setAllNodeLabels(nodeLabels);
		
		String[] edgeLabels = {};
		g.setAllEdgeLabels(edgeLabels);
	}
	
	
	@Test
	public void test112() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		String[] nodeLabels = {"node label 1"};
		g.setAllNodeLabels(nodeLabels);
		assertEquals(g.getNodeLabel(0),nodeLabels[0]);
	}
	
	@Test
	public void test113() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		String[] edgeLabels = {"edge label 1"};
		g.setAllEdgeLabels(edgeLabels);
		assertEquals(g.getEdgeLabel(0),edgeLabels[0]);
	}
	
	@Test
	public void test114() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),false);
		String[] nodeLabels = {"node label 1","","a","qwertyuiop.4","5"};
		g.setAllNodeLabels(nodeLabels);
		assertEquals(g.getNodeLabel(0),nodeLabels[0]);
		assertEquals(g.getNodeLabel(1),nodeLabels[1]);
		assertEquals(g.getNodeLabel(2),nodeLabels[2]);
		assertEquals(g.getNodeLabel(3),nodeLabels[3]);
		assertEquals(g.getNodeLabel(4),nodeLabels[4]);
		
		String[] edgeLabels = {"edge label 1","","a","lkjhgfdsa.4","8"};
		g.setAllEdgeLabels(edgeLabels);
		assertEquals(g.getEdgeLabel(0),edgeLabels[0]);
		assertEquals(g.getEdgeLabel(1),edgeLabels[1]);
		assertEquals(g.getEdgeLabel(2),edgeLabels[2]);
		assertEquals(g.getEdgeLabel(3),edgeLabels[3]);

	}
	
	
	@Test
	public void test115() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		String[] nodeLabels = {"","node label 2","a","qwertyuiop.4","5"};
		g.setAllNodeLabels(nodeLabels);
		assertEquals(g.getNodeLabel(0),nodeLabels[0]);
		assertEquals(g.getNodeLabel(1),nodeLabels[1]);
		String[] nodeLabels2 = {"label1","test 2","three","-four-","5,5"};
		g.setAllNodeLabels(nodeLabels2);
		assertEquals(g.getNodeLabel(0),nodeLabels2[0]);
		assertEquals(g.getNodeLabel(1),nodeLabels2[1]);
		
		String[] edgeLabels = {"","edge label 2"};
		g.setAllEdgeLabels(edgeLabels);
		assertEquals(g.getEdgeLabel(0),edgeLabels[0]);
		String[] edgeLabels2 = {"e1","e2.2"};
		g.setAllEdgeLabels(edgeLabels2);
		assertEquals(g.getEdgeLabel(0),edgeLabels2[0]);

	}
	
	
	@Test
	public void test116() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		g.setNodeWeight(0,33);
		g.setNodeType(0,(byte)(34));
		g.setNodeAge(0,(byte)(35));
		assertEquals(g.getNodeWeight(0),33);
		assertEquals(g.getNodeType(0),34);
		assertEquals(g.getNodeAge(0),35);
	}

	@Test
	public void test117() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),false);
		g.setNodeWeight(4,43);
		g.setNodeType(4,(byte)(44));
		g.setNodeAge(4,(byte)(45));
		assertEquals(g.getNodeWeight(4),43);
		assertEquals(g.getNodeType(4),44);
		assertEquals(g.getNodeAge(4),45);
	}
	
	@Test
	public void test118() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		g.setEdgeWeight(0,63);
		g.setEdgeType(0,(byte)(64));
		g.setEdgeAge(0,(byte)(65));
		assertEquals(g.getEdgeWeight(0),63);
		assertEquals(g.getEdgeType(0),64);
		assertEquals(g.getEdgeAge(0),65);
	}

	@Test
	public void test119() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		g.setEdgeWeight(4,73);
		g.setEdgeType(4,(byte)(74));
		g.setEdgeAge(4,(byte)(75));
		assertEquals(g.getEdgeWeight(4),73);
		assertEquals(g.getEdgeType(4),74);
		assertEquals(g.getEdgeAge(4),75);
	}

	@Test
	public void test120() {
		FastGraph g1;
		int[] nodes;
		int[] edges;
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		nodes = new int[0];
		edges = new int[0];
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		assertEquals(0,g1.getNumberOfNodes());
		assertEquals(0,g1.getNumberOfEdges());
		
		nodes = new int[1];
		edges = new int[0];
		nodes[0] = 4;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		assertEquals(g1.getNumberOfNodes(),1);
		assertEquals(g1.getNumberOfEdges(),0);
		assertEquals(g1.getNodeLabel(0),"node label 4");
		assertEquals(41,g1.getNodeWeight(0));
		assertEquals(42,g1.getNodeType(0));
		assertEquals(0,g1.getNodeAge(0));

		nodes = new int[2];
		edges = new int[1];
		nodes[0] = 4;
		nodes[1] = 3;
		edges[0] = 4;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		assertEquals(g1.getNumberOfNodes(),2);
		assertEquals(g1.getNumberOfEdges(),1);
		assertEquals(g1.getNodeLabel(1),"node label 3");
		assertEquals(31,g1.getNodeWeight(1));
		assertEquals(32,g1.getNodeType(1));
		assertEquals(0,g1.getNodeAge(1));
		assertEquals(g1.getEdgeLabel(0),"edge label 4");
		assertEquals(44,g1.getEdgeWeight(0));
		assertEquals(45,g1.getEdgeType(0));
		assertEquals(0,g1.getEdgeAge(0));

	}

	
	@Test
	public void test121() {
		FastGraph g1;
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		Graph displayGraph = new Graph("test graph ");
		
		int[] nodes;
		int[] edges;
		nodes = new int[1];
		edges = new int[0];
		nodes[0] = 0;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		Node n0 = new Node("n0");
		displayGraph.addNode(n0);
		assertTrue(displayGraph.isomorphic(g1.generateDisplayGraph()));
		assertTrue(displayGraph.consistent());
		
		nodes = new int[2];
		edges = new int[0];
		nodes[0] = 0;
		nodes[1] = 1;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		Node n1 = new Node("n1");
		displayGraph.addNode(n1);
		assertTrue(displayGraph.isomorphic(g1.generateDisplayGraph()));
		assertTrue(displayGraph.consistent());
		
		nodes = new int[2];
		edges = new int[1];
		nodes[0] = 0;
		nodes[1] = 1;
		edges[0] = 0;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		Edge e0 = new Edge(n0,n1,"e1");
		displayGraph.addEdge(e0);
		assertTrue(displayGraph.isomorphic(g1.generateDisplayGraph()));
		assertTrue(displayGraph.consistent());
		
		nodes = new int[3];
		edges = new int[3];
		nodes[0] = 2;
		nodes[1] = 0;
		nodes[2] = 1;
		edges[0] = 2;
		edges[1] = 0;
		edges[2] = 1;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		Node n2 = new Node("n2");
		displayGraph.addNode(n2);
		Edge e1 = new Edge(n2,n1,"e1");
		Edge e2 = new Edge(n2,n0,"e2");
		displayGraph.addEdge(e1);
		displayGraph.addEdge(e2);
		assertTrue(displayGraph.isomorphic(g1.generateDisplayGraph()));
		assertTrue(displayGraph.consistent());

		nodes = new int[5];
		edges = new int[5];
		nodes[0] = 4;
		nodes[1] = 3;
		nodes[2] = 2;
		nodes[3] = 1;
		nodes[4] = 0;
		edges[0] = 4;
		edges[1] = 3;
		edges[2] = 2;
		edges[3] = 1;
		edges[4] = 0;
		g1 = g.generateGraphFromSubgraph(nodes,edges);
		Node n3 = new Node("n3");
		Node n4 = new Node("n4");
		displayGraph.addNode(n3);
		displayGraph.addNode(n4);
		Edge e3 = new Edge(n2,n3,"e3");
		Edge e4 = new Edge(n4,n3,"e4");
		displayGraph.addEdge(e3);
		displayGraph.addEdge(e4);
		assertTrue(displayGraph.isomorphic(g1.generateDisplayGraph()));
		assertTrue(displayGraph.consistent());

	}

	
	@Test
	public void test122() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		assertEquals(g.oppositeEnd(4,3),4);
		assertEquals(g.oppositeEnd(4,4),3);
		assertEquals(g.oppositeEnd(0,1),0);
		assertEquals(g.oppositeEnd(0,0),1);
	}
		
	@Test
	public void test123() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(g.oppositeEnd(1,0),0);
	}

	@Test
	public void test124() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		FastGraph g1;
		Graph displayGraph;
		int[] nodes;
		int[] edges;
		
		nodes = new int[0];
		edges = new int[0];
		g1 = g.generateGraphByDeletingItems(nodes,edges,true);
		assertEquals(5,g1.getNumberOfNodes());
		assertEquals(5,g1.getNumberOfEdges());
		assertEquals(5,g1.getNumberOfEdges());
		displayGraph = g1.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		
		nodes = new int[0];
		edges = new int[1];
		edges[0] = 1;
		g1 = g.generateGraphByDeletingItems(nodes,edges,true);
		assertEquals(5,g1.getNumberOfNodes());
		assertEquals(4,g1.getNumberOfEdges());
		assertEquals("edge label 4",g1.getEdgeLabel(3));
		displayGraph = g1.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		
		nodes = new int[1];
		nodes[0] = 1;
		edges = new int[0];
		g1 = g.generateGraphByDeletingItems(nodes,edges,true);
		assertEquals(4,g1.getNumberOfNodes());
		assertEquals(3,g1.getNumberOfEdges());
		assertEquals("node label 2",g1.getNodeLabel(1));
		assertEquals("edge label 1",g1.getEdgeLabel(0));
		displayGraph = g1.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		
		nodes = new int[0];
		edges = new int[5];
		edges[0] = 1;
		edges[1] = 0;
		edges[2] = 4;
		edges[3] = 3;
		edges[4] = 2;
		g1 = g.generateGraphByDeletingItems(nodes,edges,true);
		assertEquals(5,g1.getNumberOfNodes());
		assertEquals(0,g1.getNumberOfEdges());
		displayGraph = g1.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		
		nodes = new int[5];
		nodes[0] = 0;
		nodes[1] = 1;
		nodes[2] = 2;
		nodes[3] = 3;
		nodes[4] = 4;
		edges = new int[0];
		g1 = g.generateGraphByDeletingItems(nodes,edges,true);
		assertEquals(0,g1.getNumberOfNodes());
		assertEquals(0,g1.getNumberOfEdges());
		displayGraph = g1.generateDisplayGraph();
		assertTrue(displayGraph.consistent());
		
	}

	@Test
	public void test125() throws Exception {
		FastGraph g = FastGraph.nodeListEdgeListGraphFactory(0, 0, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "test1", false);
		assertEquals(0,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
	}

	@Test
	public void test126() throws Exception {
		FastGraph g = FastGraph.nodeListEdgeListGraphFactory(1, 0, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "test2", false);
		assertEquals(1,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
		assertEquals("node label 1",g.getNodeLabel(0));
		assertEquals(23,g.getNodeWeight(0));
		assertEquals(12,g.getNodeType(0));
		assertEquals(0,g.getNodeAge(0));
	}

	@Test
	public void test127() throws Exception {
		Graph displayGraph;
		FastGraph g = FastGraph.nodeListEdgeListGraphFactory(2, 1, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "test3", false);

		assertEquals(2,g.getNumberOfNodes());
		assertEquals(1,g.getNumberOfEdges());
		assertEquals("node label 2",g.getNodeLabel(1));
		assertEquals(33,g.getNodeWeight(1));
		assertEquals(32,g.getNodeType(1));
		assertEquals(0,g.getNodeAge(1));
		assertEquals(1,g.getEdgeNode1(0));
		assertEquals(0,g.getEdgeNode2(0));
		assertEquals("edge label 1",g.getEdgeLabel(0));
		assertEquals(223,g.getEdgeWeight(0));
		assertEquals(43,g.getEdgeType(0));
		assertEquals(0,g.getEdgeAge(0));
		displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());

	}

	@Test
	public void test128() throws Exception {
		Graph displayGraph;
		FastGraph g = FastGraph.nodeListEdgeListGraphFactory(3, 4, Launcher.startingWorkingDirectory+File.separatorChar+"testData", "test4", false);

		assertEquals(3,g.getNumberOfNodes());
		assertEquals(4,g.getNumberOfEdges());
		assertEquals("node label 3",g.getNodeLabel(2));
		assertEquals(1,g.getNodeWeight(2));
		assertEquals(2,g.getNodeType(2));
		assertEquals(0,g.getNodeAge(2));
		assertEquals(2,g.getEdgeNode1(3));
		assertEquals(2,g.getEdgeNode2(3));
		assertEquals("edge label 4",g.getEdgeLabel(3));
		assertEquals(4,g.getEdgeWeight(3));
		assertEquals(5,g.getEdgeType(3));
		assertEquals(0,g.getEdgeAge(3));
		displayGraph = g.generateDisplayGraph();
		assertTrue(displayGraph.consistent());

	}

	@Test
	public void test129() throws Exception {
		// bug with too small node degree. Fixed by changing node degrees to ints
		int overMaxShort = Short.MAX_VALUE*2;
		FastGraph g = FastGraph.randomGraphFactory(1, overMaxShort, false);
		assertEquals(overMaxShort,g.getNodeOutDegree(0));
		assertEquals(overMaxShort,g.getNodeInDegree(0));
		assertEquals(overMaxShort*2,g.getNodeDegree(0));
		assertEquals(overMaxShort-1,g.getNodeConnectingEdges(0)[overMaxShort-1]);
		assertEquals(0,g.getNodeConnectingNodes(0)[overMaxShort-1]);

	}

	@Test
	public void test130() {
		FastGraph g;
		ByteBuffer nodeBuf, connectionBuf, edgeBuf;
		int offset;
		
		g= FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		assertTrue(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		assertTrue(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		nodeBuf = g.getNodeBuf();
		offset = FastGraph.NODE_IN_CONNECTION_START_OFFSET+2*FastGraph.NODE_BYTE_SIZE;
		nodeBuf.putInt(offset,5);
		assertFalse(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		nodeBuf = g.getNodeBuf();
		offset = FastGraph.NODE_OUT_CONNECTION_START_OFFSET+1*FastGraph.NODE_BYTE_SIZE;
		nodeBuf.putInt(offset,3);
		assertFalse(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		connectionBuf = g.getConnectionBuf();
		offset = 55;
		connectionBuf.putInt(offset,3);
		assertFalse(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		edgeBuf = g.getEdgeBuf();
		offset = FastGraph.EDGE_NODE1_OFFSET+3*FastGraph.EDGE_BYTE_SIZE;
		edgeBuf.putInt(offset,1);
		assertFalse(g.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		edgeBuf = g.getEdgeBuf();
		offset = FastGraph.EDGE_NODE2_OFFSET+0*FastGraph.EDGE_BYTE_SIZE;
		edgeBuf.putInt(offset,0);
		assertFalse(g.checkConsistency());
		
	}
		

	
	@Test
	public void test131() {
		FastGraph g, g2;
		LinkedList<int[]> rewiring;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		rewiring = new LinkedList<int[]>();
		g2 = g.generateRewiredGraph(rewiring);
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		rewiring = new LinkedList<int[]>();
		int[] r1 = {3,1,2};
		rewiring.add(r1);
		g2 = g.generateRewiredGraph(rewiring);
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertEquals(1,g2.getEdgeNode1(3));
		assertEquals(2,g2.getEdgeNode2(3));
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		rewiring = new LinkedList<int[]>();
		int[] r2 = {0,0,0};
		int[] r3 = {1,3,1};
		rewiring.add(r1);
		rewiring.add(r2);
		rewiring.add(r3);
		g2 = g.generateRewiredGraph(rewiring);
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertEquals(1,g2.getEdgeNode1(3));
		assertEquals(2,g2.getEdgeNode2(3));
		assertEquals(0,g2.getEdgeNode1(0));
		assertEquals(0,g2.getEdgeNode2(0));
		assertEquals(3,g2.getEdgeNode1(1));
		assertEquals(1,g2.getEdgeNode2(1));
	}

	
	@Test
	public void test132() {
		FastGraph g;
		int[] arr;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get0Node0Edge(),false);
		arr = g.degreeProfile();
		int[] correct1 = new int[0];
		Assert.assertArrayEquals(correct1,arr);
		arr = g.inDegreeProfile();
		int[] correct1a = new int[0];
		Assert.assertArrayEquals(correct1a,arr);
		arr = g.outDegreeProfile();
		int[] correct1b = new int[0];
		Assert.assertArrayEquals(correct1b,arr);
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		arr = g.degreeProfile();
		int[] correct2 = {1};
		Assert.assertArrayEquals(correct2,arr);
		arr = g.inDegreeProfile();
		int[] correct2a = {1};
		Assert.assertArrayEquals(correct2a,arr);
		arr = g.outDegreeProfile();
		int[] correct2b = {1};
		Assert.assertArrayEquals(correct2b,arr);
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		arr = g.degreeProfile();
		int[] correct3 = {0,1,3,1};
		Assert.assertArrayEquals(correct3,arr);
		arr = g.inDegreeProfile();
		int[] correct3a = {1,3,1};
		Assert.assertArrayEquals(correct3a,arr);
		arr = g.outDegreeProfile();
		int[] correct3b = {1,3,1};
		Assert.assertArrayEquals(correct3b,arr);
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		arr = g.degreeProfile();
		int[] correct4 = {0,1,0,1};
		Assert.assertArrayEquals(correct4,arr);
		arr = g.inDegreeProfile();
		int[] correct4a = {0,2};
		Assert.assertArrayEquals(correct4a,arr);
		arr = g.outDegreeProfile();
		int[] correct4b = {1,0,1};
		Assert.assertArrayEquals(correct4b,arr);
		
	}

	
	@Test
	public void test133() {
		FastGraph g,g1;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		g1 = g.generateRandomRewiredGraph(5,888);
		assertTrue(g1.checkConsistency());
		assertTrue((g.getEdgeNode1(1) != g1.getEdgeNode1(1)) || (g.getEdgeNode2(1) != g1.getEdgeNode2(1)));
		assertTrue((g.getEdgeNode1(0) != g1.getEdgeNode1(0)) || (g.getEdgeNode1(1) != g1.getEdgeNode1(1)) || (g.getEdgeNode1(2) != g1.getEdgeNode1(2)) || (g.getEdgeNode1(3) != g1.getEdgeNode1(3)) || (g.getEdgeNode1(4) != g1.getEdgeNode1(4)));
		assertTrue((g.getEdgeNode2(0) != g1.getEdgeNode2(0)) || (g.getEdgeNode2(1) != g1.getEdgeNode2(1)) || (g.getEdgeNode2(2) != g1.getEdgeNode2(2)) || (g.getEdgeNode2(3) != g1.getEdgeNode2(3)) || (g.getEdgeNode2(4) != g1.getEdgeNode2(4)));
		Assert.assertArrayEquals(g.inDegreeProfile(),g1.inDegreeProfile());
		Assert.assertArrayEquals(g.outDegreeProfile(),g1.outDegreeProfile());
		
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		g1 = g.generateRandomRewiredGraph(10,111);
		assertTrue(g1.checkConsistency());
		assertTrue((g.getEdgeNode1(1) != g1.getEdgeNode1(1)) || (g.getEdgeNode2(1) != g1.getEdgeNode2(1)));
		Assert.assertArrayEquals(g.inDegreeProfile(),g1.inDegreeProfile());
		Assert.assertArrayEquals(g.outDegreeProfile(),g1.outDegreeProfile());
		
	}
	
	
	@Test
	public void test134() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Node n2 = new Node("nA");
		displayGraph1.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		displayGraph1.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		displayGraph1.addEdge(e2);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(1,g1.edgesBetween(0, 1).size());
		assertTrue(g1.edgesBetween(0, 1).get(0) == 0);
	}
	
	
	@Test
	public void test135() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Node n2 = new Node("nA");
		displayGraph1.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		displayGraph1.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		displayGraph1.addEdge(e2);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(1,g1.edgesBetween(0, 1).size());
		assertTrue(g1.edgesBetween(0, 1).get(0) == 0);
	}
	
	
	@Test
	public void test136() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Edge e0 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e0);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(1,g1.edgesBetween(0, 1).size());
		assertTrue(g1.edgesBetween(0, 1).get(0) == 0);
	}
	
	
	@Test
	public void test137() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Node n2 = new Node("nC");
		displayGraph1.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e0);
		Edge e1 = new Edge(n1,n2,"eB");
		displayGraph1.addEdge(e1);
		Edge e2 = new Edge(n1,n0,"eA");
		displayGraph1.addEdge(e2);
		Edge e3 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e3);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(3,g1.edgesBetween(0, 1).size());
		assertTrue(g1.edgesBetween(0, 1).get(0) == 2);
		assertTrue(g1.edgesBetween(0, 1).get(1) == 0);
		assertTrue(g1.edgesBetween(0, 1).get(2) == 3);
	}
	
	
	@Test
	public void test138() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Node n2 = new Node("nA");
		displayGraph1.addNode(n2);
		Edge e0 = new Edge(n0,n1,"eA");
		displayGraph1.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		displayGraph1.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		displayGraph1.addEdge(e2);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(1,g1.edgesBetween(0, 1).size());
		assertTrue(g1.edgesBetween(0, 1).get(0) == 0);
	}
	
	
	@Test
	public void test139() {
		Graph displayGraph1 = new Graph("");
		Node n0 = new Node("nA");
		displayGraph1.addNode(n0);
		Node n1 = new Node("nB");
		displayGraph1.addNode(n1);
		Node n2 = new Node("nA");
		displayGraph1.addNode(n2);
		Edge e0 = new Edge(n2,n1,"eA");
		displayGraph1.addEdge(e0);
		Edge e1 = new Edge(n0,n2,"eB");
		displayGraph1.addEdge(e1);
		Edge e2 = new Edge(n1,n2,"eA");
		displayGraph1.addEdge(e2);
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph1,false);

		assertEquals(0,g1.edgesBetween(0, 1).size());
	}
	
	
	@Test
	public void test140() {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		
		assertEquals(0,g.findMaximumNodeAge());
		assertEquals(0,g.findMinimumNodeAge());
		assertEquals(0,g.findMaximumEdgeAge());
		assertEquals(0,g.findMinimumEdgeAge());
	}
	
	
	
	@Test
	public void test141() {
		Graph displayGraph = new Graph();
	
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());

	}

	
	@Test
	public void test142() {
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		displayGraph.addNode(n0);
		n0.setAge(0);
		n0.setScore(77);
	
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();

		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(2,g2.getNumberOfNodes());
		assertEquals(1,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(0),g2.getNodeLabel(1));
		assertEquals(g2.getNodeType(0),g2.getNodeType(1));
		assertEquals(g2.getNodeWeight(0),g2.getNodeWeight(1));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(1,g2.getNodeAge(1));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(0));
		assertEquals(1,g2.getEdgeAge(0));
		
		FastGraph g3 = g2.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(3,g3.getNumberOfNodes());
		assertEquals(2,g3.getNumberOfEdges());
		
		assertEquals(g3.getNodeLabel(0),g1.getNodeLabel(0));
		assertEquals(g3.getNodeType(0),g1.getNodeType(0));
		assertEquals(g3.getNodeWeight(0),g1.getNodeWeight(0));
		assertEquals(g3.getNodeLabel(0),g3.getNodeLabel(1));
		assertEquals(g3.getNodeType(0),g3.getNodeType(1));
		assertEquals(g3.getNodeWeight(0),g3.getNodeWeight(1));
		assertEquals(g3.getNodeLabel(1),g3.getNodeLabel(2));
		assertEquals(g3.getNodeType(1),g3.getNodeType(2));
		assertEquals(g3.getNodeWeight(1),g3.getNodeWeight(2));
		assertEquals(0,g3.getNodeAge(0));
		assertEquals(1,g3.getNodeAge(1));
		assertEquals(2,g3.getNodeAge(2));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(0));
		assertEquals(1,g3.getEdgeAge(0));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(1));
		assertEquals(2,g3.getEdgeAge(1));
		
		assertEquals(0,g3.getEdgeNode1(0));
		assertEquals(1,g3.getEdgeNode2(0));
		assertEquals(1,g3.getEdgeNode1(1));
		assertEquals(2,g3.getEdgeNode2(1));
		

	}
	
	@Test
	public void test143() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		n0.setAge(0);
		n1.setAge(0);
		n0.setScore(77);
		n1.setScore(77);
		Edge e0 = new Edge(n0,n1,"e0");
		e0.setAge(0);
		e0.setScore(88);
		displayGraph.addEdge(e0);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(4,g2.getNumberOfNodes());
		assertEquals(4,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(0),g2.getNodeLabel(2));
		assertEquals(g2.getNodeType(0),g2.getNodeType(2));
		assertEquals(g2.getNodeWeight(0),g2.getNodeWeight(2));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(1,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		
		assertEquals(g2.getEdgeLabel(0),g2.getEdgeLabel(3));
		assertEquals(g2.getEdgeType(0),g2.getEdgeType(3));
		assertEquals(g2.getEdgeWeight(0),g2.getEdgeWeight(3));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(0));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(1,g2.getEdgeAge(3));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(1));
		assertEquals(1,g2.getEdgeAge(1));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(2));
		assertEquals(1,g2.getEdgeAge(1));

		assertEquals(0,g2.getEdgeNode1(0));
		assertEquals(1,g2.getEdgeNode2(0));
		assertEquals(0,g2.getEdgeNode1(1));
		assertEquals(2,g2.getEdgeNode2(1));
		assertEquals(1,g2.getEdgeNode1(2));
		assertEquals(3,g2.getEdgeNode2(2));
		assertEquals(2,g2.getEdgeNode1(3));
		assertEquals(3,g2.getEdgeNode2(3));

	}
	
	@Test
	public void test144() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		displayGraph.addNode(n2);
		n0.setAge(0);
		n1.setAge(0);
		n2.setAge(0);
		n0.setScore(77);
		n1.setScore(66);
		n2.setScore(55);
		Edge e0 = new Edge(n0,n1,"e0");
		Edge e1 = new Edge(n2,n1,"e1");
		Edge e2 = new Edge(n0,n2,"e2");
		e0.setAge(0);
		e1.setAge(0);
		e2.setAge(0);
		e0.setScore(11);
		e1.setScore(12);
		e2.setScore(13);
		displayGraph.addEdge(e0);
		displayGraph.addEdge(e1);
		displayGraph.addEdge(e2);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(6,g2.getNumberOfNodes());
		assertEquals(9,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(2),g2.getNodeLabel(5));
		assertEquals(g2.getNodeType(2),g2.getNodeType(5));
		assertEquals(g2.getNodeWeight(2),g2.getNodeWeight(5));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(0,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		assertEquals(1,g2.getNodeAge(4));
		assertEquals(1,g2.getNodeAge(5));
		
		assertEquals(g2.getEdgeLabel(2),g2.getEdgeLabel(8));
		assertEquals(g2.getEdgeType(2),g2.getEdgeType(8));
		assertEquals(g2.getEdgeWeight(2),g2.getEdgeWeight(8));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(0,g2.getEdgeAge(1));
		assertEquals(0,g2.getEdgeAge(2));
		assertEquals(1,g2.getEdgeAge(3));
		assertEquals(1,g2.getEdgeAge(4));
		assertEquals(1,g2.getEdgeAge(5));
		assertEquals(1,g2.getEdgeAge(6));
		assertEquals(1,g2.getEdgeAge(7));
		assertEquals(1,g2.getEdgeAge(8));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(3));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(4));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(5));

		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(2));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(8));

		assertEquals(0,g2.getEdgeNode1(3));
		assertEquals(3,g2.getEdgeNode2(3));
		assertEquals(1,g2.getEdgeNode1(4));
		assertEquals(4,g2.getEdgeNode2(4));
		assertEquals(2,g2.getEdgeNode1(5));
		assertEquals(5,g2.getEdgeNode2(5));

		assertEquals(0,g2.getEdgeNode1(2));
		assertEquals(2,g2.getEdgeNode2(2));
		
		assertEquals(3,g2.getEdgeNode1(8));
		assertEquals(5,g2.getEdgeNode2(8));
		
		
		FastGraph g3 = g2.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(9,g3.getNumberOfNodes());
		assertEquals(15,g3.getNumberOfEdges());
		
		assertEquals(g3.getNodeLabel(5),g3.getNodeLabel(8));
		assertEquals(g3.getNodeType(5),g3.getNodeType(8));
		assertEquals(g3.getNodeWeight(5),g3.getNodeWeight(8));
		assertEquals(0,g3.getNodeAge(0));
		assertEquals(0,g3.getNodeAge(1));
		assertEquals(0,g3.getNodeAge(2));
		assertEquals(1,g3.getNodeAge(3));
		assertEquals(1,g3.getNodeAge(4));
		assertEquals(1,g3.getNodeAge(5));
		assertEquals(2,g3.getNodeAge(6));
		assertEquals(2,g3.getNodeAge(7));
		assertEquals(2,g3.getNodeAge(8));
		
		assertEquals(g3.getEdgeLabel(2),g3.getEdgeLabel(14));
		assertEquals(g3.getEdgeType(2),g3.getEdgeType(14));
		assertEquals(g3.getEdgeWeight(2),g3.getEdgeWeight(14));
		assertEquals(g3.getEdgeLabel(8),g3.getEdgeLabel(14));
		assertEquals(g3.getEdgeType(8),g3.getEdgeType(14));
		assertEquals(g3.getEdgeWeight(8),g3.getEdgeWeight(14));
		assertEquals(0,g3.getEdgeAge(0));
		assertEquals(0,g3.getEdgeAge(1));
		assertEquals(0,g3.getEdgeAge(2));
		assertEquals(1,g3.getEdgeAge(3));
		assertEquals(1,g3.getEdgeAge(4));
		assertEquals(1,g3.getEdgeAge(5));
		assertEquals(1,g3.getEdgeAge(6));
		assertEquals(1,g3.getEdgeAge(7));
		assertEquals(1,g3.getEdgeAge(8));
		assertEquals(2,g3.getEdgeAge(9));
		assertEquals(2,g3.getEdgeAge(10));
		assertEquals(2,g3.getEdgeAge(11));
		assertEquals(2,g3.getEdgeAge(12));
		assertEquals(2,g3.getEdgeAge(13));
		assertEquals(2,g3.getEdgeAge(14));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(3));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(9));

		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(1));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(7));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g3.getEdgeType(13));

		assertEquals(0,g3.getEdgeNode1(3));
		assertEquals(3,g3.getEdgeNode2(3));
		assertEquals(3,g3.getEdgeNode1(9));
		assertEquals(6,g3.getEdgeNode2(9));

		assertEquals(0,g3.getEdgeNode1(2));
		assertEquals(2,g3.getEdgeNode2(2));
		
		assertEquals(3,g3.getEdgeNode1(8));
		assertEquals(5,g3.getEdgeNode2(8));
		
		assertEquals(6,g3.getEdgeNode1(14));
		assertEquals(8,g3.getEdgeNode2(14));

	}
	
	
	@Test
	public void test145() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		displayGraph.addNode(n2);
		n0.setAge(0);
		n1.setAge(0);
		n2.setAge(0);
		n0.setScore(77);
		n1.setScore(66);
		n2.setScore(55);
		Edge e0 = new Edge(n0,n1,"e0");
		Edge e1 = new Edge(n2,n1,"e1");
		Edge e2 = new Edge(n0,n2,"e2");
		e0.setAge(0);
		e1.setAge(0);
		e2.setAge(0);
		e0.setScore(11);
		e1.setScore(12);
		e2.setScore(13);
		displayGraph.addEdge(e0);
		displayGraph.addEdge(e1);
		displayGraph.addEdge(e2);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		deleteNodes.add(1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(5,g2.getNumberOfNodes());
		assertEquals(6,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(2),g2.getNodeLabel(4));
		assertEquals(g2.getNodeType(2),g2.getNodeType(4));
		assertEquals(g2.getNodeWeight(2),g2.getNodeWeight(4));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(0,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		assertEquals(1,g2.getNodeAge(4));
		
		assertEquals(g2.getEdgeLabel(2),g2.getEdgeLabel(5));
		assertEquals(g2.getEdgeType(2),g2.getEdgeType(5));
		assertEquals(g2.getEdgeWeight(2),g2.getEdgeWeight(5));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(0,g2.getEdgeAge(1));
		assertEquals(0,g2.getEdgeAge(2));
		assertEquals(1,g2.getEdgeAge(3));
		assertEquals(1,g2.getEdgeAge(4));
		assertEquals(1,g2.getEdgeAge(5));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(3));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(4));
		
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(0));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(5));

		assertEquals(0,g2.getEdgeNode1(3));
		assertEquals(3,g2.getEdgeNode2(3));
		assertEquals(2,g2.getEdgeNode1(4));
		assertEquals(4,g2.getEdgeNode2(4));

		assertEquals(0,g2.getEdgeNode1(2));
		assertEquals(2,g2.getEdgeNode2(2));
		
		assertEquals(3,g2.getEdgeNode1(5));
		assertEquals(4,g2.getEdgeNode2(5));

	}
	
	@Test
	public void test146() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		displayGraph.addNode(n2);
		n0.setAge(0);
		n1.setAge(0);
		n2.setAge(0);
		n0.setScore(77);
		n1.setScore(66);
		n2.setScore(55);
		Edge e0 = new Edge(n0,n1,"e0");
		Edge e1 = new Edge(n2,n1,"e1");
		Edge e2 = new Edge(n0,n2,"e2");
		e0.setAge(0);
		e1.setAge(0);
		e2.setAge(0);
		e0.setScore(11);
		e1.setScore(12);
		e2.setScore(13);
		displayGraph.addEdge(e0);
		displayGraph.addEdge(e1);
		displayGraph.addEdge(e2);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		deleteEdges.add(1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(6,g2.getNumberOfNodes());
		assertEquals(8,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(1),g2.getNodeLabel(4));
		assertEquals(g2.getNodeType(1),g2.getNodeType(4));
		assertEquals(g2.getNodeWeight(1),g2.getNodeWeight(4));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(0,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		assertEquals(1,g2.getNodeAge(4));
		assertEquals(1,g2.getNodeAge(5));
		
		assertEquals(g2.getEdgeLabel(2),g2.getEdgeLabel(7));
		assertEquals(g2.getEdgeType(2),g2.getEdgeType(7));
		assertEquals(g2.getEdgeWeight(2),g2.getEdgeWeight(7));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(0,g2.getEdgeAge(1));
		assertEquals(0,g2.getEdgeAge(2));
		assertEquals(1,g2.getEdgeAge(3));
		assertEquals(1,g2.getEdgeAge(4));
		assertEquals(1,g2.getEdgeAge(5));
		assertEquals(1,g2.getEdgeAge(6));
		assertEquals(1,g2.getEdgeAge(7));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(3));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(4));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(5));

		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(2));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(6));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(7));

		assertEquals(0,g2.getEdgeNode1(3));
		assertEquals(3,g2.getEdgeNode2(3));
		assertEquals(1,g2.getEdgeNode1(4));
		assertEquals(4,g2.getEdgeNode2(4));
		assertEquals(2,g2.getEdgeNode1(5));
		assertEquals(5,g2.getEdgeNode2(5));

		assertEquals(0,g2.getEdgeNode1(2));
		assertEquals(2,g2.getEdgeNode2(2));
		
		assertEquals(3,g2.getEdgeNode1(7));
		assertEquals(5,g2.getEdgeNode2(7));
		

	}
	
	
	@Test
	public void test147() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		displayGraph.addNode(n2);
		n0.setAge(0);
		n1.setAge(0);
		n2.setAge(0);
		n0.setScore(77);
		n1.setScore(66);
		n2.setScore(55);
		Edge e0 = new Edge(n0,n1,"e0");
		Edge e1 = new Edge(n2,n1,"e1");
		Edge e2 = new Edge(n0,n2,"e2");
		e0.setAge(0);
		e1.setAge(0);
		e2.setAge(0);
		e0.setScore(11);
		e1.setScore(12);
		e2.setScore(13);
		displayGraph.addEdge(e0);
		displayGraph.addEdge(e1);
		displayGraph.addEdge(e2);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns1 = new NodeStructure(111,"ns1", 33, (byte)8, (byte)112);
		addNodes.add(ns1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(7,g2.getNumberOfNodes());
		assertEquals(9,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(2),g2.getNodeLabel(5));
		assertEquals(g2.getNodeType(2),g2.getNodeType(5));
		assertEquals(g2.getNodeWeight(2),g2.getNodeWeight(5));
		assertEquals("ns1",g2.getNodeLabel(6));
		assertEquals(8,g2.getNodeType(6));
		assertEquals(33,g2.getNodeWeight(6));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(0,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		assertEquals(1,g2.getNodeAge(4));
		assertEquals(1,g2.getNodeAge(5));
		assertEquals(1,g2.getNodeAge(6));
		
		assertEquals(g2.getEdgeLabel(2),g2.getEdgeLabel(8));
		assertEquals(g2.getEdgeType(2),g2.getEdgeType(8));
		assertEquals(g2.getEdgeWeight(2),g2.getEdgeWeight(8));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(1,g2.getEdgeAge(4));
		assertEquals(1,g2.getEdgeAge(8));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(5));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(8));

		assertEquals(0,g2.getEdgeNode1(3));
		assertEquals(3,g2.getEdgeNode2(3));
		assertEquals(1,g2.getEdgeNode1(4));
		assertEquals(4,g2.getEdgeNode2(4));
		assertEquals(2,g2.getEdgeNode1(5));
		assertEquals(5,g2.getEdgeNode2(5));

		assertEquals(0,g2.getEdgeNode1(2));
		assertEquals(2,g2.getEdgeNode2(2));
		
		assertEquals(3,g2.getEdgeNode1(8));
		assertEquals(5,g2.getEdgeNode2(8));
	}
	
	
	@Test
	public void test148() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		Node n2 = new Node("n2");
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		displayGraph.addNode(n2);
		n0.setAge(0);
		n1.setAge(0);
		n2.setAge(0);
		n0.setScore(77);
		n1.setScore(66);
		n2.setScore(55);
		Edge e0 = new Edge(n0,n1,"e0");
		Edge e1 = new Edge(n2,n1,"e1");
		e0.setAge(0);
		e1.setAge(0);
		e0.setScore(11);
		e1.setScore(12);
		displayGraph.addEdge(e0);
		displayGraph.addEdge(e1);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		EdgeStructure es1 = new EdgeStructure(1,"es1", 54, (byte)64, (byte)74, 0, 2);
		addEdges.add(es1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		assertEquals(6,g2.getNumberOfNodes());
		assertEquals(8,g2.getNumberOfEdges());
		
		assertEquals(g2.getNodeLabel(0),g2.getNodeLabel(3));
		assertEquals(g2.getNodeType(0),g2.getNodeType(3));
		assertEquals(g2.getNodeWeight(0),g2.getNodeWeight(3));
		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		assertEquals(0,g2.getNodeAge(2));
		assertEquals(1,g2.getNodeAge(3));
		assertEquals(1,g2.getNodeAge(4));
		assertEquals(1,g2.getNodeAge(5));
		
		assertEquals(g2.getEdgeLabel(0),g2.getEdgeLabel(5));
		assertEquals(g2.getEdgeType(0),g2.getEdgeType(5));
		assertEquals(g2.getEdgeWeight(0),g2.getEdgeWeight(5));
		assertEquals("es1",g2.getEdgeLabel(7));
		assertEquals(64,g2.getEdgeType(7));
		assertEquals(54,g2.getEdgeWeight(7));
		assertEquals(0,g2.getEdgeAge(0));
		assertEquals(0,g2.getEdgeAge(1));
		assertEquals(1,g2.getEdgeAge(2));
		assertEquals(1,g2.getEdgeAge(3));
		assertEquals(1,g2.getEdgeAge(4));
		assertEquals(1,g2.getEdgeAge(5));
		assertEquals(1,g2.getEdgeAge(6));
		assertEquals(1,g2.getEdgeAge(7));

		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(2));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(3));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(4));

		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(1));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g2.getEdgeType(7));

		assertEquals(0,g2.getEdgeNode1(2));
		assertEquals(3,g2.getEdgeNode2(2));
		assertEquals(1,g2.getEdgeNode1(3));
		assertEquals(4,g2.getEdgeNode2(3));
		assertEquals(2,g2.getEdgeNode1(4));
		assertEquals(5,g2.getEdgeNode2(4));

		assertEquals(3,g2.getEdgeNode1(5));
		assertEquals(4,g2.getEdgeNode2(5));
		
		assertEquals(3,g2.getEdgeNode1(7));
		assertEquals(5,g2.getEdgeNode2(7));
		
	}
	
	
	@Test
	public void test149() {
		
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		n0.setAge(0);
		n1.setAge(0);
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"e0");
		e0.setAge(0);
		displayGraph.addEdge(e0);

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		
		assertEquals(0,g1.getGeneration());
		
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		deleteNodes.add(0);
		deleteNodes.add(1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(1,g2.getGeneration());
		
		assertEquals(2,g2.getNumberOfNodes());
		assertEquals(1,g2.getNumberOfEdges());

		assertEquals(0,g2.getNodeAge(0));
		assertEquals(0,g2.getNodeAge(1));
		
		assertEquals(0,g2.getEdgeAge(0));

		
		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns1 = new NodeStructure(111,"ns1", 33, (byte)8, (byte)112);
		NodeStructure ns2 = new NodeStructure(222,"ns2", 44, (byte)9, (byte)113);
		addNodes.add(ns1);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(1,"es1", 54, (byte)64, (byte)74, 111, 222);
		addEdges.add(es1);
		
		FastGraph g3 = g2.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals(2,g3.getGeneration());

		assertEquals(4,g3.getNumberOfNodes());
		assertEquals(2,g3.getNumberOfEdges());
		
		assertEquals(0,g3.getNodeAge(0));
		assertEquals(0,g3.getNodeAge(1));
		assertEquals(2,g3.getNodeAge(2));
		assertEquals(2,g3.getNodeAge(3));

		assertEquals(0,g3.getEdgeAge(0));
		assertEquals(2,g3.getEdgeAge(1));
		
	}
		
	
	@Test
	public void test150() {
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		n0.setAge(0);
		n1.setAge(0);
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"e0");
		e0.setAge(0);
		displayGraph.addEdge(e0);
		
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);

		assertEquals(0,g1.getGeneration());

	}
		
	@Test
	public void test151() {
		Graph displayGraph = new Graph();
		Node n0 = new Node("n0");
		Node n1 = new Node("n1");
		n0.setAge(0);
		n1.setAge(7);
		displayGraph.addNode(n0);
		displayGraph.addNode(n1);
		Edge e0 = new Edge(n0,n1,"e0");
		e0.setAge(0);
		displayGraph.addEdge(e0);
		
		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);

		assertEquals(7,g1.getGeneration());

	}
		

	@Test
	public void test152() {
		
		Graph displayGraph = new Graph();

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		g1.setName("g1");
		
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals("g1-1",g2.getName());

		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());

		
		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns0 = new NodeStructure(100, "ns0", 33, (byte)8, (byte)112);
		NodeStructure ns1 = new NodeStructure(101, "ns1", 44, (byte)18, (byte)112);
		EdgeStructure es0 = new EdgeStructure(201, "es0", 99, (byte)9, (byte)88, 101, 100);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges.add(es0);
		
		FastGraph g3 = g2.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals("g1-1-2",g3.getName());

		assertEquals(2,g3.getNumberOfNodes());
		assertEquals(1,g3.getNumberOfEdges());
		
		assertEquals("ns1",g3.getNodeLabel(1));
		assertEquals(18,g3.getNodeType(1));
		assertEquals(44,g3.getNodeWeight(1));
		
		assertEquals(2,g3.getNodeAge(0));
		assertEquals(2,g3.getNodeAge(1));
		
		assertEquals("es0",g3.getEdgeLabel(0));
		assertEquals(9,g3.getEdgeType(0));
		assertEquals(99,g3.getEdgeWeight(0));
		
		assertEquals(2,g3.getEdgeAge(0));

		assertEquals(1,g3.getEdgeNode1(0));
		assertEquals(0,g3.getEdgeNode2(0));
		
		
		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns2 = new NodeStructure(12,"ns2", 24, (byte)25, (byte)9);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(99,"es1", 34, (byte)44, (byte)54, 12, 0);
		addEdges.add(es1);

		FastGraph g4 = g3.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals("g1-1-2-3",g4.getName());

		assertEquals(5,g4.getNumberOfNodes());
		assertEquals(5,g4.getNumberOfEdges());
		
		assertEquals("ns2",g4.getNodeLabel(4));
		assertEquals(25,g4.getNodeType(4));
		assertEquals(24,g4.getNodeWeight(4));
		
		assertEquals(2,g4.getNodeAge(0));
		assertEquals(2,g4.getNodeAge(1));
		assertEquals(3,g4.getNodeAge(2));
		assertEquals(3,g4.getNodeAge(3));
		assertEquals(3,g4.getNodeAge(4));
		
		assertEquals("es1",g4.getEdgeLabel(4));
		assertEquals(44,g4.getEdgeType(4));
		assertEquals(34,g4.getEdgeWeight(4));
		
		assertEquals(2,g4.getEdgeAge(0));
		assertEquals(3,g4.getEdgeAge(1));
		assertEquals(3,g4.getEdgeAge(2));
		assertEquals(3,g4.getEdgeAge(3));
		assertEquals(3,g4.getEdgeAge(4));
		
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g4.getEdgeType(0));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g4.getEdgeType(1));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g4.getEdgeType(2));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g4.getEdgeType(3));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g4.getEdgeType(4));

		assertEquals(4,g4.getEdgeNode1(4));
		assertEquals(2,g4.getEdgeNode2(4));

		
		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		deleteNodes.add(2);
		deleteEdges.add(3);
		deleteEdges.add(4);
		NodeStructure ns3 = new NodeStructure(37,"ns3", 31, (byte)32, (byte)-1);
		addNodes.add(ns3);
		EdgeStructure es2 = new EdgeStructure(99,"es2", 71, (byte)72, (byte)-1, 3, 4);
		addEdges.add(es2);
		
		FastGraph g5 = g4.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		assertEquals("g1-1-2-3-4",g5.getName());

		assertEquals(8,g5.getNumberOfNodes());
		assertEquals(8,g5.getNumberOfEdges());
		
		assertEquals("ns3",g5.getNodeLabel(7));
		assertEquals(32,g5.getNodeType(7));
		assertEquals(31,g5.getNodeWeight(7));
		
		assertEquals(2,g5.getNodeAge(0));
		assertEquals(2,g5.getNodeAge(1));
		assertEquals(3,g5.getNodeAge(2));
		assertEquals(3,g5.getNodeAge(3));
		assertEquals(3,g5.getNodeAge(4));
		assertEquals(4,g5.getNodeAge(5));
		assertEquals(4,g5.getNodeAge(6));
		assertEquals(4,g5.getNodeAge(7));
				
		assertEquals("es2",g5.getEdgeLabel(7));
		assertEquals(72,g5.getEdgeType(7));
		assertEquals(71,g5.getEdgeWeight(7));
		
		assertEquals(2,g5.getEdgeAge(0));
		assertEquals(3,g5.getEdgeAge(1));
		assertEquals(3,g5.getEdgeAge(2));
		assertEquals(3,g5.getEdgeAge(3));
		assertEquals(3,g5.getEdgeAge(4));
		assertEquals(4,g5.getEdgeAge(5));
		assertEquals(4,g5.getEdgeAge(6));
		assertEquals(4,g5.getEdgeAge(7));
		
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(0));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(1));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(2));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(3));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(4));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(5));
		assertEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(6));
		assertNotEquals(FastGraphEdgeType.TIME.getValue(),g5.getEdgeType(7));

		assertEquals(5,g5.getEdgeNode1(7));
		assertEquals(6,g5.getEdgeNode2(7));
		
		assertEquals(4,g5.getEdgeNode1(6));
		assertEquals(6,g5.getEdgeNode2(6));

	}
	
	
	@Test
	public void test153() {

		ArrayList<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		ArrayList<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)3, addNodes, addEdges, true);
		
		assertEquals("g1",g1.getName());
		assertEquals(3,g1.getGeneration());
		assertEquals(0,g1.getNumberOfNodes());
		assertEquals(0,g1.getNumberOfEdges());
		
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		NodeStructure ns1 = new NodeStructure(0,"ns1", 31, (byte)32, (byte)4);
		NodeStructure ns2 = new NodeStructure(1,"ns2", 44, (byte)67, (byte)5);
		addNodes.add(ns1);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(0,"es1", 31, (byte)32, (byte)5, 1, 0);
		addEdges.add(es1);

		g1 = FastGraph.structureFactory("g1", (byte)3, addNodes, addEdges, false);
		
		assertEquals("g1",g1.getName());
		assertEquals(5,g1.getGeneration());
		assertEquals(2,g1.getNumberOfNodes());
		assertEquals(1,g1.getNumberOfEdges());
		
		assertEquals("ns1",g1.getNodeLabel(0));
		assertEquals(31,g1.getNodeWeight(0));
		assertEquals(32,g1.getNodeType(0));
		assertEquals(4,g1.getNodeAge(0));
		
		assertEquals("ns2",g1.getNodeLabel(1));
		assertEquals(44,g1.getNodeWeight(1));
		assertEquals(67,g1.getNodeType(1));
		assertEquals(5,g1.getNodeAge(1));
		
		assertEquals("es1",g1.getEdgeLabel(0));
		assertEquals(31,g1.getEdgeWeight(0));
		assertEquals(32,g1.getEdgeType(0));
		assertEquals(5,g1.getEdgeAge(0));
		assertEquals(1,g1.getEdgeNode1(0));
		assertEquals(0,g1.getEdgeNode2(0));
		
	}		

	
	@Test
	public void test154() {

		ArrayList<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		ArrayList<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)3, addNodes, addEdges, true);
		
		FastGraph g2 = g1.findGenerationSubGraph((byte)0, true);
		
		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());
		
		g2 = g1.findGenerationSubGraph((byte)3, true);
		
		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());
		
	}

	@Test
	public void test155() {

		ArrayList<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		ArrayList<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns1 = new NodeStructure(0,"ns1", 31, (byte)32, (byte)3);
		NodeStructure ns2 = new NodeStructure(1,"ns2", 44, (byte)67, (byte)3);
		addNodes.add(ns1);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(0,"es1", 31, (byte)32, (byte)3, 1, 0);
		addEdges.add(es1);

		FastGraph g1 = FastGraph.structureFactory("g1", (byte)3, addNodes, addEdges, true);
		
		FastGraph g2 = g1.findGenerationSubGraph((byte)0, true);
		
		assertEquals(0,g2.getNumberOfNodes());
		assertEquals(0,g2.getNumberOfEdges());
		
		g2 = g1.findGenerationSubGraph((byte)3, true);
		
		assertEquals(2,g2.getNumberOfNodes());
		assertEquals(1,g2.getNumberOfEdges());
		
	}

	@Test
	public void test156() {

		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		ArrayList<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		ArrayList<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g1 = FastGraph.structureFactory("g1", (byte)0, addNodes, addEdges, true);
		
		NodeStructure ns1 = new NodeStructure(0,"ns1", 31, (byte)32, (byte)1);
		NodeStructure ns2 = new NodeStructure(1,"ns2", 44, (byte)67, (byte)1);
		addNodes.add(ns1);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(0,"es1", 31, (byte)32, (byte)1, 0, 1);
		addEdges.add(es1);
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		FastGraph ga = g2.findGenerationSubGraph((byte)0, true);
		
		assertEquals("g1-1-sub0",ga.getName());
		
		assertEquals(0,ga.getNumberOfNodes());
		assertEquals(0,ga.getNumberOfEdges());
		
		ga = g2.findGenerationSubGraph((byte)1, true);
		
		assertEquals("g1-1-sub1",ga.getName());
		
		assertEquals(2,ga.getNumberOfNodes());
		assertEquals(1,ga.getNumberOfEdges());
		
		assertEquals("ns1",ga.getNodeLabel(0));
		assertEquals(31,ga.getNodeWeight(0));
		assertEquals(32,ga.getNodeType(0));
		assertEquals(1,ga.getNodeAge(0));
		
		assertEquals("ns2",ga.getNodeLabel(1));
		assertEquals(44,ga.getNodeWeight(1));
		assertEquals(67,ga.getNodeType(1));
		assertEquals(1,ga.getNodeAge(1));
		
		assertEquals("es1",ga.getEdgeLabel(0));
		assertEquals(31,ga.getEdgeWeight(0));
		assertEquals(32,ga.getEdgeType(0));
		assertEquals(1,ga.getEdgeAge(0));
		assertEquals(0,ga.getEdgeNode1(0));
		assertEquals(1,ga.getEdgeNode2(0));
		

		
	}

	
	@Test
	public void test157() {
		
		Graph displayGraph = new Graph();

		FastGraph g1 = FastGraph.displayGraphFactory(displayGraph, false);
		g1.setName("g1");
		
		Collection<Integer> deleteNodes = new ArrayList<Integer>();
		Collection<Integer> deleteEdges = new ArrayList<Integer>();
		Collection<NodeStructure> addNodes = new ArrayList<NodeStructure>();
		Collection<EdgeStructure> addEdges = new ArrayList<EdgeStructure>();
		
		FastGraph g2 = g1.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns0 = new NodeStructure(100, "ns0", 33, (byte)8, (byte)112);
		NodeStructure ns1 = new NodeStructure(101, "ns1", 44, (byte)18, (byte)112);
		EdgeStructure es0 = new EdgeStructure(201, "es0", 99, (byte)9, (byte)88, 101, 100);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges.add(es0);
		
		FastGraph g3 = g2.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);
		
		deleteNodes = new ArrayList<Integer>();
		deleteEdges = new ArrayList<Integer>();
		addNodes = new ArrayList<NodeStructure>();
		addEdges = new ArrayList<EdgeStructure>();
		
		NodeStructure ns2 = new NodeStructure(12,"ns2", 24, (byte)25, (byte)9);
		addNodes.add(ns2);
		EdgeStructure es1 = new EdgeStructure(99,"es1", 34, (byte)44, (byte)54, 12, 0);
		addEdges.add(es1);

		FastGraph g4 = g3.addNewTimeSlice(deleteNodes, deleteEdges, addNodes, addEdges, false);

		FastGraph ga = g4.findGenerationSubGraph((byte)0, true);
		
		assertEquals("g1-1-2-3-sub0",ga.getName());
		
		assertEquals(0,ga.getNumberOfNodes());
		assertEquals(0,ga.getNumberOfEdges());
		
		ga = g4.findGenerationSubGraph((byte)2, true);
		
		assertEquals("g1-1-2-3-sub2",ga.getName());
		
		assertEquals(2,ga.getNumberOfNodes());
		assertEquals(1,ga.getNumberOfEdges());
		
		assertEquals("ns0",ga.getNodeLabel(0));
		assertEquals(33,ga.getNodeWeight(0));
		assertEquals(8,ga.getNodeType(0));
		assertEquals(2,ga.getNodeAge(0));
		
		assertEquals("ns1",ga.getNodeLabel(1));
		assertEquals(44,ga.getNodeWeight(1));
		assertEquals(18,ga.getNodeType(1));
		assertEquals(2,ga.getNodeAge(1));
		
		assertEquals("es0",ga.getEdgeLabel(0));
		assertEquals(99,ga.getEdgeWeight(0));
		assertEquals(9,ga.getEdgeType(0));
		assertEquals(2,ga.getEdgeAge(0));
		assertEquals(1,ga.getEdgeNode1(0));
		assertEquals(0,ga.getEdgeNode2(0));
		
		ga = g4.findGenerationSubGraph((byte)3, true);
		
		assertEquals("g1-1-2-3-sub3",ga.getName());
		
		assertEquals(3,ga.getNumberOfNodes());
		assertEquals(2,ga.getNumberOfEdges());
		
		assertEquals("ns0",ga.getNodeLabel(0));
		assertEquals(33,ga.getNodeWeight(0));
		assertEquals(8,ga.getNodeType(0));
		assertEquals(3,ga.getNodeAge(0));
		
		assertEquals("ns1",ga.getNodeLabel(1));
		assertEquals(44,ga.getNodeWeight(1));
		assertEquals(18,ga.getNodeType(1));
		assertEquals(3,ga.getNodeAge(1));
		
		assertEquals("ns2",ga.getNodeLabel(2));
		assertEquals(24,ga.getNodeWeight(2));
		assertEquals(25,ga.getNodeType(2));
		assertEquals(3,ga.getNodeAge(2));
		
		assertEquals("es0",ga.getEdgeLabel(0));
		assertEquals(99,ga.getEdgeWeight(0));
		assertEquals(9,ga.getEdgeType(0));
		assertEquals(3,ga.getEdgeAge(0));
		assertEquals(1,ga.getEdgeNode1(0));
		assertEquals(0,ga.getEdgeNode2(0));
		
		assertEquals("es1",ga.getEdgeLabel(1));
		assertEquals(34,ga.getEdgeWeight(1));
		assertEquals(44,ga.getEdgeType(1));
		assertEquals(3,ga.getEdgeAge(1));
		assertEquals(2,ga.getEdgeNode1(1));
		assertEquals(0,ga.getEdgeNode2(1));
	
	}
}