package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller.withJson;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class AuthControllerJsonTest extends AbstractIntegrationTest {

  private static TokenResponseDto tokenDto;

  @Test
  @Order(1)
  void signIn() {
    LoginRequestDto loginRequestDto = new LoginRequestDto("Username1", "1384");

    tokenDto =
        given()
            .basePath("/auth/signin")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .port(TestsConfigs.SERVER_PORT)
            .body(loginRequestDto)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TokenResponseDto.class);

    assertNotNull(tokenDto);
    assertNotNull(tokenDto.accessToken());
    assertNotNull(tokenDto.refreshToken());
  }

  @Test
  @Order(2)
  void refreshToken() {

    tokenDto =
        given()
            .basePath("/auth/refresh-token")
            .port(TestsConfigs.SERVER_PORT)
            .pathParam("username", tokenDto.username())
            .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.refreshToken())
            .when()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(TokenResponseDto.class);

    assertNotNull(tokenDto);
    assertNotNull(tokenDto.accessToken());
    assertNotNull(tokenDto.refreshToken());
  }
}
