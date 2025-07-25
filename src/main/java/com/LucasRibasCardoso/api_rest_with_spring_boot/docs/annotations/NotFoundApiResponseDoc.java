package com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.DefaultResponseException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.MediaType;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
    description = "Not found",
    responseCode = "404",
    content = {
      @Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = DefaultResponseException.class)),
      @Content(
          mediaType = MediaType.APPLICATION_YAML_VALUE,
          schema = @Schema(implementation = DefaultResponseException.class)),
      @Content(
          mediaType = MediaType.APPLICATION_XML_VALUE,
          schema = @Schema(implementation = DefaultResponseException.class))
    })
public @interface NotFoundApiResponseDoc {}

/** Essa anotação é usada para documentar a resposta de erro 404 (Not Found). */
