package io.github.iamuv.berest.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamuv.berest.core.BeRestChainContext;
import io.github.iamuv.berest.core.result.RestfulResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class DefaultSpringBodyAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    Logger logger = LoggerFactory.getLogger("BeRest.DefaultSpringBodyAdvice");

    protected BeRestChainContext beRestChainContext;

    protected ObjectMapper objectMapper;

    @Autowired
    public void setRestfulChainContext(BeRestChainContext beRestChainContext) {
        this.beRestChainContext = beRestChainContext;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String requestId = response.getHeaders().getFirst(BeRestChainContext.HEADER_X_REQUEST_ID);
        RestfulResult result = beRestChainContext.getResult(requestId);

        ResponseStatus responseStatus = returnType.getMethod().getAnnotation(ResponseStatus.class);
        Integer responseStatusCode = null;
        if (responseStatus != null) {
            responseStatusCode = responseStatus.code().value();
        }

        Class<?> clazz = returnType.getMethod().getReturnType();
        if (clazz == void.class) {
            result = beRestChainContext.getResult(requestId);
            result = beRestChainContext.handleResult(body, returnType.getMethod(), request.getMethod().name(), result, responseStatusCode);
            return beRestChainContext.complete(result);
        }

        if (body instanceof RestfulResult) {
            result = ((RestfulResult) body);
            return beRestChainContext.complete(result);
        }

        result = beRestChainContext.handleResult(body, returnType.getMethod(), request.getMethod().name(), result, responseStatusCode);

        if (CharSequence.class.isAssignableFrom(clazz) || clazz == char.class) {
            try {
                return objectMapper.writeValueAsString(beRestChainContext.complete(result));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return beRestChainContext.complete(result);
    }

    @Override
    public void afterPropertiesSet() {
        if (beRestChainContext == null) {
            ClassNotFoundException exception = new ClassNotFoundException(BeRestChainContext.class.getName());
            logger.error("DefaultSpringResponseBodyAdvice initialization failed", exception);
            throw new RuntimeException(exception);
        }
        if (objectMapper == null) {
            ClassNotFoundException exception = new ClassNotFoundException(ObjectMapper.class.getName());
            logger.error("DefaultSpringResponseBodyAdvice initialization failed", exception);
            throw new RuntimeException(exception);
        }
        logger.info("DefaultSpringResponseBodyAdvice initialization completed: " +
                "RestfulChainContext using " + beRestChainContext.getClass().getName() + ", " +
                "ObjectMapper using " + objectMapper.getClass().getName());
    }
}
