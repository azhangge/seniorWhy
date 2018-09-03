package com.nd.auxo.recommend.core.repository;

import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
public interface RecommendCourseRepository extends JpaRepository<RecommendCourse,UUID>{
    RecommendCourse findByProjectIdAndCustomTypeAndCustomId(Long projectId, String customType, String customId);

    Page<RecommendCourse> findByProjectId(Long projectId, Pageable pageable);

    Page<RecommendCourse> findByProjectIdAndStatus(Long projectId, Integer status, Pageable pageable);

    @Query("select max(a.sortNumber) from RecommendCourse a where a.projectId = ?1")
    Long maxSortNumberByProjectId(Long projectId);


    /**
     * 移动标签
     * @param offSet 移动的偏移量
     * @param projectId 修改的父标签
     * @param courseId   不修改的标签
     * @param beginSortNumber 大等于的排序号
     * @param endSortNumber 小等于的排序号
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(" update RecommendCourse a set a.sortNumber=a.sortNumber+?1 where a.projectId=?2 and a.id<>?3 and a.sortNumber>=?4 and a.sortNumber<=?5")
    void moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(int offSet, Long projectId, UUID courseId, Integer beginSortNumber, Integer endSortNumber);
}
