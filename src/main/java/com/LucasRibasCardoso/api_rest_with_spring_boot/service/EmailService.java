package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.config.EmailConfig;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.mail.EmailRequestDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  private final EmailSender emailSender;
  private final EmailConfig emailConfig;

  public EmailService(EmailSender emailSender, EmailConfig emailConfig) {
    this.emailSender = emailSender;
    this.emailConfig = emailConfig;
  }

  public void sendSimpleEmail(EmailRequestDto requestDto) {
    emailSender
        .to(requestDto.to())
        .withSubject(requestDto.subject())
        .withMessage(requestDto.body())
        .send(emailConfig);
  }

  public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
    File tempFile = null;

    try {
      // Converte JSON para EmailRequestDto
      EmailRequestDto emailRequest =
          new ObjectMapper().readValue(emailRequestJson, EmailRequestDto.class);

      tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
      attachment.transferTo(tempFile);

      emailSender
          .to(emailRequest.to())
          .withSubject(emailRequest.subject())
          .withMessage(emailRequest.body())
          .attachment(tempFile.getAbsolutePath())
          .send(emailConfig);

    } catch (JsonProcessingException e) {
      logger.error("Failed to parse email request JSON: {}", emailRequestJson, e);
      throw new RuntimeException("Failed to parse email request JSON", e);
    } catch (IOException e) {
      logger.error("Failed to send email with attachment: {}", attachment.getOriginalFilename(), e);
      throw new RuntimeException("Failed to send email with attachment", e);
    } finally {
      if (tempFile != null && tempFile.exists()) tempFile.delete();
    }
  }
}
