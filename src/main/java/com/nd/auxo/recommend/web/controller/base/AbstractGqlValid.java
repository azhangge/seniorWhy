package com.nd.auxo.recommend.web.controller.base;

import com.nd.gaea.gql.GqlValid;
import com.nd.gaea.gql.node.OrderDirection;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 16-2-19.
 */
public abstract class AbstractGqlValid implements GqlValid {
    public static final char UNDERLINE = '_';

    /**
     * 拒绝访问的成员列表
     */
    private Set<String> deniedMembers = new HashSet<>();

    public AbstractGqlValid() {
        super();
        addDeniedMembers(deniedMembers);
    }

    /**
     * 添加禁止作为查询条件的字段("字段名用蛇形")
     *
     * @param deniedMembers
     */
    public abstract void addDeniedMembers(Set<String> deniedMembers);

    public Set<String> getDeniedMembers() {
        return this.deniedMembers;
    }

    @Override
    public String getGql() {
        return null;
    }

    public void onMember(String member) {
        if (deniedMembers != null && deniedMembers.contains(camelToUnderline(member))) {
            throw new ArgumentValidationException(MessageFormat.format("字段{0}禁止作为查询条件", member));
        }
    }

    @Override
    public void onIn(String member, List values) {

    }

    @Override
    public void onLike(String member, String value) {

    }

    @Override
    public void onOrder(String member, OrderDirection direction) {

    }

    @Override
    public void onSelect(String member) {

    }

    @Override
    public void onLimit(int limit) {

    }

    @Override
    public void onOffset(int offset) {

    }

    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}