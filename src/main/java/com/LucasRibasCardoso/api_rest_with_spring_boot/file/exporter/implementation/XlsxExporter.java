package com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.implementation;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.contract.FileExporter;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class XlsxExporter implements FileExporter {

  @Override
  public Resource exportFile(List<PersonResponseDto> listOfPerson) throws Exception {
    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Persons");
      Row headerRow = sheet.createRow(0);
      String[] headers = {"ID", "First Name", "Last Name", "CPF", "Gender", "Enabled"};

      for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(createHeaderCellStyle(workbook));
      }

      int rowIndex = 1;

      for (PersonResponseDto person : listOfPerson) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(person.getId());
        row.createCell(1).setCellValue(person.getFirstName());
        row.createCell(2).setCellValue(person.getLastName());
        row.createCell(3).setCellValue(person.getCpf());
        row.createCell(4).setCellValue(person.getGender().name());
        row.createCell(5)
            .setCellValue(person.getEnabled() != null && person.getEnabled() ? "Yes" : "No");
      }

      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);

      return new ByteArrayResource(outputStream.toByteArray());
    }
  }

  private CellStyle createHeaderCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
  }
}
