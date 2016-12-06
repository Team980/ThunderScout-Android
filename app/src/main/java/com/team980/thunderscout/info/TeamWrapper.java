package com.team980.thunderscout.info;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team980.thunderscout.data.AverageScoutData;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ScalingStats;

import java.io.Serializable;
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

    public TeamWrapper(String num) {
        teamNumber = num;

        childItems = new ArrayList<>();
        averageData = new AverageScoutData(childItems);
    }

    public TeamWrapper(String num, ScoutData... dataToInsert) {
        teamNumber = num;

        childItems = new ArrayList<>();

        for (ScoutData data : dataToInsert) {
            childItems.add(data);
        }

        averageData = new AverageScoutData(childItems);
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public int getNumberOfMatches() {
        return childItems.size();
    }

    public String getDescriptor(TeamComparator sortMode) {
        switch (sortMode) {
            case SORT_AUTO_UNIQUE_DEFENSE_COUNT:
                return getAverageScoutData().getAutoDefenseCrossings().size() + " defenses crossed";
            case SORT_AUTO_TOTAL_GOALS_SCORED:
                return getAverageScoutData().getAverageAutoTotalGoals() + " goals scored";
            case SORT_TELEOP_UNIQUE_DEFENSE_COUNT:
                return getAverageScoutData().getTeleopDefenseCrossings().size() + " defenses crossed";
            case SORT_TELEOP_TOTAL_GOALS_SCORED:
                return getAverageScoutData().getAverageTeleopTotalGoals() + " goals scored";
            case SORT_SUMMARY_FULL_SCALE_PERCENTAGE:
                return "Scaled the tower in " + getAverageScoutData().getScalingStatsPercentage(ScalingStats.FULL) + "% of matches";
            case SORT_SUMMARY_CHALLENGED_TOWER_PERCENTAGE:
                return "Challenged the tower in " + getAverageScoutData().getChallengedTowerPercentage() + "% of matches";
            default:
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
        SORT_TEAM_NUMBER {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return Integer.valueOf(o1.getTeamNumber())
                        .compareTo(Integer.valueOf(o2.getTeamNumber()));
            }
        },

        SORT_AUTO_UNIQUE_DEFENSE_COUNT {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Integer.valueOf(o1.getAverageScoutData().getAutoDefenseCrossings().size())
                        .compareTo(o2.getAverageScoutData().getAutoDefenseCrossings().size());
            }
        },

        SORT_AUTO_TOTAL_GOALS_SCORED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Float.valueOf(o1.getAverageScoutData().getAverageAutoTotalGoals())
                        .compareTo(o2.getAverageScoutData().getAverageAutoTotalGoals());
            }
        },

        SORT_TELEOP_UNIQUE_DEFENSE_COUNT {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Integer.valueOf(o1.getAverageScoutData().getTeleopDefenseCrossings().size())
                        .compareTo(o2.getAverageScoutData().getTeleopDefenseCrossings().size());
            }
        },

        SORT_TELEOP_TOTAL_GOALS_SCORED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Float.valueOf(o1.getAverageScoutData().getAverageTeleopTotalGoals())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopTotalGoals());
            }
        },

        SORT_SUMMARY_FULL_SCALE_PERCENTAGE {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Double.valueOf(o1.getAverageScoutData().getScalingStatsPercentage(ScalingStats.FULL))
                        .compareTo(o2.getAverageScoutData().getScalingStatsPercentage(ScalingStats.FULL));
            }
        },

        SORT_SUMMARY_CHALLENGED_TOWER_PERCENTAGE {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Double.valueOf(o1.getAverageScoutData().getChallengedTowerPercentage())
                        .compareTo(o2.getAverageScoutData().getChallengedTowerPercentage());
            }
        };

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
    }
}
