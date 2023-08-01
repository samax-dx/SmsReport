package com.telcobright.SmsReport.Client.controller;

import com.telcobright.SmsReport.Client.repositories.CampaignReportRepository;
import com.telcobright.SmsReport.Models.CampaignTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.content.text.Generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/reports")
public class CampaignReportController {

    final CampaignReportRepository campaignReportRepository;

    public CampaignReportController(CampaignReportRepository campaignReportRepository) {
        this.campaignReportRepository = campaignReportRepository;
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

        List<Object[]> report = campaignReportRepository.routeWise(routeId);

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
            value = "/campaignWise",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Map<String, String>> campaignWiseReports(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String campaignId = ( payload.get("campaignId") != null ? (String) payload.get("campaignId"): " ");
        Integer page = (int) payload.getOrDefault("page", 1) - 1;
        Integer limit = (int) payload.getOrDefault("limit", 10);

        List<Object[]> report = campaignReportRepository.campaignWiseReports(campaignId);

        Map<String, Map<String, String>> result = new HashMap<>();

        for (Object[] row : report) {
            String campaignIds = (row[0] != null) ? row[0].toString() : "null";
            String total = String.valueOf(row[1]);
            String sent = String.valueOf(row[2]);
            String delivered = String.valueOf(row[3]);
            String inProcess = String.valueOf(row[4]);
            String absentSubscriberSM = String.valueOf(row[5]);
            String unidentifiedSubscriber = String.valueOf(row[6]);

            Map<String, String> campaignData = new HashMap<>();
            campaignData.put("total", total);
            campaignData.put("sent", sent);
            campaignData.put("delivered", delivered);
            campaignData.put("inProcess", inProcess);
            campaignData.put("absentSubscriberSM", absentSubscriberSM);
            campaignData.put("unidentifiedSubscriber", unidentifiedSubscriber);

            result.put("report",campaignData);
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

        List<Object[]> report = campaignReportRepository.campaignAndRouteWiseReports(campaignId, routeId);

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

        List<Object[]> report = campaignReportRepository.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId);

//    Map<String, Map<String, Map<String, Map<String, Integer>>>> result = new HashMap<>();
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

