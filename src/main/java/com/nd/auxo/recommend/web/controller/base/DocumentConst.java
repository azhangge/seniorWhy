package com.nd.auxo.recommend.web.controller.base;

/**
 * Created by Administrator on 16-3-23.
 */
public class DocumentConst {
    public static final String ODATA_NOTES = "采用ODATA格式；入参如/xxxx/search?$filter=title eq'title'&$order=user_id desc&$offset=100&$limit=10&$result=list&$select=name；" +
            "回参为PagerResult封装{\"total\":0,\"items\":[]}";
}
