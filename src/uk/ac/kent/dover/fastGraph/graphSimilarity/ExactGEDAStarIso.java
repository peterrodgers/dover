package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.comparators.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;

/**
 * A simple approximation method for GED, make a node match, add in nodes,
 * delete unnecessary edges, add in necessary edges.
 * 
 * @author Peter Rodgers
 *
 */
public class ExactGEDAStarIso extends GraphEditDistance {

	ArrayList<EditOperation> editOperations;
	boolean directed;
	boolean nodeLabels;
	HashMap<Integer,Double> editCosts;
	
	HashSet<String> g2NodeLabelSet;
	
	private Double deleteNodeCost;
	private Double addNodeCost;
	private Double deleteEdgeCost;
	private Double addEdgeCost;
	private Double relabelNodeCost;

	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {
			long seed = 22114455;
			Random r = new Random(seed);
			FastGraph g1,g2,gRet;
			HashMap<Integer,Double> editCosts;
			ExactGEDAStarIso ged;
			EditList el, retEditList1;
			int maxNodes = 4;
			int maxEdges = 7;
		
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

System.out.println(retEditList1);
System.out.println(ExactIsomorphism.isomorphic(gRet, g2)+" "+gRet.checkConsistency());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	/**
	 * defaults to treating graph as undirected and no node label comparison,
	 * with all edit costs at 1.0.
	 * 
	 * @throws FastGraphException should not throw this as all required edit operations are present by default
	 */
	public ExactGEDAStarIso() throws FastGraphException {
		super();
		HashMap<Integer,Double> defaultEditCosts = new HashMap<>();
		defaultEditCosts.put(EditOperation.DELETE_NODE,1.0);
		defaultEditCosts.put(EditOperation.ADD_NODE,1.0);
		defaultEditCosts.put(EditOperation.DELETE_EDGE,1.0);
		defaultEditCosts.put(EditOperation.ADD_EDGE,1.0);
		this.editCosts = defaultEditCosts;

		init();
	}

	/**
	 * defaults to treating graph as undirected and no node label comparison.
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * 
	 * @param editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ExactGEDAStarIso(HashMap<Integer,Double> editCosts) throws FastGraphException {
		
		super();
		this.editCosts = editCosts;
		this.directed = false;
		this.nodeLabels = false;
		
		init();
	}

	/**
	 * editOperations should include nodeDelete, nodeAdd, edgeDelete and edgeAdd.
	 * Defaults to no attempted node map swaps
	 * 
	 * @param directed true if the graph is treated as directed, false if undirected
	 * @param nodeLabels true if node label operations should be considered, false if they are ignored
	 * @param editCosts are a mapping between edit codes and their costs DELETE_NODE, ADD_NODE, DELETE_EDGE, ADD_EDGE, if @see{nodeLabels} is true, RELABEL_NODE also needs to be present
	 * @throws FastGraphException if an edit operation cost is missing
	 */
	public ExactGEDAStarIso(boolean directed, boolean nodeLabels, HashMap<Integer,Double> editCosts) throws FastGraphException {

		super();
		this.editCosts = editCosts;
		this.directed = directed;
		this.nodeLabels = nodeLabels;
		
		init();
	}

	/**
	 * Set up the edit operation costs from the cost mapping.
	 * 
	 * @throws FastGraphException if any required cost is not present
	 */
	private void init() throws FastGraphException {

		deleteNodeCost = editCosts.get(EditOperation.DELETE_NODE);
		addNodeCost = editCosts.get(EditOperation.ADD_NODE);
		deleteEdgeCost = editCosts.get(EditOperation.DELETE_EDGE);
		addEdgeCost = editCosts.get(EditOperation.ADD_EDGE);
		relabelNodeCost = editCosts.get(EditOperation.RELABEL_NODE);

		if(deleteNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.DELETE_NODE");
		}
		if(addNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.ADD_NODE");
		}
		if(deleteEdgeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.DELETE_EDGE");
		}
		if(addEdgeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.ADD_EDGE");
		}
		if(nodeLabels && relabelNodeCost == null) {
			throw new FastGraphException("Missing editCosts entry for EditOperation.RELABEL_NODE");
		}
		
	}

	
	/**
	 * This returns an the graph edit distance between the two graphs. 
	 * Greater values mean more dissimilarity. 
	 * 
	 * Works by taking the current member of the editlists with least cost
	 * and adding all possible edits to it.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the cost of the edits between two graphs.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {

		g2NodeLabelSet = new HashSet<>(g2.getNumberOfNodes());
		for(int i = 0; i < g2.getNumberOfNodes(); i++) {
			g2NodeLabelSet.add(g2.getNodeLabel(i));
		}
		
		HashSet<EditList> currentCandidates = new HashSet<>(1000000);
		
		EditList startEL = new EditList();
		currentCandidates.add(startEL);
		
		ExactIsomorphism ei = new ExactIsomorphism(g2,directed,nodeLabels);

		EditList resultEditList = null;
		boolean found = false;
		while(!found) {
			
			EditList tryEditList = null;
			FastGraph g = null;
			while(g == null) { // get the cheapest valid edit list
				tryEditList = findCheapestEditList(currentCandidates);
				currentCandidates.remove(tryEditList);
				g = tryEditList.applyOperations(g1);
			}
			
			if(ei.isomorphic(g)) {
				found = true;
				resultEditList = tryEditList;
				break;
			}
			
			HashSet<EditList> additionalEditLists = addAllPossibleEdits(tryEditList,g);
			currentCandidates.addAll(additionalEditLists);
//System.out.println("currentCandidates.size() "+currentCandidates.size());
//System.out.println("tryEditList.getCost() "+tryEditList.getCost());
//System.out.println("tryEditList.getEditList().size() "+tryEditList.getEditList().size());
//System.out.println("tryEditList\n"+tryEditList);
			
		}
		
		editList = resultEditList;
		return editList.getCost();	
	}


	/**
	 * 
	 * @param el the edit list to add edits to
	 * @param g the graph created from g1 by applying el
	 * @return a collection of edit lists, these are copies of el with every valid edit that can be applied to g
	 */
	private HashSet<EditList> addAllPossibleEdits(EditList el,FastGraph g) {
		
		HashSet<EditList> ret = new HashSet<>();
		
		for(String label : g2NodeLabelSet) {
			EditList newEL = new EditList(el);
			EditOperation newEO = new EditOperation(EditOperation.ADD_NODE,addNodeCost,-1,label,-1,-1);
			if(newEO != null) {
				newEL.addOperation(newEO);
				ret.add(newEL);
			}
		}
		
		for(int n = 0; n < g.getNumberOfNodes(); n++) {
			if(g.getNodeDegree(n) == 0) {
				EditList newEL = new EditList(el);
				EditOperation newEO = new EditOperation(EditOperation.DELETE_NODE,deleteNodeCost,n,null,-1,-1);
				newEL.addOperation(newEO);
				ret.add(newEL);
			}
		}

		for(int n1 = 0; n1 < g.getNumberOfNodes(); n1++) {
			for(int n2 = 0; n2 < g.getNumberOfNodes(); n2++) {
				EditList newEL = new EditList(el);
				EditOperation newEO = new EditOperation(EditOperation.ADD_EDGE,addEdgeCost,-1,"",n1,n2);
				newEL.addOperation(newEO);
				ret.add(newEL);
			}
		}
		for(int e = 0; e < g.getNumberOfEdges(); e++) {
			EditList newEL = new EditList(el);
			EditOperation newEO = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,e,null,-1,-1);
			newEL.addOperation(newEO);
			ret.add(newEL);
		}
	
		if(nodeLabels) {
			for(int n = 0; n < g.getNumberOfNodes(); n++) {
				for(String label : g2NodeLabelSet) {
					if(!g.getNodeLabel(n).equals(label)) {
						EditList newEL = new EditList(el);
						EditOperation newEO = new EditOperation(EditOperation.RELABEL_NODE,relabelNodeCost,n,label,-1,-1);;
						newEL.addOperation(newEO);
						ret.add(newEL);
					}
				}
			}
		}

		
		return ret;

	}


	/**
	 * 
	 * @param currentCandidates collection of EditLists
	 * @return the cheapest of the EditLists or null if the collection is empty
	 */
	private EditList findCheapestEditList(HashSet<EditList> editLists) {
		
		EditList ret = null;
		double cheapest = Double.MAX_VALUE;

		for(EditList el : editLists) {
			if(el.getCost() < cheapest) {
				cheapest = el.getCost();
				ret = el;
			}
		}
		
		return ret;

	}
	
	
}
