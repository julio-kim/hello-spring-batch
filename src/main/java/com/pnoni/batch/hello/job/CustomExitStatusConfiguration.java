package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomExitStatusConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job customExitStatusJob() {
        return jobBuilderFactory.get("customExitStatusJob")
                .start(customExitStep1())
                    .on("FAILED").to(customExitStep2())
                    .on("PASS").stop()
                    .end()
                .build();
    }

    @Bean
    public Step customExitStep1() {
        return stepBuilderFactory.get("customExitStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    stepContribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
                    log.info("====================");
                    log.info("Hello Spring Batch 1");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step customExitStep2() {
        return stepBuilderFactory.get("customExitStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 2");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        // Conditional Logic??
                        return new ExitStatus("PASS");
                    }
                })
                .build();
    }
}
