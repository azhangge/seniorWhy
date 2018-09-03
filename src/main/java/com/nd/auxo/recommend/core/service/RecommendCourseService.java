package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.RecommendCourse;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetail;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseDetailVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForCreate;
import com.nd.gaea.context.service.EntityService;
import com.nd.gaea.uranus.gql.PageResult;
import java.util.List;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
public interface RecommendCourseService extends EntityService<RecommendCourse, UUID> {
    /**
     * 创建推荐课程
     *
     * @param recommendCourseVoForCreate
     * @param projectId
     * @param userId
     * @return
     */
    RecommendCourseVo create(RecommendCourseVoForCreate recommendCourseVoForCreate, Long projectId, Long userId);

    /**
     * 更新推荐课程
     *
     * @param recommendCourse
     * @param userId
     * @param recommendCourseDetailList
     * @return
     */
    RecommendCourseVo update(RecommendCourse recommendCourse, Long userId, List<RecommendCourseDetailVo> recommendCourseDetailList);

    /**
     * 分页查询推荐课程
     *
     * @param status
     * @param page
     * @param size
     * @return
     */
    PageResult<RecommendCourseVo> listPages(Long projectId, Integer status, Integer page, Integer size);

    RecommendCourseVo convert(RecommendCourse recommendCourse);

    /**
     * 移动
     *
     * @param courseId
     * @param sortNumber 新的位置
     * @param projectId
     */
    void move(UUID courseId, Integer sortNumber, Long projectId);

    /**
     * 删除 推荐课程 明细
     *
     * @param recommendId 推荐课程id
     */
    void deleteRecommendCourseDetail(UUID recommendId);

    /**
     * 查找  推荐课程 明细
     * @param recommendId 推荐课程id
     * @return
     */
    List<RecommendCourseDetail> findRecommendCourseDetail(UUID recommendId);
}
