package com.LucasRibasCardoso.api_rest_with_spring_boot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("API RESTFULL completa com Spring Boot")
                .version("1")
                .termsOfService("https://github.com/LucasRibasCardoso")
                .description(
                    """
                      API RESTFULL desenvolvida para estudo e aprendizado de Spring Boot,
                      Spring Data JPA, Spring Security, Swagger, H2 Database, entre outras
                      tecnologias e boas práticas de desenvolvimento.
                      """)
                .license(
                    new License().name("Apache 2.0").url("https://github.com/LucasRibasCardoso")));
  }
}
