/**
 * Copyright 2018 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.batch.demo.ch10;

import com.example.batch.demo.ch10.domain.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;

/**
 * @author Michael Minella
 */
@Configuration
public class MultithreadedJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job muulthJob() throws Exception {
        return this.jobBuilderFactory.get("muulthJob")
                .incrementer(new RunIdIncrementer())
                .start(mulThStep())
                .build();
    }

    @Bean
    public Step mulThStep() throws Exception {
        return this.stepBuilderFactory.get("mulThStep")
                .<Transaction, Transaction>chunk(100)
                .reader(multiThreadReader())
                .writer(multhWriter(null))
                .taskExecutor(new SimpleAsyncTaskExecutor())// 다중스레드스텝
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Transaction> multhWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("INSERT INTO TRANSACTION (description, credit, timestamp) VALUES (:description, :credit, :timestamp)")
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> multiThreadReader() throws Exception {

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("multiThreadReader")
                .resource(new ClassPathResource("data/bigtransaction.csv"))
                .saveState(false) // 다중스레드스텝
                .delimited()
                .names(new String[]{"description", "credit", "timestamp"})
                .fieldSetMapper(fieldSet -> {
                    Transaction transaction = new Transaction();
                    transaction.setDescription(fieldSet.readString("description"));
                    transaction.setCredit(fieldSet.readBigDecimal("credit"));
                    transaction.setTimestamp(fieldSet.readDate("timestamp", "yyyy-MM-dd HH:mm:ss"));

                    return transaction;
                })
                .build();
    }





    @Bean
    public Job paralStepJob() throws Exception {
        Flow secondFlow = new FlowBuilder<Flow>("secondFlow")
                .start(s1())
                .build();

        Flow paFlow = new FlowBuilder<Flow>("paFlow")
                .start(s2())
                .split(new SimpleAsyncTaskExecutor())
                .add(secondFlow)
                .build();

        return this.jobBuilderFactory.get("paralStepJob")
                .incrementer(new RunIdIncrementer())
                .start(paFlow)
                .end().build();
    }

    @Bean
    public Step s1() throws Exception {
        return this.stepBuilderFactory.get("s1")
                .<Transaction, Transaction>chunk(100)
                .reader(multiThreadReader())
                .writer(multhWriter(null))
                .build();
    }

    @Bean
    public Step s2() throws Exception {
        return this.stepBuilderFactory.get("s2")
                .<Transaction, Transaction>chunk(100)
                .reader(multiThreadReader())
                .writer(multhWriter(null))
                .build();
    }


}
