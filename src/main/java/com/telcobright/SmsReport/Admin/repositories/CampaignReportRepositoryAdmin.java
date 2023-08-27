package com.telcobright.SmsReport.Admin.repositories;

import com.telcobright.SmsReport.Models.CampaignTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignReportRepositoryAdmin extends JpaRepository<CampaignTask,Long>{
        @Query(value = "select ROUTE_ID , SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent, SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect ROUTE_ID , count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tgroup by ct.ROUTE_ID #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by ct.ROUTE_ID #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by ct.ROUTE_ID #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "\t\tgroup by ct.ROUTE_ID #failed\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand (STATUS ='sent')\n" +
                "\t\tgroup by ct.ROUTE_ID #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.ROUTE_ID, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.ROUTE_ID #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.ROUTE_ID, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.ROUTE_ID #absentSubscriberSM\n" +
                "\t) t\n" +
                "\tgroup by route_id;",
                nativeQuery = true)
        List<Object[]> routeWise(String routeId, LocalDateTime createdStartTime, LocalDateTime createdEndTime);

        @Query(value = "select PARTY_ID, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent, SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect PARTY_ID , count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tgroup by c2.PARTY_ID #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by c2.PARTY_ID #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by c2.PARTY_ID #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "\t\tgroup by c2.PARTY_ID #failed\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (STATUS ='sent')\n" +
                "\t\tgroup by c2.PARTY_ID #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by c2.PARTY_ID #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect PARTY_ID, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by c2.PARTY_ID #absentSubscriberSM\n" +
                "\t) t\n" +
                "\tgroup by PARTY_ID;",
                nativeQuery = true)
        List<Object[]> partyWise(String partyId, LocalDateTime createdStartTime, LocalDateTime createdEndTime);

        @Query(value = "select CAMPAIGN_ID , SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent,SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect CAMPAIGN_ID , count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #failed\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID , (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand (STATUS ='sent')\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?2 and ct.last_updated_tx_stamp <= ?3)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.CAMPAIGN_ID #absentSubscriberSM\n" +
                "\t) t\n" +
                "\tgroup by CAMPAIGN_ID;",
                nativeQuery = true)
        List<Object[]> campaignWise(String campaignId, LocalDateTime createdStartTime, LocalDateTime createdEndTime);


        @Query(value = "select campaign_id,route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent, SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tgroup by ct.campaign_id,route_id #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by ct.campaign_id,route_id #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by ct.campaign_id,route_id #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "\t\tgroup by ct.campaign_id,route_id #failed\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, count(*) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='sent')\n" +
                "\t\tgroup by ct.campaign_id,route_id #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.campaign_id,route_id #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.campaign_id,route_id #absentSubscriberSM\n" +
                "\t) t\n" +
                "\tgroup by campaign_id, route_id;\n" +
                "\t\n",
                nativeQuery = true)
        List<Object[]> campaignAndRouteWiseReports(String campaignId, String routeId,LocalDateTime createdStartTime, LocalDateTime createdEndTime);

        @Query(value = "select PARTY_ID,campaign_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent,SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from(\n" +
                "     select ct.CAMPAIGN_ID ,c2.PARTY_ID, count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct\n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null \n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     group by ct.campaign_id,c2.PARTY_ID #TOTAL\n" +
                "     union all\n" +
                "     select ct.CAMPAIGN_ID ,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and ct.STATUS_EXTERNAL='delivered'\n" +
                "     group by ct.campaign_id,c2.PARTY_ID #DELIVERED\n" +
                "     union all\n" +
                "     select ct.CAMPAIGN_ID ,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and STATUS = 'sent'\n" +
                "     and (STATUS_EXTERNAL is null \n" +
                "     or STATUS_EXTERNAL != 'delivered')\n" +
                "     and ERROR_CODE_EXTERNAL is null\n" +
                "     group by ct.campaign_id,c2.PARTY_ID  #inprocess\n" +
                "     union all\n" +
                "     select ct.CAMPAIGN_ID ,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "     group by ct.campaign_id,c2.party_id #failed \n" +
                "     union all\n" +
                "     select ct.CAMPAIGN_ID ,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,count(*)  as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and (STATUS ='sent')\n" +
                "     group by ct.campaign_id,c2.party_id #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,c2.party_id, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.campaign_id,c2.party_id #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,c2.party_id, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.campaign_id,c2.party_id#absentSubscriberSM\n" +
                "     ) t\n" +
                "     group by PARTY_ID,campaign_id;",
                nativeQuery = true)
        List<Object[]> campaignAndPartyWiseReports(String campaignId, String partyId,LocalDateTime createdStartTime, LocalDateTime createdEndTime);

        @Query(value = "select PARTY_ID,route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent,SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from(\n" +
                "     select ct.route_id ,c2.PARTY_ID, count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct\n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null \n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     group by ct.route_id,c2.PARTY_ID #TOTAL\n" +
                "     union all\n" +
                "     select ct.route_id ,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and ct.STATUS_EXTERNAL='delivered'\n" +
                "     group by ct.route_id,c2.PARTY_ID #DELIVERED\n" +
                "     union all\n" +
                "     select ct.route_id ,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and STATUS = 'sent'\n" +
                "     and (STATUS_EXTERNAL is null \n" +
                "     or STATUS_EXTERNAL != 'delivered')\n" +
                "     and ERROR_CODE_EXTERNAL is null\n" +
                "     group by ct.route_id,c2.PARTY_ID  #inprocess\n" +
                "     union all\n" +
                "     select ct.route_id ,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "     group by ct.route_id,c2.party_id #failed \n" +
                "     union all\n" +
                "     select ct.route_id ,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,count(*)  as sent\n" +
                "     from campaign_task ct \n" +
                "     join campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "     where ct.PARENT_ID  is null\n" +
                "     and (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "     and (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "     and ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "     and (STATUS ='sent')\n" +
                "     group by ct.route_id,c2.party_id #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.route_id ,c2.party_id, (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.route_id,c2.party_id #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.route_id ,c2.party_id, (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?3 and ct.last_updated_tx_stamp <= ?4)\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.route_id,c2.party_id#absentSubscriberSM\n" +
                "     ) t\n" +
                "     group by PARTY_ID,route_id;",
                nativeQuery = true)
        List<Object[]> routeAndPartyWiseReports(String routeId, String partyId,LocalDateTime createdStartTime, LocalDateTime createdEndTime);

        @Query(value = "select PARTY_ID,campaign_id, route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(sent) as sent,SUM(unidentifiedSubscriber) as unidentifiedSubscriber, SUM(absentSubscriberSM) as absentSubscriberSM\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, count(*)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct\n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null \n" +
                "\t\tor STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID  #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand (STATUS ='failed' or (STATUS_EXTERNAL != 'delivered' and (ERROR_CODE is not null or ERROR_CODE_EXTERNAL is not null) ))\n" +
                "\t\tgroup by ct.campaign_id, route_id,c2.party_id #failed \n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id ,(select 0)  as total,(select 0) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,count(*)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand (STATUS ='sent')\n" +
                "\t\tgroup by ct.campaign_id, route_id,c2.party_id #sent\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id , (select 0)  as total,(select 0) as absentSubscriberSM, count(*) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is not null and ERROR_CODE_EXTERNAL = '5')\n" +
                "\t\tgroup by ct.CAMPAIGN_ID ,route_id,c2.party_id  #unidentifiedSubscriber\n" +
                "\t\tunion all\n" +
                "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id , (select 0)  as total,count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as failed, (select 0) as delivered,(select 0) as inProcess,(select 0)  as sent\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (ct.last_updated_tx_stamp >= ?4 and ct.last_updated_tx_stamp <= ?5)\n" +
                "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
                "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
                "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
                "\t\tand (STATUS ='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
                "\t\tgroup by ct.CAMPAIGN_ID ,route_id,c2.party_id #absentSubscriberSM\n" +
                "\n" +
                "\t) t\n" +
                "\tgroup by PARTY_ID,campaign_id, route_id limit ?6 offset ?7",
                nativeQuery = true)

        List<Object[]> campaignRouteAndPartyWiseReports(String campaignId, String routeId, String partyId, LocalDateTime createdStartTime, LocalDateTime createdEndTime, Integer limit, Integer offset);
        @Query(value = "select count(*) from campaign c where (c.campaign_id = ?1 or ?1 = ' ' or ?1 = null) and (c.party_id = ?2 or ?2 = ' ' or ?2 = null)", nativeQuery = true)
        Integer countAllCampaign(String campaignId, String partyId);
        @Query(value = "select count(*) from campaign_task where (route_id = ?1 or ?1 = ' ' or ?1 = null)", nativeQuery = true)
        Integer countAllRoute(String routeId);
        @Query(value = "select count(*) from campaign where (party_id = ?1 or ?1 = ' ' or ?1 = null)", nativeQuery = true)
        Integer countAllParty(String partyId);
        @Query(value = "select count(*) from campaign_task where (campaign_id = ?1 or ?1 = ' ' or ?1 = null) and (route_id = ?2 or ?2 = ' ' or ?2 = null)", nativeQuery = true)
        Integer countCampaignWithRoute(String campaignId, String routeId);

}


