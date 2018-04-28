package uk.ac.kent.dover.fastGraph.profiling;

import java.text.*;
import java.util.*;

import uk.ac.kent.dover.fastGraph.*;
import uk.ac.kent.dover.fastGraph.editOperation.*;
import uk.ac.kent.dover.fastGraph.graphSimilarity.*;

public class ProfileGED {

	public static void main(String [] args) {
		
		Debugger.enabled = false;
		
		profileRandomGraphsWithEdits();
		
	}
	
	

	protected static void profileRandomGraphsWithEdits() {
		try {
			
			Debugger.enabled = false;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			ArrayList<String> labels = new ArrayList<>();
			labels.add("blue");
			labels.add("black");
			labels.add("green");

			long startTime;
			Random r;
			final int iterations = 10;
			final int startNodes = 20;
			final int startEdges = startNodes*5;
			final int startEdits = 5;
			final int incrNodes = 20;
			final int incrEdges = incrNodes*5;
			final int incrEdits = 5;
			
			final boolean directed = false;
			final boolean labelled = true;
			final boolean simple = true;
			
			int nodes = startNodes;
			int edges = startEdges;
			int edits = startEdits;
			
			long startSeed = System.currentTimeMillis();
//			long startSeed = 55999;

			while(nodes < 10000000) {
				for(int i = 1; i <= iterations; i++) {
					
					long seed1 = startSeed*i*nodes;
					long seed2 = startSeed*i*nodes*1199;
					long seed3 = startSeed*i*nodes*3311;
					long seed4 = startSeed*i*nodes*7711;
					long seed5 = startSeed*i*nodes*1177;
					long seed6 = startSeed*i*nodes*4477;
					long seed7 = startSeed*i*nodes*3399;
					
					FastGraph g1 = FastGraph.randomGraphFactory(nodes, edges, seed1, simple, false);
					
					r = new Random(seed6);
					HashMap<Integer,Double> editCosts = new HashMap<>();
					editCosts.put(EditOperation.DELETE_NODE,(double)(r.nextInt(10)+1));
					editCosts.put(EditOperation.ADD_NODE,(double)(r.nextInt(10)+1));
					editCosts.put(EditOperation.DELETE_EDGE,(double)(r.nextInt(10)+1));
					editCosts.put(EditOperation.ADD_EDGE,(double)(r.nextInt(10)+1));
					editCosts.put(EditOperation.RELABEL_NODE,(double)(r.nextInt(10)+1));
				
					if(labelled) {
						r = new Random(seed3);
						for(int n = 0; n < g1.getNumberOfNodes(); n++) {
							String label = labels.get(r.nextInt(labels.size()));
							g1 = g1.generateGraphByRelabellingNode(n, label);
						}
					}
					
					EditList el = EditList.generateEditList(g1,edits,labels,editCosts, seed2);

					FastGraph g2 = el.applyOperations(g1);
					g2 = ExactIsomorphism.generateRandomIsomorphicGraph(g2, seed5, false);
					
					startTime = System.currentTimeMillis();
					ApproximateGEDSimple simpleGED = new ApproximateGEDSimple(directed, labelled, editCosts, 1000, 0, seed4);
					double simpleSimilarity = simpleGED.similarity(g1, g2);
					long simpleTime = System.currentTimeMillis()-startTime;

					startTime = System.currentTimeMillis();
					ApproximateGEDBipartite bipartiteGED = new ApproximateGEDBipartite(directed,labelled,editCosts);
					double bipartiteSimilarity = bipartiteGED.similarity(g1, g2);
					long bipartiteTime = System.currentTimeMillis()-startTime;
					
					double hausdorffSimilarity = -1;
					long hausdorffTime = -1;
					if(!directed) {
						startTime = System.currentTimeMillis();
						ApproximateGEDHausdorff hausdorffGED = new ApproximateGEDHausdorff(labelled,editCosts);
						hausdorffSimilarity = hausdorffGED.similarity(g1, g2);
						hausdorffTime = System.currentTimeMillis()-startTime;
					}

					double lowerSimilarity = -1;
					long lowerTime = -1;
					if(!directed) {
						startTime = System.currentTimeMillis();
						ApproximateGEDLowerBoundsSimple lowerGED = new ApproximateGEDLowerBoundsSimple(labelled,editCosts);
						lowerSimilarity = lowerGED.similarity(g1, g2);
						lowerTime = System.currentTimeMillis()-startTime;
					}

					double beliefSimpleSimilarity = -1;
					long beliefSimpleTime = -1;
					if(!directed && !labelled) {
						startTime = System.currentTimeMillis();
						BeliefPropagationSimple beliefSimple = new BeliefPropagationSimple();
						beliefSimpleSimilarity = beliefSimple.similarity(g1, g2);
						beliefSimpleTime = System.currentTimeMillis()-startTime;
					}

					double beliefCalculationSimilarity = -1;
					long beliefCalculationTime = -1;
/*					if(!directed && !labelled && simple) {
						startTime = System.currentTimeMillis();
						BeliefPropagationCalculation beliefSimple = new BeliefPropagationCalculation(g1, g2, mapping);
						beliefCalculationSimilarity = beliefCalculation.similarity(g1, g2);
						beliefCalculationTime = System.currentTimeMillis()-startTime;
					}
*/

					double neighbourhoodSimilarity = -1;
					long neighbourhoodTime = -1;
					if(!labelled) {
						startTime = System.currentTimeMillis();
						NeighbourhoodSimilarity neighbourhood = new NeighbourhoodSimilarity(directed);
//						neighbourhoodSimilarity = neighbourhood.similarity(g1, g2);
						neighbourhoodTime = System.currentTimeMillis()-startTime;
					}

					double degreeDifferenceSimilarity = -1;
					long degreeDifferenceTime = -1;
					if(!labelled) {
						startTime = System.currentTimeMillis();
						NodeDegreeDifference degreeDifference = new NodeDegreeDifference(directed);
						degreeDifferenceSimilarity = degreeDifference.similarity(g1, g2);
						degreeDifferenceTime = System.currentTimeMillis()-startTime;
					}

					double randomTrailSimilarity = -1;
					long randomTrailTime = -1;
					startTime = System.currentTimeMillis();
					RandomTrailSimilarity randomTrail = new RandomTrailSimilarity(directed,labelled,seed7);
					randomTrail.setTrailLength(3);
					randomTrail.setTrailsPerNode(1);
					randomTrailSimilarity = randomTrail.similarity(g1, g2);
					randomTrailTime = System.currentTimeMillis()-startTime;

					
					System.out.print(dateFormat.format(new Date())+"\t");
					System.out.print("nodes\t"+nodes+"\tedges\t"+edges+"\tedits\t"+edits+"\tdirected\t"+directed+"\tlabelled\t"+labelled+"\tsimple\t"+simple+"\t");
					System.out.print(i + "\toriginal cost:\t"+el.getCost()+"\tGED simple cost:\t"+simpleSimilarity+"\tGED bipartite cost:\t"+bipartiteSimilarity+"\t"+"\tGED hausdorff cost:\t"+hausdorffSimilarity+"\t"+"\tGED lower cost:\t"+lowerSimilarity+"\tbelief simple cost:\t"+beliefSimpleSimilarity+"\tbelief calculation cost:\t"+beliefCalculationSimilarity+"\tneighbourhood cost:\t"+neighbourhoodSimilarity+"\tdegree difference cost:\t"+degreeDifferenceSimilarity+"\trandom trail cost:\t"+randomTrailSimilarity+"\t");
					System.out.println("original time:\t-1.0"+"\tGED simple time:\t"+(simpleTime/1000.0)+"\tGED bipartite time:\t"+(bipartiteTime/1000.0)+"\tGED hausdorff time:\t"+(hausdorffTime/1000.0)+"\tGED lower time:\t"+(lowerTime/1000.0)+"\tbelief simple time:\t"+(beliefSimpleTime/1000.0)+"\tbelief calculation time:\t"+(beliefCalculationTime/1000.0)+"\tneighbourhood time:\t"+(neighbourhoodTime/1000.0)+"\tdegree difference time:\t"+(degreeDifferenceTime/1000.0)+"\trandom trail time:\t"+(randomTrailTime/1000.0));
					
				}
				
				nodes += incrNodes;
				edges += incrEdges;
				edits += incrEdits;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected static void profileSimpleBipartite() {
		try {

			long startTime,GEDTime;
			final int iterations = 100;
			final int startNodes = 5;
			final boolean directed = false;
			final boolean labelled = true;
			final boolean simple = true;

			System.out.println("startNodes\t"+startNodes+"\tdirected\t"+directed+"\tlabelled\t"+labelled+"\tsimple\t"+simple);

			for(int i = 1; i <= iterations; i++) {
//i = 3;
				int nodes = i*startNodes;
				int edges = nodes*nodes/4;
				if(edges > nodes*4) {
					edges = nodes*4;
				}
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
					ApproximateGEDHausdorff gedh;
					ApproximateGEDLowerBoundsSimple gedlbs;
					EditList el;
					
					editCosts = new HashMap<>();
					editCosts.put(EditOperation.DELETE_NODE,1.0);
					editCosts.put(EditOperation.ADD_NODE,2.0);
					editCosts.put(EditOperation.DELETE_EDGE,3.0);
					editCosts.put(EditOperation.ADD_EDGE,4.0);
					editCosts.put(EditOperation.RELABEL_NODE,5.0);
					
					g1 = FastGraph.randomGraphFactory(g1Nodes, g1Edges, seed*17, simple, false);
					g2 = FastGraph.randomGraphFactory(g2Nodes, g2Edges, seed*13, simple, false);
					
					if(labelled) {
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
					}

					
					System.out.print("i\t"+i+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg2Edges\t"+g2Edges);
					
					startTime = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					ret = gedb.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
//					System.out.println("BIPARTITE VJ\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg2Edges\t"+g2Edges+"\tsimilarity time\t"+GEDTime/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());
					System.out.print("\tBIPARTITE similarity time\t"+GEDTime/1000.0+"\tcost\t"+ret);
/*
					startTime = System.currentTimeMillis();
					gedb = new ApproximateGEDBipartite(directed,labelled,editCosts);
					gedb.setUseHungarian(true);
					ret = gedb.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
					System.out.println("BIPARTITE H\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg2Edges\t"+g2Edges+"\tsimilarity time\t"+GEDTime/1000.0+"\tcost\t"+ret+"\tlength\t"+gedb.getEditList().getEditList().size());

					startTime = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,GEDBtime,0,seed*11);
					ret = geds.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
					System.out.println("SIMPLE same time as VJ\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg2Edges\t"+g2Edges+"\tsimilarity time\t"+GEDtime/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());
*/
					startTime = System.currentTimeMillis();
					geds = new ApproximateGEDSimple(directed,labelled,editCosts,0,g1.getNumberOfNodes()*10,seed*23);
					ret = geds.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
//					System.out.println("SIMPLE 10xNode swaps\t"+i+"\tseed\t"+seed+"\tg1Nodes\t"+g1Nodes+"\tg1Edges\t"+g1Edges+"\tg2Nodes\t"+g2Nodes+"\tg2Edges\t"+g2Edges+"\tsimilarity time\t"+GEDTime/1000.0+"\tcost\t"+ret+"\tlength\t"+geds.getEditList().getEditList().size()+"\tswaps\t"+geds.getNodeSwaps());
					System.out.print("\tSIMPLE 10xNode swaps similarity time\t"+GEDTime/1000.0+"\tcost\t"+ret);

					startTime = System.currentTimeMillis();
					gedh = new ApproximateGEDHausdorff(labelled,editCosts);
					ret = gedh.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
					System.out.print("\tHAUSDORFF similarity time\t"+GEDTime/1000.0+"\tcost\t"+ret);

					startTime = System.currentTimeMillis();
					gedlbs = new ApproximateGEDLowerBoundsSimple(labelled,editCosts);
					ret = gedlbs.similarity(g1, g2);
					GEDTime = System.currentTimeMillis()-startTime;
					System.out.print("\tLB SIMPLE similarity time\t"+GEDTime/1000.0+"\tcost\t"+ret);
					
					System.out.println();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}



}
