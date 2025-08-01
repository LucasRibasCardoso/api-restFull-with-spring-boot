package com.LucasRibasCardoso.api_rest_with_spring_boot.file.exporter.contract;

import com.LucasRibasCardoso.api_rest_with_spring_boot.dto.person.PersonResponseDto;
import java.util.List;
import org.springframework.core.io.Resource;

public interface FileExporter {

  Resource exportFile(List<PersonResponseDto> listOfPersonDto) throws Exception;
}
