package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.EmailControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.mail.EmailRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/email")
@SecurityRequirement(name = "bearerAuth")
public class EmailController implements EmailControllerDocs {

  private final EmailService emailService;

  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  @Override
  public ResponseEntity<String> sendSimpleEmail(
      @RequestBody @Valid EmailRequestDto emailRequestDto) {

    emailService.sendSimpleEmail(emailRequestDto);
    return ResponseEntity.ok().body("Email sent with success!");
  }

  @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Override
  public ResponseEntity<String> sendEmailWithAttachment(
      @RequestParam("emailRequest") String emailRequest,
      @RequestParam("attachment") MultipartFile attachment) {

    emailService.sendEmailWithAttachment(emailRequest, attachment);
    return ResponseEntity.ok().body("Email sent with success!");
  }
}
