package com.telcobright.SmsReport.Models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "account_balance")
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "account_id")
    public String accountId;

    @Column(precision=8, scale=2,name = "prev_balance")
    public Double prevBalance;

    @Column(precision=8, scale=2,name = "new_amount")
    public Double newAmount;

    @Column(precision=8, scale=2,name = "new_balance")
    public Double newBalance;

    @Column(name = "remark")
    public String remark;

    @Column(name = "tx_identifier")
    public String txIdentifier;
}
