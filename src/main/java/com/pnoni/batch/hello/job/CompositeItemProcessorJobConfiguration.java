package com.pnoni.batch.hello.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompositeItemProcessorJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job compositeProcessorJob() throws Exception {
        return jobBuilderFactory.get("compositeProcessorJob")
                .start(compositeProcessorStep())
                .build();
    }

    @Bean
    public Step compositeProcessorStep() throws Exception {
        return stepBuilderFactory.get("compositeProcessorStep")
                .<Employee, Employee>chunk(10)
                .reader(compositeProcessorReader())
                .processor(compositItemProcessor())
                .writer(employees -> employees.forEach(employee -> log.info("Employee: {}", employee)))
                .build();
    }

    @Bean
    public ItemReader<Employee> compositeProcessorReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Employee>()
                .name("compositeProcessorReader")
                .pageSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Employee.class))
                .queryProvider(compositeProcessorQueryProvider())
                .build();
    }

    public PagingQueryProvider compositeProcessorQueryProvider() throws Exception {
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
    public ItemProcessor<Employee, Employee> compositItemProcessor() {
        return new CompositeItemProcessorBuilder<Employee, Employee>()
                .delegates(
                        filterGenderProcessor(),
                        filterShirtSizeProcessor(),
                        transFirstnameProcessor()
                )
                .build();
    }

    private ItemProcessor<Employee, Employee> filterGenderProcessor() {
        return employee -> (employee.getGender() == Employee.Gender.Female) ? employee : null;
    }

    private ItemProcessor<Employee, Employee> filterShirtSizeProcessor() {
        return employee -> (employee.getShirtSize() == Employee.ShirtSize.S) ? employee : null;
    }

    private ItemProcessor<Employee, Employee> transFirstnameProcessor() {
        return employee -> {
            employee.setFirstname("[New]" + employee.getFirstname());
            return employee;
        };
    }
}
