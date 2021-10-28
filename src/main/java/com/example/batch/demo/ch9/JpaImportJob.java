package com.example.batch.demo.ch9;

import com.example.batch.demo.ch9.domain.Customer2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
public class JpaImportJob {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    public JpaImportJob(JobBuilderFactory jobBuilderFactory,
                        StepBuilderFactory stepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer2> customerFileReader5() {

        return new FlatFileItemReaderBuilder<Customer2>()
                .name("customerFileReader5")
                .resource(new ClassPathResource("input/customer.csv"))
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer2.class)
                .build();
    }

    @Bean
    public JpaItemWriter<Customer2> jpaItemWriter(EntityManagerFactory entityManager) {

        JpaItemWriter<Customer2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManager);
        return jpaItemWriter;
    }

    @Bean
    public Step jpaFormatStep() throws Exception {
        return this.stepBuilderFactory.get("jpaFormatStep")
                .<Customer2, Customer2>chunk(10)
                .reader(customerFileReader5())
                .writer(jpaItemWriter(null))
                .build();
    }

    @Bean
    public Job jpaFormatJob() throws Exception {
        return this.jobBuilderFactory.get("jpaFormatJob")
                .start(jpaFormatStep())
                .build();
    }
}