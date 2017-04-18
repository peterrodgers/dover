package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.EnumerateSubgraphNeighbourhood;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.FastGraphException;
import uk.ac.kent.dover.fastGraph.KMedoids;

public class KMedoidsTest {

	@Test
	public void test001() throws IOException, FastGraphException {
		FastGraph g1 = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		int minSize = 3, maxSize = 3;
		int numOfClusters = 2, iterations = 1;
		int subsPerNode = 1, attempts = 20;
		KMedoids km = new KMedoids(g1, numOfClusters, iterations);
		EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(g1);
		HashSet<FastGraph> subs = new HashSet<FastGraph>();
		for(int i = minSize; i <= maxSize; i++) {//build a list of potential subgraphs
			subs.addAll(esn.enumerateSubgraphs(i, subsPerNode, attempts));
		}
		
		ArrayList<FastGraph> subgraphs = new ArrayList<FastGraph>(subs);
		ArrayList<ArrayList<FastGraph>> clusters = km.cluster(subgraphs);
		
		assertEquals(4, subgraphs.size());
		assertEquals(2, clusters.size());
		assertEquals(1, clusters.get(0).size());
		assertEquals(3, clusters.get(1).size());
	}
	
	
}
