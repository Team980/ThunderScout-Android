package com.team980.thunderscout.info;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team980.thunderscout.data.AverageScoutData;
import com.team980.thunderscout.data.ScoutData;
import com.team980.thunderscout.data.enumeration.ClimbingStats;

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
