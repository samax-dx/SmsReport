package com.telcobright.smsreport.controller;

import com.telcobright.smsreport.repositories.DashBoardRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/DashBoard")
public class DashBoardController {
    final DashBoardRepository dashBoardRepository;

    public DashBoardController(DashBoardRepository dashBoardRepository) {
        this.dashBoardRepository = dashBoardRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/campaignCount",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object campaignCount(@RequestBody Map<String, Object> payload) {
        return dashBoardRepository.campaignTaskCount();
    }

    @RequestMapping(
            value = "/campaignSuccess",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object campaignSuccess(@RequestBody Map<String, Object> payload) {
        return 80;
    }

    @RequestMapping(
            value = "/campaignFailed",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object campaignFailed(@RequestBody Map<String, Object> payload) {
        return 18;
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
