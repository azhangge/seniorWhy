package com.nd.auxo.recommend.core.service.internal;


import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.FAQManage;
import com.nd.auxo.recommend.core.repository.FAQManageRepository;
import com.nd.auxo.recommend.core.service.FAQManageService;
import com.nd.auxo.recommend.core.util.WordUtils;
import com.nd.auxo.recommend.web.controller.v1.faqmanage.FAQManageVo;

import com.nd.gaea.SR;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
@Service
public class FAQManageServiceImpl extends AbstractService<FAQManage,UUID> implements FAQManageService {

    @Autowired
    private FAQManageRepository faqManageRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(FAQManageServiceImpl.class);

    public FAQManageVo convert(FAQManage param){

        FAQManageVo faqManageVo=new FAQManageVo();
        faqManageVo.setId(param.getId());
        faqManageVo.setQuestionType(param.getQuestionType());
        faqManageVo.setQuestionName(param.getQuestionName());
        faqManageVo.setCustomType(param.getCustomType());
        faqManageVo.setSortNumber(param.getSortNumber());
        faqManageVo.setCreateTime(param.getCreateTime());
        faqManageVo.setQuestionAnswer(param.getQuestionAnswer());
        return faqManageVo;
    }

    @Override
    public Repository<FAQManage, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(this.faqManageRepository);
    }

    @Override
    public FAQManageVo create(@Valid FAQManageVo createVo) {

        FAQManage faqManage=new FAQManage();
        if (createVo==null){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND));
        }
        if (StringUtils.isBlank(createVo.getQuestionName()) || StringUtils.isBlank(createVo.getCustomType()) || createVo.getQuestionType()==null){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (StringUtils.isNotBlank(createVo.getQuestionName()) && createVo.getQuestionName().length()>4000){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (StringUtils.isNotBlank(createVo.getQuestionAnswer()) && createVo.getQuestionAnswer().length()>4000){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (createVo.getCustomType()!=null){
            faqManage.setCustomType(createVo.getCustomType());
        }
        if (createVo.getQuestionName()!=null){
            faqManage.setQuestionName(createVo.getQuestionName());
            faqManage.setAskUserId(GaeaContext.getUserId());
        }
        if (createVo.getQuestionAnswer()!=null){
            faqManage.setQuestionAnswer(createVo.getQuestionAnswer());
            faqManage.setAnswerUserId(GaeaContext.getUserId());
            faqManage.setQueryText(WordUtils.unEscapeHtml(WordUtils.removeHtml(createVo.getQuestionAnswer())));
        }
        if (createVo.getQuestionType()!=null){
            faqManage.setQuestionType(createVo.getQuestionType());
        }
        if (this.faqManageRepository.findCount(GaeaContext.getAppId())==0) {
             faqManage.setSortNumber(1);
        }else {
            faqManage.setSortNumber(this.faqManageRepository.findMaxSortNumber(GaeaContext.getAppId()) + 1);
        }
        faqManage.setCreateTime(new Date());
        faqManage.setId(UUID.randomUUID());
        faqManage.setProjectId(GaeaContext.getAppId());
        faqManage=this.faqManageRepository.save(faqManage);
        LOGGER.info("创建FAQ\""+"("+faqManage.getId()+")\"");
        return convert(faqManage);
    }

    @Override
    public FAQManageVo update(UUID id, FAQManageVo updateVo) {

        FAQManage faqManage=this.faqManageRepository.findOne(id);
        if (faqManage == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND));
        }
        if (StringUtils.isBlank(updateVo.getQuestionName()) || StringUtils.isBlank(updateVo.getCustomType()) || updateVo.getQuestionType()==null){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (StringUtils.isNotBlank(updateVo.getQuestionName()) && updateVo.getQuestionName().length()>4000){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (StringUtils.isNotBlank(updateVo.getQuestionAnswer()) && updateVo.getQuestionAnswer().length()>4000){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        if (updateVo.getQuestionType()!=null){
            faqManage.setQuestionType(updateVo.getQuestionType());
        }
        if (updateVo.getCustomType()!=null) {
            faqManage.setCustomType(updateVo.getCustomType());
        }
        if (updateVo.getQuestionName()!=null) {
            faqManage.setQuestionName(updateVo.getQuestionName());
        }
        if (updateVo.getQuestionAnswer()!=null) {
            faqManage.setQuestionAnswer(updateVo.getQuestionAnswer());
            faqManage.setQueryText(WordUtils.unEscapeHtml(WordUtils.removeHtml(updateVo.getQuestionAnswer())));
        }
        faqManage=this.faqManageRepository.save(faqManage);
        LOGGER.info("更新FAQ\""+"("+faqManage.getId()+")\"");
        return convert(faqManage);
    }

    @Override
    public void delete(UUID id) {

        FAQManage faqManage=this.faqManageRepository.findOne(id);
        if (faqManage == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND,id));
        }
        this.faqManageRepository.delete(id);
        LOGGER.info("删除FAQ\"" + "(" + faqManage.getId() + ")\"");
    }

    @Override
    public PageResult<FAQManageVo> search(String questionName, String queryText, Integer questionType, String customType, int page, int size) {

        PageResult<FAQManageVo> result=new PageResult<>();
        List<FAQManageVo> list=new ArrayList<>();
        Long projectId=GaeaContext.getAppId();
        PagerResult<FAQManage> faqManageResult=this.faqManageRepository.listPages(projectId,questionName,queryText,questionType,customType,page,size);
        for (FAQManage faqManage:faqManageResult.getItems()){
            list.add(convert(faqManage));
        }
        result.setItems(list);
        result.setCount(faqManageResult.getTotal());
        return result;
    }

    @Override
    public void move(UUID id, Integer sortNumber) {

        FAQManage faqManage=this.faqManageRepository.findOne(id);
        if (faqManage == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND));
        }
        if (faqManage.getSortNumber()==null){
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND));
        }
        Integer oldSortNumber = faqManage.getSortNumber();
        if (sortNumber.compareTo(oldSortNumber) == 0){
            return;
        }else if (sortNumber.compareTo(oldSortNumber) < 0){
            this.faqManageRepository.moveSortNumberByIdAndGeSortNumberAndLeSortNumber(1, id, sortNumber, oldSortNumber);
        }else {
            this.faqManageRepository.moveSortNumberByIdAndGeSortNumberAndLeSortNumber(-1, id, oldSortNumber, sortNumber);
        }
        faqManage.setSortNumber(sortNumber);
        this.faqManageRepository.save(faqManage);
        LOGGER.info("移动FAQ排序\"" + "(" + faqManage.getId() + ")\"");
    }
}
