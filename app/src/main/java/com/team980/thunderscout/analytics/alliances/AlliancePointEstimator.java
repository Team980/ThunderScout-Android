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

package com.team980.thunderscout.analytics.alliances;

import com.team980.thunderscout.analytics.matches.MatchPointEstimator;
import com.team980.thunderscout.analytics.rankings.TeamPointEstimator;
import com.team980.thunderscout.schema.ScoutData;

public class AlliancePointEstimator {

    public static double getPointContribution(AverageAllianceData data) { //based on TeamPointEstimator.java
        float contribution = 0;

        contribution += getBaselinePoints(data);
        contribution += getAutoFuelPoints(data);
        contribution += getAutoRotorPoints(data);
        contribution += getTeleopFuelPoints(data);
        contribution += getTeleopRotorPoints(data);
        contribution += getClimbingPoints(data);
        contribution += getAverageRankingPoints(data);

        return contribution;
    }

    public static double getBaselinePoints(AverageAllianceData data) {
        return TeamPointEstimator.getBaselinePoints(data.getStation1())
                + TeamPointEstimator.getBaselinePoints(data.getStation2())
                + TeamPointEstimator.getBaselinePoints(data.getStation3());
    }

    public static double getAutoFuelPoints(AverageAllianceData data) {
        return TeamPointEstimator.getAutoFuelPoints(data.getStation1())
                + TeamPointEstimator.getAutoFuelPoints(data.getStation2())
                + TeamPointEstimator.getAutoFuelPoints(data.getStation3());
    }

    public static double getAutoRotorPoints(AverageAllianceData data) { //TODO this is most definitely wrong
        return TeamPointEstimator.getAutoRotorPoints(data.getStation1())
                + TeamPointEstimator.getAutoRotorPoints(data.getStation2())
                + TeamPointEstimator.getAutoRotorPoints(data.getStation3());
    }

    public static double getTeleopFuelPoints(AverageAllianceData data) {
        return TeamPointEstimator.getTeleopFuelPoints(data.getStation1())
                + TeamPointEstimator.getTeleopFuelPoints(data.getStation2())
                + TeamPointEstimator.getTeleopFuelPoints(data.getStation3());
    }

    public static double getTeleopRotorPoints(AverageAllianceData data) { //TODO this is most definitely wrong
        return TeamPointEstimator.getTeleopRotorPoints(data.getStation1())
                + TeamPointEstimator.getTeleopRotorPoints(data.getStation2())
                + TeamPointEstimator.getTeleopRotorPoints(data.getStation3());
    }

    public static double getClimbingPoints(AverageAllianceData data) {
        return TeamPointEstimator.getClimbingPoints(data.getStation1())
                + TeamPointEstimator.getClimbingPoints(data.getStation2())
                + TeamPointEstimator.getClimbingPoints(data.getStation3());
    }

    public static double getAverageRankingPoints(AverageAllianceData data) { //TODO I don't like this
        double pts1 = 0;
        for (ScoutData match : data.getStation1().getDataList()) {
            pts1 += MatchPointEstimator.getRankingPoints(match);
            pts1 /= data.getStation1().getDataList().size();
        }

        double pts2 = 0;
        for (ScoutData match : data.getStation2().getDataList()) {
            pts2 += MatchPointEstimator.getRankingPoints(match);
            pts2 /= data.getStation2().getDataList().size();
        }

        double pts3 = 0;
        for (ScoutData match : data.getStation3().getDataList()) {
            pts3 += MatchPointEstimator.getRankingPoints(match);
            pts3 /= data.getStation3().getDataList().size();
        }

        return pts1 + pts2 + pts3;
    }
}
