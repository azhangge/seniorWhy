package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.repository.UserInfo;
import com.nd.auxo.recommend.core.repository.UserInfoRepository;
import com.nd.auxo.recommend.core.service.UserInfoService;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 学员
 * <p/>
 * Created by jsc on 2016/7/19.
 */
@Service
public class UserInfoServiceImpl extends AbstractService<UserInfo, Long> implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Repository<UserInfo, Long> getRepository() {
        return new JpaRepositoryAdapter(this.userInfoRepository);
    }


}
