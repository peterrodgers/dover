package uk.ac.kent.dover.fastGraph.graphSimilarity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import uk.ac.kent.dover.fastGraph.Debugger;
import uk.ac.kent.dover.fastGraph.ExactIsomorphism;
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

			long startTime;
			final int iterations = 100;
			final int startNodes = 100;
			final boolean directed = false;
			final boolean labelled = true;

			System.out.println("startNodes\t"+startNodes+"\tdirected\t"+directed+"\tlabelled\t"+labelled);

			for(int i = 1; i <= iterations; i++) {
//i = 3;
				int nodes = i*startNodes;
				int edges = nodes*2;
				for(int j = 1; j <= 1; j++) {
					long seed = System.currentTimeMillis();
//seed = 1515362131439L;
					Random r = new Random(seed);

					// bit of variance in number of items, between 0.8 and 1.2 of the number of intended nodes
					int g1Nodes = nodes+(int)(nodes*0.2)-(int)(r.nextInt(nodes)*0.4+1);
					if(g1Nodes < 1) {
						g1Nodes = 1;
					}
					int g2Nodes = nodes+(int)(nodes*0.2)-(int)(r.nextInt(nodes)*0.4+1);
					if(g2Nodes < 1) {
						g2Nodes = 1;
					}
					
					int g1Edges = edges+(int)(edges*0.2)-(int)(r.nextInt(edges)*0.4+1);
					if(g1Edges < 0) {
						g1Edges = 0;
					}
					int g2Edges = edges+(int)(edges*0.2)-(int)(r.nextInt(edges)*0.4+1);
					if(g2Edges < 0) {
						g2Edges = 0;
					}
					
					double ret;

					FastGraph g1,g2;
					HashMap<Integer,Double> editCosts;
					ApproximateGEDSimple geds;
					ApproximateGEDBipartite gedb;
					EditList el;
					
					editCosts = new HashMap<>();
					editCosts.put(EditOperation.DELETE_NODE,1.0);
					editCosts.put(EditOperation.ADD_NODE,2.0);
					editCosts.put(EditOperation.DELETE_EDGE,3.0);
					editCosts.put(EditOperation.ADD_EDGE,4.0);
					editCosts.put(EditOperation.RELABEL_NODE,5.0);
					
					g1 = FastGraph.randomGraphFactory(g1Nodes, g1Edges, seed*17, false, false);
					g2 = FastGraph.randomGraphFactory(g2Nodes, g2Edges, seed*13, false, false);
					
					el = new EditList();
					for(int c = 0; c < g1.getNumberOfNodes(); c++) {
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
						el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,c,color,-1,-1));
					}
					g1 = el.applyOperations(g1);

					el = new EditList();
					for(int c = 0; c < g2.getNumberOfNodes(); c++) {
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
						el.addOperation(new EditOperation(EditOperation.RELABEL_NODE,-1,c,color,-1,-1));
					}
					g2 = el.applyOperations(g2);

					
					startTime = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					ret = gedb.similarity(g1, g2);
					long GEDBtime = System.currentTimeMillis()-startTime;
					System.out.println("BIPARTITE VJ\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+GEDBtime/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());

					startTime = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					gedb.setUseHungarian(true);
					ret = gedb.similarity(g1, g2);
					System.out.println("BIPARTITE H\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-startTime)/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());

					startTime = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,GEDBtime,0,seed*11);
					ret = geds.similarity(g1, g2);
					System.out.println("SIMPLE same time as VJ\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-startTime)/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());

					startTime = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,0,g1.getNumberOfNodes()*2,seed*23);
					ret = geds.similarity(g1, g2);
					System.out.println("SIMPLE 2xNode swaps\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg1Edges\t"+g2Edges+"\tsimilarity time\t"+(System.currentTimeMillis()-startTime)/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
