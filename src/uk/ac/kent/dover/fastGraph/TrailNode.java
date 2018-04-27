package uk.ac.kent.dover.fastGraph;

/**
 * 
 * An object with useful information, @see uk.ac.kent.dover.fastGraph.RandomTrail.
 * 
 * @author pjr
 *
 */
public class TrailNode {
	
	int position; // the nodes position in the trail
	int node; // the node in the trail
	int duplicatePosition; // if the node was used previously, the first position it occurs
	int edge; // the edge used to get to this node, -1 if the first node


	/**
	 * 
	 * @param position the list position
	 * @param node the node id
	 * @param duplicatePosition the first position of a node if already visited, otherwise -1;
	 * @param outwards true if the edge used points at this node, false otherwise;
	 */
	public TrailNode(int position, int node, int duplicatePosition, int edge) {
		this.position = position;
		this.node = node;
		this.duplicatePosition = duplicatePosition;
		this.edge = edge;
	}
	
	
	/**
	 *gets the node's position in the list.
	 * @return the position
	 */
	public int getPosition() {return position;}
	
	/**
	 * gets the node id.
	 * @return the node id
	 */
	public int getNode() {return node;}
	
	/**
	 * gets the first instance of the node in the list, -1 if not duplicated.
	 * @return the first instance of the node in the list
	 */
	public int getDuplicatePosition() {return duplicatePosition;}

	/**
	 * gets the edge id.
	 * @return the edge id
	 */
	public int getEdge() {return edge;}
	
	/**
	 * sets the node's position in the list.
	 * @param position the position
	 */
	public void setPosition(int position) {this.position = position;}
	
	/**
	 * sets the node id.
	 * @param node the node
	 */
	public void setNode(int node) {this.node = node;}
	
	/**
	 * sets the first instance of the node in the list, use -1 if not duplicated
	 * @param duplicatePosition the first instance of this node in the list
	 */
	public void setDuplicatePositon(int duplicatePosition) {this.duplicatePosition = duplicatePosition;}

	/**
	 * sets the edge id.
	 * @param edge the edge pointing to the node
	 */
	public void setEdge(int edge) {this.edge = edge;}
	
	
	/**
	 * @return a String version of the Trail node for debugging.
	 */
	public String toString () {
		String ret = "node:"+node+"|"+"position:"+position+"|"+"duplicatePosition:"+duplicatePosition+"|edge: "+edge;
		return ret;
	}
	

}
