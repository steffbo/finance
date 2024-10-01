package cc.remer.finance.transaction;

import cc.remer.finance.bank.Bank;
import jakarta.persistence.*;

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

  @Column(nullable = false)
  private LocalDate bookingDate;
  @Column(nullable = false)
  private LocalDate valueDate;
  @Column(nullable = false)
  private String status;
  @Column(nullable = false)
  private String payer;
  @Column(nullable = false)
  private String payee;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private String transactionType;
  @Column(nullable = false)
  private String iban;
  @Column(nullable = false)
  private BigDecimal amount;

  private String creditorId;
  private String mandateReference;
  private String customerReference;

}
