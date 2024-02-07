package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.StandardExceptionHandler;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;

import java.lang.reflect.Method;

public class StandardBindExceptionHandler extends StandardExceptionHandler<BindException> {

    @Override
    public StandardRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, BindException throwable, StandardRestfulResult result, ResultConfigurer configuration) {
        if (throwable instanceof ErrorResponse) {
            result.setHttpStatus(((ErrorResponse) throwable).getStatusCode().value());
        } else {
            result.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        StringBuilder message = new StringBuilder();
        throwable.getBindingResult().getAllErrors().forEach(objectError -> {
            message.append(objectError.getDefaultMessage()).append(';');
        });
        result.setError(message.toString(), throwable.getLocalizedMessage(), throwable.getBindingResult().getFieldErrors());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
