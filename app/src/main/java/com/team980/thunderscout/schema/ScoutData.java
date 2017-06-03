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

package com.team980.thunderscout.schema;

import com.google.firebase.crash.FirebaseCrash;
import com.team980.thunderscout.schema.enumeration.AllianceStation;
import com.team980.thunderscout.schema.enumeration.ClimbingStats;
import com.team980.thunderscout.schema.enumeration.FuelDumpAmount;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Implements data for one team from one matchNumber.
 */
public class ScoutData implements Serializable {

    /**
     * ScoutData Version 2017-3b
     * <p>
     * 2017-3b: More structural tweaks, new AllianceStation field
     * 2017-3a: Update structure, tweak fields, and prepare for GSON serialization
     * 2017-2: Serializable ArrayList for dumps in teleop
     * 2017-1: First 2017 spec
     */
    private static final long serialVersionUID = 5;

    // INIT
    private String team;
    private int matchNumber;
    private AllianceStation allianceStation;

    private Date date;
    private String source;

    // AUTO
    private Autonomous autonomous;

    // TELEOP
    private Teleop teleop;

    // SUMMARY
    private String troubleWith;
    private String comments;

    public ScoutData() {
        //default values
        autonomous = new Autonomous();
        teleop = new Teleop();
    }

    /**
     * Copy constructor
     */
    public ScoutData(ScoutData other) {
        //Init
        setTeam(other.getTeam());
        setMatchNumber(other.getMatchNumber());
        setAllianceStation(other.getAllianceStation());
        setDate(other.getDate());
        setSource(other.getSource());

        //Auto
        autonomous = new Autonomous();
        autonomous.setGearsDelivered(other.getAutonomous().getGearsDelivered());
        autonomous.setGearsDropped(other.getAutonomous().getGearsDropped());
        autonomous.setLowGoalDumpAmount(other.getAutonomous().getLowGoalDumpAmount());
        autonomous.setHighGoals(other.getAutonomous().getHighGoals());
        autonomous.setMissedHighGoals(other.getAutonomous().getMissedHighGoals());
        autonomous.setCrossedBaseline(other.getAutonomous().getCrossedBaseline());

        //Teleop
        teleop = new Teleop();
        teleop.setGearsDelivered(other.getTeleop().getGearsDelivered());
        teleop.setGearsDropped(other.getTeleop().getGearsDropped());
        teleop.getLowGoalDumps().addAll(other.getTeleop().getLowGoalDumps());
        teleop.setHighGoals(other.getTeleop().getHighGoals());
        teleop.setMissedHighGoals(other.getTeleop().getMissedHighGoals());
        teleop.setClimbingStats(other.getTeleop().getClimbingStats());

        //Summary
        setTroubleWith(other.getTroubleWith());
        setComments(other.getComments());
    }

    // --- INIT ---

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public AllianceStation getAllianceStation() {
        return allianceStation;
    }

    public void setAllianceStation(AllianceStation alliance) {
        this.allianceStation = alliance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date d) {
        date = d;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String d) {
        source = d;
    }

    // --- AUTO ---

    public Autonomous getAutonomous() {
        return autonomous;
    }

    // --- TELEOP ---

    public Teleop getTeleop() {
        return teleop;
    }

    public void setTeleop(Teleop teleop) {
        this.teleop = teleop;
    }

    // --- SUMMARY ---

    public String getTroubleWith() {
        return troubleWith;
    }

    public void setTroubleWith(String troubleWith) {
        this.troubleWith = troubleWith;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // --- OTHER METHODS ---

    public String[] toStringArray() {
        ArrayList<String> fieldList = new ArrayList<>();

        //Init
        fieldList.add(getTeam());
        fieldList.add(String.valueOf(getMatchNumber()));
        fieldList.add(getAllianceStation().name());
        fieldList.add(getDate().toString());
        fieldList.add(getSource());

        //Auto
        fieldList.add(String.valueOf(getAutonomous().getGearsDelivered()));
        fieldList.add(String.valueOf(getAutonomous().getGearsDropped()));
        fieldList.add(getAutonomous().getLowGoalDumpAmount().name());
        fieldList.add(String.valueOf(getAutonomous().getHighGoals()));
        fieldList.add(String.valueOf(getAutonomous().getMissedHighGoals()));
        fieldList.add(String.valueOf(getAutonomous().getCrossedBaseline()));

        //Teleop
        fieldList.add(String.valueOf(getTeleop().getGearsDelivered()));
        fieldList.add(String.valueOf(getTeleop().getGearsDropped()));
        fieldList.add(getTeleop().getLowGoalDumps().toString());
        fieldList.add(String.valueOf(getTeleop().getHighGoals()));
        fieldList.add(String.valueOf(getTeleop().getMissedHighGoals()));
        fieldList.add(getTeleop().getClimbingStats().name());

        //Summary
        fieldList.add(getTroubleWith());
        fieldList.add(getComments());

        return fieldList.toArray(new String[fieldList.size()]);
    }

    public static ScoutData fromStringArray(String[] array) {
        ScoutData data = new ScoutData();

        //Init
        data.setTeam(array[0]);
        data.setMatchNumber(Integer.parseInt(array[1]));
        data.setAllianceStation(AllianceStation.valueOf(array[2]));
        try {
            data.setDate(DateFormat.getDateTimeInstance().parse(array[3]));
        } catch (ParseException e) {
            FirebaseCrash.report(e);
        }
        data.setSource(array[4]);

        //Auto
        data.getAutonomous().setGearsDelivered(Integer.parseInt(array[5]));
        data.getAutonomous().setGearsDropped(Integer.parseInt(array[6]));
        data.getAutonomous().setLowGoalDumpAmount(FuelDumpAmount.valueOf(array[7]));
        data.getAutonomous().setHighGoals(Integer.parseInt(array[8]));
        data.getAutonomous().setMissedHighGoals(Integer.parseInt(array[9]));
        data.getAutonomous().setCrossedBaseline(Boolean.parseBoolean(array[10]));

        //Teleop
        data.getTeleop().setGearsDelivered(Integer.parseInt(array[11]));
        data.getTeleop().setGearsDropped(Integer.parseInt(array[12]));
        for (String amount : Arrays.asList(array[13].substring(1, array[13].length() - 1).split(", "))) {
            if (amount.isEmpty()) {
                break;
            }
            data.getTeleop().getLowGoalDumps().add(FuelDumpAmount.valueOf(amount.toUpperCase()));
        }
        data.getTeleop().setHighGoals(Integer.parseInt(array[14]));
        data.getTeleop().setMissedHighGoals(Integer.parseInt(array[15]));
        data.getTeleop().setClimbingStats(ClimbingStats.valueOf(array[16]));

        //Summary
        data.setTroubleWith(array[17]);
        data.setComments(array[18]);

        return data;
    }

    public class Autonomous implements Serializable {

        private int gearsDelivered;
        private int gearsDropped;
        private FuelDumpAmount lowGoalDumpAmount;
        private int highGoals;
        private int missedHighGoals;
        private Boolean crossedBaseline;

        public Autonomous() {
            lowGoalDumpAmount = FuelDumpAmount.NONE;
        }

        private final static long serialVersionUID = 5;

        public int getGearsDelivered() {
            return gearsDelivered;
        }

        public void setGearsDelivered(int gearsDelivered) {
            this.gearsDelivered = gearsDelivered;
        }

        public int getGearsDropped() {
            return gearsDropped;
        }

        public void setGearsDropped(int gearsDropped) {
            this.gearsDropped = gearsDropped;
        }

        public FuelDumpAmount getLowGoalDumpAmount() {
            return lowGoalDumpAmount;
        }

        public void setLowGoalDumpAmount(FuelDumpAmount lowGoalDumpAmount) {
            this.lowGoalDumpAmount = lowGoalDumpAmount;
        }

        public int getHighGoals() {
            return highGoals;
        }

        public void setHighGoals(int highGoals) {
            this.highGoals = highGoals;
        }

        public int getMissedHighGoals() {
            return missedHighGoals;
        }

        public void setMissedHighGoals(int missedHighGoals) {
            this.missedHighGoals = missedHighGoals;
        }

        public Boolean getCrossedBaseline() {
            return crossedBaseline;
        }

        public void setCrossedBaseline(Boolean crossedBaseline) {
            this.crossedBaseline = crossedBaseline;
        }

    }

    public class Teleop implements Serializable {

        private int gearsDelivered;
        private int gearsDropped;
        private ArrayList<FuelDumpAmount> lowGoalDumps;
        private int highGoals;
        private int missedHighGoals;
        private ClimbingStats climbingStats;

        private final static long serialVersionUID = 5;

        public Teleop() {
            lowGoalDumps = new ArrayList<>();
            climbingStats = ClimbingStats.DID_NOT_CLIMB;
        }

        public int getGearsDelivered() {
            return gearsDelivered;
        }

        public void setGearsDelivered(int gearsDelivered) {
            this.gearsDelivered = gearsDelivered;
        }

        public int getGearsDropped() {
            return gearsDropped;
        }

        public void setGearsDropped(int gearsDropped) {
            this.gearsDropped = gearsDropped;
        }

        public ArrayList<FuelDumpAmount> getLowGoalDumps() {
            return lowGoalDumps;
        }

        public int getHighGoals() {
            return highGoals;
        }

        public void setHighGoals(int highGoals) {
            this.highGoals = highGoals;
        }

        public int getMissedHighGoals() {
            return missedHighGoals;
        }

        public void setMissedHighGoals(int missedHighGoals) {
            this.missedHighGoals = missedHighGoals;
        }

        public ClimbingStats getClimbingStats() {
            return climbingStats;
        }

        public void setClimbingStats(ClimbingStats climbingStats) {
            this.climbingStats = climbingStats;
        }

    }
}
