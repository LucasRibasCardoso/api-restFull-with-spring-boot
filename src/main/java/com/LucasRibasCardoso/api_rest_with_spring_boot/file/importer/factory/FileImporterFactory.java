package com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.factory;

import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.filesExceptions.UnsupportedFileExtensionException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.contract.FileImporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.implementation.CsvImporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.implementation.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

  private static final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

  private final ApplicationContext applicationContext;

  public FileImporterFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public FileImporter getImporter(String fileName) throws Exception {

    if (fileName.endsWith(".xlsx")) {
      // O spring é responsável por instanciar o XlsxImporter ao invés de usar o new
      return applicationContext.getBean(XlsxImporter.class);
    }

    if (fileName.endsWith(".csv")) {
      // O spring é responsável por instanciar o CsvImporter ao invés de usar o new
      return applicationContext.getBean(CsvImporter.class);
    }

    logger.error(
        "Unsupported file extension for file: {}, Please upload a .xlsx or .csv file", fileName);
    throw new UnsupportedFileExtensionException(
        "Unsupported file extension. Please upload a .xlsx or .csv file.");
  }
}
