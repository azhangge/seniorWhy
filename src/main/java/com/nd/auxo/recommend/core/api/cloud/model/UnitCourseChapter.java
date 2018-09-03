package com.nd.auxo.recommend.core.api.cloud.model;

import lombok.Data;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class UnitCourseChapter {

    private Integer id;
    private String title;
    private Integer sortNumber;
    private Integer parentId;
    private Integer index;
}
