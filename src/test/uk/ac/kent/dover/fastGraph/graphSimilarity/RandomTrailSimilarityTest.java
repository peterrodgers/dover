package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.RandomTrailSimilarity;

public class RandomTrailSimilarityTest {
	
	
	@Test
	public void test001() throws FastGraphException {
		
		double similarity;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		
		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,false,System.currentTimeMillis());
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,System.currentTimeMillis());
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,System.currentTimeMillis());
		rts.setTrailLength(6);
		rts.setTrailsPerNode(12);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
		
		assertEquals(6, rts.getTrailLength());
		assertEquals(12, rts.getTrailsPerNode());
	}
	
	
	@Test
	public void test002() throws FastGraphException {
		
		double similarity;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1));
		
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"A", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 1, 0));
		
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,false,System.currentTimeMillis());
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,System.currentTimeMillis());
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,System.currentTimeMillis());
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
		
		assertEquals(1, rts.getMapping()[0]);
		assertEquals(0, rts.getMapping()[1]);
		
		assertEquals(0, rts.getCostMatrix()[0][1], 0.001);
		assertEquals(0, rts.getCostMatrix()[1][0], 0.001);

	}
	
	
	
	@Test
	public void test003() throws FastGraphException {
		
		double similarity;
		NodeStructure ns0,ns1,ns2;
		EdgeStructure es0,es1;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		int trials = 15;
		int length = 3;
		
		addNodes = new LinkedList<NodeStructure>();
		ns0 = new NodeStructure(0,"A", 1, (byte)1, (byte)0);
		ns1 = new NodeStructure(1,"B", 1, (byte)1, (byte)0);
		ns2 = new NodeStructure(2,"C", 1, (byte)1, (byte)0);
		addNodes.add(ns0);
		addNodes.add(ns1);
		addNodes.add(ns2);
		addEdges = new LinkedList<EdgeStructure>();
		es0 = new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1);
		es1 = new EdgeStructure(1,"es02", 1, (byte)0, (byte)0, 1, 2);
		addEdges.add(es0);
		addEdges.add(es1);
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,false,4455);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,887766);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,886633);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
	}
	
	
	@Test
	public void test004() throws FastGraphException {
		
		double similarity;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		int trials = 8;
		int length = 3;
		
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"C", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 0));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 2, 0));
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"C", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"A", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"esa", 1, (byte)0, (byte)0, 2, 1));
		addEdges.add(new EdgeStructure(1,"esb", 1, (byte)0, (byte)0, 2, 2));
		addEdges.add(new EdgeStructure(2,"esc", 1, (byte)0, (byte)0, 0, 2));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,false,777444);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,333222);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,111222);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
	}
	
	
	@Test
	public void test005() throws FastGraphException {
		
		double similarity;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		int trials = 7;
		int length = 7;
		
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"C", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 2));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 2, 1));
		addEdges.add(new EdgeStructure(3,"es3", 1, (byte)0, (byte)0, 1, 2));
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"C", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"A", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"esa", 1, (byte)0, (byte)0, 2, 1));
		addEdges.add(new EdgeStructure(1,"esb", 1, (byte)0, (byte)0, 1, 0));
		addEdges.add(new EdgeStructure(2,"esc", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(3,"esd", 1, (byte)0, (byte)0, 1, 0));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,false,666555444);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,112299);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,996622);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
	}
	
	
	@Test
	public void test006() throws FastGraphException {
		
		double similarity;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		int trials = 14;
		int length = 6;
		
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"C", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(3,"D", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 2, 0));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 2, 1));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 2, 3));
		addEdges.add(new EdgeStructure(3,"es3", 1, (byte)0, (byte)0, 0, 2));
		addEdges.add(new EdgeStructure(4,"es4", 1, (byte)0, (byte)0, 1, 0));
		addEdges.add(new EdgeStructure(5,"es5", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(6,"es6", 1, (byte)0, (byte)0, 3, 2));
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);
		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"C", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"D", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(3,"A", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"esa", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(1,"esb", 1, (byte)0, (byte)0, 0, 2));
		addEdges.add(new EdgeStructure(2,"esc", 1, (byte)0, (byte)0, 0, 3));
		addEdges.add(new EdgeStructure(3,"esd", 1, (byte)0, (byte)0, 3, 0));
		addEdges.add(new EdgeStructure(4,"ese", 1, (byte)0, (byte)0, 1, 3));
		addEdges.add(new EdgeStructure(5,"esf", 1, (byte)0, (byte)0, 3, 1));
		addEdges.add(new EdgeStructure(6,"esg", 1, (byte)0, (byte)0, 2, 0));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,false,112211);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(false,true,553355);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);

		rts = new RandomTrailSimilarity(true,true,995566);
		rts.setTrailLength(length);
		rts.setTrailsPerNode(trials);
		similarity = rts.similarity(g1, g2);
		assertEquals(0, similarity, 0.001);
		similarity = rts.similarity(g2, g1);
		assertEquals(0, similarity, 0.001);
	}
	
	
	@Test
	public void test007() throws FastGraphException {
		
		double similarity;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;
		int trials = 10;
		int length = 5;
		g1 = null;
		
		for(int i = 0; i < 10; i++) {
			try {
				g1 = FastGraph.randomGraphFactory(10,15,i*44333,false,false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g1,i*222211,false);
			
			rts = new RandomTrailSimilarity(false,false,551122);
			rts.setTrailLength(length);
			rts.setTrailsPerNode(trials);
			similarity = rts.similarity(g1, g2);
			assertEquals(0, similarity, 0.001);
			similarity = rts.similarity(g2, g1);
			assertEquals(0, similarity, 0.001);
	
			rts = new RandomTrailSimilarity(true,false,551122);
			rts.setTrailLength(length);
			rts.setTrailsPerNode(trials);
			similarity = rts.similarity(g1, g2);
			assertEquals(0, similarity, 0.001);
			similarity = rts.similarity(g2, g1);
			assertEquals(0, similarity, 0.001);
	
			rts = new RandomTrailSimilarity(false,true,117733);
			rts.setTrailLength(length);
			rts.setTrailsPerNode(trials);
			similarity = rts.similarity(g1, g2);
			assertEquals(0, similarity, 0.001);
			similarity = rts.similarity(g2, g1);
			assertEquals(0, similarity, 0.001);
	
			rts = new RandomTrailSimilarity(true,true,331199);
			rts.setTrailLength(length);
			rts.setTrailsPerNode(trials);
			similarity = rts.similarity(g1, g2);
			assertEquals(0, similarity, 0.001);
			similarity = rts.similarity(g2, g1);
			assertEquals(0, similarity, 0.001);
		}
	}
	
	
	@Test
	public void test008() throws FastGraphException {
		double similarity1,similarity2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es01", 1, (byte)0, (byte)0, 0, 1));
		
		g1 = FastGraph.structureFactory("g1",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addEdges = new LinkedList<EdgeStructure>();
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity();
		similarity1 = rts.similarity(g1, g2);
		assertEquals(1.0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,false,6644);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(1.0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(false,true,1177);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(1.0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,true,9911);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(1.0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);
	}
	
	
	@Test
	public void test009() throws FastGraphException {
		double similarity1,similarity2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(3,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(4,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(5,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 2));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 2, 3));
		addEdges.add(new EdgeStructure(3,"es3", 1, (byte)0, (byte)0, 3, 4));
		addEdges.add(new EdgeStructure(4,"es4", 1, (byte)0, (byte)0, 4, 5));
		addEdges.add(new EdgeStructure(5,"es5", 1, (byte)0, (byte)0, 5, 0));
		addEdges.add(new EdgeStructure(6,"es6", 1, (byte)0, (byte)0, 0, 5));
		g1 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(2,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(3,"B", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(4,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(5,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 1, 2));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 2, 3));
		addEdges.add(new EdgeStructure(3,"es3", 1, (byte)0, (byte)0, 3, 4));
		addEdges.add(new EdgeStructure(4,"es4", 1, (byte)0, (byte)0, 4, 5));
		addEdges.add(new EdgeStructure(5,"es5", 1, (byte)0, (byte)0, 5, 0));
		addEdges.add(new EdgeStructure(6,"es6", 1, (byte)0, (byte)0, 0, 3));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity(false,false,5566);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,false,8866);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(false,true,1133);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,true,3399);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);
	}
	
	
	@Test
	public void test010() throws FastGraphException {
		double similarity1,similarity2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 1));
		g1 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 0));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity(false,false,5566);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(0.875, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,false,8866);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(0.75, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(false,true,1133);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(0.875, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,true,3399);
		similarity1 = rts.similarity(g1, g2);
		assertEquals(0.75, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);
	}
	
	
	@Test
	public void test011() throws FastGraphException {
		double similarity1,similarity2;
		LinkedList<NodeStructure> addNodes;
		LinkedList<EdgeStructure> addEdges;
		FastGraph g1,g2;
		RandomTrailSimilarity rts;

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 1));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 0));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 0, 1));
		g1 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);

		addNodes = new LinkedList<NodeStructure>();
		addNodes.add(new NodeStructure(0,"A", 1, (byte)1, (byte)0));
		addNodes.add(new NodeStructure(1,"B", 1, (byte)1, (byte)0));
		addEdges = new LinkedList<EdgeStructure>();
		addEdges.add(new EdgeStructure(0,"es0", 1, (byte)0, (byte)0, 1, 0));
		addEdges.add(new EdgeStructure(1,"es1", 1, (byte)0, (byte)0, 0, 1));
		addEdges.add(new EdgeStructure(2,"es2", 1, (byte)0, (byte)0, 0, 1));
		g2 = FastGraph.structureFactory("g2",(byte)0,addNodes,addEdges,false);
		
		rts = new RandomTrailSimilarity(false,false,5566);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,false,8866);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(false,true,1133);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);

		rts = new RandomTrailSimilarity(true,true,3399);
		similarity1 = rts.similarity(g1, g2);
		assertNotEquals(0, similarity1, 0.001);
		similarity2 = rts.similarity(g2, g1);
		assertEquals(similarity2, similarity2, 0.001);
	}
	
	@Test
	public void test012() throws FastGraphException {
		RandomTrailSimilarity rts;
		double similarity1,similarity2;
		boolean directed,labels;
		FastGraph g1,g2;
		long g1Seed,g2Seed,rtsSeed;
		int trailLength = 4;
		int trailsPerNode = 1;
		int nodes = 2;
		int edges = 4;
		int i = 717;
		
		g1Seed = i*111;
		g2Seed = i*333;
		rtsSeed = i*555;
		
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		
		directed = false;
		labels = false;
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
		assertEquals(similarity1, similarity2, 0.0001);

		directed = true;
		labels = false;
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = false;
		labels = true;
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = true;
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
		assertEquals(similarity1, similarity2, 0.0001);
	}
	
	
	@Test
	public void test013() throws FastGraphException {
		RandomTrailSimilarity rts;
		double similarity1,similarity2;
		boolean directed,labels;
		FastGraph g1,g2;
		long g1Seed,g2Seed,rtsSeed;
		int trailLength = 6;
		int trailsPerNode = 10;
		int nodes = 3;
		int edges = 4;
		int i = 1349;
		g1Seed = i*7777;
		g2Seed = i*111;
		rtsSeed = i*99999;
		
		directed = false;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = false;
		labels = true;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = true;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
	}
	
	@Test
	public void test014() throws FastGraphException {
		RandomTrailSimilarity rts;
		double similarity1,similarity2;
		boolean directed,labels;
		FastGraph g1,g2;
		long g1Seed,g2Seed,rtsSeed;
		int trailLength = 6;
		int trailsPerNode = 10;
		int nodes = 3;
		int edges = 4;
		int i = 5551;
		g1Seed = i*7777;
		g2Seed = i*111;
		rtsSeed = i*99999;
		
		directed = false;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = false;
		labels = true;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = true;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
		directed = true;
		labels = false;
		g1 = FastGraph.randomGraphFactory(nodes, edges, g1Seed, false);
		g2 = FastGraph.randomGraphFactory(nodes, edges, g2Seed, false);
		rts = new RandomTrailSimilarity(directed,labels,rtsSeed);
		rts.setTrailLength(trailLength);
		rts.setTrailsPerNode(trailsPerNode);
		similarity1 = rts.similarity(g1, g2);
		similarity2 = rts.similarity(g2, g1);
		assertTrue(similarity1 > 0.001);
//		assertEquals(similarity1, similarity2, 0.0001);
		
	}

}
