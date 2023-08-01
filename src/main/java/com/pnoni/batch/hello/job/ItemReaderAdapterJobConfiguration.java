package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReaderAdapterJob() {
        return jobBuilderFactory.get("itemReaderAdapterJob")
                .start(itemReaderAdapterStep())
                .build();
    }

    @Bean
    public Step itemReaderAdapterStep() {
        return stepBuilderFactory.get("itemReaderAdapterStep")
                .<Employee, Employee>chunk(10)
                .reader(itemReaderAdapter())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> itemReaderAdapter() {
        ItemReaderAdapter<Employee> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(employeeService());
        reader.setTargetMethod("getEmployee");
        return reader;
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeService();
    }
}
