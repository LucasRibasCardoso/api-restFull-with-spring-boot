package com.LucasRibasCardoso.api_rest_with_spring_boot.docs;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.*;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.book.BookResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(
    name = "Authentication and Authorization",
    description = "Endpoints responsible for user authentication and authorization.")
public interface AuthControllerDocs {

  @Operation(
      summary = "Authenticate a user and return a JWT token.",
      tags = {"Authentication and Authorization"},
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
  ResponseEntity<TokenResponseDto> signin(@RequestBody @Valid LoginRequestDto loginRequestDto);

  @Operation(
      summary = "Refresh Token for authentication user and return a JWT token.",
      tags = {"Authentication and Authorization"},
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
  ResponseEntity<TokenResponseDto> refreshToken(
      @PathVariable("username") String username,
      @RequestHeader("Authorization") String refreshToken);

  @Operation(
      summary = "Sign up a new user",
      tags = {"Authentication and Authorization"},
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
  ResponseEntity<SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto credentialsDto);
}
