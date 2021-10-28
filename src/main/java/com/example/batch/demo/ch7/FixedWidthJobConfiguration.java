//package com.example.batch.demo.ch7;
//
//import com.example.batch.demo.ch7.domain.Customer;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.transform.Range;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//
//@Configuration
//public class FixedWidthJobConfiguration {
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<Customer> fixedWidthItemReader(
//            @Value("#{jobParameters['customerFile']}") Resource inputFile) {
//
//        return new FlatFileItemReaderBuilder<Customer>()
//                .name("fixedWidthItemReader")
//                .resource(inputFile)
//                .fixedLength()
//                .columns(new Range[]{new Range(1, 11), new Range(12, 12), new Range(13, 22),
//                        new Range(23, 26), new Range(27, 46), new Range(47, 62), new Range(63, 64),
//                        new Range(65, 69)})
//                .names(new String[]{"firstName", "middleInitial", "lastName",
//                        "addressNumber", "street", "city", "state", "zipCode"})
//                .targetType(Customer.class)
//                .build();
//    }
//
//    @Bean
//    public ItemWriter<Customer> fixedWidthItemWriter() {
//        return (items) -> items.forEach(System.out::println);
//    }
//
//    @Bean
//    public Step fixedWidthStep() {
//        return this.stepBuilderFactory.get("fixedWidthStep")
//                .<Customer, Customer>chunk(10)
//                .reader(fixedWidthItemReader(null))
//                .writer(fixedWidthItemWriter())
//                .build();
//    }
//
//    @Bean
//    public Job fixedWidthJob() {
//        return this.jobBuilderFactory.get("fixedWidthJob")
//                .start(fixedWidthStep())
//                .build();
//    }
//}
