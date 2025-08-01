package com.LucasRibasCardoso.api_rest_with_spring_boot.file.importer.contract;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonCreateDto;
import com.LucasRibasCardoso.api_rest_with_spring_boot.model.Gender;
import java.io.InputStream;
import java.util.List;

public interface FileImporter {

  List<PersonCreateDto> importFile(InputStream inputStream) throws Exception;

  default Gender parseGender(String value) {
    if ("M".equalsIgnoreCase(value)) {
      return Gender.M;
    }
    if ("F".equalsIgnoreCase(value)) {
      return Gender.F;
    }
    return null;
  }
}
