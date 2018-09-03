package com.nd.auxo.recommend.core.repository;
import com.nd.auxo.recommend.BasicLombokObjTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Created by auto on 2017-02-10.
 */

@RunWith(BlockJUnit4ClassRunner.class)
public class FAQManageTest extends BasicLombokObjTest {
    @Before
    public void init() throws InstantiationException, IllegalAccessException {
        super.init(new FAQManage());
    }
}
