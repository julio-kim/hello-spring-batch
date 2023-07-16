package com.pnoni.batch.hello.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;

import java.util.List;

@Slf4j
public class CustomItemStreamReader implements ItemStreamReader<String> {
    private final List<String> items;
    private int index;
    private boolean restart;

    public CustomItemStreamReader(List<String> items) {
        this.items = items;
        this.index = 0;
        this.restart = false;
    }
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String item = null;

        if (this.index < this.items.size()) {
            item = this.items.get(index);
            index++;
        }

        // 예외가 발생하는 상황을 만들기 위해 restart 사용
        if (this.index == 13 && !restart) {
            throw new RuntimeException("Restart is required");
        }

        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey("index")) {
            this.index = executionContext.getInt("index");
            this.restart = true;
        } else {
            this.index = 0;
            executionContext.put("index", index);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("CustomItemReader, update {}", index);
        executionContext.put("index", index);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("CustomItemStreamReader is closed");
    }
}
