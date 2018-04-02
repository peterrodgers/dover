package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

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
		similarity = ndd.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		// new test
		
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

		ndd = new NodeDegreeDifference();
		similarity = ndd.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		// new test
		
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
		similarity = ndd.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		// new test
		
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
		
		ndd = new NodeDegreeDifference(false);
		similarity = ndd.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
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
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		ndd = new NodeDegreeDifference(false);
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);

		// new test

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 0);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);

		// new test

		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 0);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		ndd = new NodeDegreeDifference();
		similarity = ndd.similarity(g1, g2);
		assertEquals(2.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(2.0, similarity, 0.001);

		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es11", 1, (byte)0, (byte)0, 1, 2);
		es2 = new EdgeStructure(2,"es21", 1, (byte)0, (byte)0, 2, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);

		// new test

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)1);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)1, 0, 1);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);

		// new test

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)1);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)1);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)1, 0, 1);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(6.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(6.0, similarity, 0.001);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)1);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)1);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)1, 0, 1);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)0, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(false);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);
	}
	
	
	@Test
	public void test003() throws IOException, FastGraphException {
		
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
	
		ndd = new NodeDegreeDifference(true);
		similarity = ndd.similarity(g1, g2);
		assertEquals(0.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addEdges = new LinkedList<EdgeStructure>();
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
	
		ndd = new NodeDegreeDifference(true);
		similarity = ndd.similarity(g1, g2);
		assertEquals(0.0, similarity, 0.001);
		
		// new test
		
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
		assertEquals(0.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)1, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
	
		similarity = ndd.similarity(g1, g2);
		assertEquals(0.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 2, 0);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 0, 1);
		es2 = new EdgeStructure(2,"es22", 1, (byte)0, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 1, 0);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es22", 1, (byte)0, (byte)0, 0, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
	
		similarity = ndd.similarity(g1, g2);
		assertEquals(0.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0.0, similarity, 0.001);
		
		
	}



	@Test
	public void test004() throws IOException, FastGraphException {
		
		double similarity;
		NodeStructure ns0,ns1,ns2,ns3;
		EdgeStructure es0,es1,es2,es3;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		NodeDegreeDifference ndd;

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
/*
System.out.println(Arrays.toString(g1.findDegrees()));
System.out.println(Arrays.toString(g2.findDegrees()));
g1.displayFastGraph();
g2.displayFastGraph();
try {
	Thread.sleep(10000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
*/
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)0, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)0, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 0);
		addEdges.add(es0);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		ndd = new NodeDegreeDifference(true);
		
		similarity = ndd.similarity(g1, g2);
		assertEquals(0.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(0.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 2, 0);
		es1 = new EdgeStructure(1,"es11", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es21", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 1, 0);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es22", 1, (byte)0, (byte)0, 0, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
	
		similarity = ndd.similarity(g1, g2);
		assertEquals(8.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(8.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)0);
		ns3 = new NodeStructure(3,"ns31", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es11", 1, (byte)0, (byte)0, 1, 2);
		es2 = new EdgeStructure(2,"es21", 1, (byte)0, (byte)0, 2, 3);
		es3 = new EdgeStructure(3,"es21", 1, (byte)0, (byte)0, 3, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		addEdges.add(es3);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)1, (byte)0);
		ns3 = new NodeStructure(3,"ns32", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 1, 2);
		es2 = new EdgeStructure(2,"es22", 1, (byte)0, (byte)0, 2, 3);
		es3 = new EdgeStructure(3,"es22", 1, (byte)0, (byte)0, 3, 1);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		addEdges.add(es3);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		similarity = ndd.similarity(g1, g2);
		assertEquals(4.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(4.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)0);
		ns3 = new NodeStructure(3,"ns31", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 2, 0);
		es1 = new EdgeStructure(1,"es11", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es21", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns02", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns12", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns22", 1, (byte)1, (byte)0);
		ns3 = new NodeStructure(3,"ns32", 1, (byte)1, (byte)1);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addNodes.add(ns3);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es02", 1, (byte)0, (byte)0, 1, 0);
		es1 = new EdgeStructure(1,"es12", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es22", 1, (byte)0, (byte)0, 0, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		similarity = ndd.similarity(g1, g2);
		assertEquals(12.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(12.0, similarity, 0.001);
		
		// new test
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"ns01", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"ns11", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"ns21", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 2, 0);
		es1 = new EdgeStructure(1,"es11", 1, (byte)0, (byte)0, 2, 1);
		es2 = new EdgeStructure(2,"es21", 1, (byte)0, (byte)0, 1, 0);
		addEdges.add(es0);
		addEdges.add(es1);
		addEdges.add(es2);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		similarity = ndd.similarity(g1, g2);
		assertEquals(6.0, similarity, 0.001);
		similarity = ndd.similarity(g2, g1);
		assertEquals(6.0, similarity, 0.001);
		
		
	}
	
	
	
}
