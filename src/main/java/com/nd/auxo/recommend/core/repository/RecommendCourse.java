package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Entity
@Table(name = "recommend_course")
@ApiModel(description = "课程推荐")
@Data
public class RecommendCourse {


    public static final String FIELD_SORT_NUMBER = "sortNumber";


    @Id
    @ApiModelProperty("标识（只读）")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @ApiModelProperty("项目标识")
    @Column(name = "project_id")
    private Long projectId;


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

    public RecommendCourse(){
        this.id = UUID.randomUUID();
    }

    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @Length(max = 50)
    @ApiModelProperty(value = "自定义ID")
    @Column(name = "custom_id")
    private String customId;


    @NotBlank
    @Length(max = 50)
    @ApiModelProperty(value = "推荐名称", required = true)
    @Column(name = "title")
    private String title;


    @Range(min=0,max = 1)
    @ApiModelProperty(value = "推荐状态：0-上线，1下线")
    @Column(name = "status")
    private Integer status;


    @ApiModelProperty(value = "排序")
    @Column(name = "sort_number")
    private Integer sortNumber;

    @ApiModelProperty(value = "自定义的排序设置  1最新 2最热 0综合")
    @Column(name = "custom_order_by")
    private Integer customOrderBy;


    @Column(name = "custom_id_type")
    @ApiModelProperty(value = "自定义id对应的类型，root_tag=根标签")
    private String customIdType;

    @Column(name = "recommend_mode")
    @ApiModelProperty(value = "推荐模式:0--标签推荐 1--自定义推荐")
    private Integer recommendMode;

}
