package com._2kblacktoprandomizer.loganalyzer.aws;

import com.amazonaws.AmazonServiceException;

import java.io.File;
import java.io.IOException;

public interface S3Client {

    boolean hasNextLogFile();

    File nextLogFile() throws AmazonServiceException, IOException;

}
