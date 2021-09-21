package com.example.batch.demo.ch6;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job restJob() {
        return jobBuilderFactory.get("restJob")
                .incrementer(new RunIdIncrementer())
                .start(restStep())
                .build();
    }

    @Bean
    public Step restStep() {
        return stepBuilderFactory.get("restStep")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("step 1 ran today!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
