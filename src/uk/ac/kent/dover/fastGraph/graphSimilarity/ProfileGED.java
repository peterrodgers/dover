package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.editOperation.EditList;
import uk.ac.kent.dover.fastGraph.editOperation.EditOperation;

public class ProfileGED {

	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		profileSimpleBipartite();
		
	}
	
	protected static void profileSimpleBipartite() {
		try {

			long start;
			int startNodes = 300;
			boolean directed = false;
			boolean labelled = true;
			for(int nodes = startNodes; nodes< startNodes*30; nodes+=startNodes) {
				int edges = nodes*10;
				for(int j = 0; j < 1; j++) {
					long seed = System.currentTimeMillis();
					
					Random r = new Random(seed);

					// bit of variance in number of items, between 0.8 and 1.2 of the number of intended nodes
					int g1Nodes = nodes+(int)(nodes*0.2)-(int)(r.nextInt(nodes)*0.4+1);
					int g2Nodes = nodes+(int)(nodes*0.2)-(int)(r.nextInt(nodes)*0.4+1);
					
					int g1Edges = edges+(int)(edges*0.2)-(int)(r.nextInt(edges)*0.4+1);
					int g2Edges = edges+(int)(edges*0.2)-(int)(r.nextInt(edges)*0.4+1);
					
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
					
					g1 = FastGraph.randomGraphFactory(g1Nodes, g1Edges, seed*17, false);
					g2 = FastGraph.randomGraphFactory(g2Nodes, g2Edges, seed*13, false);
					
					start = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					ret = gedb.similarity(g1, g2);
					long GEDBtime = System.currentTimeMillis()-start;
					System.out.println("BIPARTITE VJ, seed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+GEDBtime/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());
					
					start = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					gedb.setUseHungarian(true);
					ret = gedb.similarity(g1, g2);
					System.out.println("BIPARTITE H, seed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-start)/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());
					
					start = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,GEDBtime,0,seed*11);
					ret = geds.similarity(g1, g2);
					System.out.println("SIMPLE same time as VJ, seed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-start)/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());

					start = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,0,g1.getNumberOfNodes()*2,seed*23);
					ret = geds.similarity(g1, g2);
					System.out.println("SIMPLE 2xNode swaps, seed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-start)/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());
								
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
