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

package com.team980.thunderscout.analytics.matches;

import android.support.annotation.Nullable;

import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.AllianceStation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Represents data for one match, for all of the teams involved.
 */
public class MatchWrapper implements Serializable {

    private int matchNumber;

    private EnumMap<AllianceStation, List<ScoutData>> dataMap;

    public MatchWrapper(int matchNumber) {
        this.matchNumber = matchNumber;
        dataMap = new EnumMap<>(AllianceStation.class);
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    @Nullable
    public ScoutData getData(AllianceStation station) {
        if (dataMap.get(station) == null) {
            return null;
        }

        return dataMap.get(station).get(0);
    }

    public List<ScoutData> getDataList(AllianceStation station) {
        return dataMap.get(station);
    }

    public void setData(AllianceStation station, ScoutData data) {
        if (!dataMap.containsKey(station)) {
            dataMap.put(station, new ArrayList<ScoutData>());
        }

        dataMap.get(station).add(data);
    }

    public void removeData(AllianceStation station) {
        dataMap.remove(station);
    }

    public void removeData(AllianceStation station, ScoutData data) {
        dataMap.get(station).remove(data);

        if (dataMap.get(station).isEmpty()) {
            dataMap.remove(station);
        }
    }

    public boolean isEmpty() {
        return dataMap.isEmpty();
    }
}
