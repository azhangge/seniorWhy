package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.web.controller.v1.point.PointExperienceVo;
import com.nd.gaea.uranus.gql.PageResult;
import java.text.ParseException;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public interface PointExperienceService {

    PointExperienceVo reward(PointExperienceVo param) throws ParseException;

    PageResult<PointExperienceVo> getPage(String rewardUserName, String operateUserName, String startTime, String endTime, int page, int size) throws ParseException;
}
