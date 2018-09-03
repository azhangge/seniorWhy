package com.nd.auxo.recommend.core.utils;

import com.nd.auxo.recommend.core.util.MD5Util;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class MD5UtilTest {

    @InjectMocks
    private MD5Util md5Util;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEncryptMD5_ND(){
        this.md5Util.encryptMD5_ND("abc");
    }

}
