package io.github.iamuv.berest.core.handler.exception;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class SimpleInternalServerErrorHandler extends SimpleExceptionHandler<Throwable> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, Throwable throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        if (result.getMessage() == null)
            result.setMessage(throwable.getLocalizedMessage() == null ? "Internal Server Error." : throwable.getLocalizedMessage());

        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
