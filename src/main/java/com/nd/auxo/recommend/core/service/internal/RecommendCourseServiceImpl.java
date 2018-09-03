package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.RecommendCourse;
import com.nd.auxo.recommend.core.repository.RecommendCourseRepository;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetail;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetailRepository;
import com.nd.auxo.recommend.core.service.RecommendCourseService;

import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseDetailVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForCreate;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.*;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Service
@Validated
public class RecommendCourseServiceImpl extends AbstractService<RecommendCourse, UUID> implements RecommendCourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendCourseServiceImpl.class);


    @Autowired
    private RecommendCourseRepository recommendCourseRepository;

    @Autowired
    private RecommendCourseDetailRepository recommendCourseDetailRepository;

    @Override
    public Repository<RecommendCourse, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(recommendCourseRepository);
    }

    @Override
    public RecommendCourseVo create(RecommendCourseVoForCreate recommendCourseVoForCreate, Long projectId, Long userId) {
        //已推荐的不能再推荐
        if (Integer.valueOf(0).equals(recommendCourseVoForCreate.getRecommendMode()) && this.recommendCourseRepository.findByProjectIdAndCustomTypeAndCustomId(projectId, recommendCourseVoForCreate.getCustomType(), recommendCourseVoForCreate.getCustomId()) != null) {
            throw new AlreadyUsedException(SR.getString(I18nExceptionMsgConstant.COMMON_ALREADY_USED));
        }
          /*
        sortNumber
         */
        Long sortNumber = this.recommendCourseRepository.maxSortNumberByProjectId(projectId);
        if (sortNumber == null) {
            sortNumber = 0l;
        } else {
            sortNumber = sortNumber + 1;
        }
        RecommendCourse recommendCourse = this.convertByCreate(recommendCourseVoForCreate);
        recommendCourse.setSortNumber(sortNumber.intValue());
        recommendCourse.setProjectId(projectId);
        recommendCourse.setCreateTime(new Date());
        recommendCourse.setCreateUserId(userId);
        recommendCourse.setUpdateTime(new Date());
        recommendCourse.setUpdateUserId(userId);
        this.recommendCourseRepository.save(recommendCourse);
        /*
        courseDetail
         */
        addRecommendCourseDetail(recommendCourseVoForCreate.getRecommendCourseDetailList(),recommendCourse.getId());
        LOGGER.info("新建课程推荐\"" + recommendCourse.getTitle() + "(" + recommendCourse.getId() + ")\"");
        return convert(this.recommendCourseRepository.findOne(recommendCourse.getId()));
    }


    public RecommendCourseVo convert(RecommendCourse recommendCourse) {
        RecommendCourseVo recommendCourseVo = new RecommendCourseVo();
        recommendCourseVo.setCustomType(recommendCourse.getCustomType());
        recommendCourseVo.setStatus(recommendCourse.getStatus());
        recommendCourseVo.setCustomOrderBy(recommendCourse.getCustomOrderBy());
        recommendCourseVo.setCreateTime(recommendCourse.getCreateTime());
        recommendCourseVo.setProjectId(recommendCourse.getProjectId());
        recommendCourseVo.setCreateUserId(recommendCourse.getCreateUserId());
        recommendCourseVo.setTitle(recommendCourse.getTitle());
        recommendCourseVo.setSortNumber(recommendCourse.getSortNumber());
        recommendCourseVo.setCustomId(recommendCourse.getCustomId());
        recommendCourseVo.setUpdateTime(recommendCourse.getUpdateTime());
        recommendCourseVo.setUpdateUserId(recommendCourse.getUpdateUserId());
        recommendCourseVo.setId(recommendCourse.getId());
        recommendCourseVo.setCustomIdType(recommendCourse.getCustomIdType());
        recommendCourseVo.setRecommendMode(recommendCourse.getRecommendMode());
        return recommendCourseVo;
    }


    private RecommendCourse convertByCreate(RecommendCourseVoForCreate create) {
        RecommendCourse recommendCourse = new RecommendCourse();
        recommendCourse.setCustomId(create.getCustomId());
        recommendCourse.setCustomType(create.getCustomType());
        recommendCourse.setStatus(create.getStatus());
        recommendCourse.setCustomOrderBy(create.getCustomOrderBy());
        recommendCourse.setTitle(create.getTitle());
        recommendCourse.setCustomIdType(create.getCustomIdType());
        recommendCourse.setRecommendMode(create.getRecommendMode());
        return recommendCourse;
    }

    @Override
    public RecommendCourseVo update(RecommendCourse recommendCourse, Long userId, List<RecommendCourseDetailVo> recommendCourseDetailList) {
        recommendCourse.setUpdateTime(new Date());
        recommendCourse.setUpdateUserId(userId);
        recommendCourse = this.recommendCourseRepository.save(recommendCourse);
        deleteRecommendCourseDetail(recommendCourse.getId());
        addRecommendCourseDetail(recommendCourseDetailList, recommendCourse.getId());
        LOGGER.info("编辑课程推荐\"" + recommendCourse.getTitle() + "(" + recommendCourse.getId() + ")\"");
        return convert(recommendCourse);
    }



    @Override
    public PageResult<RecommendCourseVo> listPages(Long projectId, Integer status, Integer page, Integer size) {
        Page<RecommendCourse> voPage;
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, RecommendCourse.FIELD_SORT_NUMBER);
        if (status == null) {
            voPage = this.recommendCourseRepository.findByProjectId(projectId, pageable);
        } else {
            voPage = this.recommendCourseRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        if (CollectionUtils.isEmpty(voPage.getContent())) {
            return new PageResult<>();
        }
        List<RecommendCourseVo> voList = new ArrayList<>();
        //
        List<UUID> uuidList = new ArrayList<>();
        for (RecommendCourse course : voPage.getContent()) {
            voList.add(this.convert(course));
            if(RecommendCourseVo.RecommendMode.RECOMMEND_CUSTOM.getValue().equals(course.getRecommendMode())){
                uuidList.add(course.getId());
            }
        }
        PageResult pageResult = new PageResult();
        pageResult.setCount((int) voPage.getTotalElements());
        pageResult.setItems(voList);
        //返回
        if(CollectionUtils.isEmpty(uuidList)){
            return pageResult;
        }
        List<RecommendCourseDetail> recommendCourseDetailList = this.recommendCourseDetailRepository.findByRecommendIdIn(uuidList);
        if(CollectionUtils.isEmpty(recommendCourseDetailList)){
            return pageResult;
        }
        // 处理子课程
        Map<UUID,List<RecommendCourseDetailVo>> detailMap = new HashMap<>();
        List<RecommendCourseDetailVo> tempList;
        for(RecommendCourseDetail detail:recommendCourseDetailList){
            tempList = detailMap.get(detail.getRecommendId());
            if(tempList != null){
                tempList.add(detail.convert());
                continue;
            }
            tempList = new ArrayList<>();
            tempList.add(detail.convert());
            detailMap.put(detail.getRecommendId(),tempList);
        }
        for(RecommendCourseVo vo:voList){
            if(detailMap.containsKey(vo.getId())){
                vo.setRecommendCourseDetailList(detailMap.get(vo.getId()));
            }
        }
        return pageResult;
    }

    @Override
    public void move(UUID courseId, Integer sortNumber, Long projectId) {
        RecommendCourse recommendCourse = this.recommendCourseRepository.findOne(courseId);
        if (recommendCourse == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, courseId));
        }
        Integer oldSortNumber = recommendCourse.getSortNumber();
        if (oldSortNumber == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NULL_OLD_SORT_NUMBER, courseId));
        }
        if (sortNumber.compareTo(oldSortNumber) == 0) {
            return;
        } else if (sortNumber.compareTo(oldSortNumber) < 0) {
            this.recommendCourseRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(1, projectId, courseId, sortNumber, oldSortNumber);
        } else {
            this.recommendCourseRepository.moveSortNumberByProjectIdAndBannerIdAndGeSortNumberAndLeSortNumber(-1, projectId, courseId, oldSortNumber, sortNumber);
        }
        recommendCourse.setSortNumber(sortNumber);
        this.recommendCourseRepository.save(recommendCourse);
        LOGGER.info("移动课程推荐\"" + recommendCourse.getTitle() + "(" + recommendCourse.getId() + ")\"");
    }


    /**
     * 添加 推荐课程 明细
     *
     * @param recommendCourseDetailList
     * @param recommendId
     */
    private void addRecommendCourseDetail(List<RecommendCourseDetailVo> recommendCourseDetailList, UUID recommendId) {
        if (CollectionUtils.isEmpty(recommendCourseDetailList)) {
            return;
        }
        RecommendCourseDetail recommendCourseDetail;
        for (RecommendCourseDetailVo detailVo : recommendCourseDetailList) {
            recommendCourseDetail = RecommendCourseDetail.convert(detailVo);
            recommendCourseDetail.setRecommendId(recommendId);
            this.recommendCourseDetailRepository.save(recommendCourseDetail);
        }
    }

    /**
     * 删除 推荐课程 明细
     *
     * @param recommendId
     */
    public void deleteRecommendCourseDetail(UUID recommendId) {
        this.recommendCourseDetailRepository.deleteByRecommendId(recommendId);
    }

    @Override
    public List<RecommendCourseDetail> findRecommendCourseDetail(UUID recommendId) {
        return this.recommendCourseDetailRepository.findByRecommendId(recommendId);
    }
}
