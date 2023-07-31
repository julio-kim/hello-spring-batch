package com.pnoni.batch.hello.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MybatisConfig {
    private final DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        factoryBean.setMapperLocations(new ClassPathResource("mybatis/hello/employee-mapper.xml"));
        factoryBean.setTypeAliasesPackage("com.pnoni.batch.hello");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

}
