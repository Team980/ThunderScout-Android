package com.team980.thunderscout.data;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

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
            case SORT_AVERAGE_DEFENSES_BREACHED:
                return getAverageScoutData().getAverageTeleopDefensesBreached() + " defenses breached";
            case SORT_TOTAL_DEFENSES_BREACHED:
                return getAverageScoutData().getCumulativeTeleopDefensesBreached() + " defenses breached";
            case SORT_AVERAGE_GOALS_SCORED:
                return getAverageScoutData().getAverageTeleopGoalsScored() + " boulders scored";
            case SORT_TOTAL_GOALS_SCORED:
                return getAverageScoutData().getCumulativeTeleopGoalsScored() + " boulders scored";
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
        SORT_AVERAGE_DEFENSES_BREACHED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Float.valueOf(o1.getAverageScoutData().getAverageTeleopDefensesBreached())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopDefensesBreached());
            }
        },
        SORT_TOTAL_DEFENSES_BREACHED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Float.valueOf(o1.getAverageScoutData().getCumulativeTeleopDefensesBreached())
                        .compareTo(o2.getAverageScoutData().getCumulativeTeleopDefensesBreached());
            }
        },
        SORT_AVERAGE_GOALS_SCORED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Integer.valueOf(o1.getAverageScoutData().getAverageTeleopGoalsScored())
                        .compareTo(o2.getAverageScoutData().getAverageTeleopGoalsScored());
            }
        },
        SORT_TOTAL_GOALS_SCORED {
            public int compare(TeamWrapper o1, TeamWrapper o2) {
                return -Integer.valueOf(o1.getAverageScoutData().getCumulativeTeleopGoalsScored())
                        .compareTo(o2.getAverageScoutData().getCumulativeTeleopGoalsScored());
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
