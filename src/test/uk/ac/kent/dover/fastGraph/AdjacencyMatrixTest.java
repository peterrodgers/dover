package test.uk.ac.kent.dover.fastGraph;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import test.uk.ac.kent.dover.TestRunner;
import uk.ac.kent.dover.fastGraph.AdjacencyMatrix;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.Util;

public class AdjacencyMatrixTest {

	@Test
	public void test001() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.deepToString(new int[][]{{0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.deepToString(new int[][]{{0,1},{1,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{2,1},{1,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{0,1,1,0},{1,0,1,1},{1,1,0,1},{0,1,1,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node5Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{0,1,1,0,0},{1,0,1,0,0},{1,1,0,1,0},{0,0,1,0,1},{0,0,0,1,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get5Node4Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{0,1,1,0,0},{1,0,1,0,0},{1,1,0,0,0},{0,0,0,0,1},{0,0,0,1,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntAdjacencyMatrix()));
	
	}
	
	@Test
	public void test001b() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.deepToString(new boolean[][]{{false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.deepToString(new boolean[][]{{false,true},{true,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(Arrays.deepToString(new boolean[][]{{true,true},{true,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(Arrays.deepToString(new boolean[][]{{false,true,true,false},{true,false,true,true},{true,true,false,true},{false,true,true,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanAdjacencyMatrix()));
	}
 
	@Test
	public void test002() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.deepToString(new int[][]{{0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.deepToString(new int[][]{{0,1},{0,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{1,1},{0,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(Arrays.deepToString(new int[][]{{0,1,1,0},{0,0,0,1},{0,1,0,1},{0,0,0,0}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix()));
	}
	
	@Test
	public void test002b() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.deepToString(new boolean[][]{{false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.deepToString(new boolean[][]{{false,true},{false,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),true);
		assertEquals(Arrays.deepToString(new boolean[][]{{true,true},{false,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix()));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),true);
		assertEquals(Arrays.deepToString(new boolean[][]{{false,true,true,false},{false,false,false,true},{false,true,false,true},{false,false,false,false}}),Arrays.deepToString((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix()));
	}
	
	
	@Test
	public void test003() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.toString(new double[]{0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,1}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,0,0,0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildIntDirectedAdjacencyMatrix())));
	}
	
	@Test
	public void test003b() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.toString(new double[]{0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node1Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get2Node2Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,1}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix())));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get4Node5Edge(),false);
		assertEquals(Arrays.toString(new double[]{0,0,0,0}),Arrays.toString((new AdjacencyMatrix(g)).findEigenvalues((new AdjacencyMatrix(g)).buildBooleanDirectedAdjacencyMatrix())));
	}
	
	@Test
	public void test003c() {
		FastGraph g;
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.toString(new int[]{0,3,3,5,5}),Arrays.toString(Util.roundArray((new AdjacencyMatrix(g)).findEigenvalues(new int[][]{{4,-1,-1,-1,-1},{-1,3,-1,0,1},{-1,-1,3,-1,0},{-1,0,-1,3,-1},{-1,-1,0,-1,3}}))));
		g = FastGraph.jsonStringGraphFactory(TestRunner.get1Node0Edge(),false);
		assertEquals(Arrays.toString(new int[]{-2,-1,1,2}),Arrays.toString(Util.roundArray((new AdjacencyMatrix(g)).findEigenvalues(new int[][]{{0,1,0,0},{1,0,1,0},{0,1,0,1},{0,0,1,0}}))));
	}

}
