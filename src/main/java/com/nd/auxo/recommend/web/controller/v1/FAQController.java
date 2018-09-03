package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.service.FAQManageService;
import com.nd.auxo.recommend.web.controller.v1.faqmanage.FAQManageVo;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
@Api("FAQ管理")
@RestController
@RequestMapping("")
public class FAQController {

    @Autowired
    private FAQManageService faqManageService;

    @ApiOperation("创建问题")
    @RequestMapping(value = "/v1/recommends/faq", method = RequestMethod.POST)
    public FAQManageVo create(@ApiParam("FAQ问题对象") @RequestBody FAQManageVo FAQCreateVo){

        return this.faqManageService.create(FAQCreateVo);
    }

    @ApiOperation("编辑问题")
    @RequestMapping(value = "/v1/recommends/faq/{id}", method = RequestMethod.PUT)
    public FAQManageVo update(@ApiParam("FAQ问题标识") @PathVariable("id") UUID id,
                              @ApiParam("FAQ问题对象") @Valid @RequestBody FAQManageVo FAQUpdateVo){

        return this.faqManageService.update(id, FAQUpdateVo);
    }

    @ApiOperation("删除问题")
    @RequestMapping(value = "/v1/recommends/faq/{id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("FAQ问题标识") @PathVariable("id") UUID id){

        this.faqManageService.delete(id);
    }

    @ApiOperation("查询问题")
    @RequestMapping(value = "/v1/recommends/faq", method = RequestMethod.GET)
    public PageResult<FAQManageVo> search(@ApiParam("FAQ问题名称") @RequestParam(value = "question_name",required = false) String questionName,
                                          @ApiParam("FAQ回答内容") @RequestParam(value = "query_text",required = false) String queryText,
                                          @ApiParam("FAQ问题类型") @RequestParam(value = "question_type",required = false) Integer questionType,
                                          @ApiParam("FAQ自定义类型") @RequestParam(value = "custom_type",required = false) String customType,
                                          @ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                          @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size){

        PageResult<FAQManageVo> pageResult = faqManageService.search(questionName, queryText, questionType, customType, page, size);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        return pageResult;
    }

    @ApiOperation("拖拽更改排序")
    @RequestMapping(value = "/v1/recommends/faq/{id}/move", method = RequestMethod.PUT)
    public void move(@ApiParam("FAQ问题标识") @PathVariable("id") UUID id,
                              @ApiParam("新的排序位置") @RequestParam("sort_number") Integer sortNumber){

        this.faqManageService.move(id, sortNumber);
    }
}
