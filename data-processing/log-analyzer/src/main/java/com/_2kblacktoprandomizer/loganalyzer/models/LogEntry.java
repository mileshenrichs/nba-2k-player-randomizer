package com._2kblacktoprandomizer.loganalyzer.models;

import java.util.Date;

public class LogEntry {

    private Date date;

    private ApplicationState state;

    public LogEntry(Date date, ApplicationState state) {
        this.date = date;
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public ApplicationState getState() {
        return state;
    }
}
