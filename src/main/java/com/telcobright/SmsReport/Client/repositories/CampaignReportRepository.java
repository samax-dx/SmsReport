package com.telcobright.SmsReport.Client.repositories;

import com.telcobright.SmsReport.Models.CampaignTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignReportRepository extends JpaRepository<CampaignTask,Long> {
    @Query(value = "select campaign_id , SUM(total) as total,SUM(sent) as sent,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect campaign_id , count(*)  as total, (select 0) as sent, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tgroup by ct.campaign_id #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total, count(*)  as sent, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.STATUS='sent'\n"+
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tgroup by ct.campaign_id #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total, (select 0) as sent, (select 0) as failed, count(*) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.campaign_id #DELIVERED\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, (select 0) as failed, (select 0) as delivered, count(*) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.campaign_id #inprocess\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, count(*) as failed, (select 0) as delivered,(select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and STATUS_EXTERNAL !='delivered' )\n" +
            "\t\tgroup by ct.campaign_id #failed\n" +
            "\t) t\n" +
            "\tgroup by campaign_id;",
            nativeQuery = true)
    List<Object[]> campaignWiseReports(String campaignId);
}
