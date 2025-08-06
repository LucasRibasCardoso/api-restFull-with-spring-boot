package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.controller.withJson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.AbstractIntegrationTest;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config.TestsConfigs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.integration.dto.wrapper.WrapperBookResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class BookControllerJsonTest extends AbstractIntegrationTest {

  private static RequestSpecification specification;
  private static ObjectMapper objectMapper;
  private static BookResponseDto bookResponse;
  private static TokenResponseDto tokenDto;

  @BeforeAll
  static void setUp() {
    objectMapper = new ObjectMapper();
    JavaTimeModule jtm = new JavaTimeModule();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    jtm.addDeserializer(LocalDate.class, new LocalDateDeserializer(fmt));
    jtm.addSerializer(LocalDate.class, new LocalDateSerializer(fmt));
    objectMapper.registerModule(jtm);
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    bookResponse = new BookResponseDto();
  }

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

    specification =
        new RequestSpecBuilder()
            .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_VALID)
            .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.accessToken())
            .setBasePath("/api/v1/books")
            .setPort(TestsConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
  }

  @Test
  @Order(2)
  void create() throws IOException {
    BookCreateDto bookCreateDto =
        new BookCreateDto(
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            LocalDate.of(1925, 4, 10),
            BigDecimal.valueOf(95.00));

    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(bookCreateDto)
            .when()
            .post()
            .then()
            .statusCode(201)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();

    bookResponse = objectMapper.readValue(content, BookResponseDto.class);

    assertNotNull(bookResponse.getId());
    assertTrue(bookResponse.getId() > 0);
    assertEquals("The Great Gatsby", bookResponse.getTitle());
    assertEquals("F. Scott Fitzgerald", bookResponse.getAuthor());
    assertEquals(LocalDate.of(1925, 4, 10), bookResponse.getLaunchDate());
    assertEquals(0, BigDecimal.valueOf(95.00).compareTo(bookResponse.getPrice()));
  }

  @Test
  @Order(3)
  void update() throws IOException {
    BookUpdateDto bookUpdateDto = new BookUpdateDto("The Great Gatsby - Updated", null, null, null);

    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", bookResponse.getId())
            .body(bookUpdateDto)
            .when()
            .patch("{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    BookResponseDto updatedBookResponseDto = objectMapper.readValue(content, BookResponseDto.class);
    bookResponse = updatedBookResponseDto;

    assertEquals("The Great Gatsby - Updated", updatedBookResponseDto.getTitle());
  }

  @Test
  @Order(4)
  void findById() throws IOException {
    String content =
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", bookResponse.getId())
            .when()
            .get("{id}")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();
    BookResponseDto foundBookResponseDto = objectMapper.readValue(content, BookResponseDto.class);
    assertEquals(bookResponse.getId(), foundBookResponseDto.getId());
  }

  @Test
  @Order(5)
  void delete() {
    given(specification)
        .pathParam("id", bookResponse.getId())
        .when()
        .delete("{id}")
        .then()
        .statusCode(204);
  }

  @Test
  @Order(6)
  void findAll() throws JsonProcessingException {
    String content =
        given(specification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 1)
            .queryParam("size", 3)
            .queryParam("direction", "asc")
            .queryParam("sortBy", "author")
            .when()
            .get()
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .extract()
            .body()
            .asString();

    WrapperBookResponseDto wrapper = objectMapper.readValue(content, WrapperBookResponseDto.class);

    BookResponseDto bookOne = wrapper.getEmbedded().getBooks().get(0);
    assertNotNull(bookOne.getId());
    assertTrue(bookOne.getId() > 0);
    assertEquals("Domain Driven Design", bookOne.getTitle());
    assertEquals("Eric Evans", bookOne.getAuthor());
    assertEquals(LocalDate.of(2017, 11, 7), bookOne.getLaunchDate());
    assertEquals(0, BigDecimal.valueOf(92.00).compareTo(bookOne.getPrice()));

    BookResponseDto bookTwo = wrapper.getEmbedded().getBooks().get(1);
    assertNotNull(bookTwo.getId());
    assertTrue(bookTwo.getId() > 0);
    assertEquals("Head First Design Patterns", bookTwo.getTitle());
    assertEquals("Eric Freeman, Elisabeth Freeman, Kathy Sierra, Bert Bates", bookTwo.getAuthor());
    assertEquals(LocalDate.of(2017, 11, 7), bookTwo.getLaunchDate());
    assertEquals(0, BigDecimal.valueOf(110.00).compareTo(bookTwo.getPrice()));

    BookResponseDto bookThree = wrapper.getEmbedded().getBooks().get(2);
    assertNotNull(bookThree.getId());
    assertTrue(bookThree.getId() > 0);
    assertEquals("Os 11 segredos de líderes de TI altamente influentes", bookThree.getTitle());
    assertEquals("Marc J. Schiller", bookThree.getAuthor());
    assertEquals(LocalDate.of(2017, 11, 7), bookThree.getLaunchDate());
    assertEquals(0, BigDecimal.valueOf(45.00).compareTo(bookThree.getPrice()));
  }
}
