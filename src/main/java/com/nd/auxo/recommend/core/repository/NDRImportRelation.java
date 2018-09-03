package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by way on 2016/11/1.
 */
@Data
@Entity
@Table(name = "ndr_import_relation")
public class NDRImportRelation {
    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("ndr的教材id")
    @Type(type = "uuid-char")
    @Column(name = "ndr_id")
    private UUID ndrId;

    @ApiModelProperty("资源课程id")
    @Column(name = "course_id")
    @Type(type = "uuid-char")
    private UUID courseId;

    @ApiModelProperty("基础平台元课程id")
    @Column(name = "unit_id")
    private Integer unitId;


    @ApiModelProperty("项目标识")
    @Column(name = "project_id")
    private Long projectId;

    @ApiModelProperty(value = "自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @ApiModelProperty(value = "自定义ID")
    @Column(name = "custom_id")
    private String customId;

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
}
