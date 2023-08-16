package com.telcobright.SmsReport.Client.repositories;

import com.telcobright.SmsReport.Models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DropdownRepositoryClient extends JpaRepository<Campaign, Long> {
    @Query("select c from campaign c where PARTY_ID = ?1")
    List<Campaign> findAllCampaign(String partyId);

    @Query(value = "select distinct ct.ROUTE_ID from campaign c join campaign_task ct on  ct.CAMPAIGN_ID  = c.CAMPAIGN_ID \n" +
            "where ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1)", nativeQuery = true)
    List<Map<String, Object>> findAllRoutes(String partyId);
}
