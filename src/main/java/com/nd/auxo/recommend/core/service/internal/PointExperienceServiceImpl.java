package com.nd.auxo.recommend.core.service.internal;

import com.nd.auxo.recommend.core.api.erp.ErpCustomCampaignApi;
import com.nd.auxo.recommend.core.api.erp.constant.ErpVirtualType;
import com.nd.auxo.recommend.core.api.erp.repository.ErpResponse;
import com.nd.auxo.recommend.core.api.uc.UcApi;

import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.auxo.recommend.core.repository.PointExperience;
import com.nd.auxo.recommend.core.repository.PointExperienceRepository;
import com.nd.auxo.recommend.core.service.PointExperienceService;
import com.nd.auxo.recommend.core.util.MD5Util;
import com.nd.auxo.recommend.web.controller.v1.point.PointExperienceVo;

import com.nd.elearning.sdk.uc.bean.UcUser;
import com.nd.elearning.sdk.uc.bean.UserInfoVo;
import com.nd.elearning.sdk.uc.repository.UcUserRepository;
import com.nd.gaea.SR;
import com.nd.gaea.context.repository.Repository;
import com.nd.gaea.context.service.AbstractService;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.uranus.common.exception.BusinessException;
import com.nd.gaea.uranus.common.exception.UnauthorizedException;
import com.nd.gaea.uranus.gql.PageResult;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
@Service
public class PointExperienceServiceImpl extends AbstractService<PointExperience, Integer> implements PointExperienceService {

    @Autowired
    private PointExperienceRepository pointExperienceRepository;
    @Autowired
    private UcApi ucApi;
    @Autowired
    private ErpCustomCampaignApi erpCustomCampaignApi;
    @Autowired
    private UcUserRepository ucUserRepository;

    //权限期限起始时间
    private long nd = (long) 1000 * 24 * 60 * 60;//一天的毫秒数
    private long nh = (long) 1000 * 60 * 60;//一小时的毫秒数
    private long nm = (long) 1000 * 60;//一分钟的毫秒数

    @Override
    public Repository<PointExperience, Integer> getRepository() {
        return null;
    }

    public PointExperienceVo convert(PointExperience param) {
        PointExperienceVo pointExperienceVo = new PointExperienceVo();
        pointExperienceVo.setExperiences(param.getExperiences());
        pointExperienceVo.setOperateUserName(param.getOperateUserName());
        pointExperienceVo.setPoints(param.getPoints());
        pointExperienceVo.setRewardDescription(param.getRewardDescription());
        pointExperienceVo.setRewardTime(param.getRewardTime());
        pointExperienceVo.setRewardUserName(param.getRewardUserName());
        pointExperienceVo.setType(param.getType());
        pointExperienceVo.setStatus(param.getStatus());
        return pointExperienceVo;
    }


    @Override
    public PointExperienceVo reward(PointExperienceVo param) throws ParseException {

        //参数及权限验证
        this.availed(param);
        PointExperienceVo pointExperienceVo = new PointExperienceVo();
        String userIdStr = this.ucApi.thirdQueryUid(param.getRewardObject());
        String[] userIdsArr = userIdStr.split(",");
        if (userIdsArr.length > 100) {
            throw new BusinessException(SR.getString(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        List<Long> idLists = new ArrayList<>();
        for (String id : userIdsArr) {
            idLists.add(Long.parseLong(id));
        }
        List<UserInfoVo> userInfoVos = this.ucUserRepository.list(idLists);
        StringBuffer failedPoints = new StringBuffer();
        StringBuffer failedExp = new StringBuffer();
        ErpResponse erpPoint;
        ErpResponse erpExp;
        for (UserInfoVo user : userInfoVos) {
            erpPoint = null;
            erpExp = null;
            PointExperience pointExperience = new PointExperience();
            if (param.getPoints() != null && param.getPoints() > 0) {
                erpPoint = this.erpCustomCampaignApi.addPointAndExp(ErpVirtualType.POINT, param.getPoints(), user.getUserId(), user.getOrgId());
                //送积分成功
                if (erpPoint!=null && Integer.valueOf(1).equals(erpPoint.getResult())){
                    pointExperience.setPoints(param.getPoints());
                //送积分失败
                }else {
                    this.pointFiled(pointExperienceVo,user,failedPoints);
                }
            }
            if (param.getExperiences() != null && param.getExperiences() > 0) {
                erpExp = this.erpCustomCampaignApi.addPointAndExp(ErpVirtualType.EXP, param.getExperiences(), user.getUserId(), user.getOrgId());
                //送经验成功
                if (erpExp!=null && Integer.valueOf(1).equals(erpExp.getResult())){
                    pointExperience.setExperiences(param.getExperiences());
                //送经验失败
                }else {
                    this.expFiled(pointExperienceVo,user,failedExp);
                }
            }
            if (erpPoint == null && erpExp ==null){
                throw new BusinessException(SR.getString(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
            }
            //存本地日志
            if ((erpPoint != null && Integer.valueOf(1).equals(erpPoint.getResult())) || (erpExp !=null && Integer.valueOf(1).equals(erpExp.getResult()))){
                this.success(param, user, pointExperience);
            }else {
                continue;
            }
        }
        //返回vo
        if (StringUtils.isBlank(pointExperienceVo.getFailedPoints()) && StringUtils.isBlank(pointExperienceVo.getFailedExp())) {
            pointExperienceVo.setStatus(1);
        } else {
            pointExperienceVo.setStatus(0);
        }
        pointExperienceVo.setPoints(param.getPoints());
        pointExperienceVo.setExperiences(param.getExperiences());
        pointExperienceVo.setAuth(param.getAuth());
        if (param.getType() != null) {
            pointExperienceVo.setType(param.getType());
        }
        if (StringUtils.isNotEmpty(param.getRewardDescription())) {
            pointExperienceVo.setRewardDescription(param.getRewardDescription());
        }
        return pointExperienceVo;
    }

    @Override
    public PageResult<PointExperienceVo> getPage(String rewardUserName, String operateUserName, String startTime, String endTime, int page, int size) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTimeDate = null;
        Date endTimeDate = null;
        if (StringUtils.isNoneBlank(startTime)) {
            startTimeDate = format.parse(startTime);
        }
        if (StringUtils.isNoneBlank(endTime)) {
            endTimeDate = format.parse(endTime);
        }
        PagerResult<PointExperience> pagerResult = this.pointExperienceRepository.listPages(rewardUserName, operateUserName, startTimeDate, endTimeDate, page, size);
        PageResult<PointExperienceVo> result = new PageResult<>();
        List<PointExperienceVo> list = new ArrayList<>();
        for (PointExperience pointExperience : pagerResult.getItems()) {
            list.add(convert(pointExperience));
        }
        result.setCount(pagerResult.getTotal());
        result.setItems(list);
        return result;
    }

    private void success(PointExperienceVo param, UserInfoVo user, PointExperience pointExperience) {

        UcUser operateUser = this.ucUserRepository.getUser(GaeaContext.getUserId());
        String operateUserName = operateUser.getOrgExinfo().getRealName() + "(" + operateUser.getUserId() + "@" + operateUser.getOrgExinfo().getOrgName() + ")";
        pointExperience.setOperateUserName(operateUserName);
        if (StringUtils.isNotEmpty(param.getRewardDescription())) {
            pointExperience.setRewardDescription(param.getRewardDescription());
        }
        pointExperience.setProjectId(GaeaContext.getAppId());
        pointExperience.setRewardTime(new Date());
        pointExperience.setRewardUserId(user.getUserId());
        pointExperience.setRewardUserName(user.getName() + "(" + user.getUserId() + "@" + user.getOrgName() + ")");
        pointExperience.setStatus(1);
        if (param.getType() != null) {
            pointExperience.setType(param.getType());
        }
        this.pointExperienceRepository.save(pointExperience);
    }

    private void pointFiled(PointExperienceVo pointExperienceVo, UserInfoVo user, StringBuffer failedPoints) {

        if (failedPoints.length()<1){
            failedPoints.append(user.getUserId());
        }else {
            failedPoints.append(","+user.getUserId());
        }
        pointExperienceVo.setFailedPoints(failedPoints.toString());
    }

    private void expFiled(PointExperienceVo pointExperienceVo,  UserInfoVo user, StringBuffer failedExp) {

        if (failedExp.length()<1){
            failedExp.append(user.getUserId());
        }else {
            failedExp.append("," + user.getUserId());
        }
        pointExperienceVo.setFailedExp(failedExp.toString());
    }

    private void availed(PointExperienceVo param){

        if (StringUtils.isBlank(param.getRewardObject()) || param.getAuth() == null) {
            throw new BusinessException(SR.getString(I18nExceptionMsgConstant.COMMON_REQUEST_OBJECT_IS_EMPTY));
        }
        //授予的积分或经验必须为正整数
        if ((param.getPoints() != null && param.getPoints() <= 0) && (param.getExperiences() != null && param.getExperiences() <= 0)) {
            throw new BusinessException(SR.getString(I18nExceptionMsgConstant.COMMON_PARAMETER_ERROR));
        }
        //权限验证与过期验证
        String[] authTime = param.getAuth().split(",");
        if (authTime.length !=2) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_AUTHENTICATION_FAILED));
        }
        for (int i=authTime[1].length();--i>=0;){
            int c=authTime[1].charAt(i);
            if (c<48 || c>57){
                throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_AUTHENTICATION_FAILED));
            }
        }
        String operateUserId = GaeaContext.getUserId().toString();
        if (!MD5Util.encryptMD5_ND(operateUserId).equals(authTime[0])) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_AUTHENTICATION_FAILED));
        }
        Long diffTime = System.currentTimeMillis() - Long.parseLong(authTime[1]);
        if (diffTime % nd % nh / nm > 5) {
            throw new UnauthorizedException(SR.getString(I18nExceptionMsgConstant.COMMON_AUTHENTICATION_OVERDUE));
        }

    }
}















