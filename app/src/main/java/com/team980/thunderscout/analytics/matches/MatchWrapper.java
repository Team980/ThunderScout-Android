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

import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.AllianceStation;

import java.util.EnumMap;

/**
 * Represents data for one match, for all of the teams involved.
 */
public class MatchWrapper { //TODO needs a way to handle overlapping data (same matchNumber and AllianceStation)

    private int matchNumber;

    private EnumMap<AllianceStation, ScoutData> dataMap;

    public MatchWrapper(int matchNumber) {
        this.matchNumber = matchNumber;
        dataMap = new EnumMap<>(AllianceStation.class);
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public ScoutData getData(AllianceStation station) {
        return dataMap.get(station);
    }

    public void setData(AllianceStation station, ScoutData data) {
        dataMap.put(station, data);
    }

    public void removeData(AllianceStation station) {
        dataMap.remove(station);
    }

    public boolean isEmpty() {
        return dataMap.isEmpty();
    }
}
