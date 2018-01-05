package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDBipartite;

public class ApproximateGEDBipartiteTest {
	
	@Rule
	public ExpectedException thrown1 = ExpectedException.none();
	@Test
	public void test001() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_NODE, 4.0);
		editCosts.put(EditOperation.ADD_EDGE, 1.22);
		editCosts.put(EditOperation.ADD_NODE, 13.4);
		
	    thrown1.expect(FastGraphException.class);
		new ApproximateGEDBipartite(editCosts);
	}
	
	@Rule
	public ExpectedException thrown2 = ExpectedException.none();
	@Test
	public void test002() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_NODE, 44.0);
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		
	    thrown2.expect(FastGraphException.class);
		new ApproximateGEDBipartite(editCosts);
	}
	
	@Rule
	public ExpectedException thrown3 = ExpectedException.none();
	@Test
	public void test003() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		
	    thrown3.expect(FastGraphException.class);
		new ApproximateGEDBipartite(editCosts);
	}
	
	@Rule
	public ExpectedException thrown4 = ExpectedException.none();
	@Test
	public void test004() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		
	    thrown4.expect(FastGraphException.class);
		new ApproximateGEDBipartite(editCosts);
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
		new ApproximateGEDBipartite(true,true,editCosts);
	}
	
	
	@Test
	public void test006() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
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
	
		el = new EditList();
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite(editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(2, ret, 0.001);
		assertEquals(2, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.DELETE_NODE, retList.get(0).getOperationCode());
		assertEquals(0, retList.get(0).getId());
		assertEquals(1,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());

		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test007() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		ApproximateGEDBipartite ged;
		
		el = new EditList();
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node0",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite();
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(1, ret, 0.001);
		assertEquals(1, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals("node0", retList.get(0).getLabel());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	
	@Test
	public void test008() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,12.0);
		editCosts.put(EditOperation.ADD_NODE,31.0);
		editCosts.put(EditOperation.DELETE_EDGE,24.0);
		editCosts.put(EditOperation.ADD_EDGE,0.1);
		editCosts.put(EditOperation.RELABEL_NODE,66.0);
		
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
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node B",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite(false,false,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(4, ret, 0.001);
		assertEquals(4, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(0, retList.get(0).getId());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(1,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}


	
}
