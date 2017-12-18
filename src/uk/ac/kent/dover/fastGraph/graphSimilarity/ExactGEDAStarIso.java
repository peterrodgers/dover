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
private int countPruneBySingleRelabelling = 0;
private int countPruneByEdgePreviouslyAdded = 0;
private int countPruneByEdgePreviouslyDeleted = 0;


	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {
			
			
			int count = 0;
			while(count < 10) {
				count++;
				long seed = 884455*count;
				Random r = new Random(seed);
				FastGraph g1,g2,gRet;
				HashMap<Integer,Double> editCosts;
				ExactGEDAStarIso ged;
				EditList el, retEditList1;
				ApproximateGEDSimple aged;
				
				int maxNodes = 4;
				int maxEdges = 6;
			
				boolean directed = false;
				boolean nodeLabels = false;
		
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
System.out.println("GED TEST "+count);
//System.out.println(g1);
//System.out.println(g2);
				aged = new ApproximateGEDSimple(directed,nodeLabels,editCosts,0,1000,888);
				aged.similarity(g1, g2);
long approxStartTime = System.currentTimeMillis();
System.out.println("approx time "+(System.currentTimeMillis()-approxStartTime)/1000.0+" approx cost "+aged.getEditList().getCost()+" approx length "+aged.getEditList().getEditList().size());	
				ged = new ExactGEDAStarIso(directed,nodeLabels,editCosts);
				ged.similarity(g1, g2);
				retEditList1 = ged.getEditList();
				if(retEditList1 == null) {
					gRet = null;
				} else {
					gRet = retEditList1.applyOperations(g1);
				}

//System.out.print(retEditList1);
if(retEditList1 != null) {
	System.out.println("exact time "+(ged.fullSearchTime)/1000.0+" exact cost "+retEditList1.getCost()+" exact length "+retEditList1.getEditList().size()+" same as approx "+(retEditList1.getCost() == aged.getEditList().getCost())+" checks "+ExactIsomorphism.isomorphic(gRet, g2, directed, nodeLabels)+" "+gRet.checkConsistency());
	if(retEditList1.getCost() > aged.getEditList().getCost()) {
		System.out.print("************* ERROR approx cost less than exact cost. seed: "+seed+" count: "+count);
	}
} else {
	System.out.println("NO SOLUTION ");
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
	 * Don't use this unless you have a pressing reason, the algorithm defaults this to an approximation of GED.
	 * 
	 * @param maxCost defines the cost, which, if exceeded, stops the search.
	 */
	public void setMaxCost(Double maxCost) {this.maxCost = maxCost;}
	

long fullSearchTime = 0;
	
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
	 * @throws FastGraphException if an EditList tries to delete an attached node
	 */
	@Override
	public double similarity(FastGraph g1, FastGraph g2) {

		if(Math.abs(Double.MAX_VALUE-maxCost) < 0.001) { // if maxCost has not been set, use an approximation to set it.
			try {
				ApproximateGEDSimple aged = new ApproximateGEDSimple(directed,nodeLabels,editCosts,0,100,888);
				double approxCost = aged.similarity(g1, g2);
				maxCost = approxCost + 0.001;
			} catch (FastGraphException e) {} // if it fails, then never mind.
		}



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
				if(tryEditList == null) { // no candidates left
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
			try {
				HashSet<EditList> additionalEditLists = addAllPossibleEdits(tryEditList,g,g1,g2);
				currentCandidates.addAll(additionalEditLists);
			} catch (FastGraphException e) {
				e.printStackTrace();
				return -1;
			}
addAdditionalTime += System.currentTimeMillis()-time;

if(iterations%100000 == 0) {
	fullSearchTime = (System.currentTimeMillis()-startSearchTime);
	System.out.println("iterations "+iterations/1000000.0+ " million, directed: "+directed+", nodeLabels: "+nodeLabels);
	System.out.println("time: total\tfindCheapest\tisomorphism\tfindAdditional\t"+(fullSearchTime/1000.0)+"\t"+(findCheapestTime/1000.0)+"\t"+(isomorphicTime/1000.0)+"\t"+(addAdditionalTime/1000.0));
	System.out.println("currentCandidates.size() "+currentCandidates.size()/1000000.0+ " million\t");
	System.out.println("countPruneByMaxNodeDegree: "+countPruneByMaxNodeDegree/1000000.0+ " million"+" countPruneByNotAddingNodes: "+countPruneByNotAddingNodes/1000000.0+ " million "+"countPruneByNotDeletingNodes: "+countPruneByNotDeletingNodes/1000000.0+ " million\t"+" countPruneBySingleRelabelling: "+countPruneBySingleRelabelling/1000000.0+ " million countPruneByEdgePreviouslyAdded: "+countPruneByEdgePreviouslyAdded/1000000.0+ " million countPruneByEdgePreviouslyDeleted: "+countPruneByEdgePreviouslyDeleted/1000000.0+ " million");	
	
//	System.out.print("tryEditList\n"+tryEditList);
	System.out.println("tryEditList.getCost() "+tryEditList.getCost());
	System.out.println("tryEditList.getEditList().size() "+tryEditList.getEditList().size());
}
			
		}
fullSearchTime = (System.currentTimeMillis()-startSearchTime);
		
		editList = resultEditList;
		return editList.getCost();	
	}


	/**
	 * 
	 * @param el the edit list to add edits to
	 * @param g the graph created from g1 by applying el
	 * @param gSource the graph the edits started from
	 * @param gTarget the graph the edits are aiming at
	 * @return a collection of edit lists, these are copies of el with every valid edit that can be applied to g, or null if maxCost is exceeded
	 * @throws FastGraphException if an EditList tries to delete an attached node
	 */
	private HashSet<EditList> addAllPossibleEdits(EditList el,FastGraph g, FastGraph gSource, FastGraph gTarget) throws FastGraphException {
		
		HashSet<EditList> ret = new HashSet<>();
		
		ArrayList<Integer> relabelledNodes = new ArrayList<>(gTarget.getNumberOfNodes());
		ArrayList<Integer> edgeN1Deleted = new ArrayList<>(gTarget.getNumberOfNodes());
		ArrayList<Integer> edgeN2Deleted = new ArrayList<>(gTarget.getNumberOfNodes());
		ArrayList<Integer> edgeN1Added = new ArrayList<>(gTarget.getNumberOfNodes());
		ArrayList<Integer> edgeN2Added = new ArrayList<>(gTarget.getNumberOfNodes());
/*
try {
g = FastGraph.randomGraphFactory(10, 0, false);
} catch(Exception e) {}
el = new EditList();
el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,111,10,"def",-1,-1));
el.addOperation(new EditOperation(EditOperation.ADD_EDGE,111,-1,"blue",15,12));
el.addOperation(new EditOperation(EditOperation.DELETE_EDGE,111,-1,"blue",7,17));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,0,null,-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_EDGE,111,-1,"blue",12,11));
el.addOperation(new EditOperation(EditOperation.ADD_EDGE,111,-1,"blue",11,3));
el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,111,43,"hykr",-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,4,null,-1,-1));
el.addOperation(new EditOperation(EditOperation.ADD_EDGE,111,-1,"blue",4,40));
el.addOperation(new EditOperation(EditOperation.ADD_EDGE,111,-1,"",15,50));
el.addOperation(new EditOperation(EditOperation.DELETE_EDGE,111,-1,"blue",2,500));
el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,111,87,"red",-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,45,null,-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,8,null,-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,498,null,-1,-1));
el.addOperation(new EditOperation(EditOperation.DELETE_NODE,111,3,null,-1,-1));

System.out.println("relabeled nodes 40 84");
System.out.println("n1 Added 11 3 13");
System.out.println("n2 Added 8 38 47");
System.out.println("n1 Deleted 4 9");
System.out.println("n2 Deleted 13 8");
//System.out.println(el);
*/
		for(EditOperation eo : el.getEditList()) {
			if(eo.getOperationCode() == EditOperation.RELABEL_NODE) {
				relabelledNodes.add(eo.getId());
			}
			if(eo.getOperationCode() == EditOperation.DELETE_EDGE) {
				edgeN1Deleted.add(eo.getN1());
				edgeN2Deleted.add(eo.getN2());
			}
			if(eo.getOperationCode() == EditOperation.ADD_EDGE) {
				edgeN1Added.add(eo.getN1());
				edgeN2Added.add(eo.getN2());
			}
			
			// have to sort out potential node id changes
			if(eo.getOperationCode() == EditOperation.DELETE_NODE) {
				int deletedNode = eo.getId();
				
				// relabelling 
				for (int i = 0; i < relabelledNodes.size(); i++) {
					int n = relabelledNodes.get(i);
					if(n == deletedNode) {
						relabelledNodes.remove(i);
						i--;
					}
					if(n > deletedNode) {
						relabelledNodes.set(i,n-1);
					}
				}

				// deleted edge lists
				for (int i = 0; i < edgeN1Deleted.size(); i++) {
					int n1 = edgeN1Deleted.get(i);
					int n2 = edgeN2Deleted.get(i);
					if(n1 == deletedNode || n2 == deletedNode) { // a connection was deleted - presumably after this edge was added then deleted, so delete from consideration
						edgeN1Deleted.remove(i);
						edgeN2Deleted.remove(i);
						i--;
					}  else {
						if(n1 > deletedNode) {
							edgeN1Deleted.set(i, n1-1);
						}
						if(n2 > deletedNode) {
							edgeN2Deleted.set(i, n2-1);
						}
					}
				}

				// added edge lists
				for (int i = 0; i < edgeN1Added.size(); i++) {
					int n1 = edgeN1Added.get(i);
					int n2 = edgeN2Added.get(i);
					if(n1 == deletedNode || n2 == deletedNode) { // a connection was deleted - presumably after this edge was added then deleted, so delete from consideration
						edgeN1Added.remove(i);
						edgeN2Added.remove(i);
						i--;
					}  else {
						if(n1 > deletedNode) {
							edgeN1Added.set(i, n1-1);
						}
						if(n2 > deletedNode) {
							edgeN2Added.set(i, n2-1);
						}
					}
				}

			}
		}
/*
System.out.println("relabelled nodes : "+relabelledNodes);
System.out.println("n1 Added : "+edgeN1Added);
System.out.println("n2 Added : "+edgeN2Added);
System.out.println("n1 Deleted : "+edgeN1Deleted);
System.out.println("n2 Deleted : "+edgeN2Deleted);
//if(true) {
//	System.exit(0);
//}
*/

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
				for (int i = 0; i < edgeN1Deleted.size(); i++) { // test to avoid adding nodes that are deleted in the EditList
					int nodeDeleted1 = edgeN1Deleted.get(i);
					int nodeDeleted2 = edgeN2Deleted.get(i);
					if(n1 == nodeDeleted1 && n2 == nodeDeleted2) { // a connection was deleted - presumably after this edge was added then deleted, so delete from consideration
countPruneByEdgePreviouslyDeleted++;
						continue;
					}
					if(!directed && n2 == nodeDeleted1 && n2 == nodeDeleted2) { // without direction, we should check both ways
countPruneByEdgePreviouslyDeleted++;
						continue;
					}
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
			int n1 = g.getEdgeNode1(e);
			int n2 = g.getEdgeNode2(e);
			for (int i = 0; i < edgeN1Added.size(); i++) { // test to avoid deleting nodes that are added in the EditList
				int nodeAdded1 = edgeN1Added.get(i);
				int nodeAdded2 = edgeN2Added.get(i);
				if(n1 == nodeAdded1 && n2 == nodeAdded2) { // a connection was deleted - presumably after this edge was added then deleted, so delete from consideration
countPruneByEdgePreviouslyAdded++;
					continue;
				}
				if(!directed && n2 == nodeAdded1 && n2 == nodeAdded2) { // without direction, we should check both ways
countPruneByEdgePreviouslyAdded++;
					continue;
				}
			}

			EditOperation newEO = new EditOperation(EditOperation.DELETE_EDGE,deleteEdgeCost,e,null,n1,n1);
			EditList newEL = new EditList(el);
			newEL.addOperation(newEO);
			if(newEL.getCost() < getMaxCost()) {
				ret.add(newEL);
			}
		}

		// relabel node
		if(nodeLabels) {
			for(int n = 0; n < g.getNumberOfNodes(); n++) {
				if(!relabelledNodes.contains(n)) {
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
				} else {
countPruneBySingleRelabelling++;
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
