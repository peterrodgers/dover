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
	
	private Double maxCost = Double.MAX_VALUE; // this is to aid performance

	private int maxDegree2 = 0;
	private int maxInDegree2 = 0;
	private int maxOutDegree2 = 0;
private int countPruneByMaxNodeDegree = 0;
private int countPruneByNotAddingNodes = 0;
private int countPruneByNotDeletingNodes = 0;


	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		int count = 0;
		try {
			while(count < 10) {
				count++;
				long seed = 6655*count;
				Random r = new Random(seed);
				FastGraph g1,g2,gRet;
				HashMap<Integer,Double> editCosts;
				ExactGEDAStarIso ged;
				EditList el, retEditList1;
				int maxNodes = 3;
				int maxEdges = 5;
		
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
System.out.println(g1);
System.out.println(g2);
	
				ged = new ExactGEDAStarIso(false,false,editCosts);
				ged.setMaxCost(110.0);
				ged.similarity(g1, g2);
				retEditList1 = ged.getEditList();
				if(retEditList1 == null) {
					gRet = null;
				} else {
					gRet = retEditList1.applyOperations(g1);
				}

System.out.println("ITERATION "+count);
System.out.print(retEditList1);
if(retEditList1 != null) {
	System.out.println(ExactIsomorphism.isomorphic(gRet, g2)+" "+gRet.checkConsistency());
}
			}
	
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
	 * 
	 * @return the cost, which, if exceeded, stops the search.
	 */
	public Double getMaxCost() {return maxCost;}

	/** 
	 * 
	 * Setting this to a sensible value just above the expected cost
	 * can greatly improve search speeds. For instance, it is possible to
	 * keep increasing this slightly until a edit list is found. It will throw
	 * away any solutions above maximum cost, so increases both space and time
	 * performance, but particularly space.
	 * 
	 * @param maxCost defines the cost, which, if exceeded, stops the search.
	 */
	public void setMaxCost(Double maxCost) {this.maxCost = maxCost;}
	


	
	/**
	 * This returns an the graph edit distance between the two graphs. 
	 * Greater values mean more dissimilarity. 
	 * 
	 * Works by taking the current member of the editlists with least cost
	 * and adding all possible edits to it.
	 * 
	 * @param g1 the first graph to be compared.
	 * @param g2 the second graph to be compared.
	 * @return the cost of the edits between two graphs or -1 if a cost cannot be found due to no solutions less than maxCost.
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {

		g2NodeLabelSet = new HashSet<>(g2.getNumberOfNodes());
		for(int i = 0; i < g2.getNumberOfNodes(); i++) {
			g2NodeLabelSet.add(g2.getNodeLabel(i));
		}
		
		EditListCostComparator elc = new EditListCostComparator();
		PriorityQueue<EditList> currentCandidates = new PriorityQueue<>(1000000,elc);
		
		EditList startEL = new EditList();
		currentCandidates.add(startEL);
		
		ExactIsomorphism ei = new ExactIsomorphism(g2,directed,nodeLabels);

		EditList resultEditList = null;
		boolean found = false;
		int iterations = 0;
long startSearchTime = System.currentTimeMillis();
long findCheapestTime = 0;
long isomorphicTime = 0;
long addAdditionalTime = 0;
long time;
		while(!found) {
			iterations++;

time = System.currentTimeMillis();
			EditList tryEditList = null;
			FastGraph g = null;
			while(g == null) { // get the cheapest valid edit list
				tryEditList = findCheapestEditList(currentCandidates);
				if(tryEditList == null) { // no candiates left
					return -1;
				}
				g = tryEditList.applyOperations(g1);
			}
			maxDegree2 = g2.maximumDegree();
			maxInDegree2 = g2.maximumInDegree();
			maxOutDegree2 = g2.maximumOutDegree();
findCheapestTime += System.currentTimeMillis()-time;

time = System.currentTimeMillis();
			if(ei.isomorphic(g)) {
				found = true;
				resultEditList = tryEditList;
				break;
			}
isomorphicTime += System.currentTimeMillis()-time;

time = System.currentTimeMillis();
			HashSet<EditList> additionalEditLists = addAllPossibleEdits(tryEditList,g,g2);
			currentCandidates.addAll(additionalEditLists);
addAdditionalTime += System.currentTimeMillis()-time;

if(iterations%100000 == 0) {
	System.out.println("iterations "+iterations/1000000.0+ " million" +" directed "+directed+" nodeLabels "+nodeLabels);
	System.out.println("time: total\tfindCheapest\tisomorphism\tfindAdditional\t"+((System.currentTimeMillis()-startSearchTime)/1000.0)+"\t"+(findCheapestTime/1000.0)+"\t"+(isomorphicTime/1000.0)+"\t"+(addAdditionalTime/1000.0));
	System.out.println("currentCandidates.size() "+currentCandidates.size()/1000000.0+ " million\t");
	System.out.println("countPruneByMaxNodeDegree\tcountPruneByNotAddingNodes\tcountPruneByNotDeletingNodes\t"+countPruneByMaxNodeDegree/1000000.0+ " million\t"+countPruneByNotAddingNodes/1000000.0+ " million\t"+countPruneByNotDeletingNodes/1000000.0+ " million\t");
	
//	System.out.print("tryEditList\n"+tryEditList);
	System.out.println("tryEditList.getCost() "+tryEditList.getCost());
	System.out.println("tryEditList.getEditList().size() "+tryEditList.getEditList().size());
}
			
		}
		
		editList = resultEditList;
		return editList.getCost();	
	}


	/**
	 * 
	 * @param el the edit list to add edits to
	 * @param g the graph created from g1 by applying el
	 * @param gTarget the graph the edits are aiming at
	 * @return a collection of edit lists, these are copies of el with every valid edit that can be applied to g, or null if maxCost is exceeded
	 */
	private HashSet<EditList> addAllPossibleEdits(EditList el,FastGraph g, FastGraph gTarget) {
		
		HashSet<EditList> ret = new HashSet<>();

		// add node
		if(nodeLabels || (g.getNumberOfNodes() < gTarget.getNumberOfNodes())) {
			for(String label : g2NodeLabelSet) {
				EditOperation newEO = new EditOperation(EditOperation.ADD_NODE,addNodeCost,-1,label,-1,-1);
				EditList newEL = new EditList(el);
				if(newEO != null) {
					newEL.addOperation(newEO);
					if(newEL.getCost() < getMaxCost()) {
						ret.add(newEL);
					}
				}
			}
		} else {
countPruneByNotAddingNodes++;
		}

		// delete node
		if(nodeLabels || (g.getNumberOfNodes() > gTarget.getNumberOfNodes())) {
			for(int n = 0; n < g.getNumberOfNodes(); n++) {
				if(g.getNodeDegree(n) == 0) {
					EditOperation newEO = new EditOperation(EditOperation.DELETE_NODE,deleteNodeCost,n,null,-1,-1);
					EditList newEL = new EditList(el);
					newEL.addOperation(newEO);
					if(newEL.getCost() < getMaxCost()) {
						ret.add(newEL);
					}
				}
			}
		} else {
countPruneByNotDeletingNodes++;
		}

		// add edge
		for(int n1 = 0; n1 < g.getNumberOfNodes(); n1++) {
			int n2Start = 0;
			if(!directed) { // prevents lots of duplicates n1-n2 and n2-n1 from being created
				n2Start = n1;
			}
			for(int n2 = n2Start; n2 < g.getNumberOfNodes(); n2++) {
				if(pruneByMaxNodeDegree(g,n1,n2)) {
countPruneByMaxNodeDegree++;
					continue;
				}
				EditOperation newEO = new EditOperation(EditOperation.ADD_EDGE,addEdgeCost,-1,"",n1,n2);
				EditList newEL = new EditList(el);
				newEL.addOperation(newEO);
				if(newEL.getCost() < getMaxCost()) {
					ret.add(newEL);
				}
			}
		}
		
		// delete edge
		for(int e = 0; e < g.getNumberOfEdges(); e++) {
			EditOperation newEO = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,e,null,-1,-1);
			EditList newEL = new EditList(el);
			newEL.addOperation(newEO);
			if(newEL.getCost() < getMaxCost()) {
				ret.add(newEL);
			}
		}

		// relabel node
		if(nodeLabels) {
			for(int n = 0; n < g.getNumberOfNodes(); n++) {
				for(String label : g2NodeLabelSet) {
					if(!g.getNodeLabel(n).equals(label)) {
						EditOperation newEO = new EditOperation(EditOperation.RELABEL_NODE,relabelNodeCost,n,label,-1,-1);;
						EditList newEL = new EditList(el);
						newEL.addOperation(newEO);
						if(newEL.getCost() < getMaxCost()) {
							ret.add(newEL);
						}
					}
				}
			}
		}
		
		return ret;

	}


	/**
	 * When adding an edge, if any connecting node has a degree that is equal to or more
	 * than the max in the target graph,
	 * then there is no need add the el to the candidate set.
	 * @param g graph containing the nodes
	 * @param n1 source node of the potential new edge
	 * @param n2 target node of the potential new edge
	 * @return true if the el can be ignored due to node degree, false if not and so add it to the candidates.
	 */
	private boolean pruneByMaxNodeDegree(FastGraph g, int n1, int n2) {
		if(!directed) {
			if(g.getNodeDegree(n1) >= maxDegree2) {
				return true;
			}
			if(g.getNodeDegree(n2) >= maxDegree2) {
				return true;
			}
		} else {
			if(g.getNodeInDegree(n1) >= maxInDegree2) {
				return true;
			}
			if(g.getNodeInDegree(n2) >= maxInDegree2) {
				return true;
			}
			if(g.getNodeOutDegree(n1) >= maxOutDegree2) {
				return true;
			}
			if(g.getNodeOutDegree(n2) >= maxOutDegree2) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Find the cheapest candidate and remove from list.
	 * 
	 * @param currentCandidates collection of EditLists
	 * @return the cheapest of the EditLists or null if the collection is empty
	 */
	private EditList findCheapestEditList(PriorityQueue<EditList> editLists) {
		if(editLists.isEmpty()) {
			return null;
		}
		EditList ret = editLists.remove();
		return ret;
	}



	
}
