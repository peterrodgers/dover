package test.uk.ac.kent.dover.fastGraph.graphSimilarity;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDSimple;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ExactGEDAStarIso;

/**
 * Tests for the ExactGDAAStarIso class
 * 
 * @author Peter Rodgers
 *
 */
public class ExactGEDAStarIsoTest {

	
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
		new ExactGEDAStarIso(editCosts);
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
		new ExactGEDAStarIso(editCosts);
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
		new ExactGEDAStarIso(editCosts);
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
		new ExactGEDAStarIso(editCosts);
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
		new ExactGEDAStarIso(true,true,editCosts);
	}
	
	@Test
	public void test006() throws Exception {
	}

	
	@Test
	public void test007() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ExactGEDAStarIso ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ExactGEDAStarIso(false,false,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(2, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.DELETE_NODE, retList.get(0).getOperationCode());
		assertEquals(0, retList.get(0).getId());
		assertEquals(1,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());

		ged = new ExactGEDAStarIso(false,false,editCosts);
		
		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		

		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();
		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(3,retEditList.getCost(), 0.001);
		assertEquals(1,retList.size());
		assertEquals(EditOperation.ADD_NODE,retList.get(0).getOperationCode());
		assertEquals("node 0",retList.get(0).getLabel());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(1,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());

		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}
	
	
	@Test
	public void test008() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ExactGEDAStarIso ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"n1",-1,-1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ExactGEDAStarIso(true,false,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(4.0,retEditList.getCost(), 0.001);
		assertEquals(1,retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(0,retList.get(0).getId());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(1,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		
		ged = new ExactGEDAStarIso(false,false,editCosts);

		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(5.0,retEditList.getCost(), 0.001);
		assertEquals(1,retList.size());
		assertEquals(EditOperation.ADD_EDGE, retList.get(0).getOperationCode());
		assertEquals("",retList.get(0).getLabel());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(1,retEditList.findAddEdgeOperations().size());

		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}
	
	
	@Test
	public void test009() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ExactGEDAStarIso ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,2.5);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"new label",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"e0",0,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ExactGEDAStarIso(true,true,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(2.5,retEditList.getCost(), 0.001);
		assertEquals(1,retList.size());
		assertEquals(EditOperation.RELABEL_NODE, retList.get(0).getOperationCode());
		assertEquals("new label",retList.get(0).getLabel());
		assertEquals(1,retList.get(0).getId());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(1,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		

		ret = ged.similarity(g2, g1);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(2.5,retEditList.getCost(), 0.001);
		assertEquals(1,retList.size());
		assertEquals(EditOperation.RELABEL_NODE, retList.get(0).getOperationCode());
		assertEquals("node 1",retList.get(0).getLabel());
		assertEquals(1,retList.get(0).getId());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(1,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g1,gRet,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}
	
	
	@Test
	public void test010() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ExactGEDAStarIso ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,2.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,4.0);
		editCosts.put(EditOperation.ADD_EDGE,5.0);
		editCosts.put(EditOperation.RELABEL_NODE,2.5);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"e0",0,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ExactGEDAStarIso();
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(0,retEditList.getCost(), 0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(0,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
		

		ged = new ExactGEDAStarIso(false,true,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(0,retEditList.getCost(), 0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(0,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

		ged = new ExactGEDAStarIso(true,false,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(0,retEditList.getCost(), 0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(0,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

		ged = new ExactGEDAStarIso(true,true,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(retEditList.getCost(), ret, 0.001);
		assertEquals(0,retEditList.getCost(), 0.001);
		assertEquals(0,retList.size());
		assertEquals(0,retEditList.findDeleteNodeOperations().size());
		assertEquals(0,retEditList.findAddNodeOperations().size());
		assertEquals(0,retEditList.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList.findAddEdgeOperations().size());
		assertEquals(0,retEditList.findRelabelNodeOperations().size());

		gRet = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());

	}

	@Test
	public void test011() throws Exception {

		long seed = 70928;
		Random r = new Random(seed);
		FastGraph g1,g2,gRet;
		HashMap<Integer,Double> editCosts;
		ExactGEDAStarIso ged;
		EditList el, retEditList1;
		ApproximateGEDSimple aged;
		
		int maxNodes = 3;
		int maxEdges = 3;
	
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,11.0);
		editCosts.put(EditOperation.ADD_NODE,12.0);
		editCosts.put(EditOperation.DELETE_EDGE,13.0);
		editCosts.put(EditOperation.ADD_EDGE,14.0);
		editCosts.put(EditOperation.RELABEL_NODE,15.0);
	
		g1 = FastGraph.randomGraphFactory(1, 1, seed+20, false);
		g2 = FastGraph.randomGraphFactory(2, 2, seed+20, false);
		ged = new ExactGEDAStarIso(true,false,editCosts);
		ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		
		gRet = retEditList1.applyOperations(g1);
		
		aged = new ApproximateGEDSimple(true,false,editCosts,0,1000,888);
		aged.similarity(g1, g2);
		assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

		assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		assertTrue(gRet.checkConsistency());
	}
	
	
	@Test
	public void test012() throws Exception {
		int count = 0;
		while(count < 10) {
			count++;
			long seed = 8866L*count;
			Random r = new Random(seed);
			FastGraph g1,g2,gRet;
			HashMap<Integer,Double> editCosts;
			ExactGEDAStarIso ged;
			EditList el, retEditList1;
			ApproximateGEDSimple aged;
			
			int maxNodes = 3;
			int maxEdges = 3;
		
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,11.0);
			editCosts.put(EditOperation.ADD_NODE,12.0);
			editCosts.put(EditOperation.DELETE_EDGE,13.0);
			editCosts.put(EditOperation.ADD_EDGE,14.0);
			editCosts.put(EditOperation.RELABEL_NODE,15.0);

			g1 = FastGraph.randomGraphFactory(r.nextInt(maxNodes)+1, r.nextInt(maxEdges+1), seed+10, false);
			el = new EditList();
			for(int i = 0; i < g1.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g1 = el.applyOperations(g1);

			g2 = FastGraph.randomGraphFactory(r.nextInt(maxNodes)+1, r.nextInt(maxEdges+1), seed+20, false);
			el = new EditList();
			for(int i = 0; i < g2.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g2 = el.applyOperations(g2);

			ged = new ExactGEDAStarIso(false,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(false,false,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(false,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(false,true,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(true,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(true,false,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(true,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(true,true,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());
		}
	}
	
	@Test
	public void test013() throws Exception {
		int count = 0;
		while(count < 10) {
			count++;
			long seed = 44332211L*count;
System.out.println(count+" "+seed);
			Random r = new Random(seed);
			FastGraph g1,g2,gRet;
			HashMap<Integer,Double> editCosts;
			ExactGEDAStarIso ged;
			EditList el, retEditList1;
			ApproximateGEDSimple aged;
			
			int maxNodes = 3;
			int maxEdges = 4;
		
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,11.0);
			editCosts.put(EditOperation.ADD_NODE,12.0);
			editCosts.put(EditOperation.DELETE_EDGE,13.0);
			editCosts.put(EditOperation.ADD_EDGE,14.0);
			editCosts.put(EditOperation.RELABEL_NODE,15.0);

			g1 = FastGraph.randomGraphFactory(r.nextInt(maxNodes)+1, r.nextInt(maxEdges+1), seed+10, false);
			el = new EditList();
			for(int i = 0; i < g1.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g1 = el.applyOperations(g1);

			g2 = FastGraph.randomGraphFactory(r.nextInt(maxNodes)+1, r.nextInt(maxEdges+1), seed+20, false);
			el = new EditList();
			for(int i = 0; i < g2.getNumberOfNodes(); i++) {
				String color = "yellow";
				int a = r.nextInt(4);
				if(a == 0) {
					color = "teal";
				}
				if(a == 1) {
					color = "black";
				}
				if(a == 2) {
					color = "red";
				};
				el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,i,color,-1,-1));
			}
			g2 = el.applyOperations(g2);

			ged = new ExactGEDAStarIso(false,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(false,false,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(false,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(false,true,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(true,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(true,false,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ExactGEDAStarIso(true,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			
			aged = new ApproximateGEDSimple(true,true,editCosts,0,1000,888);
			aged.similarity(g1, g2);
			assertTrue(retEditList1.getCost() <= aged.similarity(g1, g2));

			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());
		}
	}


	
}
