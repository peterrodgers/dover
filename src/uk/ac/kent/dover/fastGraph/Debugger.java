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
	 * Returns the current time for time keeping
	 * 
	 * @return The current time
	 */
	public static long createTime() {
		return System.currentTimeMillis();
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
	
	/**
	 * Outputs the time difference between this call and the last createTime() call for the given time
	 * 
	 * @param time A given timing variable
	 */
	public static void outputTime(long time) {
		log((System.currentTimeMillis()-time)/1000.0+" seconds");
	}
	
	/**
	 * Outputs the time difference between this call and the last createTime() call for the given time with a message
	 * 
	 * @param Object o The object to to be printed
 	 * @param time A given timing variable
	 */
	public static void outputTime(Object o, long time) {
		log(o.toString() + " " + (System.currentTimeMillis()-time)/1000.0+" seconds");
	}
	
	/**
	 * Outputs the difference between the current time and the one given
	 * 
	 * @param time The given time
	 * @return The difference between now and the current time
	 */
	public static long getTimeSinceInSeconds(long time) {
		return (System.currentTimeMillis()-time)/(long) 1000.0;
	}
}
