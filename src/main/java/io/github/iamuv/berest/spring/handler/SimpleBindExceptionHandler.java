package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.SimpleExceptionHandler;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;

import java.lang.reflect.Method;

public class SimpleBindExceptionHandler extends SimpleExceptionHandler<BindException> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, BindException throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        StringBuilder message = new StringBuilder();
        throwable.getBindingResult().getAllErrors().forEach(objectError -> {
            message.append(objectError.getDefaultMessage()).append(';');
        });
        result.setMessage(message.toString());
        result.setData(throwable.getBindingResult().getFieldErrors());
        if (throwable instanceof ErrorResponse) {
            result.setHttpStatus(((ErrorResponse) throwable).getStatusCode().value());
        } else {
            result.setHttpStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
