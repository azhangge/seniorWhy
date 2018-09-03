package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

import lombok.Data;

/**
 * @author way
 *         Created on 2016/6/21.
 */
@Data
public class ModuleSetting {
    private String name;
    private String type;
    private Integer status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
