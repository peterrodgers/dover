package uk.ac.kent.displayGraph.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.GraphSelection;


/**
 * Creates a edit edge dialog. This class changes the values of the edge list passed in the creator.
 * It takes the current values of the edge from the first edge in the list.
 *
 * @author Peter Rodgers
 */
public class EditEdgeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Edge> edges;
	Edge edge;
	JPanel parentPanel;
	GraphSelection selection;

	String label;
	EdgeType type;
	boolean visited;
	double score;
	double weight;

	JPanel panel;
	JTextField labelField;
	JTextField visitedField;
	JTextField scoreField;
	JTextField weightField;
	JComboBox<String> typeBox;
	JButton okButton;
	JButton cancelButton;


/** Edge list must have at least one element */
	public EditEdgeDialog(ArrayList<Edge> edgeList, JPanel inPanel, Frame containerFrame, GraphSelection inSelection) {

		super(containerFrame,"Edit Edge",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		edges = edgeList;
		parentPanel = inPanel;
		selection = inSelection;

		edge = (Edge)edges.get(0);
		label = edge.getLabel();
		type = edge.getType();
		visited = edge.getVisited();
		score = edge.getScore();
		weight = edge.getWeight();

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

		weightField = new JTextField(6);
		weightField.setText(Double.toString(weight));
		JLabel weightLabel = new JLabel("Weight: ", SwingConstants.LEFT);

		Vector<String> types = new Vector<String>();
		for(EdgeType nt : EdgeType.getExistingTypes()){
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

// no coordinate or label editing for multiple edges
		if(edges.size() == 1) {

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

			c.gridx = 0;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.EAST;
			gridbag.setConstraints(weightLabel,c);
			widgetPanel.add(weightLabel);

			c.gridx = 1;
			c.gridy = yLevel;
			c.anchor = GridBagConstraints.WEST;
			gridbag.setConstraints(weightField,c);
			widgetPanel.add(weightField);

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
		for(Edge e : edges) {
			EdgeType et = EdgeType.withLabel((String)(typeBox.getSelectedItem()));
			e.setType(et);
		}
		if(edges.size() == 1) {
			edge.setLabel(labelField.getText());
			if (visitedField.getText().equals("true")) {
				edge.setVisited(true);
			} else {
				edge.setVisited(false);
			}
			edge.setScore(Double.parseDouble(scoreField.getText()));
			edge.setWeight(Double.parseDouble(weightField.getText()));
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



