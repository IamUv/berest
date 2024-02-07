package io.github.iamuv.berest.core.result;

import io.github.iamuv.berest.core.configuration.ResultConfigurer;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.github.iamuv.berest.core.result.RestfulResult.FILED.ID;
import static io.github.iamuv.berest.core.result.RestfulResult.FILED.STATUS;

public abstract class RestfulResult {

    public static class FILED {
        public static final String ID = "ID";
        public static final String STATUS = "status";

    }

    public RestfulResult(String id) {
        this.id = id;
    }

    protected String id;

    protected int httpStatus;

    public RestfulResult setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public RestfulResult setHttpStatus(int status) {
        this.httpStatus = status;
        return this;
    }

    public int getHttpStatus(ResultConfigurer configuration) {
        return configuration.convertHttpStatus(httpStatus);
    }

    public Map<String, Object> getResultMap(ResultConfigurer configuration) {
        Map<String, Object> result = new LinkedHashMap<>(9);
        if (configuration.isEnableRequestId())
            result.put(ID, getId());
        if (configuration.isEnableHttpStatus())
            result.put(STATUS, getHttpStatus(configuration));
        return buildReturnResult(configuration, result);
    }

    public abstract Map<String, Object> buildReturnResult(ResultConfigurer configuration, Map<String, Object> result);


}
