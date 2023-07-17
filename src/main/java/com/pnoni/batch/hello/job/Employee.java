package com.pnoni.batch.hello.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Employee {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String job;
}
