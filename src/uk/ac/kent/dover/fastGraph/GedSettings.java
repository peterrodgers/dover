package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.UserSettings;

/**
 * Created by dw3 on 07/12/2016.
 */
public class GedSettings extends UserSettings {
    // Algorithm settings
    public double weightPairsum = 1;
    public double weightGraphlets = 0;
    public double weightGED = 0;

    public double pairWeightGraphlets = 0.5;
    public double pairWeightGED = 1;

    public float basicHealth = 100;
    public float maxHealthDrop = 100;

    public double pairNullValue = 1;

    public boolean forceUndirectedEdges = true;
    public boolean matchSameNames = false;
    public boolean trimNames = false;
    public boolean ignoreSelfLoops = false;

    public double ged_eAdd = 1.0;
    public double ged_eRm = 1.0;
    public double ged_eSub = 0.0;
    public double ged_eFlip = 0.8;
    public double ged_eD2U = 0.2;
    public double ged_eU2D = 0.2;
    public double ged_nAdd = 0.0;
    public double ged_nRm = 0.0;

    public boolean evo_greedyInitOnInit = false;

    // varGroup is not needed, can leave this at default

    public int maxAgents = 400;

    public int abort_seconds = 10;
    public int abort_iterations = 100;
    public int abort_nochange = 10;

    public int numThreads = 0;
    public int autosaveSecs = DEFAULT_AUTOSAVE_TIME;
    public boolean keepWorkfiles = false; // they would be just in the way

    int logger_iterations = 0;
    String logger_file = "";

    String saveResultsFileName = "";
    boolean saveResultsAddTimeStamp;

}
