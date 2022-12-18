package com.telcobright.SmsReport.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "contact_book_contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    public Long contactId;

    @Column(name = "group_id")
    public long groupId;

    @Column(name = "party_id")
    public String partyId;

    @Column(name = "contact_name")
    public String contactName;

    @Column(name = "contact_number")
    public String contactNumber;

    public void contactName(String contactName) {
    }

    public void contactNumber(long contactNumber) {
    }

    public void partyId(long partyId) {
    }

    public void groupId(long groupId) {
    }
}
