package com.nd.auxo.recommend.core.api.erp.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by way on 2016/7/25.
 */
@Data
public class ErpFinishResponse {

    /*
    IsSuccess (boolean, optional), 是否成功
FinishId (integer, optional), 新生成的完成id
CampaignId (integer, optional) 养成任务Id
     */
    @JsonProperty("IsSuccess")
    private Boolean isSuccess;

    @JsonProperty("FinishId")
    private Long finishId;

    @JsonProperty("CampaignId")
    private Long campaignId;
}
