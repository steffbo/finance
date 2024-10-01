package cc.remer.finance.imports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaImportService {

  public static final String CSV_TYPE = "text/csv";
  public static final String PDF_TYPE = "application/pdf";

  private final List<ImportService> importServices;

  ImportService getImportService(MultipartFile file) {
    return importServices.stream()
      .filter(importService -> importService.isMatchingService(file))
      .findFirst()
      .orElseThrow();
  }
}
