package com.nd.auxo.recommend.core.api.cloud.model;

import lombok.Data;

import java.util.List;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class CatalogBatchCreate {
    private List<String> titles;
}
