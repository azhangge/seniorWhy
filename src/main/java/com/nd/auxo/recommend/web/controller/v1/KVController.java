package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.core.api.log.LogForCreate;
import com.nd.auxo.recommend.core.api.log.RecommendSdkLogManageApi;
import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.KVInfo;
import com.nd.auxo.recommend.core.repository.param.KVSearchParam;
import com.nd.auxo.recommend.core.service.KVService;
import com.nd.auxo.recommend.core.util.IpUtils;
import com.nd.auxo.sdk.recommend.vo.kv.KVInfoVo;
import com.nd.gaea.SR;
import com.nd.gaea.core.exception.BaseRuntimeException;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.UUID;

/**
 * KV键值服务
 *
 * @author wbh
 * @version latest
 * @date 2016/08/12
 */
@Api("键值系统")
@RestController
@RequestMapping("")
public class KVController {

    private static final Logger LOGGER = LoggerFactory.getLogger(KVController.class);

    @Autowired
    private KVService kvService;

    @Autowired
    private RecommendSdkLogManageApi logManageApi;


    @ApiOperation("根据key获取键值")
    @RequestMapping(value = "/v1/recommends/kvs/{key}", method = RequestMethod.GET)
    public KVInfoVo get(@ApiParam("主键") @PathVariable("key") String key,
                        @ApiParam("隔离策略：0(项目隔离),1(全站共享)，2(虚拟项目隔离),默认值0") @RequestParam(value = "isolation_strategy", required = false, defaultValue = "0") Integer isolationStrategy,
                        @ApiParam("隔离参数") @RequestParam(value = "isolation_param", required = false) String isolationParam) {
        KVInfo info = this.kvService.getValue(key, KVInfo.IsolationStrategy.getByValue(isolationStrategy)
                , this.kvService.getRealIsolationParam(isolationStrategy, isolationParam));
        return this.kvService.convert(info);
    }

    @ApiOperation("创建键值")
    @RequestMapping(value = "/v1/recommends/kvs", method = RequestMethod.POST)
    public KVInfoVo create(@ApiParam("KV配置") @RequestBody KVInfoVo info) {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        if (info.getIsolationStrategy() == null) {
            throw new ArgumentValidationException(" isolation_strategy empty");
        }
        info.setGroupKey("" + GaeaContext.getUserId());
        info.setIsolationParam(this.kvService.getRealIsolationParam(info.getIsolationStrategy(), info.getIsolationParam()));
        KVInfoVo vo = this.kvService.convert(this.kvService.create(this.kvService.convert(info)));
        if (KVInfo.IsolationStrategy.PROJECT_SINGLE.getValue().equals(vo.getIsolationStrategy())) {
            LOGGER.info("添加隐私级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"");
        } else if (KVInfo.IsolationStrategy.PROJECT_VIRTUAL.getValue().equals(vo.getIsolationStrategy())) {
            LOGGER.info("添加虚拟级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"");
        } else {
            createLog("添加共享级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"", "POST");
        }
        return vo;
    }


    @ApiOperation("更新键值")
    @RequestMapping(value = "/v1/recommends/kvs/{id}", method = RequestMethod.PUT)
    public KVInfoVo update(@ApiParam("主键") @PathVariable("id") UUID id
            , @ApiParam("KV配置") @RequestBody KVInfoVo info) {
        if (GaeaContext.getUserId() == null) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_NEED_AUTH));
        }
        KVInfo kvInfo = this.kvService.findOne(id);
        if (kvInfo == null) {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, id));
        }
        Integer oldIsolationStrategy = kvInfo.getIsolationStrategy();
        String oldIsolationParam = kvInfo.getIsolationParam();
        if (info.getIsolationStrategy() == null) {
            throw new ArgumentValidationException(" isolation_strategy empty");
        }
        if (StringUtils.isBlank(info.getKey())) {
            throw new ArgumentValidationException(" key empty");
        }
        kvInfo.setGroupKey("" + GaeaContext.getUserId());
        kvInfo.setIsolationParam(this.kvService.getRealIsolationParam(info.getIsolationStrategy(), info.getIsolationParam()));
        kvInfo.setValue(info.getValue());
        kvInfo.setRemark(info.getRemark());
        kvInfo.setIsolationStrategy(info.getIsolationStrategy());
        KVInfoVo vo = this.kvService.convert(this.kvService.update(info.getKey(), kvInfo, oldIsolationStrategy, oldIsolationParam));
        if (KVInfo.IsolationStrategy.PROJECT_SINGLE.getValue().equals(vo.getIsolationStrategy())) {
            LOGGER.info("编辑隐私级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"");
        } else if (KVInfo.IsolationStrategy.PROJECT_VIRTUAL.getValue().equals(vo.getIsolationStrategy())) {
            LOGGER.info("编辑虚拟级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"");
        } else {
            createLog("编辑共享级在线参数\"" + vo.getKey() + "(" + vo.getId() + ")\"", "PUT");
        }
        return vo;
    }

    @ApiOperation("删除键值")
    @RequestMapping(value = "/v1/recommends/kvs/{id}", method = RequestMethod.DELETE)
    public void delete(@ApiParam("主键") @PathVariable("id") UUID id) throws BaseRuntimeException {
        KVInfo info = this.kvService.findOne(id);
        if (info != null) {
            this.kvService.deleteId(id, info.getKey(), info.getIsolationStrategy(), info.getIsolationParam());
            if (KVInfo.IsolationStrategy.PROJECT_SINGLE.getValue().equals(info.getIsolationStrategy())) {
                LOGGER.info("删除隐私级在线参数\"" + info.getKey() + "(" + info.getId() + ")\"");
            } else if (KVInfo.IsolationStrategy.PROJECT_VIRTUAL.getValue().equals(info.getIsolationStrategy())) {
                LOGGER.info("删除虚拟级在线参数\"" + info.getKey() + "(" + info.getId() + ")\"");
            } else {
                createLog("删除共享级在线参数\"" + info.getKey() + "(" + info.getId() + ")\"", "DELETE");
            }
        } else {
            throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, id));
        }
    }

    @ApiOperation("列表查询")
    @RequestMapping(value = "/v1/recommends/kvs", method = RequestMethod.GET)
    public PageResult<KVInfoVo> listPages(
            @ApiParam("键") @RequestParam(value = "key", required = false) String key,
            @ApiParam("分组键") @RequestParam(value = "group_key", required = false) String groupKey,
            @ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        KVSearchParam param = new KVSearchParam();
        param.setProjectId(GaeaContext.getAppId());
        param.setKey(key);
        param.setGroupKey(groupKey);
        param.setPage(page);
        param.setSize(size);
        PageResult<KVInfoVo> pageResult = kvService.listPages(param);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            PageResult result = new PageResult();
            result.setCount(0);
            result.setItems(new ArrayList());
            return result;
        }
        return pageResult;
    }


    private void createLog(String message, String method) {
        HttpServletRequest request = GaeaContext.getRequest();
        LogForCreate logForCreate = new LogForCreate();
        logForCreate.setIpAddress(IpUtils.getIp(request));
        logForCreate.setLogger("KVController");
        logForCreate.setMethod(method);
        logForCreate.setUri(request.getRequestURI());
        logForCreate.setSourceType(logForCreate.SOURCE_TYPE_SYSTEM);
        logForCreate.setMessage(message);
        this.logManageApi.create(logForCreate);
    }
}
