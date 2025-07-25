package com.LucasRibasCardoso.api_rest_with_spring_boot.docs;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.*;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface BookControllerDocs {

  @Operation(
      summary = "Find a Book",
      description = "Return a specific book by ID",
      tags = {"Books"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class))
            })
      })
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<BookResponseDto> findById(@PathVariable Long id);

  @Operation(
      summary = "Find all books",
      description = "Returns a list of all books",
      tags = {"Books"},
      responses = {
        @ApiResponse(
            description = "Returned list of books",
            responseCode = "200",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = BookResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = BookResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = BookResponseDto.class)))
            }),
      })
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<List<BookResponseDto>> findAll();

  @Operation(
      summary = "Delete a book",
      description = "Delete a specific book by ID",
      tags = {"Books"},
      responses = {@ApiResponse(description = "Success", responseCode = "204")})
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<Void> delete(@PathVariable Long id);

  @Operation(
      summary = "Create a book",
      description = "Creates a new book",
      tags = {"Books"},
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Book created successfully",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class))
            })
      })
  @ConflictApiResponseDoc
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @ValidationExceptionApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<BookResponseDto> save(@RequestBody @Valid BookCreateDto bookCreateDto);

  @Operation(
      summary = "Update a book",
      description = "Updates an existing book",
      tags = {"Books"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = BookResponseDto.class))
            })
      })
  @NotFoundApiResponseDoc
  @ConflictApiResponseDoc
  @UnauthorizedApiResponseDoc
  @ValidationExceptionApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<BookResponseDto> update(
      @PathVariable Long id, @RequestBody @Valid BookUpdateDto bookUpdateDto);
}
