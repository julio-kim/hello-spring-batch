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
public class FlowJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                .start(helloFlowStep1())
                .on("COMPLETED").to(helloFlowStep2())
                .from(helloFlowStep1())
                .on("FAILED").to(helloFlowStep3())
                .end()
                .build();
    }

    @Bean
    public Step helloFlowStep1() {
        return stepBuilderFactory.get("helloFlowStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Flow Spring Batch 1");
                    log.info("====================");
                    throw new RuntimeException("helloFlowStep1 is failed...");

//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep2() {
        return stepBuilderFactory.get("helloFlowStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Flow Spring Batch 2");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep3() {
        return stepBuilderFactory.get("helloFlowStep3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Flow Spring Batch 3");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
