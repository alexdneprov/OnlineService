package com.myservice.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan
public class DataBaseConfig  {

    @Bean
    public DataSource getDataSource () {
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setUrl("jdbc:mysql://localhost/onlineservice?useUnicode=true&serverTimezone=UTC");
        source.setUsername("root");
        source.setPassword("qwerty123+");
        return source;
    }
}
