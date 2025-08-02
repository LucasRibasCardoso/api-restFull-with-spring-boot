package com.LucasRibasCardoso.api_rest_with_spring_boot.docs;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.BadRequestApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.InternalServerErrorApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.UnauthorizedApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.mail.EmailRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Email", description = "Endpoints for sending emails")
public interface EmailControllerDocs {

  @Operation(
      summary = "Send an e-mail",
      description = "Sends an email by providing details, subject and body.",
      tags = {"Email"},
      responses = @ApiResponse(description = "Success", responseCode = "200", content = @Content))
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<String> sendSimpleEmail(EmailRequestDto emailRequestDto);

  @Operation(
      summary = "Send an e-mail with attachment",
      description = "Sends an email by providing details, subject, body and an attachment.",
      tags = {"Email"},
      responses = @ApiResponse(description = "Success", responseCode = "200", content = @Content))
  @BadRequestApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);
}
