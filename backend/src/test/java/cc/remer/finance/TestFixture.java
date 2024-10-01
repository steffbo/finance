package cc.remer.finance;

import cc.remer.finance.bank.Bank;
import cc.remer.finance.bank.BankType;

public class TestFixture {

  public static final String TEST_BANK_NAME = "Test Bank";
  public static final String TEST_BANK_IBAN = "DE00120300000000000000";
  public static final BankType TEST_BANK_TYPE = BankType.DKB;

  public static Bank bank() {
    Bank bank = new Bank();
    bank.setName(TEST_BANK_NAME);
    bank.setType(TEST_BANK_TYPE);
    bank.setIban(TEST_BANK_IBAN);
    return bank;
  }

}
