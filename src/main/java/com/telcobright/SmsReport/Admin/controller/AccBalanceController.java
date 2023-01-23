package com.telcobright.SmsReport.Admin.controller;
import com.telcobright.SmsReport.Admin.repositories.AccBalanceRepository;
import com.telcobright.SmsReport.Models.AccountBalance;
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
    public Object accountBalance(@RequestBody Map<String, Object> payload) {
        String accountId = (String) payload.get("accountId");
        try {
            Object accountInfo = accBalanceRepository.getAccountInfo(accountId);

            if (accountInfo==null){
                AccountBalance accountBalanceInfo = new AccountBalance();
                accountBalanceInfo.accountId = accountId;
                accountBalanceInfo.newBalance = getBalanceFromOfbiz(accountId);
                return accBalanceRepository.save(accountBalanceInfo);

            }else {

                return accountInfo;
            }

        }
        catch (Exception e){
            return e;
        }

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/updateAccountBalance",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object updateAccountBalance(@RequestBody Map<String, Object> payload) {
        String accountId = (String) payload.get("accountId");
        Double amount = Double.parseDouble((String) payload.get("amount"));
        String txIdentifier = (String) payload.get("txIdentifier");
        String remark = (String) payload.get("remark");
        try {
            Map<String,Object> id = new HashMap<>();
            id.put("accountId",accountId);
            AccountBalance updatedAccountBalance =  (AccountBalance) accountBalance(id);

            Double prevBalance= updatedAccountBalance.newBalance!=null?updatedAccountBalance.newBalance:Double.parseDouble("0.0");
            Double newBalance = prevBalance + amount;
            updatedAccountBalance.prevBalance = prevBalance;
            updatedAccountBalance.newBalance = newBalance;
            updatedAccountBalance.txIdentifier = txIdentifier;
            updatedAccountBalance.remark = remark;
            return accBalanceRepository.save(updatedAccountBalance);

        }
        catch (Exception e){
            return e;
        }

    }


}
