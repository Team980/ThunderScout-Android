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
import com.team980.thunderscout.schema.enumeration.ClimbingStats;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;

public class MatchPointEstimator {

    public static int getPointContribution(ScoutData data) { //reference: 2017 game manual pg. 44
        int contribution = 0;

        contribution += getBaselinePoints(data);
        contribution += getAutoFuelPoints(data);
        contribution += getAutoRotorPoints(data);
        contribution += getTeleopFuelPoints(data);
        contribution += getTeleopRotorPoints(data);
        contribution += getClimbingPoints(data);
        contribution += getRankingPoints(data);

        return contribution;
    }

    public static int getBaselinePoints(ScoutData data) {
        if (data.getAutonomous().getCrossedBaseline()) {
            return 5; //5 points if the robot crossed the baseline
        }
        return 0;
    }

    public static int getAutoFuelPoints(ScoutData data) { //NO FRACTIONS
        int low = ((data.getAutonomous().getLowGoalDumpAmount().getMinimumAmount()
                + data.getAutonomous().getLowGoalDumpAmount().getMaximumAmount()) / 2) / 3;
        //Average the minimum and maximum low goal dump size,
        // then divide it by 3 and truncate due to integer math

        int high = data.getAutonomous().getHighGoals();
        //Just take the number of high goals

        return low + high;
    }

    public static int getAutoRotorPoints(ScoutData data) {
        return getNumberOfRotors(data.getAutonomous().getGearsDelivered()) * 60; //60pts per rotor turning in auto
    }

    public static int getTeleopFuelPoints(ScoutData data) {
        int low = 0;
        for (FuelDumpAmount dumpAmount : data.getTeleop().getLowGoalDumps()) {
            low += ((dumpAmount.getMinimumAmount() + dumpAmount.getMaximumAmount()) / 2) / 9;
            //For each fuel dump, average the minimum and maximum low goal dump size,
            // then divide it by 9 and truncate due to integer math
        }

        int high = data.getTeleop().getHighGoals() / 3;
        //Just take the number of high goals, divide it by 3 and truncate due to integer math

        return low + high;
    }

    public static int getTeleopRotorPoints(ScoutData data) {
        return (getNumberOfRotors(data.getTeleop().getGearsDelivered() + data.getAutonomous().getGearsDelivered())
                - getNumberOfRotors(data.getAutonomous().getGearsDelivered())) * 40;
        //40pts per rotor turning in teleop that was not turning in auto

    }

    public static int getClimbingPoints(ScoutData data) {
        if (data.getTeleop().getClimbingStats() == ClimbingStats.PRESSED_TOUCHPAD) {
            return 50; //50 points if the robot pressed the touchpad
        }
        return 0;
    }

    public static int getRankingPoints(ScoutData data) { //Points awarded are based on ELIMINATION values, not qf ranking points themselves
        int rp = 0;

        if (getNumberOfRotors(data.getTeleop().getGearsDelivered() + data.getAutonomous().getGearsDelivered()) >= 4) {
            rp += 100; //if all 4 rotors are turning, award 100 points
        }

        if (getAutoFuelPoints(data) + getTeleopFuelPoints(data) >= 40) {
            rp += 20; //If the kPa (equal to number of fuel points scored) is >= 40, award 20 points
        }

        return rp;
    }

    private static int getNumberOfRotors(int numberOfGears) { //assumes no change to gear prepopulation and that the robot is alone on the field
        if (numberOfGears >= 13) { //1+2+4+6 placed
            return 4;
        }

        if (numberOfGears >= 7) { //1+2+4 placed
            return 3;
        }

        if (numberOfGears >= 3) { //1+2 placed
            return 2;
        }

        if (numberOfGears >= 1) { //1 placed
            return 1;
        }

        return 0;
    }
}
