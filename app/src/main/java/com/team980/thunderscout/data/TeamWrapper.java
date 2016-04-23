package com.team980.thunderscout.data;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.io.Serializable;
import java.util.ArrayList;
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
    }

    public String getTeamNumber() {
        return teamNumber;
    }

    public String getDescriptor() {
        return "Test Data";
    } //TODO generate descriptor based on user settings

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
}
