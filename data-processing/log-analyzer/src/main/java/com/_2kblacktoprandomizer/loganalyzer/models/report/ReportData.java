package com._2kblacktoprandomizer.loganalyzer.models.report;

import com._2kblacktoprandomizer.loganalyzer.models.Player;
import com._2kblacktoprandomizer.loganalyzer.models.RandomizationMode;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportData {

    // Metadata
    public Date fromDate;
    public Date toDate;
    public int totalRandomizations = 0;
    public int totalPlayersGenerated = 0;

    // Usage Details
    public Map<Date, Integer> randomizationsPerDay = new LinkedHashMap<>();
    public Map<DayOfWeek, Integer> randomizationsPerDayOfWeek = new HashMap<>();
    public Map<Integer, Integer> randomizationsPerHour = new HashMap<>();
    public Map<Date, Integer> randomizationsPerMonth = new LinkedHashMap<>();

    // General Player Distribution
    public Map<Player, Integer> playerFrequencies = new HashMap<>();
    public float team1AveragePlayerOverall = 0;
    public float team2AveragePlayerOverall = 0;

    // User Settings
    public float averageTeamSize = 0;
    public Map<RandomizationMode, Float> randomizationModeDistribution = new HashMap<>();
    public float percentageCurrentPlayersOnlyActivated = 0;
    public float percentageYearRangeSet = 0;
    public int averageStartYear = 0;
    public int averageEndYear = 0;
    public float percentageMinPlayerPPGSet = 0;
    public float averageMinPlayerPPG = 0;
    public float percentageMinPlayerRatingSet = 0;
    public float averageMinPlayerRating = 0;

    protected ReportData() { }

    public ReportData(Date from, Date to) {
        this.fromDate = from;
        this.toDate = to;
    }
}
