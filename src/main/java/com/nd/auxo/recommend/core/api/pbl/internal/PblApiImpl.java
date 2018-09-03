package com.nd.auxo.recommend.core.api.pbl.internal;

import com.nd.auxo.recommend.core.api.pbl.PblApi;
import com.nd.auxo.recommend.core.api.pbl.repository.PblResponse;
import com.nd.auxo.recommend.core.api.pbl.repository.TriggerEvent;
import com.nd.auxo.recommend.core.constant.OrganizationType;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by way on 2016/7/18.
 */
@Service
public class PblApiImpl implements PblApi {

    @Value("${pbl.server.url}")
    private String pbl4eventUrl;

    private WafSecurityHttpClient pblHttpClient = new WafSecurityHttpClient();

    @Override
    public PblResponse doOneDay3Questions(String appName, Integer appType, TriggerEvent triggerEvent) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_GX);
            ResponseEntity<PblResponse> responseEntity;
            HttpHeaders headers = new HttpHeaders();
            if (appType == OrganizationType.VIRTUAL.getValue()) {
                headers.add("vorg", appName);
                GaeaContext.put(GaeaContext.CUSTOM_HTTP_HEADER, headers);
            }
            String url = pbl4eventUrl + "/v0.9/app/{appName}/trigger/event/doOneDay3Questions";
            responseEntity = pblHttpClient.postForEntity(url, triggerEvent, PblResponse.class, appName);
            return responseEntity.getBody();
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
            GaeaContext.put(GaeaContext.CUSTOM_HTTP_HEADER, null);
        }
    }
}
