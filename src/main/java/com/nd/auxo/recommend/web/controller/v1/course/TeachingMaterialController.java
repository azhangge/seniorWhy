package com.nd.auxo.recommend.web.controller.v1.course;

import com.elearningfit.service.project.ProjectService;
import com.nd.auxo.recommend.core.api.cloud.CloudCatalog;
import com.nd.auxo.recommend.core.api.cloud.CloudCatalogTree;
import com.nd.auxo.recommend.core.api.cloud.RecommendSdkCloudApi;
import com.nd.auxo.recommend.core.api.cloud.model.CloudNDRCreate;
import com.nd.auxo.recommend.core.api.cloud.model.NDRResource;
import com.nd.auxo.recommend.core.api.cloud.model.UnitCourse;
import com.nd.auxo.recommend.core.api.cloud.model.UnitCourseCreate;
import com.nd.auxo.recommend.core.api.course.RecommendSdkCourseApi;
import com.nd.auxo.recommend.core.api.course.ResourceCourseSyncParam;
import com.nd.auxo.recommend.core.api.course.ResourceCourseVo;
import com.nd.auxo.recommend.core.constant.SwitchKeyConsts;
import com.nd.auxo.recommend.core.repository.NDRImportRelation;
import com.nd.auxo.recommend.core.repository.TagCategoryRelation;
import com.nd.auxo.recommend.core.service.NDRImportRelationService;
import com.nd.auxo.recommend.core.service.TagCategoryRelationService;
import com.nd.auxo.recommend.core.util.FileUploadDownUtils;
import com.nd.auxo.recommend.core.util.ImageUtils;
import com.nd.auxo.recommend.core.util.ListRequestHelper;
import com.nd.auxo.recommend.core.util.RegexUtils;
import com.nd.auxo.recommend.web.controller.v1.course.vo.*;
import com.nd.auxo.sdk.opencourse.OpenCourseApi;
import com.nd.auxo.sdk.opencourse.OpenCourseTagApi;
import com.nd.auxo.sdk.opencourse.domain.OpenCourse;
import com.nd.auxo.sdk.opencourse.vo.OpenCourseEditVo;
import com.nd.auxo.sdk.recommend.common.CustomType;
import com.nd.auxo.sdk.tag.tag.TagApi;
import com.nd.auxo.sdk.tag.tag.TagTreeVo;
import com.nd.auxo.sdk.tag.tag.TagVo;
import com.nd.elearning.sdk.ndr.bean.ChapterVo;
import com.nd.elearning.sdk.ndr.bean.PageResultVo;
import com.nd.elearning.sdk.ndr.bean.ResourceVo;
import com.nd.elearning.sdk.ndr.define.CoverageDef;
import com.nd.elearning.sdk.ndr.define.ResourceTypeDef;
import com.nd.elearning.sdk.ndr.repository.ResourceRepository;
import com.nd.elearning.sdk.ndr.repository.TeachingMaterialRepository;
import com.nd.gaea.core.exception.BaseRuntimeException;
import com.nd.gaea.core.utils.CollectionUtils;
import com.nd.gaea.core.utils.StringUtils;
import com.nd.gaea.uranus.common.exception.BusinessException;
import com.nd.gaea.uranus.common.exception.ObjectNotFoundException;
import com.nd.gaea.util.WafJsonMapper;
import com.nd.gaea.waf.security.DESUtil;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.servicemanager.ServiceManagerDo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by way on 2016/10/27.
 */
@RestController
@Slf4j
@Api("教材")
@RequestMapping("")
public class TeachingMaterialController {

    private static final long IMPORT_INTERVAL_TIME = 600000l;//导入间隔时间
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RecommendSdkCloudApi cloudApi;
    @Autowired
    private RecommendSdkCourseApi courseSdkApi;
    @Autowired
    private OpenCourseApi openCourseApi;
    @Autowired
    private TagApi tagApi;
    @Autowired
    private OpenCourseTagApi openCourseTagApi;
    @Autowired
    private TeachingMaterialRepository teachingMaterialRepository;
    @Autowired
    private RecommendSdkCloudApi recommendSdkCloudApi;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TagCategoryRelationService tagCategoryRelationService;
    @Autowired
    private NDRImportRelationService ndrImportRelationService;
    @Value("${auxo.cloud.url}")
    private String cloudUrl;
    @Value("${cs.url}")
    private String csUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(TeachingMaterialController.class);
    /**
     * 查询教材
     *
     * @param words
     * @param page
     * @param size
     * @param category
     * @return
     */
    @ApiOperation("查询教材")
    @RequestMapping(value = "/v1/recommends/teaching_materials", method = RequestMethod.GET)
    public PageResultVo<ResourceVo> query(
            @ApiParam("第几页，从0开始") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @ApiParam("每页的记录数") @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @ApiParam("教材关键字") @RequestParam(value = "words", required = false) String words,
            @ApiParam("教材分类维度，如小学/一年级/语文/人教版/所有子版本，则值为$ON020000/$ON020100/$SB0100/$E001000/*。") @RequestParam(value = "category", required = false) String category
    ) {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_NDR)) {
            throw new ObjectNotFoundException("Not Found");
        }
        List<String> categoryList = new ArrayList<>();
        categoryList.add(category);
        List<String> coverageList = new ArrayList<>();
        coverageList.add(CoverageDef.COVERAGE_ND + "/");
        return resourceRepository.manageQuery(ResourceTypeDef.TEACHINGMATERIALS, words, page, size, "create_time desc", coverageList, null, categoryList, null);
    }

    /**
     * 查询教材
     *
     * @return
     */
    @ApiOperation("查询教材的统计数")
    @RequestMapping(value = "/v1/recommends/teaching_materials/counts", method = RequestMethod.POST)
    public List<ResourceCount> queryCount(@RequestBody TeachingMaterialImport teachingMaterialImport) {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_NDR)) {
            throw new ObjectNotFoundException("Not Found");
        }
        List<UUID> ids = teachingMaterialImport.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ResourceCount> resourceCountList = new ArrayList<>();
        ResourceCount resourceCount;
        List<ChapterVo> chapterVoList;
        for (UUID teachingId : ids) {
            resourceCount = new ResourceCount();
            resourceCount.setIdentifier(teachingId);
            chapterVoList = teachingMaterialRepository.getSubItems(teachingId, null);
            resourceCount.setChapterCount(chapterVoList.size());
            resourceCountList.add(resourceCount);
        }
        return resourceCountList;

    }

    /**
     * 教材导入前校验
     * <p/>
     */
    @ApiOperation("教材导入前校验")
    @RequestMapping(value = "/v1/recommends/teaching_materials/import/valid", method = RequestMethod.POST)
    public ImportValidResult validImport(@RequestBody TeachingMaterialImport teachingMaterialImport) {
        List<UUID> ids = teachingMaterialImport.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return new ImportValidResult(0, null);
        }
        //是否已导入 导入时间
        List<NDRImportRelation> list = ndrImportRelationService.findByProjectIdAndCustomTypeAndNdrIdIn(GaeaContext.getAppId(),
                CustomType.AUXO_OPEN_COURSE.getValue(), teachingMaterialImport.getIds());
        if (CollectionUtils.isEmpty(list)) {
            return new ImportValidResult(0, null);
        } else {
            ImportValidResult importValidResult = new ImportValidResult();
            importValidResult.setCode(1);
            List<UUID> uuidList = new ArrayList<>();
            for (NDRImportRelation ndrImportRelation : list) {
                uuidList.add(ndrImportRelation.getNdrId());
            }
            importValidResult.setIdList(uuidList);
            return importValidResult;
        }
    }

    /**
     * 教材导入
     * <p/>
     * 导入模式
     * 1 . 只导教材
     * 2. 教材 章节
     * 3. 教材 章节 资源
     */
    @ApiOperation("教材导入")
    @RequestMapping(value = "/v1/recommends/teaching_materials/import", method = RequestMethod.POST)
    public List<ImportResult> importResource(
            @ApiParam("导入模式 1 . 只导教材（默认）， 2. 教材 章节，3. 教材 章节 资源") @RequestParam(value = "import_mode", required = false, defaultValue = "1") Integer importMode,
            @RequestBody TeachingMaterialImport teachingMaterialImport) throws Exception {
        if (!ServiceManagerDo.isServiceEnable(SwitchKeyConsts.RECOMMEND_NDR)) {
            throw new ObjectNotFoundException("Not Found");
        }
        if (teachingMaterialImport == null) {
            return Collections.emptyList();
        }
        if (CollectionUtils.isEmpty(teachingMaterialImport.getIds())) {
            return Collections.emptyList();
        }
        if (Integer.valueOf(3).equals(importMode) && CollectionUtils.isEmpty(teachingMaterialImport.getResourceTypes())) {
            throw new BusinessException(" importMode = 3 , resourceType can't be empty ");
        }
        List<ImportResult> importResultList = new ArrayList<>();
        ImportResult importResult;
        String cloudToken = DESUtil.encode(projectService.getByProjectId(GaeaContext.getAppId()).getClientId() + "," + GaeaContext.getUserId());
        NDRImportRelation ndrImportRelation;
        StringBuilder logMessage;
        for (UUID teachingId : teachingMaterialImport.getIds()) {
            logMessage = new StringBuilder();
            importResult = new ImportResult();
            importResult.setId(teachingId);
            importResultList.add(importResult);
            long startTime = System.currentTimeMillis();
            ndrImportRelation = handleTeachingMaterial(teachingId, cloudToken, teachingMaterialImport, logMessage);
            //
            // 处理 章节
            if (importMode == 1) {
                // 导入结束  让基础平台 mq通知课程组件
                noticeMq(cloudToken, ndrImportRelation.getUnitId());
                long endTime = System.currentTimeMillis();
                log.error("teachingId(" + teachingId + ") used total time: " + (endTime - startTime) + "ms");

                addLogInfo(logMessage);
                continue;
            }
            handleChapterAndResource(teachingId, importMode, ndrImportRelation.getUnitId(), cloudToken, importResult, teachingMaterialImport, logMessage);
            // 导入结束  让基础平台 mq通知课程组件
            noticeMq(cloudToken, ndrImportRelation.getUnitId());

            addLogInfo(logMessage);

            long endTime = System.currentTimeMillis();
            log.error("teachingId(" + teachingId + ") used total time: " + (endTime - startTime) + "ms");
        }
        return importResultList;
    }


    /**
     * 导入教材
     *
     * @param teachingId
     * @param cloudToken
     * @param teachingMaterialImport
     * @param logMessage
     * @return
     * @throws Exception
     */
    private NDRImportRelation handleTeachingMaterial(UUID teachingId, String cloudToken, TeachingMaterialImport teachingMaterialImport, StringBuilder logMessage) throws Exception{
        long startTime = System.currentTimeMillis();

        NDRImportRelation ndrImportRelation = ndrImportRelationService.findByProjectIdAndCustomTypeAndNdrId(GaeaContext.getAppId(), CustomType.AUXO_OPEN_COURSE.getValue(), teachingId);
        if (ndrImportRelation == null) {
        } else if ((new Date().getTime() - ndrImportRelation.getUpdateTime().getTime()) <= IMPORT_INTERVAL_TIME) {
            long time = IMPORT_INTERVAL_TIME / 1000 / 60;
            throw new BusinessException("教材（" + teachingId + "）正在导入或已导入,若需重新导入请等待" + time + "分钟");
        } else if (ndrImportRelation.getUnitId() == null || ndrImportRelation.getCourseId() == null || ndrImportRelation.getCustomId() == null) {
            //导入失败的张数据 或者10分钟都没导完的 删除掉 重新导入
            ndrImportRelationService.delete(ndrImportRelation);
        } else {
            //元课程 是否存在 ，不存在 删除 关联 数据
            UnitCourse unitCourse = cloudApi.get(cloudToken, ndrImportRelation.getUnitId(), false);
            if (unitCourse != null) {
                UUID updateCustomId = updateCourseByUnitId(ndrImportRelation.getUnitId(), cloudToken, teachingId, ndrImportRelation.getCourseId(), UUID.fromString(ndrImportRelation.getCustomId()), teachingMaterialImport);
                //
                ndrImportRelation.setUpdateTime(new Date());
                ndrImportRelation.setUpdateUserId(GaeaContext.getTrueUserId());
                ndrImportRelation.setCustomId(updateCustomId.toString());
                ndrImportRelationService.save(ndrImportRelation);

                logMessage.append("NDR资源管理导入教材\"" + unitCourse.getTitle() + "(" + unitCourse.getIdentity() + ")\"");
                // 不需要再导入教材
                return ndrImportRelation;
            } else {
                //元课程 不存在
                ndrImportRelationService.delete(ndrImportRelation);
            }
        }
        //开始导入
        ndrImportRelation = new NDRImportRelation();
        ndrImportRelation.setCreateUserId(GaeaContext.getTrueUserId());
        ndrImportRelation.setUpdateUserId(GaeaContext.getTrueUserId());
        ndrImportRelation.setCreateTime(new Date());
        ndrImportRelation.setUpdateTime(new Date());
        ndrImportRelation.setProjectId(GaeaContext.getAppId());
        ndrImportRelation.setCustomType(CustomType.AUXO_OPEN_COURSE.getValue());
        ndrImportRelation.setNdrId(teachingId);
        ndrImportRelationService.save(ndrImportRelation);


        ResourceVo resourceVo = resourceRepository.getResource(ResourceTypeDef.TEACHINGMATERIALS, teachingId);
        Map<String, String> previewMap = resourceVo.getPreview();
        Integer fmId = null;
        if (previewMap != null && previewMap.containsKey("fm")) {
            fmId = dealFrontCover(previewMap.get("fm"), cloudToken);
        }
        //1 创建元课程
        UnitCourseCreate unitCourseCreate = new UnitCourseCreate();
        unitCourseCreate.setTitle(resourceVo.getTitle());
        unitCourseCreate.setFrontCoverObjectId(fmId);
        unitCourseCreate.setSummary(resourceVo.getDescription());
        UnitCourse unitCourse = cloudApi.create(unitCourseCreate, cloudToken, false);
        //2 从基础平台同步该元课程为资源课程
        ResourceCourseSyncParam syncParam = new ResourceCourseSyncParam();
        syncParam.setSource(ResourceCourseSyncParam.SOURCE_CLOUD);
        syncParam.setSourceId(UUID.fromString(unitCourse.getIdentity()));
        ResourceCourseVo syncResult = courseSdkApi.sync(syncParam);
        //3 从课程池创建公开课
        List<OpenCourse> batchCreateOpenCourseList = new ArrayList<>();
        OpenCourse openCourse = new OpenCourse();
        openCourse.setId(syncResult.getId());
        openCourse.setProjectId(syncResult.getProjectId());
        batchCreateOpenCourseList.add(openCourse);
        List<OpenCourseEditVo> openCourseEditVoList = openCourseApi.batchCreate(batchCreateOpenCourseList);
        //4  创建标签
        List<UUID> tagIds = handleTag(teachingMaterialImport);
        //5 创建公开课和标签关系
        if (CollectionUtils.isNotEmpty(tagIds)) {
            openCourseTagApi.updateTags(openCourseEditVoList.get(0).getId(), tagIds);
        }
        //教材导入结束
        ndrImportRelation.setUpdateTime(new Date());
        ndrImportRelation.setUnitId(unitCourse.getUnitId());
        ndrImportRelation.setCourseId(syncResult.getId());
        ndrImportRelation.setCustomId(openCourseEditVoList.get(0).getId().toString());
        ndrImportRelationService.save(ndrImportRelation);

        logMessage.append("NDR资源管理导入教材\"" + unitCourse.getTitle() + "(" + unitCourse.getIdentity() + ")\"");

        long endTime = System.currentTimeMillis();
        log.error("teachingId(" + teachingId + ") create open_course used total time: " + (endTime - startTime) + "ms");
        return ndrImportRelation;
    }


    /**
     * 处理标签
     *
     * @param teachingMaterialImport
     * @return
     */
    private List<UUID> handleTag(TeachingMaterialImport teachingMaterialImport) throws Exception{
        //  标签如果code 为空 不传
        List<UUID> list = new ArrayList<>();
        TagTreeVo tagTreeVo = tagApi.getTree(CustomType.AUXO_OPEN_COURSE.getValue(), false);
        if (tagTreeVo == null) {
            //如果没有根标签的场景 则先不创建。
            return list;
        }
        UUID gradeId = null;
        if (teachingMaterialImport.getGradeTag() != null && StringUtils.isNotBlank(teachingMaterialImport.getGradeTag().getName())) {
            gradeId = getTagId(teachingMaterialImport.getGradeTag(), tagTreeVo.getId(), TagCategoryRelation.CODE_CUSTOM_GRADE, "年级");
        }
        UUID subjectId = null;
        if (teachingMaterialImport.getSubjectTag() != null && StringUtils.isNotBlank(teachingMaterialImport.getSubjectTag().getName())) {
            subjectId = getTagId(teachingMaterialImport.getSubjectTag(), tagTreeVo.getId(), TagCategoryRelation.CODE_CUSTOM_SUBJECT, "学科");
        }
        if (gradeId != null) {
            list.add(gradeId);
        }
        if (subjectId != null) {
            list.add(subjectId);
        }
        return list;
    }

    /**
     * 获取标签id
     *
     * @param importTag
     * @param rootId
     * @param parentTagCode
     * @param parentName
     * @return
     * @throws Exception
     */
    private UUID getTagId(ImportTag importTag, UUID rootId, String parentTagCode, String parentName) throws Exception{
        Assert.assertNotNull(importTag);
        Assert.assertNotNull(importTag.getName());
        Assert.assertNotNull(importTag.getCode());
        TagCategoryRelation tagCategoryRelation = tagCategoryRelationService.getByProjectIdAndCustomTypeAndNdCodeAndStatus(GaeaContext.getAppId(), CustomType.AUXO_OPEN_COURSE.getValue()
                , importTag.getCode(), TagCategoryRelation.STATUS_ENABLE);
        if (tagCategoryRelation != null) {
            UUID tagId = tagCategoryRelation.getTagId();
            TagVo tagVo = getTagVo(tagId);
            if (tagVo != null) {
                if (!tagCategoryRelation.getNdPath().contains(importTag.getPath())) {
                    //更新标签来源
                    tagCategoryRelation.setNdPath(tagCategoryRelation.getNdPath() + "," + importTag.getPath());
                    tagCategoryRelation.setStatus(TagCategoryRelation.STATUS_ENABLE);
                    tagCategoryRelationService.saveRelation(tagCategoryRelation);
                }
                return tagId;
            } else {
                //子标签被删除,废弃掉关系
                tagCategoryRelation.setStatus(TagCategoryRelation.STATUS_DISABLE);
                tagCategoryRelationService.saveRelation(tagCategoryRelation);
            }
        }
        //1 查询父标签
        UUID parentTagId;
        TagCategoryRelation parentTag = tagCategoryRelationService.getByProjectIdAndCustomTypeAndNdCodeAndStatus(GaeaContext.getAppId(), CustomType.AUXO_OPEN_COURSE.getValue()
                , parentTagCode, TagCategoryRelation.STATUS_ENABLE);
        if (parentTag == null) {
            //如果没有创建
            parentTagId = getTagIdByCreate(rootId, parentName, parentTagCode, null);
        } else {
            TagVo tagVo = getTagVo(parentTag.getTagId());
            if (tagVo != null) {
                parentTagId = parentTag.getTagId();
            } else {
                //父标签被删除,废弃掉关系
                parentTag.setStatus(TagCategoryRelation.STATUS_DISABLE);
                tagCategoryRelationService.saveRelation(parentTag);
                //重新创建
                parentTagId = getTagIdByCreate(rootId, parentName, parentTagCode, null);
            }
        }
        //2 创建子标签
        return getTagIdByCreate(parentTagId, importTag.getName(), importTag.getCode(), importTag.getPath());
    }

    private TagVo getTagVo(UUID tagId) {
        try {
            return tagApi.get(tagId);
        } catch (Exception e) {
            LOGGER.debug("", e);
            log.trace(e.getMessage());
        }
        return null;
    }

    /**
     * 获取 创建的标签id
     *
     * @param parentId
     * @param tagName
     * @param tagCode
     * @param tagPath
     * @return
     * @throws Exception
     */
    private UUID getTagIdByCreate(UUID parentId, String tagName, String tagCode, String tagPath) throws Exception {
        String noRepeatTagName = tagName;
        //
        TagVo tagVo = new TagVo();
        tagVo.setParentId(parentId);
        tagVo.setCustomType(CustomType.AUXO_OPEN_COURSE.getValue());
        UUID tagId = null;
        for (int retry = 0; retry < 5; retry++) {
            try {
                tagVo.setTitle(noRepeatTagName);
                TagVo resultTagVo = tagApi.create(tagVo);
                tagId = resultTagVo.getId();
                break;
            } catch (Exception e) {
                if (retry < 4) {
                    log.warn("", e);
                    noRepeatTagName += "(1)";
                    continue;
                }
                throw new BusinessException("导入教材创建标签(" + tagName + ")失败，标签中存在" + tagName + "(1)此类的数据");
            }
        }

        TagCategoryRelation tagCategoryRelation = new TagCategoryRelation();
        tagCategoryRelation.setNdCode(tagCode);
        tagCategoryRelation.setNdPath(tagPath);
        tagCategoryRelation.setTagId(tagId);
        tagCategoryRelation.setCustomType(CustomType.AUXO_OPEN_COURSE.getValue());
        tagCategoryRelation.setCreateTime(new Date());
        tagCategoryRelation.setCreateUserId(GaeaContext.getTrueUserId());
        tagCategoryRelation.setStatus(TagCategoryRelation.STATUS_ENABLE);

        tagCategoryRelationService.saveRelation(tagCategoryRelation);
        return tagId;
    }


    /**
     * 下载上传封面
     *
     * @param fm
     * @return
     */
    private Integer dealFrontCover(String fm, String cloudToken) {
        SecureRandom secureRandom = new SecureRandom();
        fm = fm.replace("${ref-path}", csUrl + "/v0.1/static");
        String oldFileName = "photo_" + "_" + (new Date()).getTime() +
                ((int) (secureRandom.nextDouble() * 9 + 1) * 100000) + "_";
        String fileName = oldFileName + ".png";
        Integer storeObjectId = null;
        try {
            FileUploadDownUtils.getFile(fm, oldFileName);
            //k12的图片 转换为 101适应的图片尺寸
            ImageUtils.scale2(oldFileName, fileName, 400, 600, true);
            storeObjectId = FileUploadDownUtils.postFileToCloud(cloudUrl + "/v2/store/object/upload?cloud_token=" + URLEncoder.encode(cloudToken, "UTF-8")
                    + "&bucketName=$image", fileName);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            File file = new File(oldFileName);
            if (file.exists()) {
                file.delete();
            }
            file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }
        return storeObjectId;
    }

    /**
     * 处理章节和资源
     *
     * @param teachingId
     * @param mode
     * @param unitId
     * @param importResult
     * @param teachingMaterialImport
     * @param logMessage
     */
    private void handleChapterAndResource(UUID teachingId, Integer mode, Integer unitId, String cloudToken, ImportResult importResult, TeachingMaterialImport teachingMaterialImport, StringBuilder logMessage) {
        long startTime = System.currentTimeMillis();
        List<ChapterVo> list = teachingMaterialRepository.getSubItems(teachingId, null);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 获取根目录
        CloudCatalogTree cloudCatalogTree = recommendSdkCloudApi.getCatalogTree(unitId, cloudToken, false);
        //处理章节
        Map<UUID, UUID> chapterTwoLevelParentMap = new HashMap<>();//2级节下的章对应的2级章
        Map<UUID, Integer> chapterCatalogMap = handleChapter(list, teachingId, cloudCatalogTree.getId(), unitId, cloudToken, chapterTwoLevelParentMap);
        long endTime = System.currentTimeMillis();
        log.error("teachingId(" + teachingId + ") handleChapter  used  total time: " + (endTime - startTime) + "ms");
        startTime = System.currentTimeMillis();
        for (ChapterVo vo : list) {
            if (mode >= 3) {
                //需要导入资源
                if (chapterCatalogMap.containsKey(vo.getIdentifier())) {
                    if (chapterTwoLevelParentMap.containsKey(vo.getIdentifier())) {
                        //2级目录下的资源均挂到2级目录
                        handleResource(vo.getIdentifier(), unitId, chapterCatalogMap.get(chapterTwoLevelParentMap.get(vo.getIdentifier())), cloudToken, teachingId, importResult, mode, teachingMaterialImport, logMessage);
                    } else {
                        handleResource(vo.getIdentifier(), unitId, chapterCatalogMap.get(vo.getIdentifier()), cloudToken, teachingId, importResult, mode, teachingMaterialImport, logMessage);
                    }
                }
            }
        }
        //统计章节数
        importResult.setChapterCount(list.size());
        endTime = System.currentTimeMillis();
        log.error("teachingId(" + teachingId + ") handle Resource used  total time: " + (endTime - startTime) + "ms");
    }

    /**
     * 处理资源
     *
     * @param chapterVoList
     * @param teachingId
     * @param treeRootId
     * @param unitId
     * @param cloudToken
     * @param chapterTwoLevelParentMap
     * @return map<chapterId,catalogId>
     */
    private Map<UUID, Integer> handleChapter(List<ChapterVo> chapterVoList, UUID teachingId, Integer treeRootId, Integer unitId, String cloudToken, Map<UUID, UUID> chapterTwoLevelParentMap) {
        //1 chapterList-->titles list(tree)
        List<String> titles = new ArrayList<>();
        Map<UUID, String> chapterLevelMap = new HashMap<>();
        chapterLevelMap.put(teachingId, "");//章层无#，1层1个#,2层2个#，以此类推
        // 基础平台是用"#"来标识章节层级的，如果章节标题有“#”开头，这里会有坑
        Map<UUID, Map<String, String>> catalogTitleNoRepeatMap = new HashMap<>();
        String level;
        UUID parentId;
        UUID tempTwoLevelParent = null;//即节的目录id
        for (ChapterVo vo : chapterVoList) {
            parentId = UUID.fromString(vo.getParent());
            level = chapterLevelMap.get(parentId);
            titles.add(level + getNoRepeatCatalogName(vo.getTitle(), parentId, catalogTitleNoRepeatMap));
            chapterLevelMap.put(vo.getIdentifier(), level + "#");
            //
            if ("#".equals(level)) {
                //1级节目录
                tempTwoLevelParent = vo.getIdentifier();
            } else if (!"".equals(level)) {
                // 非章目录和1级节目录
                chapterTwoLevelParentMap.put(vo.getIdentifier(), tempTwoLevelParent);
            }
        }
        cloudApi.appendBatch(unitId, treeRootId, titles, cloudToken, false);
        //2 getCatalogTree --> catalogList
        CloudCatalogTree cloudCatalogTree = recommendSdkCloudApi.getCatalogTree(unitId, cloudToken, false);
        List<CloudCatalog> catalogList = new ArrayList<>();
        convertCatalogTreeToCatalogList(cloudCatalogTree, catalogList);
        //3 chapterVoList+catalogList --> map<chapterId-->catalogId>
        Map<UUID, Integer> chapterCatalogMap = new HashMap<>();
        for (int i = 1; i < catalogList.size(); i++) {
            //catalogList 会多一个根目录
            chapterCatalogMap.put(chapterVoList.get(i - 1).getIdentifier(), catalogList.get(i).getId());
        }
        return chapterCatalogMap;
    }

    /**
     * 将树 按深度优先 转化为 list,保持与NDR的章节list的结构一致
     *
     * @param cloudCatalogTree
     * @param catalogList
     * @return
     */
    private void convertCatalogTreeToCatalogList(CloudCatalogTree cloudCatalogTree, List<CloudCatalog> catalogList) {
        catalogList.add(CloudCatalog.convert(cloudCatalogTree));
        List<CloudCatalogTree> children = cloudCatalogTree.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        for (CloudCatalogTree child : children) {
            convertCatalogTreeToCatalogList(child, catalogList);
        }
    }


    /**
     * 处理章节
     *
     * @param chapterId
     * @param unitId
     * @param catalogId
     * @param cloudToken
     * @param teachingId
     * @param importResult
     * @param importMode
     * @param logMessage
     */
    private void handleResource(UUID chapterId, Integer unitId, Integer catalogId, String cloudToken, UUID teachingId, ImportResult importResult, Integer importMode, TeachingMaterialImport teachingMaterialImport, StringBuilder logMessage) {
        long startTime = System.currentTimeMillis();


        List<String> coverageList = new ArrayList<>();
        coverageList.add(CoverageDef.COVERAGE_ND + "/");
        //关联章节
        List<String> relationList = new ArrayList<>();
        relationList.add("chapters/" + chapterId + "/");

        // 循环
        Set<Integer> resourceTypes = teachingMaterialImport.getResourceTypes();
        List<NDRResource> ndrResourceList = new ArrayList<>();
        PageResultVo<ResourceVo> pageResultVo;
        for (Integer resourceType : resourceTypes) {
            switch (resourceType) {
                case 1:
                    //文档 视频 音频
                    List<String> categoryList = new ArrayList<>();
                    categoryList.add("$RA0101");
                    categoryList.add("$RA0102");
                    categoryList.add("$RA0103");
                    categoryList.add("$RA0108");
                    pageResultVo = resourceRepository.query(ResourceTypeDef.ASSETS, "", 0, Integer.MAX_VALUE, "create_time asc", coverageList, null, categoryList, relationList);
                    // 在章节上面添加NDR资源引用
                    convertResourceToCloud(pageResultVo, ndrResourceList, ResourceTypeDef.ASSETS);
                    break;
                case 2:
                    //课件
                    pageResultVo = resourceRepository.query("coursewares", "", 0, Integer.MAX_VALUE, null, coverageList, null, null, relationList);
                    convertResourceToCloud(pageResultVo, ndrResourceList, "coursewares");
                    break;
                default:
                    break;
            }
        }

          /* 基础平台不支持
            //教学目标
            pageResultVo = resourceRepository.query("instructionalobjectives", null, 0, Integer.MAX_VALUE, null, coverageList, null, null, relationList);
            convertResourceToCloud(pageResultVo, ndrResourceList,"instructionalobjectives");
            //教案
            pageResultVo = resourceRepository.query("lessonplans", null, 0, Integer.MAX_VALUE, null, coverageList, null, null, relationList);
            convertResourceToCloud(pageResultVo, ndrResourceList,"lessonplans");
            //电子教材
            pageResultVo = resourceRepository.query("ebooks", null, 0, Integer.MAX_VALUE, null, coverageList, null, null, relationList);
            convertResourceToCloud(pageResultVo, ndrResourceList,"ebooks");
            */


        //在章节上面添加NDR资源引用
        if (CollectionUtils.isEmpty(ndrResourceList)) {
            return;
        }
        CloudNDRCreate ndrCreate = new CloudNDRCreate();
        ndrCreate.setCatalogId(catalogId);
        ndrCreate.setUnitId(unitId);
        List<List<NDRResource>> lists = ListRequestHelper.subList(ndrResourceList);
        for (List<NDRResource> sub : lists) {
            // cha
            ndrCreate.setResources(sub);
            //添加日志 todo delete
            try {
                log.error("teachingId(" + teachingId + ") chapterId (" + chapterId + ") param_sub " + cloudToken + ", " + WafJsonMapper.toJson(ndrCreate));
            } catch (IOException e) {
                log.error("", e);
            }
            cloudApi.createBatchNDRResource(cloudToken, ndrCreate, false);
        }
        //统计资源数
        importResult.setResourceCount(importResult.getResourceCount() + ndrResourceList.size());

        long endTime = System.currentTimeMillis();
        log.error("teachingId(" + teachingId + ") chapterId (" + chapterId + ")handleResource used  total time: " + (endTime - startTime) + "ms");
    }

    /*
        转化为基础平台添加NDR资源时需要的参数
     */
    private void convertResourceToCloud(PageResultVo<ResourceVo> pageResultVo, List<NDRResource> ndrResourceList, String resType) {
        if (pageResultVo.getTotal() <= 0l) {
            return;
        }
        NDRResource ndrResource;
        for (ResourceVo vo : pageResultVo.getItems()) {
            ndrResource = new NDRResource();
            ndrResource.setTitle(vo.getTitle());
            ndrResource.setAlias(vo.getTitle());
            ndrResource.setIdentifier(vo.getIdentifier());
            ndrResource.setResType(resType);
            //去除lifeCycle
//            ndrResource.setLifeCycle(vo.getLifeCycle());
            ndrResourceList.add(ndrResource);
        }

    }

    /**
     * 获取无重复的目录名称（同级下基础平台不允许重复）
     *
     * @param oldName
     * @param level
     * @param catalogLevelMap
     * @return
     */
    private String getNoRepeatCatalogName(String oldName, UUID level, Map<UUID, Map<String, String>> catalogLevelMap) {
        String subName = oldName;
        if (oldName.length() >= 50) {
            subName = oldName.substring(0, 50);
        }

        if (!catalogLevelMap.containsKey(level)) {
            // 无这个等级的目录
            Map<String, String> titleMap = new HashMap<>();
            titleMap.put(subName, "");
            catalogLevelMap.put(level, titleMap);
            return subName;
        }
        Map<String, String> titleMap = catalogLevelMap.get(level);
        if (!titleMap.containsKey(subName)) {
            // 这个等级的目录下 无此名称
            titleMap.put(subName, "");
            return subName;
        }
        //匹配有subName(1),则变为subName(1+1),否则为subName(1)
        int index = 1;
        Set<String> titleSet = titleMap.keySet();
        Pattern pattern = Pattern.compile(
                "^" + RegexUtils.escapeExprSpecialWord(subName) + "\\((\\d+)\\)$"
        );
        Matcher m;
        int tempIndex;
        for (String title : titleSet) {
            m = pattern.matcher(title);
            if (m.find()) {
                tempIndex = Integer.valueOf(m.group(1));
                if (index <= tempIndex) {
                    index = tempIndex + 1;
                }
            }
        }
        String result = subName + "(" + index + ")";
        titleMap.put(result, "");
        return result;
    }


    /**
     * 根据元课程来更新 课程
     *
     * @param unitId
     * @param cloudToken
     * @param teachingId
     * @param courseId
     * @param customId
     * @param teachingMaterialImport
     */
    private UUID updateCourseByUnitId(Integer unitId, String cloudToken, UUID teachingId, UUID courseId, UUID customId, TeachingMaterialImport teachingMaterialImport) throws Exception {
        recommendSdkCloudApi.restCloudCatalogTree(unitId, cloudToken, false);
        //修改 标题简介 封面
        ResourceVo resourceVo = resourceRepository.getResource(ResourceTypeDef.TEACHINGMATERIALS, teachingId);
        Map<String, String> previewMap = resourceVo.getPreview();
        Integer fmId = null;
        if (previewMap != null && previewMap.containsKey("fm")) {
            fmId = dealFrontCover(previewMap.get("fm"), cloudToken);
        }
        //1 更新元课程
        UnitCourseCreate unitCourseUpdate = new UnitCourseCreate();
        unitCourseUpdate.setTitle(resourceVo.getTitle());
        unitCourseUpdate.setFrontCoverObjectId(fmId);
        unitCourseUpdate.setSummary(resourceVo.getDescription());
        cloudApi.update(unitCourseUpdate, cloudToken, unitId, false);
        // 重新同步下
        ResourceCourseSyncParam syncParam = new ResourceCourseSyncParam();
        syncParam.setSource(ResourceCourseSyncParam.SOURCE_CLOUD);
        syncParam.setSourceId(courseId);
        ResourceCourseVo syncResult = courseSdkApi.sync(syncParam);
        //公开课是否存在 不存在 重新创建，创建标签关系，更新关联表
        OpenCourseEditVo openCourseEditVo = null;
        try {
            openCourseEditVo = openCourseApi.get(customId);
        } catch (Exception e) {
            LOGGER.debug("", e);
            openCourseEditVo = null;
        }
        if (openCourseEditVo != null) {
            return openCourseEditVo.getId();
        }

        //3 从课程池创建公开课
        List<OpenCourse> batchCreateOpenCourseList = new ArrayList<>();
        OpenCourse openCourse = new OpenCourse();
        openCourse.setId(syncResult.getId());
        openCourse.setProjectId(syncResult.getProjectId());
        batchCreateOpenCourseList.add(openCourse);
        List<OpenCourseEditVo> openCourseEditVoList = openCourseApi.batchCreate(batchCreateOpenCourseList);
        //4  创建标签
        List<UUID> tagIds = handleTag(teachingMaterialImport);
        //5 创建公开课和标签关系
        if (CollectionUtils.isNotEmpty(tagIds)) {
            openCourseTagApi.updateTags(openCourseEditVoList.get(0).getId(), tagIds);
        }
        return openCourseEditVoList.get(0).getId();
    }

    /**
     * 基础平台 通知课程组件
     */
    private void noticeMq(String cloudToken, Integer unitId) {
        // 获取元课程
        CloudCatalogTree cloudCatalogTree = recommendSdkCloudApi.getCatalogTree(unitId, cloudToken, false);
        if (CollectionUtils.isEmpty(cloudCatalogTree.getChildren())) {
            return;
        }
        CloudCatalogTree first = cloudCatalogTree.getChildren().get(0);
        //1 更新第一个目录
        cloudApi.updateCatalog(unitId, cloudToken, first.getId(), first.getTitle(), true);
    }

    /**
     * 记录日志
     *
     * @param logMessage
     */
    private void addLogInfo(StringBuilder logMessage) {
        log.info(logMessage.toString());
    }
}
