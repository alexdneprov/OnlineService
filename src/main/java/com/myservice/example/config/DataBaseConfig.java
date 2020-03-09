package com.myservice.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig  {

//    @Bean
//    public DataSource getDataSource () {
//        DriverManagerDataSource source = new DriverManagerDataSource();
//        source.setUrl("jdbc:mysql://localhost/onlineservice?useUnicode=true&serverTimezone=UTC");
//        source.setUsername("root");
//        source.setPassword("qwerty123+");
//        return source;
//    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
