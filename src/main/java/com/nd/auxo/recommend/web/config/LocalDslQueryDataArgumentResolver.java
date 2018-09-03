package com.nd.auxo.recommend.web.config;

import com.nd.gaea.WafException;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.gql.DefaultGqlValid;
import com.nd.gaea.gql.Gql;
import com.nd.gaea.gql.GqlValid;
import com.nd.gaea.gql.dsl.DslVisitor;
import com.nd.gaea.gql.dsl.QueryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * Created by luocl on 16-2-18.
 */
@Slf4j
public class LocalDslQueryDataArgumentResolver implements HandlerMethodArgumentResolver {
    private EntityManager entityManager;

    public LocalDslQueryDataArgumentResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(QueryData.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Gql gqlAnnotation = parameter.getMethodAnnotation(Gql.class);
        if (gqlAnnotation != null) {
            Map<String, String[]> parameterMap = webRequest.getParameterMap();

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                if (entry.getKey().startsWith("$"))
                    sb.append("&" + entry.getKey() + "=" + StringUtils.trim(entry.getValue()[0]));
            }
            //默认返回20条数据
            if (!parameterMap.keySet().contains("$limit")) {
                sb.append("&$limit=20");
            }
            if (sb.length() > 0)
                sb.deleteCharAt(0);

            Class resultClass = gqlAnnotation.resultClass();
            if (resultClass.equals(void.class))
                resultClass = null;

            String gql = sb.toString();
            GqlValid valid = gqlAnnotation.validClass().equals(void.class)
                    ? new DefaultGqlValid(gql, gqlAnnotation)
                    : (GqlValid) gqlAnnotation.validClass().newInstance();

            try {
                return DslVisitor.parse(gql, entityManager, gqlAnnotation.rootClass(), resultClass, valid);
            } catch (Exception e) {
                log.error("解析查询参数失败", e);
                throw new WafException("WAF/BUSINESS_EXCEPTION", "解析查询参数失败:" + e.getLocalizedMessage());
            }
        }

        return null;
    }
}
