package com.pnoni.batch.hello.job;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Employee {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String job;
    private Gender gender;
    private String language;
    private ShirtSize shirtSize;

    public enum Gender {
        Male, Female
    }

    public enum ShirtSize {
        XS, S, M, L, XL
    }
}
