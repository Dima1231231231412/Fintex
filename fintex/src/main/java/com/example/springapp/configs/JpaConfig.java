package com.example.springapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.springapp.database.JPA"})
public class JpaConfig {
}
