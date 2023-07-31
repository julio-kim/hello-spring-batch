package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MybatisCursorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job mybatisCursorJob() {
        return jobBuilderFactory.get("mybatisCursorJob")
                .start(mybatisCursorStep())
                .build();
    }

    @Bean
    public Step mybatisCursorStep() {
        return stepBuilderFactory.get("mybatisCursorStep")
                .<Employee, Employee>chunk(10)
                .reader(mybatisCursorReader())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> mybatisCursorReader() {
        Map<String, Object> params = new HashMap<>();
        params.put("gender", Employee.Gender.Female);
        params.put("shirtSize", Employee.ShirtSize.S);

        return new MyBatisCursorItemReaderBuilder<Employee>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("selectEmployees")
                .parameterValues(params)
                .build();
    }
}
