package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.HashMap;
import java.util.List;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;
import uk.ac.kent.dover.fastGraph.editOperation.EditOperation;

public class ProfileGED {

	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		try {
			
			int startNodes = 100;
			int nodes = startNodes;
			while(true) {
				double ret;
				List<EditOperation> retList;
				FastGraph g1,g2;
				HashMap<Integer,Double> editCosts;
				EditList retEditList1, retEditList2;
				ApproximateGEDSimple geds;
				ApproximateGEDBipartite gedb;
				
				editCosts = new HashMap<>();
				editCosts.put(EditOperation.DELETE_NODE,1.0);
				editCosts.put(EditOperation.ADD_NODE,2.0);
				editCosts.put(EditOperation.DELETE_EDGE,3.0);
				editCosts.put(EditOperation.ADD_EDGE,4.0);
				editCosts.put(EditOperation.RELABEL_NODE,5.0);
				
				int edges = nodes*10;
				
				long seed1 = System.currentTimeMillis();
				long seed2 = System.currentTimeMillis()*2;
				long seed3 = System.currentTimeMillis()*3;
				
				System.out.println("seed1 "+seed1);
				
				g1 = FastGraph.randomGraphFactory(nodes, edges, seed1, false);
				
				g2 = FastGraph.randomGraphFactory(nodes, edges, seed2, false);
				
				long start1 = System.currentTimeMillis();
				gedb = new ApproximateGEDBipartite(false,true,editCosts);
				ret = gedb.similarity(g1, g2);
				long GEDBtime = System.currentTimeMillis()-start1;
				System.out.println("       BIPARTITE nodes "+nodes+" edges "+edges+" similarity time "+GEDBtime/1000.0+" cost "+ret+" length "+gedb.getEditList().getEditList().size());
				
				long start2 = System.currentTimeMillis();
//				geds = new ApproximateGEDSimple(false,true,editCosts,GEDBtime,0,seed3);
				geds = new ApproximateGEDSimple(false,true,editCosts,0,g1.getNumberOfNodes()*2,seed3);
//				geds = new ApproximateGEDSimple(false,true,editCosts,0,0,seed3);
				ret = geds.similarity(g1, g2);
				System.out.println("SIMPLE nodes "+nodes+" edges "+edges+" swaps "+geds.getNodeSwaps()+" similarity time "+(System.currentTimeMillis()-start2)/1000.0+" cost "+ret+" length "+geds.getEditList().getEditList().size());
								
				nodes = nodes + startNodes;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
