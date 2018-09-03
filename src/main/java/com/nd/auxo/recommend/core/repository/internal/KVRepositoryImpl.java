package com.nd.auxo.recommend.core.repository.internal;


import com.nd.auxo.recommend.core.repository.KVInfo;
import com.nd.auxo.recommend.core.repository.param.KVSearchParam;
import com.nd.auxo.recommend.core.util.JpaUtils;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.uranus.gql.PagerResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by way on 2016/11/4.
 */
public class KVRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public PagerResult<KVInfo> listPages(KVSearchParam param) {
        Map<String, Object> queryParam = new HashMap<>();
        String sql = " KVInfo  ";
        if (param.getProjectId() == null) {
            sql += "  where isolationStrategy=:projectAll";
            queryParam.put("projectAll", KVInfo.IsolationStrategy.PROJECT_ALL.getValue());
        } else {
            sql += "  where ( " +
                    " isolationStrategy=:projectAll or " +
                    " ( isolationStrategy=:projectSingle and isolationParam=:isolationParam)  " +
                    " )";
            queryParam.put("projectAll", KVInfo.IsolationStrategy.PROJECT_ALL.getValue());
            queryParam.put("projectSingle", KVInfo.IsolationStrategy.PROJECT_SINGLE.getValue());
            queryParam.put("isolationParam", "" + param.getProjectId());
        }
        if (StringUtils.isNotBlank(param.getKey())) {
            sql += " AND key like :key";
            queryParam.put("key", JpaUtils.wrapLikeParam(param.getKey()));
        }
        if (StringUtils.isNotBlank(param.getGroupKey())) {
            sql += " AND groupKey=:groupKey";
            queryParam.put("groupKey", param.getGroupKey());
        }
        PagerResult<KVInfo> result = new PagerResult<>();
        Long count = JpaUtils.count(entityManager, "SELECT COUNT(1) FROM " + sql, queryParam);
        result.setTotal(count.intValue());
        if (count <= 0l) {
            result.setItems(Collections.EMPTY_LIST);
            return result;
        }
        List<KVInfo> list = JpaUtils.queryList(entityManager, " FROM " + sql + " ORDER BY updateTime DESC", queryParam, param.getPage(), param.getSize());
        result.setItems(list);
        return result;
    }

}
