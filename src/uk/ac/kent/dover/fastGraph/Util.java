package uk.ac.kent.dover.fastGraph;

import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;

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
	 * Adds all items in a primitive array to the given linked list
	 * @param list The list to be added to
	 * @param array The array of items to add
	 */
	public static void addAll(LinkedList<Integer> list, int[] array) {
		for(int i : array) {
			list.add(i);
		}
	}
}
