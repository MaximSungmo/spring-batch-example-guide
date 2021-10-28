//package com.example.batch.demo.ch9;
//
//import com.example.batch.demo.ch9.domain.Customer2;
//import org.hibernate.Session;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.database.HibernateItemWriter;
//import org.springframework.batch.item.database.builder.HibernateItemWriterBuilder;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.persistence.EntityManagerFactory;
//
//@Configuration
//public class HibernateImportJob {
//
//    private JobBuilderFactory jobBuilderFactory;
//
//    private StepBuilderFactory stepBuilderFactory;
//
//    public HibernateImportJob(JobBuilderFactory jobBuilderFactory,
//                              StepBuilderFactory stepBuilderFactory) {
//
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//    }
//
//    @Bean
//    @StepScope
//    public FlatFileItemReader<Customer2> customerFileReader5() {
//
//        return new FlatFileItemReaderBuilder<Customer2>()
//                .name("customerFileReader5")
//                .resource(new ClassPathResource("input/customer.csv"))
//                .delimited()
//                .names(new String[]{"firstName",
//                        "middleInitial",
//                        "lastName",
//                        "address",
//                        "city",
//                        "state",
//                        "zip"})
//                .targetType(Customer2.class)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public HibernateItemWriter<Customer2> hibernateItemWriter(EntityManagerFactory entityManager) {
//        return new HibernateItemWriterBuilder<Customer2>()
//                .sessionFactory(entityManager.unwrap(Session.class).getSessionFactory())
//                .build();
//    }
//
//    @Bean
//    public Step hibernateFormatStep() throws Exception {
//        return this.stepBuilderFactory.get("hibernateFormatStep")
//                .<Customer2, Customer2>chunk(10)
//                .reader(customerFileReader5())
//                .writer(hibernateItemWriter(null))
//                .build();
//    }
//
//    @Bean
//    public Job hibernateFormatJob() throws Exception {
//        return this.jobBuilderFactory.get("hibernateFormatJob")
//                .start(hibernateFormatStep())
//                .build();
//    }
//}