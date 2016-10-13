package uk.ac.kent.displayGraph.dialogs;

import javax.swing.*;

import uk.ac.kent.displayGraph.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Creates a edit node modal dialog. This class changes the values of the node list
 * passed in the creator. It takes the current values of the node from the first
 * node in the list.
 *
 * @author Peter Rodgers
 */
public class EditNodeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Node> nodes;
	Node node;
	JPanel parentPanel;
	GraphSelection selection;
	
	String label;
	NodeType type;
	int x;
	int y;
	boolean visited;
	double score;

	JPanel panel;
	JTextField labelField;
	JTextField xField;
	JTextField yField;
	JTextField visitedField;
	JTextField scoreField;
	JComboBox<String> typeBox;
	JButton okButton;
	JButton cancelButton;


/** Node list must have at least one element */
	public EditNodeDialog(ArrayList<Node> nodeList, JPanel inPanel, Frame containerFrame, GraphSelection inSelection) {

		super(containerFrame,"Edit Node",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		setLocationRelativeTo(inPanel.getContainerFrame());
		setLocationRelativeTo(inPanel);

		nodes = nodeList;
		parentPanel = inPanel;
		selection = inSelection;

		node = (Node)nodes.get(0);
		label = node.getLabel();
		type = node.getType();
		x = node.getX();
		y = node.getY();
		visited = node.getVisited();
		score = node.getScore();


		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);

		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		labelField = new JTextField(16);
		labelField.setText(label);
		labelField.setCaretPosition(0);
		labelField.moveCaretPosition(label.length());
		JLabel labelLabel = new JLabel("Label: ", SwingConstants.LEFT);

		xField = new JTextField(4);
		xField.setText(Integer.toString(x));
		JLabel xLabel = new JLabel("X Coordinate: ", SwingConstants.LEFT);

		yField = new JTextField(4);
		yField.setText(Integer.toString(y));
		JLabel yLabel = new JLabel("Y Coordinate: ", SwingConstants.LEFT);

		visitedField = new JTextField(6);
		if(visited) {
			visitedField.setText("true");
		} else {
			visitedField.setText("false");
		}
		JLabel visitedLabel = new JLabel("Visited: ", SwingConstants.LEFT);

		scoreField = new JTextField(6);
		scoreField.setText(Double.toString(score));
		JLabel scoreLabel = new JLabel("Score: ", SwingConstants.LEFT);

		Vector<String> types = new Vector<String>();
		for(NodeType nt : NodeType.getExistingTypes()){
			types.add(nt.getLabel());
		}
		typeBox = new JComboBox<String>(types);
		typeBox.setSelectedIndex(types.indexOf(type.getLabel()));

		JLabel typeLabel = new JLabel("Type: ", SwingConstants.LEFT);

		okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okButton(event);
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelButton(event);
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		int yLevel = 0;

// no coordinate or label editing for multiple nodes
		if(nodes.size() == 1) {

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(labelLabel,c);
			widgetPanel.add(labelLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(labelField,c);
			widgetPanel.add(labelField);
			labelField.requestFocus();

			yLevel++;

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(xLabel,c);
			widgetPanel.add(xLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(xField,c);
			widgetPanel.add(xField);

			yLevel++;

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(yLabel,c);
			widgetPanel.add(yLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(yField,c);
			widgetPanel.add(yField);

			yLevel++;

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(visitedLabel,c);
			widgetPanel.add(visitedLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(visitedField,c);
			widgetPanel.add(visitedField);

			yLevel++;

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(scoreLabel,c);
			widgetPanel.add(scoreLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(scoreField,c);
			widgetPanel.add(scoreField);

			yLevel++;
		}

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(typeLabel,c);
		widgetPanel.add(typeLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(typeBox,c);
		widgetPanel.add(typeBox);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(cancelButton,c);
   		widgetPanel.add(cancelButton);

	}


	public void actionPerformed(ActionEvent event) {
	}


	public void okButton(ActionEvent event) {
		if((event.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
    
// if the button has been initiated by a non button press, and the
// cancel button has the focus, redirect to the cancel 
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}
		for(Node n : nodes) {
			NodeType nt = NodeType.withLabel((String)(typeBox.getSelectedItem()));
			n.setType(nt);
		}
		if(nodes.size() == 1) {
			node.setX((int)(Double.parseDouble(xField.getText())));
			node.setY((int)(Double.parseDouble(yField.getText())));
			if (visitedField.getText().equals("true")) {
				node.setVisited(true);
			} else {
				node.setVisited(false);
			}
			node.setScore(Double.parseDouble(scoreField.getText()));
			node.setLabel(labelField.getText());
		}

		selection.clear();
		parentPanel.requestFocus();
		parentPanel.update(parentPanel.getGraphics());

		dispose();

	}


	public void cancelButton(ActionEvent event) {
				dispose();
	}

}



