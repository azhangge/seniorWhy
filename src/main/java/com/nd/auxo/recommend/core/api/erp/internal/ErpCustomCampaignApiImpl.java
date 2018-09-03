package com.nd.auxo.recommend.core.api.erp.internal;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.constant.ErpVirtualType;
import com.nd.auxo.recommend.core.api.erp.repository.CustomCampaign;
import com.nd.auxo.recommend.core.api.erp.repository.ErpCustomCampaignRepository;
import com.nd.auxo.recommend.core.api.erp.repository.ErpFinishResponse;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import com.nd.gaea.client.http.WafHttpClient;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.client.support.WafClientContextHolder;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.DateUtils;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by way on 2016/7/18.
 */
@Service
public class ErpCustomCampaignApiImpl implements ErpCustomCampaignApi {

    @Autowired
    private ErpCustomCampaignRepository erpCustomCampaignRepository;

    @Value("${erp.server.url}")
    private String erpServerUrl;


    private WafHttpClient wafHttpClient = new WafHttpClient();



    @Override
    public CustomCampaign create(CustomCampaign customCampaign) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_GX);
            return erpCustomCampaignRepository.create(customCampaign.getOrgId(), customCampaign);
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
        }
    }

    @Override
    public CustomCampaign update(String id, CustomCampaign customCampaign) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_GX);
            return erpCustomCampaignRepository.update(id, customCampaign.getOrgId(), customCampaign.getAppKey(), customCampaign);
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
        }
    }

    @Override
    public ErpResponse addPointAndExp(ErpVirtualType type, Integer virtualAmount, Long userId, Long orgId) {
        String url = erpServerUrl + "/ServiceHost/ToDoList/json/AddPointAndExp?" +
                "virtual_type=" + type.getValue() + "&virtual_amount=" + virtualAmount + "" +
                "&uid=" + userId + "&orgid=" + orgId + "&source_type=40";
//        Map<String,String> headerMap = new HashMap<>();
//        headerMap.put("Nd-CompanyOrgId",""+orgId);
        ResponseEntity<ErpResponse> responseEntity = this.executeHttp(url, HttpMethod.POST, ErpResponse.class,null);
        return responseEntity.getBody();
    }

    @Override
    public ErpResponse addPointAndExpRetry(ErpVirtualType type, Integer virtualAmount, Long userId, Long orgId, String rawId) {
        String url = erpServerUrl + "/ServiceHost/ToDoList/json/AddPointAndExp?" +
                "virtual_type=" + type.getValue() + "&virtual_amount=" + virtualAmount + "" +
                "&uid=" + userId + "&orgid=" + orgId +"&rawId="+ rawId + "&source_type=40";
        ResponseEntity<ErpResponse> responseEntity = this.executeHttp(url, HttpMethod.POST, ErpResponse.class,null);
        return responseEntity.getBody();
    }

    @Override
    public ErpFinishResponse finishCampaign(Long orgId, Long campaignId, Long uid, Date taskDate, String appKey) {
        String url = erpServerUrl + "/ServiceHost/ToDoList/json/FinishCampaign?CustomContentType=jsonIsoDateFormat" +
                "&orgId="+orgId+"&campaignId="+campaignId+"&uid="+uid+"&taskDate="+ DateUtils.format(taskDate,"yyyy-MM-dd'T'HH:mm:ss.SSS")+"&appkey="+appKey ;
        ResponseEntity<ErpFinishResponse> responseEntity =  this.executeHttp(url, HttpMethod.POST, ErpFinishResponse.class,null);
        return responseEntity.getBody();
    }

    /**
     * @param url
     * @param httpMethod
     * @param responseType
     * @param uriVariables
     * @param <T>
     * @return
     */
    private <T> ResponseEntity<T> executeHttp(String url, HttpMethod httpMethod, Type responseType, Map<String,String> headerMap,Object... uriVariables) {
        //共享平台
        GaeaToken gaeaToken = GaeaContext.getGaeaToken();
        String gt = null;
        if (gaeaToken != null) {
            gt = gaeaToken.getGaeaToken();

        }
        if (StringUtils.isEmpty(gt)) {
            gt = WafClientContextHolder.getToken().getBearerToken();
        }
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set("Authorization", "Bearer \"" + gt + "\"");
        headers.set("Content-Type", "application/json");
        if(headerMap != null && !headerMap.isEmpty()){
            for(Map.Entry<String,String> entry:headerMap.entrySet()){
                headers.set(entry.getKey(),entry.getValue());
            }
        }
        HttpEntity requestEntity = new HttpEntity<>(headers);
        return wafHttpClient.executeForEntity(url, httpMethod, requestEntity, responseType, uriVariables);
    }
}
