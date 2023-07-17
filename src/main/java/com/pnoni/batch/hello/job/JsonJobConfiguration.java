package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JsonJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jsonJob() {
        return jobBuilderFactory.get("jsonJob")
                .incrementer(new RunIdIncrementer())
                .start(jsonStep())
                .build();
    }

    @Bean
    public Step jsonStep() {
        return stepBuilderFactory.get("jsonStep")
                .<Employee, Employee>chunk(5)
                .reader(jsonItemReader())
                .writer(items -> log.info("Items : {}", items))
                .build();
    }

    @Bean
    public ItemReader<Employee> jsonItemReader() {
        return new JsonItemReaderBuilder<Employee>()
                .name("jsonReader")
                .resource(new ClassPathResource("template/employee.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Employee.class))
                .build();
    }
}
