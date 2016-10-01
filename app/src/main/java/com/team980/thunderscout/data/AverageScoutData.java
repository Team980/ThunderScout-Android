package com.team980.thunderscout.data;

import java.io.Serializable;
import java.util.List;

/**
 * Class to manage averaging of values and objects.
 * Created by TeamWrapper.
 */
@Deprecated
/**
 * TODO rewrite for alliance selection usefulness
 */
public class AverageScoutData implements Serializable {

    private List<ScoutData> data;

    public AverageScoutData(List<ScoutData> d) {
        data = d;
    }
}
