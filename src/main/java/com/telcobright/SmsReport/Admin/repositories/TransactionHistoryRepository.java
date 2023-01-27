package com.telcobright.SmsReport.Admin.repositories;

import com.telcobright.SmsReport.Models.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
        @Modifying
        @Transactional
        @Query(value = "truncate table transaction_history", nativeQuery = true)
        void deleteTransactionHistory();

}
