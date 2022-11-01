package com.telcobright.smsreport.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
class __sms_report__dashboard {
    @Id
    private Integer id;
}

public interface DashBoardRepository extends JpaRepository<__sms_report__dashboard, Integer> {
    @Query("select count(ct) from campaign_task ct")
    int campaignTaskCount();
}
