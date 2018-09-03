package com.nd.auxo.recommend.core.api.erp.repository;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by way on 2016/7/19.
 */
@FeignClient(url = "${erp.server.url}", value = "erp-api")
public interface ErpCustomCampaignRepository {
    /**
     * 新增任务对象
     */
    @ApiOperation("创建")
    @RequestMapping(value = "/ServiceHost/ToDoList/json/CustomCampaign?CustomContentType=jsonIsoDateFormat", method = RequestMethod.POST)
    CustomCampaign create(@RequestParam("orgId") Long orgId, @RequestBody CustomCampaign customCampaign);

    /**
     * 更新
     *
     * @param id
     * @param customCampaign
     */
    @ApiOperation("更新")
    @RequestMapping(value = "/ServiceHost/ToDoList/json/PatchCustomCampaign?CustomContentType=jsonIsoDateFormat", method = RequestMethod.POST)
    CustomCampaign update(@RequestParam("ID") String id, @RequestParam("orgId") Long orgId, @RequestParam("appkey") String appkey,
                          @RequestBody CustomCampaign customCampaign);


    /**
     * 送积分经验
     *
     * @param erpVirtualType
     * @param virtualAmount
     * @param uid
     * @param orgId
     * @return
     */
    @Deprecated
    @ApiOperation("送积分经验")
    @RequestMapping(value = "/ServiceHost/ToDoList/json/AddPointAndExp", method = RequestMethod.POST)
    ErpResponse addPointAndExp(@RequestParam("virtual_type") Integer erpVirtualType,
                               @RequestParam("virtual_amount") Integer virtualAmount,
                               @RequestParam("uid") Long uid,
                               @RequestParam("orgid") Long orgId,
                               @RequestParam("source_type") Integer sourceType);


    /**
     * 完成养成任务
     *
     * @param orgId                                        uc的组织Id（必填）
     * @param campaignId:调用获取任务，或者添加任务时候返回的任务唯一Id          （必填）
     * @param uid
     * @param taskDate:完成日期时间，特别的情况用于指定完成某一天的任务，平时传当前时间即可。
     * @param appkey，通过分配每个接入子系统点一个appkey（必填）
     * @return
     */
    @Deprecated
    @RequestMapping(value = "/ServiceHost/ToDoList/json/FinishCampaign?CustomContentType=jsonIsoDateFormat", method = RequestMethod.POST)
    ErpFinishResponse finishCampaign(@RequestParam("orgId") Long orgId,
                                     @RequestParam("campaignId") Long campaignId,
                                     @RequestParam("uid") Long uid,
                                     @RequestParam("taskDate") Date taskDate,
                                     @RequestParam("appkey") String appkey);
}
