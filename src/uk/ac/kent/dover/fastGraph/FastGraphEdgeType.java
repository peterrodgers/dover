package uk.ac.kent.dover.fastGraph;

/**
 * Enum to hold the various edge types in the data. <br>
 * Each has a byte value which is stoed in the edge type area in the edge buffer.
 * 
 * @author Rob Baker
 *
 */
public enum FastGraphEdgeType {

	UNKNOWN(0),
	SIBLING(1),
	MARRIED(2),
	PARENT(3),
	RELATIONSHIP(4),
	FRIEND(5),
	ACQUAINTANCE(6),
	COLLEAGUE(7),
	MANAGER(7),
	CUSTOMER(9),
	TEAMMATE(10),
	NEIGHBOUR(11),
	BUSINESS_PARTNER(12),
	DOCTOR(13);
	
	private byte value; //the value of this type
	
	/**
	 * Constructor that assigns (and converts) the value given in the Enum to a byte for the buffers
	 * 
	 * @param value The value of this type
	 */
	private FastGraphEdgeType(int value) {
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
