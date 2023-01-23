package com.telcobright.SmsReport.Admin.repositories;

import com.telcobright.SmsReport.Models.AccountBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class __sms_report_admin__accbalance {
    @Id
    private Integer id;
}
public interface AccBalanceRepository extends CrudRepository<AccountBalance, Integer> {

    @Query("select ac from account_balance ac where ac.accountId =?1")
    Object getAccountInfo(String accountId);

//    @Query("select ac from account_balance ac where ac.accountId =?1")
//    Page<AccountBalance> getAccBalance(String accountId,Pageable pageable);

}
