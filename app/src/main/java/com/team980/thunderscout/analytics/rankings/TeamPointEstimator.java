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

import com.team980.thunderscout.analytics.matches.MatchPointEstimator;
import com.team980.thunderscout.analytics.rankings.breakdown.AverageScoutData;
import com.team980.thunderscout.schema.ScoutData;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;

public class TeamPointEstimator {

    public static double getPointContribution(AverageScoutData data) { //based on MatchPointEstimator.java
        float contribution = 0;

        contribution += getBaselinePoints(data);
        contribution += getAutoFuelPoints(data);
        contribution += getAutoRotorPoints(data);
        contribution += getTeleopFuelPoints(data);
        contribution += getTeleopRotorPoints(data);
        contribution += getClimbingPoints(data);
        contribution += getRankingPoints(data);

        return contribution;
    }

    public static double getBaselinePoints(AverageScoutData data) {
        return 5 * (data.getCrossedBaselinePercentage() / 100);
    }

    public static double getAutoFuelPoints(AverageScoutData data) {
        double pts = 0;

        for (ScoutData match : data.getDataList()) {
            pts += MatchPointEstimator.getAutoFuelPoints(match);
        }

        return pts / data.getDataList().size();
    }

    public static double getAutoRotorPoints(AverageScoutData data) { //Average the rotor points
        double pts = 0;

        for (ScoutData match : data.getDataList()) {
            pts += MatchPointEstimator.getAutoRotorPoints(match);
        }

        return pts / data.getDataList().size();
    }

    public static double getTeleopFuelPoints(AverageScoutData data) {
        double pts = 0;

        for (ScoutData match : data.getDataList()) {
            pts += MatchPointEstimator.getTeleopFuelPoints(match);
        }

        return pts / data.getDataList().size();
    }

    public static double getTeleopRotorPoints(AverageScoutData data) {
        double pts = 0;

        for (ScoutData match : data.getDataList()) {
            pts += MatchPointEstimator.getTeleopRotorPoints(match);
        }

        return pts / data.getDataList().size();
    }

    public static double getClimbingPoints(AverageScoutData data) {
        return 50 * (data.getClimbingStatsPercentage(ClimbingStats.PRESSED_TOUCHPAD) / 100);
    }

    public static double getRankingPoints(AverageScoutData data) {
        double pts = 0;

        for (ScoutData match : data.getDataList()) {
            pts += MatchPointEstimator.getRankingPoints(match);
        }

        return pts / data.getDataList().size();
    }
}
