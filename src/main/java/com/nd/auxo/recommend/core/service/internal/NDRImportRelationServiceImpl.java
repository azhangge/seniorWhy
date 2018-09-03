package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.repository.NDRImportRelation;
import com.nd.auxo.recommend.core.repository.NDRImportRelationRepository;
import com.nd.auxo.recommend.core.service.NDRImportRelationService;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
@Service
public class NDRImportRelationServiceImpl extends AbstractService<NDRImportRelation, Long> implements NDRImportRelationService {

    @Autowired
    private NDRImportRelationRepository ndrImportRelationRepository;

    @Override
    public Repository<NDRImportRelation, Long> getRepository() {
        return new JpaRepositoryAdapter(this.ndrImportRelationRepository);
    }

    @Override
    public NDRImportRelation findByProjectIdAndCustomTypeAndNdrId(Long projectId, String customType, UUID ndrId) {
        return this.ndrImportRelationRepository.findByProjectIdAndCustomTypeAndNdrId(projectId,customType,ndrId);
    }

    @Override
    public List<NDRImportRelation> findByProjectIdAndCustomTypeAndNdrIdIn(Long projectId, String customType, List<UUID> ids) {
        return this.ndrImportRelationRepository.findByProjectIdAndCustomTypeAndNdrIdIn(projectId,customType,ids);
    }
}
