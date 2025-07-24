package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mapper.BookMapper;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Book;
import com.LucasRibasCardoso.api_rest_with_spring_boot.repository.BookRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @InjectMocks private BookService service;

  @Mock private BookRepository repository;

  @Mock private BookMapper mapper;

  private Book bookEntity;
  private BookResponseDto bookResponseDto;
  private BookCreateDto bookCreateDto;

  @BeforeEach
  void setUp() {
    bookEntity =
        new Book(
            1L,
            "Robert C. Martin",
            LocalDate.of(2005, 8, 1),
            new BigDecimal("45.90"),
            "Clean Code");
    bookResponseDto =
        new BookResponseDto(
            1L,
            "Clean Code",
            "Robert C. Martin",
            LocalDate.of(2005, 8, 1),
            new BigDecimal("45.90"));
    bookCreateDto =
        new BookCreateDto(
            "Clean Code", "Robert C. Martin", LocalDate.of(2005, 8, 1), new BigDecimal("45.90"));
  }

  @Test
  void findById() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.of(bookEntity));
    when(mapper.toDto(bookEntity)).thenReturn(bookResponseDto);

    // Act
    BookResponseDto result = service.findById(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("delete")));

    assertEquals(bookEntity.getTitle(), result.getTitle());
    assertEquals(bookEntity.getAuthor(), result.getAuthor());
    assertEquals(bookEntity.getLaunchDate(), result.getLaunchDate());
    assertEquals(bookEntity.getPrice(), result.getPrice());
  }

  @Test
  void findAll() {
    // Arrange
    when(repository.findAll()).thenReturn(List.of(bookEntity));
    when(mapper.toDto(bookEntity)).thenReturn(bookResponseDto);

    // Act
    List<BookResponseDto> result = service.findAll();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());

    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getFirst().getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("delete")));

    assertEquals(bookEntity.getTitle(), result.getFirst().getTitle());
    assertEquals(bookEntity.getAuthor(), result.getFirst().getAuthor());
    assertEquals(bookEntity.getLaunchDate(), result.getFirst().getLaunchDate());
    assertEquals(bookEntity.getPrice(), result.getFirst().getPrice());
  }

  @Test
  void save() {
    // Arrange
    when(mapper.toEntity(bookCreateDto)).thenReturn(bookEntity);
    when(mapper.toDto(bookEntity)).thenReturn(bookResponseDto);
    when(repository.save(bookEntity)).thenReturn(bookEntity);

    // Act
    BookResponseDto result = service.save(bookCreateDto);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("delete")));

    assertEquals(bookEntity.getTitle(), result.getTitle());
    assertEquals(bookEntity.getAuthor(), result.getAuthor());
    assertEquals(bookEntity.getLaunchDate(), result.getLaunchDate());
    assertEquals(bookEntity.getPrice(), result.getPrice());
  }

  @Test
  void saveWithNullDto() {
    Exception exception =
        assertThrows(
            RequiredObjectIsNullException.class,
            () -> {
              service.save(null);
            });

    String expectedMessage = "It is not allowed to persist a null object.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void saveBookAlreadyExists() {
    // Arrange
    when(repository.existsByAuthorIgnoreCase(bookCreateDto.author())).thenReturn(true);
    when(repository.existsByTitleIgnoreCase(bookCreateDto.title())).thenReturn(true);

    // Act & Assert
    Exception exception =
        assertThrows(
            BookAlreadyExistsException.class,
            () -> {
              service.save(bookCreateDto);
            });

    String expectedMessage =
        "This book already exists! Title: "
            + bookEntity.getTitle()
            + ", Author: "
            + bookEntity.getAuthor();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void update() {
    // Arrange
    BookUpdateDto bookUpdateDto = new BookUpdateDto("Código Limpo", "Uncle Bob", null, null);
    when(repository.findById(1L)).thenReturn(Optional.of(bookEntity));
    doNothing().when(mapper).updateEntityFromDto(bookUpdateDto, bookEntity);
    when(repository.save(bookEntity)).thenReturn(bookEntity);
    when(mapper.toDto(bookEntity)).thenReturn(bookResponseDto);

    // Act
    BookResponseDto result = service.update(1L, bookUpdateDto);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());

    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("self")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("GET")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("create")
                        && link.getHref().endsWith("api/v1/books")
                        && link.getType().equals("POST")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("update")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("PATCH")));
    assertNotNull(
        result.getLinks().stream()
            .anyMatch(
                link ->
                    link.getRel().value().equals("delete")
                        && link.getHref().endsWith("api/v1/books/1")
                        && link.getType().equals("delete")));

    assertEquals(bookEntity.getTitle(), result.getTitle());
    assertEquals(bookEntity.getAuthor(), result.getAuthor());
    assertEquals(bookEntity.getLaunchDate(), result.getLaunchDate());
    assertEquals(bookEntity.getPrice(), result.getPrice());
  }

  @Test
  void updateWithNullDto() {
    Exception exception =
        assertThrows(
            RequiredObjectIsNullException.class,
            () -> {
              service.update(1L, null);
            });

    String expectedMessage = "It is not allowed to persist a null object.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void updateBookNotFound() {
    // Arrange
    BookUpdateDto bookUpdateDto = new BookUpdateDto("Código Limpo", "Uncle Bob", null, null);
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            BookNotFoundException.class,
            () -> {
              service.update(1L, bookUpdateDto);
            });

    String expectedMessage = "Book not found with id: " + bookEntity.getId();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void delete() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.of(bookEntity));

    // Act
    service.delete(1L);

    // Assert
    verify(repository, times(1)).delete(bookEntity);
    verify(repository, times(1)).findById(1L);
  }

  @Test
  void deleteBookNotFound() {
    // Arrange
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    Exception exception =
        assertThrows(
            BookNotFoundException.class,
            () -> {
              service.delete(1L);
            });

    String expectedMessage = "Book not found with id: " + bookEntity.getId();
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
}
