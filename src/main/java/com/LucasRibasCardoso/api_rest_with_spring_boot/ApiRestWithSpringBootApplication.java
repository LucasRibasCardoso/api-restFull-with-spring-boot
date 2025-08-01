package com.LucasRibasCardoso.api_rest_with_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
public class ApiRestWithSpringBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiRestWithSpringBootApplication.class, args);
  }
}
