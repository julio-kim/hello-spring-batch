package com.pnoni.batch.hello.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.util.List;

@Slf4j
public class CustomItemStreamWriter implements ItemStreamWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(item -> log.info("Item: {}", item));
    }
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("CustomItemStreamWriter, open");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("CustomItemStreamWriter, update");
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("CustomItemStreamWriter, close");
    }
}
