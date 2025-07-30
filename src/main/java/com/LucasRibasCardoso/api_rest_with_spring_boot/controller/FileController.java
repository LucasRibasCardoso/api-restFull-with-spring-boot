package com.LucasRibasCardoso.api_rest_with_spring_boot.controller;

import com.LucasRibasCardoso.api_rest_with_spring_boot.docs.FileControllerDocs;
import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.file.UploadFileResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/files")
public class FileController implements FileControllerDocs {

  private static final Logger logger = LoggerFactory.getLogger(FileController.class);

  private final FileStorageService fileStorageService;

  public FileController(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @PostMapping("/uploadFile")
  @Override
  public UploadFileResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
    String fileName = fileStorageService.fileUpload(file);

    String fileDownloadUri =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/v1/files/download/")
            .path(fileName)
            .toUriString();

    return new UploadFileResponseDto(
        fileName, fileDownloadUri, file.getContentType(), file.getSize());
  }

  @PostMapping("/uploadFiles")
  @Override
  public List<UploadFileResponseDto> uploadMultiFiles(
      @RequestParam("files") MultipartFile[] files) {
    return Arrays.stream(files).map(this::uploadFile).toList();
  }

  @GetMapping("/download/{fileName:.+}")
  @Override
  public ResponseEntity<Resource> downloadFile(
      @PathVariable String fileName, HttpServletRequest request) {
    Resource resource = fileStorageService.loadFileAsResource(fileName);
    String contentType = null;

    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (Exception ex) {
      logger.error("Could not determine file type.");
    }

    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
