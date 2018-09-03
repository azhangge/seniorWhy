package com.nd.auxo.recommend.core.repository;

import com.nd.auxo.sdk.recommend.common.CustomType;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by way on 2016/11/1.
 */
public interface TagCategoryRelationRepository extends JpaRepository<TagCategoryRelation, Long> {
    /**
     * 获取
     *
     * @param projectId
     * @param customType
     * @param ndCode
     * @return
     */
    TagCategoryRelation findByProjectIdAndCustomTypeAndNdCodeAndStatus(Long projectId, String customType, String ndCode, Integer status);
}
