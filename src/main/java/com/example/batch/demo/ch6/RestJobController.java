package com.example.batch.demo.ch6;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobExplorer jobExplorer;


    @PostMapping("/run-1")
    public ExitStatus runJob1(@RequestBody JobLauncherRequest jobLauncherRequest) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Job job = this.applicationContext.getBean(jobLauncherRequest.getName(), Job.class);

        return this.jobLauncher.run(job, jobLauncherRequest.getJobParameters()).getExitStatus();
    }

    /* 파라미터 조작 */
    @PostMapping("/run-2")
    public ExitStatus runJob2(@RequestBody JobLauncherRequest jobLauncherRequest) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Job job = this.applicationContext.getBean(jobLauncherRequest.getName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(jobLauncherRequest.getJobParameters(), this.jobExplorer).getNextJobParameters(job).toJobParameters();

        return this.jobLauncher.run(job, jobParameters).getExitStatus();
    }
}
