package io.github.iamuv.berest.spring.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.handler.exception.StandardExceptionHandler;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

public class StandardMethodArgumentNotValidExceptionHandler extends StandardExceptionHandler<MethodArgumentNotValidException> {

    @Override
    public StandardRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, MethodArgumentNotValidException throwable, StandardRestfulResult result, ResultConfigurer configuration) {
        result.setHttpStatus(throwable.getStatusCode().value());
        StringBuilder message = new StringBuilder();
        throwable.getBindingResult().getAllErrors().forEach(objectError -> {
            message.append(objectError.getDefaultMessage()).append(';');
        });
        result.setError(message.toString(), throwable.getLocalizedMessage(), throwable.getBindingResult().getAllErrors());
        return super.handleExceptionResult(request, response, method, throwable, result, configuration);
    }

}
