package com.nd.auxo.recommend.core.service;

import com.nd.auxo.recommend.core.cache.RedisCacheNames;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.KVInfo;
import com.nd.auxo.recommend.core.repository.KVRepository;
import com.nd.auxo.recommend.core.repository.param.KVSearchParam;
import com.nd.auxo.recommend.web.rabbitmq.message.RecommendKVMessage;
import com.nd.auxo.recommend.web.rabbitmq.producer.RecommendProduce;

import com.nd.auxo.sdk.recommend.vo.kv.KVInfoVo;
import com.nd.gaea.SR;
import com.nd.gaea.context.service.DomainService;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.uranus.common.exception.AlreadyUsedException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * KV键值对管理系统服务
 *
 * @author wbh
 * @version latest
 * @date 2016/08/12
 */
@Service
@DomainService
@CacheConfig(cacheNames = {RedisCacheNames.KV_SYSTEM_LIST})
public class KVService {

    @Autowired
    private KVRepository kvRepository;
    @Autowired
    private RecommendProduce recommendProduce;
    /**
     * 根据主键查询配置数据
     *
     * @param key 主键
     * @return 数据
     */
    @Cacheable(value = RedisCacheNames.KV_SYSTEM_LIST, key = "#strategy.getValue()+'_'+#isolationParam+'_'+#key")
    public KVInfo getValue(String key, KVInfo.IsolationStrategy strategy, String isolationParam) {
        KVInfo info = this.kvRepository.findByKeyAndIsolationStrategyAndIsolationParam(key, strategy.getValue(), isolationParam);
        if (info == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, key + "," + strategy.getValue() + "," + isolationParam));
        }
        return info;
    }

    /**
     * 删除主键配置内容
     *
     * @param isolationStrategy
     * @param key               主键
     */
    @CacheEvict(value = RedisCacheNames.KV_SYSTEM_LIST, key = "#isolationStrategy+'_'+#isolationParam+'_'+#key")
    public void deleteId(UUID id, String key, Integer isolationStrategy, String isolationParam) {
        this.kvRepository.delete(id);
        //删除键值发送消息
        RecommendKVMessage recommendKVMessage =new RecommendKVMessage();
        recommendKVMessage.setKVId(id);
        recommendKVMessage.setOperateType("delete");
        recommendKVMessage.setIsolationStrategy(isolationStrategy);
        recommendKVMessage.setKVKey(key);
        recommendKVMessage.setUserId(GaeaContext.getUserId());
        this.recommendProduce.send(recommendKVMessage);
    }

    /**
     * 保存主键配置内容
     *
     * @param info KV配置
     * @return KV配置
     */
    public KVInfo create(KVInfo info) {
        if (this.kvRepository.findByKeyAndIsolationStrategyAndIsolationParam(info.getKey()
                , info.getIsolationStrategy()
                , info.getIsolationParam()) != null) {
            throw new AlreadyUsedException(SR.getString(I18nExceptionMsgConstant.KV_ALREADY_USED));
        }
        info.setId(UUID.randomUUID());
        info.setUpdateTime(new Date());
        return this.kvRepository.save(info);
    }

    /**
     * 保存主键配置内容
     *
     * @param key                  主键
     * @param info                 KV配置
     * @param oldIsolationStrategy
     * @param oldIsolationParam    @return KV配置
     */
    @CacheEvict(value = RedisCacheNames.KV_SYSTEM_LIST, key = "#oldIsolationStrategy+'_'+#oldIsolationParam+'_'+#key")
    public KVInfo update(String key, KVInfo info, Integer oldIsolationStrategy, String oldIsolationParam) {
        KVInfo info1 = this.kvRepository.findByKeyAndIsolationStrategyAndIsolationParam(info.getKey()
                , info.getIsolationStrategy()
                , info.getIsolationParam());
        if (info1 != null && !info.getId().equals(info1.getId())) {
            throw new AlreadyUsedException(SR.format(I18nExceptionMsgConstant.KV_ALREADY_USED));
        }
        info.setUpdateTime(new Date());
        KVInfo kvInfo= this.kvRepository.save(info);
        //更新键值发送消息
        RecommendKVMessage recommendKVMessage =new RecommendKVMessage();
        recommendKVMessage.setKVId(kvInfo.getId());
        recommendKVMessage.setOperateType("update");
        recommendKVMessage.setIsolationStrategy(kvInfo.getIsolationStrategy());
        recommendKVMessage.setKVKey(kvInfo.getKey());
        recommendKVMessage.setUserId(GaeaContext.getUserId());
        this.recommendProduce.send(recommendKVMessage);

        return kvInfo;
    }

    public KVInfo convert(KVInfoVo kvInfoVo) {
        KVInfo kVInfo = new KVInfo(kvInfoVo.getKey());
        kVInfo.setIsolationParam(kvInfoVo.getIsolationParam());
        kVInfo.setIsolationStrategy(kvInfoVo.getIsolationStrategy());
        kVInfo.setValue(kvInfoVo.getValue());
        kVInfo.setGroupKey(kvInfoVo.getGroupKey());
        kVInfo.setUpdateTime(kvInfoVo.getUpdateTime());
        kVInfo.setKey(kvInfoVo.getKey());
        kVInfo.setRemark(kvInfoVo.getRemark());
        kVInfo.setId(kvInfoVo.getId());
        return kVInfo;
    }

    public KVInfoVo convert(KVInfo kvInfo) {
        KVInfoVo kVInfoVo = new KVInfoVo();
        kVInfoVo.setIsolationParam(kvInfo.getIsolationParam());
        kVInfoVo.setIsolationStrategy(kvInfo.getIsolationStrategy());
        kVInfoVo.setValue(kvInfo.getValue());
        kVInfoVo.setGroupKey(kvInfo.getGroupKey());
        kVInfoVo.setUpdateTime(kvInfo.getUpdateTime());
        kVInfoVo.setKey(kvInfo.getKey());
        kVInfoVo.setRemark(kvInfo.getRemark());
        kVInfoVo.setId(kvInfo.getId());
        return kVInfoVo;
    }

    public PageResult<KVInfoVo> listPages(KVSearchParam param) {
        PagerResult<KVInfo> kvInfoPage = this.kvRepository.listPages(param);
        if (CollectionUtils.isEmpty(kvInfoPage.getItems())) {
            return new PageResult<>();
        }
        List<KVInfoVo> voList = new ArrayList<>();
        for (KVInfo kvInfo : kvInfoPage.getItems()) {
            voList.add(this.convert(kvInfo));
        }
        PageResult pageResult = new PageResult();
        pageResult.setCount(kvInfoPage.getTotal());
        pageResult.setItems(voList);
        return pageResult;
    }

    /**
     * 获取实际的 分隔参数
     *
     * @param isolationStrategy
     * @param isolationParam
     * @return
     */
    public String getRealIsolationParam(Integer isolationStrategy, String isolationParam) {
        if (KVInfo.IsolationStrategy.PROJECT_SINGLE.getValue().equals(isolationStrategy)) {
            return "" + GaeaContext.getAppId();
        } else if (KVInfo.IsolationStrategy.PROJECT_ALL.getValue().equals(isolationStrategy)) {
            return "0";
        } else {
            return isolationParam;
        }
    }

    public KVInfo findOne(UUID id) {
        return this.kvRepository.findOne(id);
    }
}
