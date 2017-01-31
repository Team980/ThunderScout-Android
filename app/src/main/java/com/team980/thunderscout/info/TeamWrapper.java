package com.team980.thunderscout.info;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team980.thunderscout.data.AverageScoutData;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Container for ScoutData. Implements averaging, grouping methods by creating an instance of AverageScoutData.
 */
public class TeamWrapper implements ParentListItem, Serializable {

    private String teamNumber;

    private AverageScoutData averageData;
    private List<ScoutData> childItems;

    private NumberFormat formatter;

    public TeamWrapper(String num) {
        teamNumber = num;

        childItems = new ArrayList<>();
        averageData = new AverageScoutData(childItems);

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }

    public TeamWrapper(String num, ScoutData... dataToInsert) {
        teamNumber = num;

        childItems = new ArrayList<>();

        for (ScoutData data : dataToInsert) {
            childItems.add(data);
        }

        averageData = new AverageScoutData(childItems);

        formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public int getNumberOfMatches() {
        return childItems.size();
    }

    public String getDescriptor(TeamComparator sortMode) {
        switch (sortMode) {
            case SORT_LAST_UPDATED:
                return "Last updated " + SimpleDateFormat.getDateTimeInstance().format(getAverageScoutData().getLastUpdated());
            case SORT_AVERAGE_AUTO_GEARS_DELIVERED:
                return formatter.format(getAverageScoutData().getAverageAutoGearsDelivered()) + " gears delivered";
            case SORT_AVERAGE_AUTO_LOW_GOAL_DUMP_AMOUNT:
                return getAverageScoutData().getAverageAutoLowGoalDumpAmount() + " amount of fuel dumped";
            case SORT_AVERAGE_AUTO_HIGH_GOALS:
                return formatter.format(getAverageScoutData().getAverageAutoHighGoals()) + " high goals";
            case SORT_AVERAGE_AUTO_MISSED_HIGH_GOALS:
                return formatter.format(getAverageScoutData().getAverageAutoHighGoals()) + " missed high goals";
            case SORT_CROSSED_BASELINE_PERCENTAGE:
                return "Crossed the baseline in " + formatter.format(getAverageScoutData().getCrossedBaselinePercentage()) + "% of matches";
            case SORT_AVERAGE_TELEOP_GEARS_DELIVERED:
                return formatter.format(getAverageScoutData().getAverageTeleopGearsDelivered()) + " gears delivered";
            case SORT_AVERAGE_TELEOP_DUMP_FREQUENCY:
                return formatter.format(getAverageScoutData().getAverageTeleopDumpFrequency()) + " dumps per match";
            case SORT_AVERAGE_TELEOP_DUMP_AMOUNT:
                return getAverageScoutData().getAverageTeleopLowGoalDumpAmount() + " amount of fuel dumped";
            case SORT_AVERAGE_TELEOP_HIGH_GOALS:
                return formatter.format(getAverageScoutData().getAverageTeleopHighGoals()) + " high goals";
            case SORT_AVERAGE_TELEOP_MISSED_HIGH_GOALS:
                return formatter.format(getAverageScoutData().getAverageTeleopHighGoals()) + " missed high goals";
            //case SORT_CLIMBING_STATS_PERCENTAGE: TODO
            default: //Team number, fallback
                return getNumberOfMatches() + " matches"; //TODO make the layout change?
        }
    }

    @Override
    public List<ScoutData> getChildItemList() {
        return childItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public AverageScoutData getAverageScoutData() {
        return averageData;
    }

    /**
     * Ridiculously cool code
     */
    public enum TeamComparator implements Comparator<TeamWrapper> {
        SORT_TEAM_NUMBER("Team number") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Integer.valueOf(o1.getTeamNumber())
                        .compareTo(Integer.valueOf(o2.getTeamNumber()));
            }
        },

        SORT_LAST_UPDATED("Time updated") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Long.valueOf(o1.getAverageScoutData().getLastUpdated())
                        .compareTo(o2.getAverageScoutData().getLastUpdated());
            }
        },

        SORT_AVERAGE_AUTO_GEARS_DELIVERED("Auto gears delivered") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageAutoGearsDelivered())
                        .compareTo(o2.getAverageScoutData().getAverageAutoGearsDelivered());
            }
        },

        SORT_AVERAGE_AUTO_LOW_GOAL_DUMP_AMOUNT("Auto low goal dump amount") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return o1.getAverageScoutData().getAverageAutoLowGoalDumpAmount()
                        .compareTo(o2.getAverageScoutData().getAverageAutoLowGoalDumpAmount());
            }
        },

        SORT_AVERAGE_AUTO_HIGH_GOALS("Auto high goals") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageAutoHighGoals())
                        .compareTo(o2.getAverageScoutData().getAverageAutoHighGoals());
            }
        },

        SORT_AVERAGE_AUTO_MISSED_HIGH_GOALS("Auto missed high goals") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageAutoMissedHighGoals())
                        .compareTo(o2.getAverageScoutData().getAverageAutoMissedHighGoals());
            }
        },

        SORT_CROSSED_BASELINE_PERCENTAGE("Crossed baseline percentage") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Double.valueOf(o1.getAverageScoutData().getCrossedBaselinePercentage())
                        .compareTo(o2.getAverageScoutData().getCrossedBaselinePercentage());
            }
        },

        SORT_AVERAGE_TELEOP_GEARS_DELIVERED("Teleop gears delivered") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageTeleopGearsDelivered())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopGearsDelivered());
            }
        },

        SORT_AVERAGE_TELEOP_DUMP_FREQUENCY("Teleop dump frequency") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageTeleopDumpFrequency())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopDumpFrequency());
            }
        },

        SORT_AVERAGE_TELEOP_DUMP_AMOUNT("Teleop dump amount") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return o1.getAverageScoutData().getAverageTeleopLowGoalDumpAmount()
                        .compareTo(o2.getAverageScoutData().getAverageTeleopLowGoalDumpAmount());
            }
        },

        SORT_AVERAGE_TELEOP_HIGH_GOALS("Teleop high goals") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageTeleopHighGoals())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopHighGoals());
            }
        },

        SORT_AVERAGE_TELEOP_MISSED_HIGH_GOALS("Teleop missed high goals") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Float.valueOf(o1.getAverageScoutData().getAverageTeleopMissedHighGoals())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopMissedHighGoals());
            }
        },

        SORT_CLIMBING_STATS_PERCENTAGE("Teleop climbing stats percentage") {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                int pressedTouchpad = Double.valueOf(o1.getAverageScoutData().getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD))
                        .compareTo(o2.getAverageScoutData().getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD));

                if (pressedTouchpad == 0) { //If it's the same...
                    return Double.valueOf(o1.getAverageScoutData().getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB))
                            .compareTo(o2.getAverageScoutData().getClimbingStatsPercentage(ClimbingStats.ATTEMPTED_CLIMB));
                } else {
                    return pressedTouchpad;
                }
            }
        };

        private String displayName;

        TeamComparator(String par1) {
            displayName = par1;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public static Comparator<TeamWrapper> getComparator(final TeamComparator... multipleOptions) {
            return new Comparator<TeamWrapper>() {
                public int compare(TeamWrapper o1, TeamWrapper o2) {
                    for (TeamComparator option : multipleOptions) {
                        int result = option.compare(o1, o2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return 0;
                }
            };
        }

        public static String[] getFormattedList() {
            TeamComparator[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].toString();
            }

            return names;
        }
    }
}
