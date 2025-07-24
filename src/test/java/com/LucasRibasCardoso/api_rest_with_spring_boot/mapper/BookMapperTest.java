package com.LucasRibasCardoso.api_rest_with_spring_boot.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookMapperTest {

  private BookMapper mapper;
  private Book sampleBook;
  private BookCreateDto createDto;
  private BookUpdateDto updateDto;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(BookMapper.class);
    sampleBook =
        new Book(
            1L,
            "Robert C. Martin",
            LocalDate.of(2008, 8, 1),
            new BigDecimal("45.90"),
            "Clean Code");
    createDto =
        new BookCreateDto(
            "Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1), new BigDecimal("45.90"));
  }

  @Test
  void shouldMapEntityToDto() {
    BookResponseDto dto = mapper.toDto(sampleBook);
    assertNotNull(dto);
    assertEquals(sampleBook.getId(), dto.getId());
    assertEquals(sampleBook.getTitle(), dto.getTitle());
    assertEquals(sampleBook.getAuthor(), dto.getAuthor());
    assertEquals(sampleBook.getLaunchDate(), dto.getLaunchDate());
  }

  @Test
  void shouldMapCreateDtoToEntity() {
    Book entity = mapper.toEntity(createDto);
    assertNotNull(entity);
    assertNull(entity.getId(), "ID must be null when mapping from create DTO");
    assertNull(entity.getId());
    assertEquals(sampleBook.getTitle(), entity.getTitle());
    assertEquals(sampleBook.getAuthor(), entity.getAuthor());
    assertEquals(sampleBook.getLaunchDate(), entity.getLaunchDate());
  }

  @Test
  void shouldUpdateEntityFromDto_AllFields() {
    updateDto = new BookUpdateDto(
            "Código Limpo",
            "Uncle Bob",
            LocalDate.of(2008, 9, 1),
            new BigDecimal("50.00")
        );

    mapper.updateEntityFromDto(updateDto, sampleBook);

    assertEquals("Código Limpo", sampleBook.getTitle());
    assertEquals("Uncle Bob", sampleBook.getAuthor());
    assertEquals(LocalDate.of(2008, 9, 1), sampleBook.getLaunchDate());
    assertEquals(new BigDecimal("50.00"), sampleBook.getPrice());
  }

  @Test
  void shouldUpdateEntityFromDto_PartialFields() {
    updateDto = new BookUpdateDto("Código Limpo", null, null, null);
    String originalAuthor = sampleBook.getAuthor();
    LocalDate originalLaunchDate = sampleBook.getLaunchDate();
    BigDecimal originalPrice = sampleBook.getPrice();

    mapper.updateEntityFromDto(updateDto, sampleBook);

    assertEquals("Código Limpo", sampleBook.getTitle());
    assertEquals(originalAuthor, sampleBook.getAuthor());
    assertEquals(originalLaunchDate, sampleBook.getLaunchDate());
    assertEquals(originalPrice, sampleBook.getPrice());
  }

  @Test
  void shouldNotModifyEntityWhenAllDtoFieldsNull() {
    updateDto = new BookUpdateDto(null, null, null, null);
    String originalTitle = sampleBook.getTitle();
    String originalAuthor = sampleBook.getAuthor();
    LocalDate originalLaunchDate = sampleBook.getLaunchDate();
    BigDecimal originalPrice = sampleBook.getPrice();

    mapper.updateEntityFromDto(updateDto, sampleBook);

    assertEquals(originalTitle, sampleBook.getTitle());
    assertEquals(originalAuthor, sampleBook.getAuthor());
    assertEquals(originalLaunchDate, sampleBook.getLaunchDate());
    assertEquals(originalPrice, sampleBook.getPrice());
  }
}
