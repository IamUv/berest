package io.github.iamuv.berest.core.handler;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;
import io.github.iamuv.berest.core.result.StandardRestfulResult;
import io.github.iamuv.berest.core.generator.ResultIdGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public class StandardContextChainHandler extends DefaultBeRestHandler<StandardRestfulResult> {
    @Override
    public StandardRestfulResult initializeResult(HttpServletRequest request, HttpServletResponse response, ResultConfigurer configuration, ResultIdGenerator resultIdGenerator) {
        StandardRestfulResult result = new StandardRestfulResult.Builder<>()
                .id(resultIdGenerator.generateId(System.currentTimeMillis(), request))
                .requestTime(LocalDateTime.now())
                .build();
        return result;
    }

    @Override
    public StandardRestfulResult handleReturnCharSequence(StandardRestfulResult result, String returnValue) {
        result.setData(returnValue);
        return result;
    }

    @Override
    public StandardRestfulResult handleReturnNonCharSequence(StandardRestfulResult result, Object returnValue) {
        result.setData(returnValue);
        return result;
    }
}
