package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

import lombok.Data;

/**
 * @author way
 *         Created on 2016/5/17.
 */
@Data
public class Language {
    private Integer sortNum;
    private String code;
    private String description;

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
