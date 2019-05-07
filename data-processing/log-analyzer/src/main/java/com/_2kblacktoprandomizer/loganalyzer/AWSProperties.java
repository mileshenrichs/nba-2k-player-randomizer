package com._2kblacktoprandomizer.loganalyzer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AWSProperties {

    /**
     * AWS Client ID
     */
    private String accessKey = "";

    /**
     * AWS Client Secret
     */
    private String secretKey = "";

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
