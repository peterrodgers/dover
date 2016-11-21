package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.nio.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;

import uk.ac.kent.displayGraph.*;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.ExactMotifFinder.IsoHolder;

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
		ExactMotifFinder emf = new ExactMotifFinder(g);
		emf.findAndExportAllMotifs(10, 4, 8, 0, true);
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = emf.getHashBuckets();
		emf.findAndExportAllMotifs(10, 4, 8, 0, true);
		HashMap<String,LinkedList<IsoHolder>> hashBuckets2 = emf.getHashBuckets();
		assertEquals(hashBuckets.toString(),hashBuckets2.toString());
	}
	
	@Test
	public void test002() throws IOException {
		FastGraph g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),false);
		ExactMotifFinder emf = new ExactMotifFinder(g);
		emf.findAndExportAllMotifs(10, 4, 8, 0, true);
		HashMap<String,LinkedList<IsoHolder>> hashBuckets = emf.getHashBuckets();
		emf.findAndExportAllMotifs(10, 4, 8, 0, true);
		HashMap<String,LinkedList<IsoHolder>> hashBuckets2 = emf.getHashBuckets();
		assertEquals(hashBuckets.toString(),hashBuckets2.toString());
	}
}
