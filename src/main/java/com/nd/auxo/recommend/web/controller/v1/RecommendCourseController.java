package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.RecommendCourse;
import com.nd.auxo.recommend.core.repository.course.RecommendCourseDetail;
import com.nd.auxo.recommend.core.service.RecommendCourseService;
import com.nd.auxo.recommend.core.util.JsonUtils;
import com.nd.auxo.sdk.recommend.RecommendCourseApi;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseDetailVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVo;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForCreate;
import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseVoForUpdate;
import com.nd.gaea.SR;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Api("推荐课程")
@RestController
@RequestMapping("")
public class RecommendCourseController implements RecommendCourseApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendCourseController.class);


    @Autowired
    private RecommendCourseService recommendCourseService;


    @ApiOperation("创建推荐课程")
    @RequestMapping(value = "/v1/recommends/courses", method = RequestMethod.POST)
    public RecommendCourseVo create(@ApiParam("推荐课程对象") @Valid @RequestBody RecommendCourseVoForCreate recommendCourseVoForCreate) {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        return this.recommendCourseService.create(recommendCourseVoForCreate, GaeaContext.getAppId(), GaeaContext.getUserId());
    }


    @ApiOperation("更新")
    @RequestMapping(value = "/v1/recommends/courses/{course_id}", method = RequestMethod.PUT)
    public RecommendCourseVo update(@ApiParam("推荐课程标识") @PathVariable("course_id") UUID courseId,
                                    @ApiParam("推荐课程对象") @Valid @RequestBody RecommendCourseVoForUpdate recommendCourseVoForUpdate) throws IOException {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        RecommendCourse recommendCourse = this.recommendCourseService.get(courseId);
        if (recommendCourse == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, courseId));
        }
        JsonUtils.update(recommendCourse, ObjectUtils.toJson(recommendCourseVoForUpdate));

        return this.recommendCourseService.update(recommendCourse, GaeaContext.getUserId(), recommendCourseVoForUpdate.getRecommendCourseDetailList());
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/v1/recommends/courses/{course_id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("推荐课程标识") @PathVariable("course_id") UUID courseId) {
        RecommendCourse recommendCourse = new RecommendCourse();
        recommendCourse.setId(courseId);
        this.recommendCourseService.delete(recommendCourse);
        this.recommendCourseService.deleteRecommendCourseDetail(courseId);
        LOGGER.info("删除课程推荐\"(" + recommendCourse.getId() + ")\"");

    }

    @ApiOperation("列表查询")
    @RequestMapping(value = "/v1/recommends/courses", method = RequestMethod.GET)
    public PageResult<RecommendCourseVo> listPages(
            @ApiParam("状态 1上线 0下线") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        PageResult<RecommendCourseVo> pageResult = recommendCourseService.listPages(GaeaContext.getAppId(), status, page, size);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        return pageResult;
    }

    @ApiOperation("获取")
    @RequestMapping(value = "/v1/recommends/courses/{course_id}", method = RequestMethod.GET)
    public RecommendCourseVo get(@ApiParam("推荐课程标识") @PathVariable("course_id") UUID courseId) {
        RecommendCourse recommendCourse = this.recommendCourseService.get(courseId);
        if (recommendCourse == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, courseId));
        }
        RecommendCourseVo vo = this.recommendCourseService.convert(recommendCourse);
        handleRecommendCourseDetail(vo);
        return vo;
    }


    @ApiOperation("移动")
    @RequestMapping(value = "/v1/recommends/courses/{course_id}/move", method = RequestMethod.PUT)
    public void move(@ApiParam("标识") @PathVariable("course_id") UUID courseId, @ApiParam("新的排序位置") @RequestParam("sort_number") Integer sortNumber) {
        this.recommendCourseService.move(courseId, sortNumber, GaeaContext.getAppId());
    }


    private void handleRecommendCourseDetail(RecommendCourseVo vo) {
        List<RecommendCourseDetail> detailList = this.recommendCourseService.findRecommendCourseDetail(vo.getId());
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }
        List<RecommendCourseDetailVo> detailVoList = new ArrayList<>();
        RecommendCourseDetailVo detailVo;
        for (RecommendCourseDetail detail : detailList) {
            detailVo = detail.convert();
            detailVoList.add(detailVo);
        }
        vo.setRecommendCourseDetailList(detailVoList);
    }
}
