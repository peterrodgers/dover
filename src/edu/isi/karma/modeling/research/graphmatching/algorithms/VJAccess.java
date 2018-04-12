package edu.isi.karma.modeling.research.graphmatching.algorithms;

/**
 * This is present due to the GPLv3 nature of edu.isi.karma.modeling.research.graphmatching.algorithms.VolgenantJonker which
 * is not a public class. By 
 * making no changes to that code, we avoid licencing issues.
 * 
 * @author Peter Rodgers
 *
 */
public class VJAccess {

	VolgenantJonker vj;
	
	public VJAccess() {
		vj = new VolgenantJonker();
	}


	public double computeAssignment(double[][] costMatrix) {
		return vj.computeAssignment(costMatrix);
	}

	
	public  int[] getAssignment() {
		return vj.getAssignment();
	}


}
