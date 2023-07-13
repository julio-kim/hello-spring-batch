package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;
    private final Job helloJob;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .next(jobStep())
                .next(simpleStep2())
                .build();
    }

    @Bean Step jobStep() {
        return stepBuilderFactory.get("helloJobStep")
                .job(helloJob)
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().putLong("age", 20);
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return null;
                    }
                })
                .build();
    }

    private JobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"age"});
        return extractor;
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
