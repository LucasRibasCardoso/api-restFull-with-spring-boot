package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller.withJson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper.WrapperPersonResponseDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class PersonControllerJsonTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;
  private static PersonResponseDto personResponse;
  private static TokenResponseDto tokenDto;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    personResponse = new PersonResponseDto();
  }

  @Test
  @Order(1)
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

    specification =
        new RequestSpecBuilder()
            .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_VALID)
            .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.accessToken())
            .setBasePath("/api/v1/person")
            .setPort(TestsConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
  }

  @Test
  @Order(2)
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
  @Order(3)
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
  @Order(4)
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
  @Order(5)
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
  @Order(6)
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
  @Order(7)
  void delete() {
    given(specification)
        .pathParam("id", personResponse.getId())
        .when()
        .delete("/{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(8)
  void findAll() throws IOException {
    String content =
        given(specification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 3)
            .queryParam("size", 5)
            .queryParam("direction", "asc")
            .queryParam("sortBy", "firstName")
            .when()
            .get()
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();

    WrapperPersonResponseDto wrapper =
        objectMapper.readValue(content, WrapperPersonResponseDto.class);

    assertNotNull(wrapper.getEmbedded());
    assertEquals(5, wrapper.getEmbedded().getPeople().size());

    PersonResponseDto personOne = wrapper.getEmbedded().getPeople().get(0);
    assertNotNull(personOne.getId());
    assertTrue(personOne.getId() > 0);
    assertEquals("Débora", personOne.getFirstName());
    assertEquals("Lopes", personOne.getLastName());
    assertEquals("909.980.991-89", personOne.getCpf());
    assertEquals(Gender.F, personOne.getGender());
    assertTrue(personOne.getEnabled());

    PersonResponseDto personTwo = wrapper.getEmbedded().getPeople().get(1);
    assertNotNull(personTwo.getId());
    assertTrue(personTwo.getId() > 0);
    assertEquals("Eduardo", personTwo.getFirstName());
    assertEquals("Nascimento", personTwo.getLastName());
    assertEquals("777.888.999-07", personTwo.getCpf());
    assertEquals(Gender.M, personTwo.getGender());
    assertTrue(personTwo.getEnabled());

    PersonResponseDto personThree = wrapper.getEmbedded().getPeople().get(2);
    assertNotNull(personThree.getId());
    assertTrue(personThree.getId() > 0);
    assertEquals("Elaine", personThree.getFirstName());
    assertEquals("Peixoto", personThree.getLastName());
    assertEquals("505.546.557-45", personThree.getCpf());
    assertEquals(Gender.F, personThree.getGender());
    assertTrue(personThree.getEnabled());

    PersonResponseDto personFour = wrapper.getEmbedded().getPeople().get(3);
    assertNotNull(personFour.getId());
    assertTrue(personFour.getId() > 0);
    assertEquals("Felipe", personFour.getFirstName());
    assertEquals("Barros", personFour.getLastName());
    assertEquals("111.222.333-41", personFour.getCpf());
    assertEquals(Gender.M, personFour.getGender());
    assertTrue(personFour.getEnabled());

    PersonResponseDto personFive = wrapper.getEmbedded().getPeople().get(4);
    assertNotNull(personFive.getId());
    assertTrue(personFive.getId() > 0);
    assertEquals("Fernanda", personFive.getFirstName());
    assertEquals("Almeida", personFive.getLastName());
    assertEquals("012.345.678-90", personFive.getCpf());
    assertEquals(Gender.F, personFive.getGender());
    assertTrue(personFive.getEnabled());
  }

  @Test
  @Order(9)
  void findByName() throws IOException {
    String content =
        given(specification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("firstName", "Débora")
            .when()
            .get("/findByName/{firstName}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();

    WrapperPersonResponseDto wrapper =
        objectMapper.readValue(content, WrapperPersonResponseDto.class);

    assertNotNull(wrapper.getEmbedded());
    assertFalse(wrapper.getEmbedded().getPeople().isEmpty());

    PersonResponseDto person = wrapper.getEmbedded().getPeople().get(0);
    assertNotNull(person.getId());
    assertTrue(person.getId() > 0);
    assertEquals("Débora", person.getFirstName());
    assertEquals("Lopes", person.getLastName());
    assertEquals("909.980.991-89", person.getCpf());
    assertEquals(Gender.F, person.getGender());
    assertTrue(person.getEnabled());
  }
}
