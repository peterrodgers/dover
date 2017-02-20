package uk.ac.kent.displayGraph.display;

import java.awt.*;

import uk.ac.kent.displayGraph.*;


/** Example of using Graph Panel */
public class GraphDisplay {

	public static void main(String[] args) {

		NodeType top = new NodeType("top");
		top.setShapeString("Rectangle");
		top.setFillColor(Color.red);
		top.setBorderColor(Color.blue);
		top.setWidth(30);
		top.setHeight(20);

		NodeType small = new NodeType("small");
		small.setShapeString("Rectangle");
		small.setWidth(15);
		small.setHeight(15);

		EdgeType red = new EdgeType("red");
		red.setLineColor(Color.red);
		red.setSelectedLineColor(Color.gray);
		red.setPriority(1020);

		EdgeType blue = new EdgeType("blue");
		blue.setLineColor(Color.blue);
		blue.setSelectedLineColor(Color.gray);
		blue.setPriority(1019);

		Graph graph = new Graph("Display");

/*
		Node s1 = new Node("small1",small,new Point(100,100));
		Node s2 = new Node("small2",small,new Point(150,400));
		Node s3 = new Node("small3",small,new Point(400,100));
		Node s4 = new Node("small4",small,new Point(400,400));
		Node s5 = new Node("small5",small,new Point(260,220));
		Node s6 = new Node("top1",top,new Point(220,260));
		Node s7 = new Node("top2",top,new Point(300,300));
		Node s8 = new Node("top3",top,new Point(220,300));

		graph.addNode(s1);
		graph.addNode(s2);
		graph.addNode(s3);
		graph.addNode(s4);
		graph.addNode(s5);
		graph.addNode(s6);
		graph.addNode(s7);
		graph.addNode(s8);

		Edge es1 = new Edge(s1,s5);
		Edge es2 = new Edge(s1,s6);
		Edge es3 = new Edge(s2,s7);
		Edge es4 = new Edge(s3,s8);
		Edge es5 = new Edge(s4,s8);
		Edge es6 = new Edge(s3,s6);
		Edge es7 = new Edge(s2,s6);
		Edge es8 = new Edge(s2,s1);

		es1.getEdgeBends().add(new Point(120,80));

		es5.getEdgeBends().add(new Point(140,150));
		es5.getEdgeBends().add(new Point(200,150));
		es5.getEdgeBends().add(new Point(70,360));

		es2.getEdgeBends().add(new Point(100,260));

		graph.addEdge(es1);
		graph.addEdge(es2);
		graph.addEdge(es3);
		graph.addEdge(es4);
		graph.addEdge(es5);
		graph.addEdge(es6);
		graph.addEdge(es7);
		graph.addEdge(es8);
*/

/*

		Node n1 = new Node("a",new Point(30,30));
		Node n2 = new Node("b",new Point(80,80));
		Node n3 = new Node("c",new Point(150,120));
		Node n4 = new Node("d",new Point(110,220));
		Node n5 = new Node("e",new Point(250,250));
		Node n6 = new Node("f",new Point(70,110));
		Node n7 = new Node("g",new Point(40,110));
		Edge e1 = new Edge(n1,n2);
		Edge e2 = new Edge(n3,n2);
		Edge e3 = new Edge(n1,n4);
		Edge e4 = new Edge(n3,n1);
		Edge e5 = new Edge(n3,n4);
		Edge e6 = new Edge(n4,n6);
		Edge e7 = new Edge(n5,n2);
		Edge e8 = new Edge(n6,n2);
		Edge e9 = new Edge(n6,n7);
		Edge e10 = new Edge(n7,n1);

		e1.setLabel("5.4");
		e2.setLabel("0.56");
		e3.setLabel("3.0");
		e3.setLabel("the fourth edge");

		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);

		graph.addNode(n5);
		graph.addNode(n6);
		graph.addNode(n7);

		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		graph.addEdge(e6);
		graph.addEdge(e7);
		graph.addEdge(e8);
		graph.addEdge(e9);
		graph.addEdge(e10);

		Edge eX1 = new Edge(n3,s2);
		Edge eX2 = new Edge(n6,s1);

		eX1.setLabel("1.5");
		eX2.setLabel("12");

		graph.addEdge(eX1);

		EdgeType et = new EdgeType("red");
		et.setStroke(new BasicStroke(1.0f));
		et.setLineColor(Color.red);
  		et.setTextColor(Color.red);

		eX2.setType(et);

		graph.addEdge(eX2);
*/
/*

		graph.generateRandomGraph(200,600);
		while(!graph.connected()) {
			graph.generateRandomGraph(200,600);
		}

*/
		new GraphWindow(graph,true);
	}

}



