package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.NodeDegreeDifference;

public class NodeDegreeDifferenceTest {

	@Test
	public void test001() throws IOException, FastGraphException {
		
		double similarity;
		NodeStructure ns0,ns1,ns2;
		EdgeStructure es0,es1,es2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		NodeDegreeDifference ndd;
		
		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(0, similarity, 0.001);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		similarity = ndd.similarity(g1, g2);

		ndd = new NodeDegreeDifference(false);
		
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es1", 2, (byte)0, (byte)0, 1, 2);
		es2 = new EdgeStructure(2,"es2", 2, (byte)0, (byte)0, 2, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)1, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es1", 2, (byte)2, (byte)0, 0, 2);
		es2 = new EdgeStructure(2,"es2", 2, (byte)3, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(0, similarity, 0.001);

		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)1, 2, 2);
		es1 = new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)0, (byte)1);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)1, 0, 0);
		es1 = new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(0, similarity, 0.001);

		
		
	}
	
	
	@Test
	public void test002() throws IOException, FastGraphException {
		
		double similarity;
		NodeStructure ns0,ns1,ns2;
		EdgeStructure es0,es1,es2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		NodeDegreeDifference ndd;
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(2, similarity, 0.001);

		ndd = new NodeDegreeDifference(false);
		
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es1", 2, (byte)0, (byte)0, 1, 2);
		es2 = new EdgeStructure(2,"es2", 2, (byte)0, (byte)0, 2, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)1, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(4, similarity, 0.001);

		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)1, 2, 2);
		es1 = new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns0", 1, (byte)0, (byte)1);
		ns1 = new NodeStructure(1,"ns1", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns2", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es0", 1, (byte)0, (byte)1, 0, 0);
		es1 = new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
/*
System.out.println(g1.getEdgeNode1(0));
System.out.println(g1.getEdgeNode2(0));
System.out.println(g2.getEdgeNode1(0));
System.out.println(g2.getEdgeNode2(0));
g1.displayFastGraph();
g2.displayFastGraph();
try {
	Thread.sleep(10000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
*/
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);

		assertEquals(2, similarity, 0.001);

		
		
	}
	
	
	
}
