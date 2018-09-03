package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

import lombok.Data;

/**
 * @author way
 *         Created on 2016/5/9.
 */
@Data
public class Channel {

    private Long id;
    private String type;
    private String title;
    private String alias;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
