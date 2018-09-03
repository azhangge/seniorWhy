package com.nd.auxo.recommend.core.api.erp;

import com.nd.auxo.recommend.core.api.erp.constant.ErpVirtualType;
import com.nd.auxo.recommend.core.api.erp.repository.CustomCampaign;
import com.nd.auxo.recommend.core.api.erp.repository.ErpFinishResponse;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

/**
 * Created by way on 2016/7/18.
 */
public interface ErpCustomCampaignApi {

    /**
     * 新增任务对象
     */
    CustomCampaign create(CustomCampaign customCampaign);

    /**
     * 更新
     *
     * @param id
     * @param customCampaign
     */
    CustomCampaign update(String id, @RequestBody CustomCampaign customCampaign);

    /**
     * 送积分经验
     */
    ErpResponse addPointAndExp(ErpVirtualType type, Integer virtualAmount, Long userId, Long orgId);

    /**
     *  送积分重试策略
     * @param type
     * @param virtualAmount
     * @param userId
     * @param orgId
     * @param rawId
     * @return
     */
    ErpResponse addPointAndExpRetry(ErpVirtualType type, Integer virtualAmount, Long userId, Long orgId,String rawId);

    /**
     * 完成养成任务
     *
     * @param orgId
     * @param id
     * @param uid
     * @param taskDate
     * @param appKey
     * @return
     */
    ErpFinishResponse finishCampaign(Long orgId, Long id, Long uid, Date taskDate, String appKey);
}
