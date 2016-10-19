package uk.ac.kent.dover.fastGraph;

/**
 * Trivial class to hold a name and Cumulative Probability pair<br>
 * 
 * @author Rob Baker
 *
 */
public class Name {
	
	private String name; //Person's name
	private double prob; //Probability of that person being chosen
	
	/**
	 * Simple Constructor
	 * @param name Person's Name
	 * @param prob Probability of that person being chosen
	 */
	public Name(String name, double prob) {
		this.name = name;
		this.prob = prob;
	}	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the probability of this name
	 */
	public double getProb() {
		return prob;
	}
	
	/**
	 * Overwrites the toString() for this class
	 * 
	 * @returns String representation of a name
	 */
	public String toString() {
		return name + "(" + prob + ")";
	}

}
