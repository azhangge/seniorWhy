package com.nd.auxo.recommend.core.api.cloud.model;

import lombok.Data;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class LifeCycle {
    private String version;
    private String status;
    private Boolean enable;
    private String creator;
}
