package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcCursorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job jdbcCursorJob() {
        return jobBuilderFactory.get("jdbcCursorJob")
                .start(jdbcCursorStep())
                .build();
    }

    @Bean
    public Step jdbcCursorStep() {
        return stepBuilderFactory.get("jdbcCursorStep")
                .<Employee, Employee>chunk(10)
                .reader(jdbcCursorReader())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> jdbcCursorReader() {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("jdbcCursorReader")
                .fetchSize(10)
                .sql("select * from employee where gender = ? and shirt_size = ? " +
                        "order by lastname, firstname")
                .beanRowMapper(Employee.class)
                .queryArguments("Female", "S")
                .dataSource(dataSource)
                .build();
    }
}
