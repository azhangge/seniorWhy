package com.nd.auxo.recommend.web.controller.v1.course.vo;

import com.nd.elearning.sdk.ndr.bean.ChapterVo;
import lombok.Data;

import java.util.List;

/**
 * Created by way on 2016/10/31.
 */
@Data
public class ChapterTree extends ChapterVo{

    public List<ChapterTree> chapterTreeList;

    public void convert(ChapterVo vo) {
        this.setIdentifier(vo.getIdentifier());
        this.setDescription(vo.getDescription());
        this.setTeachingMaterial(vo.getTeachingMaterial());
        this.setParent(vo.getParent());
        this.setTitle(vo.getTitle());
    }

}
