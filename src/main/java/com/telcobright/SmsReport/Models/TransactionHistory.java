package com.telcobright.SmsReport.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "transaction_history")
public class TransactionHistory {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        public Long id;

        @Column(name = "account_id")
        public String accountId;

        @Column(name = "prev_balance")
        public Double prevBalance;

        @Column(name = "new_amount")
        public Double newAmount;

        @Column(name = "new_balance")
        public Double newBalance;

        @Column(name = "remark")
        public String remark;

        @Column(name = "tx_identifier")
        public String txIdentifier;


        public static TransactionHistory fromAccountBalance(AccountBalance accountBalance) {
                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.accountId = accountBalance.accountId;
                transactionHistory.newAmount = accountBalance.newAmount;
                transactionHistory.newBalance = accountBalance.newBalance;
                transactionHistory.prevBalance = accountBalance.prevBalance;
                transactionHistory.remark = accountBalance.remark;
                transactionHistory.txIdentifier = accountBalance.txIdentifier;
                return transactionHistory;
        }
}
