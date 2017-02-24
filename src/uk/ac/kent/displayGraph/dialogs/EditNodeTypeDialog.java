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
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
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

import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.ItemType;
import uk.ac.kent.displayGraph.NodeType;


/**
 * Creates a edit node type dialog.
 *
 * @author Peter Rodgers
 */
public class EditNodeTypeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	protected ManageNodeTypesDialog mntd;
	protected NodeType nt;
	protected JPanel parentPanel;
	protected boolean newFlag;

	protected Vector<String> indentedTypeLabels;

	protected JTextField labelField;
	protected JTextField widthField;
	protected JTextField heightField;
	protected JTextField strokeField;
	protected JTextField selectedStrokeField;
	protected JButton borderColorButton;
	protected JPanel borderColorPanel;
	protected JButton fillColorButton;
	protected JPanel fillColorPanel;
	protected JButton selectedBorderColorButton;
	protected JPanel selectedBorderColorPanel;
	protected JButton selectedFillColorButton;
	protected JPanel selectedFillColorPanel;
	protected JButton textColorButton;
	protected JPanel textColorPanel;
	protected JButton selectedTextColorButton;
	protected JPanel selectedTextColorPanel;
	protected JList<String> typeList;
	protected JComboBox<String> shapeBox;

	protected JButton okButton;
	protected JButton cancelButton;

	protected JPanel typePanel;
	protected JPanel buttonPanel;

	public static final Dimension PANELSIZE = new Dimension(20,20);
	public static final Dimension TEXTSIZE = new Dimension(150,32);
	public static final int FIELDWIDTH = 10;

	public EditNodeTypeDialog(ManageNodeTypesDialog manageDialog, JPanel inPanel, Frame containerFrame, NodeType nodeType, boolean inNewFlag, String windowLabel) {

		super(containerFrame,windowLabel,true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		mntd = manageDialog;
		nt = nodeType;
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

		ArrayList<NodeType> allRoots = NodeType.allRoots();
		for(NodeType t : allRoots) {
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

// label

		labelField = new JTextField(FIELDWIDTH);

		if(newFlag) { labelField.setText("");}
		else { labelField.setText(nt.getLabel());}

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

// shape
		
		String[] shapeArray = {"Ellipse","Rectangle"};
		shapeBox = new JComboBox<String>(shapeArray);
		shapeBox.setSelectedIndex(0);

		yLevel++;

		label = new JLabel("Type Shape: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(shapeBox,c);
		panel.add(shapeBox);

// width

		yLevel++;

		widthField = new JTextField(FIELDWIDTH);

		if(nt == null) { widthField.setText("30");}
		else { widthField.setText(Integer.toString(nt.getWidth()));}

		label = new JLabel("Width: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(widthField,c);
		panel.add(widthField);

// height

		yLevel++;

		heightField = new JTextField(FIELDWIDTH);

		if(nt == null) { heightField.setText("30");}
		else { heightField.setText(Integer.toString(nt.getHeight()));}

		label = new JLabel("Height: ", SwingConstants.RIGHT);

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(label,c);
		panel.add(label);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(heightField,c);
		panel.add(heightField);

// stroke

		yLevel++;

		strokeField = new JTextField(FIELDWIDTH);

		if(nt == null) { strokeField.setText("2.0");}
		else { strokeField.setText(Float.toString(nt.getStroke().getLineWidth()));}

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

		if(nt == null) { selectedStrokeField.setText("2.0");}
		else { selectedStrokeField.setText(Float.toString(nt.getSelectedStroke().getLineWidth()));}

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

// normal border color

		yLevel++;

		borderColorButton = new JButton("Border Color");
		borderColorButton.setPreferredSize(TEXTSIZE);
		borderColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Node Border Color",borderColorPanel.getBackground());
				borderColorPanel.setBackground(chosenColor);
				borderColorPanel.update(borderColorPanel.getGraphics());
			}
		});

		borderColorPanel = new JPanel();

		if(nt == null) { borderColorPanel.setBackground(Color.black);}
		else { borderColorPanel.setBackground(nt.getBorderColor());}

		borderColorPanel.setMinimumSize(PANELSIZE);
		borderColorPanel.setPreferredSize(PANELSIZE);
		borderColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(borderColorButton,c);
   		panel.add(borderColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(borderColorPanel,c);
   		panel.add(borderColorPanel);


// selected border color

		yLevel++;

		selectedBorderColorButton = new JButton("Selected Border Color");
		selectedBorderColorButton.setPreferredSize(TEXTSIZE);
		selectedBorderColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Selected Border Line Color",selectedBorderColorPanel.getBackground());
				selectedBorderColorPanel.setBackground(chosenColor);
				selectedBorderColorPanel.update(selectedBorderColorPanel.getGraphics());
			}
		});

		selectedBorderColorPanel = new JPanel();

		if(nt == null) { selectedBorderColorPanel.setBackground(Color.white);}
		else { selectedBorderColorPanel.setBackground(nt.getSelectedBorderColor());}

		selectedBorderColorPanel.setMinimumSize(PANELSIZE);
		selectedBorderColorPanel.setPreferredSize(PANELSIZE);
		selectedBorderColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedBorderColorButton,c);
   		panel.add(selectedBorderColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedBorderColorPanel,c);
   		panel.add(selectedBorderColorPanel);

// normal fill color

		yLevel++;

		fillColorButton = new JButton("Fill Color");
		fillColorButton.setPreferredSize(TEXTSIZE);
		fillColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Node Fill Color",fillColorPanel.getBackground());
				fillColorPanel.setBackground(chosenColor);
				fillColorPanel.update(fillColorPanel.getGraphics());
			}
		});

		fillColorPanel = new JPanel();

		if(nt == null) { fillColorPanel.setBackground(Color.white);}
		else { fillColorPanel.setBackground(nt.getFillColor());}

		fillColorPanel.setMinimumSize(PANELSIZE);
		fillColorPanel.setPreferredSize(PANELSIZE);
		fillColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(fillColorButton,c);
   		panel.add(fillColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(fillColorPanel,c);
   		panel.add(fillColorPanel);


// selected fill color

		yLevel++;

		selectedFillColorButton = new JButton("Selected Fill Color");
		selectedFillColorButton.setPreferredSize(TEXTSIZE);
		selectedFillColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Selected Node Fill Color",selectedFillColorPanel.getBackground());
				selectedFillColorPanel.setBackground(chosenColor);
				selectedFillColorPanel.update(selectedFillColorPanel.getGraphics());
			}
		});

		selectedFillColorPanel = new JPanel();

		if(nt == null) { selectedFillColorPanel.setBackground(Color.black);}
		else { selectedFillColorPanel.setBackground(nt.getSelectedFillColor());}

		selectedFillColorPanel.setMinimumSize(PANELSIZE);
		selectedFillColorPanel.setPreferredSize(PANELSIZE);
		selectedFillColorPanel.setMaximumSize(PANELSIZE);

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedFillColorButton,c);
   		panel.add(selectedFillColorButton);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(selectedFillColorPanel,c);
   		panel.add(selectedFillColorPanel);


// normal text color

		yLevel++;

		textColorButton = new JButton("Text Color");
		textColorButton.setPreferredSize(TEXTSIZE);
		textColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color chosenColor = getColor("Node Text Color",textColorPanel.getBackground());
				textColorPanel.setBackground(chosenColor);
				textColorPanel.update(textColorPanel.getGraphics());
			}
		});

		textColorPanel = new JPanel();

		if(nt == null) { textColorPanel.setBackground(Color.black);}
		else { textColorPanel.setBackground(nt.getTextColor());}

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
				Color chosenColor = getColor("Selected Node Text Color",selectedTextColorPanel.getBackground());
				selectedTextColorPanel.setBackground(chosenColor);
				selectedTextColorPanel.update(selectedTextColorPanel.getGraphics());
			}
		});

		selectedTextColorPanel = new JPanel();

		if(nt == null) { selectedTextColorPanel.setBackground(Color.gray);}
		else { selectedTextColorPanel.setBackground(nt.getSelectedTextColor());}

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

		if(nt != null) {
			int index = 0;
			ItemType parent = nt.getParent();
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
			if(NodeType.withLabel(label) != null) {
				JOptionPane.showMessageDialog(parentPanel, "That label already exists.","Error",JOptionPane.PLAIN_MESSAGE);
				return;
			}
			nt = new NodeType(label);
		}

		NodeType parent = null;
		if(!typeList.isSelectionEmpty()) {
			int index = typeList.getSelectedIndex();
			String parentText = (String)indentedTypeLabels.get(index);
			parentText = parentText.trim();
			if(!parentText.equals("")) {
				parent = NodeType.withLabel(parentText);
			}
		}

		if(!nt.setParent(parent)) {
			JOptionPane.showMessageDialog(parentPanel, "Invalid Parent, loop caused in parent heirarchy.","Error",JOptionPane.PLAIN_MESSAGE);
			return;
		}

		nt.setShapeString((String)shapeBox.getSelectedItem());
		nt.setBorderColor(borderColorPanel.getBackground());
		nt.setFillColor(fillColorPanel.getBackground());
		nt.setStroke(new BasicStroke(Float.parseFloat(strokeField.getText())));
		nt.setWidth((int)(Double.parseDouble(widthField.getText())));
		nt.setHeight((int)(Double.parseDouble(heightField.getText())));
		nt.setSelectedStroke(new BasicStroke(Float.parseFloat(selectedStrokeField.getText())));
		nt.setSelectedBorderColor(selectedBorderColorPanel.getBackground());
		nt.setSelectedFillColor(selectedFillColorPanel.getBackground());
		nt.setTextColor(textColorPanel.getBackground());
		nt.setSelectedTextColor(selectedTextColorPanel.getBackground());

		dispose();
	}

	public void cancelButton(ActionEvent event) {

		dispose();
	}



}



