package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;


import org.junit.Test;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;

public class ExactSubgraphIsomorphismTest {

	
	
	@Test
	public void test001() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleNodeLabelComparator snlc;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		snlc = new SimpleNodeLabelComparator(g1,g2);
		assertTrue(snlc.compare(4,4) == 0);
		assertTrue(snlc.compare(3,5) < 0);
		assertTrue(snlc.compare(5,3) > 0);
		assertTrue(snlc.compare(2,1) > 0);
	}

	@Test
	public void test002() throws Exception {
		FastGraph g1,g2;
		ExactIsomorphism ei;
		SimpleEdgeLabelComparator selc;
		g1 = FastGraph.randomGraphFactory(10,20,1,false);
		g2 = FastGraph.randomGraphFactory(10,20,1,false);
		selc = new SimpleEdgeLabelComparator(g1,g2);
		assertTrue(selc.compare(0,0) == 0);
		assertTrue(selc.compare(13,15) < 0);
		assertTrue(selc.compare(19,0) > 0);
		assertTrue(selc.compare(12,13) < 0);
	}

	
	


}
