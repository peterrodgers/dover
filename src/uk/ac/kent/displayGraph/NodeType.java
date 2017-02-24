package uk.ac.kent.displayGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;


/**
 * The type of a node in a graph. Utilises the tree structure of GraphType.
 * Allows matching to use a type heirarchy and associates various display
 * information with collections of Nodes.
 *
 * @see Node
 * @author Peter Rodgers
 */
public class NodeType extends ItemType {

/** Height of a node, top to bottom. */
	protected int height = 30;
/** Width of a node, leftmost to rightmost. */
	protected int width = 30;
	
/** If set the labels of this node are not shown */
	protected boolean hideLabels = false;
/** This can be Rectangle or Ellipse */
    protected String shapeString = new String("Ellipse");
	protected Color fillColor = Color.white;
	protected Color borderColor = Color.black;
	protected Color textColor = Color.black;
    protected BasicStroke stroke = new BasicStroke(2.0f);
	protected Color selectedFillColor = Color.black;
	protected Color selectedBorderColor = Color.black;
	protected Color selectedTextColor = Color.white;
    protected BasicStroke selectedStroke = new BasicStroke(2.0f);
/** A list of all the node types */
    protected static ArrayList<NodeType> existingTypes = new ArrayList<NodeType>();

/** Trivial constructor */
	public NodeType(String inLabel) {
		super(inLabel);
		existingTypes.add(this);
	}

	public NodeType(String inLabel, int width, int height, String shapeString, boolean hideLabels) {
		super(inLabel);
		this.width = width;
		this.height = height;
		this.shapeString = shapeString;
		this.hideLabels = hideLabels;
		existingTypes.add(this);
	}

	public NodeType(String inLabel, int width, int height, String shapeString, boolean hideLabels, Color borderColor, Color textColor) {
		super(inLabel);
		this.width = width;
		this.height = height;
		this.shapeString = shapeString;
		this.hideLabels = hideLabels;
		this.borderColor = borderColor;
		this.textColor = textColor;
		existingTypes.add(this);
	}


	/** trivial accessor */
	public int getHeight() {return height;}
	/** trivial accessor */
	public int getWidth() {return width;}
	/** trivial accessor */
	/** trivial accessor */
	public boolean getHideLabels() {return hideLabels;}
	public String getShapeString() {return shapeString;}
	/** trivial accessor */
	public Color getFillColor() {return fillColor;}
	/** trivial accessor */
	public Color getBorderColor() {return borderColor;}
	/** trivial accessor */
	public Color getTextColor() {return textColor;}
	/** trivial accessor */
	public BasicStroke getStroke() {return stroke;}
	/** trivial accessor */
	public Color getSelectedFillColor() {return selectedFillColor;}
	/** trivial accessor */
	public Color getSelectedBorderColor() {return selectedBorderColor;}
	/** trivial accessor */
	public Color getSelectedTextColor() {return selectedTextColor;}
	/** trivial accessor */
	public BasicStroke getSelectedStroke() {return selectedStroke;}
	/** trivial accessor */
	public static ArrayList<NodeType> getExistingTypes() {return existingTypes;}

	/** trivial mutator */
	public void setHeight(int h) {height = h;}
	/** trivial mutator */
	public void setWidth(int w) {width = w;}
	/** trivial mutator */
	public void setHideLabels(boolean b) {hideLabels = b;}
	/** trivial mutator */
	public void setShapeString(String s) {shapeString = s;}
	/** trivial mutator */
	public void setFillColor(Color c) {fillColor = c;}
	/** trivial mutator */
	public void setBorderColor(Color c) {borderColor = c;}
	/** trivial mutator */
	public void setTextColor(Color c) {textColor = c;}
	/** trivial mutator */
	public void setStroke(BasicStroke s) {stroke = s;}
	/** trivial mutator */
	public void setSelectedFillColor(Color c) {selectedFillColor = c;}
	/** trivial mutator */
	public void setSelectedBorderColor(Color c) {selectedBorderColor = c;}
	/** trivial mutator */
	public void setSelectedTextColor(Color c) {selectedTextColor = c;}
	/** trivial mutator */
	public void setSelectedStroke(BasicStroke s) {selectedStroke = s;}


	/** Returns the edge type with the given label or if there is no such type, returns null. */
	public static NodeType withLabel(String label) {
		NodeType nt = null;
		for(NodeType nt2 : existingTypes){
			if(nt2.getLabel().equals(label)) {
				nt = nt2;
			}
		}
		return nt;
	}

	/** Returns a list of all the roots in the type heirarchy removing duplicates */
	public static ArrayList<NodeType> allRoots() {

		ArrayList<NodeType> ret = new ArrayList<NodeType>();
		for(NodeType t : existingTypes){
			NodeType root = (NodeType)t.root();
			if(!ret.contains(root)) {
				ret.add(root);
			}
		}
		return ret;
	}

	/** 
	* Outputs the content of the node type for debugging purposes.
	*/
	public String toString() {
		return(getLabel());
	}

}



