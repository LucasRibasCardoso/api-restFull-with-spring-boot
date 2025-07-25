package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

  /**
   * Teste reponsável por verificar se a página do Swagger UI está sendo renderizada corretamente.
   */
  @Test
  void shouldDisplaySwaggerUIPage() {
    var content =
        given()
            .basePath("/swagger-ui/index.html")
            .port(TestsConfigs.SERVER_PORT)
            .when()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    assertTrue(content.contains("Swagger UI"));
  }
}
