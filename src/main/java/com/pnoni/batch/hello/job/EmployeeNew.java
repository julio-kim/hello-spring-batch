package com.pnoni.batch.hello.job;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter @Setter @ToString
public class EmployeeNew {
    @Id @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String job;
    @Enumerated(EnumType.STRING)
    private Employee.Gender gender;
    private String language;
    @Enumerated(EnumType.STRING)
    private Employee.ShirtSize shirtSize;

    public EmployeeNew(Employee employee) {
//        this.id = employee.getId();
        this.firstname = employee.getFirstname();
        this.lastname = employee.getLastname();
        this.email = employee.getEmail();
        this.job = employee.getJob();
        this.gender = employee.getGender();
        this.language = employee.getLanguage();
        this.shirtSize = employee.getShirtSize();
    }
}
