package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.RecommendTag;
import com.nd.auxo.recommend.core.repository.RecommendTagRepository;
import com.nd.auxo.recommend.core.service.RecommendTagService;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVo;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForCreate;
import com.nd.gaea.SR;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.uranus.common.exception.AlreadyUsedException;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Service
@Validated
@Slf4j
public class RecommendTagServiceImpl extends AbstractService<RecommendTag, UUID> implements RecommendTagService {

    @Autowired
    private RecommendTagRepository recommendTagRepository;

    @Override
    public Repository<RecommendTag, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(recommendTagRepository);
    }

    @Override
    public RecommendTagVo create(RecommendTagVoForCreate recommendTagVoForCreate, Long projectId, Long userId) {
        //已推荐的不能再推荐
        if (this.recommendTagRepository.findByProjectIdAndCustomTypeAndCustomId(projectId, recommendTagVoForCreate.getCustomType(), recommendTagVoForCreate.getCustomId()) != null) {
            throw new AlreadyUsedException(SR.getString(I18nExceptionMsgConstant.COMMON_ALREADY_USED));
        }
           /*
        sortNumber
         */
        Long sortNumber = this.recommendTagRepository.maxSortNumberByProjectId(projectId);
        if (sortNumber == null) {
            sortNumber = 0l;
        } else {
            sortNumber = sortNumber + 1;
        }
        RecommendTag recommendTag = this.convertByCreate(recommendTagVoForCreate);
        recommendTag.setSortNumber(sortNumber.intValue());
        recommendTag.setProjectId(projectId);
        recommendTag.setCreateTime(new Date());
        recommendTag.setCreateUserId(userId);
        recommendTag.setUpdateTime(new Date());
        recommendTag.setUpdateUserId(userId);
        this.recommendTagRepository.save(recommendTag);
        return convert(this.recommendTagRepository.findOne(recommendTag.getId()));
    }

    private RecommendTag convertByCreate(RecommendTagVoForCreate recommendTagVoForCreate) {
        RecommendTag recommendTag = new RecommendTag();
        recommendTag.setCustomType(recommendTagVoForCreate.getCustomType());
        recommendTag.setStatus(recommendTagVoForCreate.getStatus());
        recommendTag.setCustomOrderBy(recommendTagVoForCreate.getCustomOrderBy());
        recommendTag.setTitle(recommendTagVoForCreate.getTitle());
        recommendTag.setCustomId(recommendTagVoForCreate.getCustomId());
        recommendTag.setAppStoreObjectId(recommendTagVoForCreate.getAppStoreObjectId());
        recommendTag.setCustomIdType(recommendTagVoForCreate.getCustomIdType());
        return recommendTag;
    }

    @Override
    public RecommendTagVo update(RecommendTag recommendTag, Long userId) {
        recommendTag.setUpdateTime(new Date());
        recommendTag.setUpdateUserId(userId);
        return convert(recommendTagRepository.save(recommendTag));
    }

    @Override
    public PageResult<RecommendTagVo> listPages(Long projectId, Integer status, Integer page, Integer size) {
        Page<RecommendTag> tagPage;
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, RecommendTag.FIELD_SORT_NUMBER);
        if (status == null) {
            tagPage = this.recommendTagRepository.findByProjectId(projectId, pageable);
        } else {
            tagPage = this.recommendTagRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        if (CollectionUtils.isEmpty(tagPage.getContent())) {
            return new PageResult<>();
        }
        List<RecommendTagVo> voList = new ArrayList<>();
        for (RecommendTag tag : tagPage.getContent()) {
            voList.add(this.convert(tag));
        }
        PageResult pageResult = new PageResult();
        pageResult.setCount((int) tagPage.getTotalElements());
        pageResult.setItems(voList);
        return pageResult;
    }

    @Override
    public RecommendTagVo convert(RecommendTag recommendTag) {
        RecommendTagVo recommendTagVo = new RecommendTagVo();
        recommendTagVo.setCustomType(recommendTag.getCustomType());
        recommendTagVo.setStatus(recommendTag.getStatus());
        recommendTagVo.setCustomOrderBy(recommendTag.getCustomOrderBy());
        recommendTagVo.setCreateTime(recommendTag.getCreateTime());
        recommendTagVo.setProjectId(recommendTag.getProjectId());
        recommendTagVo.setCreateUserId(recommendTag.getCreateUserId());
        recommendTagVo.setTitle(recommendTag.getTitle());
        recommendTagVo.setSortNumber(recommendTag.getSortNumber());
        recommendTagVo.setCustomId(recommendTag.getCustomId());
        recommendTagVo.setUpdateTime(recommendTag.getUpdateTime());
        recommendTagVo.setUpdateUserId(recommendTag.getUpdateUserId());
        recommendTagVo.setId(recommendTag.getId());
        recommendTagVo.setAppStoreObjectId(recommendTag.getAppStoreObjectId());
        recommendTagVo.setCustomIdType(recommendTag.getCustomIdType());
        return recommendTagVo;
    }

    @Override
    public void move(UUID tagId, Integer sortNumber, Long projectId) {
        RecommendTag tag = this.recommendTagRepository.findOne(tagId);
        if (tag == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, tagId));
        }
        Integer oldSortNumber = tag.getSortNumber();
        if (oldSortNumber == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NULL_OLD_SORT_NUMBER, tagId));
        }
        if (sortNumber.compareTo(oldSortNumber) == 0) {
            return;
        } else if (sortNumber.compareTo(oldSortNumber) < 0) {
            this.recommendTagRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(1, projectId, tagId, sortNumber, oldSortNumber);
        } else {
            this.recommendTagRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(-1, projectId, tagId, oldSortNumber, sortNumber);
        }
        tag.setSortNumber(sortNumber);
        this.recommendTagRepository.save(tag);
        log.info("移动推荐标签\"" + tag.getTitle() + "(" + tag.getId() + ")\"");
    }
}
