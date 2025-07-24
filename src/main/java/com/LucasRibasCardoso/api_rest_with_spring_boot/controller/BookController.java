package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.BookControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Books", description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/v1/books")
public class BookController implements BookControllerDocs {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(
      value = "/{id}",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<BookResponseDto> findById(@PathVariable Long id) {
    BookResponseDto bookResponseDto = bookService.findById(id);
    return ResponseEntity.ok(bookResponseDto);
  }

  @GetMapping(
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<List<BookResponseDto>> findAll() {
    List<BookResponseDto> listOfBookResponse = bookService.findAll();
    return ResponseEntity.ok(listOfBookResponse);
  }

  @PostMapping(
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<BookResponseDto> save(@RequestBody @Valid BookCreateDto bookCreateDto) {
    BookResponseDto savedBookResponseDto = bookService.save(bookCreateDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedBookResponseDto.getId())
            .toUri();
    return ResponseEntity.created(location).body(savedBookResponseDto);
  }

  @PatchMapping(
      value = "/{id}",
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<BookResponseDto> update(
      @PathVariable Long id, @RequestBody @Valid BookUpdateDto bookUpdateDto) {
    BookResponseDto updatedBookResponseDto = bookService.update(id, bookUpdateDto);
    return ResponseEntity.ok(updatedBookResponseDto);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    bookService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
