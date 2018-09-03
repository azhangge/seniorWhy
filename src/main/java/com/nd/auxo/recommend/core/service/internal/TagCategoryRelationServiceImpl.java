package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.repository.TagCategoryRelation;
import com.nd.auxo.recommend.core.repository.TagCategoryRelationRepository;
import com.nd.auxo.recommend.core.service.TagCategoryRelationService;
import com.nd.auxo.sdk.recommend.common.CustomType;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by way on 2016/11/1.
 */
@Service
public class TagCategoryRelationServiceImpl extends AbstractService<TagCategoryRelation, Long> implements TagCategoryRelationService {

    @Autowired
    private TagCategoryRelationRepository tagCategoryRelationRepository;

    @Override
    public Repository<TagCategoryRelation, Long> getRepository() {
        return new JpaRepositoryAdapter(this.tagCategoryRelationRepository);
    }

    @Override
    public void saveRelation(TagCategoryRelation tagCategoryRelation) {
        tagCategoryRelation.setProjectId(GaeaContext.getAppId());
        tagCategoryRelation.setUpdateTime(new Date());
        tagCategoryRelation.setUpdateUserId(GaeaContext.getTrueUserId());
        save(tagCategoryRelation);
    }

    @Override
    public TagCategoryRelation getByProjectIdAndCustomTypeAndNdCodeAndStatus(Long projectId, String customType, String ndCode, Integer status) {
        return this.tagCategoryRelationRepository.findByProjectIdAndCustomTypeAndNdCodeAndStatus(projectId, customType, ndCode, status);
    }
}
