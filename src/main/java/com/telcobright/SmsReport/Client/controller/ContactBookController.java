package com.telcobright.SmsReport.Client.controller;

import com.telcobright.SmsReport.Client.Util.AuthToken;
import com.telcobright.SmsReport.Models.Contact;
import com.telcobright.SmsReport.Models.ContactGroup;
import com.telcobright.SmsReport.Client.repositories.ContactGroupRepository;
import com.telcobright.SmsReport.Client.repositories.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/ContactBook")
public class ContactBookController {
    final ContactGroupRepository contactGroupRepository;
    final ContactRepository contactRepository;

    public ContactBookController(ContactGroupRepository contactGroupRepository, ContactRepository contactRepository) {
        this.contactGroupRepository = contactGroupRepository;
        this.contactRepository = contactRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/listContactGroupContacts",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Object> listContactGroupContacts(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long groupId = Long.decode(payload.getOrDefault("groupId", -1).toString());
        String contactName = (String) payload.getOrDefault("contactName", "");
        String contactNumber = (String) payload.getOrDefault("contactNumber", "");
        String partyId = AuthToken.findPartyId(token);
        int page = (int) payload.getOrDefault("page", 1) - 1;
        int limit = (int) payload.getOrDefault("limit", 10);

        ContactGroup group = contactGroupRepository.findGroupById(groupId).orElse(null);
        Page<Contact> contacts = contactRepository.searchGroupContacts(partyId, groupId, contactName, contactNumber, PageRequest.of(page, limit));

        Map<String, Object> response = new HashMap<>();
        response.put("group", group);
        response.put("contacts", contacts.toList());
        response.put("count", contacts.getTotalElements());
        return response;
    }

}
