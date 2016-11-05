package com.team980.thunderscout.data;

import java.io.Serializable;
import java.util.List;

/**
 * Class to manage averaging of values and objects.
 * Created by TeamWrapper.
 */

public class AverageScoutData implements Serializable {

    private List<ScoutData> data;

    //INIT
    private String teamNumber;
    private long lastUpdated; //dateAdded of newest match

    //AUTO

    //TELEOP

    //SUMMARY

    public AverageScoutData(List<ScoutData> d) {
        data = d;
    }
}
