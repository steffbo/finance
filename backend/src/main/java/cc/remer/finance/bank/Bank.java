package cc.remer.finance.bank;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Bank {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private BankType type;

  private String name;
  private String iban;
}
