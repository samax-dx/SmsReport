package com.telcobright.SmsReport.Admin.controller;

import com.telcobright.SmsReport.Admin.repositories.CampaignReportRepositoryAdmin;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
            value = "/campaignRouteAndPartyWiseReports",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Object> campaignRouteAndPartyWiseReport(
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

        List<Object[]> report = new ArrayList<>();
        Integer size = 0;
        List<Map<String, Object>> result = new ArrayList<>();
        if (campaignId == " " && routeId == " " && partyId == " ") {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit, offset);
                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            } else {
//                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0); // Default start date: January 1, 1970 00:00:00
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit, offset);
                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            }

            for (Object[] row : report) {
                String currentPartyId = (row[0] != null) ? row[0].toString() : "null";
                String currentCampaignId = (row[1] != null) ? row[1].toString() : "null";
                String currentRouteId = (row[2] != null) ? row[2].toString() : "null";
                int total = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int delivered = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int inProcess = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int failed = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int sent = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;
                int unidentifiedSubscriber = (row[8] != null) ? Integer.parseInt(row[8].toString()) : 0;
                int absentSubscriberSM = (row[9] != null) ? Integer.parseInt(row[9].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("partyId", currentPartyId);
                reports.put("campaignId", currentCampaignId);
                reports.put("routeId", currentRouteId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }
        else if (campaignId != " " && routeId != " " && partyId != " ") {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit, offset);
//                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId,partyId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.campaignRouteAndPartyWiseReports(campaignId, routeId, partyId, createdStartTime, createdEndTime, limit, offset);
//                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            }

            for (Object[] row : report) {
                String currentPartyId = (row[0] != null) ? row[0].toString() : "null";
                String currentCampaignId = (row[1] != null) ? row[1].toString() : "null";
                String currentRouteId = (row[2] != null) ? row[2].toString() : "null";
                int total = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int delivered = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int inProcess = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int failed = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int sent = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;
                int unidentifiedSubscriber = (row[8] != null) ? Integer.parseInt(row[8].toString()) : 0;
                int absentSubscriberSM = (row[9] != null) ? Integer.parseInt(row[9].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("partyId", currentPartyId);
                reports.put("campaignId", currentCampaignId);
                reports.put("routeId", currentRouteId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }
        else if (campaignId != null && routeId.equals(" ") && partyId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.campaignWise(campaignId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.campaignWise(campaignId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            }

            for (Object[] row : report) {
                String currentCampaignId = (row[0] != null) ? row[0].toString() : "null";
                int total = (row[1] != null) ? Integer.parseInt(row[1].toString()) : 0;
                int delivered = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int inProcess = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int failed = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int sent = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int unidentifiedSubscriber = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int absentSubscriberSM = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("campaignId", currentCampaignId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        } else if (routeId != null && campaignId.equals(" ") && partyId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.routeWise(routeId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllRoute(routeId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.routeWise(routeId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllRoute(routeId);
            }

            for (Object[] row : report) {
                String currentRouteId = (row[0] != null) ? row[0].toString() : "null";
                int total = (row[1] != null) ? Integer.parseInt(row[1].toString()) : 0;
                int delivered = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int inProcess = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int failed = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int sent = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int unidentifiedSubscriber = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int absentSubscriberSM = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("routeId", currentRouteId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }else if (partyId != null && campaignId.equals(" ") && routeId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.partyWise(partyId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllParty(partyId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.partyWise(partyId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllParty(partyId);
            }

            for (Object[] row : report) {
                String currentPartyId = (row[0] != null) ? row[0].toString() : "null";
                int total = (row[1] != null) ? Integer.parseInt(row[1].toString()) : 0;
                int delivered = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int inProcess = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int failed = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int sent = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int unidentifiedSubscriber = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int absentSubscriberSM = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("partyId", currentPartyId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }else if (campaignId != null && routeId != null && partyId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.campaignAndRouteWiseReports(campaignId,routeId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countCampaignWithRoute(campaignId, routeId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.campaignAndRouteWiseReports(campaignId,routeId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countCampaignWithRoute(campaignId, routeId);
            }

            for (Object[] row : report) {
                String currentCampaignId = (row[0] != null) ? row[0].toString() : "null";
                String currentRouteId = (row[1] != null) ? row[1].toString() : "null";
                int total = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int delivered = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int inProcess = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int failed = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int sent = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int unidentifiedSubscriber = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;
                int absentSubscriberSM = (row[8] != null) ? Integer.parseInt(row[8].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("campaignId", currentCampaignId);
                reports.put("routeId", currentRouteId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }else if (campaignId != null && partyId != null && routeId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.campaignAndPartyWiseReports(campaignId,partyId, createdStartTime, createdEndTime);
                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.campaignAndPartyWiseReports(campaignId,partyId, createdStartTime, createdEndTime);
                size = campaignReportRepositoryAdmin.countAllCampaign(campaignId, partyId);
            }

            for (Object[] row : report) {
                String  currentPartyId= (row[0] != null) ? row[0].toString() : "null";
                String currentCampaignId = (row[1] != null) ? row[1].toString() : "null";
                int total = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int delivered = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int inProcess = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int failed = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int sent = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int unidentifiedSubscriber = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;
                int absentSubscriberSM = (row[8] != null) ? Integer.parseInt(row[8].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("partyId", currentPartyId);
                reports.put("campaignId", currentCampaignId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }else if (partyId != null && routeId != null && campaignId.equals(" ")) {
            if (createdStartTime != null && createdEndTime != null) {
                report = campaignReportRepositoryAdmin.routeAndPartyWiseReports(routeId,partyId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllParty(partyId);
            } else {
                createdStartTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
                createdEndTime = LocalDateTime.now();
                report = campaignReportRepositoryAdmin.routeAndPartyWiseReports(routeId,partyId, createdStartTime, createdEndTime);
//                size = campaignReportRepositoryAdmin.countAllParty(partyId);
            }

            for (Object[] row : report) {
                String  currentPartyId= (row[0] != null) ? row[0].toString() : "null";
                String currentRouteId = (row[1] != null) ? row[1].toString() : "null";
                int total = (row[2] != null) ? Integer.parseInt(row[2].toString()) : 0;
                int delivered = (row[3] != null) ? Integer.parseInt(row[3].toString()) : 0;
                int inProcess = (row[4] != null) ? Integer.parseInt(row[4].toString()) : 0;
                int failed = (row[5] != null) ? Integer.parseInt(row[5].toString()) : 0;
                int sent = (row[6] != null) ? Integer.parseInt(row[6].toString()) : 0;
                int unidentifiedSubscriber = (row[7] != null) ? Integer.parseInt(row[7].toString()) : 0;
                int absentSubscriberSM = (row[8] != null) ? Integer.parseInt(row[8].toString()) : 0;

                Map<String, Object> reports = new LinkedHashMap<>();
                reports.put("partyId", currentPartyId);
                reports.put("routeId", currentRouteId);
                reports.put("total", total);
                reports.put("sent", sent);
                reports.put("delivered", delivered);
                reports.put("inProcess", inProcess);
//                reports.put("failed", failed);
                reports.put("unidentifiedSubscriber", unidentifiedSubscriber);
                reports.put("absentSubscriberSM", absentSubscriberSM);

                result.add(reports);
            }
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", size);
        responseMap.put("reports", result);


// Return the JSON response
        return responseMap;
//        return result;
    }
}

