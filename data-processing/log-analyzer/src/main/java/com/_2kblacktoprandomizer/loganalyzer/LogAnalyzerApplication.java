package com._2kblacktoprandomizer.loganalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Application to generate reports that analyze log entries captured from users of
 * the NBA 2K Blacktop Player Randomizer application.
 *
 * This application listens on localhost port 4000.
 */
@SpringBootApplication
@EnableConfigurationProperties({AWSProperties.class})
public class LogAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogAnalyzerApplication.class, args);
    }

}
