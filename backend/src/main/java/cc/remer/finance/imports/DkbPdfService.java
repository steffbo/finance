package cc.remer.finance.imports;

import cc.remer.finance.bank.Bank;
import cc.remer.finance.bank.BankRepository;
import cc.remer.finance.bank.BankType;
import cc.remer.finance.transaction.Direction;
import cc.remer.finance.transaction.Status;
import cc.remer.finance.transaction.Transaction;
import cc.remer.finance.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static cc.remer.finance.imports.MetaImportService.CSV_TYPE;
import static cc.remer.finance.imports.MetaImportService.PDF_TYPE;

@Service
@RequiredArgsConstructor
public class DkbPdfService implements ImportService {

  private final BankRepository bankRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public boolean isMatchingService(MultipartFile file) {
    if (!PDF_TYPE.equals(file.getContentType())) {
      return false;
    }
return true;
//    return containsMatchingIban(file);
  }

  @Override
  public int importFile(MultipartFile file) throws Exception {
    try {
      PDDocument document = PDDocument.load(file.getInputStream());
      PDFTextStripper stripper = new PDFTextStripper();
      String text = stripper.getText(document);
      System.out.println(text);
      document.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }

}
