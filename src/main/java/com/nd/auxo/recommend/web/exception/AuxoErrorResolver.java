package com.nd.auxo.recommend.web.exception;

import com.nd.gaea.rest.exceptions.WafRestErrorResolver;
import com.nd.gaea.rest.exceptions.rest.AbstractRestErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luocl on 2015/10/28.
 */
public class AuxoErrorResolver extends WafRestErrorResolver {

    private static final Logger log = LoggerFactory.getLogger(AuxoErrorResolver.class);


    protected Map<Class, AbstractRestErrorHandler> handlerMap = new HashMap<>();

    public void addHandler(Class throwableClass, AbstractRestErrorHandler handler) {
        Assert.notNull(throwableClass);
        Assert.notNull(handler);

        this.handlerMap.put(throwableClass, handler);
    }

    public AbstractRestErrorHandler getHandler(Class throwableClass) {
        Assert.notNull(throwableClass);

        for (Class clazz = throwableClass; clazz != Throwable.class; clazz = clazz.getSuperclass()) {
            AbstractRestErrorHandler handler = this.handlerMap.get(clazz);
            if (handler != null)
                return handler;
        }
        return null;
    }

    @Override
    public boolean process(Throwable throwable, HttpServletRequest request, HttpServletResponse response) {
        if (getHandler(throwable.getClass()) == null) {
            return false;
        }
        log.error(throwable.getMessage(), throwable);
        return this.resolver(throwable, request, response);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
