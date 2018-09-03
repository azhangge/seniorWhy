package com.nd.auxo.recommend.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


/**
 * Created by way on 2016/11/1.
 */
public interface NDRImportRelationRepository extends JpaRepository<NDRImportRelation,Long> {

    /**
     * 获取
     * @param projectId
     * @param customType
     * @param ndrId
     * @return
     */
    NDRImportRelation findByProjectIdAndCustomTypeAndNdrId(Long projectId, String customType, UUID ndrId);

    /**
     * 批量查询
     * @param projectId
     * @param customType
     * @param ids
     * @return
     */
    List<NDRImportRelation> findByProjectIdAndCustomTypeAndNdrIdIn(Long projectId, String customType, List<UUID> ids);
}
