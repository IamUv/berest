package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.SimpleExceptionHandler;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.ErrorResponse;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class SimpleErrorResponseHandler extends SimpleExceptionHandler<ErrorResponse> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, ErrorResponse throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        if (getExceptionClass().isAssignableFrom(throwable.getClass())) {
            result.setHttpStatus(throwable.getStatusCode().value());
        } else {
            result.setHttpStatus(response.getStatus() >= 100 ? response.getStatus() : SC_INTERNAL_SERVER_ERROR);
        }
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

    @Override
    public Class getExceptionClass() {
        return ErrorResponse.class;
    }
}
