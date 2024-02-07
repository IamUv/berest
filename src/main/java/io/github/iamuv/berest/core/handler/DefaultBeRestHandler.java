package io.github.iamuv.berest.core.handler;

import io.github.iamuv.berest.annotation.ResultField;
import io.github.iamuv.berest.core.BeRestHandler;
import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.RestfulResult;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class DefaultBeRestHandler<T extends RestfulResult> implements BeRestHandler<T> {


    @Override
    public T handleResult(Object methodReturnValue, Method method, String httpMethod, T result, ResultConfigurer configuration) {
        Class<?> clazz = method.getReturnType();
        if (clazz == void.class) {
            return result;
        }

        if (CharSequence.class.isAssignableFrom(clazz) || clazz == char.class) {
            handleReturnCharSequence(result, methodReturnValue.toString());
        } else {
            handleReturnNonCharSequence(result, methodReturnValue);
        }
        ResultField resultField = method.getAnnotation(ResultField.class);
        if (resultField != null) {
            setField(result, resultField.value(), methodReturnValue);
        }

        Field[] fields = methodReturnValue.getClass().getFields();
        for (Field field : fields) {
            if (field.trySetAccessible()) {
                resultField = field.getAnnotation(ResultField.class);
                if (resultField != null) {
                    try {
                        setField(result, resultField.value(), field.get(methodReturnValue));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return result;
    }

    public abstract T handleReturnCharSequence(T result, String returnValue);

    public abstract T handleReturnNonCharSequence(T result, Object returnValue);

    private T setField(T result, String field, Object value) {
        try {
            Field filed = result.getClass().getDeclaredField(field);
            filed.setAccessible(true);
            filed.set(result, value);
            return result;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
