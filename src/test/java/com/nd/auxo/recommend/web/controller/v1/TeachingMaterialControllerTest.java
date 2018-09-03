package com.nd.auxo.recommend.web.controller.v1;

import com.elearningfit.service.project.ProjectService;
import com.elearningfit.service.project.repository.Project;
import com.nd.auxo.recommend.core.repository.NDRImportRelation;
import com.nd.auxo.recommend.core.service.NDRImportRelationService;
import com.nd.auxo.recommend.web.controller.v1.course.TeachingMaterialController;
import com.nd.auxo.recommend.web.controller.v1.course.vo.TeachingMaterialImport;
import com.nd.elearning.sdk.ndr.bean.ChapterVo;
import com.nd.elearning.sdk.ndr.bean.PageResultVo;
import com.nd.elearning.sdk.ndr.bean.ResourceVo;
import com.nd.elearning.sdk.ndr.repository.ResourceRepository;
import com.nd.elearning.sdk.ndr.repository.TeachingMaterialRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by way on 2016/10/31.
 */
public class TeachingMaterialControllerTest {

    @InjectMocks
    private TeachingMaterialController teachingMaterialController;
    @Mock
    private ResourceRepository resourceRepository;
    @Mock
    private TeachingMaterialRepository teachingMaterialRepository;
    @Mock
    private NDRImportRelationService ndrImportRelationService;
    @Mock
    private ProjectService projectService;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
        when(this.resourceRepository.manageQuery(anyString(), anyString(), anyInt(), anyInt(),anyString(), anyList(), anyList(), anyList(), anyList())).thenReturn(new PageResultVo<ResourceVo>());
        when(this.teachingMaterialRepository.getSubItems(any(UUID.class), anyString())).thenReturn(new ArrayList<ChapterVo>());
        when(this.ndrImportRelationService.findByProjectIdAndCustomTypeAndNdrIdIn(anyLong(), anyString(), anyList())).thenReturn(new ArrayList<NDRImportRelation>());
        Project project = new Project();
        project.setClientId(1);
        when(this.projectService.getByProjectId(anyLong())).thenReturn(project);
    }

    //@Test
    public void test(){
        this.teachingMaterialController.query(0,20,"小学","$ON020000/$ON020100/$SB0100/$E001000/*");
    }

    @Test
    public void testQueryCount(){
        TeachingMaterialImport teachingMaterialImport = new TeachingMaterialImport();
        List<UUID> ids = new ArrayList<>();
        ids.add(UUID.randomUUID());
        teachingMaterialImport.setIds(ids);
        this.teachingMaterialController.queryCount(teachingMaterialImport);
    }

    //@Test
    public void testValidImport(){
        TeachingMaterialImport teachingMaterialImport = new TeachingMaterialImport();
        List<UUID> ids = new ArrayList<>();
        ids.add(UUID.randomUUID());
        teachingMaterialImport.setIds(ids);
        this.teachingMaterialController.validImport(teachingMaterialImport);
    }

 }
