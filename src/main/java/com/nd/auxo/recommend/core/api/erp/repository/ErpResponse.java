package com.nd.auxo.recommend.core.api.erp.repository;

import lombok.Data;

/**
 * Created by way on 2016/7/21.
 */
@Data
public class ErpResponse {

    private Integer result;// 1 成功

    private String msg;

    private String code;
}
