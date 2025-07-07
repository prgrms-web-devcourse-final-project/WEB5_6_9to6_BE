package com.grepp.studium.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.grepp.studium")
@EnableJpaRepositories("com.grepp.studium")
@EnableTransactionManagement
public class DomainConfig {
}
