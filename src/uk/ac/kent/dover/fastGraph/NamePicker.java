package uk.ac.kent.dover.fastGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class will load the list of potential names and can give a weighted random forename, surname pair<br>
 * Data has been obtained from http://www.census.gov/topics/population/genealogy/data/1990_census/1990_census_namefiles.html<br>
 * Data is from the 1990 US Census. The UK does not produce such stats and the US data has no copyright. https://ask.census.gov/faq.php?id=5000&faqId=431<br>
 * Data is stored in the /names/ directory, and has been modified in the following ways:
 * <ul>
 * <li>The surnames list has been cut short to 5000 names
 * <li>The female forenames list has been cut short so that each mane represents at least 0.002% of all names - 
 * this ensures that the cumulative frequency increases each time (due to rounding it does not for lower values).
 * <li>The name forenames list is unchanged
 * </ul>
 * 
 * @author Rob Baker
 *
 */
public class NamePicker {
	
	//The maximum cumulative frequency of the names in the original source material
	private static final double SURNAME_TOTAL_PROB = 63.234;
	private static final double MALE_FORENAME_TOTAL_PROB = 90.040;
	private static final double FEMALE_FORENAME_TOTAL_PROB = 88.594;
	
	private LinkedList<Name> surnames = new LinkedList<>();
	private LinkedList<Name> maleForenames = new LinkedList<>();
	private LinkedList<Name> femaleForenames = new LinkedList<>();
	
	/**
	 * Constructor builds the three lists of names
	 * 
	 * @throws IOException If any of the files cannot be loaded
	 */
	public NamePicker() throws IOException{
		buildNamesList(surnames,"names/dist.all.last.short"); //limited to 5000 names
		buildNamesList(maleForenames,"names/dist.male.first"); //as original		
		buildNamesList(femaleForenames,"names/dist.female.first.short"); //limited to the probability change is 0.002
	}
	
	/**
	 * Builds a list of names and cumulative probabilities
	 * 
	 * @param list The list of Names to populate
	 * @param filename The filename to extract the data from
	 * @throws IOException If the files cannot be loaded, or the values not parsed to a double
	 */
	private void buildNamesList(LinkedList<Name> list, String filename) throws IOException {		
		BufferedReader br = new BufferedReader(new FileReader(filename));
	    String line = br.readLine();
	    while (line != null) {
	    	
	    	String name = line.substring(0, 15).trim();
	    	name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	    	String probString = line.substring(21, 28).trim();
	    	double prob = Double.parseDouble(probString);
	    	list.add(new Name(name,prob));	    	
	        line = br.readLine();
	    }
	    br.close();
	}
	
	/**
	 * Returns a surname, based on weighted probabilities.
	 * @return The surname
	 */
	public String getSurname() {
		double rNum = Math.random() * SURNAME_TOTAL_PROB;
		for(Name name : surnames) {
			if (rNum < name.getProb()) {
				//System.out.println("surname: " + name.getName() + " " + rNum);
				return name.getName();
			}
		}
		//System.out.println("No surname:");
		return null;
	}
	
	/**
	 * Returns a forename, based on weighted probabilities.
	 * 50/50 choice as to whether a male or female name is returned
	 * @return The forename
	 */
	public String getForename() {
		//if we're after a man
		if((new Random()).nextBoolean()) {
			double rNum = Math.random() * MALE_FORENAME_TOTAL_PROB;
			for(Name name : maleForenames) {
				if (rNum < name.getProb()) {
					//System.out.println("maleForename: " + name.getName() + " " + rNum);
					return name.getName();
				}
			}
			//System.out.println("No maleForename:");
			return null;
			//else, a woman
		} else {
			double rNum = Math.random() * FEMALE_FORENAME_TOTAL_PROB;
			for(Name name : femaleForenames) {
				if (rNum < name.getProb()) {
					//System.out.println("femaleForename: " + name.getName() + " " + rNum);
					return name.getName();
				}
			}
			//System.out.println("No femaleForename:");
			return null;
		}
		
	}
	
	/**
	 * Returns a full name, using the getForename() and getSurname() methods;
	 * @return The full name
	 */
	public String getName() {
		return getForename() + " " + getSurname();
	}
	
	/**
	 * Returns a list of full names
	 * 
	 * @param number The number of name to return
	 * @return The list of full names
	 */
	public String[] getNames(int number) {
		String[] names = new String[number];
		for (int i = 0; i < number; i++) {
			names[i] = getName();
		}
		return names;
	}
	
}
