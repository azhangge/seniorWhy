package com.nd.auxo.recommend.web.controller.v1;

import com.nd.auxo.recommend.web.config.AuxoApplication;
import com.nd.auxo.recommend.web.config.WebMvcConfig;
import com.nd.auxo.recommend.web.controller.v1.MessageController;
import com.nd.auxo.sdk.interaction.message.vo.ChannelVo;
import com.nd.auxo.sdk.recommend.common.CustomType;
import com.nd.auxo.sdk.recommend.vo.message.*;
import com.nd.gaea.rest.security.authens.*;
import com.nd.gaea.waf.config.WafAutoConfiguration;
import com.nd.gaea.waf.security.gaea.GaeaContext;
import com.nd.gaea.waf.security.gaea.GaeaToken;
import com.nd.gaea.waf.security.gaea.GaeaUserAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author way
 *         Created on 2016/6/13.
 */
public class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    /*
     * 不会重复的,便于清除数据
     */
    private Long projectId;
    private CustomType customType = CustomType.JUST_TEST;
    private String accessToken = "E379FA2E010AD6629FFBC3147A5F6D6360B1EF1AD3159A99DD6ECC668FBD93CD0C84402523571974";
    private String userId = "2107168459";
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageControllerTest.class);

    @Before
    public void doBefore() {
        MockitoAnnotations.initMocks(this);
            }

    @Test
    public void testCreate() {
        //测评中心 create
        RecommendMessageVoForCreate examCreate = new RecommendMessageVoForCreate();
        ChannelVo channelVo = new ChannelVo();
        channelVo.setChannelId(2);
        channelVo.setChannelParam("{\"userName\":\"nd\",\"password\":\"b5bb1f820be6971626e1a8a9e8d446f178cc0ed5\",\"appId\":\"b6f5d1a314b4101a\"}");
        List<ChannelVo> channelVoList = new ArrayList<>();
        channelVoList.add(channelVo);
        examCreate.setChannels(channelVoList);

        examCreate.setContent("exam content");
        examCreate.setTitle("exam title");
        examCreate.setJumpType(JumpType.AUXO_EXAM_CENTER);
        examCreate.setStartTime(new Date(System.currentTimeMillis()+360000l));
        JumpParam jumpParam = new JumpParam();
        jumpParam.setId(" exam id");
        jumpParam.setProjectCode("onetest");
        examCreate.setJumpParam(jumpParam);

        examCreate.setReceiveObjectType(1);
        examCreate.setReceiveObject("113216@nd");
        examCreate.setCustomType(CustomType.JUST_TEST);
        try {
            RecommendMessageVo vo = this.messageController.createMessage(examCreate);

            //update
            RecommendMessageVoForUpdate update = new RecommendMessageVoForUpdate();
            update.setChannels(channelVoList);

            update.setContent("exam content");
            update.setTitle(" update test");
            update.setJumpType(JumpType.AUXO_EXAM_CENTER);
            update.setStartTime(new Date(System.currentTimeMillis() + 360000l));
            jumpParam.setId(" exam id");
            jumpParam.setProjectCode("onetest");
            update.setJumpParam(jumpParam);

            update.setReceiveObjectType(1);
            update.setReceiveObject("113216@nd");

            update.setCustomType(this.customType);
            this.messageController.updateMessage(vo.getId(), update);

            //get
            RecommendMessageVo voForGet = this.messageController.getMessage(vo.getId());

            this.messageController.deleteMessage(vo.getId());
        }catch (Exception e){
            LOGGER.debug("",e);
        }

    }


}
