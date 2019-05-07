package com._2kblacktoprandomizer.loganalyzer.models.report;

import java.util.Date;

public class Report {

    private int reportId;

    private Date fromDate;

    private Date toDate;

    protected Report() { }

    public Report(int reportId, Date fromDate, Date toDate) {
        this.reportId = reportId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getReportId() {
        return reportId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

}
