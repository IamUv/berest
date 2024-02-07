package io.github.iamuv.berest.core;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.RestfulResult;
import io.github.iamuv.berest.core.generator.ResultIdGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public interface BeRestHandler<T extends RestfulResult> {

    T initializeResult(HttpServletRequest request, HttpServletResponse response, ResultConfigurer configuration, ResultIdGenerator resultIdGenerator);

    T handleResult(Object methodReturnValue, Method method, String httpMethod, T result, ResultConfigurer configuration);

    default T beforeRedirect(HttpServletRequest request, HttpServletResponse response, String url, boolean isClearBuffer, T result, ResultConfigurer configuration) {
        return result;
    }

}
