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
 *         Created on 2016/5/23.
 */
@Entity
@Table(name = "banner")
@ApiModel(description = "广告横幅")
@Data
public class Banner {

    public static final Integer STATUS_ENABLE = 1;

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

    public Banner(){
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
    @ApiModelProperty(value = "Banner标题", required = true)
    @Column(name = "title")
    private String title;

    @Range(min=0,max = 1)
    @ApiModelProperty(value = "推荐状态：0-未推荐，1推荐")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "web banner 图片")
    @Type(type = "uuid-char")
    @Column(name = "web_store_object_id")
    private UUID webStoreObjectId;

    @ApiModelProperty(value = "app banner 图片")
    @Type(type = "uuid-char")
    @Column(name = "app_store_object_id")
    private UUID appStoreObjectId;


    @ApiModelProperty(value = "排序")
    @Column(name = "sort_number")
    private Integer sortNumber;

    @ApiModelProperty(value = "类型为url地址时的 webUrl")
    @Column(name = "web_url")
    private String webUrl;

    @ApiModelProperty(value = "类型为url地址时的 appUrl")
    @Column(name = "app_url")
    private String appUrl;

}
