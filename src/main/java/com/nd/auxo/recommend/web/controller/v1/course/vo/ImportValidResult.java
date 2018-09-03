package com.nd.auxo.recommend.web.controller.v1.course.vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportValidResult {
    @ApiParam("校验结果 0 可进行导入，1不建议导入")
    private Integer code;
    @ApiParam("不建议导入 原因")
    private List<UUID> idList;
}
