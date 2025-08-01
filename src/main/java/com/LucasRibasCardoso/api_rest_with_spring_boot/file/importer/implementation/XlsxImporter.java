package com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.implementation;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.contract.FileImporter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class XlsxImporter implements FileImporter {

  @Override
  public List<PersonCreateDto> importFile(InputStream inputStream) throws Exception {

    try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
      XSSFSheet sheet = workbook.getSheetAt(0); // Pega a primeira planilha
      Iterator<Row> rowIterator = sheet.iterator();

      if (rowIterator.hasNext()) rowIterator.next(); // Pula o cabeçalho

      return parseRowsToPersonCreateDtoList(rowIterator);
    }
  }

  private List<PersonCreateDto> parseRowsToPersonCreateDtoList(Iterator<Row> rowIterator) {
    List<PersonCreateDto> people = new ArrayList<>();

    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();

      if (isRowValid(row)) {
        people.add(parseRowToPersonCreateDto(row));
      }
    }

    return people;
  }

  public PersonCreateDto parseRowToPersonCreateDto(Row row) {
    String firstName = row.getCell(0).getStringCellValue();
    String lastName = row.getCell(1).getStringCellValue();
    String cpf = row.getCell(2).getStringCellValue();
    String genderString = row.getCell(3).getStringCellValue();

    return new PersonCreateDto(firstName, lastName, cpf, parseGender(genderString));
  }

  private boolean isRowValid(Row row) {
    return row.getCell(0) != null
        && row.getCell(0).getCellType() != CellType.BLANK
        && row.getCell(1) != null
        && row.getCell(1).getCellType() != CellType.BLANK
        && row.getCell(2) != null
        && row.getCell(2).getCellType() != CellType.BLANK
        && row.getCell(3) != null
        && row.getCell(3).getCellType() != CellType.BLANK;
  }
}
