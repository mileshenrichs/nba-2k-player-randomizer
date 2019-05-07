package com._2kblacktoprandomizer.loganalyzer.models;

import java.util.List;
import java.util.Objects;

public class Player {

    private double careerPPG;

    private List<PlayerVersion> versions;

    private int latestYearPlayed;

    private String name;

    private Position position;

    private int earliestYearPlayed;

    private int versionIndex;

    public double getCareerPPG() {
        return careerPPG;
    }

    public void setCareerPPG(double careerPPG) {
        this.careerPPG = careerPPG;
    }

    public List<PlayerVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<PlayerVersion> versions) {
        this.versions = versions;
    }

    public int getLatestYearPlayed() {
        return latestYearPlayed;
    }

    public void setLatestYearPlayed(int latestYearPlayed) {
        this.latestYearPlayed = latestYearPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getEarliestYearPlayed() {
        return earliestYearPlayed;
    }

    public void setEarliestYearPlayed(int earliestYearPlayed) {
        this.earliestYearPlayed = earliestYearPlayed;
    }

    public int getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(int versionIndex) {
        this.versionIndex = versionIndex;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Player)) return false;
        Player other = (Player) o;
        return name.equals(other.name) && position.equals(other.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }

    public String toString() {
        return name;
    }
}
