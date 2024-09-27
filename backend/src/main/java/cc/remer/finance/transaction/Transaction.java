package cc.remer.finance.transaction;

import cc.remer.finance.bank.Bank;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "bank_id")
  private Bank bank;

  private LocalDate bookingDate;
  private LocalDate valueDate;
  private String status;
  private String payer;
  private String payee;
  private String description;
  private String transactionType;
  private String iban;
  private BigDecimal amount;
  private String creditorId;
  private String mandateReference;
  private String customerReference;

}
