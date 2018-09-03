package com.nd.auxo.recommend.core.utils;

import com.nd.auxo.recommend.core.util.ListRequestHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class ListRequestHelperTest {

    @InjectMocks
    private ListRequestHelper listRequestHelper;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubList(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        this.listRequestHelper.subList(list, 1);
    }

    @Test
    public void testSubArray(){
        Integer[] integers = new Integer[5];
        integers[0] = 2;
        this.listRequestHelper.subArray(integers, 1);
    }
}
