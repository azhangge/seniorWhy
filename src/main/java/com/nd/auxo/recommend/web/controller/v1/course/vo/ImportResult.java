package com.nd.auxo.recommend.web.controller.v1.course.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.UUID;

/**
 * Created by way on 2016/11/2.
 */
@Data
public class ImportResult {
    @ApiParam("导入的教材id")
    private UUID id;

    @ApiParam("导入的章节数")
    private int chapterCount;

    @ApiParam("导入的资源数")
    private int resourceCount;
}
