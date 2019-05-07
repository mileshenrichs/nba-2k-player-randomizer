package com._2kblacktoprandomizer.loganalyzer.models.report;

import java.util.ArrayList;
import java.util.List;

public class ReportsMetadata {

    private int nextReportId;

    private List<Report> generatedReports;

    public ReportsMetadata() {
        this.nextReportId = 1;
        this.generatedReports = new ArrayList<>();
    }

    public List<Report> getGeneratedReports() {
        return generatedReports;
    }

    public int addGeneratedReport(ReportData newReport) {
        Report report = new Report(nextReportId, newReport.fromDate, newReport.toDate);
        this.generatedReports.add(report);
        return nextReportId++;
    }

    public int getNextReportId() {
        return nextReportId;
    }

    public void setNextReportId(int nextReportId) {
        this.nextReportId = nextReportId;
    }

    public void setGeneratedReports(List<Report> generatedReports) {
        this.generatedReports = generatedReports;
    }

}
