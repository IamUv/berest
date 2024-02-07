package io.github.iamuv.berest.core.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.generator.ResultIdGenerator;
import io.github.iamuv.berest.core.result.SimpleRestfulResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class SimpleContextChainHandler extends DefaultBeRestHandler<SimpleRestfulResult> {

    @Override
    public SimpleRestfulResult initializeResult(HttpServletRequest request, HttpServletResponse response, ResultConfigurer configuration, ResultIdGenerator resultIdGenerator) {
        return new SimpleRestfulResult(resultIdGenerator.generateId(System.currentTimeMillis(), request));
    }

    @Override
    public SimpleRestfulResult handleResult(Object methodReturnValue, Method method, String httpMethod, SimpleRestfulResult result, ResultConfigurer configuration) {
        return super.handleResult(methodReturnValue, method, httpMethod, result, configuration);
    }

    @Override
    public SimpleRestfulResult handleReturnCharSequence(SimpleRestfulResult result, String returnValue) {
        result.setMessage(returnValue.toString());
        return result;
    }

    @Override
    public SimpleRestfulResult handleReturnNonCharSequence(SimpleRestfulResult result, Object returnValue) {
        result.setData(returnValue);
        return result;
    }

}
