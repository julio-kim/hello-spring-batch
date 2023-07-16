package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemStreamJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemStreamJob() {
        return jobBuilderFactory.get("itemStreamJob")
                .start(streamStep())
                .build();
    }

    @Bean
    public Step streamStep() {
        return stepBuilderFactory.get("streamStep")
                .<String, String>chunk(5)
                .reader(new CustomItemStreamReader(
                        IntStream.range(0, 20).boxed().map(Object::toString).collect(Collectors.toList())
                ))
                .writer(new CustomItemStreamWriter())
                .build();
    }
}
