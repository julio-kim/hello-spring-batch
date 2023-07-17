package com.pnoni.batch.hello.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BatchLog {
    private String occurTime;
    private String level;
    private String threadName;
    private String clazz;
    private String message;
}
