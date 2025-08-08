package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller.withJson;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpResponseDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class AuthControllerJsonTest extends AbstractIntegrationTest {

  private static TokenResponseDto tokenDto;

  @Test
  @Order(1)
  void signUp() {
    SignUpRequestDto signUpRequestDto =
        new SignUpRequestDto(
            "username1", "Username Teste", "username@gmail.com", "(11) 99999-9999", "1384", "1384");

    SignUpResponseDto signUpResponseDto =
        given()
            .basePath("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .port(TestsConfigs.SERVER_PORT)
            .body(signUpRequestDto)
            .when()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .as(SignUpResponseDto.class);

    assertNotNull(signUpResponseDto);
    assertEquals("username1", signUpResponseDto.username());
    assertEquals("Username Teste", signUpResponseDto.fullName());
    assertEquals("username@gmail.com", signUpResponseDto.email());
    assertEquals("(11) 99999-9999", signUpResponseDto.phone());
  }

  @Test
  @Order(2)
  void signIn() {
    LoginRequestDto loginRequestDto = new LoginRequestDto("username1", "1384");

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
  @Order(3)
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
