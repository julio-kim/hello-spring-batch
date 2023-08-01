package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MybatisPagingJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job mybatisPagingJob() {
        return jobBuilderFactory.get("mybatisPagingJob")
                .start(mybatisPagingStep())
                .build();
    }

    @Bean
    public Step mybatisPagingStep() {
        return stepBuilderFactory.get("mybatisPagingStep")
                .<Employee, Employee>chunk(10)
                .reader(mybatisPagingReader())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> mybatisPagingReader() {
        Map<String, Object> params = new HashMap<>();
        params.put("gender", Employee.Gender.Female);
        params.put("shirtSize", Employee.ShirtSize.S);

        return new MyBatisPagingItemReaderBuilder<Employee>()
                .sqlSessionFactory(sqlSessionFactory)
                .pageSize(10)
                .queryId("selectPagedEmployees")
                .parameterValues(params)
                .build();
    }
}
