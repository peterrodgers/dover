package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.ApproximateSubgraphIsomorphism;
import uk.ac.kent.dover.fastGraph.FastGraph;

public class ApproximateSubgraphIsomorphismTest {

	@Test
	public void test001() throws Exception {
		FastGraph target = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(), false);
		FastGraph pattern = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(), false);
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(target, pattern, 4, 1);
		int count = isi.subgraphIsomorphismFinder();
		assertEquals(count,2);
	}
	
	@Test
	public void test002() throws Exception {
		FastGraph target = FastGraph.jsonStringGraphFactory(TestRunner.get5Node7EdgeA(), false);
		FastGraph pattern = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(), false);
		ApproximateSubgraphIsomorphism isi = new ApproximateSubgraphIsomorphism(target, pattern, 5, 1);
		int count = isi.subgraphIsomorphismFinder();
		assertEquals(count,0);
	}
	
}