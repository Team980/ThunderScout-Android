package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.Defense;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Class to manage averaging of values and objects.
 * Created by TeamWrapper.
 */
public class AverageScoutData implements Serializable {

    private List<ScoutData> data;

    public AverageScoutData(List<ScoutData> d) {
        data = d;
    }
}
