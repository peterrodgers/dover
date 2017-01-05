package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;


import org.junit.Test;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.NodeType;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;

public class ExactSubgraphIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleNodeLabelComparator snlc;
		AlwaysTrueNodeComparator atnc;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		snlc = new SimpleNodeLabelComparator(g1,g2);
		atnc = new AlwaysTrueNodeComparator(g1,g2);
		assertTrue(snlc.compare(4,4) == 0);
		assertTrue(snlc.compare(3,5) < 0);
		assertTrue(snlc.compare(5,3) > 0);
		assertTrue(snlc.compare(2,1) > 0);
		
		assertTrue(atnc.compare(4,4) == 0);
		assertTrue(atnc.compare(3,5) == 0);
		assertTrue(atnc.compare(5,3) == 0);
		assertTrue(atnc.compare(2,1) == 0);
	}

	@Test
	public void test002() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleEdgeLabelComparator selc;
		AlwaysTrueEdgeComparator atec;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		selc = new SimpleEdgeLabelComparator(g1,g2);
		atec = new AlwaysTrueEdgeComparator(g1, g2);
		assertTrue(selc.compare(0,0) == 0);
		assertTrue(selc.compare(13,15) < 0);
		assertTrue(selc.compare(19,0) > 0);
		assertTrue(selc.compare(12,13) < 0);
		
		assertTrue(atec.compare(0,0) == 0);
		assertTrue(atec.compare(13,15) == 0);
		assertTrue(atec.compare(19,0) == 0);
		assertTrue(atec.compare(12,13) == 0);
	}

	
	
	
	@Test
	public void test003() {
		
		
	}



}
