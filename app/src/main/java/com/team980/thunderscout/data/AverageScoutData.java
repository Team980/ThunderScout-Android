package com.team980.thunderscout.data;

import com.team980.thunderscout.data.enumeration.Defense;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.List;

/**
 * Class to manage averaging of values and objects.
 * Created by TeamWrapper.
 */

public class AverageScoutData implements Serializable {

    private List<ScoutData> dataList;

    public AverageScoutData(List<ScoutData> d) {
        dataList = d;
    }

    //INIT

    public String getTeamNumber() {
        return dataList.get(0).getTeamNumber();
    }

    public long getLastUpdated() {
        return dataList.get(0).getDateAdded(); //dateAdded of newest match
    }

    public int getNumberOfMatches() {
        return dataList.size();
    }

    //AUTO

    public EnumMap<Defense, Integer> getAutoDefenseCrossings() {
        EnumMap<Defense, Integer> map = new EnumMap<>(Defense.class);

        for (ScoutData data : dataList) {
            Defense defense = data.getAutoDefenseCrossed();
            if (defense == Defense.NONE) {
                continue; //We don't want NONE in our map
            }

            if (map.containsKey(defense)) {
                map.put(defense, map.get(defense) + 1);
            } else {
                map.put(defense, 1);
            }
        }

        return map;
    }

    public float getAverageAutoTotalGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getAutoLowGoals();
            i += data.getAutoHighGoals();
        }

        return i / dataList.size();
    }

    public float getAverageAutoLowGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getAutoLowGoals();
        }

        return i / dataList.size();
    }

    public float getAverageAutoHighGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getAutoHighGoals();
        }

        return i / dataList.size();
    }

    public float getAverageAutoMissedGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getAutoMissedGoals();
        }

        return i / dataList.size();
    }

    //TELEOP

    public EnumMap<Defense, Integer> getTeleopDefenseCrossings() {
        EnumMap<Defense, Integer> map = new EnumMap<>(Defense.class);

        for (ScoutData data : dataList) {
            for (Defense defense : Defense.values()) {
                if (defense == Defense.NONE) {
                    continue; //We don't want NONE in our map
                }

                if (data.getTeleopDefenseCrossings().get(defense) == null) {
                    continue; //We don't want NULLs in our map
                }

                if (map.containsKey(defense)) {
                    map.put(defense, map.get(defense) + data.getTeleopDefenseCrossings().get(defense));
                } else {
                    map.put(defense, data.getTeleopDefenseCrossings().get(defense));
                }
            }
        }

        return map;
    }

    public float getAverageTeleopTotalGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getTeleopLowGoals();
            i += data.getTeleopLowGoals();
        }

        return i / dataList.size();
    }

    public float getAverageTeleopLowGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getTeleopLowGoals();
        }

        return i / dataList.size();
    }

    public float getAverageTeleopHighGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getTeleopHighGoals();
        }

        return i / dataList.size();
    }

    public float getAverageTeleopMissedGoals() {
        float i = 0;
        for (ScoutData data : dataList) {
            i += data.getTeleopMissedGoals();
        }

        return i / dataList.size();
    }

    //SUMMARY
}
