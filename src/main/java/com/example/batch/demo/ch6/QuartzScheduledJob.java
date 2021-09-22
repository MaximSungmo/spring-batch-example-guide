package com.example.batch.demo.ch6;

import org.quartz.JobExecutionContext;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzScheduledJob extends QuartzJobBean {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void executeInternal(JobExecutionContext context) throws org.quartz.JobExecutionException {
        Job job = this.applicationContext.getBean("quartzJob", Job.class);

        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();

        try {
            this.jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
