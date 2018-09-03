package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/6/12.
 */
@Entity
@Table(name = "message")
@ApiModel(description = "消息")
@Data
public class Message {
    /*
     <p>
          id: id（消息id）与linkId(消息网关的id)一致，
         因为分页条件查询是在消息网关实现所致。
     </p>
     <p>
        custom_type:目前并不需要，所以无此字段。其默认的值为CustomType.AUXO_OPERATE_MANAGE(运营管理)
      </p>
     */

    @Id
    @ApiModelProperty("标识（只读）")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

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


    @ApiModelProperty("对应消息中心的标识(创建、修改不需要传值)")
    @Column(name = "link_id")
    @Type(type = "uuid-char")
    private UUID linkId;

    @ApiModelProperty("消息跳转类型")
    @Column(name = "jump_type")
    private String jumpType;

    @ApiModelProperty("消息跳转参数")
    @Column(name = "jump_param")
    private String jumpParam;

}
