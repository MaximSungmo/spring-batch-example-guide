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
import org.springframework.core.io.FileSystemResource;

@Configuration
public class DelimetedTextFileJob {
//delimetedJob
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    public DelimetedTextFileJob(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFileReader2() {

        System.out.println("customerFileReader Start");
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerFileReader2")
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
    public FlatFileItemWriter<Customer> customerItemWriter2() {

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
                .name("customerItemWriter2")
                .resource(new FileSystemResource("/output/formatted.txt"))
                .delimited()
                .delimiter(";")
                .names(new String[]{"firstName", "lastName", "address", "city", "state", "zip"})
//				.lineAggregator(lineAggregator)
//                .append(true) // 파일 있을 시 이어쓰기
//                .shouldDeleteIfExists(false) // 파일 있을 시 에러
                .build();
    }

    @Bean
    public Step delimetedStep() {
        return this.stepBuilderFactory.get("delimetedStep")
                .<Customer, Customer>chunk(10)
                .reader(customerFileReader2())
                .writer(customerItemWriter2())
                .build();
    }

    @Bean
    public Job delimetedJob() {
        return this.jobBuilderFactory.get("delimetedJob")
                .start(delimetedStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}