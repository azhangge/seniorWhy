package com.nd.auxo.recommend.core.api.pbl.repository;

import lombok.Data;

/**
 * Created by way on 2016/7/21.
 */
@Data
public class TriggerEvent {

    private String eventSource;
    private String uid;
    private String uniqueNo;
    private EventParam params;
}
