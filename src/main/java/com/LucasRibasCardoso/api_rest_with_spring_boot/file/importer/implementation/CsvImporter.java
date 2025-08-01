package com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.implementation;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.contract.FileImporter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CsvImporter implements FileImporter {
  @Override
  public List<PersonCreateDto> importFile(InputStream inputStream) throws Exception {
    CSVFormat csvFormat =
        CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .get();

    Iterable<CSVRecord> records = csvFormat.parse(new InputStreamReader(inputStream));

    return parseRecordsToPersonCreateDtoList(records);
  }

  private List<PersonCreateDto> parseRecordsToPersonCreateDtoList(Iterable<CSVRecord> records) {
    List<PersonCreateDto> people = new ArrayList<>();

    for (CSVRecord record : records) {
      PersonCreateDto person =
          new PersonCreateDto(
              record.get("first_name"),
              record.get("last_name"),
              record.get("cpf"),
              parseGender(record.get("gender")));
      people.add(person);
    }

    return people;
  }
}
