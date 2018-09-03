package com.nd.auxo.recommend.core.utils;

import com.nd.auxo.recommend.core.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class DateUtilsTest {

    @InjectMocks
    private DateUtils dateUtils;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDayEndTime(){
        this.dateUtils.getDayEndTime(1474754640);
    }

    @Test
    public void testTimeDiff(){
        this.dateUtils.timeDiff(new Date(), new Date());
    }
}
