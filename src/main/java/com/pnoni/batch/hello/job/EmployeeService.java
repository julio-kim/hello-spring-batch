package com.pnoni.batch.hello.job;

public class EmployeeService {
    private static Long count = 0L;
    public Employee getEmployee() {
        return (count < 100) ? new Employee(count++, "Julio", "Kim") : null;
    }
}
