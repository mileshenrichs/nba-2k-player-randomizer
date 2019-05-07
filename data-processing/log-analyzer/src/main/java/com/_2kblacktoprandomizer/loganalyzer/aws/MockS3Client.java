package com._2kblacktoprandomizer.loganalyzer.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service("mock-s3client")
public class MockS3Client implements S3Client {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private boolean hasUsedLogFile;

    public boolean hasNextLogFile() {
        return !hasUsedLogFile;
    }

    public File nextLogFile() throws IOException {
        logger.info("Getting mock log file");
        hasUsedLogFile = true;

        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path tempFolder = Paths.get(currentPath.toString(), "tmp");
        Path logFilePath = Paths.get(tempFolder.toString(), "mock-log-file-long.txt");

        // make copy of mock log file
        Path copyFilePath = Paths.get(tempFolder.toString(), "mock-log-file-copy.txt");
        Files.copy(logFilePath, copyFilePath, StandardCopyOption.REPLACE_EXISTING);

        return new File(copyFilePath.toUri());
    }

}
