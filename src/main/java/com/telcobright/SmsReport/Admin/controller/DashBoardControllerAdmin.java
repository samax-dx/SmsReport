package com.telcobright.SmsReport.Admin.controller;

import com.telcobright.SmsReport.Admin.repositories.DashBoardRepositoryAdmin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/DashBoard")
public class DashBoardControllerAdmin {
    final DashBoardRepositoryAdmin dashBoardRepository;

    public DashBoardControllerAdmin(DashBoardRepositoryAdmin dashBoardRepository) {
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
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try {
            int weekCampaignCount = dashBoardRepository.weekCampaignCountByPartyId(partyId,weekStartDate,weekEndDate);
            return weekCampaignCount;
        }
        catch (Exception e){
            return e;
        }

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekCampaignCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object weekCampaignCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try {
            int weekCampaignCountAdmin = dashBoardRepository.weekCampaignCount(weekStartDate,weekEndDate);
            return weekCampaignCountAdmin;
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
            value = "/todayCampaignCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todayCampaignCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try {
            int todayCampaignCountAdmin = dashBoardRepository.todaysCampaignCount(startDate,endDate);
            return todayCampaignCountAdmin;
        }
        catch (Exception e){
            return e;
        }

    }


    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/rtCampaignCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object rtCampaignCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime rtStart = LocalDateTime.now();
        LocalDateTime rtEnd = LocalDateTime.now().minusMinutes(20);
        try {
            int rtCampaignCountAdmin = dashBoardRepository.rtCampaignCount(rtStart,rtEnd);
            return rtCampaignCountAdmin;
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
            int todaysTotalSuccessCount = dashBoardRepository.todaysTotalSuccessCountByPartyId(partyId,startDate,endDate);
            return todaysTotalSuccessCount;
        }
        catch (Exception e){
            return e;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/todayCampaignSuccessAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todaycampaignSuccessAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try{
            int todaysTotalSuccessCountAdmin = dashBoardRepository.todaysTotalSuccessCount(startDate,endDate);
            return todaysTotalSuccessCountAdmin;
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
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{
            int weekTotalSuccessCount = dashBoardRepository.weekTotalSuccessCountByPartyId(partyId,weekStartDate,weekEndDate);
            return weekTotalSuccessCount;
        }
        catch (Exception e){
            return e;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekCampaignSuccessAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )

    public Object weekcampaignSuccessAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{
            int weekTotalSuccessCountAdmin = dashBoardRepository.weekTotalSuccessCount(weekStartDate,weekEndDate);
            return weekTotalSuccessCountAdmin;
        }
        catch (Exception e){
            return e;
        }
    }



    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/rtCampaignSuccessAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )

    public Object rtcampaignSuccessAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime rtStart = LocalDateTime.now();
        LocalDateTime rtEnd = LocalDateTime.now().minusMinutes(20);
        try{
            int rtTotalSuccessCountAdmin = dashBoardRepository.rtTotalSuccessCount(rtStart,rtEnd);
            return rtTotalSuccessCountAdmin;
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

            int todaysTotalTaskCount = dashBoardRepository.todaysTotalTaskCountByPartyId(partyId,startDate,endDate);
            return todaysTotalTaskCount;
        }
        catch (Exception e){
            return e;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/todayTotalTaskCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object todayTotalTaskCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try{

            int todayTotalTaskCountAdmin = dashBoardRepository.todaysTotalTaskCount(startDate,endDate);
            return todayTotalTaskCountAdmin;
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
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{

            int weekTotalTaskCount = dashBoardRepository.weekTotalTaskCountByPartyId(partyId,weekStartDate,weekEndDate);
            return weekTotalTaskCount;
        }
        catch (Exception e){
            return e;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/weekTotalTaskCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object weekTotalTaskCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime weekStartDate = LocalDateTime.now().with(LocalTime.of(23,59,0));
        LocalDateTime weekEndDate = LocalDateTime.now().minusDays(7).with(LocalTime.of(0,0,0));
        try{

            int weekTotalTaskCountAdmin = dashBoardRepository.weekTotalTaskCount(weekStartDate,weekEndDate);
            return weekTotalTaskCountAdmin;
        }
        catch (Exception e){
            return e;
        }
    }



    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/rtTotalTaskCountAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object rtTotalTaskCountAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime rtStart = LocalDateTime.now();
        LocalDateTime rtEnd = LocalDateTime.now().minusMinutes(20);
        try{

            int rtTotalTaskCountAdmin = dashBoardRepository.rtTotalTaskCount(rtStart,rtEnd);
            return rtTotalTaskCountAdmin;
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
           List<Object[]> routeDetails = dashBoardRepository.routeDetailsByPartyId(partyId,startDate,endDate);
           List<Map<String, Long>> records = routeDetails.stream().map(row->{
               Map<String,Long> rowAsMap= new HashMap<>();
               rowAsMap.put(row[0]!=null?row[0].toString():"null", (Long) row[1]);
               return rowAsMap;
           }).collect(Collectors.toList());
         return records;
       }
       catch (Exception e){
           return e;
       }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/routeDetailsAdmin",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object routeDetailsAdmin(@RequestBody Map<String, Object> payload) {
        LocalDateTime startDate = LocalDateTime.now().with(LocalTime.of(0,0,0));
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).with(LocalTime.of(0,0,0));
        try {
            List<Object[]> routeDetails = dashBoardRepository.routeDetails(startDate,endDate);
            List<Map<String, Long>> records = routeDetails.stream().map(row->{
                Map<String,Long> rowAsMap= new HashMap<>();
                rowAsMap.put(row[0]!=null?row[0].toString():"null", (Long) row[1]);
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

    @RequestMapping(
            value = "/routesDetails",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object routesDetails(@RequestBody Map<String, Object> payload) {
        return 5;
    }

}
