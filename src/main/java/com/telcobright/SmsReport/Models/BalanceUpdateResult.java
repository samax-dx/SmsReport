package com.telcobright.SmsReport.Models;

public class BalanceUpdateResult {
    public String errorMessage;
    public Double requestedAmount;
    public Long id;
    public String accountId;
    public Double prevBalance;
    public Double newAmount;
    public Double newBalance;
    public String remark;
    public String txIdentifier;

    public static BalanceUpdateResult fromAccountBalance(AccountBalance accountBalance) {
        BalanceUpdateResult balanceUpdateResult = new BalanceUpdateResult();
        balanceUpdateResult.id = accountBalance.id;
        balanceUpdateResult.accountId = accountBalance.accountId;
        balanceUpdateResult.prevBalance = accountBalance.prevBalance;
        balanceUpdateResult.newBalance = accountBalance.newBalance;
        balanceUpdateResult.newAmount = accountBalance.newAmount;
        balanceUpdateResult.remark = accountBalance.remark;
        balanceUpdateResult.txIdentifier = accountBalance.txIdentifier;
        balanceUpdateResult.errorMessage = null;
        balanceUpdateResult.requestedAmount = null;
        return balanceUpdateResult;
    }

    public static BalanceUpdateResult fromBalanceUpdateError(BalanceUpdateError balanceUpdateError) {
        BalanceUpdateResult balanceUpdateResult = new BalanceUpdateResult();
        balanceUpdateResult.id = null;
        balanceUpdateResult.accountId = null;
        balanceUpdateResult.prevBalance = null;
        balanceUpdateResult.newBalance = null;
        balanceUpdateResult.newAmount = null;
        balanceUpdateResult.remark = null;
        balanceUpdateResult.txIdentifier = null;
        balanceUpdateResult.requestedAmount = balanceUpdateError.requestedAmount;
        balanceUpdateResult.errorMessage = balanceUpdateError.errorMessage;
        return balanceUpdateResult;
    }

}
