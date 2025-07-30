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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "People", description = "Endpoints for managing people")
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
      description = "Returns a list of all person",
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
  ResponseEntity<PagedModel<EntityModel<PersonResponseDto>>> findAll(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "direction", defaultValue = "ASC") String direction,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      PagedResourcesAssembler<PersonResponseDto> assembler);

  @Operation(
      summary = "Find people filtered by first name",
      description = "Returns a list of all people",
      tags = {"People"},
      responses = {
        @ApiResponse(
            description = "Returned list of person filtered by first name",
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
  ResponseEntity<PagedModel<EntityModel<PersonResponseDto>>> findByName(
      @PathVariable String firstName,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "direction", defaultValue = "ASC") String direction,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      PagedResourcesAssembler<PersonResponseDto> assembler);

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

  @Operation(
      summary = "Disable a person",
      description = "Disable a specific person by id",
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
  @UnauthorizedApiResponseDoc
  @NotFoundApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<PersonResponseDto> disablePerson(@PathVariable Long id);

  @Operation(
      summary = "Enable a person",
      description = "Enable a specific person by id",
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
  @UnauthorizedApiResponseDoc
  @NotFoundApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<PersonResponseDto> enablePerson(@PathVariable Long id);
}
