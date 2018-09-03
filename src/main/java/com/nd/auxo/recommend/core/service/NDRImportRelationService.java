package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.NDRImportRelation;
import com.nd.gaea.context.service.EntityService;

import java.util.List;
import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
public interface NDRImportRelationService extends EntityService<NDRImportRelation, Long> {

    /**
     * 查询
     * @param projectId
     * @param customType
     * @param ndrId
     * @return
     */
    NDRImportRelation findByProjectIdAndCustomTypeAndNdrId(Long projectId, String customType, UUID ndrId);

    /**
     *  批量查询
     * @param projectId
     * @param customType
     * @param ids
     * @return
     */
    List<NDRImportRelation> findByProjectIdAndCustomTypeAndNdrIdIn(Long projectId, String customType, List<UUID> ids);
}
