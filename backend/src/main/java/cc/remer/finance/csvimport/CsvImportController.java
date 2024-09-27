package cc.remer.finance.csvimport;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/csv")
@RequiredArgsConstructor
public class CsvImportController {

  private final CsvImportService csvImportService;

  @PostMapping("/import/{bankId}")
  public ResponseEntity<String> importCsv(@PathVariable Long bankId, @RequestParam("file") MultipartFile file) {
    try {
      csvImportService.importCsv(file, bankId);
      return ResponseEntity.ok("CSV imported successfully");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error importing CSV: " + e.getMessage());
    }
  }
}
