package com.example.batch.demo.ch9;

import com.example.batch.demo.ch9.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FormattedTextFileJob {
    //formatJob
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    public FormattedTextFileJob(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFileReader() {

        System.out.println("customerFileReader Start");
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerFileReader")
                .resource(new ClassPathResource("input/customer.csv"))
                .delimited()
                .names(new String[]{"firstName",
                        "middleInitial",
                        "lastName",
                        "address",
                        "city",
                        "state",
                        "zip"})
                .targetType(Customer.class)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Customer> customerItemWriter() {

        System.out.println("customerItemWriter Start");
//		BeanWrapperFieldExtractor<Customer> fieldExtractor = new BeanWrapperFieldExtractor<>();
//
//		fieldExtractor.setNames(new String[] {"firstName", "lastName", "address", "city", "state", "zip"});
//
//		fieldExtractor.afterPropertiesSet();
//
//		FormatterLineAggregator<Customer> lineAggregator = new FormatterLineAggregator<>();
//
//		lineAggregator.setFieldExtractor(fieldExtractor);
//		lineAggregator.setFormat("%s %s lives at %s %s in %s, %s.");

        return new FlatFileItemWriterBuilder<Customer>()
                .name("customerItemWriter")
                .resource(new ClassPathResource("output/formatted.txt"))
                .formatted()
                .format("%s %s lives at %s %s in %s, %s.")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
//				.lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public Step formatStep() {
        return this.stepBuilderFactory.get("formatStep")
                .<Customer, Customer>chunk(3)
                .reader(customerFileReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job formatJob() {
        return this.jobBuilderFactory.get("formatJob")
                .start(formatStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}