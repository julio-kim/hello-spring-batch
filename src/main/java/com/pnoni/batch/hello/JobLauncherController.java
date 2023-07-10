package com.pnoni.batch.hello;

import com.pnoni.batch.hello.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobLauncherController {
    private final Job job;
    private final BasicBatchConfigurer basicBatchConfigurer;

    @PostMapping("/batch")
    public String launch(@RequestBody User user) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", user.getName())
                .toJobParameters();

        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return "Batch Completed, Job Status: " + jobExecution.getStatus();
    }
}
