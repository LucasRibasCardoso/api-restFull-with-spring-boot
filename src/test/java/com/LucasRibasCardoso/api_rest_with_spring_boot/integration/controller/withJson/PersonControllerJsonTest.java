package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller.withJson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;
  private static PersonResponseDto personResponse;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    personResponse = new PersonResponseDto();
    specification =
        new RequestSpecBuilder()
            .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_VALID)
            .setBasePath("/api/v1/person")
            .setPort(TestsConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
  }

  @Test
  @Order(1)
  void createTest() throws IOException {
    PersonCreateDto personCreate = new PersonCreateDto("John", "Doe", "030.228.230-02", Gender.M);

    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(personCreate)
            .when()
            .post()
            .then()
            .statusCode(201)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    personResponse = objectMapper.readValue(content, PersonResponseDto.class);

    assertNotNull(personResponse.getId());
    assertTrue(personResponse.getId() > 0);
    assertEquals("John", personResponse.getFirstName());
    assertEquals("Doe", personResponse.getLastName());
    assertEquals(Gender.M, personResponse.getGender());
    assertTrue(personResponse.getEnabled());
  }

  @Test
  @Order(2)
  void updateTest() throws IOException {
    PersonUpdateDto update = new PersonUpdateDto("Jane", null, null, null);
    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", personResponse.getId())
            .body(update)
            .when()
            .patch("{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    PersonResponseDto updated = objectMapper.readValue(content, PersonResponseDto.class);
    personResponse = updated;
    assertEquals("Jane", updated.getFirstName());
  }

  @Test
  @Order(3)
  void findByIdTest() throws IOException {
    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", personResponse.getId())
            .when()
            .get("{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    PersonResponseDto found = objectMapper.readValue(content, PersonResponseDto.class);
    assertEquals(personResponse.getId(), found.getId());
  }

  @Test
  @Order(4)
  void disableTest() throws IOException {
    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", personResponse.getId())
            .when()
            .patch("disable/{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    PersonResponseDto disabled = objectMapper.readValue(content, PersonResponseDto.class);
    assertFalse(disabled.getEnabled());
  }

  @Test
  @Order(5)
  void enablePerson() throws IOException {
    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", personResponse.getId())
            .when()
            .patch("enable/{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    PersonResponseDto disabled = objectMapper.readValue(content, PersonResponseDto.class);
    assertTrue(disabled.getEnabled());
  }

  @Test
  @Order(6)
  void deleteTest() {
    given(specification)
        .pathParam("id", personResponse.getId())
        .when()
        .delete("/{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(7)
  void findAllTest() throws IOException {
    String content =
        given(specification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get()
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    List<PersonResponseDto> list =
        objectMapper.readValue(content, new TypeReference<List<PersonResponseDto>>() {});
    assertFalse(list.isEmpty());
  }
}
