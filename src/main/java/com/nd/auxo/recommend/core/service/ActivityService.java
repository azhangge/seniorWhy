package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVo;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForCreate;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForUpdate;
import com.nd.gaea.context.service.EntityService;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
public interface ActivityService extends EntityService<Activity, UUID> {


    /**
     * 创建
     *
     * @param create
     * @param projectId
     * @param userId
     * @return
     */
    ActivityVo create(@Valid ActivityVoForCreate create, Long projectId, Long userId);


    /**
     * 更新
     *
     * @param update
     * @param userId
     * @return
     */
    ActivityVo update(UUID id, ActivityVoForUpdate update, Long projectId,Long userId);

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(UUID id) throws Exception;


    /**
     * 转化
     *
     * @param
     * @return
     */
    ActivityVo convertActivityVo(Activity activity);

}
