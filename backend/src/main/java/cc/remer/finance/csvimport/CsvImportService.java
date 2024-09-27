package cc.remer.finance.csvimport;

import cc.remer.finance.bank.Bank;
import cc.remer.finance.bank.BankRepository;
import cc.remer.finance.transaction.Transaction;
import cc.remer.finance.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvImportService {

  // TODO this is just a draft implementation, it needs to be improved

  private final BankRepository bankRepository;
  private final TransactionRepository transactionRepository;

  public void importCsv(MultipartFile file, Long bankId) throws Exception {
    Bank bank = bankRepository.findById(bankId)
        .orElseThrow(() -> new RuntimeException("Bank not found"));

    List<Transaction> transactions = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
    String line;
    boolean dataStarted = false;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    while ((line = reader.readLine()) != null) {
      if (line.startsWith("Buchungsdatum")) {
        dataStarted = true;
        continue;
      }
      if (!dataStarted) continue;

      String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      if (fields.length < 12) continue;

      Transaction transaction = getTransaction(bank, fields, dateFormatter);

      transactions.add(transaction);
    }

    transactionRepository.saveAll(transactions);
  }

  private static Transaction getTransaction(Bank bank, String[] fields, DateTimeFormatter dateFormatter) {
    Transaction transaction = new Transaction();
    transaction.setBank(bank);
    transaction.setBookingDate(LocalDate.parse(fields[0], dateFormatter));
    transaction.setValueDate(LocalDate.parse(fields[1], dateFormatter));
    transaction.setStatus(fields[2]);
    transaction.setPayer(fields[3]);
    transaction.setPayee(fields[4]);
    transaction.setDescription(fields[5]);
    transaction.setTransactionType(fields[6]);
    transaction.setIban(fields[7]);
    transaction.setAmount(new BigDecimal(fields[8].replace(".", "").replace(",", ".")));
    transaction.setCreditorId(fields[9]);
    transaction.setMandateReference(fields[10]);
    transaction.setCustomerReference(fields[11]);
    return transaction;
  }
}
