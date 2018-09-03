package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.TagCategoryRelation;
import com.nd.gaea.context.service.EntityService;

/**
 * Created by way on 2016/11/1.
 */
public interface TagCategoryRelationService extends EntityService<TagCategoryRelation, Long> {
    /**
     * 保存
     * @param tagCategoryRelation
     */
    void saveRelation(TagCategoryRelation tagCategoryRelation);

    /**
     * 查找
     * @param projectId
     * @param customType
     * @param ndCode
     * @param status
     * @return
     */
    TagCategoryRelation getByProjectIdAndCustomTypeAndNdCodeAndStatus(Long projectId, String customType, String ndCode, Integer status);
}
