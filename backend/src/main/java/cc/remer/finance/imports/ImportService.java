package cc.remer.finance.imports;

import org.springframework.web.multipart.MultipartFile;

public interface ImportService {

  boolean isMatchingService(MultipartFile file);

  int importFile(MultipartFile file) throws Exception;
}
