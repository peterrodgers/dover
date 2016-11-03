package uk.ac.kent.dover.fastGraph;

/**
 * Class to handle simple debugging.
 * Need to call <code>Debugger.enabled = true</code> to enable to debugging.
 * 
 * @author Rob Baker
 *
 */
public class Debugger {

	//if the debugger is enabled
    public static boolean enabled = false;
    private static long time;
	
    /**
     * Prints a message to the console, if debugging is switched on
     * @param o The object to be printed
     */
	public static void log(Object o){
	    if(enabled) {
	        System.out.println(o.toString());
	    }           
	}
	
    /**
     * Prints a empty line to the console, if debugging is switched on
     */
	public static void log(){
	    if(enabled) {
	        System.out.println();
	    }           
	}
	
	/**
	 * Resets the timing system
	 */
	public static void resetTime() {
		time = System.currentTimeMillis();
	}
	
	/**
	 * Outputs the time difference between this call and the last resetTime() call
	 */
	public static void outputTime() {
		log((System.currentTimeMillis()-time)/1000.0+" seconds");
	}
	
	/**
	 * Outputs the time difference between this call and the last resetTime() call with a message
	 * 
	 * @param Object o The object to to be printed
	 */
	public static void outputTime(Object o) {
		log(o.toString() + " " + (System.currentTimeMillis()-time)/1000.0+" seconds");
	}
}
