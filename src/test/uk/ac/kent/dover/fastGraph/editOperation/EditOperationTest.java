package test.uk.ac.kent.dover.fastGraph.editOperation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDSimple;

/**
 * 
 * @author Peter Rodgers
 *
 */
public class EditOperationTest {

	@Test
	public void test001() throws Exception {
		EditOperation deleteNode, addNode, deleteEdge, addEdge, relabelNode;		
		EditList el1,el2;
		FastGraph g;
		
		el1 = new EditList();
		addNode = new EditOperation(EditOperation.ADD_NODE,1,-1,"node 0",-1,-1);
		el1.addOperation(addNode);
		addEdge = new EditOperation(EditOperation.ADD_NODE,2,-1,"node 1",-1,-1);
		el1.addOperation(addEdge);
		addEdge = new EditOperation(EditOperation.ADD_EDGE,4,-1,"edge 0",0,1);
		el1.addOperation(addEdge);
		
		assertEquals(7.0,el1.getCost(),0.001);
		assertEquals(3,el1.getEditList().size());
		
		g = FastGraph.randomGraphFactory(0, 0, false);
		g = el1.applyOperations(g);
		
		assertEquals(2,g.getNumberOfNodes());
		assertEquals(1,g.getNumberOfEdges());
		assertEquals("node 1",g.getNodeLabel(1));
		assertEquals("edge 0",g.getEdgeLabel(0));
		
		el2 = new EditList();
		deleteNode = new EditOperation(EditOperation.DELETE_NODE,9,0,null,-1,-1);
		el2.addOperation(deleteNode);
		assertEquals(9.0,el2.getCost(),0.001);
		assertEquals(null,el2.applyOperations(g));

		el2 = new EditList();
		deleteEdge = new EditOperation(EditOperation.DELETE_EDGE,5,0,null,-1,-1);
		el2.addOperation(deleteEdge);
		relabelNode = new EditOperation(EditOperation.RELABEL_NODE,4,1,"new name",-1,-1);
		el2.addOperation(relabelNode);
		deleteNode = new EditOperation(EditOperation.DELETE_NODE,9,0,null,-1,-1);
		el2.addOperation(deleteNode);

		assertEquals(18.0,el2.getCost(),0.001);
		assertEquals(3,el2.getEditList().size());
		
		g = el2.applyOperations(g);
		
		assertEquals(1,g.getNumberOfNodes());
		assertEquals(0,g.getNumberOfEdges());
		assertEquals("new name",g.getNodeLabel(0));
		assertTrue(g.checkConsistency());
	}
	
	@Test
	public void test002() throws Exception {
		EditOperation deleteNode, addNode, deleteEdge, addEdge, relabelNode;		
		EditList el1,el2;
		FastGraph g,g2;
		
		el1 = new EditList();
		addNode = new EditOperation(EditOperation.ADD_NODE,3,-1,"node 0",-1,-1);
		el1.addOperation(addNode);
		addNode = new EditOperation(EditOperation.ADD_NODE,1,-1,"node 1",-1,-1);
		el1.addOperation(addNode);
		addNode = new EditOperation(EditOperation.ADD_NODE,1,-1,"node 2",-1,-1);
		el1.addOperation(addNode);
		addEdge = new EditOperation(EditOperation.ADD_EDGE,2,-1,"edge 0",0,1);
		el1.addOperation(addEdge);
		addEdge = new EditOperation(EditOperation.ADD_EDGE,4,-1,"edge 1",0,2);
		el1.addOperation(addEdge);
		deleteEdge = new EditOperation(EditOperation.DELETE_EDGE,5,0,null,-1,-1);
		el1.addOperation(deleteEdge);
		relabelNode = new EditOperation(EditOperation.RELABEL_NODE,4,2,"new name",-1,-1);
		el1.addOperation(relabelNode);
		deleteNode = new EditOperation(EditOperation.DELETE_NODE,9,1,null,-1,-1);
		el1.addOperation(deleteNode);
		assertEquals(29.0,el1.getCost(),0.001);
		assertEquals(8,el1.getEditList().size());
		
		g = FastGraph.randomGraphFactory(0, 0, false);
		g = el1.applyOperations(g);
		
		assertEquals(2,g.getNumberOfNodes());
		assertEquals(1,g.getNumberOfEdges());
		assertEquals("new name",g.getNodeLabel(1));
		
		el2 = new EditList(el1);
		
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el1.applyOperations(g2);
		
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,true));
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		g = FastGraph.randomGraphFactory(0, 0, false);
		g = el1.applyOperations(g);
		el2 = new EditList(el1);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el2.applyOperations(g2);
		assertEquals(8,el1.getEditList().size());
		assertEquals(8,el2.getEditList().size());
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,true));
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		addNode = new EditOperation(EditOperation.ADD_NODE,1,-1,"node 3",-1,-1);
		el1.addOperation(addNode);
		assertEquals(9,el1.getEditList().size());
		assertEquals(8,el2.getEditList().size());
		g = FastGraph.randomGraphFactory(0, 0, false);
		g = el1.applyOperations(g);
		assertTrue(g.checkConsistency());
		
		addNode = new EditOperation(EditOperation.ADD_NODE,1,-1,"node 3",-1,-1);
		el2.addOperation(addNode);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el2.applyOperations(g2);
		assertEquals(9,el1.getEditList().size());
		assertEquals(9,el2.getEditList().size());
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el2.applyOperations(g2);
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,true));
		assertTrue(g2.checkConsistency());
		
	}
	
	
	@Test
	public void test003() throws Exception {
		FastGraph g2;
		EditOperation eo;		
		EditList el;

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
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 0",0,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 1",1,3);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,1.5,-1,"edge 2",0,4);
		el.addOperation(eo);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el.applyOperations(g2);
		
		
		int[] c = g2.getNodeConnectingEdges(0);
		assertEquals(2,c.length);
		assertEquals(0,g2.getEdgeNode1(c[0]));
		assertEquals(2,g2.getEdgeNode2(c[0]));
		assertEquals(0,g2.getEdgeNode1(c[1]));
		assertEquals(4,g2.getEdgeNode2(c[1]));
	}

	
	@Test
	public void test004() throws Exception {
		FastGraph g1;
		EditOperation eo;		
		EditList el;

		el = new EditList();
		el.sort();
		assertEquals(0, el.getEditList().size());
		
		eo = new EditOperation(EditOperation.DELETE_EDGE,2.5,0,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.DELETE_NODE,2.5,2,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.DELETE_NODE,2.5,3,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,3.5,-1,"node0",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,7.5,-1,"edge 0",0,1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,3.5,-1,"node1",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.DELETE_EDGE,2.5,1,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,3.5,-1,"node2",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_NODE,3.5,-1,"node3",-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.DELETE_NODE,2.5,1,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,3.5,-1,"edge2",3,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,3.5,-1,"edge3",1,2);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.DELETE_EDGE,3.5,2,null,-1,-1);
		el.addOperation(eo);
		eo = new EditOperation(EditOperation.ADD_EDGE,3.5,-1,"edge1",0,0);
		el.addOperation(eo);
		g1 = FastGraph.randomGraphFactory(0, 0, false);
		el.sort();
		
		assertEquals(EditOperation.ADD_NODE, el.getEditList().get(0).getOperationCode());
		assertEquals(EditOperation.ADD_NODE, el.getEditList().get(1).getOperationCode());
		assertEquals(EditOperation.ADD_NODE, el.getEditList().get(2).getOperationCode());
		assertEquals(EditOperation.ADD_NODE, el.getEditList().get(3).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, el.getEditList().get(4).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, el.getEditList().get(5).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, el.getEditList().get(6).getOperationCode());
		assertEquals(EditOperation.ADD_EDGE, el.getEditList().get(7).getOperationCode());
		assertEquals(EditOperation.DELETE_EDGE, el.getEditList().get(8).getOperationCode());
		assertEquals(2, el.getEditList().get(8).getId());
		assertEquals(EditOperation.DELETE_EDGE, el.getEditList().get(9).getOperationCode());
		assertEquals(1, el.getEditList().get(9).getId());
		assertEquals(EditOperation.DELETE_EDGE, el.getEditList().get(10).getOperationCode());
		assertEquals(0, el.getEditList().get(10).getId());
		assertEquals(EditOperation.DELETE_NODE, el.getEditList().get(11).getOperationCode());
		assertEquals(3, el.getEditList().get(11).getId());
		assertEquals(EditOperation.DELETE_NODE, el.getEditList().get(12).getOperationCode());
		assertEquals(2, el.getEditList().get(12).getId());
		assertEquals(EditOperation.DELETE_NODE, el.getEditList().get(13).getOperationCode());
		assertEquals(1, el.getEditList().get(13).getId());
		
		g1 = el.applyOperations(g1);
		assertTrue(g1.checkConsistency());

	}

	
	
}
