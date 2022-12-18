package com.telcobright.smsreport.controller;

import com.telcobright.smsreport.repositories.DashBoardRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/DashBoard")
public class DashBoardController {
    final DashBoardRepository dashBoardRepository;

    public DashBoardController(DashBoardRepository dashBoardRepository) {
        this.dashBoardRepository = dashBoardRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekCampaignCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object weekCampaignCount(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(12,0,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try {
            int weekCampaignCount = dashBoardRepository.weekCampaignCountById(partyId,weekStartDate,weekEndDate);
            return weekCampaignCount;
        }
        catch (Exception e){
            return e;
        }

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/todayCampaignCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todayCampaignCount(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try {
            int todayCampaignCount = dashBoardRepository.todaysCampaignCountById(partyId,startDate,endDate);
            return todayCampaignCount;
        }
        catch (Exception e){
            return e;
        }

    }
    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/todayCampaignSuccess",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todaycampaignSuccess(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try{
            int todaysTotalSuccessCount = dashBoardRepository.todaysTotalSuccessCountById(partyId,startDate,endDate);
            return todaysTotalSuccessCount;
        }
        catch (Exception e){
            return e;
        }
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekCampaignSuccess",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )

    public Object weekcampaignSuccess(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(12,0,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{
            int weekTotalSuccessCount = dashBoardRepository.weekTotalSuccessCountById(partyId,weekStartDate,weekEndDate);
            return weekTotalSuccessCount;
        }
        catch (Exception e){
            return e;
        }
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/todayTotalTaskCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todayTotalTaskCount(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try{

            int todaysTotalTaskCount = dashBoardRepository.todaysTotalTaskCountById(partyId,startDate,endDate);
            return todaysTotalTaskCount;
        }
        catch (Exception e){
            return e;
        }
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekTotalTaskCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object weekTotalTaskCount(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(12,0,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{

            int weekTotalTaskCount = dashBoardRepository.weekTotalTaskCountById(partyId,weekStartDate,weekEndDate);
            return weekTotalTaskCount;
        }
        catch (Exception e){
            return e;
        }
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/routeDetails",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object routeDetails(@RequestBody Map<String, Object> payload) {
        String partyId = payload.get("partyId").toString();
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
       try {
           List<Object[]> routeDetails = dashBoardRepository.routeDetails(partyId,startDate,endDate);
           List<Map<String, Long>> records = routeDetails.stream().map(row->{
               Map<String,Long> rowAsMap= new HashMap<>();
               rowAsMap.put(row[0]!=null?row[0].toString().toLowerCase():"null", (Long) row[1]);
               return rowAsMap;
           }).collect(Collectors.toList());
         return records;
       }
       catch (Exception e){
           return e;
       }
    }

    @RequestMapping(
            value = "/smsCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object smsCount(@RequestBody Map<String, Object> payload) {
        return 100;
    }

    @RequestMapping(
            value = "/smsSentCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object smsSentCount(@RequestBody Map<String, Object> payload) {
        return 75;
    }

    @RequestMapping(
            value = "/smsFailedCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object smsFailedCount(@RequestBody Map<String, Object> payload) {
        return 25;
    }



}
