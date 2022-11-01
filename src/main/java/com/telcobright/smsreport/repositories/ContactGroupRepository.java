package com.telcobright.smsreport.repositories;

import com.telcobright.smsreport.models.ContactGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ContactGroupRepository extends CrudRepository<ContactGroup, Integer> {
    @Query("select cg from contact_book_group cg where cg.groupName like %?1%")
    Page<ContactGroup> findContactGroupsByName(String name, Pageable pageable);

    @Query("select cg from contact_book_group cg where cg.groupId = ?1")
    Optional<ContactGroup> findGroupById(long groupId);
}
