package uk.ac.kent.dover.fastGraph;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Enum to hold the various edge types in the data. <br>
 * Each has a byte value which is stored in the edge type area in the edge buffer.
 * 
 * Also stored in /techreport/Node and Edge Types.txt
 * 
 * @author Rob Baker
 *
 */
public enum FastGraphEdgeType {

	TIME(127),
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
	
	/**
	 * Picks a random edge type, that isn't a family or time relation
	 * @param r A random number generator
	 * @return The random type
	 */
	public static FastGraphEdgeType pickRandomExceptFamilyAndTime(Random r) {
		List<FastGraphEdgeType> notAllowed = Arrays.asList(TIME, UNKNOWN, SIBLING, MARRIED, PARENT);
		
		FastGraphEdgeType[] values = FastGraphEdgeType.values();
		while(true) {
			FastGraphEdgeType pick = values[r.nextInt(values.length)]; //the choice of type
			if(!notAllowed.contains(pick)) { //if this isn't in the not allowed list
				return pick; 
			}
		}	
	}
	
}
