package com.nd.auxo.recommend.core.api.uc.repository;

import com.nd.gaea.uranus.gql.PagerResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by way on 2016/7/19.
 */
@FeignClient(url = "${uc.server.url}", value = "uc-api")
public interface UcUserRepository {

    @RequestMapping(value = "/v0.93/users/actions/third_query", method = RequestMethod.POST)
    PagerResult<UcUser> thirdQuery(@RequestBody List<UcUser> ucUserList);
}
