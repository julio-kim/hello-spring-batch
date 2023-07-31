package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaCursorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaCursorJob() {
        return jobBuilderFactory.get("jpaCursorJob")
                .start(jpaCursorStep())
                .build();
    }

    @Bean
    public Step jpaCursorStep() {
        return stepBuilderFactory.get("jpaCursorStep")
                .<Employee, Employee>chunk(10)
                .reader(jpaCursorReader())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> jpaCursorReader() {
        Map<String, Object> params = new HashMap<>();
        params.put("gender", Employee.Gender.Female);
        params.put("shirtSize", Employee.ShirtSize.S);
        return new JpaCursorItemReaderBuilder<Employee>()
                .name("jpaCursorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select e from Employee e where e.gender = :gender and e.shirtSize = :shirtSize " +
                        "order by lastname, firstname")
                .parameterValues(params)
                .build();
    }
}
