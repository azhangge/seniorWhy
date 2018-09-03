package com.nd.auxo.recommend.web.rabbitmq.message;

import com.nd.auxo.recommend.web.rabbitmq.constant.ExamOperateType;
import com.nd.auxo.recommend.web.rabbitmq.constant.ExamResourceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>测评消息</p>
 *
 * @author zhangchh
 * @version latest
 * @date 2016/7/18
 */
@Data
public class ExamMessage implements Serializable {
    @ApiModelProperty("操作类型")
    private ExamOperateType operateType;
    @ApiModelProperty("资源类型")
    private ExamResourceType resourceType;
    @ApiModelProperty("应用标识")
    private Long appId;
    @ApiModelProperty("业务标识")
    private String customId;
    @ApiModelProperty("创建来源")
    private String customType;
    @ApiModelProperty("测评标识")
    private UUID examId;
    @ApiModelProperty("用户标识")
    private Long userId;
    @ApiModelProperty("自定义数据")
    private String customData;
    @ApiModelProperty("数据")
    private HashMap<String, Object> data;

}
