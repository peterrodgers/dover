package uk.ac.kent.dover.fastGraph.Gui;

/**
 * This handles the progress info to be used to adjust the values of progress bars
 * 
 * @author Rob Baker
 *
 */
public class Progress {

	private int mainTaskNum = 0;
	private String mainTaskText = "";
	private int childTaskNum = 0;
	private String childTaskText = "";

	/**
	 * Empty constructor
	 */
	public Progress() {
		
	}
	
	/**
	 * Assigns the values given for this progress
	 * 
	 * @param mainTaskNum The number of the main Progress bar (0-100)
	 * @param mainTaskText The text to be displayed on the main Progress bar
	 * @param childTaskNum The number of the smaller Progress bar (0-100)
	 * @param childTaskText The text to be displayed on the smaller Progress bar
	 */
	public Progress(int mainTaskNum, String mainTaskText, int childTaskNum, String childTaskText) {
		super();
		this.mainTaskNum = mainTaskNum;
		this.mainTaskText = mainTaskText;
		this.childTaskNum = childTaskNum;
		this.childTaskText = childTaskText;
	}

	/**
	 * @return the mainTaskNum
	 */
	public int getMainTaskNum() {
		return mainTaskNum;
	}

	/**
	 * @param mainTaskNum the mainTaskNum to set
	 */
	public void setMainTaskNum(int mainTaskNum) {
		this.mainTaskNum = mainTaskNum;
	}

	/**
	 * @return the mainTaskText
	 */
	public String getMainTaskText() {
		return mainTaskText;
	}

	/**
	 * @param mainTaskText the mainTaskText to set
	 */
	public void setMainTaskText(String mainTaskText) {
		this.mainTaskText = mainTaskText;
	}

	/**
	 * @return the childTaskNum
	 */
	public int getChildTaskNum() {
		return childTaskNum;
	}

	/**
	 * @param childTaskNum the childTaskNum to set
	 */
	public void setChildTaskNum(int childTaskNum) {
		this.childTaskNum = childTaskNum;
	}

	/**
	 * @return the childTaskText
	 */
	public String getChildTaskText() {
		return childTaskText;
	}

	/**
	 * @param childTaskText the childTaskText to set
	 */
	public void setChildTaskText(String childTaskText) {
		this.childTaskText = childTaskText;
	}
	
	
}
