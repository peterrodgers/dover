package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.UserSettings;

/**
 * Created by dw3 on 07/12/2016.
 */
public class DoverSettings extends UserSettings {
    public String saveResultsFileName = "";
    public boolean saveResultsAddTimeStamp = false;

    public int abort_seconds = 5; // means never abort
    public int abort_iterations = 50; // means never abort
    public int abort_nochange = 5; // means never abort

}
