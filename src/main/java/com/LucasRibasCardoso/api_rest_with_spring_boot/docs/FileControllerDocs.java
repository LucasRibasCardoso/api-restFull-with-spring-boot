package com.LucasRibasCardoso.api_rest_with_spring_boot.docs;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.BadRequestApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.InternalServerErrorApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.NotFoundApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.annotations.UnauthorizedApiResponseDoc;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.file.UploadFileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Files", description = "Endpoints for file operations")
public interface FileControllerDocs {

  @Operation(
      summary = "Upload a file",
      description = "Upload a file to the server",
      tags = {"Files"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = UploadFileResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  schema = @Schema(implementation = UploadFileResponseDto.class)),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  schema = @Schema(implementation = UploadFileResponseDto.class))
            })
      })
  @BadRequestApiResponseDoc
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  UploadFileResponseDto uploadFile(@RequestParam("file") MultipartFile file);

  @Operation(
      summary = "Upload multiple files",
      description = "Upload multiple files to the server",
      tags = {"Files"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Files uploaded successfully",
            content = {
              @Content(
                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                  array =
                      @ArraySchema(schema = @Schema(implementation = UploadFileResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_YAML_VALUE,
                  array =
                      @ArraySchema(schema = @Schema(implementation = UploadFileResponseDto.class))),
              @Content(
                  mediaType = MediaType.APPLICATION_XML_VALUE,
                  array =
                      @ArraySchema(schema = @Schema(implementation = UploadFileResponseDto.class)))
            })
      })
  @BadRequestApiResponseDoc
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  List<UploadFileResponseDto> uploadMultiFiles(@RequestParam("files") MultipartFile[] files);

  @Operation(
      summary = "Download a file",
      description = "Download a file by its filename",
      tags = {"Files"},
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "File downloaded successfully",
            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
      })
  @NotFoundApiResponseDoc
  @UnauthorizedApiResponseDoc
  @InternalServerErrorApiResponseDoc
  ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request);
}
