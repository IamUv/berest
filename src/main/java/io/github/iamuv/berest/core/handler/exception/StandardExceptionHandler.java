package io.github.iamuv.berest.core.handler.exception;

import io.github.iamuv.berest.core.BeRestExceptionHandler;
import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.exception.HttpStatusCode;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import io.github.iamuv.berest.core.exception.ErrorDescription;
import io.github.iamuv.berest.core.exception.StandardCode;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public abstract class StandardExceptionHandler<T> implements BeRestExceptionHandler<T, StandardRestfulResult> {

    @Override
    public StandardRestfulResult handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, T throwable, StandardRestfulResult result, ResultConfigurer configuration) {
        result.setSuccess(false);
        if (configuration.isDebug()) {
            result.setError(getMessage(throwable), getDescription(throwable), throwable);
        } else {
            result.setError(getMessage(throwable), getDescription(throwable), throwable.getClass().getSimpleName());
        }
        if (result.getCode() == null) {
            result.setCode(getCode(throwable));
        }
        if (HttpStatusCode.class.isAssignableFrom(throwable.getClass())) {
            result.setHttpStatus(((HttpStatusCode) throwable).getHttpStatusCode());
        } else if (result.getHttpStatus(configuration) <= 100) {
            result.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    protected String getMessage(T throwable) {
        if (Throwable.class.isAssignableFrom(throwable.getClass())) {
            return ((Throwable) throwable).getLocalizedMessage();
        }
        return tryGetFieldValue(throwable, "message");
    }

    protected String getDescription(T throwable) {
        if (ErrorDescription.class.isAssignableFrom(throwable.getClass())) {
            return ((ErrorDescription) throwable).getDescription();
        }
        return tryGetFieldValue(throwable, "description");
    }

    protected String getCode(T throwable) {
        if (StandardCode.class.isAssignableFrom(throwable.getClass())) {
            return ((StandardCode) throwable).getCode();
        }
        return tryGetFieldValue(throwable, "code");
    }

}
