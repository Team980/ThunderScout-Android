package com.team980.thunderscout.data.object;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.team980.thunderscout.data.ScoutData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamWrapper implements ParentListItem, Serializable {

    private String teamNumber;

    private List<ScoutData> childItems;

    public TeamWrapper(String num) {
        teamNumber = num;

        childItems = new ArrayList<>();
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
}
