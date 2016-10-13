package uk.ac.kent.displayGraph.utilities;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import uk.ac.kent.displayGraph.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class GraphUtilityCreateRandomGraph extends GraphUtility implements ActionListener {


	protected int numberOfNodes = 10;
	protected int numberOfEdges = 20;

	JFrame frame;
	JPanel panel;
	JTextField nodeField;
	JTextField edgeField;
	JLabel nodeLabel;
	JLabel edgeLabel;
	JButton okButton;

/** Trivial constructor. */
	public GraphUtilityCreateRandomGraph() {
		super(KeyEvent.VK_9,"Create Random Graph");
	}

/** Trivial constructor. */
	public GraphUtilityCreateRandomGraph(int key, String s) {
		super(key,s);
	}

/** Trival accessor. */
	public int getNumberOfNodes() {return numberOfNodes;}
/** Trival accessor. */
	public int getNumberOfEdges() {return numberOfEdges;}

/** Trival mutator. */
	public void setNumberOfNodes(int nodes) {numberOfNodes = nodes;}
/** Trival mutator. */
	public void setNumberOfEdges(int edges) {numberOfEdges = edges;}


	public void apply() {
		createFrame();
	}


	protected void createFrame() {

		frame = new JFrame("Create Random Graph");
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();

		panel.setLayout(gridbag);

		addWidgets(panel,gridbag);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		nodeField = new JTextField(4);
		nodeField.setText(Integer.toString(numberOfNodes));
		nodeLabel = new JLabel("Number of Nodes: ", SwingConstants.LEFT);

		edgeField = new JTextField(4);
		edgeField.setText(Integer.toString(numberOfEdges));
		edgeLabel = new JLabel("Number of Edges: ", SwingConstants.LEFT);

		okButton = new JButton("OK");
		frame.getRootPane().setDefaultButton(okButton);

		okButton.addActionListener(this);

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(nodeLabel,c);
		widgetPanel.add(nodeLabel);

		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(nodeField,c);
		widgetPanel.add(nodeField);
		nodeField.requestFocus();

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(edgeLabel,c);
		widgetPanel.add(edgeLabel);

		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(edgeField,c);
		widgetPanel.add(edgeField);

		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

	}


	protected void randomizeGraph() {
		Graph graph = getGraph();

		int maxCount =100;
		int count =1;
		graph.generateRandomGraph(numberOfNodes,numberOfEdges,false,false);
		while(!graph.connected()) {
			if (count >= maxCount) {
				System.out.println("Failed to create a connected graph after "+count+ " attempts");
				graph.clear();
				getGraphPanel().update(getGraphPanel().getGraphics());
				getGraphPanel().requestFocus();
				return;
			}
			count++;
			graph.generateRandomGraphExact(numberOfNodes,numberOfEdges,false);
		}

		graph.randomizeNodePoints(new Point(50,50),400,400);

		getGraphPanel().update(getGraphPanel().getGraphics());
	}



	public void actionPerformed(ActionEvent event) {

		numberOfNodes = (int)(Double.parseDouble(nodeField.getText()));
		numberOfEdges = (int)(Double.parseDouble(edgeField.getText()));
		randomizeGraph();

		getGraphPanel().requestFocus();
		frame.dispose();
	}

}
