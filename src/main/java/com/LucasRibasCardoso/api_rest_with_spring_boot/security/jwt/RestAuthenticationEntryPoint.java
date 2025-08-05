package com.LucasRibasCardoso.api_rest_with_spring_boot.security.jwt;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.exceptions.DefaultResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
  private static final int UNAUTHORIZED_STATUS = HttpServletResponse.SC_UNAUTHORIZED;

  private final ObjectMapper objectMapper;

  public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {

    LOGGER.warn(
        "Failed authentication to [{}]: [{}]:", request.getRequestURI(), exception.getMessage());
    response.setStatus(UNAUTHORIZED_STATUS);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    objectMapper.writeValue(response.getOutputStream(), createErrorResponse(request));
  }

  private DefaultResponseException createErrorResponse(HttpServletRequest request) {
    String errorMessage = "Error trying to authenticate user";

    return new DefaultResponseException(
        Instant.now(), UNAUTHORIZED_STATUS, errorMessage, request.getRequestURI());
  }
}
