package uk.ac.kent.displayGraph.test;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import uk.ac.kent.displayGraph.Edge;
import uk.ac.kent.displayGraph.EdgeType;
import uk.ac.kent.displayGraph.Graph;
import uk.ac.kent.displayGraph.Node;
import uk.ac.kent.displayGraph.NodeType;


public class Test {
	
	public static void main(String[] args) {
		System.out.println("Start Graph Tests");
		testA();
//		testA1();
		testB();
//		testC();
//		testD();
//		testE();
		testF();
		System.out.println("\nEnd Graph Tests");

	}

	public static void testA() {

		System.out.println("Test 1 START: Basic Node and edge methods");

		// Graph creation and deletion
		Graph g1 = new Graph();
		Node nX = new Node("nY");
		Node nY = new Node("nX");
		if(!g1.addNode(nX)) {
			System.out.println("Test1.0.1 Fail");
		}
		if(!g1.addNode(nY)) {
			System.out.println("Test1.0.2 Fail");
		}
		if(g1.addNode(nY)) {
			System.out.println("Test1.0.3 Fail");
		}
		Edge eX = new Edge(nX,nY);
		if(!g1.addEdge(eX)) {
			System.out.println("Test1.0.4 Fail");
		}
		if(g1.addEdge(eX)) {
			System.out.println("Test1.0.5 Fail");
		}

		// test the consistency checker
		if(!g1.consistent()) {
			System.out.println("Test1.0.6 Fail");
		}

		ArrayList<Edge> nYTo = nY.getEdgesTo();
		nYTo.remove(eX);
		if(g1.consistent()) {
			System.out.println("Test1.0.7 Fail");
		}
		nYTo.add(eX);
		if(!g1.consistent()) {
			System.out.println("Test1.0.8 Fail");
		}
		ArrayList<Edge> g1Edges = g1.getEdges();
		g1Edges.remove(eX);
		if(g1.consistent()) {
			System.out.println("Test1.0.9 Fail");
		}
		g1Edges.add(eX);
		if(!g1.consistent()) {
			System.out.println("Test1.0.10 Fail");
		}

		Node nOutside = new Node();
		eX.setFrom(nOutside);
		if(g1.consistent()) {
			System.out.println("Test1.0.11 Fail");
		}
		eX.setFrom(nX);
		if(!g1.consistent()) {
			System.out.println("Test1.0.12 Fail");
		}

		g1 = new Graph("G");
		if(!g1.getLabel().equals("G")) {
			System.out.println("Test1.0.13 Fail");
		}
		g1.setLabel("H");
		if(!g1.getLabel().equals("H")) {
			System.out.println("Test1.0.14 Fail");
		}
		if(!g1.consistent()) {
			System.out.println("Test1.0.15 Fail");
		}


		/* first real test graph
		
					   ^e9v			  
		 n1  e1> n2 e2> n3		  n6
		 e3v	e4^	e5ve6ve7^
		  ---n4--- <e8 n5
		*/
		
		// Graph Object creation

		g1.clear();
		Node n1 = new Node("n1");
		if(!g1.addNode(n1)) {
			System.out.println("Test1.1.1 Fail");
		}
		Node n2 = new Node("n2");
		if(!g1.addNode(n2)) {
			System.out.println("Test1.1.2 Fail");
		}
		Node n3 = new Node();
		if(!g1.addNode(n3)) {
			System.out.println("Test1.1.3 Fail");
		}
		Node n4 = new Node("n4");
		if(!g1.addNode(n4)) {
			System.out.println("Test1.1.4 Fail");
		}
		Node n5 = new Node("test");
		if(!g1.addNode(n5)) {
			System.out.println("Test1.1.5 Fail");
		}
		Node n6 = new Node("n6");
		if(!g1.addNode(n6)) {
			System.out.println("Test1.1.6 Fail");
		}

		Edge e1 = new Edge(n6,n6);
		if(!g1.addEdge(e1)) {
			System.out.println("Test1.1.7 Fail");
		}
		Edge e2 = new Edge(n2,n3);
		if(!g1.addEdge(e2)) {
			System.out.println("Test1.1.8 Fail");
		}
		Edge e3 = new Edge(n1,n4);
		if(!g1.addEdge(e3)) {
			System.out.println("Test1.1.9 Fail");
		}
		Edge e4 = new Edge(n4,n2);
		if(!g1.addEdge(e4)) {
			System.out.println("Test1.1.10 Fail");
		}
		Edge e5 = new Edge(n3,n5);
		if(!g1.addEdge(e5)) {
			System.out.println("Test1.1.11 Fail");
		}
		Edge e6 = new Edge(n3,n5);
		if(!g1.addEdge(e6)) {
			System.out.println("Test1.1.12 Fail");
		}
		Edge e7 = new Edge(n3,n5);
		if(!g1.addEdge(e7)) {
			System.out.println("Test1.1.13 Fail");
		}
		Edge e8 = new Edge(n1,n1);
		if(!g1.addEdge(e8)) {
			System.out.println("Test1.1.14 Fail");
		}
		Edge e9 = new Edge(n3,n3);
		if(!g1.addEdge(e9)) {
			System.out.println("Test1.1.15 Fail");
		}


		// oposite end test
		if (e1.getOppositeEnd(n6) != n6) {
			System.out.println("Test1.1.16 Fail");
		}
		if (e2.getOppositeEnd(n2) != n3) {
			System.out.println("Test1.1.17 Fail");
		}
		if (e2.getOppositeEnd(n3) != n2) {
 			System.out.println("Test1.1.18 Fail");
		}
		if (e2.getOppositeEnd(n6) != null) {
			System.out.println("Test1.1.19 Fail");
		}


		// changing the connections of edges
		e1.setFromTo(n4,n4);
		e1.setFrom(n5);
		e1.setTo(n5);
		e1.setFromTo(n3,n6);
		e1.setFromTo(n1,n2);
		e8.setFrom(n5);
		e8.setTo(n4);
		e7.reverse();
		e9.reverse();
		
		// resetting labels
		n3.setLabel("n3");
		n5.setLabel("n5");

		Node tempTo = e1.getTo();
		Node tempFrom = e1.getFrom();

		e1.reverse();
		if(e1.getTo() != tempFrom) {
			System.out.println("Test1.1.20 Fail");
		}

		if(e1.getFrom() != tempTo) {
			System.out.println("Test1.1.21 Fail");
		}

		e1.reverse();

		if(e1.getTo() != tempTo) {
			System.out.println("Test1.1.22 Fail");
		}

		if(e1.getFrom() != tempFrom) {
			System.out.println("Test1.1.23 Fail");
		}

		if(!g1.consistent()) {
			System.out.println("Test1.1.24 Fail");
		}


		// Node connectivity testing
		ArrayList<Node> testN = new ArrayList<Node>();

		if(!n6.connectingNodes().equals(testN)) {
			System.out.println("Test1.2.1 Fail");
		}
		if(!n6.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.2.2 Fail");
		}
		if(!n6.connectingEdges().equals(testN)) {
			System.out.println("Test1.2.4 Fail");
		}

		testN = new ArrayList<Node>();
		testN.add(n5);
		testN.add(n3);
		testN.add(n2);

		if(!n3.connectingNodes().equals(testN)) {
			System.out.println("Test1.2.5 Fail");
		}

		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.2.7 Fail");
		}


		// Node visited flag testing

		n2.setVisited(true);

		if(!n3.connectingNodes().equals(testN)) {
			System.out.println("Test1.3.1 Fail");
		}
		testN.remove(n2);
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.3.3 Fail");
		}

		g1.setNodesVisited(true);
		testN = new ArrayList<Node>();

		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.3.4 Fail");
		}

		n2.setVisited(false);
		testN.add(n2);

		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.3.5 Fail");
		}

		testN.remove(n2);

		n2.setVisited(true);
		testN = new ArrayList<Node>();
		if(!n3.unvisitedConnectingNodes().equals(testN)) {
			System.out.println("Test1.3.6 Fail");
		}

		testN = new ArrayList<Node>();
		testN.add(n5);
		testN.add(n3);
		testN.add(n2);
		if(!n3.connectingNodes().equals(testN)) {
			System.out.println("Test1.3.7 Fail");
		}
		g1.setNodesVisited();


		// Edge visited flag testing
		
		ArrayList<Edge> testE = new ArrayList<Edge>();

		testE = new ArrayList<Edge>();
		testE.add(e3);
		testE.add(e1);
		if(!n1.connectingEdges().equals(testE)) {
			System.out.println("Test1.5.1 Fail");
		}
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			System.out.println("Test1.5.2 Fail");
		}

		testE.remove(e1);
		e1.setVisited(true);
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			System.out.println("Test1.5.3 Fail");
		}
		e1.setVisited(false);
		e2.setVisited(false);

		g1.setEdgesVisited(true);
		testE = new ArrayList<Edge>();
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			System.out.println("Test1.5.4 Fail");
		}
		e1.setVisited(true);
		e2.setVisited(true);
		g1.setEdgesVisited(false);
		testE = new ArrayList<Edge>();
		testE.add(e3);
		testE.add(e1);
		if(!n1.connectingEdges().equals(testE)) {
			System.out.println("Test1.5.5 Fail");
		}
		if(!n1.unvisitedConnectingEdges().equals(testE)) {
			System.out.println("Test1.5.6 Fail");
		}

		System.out.print("Test 1 END");


		// test dynamic graph stuff
		System.out.println(" | Test 2 START: Object removal");
		Node nd1 = new Node("nd1");
		Node nd2 = new Node("nd2");
		Node nd3 = new Node("nd3");

		Graph g2 = new Graph("g2");
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);

		Edge ed1 = new Edge(nd1,nd2);
		Edge ed2 = new Edge(nd1,nd2);
		Edge ed3 = new Edge(nd2,nd1);
		Edge ed4 = new Edge(nd1,nd1);
		Edge ed5 = new Edge(nd3,nd2);
		g2.addEdge(ed1);
		g2.addEdge(ed2);
		g2.addEdge(ed3);
		g2.addEdge(ed4);
		g2.addEdge(ed5);
		
		if((g2.getEdge(nd1,nd3)!= null)) {System.out.println("Test2.0.1 Fail");}
		if((g2.getEdge(nd3,nd1)!= null)) {System.out.println("Test2.0.2 Fail");}
		if((g2.getEdge(nd2,nd3)!= ed5)) {System.out.println("Test2.0.3 Fail");}
		if((g2.getEdge(nd3,nd2)!= ed5)) {System.out.println("Test2.0.4 Fail");}
		if((g2.getEdge(nd1,nd1)!= ed4)) {System.out.println("Test2.0.5 Fail");}
		if((g2.getEdge(nd2,nd2)!= null)) {System.out.println("Test2.0.6 Fail");}


		g2.removeNode("nd1");

		ArrayList<Node> testNodeAL;
		ArrayList<Edge> testEdgeAL;
		testEdgeAL = new ArrayList<Edge>();
		testEdgeAL.add(ed5);
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.1 Fail");
		}
		g2.addNode(nd3);
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.2 Fail");
		}
		g2.removeNode(nd2);
		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.3 Fail");
		}
		testNodeAL = new ArrayList<Node>();
		testNodeAL.add(nd3);
		if(!g2.getNodes().equals(testNodeAL)) {
			System.out.println("Test2.4 Fail");
		}
		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.5 Fail");
		}

		if(!g2.consistent()) {
			System.out.println("Test2.5.0 Fail");
		}


		g2.clear();

		Node dyNode1 = new Node("dyNode1");
		Node dyNode2 = new Node("dyNode2");
		g2.addNode(dyNode1);
		g2.addNode(dyNode2);

		Edge dyEdge1 = new Edge(dyNode1,dyNode2,"dyEdge2");
		g2.addEdge(dyEdge1);

		g2.removeEdge(dyEdge1);

		if(dyEdge1.getFrom() != null) {
			System.out.println("Test2.5.1 Fail");
		}
		if(dyEdge1.getTo() != null) {
			System.out.println("Test2.5.2 Fail");
		}

		ArrayList<Edge> dyTest = new ArrayList<Edge>();

		if(dyNode1.getEdgesFrom().equals(dyTest) == false) {
			System.out.println("Test2.5.3 Fail");
		}
		if(dyNode1.getEdgesTo().equals(dyTest) == false) {
			System.out.println("Test2.5.4 Fail");
		}
		if(dyNode2.getEdgesFrom().equals(dyTest) == false) {
			System.out.println("Test2.5.5 Fail");
		}
		if(dyNode2.getEdgesTo().equals(dyTest) == false) {
			System.out.println("Test2.5.6 Fail");
		}
		if(!g2.consistent()) {
			System.out.println("Test2.5.7 Fail");
		}


		g2.clear();
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);

		ed1 = new Edge(nd1,nd2,"A",1.1);
		ed2 = new Edge(nd1,nd2,"B");
		ed3 = new Edge(nd2,nd1,1);
		ed4 = new Edge(nd1,nd1);
		ed5 = new Edge(nd3,nd2);
		g2.addEdge(ed1);
		g2.addEdge(ed2);
		g2.addEdge(ed3);
		g2.addEdge(ed4);
		g2.addEdge(ed5);


		// edge label and weight testing
		if(!ed1.getLabel().equals("A")) {
			System.out.println("Test2.5.1 Fail");
		}
		ed1.setLabel("C");
		if(!ed1.getLabel().equals("C")) {
			System.out.println("Test2.5.2 Fail");
		}
		if(!ed2.getLabel().equals("B")) {
			System.out.println("Test2.5.3 Fail");
		}
		if(ed1.getWeight()!=1.1) {
			System.out.println("Test2.5.4 Fail");
		}
		ed1.setWeight(2.2);
		if(ed1.getWeight()!=2.2) {
			System.out.println("Test2.5.5 Fail");
		}
		if(ed3.getWeight()!=1) {
			System.out.println("Test2.5.6 Fail");
		}

		g2.removeEdge(ed2);
		testEdgeAL = new ArrayList<Edge>();
		testEdgeAL.add(ed1);
		testEdgeAL.add(ed3);
		testEdgeAL.add(ed4);
		testEdgeAL.add(ed5);
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.6 Fail");
		}
		ArrayList<Node> testNodeAL2;
		
		testNodeAL2 = new ArrayList<Node>();
		testNodeAL2.add(nd1);
		testNodeAL2.add(nd2);
		testNodeAL2.add(nd3);
		if(!g2.getNodes().equals(testNodeAL2)) {
			System.out.println("Test2.7 Fail");
		}

		g2.removeEdge(ed4);

		testEdgeAL.remove(ed4);
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.8 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			System.out.println("Test2.9 Fail");
		}

		ed4 = new Edge(nd3,nd3);
		g2.addEdge(ed4);
		testEdgeAL.add(ed4);
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.10 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			System.out.println("Test2.11 Fail");
		}

		if(!g2.consistent()) {
			System.out.println("Test2.11.0 Fail");
		}

		g2.clear();

		testEdgeAL = new ArrayList<Edge>();
		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.12 Fail");
		}
		if(!g2.getNodes().equals(testEdgeAL)) {
			System.out.println("Test2.13 Fail");
		}

		ed4 = new Edge(nd2,nd1);
		g2.addNode(nd1);
		g2.addNode(nd2);
		g2.addNode(nd3);
		g2.addEdge(ed4);

		g2.removeEdge(ed4);

		if(!g2.getEdges().equals(testEdgeAL)) {
			System.out.println("Test2.14 Fail");
		}
		if(!g2.getNodes().equals(testNodeAL2)) {
			System.out.println("Test2.15 Fail");
		}

		if(!g2.consistent()) {
			System.out.println("Test2.15.0 Fail");
		}

		//score field testing
		Graph gScore = new Graph();

		Node ns1 = new Node("A");
		Node ns2 = new Node();
		Node ns3 = new Node("C");
		Node ns4 = new Node("D");

		gScore.addNode(ns1);
		gScore.addNode(ns2);
		gScore.addNode(ns3);
		gScore.addNode(ns4);

		Edge es1 = new Edge(ns1,ns2,"A",1.1);
		Edge es2 = new Edge(ns4,ns1);
		Edge es3 = new Edge(ns1,ns3);

		gScore.addEdge(es1);
		gScore.addEdge(es2);
		gScore.addEdge(es3);

		if(ns1.getScore() != 0.0) {
			System.out.println("Test2.16.1 Fail");
		}

		if(es1.getScore() != 0.0) {
			System.out.println("Test2.16.2 Fail");
		}

		ns1.setScore(1.3);
		es1.setScore(2.4);

		if(ns1.getScore() != 1.3) {
			System.out.println("Test2.16.3 Fail");
		}

		if(es1.getScore() != 2.4) {
			System.out.println("Test2.16.4 Fail");
		}

		ArrayList<Node> alNodeScore = new ArrayList<Node>();
		ArrayList<Edge> alEdgeScore = new ArrayList<Edge>();
		gScore.setNodesScores(alNodeScore,3.5);
		gScore.setEdgesScores(alEdgeScore,4.6);

		if(ns1.getScore() != 1.3) {
			System.out.println("Test2.16.5 Fail");
		}

		if(es1.getScore() != 2.4) {
			System.out.println("Test2.16.6 Fail");
		}

		alNodeScore.add(ns1);
		alNodeScore.add(ns2);
		gScore.setNodesScores(alNodeScore,5.7);
		alNodeScore.remove(ns1);
		alNodeScore.remove(ns2);
		alEdgeScore.add(es1);
		alEdgeScore.add(es2);
		gScore.setEdgesScores(alEdgeScore,6.8);

		if(ns1.getScore() != 5.7) {
			System.out.println("Test2.16.7 Fail");
		}
		if(ns3.getScore() != 0.0) {
			System.out.println("Test2.16.8 Fail");
		}

		if(es1.getScore() != 6.8) {
			System.out.println("Test2.16.9 Fail");
		}
		if(es3.getScore() != 0.0) {
			System.out.println("Test2.16.10 Fail");
		}

		gScore.setNodesScores(7.9);
		gScore.setEdgesScores(8.01);

		if(ns1.getScore() != 7.9) {
			System.out.println("Test2.16.11 Fail");
		}
		if(ns3.getScore() != 7.9) {
			System.out.println("Test2.16.12 Fail");
		}

		if(es1.getScore() != 8.01) {
			System.out.println("Test2.16.13 Fail");
		}
		if(es3.getScore() != 8.01) {
			System.out.println("Test2.16.14 Fail");
		}

		Graph testChangeGraph = new Graph();

		if(testChangeGraph.moveNodeToEnd(n1)) {
			System.out.println("Test2.17.1 Fail");
		}
		if(testChangeGraph.moveEdgeToEnd(e1)) {
			System.out.println("Test2.17.2 Fail");
		}

		ArrayList<Node> testChangeNodeAL = new ArrayList<Node>();

		Node changeNode1 = new Node("c1");
		testChangeGraph.addNode(changeNode1);

		if(testChangeGraph.moveNodeToEnd(n1)) {
			System.out.println("Test2.17.3 Fail");
		}

		testChangeGraph.moveNodeToEnd(changeNode1);

		testChangeNodeAL = new ArrayList<Node>();
		testChangeNodeAL.add(changeNode1);

		if(!testChangeGraph.getNodes().equals(testChangeNodeAL)) {
			System.out.println("Test2.17.4 Fail");
		}

		Node changeNode2 = new Node("c2");
		Node changeNode3 = new Node("c3");

		Edge changeEdge1 = new Edge(changeNode1,changeNode2);
		Edge changeEdge2 = new Edge(changeNode3,changeNode2);
		Edge changeEdge3 = new Edge(changeNode1,changeNode3);

		testChangeGraph.addNode(changeNode2);
		testChangeGraph.addNode(changeNode3);

		testChangeGraph.addEdge(changeEdge1);
		testChangeGraph.addEdge(changeEdge2);
		testChangeGraph.addEdge(changeEdge3);

		testChangeNodeAL = new ArrayList<Node>();
		testChangeNodeAL.add(changeNode1);
		testChangeNodeAL.add(changeNode3);
		testChangeNodeAL.add(changeNode2);

		testChangeGraph.moveNodeToEnd(changeNode2);

		if(!testChangeGraph.getNodes().equals(testChangeNodeAL)) {
			System.out.println("Test2.17.5 Fail");
		}

		ArrayList<Edge> testChangeEdgeAL = new ArrayList<Edge>();
		testChangeEdgeAL.add(changeEdge2);
		testChangeEdgeAL.add(changeEdge3);
		testChangeEdgeAL.add(changeEdge1);

		testChangeGraph.moveEdgeToEnd(changeEdge1);

		if(!testChangeGraph.getEdges().equals(testChangeEdgeAL)) {
			System.out.println("Test2.17.6 Fail");
		}
		
		n1 = new Node(new Point(0,0));
		n2 = new Node(new Point(3,4));
		e1 = new Edge(n1,n2);
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 5.0) {System.out.println("Test2.18.1 Fail");}
		e1 = new Edge(n2,n1);
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 5.0) {System.out.println("Test2.18.2 Fail");}
		n1 = new Node(new Point(0,0));
		n2 = new Node(new Point(1,1));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,1));
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 2.0) {System.out.println("Test2.18.3 Fail");}
		e1 = new Edge(n2,n1);
		e1.addBend(new Point(0,1));
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 2.0) {System.out.println("Test2.18.4 Fail");}
		n1 = new Node(new Point(0,0));
		n2 = new Node(new Point(5,4));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,1));
		e1.addBend(new Point(1,1));
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 7.0) {System.out.println("Test2.18.5 Fail");}
		e1 = new Edge(n2,n1);
		e1.addBend(new Point(5,3));
		e1.addBend(new Point(4,3));
		if(uk.ac.kent.displayGraph.Util.round(e1.findLength(),2) != 7.0) {System.out.println("Test2.18.6 Fail");}


		System.out.print("Test 2 END");


		System.out.println(" | Test 3 START: Shortest path");

		ArrayList<Node> path = new ArrayList<Node>();
		g1 = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		g1.addNode(n1);
		g1.addNode(n2);
		g1.addNode(n3);
		g1.addNode(n4);
		g1.addNode(n5);
		g1.addAdjacencyEdge("n1","n2");
		g1.addAdjacencyEdge("n4","n2");
		g1.addAdjacencyEdge("n3","n5");
		g1.addAdjacencyEdge("n5","n3");
		g1.addAdjacencyEdge("n3","n5");
		g1.addAdjacencyEdge("n3","n3");
		g1.addAdjacencyEdge("n2","n3");
		g1.addAdjacencyEdge("n5","n4");
		g1.addAdjacencyEdge("n1","n4");
		path.add(n5);
		path.add(n4);
		path.add(n1);
		if(!g1.unweightedShortest(n5,n1).equals(path)) {
			System.out.println("Test3.1 Fail");
		}
		path = new ArrayList<Node>();
		path.add(n3);
		if(!g1.unweightedShortest(n3,n3).equals(path)) {
			System.out.println("Test3.2 Fail");
		}
		if(g1.unweightedShortest(n4,n6) != null) {
			System.out.println("Test3.3 Fail");
		}

		System.out.print("Test 3 END");


		//Adjacency Edge Graph creation stuff

		System.out.println(" | Test 4 START: Adjacency Graph");

		g1 = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		n6 = new Node("n6");
		g1.addNode(n1);
		g1.addNode(n2);
		g1.addNode(n3);
		g1.addNode(n4);
		g1.addNode(n5);
		g1.addNode(n6);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n4,n2);
		e3 = new Edge(n3,n5);
		e4 = new Edge(n5,n3);
		e5 = new Edge(n3,n5);
		e6 = new Edge(n3,n3);
		e7 = new Edge(n2,n3);
		e8 = new Edge(n5,n4);
		e9 = new Edge(n6,n6);
		g1.addEdge(e1);
		g1.addEdge(e2);
		g1.addEdge(e3);
		g1.addEdge(e4);
		g1.addEdge(e5);
		g1.addEdge(e6);
		g1.addEdge(e7);
		g1.addEdge(e8);
		g1.addEdge(e9);

		//finding or adding a node from a label
		Node nA = g1.addAdjacencyNode("nA");
		testNodeAL = new ArrayList<Node>();
		testNodeAL.add(n1);
		testNodeAL.add(n2);
		testNodeAL.add(n3);
		testNodeAL.add(n4);
		testNodeAL.add(n5);
		testNodeAL.add(n6);
		testNodeAL.add(nA);
		if(!g1.getNodes().equals(testNodeAL)) {
			System.out.println("Test4.1 Fail");
		}

		if(g1.addAdjacencyNode("n2") != n2) {
			System.out.println("Test4.2 Fail");
		}
		if(!g1.getNodes().equals(testNodeAL)) {
			System.out.println("Test4.3 Fail");
		}

		Node nDuplicate = new Node("n1");
		g1.addNode(nDuplicate);
		if(g1.addAdjacencyNode("n1") != null) {
			System.out.println("Test4.4 Fail");
		}
		g1.removeNode(nDuplicate);

		if(!g1.consistent()) {
			System.out.println("Test4.4.0 Fail");
		}

		//adding an adjacency edge
		Edge eZ1 = g1.addAdjacencyEdge("n3","n3");

		Edge[] tempArray = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1};
		ArrayList<Edge> testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			System.out.println("Test4.5 Fail");
		}
		if(!g1.getNodes().equals(testNodeAL)) {
			System.out.println("Test4.6 Fail");
		}

		g1.addAdjacencyEdge("nB","");
		if(g1.getNodes().size() != 8) {
			System.out.println("Test4.7 Fail");
		}

		Edge eZ3 = g1.addAdjacencyEdge("nB","n1");

		Edge[] tempArray2 = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1,eZ3};
		testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray2));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			System.out.println("Test4.8 Fail");
		}

		g1.addAdjacencyEdge("","");
		Edge eZ4 = g1.addAdjacencyEdge("nC","nD");

		Edge[] tempArray3 = {e1,e2,e3,e4,e5,e6,e7,e8,e9,eZ1,eZ3,eZ4};
		testEdgeAL2 = new ArrayList<Edge>(Arrays.asList(tempArray3));

		if(!g1.getEdges().equals(testEdgeAL2)) {
			System.out.println("Test4.9 Fail");
		}
		if(g1.getNodes().size() != 10) {
			System.out.println("Test4.10 Fail");
		}

		System.out.print("Test 4 END");

		System.out.println(" | Test 5 START: Connected");

		if(g1.connected()) {
			System.out.println("Test5.1 Fail");
		}
		g1.addEdge(new Edge(nA,n6));
		if(g1.connected()) {
			System.out.println("Test5.2 Fail");
		}
		g1.addAdjacencyEdge("nA","nD");
		g1.addAdjacencyEdge("nA","n4");
		if(!g1.connected()) {
			System.out.println("Test5.3 Fail");
		}

		if(!g1.consistent()) {
			System.out.println("Test5.3.0 Fail");
		}

		Graph g4 = new Graph("g4");
		if(!g4.connected()) {
			System.out.println("Test5.4.1 Fail");
		}
		g4.addNode(new Node("z1"));
		if(!g4.connected()) {
			System.out.println("Test5.4.2 Fail");
		}
		g4.addNode(new Node("z2"));
		if(g4.connected()) {
			System.out.println("Test5.4.3 Fail");
		}
		g4.addAdjacencyEdge("z1","z2");
		if(!g4.connected()) {
			System.out.println("Test5.4.4 Fail");
		}
		g4.addAdjacencyEdge("z2","z3");
		if(!g4.connected()) {
			System.out.println("Test5.4.5 Fail");
		}
		g4.removeNode("z2");
		if(g4.connected()) {
			System.out.println("Test5.4.6 Fail");
		}
		
		g4 = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		if(g4.nodesConnected(n1,n1)) {System.out.println("Test5.5.1 Fail");}
		if(g4.nodesConnected(n1,n2)) {System.out.println("Test5.5.2 Fail");}
		if(g4.nodesConnected(n2,n1)) {System.out.println("Test5.5.3 Fail");}
		g4.addNode(n1);
		if(!g4.nodesConnected(n1,n1)) {System.out.println("Test5.5.4 Fail");}
		if(g4.nodesConnected(n2,n2)) {System.out.println("Test5.5.5 Fail");}
		g4.addNode(n2);
		if(!g4.nodesConnected(n1,n1)) {System.out.println("Test5.5.6 Fail");}
		if(!g4.nodesConnected(n2,n2)) {System.out.println("Test5.5.7 Fail");}
		g4.addEdge(new Edge(n1,n1));
		if(g4.nodesConnected(n1,n2)) {System.out.println("Test5.5.8 Fail");}
		if(g4.nodesConnected(n2,n1)) {System.out.println("Test5.5.9 Fail");}
		g4.addEdge(new Edge(n1,n2));
		if(!g4.nodesConnected(n1,n2)) {System.out.println("Test5.5.10 Fail");}
		if(!g4.nodesConnected(n2,n1)) {System.out.println("Test5.5.11 Fail");}

		g4 = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n3 = new Node("3");
		n4 = new Node("4");
		n5 = new Node("5");
		g4.addNode(n1);
		g4.addNode(n2);
		g4.addNode(n3);
		g4.addNode(n4);
		g4.addNode(n5);
		if(!g4.nodesConnected(n1,n1)) {System.out.println("Test5.5.12 Fail");}
		if(g4.nodesConnected(n2,n4)) {System.out.println("Test5.5.13 Fail");}
		g4.addEdge(new Edge(n1,n2));
		g4.addEdge(new Edge(n3,n2));
		g4.addEdge(new Edge(n1,n3));
		if(!g4.nodesConnected(n1,n3)) {System.out.println("Test5.5.14 Fail");}
		if(!g4.nodesConnected(n3,n1)) {System.out.println("Test5.5.15 Fail");}
		if(g4.nodesConnected(n1,n4)) {System.out.println("Test5.5.16 Fail");}
		g4.addEdge(new Edge(n5,n4));
		if(!g4.nodesConnected(n4,n5)) {System.out.println("Test5.5.17 Fail");}
		if(!g4.nodesConnected(n5,n4)) {System.out.println("Test5.5.18 Fail");}
		if(g4.nodesConnected(n1,n4)) {System.out.println("Test5.5.19 Fail");}
		g4.addEdge(new Edge(n5,n2));
		if(!g4.nodesConnected(n4,n1)) {System.out.println("Test5.5.20 Fail");}
		if(!g4.nodesConnected(n1,n4)) {System.out.println("Test5.5.21 Fail");}

		ArrayList<Node> retNodes;
		
		g4 = new Graph();
		n1 = new Node("n1");
		g4.addNode(n1);
		retNodes = g4.connectedUnvisitedNodes(n1);
		if(retNodes.size() != 1) {System.out.println("Test5.6.1 Fail");}
		if(retNodes.get(0) != n1) {System.out.println("Test5.6.2 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n1);
		if(retNodes.size() != 0) {System.out.println("Test5.6.3 Fail");}
		
		g4 = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		
		g4.addNode(n1);
		g4.addNode(n2);
		g4.addNode(n3);
		g4.addNode(n4);
		g4.addNode(n5);
		
		g4.addEdge(new Edge(n1,n2));
		g4.addEdge(new Edge(n3,n2));
		g4.addEdge(new Edge(n4,n5));
		
		retNodes = g4.connectedUnvisitedNodes(n2);
		if(retNodes.size() != 3) {System.out.println("Test5.6.4 Fail");}
		if(retNodes.get(0) == retNodes.get(1)) {System.out.println("Test5.6.5 Fail");}
		if(retNodes.get(1) == retNodes.get(2)) {System.out.println("Test5.6.6 Fail");}
		for(Node retN : retNodes) {
			if(retN == n1) {continue;}
			if(retN == n2) {continue;}
			if(retN == n3) {continue;}
			System.out.println("Test5.6.7 Fail");
		}
		if(g4.unvisitedNodes().size() != 2) {System.out.println("Test5.6.8 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n2);
		if(retNodes.size() != 0) {System.out.println("Test5.6.9 Fail");}
		if(g4.unvisitedNodes().size() != 2) {System.out.println("Test5.6.10 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n5);
		if(retNodes.size() != 2) {System.out.println("Test5.6.11 Fail");}
		if(retNodes.get(0) == retNodes.get(1)) {System.out.println("Test5.6.12 Fail");}
		for(Node retN : retNodes) {
			if(retN == n4) {continue;}
			if(retN == n5) {continue;}
			System.out.println("Test5.6.13 Fail");
		}
		if(g4.unvisitedNodes().size() != 0) {System.out.println("Test5.6.14 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n5);
		if(retNodes.size() != 0) {System.out.println("Test5.6.15 Fail");}
		if(g4.unvisitedNodes().size() != 0) {System.out.println("Test5.6.16 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n2);
		if(retNodes.size() != 0) {System.out.println("Test5.6.16 Fail");}
		if(g4.unvisitedNodes().size() != 0) {System.out.println("Test5.6.16 Fail");}
		
		retNodes = g4.connectedUnvisitedNodes(n4);
		if(retNodes.size() != 0) {System.out.println("Test5.6.17 Fail");}
		if(g4.unvisitedNodes().size() != 0) {System.out.println("Test5.6.16 Fail");}
		
		

		System.out.print("Test 5 END");

		System.out.println(" | Test 6 START: Equality by label testing");

		Graph gc1 = new Graph("gc1");
		Graph gc2 = new Graph("gc2");

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.1 Fail");
		}

		gc1.addNode(new Node("A"));
		if(gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.2 Fail");
		}
		gc2.addNode(new Node("B"));

		if(gc2.equalsByNodeLabel(gc1)) {
			System.out.println("Test6.3 Fail");
		}

		gc2.addNode(new Node("A"));

		if(gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.4 Fail");
		}

		gc1.addNode(new Node("B"));

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.5 Fail");
		}

		gc1.addNode(new Node("C"));
		gc2.addNode(new Node("C"));

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.6 Fail");
		}


		gc1.addAdjacencyEdge("A","B");

		if(gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.7 Fail");
		}

		gc2.addAdjacencyEdge("A","B");

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.8 Fail");
		}

		gc1.addAdjacencyEdge("C","B");
		gc2.addAdjacencyEdge("C","B");

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.9 Fail");
		}

		gc1.addAdjacencyEdge("C","A");
		gc2.addAdjacencyEdge("A","C");

		if(gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.10 Fail");
		}

		//this test is two non isomorphic graphs for which the comparison succeeds.

		gc1.clear();
		gc2.clear();

		Node gcn1 = new Node("");
		Node gcn2 = new Node("");
		Node gcn3 = new Node("");
		Node gcn4 = new Node("");
		gc1.addNode(gcn1);
		gc1.addNode(gcn2);
		gc2.addNode(gcn3);
		gc2.addNode(gcn4);

		gc1.addEdge(new Edge(gcn1,gcn2));

		gc2.addEdge(new Edge(gcn3,gcn3));

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.11 Fail");
		}

		// despite the equal labels these graphs are not equal due to the extra edge
		gc2.addEdge(new Edge(gcn4,gcn3));

		if(gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.12 Fail");
		}

		gc1.addEdge(new Edge(gcn1,gcn2));

		if(!gc1.equalsByNodeLabel(gc2)) {
			System.out.println("Test6.13 Fail");
		}

		if(!gc1.consistent()) {
			System.out.println("Test6.13.0 Fail");
		}


		System.out.print("Test 6 END");


		//Adjacency file testing. This test creates a file called test.adj
		//It will crash with an exception if it cant create the file.

		System.out.println(" | Test 7 START: Adjacency file - needs to read and write test.adj");

		g4.generateRandomGraph(10,15);
		g4.saveAdjacencyFile("test.adj");
		Graph g5 = new Graph("g5");
		g5.loadAdjacencyFile("test.adj");

		if(!g4.equalsByNodeLabel(g5)) {
			System.out.println("Test7.1 Fail");
		}

		if(!g4.consistent()) {
			System.out.println("Test7.1.0 Fail");
		}
		if(!g5.consistent()) {
			System.out.println("Test7.1.1 Fail");
		}


		g4.clear();
		g4.saveAdjacencyFile("test.adj");
		g5.clear();
		g5.generateRandomGraph(10,15);
		g4.loadAdjacencyFile("test.adj");
		if(g4.equalsByNodeLabel(g5)) {
			System.out.println("Test7.2 Fail");
		}
		g4.generateRandomGraph(20,30);
		g4.addNode(n1);
		g4.saveAdjacencyFile("test.adj");
		g5.generateRandomGraph(10,15);
		g5.loadAdjacencyFile("test.adj");
		g4.loadAdjacencyFile("test.adj");
		if(!g5.equalsByNodeLabel(g4)) {
			System.out.println("Test7.3 Fail");
		}
		g4.clear();
		g4.saveAdjacencyFile("test.adj");
		g4.addNode(n1);
		g4.loadAdjacencyFile("test.adj");
		if(!g4.equalsByNodeLabel(new Graph())) {
			System.out.println("Test7.4 Fail");
		}

		if(!g4.consistent()) {
			System.out.println("Test7.4.0 Fail");
		}

		System.out.print("Test 7 END");


		//partial node and edge access

		System.out.println(" | Test 8 START: visited and path fields");

		Graph g = new Graph();
		ArrayList<Node> testa = new ArrayList<Node>();
		ArrayList<Node> testb = new ArrayList<Node>();

		if(!g.unvisitedNodes().equals(testa)) {
			System.out.println("Test8.1 Fail");
		}
		if(!g.visitedNodes().equals(testb)) {
			System.out.println("Test8.2 Fail");
		}

		n1 = new Node("1");
		n2 = new Node("2");
		n3 = new Node("3");
		n4 = new Node("4");

		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		
		Edge eV1 = new Edge(n1,n2);
		Edge eV2 = new Edge(n3,n2);
		Edge eV3 = new Edge(n4,n4);

		testa.add(n1);
		testa.add(n2);
		testa.add(n3);
		testa.add(n4);
		
		if(!g.unvisitedNodes().equals(testa)) {
			System.out.println("Test8.5 Fail");
		}
		if(!g.visitedNodes().equals(testb)) {
			System.out.println("Test8.6 Fail");
		}

		n1.setVisited(true);
		n2.setVisited(true);

		testa.remove(n1);
		testa.remove(n2);

		testb.add(n1);
		testb.add(n2);

		if(!g.unvisitedNodes().equals(testa)) {
			System.out.println("Test8.9 Fail");
		}
		if(!g.visitedNodes().equals(testb)) {
			System.out.println("Test8.10 Fail");
		}

		ArrayList<Edge> teste1 = new ArrayList<Edge>();
		ArrayList<Edge> teste2 = new ArrayList<Edge>();

		if(!g.unvisitedEdges().equals(teste1)) {
			System.out.println("Test8.13 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			System.out.println("Test8.14 Fail");
		}

		g.addEdge(eV1);
		g.addEdge(eV2);
		g.addEdge(eV3);

		eV1.setVisited(true);
		eV2.setVisited(true);
		eV3.setVisited(true);

		teste2.add(eV1);
		teste2.add(eV2);
		teste2.add(eV3);

		if(!g.unvisitedEdges().equals(teste1)) {
			System.out.println("Test8.15 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			System.out.println("Test8.16 Fail");
		}

		eV3.setVisited(false);

		teste1.add(eV3);
		teste2.remove(eV3);

		if(!g.unvisitedEdges().equals(teste1)) {
			System.out.println("Test8.17 Fail");
		}
		if(!g.visitedEdges().equals(teste2)) {
			System.out.println("Test8.18 Fail");
		}

		System.out.print("Test 8 END");

		//euler tour tests
		System.out.println(" | Test 9 START: Euler Tour - needs to read and write test.euler");

		g = new Graph();
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.1 Fail");}
		
		Node na1 = new Node("1");
		g.addNode(na1);
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.2 Fail");}
		Node na2 = new Node("2"); 
		g.addNode(na2);
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.3 Fail");}
		g.addEdge(new Edge(na1,na1));
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.4 Fail");}
		g.addEdge(new Edge(na1,na2));
		if (g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.5 Fail");}
		g.addEdge(new Edge(na2,na1));
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.6 Fail");}

		g = new Graph();
		na1 = new Node("1");
		na2 = new Node("2"); 
		Node na3 = new Node("3"); 
		Node na4 = new Node("4"); 
		g.addNode(na1);
		g.addNode(na2);
		g.addNode(na3);
		g.addNode(na4);
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.7 Fail");}
		g.addEdge(new Edge(na1,na2));
		if (g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.8 Fail");}
		g.addEdge(new Edge(na1,na2));
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.9 Fail");}
		g.addEdge(new Edge(na1,na3));
		if (g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.10 Fail");}
		g.addEdge(new Edge(na3,na4));
		if (g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.11 Fail");}
		g.addEdge(new Edge(na1,na4));
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.12 Fail");}
		g.addEdge(new Edge(na1,na4));
		if (g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.13 Fail");}
		g.addEdge(new Edge(na1,na4));
		if (!g.nodesHaveEvenDegree()) {System.out.println("Test 9.0.1.14 Fail");}

		Graph eg = new Graph();
		ArrayList<Node> tour1 = new ArrayList<Node>();
		eg.saveTour("test.euler",tour1);
		ArrayList<Node> tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.1 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2.add(n1);
		if (eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.2 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.3 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}

		eg = new Graph();
		eg.generateRandomEulerGraph(5,7);
		tour1 = eg.euler();
		eg.saveTour("test.euler",tour1);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.4 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2.remove(2);
		if (eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.5 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.addAdjacencyEdge("1","2");
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.6 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}

		if(!eg.consistent()) {
			System.out.println("Test 9.6.0 Fail");
		}
		
		eg.clear();
		n1 = new Node("1");
		eg.addNode(n1);
		tour1 = eg.euler();
		tour2 = new ArrayList<Node>();
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.6.1 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.generateRandomEulerGraph(2,4);
		tour1 = eg.euler();
		eg.saveTour("test.euler",tour1);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.7 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		tour2 = new ArrayList<Node>();
		if (eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.8 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}
		eg.addNode(new Node());
		eg.saveTour("test.euler",tour1,false);
		tour2 = eg.loadTour("test.euler");
		if (!eg.eulerTourInGraph(tour2)) {
			System.out.println("Test 9.9 Failed with tour "+tour2 +"\nand graph\n"+eg);
		}

		if(!eg.consistent()) {
			System.out.println("Test 9.9.0 Fail");
		}

		System.out.print("Test 9 END");


		// test the brute force tsp
		System.out.println(" | Test 10 START: tsp");

		Graph tspGraph = new Graph();

		ArrayList<Edge> tspTest = new ArrayList<Edge>();

		if (!tspGraph.tsp().equals(tspTest)) {
			System.out.println("Test 10.1 Failed");
		}

		Node tspN1 = new Node("A");
		tspGraph.addNode(tspN1);

		if (!tspGraph.tsp().equals(tspTest)) {
			System.out.println("Test 10.2 Failed");
		}

		Node tspN2 = new Node("B");
		tspGraph.addNode(tspN2);

		Edge tspE1 = new Edge(tspN1,tspN2,2);

		tspGraph.addEdge(tspE1);

		tspTest.add(tspE1);
		tspTest.add(tspE1);

		if (!tspGraph.tsp().equals(tspTest)) {
			System.out.println("Test 10.3 Failed");
		}
		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 4.0) {
			System.out.println("Test 10.4 Failed");
		}

		Node tspN3 = new Node("C");
		tspGraph.addNode(tspN3);

		Edge tspE2 = new Edge(tspN1,tspN3,40);
		Edge tspE3 = new Edge(tspN2,tspN3,3);

		tspGraph.addEdge(tspE2);
		tspGraph.addEdge(tspE3);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 10.0) {
			System.out.println("Test 10.5 Failed");
		}

		tspE2.setWeight(4);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 9.0) {
			System.out.println("Test 10.6 Failed");
		}

		Node tspN4 = new Node("D");
		tspGraph.addNode(tspN4);

		tspGraph.addEdge(new Edge(tspN1,tspN4,3.5));
		tspGraph.addEdge(new Edge(tspN2,tspN4,6));
		tspGraph.addEdge(new Edge(tspN4,tspN3,2.4));

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 10.9) {
			System.out.println("Test 10.7 Failed");
		}

		tspGraph.clear();

		tspGraph.addAdjacencyEdge("A","H",17.0);
		tspGraph.addAdjacencyEdge("G","H",1.0);
		tspGraph.addAdjacencyEdge("H","E",2);
		tspGraph.addAdjacencyEdge("G","E",9.0);
		tspGraph.addAdjacencyEdge("E","D",20.0);
		tspGraph.addAdjacencyEdge("C","D",2.5);
		tspGraph.addAdjacencyEdge("C","B",3.0);
		tspGraph.addAdjacencyEdge("D","B",4.0);

		if (tspGraph.sumEdgeWeights(tspGraph.tsp()) != 89.5) {
			System.out.println("Test 10.8 Failed");
		}

		tspGraph.clear();

		tspGraph.addAdjacencyEdge("A","H","17",17.0);
		tspGraph.addAdjacencyEdge("G","H","1",1.0);
		tspGraph.addAdjacencyEdge("G","A","1",1.0);
		tspGraph.addAdjacencyEdge("E","D","20",20.0);
		tspGraph.addAdjacencyEdge("C","D","2 and a half",2.5);
		tspGraph.addAdjacencyEdge("C","B","3",3.0);
		tspGraph.addAdjacencyEdge("D","B","4",4.0);

		if (tspGraph.tsp() != null) {
			System.out.println("Test 10.9 Failed");
		}

		System.out.print("Test 10 END");


		// test the types
		System.out.println(" | Test 12 START: Node and Edge Types");

		NodeType nt11= new NodeType("nt11");
		NodeType nt21= new NodeType("nt21");
		NodeType nt31= new NodeType("nt31");
		NodeType nt22= new NodeType("nt22");
		NodeType nt32= new NodeType("nt32");
		NodeType nt33= new NodeType("nt33");
		NodeType nt34= new NodeType("nt34");
		NodeType nt23= new NodeType("nt23");
		NodeType nt41= new NodeType("nt41");
		NodeType nt42= new NodeType("nt42");
		NodeType nt43= new NodeType("nt43");
		nt21.setParent(nt11);
		nt31.setParent(nt21);
		nt22.setParent(nt11);
		nt32.setParent(nt21);
		nt33.setParent(nt22);
		nt34.setParent(nt21);
		nt23.setParent(nt11);
		nt41.setParent(nt32);
		nt42.setParent(nt32);
		nt43.setParent(nt32);

		nt22.removeParent();
		NodeType ntmove = (NodeType)nt33.getParent();
		nt32.setParent(ntmove);

		if(nt11.setParent(nt31)) {
				System.out.println("Test 12.3.1 FAIL");
		}
		if(!nt11.setParent(nt43)) {
				System.out.println("Test 12.3.2 FAIL");
		}

		if(nt43.setParent(nt43)) {
				System.out.println("Test 12.3.3 FAIL");
		}

		if(nt43.setParent(nt43)) {
				System.out.println("Test 12.3.4 FAIL");
		}

		if(!nt43.ancestor(nt32)) {
				System.out.println("Test 12.3.5 FAIL");
		}
		if(nt41.ancestor(nt41)) {
				System.out.println("Test 12.3.6 FAIL");
		}
		if(!nt33.ancestor(nt22)) {
				System.out.println("Test 12.3.7 FAIL");
		}
		if(nt41.ancestor(nt42)) {
				System.out.println("Test 12.3.8 FAIL");
		}

		if(nt41.root() != nt22) {
				System.out.println("Test 12.3.9 FAIL");
		}
		if(nt22.root() != nt22) {
				System.out.println("Test 12.3.10 FAIL");
		}


		EdgeType et11 = new EdgeType("et11");
		EdgeType et21 = new EdgeType("et21");
		EdgeType et22 = new EdgeType("et22");
		EdgeType et31 = new EdgeType("et31");
		et21.setParent(et11);
		et31.setParent(et22);
		et22.setParent(et11);
		et21.setDirected(true);

		if(!et31.ancestor(et11)) {
				System.out.println("Test 12.4.1 FAIL");
		}
		if(et11.ancestor(et21)) {
				System.out.println("Test 12.4.2 FAIL");
		}
		if(et21.root() != et11) {
				System.out.println("Test 12.4.3 FAIL");
		}
		if(et11.root() != et11) {
				System.out.println("Test 12.4.4 FAIL");
		}

		Node nt1 = new Node("nt1", new Point(100,100));
		Node nt2 = new Node("nt2",nt32, new Point(110,100));
		Node nt3 = new Node("nt3",nt11, new Point(200,200));

		Edge et1 = new Edge(nt1,nt2,"e1",3.0,et11);
		Edge et2 = new Edge(nt2,nt3,"e2");
		Edge et3 = new Edge(nt1,nt3,"e3",0.0,et31);

		Graph gt = new Graph("gt1");

		gt.addNode(nt1);
		gt.addNode(nt2);
		gt.addNode(nt3);
		gt.addEdge(et1);
		gt.addEdge(et2);
		gt.addEdge(et3);

		if(nt1.getType() != Graph.DEFAULT_NODE_TYPE) {
				System.out.println("Test 12.5.1 FAIL");
		}

		if(nt2.getType() != nt32) {
				System.out.println("Test 12.5.2 FAIL");
		}

		nt2.setType(nt41);

		if(nt2.getType() != nt41) {
				System.out.println("Test 12.5.3 FAIL");
		}


		if(et2.getType() != Graph.DEFAULT_EDGE_TYPE) {
				System.out.println("Test 12.5.4 FAIL");
		}

		if(et3.getType() != et31) {
				System.out.println("Test 12.5.5 FAIL");
		}

		et3.setType(Graph.DEFAULT_EDGE_TYPE);

		if(et3.getType() != Graph.DEFAULT_EDGE_TYPE) {
				System.out.println("Test 12.5.7 FAIL");
		}

		if(Graph.DEFAULT_EDGE_TYPE.root() != Graph.DEFAULT_EDGE_TYPE) {
				System.out.println("Test 12.5.8 FAIL");
		}
		
		
		System.out.print("Test 12 END");

	}
	

	public static void testA1() {
		
		System.out.println(" | Test 13 START: mst & cycle detection");

		Graph mstGraph = new Graph();

		//generating a complete graph
		mstGraph.generateCompleteGraph(0);
		if(mstGraph.getNodes().size() != 0) {
			System.out.println("Test 13.1.1 Fail");
		}
		if(mstGraph.getEdges().size() != 0) {
			System.out.println("Test 13.1.2 Fail");
		}

		mstGraph.generateCompleteGraph(1);
		if(mstGraph.getNodes().size() != 1) {
			System.out.println("Test 13.1.3 Fail");
		}
		if(mstGraph.getEdges().size() != 0) {
			System.out.println("Test 13.1.4 Fail");
		}

		if(!mstGraph.consistent()) {
			System.out.println("Test 13.1.4.0 Fail");
		}

		mstGraph.generateCompleteGraph(5);
		if(mstGraph.getNodes().size() != 5) {
			System.out.println("Test 13.1.5 Fail");
		}
		if(mstGraph.getEdges().size() != 10) {
			System.out.println("Test 13.1.6 Fail");
		}

		mstGraph.generateCompleteGraph(8);
		if(mstGraph.getNodes().size() != 8) {
			System.out.println("Test 13.1.7 Fail");
		}
		if(mstGraph.getEdges().size() != 28) {
			System.out.println("Test 13.1.8 Fail");
		}

		mstGraph.setEdgesWeights(1.0);
		if(mstGraph.sumEdgeWeights(mstGraph.prim()) != 7) {
			System.out.println("Test 13.2.1 Fail");
		}

		mstGraph.clear();

		ArrayList<Edge> mstCompare = new ArrayList<Edge>();

		if(!mstCompare.equals(mstGraph.prim())) {
			System.out.println("Test 13.2.3 Fail");
		}

		Node mstN1 = new Node("A");
		Node mstN2 = new Node("B");
		Node mstN3 = new Node("C");

		mstGraph.addNode(mstN1);

		if(!mstCompare.equals(mstGraph.prim())) {
			System.out.println("Test 13.2.5 Fail");
		}
		
		mstGraph.addNode(mstN2);

		if(mstGraph.prim() != null) {
			System.out.println("Test 13.2.7 Fail");
		}

		Edge mstE1 = new Edge(mstN1,mstN2,"A",1);

		mstGraph.addEdge(mstE1);

		mstCompare.add(mstE1);
		if(!mstCompare.equals(mstGraph.prim())) {
			System.out.println("Test 13.2.9 Fail");
		}

		mstGraph.addNode(mstN3);
		Edge mstE2 = new Edge(mstN2,mstN3,"B",5);
		Edge mstE3 = new Edge(mstN3,mstN1,"C",2);
		Edge mstE4 = new Edge(mstN2,mstN2,"D",1);

		mstGraph.addEdge(mstE2);
		mstGraph.addEdge(mstE3);
		mstGraph.addEdge(mstE4);

		mstCompare.add(mstE3);

		if(mstGraph.sumEdgeWeights(mstGraph.prim()) != 3) {
			System.out.println("Test 13.2.11 Fail");
		}
		
		Graph g;
		Node n1,n2,n3,n4;


		
		// test cycle detector
		ArrayList<Node> nodeCycle;
		
		g = new Graph();
		nodeCycle = new ArrayList<Node>();
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.1 FAIL");}
		
		n1 = new Node("1");
		n2 = new Node("2");
		g.addNode(n1);
		g.addNode(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.2 FAIL");}
		
		nodeCycle.add(n1);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.3 FAIL");}
		nodeCycle.add(n2);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.4 FAIL");}
		
		g.addEdge(new Edge(n1,n2));
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.5 FAIL");}
		g.addEdge(new Edge(n1,n2));
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.6 FAIL");}

		g = new Graph();
		n3 = new Node("3");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n1,n3));
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.7 FAIL");}

		g.addNode(n3);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.8 FAIL");}
		g.addEdge(new Edge(n2,n3));
		nodeCycle.add(n3);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.9 FAIL");}

		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.10 FAIL");}

		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n3 = new Node("3");
		n4 = new Node("4");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addEdge(new Edge(n2,n1));
		g.addEdge(new Edge(n2,n3));
		g.addEdge(new Edge(n3,n4));
		nodeCycle = new ArrayList<Node>();
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.11 FAIL");}
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		nodeCycle.add(n4);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.12 FAIL");}
		g.addEdge(new Edge(n1,n4));
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.13 FAIL");}
		
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n3);
		nodeCycle.add(n2);
		nodeCycle.add(n4);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.14 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n3);
		nodeCycle.add(n4);
		nodeCycle.add(n2);
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.15 FAIL");}
		
		g.addAdjacencyEdge("1","5");
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.16 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n2);
		nodeCycle.add(n1);
		nodeCycle.add(n4);
		nodeCycle.add(n3);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.17 FAIL");}
		g.addAdjacencyEdge("5","1");
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.18 FAIL");}
		g.addAdjacencyEdge("5","6");
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.19 FAIL");}
		g.addAdjacencyEdge("2","6");
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.20 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n2);
		nodeCycle.add(n4);
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.21 FAIL");}
		g.addAdjacencyEdge("1","2");
		nodeCycle = new ArrayList<Node>();
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.22 FAIL");}
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.23 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.24 FAIL");}


		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n3 = new Node("3");
		n4 = new Node("4");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n2,n3));
		g.addEdge(new Edge(n3,n1));
		g.addEdge(new Edge(n1,n4));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n4);
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.25 FAIL");}
		g.addEdge(new Edge(n1,n4));
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.26 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n4);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.27 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n4);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.28 FAIL");}
		
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.29 FAIL");}
		
		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		g.addNode(n1);
		g.addNode(n2);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n1,n2));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.30 FAIL");}
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.31 FAIL");}
		nodeCycle.add(n2);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.32 FAIL");}

		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		g.addNode(n1);
		g.addNode(n2);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n1,n2));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.33 FAIL");}
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.34 FAIL");}
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.35 FAIL");}
		nodeCycle.add(n1);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.36 FAIL");}
		nodeCycle.add(n2);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.37 FAIL");}

		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n2 = new Node("3");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n3,n2));
		g.addEdge(new Edge(n3,n1));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.38 FAIL");}
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.39 FAIL");}

		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n2 = new Node("3");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n3,n2));
		g.addEdge(new Edge(n3,n1));
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n3,n2));
		g.addEdge(new Edge(n3,n1));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.40 FAIL");}
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.41 FAIL");}
		nodeCycle.add(n3);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.42 FAIL");}
		
		g = new Graph();
		n1 = new Node("1");
		n2 = new Node("2");
		n2 = new Node("3");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n2,n3));
		g.addEdge(new Edge(n3,n2));
		g.addEdge(new Edge(n3,n1));
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.43 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.44 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n1);
		nodeCycle.add(n3);
		if(g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.45 FAIL");}
		nodeCycle = new ArrayList<Node>();
		nodeCycle.add(n1);
		nodeCycle.add(n2);
		nodeCycle.add(n3);
		if(!g.isNodeCycle(nodeCycle)) {System.out.println("Test 13.4.46 FAIL");}

		
		System.out.print("Test 13 END");


		
	}
	
	public static void testB() {

		System.out.println(" | Test 14 START: Graph.clone and other utilities");
		
		Graph graph;
		Graph newGraph;
		ArrayList<Node> nodes;
		ArrayList<Edge> edges;
		ArrayList<Point> bends;
		Node n1;
		Node n2;
		Node n3;
		Node n4;
		Node n5;
		Edge e1;
		Edge e2;
		Edge e3;
		Edge e4;
		
		graph = new Graph();
		newGraph = graph.clone();
		if(!newGraph.getLabel().equals("")) {System.out.println("Test 14.1.1 FAIL");}
		graph = new Graph("fred");
		newGraph = graph.clone();
		if(!newGraph.getLabel().equals("fred")) {System.out.println("Test 14.1.2 FAIL");}
		graph.addNode(new Node());
		if(newGraph.getNodes().size() != 0) {System.out.println("Test 14.1.3 FAIL");}
		if(newGraph.getEdges().size() != 0) {System.out.println("Test 14.1.3a FAIL");}
		if(!graph.consistent()) {System.out.println("Test 14.1.3b FAIL");}
		if(!newGraph.consistent()) {System.out.println("Test 14.1.3c FAIL");}
		
		graph = new Graph("fred");
		n1 = new Node("n1");
		n1.setType(new NodeType("t2"));
		n1.setVisited(true);
		n1.setScore(5.0);
		n1.setCentre(new Point(6,7));
		graph.addNode(n1);
		newGraph = graph.clone();
		nodes = newGraph.getNodes();
		if(nodes.size() != 1) {System.out.println("Test 14.1.4 FAIL");}
		if(!nodes.get(0).getLabel().equals("n1")) {System.out.println("Test 14.1.5 FAIL");}
		if(!nodes.get(0).getType().getLabel().equals("t2")) {System.out.println("Test 14.1.16 FAIL");}
		if(nodes.get(0).getVisited() != true) {System.out.println("Test 14.1.7 FAIL");}
		if(nodes.get(0).getScore() != 5.0) {System.out.println("Test 14.1.8 FAIL");}
		if(!nodes.get(0).getCentre().equals(new Point(6,7))) {System.out.println("Test 14.1.9 FAIL");}

		n1.setLabel("changed n1");
		n1.setType(new NodeType("t3"));
		n1.setVisited(false);
		n1.setScore(15.0);
		n1.setCentre(new Point(16,17));
		
		nodes = newGraph.getNodes();
		if(nodes.size() != 1) {System.out.println("Test 14.1.10 FAIL");}
		if(!nodes.get(0).getLabel().equals("n1")) {System.out.println("Test 14.1.11 FAIL");}
		if(!nodes.get(0).getType().getLabel().equals("t2")) {System.out.println("Test 14.1.12 FAIL");}
		if(nodes.get(0).getVisited() != true) {System.out.println("Test 14.1.13 FAIL");}
		if(nodes.get(0).getScore() != 5.0) {System.out.println("Test 14.1.14 FAIL");}
		if(!nodes.get(0).getCentre().equals(new Point(6,7))) {System.out.println("Test 14.1.15 FAIL");}
		
		graph = new Graph("fred");
		n1 = new Node("n1");
		n2 = new Node("n2");
		e1 = new Edge(n1,n2);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addEdge(e1);
		e1.setLabel("e1");
		e1.setScore(1.5);
		bends = new ArrayList<Point>();
		bends.add(new Point(2,3));
		bends.add(new Point(22,33));
		e1.setBends(bends);
		e1.setType(new EdgeType("et2"));
		e1.setVisited(true);
		e1.setWeight(19);
		
		newGraph = graph.clone();
		if(!newGraph.getLabel().equals("fred")) {System.out.println("Test 14.2.1 FAIL");}
		edges = newGraph.getEdges();
		if(edges.size() != 1) {System.out.println("Test 14.2.2 FAIL");}
		if(!edges.get(0).getLabel().equals("e1")) {System.out.println("Test 14.2.3 FAIL");}
		if(edges.get(0).getScore() != 1.5) {System.out.println("Test 14.2.4 FAIL");}
		if(!edges.get(0).getFrom().getLabel().equals("n1")) {System.out.println("Test 14.2.5 FAIL");}
		if(!edges.get(0).getTo().getLabel().equals("n2")) {System.out.println("Test 14.2.6 FAIL");}
		if(!edges.get(0).getType().getLabel().equals("et2")) {System.out.println("Test 14.2.7 FAIL");}
		if(edges.get(0).getBends().size() != 2) {System.out.println("Test 14.2.8 FAIL");}
		if(edges.get(0).getVisited() != true) {System.out.println("Test 14.2.9 FAIL");}
		if(edges.get(0).getWeight() != 19) {System.out.println("Test 14.2.10 FAIL");}
		if(!graph.consistent()) {System.out.println("Test 14.2.11 FAIL");}
		if(!newGraph.consistent()) {System.out.println("Test 14.2.12 FAIL");}
				
		n1.setLabel("n1 changed");
		n2.setLabel("n2 changed");
		
		graph.removeEdge(e1);
		edges = newGraph.getEdges();
		if(edges.size() != 1) {System.out.println("Test 14.3.1 FAIL");}
		if(!graph.consistent()) {System.out.println("Test 14.3.2 FAIL");}
		if(!newGraph.consistent()) {System.out.println("Test 14.3.3 FAIL");}
		n3 = new Node("n3");
		e1 = new Edge(n1,n2);
		e2 = new Edge(n3,n3);
		graph.addNode(n3);
		graph.addEdge(e1);
		graph.addEdge(e2);
		if(edges.size() != 1) {System.out.println("Test 14.3.4 FAIL");}
		if(!graph.consistent()) {System.out.println("Test 14.3.5 FAIL");}
		if(!newGraph.consistent()) {System.out.println("Test 14.3.6 FAIL");}

		newGraph= graph.clone();
		edges = newGraph.getEdges();
		if(edges.size() != 2) {System.out.println("Test 14.3.7 FAIL");}
		if(!graph.consistent()) {System.out.println("Test 14.3.8 FAIL");}
		if(!newGraph.consistent()) {System.out.println("Test 14.3.9 FAIL");}
		nodes = newGraph.getNodes();
		if(nodes.size() != 3) {System.out.println("Test 14.3.10 FAIL");}
		
		graph = new Graph();
		nodes = new ArrayList<Node>();
		if(graph.closestNode(new Point(50,100),nodes) != null) {System.out.println("Test 14.4.1 FAIL");}
		
		nodes.add(n1);
		if(graph.closestNode(new Point(50,100),nodes) != n1) {System.out.println("Test 14.4.2 FAIL");}

		graph = new Graph();
		nodes = new ArrayList<Node>();
		n1 = new Node(new Point(0,0));
		graph.addNode(n1);
		if(graph.closestNode(new Point(50,100),nodes) != null) {System.out.println("Test 14.4.3 FAIL");}
		nodes.add(n1);
		if(graph.closestNode(new Point(50,100),nodes) != n1) {System.out.println("Test 14.4.4 FAIL");}

		graph = new Graph();
		nodes = new ArrayList<Node>();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		graph.addNode(n1);
		graph.addNode(n2);
		nodes.add(n1);
		if(graph.closestNode(new Point(-50,-100),nodes) != n1) {System.out.println("Test 14.4.5 FAIL");}
		if(graph.closestNode(new Point(200,300),nodes) != n1) {System.out.println("Test 14.4.6 FAIL");}
		nodes.add(n2);
		if(graph.closestNode(new Point(200,300),nodes) != n2) {System.out.println("Test 14.4.7 FAIL");}

		graph = new Graph();
		nodes = new ArrayList<Node>();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(500,400));
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addEdge(new Edge(n1,n2));
		graph.addEdge(new Edge(n1,n3));
		graph.addEdge(new Edge(n3,n2));
		if(graph.closestNode(new Point(100,100),nodes) != null) {System.out.println("Test 14.4.8 FAIL");}
		nodes.add(n1);
		if(graph.closestNode(new Point(0,400),nodes) != n1) {System.out.println("Test 14.4.9 FAIL");}
		nodes.add(n2);
		nodes.add(n3);
		if(graph.closestNode(new Point(0,400),nodes) != n2) {System.out.println("Test 14.4.10 FAIL");}
		if(graph.closestNode(new Point(1000,1000),nodes) != n3) {System.out.println("Test 14.4.11 FAIL");}
		
		graph = new Graph();
		if(graph.closestNode(new Point(50,100)) != null) {System.out.println("Test 14.5.1 FAIL");}
		
		graph = new Graph();
		n1 = new Node(new Point(100,100));
		graph.addNode(n1);
		if(graph.closestNode(new Point(-50,-100)) != n1) {System.out.println("Test 14.5.2 FAIL");}

		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		graph.addNode(n1);
		graph.addNode(n2);
		if(graph.closestNode(new Point(-50,-100)) != n1) {System.out.println("Test 14.5.3 FAIL");}
		if(graph.closestNode(new Point(200,200)) != n1) {System.out.println("Test 14.5.4 FAIL");}
		if(graph.closestNode(new Point(200,300)) != n2) {System.out.println("Test 14.5.5 FAIL");}

		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(500,400));
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addEdge(new Edge(n1,n2));
		graph.addEdge(new Edge(n1,n3));
		graph.addEdge(new Edge(n3,n2));
		if(graph.closestNode(new Point(100,100)) != n1) {System.out.println("Test 14.5.6 FAIL");}
		if(graph.closestNode(new Point(200,200)) != n1) {System.out.println("Test 14.5.7 FAIL");}
		if(graph.closestNode(new Point(0,400)) != n2) {System.out.println("Test 14.5.8 FAIL");}
		if(graph.closestNode(new Point(1000,1000)) != n3) {System.out.println("Test 14.5.9 FAIL");}

		
		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n1.setLabel("a");
		n2.setLabel("a");
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n1,n3);
		e3 = new Edge(n2,n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);

		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(!e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.1 FAIL");}
		if(!e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.2 FAIL");}
		
		n4 = new Node(new Point(100,110));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.3 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.4 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(110,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.5 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.6 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n5,n4);
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.7 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.8 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("b");
		e4 = new Edge(n4,n5);
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.9 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.10 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("b");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.11 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.12 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,20));
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.13 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.14 FAIL");}
		
		e1.addBend(new Point(20,30));
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,20));
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.15 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.16 FAIL");}
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,30));
		if(!e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.17 FAIL");}
		if(!e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.18 FAIL");}
		
		e1.addBend(new Point(120,130));
		
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,30));
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.19 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.20 FAIL");}

		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,30));
		e4.addBend(new Point(220,130));
		if(e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.21 FAIL");}
		if(e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.22 FAIL");}

		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		e4.addBend(new Point(20,30));
		e4.addBend(new Point(120,130));
		if(!e4.isMatchingEdge(e1)) {System.out.println("Test 14.6.23 FAIL");}
		if(!e1.isMatchingEdge(e4)) {System.out.println("Test 14.6.24 FAIL");}

		
		graph = new Graph();
		if(graph.getMatchingEdgeList(new Edge(n1,n2)).size() != 0) {System.out.println("Test 14.7.1 FAIL");}
		
		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(500,400));
		n1.setLabel("a");
		n2.setLabel("a");
		n3.setLabel("a");
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n1,n3);
		e3 = new Edge(n2,n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(graph.getMatchingEdgeList(e4).size() != 1) {System.out.println("Test 14.7.2 FAIL");}
		if(graph.getMatchingEdgeList(e4).get(0) != e1) {System.out.println("Test 14.7.3 FAIL");}
		
		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(100,400));
		n1.setLabel("a");
		n2.setLabel("a");
		n3.setLabel("a");
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n1,n3);
		e3 = new Edge(n3,n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(graph.getMatchingEdgeList(e4).size() != 2) {System.out.println("Test 14.7.4 FAIL");}
		if(!graph.getMatchingEdgeList(e4).contains(e1)) {System.out.println("Test 14.7.5 FAIL");}
		if(!graph.getMatchingEdgeList(e4).contains(e2)) {System.out.println("Test 14.7.6 FAIL");}

		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(200,400));
		n1.setLabel("a");
		n2.setLabel("a");
		n3.setLabel("a");
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n1,n2);
		e3 = new Edge(n3,n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		n4 = new Node(new Point(100,100));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(graph.getMatchingEdgeList(e4).size() != 2) {System.out.println("Test 14.7.7 FAIL");}
		if(!graph.getMatchingEdgeList(e4).contains(e1)) {System.out.println("Test 14.7.8 FAIL");}
		if(!graph.getMatchingEdgeList(e4).contains(e2)) {System.out.println("Test 14.7.9 FAIL");}

		graph = new Graph();
		n1 = new Node(new Point(100,100));
		n2 = new Node(new Point(100,400));
		n3 = new Node(new Point(200,400));
		n1.setLabel("a");
		n2.setLabel("a");
		n3.setLabel("a");
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		e1 = new Edge(n1,n2);
		e2 = new Edge(n1,n2);
		e3 = new Edge(n3,n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		n4 = new Node(new Point(100,110));
		n5 = new Node(new Point(100,400));
		n4.setLabel("a");
		n5.setLabel("a");
		e4 = new Edge(n4,n5);
		if(graph.getMatchingEdgeList(e4).size() != 0) {System.out.println("Test 14.7.10 FAIL");}


		// split edge bend methods

		graph = new Graph();
		n1 = new Node("n1", new Point(10,10));
		n2 = new Node("n2", new Point(20,20));
		e1 = new Edge(n1,n2);
		if(e1.findFirstBendOfLongestSection() != -1) System.out.println("Test 14.8.1 FAIL");
		
		n1 = new Node("n1", new Point(10,10));
		n2 = new Node("n2", new Point(20,20));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(18,18));
		if(e1.findFirstBendOfLongestSection() != -1) System.out.println("Test 14.8.2 FAIL");
		
		n1 = new Node("n1", new Point(10,10));
		n2 = new Node("n2", new Point(20,20));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(12,12));
		if(e1.findFirstBendOfLongestSection() != 0) System.out.println("Test 14.8.3 FAIL");

		n1 = new Node("n1", new Point(0,10));
		n2 = new Node("n2", new Point(0,20));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,15));
		if(e1.findFirstBendOfLongestSection() != -1) System.out.println("Test 14.8.4 FAIL");
		
		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,50));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,10));
		e1.addBend(new Point(0,20));
		e1.addBend(new Point(0,30));
		e1.addBend(new Point(0,40));
		if(e1.findFirstBendOfLongestSection() != -1) System.out.println("Test 14.8.5 FAIL");

		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,50));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,4));
		e1.addBend(new Point(0,5));
		e1.addBend(new Point(0,30));
		e1.addBend(new Point(0,40));
		if(e1.findFirstBendOfLongestSection() != 1) System.out.println("Test 14.8.6 FAIL");

		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,50));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,4));
		e1.addBend(new Point(0,5));
		e1.addBend(new Point(0,6));
		e1.addBend(new Point(0,7));
		if(e1.findFirstBendOfLongestSection() != 3) System.out.println("Test 14.8.7 FAIL");
		
		n1 = new Node("n1", new Point(40,40));
		n2 = new Node("n2", new Point(0,0));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(30,30));
		e1.addBend(new Point(20,20));
		if(e1.findFirstBendOfLongestSection() != 1) System.out.println("Test 14.8.8 FAIL");
		
		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,50));
		e1 = new Edge(n1,n2);
		if(e1.addEdgeBends(0)) System.out.println("Test 14.9.1 FAIL");
		
		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,50));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,10));
		e1.addBend(new Point(0,30));
		e1.addBend(new Point(0,35));
		e1.addBend(new Point(0,45));
		if(e1.addEdgeBends(0)) System.out.println("Test 14.9.2 FAIL");
		if(e1.addEdgeBends(4)) System.out.println("Test 14.9.3 FAIL");
		
		n1 = new Node("n1", new Point(0,0));
		n2 = new Node("n2", new Point(0,30));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(0,10));
		if(!e1.addEdgeBends(2)) System.out.println("Test 14.9.4 FAIL");
		if(e1.getBends().size() != 2) System.out.println("Test 14.9.5 FAIL");
		if(!e1.getBends().get(1).equals(new Point(0,20))) System.out.println("Test 14.9.6 FAIL");

		n1 = new Node("n1", new Point(40,40));
		n2 = new Node("n2", new Point(0,0));
		e1 = new Edge(n1,n2);
		e1.addBend(new Point(20,20));
		if(!e1.addEdgeBends(3)) System.out.println("Test 14.9.7 FAIL");
		if(e1.getBends().size() != 3) System.out.println("Test 14.9.8 FAIL");
		if(!e1.getBends().get(0).equals(new Point(30,30))) System.out.println("Test 14.9.9 FAIL");
		if(!e1.getBends().get(2).equals(new Point(10,10))) System.out.println("Test 14.9.10 FAIL");

		
		n1 = new Node("n1", new Point(0,0));
		e1 = new Edge(n1,n1);
		e1.addBend(new Point(0,20));
		e1.addBend(new Point(20,20));
		e1.addBend(new Point(20,0));
		if(!e1.addEdgeBends(6)) System.out.println("Test 14.9.11 FAIL");
		if(e1.getBends().size() != 6) System.out.println("Test 14.9.12 FAIL");
		if(!e1.getBends().get(0).equals(new Point(0,10))) System.out.println("Test 14.9.13 FAIL");
		if(!e1.getBends().get(2).equals(new Point(10,20))) System.out.println("Test 14.9.14 FAIL");
		if(!e1.getBends().get(4).equals(new Point(20,10))) System.out.println("Test 14.9.15 FAIL");
		
		n1 = new Node("n1", new Point(0,0));
		e1 = new Edge(n1,n1);
		e1.addBend(new Point(0,20));
		e1.addBend(new Point(20,20));
		e1.addBend(new Point(20,0));
		if(!e1.addEdgeBends(8)) System.out.println("Test 14.9.16 FAIL");
		if(e1.getBends().size() != 8) System.out.println("Test 14.9.17 FAIL");
		if(!e1.getBends().get(0).equals(new Point(0,5))) System.out.println("Test 14.9.18 FAIL");
		if(!e1.getBends().get(1).equals(new Point(0,10))) System.out.println("Test 14.9.19 FAIL");
		if(!e1.getBends().get(3).equals(new Point(10,20))) System.out.println("Test 14.9.20 FAIL");
		if(!e1.getBends().get(5).equals(new Point(20,10))) System.out.println("Test 14.9.21 FAIL");
		if(!e1.getBends().get(7).equals(new Point(10,00))) System.out.println("Test 14.9.22 FAIL");

			
		System.out.print("Test 14 END ");
	}
	
	
	
	public static void testD(){
		System.out.println("| Test 16 START: Graph.Util.java");

		// test utility methods
		
		Point p0;
		Point p1;
		Point p2;
		Point p3;
		Point p4;
		Point2D.Double pd0;
		Point2D.Double pd1;
		Point2D.Double pd2;
		Point2D.Double pd3;
		Point2D.Double pd4;
		int x1,y1,x2,y2;
		double xd1,yd1,xd2,yd2;
		
		double retD;
		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.1.1 FAIL");}	  		
		p1 = new Point(200,100);
		p2 = new Point(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.1.2 FAIL");}	  		
		p1 = new Point(100,200);
		p2 = new Point(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.1.3 FAIL");}	  		
		p1 = new Point(100,100);
		p2 = new Point(100,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 270.0) {System.out.println("Test 16.1.4 FAIL");}	  		
		p1 = new Point(100,100);
		p2 = new Point(200,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 315.0) {System.out.println("Test 16.1.5 FAIL");}	  		
		p1 = new Point(200,200);
		p2 = new Point(300,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.1.6 FAIL");}	  		
		p1 = new Point(300,100);
		p2 = new Point(200,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 225.0) {System.out.println("Test 16.1.6 FAIL");}	  		
		p1 = new Point(200,200);
		p2 = new Point(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 135.0) {System.out.println("Test 16.1.7 FAIL");}
		p1 = new Point(200,200);
		p2 = new Point(200,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(p1,p2);
		if(Math.toDegrees(retD) != 0) {System.out.println("Test 16.1.8 FAIL");}
		
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(200,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.1.9 FAIL");}	  		
		pd1 = new Point2D.Double(200,100);
		pd2 = new Point2D.Double(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.1.10 FAIL");}	  		
		pd1 = new Point2D.Double(100,200);
		pd2 = new Point2D.Double(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.1.11 FAIL");}	  		
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(100,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 270.0) {System.out.println("Test 16.1.12 FAIL");}	  		
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(200,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 315.0) {System.out.println("Test 16.1.13 FAIL");}	  		
		pd1 = new Point2D.Double(200,200);
		pd2 = new Point2D.Double(300,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.1.14 FAIL");}	  		
		pd1 = new Point2D.Double(300,100);
		pd2 = new Point2D.Double(200,200);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 225.0) {System.out.println("Test 16.1.15 FAIL");}	  		
		pd1 = new Point2D.Double(200,200);
		pd2 = new Point2D.Double(100,100);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 135.0) {System.out.println("Test 16.1.16 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0,0);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(Math.toDegrees(retD) != 0) {System.out.println("Test 16.1.17 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(Math.sqrt(3),-1);
		retD = uk.ac.kent.displayGraph.Util.lineAngle(pd1,pd2);
		if(uk.ac.kent.displayGraph.Util.round(Math.toDegrees(retD),0) != 30.0) {System.out.println("Test 16.1.18 FAIL");}	  		

		p1 = new Point(200,100);
		p2 = new Point(200,200);
		p3 = new Point(100,200);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.1 FAIL");}
		p1 = new Point(100,200);
		p2 = new Point(200,200);
		p3 = new Point(200,100);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.2 FAIL");}
		p1 = new Point(100,200);
		p2 = new Point(200,200);
		p3 = new Point(300,200);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.3 FAIL");}
		p1 = new Point(200,300);
		p2 = new Point(200,200);
		p3 = new Point(200,100);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.4 FAIL");}
		p1 = new Point(300,300);
		p2 = new Point(200,200);
		p3 = new Point(200,400);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.2.5 FAIL");}
		p1 = new Point(-1,-1);
		p2 = new Point(0,0);
		p3 = new Point(-1,0);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.2.6 FAIL");}
		p1 = new Point(-1,-1);
		p2 = new Point(0,0);
		p3 = new Point(-1,1);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.7 FAIL");}
		p1 = new Point(100,100);
		p2 = new Point(0,0);
		p3 = new Point(200,200);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.2.8 FAIL");}
		p1 = new Point(-200,-200);
		p2 = new Point(0,0);
		p3 = new Point(200,200);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.9 FAIL");}
		p1 = new Point(0,0);
		p2 = new Point(0,0);
		p3 = new Point(0,0);
		retD = uk.ac.kent.displayGraph.Util.angle(p1,p2,p3);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.2.10 FAIL");}
		
		pd1 = new Point2D.Double(200,100);
		pd2 = new Point2D.Double(200,200);
		pd3 = new Point2D.Double(100,200);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.11 FAIL");}
		pd1 = new Point2D.Double(100,200);
		pd2 = new Point2D.Double(200,200);
		pd3 = new Point2D.Double(200,100);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.12 FAIL");}
		pd1 = new Point2D.Double(100,200);
		pd2 = new Point2D.Double(200,200);
		pd3 = new Point2D.Double(300,200);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.13 FAIL");}
		pd1 = new Point2D.Double(200,300);
		pd2 = new Point2D.Double(200,200);
		pd3 = new Point2D.Double(200,100);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.14 FAIL");}
		pd1 = new Point2D.Double(300,300);
		pd2 = new Point2D.Double(200,200);
		pd3 = new Point2D.Double(200,400);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.2.15 FAIL");}
		pd1 = new Point2D.Double(-1,-1);
		pd2 = new Point2D.Double(0,0);
		pd3 = new Point2D.Double(-1,0);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 45.0) {System.out.println("Test 16.2.16 FAIL");}
		pd1 = new Point2D.Double(-1,-1);
		pd2 = new Point2D.Double(0,0);
		pd3 = new Point2D.Double(-1,1);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 90.0) {System.out.println("Test 16.2.17 FAIL");}
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(0,0);
		pd3 = new Point2D.Double(200,200);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.2.18 FAIL");}
		pd1 = new Point2D.Double(-200,-200);
		pd2 = new Point2D.Double(0,0);
		pd3 = new Point2D.Double(200,200);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 180.0) {System.out.println("Test 16.2.19 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0,0);
		pd3 = new Point2D.Double(0,0);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(Math.toDegrees(retD) != 0.0) {System.out.println("Test 16.2.20 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(Math.sqrt(3),0);
		pd3 = new Point2D.Double(0,1);
		retD = uk.ac.kent.displayGraph.Util.angle(pd1,pd2,pd3);
		if(uk.ac.kent.displayGraph.Util.round(Math.toDegrees(retD),0) != 30.0) {System.out.println("Test 16.2.21 FAIL");}

		if(uk.ac.kent.displayGraph.Util.round(29.51,0) != 30.0) {System.out.println("Test 16.3.1 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(30.49,0) != 30.0) {System.out.println("Test 16.3.2 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(29.51,1) != 29.5) {System.out.println("Test 16.3.3 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(30.49,1) != 30.5) {System.out.println("Test 16.3.4 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(0.5,0) != 0) {System.out.println("Test 16.3.5 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(-0.5,0) != 0) {System.out.println("Test 16.3.6 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(0.111111,0) != 0) {System.out.println("Test 16.3.7 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(0.111111,1) != 0.1) {System.out.println("Test 16.3.8 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(0.111111,2) != 0.11) {System.out.println("Test 16.3.9 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(0.111111,3) != 0.111) {System.out.println("Test 16.3.10 FAIL");}
		
		if(uk.ac.kent.displayGraph.Util.convertToInteger(0.1) != 0) {System.out.println("Test 16.3.11 FAIL");}
		if(uk.ac.kent.displayGraph.Util.convertToInteger(0.9) != 1) {System.out.println("Test 16.3.12 FAIL");}
		if(uk.ac.kent.displayGraph.Util.convertToInteger(10.51) != 11) {System.out.println("Test 16.3.13 FAIL");}
		if(uk.ac.kent.displayGraph.Util.convertToInteger(20.49) != 20) {System.out.println("Test 16.3.14 FAIL");}
		if(uk.ac.kent.displayGraph.Util.convertToInteger(-1.2) != -1) {System.out.println("Test 16.3.15 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(0,0);
		if(uk.ac.kent.displayGraph.Util.distance(p1,p2) != 0) {System.out.println("Test 16.4.1 FAIL");}
		p1 = new Point(0,0);
		p2 = new Point(0,10);
		if(uk.ac.kent.displayGraph.Util.distance(p1,p2) != 10) {System.out.println("Test 16.4.2 FAIL");}
		p1 = new Point(0,10);
		p2 = new Point(0,0);
		if(uk.ac.kent.displayGraph.Util.distance(p1,p2) != 10) {System.out.println("Test 16.4.3 FAIL");}
		p1 = new Point(-30,10);
		p2 = new Point(10,10);
		if(uk.ac.kent.displayGraph.Util.distance(p1,p2) != 40) {System.out.println("Test 16.4.4 FAIL");}
		p1 = new Point(100,100);
		p2 = new Point(103,96);
		if(uk.ac.kent.displayGraph.Util.distance(p1,p2) != 5) {System.out.println("Test 16.4.5 FAIL");}

		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0,0);
		if(uk.ac.kent.displayGraph.Util.distance(pd1,pd2) != 0) {System.out.println("Test 16.4.6 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0,10);
		if(uk.ac.kent.displayGraph.Util.distance(pd1,pd2) != 10) {System.out.println("Test 16.4.7 FAIL");}
		pd1 = new Point2D.Double(0,10);
		pd2 = new Point2D.Double(0,0);
		if(uk.ac.kent.displayGraph.Util.distance(pd1,pd2) != 10) {System.out.println("Test 16.4.8 FAIL");}
		pd1 = new Point2D.Double(-30,10);
		pd2 = new Point2D.Double(10,10);
		if(uk.ac.kent.displayGraph.Util.distance(pd1,pd2) != 40) {System.out.println("Test 16.4.9 FAIL");}
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(103,96);
		if(uk.ac.kent.displayGraph.Util.distance(pd1,pd2) != 5) {System.out.println("Test 16.4.10 FAIL");}
		pd1 = new Point2D.Double(100,100);
		pd2 = new Point2D.Double(100.1,100);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.distance(pd1,pd2),1) != 0.1) {System.out.println("Test 16.4.11 FAIL");}
		
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		if(uk.ac.kent.displayGraph.Util.distance(x1,y1,x2,y2) != 0) {System.out.println("Test 16.4.12 FAIL");}
		x1 = 0;
		y1 = -100;
		x2 = 0;
		y2 = 0;
		if(uk.ac.kent.displayGraph.Util.distance(x1,y1,x2,y2) != 100) {System.out.println("Test 16.4.13 FAIL");}
		x1 = 0;
		y1 = 0;
		x2 = 50;
		y2 = 0;
		if(uk.ac.kent.displayGraph.Util.distance(x1,y1,x2,y2) != 50) {System.out.println("Test 16.4.14 FAIL");}
		x1 = -4;
		y1 = 3;
		x2 = 0;
		y2 = 0;
		if(uk.ac.kent.displayGraph.Util.distance(x1,y1,x2,y2) != 5) {System.out.println("Test 16.4.15 FAIL");}
		x1 = 1;
		y1 = 2;
		x2 = 3;
		y2 = 4;
		if(uk.ac.kent.displayGraph.Util.distance(x1,y1,x2,y2) != Math.sqrt(8)) {System.out.println("Test 16.4.16 FAIL");}

		xd1 = 0.0;
		yd1 = 0.0;
		xd2 = 0.0;
		yd2 = 0.0;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != 0) {System.out.println("Test 16.4.17 FAIL");}

		xd1 = 0.0;
		yd1 = 0.0;
		xd2 = 0.0;
		yd2 = 0.1;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != 0.1) {System.out.println("Test 16.4.18 FAIL");}

		xd1 = -0.3;
		yd1 = 0.0;
		xd2 = 0.0;
		yd2 = 0.0;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != 0.3) {System.out.println("Test 16.4.19 FAIL");}

		xd1 = 0.0;
		yd1 = 4.0;
		xd2 = -3.0;
		yd2 = 0.0;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != 5.0) {System.out.println("Test 16.4.20 FAIL");}

		xd1 = 1.0;
		yd1 = -1.0;
		xd2 = -2.0;
		yd2 = 3.0;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != 5.0) {System.out.println("Test 16.4.21 FAIL");}

		xd1 = 0.0;
		yd1 = 0.0;
		xd2 = 1.0;
		yd2 = 1.0;
		if(uk.ac.kent.displayGraph.Util.distance(xd1,yd1,xd2,yd2) != Math.sqrt(2)) {System.out.println("Test 16.4.22 FAIL");}

		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		p3 = new Point(150,50);
		p4 = new Point(150,200);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4).equals(new Point(150,100))) {System.out.println("Test 16.5.1 FAIL");}
		p1 = new Point(-100,0);
		p2 = new Point(100,0);
		p3 = new Point(0,0);
		p4 = new Point(0,200);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4).equals(new Point(0,0))) {System.out.println("Test 16.5.2 FAIL");}
		p1 = new Point(-100,-100);
		p2 = new Point(100,100);
		p3 = new Point(-100,100);
		p4 = new Point(100,-100);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4).equals(new Point(0,0))) {System.out.println("Test 16.5.3 FAIL");}
		p1 = new Point(200,200);
		p2 = new Point(100,100);
		p3 = new Point(-100,100);
		p4 = new Point(100,-100);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4).equals(new Point(0,0))) {System.out.println("Test 16.5.4 FAIL");}
		p1 = new Point(0,0);
		p2 = new Point(0,100);
		p3 = new Point(100,0);
		p4 = new Point(100,100);
		if(uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4) != null) {System.out.println("Test 16.5.4a FAIL");}
		p1 = new Point(0,0);
		p2 = new Point(100,100);
		p3 = new Point(0,100);
		p4 = new Point(100,200);
		if(uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(p1,p2,p3,p4) != null) {System.out.println("Test 16.5.4b FAIL");}

		pd1 = new Point2D.Double(0.1,0.1);
		pd2 = new Point2D.Double(0.2,0.1);
		pd3 = new Point2D.Double(0.15,0.05);
		pd4 = new Point2D.Double(0.15,0.2);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4).equals(new Point2D.Double(0.15,0.1))) {System.out.println("Test 16.5.5 FAIL");}
		pd1 = new Point2D.Double(-0.1,0);
		pd2 = new Point2D.Double(0.1,0);
		pd3 = new Point2D.Double(0,0);
		pd4 = new Point2D.Double(0,0.2);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4).equals(new Point2D.Double(0,0))) {System.out.println("Test 16.5.6 FAIL");}
		pd1 = new Point2D.Double(-0.1,-0.1);
		pd2 = new Point2D.Double(0.1,0.1);
		pd3 = new Point2D.Double(-0.1,0.1);
		pd4 = new Point2D.Double(0.1,-0.1);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4).equals(new Point2D.Double(0,0))) {System.out.println("Test 16.5.7 FAIL");}
		pd1 = new Point2D.Double(0.2,0.2);
		pd2 = new Point2D.Double(0.1,0.1);
		pd3 = new Point2D.Double(-0.1,0.1);
		pd4 = new Point2D.Double(0.1,-0.1);
		if(!uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4).equals(new Point2D.Double(0,0))) {System.out.println("Test 16.5.8 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0,0.1);
		pd3 = new Point2D.Double(0.1,0);
		pd4 = new Point2D.Double(0.1,0.1);
		if(uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4)!= null) {System.out.println("Test 16.5.9 FAIL");}
		pd1 = new Point2D.Double(0,0);
		pd2 = new Point2D.Double(0.1,0);
		pd3 = new Point2D.Double(0,0.1);
		pd4 = new Point2D.Double(0.1,0.1);
		if(uk.ac.kent.displayGraph.Util.intersectionPointOfTwoLines(pd1,pd2,pd3,pd4)!= null) {System.out.println("Test 16.5.9 FAIL");}
		
		p0 = new Point(200,200);
		p1 = new Point(0,200);
		p2 = new Point(200,0);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(100,100))) {System.out.println("Test 16.6.1 FAIL");}
		p0 = new Point(100,100);
		p1 = new Point(300,200);
		p2 = new Point(200,200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(100,200))) {System.out.println("Test 16.6.2 FAIL");}
		p0 = new Point(-100,-100);
		p1 = new Point(-300,-200);
		p2 = new Point(-200,-200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(-100,-200))) {System.out.println("Test 16.6.3 FAIL");}
		p0 = new Point(100,100);
		p1 = new Point(200,300);
		p2 = new Point(200,200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(200,100))) {System.out.println("Test 16.6.4 FAIL");}
		p0 = new Point(-100,-100);
		p1 = new Point(-200,-300);
		p2 = new Point(-200,-200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(-200,-100))) {System.out.println("Test 16.6.5 FAIL");}
		p0 = new Point(100,100);
		p1 = new Point(1,200);
		p2 = new Point(-1,200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(100,200))) {System.out.println("Test 16.6.6 FAIL");}
		p0 = new Point(150,-150);
		p1 = new Point(200,-200);
		p2 = new Point(100,-100);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(p0,p1,p2).equals(new Point(150,-150))) {System.out.println("Test 16.6.7 FAIL");}

		pd0 = new Point2D.Double(0.2,0.2);
		pd1 = new Point2D.Double(0,0.2);
		pd2 = new Point2D.Double(0.2,0);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).equals(new Point2D.Double(0.1,0.1))) {System.out.println("Test 16.6.8 FAIL");}
		pd0 = new Point2D.Double(0.1,0.1);
		pd1 = new Point2D.Double(0.3,0.2);
		pd2 = new Point2D.Double(0.2,0.2);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).equals(new Point2D.Double(0.1,0.2))) {System.out.println("Test 16.6.9 FAIL");}
		pd0 = new Point2D.Double(-0.1,-0.1);
		pd1 = new Point2D.Double(-0.3,-0.2);
		pd2 = new Point2D.Double(-0.2,-0.2);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).equals(new Point2D.Double(-0.1,-0.2))) {System.out.println("Test 16.6.10 FAIL");}
		pd0 = new Point2D.Double(0.15,0.1);
		pd1 = new Point2D.Double(0.2,0.3);
		pd2 = new Point2D.Double(0.2,0.2);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).x,1) != 0.2 || uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).y,1) != 0.1) {System.out.println("Test 16.6.11 FAIL");}
		pd0 = new Point2D.Double(-0.15,-0.1);
		pd1 = new Point2D.Double(-0.2,-0.3);
		pd2 = new Point2D.Double(-0.2,-0.2);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).x,1) != -0.2 || uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).y,1) != -0.1) {System.out.println("Test 16.6.12 FAIL");}
		pd0 = new Point2D.Double(100,100);
		pd1 = new Point2D.Double(0.1,200);
		pd2 = new Point2D.Double(-0.1,200);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).equals(new Point2D.Double(100.0,200.0))) {System.out.println("Test 16.6.13 FAIL");}
		pd0 = new Point2D.Double(0.15,-0.15);
		pd1 = new Point2D.Double(0.2,-0.2);
		pd2 = new Point2D.Double(0.1,-0.1);
		if(!uk.ac.kent.displayGraph.Util.perpendicularPoint(pd0,pd1,pd2).equals(new Point2D.Double(0.15,-0.15))) {System.out.println("Test 16.6.14 FAIL");}
		
		
		p0 = new Point(100,100);
		p1 = new Point(0,0);
		p2 = new Point(200,200);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.1 FAIL");}

		p0 = new Point(300,300);
		p1 = new Point(0,0);
		p2 = new Point(200,200);
		if(uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.2 FAIL");}

		p0 = new Point(100,100);
		p1 = new Point(200,200);
		p2 = new Point(0,0);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.3 FAIL");}

		p0 = new Point(300,300);
		p1 = new Point(20,200);
		p2 = new Point(0,0);
		if(uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.4 FAIL");}

		p0 = new Point(100,100);
		p1 = new Point(0,200);
		p2 = new Point(200,0);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.5 FAIL");}

		p0 = new Point(300,300);
		p1 = new Point(0,200);
		p2 = new Point(200,0);
		if(uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.6 FAIL");}

		p0 = new Point(100,100);
		p1 = new Point(200,0);
		p2 = new Point(0,200);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.7 FAIL");}

		p0 = new Point(300,300);
		p1 = new Point(200,0);
		p2 = new Point(0,200);
		if(uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.8 FAIL");}

		p0 = new Point(200,0);
		p1 = new Point(200,0);
		p2 = new Point(0,200);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.9 FAIL");}

		p0 = new Point(200,0);
		p1 = new Point(0,200);
		p2 = new Point(200,0);
		if(!uk.ac.kent.displayGraph.Util.pointIsWithinBounds(p0,p1,p2)) {System.out.println("Test 16.7.10 FAIL");}

		p1 = new Point(0,0);
		p2 = new Point(0,200);
		if(!uk.ac.kent.displayGraph.Util.midPoint(p1,p2).equals(new Point(0,100))) {System.out.println("Test 16.8.1 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.midPoint(p2,p1).equals(new Point(0,100))) {System.out.println("Test 16.8.2 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(0,-200);
		if(!uk.ac.kent.displayGraph.Util.midPoint(p1,p2).equals(new Point(0,-100))) {System.out.println("Test 16.8.3 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.midPoint(p2,p1).equals(new Point(0,-100))) {System.out.println("Test 16.8.4 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(400,200);
		if(!uk.ac.kent.displayGraph.Util.midPoint(p1,p2).equals(new Point(200,100))) {System.out.println("Test 16.8.5 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.midPoint(p2,p1).equals(new Point(200,100))) {System.out.println("Test 16.8.6 FAIL");}
		
		pd1 = new Point2D.Double(-0.2,-0.3);
		pd2 = new Point2D.Double(0.2,0.3);
		if(!uk.ac.kent.displayGraph.Util.midPoint(pd1,pd2).equals(new Point2D.Double(0.0,0.0))) {System.out.println("Test 16.8.7 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.midPoint(pd2,pd1).equals(new Point2D.Double(0.0,0.0))) {System.out.println("Test 16.8.8 FAIL");}

		pd1 = new Point2D.Double(0.2,0.3);
		pd2 = new Point2D.Double(0.4,0.9);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.midPoint(pd1,pd2).getX(),2) != 0.3) {System.out.println("Test 16.8.9 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.midPoint(pd2,pd1).getX(),2) != 0.3) {System.out.println("Test 16.8.10 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.midPoint(pd1,pd2).getY(),2) != 0.6) {System.out.println("Test 16.8.11 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.midPoint(pd2,pd1).getY(),2) != 0.6) {System.out.println("Test 16.8.12 FAIL");}

		p1 = new Point(1,1);
		p2 = new Point(1,11);
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p1,p2,0.1).x != 1) {System.out.println("Test 16.8.13 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p2,p1,0.1).x != 1) {System.out.println("Test 16.8.14 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p1,p2,0.1).y != 2) {System.out.println("Test 16.8.15 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p2,p1,0.1).y != 10) {System.out.println("Test 16.8.16 FAIL");}

		p1 = new Point(0,0);
		p2 = new Point(-8,8);
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p1,p2,0.25).x != -2) {System.out.println("Test 16.8.17 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p2,p1,0.25).x != -6) {System.out.println("Test 16.8.18 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p1,p2,0.25).y != 2) {System.out.println("Test 16.8.19 FAIL");}
		if(uk.ac.kent.displayGraph.Util.betweenPoints(p2,p1,0.25).y != 6) {System.out.println("Test 16.8.20 FAIL");}
		
		pd1 = new Point2D.Double(0.20,0.10);
		pd2 = new Point2D.Double(0.30,-0.10);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd1,pd2,0.2).getX(),2) != 0.22) {System.out.println("Test 16.8.21 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd2,pd1,0.2).getX(),2) != 0.28) {System.out.println("Test 16.8.22 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd1,pd2,0.2).getY(),2) != 0.06) {System.out.println("Test 16.8.23 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd2,pd1,0.2).getY(),2) != -0.06) {System.out.println("Test 16.8.24 FAIL");}

		pd1 = new Point2D.Double(-0.20,0.30);
		pd2 = new Point2D.Double(-0.20,0.30);
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd1,pd2,0.2).getX(),2) != -0.2) {System.out.println("Test 16.8.25 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd2,pd1,0.2).getX(),2) != -0.2) {System.out.println("Test 16.8.26 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd1,pd2,0.2).getY(),2) != 0.3) {System.out.println("Test 16.8.27 FAIL");}
		if(uk.ac.kent.displayGraph.Util.round(uk.ac.kent.displayGraph.Util.betweenPoints(pd2,pd1,0.2).getY(),2) != 0.3) {System.out.println("Test 16.8.28 FAIL");}

		p1 = new Point(0,0);
		p2 = new Point(100,0);
		p3 = new Point(100,100);
		p4 = new Point(50,50);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(90)) {System.out.println("Test 16.9.1 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(90)) {System.out.println("Test 16.9.2 FAIL");}

		p4 = new Point(-250,200);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(90)) {System.out.println("Test 16.9.3 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(90)) {System.out.println("Test 16.9.4 FAIL");}

		p4 = new Point(-50,-50);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(270)) {System.out.println("Test 16.9.5 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(270)) {System.out.println("Test 16.9.6 FAIL");}
		
		p4 = new Point(-250,-250);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(270)) {System.out.println("Test 16.9.7 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(270)) {System.out.println("Test 16.9.8 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(100,0);
		p3 = new Point(200,0);
		p4 = new Point(50,50);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(180)) {System.out.println("Test 16.9.9 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(180)) {System.out.println("Test 16.9.10 FAIL");}

		p4 = new Point(-250,-250);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4)!= Math.toRadians(180)) {System.out.println("Test 16.9.11 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4)!= Math.toRadians(180)) {System.out.println("Test 16.9.12 FAIL");}
		
		p1 = new Point(205,188);
		p2 = new Point(321,191);
		p3 = new Point(188,65);
		p4 = new Point(333,201);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) < Math.toRadians(180)) {System.out.println("Test 16.9.13 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) < Math.toRadians(180)) {System.out.println("Test 16.9.14 FAIL");}
		
		p1 = new Point(50,0);
		p2 = new Point(0,0);
		p3 = new Point(50,50);
		p4 = new Point(100,200);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.15 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.16 FAIL");}

		p1 = new Point(50,0);
		p2 = new Point(0,0);
		p3 = new Point(50,50);
		p4 = new Point(200,100);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.17 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.18 FAIL");}
		
		p1 = new Point(-50,0);
		p2 = new Point(0,0);
		p3 = new Point(-50,-50);
		p4 = new Point(-100,-200);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) < Math.toRadians(315)) {System.out.println("Test 16.9.19 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) < Math.toRadians(315)) {System.out.println("Test 16.9.20 FAIL");}
		
		p1 = new Point(-50,0);
		p2 = new Point(0,0);
		p3 = new Point(-50,-50);
		p4 = new Point(-200,-100);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) < Math.toRadians(45)) {System.out.println("Test 16.9.21 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) < Math.toRadians(45)) {System.out.println("Test 16.9.22 FAIL");}
		
		p1 = new Point(0,50);
		p2 = new Point(0,0);
		p3 = new Point(50,50);
		p4 = new Point(200,100);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.23 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.24 FAIL");}
		
		p1 = new Point(0,50);
		p2 = new Point(0,0);
		p3 = new Point(50,50);
		p4 = new Point(100,200);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.25 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.26 FAIL");}
		
		p1 = new Point(0,-50);
		p2 = new Point(0,0);
		p3 = new Point(-50,-50);
		p4 = new Point(-200,-100);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.27 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(315)) {System.out.println("Test 16.9.28 FAIL");}
		
		p1 = new Point(0,-50);
		p2 = new Point(0,0);
		p3 = new Point(-50,-50);
		p4 = new Point(-100,-200);
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p1,p2,p3,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.29 FAIL");}
		if(uk.ac.kent.displayGraph.Util.getRelativeAngle(p3,p2,p1,p4) != Math.toRadians(45)) {System.out.println("Test 16.9.30 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(0,10);
		p3 = new Point(5,0);
		p4 = new Point(5,10);
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.1 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p2,p1)) {System.out.println("Test 16.10.2 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(10,10);
		p3 = new Point(50,100);
		p4 = new Point(100,150);
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.3 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p2,p1)) {System.out.println("Test 16.10.4 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p1,p3,p2,p4)) {System.out.println("Test 16.10.5 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p2,p4,p4,p1)) {System.out.println("Test 16.10.6 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(10,0);
		p3 = new Point(5,0);
		p4 = new Point(5,10);
		if(uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.7 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p2,p1)) {System.out.println("Test 16.10.8 FAIL");}
		
		p1 = new Point(0,0);
		p2 = new Point(10,10);
		p3 = new Point(50,100);
		p4 = new Point(100,151);
		if(uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.9 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p2,p1)) {System.out.println("Test 16.10.10 FAIL");}
		
		p1 = new Point(1,10);
		p2 = new Point(2,20);
		p3 = new Point(10,100);
		p4 = new Point(2,200);
		if(uk.ac.kent.displayGraph.Util.linesParallel(p2,p1,p3,p4)) {System.out.println("Test 16.10.11 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p1,p2)) {System.out.println("Test 16.10.12 FAIL");}
		
		p1 = new Point(2,1);
		p2 = new Point(14,12);
		p3 = new Point(65,6);
		p4 = new Point(45,100);
		if(uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.13 FAIL");}
		if(uk.ac.kent.displayGraph.Util.linesParallel(p3,p4,p2,p1)) {System.out.println("Test 16.10.14 FAIL");}
		
		p1 = new Point(339,220);
		p2 = new Point(362,234);
		p3 = new Point(339,220);
		p4 = new Point(316,206);
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p1,p2,p3,p4)) {System.out.println("Test 16.10.15 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.linesParallel(p4,p3,p1,p2)) {System.out.println("Test 16.10.16 FAIL");}
		
		
		if(!uk.ac.kent.displayGraph.Util.reverseString("").equals("")) {System.out.println("Test 16.11.1 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.reverseString("a").equals("a")) {System.out.println("Test 16.11.12 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.reverseString("ab").equals("ba")) {System.out.println("Test 16.11.13 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.reverseString("bab").equals("bab")) {System.out.println("Test 16.11.14 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.reverseString("abcde").equals("edcba")) {System.out.println("Test 16.11.15 FAIL");}
		if(!uk.ac.kent.displayGraph.Util.reverseString("aaabbb").equals("bbbaaa")) {System.out.println("Test 16.11.16 FAIL");}

		p1 = new Point(100,100);
		p2 = new Point(200,200);
		p3 = new Point(150,150);
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p1, p2) != 0.0) {System.out.println("Test 16.12.1 FAIL");}
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p2, p1) != 0.0) {System.out.println("Test 16.12.2 FAIL");}
		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		p3 = new Point(130,100);
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p1, p2) != 0.0) {System.out.println("Test 16.12.3 FAIL");}
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p2, p1) != 0.0) {System.out.println("Test 16.12.4 FAIL");}
		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		p3 = new Point(130,110);

		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p1, p2) != 10.0) {System.out.println("Test 16.12.5 FAIL");}
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p2, p1) != 10.0) {System.out.println("Test 16.12.6 FAIL");}
		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		p3 = new Point(70,140);
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p1, p2) != 50.0) {System.out.println("Test 16.12.7 FAIL");}
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p2, p1) != 50.0) {System.out.println("Test 16.12.8 FAIL");}
		
		p1 = new Point(100,100);
		p2 = new Point(200,100);
		p3 = new Point(300,100);
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p1, p2) != 100.0) {System.out.println("Test 16.12.7 FAIL");}
		if(uk.ac.kent.displayGraph.Util.pointLineDistance(p3, p2, p1) != 100.0) {System.out.println("Test 16.12.8 FAIL");}
		
		System.out.print("Test 16 END ");
		
		
		
		System.out.println("| Test 19 START: graph scaling");

		Graph g;
		Point p;
		
		Node n1,n2,n3;
		Edge e1,e2;
		
		g = new Graph();
		if(g.findWidth() != 0) {System.out.println("Test 19.1.1 FAIL");}
		if(g.findHeight() != 0) {System.out.println("Test 19.1.2 FAIL");}
		if(g.findMinimumX() != 0) {System.out.println("Test 19.1.2a FAIL");}
		if(g.findMinimumY() != 0) {System.out.println("Test 19.1.2b FAIL");}
		n1 = new Node("a",new Point(10,10));
		g.addNode(n1);
		if(g.findWidth() != 0) {System.out.println("Test 19.1.3 FAIL");}
		if(g.findHeight() != 0) {System.out.println("Test 19.1.4 FAIL");}
		if(g.findMinimumX() != 10) {System.out.println("Test 19.1.4a FAIL");}
		if(g.findMinimumY() != 10) {System.out.println("Test 19.1.4b FAIL");}
		n2 = new Node("b",new Point(20,30));
		g.addNode(n2);
		if(g.findWidth() != 10) {System.out.println("Test 19.1.5 FAIL");}
		if(g.findHeight() != 20) {System.out.println("Test 19.1.6 FAIL");}
		if(g.findMinimumX() != 10) {System.out.println("Test 19.1.6a FAIL");}
		if(g.findMinimumY() != 10) {System.out.println("Test 19.1.6b FAIL");}
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		if(g.findWidth() != 10) {System.out.println("Test 19.1.7 FAIL");}
		if(g.findHeight() != 20) {System.out.println("Test 19.1.8 FAIL");}
		if(g.findMinimumX() != 10) {System.out.println("Test 19.1.8a FAIL");}
		if(g.findMinimumY() != 10) {System.out.println("Test 19.1.8b FAIL");}
		n3 = new Node("b",new Point(15,20));
		g.addNode(n3);
		e2 = new Edge(n1,n3);
		g.addEdge(e2);
		if(g.findWidth() != 10) {System.out.println("Test 19.1.9 FAIL");}
		if(g.findHeight() != 20) {System.out.println("Test 19.1.10 FAIL");}
		if(g.findMinimumX() != 10) {System.out.println("Test 19.1.10a FAIL");}
		if(g.findMinimumY() != 10) {System.out.println("Test 19.1.10b FAIL");}
		e2.addBend(new Point(12,18));
		if(g.findWidth() != 10) {System.out.println("Test 19.1.11 FAIL");}
		if(g.findHeight() != 20) {System.out.println("Test 19.1.12 FAIL");}
		if(g.findMinimumX() != 10) {System.out.println("Test 19.1.12a FAIL");}
		if(g.findMinimumY() != 10) {System.out.println("Test 19.1.12b FAIL");}
		e2.addBend(new Point(0,0));
		if(g.findWidth() != 20) {System.out.println("Test 19.1.13 FAIL");}
		if(g.findHeight() != 30) {System.out.println("Test 19.1.14 FAIL");}
		if(g.findMinimumX() != 00) {System.out.println("Test 19.1.14a FAIL");}
		if(g.findMinimumY() != 00) {System.out.println("Test 19.1.14b FAIL");}
		
		e2.addBend(new Point(-5,-10));
		if(g.findWidth() != 25) {System.out.println("Test 19.1.15 FAIL");}
		if(g.findHeight() != 40) {System.out.println("Test 19.1.16 FAIL");}
		if(g.findMinimumX() != -5) {System.out.println("Test 19.1.17 FAIL");}
		if(g.findMinimumY() != -10) {System.out.println("Test 19.1.18 FAIL");}
		
		
		g = new Graph();
		g.scale(1.0);
		n1 = new Node("a",new Point(10,10));
		g.addNode(n1);
		g.scale(0.1);
		if(n1.getX() != 10) {System.out.println("Test 19.3.1 FAIL");}
		if(n1.getY() != 10) {System.out.println("Test 19.3.2 FAIL");}
		n2 = new Node("b",new Point(30,30));
		g.addNode(n2);
		g.scale(1.0);
		if(n1.getX() != 10) {System.out.println("Test 19.3.3 FAIL");}
		if(n1.getY() != 10) {System.out.println("Test 19.3.4 FAIL");}
		if(n2.getX() != 30) {System.out.println("Test 19.3.5 FAIL");}
		if(n2.getY() != 30) {System.out.println("Test 19.3.6 FAIL");}
		g.scale(0.5);
		if(n1.getX() != 15) {System.out.println("Test 19.3.7 FAIL");}
		if(n1.getY() != 15) {System.out.println("Test 19.3.8 FAIL");}
		if(n2.getX() != 25) {System.out.println("Test 19.3.9 FAIL");}
		if(n2.getY() != 25) {System.out.println("Test 19.3.10 FAIL");}
		g.scale(2.0);
		if(n1.getX() != 10) {System.out.println("Test 19.3.11 FAIL");}
		if(n1.getY() != 10) {System.out.println("Test 19.3.12 FAIL");}
		if(n2.getX() != 30) {System.out.println("Test 19.3.13 FAIL");}
		if(n2.getY() != 30) {System.out.println("Test 19.3.14 FAIL");}
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		e1.addBend(new Point(50,50));
		g.scale(0.5);
		if(n1.getX() != 20) {System.out.println("Test 19.3.15 FAIL");}
		if(n1.getY() != 20) {System.out.println("Test 19.3.16 FAIL");}
		if(n2.getX() != 30) {System.out.println("Test 19.3.17 FAIL");}
		if(n2.getY() != 30) {System.out.println("Test 19.3.18 FAIL");}
		p = e1.getBends().get(0);
		if(p.x != 40) {System.out.println("Test 19.3.19 FAIL");}
		if(p.y != 40) {System.out.println("Test 19.3.20 FAIL");}
		g.scale(2.0);
		if(n1.getX() != 10) {System.out.println("Test 19.3.21 FAIL");}
		if(n1.getY() != 10) {System.out.println("Test 19.3.22 FAIL");}
		if(n2.getX() != 30) {System.out.println("Test 19.3.23 FAIL");}
		if(n2.getY() != 30) {System.out.println("Test 19.3.24 FAIL");}
		p = e1.getBends().get(0);
		if(p.x != 50) {System.out.println("Test 19.3.25 FAIL");}
		if(p.y != 50) {System.out.println("Test 19.3.26 FAIL");}
		
		g = new Graph();
		g.centreOnPoint(20,30);
		n1 = new Node("a",new Point(10,10));
		g.addNode(n1);
		g.centreOnPoint(20,30);
		if(n1.getX() != 20) {System.out.println("Test 19.4.1 FAIL");}
		if(n1.getY() != 30) {System.out.println("Test 19.4.2 FAIL");}
		
		n2 = new Node("b",new Point(100,90));
		g.addNode(n2);
		g.centreOnPoint(200,200);
		if(n1.getX() != 160) {System.out.println("Test 19.4.3 FAIL");}
		if(n1.getY() != 170) {System.out.println("Test 19.4.4 FAIL");}
		if(n2.getX() != 240) {System.out.println("Test 19.4.5 FAIL");}
		if(n2.getY() != 230) {System.out.println("Test 19.4.6 FAIL");}
		
		//TODO check edge bends
		
		g = new Graph();
		g.fitInRectangle(0,0,100,100);
		n1 = new Node("a",new Point(6,7));
		g.addNode(n1);
		g.fitInRectangle(0,0,200,400);
		if(n1.getX() != 100) {System.out.println("Test 19.5.1 FAIL");}
		if(n1.getY() != 200) {System.out.println("Test 19.5.2 FAIL");}
		g.fitInRectangle(200,200,0,0);
		if(n1.getX() != 100) {System.out.println("Test 19.5.3 FAIL");}
		if(n1.getY() != 100) {System.out.println("Test 19.5.4 FAIL");}
		
		g = new Graph();
		n1 = new Node("a",new Point(0,200));
		n2 = new Node("b",new Point(200,0));
		g.addNode(n1);
		g.addNode(n2);
		g.fitInRectangle(0,0,200,200);
		if(n1.getX() != 0) {System.out.println("Test 19.5.5 FAIL");}
		if(n1.getY() != 200) {System.out.println("Test 19.5.6 FAIL");}
		if(n2.getX() != 200) {System.out.println("Test 19.5.7 FAIL");}
		if(n2.getY() != 0) {System.out.println("Test 19.5.8 FAIL");}
		g.fitInRectangle(150,220,110,200);
		if(n1.getX() != 120) {System.out.println("Test 19.5.9 FAIL");}
		if(n1.getY() != 220) {System.out.println("Test 19.5.10 FAIL");}
		if(n2.getX() != 140) {System.out.println("Test 19.5.11 FAIL");}
		if(n2.getY() != 200) {System.out.println("Test 19.5.12 FAIL");}
		g.fitInRectangle(1,0,7,8);
		if(n1.getX() != 1) {System.out.println("Test 19.5.13 FAIL");}
		if(n1.getY() != 7) {System.out.println("Test 19.5.14 FAIL");}
		if(n2.getX() != 7) {System.out.println("Test 19.5.15 FAIL");}
		if(n2.getY() != 1) {System.out.println("Test 19.5.16 FAIL");}
	
		System.out.print("Test 19 END ");	
	}
	
	
	public static void testF() {

		
		System.out.println("| Test 21 START: Edge Crossings");
		
		Graph g;
		Node n1,n2,n3,n4,n5,n6;
		Edge e1,e2,e3,e4,e5,e6,e7;
		Edge[] pair;
		Point p1,p2,p3,p4;
		
		p1 = new Point(200,200);
		p2 = new Point(400,400);
		p3 = new Point(400,200);
		p4 = new Point(200,400);
		if(!uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p3, p4)) {System.out.println("Test 21.1.1 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p2, p1, p3, p4)) {System.out.println("Test 21.1.2 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p4, p3)) {System.out.println("Test 21.1.3 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p2, p1, p4, p3)) {System.out.println("Test 21.1.4 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p3, p4, p1, p2)) {System.out.println("Test 21.1.5 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p3, p4, p2, p1)) {System.out.println("Test 21.1.6 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p4, p3, p1, p2)) {System.out.println("Test 21.1.7 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p4, p3, p2, p1)) {System.out.println("Test 21.1.8 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p1, p3, p2, p4)) {System.out.println("Test 21.1.9 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p4, p1, p3, p2)) {System.out.println("Test 21.1.10 Fail");}
		p1 = new Point(200,200);
		p2 = new Point(400,200);
		p3 = new Point(300,100);
		p4 = new Point(300,500);
		if(!uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p3, p4)) {System.out.println("Test 21.1.11 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p4, p3, p2, p1)) {System.out.println("Test 21.1.12 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p1, p4)) {System.out.println("Test 21.1.13 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p4, p2, p2, p1)) {System.out.println("Test 21.1.14 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p1, p3, p2, p4)) {System.out.println("Test 21.1.15 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p4, p1, p3, p2)) {System.out.println("Test 21.1.16 Fail");}
		
		if(!uk.ac.kent.displayGraph.Util.linesCross(p2, p1, p1, p2)) {System.out.println("Test 21.1.17 Fail");}
		if(!uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p1, p2)) {System.out.println("Test 21.1.18 Fail");}
		
		p1 = new Point(200,200);
		p2 = new Point(400,200);
		p3 = new Point(300,300);
		p4 = new Point(500,300);
		if(uk.ac.kent.displayGraph.Util.linesCross(p1, p2, p3, p4)) {System.out.println("Test 21.1.17 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p4, p3, p2, p1)) {System.out.println("Test 21.1.18 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p2, p1, p4, p3)) {System.out.println("Test 21.1.19 Fail");}
		if(uk.ac.kent.displayGraph.Util.linesCross(p3, p4, p1, p2)) {System.out.println("Test 21.1.20 Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		e4 = new Edge(n1,n2);
		if(e4.crossesLine(new Point(30,40), new Point(50,60))) {System.out.println("Test 21.1a.1 Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		e4 = new Edge(n1,n2);
		if(!e4.crossesLine(new Point(30,40), new Point(40,30))) {System.out.println("Test 21.1a.2 Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		e4 = new Edge(n1,n2);
		if(!e4.crossesLine(new Point(20,20), new Point(0,0))) {System.out.println("Test 21.1a.3 Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		e4 = new Edge(n1,n2);
		if(!e4.crossesLine(new Point(40,40), new Point(60,0))) {System.out.println("Test 21.1a.4 Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		e4 = new Edge(n1,n2);
		if(e4.crossesEdge(e4)) {System.out.println("Test 21.1b.1 Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(60,60));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n3);
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.2 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.2a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(60,60));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n3,n2);
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.3 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.3a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(60,60));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n1,n3);
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.4 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.4a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(60,60));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n3,n2);
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.5 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.5a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(60,60));
		n4 = new Node("n4");
		n4.setCentre(new Point(40,40));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.6 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.6a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(30,35));
		n4 = new Node("n4");
		n4.setCentre(new Point(30,15));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.7 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.7a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		n3 = new Node("n3");
		n3.setCentre(new Point(50,35));
		n4 = new Node("n4");
		n4.setCentre(new Point(50,15));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.8 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.8a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(10,20));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(-10,10));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.8 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.8a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(10,20));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,10));
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.9 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.9a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(10,20));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,10));
		e5.addBend(new Point(10,15));
		e5.addBend(new Point(-10,15));
		e5.addBend(new Point(10,20));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.10 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.10a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(10,20));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,10));
		e5.addBend(new Point(10,15));
		e5.addBend(new Point(10,17));
		e5.addBend(new Point(10,20));
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.10 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.10a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(-10,10));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,15));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.11 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.11a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(-10,10));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(-40,15));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.12 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.12a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(-10,10));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,15));
		e5.addBend(new Point(-40,15));
		e5.addBend(new Point(-40,20));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.12 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.12a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(-10,10));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(-40,15));
		e5.addBend(new Point(-40,17));
		e5.addBend(new Point(-40,20));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.13 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.13a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(0,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(0,100));
		n3 = new Node("n3");
		n3.setCentre(new Point(10,10));
		n4 = new Node("n4");
		n4.setCentre(new Point(-10,10));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n4,n3);
		e5.addBend(new Point(40,15));
		e5.addBend(new Point(40,17));
		e5.addBend(new Point(40,20));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.14 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.14a Fail");}
		
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,0));
		n3 = new Node("n3");
		n3.setCentre(new Point(200,-50));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n1,n3);
		e4.addBend(new Point(150,-50));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.15 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.15a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,0));
		n3 = new Node("n3");
		n3.setCentre(new Point(200,-50));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n3,n1);
		e4.addBend(new Point(150,-50));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.16 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.16a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,0));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,0));
		n3 = new Node("n3");
		n3.setCentre(new Point(200,50));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n3,n1);
		e4.addBend(new Point(150,-50));
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.17 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.17a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(100,50));
		e4.addBend(new Point(200,150));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.18 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.18a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n2,n1);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(100,50));
		e4.addBend(new Point(200,150));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.19 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.19a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(100,50));
		e4.addBend(new Point(200,150));
		e5.addBend(new Point(200,50));
		e5.addBend(new Point(100,150));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.20 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.20a Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(100,50));
		e4.addBend(new Point(200,150));
		e5.addBend(new Point(200,50));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.21 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.21a Fail");}

		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(100,50));
		e5.addBend(new Point(200,50));
		if(!e4.crossesEdge(e5)) {System.out.println("Test 21.1b.22 Fail");}
		if(!e5.crossesEdge(e4)) {System.out.println("Test 21.1b.22a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e5 = new Edge(n2,n1);
		e4.addBend(new Point(200,150));
		e5.addBend(new Point(200,50));
		if(e4.crossesEdge(e5)) {System.out.println("Test 21.1b.23 Fail");}
		if(e5.crossesEdge(e4)) {System.out.println("Test 21.1b.23a Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e4.addBend(new Point(200,200));
		e4.addBend(new Point(100,200));
		if(!e4.crossesEdge(e4)) {System.out.println("Test 21.1b.24 Fail");}
		
		
		n1 = new Node("n1");
		n2 = new Node("n2");
		n1.setCentre(new Point(300,300));
		n2.setCentre(new Point(300,400));
		e4 = new Edge(n1,n2);
		e4.addBend(new Point(150,250));
		if(e4.crossesEdge(e4)) {System.out.println("Test 21.1b.25 Fail");}
		
		
		
		g = new Graph();
		if(g.findEdgeCrossings().size() != 0) {System.out.println("Test 21.2.1 Fail");}
		
		n1 = new Node("n1");
		n1.setCentre(new Point(20,20));
		n2 = new Node("n2");
		n2.setCentre(new Point(40,40));
		g.addNode(n1);
		g.addNode(n2);
		if(g.findEdgeCrossings().size() != 0) {System.out.println("Test 21.2.2 Fail");}
		e1 = new Edge(n1,n2);
		g.addEdge(e1);
		if(g.findEdgeCrossings().size() != 0) {System.out.println("Test 21.2.3 Fail");}
		n3 = new Node("n3");
		n3.setCentre(new Point(20,30));
		n4 = new Node("n4");
		n4.setCentre(new Point(40,50));
		g.addNode(n3);
		g.addNode(n4);
		e2 = new Edge(n3,n4);
		g.addEdge(e2);
		if(g.findEdgeCrossings().size() != 0) {System.out.println("Test 21.2.4 Fail");}
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(40,40));
		n3.setCentre(new Point(40,0));
		n4.setCentre(new Point(0,40));
		if(g.findEdgeCrossings().size() != 1) {System.out.println("Test 21.2.5 Fail");}
		pair = g.findEdgeCrossings().get(0);
		if(!((pair[0] == e1 && pair[1] == e2) || (pair[0] == e1 && pair[1] == e2)))  {System.out.println("Test 21.1.5a Fail");}
		e3 = new Edge(n1,n2);
		g.addEdge(e3);
		if(g.findEdgeCrossings().size() != 2) {System.out.println("Test 21.2.6 Fail");}
		e4 = new Edge(n1,n3);
		g.addEdge(e4);
		if(g.findEdgeCrossings().size() != 2) {System.out.println("Test 21.2.7 Fail");}
		e5 = new Edge(n4,n2);
		e6 = new Edge(n2,n1);
		g.addEdge(e5);
		g.addEdge(e6);
		if(g.findEdgeCrossings().size() != 3) {System.out.println("Test 21.2.8 Fail");}
		e7 = new Edge(n4,n2);
		g.addEdge(e7);
		if(g.findEdgeCrossings().size() != 3) {System.out.println("Test 21.2.9 Fail");}
		
		g = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		n1.setCentre(new Point(304,152));
		n2.setCentre(new Point(118,87));
		n3.setCentre(new Point(119,224));
		n4.setCentre(new Point(165,245));
		n5.setCentre(new Point(208,145));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n5,n3));
		g.addEdge(new Edge(n4,n5));
		if(g.findEdgeCrossings().size() != 0) {System.out.println("Test 21.2.10 Fail");}
		
		g = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		n1.setCentre(new Point(300,300));
		n2.setCentre(new Point(300,400));
		n3.setCentre(new Point(0,0));
		n4.setCentre(new Point(200,300));
		n5.setCentre(new Point(200,400));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		e4 = new Edge(n1,n2);
		e4.addBend(new Point(150,250));
		g.addEdge(e4);
		g.addEdge(new Edge(n5,n3));
		g.addEdge(new Edge(n4,n5));
		if(g.findEdgeCrossings().size() != 1) {System.out.println("Test 21.2.10a Fail");}

		
		g = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n5 = new Node("n5");
		n6 = new Node("n6");
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(0,100));
		n3.setCentre(new Point(100,100));
		n4.setCentre(new Point(100,0));
		n5.setCentre(new Point(0,70));
		n6.setCentre(new Point(170,70));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		g.addEdge(new Edge(n1,n3));
		g.addEdge(new Edge(n2,n4));
		g.addEdge(new Edge(n6,n5));
		if(g.findEdgeCrossings().size() != 3) {System.out.println("Test 21.2.11 Fail");}
		
		g = new Graph();
		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n1.setCentre(new Point(0,0));
		n2.setCentre(new Point(0,100));
		n3.setCentre(new Point(0,0));
		n4.setCentre(new Point(100,0));
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addEdge(new Edge(n1,n2));
		g.addEdge(new Edge(n3,n4));
		if(g.findEdgeCrossings().size() != 1) {System.out.println("Test 21.2.12 Fail");}
		
		g = new Graph();
		n1 = new Node("n1");
		n1.setCentre(new Point(100,100));
		n2 = new Node("n2");
		n2.setCentre(new Point(200,100));
		e4 = new Edge(n1,n2);
		e4.addBend(new Point(200,200));
		e4.addBend(new Point(100,200));
		g.addNode(n1);
		g.addNode(n2);
		g.addEdge(e4);
		if(g.findEdgeCrossings().size() != 1) {System.out.println("Test 21.2.13 Fail");}

		
		System.out.print("Test 21 END ");
		
		System.out.println("| Test 22 START: finding nodes in a graph");
		
		g = new Graph();
		if(g.firstNodeWithLabel("") != null) {System.out.println("Test 22.1.1 Fail");}
		if(g.firstNodeWithLabel("a") != null) {System.out.println("Test 22.1.2 Fail");}
		if(g.firstNodeAtPoint(new Point(100,100)) != null) {System.out.println("Test 22.1.3 Fail");}
		n1 = new Node("a");
		n1.setCentre(new Point(100,100));
		g.addNode(n1);
		if(g.firstNodeWithLabel("") != null) {System.out.println("Test 22.1.4 Fail");}
		if(g.firstNodeWithLabel("b") != null) {System.out.println("Test 22.1.5 Fail");}
		if(g.firstNodeWithLabel("a") != n1) {System.out.println("Test 22.1.6 Fail");}
		if(g.firstNodeAtPoint(new Point(50,50)) != null)  {System.out.println("Test 22.1.7 Fail");}
		if(g.firstNodeAtPoint(new Point(100,100)) != n1)  {System.out.println("Test 22.1.8 Fail");}
		n2 = new Node("b");
		n2.setCentre(new Point(50,50));
		g.addNode(n2);
		if(g.firstNodeWithLabel("") != null) {System.out.println("Test 22.1.9 Fail");}
		if(g.firstNodeWithLabel("b") != n2) {System.out.println("Test 22.1.10 Fail");}
		if(g.firstNodeWithLabel("a") != n1) {System.out.println("Test 22.1.11 Fail");}
		if(g.firstNodeAtPoint(new Point(50,50)) != n2)  {System.out.println("Test 22.1.12 Fail");}
		if(g.firstNodeAtPoint(new Point(100,100)) != n1)  {System.out.println("Test 22.1.13 Fail");}
		if(g.firstNodeAtPoint(new Point(200,200)) != null)  {System.out.println("Test 22.1.14 Fail");}
		n3 = new Node("a");
		n3.setCentre(new Point(50,50));
		g.addNode(n3);
		if(g.firstNodeWithLabel("c") != null) {System.out.println("Test 22.1.15 Fail");}
		if(g.firstNodeWithLabel("b") != n2) {System.out.println("Test 22.1.16 Fail");}
		if(g.firstNodeWithLabel("a") != n1 && g.firstNodeWithLabel("a") != n3) {System.out.println("Test 22.1.17 Fail");}
		if(g.firstNodeAtPoint(new Point(100,100)) != n1)  {System.out.println("Test 22.1.18 Fail");}
		if(g.firstNodeAtPoint(new Point(50,50)) != n2 && g.firstNodeAtPoint(new Point(100,100)) != n3)  {System.out.println("Test 22.1.19 Fail");}
		if(g.firstNodeAtPoint(new Point(200,200)) != null)  {System.out.println("Test 22.1.20 Fail");}

		
		System.out.print("Test 22 END ");
		

	}
	
	

}


