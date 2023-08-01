package com.pnoni.batch.hello.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
public class Employee {
    @Id @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String job;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String language;
    @Enumerated(EnumType.STRING)
    private ShirtSize shirtSize;

    public Employee(Long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public enum Gender {
        Male, Female
    }

    public enum ShirtSize {
        XS, S, M, L, XL
    }
}
