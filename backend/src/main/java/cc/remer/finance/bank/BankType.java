package cc.remer.finance.bank;

public enum BankType {
  DKB("12030000"),
  ING("50010517");

  public final String blz;

  BankType(String blz) {
    this.blz = blz;
  }
}
