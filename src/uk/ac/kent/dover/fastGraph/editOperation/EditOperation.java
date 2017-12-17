package uk.ac.kent.dover.fastGraph.editOperation;

import java.util.Arrays;

import uk.ac.kent.dover.fastGraph.*;

/**
 * An edit operation on a graph. For example node addition, deletion. The intention
 * is that where possible, a single instance of subclasses is instantiated, and that all edit
 * operations of that kind are made with the instance.
 * 
 * @author Peter Rodgers
 *
 */
public class EditOperation {
	
	public final static int ADD_NODE = 0;
	public final static int DELETE_NODE = 1;
	public final static int ADD_EDGE = 2;
	public final static int DELETE_EDGE = 3;
	public final static int RELABEL_NODE = 4;

	private int operationCode;
	private double cost;
	private int id;
	private String label;
	private int n1;
	private int n2;
	
	/**
	 * Create one of these for each edit operation. Only some of the parameters
	 * are used for each type of edit operation. 
	 * 
	 * @param operationCode the code of the operation, required, null otherwise
	 * @param cost the cost of the operation, required
	 * @param id the id of the node or edge for delete operations, null otherwise
	 * @param label the label, for relabelling and create operations, null otherwise
	 * @param n1 the source node, for edge add operations, null otherwise
	 * @param n2 the target node, for edge add operations, null otherwise
	 */
	public EditOperation(int operationCode, double cost, int id, String label, int n1, int n2) {
		this.operationCode = operationCode;
		this.cost = cost;
		this.id = id;
		this.label = label;
		this.n1 = n1;
		this.n2 = n2;
	}

	/**
	 * Create an EditOperation just like the parameter.
	 * 
	 * @param eo the edit operation to copy.
	 */
	public EditOperation(EditOperation eo) {
		this.operationCode = eo.operationCode;
		this.cost = eo.cost;
		this.id = eo.id;
		this.label = eo.label;
		this.n1 = eo.n1;
		this.n2 = eo.n2;
	}



	/**
	 * @return the cost of the operation
	 */
	public double getCost() { return cost;}

	/**
	 * @return the code of the operation
	 */
	public int getOperationCode() {return operationCode;}

	/**
	 * @return the id of the item to be changed or removed
	 */
	public int getId() {return id;}

	/**
	 * @return the new label
	 */
	public String getLabel() {return label;}

	/**
	 * @return the source node of the edge to be added
	 */
	public int getN1() {return n1;}

	/**
	 * @return the target node of the edge to be added
	 */
	public int getN2() {return n2;}

	/**
	 * 
	 * @param operationCode the new operation code
	 */
	public void setOperationCode(int operationCode) {this.operationCode = operationCode;}

	/**
	 * 
	 * @param cost the new cost
	 */
	public void setCost(double cost) {this.cost = cost;}
	/**
	 * 
	 * @param id the new id
	 */
	public void setId(int id) {this.id = id;}
	
	/**
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {this.label = label;}
	
	/**
	 * 
	 * @param n1 the new n1
	 */
	public void setN1(int n1) {this.n1 = n1;}
	
	/**
	 * 
	 * @param n2 the new n2
	 */
	public void setN2(int n2) {this.n2 = n2;}


	/**
	 * Call this to apply the edit to a graph.
	 * 
	 * @param g graph to edit
	 * @return new graph with edit applied, null if edit fails
	 */
	public FastGraph edit(FastGraph g) {
		
		if(operationCode == DELETE_NODE) {
			FastGraph ret = g.generateGraphByDeletingSingletonNode(id);
			return ret;
		}
		
		if(operationCode == ADD_NODE) {
			NodeStructure ns = new NodeStructure(-1, label, 0, (byte)0, (byte)0);
			FastGraph ret = g.generateGraphByAddingNode(ns);
			return ret;
		}
		
		if(operationCode == DELETE_EDGE) {
			FastGraph ret = g.generateGraphByDeletingEdge(id);
			return ret;
		}
		
		if(operationCode == ADD_EDGE) {
			EdgeStructure es = new EdgeStructure(-1, label, 0, (byte)0, (byte)0, n1, n2);
			try {
				FastGraph ret = g.generateGraphByAddingEdge(es);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(operationCode == RELABEL_NODE) {
			try {
				FastGraph ret = g.generateGraphByRelabellingNode(id, label);
				return ret;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/**
	 * @return a String output for debugging
	 */
	public String toString() {
		String ret = new String();
		if(operationCode == DELETE_NODE) {
			ret += "DELETE_NODE";
		}
		if(operationCode == ADD_NODE) {
			ret += "ADD_NODE";
		}
		if(operationCode == DELETE_EDGE) {
			ret += "DELETE_EDGE";
		}
		if(operationCode == ADD_EDGE) {
			ret += "ADD_EDGE";
		}
		if(operationCode == RELABEL_NODE) {
			ret += "RELABEL_NODE";
		}
		
		ret += ", cost: "+cost;
		ret += ", id: "+id;
		ret += ", label: "+label;
		ret += ", n1: "+n1;
		ret += ", n2: "+n2;
		
		
		return ret;
	}
	



}
