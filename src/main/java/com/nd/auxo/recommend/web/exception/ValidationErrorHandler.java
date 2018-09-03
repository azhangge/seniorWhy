package com.nd.auxo.recommend.web.exception;

import com.nd.gaea.client.exception.ResponseErrorMessage;
import com.nd.gaea.rest.exceptions.rest.DefaultRestErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by wuhao on 14-10-15.
 */
public class ValidationErrorHandler extends DefaultRestErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidationErrorHandler.class);

    public ValidationErrorHandler(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public ValidationErrorHandler(HttpStatus httpStatus, String code) {
        super(httpStatus, code);
    }

    @Override
    protected ResponseErrorMessage getBody(Throwable throwable, HttpServletRequest request) {
        ResponseErrorMessage errorMessage = new ResponseErrorMessage(throwable);
        errorMessage.setMessage(getMessage(throwable));
        errorMessage.setDetail(appendStackTrace(null, throwable));
        errorMessage.setCode(getCode(throwable, request));
        updateRemoteErrorMessage(errorMessage, request);
        return errorMessage;
    }

    protected String getMessage(Throwable throwable) {
        if(ConstraintViolationException.class.isAssignableFrom(throwable.getClass())
                || MethodArgumentNotValidException.class.isAssignableFrom(throwable.getClass())
                || BindException.class.isAssignableFrom(throwable.getClass())){
            return handleValidationException(throwable);
        }else{
            return throwable.getLocalizedMessage();
        }
    }

    /**
     * 处理参数校验错误
     * @param throwable
     * @return
     * @throws java.io.IOException
     */
    protected String handleValidationException(Throwable throwable) {
        String errMsg = "";
        // Spring Validation
        if (ConstraintViolationException.class.isAssignableFrom(throwable.getClass())) {
            ConstraintViolationException exception = (ConstraintViolationException) throwable;
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            if (violations != null) {
                for (Iterator<ConstraintViolation<?>> iterator = violations.iterator(); iterator.hasNext(); ) {
                    ConstraintViolation<?> next = iterator.next();
                    errMsg += "参数（"+getParamterName(next)+"）无效："+next.getMessage()+"；";
                }
            }
        }
        // Spring Mvc Bind Validation
        else if (MethodArgumentNotValidException.class.isAssignableFrom(throwable.getClass())) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            errMsg += transformBindingResult(exception.getBindingResult());
        }
        // Spring Mvc Bind Validation
        else if (BindException.class.isAssignableFrom(throwable.getClass())) {
            BindException exception = (BindException) throwable;
            errMsg +=  transformBindingResult(exception);
        }
        return errMsg;
        //throw new ControllerException("WAF/INVALID_ARGUMENT",errMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * 转换BindingResult为验证的Result
     *
     * @param bindingResult
     * @return
     */
    private String transformBindingResult(BindingResult bindingResult) {
        String errMsg = "";
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);
            errMsg += "参数（"+fieldError.getField()+"）无效："+fieldError.getDefaultMessage()+"；";
        }
        return errMsg;
    }

    /**
     * 通过错误取得参数名称
     *
     * @param violation
     * @return 参数名称
     */
    private String getParamterName(ConstraintViolation<?> violation) {
        try {
            Path.MethodNode methodNode = null;
            Path.ParameterNode parameterNode = null;
            Path.PropertyNode propertyNode = null;
            Iterator<Path.Node> iterator = violation.getPropertyPath().iterator();
            while (iterator.hasNext()) {
                Path.Node node = iterator.next();
                if (ElementKind.METHOD.equals(node.getKind())) {
                    methodNode = (Path.MethodNode) node;
                } else if (ElementKind.PARAMETER.equals(node.getKind())) {
                    parameterNode = (Path.ParameterNode) node;
                } else if (ElementKind.PROPERTY.equals(node.getKind())) {
                    propertyNode = (Path.PropertyNode) node;
                }
            }
            if(propertyNode != null){
                return propertyNode.getName();
            }
            if(methodNode != null && parameterNode != null){
                Method method = violation.getRootBeanClass().getMethod(methodNode.getName(), methodNode.getParameterTypes().toArray(new Class[0]));
                LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                return parameterNames[parameterNode.getParameterIndex()];
            }
        } catch (NoSuchMethodException e) {
            log.warn("取得参数出错", e);
        }
        return null;
    }
}
