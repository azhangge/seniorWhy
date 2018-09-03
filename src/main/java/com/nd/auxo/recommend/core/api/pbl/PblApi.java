package com.nd.auxo.recommend.core.api.pbl;

import com.nd.auxo.recommend.core.api.pbl.repository.PblResponse;
import com.nd.auxo.recommend.core.api.pbl.repository.TriggerEvent;

/**
 * Created by way on 2016/7/21.
 */

public interface PblApi {

    PblResponse doOneDay3Questions(String appName, Integer appType, TriggerEvent triggerEvent);
}
