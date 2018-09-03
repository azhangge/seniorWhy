package com.nd.auxo.recommend.core.repository.param;

import lombok.Data;

/**
 * Created by way on 2016/11/4.
 */
@Data
public class KVSearchParam {

    private Integer page;

    private Integer size;

    private String key;

    private String groupKey;

    private Long projectId;
}
