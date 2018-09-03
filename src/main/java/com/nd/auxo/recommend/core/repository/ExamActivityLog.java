package com.nd.auxo.recommend.core.repository;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 考试活动日志
 * <p/>
 * Created by jsc on 2016/7/22.
 */
@Entity
@Table(name = "exam_activity_log")
@Data
public class ExamActivityLog {

    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("MQ消息内容")
    @Column(name = "message")
    private String message;

    @ApiModelProperty("处理状态：0失败，1成功")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty("用户标识")
    @Column(name = "user_id")
    private Long userId;

    @ApiModelProperty("活动标识")
    @Column(name = "activity_id")
    private String activityId;

    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("日志内容")
    @Column(name = "content")
    private String content;

    @ApiModelProperty("任务类型")
    @Column(name = "task_type")
    private Integer taskType;


    public enum Status {
        FAILURE,
        SUCCESS;
    }
}
