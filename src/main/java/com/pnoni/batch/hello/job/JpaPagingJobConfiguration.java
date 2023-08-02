package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPagingJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaPagingJob() {
        return jobBuilderFactory.get("jpaPagingJob")
                .start(jpaPagingStep())
                .build();
    }

    @Bean
    public Step jpaPagingStep() {
        return stepBuilderFactory.get("jpaPagingStep")
                .<Employee, EmployeeNew>chunk(10)
                .reader(jpaPagingReader())
                .processor(itemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Employee> jpaPagingReader() {
        Map<String, Object> params = new HashMap<>();
        params.put("gender", Employee.Gender.Female);
        params.put("shirtSize", Employee.ShirtSize.S);

        return new JpaPagingItemReaderBuilder<Employee>()
                .name("jpaPagingReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("select e from Employee e where e.gender = :gender and e.shirtSize = :shirtSize " +
                        "order by lastname, firstname")
                .parameterValues(params)
                .build();
    }

    @Bean
    public ItemProcessor<Employee, EmployeeNew> itemProcessor() {
        return EmployeeNew::new;
    }

    @Bean
    public ItemWriter<EmployeeNew> jpaItemWriter() {
        return new JpaItemWriterBuilder<EmployeeNew>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
