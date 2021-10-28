//package com.example.batch.demo.ch6;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class QuartzJobConfiguration {
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job quartzJob() {
//        return jobBuilderFactory.get("quartzJob")
//                .incrementer(new RunIdIncrementer())
//                .start(quartzStep())
//                .build();
//    }
//
//    @Bean
//    public Step quartzStep() {
//        return stepBuilderFactory.get("quartzStep")
//                .tasklet((stepContribution, chunkContext) -> {
//                    System.out.println("[CH6] quartz step 1 ran today!");
//                    return RepeatStatus.FINISHED;
//                }).build();
//    }
//}
