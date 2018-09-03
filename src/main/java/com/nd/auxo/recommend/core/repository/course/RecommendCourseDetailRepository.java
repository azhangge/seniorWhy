package com.nd.auxo.recommend.core.repository.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
public interface RecommendCourseDetailRepository extends JpaRepository<RecommendCourseDetail, UUID> {
    /**
     * 根据推荐课程id删除
     * @param recommendId
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(" delete from RecommendCourseDetail a where a.recommendId=?1")
    void deleteByRecommendId(UUID recommendId);

    /**
     * 查找  推荐课程 明细
     * @param recommendId
     * @return
     */
    List<RecommendCourseDetail> findByRecommendId(UUID recommendId);

    /**
     *
     * @param recommendIdList
     * @return
     */
    List<RecommendCourseDetail> findByRecommendIdIn(List<UUID> recommendIdList);
}
