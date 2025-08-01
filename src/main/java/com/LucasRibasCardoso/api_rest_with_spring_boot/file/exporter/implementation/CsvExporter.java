package com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.implementation;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.contract.FileExporter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CsvExporter implements FileExporter {

  @Override
  public Resource exportFile(List<PersonResponseDto> listOfPerson) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
    CSVFormat csvFormat =
        CSVFormat.Builder.create()
            .setHeader("ID", "First Name", "Last Name", "CPF", "Gender", "Enabled")
            .setSkipHeaderRecord(true)
            .get();

    try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
      for (PersonResponseDto person : listOfPerson) {
        csvPrinter.printRecord(
            person.getId(),
            person.getFirstName(),
            person.getLastName(),
            person.getCpf(),
            person.getGender().name(),
            person.getEnabled());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new ByteArrayResource(outputStream.toByteArray());
  }
}
