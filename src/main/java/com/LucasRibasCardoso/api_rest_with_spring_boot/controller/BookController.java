package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.BookControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
  public ResponseEntity<PagedModel<EntityModel<BookResponseDto>>> findAll(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "direction", defaultValue = "desc") String direction,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      PagedResourcesAssembler<BookResponseDto> assembler) {

    Sort.Direction sortDirection =
        "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    Page<BookResponseDto> bookPage = bookService.findAll(pageable);

    return ResponseEntity.ok(assembler.toModel(bookPage));
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
