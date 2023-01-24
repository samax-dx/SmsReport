package com.telcobright.SmsReport.Admin.repositories;

import com.telcobright.SmsReport.Models.AccountBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccBalanceRepository extends CrudRepository<AccountBalance, Integer> {

    @Query("select ac from account_balance ac where ac.accountId =?1")
    AccountBalance getAccountBalanceByAccountId(String accountId);

//    @Query("select ac from account_balance ac where ac.accountId =?1")
//    Page<AccountBalance> getAccBalance(String accountId,Pageable pageable);

}
