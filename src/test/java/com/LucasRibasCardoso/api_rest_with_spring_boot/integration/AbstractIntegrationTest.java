package com.LucasRibasCardoso.api_rest_with_spring_boot.integration;

import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgreSQLContainer =
        new PostgreSQLContainer<>("postgres:16-alpine");

    private void startContainers() {
      Startables.deepStart(Stream.of(postgreSQLContainer)).join();
    }

    private Map<String, Object> createConnectionConfiguration() {
      return Map.of(
          "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
          "spring.datasource.username", postgreSQLContainer.getUsername(),
          "spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      startContainers();
      ConfigurableEnvironment environment = applicationContext.getEnvironment();
      MapPropertySource propertySource =
          new MapPropertySource("testcontainers", createConnectionConfiguration());
      environment.getPropertySources().addFirst(propertySource);
    }
  }
}
