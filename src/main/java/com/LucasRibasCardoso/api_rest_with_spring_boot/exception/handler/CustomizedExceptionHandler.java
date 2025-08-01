package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.handler;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.DefaultResponseException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.FieldExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.ValidationExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.CustomValidationException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.bookExceptions.BookNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.*;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomizedExceptionHandler.class);

  private DefaultResponseException buildDefaultResponse(
      HttpStatus status, String message, WebRequest request) {
    return new DefaultResponseException(
        Instant.now(), status.value(), message, request.getDescription(false));
  }

  @ExceptionHandler(FileStorageException.class)
  public final ResponseEntity<DefaultResponseException> handleFileStorageException(
      FileStorageException ex, WebRequest request) {
    logger.error("File storage error", ex);
    DefaultResponseException response =
        buildDefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<DefaultResponseException> handleAllExceptions(
      Exception ex, WebRequest request) {
    logger.error("Unhandled exception", ex);
    DefaultResponseException response =
        buildDefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public final ResponseEntity<DefaultResponseException> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {
    logger.warn("Data integrity violation", ex);
    DefaultResponseException response =
        buildDefaultResponse(
            HttpStatus.CONFLICT,
            "Conflito de dados: Um ou mais valores enviados violam restrições do banco de dados.",
            request);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    List<FieldExceptionResponse> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new FieldExceptionResponse(
                        fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

    ValidationExceptionResponse validationResponse =
        new ValidationExceptionResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Um ou mais campos são inválidos. Verifique e tente novamente.",
            request.getDescription(false),
            errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResponse);
  }

  @ExceptionHandler(CustomValidationException.class)
  protected ResponseEntity<ValidationExceptionResponse> handleCustomValidationException(
      CustomValidationException ex) {

    ValidationExceptionResponse validationResponse = ex.getValidationResponse();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ValidationExceptionResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {

    List<FieldExceptionResponse> errors =
        ex.getConstraintViolations().stream()
            .map(
                v -> {
                  String invalid = String.valueOf(v.getInvalidValue());
                  String msg = String.format("%s: %s", v.getMessage(), invalid);
                  return new FieldExceptionResponse(v.getPropertyPath().toString(), msg);
                })
            .toList();

    ValidationExceptionResponse response =
        new ValidationExceptionResponse(
            Instant.now(), HttpStatus.BAD_REQUEST.value(), "Invalid request data", null, errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    String message = "O corpo da requisição é inválido ou está mal formatado.";

    if (ex.getCause() instanceof InvalidFormatException ifx
        && ifx.getTargetType() != null
        && ifx.getTargetType().isEnum()) {
      String fieldName =
          ifx.getPath().stream()
              .map(com.fasterxml.jackson.databind.JsonMappingException.Reference::getFieldName)
              .collect(Collectors.joining("."));
      String acceptedValues =
          Arrays.stream(ifx.getTargetType().getEnumConstants())
              .map(Object::toString)
              .collect(Collectors.joining(", "));
      message =
          String.format(
              "O valor '%s' não é válido para o campo '%s'. Os valores aceitos são: %s.",
              ifx.getValue(), fieldName, acceptedValues);
    }

    logger.warn("Malformed request body", ex);
    DefaultResponseException response =
        buildDefaultResponse(HttpStatus.BAD_REQUEST, message, request);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler({
    PersonNotFoundException.class,
    BookNotFoundException.class,
    FileNotFoundException.class
  })
  public final ResponseEntity<DefaultResponseException> handlerResourceNotFoundException(
      Exception ex, WebRequest request) {
    DefaultResponseException response =
        buildDefaultResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler({
    PersonAlreadyExistsException.class,
    BookAlreadyExistsException.class,
    RequiredObjectIsNullException.class,
    UnsupportedFileExtensionException.class,
    InvalidFileException.class,
    ExportFileException.class
  })
  public final ResponseEntity<DefaultResponseException> handlerBadRequestException(
      Exception ex, WebRequest request) {
    DefaultResponseException response =
        buildDefaultResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
