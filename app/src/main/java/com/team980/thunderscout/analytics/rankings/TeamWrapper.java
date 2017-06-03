/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.analytics.rankings;

import android.support.annotation.NonNull;

import com.team980.thunderscout.schema.ScoutData;

import java.util.ArrayList;
import java.util.List;

/**
 *  Represents data for one team, for all the matches they played in.
 */
public class TeamWrapper implements Comparable<TeamWrapper> {

    private String team;
    private List<ScoutData> dataList;

    public TeamWrapper(String team) {
        this.team = team;
        dataList = new ArrayList<>();
    }

    public String getTeam() {
        return team;
    }

    public List<ScoutData> getDataList() {
        return dataList;
    }

    public double getExpectedPointContribution() { //This can be a decimal
        double expected = 0.0;

        for (ScoutData data : dataList) {
            expected += PointEstimator.getPointContribution(data);
        }

        expected /= dataList.size(); //yes, that's an operator

        return expected;
        //average to find the EXPECTED point contribution
    }

    @Override
    public int compareTo(@NonNull TeamWrapper other) { //Compare by comparing the expected point contributions, higher is better
        return -Double.valueOf(getExpectedPointContribution())
                .compareTo(other.getExpectedPointContribution());
    }
}
