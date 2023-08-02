package com.telcobright.SmsReport.Client.controller;

import com.telcobright.SmsReport.Client.repositories.CampaignReportRepositoryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client/reports")
public class CampaignReportControllerClient {

    final CampaignReportRepositoryClient campaignReportRepository;

    public CampaignReportControllerClient(CampaignReportRepositoryClient campaignReportRepository) {
        this.campaignReportRepository = campaignReportRepository;
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

}

