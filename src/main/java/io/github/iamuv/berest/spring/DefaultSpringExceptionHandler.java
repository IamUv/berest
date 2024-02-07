package io.github.iamuv.berest.spring;

import io.github.iamuv.berest.core.exception.common.StandardException;
import io.github.iamuv.berest.core.BeRestChainContext;
import io.github.iamuv.berest.core.result.RestfulResult;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class DefaultSpringExceptionHandler implements InitializingBean {

    Logger logger = LoggerFactory.getLogger("BeRest.DefaultSpringExceptionHandler");

    protected BeRestChainContext beRestChainContext;

    @Autowired
    public void setRestfulChainContext(BeRestChainContext beRestChainContext) {
        this.beRestChainContext = beRestChainContext;
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            ServletRequestBindingException.class,
            HandlerMethodValidationException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            MaxUploadSizeExceededException.class})
    @Nullable
    public RestfulResult handleSpringErrorResponse(Exception exception, HttpServletResponse response) {
        logger.warn("Handle Spring ErrorResponse ", exception);
        return beRestChainContext.handleException(null, exception, null, response);
    }

    @ExceptionHandler({
            ConversionNotSupportedException.class,
            TypeMismatchException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleBeanException(BeansException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring BeanException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleNestedRuntimeException(NestedRuntimeException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring NestedRuntimeException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            MethodValidationException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleMethodValidationException(MethodValidationException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring MethodValidationException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            ConstraintViolationException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleConstraintViolationException(ConstraintViolationException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring ConstraintViolationException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            BindException.class})
    @Nullable
    public RestfulResult handleBindExceptionException(BindException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring BindException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            MethodNotAllowedException.class})
    @Nullable
    public RestfulResult handleMethodNotAllowedException(MethodNotAllowedException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring MethodNotAllowedException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring MethodArgumentTypeMismatchException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            BeansException.class})
    @Nullable
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestfulResult handleBeansException(BeansException throwable, HttpServletResponse response) {
        logger.warn("Handle Spring BeansException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            StandardException.class})
    @Nullable
    public RestfulResult handleStandardException(StandardException throwable, HttpServletResponse response) {
        logger.warn("Handle StandardException ", throwable);
        return beRestChainContext.handleException(null, throwable, null, response);
    }

    @ExceptionHandler({
            RuntimeException.class})
    @Nullable
    public RestfulResult handleRuntimeException(RuntimeException throwable, HttpServletResponse response) {
        logger.warn("Handle RuntimeException ", throwable);
        return beRestChainContext.handleException(null, throwable.getCause() != null ? throwable.getCause() : throwable, null, response);
    }

    @ExceptionHandler({
            Exception.class})
    public RestfulResult handleException(Exception throwable, HttpServletResponse response) {
        logger.warn("Handle Exception ", throwable);
        return beRestChainContext.handleInternalServerError(null, throwable.getCause() != null ? throwable.getCause() : throwable, null, response);
    }

    @ExceptionHandler({
            Error.class})
    public RestfulResult handleError(Error throwable, HttpServletResponse response) {
        logger.warn("Handle Error ", throwable);
        return beRestChainContext.handleInternalServerError(null, throwable.getCause() != null ? throwable.getCause() : throwable, null, response);
    }

    @ExceptionHandler({
            Throwable.class})
    public RestfulResult handleThrowable(Throwable throwable, HttpServletResponse response) {
        logger.warn("Handle Throwable ", throwable);
        return beRestChainContext.handleInternalServerError(null, throwable.getCause() != null ? throwable.getCause() : throwable, null, response);
    }

    @Override
    public void afterPropertiesSet() {
        if (beRestChainContext == null) {
            ClassNotFoundException exception = new ClassNotFoundException(BeRestChainContext.class.getName());
            logger.error("DefaultSpringExceptionHandler initialization failed", exception);
            throw new RuntimeException(exception);
        }
        logger.info("DefaultSpringExceptionHandler initialization completed: RestfulChainContext using " + beRestChainContext.getClass().getName());
    }

}
