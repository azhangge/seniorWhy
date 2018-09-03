package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.web.controller.v1.course.CategoryController;

import com.nd.elearning.sdk.ndr.bean.CategoryRelationVo;
import com.nd.elearning.sdk.ndr.bean.ListResultVo;
import com.nd.elearning.sdk.ndr.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by way on 2016/10/31.
 */
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryRepository categoryRepository;

    @Before
    public void doBefore(){
        MockitoAnnotations.initMocks(this);

        when(categoryRepository.find(anyString(),anyBoolean(),anyString())).thenReturn(new ListResultVo<CategoryRelationVo>());
    }

    @Test
    public void testFind(){
        this.categoryController.find();
    }

    @Test
    public void testListNotFromCache(){
        this.categoryController.listNotFromCache();
    }
}
