package com.telcobright.SmsReport.Client.repositories;

import com.telcobright.SmsReport.Models.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ContactRepository extends CrudRepository<Contact, Integer> {
    @Query("select ct from contact_book_contact ct where ct.contactId = ?1")
    Optional<Contact> findGroupById(long contactId);

    @Query("select ct from contact_book_contact ct where ct.contactName like %?1%")
    Page<Contact> findContactsByName(String name, Pageable pageable);

    @Query("select ct from contact_book_contact ct where ct.partyId = ?1 and (ct.contactName like %?2% or ct.contactNumber like %?3%)")
    Page<Contact> searchContacts(String partyId, String contactName, String contactNumber, Pageable pageable);

    @Query("select ct from contact_book_contact ct where (ct.partyId = ?1 and ct.groupId = ?2) and (ct.contactName like %?3% or ct.contactNumber like %?4%)")
    Page<Contact> searchGroupContacts(String partyId, Long groupId, String contactName, String contactNumber, Pageable pageable);
}
