package com.nd.auxo.recommend.core.api.uc.internal;

import com.nd.auxo.recommend.core.api.uc.UcApi;
import com.nd.auxo.recommend.core.api.uc.repository.UcUser;
import com.nd.auxo.recommend.core.api.uc.repository.UcUserRepository;
import com.nd.auxo.recommend.core.i18n.I18nExceptionMsgConstant;
import com.nd.gaea.SR;
import com.nd.gaea.core.utils.ArrayUtils;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.ObjectUtils;
import com.nd.gaea.uranus.common.exception.BusinessException;
import com.nd.gaea.uranus.gql.PagerResult;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by way on 2016/7/25.
 */
@Service
public class UcApiImpl implements UcApi {

    @Autowired
    private UcUserRepository ucUserRepository;

    @Override
    public PagerResult<UcUser> thirdQuery(List<UcUser> ucUserList) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_GX);
            return ucUserRepository.thirdQuery(ucUserList);
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
        }
    }

    @Override
    public String thirdQueryUid(String ucUserOrgList) {
        String[] userOrgArray = ucUserOrgList.split(",");
        if (ArrayUtils.isEmpty(userOrgArray)) {
            throw new BusinessException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, ucUserOrgList));
        }
        String[] temp;
        //
        List<UcUser> ucUserListParamTemp = new ArrayList<>();
        UcUser ucUserParam;
        List<UcUser> ucUserList = new ArrayList<>();
        int index = 0;
        for (String userOrg : userOrgArray) {
            temp = userOrg.split("@");
            if (temp.length != 2) {
                throw new BusinessException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, userOrg));
            }
            ucUserParam = new UcUser();
            ucUserParam.setUserName(temp[0]);
            ucUserParam.setOrgName(temp[1]);
            ucUserListParamTemp.add(ucUserParam);
            index++;
            if (index == 10) {
                this.addUcUserList(ucUserList,ucUserListParamTemp);
                ucUserListParamTemp = new ArrayList<>();
                index = 0;
            }
        }
        if(index > 0){
            this.addUcUserList(ucUserList,ucUserListParamTemp);
        }
        String result = "";
        boolean first = true;
        for (UcUser ucUser : ucUserList) {
            if (!first) {
                result += ",";
            }
            if(ucUser.getUserId() == 0){
                throw new BusinessException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, ucUser.getUserName()+"@"+ucUser.getOrgName()));
            }
            result += ucUser.getUserId();
            first = false;
        }
        return result;
    }

    /**
     *
     * @param ucUserList
     * @param ucUserListParam
     */
    private void addUcUserList(List<UcUser> ucUserList, List<UcUser> ucUserListParam) {
        PagerResult<UcUser> ucUserPagerResult = this.thirdQuery(ucUserListParam);
        List<UcUser> ucUserListTemp = (List<UcUser>) ucUserPagerResult.getItems();
        if (CollectionUtils.isEmpty(ucUserListTemp)) {
            throw new BusinessException(SR.format(I18nExceptionMsgConstant.COMMON_NOT_FOUND, ObjectUtils.toJson(ucUserListParam)));
        }
        ucUserList.addAll(ucUserListTemp);
    }
}
