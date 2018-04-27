package uk.ac.kent.dover.fastGraph.editOperation;

import java.util.*;
import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.EditOperationComparator;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDBipartite;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ApproximateGEDSimple;
import uk.ac.kent.dover.fastGraph.graphSimilarity.ExactGEDAStarIso;


/**
 * 
 * @author Peter Rodgers
 *
 */
public class EditList {

	LinkedList<EditOperation> editList;
	double cost = 0.0;
	
	EditOperationComparator eoc = new EditOperationComparator();
	
	static final byte MARK = (byte)111;


	public static void main(String [] args) {
		Debugger.enabled = false;
		
		HashMap<Integer,Double> editCosts = new HashMap<>();
		editCosts.put(EditOperation.DELETE_NODE,1.0);
		editCosts.put(EditOperation.ADD_NODE,3.0);
		editCosts.put(EditOperation.DELETE_EDGE,5.0);
		editCosts.put(EditOperation.ADD_EDGE,7.0);
		editCosts.put(EditOperation.RELABEL_NODE,11.0);
		
		ArrayList<String> labels = new ArrayList<>();
		labels.add("blue");
		labels.add("black");
		labels.add("green");

		
		FastGraph g;
		try {
			
//int i=3;			
			for(int i = 1; i < 200; i++) {
			
				long seed1 = System.currentTimeMillis()*i;
				long seed2 = System.currentTimeMillis()*i*999;
				long seed3 = System.currentTimeMillis()*i*3333;
				long seed4 = System.currentTimeMillis()*i*7777;
				long seed5 = System.currentTimeMillis()*i*111;
				
				System.out.println("i "+i+ " seed1: "+seed1+" seed2: "+seed2+" seed3: "+seed4+" seed3: "+seed4+" seed5: "+seed5);
				
//				seed1= 225674035693704L;
//				seed2= 225448361658010296L;
				
				boolean simple = false;
				
				g = FastGraph.randomGraphFactory(10, 15, seed1,simple, false);
				
				Random r = new Random(seed3);
				for(int n = 0; n < g.getNumberOfNodes(); n++) {
					String label = labels.get(r.nextInt(labels.size()));
					g = g.generateGraphByRelabellingNode(n, label);
				}
				
				EditList el = generateEditList(g,5,labels,editCosts, seed2);
//				System.out.println(el);
				
				FastGraph g2 = el.applyOperations(g);
				g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g2, seed5, false);
				
//				ExactGEDAStarIso exactGED = new ExactGEDAStarIso(false,true,editCosts);
//				double exactSimilarity = exactGED.similarity(g, g2);
//				EditList el2 = exactGED.getEditList();
				
				ApproximateGEDSimple simpleGED = new ApproximateGEDSimple(false,true,editCosts,100, 0, seed4);
				double simpleSimilarity = simpleGED.similarity(g, g2);
				EditList el3 = simpleGED.getEditList();

				ApproximateGEDBipartite bipartiteGED = new ApproximateGEDBipartite(false,true,editCosts);
				double bipartiteSimilarity = bipartiteGED.similarity(g, g2);
				EditList el4 = bipartiteGED.getEditList();

//System.out.println(el.getCost()+" "+exactSimilarity+" "+simpleSimilarity);
//System.out.println("calc  : "+el.getEditList().get(0)+"\nexact : "+el2.getEditList().get(0)+"\nsimple: "+el3.getEditList().get(0));
//System.out.println("calc  : "+el.getEditList().get(1)+"\nexact : "+el2.getEditList().get(1)+"\nsimple: "+el3.getEditList().get(1));
				
System.out.println("original: "+el.getCost()+" simple: "+simpleSimilarity+" bipartite: "+bipartiteSimilarity);
//System.out.println("calc  : "+el.getEditList().get(0)+"\nsimple: "+el3.getEditList().get(0));
//System.out.println("calc  : "+el.getEditList().get(1)+"\nsimple: "+el3.getEditList().get(1));
//System.out.println("calc  : "+el.getEditList().get(2)+"\nsimple: "+el3.getEditList().get(2));
//System.out.println("calc  : "+el.getEditList().get(3)+"\nsimple: "+el3.getEditList().get(3));
//System.out.println("calc  : "+el.getEditList().get(4)+"\nsimple: "+el3.getEditList().get(4));
				
//				System.out.println(g2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

			
	public EditList() {
		editList = new LinkedList<EditOperation>();
	}

	/**
	 * Makes a new list. The edit list is not shared, but the edit operations are.
	 * The expectation is that the list will have one element added at some point,
	 * hence the size of the list is slightly bigger than the length of the passed one.
	 * 
	 * @param an existing list to copy. 
	 */
	public EditList(EditList el) {
		editList = new LinkedList<EditOperation>(el.getEditList());
		cost = el.getCost();
	}

	/**
	 * 
	 * @return the editList
	 */
	public List<EditOperation> getEditList() {
		return editList;
	}
	
 	/**
 	 * Find the total cost of the edit operations.
 	 * @return
 	 */
	public double getCost() {
		return cost;
	}

	
/**
 * Add a graph edit operation to the list
 * 
 * @param eo edit operation to add to the end of the list.
 */
 	public void addOperation(EditOperation eo) {

		editList.add(eo);
		cost += eo.getCost();
	}
 	
 	/**
 	 * Apply the list in sequence.
 	 * 
 	 * @param g the graph to edit.
 	 * @return the new FastGraph, or null if any of the operation fail.
 	 */
 	public FastGraph applyOperations(FastGraph g) {
 		
 		FastGraph ret = g;
 		for(EditOperation eo : editList) {
 			ret = eo.edit(ret);
 			if(ret == null) {
 				return null;
 			}
 		}
 		
 		return ret;
 	}
 	

 	/**
 	 * 
 	 * @return returns only the delete node operations
 	 */
 	public List<EditOperation> findDeleteNodeOperations() {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.DELETE_NODE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only the add node operations
 	 */
 	public List<EditOperation> findAddNodeOperations() {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.ADD_NODE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only the delete edge operations
 	 */
 	public List<EditOperation> findDeleteEdgeOperations() {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.DELETE_EDGE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only the add edge operations
 	 */
 	public List<EditOperation> findAddEdgeOperations() {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.ADD_EDGE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * 
 	 * @return returns only the relabel node operations
 	 */
 	public List<EditOperation> findRelabelNodeOperations() {
 		LinkedList<EditOperation> ret = new LinkedList<>();
 		
 		for(EditOperation eo : editList) {
 			if(eo.getOperationCode() == EditOperation.RELABEL_NODE) {
 				ret.add(eo);
 			}
 		}
 		
 		return ret;
 	}

 	/**
 	 * Sort the edit list by preferred operation order.
 	 */
	public void sort() {
		Collections.sort(editList,eoc);
	}
	
	/**
	 * @return the hashCode of the list
	 */
	@Override
	public int hashCode() {
    	return editList.hashCode();
	}
	
    /**
 	 * @param obj the object to compare.
     * @return equality of the lists
     */
   @Override
	public boolean equals(Object obj) {
    	if (!(obj instanceof EditList)) {
    		return false;
    	}
    	EditList el = (EditList) obj;
    	return editList.equals(el.editList);
	}
    
	/**
	 * @return a String output for debugging
	 */
	public String toString() {
		String ret = "";
		for(EditOperation eo : editList) {
			ret += eo.toString()+"\n";
		}
		return ret;
	}
	
	
	
	/**
	 * 
	 * Generate a list of edit operations. Does not delete an added edge,
	 * does not delete a relabelled node, does not add a edge between two nodes that have an edge deleted.
	 * Either adds or deletes nodes, not both. The idea is to produce an edit list that is close as possible to the minimum needed
	 * to get to the new graph
	 * 
	 * @param g the graph to apply edit operations on
	 * @param numberOfEdits length of edit list
	 * @param nodeLables is the list of possible, RELABEL_NODE labels, if null, RELABEL_NODE operation are not included
	 * @param editCost the cost of edit operations
	 * @param seed the random number generator seed, pass System.currentTimeMillis() if in doubt
	 * @return the edit list with the edit operations on g
	 */
	public static EditList generateEditList(FastGraph g, int numberOfEdits, ArrayList<String> nodeLabels, HashMap<Integer,Double> editCosts, long seed) {
		
		Double deleteNodeCost = editCosts.get(EditOperation.DELETE_NODE);
		Double addNodeCost = editCosts.get(EditOperation.ADD_NODE);
		Double deleteEdgeCost = editCosts.get(EditOperation.DELETE_EDGE);
		Double addEdgeCost = editCosts.get(EditOperation.ADD_EDGE);
		Double relabelNodeCost = editCosts.get(EditOperation.RELABEL_NODE);

		boolean addedNode = false;
		boolean deletedNode = false;
		
		EditList ret = new EditList();

		Random r = new Random(seed);
		
		EditOperation eo = null;
		
		FastGraph currentGraph = g;
		
		int markNodeId = -1;
		int markEdgeId = -1;

		
		while(ret.getEditList().size() < numberOfEdits) {
			int editCode = -1;
			if(nodeLabels != null) {
				editCode = r.nextInt(5); // include node relabelling
			} else {
				editCode = r.nextInt(4); // do not include node relabelling
			}
			
			if((editCode == 0 && !deletedNode) || currentGraph.getNumberOfNodes() == 0) { // try adding a node
				addedNode = true; // this fixes it so only node additions, no node deletions occur from now on
				String label = "new node";
				if(nodeLabels != null) {
					int index = r.nextInt(nodeLabels.size());
					label = nodeLabels.get(index);
				}
				eo = new EditOperation(EditOperation.ADD_NODE,addNodeCost,-1,label,-1,-1);
				markNodeId = currentGraph.getNumberOfNodes();
				
			}
			if(editCode == 1 && !addedNode) { // try deleting a node

				deletedNode = true; // even if we don't delete a node, this will fix it so no node additions occur, only node deletions
				int deleteIndex = r.nextInt(currentGraph.getNumberOfNodes());
				if(currentGraph.getNodeDegree(deleteIndex) > 0) {
					continue;
				}
				if(currentGraph.getNodeType(deleteIndex) == MARK) {
					continue;
				}
				eo = new EditOperation(EditOperation.DELETE_NODE,deleteNodeCost,deleteIndex,null,-1,-1);

			}
			if(editCode == 2) { // try adding an edge
				int n1 = r.nextInt(currentGraph.getNumberOfNodes());
				int n2 = r.nextInt(currentGraph.getNumberOfNodes());
				
				eo = new EditOperation(EditOperation.ADD_EDGE,addEdgeCost,-1,"added Edge",n1,n2);
				markEdgeId = currentGraph.getNumberOfEdges();

			}
			if(editCode == 3) { // try deleting an edge
				if(currentGraph.getNumberOfEdges() == 0) {
					continue;
				}
				int deleteIndex = r.nextInt(currentGraph.getNumberOfEdges());
				if(currentGraph.getEdgeType(deleteIndex) == MARK) {
					continue;
				}
				eo = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,deleteIndex,null,-1,-1);
			}
			if(editCode == 4) { // try relabelling
				// find an label
				int index = r.nextInt(nodeLabels.size());
				String label = nodeLabels.get(index);
				
				// check for previous edit operations on the node
				int relabelIndex = r.nextInt(currentGraph.getNumberOfNodes());
				String currentLabel = currentGraph.getNodeLabel(relabelIndex);
				if(label.equals(currentLabel)) {
					continue;
				}
				if(currentGraph.getNodeType(relabelIndex) == MARK) {
					continue;
				}
				eo = new EditOperation(EditOperation.RELABEL_NODE,relabelNodeCost,relabelIndex,label,-1,-1);
				markNodeId = relabelIndex;
			}
			
			
			if(eo != null) {
				ret.addOperation(eo);
				currentGraph = eo.edit(currentGraph);
				if(markNodeId > 0 ) {
					currentGraph.setNodeType(markNodeId, MARK);
				}
				if(markEdgeId > 0 ) {
					currentGraph.setEdgeType(markEdgeId, MARK);
				}
				eo = null;
			}
			
			markNodeId = -1;
			markEdgeId = -1;

		}
		
		
		return ret;
	}


}
