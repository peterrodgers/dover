package uk.ac.kent.displayGraph.experiments;

import java.awt.*;
import java.awt.event.*;

import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.drawers.DrawCoordCollection;
import uk.ac.kent.displayGraph.drawers.GraphDrawerEdgeLength;
import uk.ac.kent.displayGraph.utilities.GraphUtilityDistanceStats;

/**
 *
 * @author Peter Rodgers
 */

public class GraphExperimentMetricValues extends GraphExperiment {

/** The strength of a spring */
	protected double startK = 0.0005;
/** The strength of the repulsive force */
	protected double startR = 15.0;
/** The amount of movement on each iteration */
	protected double startF = 1.0;
/** The number of iterations in the experiment */
	protected int iterations = 1000;
/** The number of experiments */
	protected int experiments = 80;
/** change on each iteration */
	protected double percentAlteration = 0.02;
/** change on each iteration */
	protected boolean cool = true;

/** Trivial constructor. */
	public GraphExperimentMetricValues() {
		super(KeyEvent.VK_N,"Metric Experiment",KeyEvent.VK_N);
	}

/** Trivial constructor. */
	public GraphExperimentMetricValues(int key, String s, int accelerator) {
		super(key,s,accelerator);
	}


/** Starts the experiment. */
	public void experiment() {

		GraphPanel gp = getGraphPanel();
		Graph g = getGraph();

		GraphUtilityDistanceStats stats = new GraphUtilityDistanceStats();
		stats.setGraphPanel(gp);

		GraphDrawerEdgeLength gdel = new GraphDrawerEdgeLength(KeyEvent.VK_S,"Spring Embedder - edge length version",KeyEvent.VK_S,false,true);
		gp.addGraphDrawer(gdel);

		gdel.setScaleAttractive(true);
		gdel.setAnimateFlag(false);
		gdel.setTimerFlag(false);
		gdel.setDialogFlag(false);

		gdel.setK(startK);
		gdel.setR(startR);
		gdel.setIterations(iterations);

		double oldScore = stats.runMeasures(g,false);

		double coolingAmount = percentAlteration/experiments;
		if(cool) {
			percentAlteration = percentAlteration + coolingAmount;
		}

		int rotate = 0;

		for(int i = 1; i <= experiments; i++) {

			if(cool) {
				percentAlteration = percentAlteration - coolingAmount;
			}
			getGraphPanel().update(getGraphPanel().getGraphics());
			DrawCoordCollection oldNodes = new DrawCoordCollection(g.getNodes());
			
			if (rotate == 0) {
				double oldK = gdel.getK();
				double newK = gdel.getK()*(1+percentAlteration);
				gdel.setK(newK);

				g.randomizeNodePoints(new Point(50,50),400,400);
				gdel.drawGraph();
				double newScore = stats.runMeasures(g,false);

				if (newScore < oldScore) {
System.out.println("BETTER experiment iteration: "+i+" new score "+newScore+" old score "+oldScore+" INCREASE k: "+gdel.getK()+" r: "+gdel.getR());
					oldScore = newScore;
					gp.update(gp.getGraphics());
				} else {
					oldNodes.switchOldCentresToNode();
					gdel.setK(oldK);
				}

				rotate++;
				continue;
			}

			if (rotate == 1) {
				double oldK = gdel.getK();
				double newK = gdel.getK()*(1-percentAlteration);
				gdel.setK(newK);

				g.randomizeNodePoints(new Point(50,50),400,400);
				gdel.drawGraph();
				double newScore = stats.runMeasures(g,false);

				if (newScore < oldScore) {
System.out.println("BETTER experiment iteration: "+i+" new score "+newScore+" old score "+oldScore+" DECREASE k: "+gdel.getK()+" r: "+gdel.getR());
					oldScore = newScore;
					gp.update(gp.getGraphics());
				} else {
					oldNodes.switchOldCentresToNode();
					gdel.setK(oldK);
				}

				rotate++;
				continue;
			}

			if (rotate == 2) {
				double oldR = gdel.getR();
				double newR = gdel.getR()*(1+percentAlteration);
				gdel.setR(newR);

				g.randomizeNodePoints(new Point(50,50),400,400);
				gdel.drawGraph();
				double newScore = stats.runMeasures(g,false);

				if (newScore < oldScore) {
System.out.println("BETTER experiment iteration: "+i+" new score "+newScore+" old score "+oldScore+" k: "+gdel.getK()+" INCREASE r: "+gdel.getR());
					oldScore = newScore;
					gp.update(gp.getGraphics());
				} else {
					oldNodes.switchOldCentresToNode();
					gdel.setR(oldR);
				}

				rotate++;
				continue;
			}

			if (rotate == 3) {
				double oldR = gdel.getR();
				double newR = gdel.getR()*(1-percentAlteration);
				gdel.setR(newR);

				g.randomizeNodePoints(new Point(50,50),400,400);
				gdel.drawGraph();
				double newScore = stats.runMeasures(g,false);

				if (newScore < oldScore) {
System.out.println("BETTER experiment iteration: "+i+" new score "+newScore+" old score "+oldScore+" k: "+gdel.getK()+" DECREASE r: "+gdel.getR());
					oldScore = newScore;
					gp.update(gp.getGraphics());
				} else {
					oldNodes.switchOldCentresToNode();
					gdel.setR(oldR);
				}
				rotate = 0;
			}

		}

	}

}
