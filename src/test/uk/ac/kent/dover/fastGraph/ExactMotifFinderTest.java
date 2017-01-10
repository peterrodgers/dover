package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

import uk.ac.kent.displayGraph.*;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.ExactMotifFinder.IsoHolder;
import uk.ac.kent.dover.fastGraph.ExactMotifFinder.MotifResultHolder;
import uk.ac.kent.dover.fastGraph.Gui.MotifTaskDummy;

import org.junit.*;

import test.uk.ac.kent.dover.TestRunner;

/**
 * 
 * @author Rob Baker
 *
 */
public class ExactMotifFinderTest {

	@Test
	public void test001() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		ExactMotifFinder emf = new ExactMotifFinder(g, new MotifTaskDummy(), false);
		emf.findAllMotifs(10,4,4);

		emf = new ExactMotifFinder(g, new MotifTaskDummy(), false);
		emf.findAllMotifs(0,4,4);
		
		ArrayList<MotifResultHolder> results = emf.compareAndExportResults(4, 4, 4);
		assertEquals(results.get(0).generateDifference(),41.59, 0.1);
	}
	
	@Test
	public void test002() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		ExactMotifFinder emf = new ExactMotifFinder(g, new MotifTaskDummy(), false);
		emf.findAllMotifs(10,4,4);

		emf = new ExactMotifFinder(g, new MotifTaskDummy(), false);
		emf.findAllMotifs(0,4,4);
		
		ArrayList<MotifResultHolder> results = emf.compareAndExportResults(4, 4, 4);
		assertEquals(results.get(0).generateDifference(),54.01, 0.1);
	}
}
