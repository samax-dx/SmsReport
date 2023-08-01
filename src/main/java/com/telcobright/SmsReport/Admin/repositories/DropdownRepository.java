package com.telcobright.SmsReport.Admin.repositories;

import com.telcobright.SmsReport.Models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface DropdownRepository extends JpaRepository<Campaign, Long> {

    @Query("select cp from campaign cp")
    List<Campaign> findAll();

    @Query(value = "select * from route", nativeQuery = true)
    List<Map<String, Object>> findAllRoutes();

    @Query(value = "select * from party_group", nativeQuery = true)
    List<Map<String, Object>> findAllParty();

//    @Query(value = "select *, (select count(*) from campaign) as totalCount from campaign", nativeQuery = true)
//    Iterable<Object[]> findAllCampaignsWithCount();

}
