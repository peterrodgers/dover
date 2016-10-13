package uk.ac.kent.displayGraph.utilities;


import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import uk.ac.kent.displayGraph.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class GraphUtilityOccludeGraph extends GraphUtility {


	protected int vvLimit = 5;
	protected int veLimit = 3;

	protected int vvAmount= 2;
	protected int veAmount = 1;

	protected static Random r = new Random();
	protected static int randomStartX = 50;
	protected static int randomStartY = 50;
	protected static int randomWidth = 400;
	protected static int randomHeight = 400;

	JFrame frame;
	JPanel panel;
	JTextField vvLimitField;
	JTextField veLimitField;
	JTextField vvAmountField;
	JTextField veAmountField;
	JButton okButton;
	
/** Trivial constructor. */
	public GraphUtilityOccludeGraph() {
		super(KeyEvent.VK_6,"Create Random Graph");
	}

/** Trivial constructor. */
	public GraphUtilityOccludeGraph(int key, String s) {
		super(key,s);
	}

	public GraphUtilityOccludeGraph(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

	public void apply() {
//getGraph().generateRandomGraph(10,20,false);
//getGraph().randomizeNodePoints(new Point(50,50),500,500);
		createFrame();

//		modify_for_occlusion(getGraph(),vvLimit,veLimit,vvAmount,veAmount);
	}
	
	protected void createFrame() {

		frame = new JFrame("Occlude Graph");
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();

		panel.setLayout(gridbag);

		addWidgets(panel,gridbag);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		JLabel label;

		GridBagConstraints c = new GridBagConstraints();
		
		c.ipadx = 5;
		c.ipady = 5;

		// vv Limit
		vvLimitField = new JTextField(4);
		vvLimitField.setText(Integer.toString(vvLimit));
		label = new JLabel("Allowed VV Overlap: ", SwingConstants.LEFT);

		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(vvLimitField,c);
		widgetPanel.add(vvLimitField);
		vvLimitField.requestFocus();
		
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		

		// ve Limit
		veLimitField = new JTextField(4);
		veLimitField.setText(Integer.toString(veLimit));
		label = new JLabel("Allowed VE Overlap: ", SwingConstants.LEFT);

		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(veLimitField,c);
		widgetPanel.add(veLimitField);
		veLimitField.requestFocus();
		
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);

		// vv Amount
		vvAmountField = new JTextField(4);
		vvAmountField.setText(Integer.toString(vvAmount));
		label = new JLabel("Amount of VV Occlusion: ", SwingConstants.LEFT);

		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(vvAmountField,c);
		widgetPanel.add(vvAmountField);
		vvAmountField.requestFocus();
		
		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);

		// ve Amount
		veAmountField = new JTextField(4);
		veAmountField.setText(Integer.toString(veAmount));
		label = new JLabel("Amount of VE Occlusion: ", SwingConstants.LEFT);

		c.gridx = 1;
		c.gridy = 3;
		gridbag.setConstraints(veAmountField,c);
		widgetPanel.add(veAmountField);
		veAmountField.requestFocus();
		
		c.gridx = 0;
		c.gridy = 3;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		
		
		okButton = new JButton("OK");
		frame.getRootPane().setDefaultButton(okButton);



		c.gridx = 0;
		c.gridy = 4;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);
		getGraphPanel().getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okButton(event);
			}
		});
	}


	protected void okButton(ActionEvent event) {
		
		int invvLimit = 0;
		int inveLimit = 0;
		int invvAmount = 0;
		int inveAmount = 0;
		try {
			invvLimit = Integer.parseInt(vvLimitField.getText());
		} catch(NumberFormatException e) {
			String message = "Cant parse "+vvLimitField.getText()+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		try {
			inveLimit = Integer.parseInt(veLimitField.getText());
		} catch(NumberFormatException e) {
			String message = "Cant parse "+veLimitField.getText()+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		try {
			invvAmount = Integer.parseInt(vvAmountField.getText());
		} catch(NumberFormatException e) {
			String message = "Cant parse "+vvAmountField.getText()+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		try {
			inveAmount = Integer.parseInt(veAmountField.getText());
		} catch(NumberFormatException e) {
			String message = "Cant parse "+vvAmountField.getText()+" as an integer: "+e;
			JOptionPane.showMessageDialog(null, message, "Occlusion", JOptionPane.INFORMATION_MESSAGE);
//			System.out.println(message);
			return;
		}
		
		vvLimit = invvLimit;
		veLimit = inveLimit;
		veAmount = inveAmount;
		vvAmount = invvAmount;

		modify_for_occlusion(getGraph(),vvLimit,veLimit,vvAmount,veAmount);
		
		GraphPanel gp = getGraphPanel();
		gp.getSelection().clear();
		gp.requestFocus();
		gp.update(gp.getGraphics());

		frame.dispose();
	}


	/** keeps moving nodes around till the occlusion count is right */
	public static void modify_for_occlusion(Graph graph, int vvLimit, int veLimit, int vv_amount, int ve_amount) {
		final int allowedIterations = 10000;
		int iterationCount = 0;
		int vv_count = -1;
		int ve_count = -1;
System.out.println("current vv occlusion "+vv_count+" desired "+vv_amount);
System.out.println("current ve occlusion "+ve_count+" desired "+ve_amount);
		
		while(vv_count != vv_amount || ve_count != ve_amount) {
			if(iterationCount >= allowedIterations) {
				JOptionPane.showMessageDialog(null, "Generating occlusion failed after "+iterationCount+" iterations.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			iterationCount++;
			vv_count = GraphUtilityCountOcclusion.get_vv_count(graph,vvLimit);

			ve_count = GraphUtilityCountOcclusion.get_ve_count(graph,vvLimit,veLimit);
System.out.println("current vv occlusion "+vv_count+" desired "+vv_amount);
System.out.println("current ve occlusion "+ve_count+" desired "+ve_amount);
			
			if(vv_count > vv_amount) {
				ArrayList<Node> vvList = GraphUtilityCountOcclusion.get_vv_list(graph,vvLimit);
				Node vv_occluding_v = (Node)vvList.get(r.nextInt(vvList.size()));
System.out.println("Excess vv: moving node "+vv_occluding_v+": "+vv_occluding_v.getX()+","+vv_occluding_v.getX());
				vv_occluding_v.setX(randomStartX+r.nextInt(randomWidth));
				vv_occluding_v.setY(randomStartY+r.nextInt(randomHeight));
System.out.println("to "+vv_occluding_v.getX()+","+vv_occluding_v.getY());
//				continue;
			}
			vv_count = GraphUtilityCountOcclusion.get_vv_count(graph,vvLimit);
			if(vv_count < vv_amount) {
				add_vv_occlusion(graph,vvLimit,veLimit);
//				continue;
			}
			ve_count = GraphUtilityCountOcclusion.get_ve_count(graph,vvLimit,veLimit);
			if(ve_count > ve_amount) {
				ArrayList<Node> veList = GraphUtilityCountOcclusion.get_ve_list(graph,vvLimit,veLimit);
				Node ve_occluding_v = (Node)veList.get(r.nextInt(veList.size()));
System.out.println("Excess ve: moving node "+ve_occluding_v+": "+ve_occluding_v.getX()+","+ve_occluding_v.getX());
				ve_occluding_v.setX(randomStartX+r.nextInt(randomWidth));
				ve_occluding_v.setY(randomStartY+r.nextInt(randomHeight));
System.out.println("to "+ve_occluding_v.getX()+","+ve_occluding_v.getY());
//				continue;
			}
			ve_count = GraphUtilityCountOcclusion.get_ve_count(graph,vvLimit,veLimit);
			if(ve_count < ve_amount) {
				add_ve_occlusion(graph,vvLimit,veLimit);
//				continue;
			}
/*			
			while(GraphUtilityCountOcclusion.countNoVEOcclusionOverlap(graph,vvLimit,veLimit) > 0) {
				ArrayList noVEList = GraphUtilityCountOcclusion.NoVEOcclusionOverlapList(graph,vvLimit,veLimit);
				Node ve_not_occluding_v = (Node)noVEList.get(0);
				ve_not_occluding_v.setX(randomStartX+r.nextInt(randomWidth));
				ve_not_occluding_v.setY(randomStartY+r.nextInt(randomHeight));
			}
*/
		}
	}
	
	/**
	 * moves a random non-occluded node over another random node
	 * both nodes might be the same, which results in
	 * a node being moved a bit
	 */
	public static void add_vv_occlusion(Graph graph, int vv_limit, int ve_limit) {
		ArrayList<Node> vertices = graph.getNodes();
		if(vertices.size() == 0) {
			return;
		}
		Node v1 = (Node)vertices.get(r.nextInt(vertices.size()));
		Node v2 = (Node)vertices.get(r.nextInt(vertices.size()));
		if(!GraphUtilityCountOcclusion.test_vv_occlusion(v1,v2,vv_limit)) {
			if(v1.connectingNodes().contains(v2)) {
				return;
			}
System.out.println("To little vv: moving node "+v1+":"+v1.getX()+","+v1.getY());
			int randomX = vv_limit-randrange(0,vv_limit*2+1);
			int randomY = vv_limit-randrange(0,vv_limit*2+1);

			int oldX = v1.getX();
			int oldY = v1.getY();
			
			int oldVECount = GraphUtilityCountOcclusion.get_ve_count(graph,vv_limit,ve_limit);
			int oldVVCount = GraphUtilityCountOcclusion.get_vv_count(graph,vv_limit);
			
			v1.setX(v2.getX()+randomX);
			v1.setY(v2.getY()+randomY);
			
			int newVECount = GraphUtilityCountOcclusion.get_ve_count(graph,vv_limit,ve_limit);
			int newVVCount = GraphUtilityCountOcclusion.get_vv_count(graph,vv_limit);
			
			if(oldVVCount+1 != newVVCount || oldVECount != newVECount) {
				v1.setX(oldX);
				v1.setY(oldY);
System.out.println("Tried to move "+v1+" but failed to increase vv occlusion by 1 and retain ve occlusion");
			}
System.out.println("to "+v1.getX()+","+v1.getY()+" over node "+v2+":"+v2.getX()+","+v2.getY());
		}
	}
	
	/**
	 * moves a random non-occluded node over a random edge
	 * that the node is not attached to
	 */
	public static void add_ve_occlusion(Graph graph, int vv_limit, int ve_limit) {
		ArrayList<Node> vertices = graph.getNodes();
		if(vertices.size() == 0) {
			return;
		}
		Node v = (Node)vertices.get(r.nextInt(vertices.size()));
		for(Edge e1 : graph.getEdges()) {
			if(GraphUtilityCountOcclusion.test_ve_occlusion(v,e1,graph,vv_limit,ve_limit)) {
				return;
			}
		}
		ArrayList<Edge> edges = graph.getEdges();
		if(edges.size() == 0) {
			return;
		}
		Edge e = (Edge)edges.get(r.nextInt(edges.size()));

		Node v1 = e.getFrom();
		Node v2 = e.getTo();
		
		if(v1 == v) {
			return;
		}
		if(v2 == v) {
			return;
		}
		
		int x1 = v1.getX();
		int y1 = v1.getY();
		int x2 = v2.getX();
		int y2 = v2.getY();

		double line_length = GraphUtilityCountOcclusion.distance_between_points(x1,y1,x2,y2);

		if (line_length == 0) {
			return;
		}

		int start_range = ve_limit;
		int end_range = (int)line_length-ve_limit*2;
		if(start_range>=end_range) {
			return;
		}
		
		int move_distance = randrange(start_range,end_range);
		
		if(randrange(0,2) == 1) {
			move_distance = -move_distance;
		}
		double x_mult = (x2-x1)/line_length;
		double y_mult = (y2-y1)/line_length;
		
System.out.println("To little ve: moving node "+v+":"+v.getX()+","+v.getY());
		int oldX = v.getX();
		int oldY = v.getY();
		
		int oldVECount = GraphUtilityCountOcclusion.get_ve_count(graph,vv_limit,ve_limit);
		int oldVVCount = GraphUtilityCountOcclusion.get_vv_count(graph,vv_limit);
		
		v.setX((int)(x1+move_distance*x_mult));
		v.setY((int)(y1+move_distance*y_mult));
		
		int randomX = ve_limit-randrange(0,ve_limit*2+1);
		int randomY = ve_limit-randrange(0,ve_limit*2+1);
		v.setX(v.getX()+randomX);
		v.setY(v.getY()+randomY);

		int newVECount = GraphUtilityCountOcclusion.get_ve_count(graph,vv_limit,ve_limit);
		int newVVCount = GraphUtilityCountOcclusion.get_vv_count(graph,vv_limit);
		
		if(oldVECount+1 != newVECount || oldVVCount != newVVCount) {
			v.setX(oldX);
			v.setY(oldY);
System.out.println("Tried to move "+v+" but failed to increase ve occlusion by 1 and retain VV occlusion");
		}
		
System.out.println("to "+v.getX()+","+v.getY()+" between nodes "+v1+":"+x1+","+y2+" "+v2+":"+x2+","+y2);
	}

	public static int randrange(int start, int end) {
		int ret = r.nextInt(end-start)+start;
		return ret;
	}

}
