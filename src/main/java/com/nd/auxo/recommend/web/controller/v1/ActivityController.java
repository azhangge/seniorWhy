package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.constant.SwitchKeyConsts;
import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.service.ActivityService;
import com.nd.auxo.recommend.web.controller.base.DocumentConst;
import com.nd.auxo.sdk.interaction.common.ODataConsts;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVo;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForCreate;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForUpdate;
import com.nd.gaea.SR;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.gql.Gql;
import com.nd.gaea.gql.dsl.QueryData;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.BusinessException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.servicemanager.ServiceManagerDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/7/18.
 */
@Api("活动")
@RestController
@RequestMapping("")
public class ActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);


    @Autowired
    private ActivityService activityService;


    public PagerResult<ActivityVo> search(@ApiParam("过滤参数") @RequestParam(ODataConsts.PARAM_FILTER) String filter, @ApiParam("排序参数") @RequestParam(ODataConsts.PARAM_ORDER) String order, @ApiParam("偏移量") @RequestParam(ODataConsts.PARAM_OFFSET) Integer offset, @ApiParam("记录数") @RequestParam(ODataConsts.PARAM_LIMIT) Integer limit, @ApiParam("返回结果格式") @RequestParam(ODataConsts.PARAM_RESULT) String result, @ApiParam("返回结果字段") @RequestParam(ODataConsts.PARAM_SELECT) String select) {
        return null;
    }

    @ApiOperation(value = "查询活动", response = ActivityVo.class, notes = DocumentConst.ODATA_NOTES)
    @RequestMapping(value = "/v1/recommends/activities/search", method = RequestMethod.GET)
    @Gql(rootClass = Activity.class, enabledLike = true, maxResult = Integer.MAX_VALUE)
    public PagerResult search(@ApiParam(hidden = true) QueryData queryData) {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_ACTIVITY)) {
            throw new ObjectNotFoundException("Not Found");
        }
        try {
            PagerResult pagerResult = new PagerResult(queryData.getResult());
            if (CollectionUtils.isEmpty(pagerResult.getItems())){
                PagerResult result = new PagerResult();
                result.setTotal(0);
                result.setItems(new ArrayList());
                return result;
            }else {
                pagerResult.setTotal(pagerResult.getItems().size());
            }
            return pagerResult;
        } catch (BusinessException e) {
            LOGGER.debug("", e);
            throw new BusinessException(e.getMessage());
        }
    }


    @ApiOperation("创建活动")
    @RequestMapping(value = "/v1/recommends/activities", method = RequestMethod.POST)
    public ActivityVo create(@ApiParam("活动信息") @RequestBody ActivityVoForCreate create) {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_ACTIVITY)) {
            throw new ObjectNotFoundException("Not Found");
        }
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        ActivityVo vo = this.activityService.create(create, GaeaContext.getAppId(), GaeaContext.getUserId());
        LOGGER.info("新建活动\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }

    @ApiOperation("编辑活动")
    @RequestMapping(value = "/v1/recommends/activities/{id}", method = RequestMethod.PUT)
    public ActivityVo update(@ApiParam("活动标识") @PathVariable("id") UUID id
            , @ApiParam("活动") @RequestBody ActivityVoForUpdate update){
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_ACTIVITY)) {
            throw new ObjectNotFoundException("Not Found");
        }
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        ActivityVo vo = this.activityService.update(id, update, GaeaContext.getAppId(), GaeaContext.getUserId());
        LOGGER.info("编辑活动\"" + vo.getTitle() + "(" + vo.getId() + ")\"");
        return vo;
    }

    @ApiOperation("删除活动")
    @RequestMapping(value = "/v1/recommends/activities/{id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("活动标识") @PathVariable("id") UUID id) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_ACTIVITY)) {
            throw new ObjectNotFoundException("Not Found");
        }
        this.activityService.delete(id);
        LOGGER.info("删除活动\"(" + id + ")\"");

    }

    @ApiOperation("根据Id获取活动")
    @RequestMapping(value = "/v1/recommends/activities/{id}", method = RequestMethod.GET)
    public ActivityVo get(@ApiParam("活动标识") @PathVariable("id") UUID id) {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_ACTIVITY)) {
            throw new ObjectNotFoundException("Not Found");
        }
        Activity activity = this.activityService.get(id);
        if (activity == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, id));
        }
        return this.activityService.convertActivityVo(activity);
    }
}
