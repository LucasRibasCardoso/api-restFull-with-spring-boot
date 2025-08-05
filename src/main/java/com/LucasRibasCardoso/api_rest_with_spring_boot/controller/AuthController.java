package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.AuthControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.LoginRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.SignUpResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.security.TokenResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(
      value = "/signin",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  public ResponseEntity<TokenResponseDto> signin(
      @RequestBody @Valid LoginRequestDto loginRequestDto) {
    TokenResponseDto tokenResponseDto = authService.signIn(loginRequestDto);
    return ResponseEntity.ok(tokenResponseDto);
  }

  @PutMapping("/refresh-token/{username}")
  @Override
  public ResponseEntity<TokenResponseDto> refreshToken(
      @PathVariable("username") String username,
      @RequestHeader("Authorization") String refreshToken) {

    TokenResponseDto token = authService.refreshToken(username, refreshToken);
    return ResponseEntity.ok(token);
  }

  @PostMapping(
      value = "/signup",
      produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      },
      consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_YAML_VALUE,
        MediaType.APPLICATION_XML_VALUE
      })
  @Override
  public ResponseEntity<SignUpResponseDto> signUp(
      @RequestBody @Valid SignUpRequestDto credentialsDto) {

    SignUpResponseDto signUpResponseDto = authService.signUp(credentialsDto);
    return ResponseEntity.ok(signUpResponseDto);
  }
}
