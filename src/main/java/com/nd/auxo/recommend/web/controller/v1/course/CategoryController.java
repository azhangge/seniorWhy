package com.nd.auxo.recommend.web.controller.v1.course;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nd.auxo.recommend.web.controller.v1.course.vo.CategoryRelationLevelVo;
import com.nd.elearning.sdk.ndr.bean.CategoryRelationVo;
import com.nd.elearning.sdk.ndr.bean.ListResultVo;
import com.nd.elearning.sdk.ndr.repository.CategoryRepository;
import com.nd.gaea.core.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by way on 2016/10/27.
 * <p/>
 * 分类数据
 * 明确K12下的适用对象： 小学（1-6年级），初中（7-9年级），高中学段。
 * 学科为：语文，数学，外语，物理，化学 版本：人教版，人教版A，人教版B，人教版一年级起点，人教版三年级起点 子版本：上册，下册，全一册，必修1，必修2，必修3，必修4，必修5
 */
@Api("分类数据")
@RestController
@RequestMapping("")
@Slf4j
public class CategoryController {

    private static final String CATEGORY_PATTERN_PATH = "K12";

    @Autowired
    private CategoryRepository categoryRepository;


    /*
   缓存
    */
    private static final Cache<String, ListResultVo<CategoryRelationLevelVo>> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(60, TimeUnit.SECONDS)//设置值后60秒
            .build();
    private static final String PREFIX_KEY = "web_recommend_category_cache_key";

    /**
     * @param
     * @return
     */
    @ApiOperation("查询分类数据")
    @RequestMapping(value = "/v1/recommends/categories", method = RequestMethod.GET)
    public ListResultVo<CategoryRelationLevelVo> find() {
        try {
            ListResultVo<CategoryRelationLevelVo> value = cache.get(PREFIX_KEY, new Callable<ListResultVo<CategoryRelationLevelVo>>() {
                @Override
                public ListResultVo<CategoryRelationLevelVo> call() {
                    log.debug(" not from cache ... ");
                    return listNotFromCache();
                }
            });
            return value;
        } catch (ExecutionException e) {
            log.error("get from cache fail", e);
            return new ListResultVo<>();
        }
    }

    public ListResultVo<CategoryRelationLevelVo> listNotFromCache() {
        ListResultVo<CategoryRelationVo> list = categoryRepository.find(CATEGORY_PATTERN_PATH, true, null);
        ListResultVo<CategoryRelationLevelVo> result = new ListResultVo<>();
        if (CollectionUtils.isEmpty(list.getItems())) {
            return result;
        }
        result.setItems(convert(list.getItems()));
        return result;
    }

    /**
     * 转化为voList
     *
     * @param list
     * @return
     */
    private List<CategoryRelationLevelVo> convert(List<CategoryRelationVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<CategoryRelationLevelVo> voList = new ArrayList<>();
        CategoryRelationLevelVo levelVo;
        for (CategoryRelationVo vo : list) {
            levelVo = CategoryRelationLevelVo.convert(vo);
            levelVo.setItems(convert(vo.getItems()));
            voList.add(levelVo);
        }
        return voList;
    }
}
