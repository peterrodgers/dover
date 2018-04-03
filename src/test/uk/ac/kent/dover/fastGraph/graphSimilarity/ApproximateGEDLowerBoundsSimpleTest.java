package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDLowerBoundsSimple;

public class ApproximateGEDLowerBoundsSimpleTest {
	
	@Rule
	public ExpectedException thrown1 = ExpectedException.none();


	@Test
	public void test001() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		new ApproximateGEDLowerBoundsSimple();
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_EDGE, 4.0);
		editCosts.put(EditOperation.ADD_EDGE, 1.22);
		editCosts.put(EditOperation.ADD_NODE, 13.4);
		
	    thrown1.expect(FastGraphException.class);
		new ApproximateGEDLowerBoundsSimple(false,editCosts);
	}
	
	@Rule
	public ExpectedException thrown2 = ExpectedException.none();
	@Test
	public void test002() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_NODE, 44.0);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		
	    thrown2.expect(FastGraphException.class);
		new ApproximateGEDLowerBoundsSimple(true,editCosts);
	}
	
	@Rule
	public ExpectedException thrown3 = ExpectedException.none();
	@Test
	public void test003() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		
	    thrown3.expect(FastGraphException.class);
		new ApproximateGEDLowerBoundsSimple(false,editCosts);
	}
	
	@Rule
	public ExpectedException thrown4 = ExpectedException.none();
	@Test
	public void test004() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		
	    thrown4.expect(FastGraphException.class);
		new ApproximateGEDLowerBoundsSimple(false,editCosts);
	}
	
	
	@Rule
	public ExpectedException thrown5 = ExpectedException.none();
	@Test
	public void test005() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 31.2);
		editCosts.put(EditOperation.DELETE_NODE, 111.2);
		
	    thrown5.expect(FastGraphException.class);
		new ApproximateGEDLowerBoundsSimple(true,editCosts);
	}
	
	
	@Test
	public void test006() throws Exception {
		double ret;
		FastGraph g1,g2;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = FastGraph.randomGraphFactory(0, 0, false);

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(0, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(0, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(0, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(0, ret, 0.001);
	}
	
	
	@Test
	public void test007() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(0, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(0, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(0, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(0, ret, 0.001);
	}
	
	
	@Test
	public void test008() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		g2 = FastGraph.randomGraphFactory(0, 0, false);

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(2, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(3, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(2, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(3, ret, 0.001);
	}
	
	@Test
	public void test009() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		g2 = FastGraph.randomGraphFactory(0, 0, false);

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(11, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(11, ret, 0.001);
	}
	
	
	@Test
	public void test010() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
	

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(3, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(2, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(3, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(2, ret, 0.001);
	}
	
	@Test
	public void test011() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
	

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(6, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(6, ret, 0.001);
	}
	
	@Test
	public void test012() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node a",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node c",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
	

		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(6, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(18, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(16, ret, 0.001);
	}
	
	
	@Test
	public void test013() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,1.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node a",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node c",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
	
		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(16, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(12, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(17, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(13, ret, 0.001);
	}
	
	
	@Test
	public void test014() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDLowerBoundsSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,7.0);
		editCosts.put(EditOperation.ADD_NODE,8.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,3.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node a",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node a",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node a",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node b",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node c",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
	
		ged = new ApproximateGEDLowerBoundsSimple(false,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(8, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(7, ret, 0.001);
		
		ged = new ApproximateGEDLowerBoundsSimple(true,editCosts);
		ret = ged.similarity(g1, g2);
		assertEquals(11, ret, 0.001);
		ret = ged.similarity(g2, g1);
		assertEquals(10, ret, 0.001);
	}
	
	
}
