package cc.remer.finance.imports;

import cc.remer.finance.bank.Bank;
import cc.remer.finance.bank.BankRepository;
import cc.remer.finance.bank.BankType;
import cc.remer.finance.transaction.Transaction;
import cc.remer.finance.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

@Service
@RequiredArgsConstructor
public class DkbCsvService implements ImportService {

  private static final String BOOKING_DATE_COLUMN = "Buchungsdatum";
  private static final String VALUE_DATE_COLUMN = "Wertstellung";
  private static final String STATUS_COLUMN = "Status";
  private static final String SENDER_COLUMN = "Zahlungspflichtige*r";
  private static final String RECEIVER_COLUMN = "Zahlungsempfänger*in";
  private static final String DESCRIPTION_COLUMN = "Verwendungszweck";
  private static final String TRANSACTION_TYPE_COLUMN = "Umsatztyp";
  private static final String IBAN_COLUMN = "IBAN";
  private static final String AMOUNT_COLUMN = "Betrag (€)";
  private static final String CREDITOR_ID_COLUMN = "Gläubiger-ID";
  private static final String MANDATE_REFERENCE_COLUMN = "Mandatsreferenz";
  private static final String CUSTOMER_REFERENCE_COLUMN = "Kundenreferenz";

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
      CSVParser parser = CSVFormat.DEFAULT
        .withFirstRecordAsHeader()
        .withSkipHeaderRecord(true)
        .parse(reader);

      // Skip the first 5 lines (including the header)
      List<CSVRecord> records = parser.getRecords();
      for (int i = 5; i < records.size(); i++) {
        CSVRecord record = records.get(i);
        Transaction transaction = new Transaction();
        transaction.setBookingDate(LocalDate.parse(record.get(BOOKING_DATE_COLUMN), dateFormatter));
        transaction.setValueDate(LocalDate.parse(record.get(VALUE_DATE_COLUMN), dateFormatter));
        transaction.setStatus(record.get(STATUS_COLUMN));
        transaction.setPayer(record.get(SENDER_COLUMN));
        transaction.setPayee(record.get(RECEIVER_COLUMN));
        transaction.setDescription(record.get(DESCRIPTION_COLUMN));
        transaction.setTransactionType(record.get(TRANSACTION_TYPE_COLUMN));
        transaction.setIban(record.get(IBAN_COLUMN));
        transaction.setAmount(new BigDecimal(record.get(AMOUNT_COLUMN).replace(".", "").replace(",", ".")));
        transaction.setCreditorId(record.get(CREDITOR_ID_COLUMN));
        transaction.setMandateReference(record.get(MANDATE_REFERENCE_COLUMN));
        transaction.setCustomerReference(record.get(CUSTOMER_REFERENCE_COLUMN));
        transactions.add(transaction);
      }
    }

    return transactions;
  }
}
