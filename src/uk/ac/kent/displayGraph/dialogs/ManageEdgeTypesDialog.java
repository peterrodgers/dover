package uk.ac.kent.displayGraph.dialogs;

import javax.swing.*;
import javax.swing.border.*;

import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.GraphPanel;
import uk.ac.kent.displayGraph.comparators.EdgeTypePriorityComparator;

import java.util.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Creates a manage edge types dialog.
 *
 * @author Peter Rodgers
 */
public class ManageEdgeTypesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JPanel parentPanel;
	protected Frame containerFrame;
	protected ArrayList<EdgeType> allTypes;
	protected Vector<String> typeLabels;

	protected JPanel panel;
	protected JList<String> typeList;
	protected JButton editButton;
	protected JButton newButton;
	protected JButton promoteButton;
	protected JButton demoteButton;
	protected JButton finishedButton;

	protected JPanel listPanel;
	protected JPanel buttonPanel;

/** Edge list must have at least one element */
	public ManageEdgeTypesDialog(JPanel inPanel, Frame inContainerFrame) {
		super(inContainerFrame,"Manage Edge Types",true);

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

		allTypes = new ArrayList<EdgeType>(EdgeType.getExistingTypes());
		EdgeTypePriorityComparator eComp = new EdgeTypePriorityComparator();
		Collections.sort(allTypes,eComp);

		typeLabels = new Vector<String>();
		for(EdgeType t : allTypes) {
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


		promoteButton = new JButton("Promote ^");
		promoteButton.setPreferredSize(GraphPanel.LARGEBUTTONSIZE);
		promoteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				promoteEdgeType();

			}
		});

		demoteButton = new JButton("Demote v");
		demoteButton.setPreferredSize(GraphPanel.LARGEBUTTONSIZE);
		demoteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				demoteEdgeType();
			}
		});

		editButton = new JButton("Edit");
		editButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editEdgeType();

			}
		});

		newButton = new JButton("New");
		newButton.setPreferredSize(GraphPanel.BUTTONSIZE);
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newEdgeType();
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
		c.fill = GridBagConstraints.VERTICAL;
		gridbag.setConstraints(scrollBar,c);
		panel.add(scrollBar);
 
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.insets = externalPadding;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(promoteButton,c);
   		panel.add(promoteButton);

		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(demoteButton,c);
   		panel.add(demoteButton);

		c.gridx = 0;
		c.gridy = 2;
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


	public void promoteEdgeType() {
		if(typeList.isSelectionEmpty()) {
			return;
		}
		int index = typeList.getSelectedIndex();
		if(index == 0) {
			return;
		}
		EdgeType selectedType = (EdgeType)allTypes.get(index);
		EdgeType aboveType = (EdgeType)allTypes.get(index-1);
		int selectedPriority = selectedType.getPriority();
		int abovePriority = aboveType.getPriority();

		selectedType.setPriority(abovePriority);
		aboveType.setPriority(selectedPriority);

		setupTypeLabels();
		typeList.setListData(typeLabels);
		typeList.setSelectedIndex(index-1);

	}


	public void demoteEdgeType() {
		if(typeList.isSelectionEmpty()) {
			return;
		}
		int index = typeList.getSelectedIndex();
		int lastIndex = typeList.getModel().getSize()-1;
		if(index == lastIndex) {
			return;
		}
		EdgeType selectedType = (EdgeType)allTypes.get(index);
		EdgeType belowType = (EdgeType)allTypes.get(index+1);
		int selectedPriority = selectedType.getPriority();
		int belowPriority = belowType.getPriority();

		selectedType.setPriority(belowPriority);
		belowType.setPriority(selectedPriority);

		setupTypeLabels();
		typeList.setListData(typeLabels);
		typeList.setSelectedIndex(index+1);
	}


	public void editEdgeType() {

		EdgeType theType = null;
		if(typeList.isSelectionEmpty()) {
			return;
		}

		int index = typeList.getSelectedIndex();
		theType = (EdgeType)allTypes.get(index);

		new EditEdgeTypeDialog(this,parentPanel,containerFrame,theType,false,"Editing Type "+theType.getLabel());

		setupTypeLabels();
		typeList.setListData(typeLabels);
	}



	public void newEdgeType() {
		if(typeList.isSelectionEmpty()) {
			new EditEdgeTypeDialog(this,parentPanel,containerFrame,null,true,"New Edge Type");
		} else {
			int index = typeList.getSelectedIndex();
			EdgeType theType = (EdgeType)allTypes.get(index);
			new EditEdgeTypeDialog(this,parentPanel,containerFrame,theType,true,"New Edge Type");
		}

		setupTypeLabels();
		typeList.setListData(typeLabels);
	}

}



