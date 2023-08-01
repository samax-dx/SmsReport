package com.telcobright.SmsReport.Admin.controller;

import com.telcobright.SmsReport.Admin.repositories.DropdownRepository;
import com.telcobright.SmsReport.Models.Campaign;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/Dropdown")
public class DropdownController {

    final DropdownRepository dropdownRepository;

    public DropdownController(DropdownRepository dropdownRepository) {
        this.dropdownRepository = dropdownRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/AllCampaignForDropdown",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Campaign> AllCampaignForDropdown(@RequestBody Map<String , Object> payload) throws Exception{
        List<Campaign> allCampaign = dropdownRepository.findAll();
        return allCampaign;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/AllRoutesForDropdown",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Map<String, Object>> AllRoutesForDropdown(@RequestBody Map<String, Object> payload) throws Exception {
        List<Map<String, Object>> allRoutes = dropdownRepository.findAllRoutes();
        return allRoutes;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/AllPartyForDropdown",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Map<String, Object>> AllPartyForDropdown(@RequestBody Map<String, Object> payload) throws Exception {
        List<Map<String, Object>> allParty = dropdownRepository.findAllParty();
        return allParty;
    }
//    public Map<String, Object> getAllCampaignsWithCountFormatted() {
//        Iterable<Object[]> result = smsRouteRepository.findAllCampaignsWithCount();
//        Map<String, Object> formattedResult = new LinkedHashMap<>();
//        List<Map<String, Object>> campaigns = new ArrayList<>();
//
//        for (Object[] row : result) {
//            Map<String, Object> campaignData = new LinkedHashMap<>();
//
//            // Extract column values and map them to the desired keys
//            campaignData.put("campaignId", row[0]);
//            campaignData.put("campaignName", row[1]);
//            campaignData.put("createdStamp", row[2]);
//            campaignData.put("createdTxStamp", row[3]);
//            campaignData.put("failedTaskCount", row[4]);
//            campaignData.put("isFlash", row[5]);
//            campaignData.put("isUnicode", row[6]);
//            campaignData.put("lastUpdatedStamp", row[7]);
//            campaignData.put("lastUpdatedTxStamp", row[8]);
//            campaignData.put("partyId", row[9]);
//            campaignData.put("pendingTaskCount", row[10]);
//            campaignData.put("policy", row[11]);
//            campaignData.put("scheduleStatus", row[12]);
//            campaignData.put("schedules", row[13]);
//            campaignData.put("senderId", row[14]);
//            campaignData.put("sentTaskCount", row[15]);
//            campaignData.put("smsCount", row[16]);
//            campaignData.put("smsEncoding", row[17]);
//            campaignData.put("smsType", row[18]);
//            campaignData.put("totalTaskCount", row[19]);
//
//            campaigns.add(campaignData);
//        }
//
//        formattedResult.put("campaigns", campaigns);
//        formattedResult.put("totalCount", campaigns.size());
//
//        return formattedResult;
//    }
}

