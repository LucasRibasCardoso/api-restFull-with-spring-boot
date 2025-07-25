package com.LucasRibasCardoso.api_rest_with_spring_boot.docs;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.BadRequestApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.ConflictApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.InternalServerErrorApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.NotFoundApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.UnauthorizedApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.ValidationExceptionApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface PersonControllerDocs {

  @Operation(
      summary = "Find a person",
      description = "Return a specific person by ID",
      tags = {"People"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class))
            })
      })
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<PersonResponseDto> findById(@PathVariable Long id);

  @Operation(
      summary = "Delete a person",
      description = "Delete a specific person by ID",
      tags = {"People"},
      responses = {@ApiResponse(description = "Success", responseCode = "204")})
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<Void> delete(@PathVariable Long id);

  @Operation(
      summary = "Find all people",
      description = "Returns a list of all people",
      tags = {"People"},
      responses = {
        @ApiResponse(
            description = "Returned list of people",
            responseCode = "200",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  array = @ArraySchema(schema = @Schema(implementation = PersonResponseDto.class)))
            }),
      })
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<List<PersonResponseDto>> findAll();

  @Operation(
      summary = "Create a person",
      description = "Creates a new person",
      tags = {"People"},
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Person created successfully",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class))
            })
      })
  @ConflictApiResponseDoc
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @ValidationExceptionApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<PersonResponseDto> save(@RequestBody PersonCreateDto personCreateDto);

  @Operation(
      summary = "Update a person",
      description = "Updates an existing person",
      tags = {"People"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = PersonResponseDto.class))
            })
      })
  @NotFoundApiResponseDoc
  @ConflictApiResponseDoc
  @UnauthorizedApiResponseDoc
  @ValidationExceptionApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<PersonResponseDto> update(
      @PathVariable Long id, @RequestBody @Valid PersonUpdateDto personUpdateDto);
}
