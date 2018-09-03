package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.RecommendTag;
import com.nd.auxo.recommend.core.repository.UserInfo;
import com.nd.gaea.context.service.EntityService;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 学员
 *
 * Created by jsc on 2016/7/19.
 */
public interface UserInfoService extends EntityService<UserInfo, Long> {
}
