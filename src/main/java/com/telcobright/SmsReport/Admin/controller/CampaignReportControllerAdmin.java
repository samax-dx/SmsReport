package com.telcobright.SmsReport.Admin.controller;

import com.telcobright.SmsReport.Admin.repositories.CampaignReportRepositoryAdmin;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin/reports")
public class CampaignReportControllerAdmin {

    final CampaignReportRepositoryAdmin campaignReportRepositoryAdmin;

    public CampaignReportControllerAdmin(CampaignReportRepositoryAdmin campaignReportRepositoryAdmin) {
        this.campaignReportRepositoryAdmin = campaignReportRepositoryAdmin;
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/routeWise",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Map<String, Object>> routeWise(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String routeId = ( payload.get("routeId") != null ? (String) payload.get("routeId"): " ");
        Integer page = (int) payload.getOrDefault("page", 1) - 1;
        Integer limit = (int) payload.getOrDefault("limit", 10);

        List<Object[]> report = campaignReportRepositoryAdmin.routeWise(routeId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : report) {
            String currentRouteId = (row[0] != null) ? row[0].toString() : "null";
            String total = String.valueOf(row[1]);
            String delivered = String.valueOf(row[2]);
            String inProcess = String.valueOf(row[3]);
            String failed = String.valueOf(row[4]);

            Map<String, Object> routeData = new HashMap<>();
            routeData.put("routeId", currentRouteId);
            routeData.put("total", total);
            routeData.put("delivered", delivered);
            routeData.put("inProcess", inProcess);
            routeData.put("failed", failed);

            result.add(routeData);
        }

        return result;
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/campaignAndRouteWiseReports",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Map<String, Map<String, String>>> campaignAndRouteWiseReports(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        String campaignId = (payload.get("campaignId") != null ? (String) payload.get("campaignId") : " ");
        String routeId = (payload.get("routeId") != null ? (String) payload.get("routeId") : " ");
        Integer page = (int) payload.getOrDefault("page", 1) - 1;
        Integer limit = (int) payload.getOrDefault("limit", 10);

        List<Object[]> report = campaignReportRepositoryAdmin.campaignAndRouteWiseReports(campaignId, routeId);

        Map<String, Map<String, Map<String, String>>> result = new HashMap<>();

        for (Object[] row : report) {
            String campaignIds = (row[0] != null) ? row[0].toString() : "null";
            String routeIds = (row[1] != null) ? row[1].toString() : "null";
            String total = String.valueOf(row[2]);
            String delivered = String.valueOf(row[3]);
            String inProcess = String.valueOf(row[4]);
            String failed = String.valueOf(row[5]);

            Map<String, Map<String, String>> campaignData = result.getOrDefault(campaignIds, new HashMap<>());

            Map<String, String> routeData = new HashMap<>();
            routeData.put("total", total);
            routeData.put("delivered", delivered);
            routeData.put("inProcess", inProcess);
            routeData.put("failed", failed);

            campaignData.put(routeIds, routeData);
            result.put(campaignIds, campaignData);
        }

        return result;
    }

//@CrossOrigin(origins = "*")
//@RequestMapping(
//        value = "/campaignRouteAndPartyWiseReports",
//        method = RequestMethod.POST,
//        consumes = {"application/json"},
//        produces = {"application/json"}
//)
//public Map<String, Map<String, Map<String, Map<String, Integer>>>> campaignRouteAndPartyWiseReports(
//        @RequestBody Map<String, Object> payload,
//        @RequestHeader(HttpHeaders.AUTHORIZATION) String token
//) {
//    String campaignId = (payload.get("campaignId") != null ? (String) payload.get("campaignId") : " ");
//    String routeId = (payload.get("routeId") != null ? (String) payload.get("routeId") : " ");
//    String partyId = (payload.get("partyId") != null ? (String) payload.get("partyId") : " ");
//    Integer page = (int) payload.getOrDefault("page", 1) - 1;
//    Integer limit = (int) payload.getOrDefault("limit", 10);
//
//    List<Object[]> report = campaignReportRepository.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId);
//
//    Map<String, Map<String, Map<String, Map<String, Integer>>>> result = new HashMap<>();
//
//    for (Object[] row : report) {
//        String partyIdVal = (row[0] != null) ? row[0].toString() : "null";
//        String campaignIds = (row[1] != null) ? row[1].toString() : "null";
//        String routeIds = (row[2] != null) ? row[2].toString() : "null";
//        int total = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
//        int delivered = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
//        int inProcess = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
//        int failed = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
//
//        Map<String, Map<String, Map<String, Integer>>> partyData = result.getOrDefault(partyIdVal, new HashMap<>());
//
//        Map<String, Map<String, Integer>> campaignData = partyData.getOrDefault(campaignIds, new HashMap<>());
//
//        Map<String, Integer> routeData = new HashMap<>();
//        routeData.put("total", total);
//        routeData.put("inProcess", inProcess);
//        routeData.put("delivered", delivered);
//        routeData.put("failed", failed);
//
//        campaignData.put(routeIds, routeData);
//        partyData.put(campaignIds, campaignData);
//        result.put(partyIdVal, partyData);
//    }
//
//    return result;
//}

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/campaignRouteAndPartyWiseReports",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Map<String, Object>> campaignRouteAndPartyWiseReport(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        String campaignId = (payload.get("campaignId") != null ? (String) payload.get("campaignId") : " ");
        String routeId = (payload.get("routeId") != null ? (String) payload.get("routeId") : " ");
        String partyId = (payload.get("partyId") != null ? (String) payload.get("partyId") : " ");
        Integer page = (int) payload.getOrDefault("page", 1) - 1;
        Integer limit = (int) payload.getOrDefault("limit", 10);
        Integer offset = page * limit;

        String startDate = (String) payload.get("createdOn_fld0_value");
        String endDate = (String) payload.get("createdOn_fld1_value");
        LocalDateTime createdStartTime = null;
        LocalDateTime createdEndTime = null;

        if (startDate != null && endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            createdStartTime = LocalDateTime.parse(startDate, formatter);
            createdEndTime = LocalDateTime.parse(endDate, formatter);
        }

        List<Object[]> report;
        if (createdStartTime != null && createdEndTime != null) {
            report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit,offset);
        } else {
            createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0); // Default start date: January 1, 1970 00:00:00
            createdEndTime = LocalDateTime.now(); // Default end date: Current date and time
            report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit, offset);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : report) {
            String currentPartyId = (row[0] != null) ? row[0].toString() : "null";
            String currentCampaignId = (row[1] != null) ? row[1].toString() : "null";
            String currentRouteId = (row[2] != null) ? row[2].toString() : "null";
            int total = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
            int delivered = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
            int inProcess = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
            int failed = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
            int suspended = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;

            Map<String, Object> reports = new HashMap<>();
            reports.put("partyId",currentPartyId);
            reports.put("campaignId",currentCampaignId);
            reports.put("routeId",currentRouteId);
            reports.put("total", total);
            reports.put("inProcess", inProcess);
            reports.put("delivered", delivered);
            reports.put("failed", failed);
            reports.put("suspended", suspended);

            result.add(reports);

        }

        return result;
    }

}

