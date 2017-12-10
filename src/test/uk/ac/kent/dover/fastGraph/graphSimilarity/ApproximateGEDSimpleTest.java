package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.NodeDegreeComparator;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDSimple;

public class ApproximateGEDSimpleTest {

	
	@Rule
	public ExpectedException thrown1 = ExpectedException.none();
	@Test
	public void test001() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_NODE, 4.0);
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		
	    thrown1.expect(FastGraphException.class);
		new ApproximateGEDSimple(editCosts);
	}
	
	@Rule
	public ExpectedException thrown2 = ExpectedException.none();
	@Test
	public void test002() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_NODE, 4.0);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		
	    thrown2.expect(FastGraphException.class);
		new ApproximateGEDSimple(editCosts);
	}
	
	@Rule
	public ExpectedException thrown3 = ExpectedException.none();
	@Test
	public void test003() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		
	    thrown3.expect(FastGraphException.class);
		new ApproximateGEDSimple(editCosts);
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
		new ApproximateGEDSimple(editCosts);
	}
	
	
	@Rule
	public ExpectedException thrown5 = ExpectedException.none();
	@Test
	public void test005() throws FastGraphException {
		HashMap<Integer,Double> editCosts;
		
		editCosts = new HashMap<Integer,Double>();
		editCosts.put(EditOperation.DELETE_NODE, 1.2);
		editCosts.put(EditOperation.ADD_NODE, 3.4);
		editCosts.put(EditOperation.DELETE_EDGE, 1.2);
		editCosts.put(EditOperation.ADD_EDGE, 1.2);
		
	    thrown5.expect(FastGraphException.class);
		new ApproximateGEDSimple(true,true,editCosts);
	}
	
	@Test
	public void test006() throws Exception {
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el1,el2;
		FastGraph g;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		NodeDegreeComparator ndc;
		ArrayList<Integer> nodeList;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,2.0);
		editCosts.put(EditOperation.ADD_EDGE,2.0);
		editCosts.put(EditOperation.RELABEL_NODE,2.0);
		
		el1 = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
		el1.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 1",-1,-1);
		el1.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 2",-1,-1);
		el1.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 0",0,1);
		el1.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 1",1,0);
		el1.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 2",1,1);
		el1.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el1.applyOperations(g1);
	
		el2 = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
		el2.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
		el2.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.0,-1,"node 0",-1,-1);
		el2.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.0,-1,"edge 0",0,0);
		el2.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el2.applyOperations(g2);
		
		ndc = new NodeDegreeComparator(g1, g2);
		assertEquals(-1, ndc.compare(2 , 0));
		assertEquals(1, ndc.compare(1 , 1));
		assertEquals(0, ndc.compare(0 , 0));

		ndc = new NodeDegreeComparator(g1, g2);
		ndc.setAscending(true);
		assertEquals(-1, ndc.compare(2 , 0));
		assertEquals(1, ndc.compare(1 , 1));
		assertEquals(0, ndc.compare(0 , 0));
		
		nodeList = new ArrayList<>();
		nodeList.add(0);
		nodeList.add(1);
		nodeList.add(2);
		
		ndc = new NodeDegreeComparator(g1, g1);
		ndc.setAscending(true);
		nodeList.sort(ndc);
		assertTrue(g1.getNodeDegree(nodeList.get(1))>g1.getNodeDegree(nodeList.get(0)));
		assertTrue(g1.getNodeDegree(nodeList.get(2))>g1.getNodeDegree(nodeList.get(1)));

		ndc = new NodeDegreeComparator(g1, g1);
		ndc.setAscending(false);
		nodeList.sort(ndc);
		assertTrue(g1.getNodeDegree(nodeList.get(0))>g1.getNodeDegree(nodeList.get(1)));
		assertTrue(g1.getNodeDegree(nodeList.get(1))>g1.getNodeDegree(nodeList.get(2)));
		
		
	}
	
	
	@Test
	public void test007() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		
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

		ged = new ApproximateGEDSimple(editCosts);
		
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
	public void test008() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		el = new EditList();
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node0",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple();
		
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
	public void test009() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		
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
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node B",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts);
		
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
	
	
	@Test
	public void test010() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node B",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,777);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(5, ret, 0.001);
		assertEquals(5, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals("", retList.get(0).getLabel());
		assertEquals(1, retList.get(0).getN1());
		assertEquals(0, retList.get(0).getN2());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(1,retEditList.findAddEdgeOperations().size());

		
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test011() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
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
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",2,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,777);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(18, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(6, retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(4).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(5).getOperationCode());
		assertEquals(2, retList.get(0).getId());
		assertEquals(1, retList.get(1).getId());
		assertEquals(0, retList.get(2).getId());
		assertEquals(2, retList.get(3).getId());
		assertEquals(1, retList.get(4).getId());
		assertEquals(0, retList.get(5).getId());
		assertEquals(3,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(3,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());

		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test012() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
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
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",2,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,777);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(24, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(6, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_NODE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_NODE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(4).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(5).getOperationCode());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(3,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(3,retEditList.findAddEdgeOperations().size());
		
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test013() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",1,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",2,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",0,4);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,777);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(1,retEditList.findDeleteEdgeOperations().size());
		assertEquals(3,retEditList.findAddEdgeOperations().size());
		assertEquals(22, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(5, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(4).getOperationCode());
		
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}
	
	
	@Test
	public void test014() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,6.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",1,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",2,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",0,4);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,777);
		
		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(1,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(3,retEditList.findDeleteEdgeOperations().size());
		assertEquals(1,retEditList.findAddEdgeOperations().size());
		assertEquals(19, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(5, retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(4).getOperationCode());
		
		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test015() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.1);
		editCosts.put(EditOperation.ADD_NODE,3.1);
		editCosts.put(EditOperation.DELETE_EDGE,4.1);
		editCosts.put(EditOperation.ADD_EDGE,5.1);
		editCosts.put(EditOperation.RELABEL_NODE,6.1);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",1,0);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(3,retEditList.findAddEdgeOperations().size());
		assertEquals(15.3, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test016() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",1,0);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(3,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(9, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test017() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",1,0);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(3,retEditList.findAddEdgeOperations().size());
		assertEquals(12, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test019() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",2,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",3,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",2,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(1,retEditList.findDeleteEdgeOperations().size());
		assertEquals(3,retEditList.findAddEdgeOperations().size());
		assertEquals(17, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(5, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(4).getOperationCode());
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test020() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",2,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",3,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",2,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		
		assertEquals(1,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(3,retEditList.findDeleteEdgeOperations().size());
		assertEquals(1,retEditList.findAddEdgeOperations().size());
		assertEquals(14, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(5, retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(2).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(3).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(4).getOperationCode());
		
		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	
	@Test
	public void test021() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",2,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",3,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",2,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,50,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(2,retEditList.findAddEdgeOperations().size());
		assertEquals(10, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,0,100,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(2,retEditList.findAddEdgeOperations().size());
		assertEquals(10, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.ADD_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}
	
	
	@Test
	public void test022() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		HashMap<Integer,Double> editCosts;
		EditList el, retEditList;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",2,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"order 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",0,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",3,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 5",2,2);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,100,3333);
		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(1,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(2,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(7, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(3, retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(1).getOperationCode());
		assertEquals(EditOperation.DELETE_NODE, retList.get(2).getOperationCode());
		
		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	@Test
	public void test023() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		EditOperation eo;		
		EditList el, retEditList;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);

		g1 = FastGraph.randomGraphFactory(0, 0, 888, false);
		
		g2 = FastGraph.randomGraphFactory(0, 0, 888, false);

		ged = new ApproximateGEDSimple(false,false,editCosts,20,0,555);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,ret,0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.getCost(),0.001);
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,0,100,444);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,ret,0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.getCost(),0.001);
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		
	}
	
	
	
	
	@Test
	public void test024() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		EditOperation eo;		
		EditList el, retEditList;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n0",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, 888, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n0",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, 888, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,50,100,4444);
		
		ret = ged.similarity(g1, g2);

		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,ret,0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.getCost(),0.001);
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	
	@Test
	public void test025() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDSimple ged;
		EditOperation eo;		
		EditList el, retEditList;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, 888, false);
		g1 = el.applyOperations(g1);

		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, 888, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,ret,0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.getCost(),0.001);
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,50,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(0,ret,0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.getCost(),0.001);
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	
	@Test
	public void test026() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		EditList retEditList1, retEditList2;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		g1 = FastGraph.randomGraphFactory(10, 20, 888, false);
		g2 = FastGraph.randomGraphFactory(15, 30, 999,false);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();

		gRet = retEditList1.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

		ged = new ApproximateGEDSimple(false,false,editCosts,0,100,333);
		
		ret = ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();

		gRet = retEditList1.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

		
	}

	
	@Test
	public void test027() throws Exception {
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		EditList retEditList1, retEditList2;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		g1 = FastGraph.randomGraphFactory(3, 3, 999,false);
		
		g2 = FastGraph.randomGraphFactory(2, 2, 888, false);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();

		gRet = retEditList1.applyOperations(g1);
		
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,0,1000,1111);
		
		ged.similarity(g1, g2);
		retEditList2 = ged.getEditList();
		retList = retEditList2.getEditList();
		assertTrue(retEditList1.getCost() == retEditList2.getCost());
		
		gRet = retEditList2.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}

	
	@Test
	public void test028() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		EditList retEditList1, retEditList2;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		g1 = FastGraph.randomGraphFactory(15, 30, 999,false);
		
		g2 = FastGraph.randomGraphFactory(10, 20, 888, false);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		assertEquals(0,ged.getNodeSwaps());
		assertEquals(0,ged.getApproximationTime());
		
		ret = ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();

		gRet = retEditList1.applyOperations(g1);

		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,0,1000,333);
		
		ret = ged.similarity(g1, g2);
		retEditList2 = ged.getEditList();
		retList = retEditList2.getEditList();
		assertTrue(retEditList1.getCost() > retEditList2.getCost());
		
		gRet = retEditList2.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	
	@Test
	public void test029() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		EditList retEditList1, retEditList2;
		ApproximateGEDSimple ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,2.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		g1 = FastGraph.randomGraphFactory(50, 500, 7777,false);
		
		g2 = FastGraph.randomGraphFactory(50, 500, 5555, false);

		ged = new ApproximateGEDSimple(false,false,editCosts,0,0,333);
		
		ret = ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();
		gRet = retEditList1.applyOperations(g1);

		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ApproximateGEDSimple(false,false,editCosts,50,1000,66666);
		
		ret = ged.similarity(g1, g2);
		retEditList2 = ged.getEditList();
		retList = retEditList2.getEditList();
		assertTrue(retEditList1.getCost() > retEditList2.getCost());
		
		gRet = retEditList2.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
	}
	
	
}
