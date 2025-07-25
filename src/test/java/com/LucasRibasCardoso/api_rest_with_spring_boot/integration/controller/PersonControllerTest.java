package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"server.port=8888"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;

  private static PersonResponseDto personResponse;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Test
  @Order(1)
  void save() throws IOException {
    PersonCreateDto personCreate = new PersonCreateDto("John", "Doe", "057.657.780-46", Gender.M);

    specification =
        new RequestSpecBuilder()
            .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_VALID)
            .setBasePath("/api/v1/person")
            .setPort(TestsConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

    var content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(personCreate)
            .when()
            .post()
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString();

    personResponse = objectMapper.readValue(content, PersonResponseDto.class);

    assertNotNull(personResponse.getId());
    assertNotNull(personResponse.getFirstName());
    assertNotNull(personResponse.getLastName());
    assertNotNull(personResponse.getCpf());
    assertNotNull(personResponse.getGender());

    assertTrue(personResponse.getId() > 0);

    assertEquals(personCreate.firstName(), personResponse.getFirstName());
    assertEquals(personCreate.lastName(), personResponse.getLastName());
    assertEquals(personCreate.cpf(), personResponse.getCpf());
    assertEquals(personCreate.gender(), personResponse.getGender());
  }

  @Test
  void findById() {}

  @Test
  void findAll() {}

  @Test
  void update() {}

  @Test
  void delete() {}
}
