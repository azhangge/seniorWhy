package com.nd.auxo.recommend.web.config;

import com.nd.gaea.context.support.ApplicationSpringContextHolder;
import com.nd.gaea.waf.config.GaeaApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by Administrator on 16-3-23.
 */
@Configuration
@ComponentScan("com")
public class AuxoApplication extends GaeaApplication {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        ApplicationSpringContextHolder.getInstance().setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletContext));
    }
}
