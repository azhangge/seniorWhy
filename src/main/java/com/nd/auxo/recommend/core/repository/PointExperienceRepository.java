package com.nd.auxo.recommend.core.repository;

import com.nd.gaea.uranus.gql.PagerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
@Repository
public interface PointExperienceRepository extends JpaRepository<PointExperience,Integer> {

    public PagerResult<PointExperience> listPages(String rewardUserName, String operateUserName, Date startTime, Date endTime, int page, int size);
}
