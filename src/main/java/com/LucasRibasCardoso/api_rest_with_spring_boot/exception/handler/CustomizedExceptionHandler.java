package com.LucasRibasCardoso.api_rest_with_spring_boot.exception.handler;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.DefaultResponseException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.FieldExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.ValidationExceptionResponse;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonAlreadyExistsException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.personExceptions.PersonNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<DefaultResponseException> handleAllExceptions(
      Exception ex, WebRequest request) {
    DefaultResponseException defaultResponseException =
        new DefaultResponseException(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ex.getMessage(),
            request.getDescription(false));
    return new ResponseEntity<>(defaultResponseException, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public final ResponseEntity<DefaultResponseException> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex, WebRequest request) {
    DefaultResponseException defaultResponseException =
        new DefaultResponseException(
            Instant.now(),
            HttpStatus.CONFLICT.value(),
            "Conflito de dados: Um ou mais valores enviados violam restrições do banco de dados.",
            request.getDescription(false));
    return new ResponseEntity<>(defaultResponseException, HttpStatus.CONFLICT);
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

    return new ResponseEntity<>(validationResponse, HttpStatus.BAD_REQUEST);
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

    DefaultResponseException defaultResponseException =
        new DefaultResponseException(
            Instant.now(), HttpStatus.BAD_REQUEST.value(), message, request.getDescription(false));

    return new ResponseEntity<>(defaultResponseException, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({PersonNotFoundException.class})
  public final ResponseEntity<DefaultResponseException> handlerResourceNotFoundException(
      Exception ex, WebRequest request) {
    DefaultResponseException defaultResponseException =
        new DefaultResponseException(
            Instant.now(),
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getDescription(false));
    return new ResponseEntity<>(defaultResponseException, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({PersonAlreadyExistsException.class})
  public final ResponseEntity<DefaultResponseException> handlerBadRequestException(
      Exception ex, WebRequest request) {
    DefaultResponseException defaultResponseException =
        new DefaultResponseException(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getDescription(false));
    return new ResponseEntity<>(defaultResponseException, HttpStatus.BAD_REQUEST);
  }
}
