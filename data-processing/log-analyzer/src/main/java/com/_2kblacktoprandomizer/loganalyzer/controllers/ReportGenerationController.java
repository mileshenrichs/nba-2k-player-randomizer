package com._2kblacktoprandomizer.loganalyzer.controllers;

import com._2kblacktoprandomizer.loganalyzer.reportgeneration.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@RestController
public class ReportGenerationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ReportGenerator reportGenerator;

    @Autowired
    public ReportGenerationController(ReportGenerator reportGenerator) {
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
            long startEpoch = 1546300800L;
            generatedReportId = reportGenerator.generateReport(new Date(startEpoch), new Date());
        } else {
            generatedReportId = reportGenerator.generateReport(from, to);
        }

        logger.info("New report successfully generated.  Report id: " + generatedReportId);

        return generatedReportId;
    }

}
