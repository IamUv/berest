package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.SimpleExceptionHandler;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.method.MethodValidationException;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class SimpleMethodValidationExceptionHandler extends SimpleExceptionHandler<MethodValidationException> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, MethodValidationException throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(response.getStatus() >= 100 ? response.getStatus() : SC_BAD_REQUEST);
        result.setMessage(throwable.getLocalizedMessage());
        result.setData(throwable.getAllValidationResults());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
