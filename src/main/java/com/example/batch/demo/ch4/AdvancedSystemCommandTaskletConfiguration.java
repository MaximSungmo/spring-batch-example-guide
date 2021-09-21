package com.example.batch.demo.ch4;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdvancedSystemCommandTaskletConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//    @BeanE3
//    public Job advancedSystemCommandJob() {
//        return this.jobBuilderFactory.get("advancedSystemCommandJob").start(advancedSystemCommandStep()).build();
//    }
//
//    @Bean
//    public Step advancedSystemCommandStep() {
//        return this.stepBuilderFactory.get("advancedSystemCommandStep").tasklet(advancedSystemCommandTasklet()).build();
//    }
//
//    @Bean
//    public SystemCommandTasklet advancedSystemCommandTasklet() {
//        SystemCommandTasklet systemCommandTasklet = new SystemCommandTasklet();
//        systemCommandTasklet.setCommand("touch tmp.txt");
//        systemCommandTasklet.setTimeout(5000);
//        systemCommandTasklet.setInterruptOnCancel(true);
//
//        systemCommandTasklet.setWorkingDirectory("/Users/minella/spring-batch");
//        systemCommandTasklet.setTerminationCheckInterval(5000);
//        systemCommandTasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
//        systemCommandTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        systemCommandTasklet.setEnvironmentParams(new String[]{
//                "JAVA_HOME=/java", "BATCH_HOME=/Users/batch"
//        });
//
//        return systemCommandTasklet;
//    }
//
//    @Bean
//    public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
//        return new SimpleSystemProcessExitCodeMapper();
//    }
}
