package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .next(simpleStep2())
//                .validator(jobParameters -> {
//                    if (Optional.ofNullable(jobParameters).isEmpty() ||
//                                Optional.ofNullable(jobParameters.getString("name")).isEmpty())
//                        throw new JobParametersInvalidException("'name' parameters is not founded");
//                })
                .validator(new DefaultJobParametersValidator(new String[] {"name"}, new String[] {"count"}))
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Simple Spring Batch 1");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step simpleStep2() {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Simple Spring Batch 2");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
