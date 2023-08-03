package com.pnoni.batch.hello.job;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

public class ShirtSizeClassifier implements Classifier<Employee, ItemProcessor<?, ? extends Employee>> {

    private final ItemProcessor<Employee, Employee> maleEmployeeProcessor;
    private final ItemProcessor<Employee, Employee> femaleEmployeeProcessor;

    public ShirtSizeClassifier(
            ItemProcessor<Employee, Employee> maleEmployeeProcessor,
            ItemProcessor<Employee, Employee> femaleEmployeeProcessor
    ) {
        this.maleEmployeeProcessor = maleEmployeeProcessor;
        this.femaleEmployeeProcessor = femaleEmployeeProcessor;
    }

    @Override
    public ItemProcessor<Employee, Employee> classify(Employee employee) {
        return (employee.getGender() == Employee.Gender.Male) ?
                maleEmployeeProcessor : femaleEmployeeProcessor;
    }
}
