package com.nd.auxo.recommend.core.repository.course;

import com.nd.auxo.sdk.recommend.vo.course.RecommendCourseDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author way
 *         Created on 2016/5/24.
 */
@Entity
@Table(name = "recommend_course_detail")
@ApiModel(description = "课程推荐明细")
@Data
public class RecommendCourseDetail {


    @Id
    @ApiModelProperty("标识（只读）")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty("推荐课程id")
    @Type(type = "uuid-char")
    @Column(name = "recommend_id")
    private UUID recommendId;


    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "自定义类型")
    @Column(name = "custom_type")
    private String customType;

    @Length(max = 50)
    @NotBlank
    @ApiModelProperty(value = "自定义ID")
    @Column(name = "custom_id")
    private String customId;


    @ApiModelProperty(value = "排序")
    @Column(name = "sort_number")
    private Integer sortNumber;

    public static RecommendCourseDetail convert(RecommendCourseDetailVo vo) {
        RecommendCourseDetail recommendCourseDetail = new RecommendCourseDetail();
        recommendCourseDetail.setCustomType(vo.getCustomType());
         recommendCourseDetail.setSortNumber(vo.getSortNumber());
        recommendCourseDetail.setCustomId(vo.getCustomId());
        return recommendCourseDetail;
    }

    public RecommendCourseDetailVo convert() {
        RecommendCourseDetailVo recommendCourseDetailVo = new RecommendCourseDetailVo();
        recommendCourseDetailVo.setCustomType(this.getCustomType());
        recommendCourseDetailVo.setSortNumber(this.getSortNumber());
        recommendCourseDetailVo.setCustomId(this.getCustomId());
        return recommendCourseDetailVo;
    }
}
