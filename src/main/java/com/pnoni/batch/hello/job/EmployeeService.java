package com.pnoni.batch.hello.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmployeeService {
    private static Long count = 0L;
    public Employee getEmployee() {
        return (count < 100) ? new Employee(count++, "Julio", "Kim") : null;
    }

    public void setEmployee(EmployeeNew employee) {
        log.info("EmployeeNew: {}", employee);
    }
}
