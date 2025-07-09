package com.grepp.spring;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class StudiumApplication {

    @Autowired
    DataSource dataSource;

    public static void main(final String[] args) {
        SpringApplication.run(StudiumApplication.class, args);
    }

    @PostConstruct
    public void printDbInfo() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Connected DB URL: " + conn.getMetaData().getURL());
            System.out.println("Connected DB User: " + conn.getMetaData().getUserName());
            System.out.println("Connected DB Name: " + conn.getCatalog());
        }
    }


}
