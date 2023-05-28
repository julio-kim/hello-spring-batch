package com.pnoni.batch.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Date;

//@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jobLauncher.run(
                job,
                new JobParametersBuilder()
                        .addString("name", "Julio")
                        .addLong("seq", 2L)
                        .addDate("date", new Date())
                        .addDouble("score", 90.5)
                        .toJobParameters()
        );
    }
}
