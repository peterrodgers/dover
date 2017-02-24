package uk.ac.kent.dover.fastGraph;

/**
 * Exception thrown and caught when an error within Fast Graph occurs that can't be described by the other 
 * exception types, but needs to be reported.
 * 
 * @author Rob Baker
 *
 */
@SuppressWarnings("serial")
public class FastGraphException extends Exception {
	
	/**
	 * Creates Exception with no message
	 */
	public FastGraphException() { 
		super(); 
	}
  
	/**
	 * Creates Exception with a message
	 * 
	 * @param message The error message
	 */
	public FastGraphException(String message) { 
		super(message);
	}
  
	/**
	 * Creates Exception with a message
	 * 
	 * @param message The error message
	 * @param cause The cause of the error
	 */
	public FastGraphException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Creates Exception with a message
	 * 
	 * @param cause The cause of the error
	 */
	public FastGraphException(Throwable cause) {
		super(cause);
	}
}
