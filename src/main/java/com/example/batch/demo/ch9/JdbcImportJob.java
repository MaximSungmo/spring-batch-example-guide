package com.example.batch.demo.ch9;

import com.example.batch.demo.ch9.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class JdbcImportJob {

	private JobBuilderFactory jobBuilderFactory;

	private StepBuilderFactory stepBuilderFactory;

	public JdbcImportJob(JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory) {

		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> customerFileReader4() {

		return new FlatFileItemReaderBuilder<Customer>()
				.name("customerFileReader4")
				.resource(new ClassPathResource("input/customer.csv"))
				.delimited()
				.names(new String[] {"firstName",
						"middleInitial",
						"lastName",
						"address",
						"city",
						"state",
						"zip"})
				.targetType(Customer.class)
				.build();
	}
//
//	@Bean
//	public JdbcBatchItemWriter<Customer> jdbcCustomerWriter(DataSource dataSource) throws Exception {
//		return new JdbcBatchItemWriterBuilder<Customer>()
//				.dataSource(dataSource)
//				.sql("INSERT INTO CUSTOMER (first_name, " +
//						"middle_initial, " +
//						"last_name, " +
//						"address, " +
//						"city, " +
//						"state, " +
//						"zip) VALUES (?, ?, ?, ?, ?, ?, ?)")
//				.itemPreparedStatementSetter(new CustomerItemPreparedStatementSetter())
//				.build();
//	}

	@Bean
	public JdbcBatchItemWriter<Customer> jdbcCustomerWriter(DataSource dataSource) throws Exception {
		return new JdbcBatchItemWriterBuilder<Customer>()
				.dataSource(dataSource)
				.sql("INSERT INTO CUSTOMER (first_name, " +
						"middle_initial, " +
						"last_name, " +
						"address, " +
						"city, " +
						"state, " +
						"zip) VALUES (:firstName, " +
						":middleInitial, " +
						":lastName, " +
						":address, " +
						":city, " +
						":state, " +
						":zip)")
				.beanMapped()
				.build();
	}

	@Bean
	public Step jdbcFormatStep() throws Exception {
		return this.stepBuilderFactory.get("jdbcFormatStep")
				.<Customer, Customer>chunk(10)
				.reader(customerFileReader4())
				.writer(jdbcCustomerWriter(null))
				.build();
	}

	@Bean
	public Job jdbcFormatJob() throws Exception {
		return this.jobBuilderFactory.get("jdbcFormatJob")
				.start(jdbcFormatStep())
				.build();
	}
}