package com.telcobright.SmsReport.Client.repositories;

import com.telcobright.SmsReport.Models.CampaignTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.net.www.content.text.Generic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CampaignReportRepository extends JpaRepository<CampaignTask,Long> {
//    @Query("SELECT statusExternal, COUNT(*) as total FROM campaign_task ct WHERE ct.campaignId = :campaignId AND ct.statusExternal='delivered' AND ct.parentId IS NULL GROUP BY ct.routeId")
//    List<Object[]> findReports(@Param("campaignId") String campaignId);
@Query("SELECT routeId, COUNT(*) as total FROM campaign_task ct WHERE ct.parentId IS NULL AND ct.statusExternal='delivered' GROUP BY ct.routeId")
List<Object[]> findRouteWiseDeliveredReports();

@Query("SELECT routeId, COUNT(*) as total FROM campaign_task ct WHERE ct.parentId IS NULL AND ct.status='sent' GROUP BY ct.routeId")
List<Object[]> findRouteWiseSentReports();

@Query("SELECT routeId, COUNT(*) as total FROM campaign_task ct WHERE ct.parentId IS NULL AND ct.status NOT IN ('sent') AND ct.statusExternal NOT IN ('delivered') GROUP BY ct.routeId")
List<Object[]> findRouteWiseFailedReports();

@Query("SELECT routeId, COUNT(*) as total FROM campaign_task ct WHERE ct.parentId IS NULL AND ct.status IN ('sent') AND ct.statusExternal IS NULL AND ct.errorCodeExternal IS NULL GROUP BY ct.routeId")
List<Object[]> findRouteWiseProcessingReports();

    @Query(value = "select PARTY_ID,campaign_id, route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed\n" +
            "from\n" +
            "\t(\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, count(*)  as total, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct\n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null \n" +
            "\t\tand ct.CAMPAIGN_ID = '10015'\n" +
            "\t\tand route_id = \"telco_gp\"\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID ='10000')\n" +
            "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #TOTAL\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total, (select 0) as failed, count(*) as delivered, (select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand ct.CAMPAIGN_ID = '10015'\n" +
            "\t\tand route_id = \"telco_gp\"\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID ='10000')\n" +
            "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
            "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #DELIVERED\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total,(select 0) as failed, (select 0) as delivered, count(*) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand ct.CAMPAIGN_ID = '10015'\n" +
            "\t\tand route_id = \"telco_gp\"\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID ='10000')\n" +
            "\t\tand STATUS = 'sent'\n" +
            "\t\tand (STATUS_EXTERNAL is null \n" +
            "\t\tor STATUS_EXTERNAL != 'delivered')\n" +
            "\t\tand ERROR_CODE_EXTERNAL is null\n" +
            "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID  #inprocess\n" +
            "\t\tunion all\n" +
            "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id ,(select 0)  as total,count(*) as failed, (select 0) as delivered,(select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
//            "\t\tand ct.CAMPAIGN_ID = '10015'\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
            "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
//            "\t\tand route_id = \"telco_gp\"\n" +
            "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID ='10000')\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and STATUS_EXTERNAL != 'delivered' )\n" +
            "\t\tgroup by ct.campaign_id, route_id,c2.party_id #failed\n" +
            "\t) t\n" +
            "\tgroup by PARTY_ID,campaign_id, route_id;",
            nativeQuery = true)
    List<Object[]> report(String campaignId, String routeId, String partyId);
@Query(value = "select ROUTE_ID , SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect ROUTE_ID , count(*)  as total, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null \n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tgroup by ct.ROUTE_ID #TOTAL\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total, (select 0) as failed, count(*) as delivered, (select 0) as inProcess\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
                "\t\tgroup by ct.ROUTE_ID #DELIVERED\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,(select 0) as failed, (select 0) as delivered, count(*) as inProcess\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (route_id = ?1  or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand STATUS = 'sent'\n" +
                "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
                "\t\tand ERROR_CODE_EXTERNAL is null\n" +
                "\t\tgroup by ct.campaign_id #inprocess\n" +
                "\t\tunion all\n" +
                "\t\tselect ROUTE_ID , (select 0)  as total,count(*) as failed, (select 0) as delivered,(select 0) as inProcess\n" +
                "\t\tfrom campaign_task ct \n" +
                "\t\twhere ct.PARENT_ID  is null\n" +
                "\t\tand (route_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
                "\t\tand (STATUS !='sent'\n" +
                "\t\tand ERROR_CODE_EXTERNAL is not null and STATUS_EXTERNAL !='delivered' )\n" +
                "\t\tgroup by ct.campaign_id #failed\n" +
                "\t) t\n" +
                "\tgroup by route_id;",
        nativeQuery = true)
List<Object[]> routeWise(String routeId);


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
            "\t\tgroup by ct.campaign_id #inprocess\n" +
            "\t\tunion all\n" +

            "\t\tselect campaign_id , (select 0)  as total,(select 0) as sent, count(*) as absentSubscriberSM, (select 0) as unidentifiedSubscriber, (select 0) as delivered,(select 0) as inProcess\n" +
            "\t\tfrom campaign_task ct \n" +
            "\t\twhere ct.PARENT_ID  is null\n" +
            "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//                "\t\tand (route_id = 'grameenphone')\n" +
            "\t\tand (STATUS ='sent'\n" +
            "\t\tand ERROR_CODE_EXTERNAL is not null and ERROR_CODE_EXTERNAL = 'absentSubscriberSM' )\n" +
            "\t\tgroup by ct.campaign_id #failed\n" +
            "\t) t\n" +
            "\tgroup by campaign_id;",
            nativeQuery = true)
List<Object[]> campaignWiseReports(String campaignId);

@Query(value = "select campaign_id,route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed\n" +
        "from\n" +
        "\t(\n" +
        "\t\tselect CAMPAIGN_ID ,route_id, count(*)  as total, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\twhere ct.PARENT_ID  is null \n" +
//        "\t\tand ct.CAMPAIGN_ID = '19090'\n" +
        "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
//        "\t\tand route_id = \"telco_gp\"\n" +
        "\t\tgroup by ct.campaign_id,route_id #TOTAL\n" +
        "\t\tunion all\n" +
        "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total, (select 0) as failed, count(*) as delivered, (select 0) as inProcess\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
//        "\t\tand ct.CAMPAIGN_ID = '19090'\n" +
//        "\t\tand route_id = \"telco_gp\"\n" +
        "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
        "\t\tgroup by ct.campaign_id,route_id #DELIVERED\n" +
        "\t\tunion all\n" +
        "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,(select 0) as failed, (select 0) as delivered, count(*) as inProcess\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
//        "\t\tand ct.CAMPAIGN_ID = '19090'\n" +
//        "\t\tand route_id = \"telco_gp\"\n" +
        "\t\tand STATUS = 'sent'\n" +
        "\t\tand (STATUS_EXTERNAL is null or STATUS_EXTERNAL != 'delivered')\n" +
        "\t\tand ERROR_CODE_EXTERNAL is null\n" +
        "\t\tgroup by ct.campaign_id #inprocess\n" +
        "\t\tunion all\n" +
        "\t\tselect CAMPAIGN_ID ,route_id, (select 0)  as total,count(*) as failed, (select 0) as delivered,(select 0) as inProcess\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
//        "\t\tand ct.CAMPAIGN_ID = '19090'\n" +
//        "\t\tand route_id = \"telco_gp\"\n" +
        "\t\tand (STATUS !='sent'\n" +
        "\t\tand ERROR_CODE_EXTERNAL is not null and STATUS_EXTERNAL !='delivered' )\n" +
        "\t\tgroup by ct.campaign_id #failed\n" +
        "\t) t\n" +
        "\tgroup by campaign_id, route_id;\n" +
        "\t\n",
        nativeQuery = true)
List<Object[]> campaignAndRouteWiseReports(String campaignId, String routeId);

@Query(value = "select PARTY_ID,campaign_id, route_id, SUM(total) as total,SUM(delivered) as delivered, SUM(inProcess) as inProcess, SUM(failed) as failed, SUM(suspended) as suspended\n" +
        "from\n" +
        "\t(\n" +
        "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, count(*)  as total, (select 0) as failed, (select 0) as delivered, (select 0) as inProcess, (select 0) as suspended\n" +
        "\t\tfrom campaign_task ct\n" +
        "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
        "\t\twhere ct.PARENT_ID  is null \n" +
        "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
        "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
        "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #TOTAL\n" +
        "\t\tunion all\n" +
        "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total, (select 0) as failed, count(*) as delivered, (select 0) as inProcess, (select 0) as suspended\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
        "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
        "\t\tand ct.STATUS_EXTERNAL='delivered'\n" +
        "\t\tgroup by ct.campaign_id,route_id,c2.PARTY_ID #DELIVERED\n" +
        "\t\tunion all\n" +
        "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.PARTY_ID, (select 0)  as total,(select 0) as failed, (select 0) as delivered, count(*) as inProcess, (select 0) as suspended\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
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
        "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id ,(select 0)  as total,count(*) as failed, (select 0) as delivered,(select 0) as inProcess, (select 0) as suspended\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
        "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
        "\t\tand (STATUS ='sent'\n" +
        "\t\tand ERROR_CODE_EXTERNAL is not null and STATUS_EXTERNAL != 'delivered' )\n" +
        "\t\tgroup by ct.campaign_id, route_id,c2.party_id #failed \n" +
        "\t\tunion all\n" +
        "\t\tselect ct.CAMPAIGN_ID ,route_id,c2.party_id ,(select 0)  as total,count(*) as failed, (select 0) as delivered,(select 0) as inProcess,count(*)  as suspended\n" +
        "\t\tfrom campaign_task ct \n" +
        "\t\tjoin campaign c2 on ct.CAMPAIGN_ID = c2.CAMPAIGN_ID \n" +
        "\t\twhere ct.PARENT_ID  is null\n" +
        "\t\tand (ct.campaign_id = ?1 or ?1 = ' ' or ?1 = null)\n" +
//        "\t\tand route_id = \"grameenphone\"\n" +
        "\t\tand (route_id = ?2 or ?2 = ' ' or ?2 = null)\n" +
        "\t\tand ct.CAMPAIGN_ID in (select c.CAMPAIGN_ID from campaign c where c.PARTY_ID = ?3 or ?3 = ' ' or ?3 = null)\n" +
        "\t\tand (STATUS ='suspended')\n" +
        "\t\tgroup by ct.campaign_id, route_id,c2.party_id #suspended\n" +
        "\n" +
        "\t) t\n" +
        "\tgroup by PARTY_ID,campaign_id, route_id limit 0,10;",
        nativeQuery = true)

List<Object[]> campaignRouteAndPartyWiseReports(String campaignId, String routeId, String partyId);

}
