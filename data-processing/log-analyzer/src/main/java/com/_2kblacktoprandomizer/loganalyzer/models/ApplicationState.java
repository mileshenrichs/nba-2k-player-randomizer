package com._2kblacktoprandomizer.loganalyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationState {

    private RandomizationMode randomizationMode;

    private boolean currentPlayersOnly;

    private YearWindow yearWindow;

    private boolean minimumPlayerPPGActive;

    private String minimumPlayerPPG;

    private boolean minimumPlayerRatingActive;

    private String minimumPlayerRating;

    private List<Player> team1;

    private List<Player> team2;

    public RandomizationMode getRandomizationMode() {
        return randomizationMode;
    }

    public void setRandomizationMode(RandomizationMode randomizationMode) {
        this.randomizationMode = randomizationMode;
    }

    public boolean isCurrentPlayersOnly() {
        return currentPlayersOnly;
    }

    public void setCurrentPlayersOnly(boolean currentPlayersOnly) {
        this.currentPlayersOnly = currentPlayersOnly;
    }

    public YearWindow getYearWindow() {
        return yearWindow;
    }

    public void setYearWindow(YearWindow yearWindow) {
        this.yearWindow = yearWindow;
    }

    public boolean isMinimumPlayerPPGActive() {
        return minimumPlayerPPGActive;
    }

    public void setMinimumPlayerPPGActive(boolean minimumPlayerPPGActive) {
        this.minimumPlayerPPGActive = minimumPlayerPPGActive;
    }

    public String getMinimumPlayerPPG() {
        return minimumPlayerPPG;
    }

    public void setMinimumPlayerPPG(String minimumPlayerPPG) {
        this.minimumPlayerPPG = minimumPlayerPPG;
    }

    public boolean isMinimumPlayerRatingActive() {
        return minimumPlayerRatingActive;
    }

    public void setMinimumPlayerRatingActive(boolean minimumPlayerRatingActive) {
        this.minimumPlayerRatingActive = minimumPlayerRatingActive;
    }

    public String getMinimumPlayerRating() {
        return minimumPlayerRating;
    }

    public void setMinimumPlayerRating(String minimumPlayerRating) {
        this.minimumPlayerRating = minimumPlayerRating;
    }

    public List<Player> getTeam1() {
        return team1;
    }

    public void setTeam1(List<Player> team1) {
        this.team1 = team1;
    }

    public List<Player> getTeam2() {
        return team2;
    }

    public void setTeam2(List<Player> team2) {
        this.team2 = team2;
    }
}
