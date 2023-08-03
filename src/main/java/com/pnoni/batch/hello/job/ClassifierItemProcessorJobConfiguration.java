package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemProcessorBuilder;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ClassifierItemProcessorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job classifierProcessor() throws Exception {
        return jobBuilderFactory.get("classifierProcessorJob")
                .start(classifierProcessorStep())
                .build();
    }

    @Bean
    public Step classifierProcessorStep() throws Exception {
        return stepBuilderFactory.get("classifierProcessorStep")
                .<Employee, Employee>chunk(10)
                .reader(classifierProcessorReader())
                .processor(compositeItemProcessor())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> classifierProcessorReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Employee>()
                .name("classifierProcessorReader")
                .pageSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Employee.class))
                .queryProvider(classifierProcessorQueryProvider())
                .build();
    }

    public PagingQueryProvider classifierProcessorQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("id, firstname, lastname, email, job, gender, language, shirt_size");
        provider.setFromClause("employee");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        provider.setSortKeys(sortKeys);

        return provider.getObject();
    }

    @Bean
    public ItemProcessor<Employee, Employee> compositeItemProcessor() {
        return new CompositeItemProcessorBuilder<Employee, Employee>()
                .delegates(
                        classifierItemProcessor(),
                        transFirstnameProcessor()
                )
                .build();
    }

    private ItemProcessor<Employee, Employee> classifierItemProcessor() {
        return new ClassifierCompositeItemProcessorBuilder<Employee, Employee>()
                .classifier(new ShirtSizeClassifier(
                        maleItemProcessor(),
                        femaleItemProcessor()
                ))
                .build();
    }

    private ItemProcessor<Employee, Employee> maleItemProcessor() {
        return employee -> Arrays.asList(
                Employee.ShirtSize.M,
                Employee.ShirtSize.L,
                Employee.ShirtSize.XL
        ).contains(employee.getShirtSize()) ? employee : null;
    }

    private ItemProcessor<Employee, Employee> femaleItemProcessor() {
        return employee -> Arrays.asList(
                Employee.ShirtSize.XS,
                Employee.ShirtSize.S,
                Employee.ShirtSize.M
        ).contains(employee.getShirtSize()) ? employee : null;
    }

    private ItemProcessor<Employee, Employee> transFirstnameProcessor() {
        return employee -> {
            employee.setFirstname("[New]" + employee.getFirstname());
            return employee;
        };
    }
}
