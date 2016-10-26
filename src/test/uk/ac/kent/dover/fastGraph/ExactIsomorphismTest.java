package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.kent.dover.fastGraph.ExactIsomorphism;
import uk.ac.kent.dover.fastGraph.FastGraph;

public class ExactIsomorphismTest {

	
	
	@Test
	public void test001() {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,2,false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(0,0,1,false);
		g2 = FastGraph.randomGraphFactory(0,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(1,0,1,false);
		g2 = FastGraph.randomGraphFactory(1,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.randomGraphFactory(2,0,1,false);
		g2 = FastGraph.randomGraphFactory(2,0,1,false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

	}

	
	@Test
	public void test002() {
		ExactIsomorphism ei;
		FastGraph g2;
		FastGraph g1;
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertTrue(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		ei = new ExactIsomorphism(g1);
		assertFalse(ei.isomorphic(g2));

	}
	
	
	
	@Test
	public void test003() {
		FastGraph g2;
		FastGraph g1;

		
		g1 = FastGraph.randomGraphFactory(12,30,111,true);
		g2 = FastGraph.randomGraphFactory(12,30,111,true);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));
		
		g1 = FastGraph.randomGraphFactory(12,30,111,false);
		g2 = FastGraph.randomGraphFactory(12,30,112,false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));
		
		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeB(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

		g1 = FastGraph.jsonStringGraphFactory(get5Node7EdgeC(),false);
		g2 = FastGraph.jsonStringGraphFactory(get5Node7EdgeA(),false);
		assertFalse(ExactIsomorphism.isomorphic(g1,g2));

	}

	
	
	
	
	/*
	 * 0-1\
	 * |/| 4
	 * 2-3/
	 */
	public String get5Node7EdgeA() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	/*
	 * 0-1\
	 * |/| 4
	 * 2-3/
	 */
	public String get5Node7EdgeB() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node 3\",\n";
		json += "\"nodeWeight\": \"31\",\n";
		json += "\"nodeType\": \"32\",\n";
		json += "\"nodeAge\": \"33\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge 0\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"0\",\n";
		json += "\"edgeLabel\": \"edge 2\",\n";
		json += "\"edgeWeight\": \"25\",\n";
		json += "\"edgeType\": \"26\",\n";
		json += "\"edgeAge\": \"27\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"4\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	
	/*  /--4
	 * 0-1 |
	 * |/| | 
	 * 2-3/
	 */
	public String get5Node7EdgeC() {
	
		String json = "{\n";
		json += "\"name\": \"four nodes, five edges\",\n";
		json += "\"nodes\": [\n";
		json += "{\n";
		json += "\"nodeIndex\": \"0\",\n";
		json += "\"nodeLabel\": \"node label 0\",\n";
		json += "\"nodeWeight\": \"10\",\n";
		json += "\"nodeType\": \"0\",\n";
		json += "\"nodeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"1\",\n";
		json += "\"nodeLabel\": \"node label 1\",\n";
		json += "\"nodeWeight\": \"1\",\n";
		json += "\"nodeType\": \"1\",\n";
		json += "\"nodeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"2\",\n";
		json += "\"nodeLabel\": \"node label 2\",\n";
		json += "\"nodeWeight\": \"2\",\n";
		json += "\"nodeType\": \"2\",\n";
		json += "\"nodeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"3\",\n";
		json += "\"nodeLabel\": \"node label 3\",\n";
		json += "\"nodeWeight\": \"3\",\n";
		json += "\"nodeType\": \"3\",\n";
		json += "\"nodeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"nodeIndex\": \"4\",\n";
		json += "\"nodeLabel\": \"node label 4\",\n";
		json += "\"nodeWeight\": \"41\",\n";
		json += "\"nodeType\": \"42\",\n";
		json += "\"nodeAge\": \"43\"\n";
		json += "},\n";
		json += "],\n";
		json += "\"edges\": [\n";
		json += "{\n";
		json += "\"edgeIndex\": \"0\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 0\",\n";
		json += "\"edgeWeight\": \"0\",\n";
		json += "\"edgeType\": \"0\",\n";
		json += "\"edgeAge\": \"0\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"1\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"2\",\n";
		json += "\"edgeLabel\": \"edge label 1\",\n";
		json += "\"edgeWeight\": \"1\",\n";
		json += "\"edgeType\": \"1\",\n";
		json += "\"edgeAge\": \"1\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"2\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"1\",\n";
		json += "\"edgeLabel\": \"edge label 2\",\n";
		json += "\"edgeWeight\": \"2\",\n";
		json += "\"edgeType\": \"2\",\n";
		json += "\"edgeAge\": \"2\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"3\",\n";
		json += "\"node1\": \"1\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 3\",\n";
		json += "\"edgeWeight\": \"3\",\n";
		json += "\"edgeType\": \"3\",\n";
		json += "\"edgeAge\": \"3\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"4\",\n";
		json += "\"node1\": \"2\",\n";
		json += "\"node2\": \"3\",\n";
		json += "\"edgeLabel\": \"edge label 4\",\n";
		json += "\"edgeWeight\": \"4\",\n";
		json += "\"edgeType\": \"4\",\n";
		json += "\"edgeAge\": \"4\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"5\",\n";
		json += "\"node1\": \"0\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 5\",\n";
		json += "\"edgeWeight\": \"55\",\n";
		json += "\"edgeType\": \"56\",\n";
		json += "\"edgeAge\": \"57\"\n";
		json += "},\n";
		json += "{\n";
		json += "\"edgeIndex\": \"6\",\n";
		json += "\"node1\": \"3\",\n";
		json += "\"node2\": \"4\",\n";
		json += "\"edgeLabel\": \"edge label 6\",\n";
		json += "\"edgeWeight\": \"65\",\n";
		json += "\"edgeType\": \"66\",\n";
		json += "\"edgeAge\": \"67\"\n";
		json += "},\n";
		json += "],\n";
		json += "}\n";
		return json;
	}
	


}
