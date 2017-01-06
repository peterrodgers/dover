package uk.ac.kent.dover.fastGraph;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class to hold a number of useful methods
 * 
 * @author Rob Baker
 *
 */
public class Util {

	/**
	 * Rounds and sorts a double[][] to an int[][]
	 * @param arr a double[][] to be rounded
	 * @return The rounded int[][]
	 */
	public static int[][] roundArray(double[][] arr) {
		int[][] newArr = new int[arr.length][arr[0].length];
		for(int i = 0; i < arr.length; i++) {
			for(int j = 0; j < arr[i].length; j++) {
				newArr[i][j] = (int) Math.round(arr[i][j]);
			}
		}
		Arrays.sort(newArr);
		return newArr;
	}
	
	/**
	 * Rounds and sorts a double[] to an int[]
	 * @param arr a double[] to be rounded
	 * @return The rounded int[]
	 */
	public static int[] roundArray(double[] arr) {
		int[] newArr = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = (int) Math.round(arr[i]);
		}
		Arrays.sort(newArr);
		return newArr;
	}
	
	
	/**
	 * Rounds and a double[] to a less precise double[]
	 * @param arr a double[] to be rounded
	 * @param decimalPlaces the amount of decimal places to round each double in arr
	 * @return The rounded double[]
	 */
	public static double[] roundArray(double[] arr, int decimalPlaces) {
		double[] newArr = new double[arr.length];
		for(int i = 0; i < arr.length; i++) {
			newArr[i] = round(arr[i],decimalPlaces);
			if(newArr[i] == -0.0) {
				newArr[i] = 0.0;
			}
				
		}
		Arrays.sort(newArr);
		return newArr;
	}
	
	
	/**
	 * Round to the given number of decimal places.
	 * @param d a double to be rounded
	 * @param decimalPlaces the number of decimal places 
	 * @return The rounded double
	 */
	public static double round(double d,int decimalPlaces) {

		long divider = 1;
		for(int i = 1; i<= decimalPlaces; i++) {
			divider *= 10;
		}
		double largeAmount = Math.rint(d*divider);
		return(largeAmount/divider);
	}

	/**
	 * Rounds and Converts a double[].
	 * Multiplies each value by places and converts to an integer
	 * 
	 * @param toConvert The array to convert
 	 * @param places The number of decimal places to be retained
	 * @return The converted array
	 */
	public static int[] roundAndConvert(double[] toConvert, int places) {
		int[] res = new int[toConvert.length];
		for(int i = 0; i < toConvert.length; i++) {
			res[i] = roundAndConvert(toConvert[i], places);
		}
		return res;
	}
	
	/**
	 * Rounds and Converts a double[].
	 * Multiplies each value by places and converts to an integer
	 * 
	 * @param res The array to be populated
	 * @param toConvert The array to convert
	 * @param places The number of decimal places to be retained
	 * @return The converted array
	 */
	public static int[] roundAndConvert(int[] res, double[] toConvert, int places) {
		for(int i = 0; i < toConvert.length; i++) {
			res[i] = roundAndConvert(toConvert[i],places);
		}
		return res;
	}
	
	/**
	 * Rounds and Converts a double.
	 * Multiplies each value by places and converts to an integer
	 * 
	 * @param toConvert The double to convert
	 * @param places The number of decimal places to be retained
	 * @return The converted and rounded int
	 */
	public static int roundAndConvert(double toConvert, int places) {
		return (int) Math.round(toConvert*places);	
	}

	
	/**
	 * Checks if a String is a positive integer.
	 * 
	 * @param input The String to be converted
	 * @throws NumberFormatException If the input is not an integer, or < 0.
	 * @return The positive integer
	 */
	public static int checkForPositiveInteger(String input) throws NumberFormatException {
		int number = Integer.parseInt(input);
		if (number < 0) {
			throw new NumberFormatException();
		} else {
			return number;
		}
	}
	
	/**
	 * Converts a LinkedList of any given object into an array of that object
	 * 
	 * @param list The list to be converted
	 * @param array The array to be populated with the new objects
	 */
	public static <T> void convertLinkedListObject(LinkedList<T> list, T[] array) {
		list.toArray(array);
	}
	
	/**
	 * Converts a LinkedList of Integer to an int[] using streams
	 * 
	 * @param list The linked list to convert
	 * @return The newly created array
	 */
	public static int[] convertLinkedList(LinkedList<Integer> list) {
		return list.stream().mapToInt(i->i).toArray();
	}
	
	/**
	 * Converts a LinkedList of Integer to a given int[] using streams
	 * 
	 * @param list The linked list to convert
	 * @param array The array to be populated
	 */
	public static void convertLinkedList(LinkedList<Integer> list, int[] array) {
		array = list.stream().mapToInt(i->i).toArray();
	}
	
	/**
	 * Converts an int[] to a LinkedList of Integer using streams
	 * 
	 * @param array The array to convert
	 * @return The newly created LinkedList
	 */
	public static LinkedList<Integer> convertArray(int[] array) {
		return new LinkedList<Integer>(IntStream.of(array).boxed().collect(Collectors.toList()));
	}
	
	
	/**
	 * Converts an int[] to a LinkedList of Integer using streams
	 * 
	 * @param array The array to convert
	 * @param list The newly populated LinkedList
	 */
	public static void convertArray(int[] array, LinkedList<Integer> list) {
		list.addAll(IntStream.of(array).boxed().collect(Collectors.toList()));
	}	
	
	/**
	 * Adds all items in a primitive array to the given linked list
	 * @param list The list to be added to
	 * @param array The array of items to add
	 */
	public static void addAll(LinkedList<Integer> list, int[] array) {
		for(int i : array) {
			list.add(i);
		}
	}
	
	/**
	 * Adds all items in a primitive array to the given linked hash set
	 * @param set The set to be added to
	 * @param array The array of items to add
	 */
	public static void addAll(LinkedHashSet<Integer> set, int[] array) {
		for(int i : array) {
			set.add(i);
		}
	}
	
	/**
	 * Adds all items in a primitive array to the given  hash set
	 * @param set The set to be added to
	 * @param array The array of items to add
	 */
	public static void addAll(HashSet<Integer> set, int[] array) {
		for(int i : array) {
			set.add(i);
		}
	}
	
	/**
	 * Converts a HashSet<Integer> to int[]
	 * 
	 * @param set The set to be converted
	 * @return The newly converted array
	 */
	public static int[] convertHashSet(HashSet<Integer> set) {
		return set.stream().mapToInt(i->i).toArray();
	}
	
	/**
	 * Converts a HashSet<Integer> to a given int[]
	 * 
	 * @param set The set to be converted
	 * @param array The newly converted array
	 */
	public static void convertHashSet(int[] array, HashSet<Integer> set) {
		array = set.stream().mapToInt(i->i).toArray();
	}
	
	/**
	 * Converts an int[] to a HashSet of Integer using streams
	 * 
	 * @param array The array to convert
	 */
	public static void convertArray(int[] array, HashSet<Integer> set) {
		set.addAll(IntStream.of(array).boxed().collect(Collectors.toList()));
	}	
	
	
	/**
	 * Deep copy of ByteBuffer.
	 * @param original the ByteBuffer to copy
	 * @return new ByteBuffer with a copy of the content of original.
	 */
	public static ByteBuffer cloneByteBuffer(ByteBuffer original) {
	       ByteBuffer clone = ByteBuffer.allocate(original.capacity());
	       original.rewind();//copy from the beginning
	       clone.put(original);
	       original.rewind();
	       clone.flip();
	       return clone;
	}
	
	/**
	 * Gets a specific numbered item from the HashSet given
	 * 
	 * @param set The Set to look through
	 * @param number The item nmber to return
	 * @return The item
	 */
	public static <T> T getFromHashSet(HashSet<T> set, int number) {
		int i = 0;
		for (T item : set) {
			if (i == number) {
				return item;
			}
			i++;
		}
		return null;
	}
	
	/**
	 * Returns a sublist.<br>
	 * A copy of List.sublist() except the end is checked and shortend if too long
	 * 
	 * @param list The list to have a sublist of
	 * @param start The start index
	 * @param end The end index
	 * @return The sublist
	 */
	public static <T> List<T> subList(ArrayList<T> list, int start, int end) {
		if(end > list.size()) {
			end = list.size();
		}
		return list.subList(start, end);
	}
	
	/**
	 * Returns the arithmetic mean of an array of doubles
	 * @param array The array
	 * @return The mean
	 */
	public double mean(double[] array) {
		if(array.length == 0) { //incase the array is empty
			return Double.NaN;
		}
		
		int total = 0;
		for(int i = 0; i < array.length; i++) {
			total += array[i];
		}
		return total/array.length;
	}
	
	/**
	 * Returns the standard deviation of an array
	 * 
	 * @param array The array itself
	 * @param mean The mean of the array
	 * @return The standard deviation
	 */
	public double standardDeviation(double[] array, int mean) {
		if (array.length == 0) {
			return Double.NaN;
		}
		double result = 0;
		for(int i = 0; i < array.length; i++) {
			result += Math.pow(array[i] - mean, 2);
		}
		result = result / (array.length-1); //variance
		return Math.sqrt(result);
	}
	
	/**
	 * Returns the standard deviation of an array
	 * 
	 * @param array The array itself
	 * @param mean The mean of the array
	 * @return The standard deviation
	 */
	public double standardDeviation(ArrayList<Integer> array, int mean) {
		if (array.size() == 0) {
			return Double.NaN;
		}
		double result = 0;
		for(int i = 0; i < array.size(); i++) {
			result += Math.pow(array.get(i) - mean, 2);
		}
		result = result / (array.size()-1); //variance
		return Math.sqrt(result);
	}
}
