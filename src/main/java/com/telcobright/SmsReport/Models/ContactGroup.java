package com.telcobright.SmsReport.Models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name="contact_book_group")
public class ContactGroup {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "group_id")
    public long groupId;

    @Column(name="party_id")
    public String partyId;

    @Column(name="group_name")
    public String groupName;
}
