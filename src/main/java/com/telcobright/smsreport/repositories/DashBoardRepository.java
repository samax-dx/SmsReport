package com.telcobright.smsreport.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Entity
class __sms_report__dashboard {
    @Id
    private Integer id;
}

public interface DashBoardRepository extends JpaRepository<__sms_report__dashboard, Integer> {

   @Query("select count(ct) from campaign ct where ct.partyId = ?1 and ct.createdTxStamp>= ?3 and ct.createdTxStamp<= ?2")
   int weekCampaignCountById(@Param("paryId") String partyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(ct) from campaign ct where ct.partyId = ?1 and ct.createdTxStamp>= ?2 and ct.createdTxStamp<= ?3")
    int todaysCampaignCountById(@Param("paryId") String partyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(*) as cnt from campaign_task ct where campaignId in (select c.campaignId as cmpId from campaign c where c.partyId= ?1) and ct.lastUpdatedTxStamp >= ?2 and ct.lastUpdatedTxStamp <= ?3")
    int todaysTotalTaskCountById(@Param("paryId") String partyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(*) as cnt from campaign_task ct where campaignId in (select c.campaignId as cmpId from campaign c where c.partyId= ?1) and ct.lastUpdatedTxStamp >= ?3 and ct.lastUpdatedTxStamp <= ?2")
    int weekTotalTaskCountById(@Param("paryId") String partyId, LocalDateTime weekStartDate, LocalDateTime weekEndDate);

    @Query("select count(*) as cnt from campaign_task ct where campaignId in (select c.campaignId as cmpId from campaign c where c.partyId= ?1) and ct.status in ('sent') and ct.lastUpdatedTxStamp >= ?2 and ct.lastUpdatedTxStamp <= ?3")
    int todaysTotalSuccessCountById(@Param("paryId") String partyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(*) as cnt from campaign_task ct where campaignId in (select c.campaignId as cmpId from campaign c where c.partyId= ?1) and ct.status in ('sent') and ct.lastUpdatedTxStamp >= ?3 and ct.lastUpdatedTxStamp <= ?2")
    int weekTotalSuccessCountById(@Param("paryId") String partyId, LocalDateTime weekStartDate, LocalDateTime weekEndDate);

    @Query("select ct.routeId as rId, count(ct) as cnt from campaign_task ct where campaignId in (select c.campaignId as cmpId from campaign c where c.partyId= ?1) and ct.lastUpdatedTxStamp >= ?2 and ct.lastUpdatedTxStamp <= ?3 group by routeId ")
    List<Object[]> routeDetails(@Param("partyId") String partyId, LocalDateTime startDate, LocalDateTime endDate);

}



    // @Query("select count(ct) as cnt, ct.statusExternal as cnts from campaign_task ct group by ct.campaignId ct having ct.statusExternal in ('success')") having ct.statusExternal in ('success','failed','delivery_pending')

//    @Query("select count(ct) as rtCnt, ct.routeId as rtId from campaign_task ct group by ct.routeId")
//    List<Object[]> routeIdCount();