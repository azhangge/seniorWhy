package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.RecommendTag;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVo;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForCreate;
import com.nd.gaea.context.service.EntityService;
import com.nd.gaea.uranus.gql.PageResult;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
public interface RecommendTagService extends EntityService<RecommendTag, UUID> {

    /**
     * 创建推荐标签
     *
     * @param recommendTagVoForCreate
     * @param projectId
     * @param userId
     * @return
     */
    RecommendTagVo create(RecommendTagVoForCreate recommendTagVoForCreate, Long projectId, Long userId);

    /**
     * 更新推荐标签
     *
     * @param recommendTag
     * @param userId
     * @return
     */
    RecommendTagVo update(RecommendTag recommendTag, Long userId);

    /**
     * 分页查询推荐标签
     *
     * @param status
     * @param page
     * @param size
     * @return
     */
    PageResult<RecommendTagVo> listPages(Long projectId, Integer status, Integer page, Integer size);

    RecommendTagVo convert(RecommendTag recommendTag);

    /**
     * 移动
     * @param tagId
     * @param sortNumber 新的位置
     * @param projectId
     */
    void move(UUID tagId, Integer sortNumber, Long projectId);
}
