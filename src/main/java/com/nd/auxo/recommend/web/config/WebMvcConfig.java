package com.nd.auxo.recommend.web.config;

import com.nd.auxo.recommend.web.exception.AuxoErrorResolver;
import com.nd.auxo.recommend.web.exception.ValidationErrorHandler;
import com.nd.gaea.rest.config.WafWebMvcConfigurerAdapter;
import com.nd.gaea.rest.exceptions.WafErrorResolver;
import com.nd.gaea.rest.exceptions.rest.DefaultRestErrorHandler;
import com.nd.gaea.uranus.common.exception.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * <p>Web整体配置</p>
 * 配置所有在Web中使用到的配置
 * <p/>
 * 该配置中对象扫描时只要Controller，避免重复创建
 *
 * @author bifeng.liu
 */

@Configuration
@Import({JpaConfig.class})
public class WebMvcConfig extends WafWebMvcConfigurerAdapter {

    @Resource
    private EntityManager entityManager;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/*.ico").addResourceLocations("/");
        registry.addResourceHandler("/*.jsp").addResourceLocations("/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LocalDslQueryDataArgumentResolver(entityManager));
    }

    /**
     * 具体执行异常处理
     *
     * @return
     */
    @Bean
    public WafErrorResolver eduAttachErrorResolver() {
        AuxoErrorResolver resolver = new AuxoErrorResolver();
        //自定义异常
        resolver.addHandler(ArgumentValidationException.class, new DefaultRestErrorHandler(HttpStatus.BAD_REQUEST));
        resolver.addHandler(AlreadyUsedException.class, new DefaultRestErrorHandler(HttpStatus.BAD_REQUEST));
        resolver.addHandler(ObjectNotFoundException.class, new DefaultRestErrorHandler(HttpStatus.NOT_FOUND));
        resolver.addHandler(UnauthorizedException.class, new DefaultRestErrorHandler(HttpStatus.UNAUTHORIZED));
        resolver.addHandler(BusinessException.class, new DefaultRestErrorHandler(HttpStatus.INTERNAL_SERVER_ERROR));

        //参数校验异常
        resolver.addHandler(ConstraintViolationException.class, new ValidationErrorHandler(HttpStatus.BAD_REQUEST));
        resolver.addHandler(MethodArgumentNotValidException.class, new ValidationErrorHandler(HttpStatus.BAD_REQUEST));
        resolver.addHandler(BindException.class, new ValidationErrorHandler(HttpStatus.BAD_REQUEST));
        return resolver;
    }


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}
