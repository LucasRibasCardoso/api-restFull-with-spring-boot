package com.LucasRibasCardoso.api_rest_with_spring_boot.integration.config;

public interface TestsConfigs {
  int SERVER_PORT = 8888;

  String HEADER_PARAM_AUTHORIZATION = "Authorization";
  String HEADER_PARAM_ORIGIN = "Origin";

  String ORIGIN_LOCAL = "http://localhost:8080";
  String ORIGIN_VALID = "http://localhost:3000";
  String ORIGIN_INVALID = "http://www.invalid.com";
}
