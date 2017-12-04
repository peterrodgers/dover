package test.uk.ac.kent.dover.fastGraph.editOperation;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.SimpleNodeLabelComparator;
import uk.ac.kent.dover.fastGraph.editOperation.*;

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
		SimpleNodeLabelComparator snlc;
		
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
		
		snlc = new SimpleNodeLabelComparator(g,g2);
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,snlc));
		assertTrue(g.checkConsistency());
		assertTrue(g2.checkConsistency());
		
		g = FastGraph.randomGraphFactory(0, 0, false);
		g = el1.applyOperations(g);
		el2 = new EditList(el1);
		g2 = FastGraph.randomGraphFactory(0, 0, false);
		g2 = el2.applyOperations(g2);
		assertEquals(8,el1.getEditList().size());
		assertEquals(8,el2.getEditList().size());
		snlc = new SimpleNodeLabelComparator(g,g2);
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,snlc));
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
		snlc = new SimpleNodeLabelComparator(g,g2);
		assertTrue(ExactIsomorphism.isomorphic(g,g2,true,snlc));
		assertTrue(g2.checkConsistency());
		
	}
	
	
}
