package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.SimpleExceptionHandler;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

public class SimpleMethodArgumentNotValidExceptionHandler extends SimpleExceptionHandler<MethodArgumentNotValidException> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, MethodArgumentNotValidException throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(throwable.getStatusCode().value());
        StringBuilder message = new StringBuilder();
        throwable.getBindingResult().getAllErrors().forEach(objectError -> {
            message.append(objectError.getDefaultMessage()).append(';');
        });
        result.setMessage(message.toString());
        result.setData(throwable.getBindingResult().getFieldErrors());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
