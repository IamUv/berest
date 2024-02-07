package io.github.iamuv.berest.core;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.RestfulResult;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface BeRestExceptionHandler<T, R extends RestfulResult> {

    Logger logger = LoggerFactory.getLogger(BeRestExceptionHandler.class);

    R handleExceptionResult(@Nullable HttpServletRequest request, HttpServletResponse response, @Nullable Method method, T throwable, R result, ResultConfigurer configuration);

    default Class getExceptionClass() {
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        if (genericInterfaces.length > 0) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Class<?> genericType = (Class<?>) typeArguments[0];
                return genericType.getClass();
            } else {
                String errorMsg = "The interface " + this.getClass().getName() + " does not define a generic";
                logger.warn(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
        } else {
            Type type = this.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
                if (typeArgs.length > 0) {
                    try {
                        return Class.forName(typeArgs[0].getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            String errorMsg = "The interface " + this.getClass().getName() + " is not a generic interface";
            logger.warn(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
    }

    default String tryGetFieldValue(T t, String filed) {
        Field[] fields = t.getClass().getDeclaredFields();
        if (fields.length == 0) {
            return null;
        }
        for (Field field : fields) {
            if (field.trySetAccessible()) {
                if (field.getName().equalsIgnoreCase(filed)) {
                    try {
                        return field.get(t).toString();
                    } catch (IllegalAccessException e) {
                        logger.warn("Try to get field " + field + " value of " + t.getClass().getName() + " failed", e);
                    }
                }
            }
        }
        return null;
    }

}
