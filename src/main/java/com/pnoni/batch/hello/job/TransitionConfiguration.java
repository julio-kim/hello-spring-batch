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
@Configuration
@RequiredArgsConstructor
public class TransitionConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job transitionJob() {
        return jobBuilderFactory.get("transitionJob")
                .start(transitionStep1())
                    .on("FAILED").to(transitionStep2())
                    .on("*").stop()
                .from(transitionStep1())
                    .on("*").to(transitionStep3())
                    .next(transitionStep4())
                        .on("*").to(transitionStep5())
                .end()
                .build();
    }
    @Bean
    public Step transitionStep1() {
        return stepBuilderFactory.get("transitionStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 1");
                    log.info("====================");
                    throw new RuntimeException("transitionStep1 is failed");

//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step transitionStep2() {
        return stepBuilderFactory.get("transitionStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 2");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step transitionStep3() {
        return stepBuilderFactory.get("transitionStep3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 3");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step transitionStep4() {
        return stepBuilderFactory.get("transitionStep4")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 4");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step transitionStep5() {
        return stepBuilderFactory.get("transitionStep5")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 5");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
