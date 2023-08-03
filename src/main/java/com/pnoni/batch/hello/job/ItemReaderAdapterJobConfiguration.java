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
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemReaderAdapterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EmployeeService employeeService;

    @Bean
    public Job itemReaderAdapterJob() {
        return jobBuilderFactory.get("itemReaderAdapterJob")
                .start(itemReaderAdapterStep())
                .build();
    }

    @Bean
    public Step itemReaderAdapterStep() {
        return stepBuilderFactory.get("itemReaderAdapterStep")
                .<Employee, EmployeeNew>chunk(10)
                .reader(itemReaderAdapter())
                .processor(adapterItemProcessor())
                .writer(itemWriterAdapter())
                .build();
    }

    @Bean
    public ItemReader<Employee> itemReaderAdapter() {
        ItemReaderAdapter<Employee> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(employeeService);
        reader.setTargetMethod("getEmployee");
        return reader;
    }

    @Bean
    public ItemProcessor<Employee, EmployeeNew> adapterItemProcessor() {
        return EmployeeNew::new;
    }

    @Bean
    public ItemWriter<EmployeeNew> itemWriterAdapter() {
        ItemWriterAdapter<EmployeeNew> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(employeeService);
        writer.setTargetMethod("setEmployee");
        return writer;
    }

}
