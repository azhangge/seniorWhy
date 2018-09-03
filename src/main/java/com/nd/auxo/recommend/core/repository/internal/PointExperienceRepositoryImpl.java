package com.nd.auxo.recommend.core.repository.internal;

import com.nd.auxo.recommend.core.repository.PointExperience;
import com.nd.auxo.recommend.core.util.JpaUtils;
import com.nd.gaea.uranus.gql.PagerResult;
import org.apache.commons.lang.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class PointExperienceRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public PagerResult<PointExperience> listPages(String rewardUserName, String operateUserName, Date startTime, Date endTime, int page, int size){

        Map<String, Object> queryParam = new HashMap<>();
        String sql = "from PointExperience where 1=1";
        StringBuffer whereSb = new StringBuffer();
        if (StringUtils.isNotBlank(rewardUserName)){
            whereSb.append(" and rewardUserName like :rewardUserName");
            queryParam.put("rewardUserName",JpaUtils.wrapLikeParam(rewardUserName));
        }
        if (StringUtils.isNotBlank(operateUserName)){
            whereSb.append(" and operateUserName like :operateUserName");
            queryParam.put("operateUserName",JpaUtils.wrapLikeParam(operateUserName));
        }
        if (startTime != null){
            whereSb.append(" and rewardTime >= :startTime");
            queryParam.put("startTime",startTime);
        }
        if (endTime != null){
            whereSb.append(" and rewardTime <= :endTime");
            queryParam.put("endTime",endTime);
        }
        String countSql="select count(1) from PointExperience where 1=1"+whereSb.toString();
        String selectSql = sql+whereSb.toString()+" order by rewardTime desc";
        PagerResult<PointExperience> result=new PagerResult<>();
        Long count=JpaUtils.count(entityManager, countSql, queryParam);
        if (count <= 0l) {
            result.setItems(Collections.EMPTY_LIST);
            return result;
        }
        result.setTotal(count.intValue());
        List<PointExperience> list=JpaUtils.queryList(entityManager, selectSql, queryParam, page, size);
        result.setItems(list);
        return result;
    }
}
