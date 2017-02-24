package uk.ac.kent.displayGraph.dialogs;

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
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.NodeType;


/**
 * Creates a manage node types dialog.
 *
 * @author Peter Rodgers
 */
public class ManageNodeTypesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	protected JPanel parentPanel;
	protected Frame containerFrame;
	protected ArrayList<NodeType> allTypes;
	protected Vector<String> typeLabels;

	protected JPanel panel;
	protected JList<String> typeList;
	protected JButton editButton;
	protected JButton newButton;
	protected JButton finishedButton;

	protected JPanel listPanel;
	protected JPanel buttonPanel;

/** Node list must have at least one element */
	public ManageNodeTypesDialog(JPanel inPanel, Frame inContainerFrame) {
		super(inContainerFrame,"Manage Node Types",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(inContainerFrame);

		parentPanel = inPanel;
		containerFrame = inContainerFrame;

		listPanel = new JPanel();
		buttonPanel = new JPanel();

		Border etchedBorder = BorderFactory.createEtchedBorder();
		Border spaceBorder1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border spaceBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border compoundBorder = BorderFactory.createCompoundBorder(etchedBorder,spaceBorder1);
		Border panelBorder = BorderFactory.createCompoundBorder(spaceBorder2,compoundBorder);

		listPanel.setBorder(panelBorder);
		buttonPanel.setBorder(spaceBorder1);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		getContentPane().setLayout(gridbag);

		c.ipadx = 2;
		c.ipady = 2;

		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(listPanel,c);
		getContentPane().add(listPanel);

		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(buttonPanel,c);
		getContentPane().add(buttonPanel);

		setupTypeLabels();
		setupList(listPanel);
		setupButtons(buttonPanel);

		pack();

		setVisible(true);
	}



	protected void setupTypeLabels() {

		allTypes = new ArrayList<NodeType>(NodeType.getExistingTypes());

		typeLabels = new Vector<String>();
		for(NodeType t : allTypes){
			typeLabels.add(t.getLabel());
		}
	}


	protected void setupList(JPanel panel) {

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);

		typeList = new JList<String>(typeLabels);
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollBar = new JScrollPane(typeList);
		scrollBar.setPreferredSize(new Dimension(150, 100));
		scrollBar.setBorder(lineBorder);

		editButton = new JButton("Edit");
		editButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editNodeType();
			}
		});

		newButton = new JButton("New");
		newButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newNodeType();
			}
		});

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		Insets externalPadding = new Insets(3,3,3,3);

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.VERTICAL;
		gridbag.setConstraints(scrollBar,c);
		panel.add(scrollBar);
 
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = externalPadding;
		gridbag.setConstraints(editButton,c);
   		panel.add(editButton);

		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(newButton,c);
   		panel.add(newButton);

	}


	protected void setupButtons(JPanel panel) {

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);

		finishedButton = new JButton("Finished");
		getRootPane().setDefaultButton(finishedButton);
		finishedButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		finishedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				finishedButton(event);
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(finishedButton,c);
   		panel.add(finishedButton);

	}


	public void actionPerformed(ActionEvent event) {
	}


	public void finishedButton(ActionEvent event) {
		dispose();
	}



	public void editNodeType() {

		NodeType theType = null;
		if(typeList.isSelectionEmpty()) {
			return;
		}

		int index = typeList.getSelectedIndex();
		theType = (NodeType)allTypes.get(index);

		new EditNodeTypeDialog(this,parentPanel,containerFrame,theType,false,"Editing Type "+theType.getLabel());

		setupTypeLabels();
		typeList.setListData(typeLabels);

	}



	public void newNodeType() {

		if(typeList.isSelectionEmpty()) {
			new EditNodeTypeDialog(this,parentPanel,containerFrame,null,true,"New Node Type");
		} else {
			int index = typeList.getSelectedIndex();
			NodeType theType = (NodeType)allTypes.get(index);
			new EditNodeTypeDialog(this,parentPanel,containerFrame,theType,true,"New Node Type");
		}

		setupTypeLabels();
		typeList.setListData(typeLabels);

	}

}



