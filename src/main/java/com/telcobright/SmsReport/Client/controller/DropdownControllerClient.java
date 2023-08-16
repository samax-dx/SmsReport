package com.telcobright.SmsReport.Client.controller;

import com.telcobright.SmsReport.Client.repositories.DropdownRepositoryClient;
import com.telcobright.SmsReport.Models.Campaign;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client/Dropdown")
public class DropdownControllerClient {

    final DropdownRepositoryClient dropdownRepositoryClient;

    public DropdownControllerClient(DropdownRepositoryClient dropdownRepositoryClient) {
        this.dropdownRepositoryClient = dropdownRepositoryClient;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/AllClientCampaignForDropdown",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Campaign> AllClientCampaignForDropdown(@RequestBody Map<String , Object> payload) throws Exception{
        String partyId = (String) payload.get("partyId");

        List<Campaign> allCampaign = dropdownRepositoryClient.findAllCampaign(partyId);
        return allCampaign;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/AllClientRoutesForDropdown",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public List<Map<String, Object>> AllRoutesForDropdown(@RequestBody Map<String, Object> payload) throws Exception {
        String partyId = (String) payload.get("partyId");

        List<Map<String, Object>> allRoutes = dropdownRepositoryClient.findAllRoutes(partyId);
        allRoutes.removeIf(route -> route == null || route.values().contains(null));
        return allRoutes;
    }
}
