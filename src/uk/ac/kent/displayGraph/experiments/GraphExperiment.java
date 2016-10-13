package uk.ac.kent.displayGraph.experiments;

import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.GraphPanel;


/**
 * This is a way of passing graph modification methods to
 * the GraphPanel class.
 * Any such modification algorithm should be implemented by
 * extending this class and overloading the {@link #experiment} method.
 * <p>
 * The acceleratorKey is the key that when pressed in the GraphPanel
 * calls the apply method.
 *
 * @author Peter Rodgers
 */


public abstract class GraphExperiment {

/** the associated GraphPanel */
	protected GraphPanel graphPanel = null;
/** the key that should be pressed to start the graph drawing method. Codes can be found in the KeyEvent class. */
	protected int acceleratorKey = 0;
/** the message that should appear in menu options to start the graph drawing method */
	protected String menuText;
/** the menu mnemonic for initiating the layout. Codes can be found in the KeyEvent class. */
	protected int mnemonicKey = 0;

/** Minimal constructor. */
	public GraphExperiment(String inMenuText) {
		menuText = inMenuText;
	}

/** Trivial constructor. */
	public GraphExperiment(int inAccelerator, String inMenuText) {
		acceleratorKey = inAccelerator;
		menuText = inMenuText;
	}

/** Trivial constructor. */
	public GraphExperiment(int inAccelerator, String inMenuText, int inMnemonic) {
		acceleratorKey = inAccelerator;
		menuText = inMenuText;
		mnemonicKey = inMnemonic;
	}

/** Trival accessor. */
	public GraphPanel getGraphPanel() {return graphPanel;}
/** Returns the graph of graphPanel, as this is the one that is drawn. */
	public Graph getGraph() {return graphPanel.getGraph();}
/** Trival accessor. */
	public int getAcceleratorKey() {return acceleratorKey;}
/** Trival accessor. */
	public String getMenuText() {return menuText;}
/** Trival accessor. */
	public int getMnemonicKey() {return mnemonicKey;}

/** This modifier should only be used by GraphPanel. */
	public void setGraphPanel(GraphPanel inGraphPanel) {graphPanel=inGraphPanel;}
/** Trivial modifier. */
	public void setAcceleratorKey(int inKey) {acceleratorKey=inKey;}
/** Trivial modifier. */
	public void setMenuText(String inMenuText) {menuText=inMenuText;}
/** Trivial modifier. */
	public void setMnemonicKey(int inKey) {mnemonicKey=inKey;}

/** Overwrite this with the code to run the experiment. */
	public abstract void experiment();

}



