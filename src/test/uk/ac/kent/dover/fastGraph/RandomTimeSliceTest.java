package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import java.io.*;
import uk.ac.kent.dover.fastGraph.*;
import org.junit.*;
import test.uk.ac.kent.dover.TestRunner;

/**
 * 
 * @author Rob Baker
 *
 */
public class RandomTimeSliceTest {

	@Test
	public void test001() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		FastGraph g2 = g.randomTimeSeriesFactory(0.2, 0.2, 2, 2, false);
		assertEquals(g2.getNumberOfNodes(),10);
		assertEquals(g2.getNumberOfEdges(),14);
		assertEquals(g2.getGeneration(),1);
		assertEquals(g2.findAllNodesOfAge(1).size(), 6);
	}
	
	@Test
	public void test002() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		FastGraph g2 = g.randomTimeSeriesFactory(0.2, 0.2, 2, 2, false);
		assertEquals(g2.getNumberOfNodes(),11);
		assertEquals(g2.getNumberOfEdges(),14);
		assertEquals(g2.getGeneration(),1);
		assertEquals(g2.findAllNodesOfAge(1).size(), 6);
	}

	@Test
	public void test003() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		FastGraph g2 = g.randomTimeSeriesFactory(0.2, 0.2, 2, 2, false);
		g2 = g2.randomTimeSeriesFactory(0.2, 0.2, 2, 2, false);
		assertEquals(g2.getNumberOfNodes(),18);
		assertEquals(g2.getNumberOfEdges(),25);
		assertEquals(g2.getGeneration(),2);
		assertEquals(g2.findAllNodesOfAge(2).size(), 7);
	}
}
