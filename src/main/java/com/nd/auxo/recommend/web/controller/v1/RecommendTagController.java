package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.RecommendTag;
import com.nd.auxo.recommend.core.service.RecommendTagService;
import com.nd.auxo.recommend.core.util.JsonUtils;
import com.nd.auxo.sdk.recommend.RecommendTagApi;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVo;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForCreate;
import com.nd.auxo.sdk.recommend.vo.tag.RecommendTagVoForUpdate;
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
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Api("推荐标签")
@RestController
@RequestMapping("")
public class RecommendTagController implements RecommendTagApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendTagController.class);


    @Autowired
    private RecommendTagService recommendTagService;

    @ApiOperation("创建推荐标签")
    @RequestMapping(value = "/v1/recommends/tags", method = RequestMethod.POST)
    public RecommendTagVo create(@ApiParam("推荐标签对象") @Valid @RequestBody RecommendTagVoForCreate recommendTagVoForCreate) {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        RecommendTagVo vo = this.recommendTagService.create(recommendTagVoForCreate, GaeaContext.getAppId(), GaeaContext.getUserId());
        LOGGER.info("新建标签推荐\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }


    @ApiOperation("更新")
    @RequestMapping(value = "/v1/recommends/tags/{tag_id}", method = RequestMethod.PUT)
    public RecommendTagVo update(@ApiParam("推荐标签标识") @PathVariable("tag_id") UUID tagId,
                                 @ApiParam("推荐标签对象") @Valid @RequestBody RecommendTagVoForUpdate recommendTagVoForUpdate) throws IOException {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        RecommendTag recommendTag = this.recommendTagService.get(tagId);
        if (recommendTag == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND,tagId));
        }
        JsonUtils.update(recommendTag, ObjectUtils.toJson(recommendTagVoForUpdate));

        RecommendTagVo vo =  this.recommendTagService.update(recommendTag, GaeaContext.getUserId());
        LOGGER.info("编辑标签推荐\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }


    @ApiOperation("删除")
    @RequestMapping(value = "/v1/recommends/tags/{tag_id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("推荐标签标识") @PathVariable("tag_id") UUID tagId) {
        RecommendTag recommendTag = new RecommendTag();
        recommendTag.setId(tagId);
        this.recommendTagService.delete(recommendTag);
        LOGGER.info("删除标签推荐\"(" + tagId + ")\"");

    }

    @ApiOperation("列表查询")
    @RequestMapping(value = "/v1/recommends/tags", method = RequestMethod.GET)
    public PageResult<RecommendTagVo> listPages(
            @ApiParam("状态 1上线 0下线") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        PageResult<RecommendTagVo> pageResult = recommendTagService.listPages(GaeaContext.getAppId(), status, page, size);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        return pageResult;
    }

    @ApiOperation("获取")
    @RequestMapping(value = "/v1/recommends/tags/{tag_id}", method = RequestMethod.GET)
    public RecommendTagVo get(@ApiParam("推荐标签标识") @PathVariable("tag_id") UUID tagId) {
        RecommendTag recommendTag = this.recommendTagService.get(tagId);
        if (recommendTag == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND,tagId));
        }
        return this.recommendTagService.convert(recommendTag);
    }

    @ApiOperation("移动")
    @RequestMapping(value = "/v1/recommends/tags/{tag_id}/move", method = RequestMethod.PUT)
    public void move(@ApiParam("标识") @PathVariable("tag_id") UUID tagId, @ApiParam("新的排序位置") @RequestParam("sort_number") Integer sortNumber) {
        this.recommendTagService.move(tagId, sortNumber,GaeaContext.getAppId());
    }
}
