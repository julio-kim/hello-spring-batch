package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
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
                .start(helloFlow1())
                .next(helloFlowStep3())
                .next(helloFlow2())
                .next(helloFlowStep6())
                .end()
                .build();
    }
    
    @Bean
    public Flow helloFlow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("helloFlow1");
        flowBuilder.start(helloFlowStep1())
                .next(helloFlowStep2())
                .end();
        return flowBuilder.build();
    }

    @Bean
    public Flow helloFlow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("helloFlow2");
        flowBuilder.start(helloFlowStep4())
                .next(helloFlowStep5())
                .end();
        return flowBuilder.build();
    }

    @Bean
    public Step helloFlowStep1() {
        return stepBuilderFactory.get("helloFlowStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 1");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep2() {
        return stepBuilderFactory.get("helloFlowStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 2");
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
                    log.info("Hello Spring Batch 3");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep4() {
        return stepBuilderFactory.get("helloFlowStep4")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 4");
                    log.info("====================");
                    throw new RuntimeException("helloFlowStep4 is failed");

//                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep5() {
        return stepBuilderFactory.get("helloFlowStep5")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 5");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloFlowStep6() {
        return stepBuilderFactory.get("helloFlowStep6")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 6");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
