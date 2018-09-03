package com.nd.auxo.recommend.core.api.uc;

import com.nd.auxo.recommend.core.api.uc.repository.UcUser;
import com.nd.gaea.uranus.gql.PagerResult;

import java.util.List;

/**
 * Created by way on 2016/7/25.
 */
public interface UcApi {

    /**
     * 批量查询用户id
     */
    PagerResult<UcUser> thirdQuery(List<UcUser> ucUserList);

    /**
     * 批量查询用户id
     * @param ucUserOrgList
     * @return userId以逗号分隔
     *
     *
     */
    String thirdQueryUid(String ucUserOrgList);
}
