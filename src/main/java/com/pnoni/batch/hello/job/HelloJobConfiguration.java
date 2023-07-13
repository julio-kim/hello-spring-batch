package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class HelloJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .next(helloStep2())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 1");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 2");
                    log.info("====================");
//                    throw new RuntimeException("helloStep2 is failed...");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
