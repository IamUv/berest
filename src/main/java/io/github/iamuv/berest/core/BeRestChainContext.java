package io.github.iamuv.berest.core;

import io.github.iamuv.berest.core.handler.SimpleContextChainHandler;
import io.github.iamuv.berest.core.cache.ResultCache;
import io.github.iamuv.berest.core.configuration.ExceptionHandlerConfigurer;
import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.generator.DefaultResultIdGenerator;
import io.github.iamuv.berest.core.generator.ResultIdGenerator;
import io.github.iamuv.berest.core.result.RestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class BeRestChainContext {

    private Logger logger = LoggerFactory.getLogger("BeRest.BeRestChainContext");

    public static final String HEADER_X_REQUEST_ID = "X-Request-Id";

    protected BeRestHandler chainHandler;

    protected ResultCache<RestfulResult> cache;

    protected ResultConfigurer resultConfigurer;

    protected ResultIdGenerator idGenerator;

    protected ExceptionHandlerConfigurer exceptionHandlerConfigurer;

    public BeRestChainContext() {
        this(null, null, null, null, null);
    }

    public BeRestChainContext(BeRestHandler handler, ResultCache<RestfulResult> cache, ResultConfigurer resultConfigurer, ResultIdGenerator idGenerator, ExceptionHandlerConfigurer exceptionHandlerConfigurer) {
        this.chainHandler = handler == null ? new SimpleContextChainHandler() : handler;
        this.cache = cache == null ? ResultCache.getInstance() : cache;
        this.resultConfigurer = resultConfigurer == null ? new ResultConfigurer() : resultConfigurer;
        this.idGenerator = idGenerator == null ? new DefaultResultIdGenerator() : idGenerator;
        this.exceptionHandlerConfigurer = exceptionHandlerConfigurer == null ? new ExceptionHandlerConfigurer() : exceptionHandlerConfigurer;
        logger.info("BeRest Chain Context initialization completed");
    }

    public BeRestHandler getChainHandler() {
        return chainHandler;
    }

    public ResultCache<RestfulResult> getCache() {
        return cache;
    }

    public ResultConfigurer getResultConfigurer() {
        return resultConfigurer;
    }

    public ResultIdGenerator getIdGenerator() {
        return idGenerator;
    }

    public ExceptionHandlerConfigurer getExceptionHandlerConfigurer() {
        return exceptionHandlerConfigurer;
    }

    public void requestReceived(HttpServletRequest request, HttpServletResponse response) {
        RestfulResult r = cache.add(
                chainHandler.initializeResult(request, response, resultConfigurer, idGenerator));
        response.setHeader(HEADER_X_REQUEST_ID, r.getId());
        logger.info("Received a new request, id is " + r.getId());
    }

    public RestfulResult handleResult(Object value, Method method, String httpMethod, RestfulResult result) {
        Class<?> clazz = method.getReturnType();
        boolean isVoid = false;
        if (clazz == void.class) {
            isVoid = true;
            logger.debug("Request " + result.getId() + " return type is void");
        }
        switch (httpMethod.toUpperCase()) {
            case "POST":
                result.setHttpStatus(isVoid ? resultConfigurer.getMethodPostReturnVoidHttpStatus() : resultConfigurer.getMethodPostDefaultHttpStatus());
                break;
            case "PUT":
                result.setHttpStatus(isVoid ? resultConfigurer.getMethodPutReturnVoidHttpStatus() : resultConfigurer.getMethodPutDefaultHttpStatus());
                break;
            case "DELETE":
                result.setHttpStatus(isVoid ? resultConfigurer.getMethodDeleteReturnVoidHttpStatus() : resultConfigurer.getMethodDeleteDefaultHttpStatus());
                break;
            default:
                result.setHttpStatus(isVoid ? resultConfigurer.getMethodGetReturnVoidHttpStatus() : SC_OK);
        }
        return chainHandler.handleResult(value, method, httpMethod, result, resultConfigurer);
    }

    public RestfulResult doRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RestfulResult result = getResult(response);
        String url = cache.getRedirectUrl(result.getId());
        logger.debug("Request " + result.getId() + " redirect to " + url);
        boolean clearBuffer = cache.isClearBuffer(result.getId());
        chainHandler.beforeRedirect(request, response, url, clearBuffer, result, resultConfigurer);
        response.sendRedirect(url, result.getHttpStatus(resultConfigurer), clearBuffer);
        return result;
    }

    public RestfulResult getResult(HttpServletResponse response) {
        return cache.getResult(response.getHeader(HEADER_X_REQUEST_ID));
    }

    public RestfulResult getResult(String requestId) {
        return cache.getResult(requestId);
    }

    public Map<String, Object> complete(RestfulResult result) {
        Map<String, Object> r = result.getResultMap(resultConfigurer);
        return r;
    }

    public Map<String, Object> complete(String requestId) {
        return complete(cache.getResult(requestId));
    }

    public boolean tryRemoveCache(String requestId) {
        return cache.remove(requestId) != null;
    }

    public boolean tryRemoveCache(HttpServletResponse response) {
        return tryRemoveCache(response.getHeader(HEADER_X_REQUEST_ID));
    }

    public void setRedirect(String requestId, String url, boolean clearBuffer, int code) {
        cache.getResult(requestId).setHttpStatus(code);
        cache.putRedirect(requestId, url, clearBuffer);
    }

    public boolean needRedirect(String requestId) {
        return cache.isRedirect(requestId);
    }

    public String getRedirectUrl(String requestId) {
        return cache.getRedirectUrl(requestId);
    }

    public boolean isClearBuffer(String requestId) {
        return cache.isClearBuffer(requestId);
    }

    public RestfulResult handleInternalServerError(@Nullable Method method, Throwable throwable, @Nullable HttpServletRequest request, HttpServletResponse response) {
        RestfulResult result = this
                .getExceptionHandlerConfigurer()
                .getDefaultHandler()
                .handleExceptionResult(request, response, method, throwable, getResult(response), resultConfigurer);
        logger.debug("Handle InternalServerError, response status is " + response.getStatus());
        logger.debug("Response status is " + response.getStatus() + ", and Result status is " + result.getHttpStatus(resultConfigurer));
        if (response.getStatus() <= 100) {
            response.setStatus(result.getHttpStatus(resultConfigurer));
        }
        logger.error("Request " + result.getId() + " return 500", throwable);
        return result;
    }

    public RestfulResult handleException(@Nullable Method method, Throwable throwable, @Nullable HttpServletRequest request, HttpServletResponse response) {
        BeRestExceptionHandler handler = this.getExceptionHandlerConfigurer().getHandler(throwable);
        if (handler == null) {
            return handleInternalServerError(method, throwable, request, response);
        }
        RestfulResult result = handler.handleExceptionResult(request, response, method, throwable, getResult(response), resultConfigurer);
        logger.debug("Handle InternalServerError, response status is " + response.getStatus());
        if (response.getStatus() > 0)
            response.setStatus(result.getHttpStatus(resultConfigurer));
        logger.warn("Request " + result.getId() + " return " + response.getStatus(), throwable);
        return result;
    }

}
