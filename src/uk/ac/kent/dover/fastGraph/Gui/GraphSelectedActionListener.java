package uk.ac.kent.dover.fastGraph.Gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JTextField;

public class GraphSelectedActionListener implements ActionListener{
	
	private JList graphList;
	private JTextField graphOneTextField;
	
	/**
	 * @param graphList
	 * @param graphOneTextField
	 */
	public GraphSelectedActionListener(JList graphList, JTextField graphOneTextField) {
		this.graphList = graphList;
		this.graphOneTextField = graphOneTextField;
	}


	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String selectedGraph = (String) graphList.getSelectedValue();
		graphOneTextField.setText(selectedGraph);
	}

}
