package com._2kblacktoprandomizer.loganalyzer.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service("real-s3client")
public class RealS3Client implements S3Client {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${aws.access-key}")
    private String awsAccessKeyId;

    @Value("${aws.secret-key}")
    private String awsSecretKeyId;

    private AmazonS3 s3Client;

    private List<String> logFileNames = new ArrayList<>();

    private int nextLogFileIndex = 1;

    public boolean hasNextLogFile() {
        return nextLogFileIndex < logFileNames.size();
    }

    public File nextLogFile() throws AmazonServiceException, IOException {
        return getFile(logFileNames.get(nextLogFileIndex++));
    }

    @PostConstruct
    private void instantiateClient() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKeyId);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        getLogFileNames();
    }

    private void getLogFileNames() {
        ListObjectsV2Result result = s3Client.listObjectsV2("nba2krandomizer-logs");
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        for (S3ObjectSummary os : objects) {
            logFileNames.add(os.getKey());
        }
    }

    private File getFile(String key) throws AmazonServiceException, IOException {
        logger.info("Downloading log file from S3 with key: " + key);

        S3Object o = s3Client.getObject("nba2krandomizer-logs", key);
        S3ObjectInputStream s3is = o.getObjectContent();
        FileOutputStream fos = new FileOutputStream(getTempFilePath(key));
        byte[] read_buf = new byte[1024];
        int read_len;
        while ((read_len = s3is.read(read_buf)) > 0) {
            fos.write(read_buf, 0, read_len);
        }
        s3is.close();
        fos.close();

        return getTempFilePath(key);
    }

    private File getTempFilePath(String fileName) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path tempFolder = Paths.get(currentPath.toString(), "tmp");
        Path newFilePath = Paths.get(tempFolder.toString(), fileName);
        return new File(newFilePath.toUri());
    }
}
