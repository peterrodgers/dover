package uk.ac.kent.displayGraph.dialogs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.ItemType;


/**
 * Creates a edit edge type dialog.
 *
 * @author Peter Rodgers
 */
public class EditEdgeTypeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	protected ManageEdgeTypesDialog metd;
	protected EdgeType et;
	protected JPanel parentPanel;
	protected boolean newFlag;

	protected Vector<String> indentedTypeLabels;

	protected JTextField labelField;
	protected JTextField priorityField;
	protected JTextField strokeField;
	protected JTextField selectedStrokeField;
	protected JButton lineColorButton;
	protected JPanel lineColorPanel;
	protected JButton selectedLineColorButton;
	protected JPanel selectedLineColorPanel;
	protected JButton textColorButton;
	protected JPanel textColorPanel;
	protected JButton selectedTextColorButton;
	protected JPanel selectedTextColorPanel;
	protected JCheckBox directionBox;
	protected JList<String> typeList;

	protected JButton okButton;
	protected JButton cancelButton;

	protected JPanel typePanel;
	protected JPanel buttonPanel;

	public static final Dimension PANELSIZE = new Dimension(20,20);
	public static final Dimension TEXTSIZE = new Dimension(150,32);
	public static final int FIELDWIDTH = 10;

	public EditEdgeTypeDialog(ManageEdgeTypesDialog manageDialog, JPanel inPanel, Frame containerFrame, EdgeType edgeType, boolean inNewFlag, String windowLabel) {

		super(containerFrame,windowLabel,true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		metd = manageDialog;
		et = edgeType;
		parentPanel = inPanel;
		newFlag = inNewFlag;

		typePanel = new JPanel();
		buttonPanel = new JPanel();

		Border etchedBorder = BorderFactory.createEtchedBorder();
		Border spaceBorder1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border spaceBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder,spaceBorder1);
		Border panelBorder = BorderFactory.createCompoundBorder(spaceBorder2,compoundBorder);

		typePanel.setBorder(panelBorder);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		getContentPane().setLayout(gridbag);

		c.ipadx = 2;
		c.ipady = 2;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(typePanel,c);
		getContentPane().add(typePanel);

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(buttonPanel,c);
		getContentPane().add(buttonPanel);

		setupTypeLabels();

		setupType(typePanel);
		setupButtons(buttonPanel);

		pack();

		setVisible(true);
	}



	protected void setupTypeLabels() {

		indentedTypeLabels = new Vector<String>();

		ArrayList<EdgeType> allRoots = EdgeType.allRoots();
		for(EdgeType t : allRoots){
			ArrayList<String> tree = t.outTreeStart("  ");
			indentedTypeLabels.addAll(tree);
		}
// add a blank to allow null parent selection
		indentedTypeLabels.add("  ");
	}



	protected void setupType(JPanel panel) {

		JLabel label;

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = externalPadding;

// type name

		labelField = new JTextField(FIELDWIDTH);

		if(newFlag) { labelField.setText("");}
		else { labelField.setText(et.getLabel());}

		label = new JLabel("Type Label: ", SwingConstants.RIGHT);

		if(!newFlag) {
			labelField.setEditable(false);
		}

		int yLevel = 0;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(labelField,c);
		panel.add(labelField);

// priority

		yLevel++;

		priorityField = new JTextField(FIELDWIDTH);

		if(et == null) { priorityField.setText("-1");}
		else { priorityField.setText(Integer.toString(et.getPriority()));}

		label = new JLabel("Priority: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(priorityField,c);
		panel.add(priorityField);

// stroke

		yLevel++;

		strokeField = new JTextField(FIELDWIDTH);

		if(et == null) { strokeField.setText("1.0");}
		else { strokeField.setText(Float.toString(et.getStroke().getLineWidth()));}

		label = new JLabel("Stroke: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(strokeField,c);
		panel.add(strokeField);

// selected stroke

		yLevel++;

		selectedStrokeField = new JTextField(FIELDWIDTH);

		if(et == null) { selectedStrokeField.setText("1.0");}
		else { selectedStrokeField.setText(Float.toString(et.getSelectedStroke().getLineWidth()));}

		label = new JLabel("Selected Stroke: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(selectedStrokeField,c);
		panel.add(selectedStrokeField);

// normal line color

		yLevel++;

		lineColorButton = new JButton("Line Color");
		lineColorButton.setPreferredSize(TEXTSIZE);
		lineColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Edge Line Color",lineColorPanel.getBackground());
				lineColorPanel.setBackground(chosenColor);
				lineColorPanel.update(lineColorPanel.getGraphics());
			}
		});

		lineColorPanel = new JPanel();

		if(et == null) { lineColorPanel.setBackground(Color.black);}
		else { lineColorPanel.setBackground(et.getLineColor());}

		lineColorPanel.setMinimumSize(PANELSIZE);
		lineColorPanel.setPreferredSize(PANELSIZE);
		lineColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(lineColorButton,c);
   		panel.add(lineColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(lineColorPanel,c);
   		panel.add(lineColorPanel);


// selected line color

		yLevel++;

		selectedLineColorButton = new JButton("Selected Line Color");
		selectedLineColorButton.setPreferredSize(TEXTSIZE);
		selectedLineColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Selected Edge Line Color",selectedLineColorPanel.getBackground());
				selectedLineColorPanel.setBackground(chosenColor);
				selectedLineColorPanel.update(selectedLineColorPanel.getGraphics());
			}
		});

		selectedLineColorPanel = new JPanel();

		if(et == null) { selectedLineColorPanel.setBackground(Color.gray);}
		else { selectedLineColorPanel.setBackground(et.getSelectedLineColor());}

		selectedLineColorPanel.setMinimumSize(PANELSIZE);
		selectedLineColorPanel.setPreferredSize(PANELSIZE);
		selectedLineColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedLineColorButton,c);
   		panel.add(selectedLineColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedLineColorPanel,c);
   		panel.add(selectedLineColorPanel);


// normal line color

		yLevel++;

		textColorButton = new JButton("Text Color");
		textColorButton.setPreferredSize(TEXTSIZE);
		textColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Edge Text Color",textColorPanel.getBackground());
				textColorPanel.setBackground(chosenColor);
				textColorPanel.update(textColorPanel.getGraphics());
			}
		});

		textColorPanel = new JPanel();

		if(et == null) { textColorPanel.setBackground(Color.black);}
		else { textColorPanel.setBackground(et.getTextColor());}

		textColorPanel.setMinimumSize(PANELSIZE);
		textColorPanel.setPreferredSize(PANELSIZE);
		textColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(textColorButton,c);
   		panel.add(textColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(textColorPanel,c);
   		panel.add(textColorPanel);


// selected text color

		yLevel++;

		selectedTextColorButton = new JButton("Selected Text Color");
		selectedTextColorButton.setPreferredSize(TEXTSIZE);
		selectedTextColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Selected Edge Text Color",selectedTextColorPanel.getBackground());
				selectedTextColorPanel.setBackground(chosenColor);
				selectedTextColorPanel.update(selectedTextColorPanel.getGraphics());
			}
		});

		selectedTextColorPanel = new JPanel();

		if(et == null) { selectedTextColorPanel.setBackground(Color.gray);}
		else { selectedTextColorPanel.setBackground(et.getSelectedTextColor());}

		selectedTextColorPanel.setMinimumSize(PANELSIZE);
		selectedTextColorPanel.setPreferredSize(PANELSIZE);
		selectedTextColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedTextColorButton,c);
   		panel.add(selectedTextColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedTextColorPanel,c);
   		panel.add(selectedTextColorPanel);

// direction

		yLevel++;

		directionBox = new JCheckBox("Direction",false);

		if(et != null) { directionBox.setSelected(et.getDirected());}

		c.gridx = 0;
		c.gridy = yLevel;
		c.gridwidth = 2;
		gridbag.setConstraints(directionBox,c);
		panel.add(directionBox);

// parent

		label = new JLabel("Parent", SwingConstants.CENTER);

		c.gridx = 2;
		c.gridy = 7;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(label,c);
		panel.add(label);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);

		typeList = new JList<String>(indentedTypeLabels);
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		if(et != null) {
			int index = 0;
			ItemType parent = et.getParent();
			if(parent != null) {
				for(String s : indentedTypeLabels){
					s = s.trim();
					if(s.equals(parent.getLabel())) {
						break;
					}
					index++;
				}
				typeList.setSelectedIndex(index);
			}
		}

		JScrollPane scrollBar = new JScrollPane(typeList);
		scrollBar.setPreferredSize(new Dimension(150, 100));
		scrollBar.setBorder(lineBorder);

		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 7;
		c.fill = GridBagConstraints.VERTICAL;

		gridbag.setConstraints(scrollBar,c);
		panel.add(scrollBar);

	}


	protected Color getColor(String title,Color color) {
		Color retColor = JColorChooser.showDialog(this, title, color);
		if(retColor == null) {
			retColor = color;
		}
		return retColor;
	}


	protected void setupButtons(JPanel panel) {

		okButton = new JButton("OK");
		okButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okButton(event);
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelButton(event);
			}
		});

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(okButton,c);
   		panel.add(okButton);

		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(cancelButton,c);
   		panel.add(cancelButton);

	}



	public void actionPerformed(ActionEvent event) {
	}



	public void okButton(ActionEvent event) {

		if(newFlag) {
			String label = labelField.getText();
			if(label.equals("")) {
				JOptionPane.showMessageDialog(parentPanel, "Cannot have an empty label.","Error",JOptionPane.PLAIN_MESSAGE);
				return;
			}
			if(EdgeType.withLabel(label) != null) {
				JOptionPane.showMessageDialog(parentPanel, "That label already exists.","Error",JOptionPane.PLAIN_MESSAGE);
				return;
			}
			et = new EdgeType(label);
		}

		EdgeType parent = null;
		if(!typeList.isSelectionEmpty()) {
			int index = typeList.getSelectedIndex();
			String parentText = (String)indentedTypeLabels.get(index);
			parentText = parentText.trim();
			if(!parentText.equals("")) {
				parent = EdgeType.withLabel(parentText);
			}
		}

		if(!et.setParent(parent)) {
			JOptionPane.showMessageDialog(parentPanel, "Invalid Parent, loop caused in parent heirarchy.","Error",JOptionPane.PLAIN_MESSAGE);
			return;
		}

		et.setLineColor(lineColorPanel.getBackground());
		et.setPriority((int)Double.parseDouble(priorityField.getText()));
		et.setStroke(new BasicStroke(Float.parseFloat(strokeField.getText())));
		et.setSelectedStroke(new BasicStroke(Float.parseFloat(selectedStrokeField.getText())));
		et.setSelectedLineColor(selectedLineColorPanel.getBackground());
		et.setTextColor(textColorPanel.getBackground());
		et.setSelectedTextColor(selectedTextColorPanel.getBackground());
		et.setDirected(directionBox.isSelected());

		dispose();
	}

	public void cancelButton(ActionEvent event) {

		dispose();
	}



}



