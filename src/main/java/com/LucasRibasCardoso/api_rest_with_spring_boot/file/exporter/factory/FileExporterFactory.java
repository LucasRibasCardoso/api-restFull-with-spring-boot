package com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.factory;

import com.LucasRibasCardoso.api_rest_with_spring_boot.exception.file.UnsupportedFileExtensionException;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.MediaTypes;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.contract.FileExporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.implementation.CsvExporter;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.implementation.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

  private static final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

  private final ApplicationContext applicationContext;

  public FileExporterFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public FileExporter getExporter(String acceptHeader) throws Exception {
    if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
      // O spring é responsável por instanciar o XlsxImporter ao invés de usar o new
      return applicationContext.getBean(XlsxExporter.class);
    }
    if (acceptHeader.equals(MediaTypes.APPLICATION_CSV_VALUE)) {
      // O spring é responsável por instanciar o CsvImporter ao invés de usar o new
      return applicationContext.getBean(CsvExporter.class);
    }

    throw new UnsupportedFileExtensionException(
        "Unsupported file extension. Please upload a .xlsx or .csv file.");
  }
}
