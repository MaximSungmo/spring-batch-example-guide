package com.example.batch.demo.ch6;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

public class JobLauncherRequest {
    private String name;
    private Properties jobParameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JobParameters getJobParameters() {
        Properties properties = new Properties();
        properties.putAll(this.jobParameters);
        return new JobParametersBuilder(properties).toJobParameters();
    }

    public void setJobParameters(Properties jobParameters) {
        this.jobParameters = jobParameters;
    }
}
