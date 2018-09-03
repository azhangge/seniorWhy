package com.nd.auxo.recommend.core.repository.internal;

import com.nd.auxo.recommend.core.repository.FAQManage;
import com.nd.auxo.recommend.core.util.JpaUtils;
import com.nd.gaea.uranus.gql.PagerResult;
import org.apache.commons.lang.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class FAQManageRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public PagerResult<FAQManage> listPages(Long  projectId, String questionName, String queryText, Integer questionType, String customType, int page, int size) {

        Map<String, Object> queryParam = new HashMap<>();
        String sql = "from FAQManage where 1=1";
        StringBuffer whereSb = new StringBuffer();
        if (StringUtils.isNotBlank(questionName)) {
            whereSb.append(" and questionName like :questionName");
            queryParam.put("questionName", JpaUtils.wrapLikeParam(questionName));
        }
        if (StringUtils.isNotBlank(queryText)) {
            whereSb.append(" and queryText like :queryText");
            queryParam.put("queryText", JpaUtils.wrapLikeParam(queryText));
        }
        if (questionType!=null) {
            whereSb.append(" and questionType =:questionType");
            queryParam.put("questionType", questionType);
        }
        if (StringUtils.isNotBlank(customType)) {
            whereSb.append(" and customType =:customType");
            queryParam.put("customType", customType);
        }
        if (projectId != null) {
            whereSb.append(" and projectId =:projectId");
            queryParam.put("projectId", projectId);
        }
        String countSql = "select count(1) from FAQManage where 1=1"+whereSb.toString() ;
        String selectSql = sql + whereSb.toString() + " order by sortNumber desc";
        PagerResult<FAQManage> result = new PagerResult<>();
        Long count = JpaUtils.count(entityManager, countSql, queryParam);
        if (count <= 0l) {
            result.setItems(Collections.EMPTY_LIST);
            return result;
        }
        result.setTotal(count.intValue());
        List<FAQManage> list = JpaUtils.queryList(entityManager, selectSql, queryParam, page, size);
        result.setItems(list);
        return result;
    }
}
