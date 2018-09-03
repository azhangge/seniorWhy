package com.nd.auxo.recommend.web.controller.v1.course.vo;

import com.nd.elearning.sdk.ndr.bean.CategoryRelationTargetVo;
import com.nd.elearning.sdk.ndr.bean.CategoryRelationVo;
import com.nd.gaea.core.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Created by way on 2016/11/2.
 */
@Data
public class CategoryRelationLevelVo {

    /**
     * 学段
     */
    private static final int LEVEL_PHASE_OF_STUDYING = 1;
    /**
     * 年级
     */
    private static final int LEVEL_GRADE = 2;
    /**
     * 学科
     */
    private static final int LEVEL_SUBJECT = 3;
    /**
     * 版本
     */
    private static final int LEVEL_EDITION = 4;
    /**
     * 子版本
     */
    private static final int LEVEL_SUB_EDITION = 5;
    /**
     * 等级
     */
    private int level;


    public static CategoryRelationLevelVo convert(CategoryRelationVo categoryRelationVo) {
        CategoryRelationLevelVo categoryRelationLevelVo = new CategoryRelationLevelVo();
        categoryRelationLevelVo.setTarget(categoryRelationVo.getTarget());
        categoryRelationLevelVo.setIdentifier(categoryRelationVo.getIdentifier());
        categoryRelationLevelVo.setOrderNum(categoryRelationVo.getOrderNum());
        categoryRelationLevelVo.setEnable(categoryRelationVo.getEnable());
        categoryRelationLevelVo.setPatternPath(categoryRelationVo.getPatternPath());
        // set level
        if (categoryRelationVo.getTarget() != null && StringUtils.isNotBlank(categoryRelationVo.getTarget().getNdCode())) {
            categoryRelationLevelVo.setLevel(convertLevelFromNdCode(categoryRelationVo.getTarget().getNdCode()));
        }
        return categoryRelationLevelVo;
    }

    /**
     * @param ndCode
     * @return
     */
    private static int convertLevelFromNdCode(String ndCode) {
        if (ndCode.startsWith("$O")) {
            if (ndCode.matches("\\$ON[0-9]{2}00[0-9]{2}")) {
                return LEVEL_PHASE_OF_STUDYING;
            } else {
                return LEVEL_GRADE;
            }
        }
        if (ndCode.startsWith("$S")) {
            return LEVEL_SUBJECT;
        }
        if (ndCode.startsWith("$E")) {
            if (ndCode.matches("\\$E[0-9]{3}000")) {
                return LEVEL_EDITION;
            } else {
                return LEVEL_SUB_EDITION;
            }
        }
        return 0;
    }

    @ApiModelProperty("ID")
    private UUID identifier;
    @ApiModelProperty("查询目标对象")
    private CategoryRelationTargetVo target;
    @ApiModelProperty("顺序")
    private Integer orderNum;
    @ApiModelProperty("是否可用")
    private Boolean enable;
    @ApiModelProperty("模式路径")
    private String patternPath;
    @ApiModelProperty("维度关系数据对象")
    private List<CategoryRelationLevelVo> items;
}
