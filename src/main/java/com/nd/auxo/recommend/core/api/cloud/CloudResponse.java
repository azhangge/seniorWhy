package com.nd.auxo.recommend.core.api.cloud;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by way on 2016/10/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CloudResponse<T> {

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Data")
    private T data;

}
