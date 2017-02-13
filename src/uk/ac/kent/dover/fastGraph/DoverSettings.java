package uk.ac.kent.dover.fastGraph;

import org.cytoscape.gedevo.UserSettings;

/**
 * Created by dw3 on 07/12/2016.
 */
public class DoverSettings extends UserSettings {
    public String saveResultsFileName = "";
    public boolean saveResultsAddTimeStamp = false;

    public int abort_seconds = 3; // 5 means never abort //how long run system for until abandon
    public int abort_iterations = 10; // 50 means never abort //how many iterations before abort
    public int abort_nochange = 3; // 5 means never abort //how many iterations of no change before iterations

}
