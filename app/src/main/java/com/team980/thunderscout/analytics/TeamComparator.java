package com.team980.thunderscout.analytics;

import android.util.Log;

import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

import java.util.Comparator;

/**
 * This needs a bit of cleanup...
 */
public enum TeamComparator implements Comparator<TeamWrapper> {
    //TODO We shouldn't be creating this many AverageScoutData instances
    //TODO The order also isn't consistent when the data is equal-
    //TODO it appears to revert to the order fetched from the database when refreshed

    SORT_POINT_CONTRIBUTION("Expected point contribution") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(TeamPointEstimator.getPointContribution(new AverageScoutData(o1.getDataList())))
                    .compareTo(TeamPointEstimator.getPointContribution(new AverageScoutData(o2.getDataList())));
        }
    },

    SORT_TEAM_NUMBER("Team number") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            Log.d("COMPARE-A", o1.getTeam() + " / " + o2.getTeam());
            Log.d("COMPARE-B", "" + Integer.valueOf(o1.getTeam()).compareTo(Integer.valueOf(o2.getTeam())));
            return -(Integer.valueOf(o1.getTeam())
                    .compareTo(Integer.valueOf(o2.getTeam()))); //For this one smaller is better
        }
    },

    SORT_LAST_UPDATED("Time updated") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return new AverageScoutData(o1.getDataList()).getLastUpdated()
                    .compareTo(new AverageScoutData(o2.getDataList()).getLastUpdated());
        }
    },

    SORT_AUTO_GEARS_DELIVERED("Auto gears delivered") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageAutoGearsDelivered())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageAutoGearsDelivered());
        }
    },

    /*SORT_AUTO_GEARS_DROPPED("Auto gears dropped") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageAutoGearsDropped())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageAutoGearsDropped());
        }
    },*/

    /*SORT_AUTO_LOW_GOAL_DUMP_AMOUNT("Auto low goal dump amount") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return new AverageScoutData(o1.getDataList()).getAverageAutoLowGoalDumpAmount()
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageAutoLowGoalDumpAmount());
        }
    },*/

    SORT_AUTO_HIGH_GOALS("Auto high goals") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageAutoHighGoals())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageAutoHighGoals());
        }
    },

    /*SORT_AUTO_MISSED_HIGH_GOALS("Auto missed high goals") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageAutoMissedHighGoals())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageAutoMissedHighGoals());
        }
    },*/

    /*SORT_CROSSED_BASELINE_PERCENTAGE("Crossed baseline percentage") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Double.valueOf(new AverageScoutData(o1.getDataList()).getCrossedBaselinePercentage())
                    .compareTo(new AverageScoutData(o2.getDataList()).getCrossedBaselinePercentage());
        }
    },*/

    SORT_TELEOP_GEARS_DELIVERED("Teleop gears delivered") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageTeleopGearsDelivered())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopGearsDelivered());
        }
    },

    /*SORT_TELEOP_GEARS_DROPPED("Teleop gears dropped") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageTeleopGearsDropped())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopGearsDropped());
        }
    },*/

    /*SORT_TELEOP_DUMP_FREQUENCY("Teleop dump frequency") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageTeleopDumpFrequency())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopDumpFrequency());
        }
    },*/

    /*SORT_TELEOP_DUMP_AMOUNT("Teleop dump amount") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return new AverageScoutData(o1.getDataList()).getAverageTeleopLowGoalDumpAmount()
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopLowGoalDumpAmount());
        }
    },*/

    SORT_TELEOP_HIGH_GOALS("Teleop high goals") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageTeleopHighGoals())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopHighGoals());
        }
    },

    /*SORT_TELEOP_MISSED_HIGH_GOALS("Teleop missed high goals") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            return Float.valueOf(new AverageScoutData(o1.getDataList()).getAverageTeleopMissedHighGoals())
                    .compareTo(new AverageScoutData(o2.getDataList()).getAverageTeleopMissedHighGoals());
        }
    },*/

    SORT_CLIMBING_STATS_PERCENTAGE("Pressed touchpad percentage") {
        public int compare(TeamWrapper o1, TeamWrapper o2) {
            int pressedTouchpad = Double.valueOf(new AverageScoutData(o1.getDataList()).getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD))
                    .compareTo(new AverageScoutData(o2.getDataList()).getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD));

            if (pressedTouchpad == 0) { //If it's the same...
                return Double.valueOf(new AverageScoutData(o1.getDataList()).getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB))
                        .compareTo(new AverageScoutData(o2.getDataList()).getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB));
            } else {
                return pressedTouchpad;
            }
        }
    };

    private String displayName;

    TeamComparator(String par1) {
        displayName = par1;
    }

    public static Comparator<TeamWrapper> getComparator(final TeamComparator option) {
        return (o1, o2) -> option.compare(o1, o2) * -1;
    }

    public static String[] getFormattedList() {
        TeamComparator[] states = values();
        String[] names = new String[states.length];

        for (int i = 0; i < states.length; i++) {
            names[i] = states[i].toString();
        }

        return names;
    }

    @Override
    public String toString() {
        return displayName;
    }
}