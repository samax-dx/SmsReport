package com.telcobright.SmsReport.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "party")
public class Party {
    @Id
    @Column(name = "party_id")
    public String partyId;

    @Column(name = "party_type_id")
    public String partyTypeId;

    @Column(name = "external_id")
    public String externalId;

    @Column(name = "preferred_currency_uom_id")
    public String preferredCurrencyUomId;

    @Column(name = "description")
    public String description;

    @Column(name = "status_id")
    public String statusId;

    @Column(name = "created_date")
    public LocalDateTime createdDate;

    @Column(name = "created_by_user_login")
    public String createdByUserLogin;

    @Column(name = "last_modified_date")
    public LocalDateTime lastModifiedDate;

    @Column(name = "last_modified_by_user_login")
    public String lastModifiedByUserLogin;

    @Column(name = "date_source_id")
    public String pendingTaskCount;

    @Column(name = "is_unread")
    public String isUnread;

    @Column(name = "last_updated_stamp")
    public LocalDateTime lastUpdatedStamp;

    @Column(name = "last_updated_tx_stamp")
    public LocalDateTime lastUpdatedTxStamp;

    @Column(name = "created_stamp")
    public LocalDateTime createdStamp;

    @Column(name = "created_tx_stamp")
    public LocalDateTime createdTxStamp;


}
