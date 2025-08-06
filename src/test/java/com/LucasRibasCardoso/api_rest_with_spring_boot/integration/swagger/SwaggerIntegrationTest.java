package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

  /**
   * Teste reponsável por verificar se a página do Swagger UI está sendo renderizada corretamente.
   */
  @Test
  void shouldDisplaySwaggerUIPage() {
    var content =
        given()
            .port(TestsConfigs.SERVER_PORT)
            .basePath("/swagger-ui/index.html")
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
