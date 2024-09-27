package cc.remer.finance;

import cc.remer.finance.bank.Bank;

public class TestFixture {

  public static final String TEST_BANK_NAME = "Test Bank";
  public static final String TEST_BANK_IBAN = "DE89370400440532013000";

  public static Bank bank() {
    Bank bank = new Bank();
    bank.setName(TEST_BANK_NAME);
    bank.setIban(TEST_BANK_IBAN);
    return bank;
  }

}
