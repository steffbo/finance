package cc.remer.finance.imports;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static java.lang.String.valueOf;

@Controller
@RequestMapping("/api/v1/import")
@RequiredArgsConstructor
public class ImportController {

  private final MetaImportService metaImportService;

  @PostMapping
  public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
    try {
      ImportService importService = metaImportService.getImportService(file);
      int transactions = importService.importFile(file);
      return ResponseEntity.ok(valueOf(transactions));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error " + e.getMessage());
    }
  }
}
