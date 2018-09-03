package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.repository.CustomCampaign;
import com.nd.auxo.recommend.core.api.oldelearning.ElearningProjectApi;
import com.nd.auxo.recommend.core.api.oldelearning.repository.org.UcOrg;
import com.nd.auxo.recommend.core.api.oldelearning.repository.project.Project;
import com.nd.auxo.recommend.core.api.uc.UcApi;
import com.nd.auxo.recommend.core.constant.ObjectType;
import com.nd.auxo.recommend.core.constant.OrganizationType;
import com.nd.auxo.recommend.core.constant.TaskType;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.Activity;
import com.nd.auxo.recommend.core.repository.ActivityRepository;
import com.nd.auxo.recommend.core.service.ActivityService;
import com.nd.auxo.recommend.core.service.internal.support.ActivityExpandParam;
import com.nd.auxo.sdk.recommend.util.CustomDataUtil;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVo;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForCreate;
import com.nd.auxo.sdk.recommend.vo.activity.ActivityVoForUpdate;
import com.nd.auxo.sdk.recommend.vo.activity.JumpData;
import com.nd.gaea.SR;
import com.nd.gaea.WafException;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.core.utils.ArrayUtils;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.repository.JpaRepositoryAdapter;
import com.nd.gaea.uranus.common.exception.ArgumentValidationException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/7/18.
 */
@Service
@Validated
public class ActivityServiceImpl extends AbstractService<Activity, UUID> implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ElearningProjectApi projectApi;

    @Autowired
    private ErpCustomCampaignApi erpCustomCampaignApi;

    @Autowired
    private UcApi ucApi;

    @Value("${erp.app.key}")
    private String erpAppKey;

    @Value("${auxo.webfront.url}")
    private String recommendUrl;

    @Value("${activity.jump.web.url}")
    private String jumpWebUrl;

    @Value("${activity.jump.mobile.url}")
    private String jumpMobileUrl;

    private static final String REGEX = "@[^,]+,";


    @Override
    public Repository<Activity, UUID> getRepository() {
        return new JpaRepositoryAdapter<>(this.activityRepository);
    }

    @Override
    public ActivityVo create(@Valid ActivityVoForCreate create, Long projectId, Long userId) {
        //获取组织 任务类型 domain等
        ActivityExpandParam activityExpandParam = this.getActivityExpandParam(create.getJoinObjectType(), create.getJoinObject(), projectId);
        Integer taskType = create.getTaskType();
        String taskId = null;
        Activity activity = convertActivity(create);
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());
        activity.setUpdateUserId(userId);
        activity.setCreateUserId(userId);
        activity.setJoinObjectOrgId(activityExpandParam.getOrgId());//组织id
        activity.setJoinObjectOrgType(activityExpandParam.getOrgType());//组织类型
        activity.setProjectId(projectId);
        activity.setJumpCmpUrl(getMobileUrl(activity));
        if (TaskType.ERP.getValue().equals(taskType)) {
            this.validErp(activity);
            //
            CustomCampaign customCampaign = this.erpCustomCampaignApi.create(convertErpCustomCampaign(activity, activityExpandParam.getProjectCode()));
            taskId = "" + customCampaign.getId();
        }
        activity.setTaskId(taskId);
        activity.setTaskType(taskType);
        this.activityRepository.save(activity);
        return convertActivityVo(activity);
    }



    @Override
    public ActivityVo update(UUID id, ActivityVoForUpdate update, Long projectId, Long userId) {
        Activity activity = this.activityRepository.findOne(id);
        if (activity == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, id));
        }
        //获取组织 任务类型 domain等
        ActivityExpandParam activityExpandParam = this.getActivityExpandParam(update.getJoinObjectType(), update.getJoinObject(), projectId);
        convertActivityByUpdate(activity, update);
        activity.setUpdateTime(new Date());
        activity.setUpdateUserId(userId);
        activity.setJoinObjectOrgId(activityExpandParam.getOrgId());//组织id
        activity.setJoinObjectOrgType(activityExpandParam.getOrgType());//组织类型
        if (TaskType.ERP.getValue().equals(activity.getTaskType())) {
            this.validErp(activity);
            erpCustomCampaignApi.update(activity.getTaskId(), convertErpCustomCampaign(activity, activityExpandParam.getProjectCode()));
        }
        this.activityRepository.save(activity);
        return convertActivityVo(activity);
    }


    @Override
    public void delete(UUID id) throws Exception {
        Activity activity = this.activityRepository.findOne(id);
        if (activity == null) {
            throw new ObjectNotFoundException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, id));
        }

        if (TaskType.ERP.getValue().equals(activity.getTaskType())) {
            CustomCampaign customCampaign = this.convertErpCustomCampaign(activity, null);

            customCampaign.setEnabled(false);
            this.erpCustomCampaignApi.update(activity.getTaskId(), customCampaign);
        }
        this.activityRepository.delete(activity);
    }


    /**
     * 校验获取组织id
     *
     * @param
     * @return
     */
    private Long getOrgIdByActivityVoForCreate(Integer joinObjectType, String joinObject) {
        if (ObjectType.USER.getValue().equals(joinObjectType)) {
            String[] joinArray = joinObject.split(",");
            if (ArrayUtils.isEmpty(joinArray)) {
                throw new WafException("WAF/BUSINESS_EXCEPTION"," joinObject is null");
            }
            String[] userArray = joinArray[0].split("@");
            if (ArrayUtils.isEmpty(userArray) || !(userArray.length == 2)) {
                throw new WafException("WAF/BUSINESS_EXCEPTION", " joinObject format is error ");
            }
            String orgName = userArray[1];
            UcOrg ucOrg = this.projectApi.getUcOrgByName(orgName);
            if (ucOrg == null) {
                throw new ArgumentValidationException(SR.format(I18nExceptionMsgConstant.ORG_NOT_FOUND, orgName));
            }
            return ucOrg.getOrgId();
        }
        return null;
    }


    /**
     * 构造jump_data
     *
     * @param activity
     * @return
     */
    private String getJumpData(Activity activity) {
        JumpData jumpData = new JumpData();
        jumpData.setActivityId(activity.getId());
        jumpData.setProjectId(activity.getProjectId());
        return CustomDataUtil.encodeCustomData(jumpData);
    }

    private String getMobileUrl(Activity activity) {
        return jumpMobileUrl + "?jump_data=" + getJumpData(activity);
    }


    /**
     * 根据activity转化CustomCampaign
     *
     * @param activity
     * @param projectCode
     * @return
     */
    private CustomCampaign convertErpCustomCampaign(Activity activity, String projectCode) {
        String jumpData = getJumpData(activity);
        CustomCampaign customCampaign = new CustomCampaign();
        customCampaign.setEndTime(activity.getEndTime());
        customCampaign.setBeginTime(activity.getStartTime());
        customCampaign.setEnabled(activity.isEnabled());
        customCampaign.setMobileUri(getMobileUrl(activity));
        if (StringUtils.isNotEmpty(projectCode)) {
            customCampaign.setWebUri(recommendUrl + "/" + projectCode + jumpWebUrl + "?jump_data=" + jumpData + "&auth=#auth#");
        }
        customCampaign.setName(activity.getTitle());
        customCampaign.setOrgId(activity.getJoinObjectOrgId());
        if (ObjectType.USER.getValue().equals(activity.getJoinObjectType())) {
            customCampaign.setUcUids(ucApi.thirdQueryUid(activity.getJoinObject()));
        } else if (ObjectType.ORG.getValue().equals(activity.getJoinObjectType())) {
            customCampaign.setUcDepartmentIds(activity.getJoinObject());
        } else if (ObjectType.ALL.getValue().equals(activity.getJoinObjectType())) {
            customCampaign.setAll(true);
        }
        customCampaign.setAppKey(erpAppKey);
        return customCampaign;
    }


    /**
     * 根据activity转化vo
     *
     * @param activity
     * @return
     */
    public ActivityVo convertActivityVo(Activity activity) {
        if (activity == null) {
            return null;
        }
        ActivityVo activityVo = new ActivityVo();
        activityVo.setCustomType(activity.getCustomType());
        activityVo.setEnabled(activity.isEnabled());
        activityVo.setDescription(activity.getDescription());
        activityVo.setTaskId(activity.getTaskId());
        activityVo.setEndTime(activity.getEndTime());
        activityVo.setJoinObjectOrgId(activity.getJoinObjectOrgId());
        activityVo.setCreateTime(activity.getCreateTime());
        activityVo.setProjectId(activity.getProjectId());
        activityVo.setStartTime(activity.getStartTime());
        activityVo.setRewardExperience(activity.getRewardExperience());
        activityVo.setCreateUserId(activity.getCreateUserId());
        activityVo.setTitle(activity.getTitle());
        activityVo.setRewardPoints(activity.getRewardPoints());
        activityVo.setJoinObjectType(activity.getJoinObjectType());
        activityVo.setCustomId(activity.getCustomId());
        activityVo.setTaskType(activity.getTaskType());
        activityVo.setUpdateTime(activity.getUpdateTime());
        activityVo.setUpdateUserId(activity.getUpdateUserId());
        activityVo.setId(activity.getId());
        activityVo.setTargetCmpUrl(activity.getTargetCmpUrl());
        activityVo.setJoinObject(activity.getJoinObject());
        activityVo.setActivityType(activity.getActivityType());
        activityVo.setActivityFinishType(activity.getActivityFinishType());
        activityVo.setJumpCmpUrl(activity.getJumpCmpUrl());
        return activityVo;
    }

    /**
     * 根据create转化activity
     *
     * @param create
     * @return
     */
    public Activity convertActivity(ActivityVoForCreate create) {
        Activity activity = new Activity();
        activity.setCustomType(create.getCustomType());
        activity.setEnabled(create.isEnabled());
        activity.setDescription(create.getDescription());
        activity.setEndTime(create.getEndTime());
        activity.setStartTime(create.getStartTime());
        activity.setRewardExperience(create.getRewardExperience());
        activity.setTitle(create.getTitle());
        activity.setRewardPoints(create.getRewardPoints());
        activity.setJoinObjectType(create.getJoinObjectType());
        activity.setCustomId(create.getCustomId());
        activity.setTargetCmpUrl(create.getTargetCmpUrl());
        activity.setJoinObject(create.getJoinObject());
        activity.setActivityType(create.getActivityType());
        activity.setActivityFinishType(create.getActivityFinishType());
        return activity;
    }

    /**
     * 根据update转化activity
     *
     * @param activity
     * @param update
     * @return
     */
    private Activity convertActivityByUpdate(Activity activity, ActivityVoForUpdate update) {
        activity.setCustomType(update.getCustomType());
        activity.setDescription(update.getDescription());
        activity.setEndTime(update.getEndTime());
        activity.setStartTime(update.getStartTime());
        activity.setRewardExperience(update.getRewardExperience());
        activity.setTitle(update.getTitle());
        activity.setEnabled(update.isEnabled());
        activity.setRewardPoints(update.getRewardPoints());
        activity.setJoinObjectType(update.getJoinObjectType());
        activity.setCustomId(update.getCustomId());
        activity.setTargetCmpUrl(update.getTargetCmpUrl());
        activity.setJoinObject(update.getJoinObject());
        activity.setActivityFinishType(update.getActivityFinishType());
        return activity;
    }

    /**
     * 获取组织 任务类型 domain等
     *
     * @param joinObjectType
     * @param joinObject
     * @return
     */
    private ActivityExpandParam getActivityExpandParam(Integer joinObjectType, String joinObject, Long projectId) {
        ActivityExpandParam param = new ActivityExpandParam();
        //校验获取组织
        Long orgId = getOrgIdByActivityVoForCreate(joinObjectType, joinObject);
        Integer orgType = OrganizationType.REAL.getValue();
        //获取项目
        Project project = this.projectApi.get(projectId);
        if (project == null) {
            throw new WafException("WAF/NOT_FOUND", " project id =" + projectId + " not exist ");
        }
        if (!ObjectType.USER.getValue().equals(joinObjectType)) {
            if (project.getOrgType() != null) {
                orgType = project.getOrgType();
            }
            if(OrganizationType.REAL.getValue().equals(orgType)){
                if (project.getOrgId() == null) {
                    throw new WafException("WAF/BUSINESS_EXCEPTION", "organization empty");
                }
                orgId = Long.parseLong(project.getOrgId());
            }else{
                if (project.getVOrgId() == null) {
                    throw new WafException("WAF/BUSINESS_EXCEPTION", "organization empty");
                }
                orgId = project.getVOrgId();
            }
        }
        param.setOrgId(orgId);
        param.setOrgType(orgType);
        param.setProjectCode(project.getDomain());
        return param;
    }

    private void validErp(Activity activity) {
        if(OrganizationType.VIRTUAL.getValue().equals(activity.getJoinObjectOrgType())){
            //ERP不支持虚拟组织
            throw new WafException("WAF/BUSINESS_EXCEPTION", "ERP Activities do not support virtual organization ");
        }
    }


}
