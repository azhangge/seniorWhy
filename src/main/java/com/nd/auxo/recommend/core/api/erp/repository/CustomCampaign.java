package com.nd.auxo.recommend.core.api.erp.repository;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;

import java.util.Date;

/**
 * Created by way on 2016/7/19.
 */
public class CustomCampaign {
    /**
     * ID (integer, optional),
     * OrgId (integer, optional): UC的组织Id（必填） ,
     * RawId (integer, optional): （可选参数默认0）用于传递外部系统各自的id，方便后期查询 ,
     * AppKey (string, optional): 需要申请接入APP对应的appkey才允许接入 ,
     * Name (string, optional): 任务名称（必填） ,
     * BeginTime (string, optional): 任务开始时间（必填） ,
     * EndTime (string, optional): （可选参数，默认永久）任务结束时间 ,
     * WebUri (string, optional): (可选，默认空)web任务跳转地址 ,其中特殊占位符有#uckey#,#auth#
     * MobileUri (string, optional): （可选，默认空）手机端必须是cmp协议，cmp://xxxx ,
     * Enabled (boolean, optional): （可选，默认启用true）是否启用任务 ,
     * IsAll (boolean, optional): 是否对全员开放，要使用 ,
     * UcUids (string, optional): （可选）如果IsAll为false,则可以填有权限访问的人的uc的uid，以逗号分隔“,” ,
     * UcDepartmentIds (string, optional): （可选）如果IsAll为false,则可以填有权限访问的人的uc的nodeid，以逗号分隔“,” ,
     * FinishType (integer, optional): （选填，默认0 无完成状态） 任务完成类型0：无完成状态，1：一次性任务,2：每天完成的任务 ,
     * IsSignOut (boolean, optional): （选填，默认false无需完成也能日事日清）是否完成后才能日事日清 ,
     * ButtonTitle (string, optional): （选填，默认“进入”），工作助手跳转按钮的文字
     */
    @JsonProperty("ID")
    private Long id;

    @JsonProperty("OrgId")
    private Long orgId;

    @JsonProperty("RawId")
    private Long rawId;

    @JsonProperty("AppKey")
    private String appKey;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("BeginTime")
    private Date beginTime;

    @JsonProperty("EndTime")
    private Date endTime;

    @JsonProperty("WebUri")
    private String webUri;

    @JsonProperty("MobileUri")
    private String mobileUri;

    @JsonProperty("IsEnabled")
    private boolean isEnabled;

    @JsonProperty("IsAll")
    private boolean isAll;

    @JsonProperty("UcUids")
    private String ucUids;

    @JsonProperty("UcDepartmentIds")
    private String ucDepartmentIds;

    @JsonProperty("FinishType")
    private Integer finishType;

    @JsonProperty("IsSignOut")
    private boolean isSignOut;

    @JsonProperty("ButtonTitle")
    private String buttonTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getRawId() {
        return rawId;
    }

    public void setRawId(Long rawId) {
        this.rawId = rawId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginTime() {
        return (Date) beginTime.clone();
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = (Date) beginTime.clone();
    }

    public Date getEndTime() {
        return (Date) endTime.clone();
    }

    public void setEndTime(Date endTime) {
        this.endTime = (Date) endTime.clone();
    }

    public String getWebUri() {
        return webUri;
    }

    public void setWebUri(String webUri) {
        this.webUri = webUri;
    }

    public String getMobileUri() {
        return mobileUri;
    }

    public void setMobileUri(String mobileUri) {
        this.mobileUri = mobileUri;
    }

    @JsonGetter("IsEnabled")
    public boolean isEnabled() {
        return isEnabled;
    }

    @JsonSetter("IsEnabled")
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @JsonGetter("IsAll")
    public boolean isAll() {
        return isAll;
    }

    @JsonSetter("IsAll")
    public void setAll(boolean all) {
        isAll = all;
    }

    public String getUcUids() {
        return ucUids;
    }

    public void setUcUids(String ucUids) {
        this.ucUids = ucUids;
    }

    public String getUcDepartmentIds() {
        return ucDepartmentIds;
    }

    public void setUcDepartmentIds(String ucDepartmentIds) {
        this.ucDepartmentIds = ucDepartmentIds;
    }

    public Integer getFinishType() {
        return finishType;
    }

    public void setFinishType(Integer finishType) {
        this.finishType = finishType;
    }

    @JsonGetter("IsSignOut")
    public boolean isSignOut() {
        return isSignOut;
    }

    @JsonSetter("IsSignOut")
    public void setSignOut(boolean signOut) {
        isSignOut = signOut;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
}
