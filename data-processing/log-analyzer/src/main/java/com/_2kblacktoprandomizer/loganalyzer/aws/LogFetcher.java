package com._2kblacktoprandomizer.loganalyzer.aws;

import com._2kblacktoprandomizer.loganalyzer.models.ApplicationState;
import com._2kblacktoprandomizer.loganalyzer.models.LogEntry;
import com.amazonaws.AmazonServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Service
public class LogFetcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private S3Client s3Client;
                                                                // 4/20/2019, 11:00:36 PM
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy, h:mm:ss a");

    @Autowired
    public LogFetcher(@Qualifier("mock-s3client") S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<LogEntry> getLogEntries(Date from, Date to) throws AmazonServiceException, IOException, ParseException {
        logger.info("Fetching log entries from S3");
        List<LogEntry> entries = new ArrayList<>();

        while(s3Client.hasNextLogFile()) {
            File logFile = s3Client.nextLogFile();
            collectEntriesFromFile(logFile, entries, from, to);
            if(!logFile.delete()) {
                logger.warn("Error deleting temporary log file: " + logFile.getName());
            }
        }

        return entries;
    }

    private void collectEntriesFromFile(File logFile, List<LogEntry> entries, Date from, Date to)
            throws IOException, ParseException {
        logger.info("Collecting log entries from file: " + logFile.getName());

        if(logFile.getName().equals("log0.txt")) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String logFileStr = readFile(logFile);
        String[] logEntryStrings = logFileStr.split("\n--------------------\n");

        for(int i = 1; i < logEntryStrings.length; i++) {
            String logEntryString = logEntryStrings[i];
            Scanner scanner = new Scanner(logEntryString);

            String entryDateStr = scanner.nextLine();
            Date entryDate = simpleDateFormat.parse(entryDateStr);
            // since entries are chronological, if encounter one that comes after
            // end date, stop reading from file
            if(entryDate.after(to)) {
                break;
            }

            if(!entryDate.before(from)) {
                String stateString = readRestOfString(scanner);
                ApplicationState appState = objectMapper.readValue(stateString, ApplicationState.class);
                entries.add(new LogEntry(entryDate, appState));
            }
        }
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), Charset.forName("UTF-8"));
    }

    private String readRestOfString(Scanner scanner) {
        StringBuilder builder = new StringBuilder();
        while(scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        scanner.close();
        return builder.toString();
    }

}
