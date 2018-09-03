package com.nd.auxo.recommend.core.i18n;

import lombok.Data;

/**
 * @author way
 *         Created on 2016/6/3.
 */
@Data
public class I18nExceptionMsgConstant {
    /*
    common
     */
    public static final String COMMON_NOT_FOUND = "common.not.found";
    public static final String COMMON_NULL_OLD_SORT_NUMBER = "common.null.old.sort.number";
    public static final String COMMON_AUTHENTICATION_FAILED = "common.authentication.failed";
    public static final String COMMON_AUTHENTICATION_OVERDUE = "common.authentication.overdue";
    public static final String COMMON_PARAMETER_ERROR = "common.parameter.error";
    public static final String COMMON_REQUEST_OBJECT_IS_EMPTY = "common.request.parameter.empty";
    /**
     * 已存在相同标签的推荐
     */
    public static final String COMMON_ALREADY_USED = "common.already.used";
    public static final String COMMON_NEED_AUTH = "common.need.auth";

    /*
    banner
     */
    public static final String BANNER_ALREADY_USED = "banner.already.used";

    /**
     * 组织
     */
    public static final String ORG_NOT_FOUND = "org.not.found";

    /**
     * kv
     */
    public static final String KV_ALREADY_USED = "kv.already.used";

}
