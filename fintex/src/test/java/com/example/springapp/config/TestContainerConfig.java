package com.example.springapp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@Configuration
@Testcontainers
public class TestContainerConfig {

    @Bean
    public DriverManagerDataSource dataSourceTest(@Qualifier("genericContainer") GenericContainer<?> h2) {
        h2.start();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost:" + h2.getMappedPort(1521) + "/test");
        return dataSource;
    }

    @Bean
    public GenericContainer<?> genericContainer() {
        GenericContainer<?> h2 = new GenericContainer<>(DockerImageName.parse("oscarfonts/h2"))
                .withExposedPorts(1521, 81)
                .withEnv("H2_OPTIONS", "-ifNotExists")
                .waitingFor(Wait.defaultWaitStrategy());
        return h2;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSourceTest") DriverManagerDataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.springapp");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }

    @Bean
    public JdbcTemplate jdbcTemplateTest(@Qualifier("dataSourceTest") DriverManagerDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}


