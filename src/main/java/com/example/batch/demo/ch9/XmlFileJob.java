package com.example.batch.demo.ch9;

import com.example.batch.demo.ch9.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class XmlFileJob {
    //formatJob
    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    public XmlFileJob(JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFileReader3() {

        System.out.println("customerFileReader Start");
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerFileReader3")
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
    public StaxEventItemWriter<Customer> staxEventItemWriter() {

        System.out.println("staxEventItemWriter Start");

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        XStreamMarshaller msh = new XStreamMarshaller();

        msh.setAliases(aliases);
        msh.afterPropertiesSet();

        return new StaxEventItemWriterBuilder<Customer>()
                .name("staxWriter")
                .resource(new ClassPathResource("output/formatted.xml"))
                .marshaller(msh)
                .rootTagName("customers")
                .build();
    }

    @Bean
    public Step xmlStep() {
        return this.stepBuilderFactory.get("xmlStep")
                .<Customer, Customer>chunk(10)
                .reader(customerFileReader3())
                .writer(staxEventItemWriter())
                .build();
    }

    @Bean
    public Job xmlJob() {
        return this.jobBuilderFactory.get("xmlJob")
                .start(xmlStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}