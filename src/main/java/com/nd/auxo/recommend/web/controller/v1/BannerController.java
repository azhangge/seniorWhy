package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.Banner;
import com.nd.auxo.recommend.core.service.BannerService;
import com.nd.auxo.recommend.core.util.JsonUtils;
import com.nd.auxo.sdk.recommend.RecommendBannerApi;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVo;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForCreate;
import com.nd.auxo.sdk.recommend.vo.banner.BannerVoForUpdate;
import com.nd.gaea.SR;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/23.
 */
@Api("广告横幅")
@RestController
@RequestMapping("")
public class BannerController implements RecommendBannerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);


    @Autowired
    private BannerService bannerService;


    @ApiOperation("创建banner")
    @RequestMapping(value = "/v1/recommends/banners", method = RequestMethod.POST)
    public BannerVo create(@ApiParam("banner对象") @Valid @RequestBody BannerVoForCreate bannerVoForCreate) {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        BannerVo vo = this.bannerService.create(bannerVoForCreate, GaeaContext.getAppId(), GaeaContext.getUserId());
        LOGGER.info("新建banner推荐\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }

    @ApiOperation("编辑")
    @RequestMapping(value = "/v1/recommends/banners/{banner_id}", method = RequestMethod.PUT)
    public BannerVo update(@ApiParam("banner标识") @PathVariable("banner_id") UUID bannerId, @ApiParam("banner对象") @Valid @RequestBody BannerVoForUpdate bannerVoForUpdate) throws IOException {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        Banner banner = this.bannerService.get(bannerId);
        if (banner == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, bannerId));
        }
        JsonUtils.update(banner, ObjectUtils.toJson(bannerVoForUpdate));

        BannerVo vo = this.bannerService.update(banner, GaeaContext.getUserId());
        LOGGER.info("编辑banner推荐\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }

    @ApiOperation("删除")
    @RequestMapping(value = "/v1/recommends/banners/{banner_id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("banner标识") @PathVariable("banner_id") UUID bannerId) {
        Banner banner = new Banner();
        banner.setId(bannerId);
        this.bannerService.delete(banner);
        LOGGER.info("删除banner推荐\"(" + bannerId + ")\"");

    }

    @ApiOperation("列表查询")
    @RequestMapping(value = "/v1/recommends/banners", method = RequestMethod.GET)
    public PageResult<BannerVo> listPages(@ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, Banner.FIELD_SORT_NUMBER);
        Page<Banner> bannerPage = this.bannerService.findByProjectId(GaeaContext.getAppId(), pageRequest);
        if (CollectionUtils.isEmpty(bannerPage.getContent())) {
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        List<BannerVo> bannerVoList = new ArrayList<>();
        for (Banner banner : bannerPage.getContent()) {
            bannerVoList.add(this.bannerService.convert(banner));
        }
        PageResult pageResult = new PageResult();
        pageResult.setCount((int) bannerPage.getTotalElements());
        pageResult.setItems(bannerVoList);
        return pageResult;
    }

    @ApiOperation("获取")
    @RequestMapping(value = "/v1/recommends/banners/{banner_id}", method = RequestMethod.GET)
    public BannerVo get(@ApiParam("banner标识") @PathVariable("banner_id") UUID bannerId) {
        Banner banner = this.bannerService.get(bannerId);
        if (banner == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, bannerId));
        }
        return this.bannerService.convert(banner);
    }

    @ApiOperation("移动")
    @RequestMapping(value = "/v1/recommends/banners/{banner_id}/move", method = RequestMethod.PUT)
    public void move(@ApiParam("标识") @PathVariable("banner_id") UUID bannerId, @ApiParam("新的排序位置") @RequestParam("sort_number") Integer sortNumber) {
        this.bannerService.move(bannerId, sortNumber, GaeaContext.getAppId());
    }
}
