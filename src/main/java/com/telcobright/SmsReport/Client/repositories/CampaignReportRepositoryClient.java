package com.telcobright.SmsReport.Client.repositories;

import com.telcobright.SmsReport.Models.CampaignTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignReportRepositoryClient extends JpaRepository<CampaignTask,Long> {
    @Query(value = "select campaign_id, route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent, SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id, count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct\n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tgroup by ct.campaign_id,route_id #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.campaign_id,route_id #DELIVERED\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is null \n" +
            "\t\tor STATUS_EXTERNAL != 'delivered')\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.campaign_id,route_id  #inprocess\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
//            "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
            "\t\tand (STATUS ='failed')\n" +
            "\t\tgroup by ct.campaign_id, route_id #failed \n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID,route_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,count(*)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tand (STATUS ='sent')\n" +
            "\t\tgroup by ct.campaign_id, route_id #sent\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
            "\t\tgroup by ct.campaign_id,route_id #unidentifiedSubscriber\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID,route_id, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3)\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
            "\t\tgroup by ct.campaign_id, route_id #absentSubscriberSM\n" +
            "\t) t\n" +
            "\tgroup by campaign_id, route_id limit ?6 offset ?7",
            nativeQuery = true)

    List<Object[]> campaignRouteAndPartyWiseReports(String campaignId, String routeId, String partyId, LocalDateTime createdStartTime, LocalDateTime createdEndTime, Integer limit, Integer offset);


@Query(value = "select campaign_id , SUM(total) as total,SUM(sent) as sent,SUM(delivered)" +
            " as delivered, SUM(inProcess) as inProcess," +
            " SUM(absentSubscriberSM) as absentSubscriberSM," + " SUM(unidentifiedSubscriber) as unidentifiedSubscriber\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect campaign_id , count(*)  as total, (select 0) as sent, (select 0) as absentSubscriberSM,(select 0) as unidentifiedSubscriber, (select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tgroup by ct.campaign_id #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total, count(*)  as sent, (select 0) as absentSubscriberSM,(select 0) as unidentifiedSubscriber, (select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.STATUS='sent'\n"+
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tgroup by ct.campaign_id #SENT\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total, (select 0) as sent, (select 0) as absentSubscriberSM,(select 0) as unidentifiedSubscriber, count(*) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.campaign_id #DELIVERED\n" +

            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, (select 0) as absentSubscriberSM,(select 0) as unidentifiedSubscriber, (select 0) as delivered, count(*) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand STATUS_EXTERNAL is null\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.campaign_id #inprocess\n" +
            "\t\tunion all\n" +

            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, (select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber,(select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
            "\t\tgroup by ct.campaign_id #unidentifiedSubscriber\n" +
            "\t\tunion all\n" +
            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as delivered,(select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
            "\t\tgroup by ct.campaign_id #absentSubscriberSM\n" +
            "\t) t\n" +
            "\tgroup by campaign_id;",
            nativeQuery = true)
List<Object[]> campaignWiseReports(String campaignId);

    @Query(value = "select CAMPAIGN_ID , SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent,SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect ct.CAMPAIGN_ID , count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #DELIVERED\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #inprocess\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #failed\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand (STATUS ='sent')\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #sent\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2)\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #unidentifiedSubscriber\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2)\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
            "\t\tgroup by ct.CAMPAIGN_ID #absentSubscriberSM\n" +
            "\t) t\n" +
            "\tgroup by CAMPAIGN_ID;",
            nativeQuery = true)
    List<Object[]> campaignWise(String campaignId, String partyId, LocalDateTime createdStartTime, LocalDateTime createdEndTime);
    @Query(value = "select ROUTE_ID , SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent, SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect ct.ROUTE_ID , count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tgroup by ct.ROUTE_ID #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.ROUTE_ID #DELIVERED\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.ROUTE_ID #inprocess\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
            "\t\tgroup by ct.ROUTE_ID #failed\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand (ct.STATUS ='sent')\n" +
            "\t\tgroup by ct.ROUTE_ID #sent\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
            "\t\tgroup by ct.ROUTE_ID #unidentifiedSubscriber\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.ROUTE_ID, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
            "\t\tand (ct.route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 )\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
            "\t\tgroup by ct.ROUTE_ID #absentSubscriberSM\n" +
            "\t) t\n" +
            "\tgroup by route_id;",
            nativeQuery = true)
    List<Object[]> routeWise(String routeId, String partyId, LocalDateTime createdStartTime, LocalDateTime createdEndTime);

    @Query(value = "select count(*) from campaign c where (c.campaign_id = ?1 or ?1 = ' ' or ?1 = null) and (c.party_id = ?2 or ?2 = ' ' or ?2 = null)", nativeQuery = true)
    Integer countAllCampaign(String campaignId, String partyId);

}
