package com.LucasRibasCardoso.api_rest_with_spring_boot.service;

import com.LucasRibasCardoso.api_rest_with_spring_boot.config.FileStorageConfig;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.FileNotFoundException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.FileStorageException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

  private final Path fileStorageLocation;

  public FileStorageService(FileStorageConfig fileStorageConfig) {
    this.fileStorageLocation =
        Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

    try {
      logger.info("Creating repository");
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      logger.error("Couldn't create directory where files will be stored");
      throw new FileStorageException("Couldn't create directory where files will be stored", ex);
    }
  }

  public String fileUpload(MultipartFile file) {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      if (fileName.contains("..")) {
        logger.error("Invalid file name, path contains invalid path sequence: {}", fileName);
        throw new FileStorageException(
            "Sorry! Filename contains a invalid path sequence " + fileName);
      }

      logger.info("Save file in Disk");
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return fileName;

    } catch (Exception ex) {
      logger.error("Could not store file {}. Please try again!", fileName);
      throw new FileStorageException(
          "Could not store file " + fileName + ". Please try again!", ex);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists()) {
        logger.error("File not found {}", fileName);
        throw new FileNotFoundException("File not found " + fileName);
      }
      return resource;
    } catch (Exception ex) {
      logger.error("File not found {}", fileName);
      throw new FileNotFoundException("File not found " + fileName, ex);
    }
  }
}
