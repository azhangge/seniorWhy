package com.nd.auxo.recommend.core.api.cloud.model;

import com.nd.elearning.sdk.ndr.bean.LifeCycleVo;
import lombok.Data;

import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class NDRResource {
    private UUID identifier;
    private String title;
    private String resType;
    private String Alias;
    private LifeCycleVo lifeCycle;
}
