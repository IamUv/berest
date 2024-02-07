package io.github.iamuv.berest.core.handler.exception;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class StandardInternalServerErrorHandler extends StandardExceptionHandler<Throwable> {

    @Override
    public StandardRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, Throwable throwable, StandardRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(response.getStatus() >= 100 ? response.getStatus() : SC_INTERNAL_SERVER_ERROR);
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

    @Override
    protected String getMessage(Throwable throwable) {
        String s = super.getMessage(throwable);
        if (s == null) {
            return "Internal Server Error.";
        } else {
            return s;
        }
    }

    @Override
    protected String getDescription(Throwable throwable) {
        String s = super.getDescription(throwable);
        if (s == null) {
            return "The server has encountered a situation it does not know how to handle.";
        } else {
            return s;
        }
    }
}
