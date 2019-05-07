package com._2kblacktoprandomizer.loganalyzer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerVersion {

    @JsonProperty("isCurrent")
    private boolean isCurrent;

    private int rating;

    private String team;

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
