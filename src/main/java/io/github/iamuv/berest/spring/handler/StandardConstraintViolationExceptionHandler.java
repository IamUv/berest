package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.StandardExceptionHandler;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class StandardConstraintViolationExceptionHandler extends StandardExceptionHandler<ConstraintViolationException> {

    @Override
    public StandardRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, ConstraintViolationException throwable, StandardRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(response.getStatus() >= 100 ? response.getStatus() : SC_BAD_REQUEST);
        result.setError(throwable.getLocalizedMessage(), getDescription(throwable), throwable.getConstraintViolations());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
