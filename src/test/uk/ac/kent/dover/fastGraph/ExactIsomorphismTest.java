package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.ExactIsomorphism;
import uk.ac.kent.dover.fastGraph.FastGraph;

public class ExactIsomorphismTest {

	
	
	@Test
	public void test001() {
		ExactIsomorphism ei = new ExactIsomorphism();
		FastGraph g2;
		FastGraph g1;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		assertTrue(ei.isomorphic(g1,g2));

		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		assertFalse(ei.isomorphic(g1,g2));

		g1 = FastGraph.randomGraphFactory(0,0,1,false);
		g2 = FastGraph.randomGraphFactory(0,0,1,false);
		assertTrue(ei.isomorphic(g1,g2));

		g1 = FastGraph.randomGraphFactory(1,0,1,false);
		g2 = FastGraph.randomGraphFactory(1,0,1,false);
		assertTrue(ei.isomorphic(g1,g2));

		g1 = FastGraph.randomGraphFactory(2,0,1,false);
		g2 = FastGraph.randomGraphFactory(2,0,1,false);
		assertTrue(ei.isomorphic(g1,g2));

	}

	
	@Test
	public void test002() {
		ExactIsomorphism ei = new ExactIsomorphism();
		FastGraph g2;
		FastGraph g1;
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		assertTrue(ei.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertTrue(ei.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		assertFalse(ei.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		assertFalse(ei.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		assertFalse(ei.isomorphic(g1,g2));
		
		g1 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeC(),false);
		assertFalse(ei.isomorphic(g1,g2));

	}

	
	
	
	
	


}
