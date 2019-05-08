package com._2kblacktoprandomizer.loganalyzer.controllers;

import com._2kblacktoprandomizer.loganalyzer.models.report.Report;
import com._2kblacktoprandomizer.loganalyzer.models.report.ReportsMetadata;
import com._2kblacktoprandomizer.loganalyzer.reportgeneration.ReportGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ReportController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ReportGenerator reportGenerator;

    @Autowired
    public ReportController(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @PostMapping("/generate-report")
    @ResponseStatus(HttpStatus.OK)
    public int generateReport(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "MM-dd-yyyy") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "MM-dd-yyyy") Date to
    ) throws IOException, ParseException {
        int generatedReportId;

        if(from == null && to == null) {
            // Jan 1, 2019
            long startEpoch = 1546322400000L;
            generatedReportId = reportGenerator.generateReport(new Date(startEpoch), new Date());
        } else {
            generatedReportId = reportGenerator.generateReport(from, to);
        }

        logger.info("New report successfully generated.  Report id: " + generatedReportId);

        return generatedReportId;
    }

    @GetMapping(value = "/reports/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getReport(@PathVariable int reportId) throws IOException {
        logger.info("Fetching report with id: " + reportId);

        File reportFile = new File(getClass().getClassLoader()
                .getResource("reports/" + reportId + ".json").getFile());

        return new String(Files.readAllBytes(reportFile.toPath()), Charset.forName("UTF-8"));
    }

    @GetMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Report> getExistingReports() throws IOException {
        logger.info("Fetching all existing reports");

        File metaFile = new File(getClass().getClassLoader()
                .getResource("reports/meta.json").getFile());

        ReportsMetadata reportsMetadata = new ObjectMapper().readValue(metaFile, ReportsMetadata.class);

        return reportsMetadata.getGeneratedReports();
    }

}
