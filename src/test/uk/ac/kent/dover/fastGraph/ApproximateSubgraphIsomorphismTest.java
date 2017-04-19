package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.ApproximateSubgraphIsomorphism;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.comparators.AlwaysTrueEdgeComparator;
import uk.ac.kent.dover.fastGraph.comparators.AlwaysTrueNodeComparator;

public class ApproximateSubgraphIsomorphismTest {
	
	private AlwaysTrueNodeComparator nc;
	private AlwaysTrueEdgeComparator ec;

	@Test
	public void test001() throws Exception {
		FastGraph target = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(), false);
		FastGraph pattern = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(), false);
		
		nc = new AlwaysTrueNodeComparator(target, pattern);
		ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(target, pattern, 4, 1, nc, ec);
		int count = isi.subgraphIsomorphismFinder();
		assertEquals(20,count);
	}
	
	@Test
	public void test002() throws Exception {
		FastGraph target = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(), false);
		FastGraph pattern = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(), false);
		
		nc = new AlwaysTrueNodeComparator(target, pattern);
		ec = new AlwaysTrueEdgeComparator(target, pattern);
		
		
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(target, pattern, 5, 1, nc, ec);
		int count = isi.subgraphIsomorphismFinder();
		assertEquals(60,count);
	}
	
}