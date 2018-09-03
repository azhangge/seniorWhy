package com.nd.auxo.recommend.core.api.oldelearning.repository.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author way
 *         Created on 2016/5/9.
 */
@Data
public class Project {

    private Long id;
    private String title;
    private String domain;
    private String description;
    private String coverUrl;
    private String solutionCode;
    private Integer status;
    private String logoUrl;
    private String faviconUrl;
    private String style;
    private String crmQq;
    private String crmPhone;
    private String offlineRemark;
    private Integer payAccountId;
    private String siteDomain;
    private Integer siteStatus;
    private String siteNavigateUrl;
    private Long client_id;
    private String clientSecret;
    private String orgId;
    private Integer orgType;
    private String ucOrgName;
    private String secretKey;
    private String footConfig;
    private Language language;
    //注册类型0为不开启,大于0为开启
    private Integer regType;
    //客服99u
    private String crm99u;
    /**
     * 项目移动应用配置信息
     */
    private List<MobileApp> mobileApps;

    /**
     * 我的学习中使用到的设置
     *
     */
    private List<ModuleSetting> moduleSettings;
    /**
     * 活动类型
     */
    private Integer activeType;

    /**
     * --组织标识
     */
    @JsonProperty("v_org_id")
    private Long vOrgId;

}
