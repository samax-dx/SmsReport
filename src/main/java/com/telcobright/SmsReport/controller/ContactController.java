package com.telcobright.SmsReport.controller;

import com.telcobright.SmsReport.Util.AuthToken;
import com.telcobright.SmsReport.models.Contact;
import com.telcobright.SmsReport.repositories.ContactRepository;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/Contact")
public class ContactController {
    final ContactRepository contactRepository;

    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/listContacts",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Map<String, Object> listContacts(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String contactName = (String) payload.getOrDefault("contactName", "");
        String contactNumber = (String) payload.getOrDefault("contactNumber", "");
        String partyId = AuthToken.findPartyId(token);
        int page = (int) payload.getOrDefault("page", 1) - 1;
        int limit = (int) payload.getOrDefault("limit", 10);

        Page<Contact> contacts = contactRepository.searchContacts(partyId, contactName, contactNumber, PageRequest.of(page, limit));

        Map<String, Object> response = new HashMap<>();
        response.put("contacts", contacts.toList());
        response.put("count", contacts.getTotalElements());
        return response;
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/saveContact",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    @Transactional
    public Contact saveContact(@RequestBody Map<String, Object> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Contact contact = new Contact();
        contact.contactName = (String) payload.get("contactName");
        contact.contactNumber = (String) payload.get("contactNumber");
        contact.groupId = Long.decode(payload.get("groupId").toString());
        contact.partyId = AuthToken.findPartyId(token);
        return contactRepository.save(contact);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/saveContacts",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    @Transactional
    public List<Contact> saveContacts(@RequestBody List<Map<String, Object>> payload, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<Contact> contacts = payload.stream()
                .map(c -> {
                    Contact contact = new Contact();
                    contact.contactName = (String) c.get("contactName");
                    contact.contactNumber = (String) c.get("contactNumber");
                    contact.groupId = Long.decode(c.get("groupId").toString());
                    contact.partyId = AuthToken.findPartyId(token);
                    return contact;
                })
                .collect(Collectors.toList());

        return StreamSupport.stream(contactRepository.saveAll(contacts).spliterator(), false)
                .collect(Collectors.toList());
    }
}

