package com.pnoni.batch.hello.job;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EmployeeFieldSetMapper implements FieldSetMapper<Employee> {
    @Override
    public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
        Employee employee = new Employee();
        employee.setId(fieldSet.readInt(0));
        employee.setFirstname(fieldSet.readString(1));
        employee.setLastname(fieldSet.readString(2));
        employee.setEmail(fieldSet.readString(3));
        employee.setJob(fieldSet.readString(4));
        return employee;
    }
}
