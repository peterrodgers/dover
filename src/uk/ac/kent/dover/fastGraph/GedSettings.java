package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.UserSettings;

/**
 * Created by dw3 on 07/12/2016.
 */
public class GedSettings extends UserSettings {

    /**
     * This constructor allows the values of the superclass to be overriden
     */
    public GedSettings() {
        // Algorithm settings
        weightPairsum = 1;
        weightGraphlets = 0;
        weightGED = 0;

        pairWeightGraphlets = 0.5;
        pairWeightGED = 1;

        basicHealth = 100;
        maxHealthDrop = 100;

        pairNullValue = 1;

        forceUndirectedEdges = true;
        matchSameNames = false;
        trimNames = false;
        ignoreSelfLoops = false;

        ged_eAdd = 1.0;
        ged_eRm = 1.0;
        ged_eSub = 0.0;
        ged_eFlip = 0.8;
        ged_eD2U = 0.2;
        ged_eU2D = 0.2;
        ged_nAdd = 0.0;
        ged_nRm = 0.0;

        evo_greedyInitOnInit = false;

        maxAgents = 20;

        abort_seconds = 10;
        abort_iterations = 100;
        abort_nochange = 10;

        numThreads = 0;
        autosaveSecs = DEFAULT_AUTOSAVE_TIME;
        keepWorkfiles = false; // they would be just in the way

        agreementFraction = 0.2f; // % of agents to consider when calculating overall agreement


    }
}
