package com.telcobright.SmsReport.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Entity(name = "campaign")
//@IdClass(CampaignTaskKey.class)
public class Campaign {
    @Id
    @Column(name = "campaign_id")
    public String campaignId;

    @Column(name = "campaign_name")
    public String campaignName;

    @Column(name = "sender_id")
    public String senderId;

    @Column(name = "sms_count")
    public Integer smsCount;

    @Column(name = "sms_encoding")
    public String smsEncoding;

    @Column(name = "is_unicode")
    public String isUnicode;

    @Column(name = "is_flash")
    public String isFlash;

    @Column(name = "party_id")
    public String partyId;

    @Column(name = "policy")
    public String policy;

    @Column(name = "sent_task_count")
    public Integer sentTaskCount;

    @Column(name = "failed_task_count")
    public Integer failedTaskCount;

    @Column(name = "pending_task_count")
    public Integer pendingTaskCount;

    @Column(name = "total_task_count")
    public Integer totalTaskCount;

    @Column(name = "last_updated_stamp")
    public LocalDateTime lastUpdatedStamp;

    @Column(name = "last_updated_tx_stamp")
    public LocalDateTime lastUpdatedTxStamp;

    @Column(name = "created_stamp")
    public LocalDateTime createdStamp;

    @Column(name = "created_tx_stamp")
    public LocalDateTime createdTxStamp;

    @Column(name = "sms_type")
    public String smsType;

    @Column(name = "schedule_status")
    public String scheduleStatus;

    @Column(name = "schedules")
    public String schedules;


}
