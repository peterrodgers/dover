package uk.ac.kent.displayGraph;

import java.util.*;



/** 
 * Shared stuff for NodeType and EdgeType. Forms a double linked tree.
 *
 * @author Peter Rodgers
 */


public abstract class ItemType {


/** unique, non empty label, set at object creation and unmodifiable */
	protected String label = "";
/** This indicates the parent, null indicates a root. */
	protected ItemType parent = null;
/** children - redundant data, mirroring the parent field. */
	protected HashSet<ItemType> children = new HashSet<ItemType>();


/** Minimal constructor. */
	public ItemType() {}
/** Trivial constructor. */
	public ItemType(String inLabel) {label = inLabel;}


/** trivial accessor */
	public String getLabel() {return label;}
/** trivial accessor */
	public ItemType getParent() {return parent;}
/** trivial accessor */
	public HashSet<ItemType> getChildren() {return children;}


/** trival mutator. Should only be used by a parent mutator method */
	protected boolean addChild(ItemType t) {return(children.add(t));}
/** trival mutator. Should only be used by a parent mutator method */
	protected boolean removeChild(ItemType t) {return(children.remove(t));}

/**
 * This changes the parent field and maintains the children field of the
 * parents involved.
 * It removes the current object from the children field of the previous
 * parent and adds it to the children field of the new parent.
 *
 * @return true if operation successful, false if the operation would create
 * a loop due to existing connections or self reference
 */
	public boolean setParent(ItemType t) {

		if (t != null) {
			if (t.ancestor(this)) {
				return false;
			}
			if (t == this) {
				return false;
			}
		}

		if (parent != null) {
			parent.removeChild(this);
		} 
		if(t != null) {
			t.addChild(this);
		}
		parent = t;
		return true;
	}

/**
 * This sets the parent field to be null and maintains the children field of the
 * current parent. It removes the current object from the children field
 * of the parent object
 */
	public void removeParent() {
		if (parent != null) {
			parent.removeChild(this);
			parent = null;
		}
	}

/** The root node of the tree. A root is defined by having a null parent. */
	public ItemType root() {
		ItemType current = this;
		while (current.getParent() != null) {
			current = current.getParent();
		}
		return(current);
	}

/**
 * Discovers if the argument is above the current item in the heirarchy.
 *
 * @return true if the argument is above this item in the tree,
 * false otherwise, including the case where they are the same item.
 */
	public boolean ancestor(ItemType t) {
		if(t == null) {
			return false;
		}
		ItemType current = this;
		while ((current.getParent() != null) && (current.getParent() != t)) {
			current = current.getParent();
		}
		if (current.getParent() == null) {
			return false;
		} else {
			return true;
		}
	}

/**
 * Check type tree consistency of the current type to ensure internal data
 * structures are correct. That is, check the redundant children and parent
 * data matches below and above this node. Its not perfect, as we start
 * at a particular node and test the nodes upwards, if 
 * This method is a useful test to ensure that internal methods that change
 * the type tree leave a tree.
 */
	public boolean consistent() {

// check the path to the root

		ArrayList<ItemType> tested = new ArrayList<ItemType>();
		ItemType rootCurrent = this;
		while (rootCurrent.getParent() != null && !tested.contains(rootCurrent)) {
			if(rootCurrent.itemConsistent()) {
				return(false);
			}
			tested.contains(rootCurrent);
			rootCurrent = rootCurrent.getParent();
		}

		if(tested.contains(rootCurrent)) {
			return(false);
		}

//TBD loop checking needs veryfying, loop checking on way down needs adding.

		ItemType root = root();
		if(!root.itemConsistent()) {
			return(false);
		}

// check all the items down from the root in the tree
		ArrayList<ItemType> below = new ArrayList<ItemType>();
		below.addAll(root.getChildren());

		while(!below.isEmpty()) {

			ItemType current = (ItemType)below.remove(0);
			if(!current.itemConsistent()) {
				return(false);
			}

			below.addAll(current.getChildren());
		}
		return(true);
	}


/**
 * Check that the children of this node have an appropriate parent attribute
 * and that the parent contains this in its children.
 * Used by consistent
 */
	private boolean itemConsistent() {

		for(ItemType c : children) {

			if (c.parent != this) {
				return(false);
			}
		}
		if(parent != null && !parent.getChildren().contains(this)) {
			return(false);
		}
		return(true);
	}


/** 
 * Outputs the content of the entire type tree that the type is in
 * for debugging purposes.
 */
	public void outTree() {
		ArrayList<String> tree = outTreeStart("  ");
		for(String s : tree){
			System.out.println(s);
		}
	}

/**
 * Starts the recursive method to output the tree. The indent parameter is the
 * String that will placed in front of each level of the tree. The output
 * is indented for levels, but the order of output of children is undefined.
 */
	public ArrayList<String> outTreeStart(String indent) {
		ArrayList<String> treeList = outTreeRec(root(),"", indent);
		return(treeList);
	}


/** Recursive method to output the tree. Used by {@link #outTreeStart} */
	private ArrayList<String> outTreeRec(ItemType item, String currentIndent, String indent) {

		ArrayList<String> ret = new ArrayList<String>();
		ret.add(new String(currentIndent+item));

		for(ItemType c : item.getChildren()) {
			ret.addAll(outTreeRec(c,currentIndent+indent,indent));
		}
		return ret;
	}


/** Just the type label */
	public String toString() {
		return(label);
	}
}



