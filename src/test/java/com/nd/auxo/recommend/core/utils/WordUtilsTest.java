package com.nd.auxo.recommend.core.utils;

import com.nd.auxo.recommend.core.util.WordUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class WordUtilsTest {

    @InjectMocks
    private WordUtils wordUtils;

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsOutOfMaxLength(){
        this.wordUtils.isOutOfMaxLength("abc", 10L);
    }

    @Test
    public void testEscapeHtmlWithImg(){
        this.wordUtils.escapeHtmlWithImg("abc");
    }

    @Test
    public void testEscapeHtml(){
        this.wordUtils.escapeHtml("abc");
    }

    @Test
    public void testUnEscapeHtml(){
        this.wordUtils.unEscapeHtml("abc");
    }

    @Test
    public void testRemoveHtml(){
        this.wordUtils.removeHtml("abc");
    }

    @Test
    public void testReplaceImageAndFormula(){
        this.wordUtils.replaceImageAndFormula("abc");
    }

    @Test
    public void testRemoveHtmlIgnoreImg(){
        this.wordUtils.removeHtmlIgnoreImg("abc");
    }

    @Test
    public void testValidCardFormat(){
        this.wordUtils.validIdCardFormat("abc");
    }

    @Test
    public void testValidPhone(){
        this.wordUtils.validPhone("abc");
    }

    @Test
    public void testValidEmail(){
        this.wordUtils.validEmail("abc");
    }

    @Test
    public void testValidAccount(){
        this.wordUtils.validAccount("abc");
    }

    @Test
    public void testValidIdCardFormat(){
        this.wordUtils.validIdCardFormat("abc");
    }

    @Test
    public void testGetIDCardStoreStr(){
        this.wordUtils.getIDCardStoreStr("abc");
    }

    @Test
    public void testGetWordCountByCode(){
        this.wordUtils.getWordCountByCode("abc","utf-8");
    }

    @Test
    public void testGetWordInLength(){
        this.wordUtils.getWordInLength("abc", 3);
    }
}
