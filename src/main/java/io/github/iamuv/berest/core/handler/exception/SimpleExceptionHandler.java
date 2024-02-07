package io.github.iamuv.berest.core.handler.exception;

import io.github.iamuv.berest.core.BeRestExceptionHandler;
import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.exception.HttpStatusCode;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public abstract class SimpleExceptionHandler<T> implements BeRestExceptionHandler<T, SimpleRestfulResult> {

    @Override
    public SimpleRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, T throwable, SimpleRestfulResult result, ResultConfigurer configuration) {
        if (configuration.isDebug() && result.getData() == null)
            result.setData(throwable);

        if (!configuration.isDebug() && result.getData() == null)
            result.setData(throwable.getClass().getSimpleName());

        if (HttpStatusCode.class.isAssignableFrom(throwable.getClass()))
            result.setHttpStatus(((HttpStatusCode) throwable).getHttpStatusCode());
        else if (result.getHttpStatus(configuration) <= 100)
            result.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        if (result.getMessage() == null) {
            String message;
            if (throwable instanceof Throwable) {
                message = ((Throwable) throwable).getLocalizedMessage();
            } else {
                message = tryGetFieldValue(throwable, "message");
            }
            result.setMessage(message);
        }

        return result;
    }


}
