package com._2kblacktoprandomizer.loganalyzer.reportgeneration;

import com._2kblacktoprandomizer.loganalyzer.aws.LogFetcher;
import com._2kblacktoprandomizer.loganalyzer.models.LogEntry;
import com._2kblacktoprandomizer.loganalyzer.models.Player;
import com._2kblacktoprandomizer.loganalyzer.models.RandomizationMode;
import com._2kblacktoprandomizer.loganalyzer.models.report.ReportData;
import com._2kblacktoprandomizer.loganalyzer.models.report.ReportsMetadata;
import com.amazonaws.AmazonServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportGenerator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private LogFetcher logFetcher;

    @Autowired
    public ReportGenerator(LogFetcher logFetcher) {
        this.logFetcher = logFetcher;
    }

    public int generateReport(Date from, Date to) throws AmazonServiceException, IOException, ParseException {
        logger.info("Generating report from date " + from + " to date " + to);

        // Get all relevant log entries from AWS S3
        List<LogEntry> logEntries = logFetcher.getLogEntries(from, to);
        logger.info(logEntries.size() + " log entries collected");
        logger.info("Beginning processing of log entries");

        ReportData report = new ReportData(from, to);
        for(LogEntry entry : logEntries) {
            report.totalRandomizations++;
            Calendar calDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
            calDate.setTime(entry.getDate());

            // Randomizations per day
            Date day = new GregorianCalendar(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH),
                    calDate.get(Calendar.DAY_OF_MONTH)).getTime();
            report.randomizationsPerDay.put(day, report.randomizationsPerDay.getOrDefault(day, 0) + 1);

            // Randomizations per day of week
            DayOfWeek dayOfWeek = DayOfWeek.from(calDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            report.randomizationsPerDayOfWeek.put(dayOfWeek, report.randomizationsPerDayOfWeek.getOrDefault(dayOfWeek, 0) + 1);

            // Randomizations per hour
            int hour = calDate.get(Calendar.HOUR_OF_DAY) + 1;
            report.randomizationsPerHour.put(hour, report.randomizationsPerHour.getOrDefault(hour, 0) + 1);

            // Randomizations per month
            Date month = new GregorianCalendar(calDate.get(Calendar.YEAR), calDate.get(Calendar.MONTH), 1).getTime();
            report.randomizationsPerMonth.put(month, report.randomizationsPerMonth.getOrDefault(month, 0) + 1);

            // Player distribution
            for(Player player : entry.getState().getTeam1()) {
                // Player frequency
                report.playerFrequencies.put(player, report.playerFrequencies.getOrDefault(player, 0) + 1);

                // Team 1 player average overall ratings
                report.team1AveragePlayerOverall += player.getVersions().get(player.getVersionIndex()).getRating();
            }
            for(Player player : entry.getState().getTeam2()) {
                // Player frequency
                report.playerFrequencies.put(player, report.playerFrequencies.getOrDefault(player, 0) + 1);

                // Team 2 player average overall ratings
                report.team2AveragePlayerOverall += player.getVersions().get(player.getVersionIndex()).getRating();
            }

            // Average team size
            report.totalPlayersGenerated += entry.getState().getTeam1().size() * 2;
            report.averageTeamSize += entry.getState().getTeam1().size();

            // Randomization mode percentage distribution
            RandomizationMode mode = entry.getState().getRandomizationMode();
            report.randomizationModeDistribution.put(mode, report.randomizationModeDistribution.getOrDefault(mode, 0F) + 1);

            // Percentage current players only setting on/off
            if(entry.getState().isCurrentPlayersOnly())
                report.percentageCurrentPlayersOnlyActivated++;

            // Frequency with which year range is set by user
            if(entry.getState().getYearWindow().isFilterActive() &&
                    !(entry.getState().getYearWindow().getFromYear().trim().isEmpty() ||
                      entry.getState().getYearWindow().getToYear().trim().isEmpty())) {
                report.percentageYearRangeSet++;

                // Average start year
                report.averageStartYear += Integer.parseInt(entry.getState().getYearWindow().getFromYear());

                // Average end year
                report.averageEndYear += Integer.parseInt(entry.getState().getYearWindow().getToYear());
            }

            // Frequency with which minimum player PPG is set by user
            if(entry.getState().isMinimumPlayerPPGActive() && !entry.getState().getMinimumPlayerPPG().trim().isEmpty()) {
                report.percentageMinPlayerPPGSet++;

                // Average minimum player PPG
                report.averageMinPlayerPPG += Float.parseFloat(entry.getState().getMinimumPlayerPPG());
            }

            // Frequency with which minimum player rating is set by user
            if(entry.getState().isMinimumPlayerRatingActive() && !entry.getState().getMinimumPlayerRating().trim().isEmpty()) {
                report.percentageMinPlayerRatingSet++;

                // Average minimum player rating
                report.averageMinPlayerRating += Integer.parseInt(entry.getState().getMinimumPlayerRating());
            }
        }

        // Divide for fields that represent percentages
        report.percentageCurrentPlayersOnlyActivated /= report.totalRandomizations;
        report.percentageYearRangeSet /= report.totalRandomizations;
        report.percentageMinPlayerPPGSet /= report.totalRandomizations;
        report.percentageMinPlayerRatingSet /= report.totalRandomizations;

        // Divide for fields that represent averages
        report.team1AveragePlayerOverall /= (report.totalPlayersGenerated / 2.0);
        report.team2AveragePlayerOverall /= (report.totalPlayersGenerated / 2.0);
        report.averageTeamSize /= report.totalRandomizations;
        report.averageStartYear /= (report.percentageYearRangeSet * report.totalRandomizations);
        report.averageEndYear /= (report.percentageYearRangeSet * report.totalRandomizations);
        report.averageMinPlayerPPG /= (report.percentageMinPlayerPPGSet * report.totalRandomizations);
        report.averageMinPlayerRating /= (report.percentageMinPlayerRatingSet * report.totalRandomizations);

        // Sort player frequencies in descending order
        report.playerFrequencies = report.playerFrequencies
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));

        return saveReportJson(report);
    }

    private int saveReportJson(ReportData report) throws IOException {
        // Update reports metadata
        ReportsMetadata reportsMetadata = readMetadataFile();
        int reportId = reportsMetadata.addGeneratedReport(report);
        writeUpdatedMetadataFile(reportsMetadata);

        // Write report data to file
        ObjectMapper objectMapper = new ObjectMapper();
        File outputFile = getHandleForReportJsonFileWithId(reportId);
        objectMapper.writeValue(outputFile, report);

        return reportId;
    }

    private ReportsMetadata readMetadataFile() throws IOException {
        ReportsMetadata reportsMetadata;
        File reportsMetaFile = getReportsMetadataFile();
        if(reportsMetaFile.exists()) {
            String contents = new String(Files.readAllBytes(reportsMetaFile.toPath()), Charset.forName("UTF-8"));
            ObjectMapper objectMapper = new ObjectMapper();
            reportsMetadata = objectMapper.readValue(contents, ReportsMetadata.class);
        } else {
            reportsMetadata = new ReportsMetadata();
        }

        return reportsMetadata;
    }

    private void writeUpdatedMetadataFile(ReportsMetadata updatedMetadata) throws IOException {
        File reportsMetaFile = getReportsMetadataFile();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(reportsMetaFile, updatedMetadata);
    }

    private File getReportsMetadataFile() {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path metaFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", "reports", "meta.json");
        return new File(metaFilePath.toUri());
    }

    private File getHandleForReportJsonFileWithId(int reportId) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path metaFilePath = Paths.get(currentPath.toString(), "src", "main", "resources", "reports", reportId + ".json");
        return new File(metaFilePath.toUri());
    }

}
