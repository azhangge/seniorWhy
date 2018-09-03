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
 * Created by Administrator on 2016/11/14 0014.
 */
@Entity
@Table(name = "recommend_faq")
@ApiModel(description = "faq管理")
@Data
public class FAQManage {

    @Id
    @ApiModelProperty("标识（只读）")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @ApiModelProperty("项目id")
    @Column(name = "project_id")
    private Long projectId;

    @ApiModelProperty("问题名称")
    @Column(name = "question_name")
    private String questionName;

    @ApiModelProperty("问题回答")
    @Column(name = "question_answer")
    private String questionAnswer;

    @ApiModelProperty("回答非html部分")
    @Column(name = "query_text")
    private String queryText;

    @ApiModelProperty("问题类型(0 选课问题,1 常见问题)")
    @Column(name = "question_type")
    private Integer  questionType;

    @ApiModelProperty("自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @ApiModelProperty("提问者id")
    @Column(name = "ask_user_id")
    private Long askUserId;

    @ApiModelProperty("回答者id")
    @Column(name = "answer_user_id")
    private Long answerUserId;

    @ApiModelProperty("问题创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty("问题排序号")
    @Column(name = "sort_number")
    private Integer sortNumber;

}
