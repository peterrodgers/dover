package uk.ac.kent.dover.fastGraph;

/**
 * Enum to hold the various node types in the data. <br>
 * Each has a byte value which is stored in the edge type area in the node buffer.
 * 
 * Also stored in /techreport/Node and Edge Types.txt
 * 
 * @author Rob Baker
 *
 */
public enum FastGraphNodeType {

	UNKNOWN(0),
	CHILD(1),
	PARENT(2);
	
	private byte value; //the value of this type
	
	/**
	 * Constructor that assigns (and converts) the value given in the Enum to a byte for the buffers
	 * 
	 * @param value The value of this type
	 */
	private FastGraphNodeType(int value) {
		this.value = (byte) value;
	}
	
	/**
	 * Gets the value of this type
	 * @return The value of this type
	 */
	public byte getValue() {
		return value;
	}
}
