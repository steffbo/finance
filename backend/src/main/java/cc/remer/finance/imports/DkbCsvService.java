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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static cc.remer.finance.imports.MetaImportService.CSV_TYPE;

@Service
@RequiredArgsConstructor
public class DkbCsvService implements ImportService {

  private static final int BOOKING_DATE_COLUMN = 0;
  private static final int VALUE_DATE_COLUMN = 1;
  private static final int STATUS_COLUMN = 2;
  private static final int SENDER_COLUMN = 3;
  private static final int RECEIVER_COLUMN = 4;
  private static final int DESCRIPTION_COLUMN = 5;
  private static final int TRANSACTION_TYPE_COLUMN = 6;
  private static final int IBAN_COLUMN = 7;
  private static final int AMOUNT_COLUMN = 8;
  private static final int CREDITOR_ID_COLUMN = 9;
  private static final int MANDATE_REFERENCE_COLUMN = 10;
  private static final int CUSTOMER_REFERENCE_COLUMN = 11;

  private final BankRepository bankRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public boolean isMatchingService(MultipartFile file) {
    if (!CSV_TYPE.equals(file.getContentType())) {
      return false;
    }

    return containsMatchingIban(file);
  }

  @Override
  public int importFile(MultipartFile file) throws Exception {
    String iban = readIbanFromCsv(file);
    Bank bank = bankRepository.findByIban(iban)
      .orElseThrow(() -> new RuntimeException("Bank not found"));

    List<Transaction> transactions = readTransactionsFromCsv(file);
    transactions.forEach(transaction -> transaction.setBank(bank));
    transactionRepository.saveAll(transactions);
    return transactions.size();
  }

  private boolean containsMatchingIban(MultipartFile file) {
    try {
      String iban = readIbanFromCsv(file);
      return iban.contains(BankType.DKB.blz);
    } catch (IOException e) {
      return false;
    }
  }

  private String readIbanFromCsv(MultipartFile file) throws IOException {
    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      CSVParser parser = CSVFormat.DEFAULT.parse(reader);
      List<CSVRecord> records = parser.getRecords();

      if (!records.isEmpty()) {
        CSVRecord firstRecord = records.getFirst();
        if (firstRecord.size() >= 2) {
          return firstRecord.get(1);
        }
      }
    }
    return "";
  }

  private List<Transaction> readTransactionsFromCsv(MultipartFile file) throws IOException {
    List<Transaction> transactions = new ArrayList<>();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      CSVParser parser = CSVFormat.DEFAULT.builder()
        .setSkipHeaderRecord(false)
        .build().parse(reader);

      // Skip the first 5 lines (including the header)
      List<CSVRecord> records = parser.getRecords();
      for (int i = 5; i < records.size(); i++) {
        CSVRecord record = records.get(i);
        Transaction transaction = new Transaction();
        transaction.setBookingDate(LocalDate.parse(record.get(BOOKING_DATE_COLUMN), dateFormatter));
        transaction.setValueDate(LocalDate.parse(record.get(VALUE_DATE_COLUMN), dateFormatter));
        transaction.setStatus(getStatus(record.get(STATUS_COLUMN)));
        transaction.setDescription(record.get(DESCRIPTION_COLUMN));
        Direction direction = getDirection(record.get(TRANSACTION_TYPE_COLUMN));
        transaction.setDirection(direction);
        transaction.setPartner(record.get(direction == Direction.IN ? SENDER_COLUMN : RECEIVER_COLUMN));
        transaction.setPartnerIban(record.get(IBAN_COLUMN));
        transaction.setAmount(new BigDecimal(record.get(AMOUNT_COLUMN).replace(".", "").replace(",", ".")));
        transaction.setCreditorId(record.get(CREDITOR_ID_COLUMN));
        transaction.setMandateReference(record.get(MANDATE_REFERENCE_COLUMN));
        transaction.setCustomerReference(record.get(CUSTOMER_REFERENCE_COLUMN));
        transactions.add(transaction);
      }
    }

    return transactions;
  }

  private Direction getDirection(String direction) {
    if ("Eingang".equals(direction)) {
      return Direction.IN;
    }
    return Direction.OUT;
  }

  private Status getStatus(String status) {
    if ("Gebucht".equals(status)) {
      return Status.COMPLETE;
    }
    return Status.PENDING;
  }
}
