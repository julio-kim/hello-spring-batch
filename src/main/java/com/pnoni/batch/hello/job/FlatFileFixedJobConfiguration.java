package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlatFileFixedJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileFixedJob() {
        return jobBuilderFactory.get("flatFileFixedJob")
                .start(flatFileFixedStep())
                .build();
    }

    @Bean
    public Step flatFileFixedStep() {
        return stepBuilderFactory.get("flatFileFixedStep")
                .<BatchLog, BatchLog>chunk(5)
                .reader(filatFileFixedItemReader())
                .writer(items -> log.info("Items: {}", items))
                .build();
    }

    @Bean
    public ItemReader<BatchLog> filatFileFixedItemReader() {
        return new FlatFileItemReaderBuilder<BatchLog>()
                .name("flatFileFixed")
                .resource(new ClassPathResource("template/batch.log"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(BatchLog.class)
                .fixedLength()
//                .strict(false)
                .addColumns(new Range(1, 24))
                .addColumns(new Range(26, 30))
                .addColumns(new Range(42, 57))
                .addColumns(new Range(59, 98))
                .addColumns(new Range(102))
//                .addColumns(new Range(150))
                .names("occurTime", "level", "threadName", "clazz", "message")
                .build();
    }
}