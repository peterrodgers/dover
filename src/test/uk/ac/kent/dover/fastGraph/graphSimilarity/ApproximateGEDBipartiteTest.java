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
		
		assertEquals(1,ged.getCostMatrix()[0].length);
		assertEquals(1,ged.getMapping().length);
		assertFalse(ged.getUseHungarian());
		String s = ApproximateGEDBipartite.formatMatrix(ged.getCostMatrix());
		assertTrue(s != null);

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
		FastGraph g1,g2,gRet;
		EditOperation eo;		
		EditList el, retEditList1, retEditList2;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,12.0);
		editCosts.put(EditOperation.ADD_NODE,31.0);
		editCosts.put(EditOperation.DELETE_EDGE,24.0);
		editCosts.put(EditOperation.ADD_EDGE,27.1);
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

		ged = new ApproximateGEDBipartite(editCosts);
		ret = ged.similarity(g1, g2);
		retEditList1 = ged.getEditList();
		retList = retEditList1.getEditList();

		assertEquals(24, ret, 0.001);
		assertEquals(ret, retEditList1.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.DELETE_EDGE, retList.get(0).getOperationCode());
		assertEquals(0, retList.get(0).getId());
		assertEquals(0,retEditList1.findDeleteNodeOperations().size());
		assertEquals(0,retEditList1.findAddNodeOperations().size());
		assertEquals(1,retEditList1.findDeleteEdgeOperations().size());
		assertEquals(0,retEditList1.findAddEdgeOperations().size());
		
		gRet = retEditList1.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(gRet.checkConsistency());
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		ged = new ApproximateGEDBipartite(editCosts);
		ged.setUseHungarian(true);
		ged.similarity(g1, g2);
		retEditList2 = ged.getEditList();
		gRet = retEditList2.applyOperations(g1);
		assertTrue(retEditList2.getEditList().equals(retEditList1.getEditList()));
		assertTrue(ExactIsomorphism.isomorphic(gRet,g2,false));
		assertTrue(gRet.checkConsistency());
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
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,7.0);
		editCosts.put(EditOperation.ADD_NODE,6.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
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

		ged = new ApproximateGEDBipartite(true,true,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(10, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(2, retList.size());
		assertEquals(EditOperation.RELABEL_NODE, retList.get(0).getOperationCode());
		assertEquals(EditOperation.RELABEL_NODE, retList.get(1).getOperationCode());
		assertEquals(0, retList.get(1).getId());

		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}

	
	@Test
	public void test010() throws Exception {
		double ret;
		List<EditOperation> retList;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,7.0);
		editCosts.put(EditOperation.ADD_NODE,6.0);
		editCosts.put(EditOperation.DELETE_EDGE,3.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
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
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node B",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite(false,true,editCosts);
		
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		retList = retEditList.getEditList();

		assertEquals(5, ret, 0.001);
		assertEquals(ret, retEditList.getCost(), 0.001);
		assertEquals(1, retList.size());
		assertEquals(EditOperation.RELABEL_NODE, retList.get(0).getOperationCode());
		assertEquals(1, retList.get(0).getId());
		assertEquals("node B", retList.get(0).getLabel());
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}

	
	@Test
	public void test011() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,7.0);
		editCosts.put(EditOperation.ADD_NODE,6.0);
		editCosts.put(EditOperation.DELETE_EDGE,8.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node X",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,0);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",1,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node B",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite(false,false,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(true,false,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(false,true,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(true,true,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}

	
	@Test
	public void test012() throws Exception {
		double ret;
		FastGraph g1,g2;
		EditOperation eo;		
		EditList el, retEditList;
		HashMap<Integer,Double> editCosts;
		ApproximateGEDBipartite ged;
		
		editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,7.0);
		editCosts.put(EditOperation.ADD_NODE,6.0);
		editCosts.put(EditOperation.DELETE_EDGE,8.0);
		editCosts.put(EditOperation.ADD_EDGE,4.0);
		editCosts.put(EditOperation.RELABEL_NODE,5.0);
		
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		g1 = el.applyOperations(g1);
	
		el = new EditList();
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node 0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node C",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,1.5,-1,"node A",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",1,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",1,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 3",1,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 4",2,1);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);

		ged = new ApproximateGEDBipartite(false,false,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(true,false,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,false));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(false,true,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,false,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());

		ged = new ApproximateGEDBipartite(true,true,editCosts);
		ret = ged.similarity(g1, g2);
		retEditList = ged.getEditList();
		assertEquals(ret, retEditList.getCost(), 0.001);
		g1 = retEditList.applyOperations(g1);
		assertTrue(ExactIsomorphism.isomorphic(g1,g2,true,true));
		assertTrue(g1.checkConsistency());
		assertTrue(g2.checkConsistency());
	}


	
	@Test
	public void test013() throws Exception {
		int count = 0;
		while(count < 10) {
			count++;
			long seed = 44555L*count;
			Random r = new Random(seed);
			FastGraph g1,g2,gRet;
			HashMap<Integer,Double> editCosts;
			ApproximateGEDBipartite ged;
			EditList el, retEditList1, retEditList2;
			int maxNodes = 20;
			int maxEdges = 80;
		
			editCosts = new HashMap<>();
			editCosts.put(EditOperation.DELETE_NODE,1.0);
			editCosts.put(EditOperation.ADD_NODE,1.0);
			editCosts.put(EditOperation.DELETE_EDGE,1.0);
			editCosts.put(EditOperation.ADD_EDGE,1.0);
			editCosts.put(EditOperation.RELABEL_NODE,1.0);

			g1 = FastGraph.randomGraphFactory(r.nextInt(maxNodes)+1, r.nextInt(maxEdges+1), seed+10, false);
			el = new EditList();
			for(int i = 0; i < g1.getNumberOfNodes(); i++) {
				String color = "brown";
				int a = r.nextInt(5);
				if(a == 0) {
					color = "black";
				}
				if(a == 1) {
					color = "orange";
				}
				if(a == 2) {
					color = "cyan";
				};
				if(a == 3) {
					color = "blue";
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

			ged = new ApproximateGEDBipartite(false,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(false,false,editCosts);
			ged.setUseHungarian(true);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(false,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(false,true,editCosts);
			ged.setUseHungarian(true);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,false,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(true,false,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(true,false,editCosts);
			ged.setUseHungarian(true);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,false));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(true,true,editCosts);
			ged.similarity(g1, g2);
			retEditList1 = ged.getEditList();
			gRet = retEditList1.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());

			ged = new ApproximateGEDBipartite(true,true,editCosts);
			ged.setUseHungarian(true);
			ged.similarity(g1, g2);
			retEditList2 = ged.getEditList();
			gRet = retEditList2.applyOperations(g1);
			assertTrue(ExactIsomorphism.isomorphic(g2,gRet,true,true));
			assertTrue(g1.checkConsistency());
			assertTrue(g2.checkConsistency());
			assertTrue(gRet.checkConsistency());
			
		}

	}
	
	
}
