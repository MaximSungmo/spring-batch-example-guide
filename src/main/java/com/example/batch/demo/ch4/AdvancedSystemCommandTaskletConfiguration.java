package com.example.batch.demo.ch4;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class AdvancedSystemCommandTaskletConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job advancedSystemCommandJob() {
        return this.jobBuilderFactory.get("advancedSystemCommandJob").start(advancedSystemCommandStep()).build();
    }

    @Bean
    public Step advancedSystemCommandStep() {
        return this.stepBuilderFactory.get("advancedSystemCommandStep").tasklet(advancedSystemCommandTasklet()).build();
    }

    @Bean
    public SystemCommandTasklet advancedSystemCommandTasklet() {
        SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();
        systemCommandTasklet.setCommand("touch tmp.txt");
        systemCommandTasklet.setTimeout(5000);
        systemCommandTasklet.setInterruptOnCancel(true);

        systemCommandTasklet.setWorkingDirectory("/Userss/mminella/spring-batch");
        systemCommandTasklet.setTerminationCheckInterval(5000);
        systemCommandTasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
        systemCommandTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
        systemCommandTasklet.setEnvironmentParams(new String[]{
                "JAVA_HOME=/java", "BATCH_HOME=/Users/batch"
        });

        return systemCommandTasklet;
    }

    @Bean
    public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
        return new SimpleSystemProcessExitCodeMapper();
    }
}
