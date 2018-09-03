package com.nd.auxo.recommend.core.service.internal.support;

import lombok.Data;

/**
 * Created by way on 2016/7/25.
 *
 * 活动需要的扩展参数
 */
@Data
public class ActivityExpandParam {
    private Long orgId;
    private Integer orgType;
    private String projectCode;
}
