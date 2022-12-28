package com.telcobright.SmsReport.Client.controller;

import com.telcobright.SmsReport.Client.Util.AuthToken;
import com.telcobright.SmsReport.Models.ContactGroup;
import com.telcobright.SmsReport.Client.repositories.ContactGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ContactGroup")
public class ContactGroupController {
    final ContactGroupRepository contactGroupRepository;

    public ContactGroupController(ContactGroupRepository contactGroupRepository) {
        this.contactGroupRepository = contactGroupRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/listContactGroups",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Object> listContactGroups(@RequestBody Map<String, Object> payload) {
        String groupName = (String) payload.getOrDefault("groupName", "");
        int page = (int) payload.getOrDefault("page", 1) - 1;
        int limit = (int) payload.getOrDefault("limit", 10);

        Page<ContactGroup> groups = contactGroupRepository.findContactGroupsByName(groupName, PageRequest.of(page, limit));

        Map<String, Object> response = new HashMap<>();
        response.put("groups", groups.toList());
        response.put("count", groups.getTotalElements());
        return response;
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/saveContactGroup",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )

    @Transactional
    public ContactGroup createContactGroup(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        ContactGroup contactGroup = new ContactGroup();
        contactGroup.groupName = (String) payload.get("groupName");
        contactGroup.partyId = AuthToken.findPartyId(token);
        return contactGroupRepository.save(contactGroup);
    }
}
