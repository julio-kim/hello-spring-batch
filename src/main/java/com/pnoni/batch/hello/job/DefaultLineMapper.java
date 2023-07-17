package com.pnoni.batch.hello.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

@Slf4j
@Setter
public class DefaultLineMapper<T> implements LineMapper<T> {
    private LineTokenizer lineTokenizer;
    private FieldSetMapper<T> fieldSetMapper;

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        log.info("lineNumber: {}, line: {}", lineNumber, line);
        return fieldSetMapper.mapFieldSet(lineTokenizer.tokenize(line));
    }
}
