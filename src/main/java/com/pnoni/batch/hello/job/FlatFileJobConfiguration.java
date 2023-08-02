package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlatFileJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
                .start(flatFileStep())
                .build();
    }

    @Bean
    public Step flatFileStep() {
        return stepBuilderFactory.get("flatFileStep")
                .<Employee, Employee>chunk(5)
                .reader(flatFileItemReader())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Employee> flatFileItemReader() {
        return new FlatFileItemReaderBuilder<Employee>()
                .name("flatFile")
                .resource(new ClassPathResource("template/employee.csv"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(Employee.class)
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("id", "firstname", "lastname", "email", "job")
                .build();
    }

    @Bean
    public ItemWriter<Employee> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<Employee>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("target/employee.psv"))
                .append(true)
                .shouldDeleteIfEmpty(true)
                .delimited().delimiter("|")
                .names("id", "firstname", "lastname", "email", "job")
                .build();
    }
}
