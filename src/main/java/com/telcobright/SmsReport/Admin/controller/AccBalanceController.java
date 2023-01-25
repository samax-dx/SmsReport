package com.telcobright.SmsReport.Admin.controller;
import com.telcobright.SmsReport.Admin.repositories.AccBalanceRepository;
import com.telcobright.SmsReport.Models.AccountBalance;
import com.telcobright.SmsReport.Models.BalanceUpdateError;
import com.telcobright.SmsReport.Models.BalanceUpdateResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/Account")
public class AccBalanceController {
    final AccBalanceRepository accBalanceRepository;

    public AccBalanceController (AccBalanceRepository accBalanceRepository){
        this.accBalanceRepository = accBalanceRepository;
    }

    private Double getBalanceFromOfbiz(String accountId){
        return 100.3;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/getBalance",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object getAccountBalance(@RequestBody Map<String, Object> payload) {
        try {
            return getAccountBalanceImpl(payload);
        } catch (Exception e) {
            return e;
        }
    }

    private AccountBalance getAccountBalanceImpl(Map<String, Object> payload) throws Exception {
        String accountId = (String) payload.get("accountId");

        AccountBalance accountBalance = accBalanceRepository.getAccountBalanceByAccountId(accountId);

        if (accountBalance == null){
            accountBalance = new AccountBalance();
            accountBalance.accountId = accountId;
            accountBalance.newBalance = getBalanceFromOfbiz(accountId);
            return accBalanceRepository.save(accountBalance);
        }

        return accountBalance;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/updateAccountBalance",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object updateAccountBalance(@RequestBody Map<String, Object> payload) throws Exception {
        String accountId = (String) payload.get("accountId");
        double amount = Double.parseDouble((String) payload.get("amount"));
        String txIdentifier = (String) payload.get("txIdentifier");
        String remark = (String) payload.get("remark");

        Map<String,Object> accountBalanceQueryData = new HashMap<>();
        accountBalanceQueryData.put("accountId", accountId);
        return getBalanceUpdateResult(amount, txIdentifier, remark, accountBalanceQueryData);
    }

    private synchronized BalanceUpdateResult getBalanceUpdateResult(double amount, String txIdentifier, String remark, Map<String, Object> accountBalanceQueryData) throws Exception {
        AccountBalance accountBalance = getAccountBalanceImpl(accountBalanceQueryData);

        double maxNegativeBalance = 0.0; // retrieve from settings
        double prevBalance = accountBalance.newBalance;
        double newBalance = prevBalance + amount; ///if amount type decrease(-5) it won't be less than 0

        if (newBalance < maxNegativeBalance){
            BalanceUpdateError balanceUpdateError = new BalanceUpdateError("Insufficient Balance", newBalance);
            return BalanceUpdateResult.fromBalanceUpdateError(balanceUpdateError);
        } else {
            accountBalance.prevBalance = prevBalance;
            accountBalance.newBalance = newBalance;
            accountBalance.newAmount = amount;
            accountBalance.txIdentifier = txIdentifier;
            accountBalance.remark = remark;
            accBalanceRepository.save(accountBalance);
            return BalanceUpdateResult.fromAccountBalance(accountBalance);
        }
    }
}
