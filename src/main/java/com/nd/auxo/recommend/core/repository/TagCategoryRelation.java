package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
@Data
@Entity
@Table(name = "tag_category_relation")
public class TagCategoryRelation {
    //constant---
    /**
     * 自定义的年级分类code。
     * 其对应的tag_id为年级大分类标签
     */
    public static final String CODE_CUSTOM_GRADE = "custom_grade_tag";
    /**
     * 自定义的学科分类code。
     * 其对应的tag_id为学科大分类标签
     */
    public static final String CODE_CUSTOM_SUBJECT = "custom_subject_tag";
    /**
     * 可用
     */
    public static final int STATUS_ENABLE = 1;
    /**
     * 不可用
     */
    public static final int STATUS_DISABLE = 0;


    //property--------
    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("101标签id")
    @Type(type = "uuid-char")
    @Column(name = "tag_id")
    private UUID tagId;

    @ApiModelProperty("NDR分类code")
    @Column(name = "nd_code")
    private String ndCode;

    @ApiModelProperty("NDR的pattern_path")
    @Column(name = "nd_path")
    private String ndPath;


    @ApiModelProperty("项目标识")
    @Column(name = "project_id")
    private Long projectId;

    @ApiModelProperty(value = "自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("创建人")
    @Column(name = "create_user_id")
    private Long createUserId;

    @ApiModelProperty("修改时间")
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty("修改人")
    @Column(name = "update_user_id")
    private Long updateUserId;

    @ApiModelProperty("状态：0-标签已被删除不可用，1-可用")
    @Column(name = "status")
    private Integer status;
}
