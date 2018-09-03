package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.service.PointExperienceService;
import com.nd.auxo.recommend.web.controller.v1.point.PointExperienceVo;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.uranus.gql.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
@Api("积分经验补齐")
@RestController
@RequestMapping("")
public class PointExperienceController {

    @Autowired
    private PointExperienceService pointExperienceService;

    @ApiOperation("补齐积分经验")
    @RequestMapping(value = "/v1/recommends/point_experiences", method = RequestMethod.POST)
    public PointExperienceVo reward(@ApiParam("补积分经验对象") @RequestBody() PointExperienceVo param) throws ParseException {

        return this.pointExperienceService.reward(param);
}

    @ApiOperation("查询补齐积分经验操作")
    @RequestMapping(value = "/v1/recommends/point_experiences", method = RequestMethod.GET)
    public PageResult<PointExperienceVo> getPage(@ApiParam("授予者姓名") @RequestParam(value = "reward_user_name",required = false) String rewardUserName,
                                                 @ApiParam("操作者姓名") @RequestParam(value = "operate_user_name",required = false) String operateUserName,
                                                 @ApiParam("开始时间") @RequestParam(value = "start_time",required = false) String startTime,
                                                 @ApiParam("结束时间") @RequestParam(value = "end_time",required = false) String endTime,
                                                 @ApiParam("第几页，从0开始") @RequestParam(value = "page",required = false,defaultValue = "0") Integer page,
                                                 @ApiParam("每页的记录数") @RequestParam(value = "size",required = false,defaultValue = "20") Integer size) throws ParseException {

        PageResult<PointExperienceVo> pageResult = pointExperienceService.getPage(rewardUserName, operateUserName, startTime, endTime, page, size);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        return pageResult;
    }
}
