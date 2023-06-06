package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

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
                .next(helloStep3())
                .build();
    }

    @Bean
    public Step helloStep1() {
        return stepBuilderFactory.get("helloStep1")
                .tasklet((stepContribution, chunkContext) -> {
                    JobParameters params = stepContribution
                            .getStepExecution()
                            .getJobParameters();
                    log.info("====================");
                    log.info("Hello Spring Batch 1");
                    log.info("--------------------");
                    log.info("name: {}", params.getString("name"));
                    log.info("seq: {}", params.getLong("seq"));
                    log.info("date: {}", params.getDate("date"));
                    log.info("score: {}", params.getDouble("score"));
                    log.info("====================");

                    ExecutionContext jobContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
                    ExecutionContext stepContext = stepContribution.getStepExecution().getExecutionContext();

                    String jobName = stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
                    String stepName = stepContribution.getStepExecution().getStepName();

                    jobContext.putString("jobName", jobName);
                    stepContext.putString("stepName", stepName);

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
                    log.info("--------------------");

                    ExecutionContext jobContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
                    ExecutionContext stepContext = stepContribution.getStepExecution().getExecutionContext();

                    log.info("jobName: {}", jobContext.get("jobName"));
                    log.info("stepName: {}", stepContext.get("stepName"));

                    Optional.ofNullable(jobContext.get("check")).ifPresentOrElse(
                            check -> log.info("Check: {}", check),
                            () -> {
                                jobContext.putString("check", "YES");
                                throw new RuntimeException("Check is not exists...");
                            }
                    );
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step helloStep3() {
        return stepBuilderFactory.get("helloStep3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Hello Spring Batch 3");
                    log.info("--------------------");

                    ExecutionContext jobContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
                    ExecutionContext stepContext = stepContribution.getStepExecution().getExecutionContext();

                    log.info("jobName: {}", jobContext.get("jobName"));
                    log.info("stepName: {}", stepContext.get("stepName"));
                    log.info("check: {}", jobContext.get("check"));

                    log.info("====================");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
