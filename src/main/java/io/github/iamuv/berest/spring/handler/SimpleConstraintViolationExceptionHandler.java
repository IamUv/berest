package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.SimpleExceptionHandler;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class SimpleConstraintViolationExceptionHandler extends SimpleExceptionHandler<ConstraintViolationException> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, ConstraintViolationException throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(response.getStatus() >= 100 ? response.getStatus() : SC_BAD_REQUEST);
        result.setMessage(throwable.getLocalizedMessage());
        result.setData(throwable.getConstraintViolations());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
