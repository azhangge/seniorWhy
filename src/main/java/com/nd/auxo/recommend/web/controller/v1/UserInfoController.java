package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.repository.UserInfo;
import com.nd.auxo.recommend.core.service.UserInfoService;
import com.nd.gaea.gql.Gql;
import com.nd.gaea.gql.dsl.QueryData;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 学员
 * <p/>
 * Created by jsc on 2016/7/19.
 */
@Api("学员")
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("创建学员")
    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    public UserInfo create(@ApiParam("学员对象") @Valid @RequestBody UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        UserInfo result = this.userInfoService.create(userInfo);
        return result;
    }

    @ApiOperation("修改学员")
    @RequestMapping(value = "/v1/users/{user_id}", method = RequestMethod.PUT)
    public UserInfo update(
            @ApiParam("学员标识") @PathVariable("user_id") long userId,
            @ApiParam("学员对象") @Valid @RequestBody UserInfo userInfo) {
        UserInfo userInfoPo = this.userInfoService.get(userId);
        if (userInfo.getRealName() != null) {
            userInfoPo.setRealName(userInfo.getRealName());
        }
        if (userInfo.getAccount() != null) {
            userInfoPo.setAccount(userInfo.getAccount());
        }
        if (userInfo.getIdCard() != null) {
            userInfoPo.setIdCard(userInfo.getIdCard());
        }
        if (userInfo.getMobile() != null) {
            userInfoPo.setMobile(userInfo.getMobile());
        }
        if (userInfo.getOrgId() != null) {
            userInfoPo.setOrgId(userInfo.getOrgId());
        }
        if (userInfo.getOrgName() != null) {
            userInfoPo.setOrgName(userInfo.getOrgName());
        }
        if (userInfo.getRegDate() != null) {
            userInfoPo.setRegDate(userInfo.getRegDate());
        }
        userInfoPo.setUpdateTime(new Date());
        UserInfo result = this.userInfoService.update(userInfoPo);
        return result;
    }

    @ApiOperation("删除学员")
    @RequestMapping(value = "/v1/users/{user_id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("学员标识") @PathVariable("user_id") long userId) {
        UserInfo userInfo = this.userInfoService.get(userId);
        if (userInfo != null) {
            this.userInfoService.delete(userInfo);
        }
    }

    @ApiOperation("获取学员")
    @RequestMapping(value = "/v1/users/{user_id}", method = RequestMethod.GET)
    public UserInfo get(@ApiParam("学员标识") @PathVariable("user_id") long userId) {
        return this.userInfoService.get(userId);
    }

    @ApiOperation(value = "查询学员", response = UserInfo.class,
            notes = "采用ODATA格式；入参如/v1/xxxs?$filter=userId eq 1&$order=user_id desc&$offset=0&$limit=10&$result=list&$select=name")
    @RequestMapping(value = "/v1/users", method = RequestMethod.GET)
    @Gql(rootClass = UserInfo.class, enabledLike = true, maxResult = Integer.MAX_VALUE)
    public PageResult<UserInfo> search(@ApiParam(hidden = true) QueryData queryData) {
        PageResult<UserInfo> pageResult = new PageResult<>();
        PagerResult pagingResult = new PagerResult(queryData.getResult());
        if (pagingResult.getItems() != null){
            pagingResult.setTotal(pagingResult.getItems().size());
        }
        pageResult.setCount(pagingResult.getTotal() != null ? pagingResult.getTotal() : 0);
        pageResult.setItems((List<UserInfo>) pagingResult.getItems());
        return pageResult;
    }
}
