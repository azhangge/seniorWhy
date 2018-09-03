package com.nd.auxo.recommend.core.api.oldelearning.internal;

import com.nd.auxo.recommend.core.api.oldelearning.ElearningProjectApi;
import com.nd.auxo.recommend.core.api.oldelearning.repository.ElearningJavaRepository;
import com.nd.auxo.recommend.core.api.oldelearning.repository.org.UcOrg;
import com.nd.auxo.recommend.core.api.oldelearning.repository.project.Project;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by way on 2016/7/19.
 */
@Service
public class ElearningProjectApiImpl implements ElearningProjectApi {

    @Autowired
    private ElearningJavaRepository elearningJavaRepository;

    @Override
    public Project get(Long projectId) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_SDP);
            return elearningJavaRepository.getProject(projectId);
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
        }
    }

    @Override
    public UcOrg getUcOrgByName(String orgName) {
        try {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, GaeaContext.CALL_TYPE_VALUE_SDP);
            return elearningJavaRepository.getUcOrgByName(orgName);
        } finally {
            GaeaContext.put(GaeaContext.CALL_TYPE_FIELD, null);
        }
    }
}
