package uk.ac.kent.displayGraph;

import java.util.*;
import java.awt.*;



/**
 * The type of an edge in a graph. Utilises the tree structure of ItemType.
 * Allows matching to use a type heirarchy and associates various display
 * information with collections of Edges.
 *
 * @see Edge
 * @author Peter Rodgers
 */
public class EdgeType extends ItemType {

/** Indicates if the edge is directed. */
	protected boolean directed = false;
	protected Color lineColor = Color.black;
	protected Color selectedLineColor = Color.blue;
    protected BasicStroke stroke = new BasicStroke(2.0f);
    protected BasicStroke selectedStroke = new BasicStroke(2.0f);
	public static double arrowAngle = 40;
	public static int arrowLength = 20;
	protected Color textColor = Color.black;
	protected Color selectedTextColor = Color.blue;
	protected int priority = -1;
/** A list of all the edge types */
    protected static ArrayList<EdgeType> existingTypes = new ArrayList<EdgeType>();
/** Holds the current least priority */
    protected static int lowestPriority = Integer.MAX_VALUE;

/** Trivial constructor. */
	public EdgeType(String inLabel) {
		super(inLabel);
		existingTypes.add(this);
	}

	/** Constructor also setting priority. */
	public EdgeType(String inLabel, int priority) {
		super(inLabel);
		existingTypes.add(this);
		setPriority(priority);
	}

	/** Constructor also setting directed. */
	public EdgeType(String inLabel, boolean directed) {
		super(inLabel);
		existingTypes.add(this);
		setDirected(directed);
	}

	/** Constructor also setting directed. */
	public EdgeType(String inLabel, boolean directed, Color lineColor) {
		super(inLabel);
		existingTypes.add(this);
		setDirected(directed);
		setLineColor(lineColor);
	}

	public boolean getDirected() {return directed;}
	public Color getLineColor() {return lineColor;}
	public Color getSelectedLineColor() {return selectedLineColor;}
	public BasicStroke getStroke() {return stroke;}
	public BasicStroke getSelectedStroke() {return selectedStroke;}
	public double getArrowAngle() {return arrowAngle;}
	public int getArrowLength() {return arrowLength;}
	public Color getTextColor() {return textColor;}
	public Color getSelectedTextColor() {return selectedTextColor;}
	public static ArrayList<EdgeType> getExistingTypes() {return existingTypes;}
	public int getPriority() {return priority;}

	public void setDirected(boolean d) {directed = d;}
	public void setLineColor(Color c) {lineColor = c;}
	public void setSelectedLineColor(Color c) {selectedLineColor = c;}
	public void setStroke(BasicStroke s) {stroke = s;}
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}
	public void setArrowAngle(double angle) {arrowAngle = angle;}
	public void setArrowLength(int length) {arrowLength = length;}
	public void setTextColor(Color c) {textColor = c;}
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}
/** This mutator may alter the current lowestPriority */
	public void setPriority(int p) {
		priority = p;
		if (p<lowestPriority) {
			lowestPriority = p-1;
		}
	}

/** Set the priority of this edge type to be the least */
	public void setPriorityToLowest() {
		priority = lowestPriority;
		lowestPriority--;
	}

/** Returns the edge type with the given label or if there is no such type, returns null. */
	public static EdgeType withLabel(String label) {
		EdgeType et = null;
		for(EdgeType et2 : EdgeType.getExistingTypes()) {
			if(et2.getLabel().equals(label)) {
				et = et2;
			}
		}
		return et;
	}


/** Returns a list of all the roots in the type heirarchy removing duplicates */
	public static ArrayList<EdgeType> allRoots() {

		ArrayList<EdgeType> ret = new ArrayList<EdgeType>();
		for(EdgeType t : EdgeType.getExistingTypes()) {
			EdgeType root = (EdgeType)t.root();
			if(!ret.contains(root)) {
				ret.add(root);
			}
		}
		return ret;
	}

/** 
* Outputs the content of the type.
*/
	public String toString() {
		return(getLabel());
	}

}



