package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 学员
 * <p/>
 * Created by jsc on 2016/7/19.
 */
@Entity
@Table(name = "user_info")
@ApiModel(description = "学员")
@Data
public class UserInfo {

    @Id
    @ApiModelProperty("学员标识")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty("真名")
    @Column(name = "real_name")
    private String realName;

    @ApiModelProperty("身份证号")
    @Column(name = "id_card")
    private String idCard;

    @ApiModelProperty("帐号")
    @Column(name = "account")
    private String account;

    @ApiModelProperty("手机号")
    @Column(name = "mobile")
    private String mobile;

    @ApiModelProperty("组织标识")
    @Column(name = "org_id")
    private Long orgId;

    @ApiModelProperty("组织名称")
    @Column(name = "org_name")
    private String orgName;

    @ApiModelProperty("注册时间")
    @Column(name = "reg_date")
    private Date regDate;

    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;
}
