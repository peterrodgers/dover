package test.uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.GedevoNativeUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.kent.dover.fastGraph.EnumerateSubgraphNeighbourhood;
import uk.ac.kent.dover.fastGraph.FastGraph;
import uk.ac.kent.dover.fastGraph.GedUtil;
import uk.ac.kent.dover.fastGraph.KMedoids;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by dw3 on 13/02/2017.
 */
public class GedTest {

    @BeforeClass
    public static void loadNativeLib() {
        boolean loaded = GedevoNativeUtil.initNativeLibs();
        if (!loaded) {
            fail();
        }
    }


    @Test
    // Test getting GED score of two directed graphs
    public void test001() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(5,5,true);
            g1 = FastGraph.randomGraphFactory(5,5,true);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test getting GED score of two undirected graphs
    public void test002() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(5, 5, false);
            g1 = FastGraph.randomGraphFactory(5, 5, false);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test getting GED score of an undirected and directed graph
    public void test003() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(5, 5, true);
            g1 = FastGraph.randomGraphFactory(5, 5, false);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }


    @Test
    // Test differently sized directed graphs
    public void test004() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(7, 9, true);
            g1 = FastGraph.randomGraphFactory(5, 5, true);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test differently sized undirected graphs
    public void test005() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(7, 9, false);
            g1 = FastGraph.randomGraphFactory(5, 5, false);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test differently sized directed and undirected graph
    public void test006() {
        FastGraph g0;
        FastGraph g1;
        try {
            g0 = FastGraph.randomGraphFactory(7, 9, true);
            g1 = FastGraph.randomGraphFactory(5, 5, false);
            float score = GedUtil.getGedScore(g0, g1);
            assertTrue(score<=1 && score>=0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test Kmedoids of a directed graph
    public void test007() {
        FastGraph g;
        try {
            g = FastGraph.randomGraphFactory(10,15,true);
            KMedoids km = new KMedoids(g, 5, 100);
            EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(g);
            HashSet<FastGraph> subs = esn.enumerateSubgraphs(4, 2, 10);
            System.out.println("subs: " + subs.size());

            ArrayList<FastGraph> subgraphs = new ArrayList<FastGraph>(subs);
            km.cluster(subgraphs);

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    // Test Kmedoids of an undirected graph
    public void test008() {
        FastGraph g;
        try {
            g = FastGraph.randomGraphFactory(10,15,false);
            KMedoids km = new KMedoids(g, 5, 100);
            EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(g);
            HashSet<FastGraph> subs = esn.enumerateSubgraphs(4, 2, 10);
            System.out.println("subs: " + subs.size());

            ArrayList<FastGraph> subgraphs = new ArrayList<FastGraph>(subs);
            km.cluster(subgraphs);

        } catch (Exception e) {
            fail();
        }
    }
}
