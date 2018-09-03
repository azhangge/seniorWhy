package com.nd.auxo.recommend.web.controller.v1.course.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by way on 2016/10/31.
 */
@Data
@Api("教材导入数据")
public class TeachingMaterialImport {
    @ApiParam("教材id数组")
    private List<UUID> ids;
    @ApiParam("年级标签")
    private ImportTag gradeTag;
    @ApiParam("年级标签")
    private ImportTag subjectTag;
    @ApiParam("要导入的资源类型 1--素材 2--课件")
    private Set<Integer> resourceTypes;
}
