package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.repository.FAQManage;
import com.nd.auxo.recommend.web.controller.v1.faqmanage.FAQManageVo;

import com.nd.gaea.context.service.EntityService;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;

import java.util.UUID;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public interface FAQManageService extends EntityService<FAQManage,UUID>{

    /**
     * 创建FAQ
     * @param createVo
     * @return
     */
    public FAQManageVo create(FAQManageVo createVo);


    /**
     * 更新FAQ
     * @param id
     * @param updateVo
     * @return
     */
    public FAQManageVo update(UUID id, FAQManageVo updateVo);


    /**
     * 删除FAQ
     * @param id
     */
    public void delete(UUID id);


    /**
     * 列表查询FAQ
     * @param questionName
     * @return
     */
    public PageResult<FAQManageVo> search(String questionName, String queryText, Integer questionType, String customType, int page, int size);


    /**
     * 拖拽更新排序号
     * @param id
     * @param sort_number
     */
    public void move(UUID id, Integer sort_number);
}
