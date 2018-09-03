package com.nd.auxo.recommend.core.api.cloud.model;

import lombok.Data;

import java.util.List;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class CloudNDRCreate {
    private Integer unitId;
    private Integer catalogId;
    private List<NDRResource> resources;
}
