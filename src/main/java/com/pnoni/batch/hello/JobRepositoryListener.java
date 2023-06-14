package com.pnoni.batch.hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {
    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String requestDate = Optional.ofNullable(jobParameters.getString("requestDate")).orElse("20230608");
        String yesterday = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
                .minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(
                jobExecution.getJobInstance().getJobName(),
                new JobParametersBuilder()
                        .addString("requestDate", yesterday)
                        .toJobParameters()
        );

        log.info("Yesterday was {}", yesterday);
        Optional.ofNullable(lastJobExecution).ifPresent(jobExec -> {
            jobExec.getStepExecutions().forEach(stepExec -> {
                log.info("StepName: {}", stepExec.getStepName());
                log.info("Status: {}", stepExec.getStatus());
                log.info("ExitStatus: {}", stepExec.getExitStatus());
            });
        });
    }
}
