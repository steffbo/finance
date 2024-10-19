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
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Direction direction;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String partner;
  private String partnerIban;
  private String creditorId;
  private String mandateReference;
  private String customerReference;

}
