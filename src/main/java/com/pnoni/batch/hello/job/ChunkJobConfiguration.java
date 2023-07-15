package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChunkJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkJob() {
        return jobBuilderFactory.get("chunkJob")
                .start(chunkStep1())
                .next(chunkStep2())
                .build();
    }

    @Bean
    public Step chunkStep1() {
        return stepBuilderFactory.get("chunkStep1")
                .<String, String>chunk(3)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
                .processor((ItemProcessor<String, String>) item -> {
                    Thread.sleep(300);
                    log.info("item = {}", item);
                    return "my_" + item;
                })
                .writer(items -> {
                    Thread.sleep(300);
                    log.info("items = {}", items);
                })
                .build();
    }

    @Bean
    public Step chunkStep2() {
        return stepBuilderFactory.get("chunkStep2")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("====================");
                    log.info("Chunk Spring Batch 2");
                    log.info("====================");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
